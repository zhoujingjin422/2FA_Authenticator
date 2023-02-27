package com.demo.example.authenticator.events;


public class PassphraseEvent {
    public static final int MODE_EXPORT = 1;
    public static final int MODE_IMPORT = 3;
    public static final int MODE_IMPORT_FROM_INTENT = 5;
    public static final int MODE_IMPORT_UNSAFE = 4;
    public static final int MODE_SHARE = 2;
    public static final int MODE_UNSET = 0;
    private final int mMode;
    private final String mPassphrase;

    
    public @interface Mode {
    }

    public PassphraseEvent(int i, String str) {
        this.mMode = i;
        this.mPassphrase = str;
    }

    public int getMode() {
        return this.mMode;
    }

    public String getPassphrase() {
        return this.mPassphrase;
    }
}
