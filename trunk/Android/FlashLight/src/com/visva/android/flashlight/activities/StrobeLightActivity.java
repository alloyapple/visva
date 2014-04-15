package com.visva.android.flashlight.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.visva.android.flashlight.customcontrols.VerticalSeekBar;
import com.visva.android.flashlight.utilities.CameraUtilities;
import com.visva.android.flashlight.utilities.LEDUtilities;
import com.visva.android.flashlightmaster.R;

public class StrobeLightActivity extends BaseActivity {

    private Button _btnFeatures;

    private VerticalSeekBar _sbTurnOnTime;

    private VerticalSeekBar _sbTurnOffTime;

    private TextView _tvLabel;

    private static long _delayOn;

    private static long _delayOff;

    private long _minDelay = 200;

    private LinearLayout _ledControlLayout;

    /**
     * Flash light
     * 
     * 1: On 0: Off
     */
    private static int _flag;
    private static FlashHandler _flashHandler;
    private ImageView _imgLEDHigh;
    private ImageView _imgLEDMedium;
    private AlertDialog _dlgWarning;
    private AdView layoutAds;

    /**
     * brightness mode 0: high 1: medium 2: low
     */
    private static int _brightnessMode;
    private static int _prevBrightnessMode;

    /** Called when the activity is first created. */
    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.strobelight);
        this.mapUIElement();
        createDialogWarning();

        if (_preferenceUtilities.isShowDefaultLightSource() || _preferenceUtilities.isShowStrobeLightLabel()) {
            flip(_tvLabel);
            _preferenceUtilities.setShowStrobeLightLabel(false);
            _preferenceUtilities.setShowDefaultLightSource(false);
        } else {
            _tvLabel.setVisibility(View.GONE);
        }

        if (!_preferenceUtilities.isFirstStrobeLightWarning()) {
            _dlgWarning.show();
            _preferenceUtilities.setFirstStrobeLightWarning(true);
        }
        layoutAds = (AdView) this.findViewById(R.id.main_adView);
        AdRequest adRequest = new AdRequest();
        adRequest.setTesting(true);
        layoutAds.loadAd(adRequest);
        layoutAds.bringToFront();
    }

    @Override
    public void onResume() {
        Log.d("HieuPV", "Go Resume");
        super.onResume();
        _brightnessMode = _preferenceUtilities.getBrightnessMode();
        _sbTurnOnTime.setProgress(_preferenceUtilities.getStrobeLightOnSpeed());
        _sbTurnOffTime.setProgress(_preferenceUtilities.getStrobeLightOffSpeed());
        // if (_flashHandler == null) {
        _flashHandler = new FlashHandler();
        // }
        _flag = 1;
        updateFlash();
        updateBrightnessUI();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

    }

    private void mapUIElement() {
        this._btnFeatures = (Button) this.findViewById(R.id.btnFeatures);
        this._btnFeatures.setOnClickListener(_btnFeature_Click);
        this._sbTurnOnTime = (VerticalSeekBar) this.findViewById(R.id.sbTurnOnTime);
        this._sbTurnOnTime.setOnSeekBarChangeListener(_sbarOn_Change);
        this._sbTurnOffTime = (VerticalSeekBar) this.findViewById(R.id.sbTurnOffTime);
        this._sbTurnOffTime.setOnSeekBarChangeListener(_sbarOff_Change);
        this._ledControlLayout = (LinearLayout) this.findViewById(R.id.ledControl);
        this._ledControlLayout.setOnClickListener(_layoutLEDControl_Click);
        this._imgLEDHigh = (ImageView) this.findViewById(R.id.ledHigh);
        this._imgLEDMedium = (ImageView) this.findViewById(R.id.ledMedium);
        this._tvLabel = (TextView) this.findViewById(R.id.tvLabel);
        if (!LEDUtilities.isSupported()) {
            this._ledControlLayout.setVisibility(View.GONE);
        }
        _brightnessMode = 0;
        _prevBrightnessMode = 0;
        _delayOn = _minDelay + 400 - 4 * _preferenceUtilities.getStrobeLightOnSpeed();
        _delayOff = _minDelay + 400 - 4 * _preferenceUtilities.getStrobeLightOffSpeed();

    }

    private void createDialogWarning() {
        _dlgWarning = new AlertDialog.Builder(this).setTitle("CAUTION!").setIcon(R.drawable.ic_menu_more)
                .setMessage("Strobe lighting can trigger seizures in photosensitive epilepsy! Proceed with caution")
                .setPositiveButton("OK", _positive_Click).create();
    }

    private android.content.DialogInterface.OnClickListener _positive_Click = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            _dlgWarning.dismiss();
        }
    };
    private OnSeekBarChangeListener _sbarOn_Change = new OnSeekBarChangeListener() {
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            _delayOn = _minDelay + 300 - 4 * progress;
            _preferenceUtilities.setStrobeLightOnSpeed(progress);
        }
    };

    private OnSeekBarChangeListener _sbarOff_Change = new OnSeekBarChangeListener() {
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            _delayOff = _minDelay + 300 - 4 * progress;
            _preferenceUtilities.setStrobeLightOffSpeed(progress);
        }
    };

    private synchronized void updateFlash() {
        if (_flag == 1) {
            turnOn();
            // CameraUtilities.turnOnFlashLight();
            _flashHandler.sleep(_delayOn);
            _flag = 0;
        } else {
            turnOff();
            // CameraUtilities.turnOffFlashLight();
            _flashHandler.sleep(_delayOff);
            _flag = 1;
        }
    }

    class FlashHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                StrobeLightActivity.this.updateFlash();
            }
        }

        public void sleep(long delayMillis) {
            removeMessages(1);
            sendMessageDelayed(obtainMessage(1), delayMillis);
        }
    }

    public static void cancelFlash() {
        turnOff();
        // CameraUtilities.turnOffFlashLight();
        if (_flashHandler != null) {
            _flashHandler.removeMessages(1);
        }
    }

    protected OnClickListener _btnFeature_Click = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // cancelFlash();
            switchToActivity(FeatureActivity.class);
            finish();
        }
    };

    protected OnClickListener _layoutLEDControl_Click = new OnClickListener() {

        @Override
        public void onClick(View v) {
            updateBrightness_click();
        }
    };

    protected DialogInterface.OnClickListener positiveButton_Click = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            _preferenceUtilities.setScreenSelected(SCREEN_LIGHT);
            dialog.dismiss();
            cancelFlash();
            finish();
        }
    };

    protected DialogInterface.OnClickListener negetiveButton_Click = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            _dlgQuit.dismiss();
        }
    };

    private synchronized void updateBrightness_click() {
        switch (_brightnessMode) {
        case 0:
            _brightnessMode = 1;
            _prevBrightnessMode = 0;
            break;
        case 1:
            if (_prevBrightnessMode == 0) {
                _brightnessMode = 2;
            } else {
                _brightnessMode = 0;
            }
            break;
        case 2:
            _brightnessMode = 1;
            _prevBrightnessMode = 2;
            break;
        default:
        }
        _preferenceUtilities.setBrightnessMode(_brightnessMode);
        updateBrightnessUI();
    }

    private void turnOn() {
        if (LEDUtilities.isSupported()) {
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
            // LEDUtilities.turnOn();
        } else {
            CameraUtilities.turnOnFlashLight();
        }
    }

    private static void turnOff() {
        if (LEDUtilities.isSupported()) {
            LEDUtilities.turnOff();
        } else {
            CameraUtilities.turnOffFlashLight();
        }
    }

    private synchronized void updateBrightnessUI() {
        switch (_brightnessMode) {
        case 0:
            _imgLEDHigh.setImageResource(R.drawable.w_led_brightness_on);
            _imgLEDMedium.setImageResource(R.drawable.w_led_brightness_on);
            break;
        case 1:
            _imgLEDHigh.setImageResource(R.drawable.w_led_brightness_off);
            _imgLEDMedium.setImageResource(R.drawable.w_led_brightness_on);
            break;
        case 2:
            _imgLEDHigh.setImageResource(R.drawable.w_led_brightness_off);
            _imgLEDMedium.setImageResource(R.drawable.w_led_brightness_off);
            break;
        default:
        }
    }

}
