package com.demo.example.authenticator.util.icon;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;

import com.demo.example.R;

import java.util.List;
import java.util.Locale;


public final class PackageUtil {
    private static PackageUtil sInstance;
    private final PackageManager mManager;
    private final List<PackageInfo> mPackages;
    private final String[] mWhitelist;

    private PackageUtil(Context context) {
        this.mWhitelist = context.getResources().getStringArray(R.array.package_whitelist);
        int i = 0;
        while (true) {
            String[] strArr = this.mWhitelist;
            if (i < strArr.length) {
                strArr[i] = strArr[i].toLowerCase(Locale.US);
                i++;
            } else {
                PackageManager packageManager = context.getPackageManager();
                this.mManager = packageManager;
                this.mPackages = packageManager.getInstalledPackages(0);
                return;
            }
        }
    }

    public static PackageUtil getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new PackageUtil(context);
        }
        return sInstance;
    }

    public Drawable findMatchingDrawable(String str) {
        String lowerCase = str.toLowerCase(Locale.US);
        String[] strArr = this.mWhitelist;
        for (String str2 : strArr) {
            if (lowerCase.contains(str2)) {
                return findDrawableFromAppName(str2);
            }
        }
        return null;
    }

    private Drawable findDrawableFromAppName(String str) {
        for (int i = 0; i < this.mPackages.size(); i++) {
            PackageInfo packageInfo = this.mPackages.get(i);
            if (packageInfo.applicationInfo.loadLabel(this.mManager).toString().toLowerCase(Locale.US).contains(str)) {
                if (Build.VERSION.SDK_INT >= 22) {
                    return packageInfo.applicationInfo.loadUnbadgedIcon(this.mManager);
                } else {
                    return packageInfo.applicationInfo.loadIcon(this.mManager);
                }
            }
        }
        for (int i2 = 0; i2 < this.mPackages.size(); i2++) {
            PackageInfo packageInfo2 = this.mPackages.get(i2);
            if (packageInfo2.packageName.toLowerCase(Locale.US).contains(str)) {
                if (Build.VERSION.SDK_INT >= 22) {
                    return packageInfo2.applicationInfo.loadUnbadgedIcon(this.mManager);
                } else {
                    return packageInfo2.applicationInfo.loadIcon(this.mManager);
                }
            }
        }
        return null;
    }
}
