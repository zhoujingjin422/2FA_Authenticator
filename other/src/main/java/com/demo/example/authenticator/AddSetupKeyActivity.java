package com.demo.example.authenticator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.demo.example.R;

import com.demo.example.authenticator.common.Epic_const;
import com.demo.example.authenticator.events.TokenEvent;
import com.demo.example.authenticator.tokens.Token;
import com.demo.example.authenticator.tokens.TokenType;
import com.demo.example.authenticator.ui.FontSpinnerAdapter;
import com.demo.example.authenticator.util.AsteriskPasswordTransformationMethod;

import java.util.Locale;


public class AddSetupKeyActivity extends AppCompatActivity {
    int ads_const;
    Intent datas;
    ImageView imgBack;
    boolean isEditing;
    LinearLayout linSave;
    private Spinner mAlgorithm;
    private EditText mInterval;
    private EditText mIssuer;
    private EditText mLabel;
    private EditText mSecret;
    private Spinner mType;
    TextView seconds_label;
    SharedPreferences spref;


    public Bundle getNonPersonalizedAdsBundle() {
        Bundle bundle = new Bundle();
        bundle.putString("npa", "1");
        return bundle;
    }

    
    @Override
    
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_add_setup_key);


        AdAdmob adAdmob = new AdAdmob(this);
        adAdmob.BannerAd((RelativeLayout) findViewById(R.id.bannerAd), this);
        adAdmob.FullscreenAd(this);




        SharedPreferences sharedPreferences = getSharedPreferences("pref_ads", 0);
        this.spref = sharedPreferences;
        this.ads_const = sharedPreferences.getInt("ads_const", 0);

        this.mType = (Spinner) findViewById(R.id.spType);
        this.imgBack = (ImageView) findViewById(R.id.imgBack);
        this.linSave = (LinearLayout) findViewById(R.id.linSave);
        this.mAlgorithm = (Spinner) findViewById(R.id.spAlgorithm);
        this.mIssuer = (EditText) findViewById(R.id.issuer);
        this.mLabel = (EditText) findViewById(R.id.label);
        this.mSecret = (EditText) findViewById(R.id.secret);
        this.mInterval = (EditText) findViewById(R.id.interval);
        this.seconds_label = (TextView) findViewById(R.id.seconds_label);
        this.mType.setAdapter((SpinnerAdapter) new FontSpinnerAdapter(this.mType.getContext(), R.array.token_types));
        this.mAlgorithm.setAdapter((SpinnerAdapter) new FontSpinnerAdapter(this.mAlgorithm.getContext(), R.array.algorithms));
        Intent intent = getIntent();
        this.datas = intent;
        if (intent.hasExtra("edit_token")) {
            this.isEditing = true;
            bindData();
        } else {
            this.isEditing = false;
        }
        this.linSave.setOnClickListener(new View.OnClickListener() { 
            @Override 
            public void onClick(View view) {
                String str;
                String str2;
                if (AddSetupKeyActivity.this.mIssuer.getText().length() > 0 && AddSetupKeyActivity.this.mSecret.getText().length() >= 8) {
                    String encode = Uri.encode(AddSetupKeyActivity.this.mIssuer.getText().toString());
                    String encode2 = Uri.encode(AddSetupKeyActivity.this.mLabel.getText().toString());
                    String encode3 = Uri.encode(AddSetupKeyActivity.this.mSecret.getText().toString());
                    if (AddSetupKeyActivity.this.mType.getSelectedItemId() == 0) {
                        str = "totp";
                    } else {
                        str = "hotp";
                    }
                    String lowerCase = AddSetupKeyActivity.this.mAlgorithm.getSelectedItem().toString().toLowerCase(Locale.US);
                    int parseInt = Integer.parseInt(AddSetupKeyActivity.this.mInterval.getText().toString());
                    String format = String.format(Locale.US, "otpauth://%s/%s:%s?secret=%s&algorithm=%s&digits=%d", str, encode, encode2, encode3, lowerCase, 6);
                    if (str.equals("totp")) {
                        str2 = format.concat(String.format(Locale.US, "&period=%d", Integer.valueOf(parseInt)));
                    } else {
                        str2 = format.concat(String.format(Locale.US, "&counter=%d", Integer.valueOf(parseInt)));
                    }
                    if (AddSetupKeyActivity.this.isEditing) {
                        Epic_const.getBus().post(new TokenEvent(str2, ((Token) AddSetupKeyActivity.this.datas.getParcelableExtra("edit_token")).getDatabaseId()));
                    } else {
                        Epic_const.getBus().post(new TokenEvent(str2));
                    }
                    AddSetupKeyActivity.this.datas = null;
                    AddSetupKeyActivity.this.finish();
                } else if (AddSetupKeyActivity.this.mIssuer.getText().length() == 0) {
                    AddSetupKeyActivity.this.mIssuer.setError("Please Enter Label.");
                } else if (AddSetupKeyActivity.this.mSecret.getText().length() == 0) {
                    AddSetupKeyActivity.this.mSecret.setError("Please Enter Secret Key.");
                } else if (AddSetupKeyActivity.this.mSecret.getText().length() < 32) {
                    AddSetupKeyActivity.this.mSecret.setError("Please Enter Valid Secret Key.");
                }
            }
        });
        this.imgBack.setOnClickListener(new View.OnClickListener() { 
            @Override 
            public void onClick(View view) {
                AddSetupKeyActivity.this.onBackPressed();
            }
        });
        this.mType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { 
            @Override 
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

            @Override 
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                ((TextView) adapterView.getChildAt(0)).setTextColor(AddSetupKeyActivity.this.getResources().getColor(R.color.colorWhite));
                TextView textView = (TextView) AddSetupKeyActivity.this.findViewById(R.id.interval_label);
                if (i == 0) {
                    textView.setText(R.string.interval);
                    AddSetupKeyActivity.this.mInterval.setText("30");
                    AddSetupKeyActivity.this.seconds_label.setText("Seconds");
                    return;
                }
                textView.setText(R.string.counter);
                AddSetupKeyActivity.this.mInterval.setText("0");
                AddSetupKeyActivity.this.seconds_label.setText("");
            }
        });
        this.mAlgorithm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { 
            @Override 
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

            @Override 
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                ((TextView) adapterView.getChildAt(0)).setTextColor(AddSetupKeyActivity.this.getResources().getColor(R.color.colorWhite));
            }
        });
    }

    private void bindData() {
        Token token = (Token) this.datas.getParcelableExtra("edit_token");
        String[] stringArray = getResources().getStringArray(R.array.algorithms);
        this.mSecret.setEnabled(false);
        this.mIssuer.setText(token.getIssuer());
        this.mLabel.setText(token.getLabel());
        this.mSecret.setText(token.getSecret());
        this.mType.setSelection(token.getType() == TokenType.TOTP ? 0 : 1);
        this.mInterval.setText(String.valueOf(token.getIntervalSec()));
        token.getDigits();
        this.mSecret.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        for (int i = 0; i < stringArray.length; i++) {
            if (stringArray[i].equalsIgnoreCase(token.getAlgorithm())) {
                this.mAlgorithm.setSelection(i);
                return;
            }
        }
    }

    @Override 
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override 
    public void onBackPressed() {
        this.datas = null;
        finish();

    }
}
