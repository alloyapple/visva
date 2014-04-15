package com.visva.android.flashlight.activities;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.visva.android.flashlight.common.Session;
import com.visva.android.flashlight.gesture.ActivitySwipeDetector;
import com.visva.android.flashlight.gesture.SwipeInterface;
import com.visva.android.flashlight.utilities.ColorUtilities;
import com.visva.android.flashlight.utilities.ScreenUtilities;
import com.visva.android.flashlightmaster.R;

public class LightBuldActivity extends BaseActivity implements SwipeInterface {

    private ActivitySwipeDetector _swipeDetector;
    private LinearLayout _lightBuldTouchLayout;
    private LinearLayout _lightBuldMainLayout;
    private LinearLayout _tipLayout;
    private Button _btnFeatures;
    private ImageView _lightBuld;
    private static final float _brightnessIncreaseStep = 0.2f;
    private static final int _distanceToMax = 200;
    private float _lastBrightness = Session.getLowestScreenBrightness();
    private TextView _tvLabel;
    private boolean _isHideTipLayout = false;
    private float _currentBrightness = 0;
    private boolean _isShowLightBuld = false;
    private int colorSelected;
    private AdView layoutAds;

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lightbuld);

        this.mapUIElement();
        this.initializeSwipeDetector();

        colorSelected = _preferenceUtilities.getLightBuldColor();
        this._lightBuldMainLayout.setBackgroundColor(colorSelected);

        checkHideTipLayout();

        if (_preferenceUtilities.isShowDefaultLightSource() || _preferenceUtilities.isShowLightBuldLabel()) {
            flip(_tvLabel);
            _preferenceUtilities.setShowLightBuldLabel(false);
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
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onStop() {
        super.onStop();
        _preferenceUtilities.setLightBuldColor(colorSelected);
        ScreenUtilities.setBrightness(LightBuldActivity.this, _currentBrightness);

    }

    private void mapUIElement() {
        this._lightBuldTouchLayout = (LinearLayout) this.findViewById(R.id.lightBuldTouchLayout);
        this._lightBuld = (ImageView) this.findViewById(R.id.lightBuld);
        this._lightBuldMainLayout = (LinearLayout) this.findViewById(R.id.lightBuldMainLayout);
        this._tipLayout = (LinearLayout) this.findViewById(R.id.tipLayout);
        this._tipLayout.setOnTouchListener(_tipLayout_Tounch);
        this._btnFeatures = (Button) this.findViewById(R.id.btnFeatures);
        this._tvLabel = (TextView) this.findViewById(R.id.tvLabel);
        this._btnFeatures.setOnClickListener(_btnFeature_Click);
        this._currentBrightness = ScreenUtilities.getCurrentBrightness(this);
    }

    private void initializeSwipeDetector() {
        this._swipeDetector = new ActivitySwipeDetector(this);
        this._lightBuldTouchLayout.setOnTouchListener(this._swipeDetector);
    }

    @Override
    public void LeftToRight(View view, float distance) {
        if (view.getId() != R.id.lightBuldTouchLayout)
            return;

        AlphaAnimation __changeColorAnim = new AlphaAnimation(1.0f, 0.9f);
        __changeColorAnim.setDuration(200);
        this._lightBuldMainLayout.setAnimationCacheEnabled(true);
        this._lightBuldMainLayout.setAnimation(__changeColorAnim);
        colorSelected = ColorUtilities.getColorLeftToRight();
        this._lightBuldMainLayout.setBackgroundColor(colorSelected);

        __changeColorAnim = new AlphaAnimation(0.9f, 1.0f);
        __changeColorAnim.setDuration(200);
        this._lightBuldMainLayout.setAnimation(__changeColorAnim);
    }

    @Override
    public void RightToLeft(View view, float distance) {
        if (view.getId() != R.id.lightBuldTouchLayout)
            return;

        AlphaAnimation __changeColorAnim = new AlphaAnimation(1.0f, 0.9f);
        __changeColorAnim.setDuration(200);
        this._lightBuldMainLayout.setAnimationCacheEnabled(true);
        this._lightBuldMainLayout.setAnimation(__changeColorAnim);
        colorSelected = ColorUtilities.getColorRightToLeft();
        this._lightBuldMainLayout.setBackgroundColor(colorSelected);

        __changeColorAnim = new AlphaAnimation(0.9f, 1.0f);
        __changeColorAnim.setDuration(200);
        this._lightBuldMainLayout.setAnimation(__changeColorAnim);
    }

    @Override
    public void BottomToTop(View view, float distance) {
        if (view.getId() != R.id.lightBuldTouchLayout)
            return;
        if (distance > _distanceToMax) {
            _lastBrightness = Session.getHighestScreenBrightness();
        } else {
            if (_lastBrightness + _brightnessIncreaseStep >= Session.getHighestScreenBrightness()) {
                _lastBrightness = Session.getHighestScreenBrightness();
            } else {
                _lastBrightness = _lastBrightness + _brightnessIncreaseStep;
            }
        }

        ScreenUtilities.setBrightness(this, _lastBrightness);
        showLightBuldAnimation(_lastBrightness);
    }

    @Override
    public void TopToBottom(View view, float distance) {
        if (view.getId() != R.id.lightBuldTouchLayout)
            return;
        if (0 - distance > _distanceToMax) {
            _lastBrightness = Session.getLowestScreenBrightness();
        } else {
            if (_lastBrightness - _brightnessIncreaseStep <= Session.getLowestScreenBrightness()) {
                _lastBrightness = Session.getLowestScreenBrightness();
            } else {
                _lastBrightness = _lastBrightness - _brightnessIncreaseStep;
            }
        }

        ScreenUtilities.setBrightness(this, _lastBrightness);
        hideLightBuldAnimation(_lastBrightness);
    }

    private void showLightBuldAnimation(float brightness) {
        int alpha = (int) (brightness * 255);
        if (!_isShowLightBuld) {
            AlphaAnimation __showLightBuld = new AlphaAnimation(0.0f, 1.0f);
            __showLightBuld.setDuration(200);
            _lightBuld.startAnimation(__showLightBuld);
            _lightBuld.setVisibility(View.VISIBLE);
            _isShowLightBuld = true;
        }
        _lightBuld.getDrawable().setAlpha(alpha);
        _lightBuld.setAlpha(alpha);
    }

    public void hideLightBuldAnimation(float brightness) {
        int alpha = (int) (brightness * 255);
        if (_lastBrightness <= Session.getLowestScreenBrightness()) {
            if (_isShowLightBuld) {
                AlphaAnimation __hideLightBuld = new AlphaAnimation(1.0f, 0.0f);
                __hideLightBuld.setDuration(200);
                _lightBuld.startAnimation(__hideLightBuld);
                _lightBuld.setVisibility(View.GONE);
                _isShowLightBuld = false;
            }
        }
        _lightBuld.getDrawable().setAlpha(alpha);
        _lightBuld.setAlpha(alpha);
    }

    private OnTouchListener _tipLayout_Tounch = new OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (!_isHideTipLayout) {
                _isHideTipLayout = true;
                hideTipLayout();
            }
            return true;
        }
    };

    private void hideTipLayout() {
        AlphaAnimation __alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        __alphaAnimation.setDuration(500);
        _tipLayout.setAnimation(__alphaAnimation);
        _tipLayout.setVisibility(View.GONE);
    }

    private void checkHideTipLayout() {
        if (!_preferenceUtilities.isFirstRunLightBuld()) {
            _tipLayout.setVisibility(View.VISIBLE);
            _preferenceUtilities.setFirstRunLightBuld(true);
        } else {
            _tipLayout.setVisibility(View.GONE);
        }
    }
}
