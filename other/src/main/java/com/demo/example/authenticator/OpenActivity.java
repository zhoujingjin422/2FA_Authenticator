package com.demo.example.authenticator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.demo.example.R;

import com.demo.example.authenticator.common.PrefManager;



public class OpenActivity extends AppCompatActivity {
    int ads_const;
    SharedPreferences.Editor editor;

    PrefManager prefManager;
    ProgressBar progressBar;
    SharedPreferences spref;
    boolean Ad_Show = false;
    boolean in_app_check = false;

    
    @Override
    
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setFlags(67108864, 67108864);
        }
        setContentView(R.layout.splashactivity);
        this.prefManager = new PrefManager(this);
        SharedPreferences sharedPreferences = getSharedPreferences("pref_ads", 0);
        this.spref = sharedPreferences;
        this.editor = sharedPreferences.edit();
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        this.progressBar = progressBar;
        progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.MULTIPLY);

        ContinueWithoutAdsProcess();

    }


    public void ContinueWithoutAdsProcess() {
        new Handler().postDelayed(new Runnable() { 
            @Override 
            public void run() {
                OpenActivity.this.HomeScreen();
            }
        }, 3000);
    }


    public void HomeScreen() {
        this.Ad_Show = false;

        startActivity(new Intent(this, MainActivity.class));

        finish();

    }

    @Override 
    public void onBackPressed() {
        super.onBackPressed();
        ExitApp();
    }

    public void ExitApp() {
        moveTaskToBack(true);
        finish();
        Process.killProcess(Process.myPid());
        System.exit(0);
    }
}
