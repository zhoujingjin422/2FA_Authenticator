package com.demo.example.authenticator;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.demo.example.R;

import net.glxn.qrgen.android.QRCode;


import com.demo.example.authenticator.tokens.Token;

public class DisplayQrActivity extends AppCompatActivity {
    ImageView imageView;
    LinearLayout linCopy;
    LinearLayout linShare;
    TextView textView;
    Token token;
    Toolbar toolbar;

    @Override
    
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_display_qr);


        AdAdmob adAdmob = new AdAdmob(this);
        adAdmob.BannerAd((RelativeLayout) findViewById(R.id.bannerAd), this);


        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().build());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.toolbar = toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        this.imageView = (ImageView) findViewById(R.id.iv_qr);
        this.linCopy = (LinearLayout) findViewById(R.id.linCopy);
        this.textView = (TextView) findViewById(R.id.tv_uri);
        this.linShare = (LinearLayout) findViewById(R.id.linShare);
        Intent intent = getIntent();
        if (intent.hasExtra("qr_token")) {
            Token token = (Token) intent.getParcelableExtra("qr_token");
            this.token = token;
            String uri = token.toUri(false).toString();

            Uri aa = Uri.fromFile(QRCode.from(uri).file());
            this.imageView.setImageURI(aa);
            this.textView.setText(uri);
        }
        this.linCopy.setOnClickListener(new View.OnClickListener() { 
            @Override 
            public void onClick(View view) {
                ((ClipboardManager) DisplayQrActivity.this.getApplicationContext().getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("text", DisplayQrActivity.this.token.toUri(false).toString()));
                Toast makeText = Toast.makeText(DisplayQrActivity.this.getApplicationContext(), "Text Copied", Toast.LENGTH_SHORT);
                makeText.setGravity(17, 0, 0);
                makeText.show();
            }
        });
        this.linShare.setOnClickListener(new View.OnClickListener() { 
            @Override 
            public void onClick(View view) {
                Intent intent2 = new Intent("android.intent.action.SEND");
                String uri2 = DisplayQrActivity.this.token.toUri(false).toString();
                intent2.setType("text/plain");
                intent2.putExtra("android.intent.extra.TEXT", uri2);
                DisplayQrActivity.this.startActivity(Intent.createChooser(intent2, "Share"));
            }
        });
    }

    @Override 
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override 
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
