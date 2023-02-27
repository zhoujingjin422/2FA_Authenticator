package com.demo.example.authenticator.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.demo.example.R;

import com.demo.example.authenticator.AdAdmob;
import com.demo.example.authenticator.common.Epic_const;
import com.demo.example.authenticator.events.PassphraseEvent;
import com.demo.example.authenticator.util.CryptoUtil;


public class PassphraseDialogFragment extends BaseAlertDialogFragment {
    public static final String FRAGMENT_TAG = "fragment_passphrase";
    public static final int MINIMUM_PASSPHRASE_LENGTH = 10;
    LinearLayout lin_backup;
    private TextView mDontForget;
    private TextView mInstructions;
    private boolean mIsPassVisible;
    private TextView mLabelRandom;
    private EditText mPhrase;
    private LinearLayout mRandom;
    private ImageView mShow;
    TextView txt_btn;
    TextView txt_cancel;

    public PassphraseDialogFragment() {
        super(0, R.layout.fragment_dialog, 17039360, 0, R.string.bt_backup);
    }

    public static PassphraseDialogFragment createInstance(int i, String str) {
        PassphraseDialogFragment passphraseDialogFragment = new PassphraseDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("arg_message", str);
        bundle.putInt("arg_is_export", i);
        passphraseDialogFragment.setArguments(bundle);
        return passphraseDialogFragment;
    }

    @Override
    public void onViewInflated(View view) {


        AdAdmob adAdmob = new AdAdmob(getActivity());
        adAdmob.BannerAd((RelativeLayout) view.findViewById(R.id.bannerAd), getActivity());

        this.mInstructions = (TextView) view.findViewById(R.id.tv_instructions);
        this.mPhrase = (EditText) view.findViewById(R.id.et_passphrase);
        this.mShow = (ImageView) view.findViewById(R.id.bt_show_pass);
        this.mRandom = (LinearLayout) view.findViewById(R.id.bt_random_passphrase);
        this.mDontForget = (TextView) view.findViewById(R.id.tv_dont_forget);
        this.mLabelRandom = (TextView) view.findViewById(R.id.tv_random_passphrase);
        this.txt_cancel = (TextView) view.findViewById(R.id.txt_cancel);
        this.lin_backup = (LinearLayout) view.findViewById(R.id.lin_backup);
        this.txt_btn = (TextView) view.findViewById(R.id.txt_btn);
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        if (i == -1) {
            Epic_const.getBus().post(new PassphraseEvent(getMode(), this.mPhrase.getText().toString()));
        } else if (i == -2) {
            onCancel(dialogInterface);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        if (bundle != null) {
            this.mIsPassVisible = bundle.getBoolean("pass_visible", false);
            this.mPhrase.onRestoreInstanceState(bundle.getParcelable("pass"));
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        ((AlertDialog) getDialog()).setCancelable(false);
        AlertDialog alertDialog = (AlertDialog) getDialog();
        if (alertDialog != null) {
            Button button = alertDialog.getButton(-1);
            button.setEnabled(false);
            button.setVisibility(View.GONE);
            Button button2 = alertDialog.getButton(-2);
            button2.setEnabled(false);
            button2.setVisibility(View.GONE);
        }
        this.lin_backup.setEnabled(false);
        this.txt_btn.setText((getMode() == 3 || getMode() == 5 || getMode() == 4) ? R.string.bt_restore : R.string.bt_backup);
        showPassViewIcon();
        this.mInstructions.setText(getArguments().getString("arg_message"));
        this.mPhrase.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                boolean z = false;
                PassphraseDialogFragment.this.lin_backup.setEnabled(editable.length() >= 10);
                ImageView imageView = PassphraseDialogFragment.this.mShow;
                if (editable.length() > 0) {
                    z = true;
                }
                imageView.setEnabled(z);
            }
        });
        this.mShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public final void onClick(View view) {
                togglePasswordVisibility(!mIsPassVisible);
            }
        });
        if (getMode() == 3 || getMode() == 5 || getMode() == 4) {
            this.mRandom.setVisibility(View.GONE);
            this.mDontForget.setVisibility(View.GONE);
            this.mLabelRandom.setVisibility(View.GONE);
        }
        this.mRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public final void onClick(View view) {
                mPhrase.setText(CryptoUtil.getRandomPassphrase((Context) getContext()));
                mPhrase.setInputType(128);
                EditText editText = mPhrase;
                editText.setSelection(editText.getText().length());
                mIsPassVisible = true;
                showPassViewIcon();
            }
        });
        this.lin_backup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Epic_const.getBus().post(new PassphraseEvent(PassphraseDialogFragment.this.getMode(), PassphraseDialogFragment.this.mPhrase.getText().toString()));
                PassphraseDialogFragment.this.dismiss();
            }
        });
        this.txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PassphraseDialogFragment.this.dismiss();
            }
        });
    }


    private void togglePasswordVisibility(boolean z) {
        int selectionStart = this.mPhrase.getSelectionStart();
        int selectionEnd = this.mPhrase.getSelectionEnd();
        this.mIsPassVisible = z;
        if (z) {
            this.mPhrase.setInputType(144);
            if (Build.VERSION.SDK_INT >= 26) {
                this.mPhrase.setImeOptions(16777216);
            }
        } else {
            this.mPhrase.setInputType(129);
            this.mPhrase.setImeOptions(128);
        }
        this.mPhrase.setSelection(selectionStart, selectionEnd);
        showPassViewIcon();
    }

    private void showPassViewIcon() {
        Drawable wrap = DrawableCompat.wrap(ContextCompat.getDrawable(getActivity(), this.mIsPassVisible ? R.drawable.ic_hide_icon : R.drawable.ic_un_hide_icon));
        DrawableCompat.setTint(wrap, ContextCompat.getColor(getActivity(), R.color.colorAccent));
        this.mShow.setImageDrawable(wrap);
    }


    public int getMode() {
        return getArguments().getInt("arg_is_export");
    }

    @Override

    public void onCancel(DialogInterface dialogInterface) {
        super.onCancel(dialogInterface);
        if (getActivity() instanceof DialogInterface.OnDismissListener) {
            ((DialogInterface.OnDismissListener) getActivity()).onDismiss(dialogInterface);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putBoolean("pass_visible", this.mIsPassVisible);
        bundle.putParcelable("pass", this.mPhrase.onSaveInstanceState());
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
