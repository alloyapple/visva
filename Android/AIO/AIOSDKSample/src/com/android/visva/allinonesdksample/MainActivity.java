package com.android.visva.allinonesdksample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.android.visva.allinonesdksample.provider.ConnectionProviderSampleActivity;
import com.android.visva.allinonesdksample.provider.LocationProviderSampleActivity;
import com.android.visva.allinonesdksample.provider.ReminderProviderSampleActivity;
import com.android.visva.allinonesdksample.provider.VolleyProviderSampleActivity;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    
    public void onClickConnectionProviderSample(View v){
    	Intent intent = new Intent(this, ConnectionProviderSampleActivity.class);
    	startActivity(intent);
    }
    
    public void onClickLocationProviderSample(View v){
    	Intent intent = new Intent(this, LocationProviderSampleActivity.class);
    	startActivity(intent);
    }
    
    public void onClickVolleyProviderSample(View v){
    	Intent intent = new Intent(this, VolleyProviderSampleActivity.class);
    	startActivity(intent);
    }
    
    public void onClickReminderProviderSample(View v){
        Intent intent = new Intent(this, ReminderProviderSampleActivity.class);
        startActivity(intent);
    }
}
