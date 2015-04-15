package com.visva.android.visvasdklibrary.remind;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

import com.visva.voicerecorder.log.AIOLog;

/**
 * ReminderProvider supports apis which can remind you with the time and className are specified
 * 
 * @author kane
 * 
 */
@SuppressLint("NewApi")
public class ReminderProvider {
    /* singleton class */
    private static final String     TAG                     = AIOConstant.TAG;
    public static final String      ACTION_DATA_ID          = "id";
    public static final String      ACTION_DATA_TRIGGER     = "triggerAtMillis";
    public static final String      ACTION_DATA_INTERVAL    = "intervalMillis";
    public static final String      ACTION_DATA_COUNT       = "count";
    public static final String      ACTION_DATA_REPEAT_MODE = "repeat_mode";
    public static final String      ACTION_DATA_ROW_ID      = "row_id";

    private static ReminderProvider mInstance;
    private static Context          mContext;
    private ReminderDBHelper        mReminderDBHelper;

    /**
     * Connector constructor
     * 
     * @param context
     */
    public ReminderProvider(Context context) {
        super();
        mContext = context;
        mReminderDBHelper = ReminderDBHelper.getInstance(context);

    }

    /**
     * get instance of scheduler singleton class
     * 
     * @param context
     * @return instance
     */
    public static synchronized ReminderProvider getInstance(Context context) {
        if (mInstance == null)
            mInstance = new ReminderProvider(context);
        mContext = context;
        return mInstance;
    }

    // =======================CORE FUNCTIOINS================================
    public void restoreScheduler() {
        runOnAsyncTask(new RunOnAsyncTask() {
            @Override
            public void doInBackground() {
                List<ReminderItem> listReminders = mReminderDBHelper.getAllReminders();
                for (ReminderItem reminderItem : listReminders) {
                    addReminder(reminderItem.className, reminderItem.id, reminderItem.triggerAtMillis, reminderItem.intervalMillis,
                            reminderItem.repeatCount, reminderItem.isRepeatMode);
                }
            }
        });
    }

    private static void runOnAsyncTask(final RunOnAsyncTask runOnAsyncTask) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... arg0) {
                runOnAsyncTask.doInBackground();
                return null;
            }
        }.execute();
    }

    public interface RunOnAsyncTask {
        public void doInBackground();
    }

    public static void runOnUIThread(Runnable r) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(r);
    }

    /**
     * Make a reminder with the time is specified triggerAtMillis.
     * At the remind time, it will return data to reminder class: className
     * 
     * @param className
     *            : must be exist class name,cannot be null
     * @param id
     * @param triggerAtMillis
     * @param intervalMillis
     * @param repeatCount
     */
    private void addReminder(String className, String id, long triggerAtMillis, long intervalMillis, int repeatCount, boolean isRepeatingMode) {

        AIOLog.d(TAG, "addSchedule(0): " + className + ":" + id + ":" + triggerAtMillis + ":" + intervalMillis + ":" + repeatCount);
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(ReminderConstant.SCHEME);
        builder.authority(ReminderConstant.AUTHORITY);
        builder.appendPath(className);
        if (id != null)
            builder.appendPath(id);

        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(ReminderConstant.ACTION_ON_REMINDER);
        Uri dataName = builder.build();
        intent.setData(dataName);
        intent.putExtra(ACTION_DATA_ID, id);
        intent.putExtra(ACTION_DATA_TRIGGER, triggerAtMillis);
        intent.putExtra(ACTION_DATA_INTERVAL, intervalMillis);
        intent.putExtra(ACTION_DATA_COUNT, repeatCount);
        intent.putExtra(ACTION_DATA_REPEAT_MODE, isRepeatingMode);

        long rowId = mReminderDBHelper.addJob(className, id, triggerAtMillis, intervalMillis, repeatCount, isRepeatingMode);
        intent.putExtra(ACTION_DATA_ROW_ID, rowId);

        // update previous intent(equal Key : DATA(className, id) see filterEquals(Intent))
        PendingIntent pIntent = PendingIntent.getBroadcast(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        try {
            if (isRepeatingMode) {
                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, triggerAtMillis, intervalMillis, pIntent);
            } else {
                //                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                if (triggerAtMillis < 0)
                    alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + intervalMillis, pIntent);
                else
                    alarmManager.set(AlarmManager.RTC_WAKEUP, triggerAtMillis, pIntent);
                //                } else {
                //
                //                    if (triggerAtMillis < 0)
                //                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + intervalMillis, pIntent);
                //                    else
                //                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, pIntent);
                //                }
            }
        } catch (SecurityException e) {
            // If there's too many registered alarms, this code would throws SecurityException
            AIOLog.d(TAG, "Unable to add schedule to AlarmManager : " + e.toString());
        }
    }

    public void addRepeatReminder(Class<?> scheduleClass, long triggerAtMillis, long intervalMillis) {
        String clazzName = scheduleClass.getName();
        addReminder(clazzName, null, triggerAtMillis, intervalMillis, 0, true);
    }

    public void addRepeatReminder(Class<?> scheduleClass, String id, long triggerAtMillis, long intervalMillis) {
        String clazzName = scheduleClass.getName();
        addReminder(clazzName, id, triggerAtMillis, intervalMillis, 0, true);
    }

    /**
     * Make a reminder with the time is specified triggerAtMillis.
     * At the remind time, it will return data to reminder class: className
     * 
     * @param className
     *            : must be exist class name,cannot be null
     * @param id
     * @param triggerAtMillis
     * @param intervalMillis
     * @param repeatCount
     */
    public void addReminder(Class<?> scheduleClass, String id, long triggerAtMillis, long intervalMillis, int repeatCount) {
        String clazzName = scheduleClass.getName();
        addReminder(clazzName, id, triggerAtMillis, intervalMillis, repeatCount, false);
    }

    public void addReminder(Class<?> reminderClass, long triggerAtMillis, long intervalMillis, int repeatCount) {
        addReminder(reminderClass, null, triggerAtMillis, intervalMillis, repeatCount);
    }

    public void addReminder(Class<?> scheduleClass, long intervalMillis, int repeatCount) {
        addReminder(scheduleClass, null, -1, intervalMillis, repeatCount);
    }

    public void addReminder(Class<?> scheduleClass, String id, long intervalMillis, int repeatCount) {
        addReminder(scheduleClass, id, -1, intervalMillis, repeatCount);
    }

    public void deleteReminder(ReminderItem alarmJob) {
        deleteReminder(alarmJob.className, alarmJob.id);
    }

    public void deleteReminder(Class<?> scheduleClass) {
        deleteReminder(scheduleClass, null);
    }

    public void deleteReminder(Class<?> scheduleClass, String id) {
        String className = scheduleClass.getName();
        deleteReminder(className, id);
    }

    public void deleteReminder(String className, String id) {
        AIOLog.d(TAG, "deleteSchedule: ");

        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(ReminderConstant.ACTION_ON_REMINDER);
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(ReminderConstant.SCHEME);
        builder.authority(ReminderConstant.AUTHORITY);
        builder.appendPath(className);
        if (id != null)
            builder.appendPath(id);

        Uri dataName = builder.build();
        intent.setData(dataName);
        PendingIntent pIntent = PendingIntent.getBroadcast(mContext, 0, intent, 0);

        alarmManager.cancel(pIntent);

        deleteReminderInDatabase(className, id);
    }

    public void deleteAllReminders() {
        List<ReminderItem> alarmJobs = mReminderDBHelper.getAllReminders();

        for (ReminderItem alarmJob : alarmJobs) {
            deleteReminder(alarmJob);
        }
    }

    public boolean deleteReminderInDatabase(String className, String id) {

        if (mReminderDBHelper.deleteJob(className, id) > 0) {
            AIOLog.d(TAG, "Delete a job to database");
            return true;
        } else {
            AIOLog.e(TAG, "Fail to Delete a job to database");
            return false;
        }
    }

    public boolean deleteScheduleInDatabase(long rowId) {

        if (mReminderDBHelper.deleteJob(rowId) > 0) {
            AIOLog.d(TAG, "Delete a job to database");
            return true;
        } else {
            AIOLog.d(TAG, "The Job is not exists");
            return false;
        }
    }
}
