package com.visva.voicerecorder.note;

import android.app.Activity;
import android.os.Bundle;

import com.visva.voicerecorder.R;
import com.visva.voicerecorder.view.widget.SlideToUnlock;

public class ActivityReminder extends Activity implements SlideToUnlock.OnUnlockListener {

    private SlideToUnlock slideToUnlock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        slideToUnlock = (SlideToUnlock) findViewById(R.id.slidetounlock);
        slideToUnlock.setOnUnlockListener(this);
    }

    @Override
    public void onUnlock() {
        finish();
    }
}
