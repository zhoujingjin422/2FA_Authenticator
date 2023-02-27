package com.demo.example.authenticator.util.icon;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;


public class AsyncIconTask extends AsyncTask<String, Void, Void> {
    private final AppIconCache mCache;
    private final PackageUtil mUtil;

    public AsyncIconTask(PackageUtil packageUtil, AppIconCache appIconCache) {
        this.mUtil = packageUtil;
        this.mCache = appIconCache;
    }

    public Void doInBackground(String... strArr) {
        for (String str : strArr) {
            Drawable findMatchingDrawable = this.mUtil.findMatchingDrawable(str);
            if (findMatchingDrawable != null) {
                this.mCache.store(str, drawableToBitmap(findMatchingDrawable));
            }
        }
        return null;
    }

    
    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        int intrinsicWidth = drawable.getIntrinsicWidth();
        int i = 1;
        if (intrinsicWidth <= 0) {
            intrinsicWidth = 1;
        }
        int intrinsicHeight = drawable.getIntrinsicHeight();
        if (intrinsicHeight > 0) {
            i = intrinsicHeight;
        }
        Bitmap createBitmap = Bitmap.createBitmap(intrinsicWidth, i, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return createBitmap;
    }
}
