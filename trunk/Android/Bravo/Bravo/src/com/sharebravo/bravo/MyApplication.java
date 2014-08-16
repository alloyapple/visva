package com.sharebravo.bravo;

import android.app.Application;
import android.content.Context;

import com.sharebravo.bravo.control.activity.HomeActivity;
import com.sharebravo.bravo.sdk.log.AIOLog;
import com.sharebravo.bravo.utils.BravoSharePrefs;

public class MyApplication extends Application {
    private static MyApplication mInstance;
    public HomeActivity          _homeActivity;
    private BravoSharePrefs      mBravoSharePrefs;

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

}
