package com.demo.example.authenticator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.demo.example.authenticator.common.Epic_const;
import com.demo.example.authenticator.events.TokenEvent;
import com.demo.example.authenticator.fragments.MainFragment;
import com.demo.example.authenticator.inter.TokenOperationHandler;

import com.demo.example.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import static com.best.now.myad.utils.PublicHelperKt.showInterstitialAd;


public class AccountActivity extends BaseActivity implements PermissionListener {
    private static final int PERMISSION_REQUEST_CODE = 1;
    Activity activity;
    FloatingActionButton btnAdd;

    Context context;
    private Fragment mFragment;
    private TokenOperationHandler mTokenHandler;
    Toolbar toolbar;

    @Override 
    public void onCreate(Bundle bundle) {
        Fragment fragment;
        super.onCreate(bundle);
        setContentView(R.layout.activity_account);
        this.activity = this;
        this.context = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.toolbar = toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if (getIntent().getIntExtra("qrcodetype", 0) == 1) {
            Intent intent = new Intent(getApplicationContext(), BarcodeActivity.class);
            intent.addFlags(67108864);
            startActivityForResult(intent, 283);
        }
        showInterstitialAd(AccountActivity.this,null);
        this.btnAdd = (FloatingActionButton) findViewById(R.id.btnAdd);
        Dexter.initialize(getApplicationContext());
        Dexter.continuePendingRequestIfPossible(this);
        if (bundle != null) {
            fragment = getSupportFragmentManager().getFragment(bundle, "fragment");
        } else {
            fragment = MainFragment.createInstance(false);
        }
        setFragment(fragment);

        this.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override 
            public void onClick(View view) {
                Intent intent2 = new Intent(AccountActivity.this, AddSetupKeyActivity.class);
                intent2.addFlags(67108864);
                AccountActivity.this.startActivity(intent2);
            }
        });
    }

    @Override 
    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
        if (!permissionGrantedResponse.getPermissionName().equals("android.permission.CAMERA")) {
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
        Toast makeText = Toast.makeText(this, (int) R.string.error_permission_camera, Toast.LENGTH_SHORT);
        makeText.setGravity(17, 0, 0);
        makeText.show();
    }

    @Override 
    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
        Toast makeText = Toast.makeText(this, (int) R.string.error_permission_camera, Toast.LENGTH_SHORT);
        makeText.setGravity(17, 0, 0);
        makeText.show();
        permissionToken.continuePermissionRequest();
    }

    public void setFragment(Fragment fragment) {
        this.mFragment = fragment;
        getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragment).commit();
    }

    public void setTokenOperationHandler(TokenOperationHandler tokenOperationHandler) {
        this.mTokenHandler = tokenOperationHandler;
    }

    @Override 
    public void onActivityResult(int i, int i2, final Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i2 != 0 && i == 283) {
            if (i2 == -1) {
                new Handler().post(new Runnable() { 
                    @Override 
                    public void run() {
                        Epic_const.getBus().post(new TokenEvent(intent.getStringExtra(BarcodeActivity.EXTRA_STR_QRCODE)));
                    }
                });
            } else if (i2 == 1) {
                new Handler().post(new Runnable() { 
                    @Override 
                    public final void run() {
                    }
                });
            }
        }
    }

    public void requestCameraAction() {
        if (!Dexter.isRequestOngoing()) {
            Dexter.checkPermission(this, "android.permission.CAMERA");
        }
    }

    public void openAccountActivity() {
        Intent intent = new Intent(this, AddSetupKeyActivity.class);
        intent.addFlags(67108864);
        startActivity(intent);
    }

    @Override 
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        getSupportFragmentManager().putFragment(bundle, "fragment", this.mFragment);
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
