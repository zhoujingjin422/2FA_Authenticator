package com.demo.example.authenticator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.demo.example.R;

import com.demo.example.authenticator.adapters.SectionsPagerAdapter;


public class HowToUseActivity extends AppCompatActivity {
    Activity activity;
    Context context;
    ImageView i1;
    ImageView i2;
    ImageView i3;
    ImageView i4;
    ImageView i5;
    ImageView i6;
    ImageView i7;
    ImageView i8;
    int pos;
    Toolbar toolbar;
    TextView tvDescription;
    TextView tvTitle;
    TextView tv_finish;
    TextView tv_next;
    ViewPager viewPager;

    
    @Override
    
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Window window = getWindow();
        window.addFlags(Integer.MIN_VALUE);
        window.clearFlags(67108864);
        window.setStatusBarColor(getResources().getColor(R.color.how_to_use_status_bar));
        setContentView(R.layout.activity_how_to_use);


        AdAdmob adAdmob = new AdAdmob(this);
        adAdmob.BannerAd((RelativeLayout) findViewById(R.id.bannerAd), this);
        adAdmob.FullscreenAd(this);


        this.activity = this;
        this.context = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.toolbar = toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        this.i1 = (ImageView) findViewById(R.id.i1);
        this.i2 = (ImageView) findViewById(R.id.i2);
        this.i3 = (ImageView) findViewById(R.id.i3);
        this.i4 = (ImageView) findViewById(R.id.i4);
        this.i5 = (ImageView) findViewById(R.id.i5);
        this.i6 = (ImageView) findViewById(R.id.i6);
        this.i7 = (ImageView) findViewById(R.id.i7);
        this.i8 = (ImageView) findViewById(R.id.i8);
        this.tv_next = (TextView) findViewById(R.id.tv_next);
        this.tv_finish = (TextView) findViewById(R.id.tv_finish);
        this.tvTitle = (TextView) findViewById(R.id.tvTitle);
        this.tvDescription = (TextView) findViewById(R.id.tvDescription);
        this.viewPager = (ViewPager) findViewById(R.id.viewPager);
        initView();
        this.tv_finish.setVisibility(View.GONE);
        this.tv_next.setVisibility(View.VISIBLE);
        this.tv_next.setOnClickListener(new View.OnClickListener() { 
            @Override 
            public void onClick(View view) {
                if (HowToUseActivity.this.pos == 7) {
                    HowToUseActivity.this.tv_finish.setVisibility(View.VISIBLE);
                    HowToUseActivity.this.tv_next.setVisibility(View.GONE);
                    return;
                }
                HowToUseActivity.this.viewPager.setCurrentItem(HowToUseActivity.this.pos + 1);
            }
        });
        this.tv_finish.setOnClickListener(new View.OnClickListener() { 
            @Override 
            public void onClick(View view) {
                HowToUseActivity.this.finish();
            }
        });
        this.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() { 
            @Override 
            public void onPageScrollStateChanged(int i) {
            }

            @Override 
            public void onPageScrolled(int i, float f, int i2) {
            }

            @Override 
            public void onPageSelected(int i) {
                if (i == 7) {
                    HowToUseActivity.this.tv_finish.setVisibility(View.VISIBLE);
                    HowToUseActivity.this.tv_next.setVisibility(View.GONE);
                } else {
                    HowToUseActivity.this.tv_finish.setVisibility(View.GONE);
                    HowToUseActivity.this.tv_next.setVisibility(View.VISIBLE);
                }
                HowToUseActivity.this.setSelection(i);
                HowToUseActivity.this.pos = i;
            }
        });
    }

    private void initView() {
        this.viewPager.setAdapter(new SectionsPagerAdapter(this, getSupportFragmentManager()));
    }

    public void setSelection(int i) {
        switch (i) {
            case 0:
                this.tvTitle.setText("Setting");
                this.tvDescription.setText("Open account setting in which you want to \nenable Two step authentication.");
                this.i1.setImageResource(R.drawable.dot_fill);
                this.i2.setImageResource(R.drawable.dot_out);
                this.i3.setImageResource(R.drawable.dot_out);
                this.i4.setImageResource(R.drawable.dot_out);
                this.i5.setImageResource(R.drawable.dot_out);
                this.i6.setImageResource(R.drawable.dot_out);
                this.i7.setImageResource(R.drawable.dot_out);
                this.i8.setImageResource(R.drawable.dot_out);
                return;
            case 1:
                this.tvTitle.setText("Security");
                this.tvDescription.setText("Select security option from account\nsetting menu.");
                this.i1.setImageResource(R.drawable.dot_out);
                this.i2.setImageResource(R.drawable.dot_fill);
                this.i3.setImageResource(R.drawable.dot_out);
                this.i4.setImageResource(R.drawable.dot_out);
                this.i5.setImageResource(R.drawable.dot_out);
                this.i6.setImageResource(R.drawable.dot_out);
                this.i7.setImageResource(R.drawable.dot_out);
                this.i8.setImageResource(R.drawable.dot_out);
                return;
            case 2:
                this.tvTitle.setText("Select Two Factor Authentication");
                this.tvDescription.setText("Find two factor authentication from the security option.\nAnd click on two step authentication.");
                this.i1.setImageResource(R.drawable.dot_out);
                this.i2.setImageResource(R.drawable.dot_out);
                this.i3.setImageResource(R.drawable.dot_fill);
                this.i4.setImageResource(R.drawable.dot_out);
                this.i5.setImageResource(R.drawable.dot_out);
                this.i6.setImageResource(R.drawable.dot_out);
                this.i7.setImageResource(R.drawable.dot_out);
                this.i8.setImageResource(R.drawable.dot_out);
                return;
            case 3:
                this.tvTitle.setText("Select Authentication App");
                this.tvDescription.setText("You can see multiple options for two step authentication.\nSelect authenticator app option from it.");
                this.i1.setImageResource(R.drawable.dot_out);
                this.i2.setImageResource(R.drawable.dot_out);
                this.i3.setImageResource(R.drawable.dot_out);
                this.i4.setImageResource(R.drawable.dot_fill);
                this.i5.setImageResource(R.drawable.dot_out);
                this.i6.setImageResource(R.drawable.dot_out);
                this.i7.setImageResource(R.drawable.dot_out);
                this.i8.setImageResource(R.drawable.dot_out);
                return;
            case 4:
                this.tvTitle.setText("Scan OR Enter Details Manually");
                this.tvDescription.setText("Scan QR Code with the help of this app.\nOr you can enter details manually and enter secret key in it.\nNOTE: - Only Scan Account Authenticator QR Code");
                this.i1.setImageResource(R.drawable.dot_out);
                this.i2.setImageResource(R.drawable.dot_out);
                this.i3.setImageResource(R.drawable.dot_out);
                this.i4.setImageResource(R.drawable.dot_out);
                this.i5.setImageResource(R.drawable.dot_fill);
                this.i6.setImageResource(R.drawable.dot_out);
                this.i7.setImageResource(R.drawable.dot_out);
                this.i8.setImageResource(R.drawable.dot_out);
                return;
            case 5:
                this.tvTitle.setText("Password Setting");
                this.tvDescription.setText("To protect account token list to unauthorized persons.");
                this.i1.setImageResource(R.drawable.dot_out);
                this.i2.setImageResource(R.drawable.dot_out);
                this.i3.setImageResource(R.drawable.dot_out);
                this.i4.setImageResource(R.drawable.dot_out);
                this.i5.setImageResource(R.drawable.dot_out);
                this.i6.setImageResource(R.drawable.dot_fill);
                this.i7.setImageResource(R.drawable.dot_out);
                this.i8.setImageResource(R.drawable.dot_out);
                return;
            case 6:
                this.tvTitle.setText("Backup Password");
                this.tvDescription.setText("Backup all accounts passwords safely in your Device.");
                this.i1.setImageResource(R.drawable.dot_out);
                this.i2.setImageResource(R.drawable.dot_out);
                this.i3.setImageResource(R.drawable.dot_out);
                this.i4.setImageResource(R.drawable.dot_out);
                this.i5.setImageResource(R.drawable.dot_out);
                this.i6.setImageResource(R.drawable.dot_out);
                this.i7.setImageResource(R.drawable.dot_fill);
                this.i8.setImageResource(R.drawable.dot_out);
                return;
            case 7:
                this.tvTitle.setText("Restore Password");
                this.tvDescription.setText("You are forgetting your password?\nYou can restore all passwords any time in just click.");
                this.i1.setImageResource(R.drawable.dot_out);
                this.i2.setImageResource(R.drawable.dot_out);
                this.i3.setImageResource(R.drawable.dot_out);
                this.i4.setImageResource(R.drawable.dot_out);
                this.i5.setImageResource(R.drawable.dot_out);
                this.i6.setImageResource(R.drawable.dot_out);
                this.i7.setImageResource(R.drawable.dot_out);
                this.i8.setImageResource(R.drawable.dot_fill);
                return;
            default:
                return;
        }
    }

    @Override 
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
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
                Toast makeText2 = Toast.makeText(getApplicationContext(), "No Internet Connection..", 0);
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
