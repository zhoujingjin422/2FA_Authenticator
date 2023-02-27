package com.demo.example.authenticator.util.icon;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;


public class AppIconCache {
    private static final String TAG = "AppIconCache";
    private final Context mContext;

    
    public static class CacheEntry {
        private static final long CACHE_DURATION_MS = TimeUnit.DAYS.toMillis(3);
        private final Drawable mDrawable;
        private final File mFile;

        public CacheEntry(File file) {
            this.mFile = file;
            this.mDrawable = Drawable.createFromPath(file.getAbsolutePath());
        }

        private long age() {
            return System.currentTimeMillis() - this.mFile.lastModified();
        }

        public boolean isExpired() {
            return age() > CACHE_DURATION_MS;
        }

        public Drawable getDrawable() {
            return this.mDrawable;
        }
    }

    public AppIconCache(Context context) {
        this.mContext = context;
    }

    private String getSanitizedFilename(String str) {
        return str.replaceAll("[^a-zA-Z0-9-_\\.]", "_");
    }

    private File getFile(String str) {
        return new File(this.mContext.getFilesDir(), getSanitizedFilename(str));
    }

    public CacheEntry find(String str) {
        File file = getFile(getSanitizedFilename(str));
        if (file.exists()) {
            return new CacheEntry(file);
        }
        return null;
    }

    public void store(String str, Bitmap bitmap) {
        try {
            FileOutputStream openFileOutput = this.mContext.openFileOutput(getSanitizedFilename(str), 0);
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, openFileOutput);
            openFileOutput.close();
        } catch (IOException | IllegalArgumentException e) {
            Log.e(TAG, "Unable to store drawable to cache", e);
        }
    }
}
