package com.visva.android.flashlight.activities;

import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.visva.android.flashlight.R;
import com.visva.android.flashlight.common.Key;
import com.visva.android.flashlight.utilities.CameraUtilities;
import com.visva.android.flashlight.utilities.HardwareUtilities;
import com.visva.android.flashlight.utilities.LEDUtilities;
import com.visva.android.flashlight.widget.TinyFlashLightWidgetProvider;
import com.visva.android.flashlight.widget.WidgetBroadcast;

public class LEDLightActivity extends BaseActivity implements Key {
    private Button btnLEDPower;
    private TextView txtBatteryLevel;
    private TextView txtBatteryBackground;
    private TextView _tvContinuousMessage;
    private BaseActivity _mainActivity;
    private Button _btnFeatures;
    private TextView _tvLabel;
    private boolean _defaultState = false;
    private boolean _isLedTurnOn = false;
    private ImageView _imgLEDHigh;
    private ImageView _imgLEDMedium;
    private AdView layoutAds;
    /**
     * brightness mode 0: high 1: medium 2: low
     */
    private static int _brightnessMode;

    private static int _prevBrightnessMode;

    private LinearLayout _ledControlLayout;

    private OnClickListener btnLEDPower_Click = new OnClickListener() {

        @Override
        public void onClick(View v) {
            turnFlashLight();
        }
    };

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ledlight);
        _prevBrightnessMode = 0;
        // get key preference Utilities
        this.mapUIElement();
        this.batteryLevel();

        if (_preferenceUtilities.isShowDefaultLightSource() || _preferenceUtilities.isShowLedLightLabel()) {
            flip(_tvLabel);
            _preferenceUtilities.setShowLedLightLabel(false);
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

    @Override
    protected void onStop() {
        super.onStop();
        // if (_isLedTurnOn) {
        // cancelLed();
        // // CameraUtilities.turnOffFlashLight();
        // }
    }

    @Override
    public void onResume() {
        super.onResume();
        _brightnessMode = _preferenceUtilities.getBrightnessModeLed();
        updateBrightnessUI();
    }

    private void mapUIElement() {
        _mainActivity = this;

        this.btnLEDPower = (Button) this.findViewById(R.id.btnLEDPower);
        this.btnLEDPower.setOnClickListener(this.btnLEDPower_Click);

        this.txtBatteryLevel = (TextView) this.findViewById(R.id.txtBatteryLevel);
        this.txtBatteryBackground = (TextView) this.findViewById(R.id.txtBatteryBackground);
        this._tvLabel = (TextView) this.findViewById(R.id.tvLabel);
        this._tvContinuousMessage = (TextView) this.findViewById(R.id.tvContinuousMessage);
        this._btnFeatures = (Button) this.findViewById(R.id.btnFeatures);
        this._btnFeatures.setOnClickListener(_btnFeature_Click);

        this._ledControlLayout = (LinearLayout) this.findViewById(R.id.ledControl);
        this._ledControlLayout.setOnClickListener(_layoutLEDControl_Click);
        this._imgLEDHigh = (ImageView) this.findViewById(R.id.ledHigh);
        this._imgLEDMedium = (ImageView) this.findViewById(R.id.ledMedium);

        if (!LEDUtilities.isSupported()) {
            this._ledControlLayout.setVisibility(View.GONE);
            _tvContinuousMessage.setVisibility(View.VISIBLE);
        } else {
            _tvContinuousMessage.setVisibility(View.GONE);
        }
        _defaultState = _preferenceUtilities.isLedDefaultState();
        if (_defaultState) {
            turnOnFlashLight();
            _ledActivated = _defaultState;
        }

        _isLedTurnOn = _preferenceUtilities.isLedLightTurnOn();
        if (_isLedTurnOn) {
            turnOnFlashLight();
            _ledActivated = _isLedTurnOn;
        }

    }

    /**
     * Method used to control flash light
     */
    private void turnFlashLight() {
        if (!_ledActivated) {
            turnOnFlashLight();

            if (_preferenceUtilities.isPlaySound()) {
                _soundUtilities.playSwitchInSound();
            }

            _ledActivated = true;
        } else {
            turnOffFlashLight();

            if (_preferenceUtilities.isPlaySound()) {
                _soundUtilities.playSwitchOutSound();
            }

            _ledActivated = false;
            WidgetBroadcast._ledActivated = true;
        }

        if (_preferenceUtilities.isLedVibration()) {
            HardwareUtilities.vibrate(_mainActivity, 50);
        }
        _preferenceUtilities.setLedLightTurnOn(_ledActivated);
    }

    /**
     * Method use to turn on flash light
     */
    private void turnOnFlashLight() {
        if (LEDUtilities.isSupported()) {
            Log.e("turn on flash", "ok1");
            _brightnessMode = _preferenceUtilities.getBrightnessModeLed();
            switch (_brightnessMode) {
            case 0:
                LEDUtilities.setBrightness(LEDUtilities.HIGH_BRIGHTNESS);
                break;
            case 1:
                LEDUtilities.setBrightness(LEDUtilities.MEDIUM_BRIGHTNESS);
                break;
            case 2:
                LEDUtilities.setBrightness(LEDUtilities.LOW_BRIGHTNESS);
                break;
            default:
            }
        } else {
            Log.e("turn on flash", "ok2");
            CameraUtilities.turnOnFlashLight();
        }
        // CameraUtilities.turnOnFlashLight();
        btnLEDPower.setSelected(true);

    }

    /**
     * Method use to turn off flash light
     */
    private void turnOffFlashLight() {
        // CameraUtilities.turnOffFlashLight();
        cancelLed();
        btnLEDPower.setSelected(false);

        if (WidgetBroadcast._ledActivated) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getBaseContext());
            ComponentName thisWidget = new ComponentName(getBaseContext(), TinyFlashLightWidgetProvider.class);
            int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
            Intent intent = new Intent(getBaseContext(), WidgetBroadcast.class);
            intent.setAction("RunWidgetBroadcast");
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), 0, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            try {
                pendingIntent.send();
            } catch (CanceledException e) {
                Log.d("ERRO", "xx = " + e.getMessage());
            }
        }
    }

    /**
     * Computes the battery level by registering a receiver to the intent triggered by a battery status/level change.
     */
    private void batteryLevel() {
        BroadcastReceiver batteryLevelReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                context.unregisterReceiver(this);
                int __rawlevel = intent.getIntExtra("level", -1);
                int __scale = intent.getIntExtra("scale", -1);
                int __level = -1;
                if (__rawlevel >= 0 && __scale > 0) {
                    __level = (__rawlevel * 100) / __scale;
                }
                txtBatteryLevel.setText(__level + "%");

                if (__level <= 50) {
                    txtBatteryBackground.setBackgroundResource(R.drawable.led_battery_yellow_bg);
                } else if (__level <= 20) {
                    txtBatteryBackground.setBackgroundResource(R.drawable.led_battery_red_bg);
                } else {
                    txtBatteryBackground.setBackgroundResource(R.drawable.led_battery_green_bg);
                }

            }
        };
        IntentFilter batteryLevelFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryLevelReceiver, batteryLevelFilter);
    }

    public static void cancelLed() {
        //CameraUtilities.turnOffFlashLight();
        if (LEDUtilities.isSupported()) {
            LEDUtilities.turnOff();
        } else {
            CameraUtilities.turnOffFlashLight();
        }
    }

    protected OnClickListener _layoutLEDControl_Click = new OnClickListener() {

        @Override
        public void onClick(View v) {
            updateBrightness_click();
            if (btnLEDPower.isSelected()) {
                turnOnFlashLight();
            }
        }
    };

    private synchronized void updateBrightness_click() {
        switch (_brightnessMode) {
        case 0:// max
            _brightnessMode = 1;
            _prevBrightnessMode = 0;
            break;
        case 1:// medium
            if (_prevBrightnessMode == 0) {
                _brightnessMode = 2;
            } else {
                _brightnessMode = 0;
            }
            break;
        case 2:// min
            _brightnessMode = 1;
            _prevBrightnessMode = 2;
            break;
        default:
        }
        _preferenceUtilities.setBrightnessModeLed(_brightnessMode);
        updateBrightnessUI();
    }

    private synchronized void updateBrightnessUI() {
        switch (_brightnessMode) {
        case 0:// max
            _imgLEDHigh.setImageResource(R.drawable.w_led_brightness_on);
            _imgLEDMedium.setImageResource(R.drawable.w_led_brightness_on);
            break;
        case 1:// medium
            _imgLEDHigh.setImageResource(R.drawable.w_led_brightness_off);
            _imgLEDMedium.setImageResource(R.drawable.w_led_brightness_on);
            break;
        case 2:// min
            _imgLEDHigh.setImageResource(R.drawable.w_led_brightness_off);
            _imgLEDMedium.setImageResource(R.drawable.w_led_brightness_off);
            break;
        default:
        }
    }
}
