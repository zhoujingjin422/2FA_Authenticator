package com.demo.example.authenticator;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.best.now.myad.WebActivity;
import com.best.now.myad.utils.Constant;
import com.demo.example.authenticator.common.Epic_const;
import com.demo.example.authenticator.locks.Utils.EasyLock;
import com.demo.example.authenticator.locks.Utils.FayazSP;
import com.demo.example.authenticator.locks.Utils.LockscreenHandler;

import com.demo.example.R;

import com.google.android.gms.ads.rewarded.RewardedAd;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import static com.best.now.myad.utils.PublicHelperKt.isRewarded;


public class MainActivity extends LockscreenHandler implements PermissionListener {
    int ad_code;
    int ads_const;
    Context context;
    LinearLayout lin_how_to;
    LinearLayout lin_scan_qr;
    LinearLayout lin_set_up_key;
    LinearLayout lin_settings;
    SharedPreferences spref;
    Toolbar toolbar;

    private void startCameraAction() {

        Dexter.checkPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE");

    }


    public Bundle getNonPersonalizedAdsBundle() {
        Bundle bundle = new Bundle();
        bundle.putString("npa", "1");
        return bundle;
    }


    
    @Override
    
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_main);

        AdAdmob adAdmob = new AdAdmob(this);
        adAdmob.BannerAd((RelativeLayout) findViewById(R.id.bannerAd), this);




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

        FayazSP.init(this);
        this.lin_set_up_key = (LinearLayout) findViewById(R.id.lin_set_up_key);
        this.lin_scan_qr = (LinearLayout) findViewById(R.id.lin_scan_qr);
        this.lin_how_to = (LinearLayout) findViewById(R.id.lin_how_to);
        this.lin_settings = (LinearLayout) findViewById(R.id.lin_settings);
        Dexter.initialize(getApplicationContext());
        Dexter.checkPermission(this, "android.permission.CAMERA");


        this.lin_set_up_key.setOnClickListener(new View.OnClickListener() { 
            @Override 
            public void onClick(View view) {
                if (isRewarded(MainActivity.this)){
                    MainActivity.this.ad_code = 1;

                    if (FayazSP.getString("password", null) == null || FayazSP.getString("password", null).equals("")) {
                        Intent intent2 = new Intent(MainActivity.this, AccountActivity.class);
                        intent2.addFlags(67108864);
                        MainActivity.this.startActivity(intent2);
                    }
                    EasyLock.checkPassword(MainActivity.this.getApplicationContext());

                }

            }
        });
        this.lin_scan_qr.setOnClickListener(new View.OnClickListener() { 
            @Override 
            public void onClick(View view) {
                if (isRewarded(MainActivity.this)) {
                    MainActivity.this.ad_code = 2;

                    Intent intent2 = new Intent(MainActivity.this, AccountActivity.class);
                    intent2.putExtra("qrcodetype", 1);
                    intent2.addFlags(67108864);
                    MainActivity.this.startActivity(intent2);
                }
            }
        });
        this.lin_how_to.setOnClickListener(new View.OnClickListener() { 
            @Override 
            public void onClick(View view) {
                if (isRewarded(MainActivity.this)) {
                    MainActivity.this.ad_code = 3;

                    Intent intent2 = new Intent(MainActivity.this, HowToUseActivity.class);
                    intent2.addFlags(67108864);
                    MainActivity.this.startActivity(intent2);
                }
            }
        });
        this.lin_settings.setOnClickListener(new View.OnClickListener() { 
            @Override 
            public void onClick(View view) {
                if (isRewarded(MainActivity.this)) {
                    Intent intent2 = new Intent(MainActivity.this, SettingsActivity.class);
                    intent2.addFlags(67108864);
                    MainActivity.this.startActivity(intent2);
                }
            }
        });
    }

    @Override 
    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
        if (permissionGrantedResponse.getPermissionName().equals("android.permission.CAMERA")) {
            startCameraAction();
        } else {
            new Handler().post(new Runnable() { 
                @Override 
                public final void run() {
                    Epic_const.getBus();
                }
            });
        }
    }

    @Override 
    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
        Toast makeText = Toast.makeText(this, (int) R.string.error_permission_camera, 0);
        makeText.setGravity(17, 0, 0);
        makeText.show();
    }

    @Override 
    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
        Toast makeText = Toast.makeText(this, (int) R.string.error_permission_camera, 0);
        makeText.setGravity(17, 0, 0);
        makeText.show();
    }

    @Override 
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    @Override 
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == 16908332) {
            finish();
            return true;
        } else if (itemId == R.id.Privacy_Policy) {
            WebActivity.Companion.startActivity(MainActivity.this,"Privacy Policy", Constant.URL_PRIVACY_POLICY);
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
