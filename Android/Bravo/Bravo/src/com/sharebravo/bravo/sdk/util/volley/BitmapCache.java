package com.sharebravo.bravo.sdk.util.volley;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.Log;

import com.android.volley.toolbox.ImageLoader.ImageCache;

public class BitmapCache extends LruCache<String, Bitmap> implements ImageCache {
    private static final String TAG = "RelatedContents's BitmapCache";
    
    public static int getDefaultLruCacheSize() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 16;

        return cacheSize;
    }

    public BitmapCache() {
        this(getDefaultLruCacheSize());
    }

    public BitmapCache(int sizeInKiloBytes) {
        super(sizeInKiloBytes);
    }

    @Override
    protected int sizeOf(String key, Bitmap value) {
        return value.getRowBytes() * value.getHeight() / 1024;
    }

    @Override
    public Bitmap getBitmap(String url) {
        Bitmap result = get(url);
        if (result != null) {
            Log.d(TAG, "Hit bitmap Cache");
        }
        return result;
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        put(url, bitmap);
    }
}