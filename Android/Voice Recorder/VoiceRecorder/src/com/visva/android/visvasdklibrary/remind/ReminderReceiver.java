package com.visva.android.visvasdklibrary.remind;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.visva.voicerecorder.log.AIOLog;
import com.visva.voicerecorder.utils.StringUtility;

/**
 * 
 * @author kane
 * 
 */
public class ReminderReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) {
            AIOLog.e(AIOConstant.TAG, "intent reminder is null");
            return;
        }
        String action = intent.getAction();
        AIOLog.d(AIOConstant.TAG, "action=>" + action);
        if(StringUtility.isEmpty(action)){
            AIOLog.e(AIOConstant.TAG, "action is null");
        }
        if (action.equals(Intent.ACTION_BOOT_COMPLETED)) {
            AIOLog.d(AIOConstant.TAG, "Boot Intent received");

            // Restore jobs
            ReminderProvider.getInstance(context).restoreScheduler();

        } else if (action.equals(ReminderConstant.ACTION_ON_REMINDER)) {
            AIOLog.d(AIOConstant.TAG, "ACTION_ON_REMINDER Intent received");
            Intent serviceIntent = new Intent(ReminderConstant.ACTION_ON_REMINDER);
            serviceIntent.setClass(context, AIOService.class);
            serviceIntent.setData(intent.getData());
            serviceIntent.putExtras(intent.getExtras());

            context.startService(serviceIntent);
        } else if (action.equals(ReminderConstant.ACTION_CLEAR_DATA)) {
            AIOLog.d(AIOConstant.TAG, "REQUEST_TO_CLEAR_USER_DATA: delete all schedule");
            try {
                ReminderProvider.getInstance(context).deleteAllReminders();
            } catch (Exception e) {
                AIOLog.e(AIOConstant.TAG, "failed to delete db");
            }
        }
    }
}
