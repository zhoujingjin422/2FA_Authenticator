package com.demo.example.authenticator.util.color;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.Pair;
import java.util.Random;


public final class ColorUtil {
    public static Pair<Integer, Integer> getColorPair(Context context, Random random) {
        int randomColor = getRandomColor(context, random);
        boolean z = false;
        int[] iArr = {Color.red(randomColor), Color.green(randomColor), Color.blue(randomColor)};
        double d = (double) (iArr[0] * iArr[0]);
        Double.isNaN(d);
        double d2 = (double) (iArr[1] * iArr[1]);
        Double.isNaN(d2);
        double d3 = (d * 0.241d) + (d2 * 0.691d);
        double d4 = (double) (iArr[2] * iArr[2]);
        Double.isNaN(d4);
        if (((int) Math.sqrt(d3 + (d4 * 0.068d))) > 200) {
            z = true;
        }
        return new Pair<>(Integer.valueOf(randomColor), Integer.valueOf(z ? -16777216 : -1));
    }

    public static int getRandomColor(Context context, Random random) {
        return getRandomColorForType(context, new String[]{"200", "400", "600", "800", "A200", "A700"}[random.nextInt(6)], random);
    }

    private static int getRandomColorForType(Context context, String str, Random random) {
        Resources resources = context.getResources();
        int identifier = resources.getIdentifier("mdcolor_" + str, "array", context.getApplicationContext().getPackageName());
        if (identifier == 0) {
            return -16777216;
        }
        TypedArray obtainTypedArray = context.getResources().obtainTypedArray(identifier);
        int color = obtainTypedArray.getColor(random.nextInt(obtainTypedArray.length()), -16777216);
        obtainTypedArray.recycle();
        return color;
    }

    private ColorUtil() {
    }
}
