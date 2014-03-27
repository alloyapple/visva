package com.visva.android.flashlight.activities;

import java.util.Timer;
import java.util.TimerTask;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.visva.android.flashlight.R;
import com.visva.android.flashlight.common.Session;
import com.visva.android.flashlight.utilities.ScreenUtilities;

public class PoliceLightActivity extends BaseActivity {
    private LinearLayout _policeLightLayout;
    private int __currentColorIndex = 0;
    private TextView _tvLabel;
    private Button _btnFeatures;
    private AdView layoutAds;

    private static final int[] _policeColor = new int[] { Color.RED, Color.BLACK, Color.RED, Color.BLACK, Color.RED,
            Color.BLACK, Color.BLACK, Color.BLUE, Color.BLACK, Color.BLUE, Color.BLACK, Color.BLUE, Color.BLACK,
            Color.BLACK, Color.LTGRAY, Color.BLACK, Color.LTGRAY, Color.BLACK, Color.LTGRAY, Color.BLACK };

    /** Called when the activity is first created. */
    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.policelight);

        ScreenUtilities.setBrightness(this, Session.getHighestScreenBrightness());

        this.mapUIElement();
        this.initializeTimer();

        if (_preferenceUtilities.isShowDefaultLightSource() || _preferenceUtilities.isShowPoliceLightLabel()) {
            flip(_tvLabel);
            _preferenceUtilities.setShowPoliceLabel(false);
            _preferenceUtilities.setShowDefaultLightSource(false);
        } else {
            _tvLabel.setVisibility(View.GONE);
        }
        refreshAdsMob();
        layoutAds = (AdView) this.findViewById(R.id.main_adView);
        AdRequest adRequest = new AdRequest();
        adRequest.setTesting(true);
        layoutAds.loadAd(adRequest);
        layoutAds.bringToFront();
    }

    private void refreshAdsMob() {
        new CountDownTimer(12000, 20000) {

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @SuppressWarnings("deprecation")
            @Override
            public void onFinish() {
                AdRequest adRequest = new AdRequest();
                adRequest.setTesting(true);
                layoutAds.refreshDrawableState();
                layoutAds.loadAd(adRequest);
                layoutAds.invalidate();
                layoutAds.bringToFront();
                refreshAdsMob();
            }
        }.start();
    }

    private void mapUIElement() {
        this._policeLightLayout = (LinearLayout) this.findViewById(R.id.policeLightLayout);
        this._tvLabel = (TextView) this.findViewById(R.id.tvLabel);
        this._btnFeatures = (Button) this.findViewById(R.id.btnFeatures);
        this._btnFeatures.setOnClickListener(_btnFeature_Click);
    }

    private void initializeTimer() {
        Timer __changeLightTimer = new Timer();
        __changeLightTimer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                executeTimerTick();

            }
        }, 0, 100);
    }

    private void executeTimerTick() {
        // timer_Tick.run();
        this.runOnUiThread(this.timer_Tick);
    }

    private Runnable timer_Tick = new Runnable() {

        @Override
        public void run() {
            if (__currentColorIndex >= _policeColor.length)
                __currentColorIndex = 0;

            _policeLightLayout.setBackgroundColor(_policeColor[__currentColorIndex]);
            __currentColorIndex += 1;
        }
    };
}
