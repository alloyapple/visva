package com.visva.android.visvasdklibrary.remind;

import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;

import com.visva.voicerecorder.log.AIOLog;

/**
 * 
 * @author kane
 * 
 */
public class AIOService extends Service {
    private static final int        SELP_STOP_TIMEOUT     = 1000 * 20;
    private Timer                   mTimer;
    private HashMap<String, Object> mInstanceMap = new HashMap<String, Object>();

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onDestroy() {
        AIOLog.v(AIOConstant.TAG, "AIOService onDestroy");
        mInstanceMap.clear();
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        AIOLog.v(AIOConstant.TAG, "AIOService start service.");

        /**
         * self stop timer
         */
        if (mTimer != null) {
            AIOLog.v(AIOConstant.TAG, "selft stop timer is canced!");
            mTimer.cancel();
        }

        // Terminated service after timeout
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                stopSelf();
            }
        };
        mTimer = new Timer();
        mTimer.schedule(timerTask, SELP_STOP_TIMEOUT);

        if (intent != null) {

            String action = intent.getAction();

            AIOLog.d(AIOConstant.TAG, "Action : " + action);
            if (action.equals(ReminderConstant.ACTION_ON_REMINDER)) {
                onRemind(intent);
                AIOLog.d(AIOConstant.TAG, "called onSchedule Action : " + intent.getData());
            } else {
            }

        } else {
            AIOLog.e(AIOConstant.TAG, "intent is null!");
        }

        // If we get killed, not restart
        return START_NOT_STICKY;
    }

    private boolean onRemind(Intent intent) {
        Uri uri = intent.getData();
        if (uri != null) {
            List<String> path = uri.getPathSegments();
            String className = path.get(0);
            if (className != null) {
                try {
                    IReminder clazzObject = null;
                    Object target = null;
                    target = mInstanceMap.get(className);
                    if (target == null) {
                        // for use existing instance
                        Class<?> clazz = this.getClassLoader().loadClass(className);
                        if (clazz != null) {
                            target = clazz.newInstance();
                            clazzObject = (IReminder) target;
                            mInstanceMap.put(className, target);

                        }
                    } else {
                        clazzObject = (IReminder) target;
                    }

                    String id = null;
                    long intervalMillis = -1;
                    long triggerAtMillis = -1;
                    int count = -1;
                    long rowId = -1;
                    boolean repeatMode = false;
                    Bundle extras = intent.getExtras();
                    if (extras != null) {
                        id = extras.getString(ReminderProvider.ACTION_DATA_ID);
                        intervalMillis = extras.getLong(ReminderProvider.ACTION_DATA_INTERVAL);
                        triggerAtMillis = extras.getLong(ReminderProvider.ACTION_DATA_TRIGGER);
                        count = extras.getInt(ReminderProvider.ACTION_DATA_COUNT);
                        rowId = extras.getLong(ReminderProvider.ACTION_DATA_ROW_ID);
                        repeatMode = extras.getBoolean(ReminderProvider.ACTION_DATA_REPEAT_MODE);
                    }

                    if (clazzObject != null) {
                        ReminderItem alramjob = new ReminderItem(className, id, triggerAtMillis, intervalMillis, count, repeatMode);
                        AIOLog.d(AIOConstant.TAG, "AlarmJob id: " + id + ", interval : " + intervalMillis + ", trigger : " + triggerAtMillis + ", repeatMode:" + repeatMode);
                        clazzObject.onRemind(this, alramjob);
                    }
                    else {
                        AIOLog.e(AIOConstant.TAG, "Fail to job scheduler, clazzObject is null" + uri.toString());
                        return false;
                    }

                    if (repeatMode == false && rowId > -1) // if not repeat mode and rowId exists, remove the job
                        ReminderProvider.getInstance(this).deleteScheduleInDatabase(rowId);

                    return true;

                } catch (ClassNotFoundException e) {
                    AIOLog.asserting(e, " ClassNotFoundException onReminder");
                } catch (InstantiationException e) {
                    AIOLog.asserting(e, "InstantiationException onReminder");
                } catch (IllegalAccessException e) {
                    AIOLog.asserting(e, "IllegalAccessException onReminder");
                } catch (Exception e) {
                    AIOLog.asserting(e, "Exception onReminder");
                }
            }

        } else {
            AIOLog.e(AIOConstant.TAG, "Fail to job scheduler");
            return false;
        }
        return false;
    }
}
