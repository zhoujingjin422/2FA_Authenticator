package com.demo.example.authenticator.locks.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.example.R;

import com.demo.example.authenticator.AccountActivity;
import com.demo.example.authenticator.locks.Interfaces.ActivityChanger;
import com.demo.example.authenticator.locks.Utils.FayazSP;
import com.demo.example.authenticator.locks.Utils.LockscreenHandler;


public class LockscreenActivity extends LockscreenHandler implements ActivityChanger {
    private static Class classToGoAfter;
    private Button btnDone;
    private Button btnErase;
    private TextView textViewDot;
    private TextView textViewHAHA;
    String tempPass = "";
    private int[] passButtonIds = {R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9, R.id.btn0};
    private String passString = "";
    private String realPass = "";
    private String status = "";
    private String checkStatus = "check";
    private String setStatus = "set";
    private String setStatus1 = "set1";
    private String disableStatus = "disable";
    private String changeStatus = "change";
    private String changeStatus1 = "change1";
    private String changeStatus2 = "change2";

    static  String access$084(LockscreenActivity lockscreenActivity, Object obj) {
        String str = lockscreenActivity.passString + obj;
        lockscreenActivity.passString = str;
        return str;
    }

    
    @Override
    
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Window window = getWindow();
        window.addFlags(Integer.MIN_VALUE);
        window.clearFlags(67108864);
        window.setStatusBarColor(getResources().getColor(R.color.text_blue));
        setContentView(R.layout.lockscreen_activity_easy);
        FayazSP.init(this);
        this.realPass = getPassword();
        initViews();
        String string = getIntent().getExtras().getString("passStatus", "check");
        this.status = string;
        if (string.equals(this.setStatus)) {
            this.textViewHAHA.setText("Enter a New Password");
        }
        if (this.status.equals(this.disableStatus)) {
            FayazSP.put("password", (String) null);
            Toast makeText = Toast.makeText(getApplicationContext(), "Password Disabled", Toast.LENGTH_SHORT);
            makeText.setGravity(17, 0, 0);
            makeText.show();
            gotoActivity();
        }
    }

    private void initViews() {
        this.textViewHAHA = (TextView) findViewById(R.id.haha_text);
        this.textViewDot = (TextView) findViewById(R.id.dotText);
        this.btnDone = (Button) findViewById(R.id.btnDone);
        Button button = (Button) findViewById(R.id.btnErase);
        this.btnErase = button;
        button.setOnClickListener(new View.OnClickListener() { 
            @Override 
            public void onClick(View view) {
                if (LockscreenActivity.this.passString.length() > 0) {
                    LockscreenActivity lockscreenActivity = LockscreenActivity.this;
                    lockscreenActivity.passString = lockscreenActivity.passString.substring(0, LockscreenActivity.this.passString.length() - 1);
                }
                LockscreenActivity.this.textViewDot.setText(LockscreenActivity.this.passString);
            }
        });
        this.btnDone.setOnClickListener(new View.OnClickListener() { 
            @Override 
            public void onClick(View view) {
                if (LockscreenActivity.this.status.equals(LockscreenActivity.this.checkStatus)) {
                    if (LockscreenActivity.this.passString.equals(LockscreenActivity.this.realPass)) {
                        Intent intent = new Intent(LockscreenActivity.this.getApplicationContext(), AccountActivity.class);
                        intent.addFlags(268435456);
                        LockscreenActivity.this.startActivity(intent);
                        LockscreenActivity.this.finish();
                        return;
                    }
                    LockscreenActivity.this.passString = "";
                    LockscreenActivity.this.textViewDot.setText(LockscreenActivity.this.passString);
                    Toast makeText = Toast.makeText(LockscreenActivity.this.getApplicationContext(), "Incorrect Password", Toast.LENGTH_SHORT);
                    makeText.setGravity(17, 0, 0);
                    makeText.show();
                } else if (LockscreenActivity.this.status.equals(LockscreenActivity.this.setStatus)) {
                    LockscreenActivity lockscreenActivity = LockscreenActivity.this;
                    lockscreenActivity.tempPass = lockscreenActivity.passString;
                    LockscreenActivity.this.passString = "";
                    LockscreenActivity lockscreenActivity2 = LockscreenActivity.this;
                    lockscreenActivity2.status = lockscreenActivity2.setStatus1;
                    LockscreenActivity.this.textViewHAHA.setText("Confirm Password");
                    LockscreenActivity.this.textViewDot.setText(LockscreenActivity.this.passString);
                } else if (LockscreenActivity.this.status.equals(LockscreenActivity.this.setStatus1)) {
                    if (LockscreenActivity.this.passString.equals(LockscreenActivity.this.tempPass)) {
                        FayazSP.put("password", LockscreenActivity.this.passString);
                        Toast makeText2 = Toast.makeText(LockscreenActivity.this.getApplicationContext(), "Password is set", Toast.LENGTH_SHORT);
                        makeText2.setGravity(17, 0, 0);
                        makeText2.show();
                        LockscreenActivity.this.gotoActivity();
                        return;
                    }
                    LockscreenActivity lockscreenActivity3 = LockscreenActivity.this;
                    lockscreenActivity3.tempPass = lockscreenActivity3.passString;
                    LockscreenActivity.this.passString = "";
                    LockscreenActivity.this.tempPass = "";
                    LockscreenActivity lockscreenActivity4 = LockscreenActivity.this;
                    lockscreenActivity4.status = lockscreenActivity4.setStatus;
                    LockscreenActivity.this.textViewDot.setText(LockscreenActivity.this.passString);
                    LockscreenActivity.this.textViewHAHA.setText("Enter a New Password");
                    Toast makeText3 = Toast.makeText(LockscreenActivity.this.getApplicationContext(), "Please Enter a New Password Again", Toast.LENGTH_SHORT);
                    makeText3.setGravity(17, 0, 0);
                    makeText3.show();
                } else if (LockscreenActivity.this.status.equals(LockscreenActivity.this.changeStatus)) {
                    if (LockscreenActivity.this.passString.equals(LockscreenActivity.this.realPass)) {
                        LockscreenActivity lockscreenActivity5 = LockscreenActivity.this;
                        lockscreenActivity5.tempPass = lockscreenActivity5.passString;
                        LockscreenActivity.this.passString = "";
                        LockscreenActivity.this.tempPass = "";
                        LockscreenActivity lockscreenActivity6 = LockscreenActivity.this;
                        lockscreenActivity6.status = lockscreenActivity6.changeStatus1;
                        LockscreenActivity.this.textViewHAHA.setText("Enter a New Password");
                        LockscreenActivity.this.textViewDot.setText(LockscreenActivity.this.passString);
                        return;
                    }
                    LockscreenActivity.this.passString = "";
                    LockscreenActivity.this.textViewDot.setText(LockscreenActivity.this.passString);
                    Toast makeText4 = Toast.makeText(LockscreenActivity.this.getApplicationContext(), "Please Enter Current Password", Toast.LENGTH_SHORT);
                    makeText4.setGravity(17, 0, 0);
                    makeText4.show();
                } else if (LockscreenActivity.this.status.equals(LockscreenActivity.this.changeStatus1)) {
                    LockscreenActivity lockscreenActivity7 = LockscreenActivity.this;
                    lockscreenActivity7.tempPass = lockscreenActivity7.passString;
                    LockscreenActivity.this.passString = "";
                    LockscreenActivity lockscreenActivity8 = LockscreenActivity.this;
                    lockscreenActivity8.status = lockscreenActivity8.changeStatus2;
                    LockscreenActivity.this.textViewHAHA.setText("Confirm Password");
                    LockscreenActivity.this.textViewDot.setText(LockscreenActivity.this.passString);
                } else if (!LockscreenActivity.this.status.equals(LockscreenActivity.this.changeStatus2)) {
                } else {
                    if (LockscreenActivity.this.passString.equals(LockscreenActivity.this.tempPass)) {
                        FayazSP.put("password", LockscreenActivity.this.passString);
                        Toast makeText5 = Toast.makeText(LockscreenActivity.this.getApplicationContext(), "Password Changed", Toast.LENGTH_SHORT);
                        makeText5.setGravity(17, 0, 0);
                        makeText5.show();
                        LockscreenActivity.this.gotoActivity();
                        return;
                    }
                    LockscreenActivity lockscreenActivity9 = LockscreenActivity.this;
                    lockscreenActivity9.tempPass = lockscreenActivity9.passString;
                    LockscreenActivity.this.passString = "";
                    LockscreenActivity.this.tempPass = "";
                    LockscreenActivity lockscreenActivity10 = LockscreenActivity.this;
                    lockscreenActivity10.status = lockscreenActivity10.changeStatus1;
                    LockscreenActivity.this.textViewDot.setText(LockscreenActivity.this.passString);
                    LockscreenActivity.this.textViewHAHA.setText("Enter a New Password");
                }
            }
        });
        int i = 0;
        while (true) {
            int[] iArr = this.passButtonIds;
            if (i < iArr.length) {
                final Button button2 = (Button) findViewById(iArr[i]);
                button2.setOnClickListener(new View.OnClickListener() { 
                    @Override 
                    public void onClick(View view) {
                        if (LockscreenActivity.this.passString.length() >= 8) {
                            Toast makeText = Toast.makeText(LockscreenActivity.this.getApplicationContext(), "Max 8 characters", Toast.LENGTH_SHORT);
                            makeText.setGravity(17, 0, 0);
                            makeText.show();
                        } else {
                            LockscreenActivity.access$084(LockscreenActivity.this, button2.getText().toString());
                        }
                        LockscreenActivity.this.textViewDot.setText(LockscreenActivity.this.passString);
                    }
                });
                i++;
            } else {
                return;
            }
        }
    }

    private String getPassword() {
        return FayazSP.getString("password", null);
    }

    
    public void gotoActivity() {
        finish();
    }

    @Override 
    public void activityClass(Class cls) {
        classToGoAfter = cls;
    }

    @Override
    
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
