package com.demo.example.authenticator.common;

import android.graphics.Typeface;
import androidx.core.content.res.ResourcesCompat;
import androidx.multidex.MultiDexApplication;

import com.demo.example.authenticator.util.icon.AppIconRequestHandler;

import com.demo.example.R;

import com.karumi.dexter.Dexter;
import com.squareup.otto.Bus;
import com.squareup.picasso.Picasso;


public class Epic_const extends MultiDexApplication {
    public static String APP_OPEN_AD_PUB_ID = "";
    public static String BANNER_AD_PUB_ID = "";
    public static String INTRESTITIAL_AD_PUB_ID = "";
    public static String NATIVE_AD_PUB_ID = "";
    public static boolean isActive_adMob = true;
    private static Bus sBus;
    private static Typeface sTypeface;

    public static Bus getBus() {
        return sBus;
    }

    public static Typeface getDefaultTypeface() {
        return sTypeface;
    }

    @Override 
    public void onCreate() {
        super.onCreate();
        sBus = new Bus();
        sTypeface = ResourcesCompat.getFont(this, R.font.overpass_regular);
        Dexter.initialize(this);
        Picasso.Builder builder = new Picasso.Builder(this);
        builder.addRequestHandler(new AppIconRequestHandler(this));
        Picasso build = builder.build();
        build.setIndicatorsEnabled(false);
        build.setLoggingEnabled(true);
        Picasso.setSingletonInstance(build);
    }
}
