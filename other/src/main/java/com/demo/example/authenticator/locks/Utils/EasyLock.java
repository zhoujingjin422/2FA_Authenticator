package com.demo.example.authenticator.locks.Utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import com.demo.example.authenticator.locks.Activities.LockscreenActivity;
import com.demo.example.authenticator.locks.Interfaces.ActivityChanger;


public class EasyLock {
    private static ActivityChanger activityChanger;
    public static int backgroundColor = Color.parseColor("#019689");
    public static View.OnClickListener onClickListener;

    private static void init(Context context) {
        FayazSP.init(context);
        if (activityChanger == null) {
            activityChanger = new LockscreenActivity();
        }
    }

    public static void setPassword(Context context) {
        init(context);
        Intent intent = new Intent(context, LockscreenActivity.class);
        intent.putExtra("passStatus", "set");
        intent.addFlags(67108864);
        context.startActivity(intent);
    }

    public static void changePassword(Context context) {
        init(context);
        Intent intent = new Intent(context, LockscreenActivity.class);
        intent.putExtra("passStatus", "change");
        context.startActivity(intent);
    }

    public static void disablePassword(Context context) {
        init(context);
        Intent intent = new Intent(context, LockscreenActivity.class);
        intent.putExtra("passStatus", "disable");
        context.startActivity(intent);
    }

    public static void checkPassword(Context context) {
        init(context);
        if (FayazSP.getString("password", null) != null) {
            Intent intent = new Intent(context, LockscreenActivity.class);
            intent.putExtra("passStatus", "check");
            intent.addFlags(268435456);
            context.startActivity(intent);
        }
    }

    public static void setBackgroundColor(int i) {
        backgroundColor = i;
    }

    public static void forgotPassword(View.OnClickListener onClickListener2) {
        onClickListener = onClickListener2;
    }
}
