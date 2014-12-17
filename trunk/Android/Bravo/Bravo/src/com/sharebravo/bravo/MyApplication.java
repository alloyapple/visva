package com.sharebravo.bravo;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.facebook.SessionDefaultAudience;
import com.sharebravo.bravo.control.activity.HomeActivity;
import com.sharebravo.bravo.sdk.log.AIOLog;
import com.sharebravo.bravo.sdk.volley.LruBitmapCache;
import com.sharebravo.bravo.utils.BravoSharePrefs;
import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.SimpleFacebookConfiguration;
import com.sromku.simple.fb.utils.Logger;

public class MyApplication extends Application {
    private static MyApplication mInstance;
    private static final String  TAG = MyApplication.class .getSimpleName();
    private static final String APP_NAMESPACE = "firsttestappdemo";
    
    public HomeActivity          _homeActivity;
    private BravoSharePrefs      mBravoSharePrefs;
    private RequestQueue         mRequestQueue;
    private ImageLoader          mImageLoader;

    public static MyApplication getInstance() {
        if (mInstance == null) {
            mInstance = new MyApplication();
        }
        return mInstance;
    }

    public Context getAndroidContext() {
        return this;
    }

    @Override
    public void onCreate() {
        AIOLog.d("onCreate");
        super.onCreate();
        mInstance = this;
        // set log to true
        Logger.DEBUG_WITH_STACKTRACE = true;

        // initialize facebook configuration
        Permission[] permissions = new Permission[] { 
                Permission.PUBLIC_PROFILE,
                Permission.PUBLISH_ACTION
                };

        SimpleFacebookConfiguration configuration = new SimpleFacebookConfiguration.Builder()
            .setAppId(getApplicationContext().getResources().getString(R.string.fb_app_id))
            .setNamespace(APP_NAMESPACE)
            .setPermissions(permissions)
            .setDefaultAudience(SessionDefaultAudience.FRIENDS)
            .setAskForAllPermissionsAtOnce(false)
            .build();

        SimpleFacebook.setConfiguration(configuration);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public BravoSharePrefs getBravoSharePrefs() {
        if (mBravoSharePrefs == null) {
            mBravoSharePrefs = new BravoSharePrefs(getAndroidContext());
        }
        return mBravoSharePrefs;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
