package com.demo.example.authenticator.common;

import android.content.Context;
import android.content.SharedPreferences;


public class PrefManager {
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String PREF_NAME = "epic_prefname";
    private static final String REMOVE_ADS = "remove_ads";
    public static String REMOVE_ADS_PRODUCT_ID = "findmyheadset.remove.ads";
    int PRIVATE_MODE = 0;
    Context _context;
    SharedPreferences.Editor editor;
    SharedPreferences pref;

    public PrefManager(Context context) {
        this._context = context;
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, 0);
        this.pref = sharedPreferences;
        this.editor = sharedPreferences.edit();
    }

    public void setFirstTimeLaunch(boolean z) {
        this.editor.putBoolean(IS_FIRST_TIME_LAUNCH, z);
        this.editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return this.pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public void setvalue(boolean z) {
        this.editor.putBoolean(REMOVE_ADS, z);
        this.editor.commit();
    }

    public boolean getvalue() {
        return this.pref.getBoolean(REMOVE_ADS, false);
    }
}
