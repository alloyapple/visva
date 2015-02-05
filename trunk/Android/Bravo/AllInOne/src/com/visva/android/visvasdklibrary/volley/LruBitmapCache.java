package com.visva.android.visvasdklibrary.volley;

import com.android.volley.toolbox.ImageLoader.ImageCache;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * control bitmap while using volley.
 * If bitmap is existed, volley will take it from cache.
 * else volley will take it from the url
 * 
 * @author kieu.thang
 * 
 */
public class LruBitmapCache extends LruCache<String, Bitmap> implements ImageCache {

    /**
     * get default cache size by get max memory of device
     * the cache size is memory limit / 8
     * 
     * @param null
     * 
     * @return Integer the size of cache
     */
    public static int getDefaultLruCacheSize() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;

        return cacheSize;
    }

    public LruBitmapCache() {
        this(getDefaultLruCacheSize());
    }

    public LruBitmapCache(int sizeInKiloBytes) {
        super(sizeInKiloBytes);
    }

    @Override
    protected int sizeOf(String key, Bitmap value) {
        return value.getRowBytes() * value.getHeight() / 1024;
    }

    @Override
    public Bitmap getBitmap(String url) {
        return get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        put(url, bitmap);
    }
}