package com.android.visva.allinonesdksample.provider;

import android.app.Activity;
import android.app.AlarmManager;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.visva.allinonesdksample.AIOSampleConstant;
import com.android.visva.allinonesdksample.R;
import com.visva.android.visvasdklibrary.log.AIOLog;
import com.visva.android.visvasdklibrary.provider.ReminderProvider;
import com.visva.android.visvasdklibrary.remind.IReminder;
import com.visva.android.visvasdklibrary.remind.ReminderItem;

public class ReminderProviderSampleActivity extends Activity implements IReminder {
    private TextView mTextView;
    private int      timeToCountDown = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_provider_sample);
        mTextView = (TextView) findViewById(R.id.textView1);
    }
    
    @Override
    public boolean onRemind(Context context, ReminderItem reminderItem) {
        Toast.makeText(context, "onRemind parent", Toast.LENGTH_SHORT).show();
        AIOLog.d(AIOSampleConstant.TAG, "reminderItem.className=>" + reminderItem.className);
        AIOLog.d(AIOSampleConstant.TAG, "reminderItem.id=>" + reminderItem.id);
        AIOLog.d(AIOSampleConstant.TAG, "reminderItem.intervalMillis=>" + reminderItem.intervalMillis);
        AIOLog.d(AIOSampleConstant.TAG, "reminderItem.repeatCount=>" + reminderItem.repeatCount);
        AIOLog.d(AIOSampleConstant.TAG, "reminderItem.triggerAtMillis=>" + reminderItem.triggerAtMillis);
        return false;
    }

    public void onClickAddReminder(View v) {
        timeToCountDown = 10;
        long timeToReminder = System.currentTimeMillis() + timeToCountDown * 1000;
        ReminderProvider.getInstance(this).addReminder(ReminderProviderSampleActivity.class, ReminderProviderSampleActivity.class.getName(), timeToReminder, AlarmManager.INTERVAL_DAY, 1);
        new CountDownTimer(timeToCountDown * 1000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                // TODO Auto-generated method stub
                timeToCountDown--;
                mTextView.setText(timeToCountDown + "");
            }

            @Override
            public void onFinish() {
                // TODO Auto-generated method stub

            }
        }.start();
    }
}
