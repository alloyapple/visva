package com.samsung.android.alwayssocial.app;

import java.util.HashMap;

import com.samsung.android.alwayssocial.constant.GlobalConstant;
import com.samsung.android.alwayssocial.database.prefs.AlwaysSharePrefs;
import com.samsung.android.alwayssocial.object.SocialUserObject;
import com.samsung.android.alwayssocial.servermanager.AlwaysSocialManager;
import com.samsung.android.alwayssocial.service.IAlwaysService;

import android.app.Application;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

public class AlwaysSocialAppImpl extends Application {

    private static final String TAG = "AlwaysSocialAppImpl";
    private static AlwaysSocialAppImpl mInstance;
    private HashMap<String, SocialUserObject> mLoggedInUsers = new HashMap<String, SocialUserObject>();
    private AlwaysSharePrefs mAlwaysSharePrefs;

    public static AlwaysSocialAppImpl getInstance()
    {
        if (mInstance == null) {
            mInstance = new AlwaysSocialAppImpl();
        }
        return mInstance;
    }

    public Context getAndroidContext() {
        return this;
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");
        super.onCreate();
        mInstance = this;
        initAlwaysManager();
        doBindAlwaysService();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        unbindService(mServiceConnection);
    }

    private void initAlwaysManager() {
        AlwaysSocialManager.getInstance().initializeSocialMap();
    }

    public AlwaysSharePrefs getAlwaysSharePrefs()
    {
        if (mAlwaysSharePrefs == null) {
            mAlwaysSharePrefs = new AlwaysSharePrefs(getAndroidContext());
        }
        return mAlwaysSharePrefs;
    }

    private void doBindAlwaysService()
    {
        if (mAlwaysBindService == null)
        {
            Log.d(TAG, "Bind Service");
            Intent i = new Intent();
            i.setAction("com.samsung.android.alwayssocial.service.AlwaysService.BIND_ACTION");
            bindService(i, mServiceConnection, Service.BIND_AUTO_CREATE);
        }
    }

    /* **********************TEST BIND SERVICE ********************************** */
    private IAlwaysService mAlwaysBindService = null;
    ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mAlwaysBindService = null;
            Log.d(TAG, "Binding --- Service is disconnected");

        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mAlwaysBindService = IAlwaysService.Stub.asInterface((IBinder) service);
            Log.d(TAG, "Binding is done - Service is connected");
        }
    };

    public IAlwaysService getAlwaysBindService()
    {
        return mAlwaysBindService;
    }

    public SocialUserObject getLoggedInUser(String socialTye) {
        SocialUserObject user = mLoggedInUsers.get(socialTye);
        if (user == null)
        {
            // try to get in SharePref
            user = new SocialUserObject();
            user.mId = getAlwaysSharePrefs().getStringValue(GlobalConstant.PREF_KEY_USER_ID + socialTye);
            if (user.mId == "") {
                return null;
            }

            user.mName = getAlwaysSharePrefs().getStringValue(GlobalConstant.PREF_KEY_USER_NAME + socialTye);
            user.mImageUrl = getAlwaysSharePrefs().getStringValue(GlobalConstant.PREF_KEY_USER_IMAGE_URL + socialTye);

        }
        return user;
    }

    public void setLoggedInUser(String socialTye, SocialUserObject user) {
        // set to sharedPref
        if (user != null) {
            getAlwaysSharePrefs().putStringValue(GlobalConstant.PREF_KEY_USER_ID + socialTye, user.mId);

            Log.d(TAG, getAlwaysSharePrefs().getStringValue(GlobalConstant.PREF_KEY_USER_ID + socialTye));
            getAlwaysSharePrefs().putStringValue(GlobalConstant.PREF_KEY_USER_NAME + socialTye, user.mName);
            getAlwaysSharePrefs().putStringValue(GlobalConstant.PREF_KEY_USER_IMAGE_URL + socialTye, user.mImageUrl);
        } else {
            getAlwaysSharePrefs().remove(GlobalConstant.PREF_KEY_USER_ID + socialTye);
            getAlwaysSharePrefs().remove(GlobalConstant.PREF_KEY_USER_NAME + socialTye);
            getAlwaysSharePrefs().remove(GlobalConstant.PREF_KEY_USER_IMAGE_URL + socialTye);
        }
        mLoggedInUsers.put(socialTye, user);
    }

    public void setViewType(int type) {
        getAlwaysSharePrefs().putIntValue(GlobalConstant.PREF_KEY_VIEW_TYPE, type);
    }

    public int getViewType() {
        return getAlwaysSharePrefs().getIntValue(GlobalConstant.PREF_KEY_VIEW_TYPE);
    }
}
