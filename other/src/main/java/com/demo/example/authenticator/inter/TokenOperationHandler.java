package com.demo.example.authenticator.inter;


public interface TokenOperationHandler {
    boolean canBackup();

    boolean isShowingHiddenTokens();

    void onBackupRestoreRequested();

    void onBackupRestoreUnencryptedRequested();

    void onToggleHiddenItemsRequested(Boolean bool);

    void onTokenBackupRequested();
}
