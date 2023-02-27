package com.demo.example.authenticator.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Layout;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.AlignmentSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import com.demo.example.authenticator.common.Epic_const;
import com.demo.example.authenticator.ui.CenteredImageSpan;
import com.demo.example.authenticator.ui.CustomTypefaceSpan;


public final class FontUtil {
    private FontUtil() {
    }

    public static void setTypeface(TextView textView) {
        textView.setTypeface(Epic_const.getDefaultTypeface());
    }

    public static SpannableString setTypeface(String str) {
        SpannableString spannableString = new SpannableString(str);
        spannableString.setSpan(new CustomTypefaceSpan(Epic_const.getDefaultTypeface()), 0, spannableString.length(), 33);
        return spannableString;
    }

    public static SpannableStringBuilder setIcon(Context context, String str, String str2, int i, int i2) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(str);
        int indexOf = str.indexOf(str2);
        int length = str2.length();
        if (indexOf != -1) {
            Drawable drawable = ContextCompat.getDrawable(context, i);
            if (i2 > 0) {
                drawable.setBounds(0, 0, i2, i2);
            } else {
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            }
            int i3 = length + indexOf;
            spannableStringBuilder.setSpan(new CenteredImageSpan(drawable), indexOf, i3, 33);
            if (i3 > 0) {
                spannableStringBuilder.setSpan(new CustomTypefaceSpan(Epic_const.getDefaultTypeface()), 0, i3 - 1, 33);
            }
            if (i3 < str.length() - 1) {
                spannableStringBuilder.setSpan(new CustomTypefaceSpan(Epic_const.getDefaultTypeface()), i3, str.length() - 1, 33);
            }
            return spannableStringBuilder;
        }
        throw new IllegalArgumentException("needle should occur in the text; '" + str2 + "' not found");
    }

    public static SpannableStringBuilder setIcon(Context context, String str, int i) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder("  " + str);
        Drawable drawable = ContextCompat.getDrawable(context, i);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        spannableStringBuilder.setSpan(new CenteredImageSpan(drawable), 0, 1, 33);
        CustomTypefaceSpan customTypefaceSpan = new CustomTypefaceSpan(Epic_const.getDefaultTypeface());
        spannableStringBuilder.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, str.length() - 1, 33);
        spannableStringBuilder.setSpan(customTypefaceSpan, 1, str.length() - 1, 33);
        return spannableStringBuilder;
    }

    public static SpannableStringBuilder setIcon(Context context, String str, int i, int i2) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder("  " + str);
        Drawable drawable = ContextCompat.getDrawable(context, i);
        if (Build.VERSION.SDK_INT >= 21) {
            drawable.setTint(i2);
        }
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        spannableStringBuilder.setSpan(new CenteredImageSpan(drawable, 1, (int) (((float) drawable.getIntrinsicWidth()) * 0.2f)), 0, 1, 33);
        CustomTypefaceSpan customTypefaceSpan = new CustomTypefaceSpan(Epic_const.getDefaultTypeface());
        spannableStringBuilder.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, str.length() - 1, 33);
        spannableStringBuilder.setSpan(customTypefaceSpan, 1, str.length() - 1, 33);
        return spannableStringBuilder;
    }

    public static void setTypefaceOnMenu(Menu menu) {
        for (int i = 0; i < menu.size(); i++) {
            setTypefaceOnMenu(menu.getItem(i));
        }
    }

    public static void setTypefaceOnMenu(MenuItem menuItem) {
        menuItem.setTitle(setTypeface(menuItem.getTitle().toString()));
    }
}
