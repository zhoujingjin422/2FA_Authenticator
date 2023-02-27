package com.demo.example.authenticator.util.color;

import android.content.Context;
import android.util.Log;
import android.util.Pair;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.drawable.DrawableCompat;

import com.demo.example.R;

import java.util.Random;


public class ColorIcon {
    private int bgColor = 0;
    private int fgColor = 0;
    private final long seed;
    private String shortTitle;

    public ColorIcon(String str) {
        Log.e("HHHH", " " + str);
        this.seed = stringToSeed(str);
        this.shortTitle = String.valueOf(str.charAt(0)).toUpperCase();
        if (str.contains(" ")) {
            String[] split = str.split(" ");
            if (split.length > 1) {
                this.shortTitle = String.valueOf(split[0].charAt(0)).toUpperCase() + String.valueOf(split[1].charAt(0));
            }
        }
    }

    private static long stringToSeed(String str) {
        long j = 0;
        if (str == null) {
            return 0;
        }
        for (char c : str.toCharArray()) {
            j = (j * 31) + ((long) c);
        }
        return j;
    }

    public void apply(TextView textView) {
        Context context = textView.getContext();
        if (this.fgColor == 0 && this.bgColor == 0) {
            Pair<Integer, Integer> colorPair = ColorUtil.getColorPair(context, new Random(this.seed));
            this.bgColor = ((Integer) colorPair.first).intValue();
            this.fgColor = ((Integer) colorPair.second).intValue();
        }
        textView.setText(this.shortTitle);
        textView.setTextColor(context.getResources().getColor(R.color.text_grey));
        textView.setBackgroundDrawable(DrawableCompat.wrap(AppCompatResources.getDrawable(context, R.drawable.icon_bg).mutate()));
    }
}
