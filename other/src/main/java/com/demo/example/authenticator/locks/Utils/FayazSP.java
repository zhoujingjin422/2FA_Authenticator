package com.demo.example.authenticator.locks.Utils;

import android.content.Context;
import android.content.SharedPreferences;


public class FayazSP {
    public static SharedPreferences sharedPreferences;

    public static SharedPreferences init(Context context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences("Lockscreen", 0);
        }
        return sharedPreferences;
    }

    public static void put(String str, boolean z) {
        sharedPreferences.edit().putBoolean(str, z).apply();
    }

    public static void put(String str, float f) {
        sharedPreferences.edit().putFloat(str, f).apply();
    }

    public static void put(String str, int i) {
        sharedPreferences.edit().putInt(str, i).apply();
    }

    public static void put(String str, long j) {
        sharedPreferences.edit().putLong(str, j).apply();
    }

    public static void put(String str, String str2) {
        sharedPreferences.edit().putString(str, str2).apply();
    }

    public static boolean getBoolean(String str, boolean z) {
        return sharedPreferences.getBoolean(str, z);
    }

    public static float getFloat(String str, float f) {
        return sharedPreferences.getFloat(str, f);
    }

    public static int getInt(String str, int i) {
        return sharedPreferences.getInt(str, i);
    }

    public static long getLong(String str, long j) {
        return sharedPreferences.getLong(str, j);
    }

    public static String getString(String str, String str2) {
        return sharedPreferences.getString(str, str2);
    }

    public static void clearAll() {
        sharedPreferences.edit().clear().commit();
    }
}
