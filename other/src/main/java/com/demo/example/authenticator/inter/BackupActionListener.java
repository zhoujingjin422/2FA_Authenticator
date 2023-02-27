package com.demo.example.authenticator.inter;


public interface BackupActionListener {
    void onBackupSelected(int i, String str);

    void onDeleteBackupRequested(int i, String str);
}
