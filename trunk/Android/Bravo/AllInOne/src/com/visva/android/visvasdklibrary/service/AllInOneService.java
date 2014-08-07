package com.visva.android.visvasdklibrary.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.visva.android.visvasdklibrary.log.AllInOneLog;

public class AllInOneService extends Service{

    @Override
    public IBinder onBind(Intent intent) {
        AllInOneLog.d(" intent="+intent);
        return mBinder;
    }

    @Override
    public void onCreate() {
        AllInOneLog.d(" onCreate=");
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        AllInOneLog.d(" onDestroy=");
        super.onDestroy();
    }

    @Override
    public void onRebind(Intent intent) {
        AllInOneLog.d(" intent="+intent);
        super.onRebind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        AllInOneLog.d(" intent="+intent);
        return super.onStartCommand(intent, START_NOT_STICKY, startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        AllInOneLog.d(" intent="+intent);
        return super.onUnbind(intent);
    }

    public class AllInOneServiceBinder extends Binder {
        public AllInOneService getService() {
            return AllInOneService.this;
        }
    }

    private final IBinder mBinder = new AllInOneServiceBinder();

    public void initialize(Object listener) throws Exception {
    }

    public int start(int interfaceType) {
        return interfaceType;
    }
}
