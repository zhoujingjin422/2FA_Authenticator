package com.demo.example.authenticator.util.icon;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.demo.example.authenticator.common.Epic_const;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Request;
import com.squareup.picasso.RequestHandler;

import java.io.IOException;
import java.util.Locale;


public class AppIconRequestHandler extends RequestHandler {
    private static final String TAG = AppIconRequestHandler.class.getSimpleName();
    private static final String URI_HOST = Epic_const.class.getPackage().getName();
    private final AppIconCache mCache;
    private final PackageUtil mUtil;

    public static String createUriString(String str) {
        if (str == null) {
            return null;
        }
        String htmlEncode = TextUtils.htmlEncode(str.toLowerCase(Locale.US));
        return "package://" + URI_HOST + "/?" + htmlEncode;
    }

    public AppIconRequestHandler(Context context) {
        this.mUtil = PackageUtil.getInstance(context);
        this.mCache = new AppIconCache(context);
    }

    @Override 
    public boolean canHandleRequest(Request request) {
        return "package".equals(request.uri.getScheme());
    }

    @Override
    public Result load(Request request) throws IOException {
        try {
            String keywordFromUri = getKeywordFromUri(request.uri);
            String str = TAG;
            Log.v(str, "Looking for icon matching '" + keywordFromUri + "'");
            AppIconCache.CacheEntry find = this.mCache.find(keywordFromUri);
            if (find == null) {
                Drawable findMatchingDrawable = this.mUtil.findMatchingDrawable(keywordFromUri);
                if (findMatchingDrawable == null) {
                    String str2 = TAG;
                    Log.v(str2, "...'" + keywordFromUri + "' not found.");
                    return null;
                }
                String str3 = TAG;
                Log.v(str3, "...'" + keywordFromUri + "' found in package list.");
                Bitmap drawableToBitmap = AsyncIconTask.drawableToBitmap(findMatchingDrawable);
                this.mCache.store(keywordFromUri, drawableToBitmap);
                return new Result(drawableToBitmap, Picasso.LoadedFrom.NETWORK);
            }
            String str4 = TAG;
            Log.v(str4, "...'" + keywordFromUri + "' found in cache.");
            Bitmap drawableToBitmap2 = AsyncIconTask.drawableToBitmap(find.getDrawable());
            if (find.isExpired()) {
                loadAsync(keywordFromUri);
            }
            return new Result(drawableToBitmap2, Picasso.LoadedFrom.DISK);
        } catch (Exception e) {
            Log.e(TAG, "Error in AppIconRequestHandler", e);
            return null;
        }
    }

    private String getKeywordFromUri(Uri uri) {
        return uri.getQuery();
    }


    private void loadAsync(String str) {
        new AsyncIconTask(this.mUtil, this.mCache).execute(str);
    }
}
