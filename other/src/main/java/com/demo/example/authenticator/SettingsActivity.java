package com.demo.example.authenticator;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.demo.example.authenticator.locks.Utils.EasyLock;
import com.demo.example.authenticator.locks.Utils.LockscreenHandler;

import com.demo.example.R;



public class SettingsActivity extends LockscreenHandler {
    int ads_const;
    Context context;
    SharedPreferences spref;
    Toolbar toolbar;

    public Bundle getNonPersonalizedAdsBundle() {
        Bundle bundle = new Bundle();
        bundle.putString("npa", "1");
        return bundle;
    }

    
    @Override 
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_settings);


        AdAdmob adAdmob = new AdAdmob(this);
        adAdmob.BannerAd((RelativeLayout) findViewById(R.id.bannerAd), this);
        adAdmob.FullscreenAd(this);




        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.toolbar = toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        this.context = this;
        SharedPreferences sharedPreferences = getSharedPreferences("pref_ads", 0);
        this.spref = sharedPreferences;
        this.ads_const = sharedPreferences.getInt("ads_const", 0);

    }

    public void setPass(View view) {
        EasyLock.setPassword(this);
    }

    public void changePass(View view) {
        EasyLock.changePassword(this);
    }

    public void disable(View view) {
        EasyLock.disablePassword(this);
    }

    @Override 
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override 
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override 
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == 16908332) {
            finish();
            return true;
        } else if (itemId == R.id.Privacy_Policy) {
            Intent intent = new Intent(this, Privacy_Policy_activity.class);
            intent.addFlags(67108864);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.rate_us) {
            if (isOnline()) {
                Intent intent3 = new Intent("android.intent.action.VIEW", Uri.parse("http://play.google.com/store/apps/details?id=" + this.context.getPackageName()));
                intent3.addFlags(67108864);
                intent3.addFlags(268435456);
                startActivity(intent3);
            } else {
                Toast makeText = Toast.makeText(getApplicationContext(), "No Internet Connection..", Toast.LENGTH_SHORT);
                makeText.setGravity(17, 0, 0);
                makeText.show();
            }
            return true;
        } else if (itemId == R.id.share_app) {
            if (isOnline()) {
                Intent intent4 = new Intent("android.intent.action.SEND");
                intent4.setType("text/plain");
                intent4.putExtra("android.intent.extra.TEXT", "Hi! I'm using a great Authenticator application. Check it out:http://play.google.com/store/apps/details?id=" + this.context.getPackageName());
                intent4.addFlags(67108864);
                startActivity(Intent.createChooser(intent4, "Share with Friends"));
            } else {
                Toast makeText2 = Toast.makeText(getApplicationContext(), "No Internet Connection..", Toast.LENGTH_SHORT);
                makeText2.setGravity(17, 0, 0);
                makeText2.show();
            }
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public boolean isOnline() {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
}
