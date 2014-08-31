package com.sharebravo.bravo.control.activity;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sharebravo.bravo.R;
import com.sharebravo.bravo.sdk.log.AIOLog;
import com.sharebravo.bravo.utils.BravoConstant;
import com.sharebravo.bravo.utils.BravoSharePrefs;

public class ActivitySplash extends VisvaAbstractActivity {

    private static int   TIME_SHOW_SPLASH = 3000;
    private LinearLayout mLoginLayout;
    private LinearLayout mRegisterLayout;
    private LinearLayout mLoginRegisterLayout;
    private TextView     mTextFlashIntro;

    @Override
    public int contentView() {
        return R.layout.activity_splash;
    }

    @Override
    public void onCreate() {
        mLoginRegisterLayout = (LinearLayout) findViewById(R.id.layout_login_regiter);
        mTextFlashIntro = (TextView) findViewById(R.id.text_flash_intro);
        mLoginLayout = (LinearLayout) findViewById(R.id.layout_login);
        mLoginLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AIOLog.d("mLoginLayout");
                Intent loginIntent = new Intent(ActivitySplash.this, ActivityLogin_Register.class);
                loginIntent.putExtra(BravoConstant.ACCESS_TYPE, BravoConstant.FRAGMENT_LOGIN_ID);
                startActivity(loginIntent);
                //finish();
            }
        });
        mRegisterLayout = (LinearLayout) findViewById(R.id.layout_register);
        mRegisterLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AIOLog.d("mRegisterLayout");
                Intent loginIntent = new Intent(ActivitySplash.this, ActivityLogin_Register.class);
                loginIntent.putExtra(BravoConstant.ACCESS_TYPE, BravoConstant.FRAGMENT_REGISTER_ID);
                startActivity(loginIntent);
                //finish();
            }
        });

        final int registerType = BravoSharePrefs.getInstance(this).getIntValue(BravoConstant.PREF_KEY_SESSION_REGISTER_TYPE);
        /* create time handle to show flash screen */
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                AIOLog.d("out of post delayed time");
                // if (registerType >= 1) {
                // Intent intent = new Intent(ActivitySplash.this, HomeActivity.class);
                // startActivity(intent);
                // finish();
                // } else {
                mLoginRegisterLayout.setVisibility(View.VISIBLE);
                mTextFlashIntro.setVisibility(View.VISIBLE);
                // }
            }
        }, TIME_SHOW_SPLASH);
    }
}
