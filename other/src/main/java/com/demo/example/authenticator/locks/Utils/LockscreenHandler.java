package com.demo.example.authenticator.locks.Utils;

import android.app.ActivityManager;
import android.content.ComponentCallbacks2;
import android.content.Intent;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;


public class LockscreenHandler extends AppCompatActivity implements ComponentCallbacks2 {
    private static boolean wentToBg = false;
    private String packageName = "";
    private String TAG = "Fayaz";

    @Override 
    public void onTrimMemory(int i) {
        List<ActivityManager.RunningTaskInfo> runningTasks = ((ActivityManager) getSystemService("activity")).getRunningTasks(1);
        Log.d("topActivity", "CURRENT Activity ::" + runningTasks.get(0).topActivity.getClassName());
        if (runningTasks.size() > 0) {
            this.packageName = runningTasks.get(0).topActivity.getPackageName();
        }
        if (!this.packageName.equals(getPackageName()) && i == 20) {
            wentToBg = true;
            String str = this.TAG;
            Log.d(str, "wentToBg: " + wentToBg);
        }
    }

    @Override 
    protected void onResume() {
        super.onResume();
        if (wentToBg && FayazSP.getString("password", null) != null) {
            wentToBg = false;
            String str = this.TAG;
            Log.d(str, "wentToBg: " + wentToBg);
            EasyLock.checkPassword(this);
        }
    }

    public void revealActivityForResult(int i, Intent intent, int i2) {
        startActivityForResult(intent, i2);
    }

    @Override 
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
