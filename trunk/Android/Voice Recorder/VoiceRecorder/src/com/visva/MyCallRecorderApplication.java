package com.visva;

import java.util.ArrayList;

import android.app.Application;
import android.content.Context;

import com.visva.voicerecorder.record.RecordingManager;
import com.visva.voicerecorder.record.RecordingSession;
import com.visva.voicerecorder.utils.ProgramHelper;
import com.visva.voicerecorder.view.activity.ActivityHome;

public class MyCallRecorderApplication extends Application {
    private static MyCallRecorderApplication mInstance;
    private static RecordingManager          mRecordingManager;
    private static ProgramHelper             helper;
    private static ActivityHome              activity;

    public static MyCallRecorderApplication getInstance() {
        if (mInstance == null) {
            mInstance = new MyCallRecorderApplication();
        }
        return mInstance;
    }

    public Context getAndroidContext() {
        return this;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public ProgramHelper getProgramHelper() {
        if (helper == null) {
            helper = new ProgramHelper();
        }
        helper.prepare();
        return helper;
    }

    public RecordingManager getRecordManager(Context context,ArrayList<RecordingSession> sessions) {
        if (mRecordingManager == null) {
            mRecordingManager = new RecordingManager(context, sessions);
        }
        return mRecordingManager;
    }

    public void stopActivity() {
        if (activity == null)
            return;
        activity.finish();
    }

    public void setActivity(ActivityHome activity) {
        MyCallRecorderApplication.activity = activity;
    }
}
