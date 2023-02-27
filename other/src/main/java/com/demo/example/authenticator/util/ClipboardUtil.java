package com.demo.example.authenticator.util;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.widget.Toast;


public final class ClipboardUtil {
    public static void copy(Context context, String str, String str2) {
        ((ClipboardManager) context.getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText(str, str2));
        Toast makeText = Toast.makeText(context, "Token Copied.", 0);
        makeText.setGravity(17, 0, 0);
        makeText.show();
    }

    public static String paste(Context context) {
        return ((ClipboardManager) context.getSystemService("clipboard")).getPrimaryClip().getItemAt(0).getText().toString();
    }

    public static boolean hasData(Context context) {
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService("clipboard");
        if (clipboardManager.hasPrimaryClip()) {
            ClipDescription primaryClipDescription = clipboardManager.getPrimaryClipDescription();
            if (primaryClipDescription.hasMimeType("text/plain") || primaryClipDescription.hasMimeType("text/html")) {
                return true;
            }
        }
        return false;
    }

    private ClipboardUtil() {
    }
}
