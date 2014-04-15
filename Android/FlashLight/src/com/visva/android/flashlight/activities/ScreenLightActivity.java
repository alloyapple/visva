package com.visva.android.flashlight.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.visva.android.flashlight.common.Session;
import com.visva.android.flashlight.utilities.HardwareUtilities;
import com.visva.android.flashlight.utilities.ScreenUtilities;
import com.visva.android.flashlight.utilities.SoundUtilities;
import com.visva.android.flashlightmaster.R;

public class ScreenLightActivity extends BaseActivity {
    private BaseActivity _mainActivity;
    private Button btnScreenLight;
    private LinearLayout _boundFrameLayout;
    private boolean _screenLightActivated = false;
    private boolean _screenLightVibration = false;
    private boolean _screenLightTurnOn = false;
    private TextView _tvLabel;
    private Button _btnFeatures;
    private float _currentBrightness;
    private SoundUtilities _soundUtilities;
    private AdView layoutAds;

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screenlight);
        this._mainActivity = this;

        this.mapUIElement();

        initActivatedScreenLight();

        if (_preferenceUtilities.isShowDefaultLightSource() || _preferenceUtilities.isShowScreenLightLabel()) {
            flip(_tvLabel);
            _preferenceUtilities.setShowScreenLightLabel(false);
            _preferenceUtilities.setShowDefaultLightSource(false);
        } else {
            _tvLabel.setVisibility(View.GONE);
        }
        layoutAds = (AdView) this.findViewById(R.id.main_adView);
        AdRequest adRequest = new AdRequest();
        adRequest.setTesting(true);
        layoutAds.loadAd(adRequest);
        layoutAds.bringToFront();
    }

    @Override
    protected void onStop() {
        super.onStop();
        ScreenUtilities.setBrightness(ScreenLightActivity.this, _currentBrightness);
    }

    /**
     * Mapping UI elements on layout into variables and assign Listeners
     * */
    private void mapUIElement() {
        this.btnScreenLight = (Button) this.findViewById(R.id.btnScreenLight);
        this.btnScreenLight.setOnClickListener(this.btnScreenLight_Click);

        this._boundFrameLayout = (LinearLayout) this.findViewById(R.id.screenBoundFrameLayout);
        this._tvLabel = (TextView) this.findViewById(R.id.tvLabel);
        this._btnFeatures = (Button) this.findViewById(R.id.btnFeatures);
        this._btnFeatures.setOnClickListener(_btnFeature_Click);
        this._currentBrightness = ScreenUtilities.getCurrentBrightness(ScreenLightActivity.this);
        _soundUtilities = new SoundUtilities(ScreenLightActivity.this, _preferenceUtilities);
    }

    public void initActivatedScreenLight() {
        _screenLightActivated = _preferenceUtilities.isScreenLightDefaultState();
        _screenLightVibration = _preferenceUtilities.isScreenLightVibration();
        _screenLightTurnOn = _preferenceUtilities.isScreenLightTurnOn();
        if (_screenLightActivated || _screenLightTurnOn) {
            turnOnScreenLight();
        }
        // else {
        // turnOffScreenLight();
        // }

    }

    public void turnOnScreenLight() {
        ScreenUtilities.setBrightness(_mainActivity, Session.getHighestScreenBrightness());
        btnScreenLight.setSelected(true);
        _boundFrameLayout.setBackgroundColor(Color.WHITE);
    }

    public void turnOffScreenLight() {
        ScreenUtilities.setBrightness(_mainActivity, Session.getLowestScreenBrightness());
        btnScreenLight.setSelected(false);
        _boundFrameLayout.setBackgroundResource(R.drawable.main_background);
    }

    public void activatedScreenLight() {
        // Turning on Screen Light
        if (!_screenLightActivated) {
            turnOnScreenLight();
            if (_screenLightVibration) {
                HardwareUtilities.vibrate(_mainActivity, 50);
            }
            _soundUtilities.playSwitchInSound();
            _screenLightActivated = true;
        } else { // Turning off Screen Light
            turnOffScreenLight();
            if (_screenLightVibration) {
                HardwareUtilities.vibrate(_mainActivity, 50);
            }
            _soundUtilities.playSwitchOutSound();
            _screenLightActivated = false;
        }
        // _preferenceUtilities.setScreenLightDefaultState(_screenLightActivated);
        _preferenceUtilities.setScreenLightTurnOn(_screenLightActivated);

    }

    private OnClickListener btnScreenLight_Click = new OnClickListener() {

        @Override
        public void onClick(View v) {
            activatedScreenLight();
        }
    };
}
