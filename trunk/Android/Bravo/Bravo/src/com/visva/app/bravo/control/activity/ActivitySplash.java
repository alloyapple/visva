package com.visva.app.bravo.control.activity;

import com.visva.app.bravo.R;
import com.visva.app.bravo.utils.BravoConstant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class ActivitySplash extends Activity {
    private LinearLayout mLoginLayout;
    private LinearLayout mRegisterLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mLoginLayout = (LinearLayout) findViewById(R.id.layout_login);
        mLoginLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(ActivitySplash.this, ActivityLogin_Register.class);
                loginIntent.putExtra(BravoConstant.ACCESS_TYPE, BravoConstant.ACCESS_BY_LOGIN);
                startActivity(loginIntent);

            }
        });
        mRegisterLayout = (LinearLayout) findViewById(R.id.layout_register);
        mRegisterLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(ActivitySplash.this, ActivityLogin_Register.class);
                loginIntent.putExtra(BravoConstant.ACCESS_TYPE, BravoConstant.ACCESS_BY_REGISTER);
                startActivity(loginIntent);

            }
        });
    }
}
