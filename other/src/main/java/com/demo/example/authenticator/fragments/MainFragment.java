package com.demo.example.authenticator.fragments;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.example.authenticator.AccountActivity;
import com.demo.example.authenticator.BaseActivity;
import com.demo.example.authenticator.Constants;
import com.demo.example.authenticator.DisplayQrActivity;
import com.demo.example.authenticator.adapters.TokenAdapter;
import com.demo.example.authenticator.common.Epic_const;
import com.demo.example.authenticator.db.TokenProvider;
import com.demo.example.authenticator.events.PassphraseEvent;
import com.demo.example.authenticator.events.TokenEvent;
import com.demo.example.authenticator.inter.TokenModifyListener;
import com.demo.example.authenticator.inter.TokenOperationHandler;
import com.demo.example.authenticator.tokens.MalformedTokenException;
import com.demo.example.authenticator.tokens.Token;
import com.demo.example.authenticator.ui.DragDropItemTouchCallback;
import com.demo.example.authenticator.util.BackupException;
import com.demo.example.authenticator.util.BackupUtils;

import com.demo.example.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.squareup.otto.Subscribe;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import nl.qbusict.cupboard.CupboardFactory;



public class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, TokenModifyListener, DragDropItemTouchCallback.ReorderListener, TokenOperationHandler {
    private static final String TAG = MainFragment.class.getSimpleName();
    LinearLayout lin_empty;
    public ImageButton mBtClearSearch;
    private RecyclerView.AdapterDataObserver mDataSetObserver;
    private EditText mFilter;
    private String mImportContent;
    private String mImportFilename;
    private int mPermissionWasFor;
    private Token mQRToken;
    private RecyclerView mRecyclerView;
    private ViewGroup mRootView;
    private Boolean mShowHidden;
    public TokenAdapter mTokenAdapter;
    PermissionListener permissionListener;
    private boolean userAuthorized;

    @Override 
    public void onItemMoveStarted(int i) {
    }


    public static MainFragment createInstance(boolean z) {
        MainFragment mainFragment = new MainFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("arg_hidden", z);
        mainFragment.setArguments(bundle);
        return mainFragment;
    }

    @Override 
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Epic_const.getBus().register(this);
    }

    @Override 
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        ViewGroup viewGroup2 = (ViewGroup) layoutInflater.inflate(R.layout.fragment_main, viewGroup, false);
        this.mRootView = viewGroup2;
        this.mRecyclerView = (RecyclerView) viewGroup2.findViewById(R.id.rv_tokens);
        LinearLayout linearLayout = (LinearLayout) viewGroup2.findViewById(R.id.lin_empty);
        this.lin_empty = linearLayout;
        linearLayout.setOnClickListener(new View.OnClickListener() { 
            public final void onClick(View view) {
                MainFragment.this.lambda$onCreateView$0$MainFragment(view);
            }
        });
        this.mFilter = (EditText) this.mRootView.findViewById(R.id.et_filter);
        this.mBtClearSearch = (ImageButton) this.mRootView.findViewById(R.id.bt_clear_search);
        restoreState(bundle);
        fitGridColumns();
        new ItemTouchHelper(new DragDropItemTouchCallback(this)).attachToRecyclerView(this.mRecyclerView);
        this.mDataSetObserver = new RecyclerView.AdapterDataObserver() { 
            public void onChanged() {
                super.onChanged();
                if (MainFragment.this.mTokenAdapter.getCount() == 0) {
                    MainFragment.this.lin_empty.setVisibility(View.VISIBLE);
                } else {
                    MainFragment.this.lin_empty.setVisibility(View.GONE);
                }
            }
        };
        getLoaderManager().restartLoader(getLoaderId(), null, this);
        this.mBtClearSearch.setOnClickListener(new View.OnClickListener() { 
            public final void onClick(View view) {
                MainFragment.this.lambda$onCreateView$1$MainFragment(view);
            }
        });
        this.mFilter.addTextChangedListener(new TextWatcher() { 
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                MainFragment.this.mBtClearSearch.setVisibility((charSequence == null || charSequence.length() == 0) ? View.GONE : View.VISIBLE);
                MainFragment.this.getLoaderManager().restartLoader(MainFragment.this.getLoaderId(), null, MainFragment.this);
            }
        });
        this.permissionListener = new PermissionListener() { 
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                if (permissionGrantedResponse.getPermissionName().equals("android.permission.WRITE_EXTERNAL_STORAGE")) {
                    new Handler().postDelayed(new Runnable() { 
                        public final void run() {
                            MainFragment.this.lambda$onPermissionGranted$3$MainFragment();
                        }
                    }, 500);
                }
            }

            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                Toast makeText = Toast.makeText(MainFragment.this.getContext(), (int) R.string.error_permission_storage, Toast.LENGTH_SHORT);
                makeText.setGravity(17, 0, 0);
                makeText.show();
            }

            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                Toast makeText = Toast.makeText(MainFragment.this.getContext(), (int) R.string.error_permission_storage, Toast.LENGTH_SHORT);
                makeText.setGravity(17, 0, 0);
                makeText.show();
                permissionToken.continuePermissionRequest();
            }
        };
        return this.mRootView;
    }

    public void lambda$onCreateView$0$MainFragment(View view) {
        onEmptyPlaceholderClicked();
    }

    public void lambda$onCreateView$1$MainFragment(View view) {
        this.mFilter.setText("");
    }

    private Activity getNonNullActivity() {
        FragmentActivity activity = getActivity();
        activity.getClass();
        return activity;
    }

    private void restoreState(Bundle bundle) {
        if (bundle != null) {
            if (this.mShowHidden == null) {
                this.mShowHidden = Boolean.valueOf(bundle.getBoolean("arg_hidden"));
            }
            if (this.mPermissionWasFor == 0) {
                this.mPermissionWasFor = bundle.getInt("perm_for", 0);
            }
        }
    }

    @Override 
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        restoreState(bundle);
        AccountActivity accountActivity = (AccountActivity) getActivity();
        if (accountActivity != null) {
            accountActivity.setTokenOperationHandler(this);
        }
        this.userAuthorized = false;
    }

    @Override 
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        fitGridColumns();
    }

    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        Boolean bool = this.mShowHidden;
        bundle.putBoolean("arg_hidden", bool != null && bool.booleanValue());
        bundle.putInt("perm_for", this.mPermissionWasFor);
    }

    private void fitGridColumns() {
        this.mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
    }

    private void onEmptyPlaceholderClicked() {
        AccountActivity accountActivity = (AccountActivity) getActivity();
        if (accountActivity != null) {
            accountActivity.openAccountActivity();
        }
    }

    @Override 
    public boolean canBackup() {
        TokenAdapter tokenAdapter = this.mTokenAdapter;
        return tokenAdapter != null && tokenAdapter.getCount() > 0;
    }

    @Override 
    public boolean isShowingHiddenTokens() {
        Boolean bool = this.mShowHidden;
        return bool != null && bool.booleanValue();
    }

    @Override 
    public void onBackupRestoreRequested() {
        if (!Dexter.isRequestOngoing()) {
            this.mPermissionWasFor = 3;
            Dexter.checkPermission(this.permissionListener, "android.permission.WRITE_EXTERNAL_STORAGE");
        }
    }

    @Override 
    public void onBackupRestoreUnencryptedRequested() {
        if (!Dexter.isRequestOngoing()) {
            this.mPermissionWasFor = 4;
            Dexter.checkPermission(this.permissionListener, "android.permission.WRITE_EXTERNAL_STORAGE");
        }
    }

    public void onToggleHiddenItemsRequested(Boolean bool) {
        if (bool.booleanValue()) {
            toggleHidden(true);
        } else {
            toggleHidden(false);
        }
    }

    public void onTokenBackupRequested() {
        if (!Dexter.isRequestOngoing()) {
            this.mPermissionWasFor = 1;
            Dexter.checkPermission(this.permissionListener, "android.permission.WRITE_EXTERNAL_STORAGE");
        }
    }

    private List<Token> getAllTokensFromDb() {
        return CupboardFactory.cupboard().withCursor(CupboardFactory.cupboard().withContext(getNonNullActivity()).query(TokenProvider.TOKEN_URI, Token.class).getCursor()).list(Token.class);
    }

    @Subscribe
    public void onTokenUriReceived(TokenEvent tokenEvent) {
        if (getActivity() != null && isAdded()) {
            try {
                Token token = new Token(tokenEvent.uri);
                List<Token> allTokensFromDb = getAllTokensFromDb();
                if (allTokensFromDb.contains(token)) {
                    scrollToToken(token, allTokensFromDb);
                    Toast makeText = Toast.makeText(getContext(), (int) R.string.toast_duplicate, Toast.LENGTH_LONG);
                    makeText.setGravity(17, 0, 0);
                    makeText.show();
                    return;
                }
                int i = getPrefs().getInt(Constants.PREF_KEY_TOKEN_COUNT, 0);
                if (tokenEvent.id > -1) {
                    token.setDatabaseId(tokenEvent.id);
                    CupboardFactory.cupboard().withContext(getActivity()).put(TokenProvider.TOKEN_URI, token);
                } else {
                    token.setSortOrder(i);
                    getPrefs().edit().putInt(Constants.PREF_KEY_TOKEN_COUNT, i + 1).apply();
                    CupboardFactory.cupboard().withContext(getActivity()).put(TokenProvider.TOKEN_URI, token);
                }
                if (this.mTokenAdapter != null) {
                    this.mTokenAdapter.notifyDataSetChanged();
                }
                Toast makeText2 = Toast.makeText(getContext(), (int) R.string.token_added, Toast.LENGTH_LONG);
                makeText2.setGravity(17, 0, 0);
                makeText2.show();
            } catch (MalformedTokenException e) {
                e.printStackTrace();
                Toast makeText3 = Toast.makeText(getContext(), (int) R.string.invalid_token, Toast.LENGTH_LONG);
                makeText3.setGravity(17, 0, 0);
                makeText3.show();
            }
        }
    }

    @Subscribe
    public void onPassphraseEntered(PassphraseEvent passphraseEvent) {
        int mode = passphraseEvent.getMode();
        if (mode == 0) {
            Log.w(TAG, "ERROR: permissionWasFor is unset");
        } else if (mode == 1) {
            try {
                if (getActivity() != null) {
                    showShareBackupSnackbar(BackupUtils.backupTokens(passphraseEvent.getPassphrase(), CupboardFactory.cupboard().withContext(getActivity()).query(TokenProvider.TOKEN_URI, Token.class).list()));
                }
            } catch (BackupException e) {
                e.printStackTrace();
                Toast makeText = Toast.makeText(getContext(), (int) R.string.error_backup_failed, Toast.LENGTH_SHORT);
                makeText.setGravity(17, 0, 0);
                makeText.show();
            }
        } else if (mode == 2) {
            createShareableBackup(passphraseEvent.getPassphrase());
        } else if (mode == 3) {
            startImport(passphraseEvent.getPassphrase());
        }
    }

    private void showShareBackupSnackbar(BackupUtils.Pair<File, String> pair) {
        if (getActivity() != null && isAdded()) {
            Toast.makeText(getContext(), (int) R.string.toast_backup_done, 1).show();
        }
    }

    private void createShareableBackup(String str) {
        try {
            shareBackupContent(BackupUtils.getBackupContent(str, CupboardFactory.cupboard().withCursor(this.mTokenAdapter.getCursor()).list(Token.class)));
        } catch (BackupException e) {
            e.printStackTrace();
            if (getActivity() != null && isAdded()) {
                Toast makeText = Toast.makeText(getContext(), (int) R.string.error_backup_failed, Toast.LENGTH_SHORT);
                makeText.setGravity(17, 0, 0);
                makeText.show();
            }
        }
    }

    private void shareBackupContent(String str) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.SEND");
        intent.setType("text/plain");
        intent.putExtra("android.intent.extra.TITLE", getString(R.string.share_content_title));
        intent.putExtra("android.intent.extra.TEXT", str);
        startActivity(intent);
    }

    private void scrollToToken(Token token, List<Token> list) {
        int indexOf = list.indexOf(token);
        boolean isHidden = list.get(indexOf).isHidden();
        Boolean bool = this.mShowHidden;
        if (bool != null && bool.booleanValue()) {
            this.mRecyclerView.scrollToPosition(indexOf);
        } else if (isHidden) {
            toggleHidden(true);
        } else {
            Cursor cursor = this.mTokenAdapter.getCursor();
            cursor.moveToFirst();
            int indexOf2 = CupboardFactory.cupboard().withCursor(cursor).list(Token.class).indexOf(token);
            if (indexOf2 >= 0) {
                this.mRecyclerView.scrollToPosition(indexOf2);
            }
        }
    }

    private SharedPreferences getPrefs() {
        return ((BaseActivity) getNonNullActivity()).getPrefs();
    }

    @Override 
    public void onDestroy() {
        super.onDestroy();
        Epic_const.getBus().unregister(this);
        TokenAdapter tokenAdapter = this.mTokenAdapter;
        if (tokenAdapter != null) {
            tokenAdapter.unregisterAdapterDataObserver(this.mDataSetObserver);
        }
    }

    private void toggleHidden(boolean z) {
        this.mShowHidden = Boolean.valueOf(z);
        getLoaderManager().restartLoader(getLoaderId(), null, this);
    }

    @Override 
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String str;
        String obj = this.mFilter.getText().toString();
        String[] strArr = null;
        String str2 = i != 1 ? "hidden=0" : null;
        if (!obj.isEmpty()) {
            if (str2 == null) {
                str = "";
            } else {
                str = str2 + " AND ";
            }
            String str3 = "%" + obj + "%";
            str2 = str + "(label LIKE ? OR issuer_ext LIKE ?)";
            strArr = new String[]{str3, str3};
        }
        return new CursorLoader(getNonNullActivity(), TokenProvider.TOKEN_URI, null, str2, strArr, "sortOrder ASC");
    }

    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        TokenAdapter tokenAdapter = this.mTokenAdapter;
        if (tokenAdapter == null) {
            TokenAdapter tokenAdapter2 = new TokenAdapter(getActivity(), this, cursor);
            this.mTokenAdapter = tokenAdapter2;
            this.mRecyclerView.setAdapter(tokenAdapter2);
            this.mTokenAdapter.registerAdapterDataObserver(this.mDataSetObserver);
            this.mDataSetObserver.onChanged();
            return;
        }
        tokenAdapter.swapCursor(cursor);
        this.mTokenAdapter.notifyDataSetChanged();
    }

    public void onLoaderReset(Loader<Cursor> loader) {
        if (this.mTokenAdapter != null && getActivity() != null && isAdded()) {
            this.mTokenAdapter.swapCursor(null);
            getActivity().invalidateOptionsMenu();
        }
    }

    public void onTokenSaveRequested(Token token) {
        if (getActivity() != null) {
            CupboardFactory.cupboard().withContext(getActivity()).put(TokenProvider.TOKEN_URI, token);
        }
    }

    public void onTokenDeleteRequested(Token token) {
        if (getActivity() != null) {
            CupboardFactory.cupboard().withContext(getActivity()).delete(TokenProvider.TOKEN_URI, token);
        }
    }

    public void onTokenShareRequested(Token token) {
        KeyguardManager keyguardManager;
        Intent createConfirmDeviceCredentialIntent;
        this.mQRToken = token;
        if (Build.VERSION.SDK_INT < 21 || this.userAuthorized || getActivity() == null || !isAdded() || (keyguardManager = (KeyguardManager) getActivity().getSystemService("keyguard")) == null || (createConfirmDeviceCredentialIntent = keyguardManager.createConfirmDeviceCredentialIntent(getString(R.string.app_name), getString(R.string.dialog_keyguard_share))) == null) {
            showQRCode(token);
        } else {
            startActivityForResult(createConfirmDeviceCredentialIntent, 285);
        }
    }

    @Override 
    public void onItemMove(int i, int i2) {
        if (i != i2) {
            this.mTokenAdapter.reorderItem(i, i2);
        }
    }

    @Override 
    public void onItemMoveComplete(int i, int i2) {
        if (i != i2) {
            getPrefs().edit().putInt(Constants.PREF_KEY_TOKEN_COUNT, this.mTokenAdapter.saveNewSortOrder() + 1).apply();
        }
    }

    @Override 
    public void onItemDismiss(int i) {
        Token item = this.mTokenAdapter.getItem(i);
        if (item != null) {
            this.mTokenAdapter.setHiddenState(item, i, true);
        }
    }

    public int getLoaderId() {
        Boolean bool = this.mShowHidden;
        return (bool == null || !bool.booleanValue()) ? 0 : 1;
    }

    private void showQRCode(Token token) {
        if (getActivity() != null && isAdded()) {
            Intent intent = new Intent(getContext(), DisplayQrActivity.class);
            intent.putExtra("qr_token", token);
            getContext().startActivity(intent);
        }
    }

    @Override 
    public void onActivityResult(int i, int i2, Intent intent) {
        if (i2 != -1) {
            return;
        }
        if (i == 284) {
            this.userAuthorized = true;
            showPassphraseDialog(this.mPermissionWasFor);
        } else if (i == 285) {
            this.userAuthorized = true;
            showQRCode(this.mQRToken);
        }
    }

    private View getSheet() {
        return this.mRootView.findViewById(R.id.nsv_list);
    }

    public void lambda$onPermissionGranted$3$MainFragment() {
        KeyguardManager keyguardManager;
        Intent createConfirmDeviceCredentialIntent;
        int i = this.mPermissionWasFor;
        if (i != 1) {
            if (i == 3 || i == 4) {
                showRestoreDialog();
            }
        } else if (Build.VERSION.SDK_INT < 21 || this.userAuthorized || getActivity() == null || !isAdded() || (keyguardManager = (KeyguardManager) getActivity().getSystemService("keyguard")) == null || (createConfirmDeviceCredentialIntent = keyguardManager.createConfirmDeviceCredentialIntent(getString(R.string.app_name), getString(R.string.dialog_keyguard_export))) == null) {
            showPassphraseDialog(this.mPermissionWasFor);
        } else {
            startActivityForResult(createConfirmDeviceCredentialIntent, 284);
        }
    }

    private void showRestoreDialog() {
        if (getFragmentManager() != null && isAdded()) {
            RestoreDialogFragment createInstance = RestoreDialogFragment.createInstance();
            createInstance.setTargetFragment(this, 1);
            createInstance.show(getFragmentManager(), RestoreDialogFragment.FRAGMENT_TAG);
        }
    }

    private void showPassphraseDialog(int i) {
        String str;
        if (i == 3) {
            str = getString(R.string.tv_passphrase_import);
        } else {
            str = getString(R.string.tv_passphrase_export, 10);
        }
        PassphraseDialogFragment createInstance = PassphraseDialogFragment.createInstance(i, str);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.getClass();
        createInstance.show(fragmentManager, PassphraseDialogFragment.FRAGMENT_TAG);
    }

    public void onRestoreContent(String str) {
        this.mImportFilename = null;
        this.mImportContent = str;
        showPassphraseDialog(3);
    }

    public void onRestoreSelected(String str) {
        this.mImportFilename = str;
        this.mImportContent = null;
        if (this.mPermissionWasFor == 4) {
            startImport(null);
        } else {
            showPassphraseDialog(3);
        }
    }

    private void startImport(String str) {
        BackupUtils.Pair<ArrayList<Token>, ArrayList<Token>> pair;
        String str2;
        if (getActivity() != null && isAdded()) {
            try {
                List<Token> allTokensFromDb = getAllTokensFromDb();
                if (this.mImportFilename != null) {
                    pair = BackupUtils.importTokensFromFile(this.mImportFilename, str, allTokensFromDb);
                } else {
                    pair = BackupUtils.importTokensFromContent(this.mImportContent, str, allTokensFromDb);
                }
                int i = getPrefs().getInt(Constants.PREF_KEY_TOKEN_COUNT, 0);
                Iterator<Token> it = pair.first.iterator();
                while (it.hasNext()) {
                    it.next().setSortOrder(i);
                    i++;
                }
                getPrefs().edit().putInt(Constants.PREF_KEY_TOKEN_COUNT, i).apply();
                CupboardFactory.cupboard().withContext(getActivity()).put(TokenProvider.TOKEN_URI, Token.class, pair.first);
                getLoaderManager().restartLoader(getLoaderId(), null, this);
                int size = pair.first.size();
                int size2 = pair.second.size();
                if (size == 0) {
                    Toast makeText = Toast.makeText(getContext(), (int) R.string.error_no_new_tokens, Toast.LENGTH_SHORT);
                    makeText.setGravity(17, 0, 0);
                    makeText.show();
                    return;
                }
                if (size2 == 0) {
                    str2 = getString(R.string.toast_import_done, getResources().getQuantityString(R.plurals.tokens_imported, size, Integer.valueOf(size)));
                } else {
                    str2 = getString(R.string.toast_import_done_dupes, getResources().getQuantityString(R.plurals.tokens_imported, size, Integer.valueOf(size)), getResources().getQuantityString(R.plurals.duplicates_skipped, size2, Integer.valueOf(size2)));
                }
                Toast makeText2 = Toast.makeText(getContext(), str2, Toast.LENGTH_SHORT);
                makeText2.setGravity(17, 0, 0);
                makeText2.show();
            } catch (BackupException e) {
                e.printStackTrace();
                Toast makeText3 = Toast.makeText(getContext(), (int) R.string.error_import_fail, Toast.LENGTH_SHORT);
                makeText3.setGravity(17, 0, 0);
                makeText3.show();
            }
        }
    }
}
