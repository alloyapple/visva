package com.visva.app.bravo.control.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.view.WindowManager;

import com.visva.app.bravo.MyApplication;
import com.visva.app.bravo.R;

public class HomeActivity extends VisvaAbstractActivity {
    @Override
    public int contentView() {
        // TODO Auto-generated method stub
        return R.layout.activity_home;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onCreate() {
        MyApplication myApp = (MyApplication) getApplication();
        myApp._homeActivity = this;

        if (Build.VERSION.SDK_INT >= 11)
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);

        // setup actionbar
    }

    protected void onActivityResult(int req, int res, Intent data) {
        super.onActivityResult(req, res, data);

    }

    public void onClick(View v) {
        switch (v.getId()) {

        default:
            break;
        }
    }

    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    // Analysis
    @Override
    protected void onStart() {
        super.onStart();
    }
}
