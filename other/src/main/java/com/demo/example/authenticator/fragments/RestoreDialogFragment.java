package com.demo.example.authenticator.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;

import com.demo.example.R;

import com.demo.example.authenticator.adapters.BackupAdapter;
import com.demo.example.authenticator.inter.BackupActionListener;
import com.demo.example.authenticator.util.BackupUtils;
import java.io.File;
import java.io.IOException;


public class RestoreDialogFragment extends BaseAlertDialogFragment implements BackupActionListener {
    public static final String FRAGMENT_TAG = "fragment_restore";
    LinearLayout lin_cancel;
    private ArrayAdapter<String> mAdapter;
    private ListView mList;
    private TextView mTvNoBackups;

    @Override 
    public void onClick(DialogInterface dialogInterface, int i) {
    }

    public RestoreDialogFragment() {
        super(R.string.dialog_title_restore, R.layout.fragment_restore, 17039360, 0, 0);
    }

    public static RestoreDialogFragment createInstance() {
        return new RestoreDialogFragment();
    }

    @Override 
    public void onViewInflated(View view) {
        this.mList = (ListView) view.findViewById(R.id.lv_backups);
        this.mTvNoBackups = (TextView) view.findViewById(R.id.tv_no_backups);
        this.lin_cancel = (LinearLayout) view.findViewById(R.id.lin_cancel);
    }

    @Override 
    public void onStart() {
        File[] listBackups = new File[0];
        super.onStart();
        AlertDialog alertDialog = (AlertDialog) getDialog();
        if (alertDialog != null) {
            Button button = alertDialog.getButton(-2);
            button.setEnabled(false);
            button.setVisibility(View.GONE);
        }
        try {
            listBackups = BackupUtils.listBackups();
            this.mAdapter = new BackupAdapter(getActivity(), this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!(listBackups == null || listBackups.length == 0)) {
            for (File file : listBackups) {
                this.mAdapter.add(file.getName());
            }
            this.mList.setAdapter((ListAdapter) this.mAdapter);
            this.mList.setOnItemClickListener(new AdapterView.OnItemClickListener() { 
                @Override 
                public final void onItemClick(AdapterView adapterView, View view, int i, long j) {
                    RestoreDialogFragment.this.lambda$onStart$1$RestoreDialogFragment(adapterView, view, i, j);
                }
            });
            this.lin_cancel.setOnClickListener(new View.OnClickListener() { 
                @Override 
                public void onClick(View view) {
                    RestoreDialogFragment.this.dismiss();
                }
            });
            return;
        }
        this.mTvNoBackups.setVisibility(View.VISIBLE);
        this.mList.setVisibility(View.GONE);
    }

    public void lambda$onStart$1$RestoreDialogFragment(AdapterView adapterView, View view, int i, long j) {
        dismiss();
    }

    @Override 
    public void onResume() {
        super.onResume();
    }

    @Override 
    public void onBackupSelected(int i, String str) {
        if (getTargetFragment() != null) {
            ((MainFragment) getTargetFragment()).onRestoreSelected(str);
        }
        dismiss();
    }

    @Override 
    public void onDeleteBackupRequested(int i, String str) {
        confirmDelete(getContext(), i, str);
    }

    private void confirmDelete(Context context, int i, final String str) {
        new AlertDialog.Builder(context).setMessage(context.getString(R.string.dialog_text_delete_backup, str)).setPositiveButton("Delete", new DialogInterface.OnClickListener() { 
            @Override 
            public final void onClick(DialogInterface dialogInterface, int i2) {
                RestoreDialogFragment.this.lambda$confirmDelete$2$RestoreDialogFragment(str, dialogInterface, i2);
            }
        }).setNegativeButton(17039360, (DialogInterface.OnClickListener) null).show();
    }

    public void lambda$confirmDelete$2$RestoreDialogFragment(String str, DialogInterface dialogInterface, int i) {
        delete(str);
        this.mAdapter.remove(str);
        this.mAdapter.notifyDataSetChanged();
    }

    private void delete(String str) {
        BackupUtils.deleteBackup(str);
        if (!BackupUtils.hasBackups()) {
            dismiss();
        }
    }
}
