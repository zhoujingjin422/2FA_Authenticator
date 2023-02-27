package com.demo.example.authenticator;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import androidx.appcompat.app.AppCompatActivity;


public abstract class BaseActivity extends AppCompatActivity {
    private int revealX;
    private int revealY;
    public View rootLayout;

    public boolean canHideStatusBar() {
        return true;
    }

    public SharedPreferences getPrefs() {
        return getSharedPreferences(Constants.PREFS_NAME, 0);
    }

    public void revealActivityForResult(int i, Intent intent, int i2) {
        startActivityForResult(intent, i2);
    }

    public void revealThisActivity(int i, Bundle bundle, Runnable runnable) {
        this.rootLayout = findViewById(i);
        Intent intent = getIntent();
        if (bundle == null && intent.hasExtra("reveal_x")) {
            intent.hasExtra("reveal_y");
        }
        this.rootLayout.setVisibility(View.VISIBLE);
        if (runnable != null) {
            runnable.run();
        }
    }

    private void unReveal() {
        double max = (double) Math.max(this.rootLayout.getWidth(), this.rootLayout.getHeight());
        Double.isNaN(max);
        Animator createCircularReveal = ViewAnimationUtils.createCircularReveal(this.rootLayout, this.revealX, this.revealY, (float) (max * 1.1d), 0.0f);
        createCircularReveal.setDuration(400);
        createCircularReveal.addListener(new AnimatorListenerAdapter() { 
            @Override 
            public void onAnimationEnd(Animator animator) {
                BaseActivity.this.rootLayout.setVisibility(View.INVISIBLE);
                BaseActivity.this.finish();
            }
        });
        createCircularReveal.start();
    }

    @Override 
    public void onBackPressed() {
        if (!getIntent().hasExtra("reveal_x") || !getIntent().hasExtra("reveal_y")) {
            super.onBackPressed();
        } else {
            unReveal();
        }
    }
}
