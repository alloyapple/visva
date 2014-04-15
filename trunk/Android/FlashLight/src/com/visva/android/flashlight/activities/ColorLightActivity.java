package com.visva.android.flashlight.activities;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.visva.android.flashlight.common.Key;
import com.visva.android.flashlight.common.Session;
import com.visva.android.flashlight.gesture.ActivitySwipeDetector;
import com.visva.android.flashlight.gesture.SwipeInterface;
import com.visva.android.flashlight.utilities.ColorUtilities;
import com.visva.android.flashlight.utilities.ScreenUtilities;
import com.visva.android.flashlightmaster.R;

public class ColorLightActivity extends BaseActivity implements SwipeInterface, Key {
    private LinearLayout _lightColorTouchLayout;
    private LinearLayout _lightColorMainLayout;
    private LinearLayout _tipLayout;
    private static final float _brightnessIncreaseStep = 0.3f;
    private static final int _distanceToMax = 100;
    private float _lastBrightness = Session.getLowestScreenBrightness();
    private TextView _tvLabel;
    private Button _btnFeatures;
    private boolean isHideTipLayout = false;
    private float _currentBrightness = 0;
    private int colorSelected;
    private AdView layoutAds;

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.colorlight);

        this.mapUIElement();
        this.initializeSwipeDetector();

        colorSelected = _preferenceUtilities.getColorLightColor();
        this._lightColorMainLayout.setBackgroundColor(colorSelected);

        checkHideTipLayout();
        if (_preferenceUtilities.isShowDefaultLightSource() || _preferenceUtilities.isShowColorLightLabel()) {
            flip(_tvLabel);
            _preferenceUtilities.setShowColorLightLabel(false);
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
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onStop() {
        super.onStop();
        _preferenceUtilities.setColorLightColor(colorSelected);
        ScreenUtilities.setBrightness(ColorLightActivity.this, _currentBrightness);
    }

    private void mapUIElement() {
        this._lightColorTouchLayout = (LinearLayout) this.findViewById(R.id.colorLightTouchLayout);
        this._lightColorMainLayout = (LinearLayout) this.findViewById(R.id.colorLightMainLayout);
        this._tipLayout = (LinearLayout) this.findViewById(R.id.tipLayout);
        this._tipLayout.setOnTouchListener(_tipLayout_Tounch);
        this._tvLabel = (TextView) this.findViewById(R.id.tvLabel);
        this._btnFeatures = (Button) this.findViewById(R.id.btnFeatures);
        this._btnFeatures.setOnClickListener(_btnFeature_Click);
        this._currentBrightness = ScreenUtilities.getCurrentBrightness(this);
    }

    private void initializeSwipeDetector() {
        ActivitySwipeDetector __swipDetector = new ActivitySwipeDetector(this);
        this._lightColorTouchLayout.setOnTouchListener(__swipDetector);
    }

    @Override
    public void BottomToTop(View view, float distance) {
        if (view.getId() != R.id.colorLightTouchLayout)
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
    }

    @Override
    public void TopToBottom(View view, float distance) {
        if (view.getId() != R.id.colorLightTouchLayout)
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
    }

    @Override
    public void LeftToRight(View view, float distance) {
        if (view.getId() != R.id.colorLightTouchLayout)
            return;

        AlphaAnimation __changeColorAnim = new AlphaAnimation(1.0f, 0.9f);
        __changeColorAnim.setDuration(200);
        this._lightColorMainLayout.setAnimationCacheEnabled(true);
        this._lightColorMainLayout.setAnimation(__changeColorAnim);
        colorSelected = ColorUtilities.getColorLeftToRight();
        this._lightColorMainLayout.setBackgroundColor(colorSelected);

        __changeColorAnim = new AlphaAnimation(0.9f, 1.0f);
        __changeColorAnim.setDuration(200);
        this._lightColorMainLayout.setAnimation(__changeColorAnim);

    }

    @Override
    public void RightToLeft(View view, float distance) {
        if (view.getId() != R.id.colorLightTouchLayout)
            return;

        AlphaAnimation __changeColorAnim = new AlphaAnimation(1.0f, 0.9f);
        __changeColorAnim.setDuration(200);
        this._lightColorMainLayout.setAnimationCacheEnabled(true);
        this._lightColorMainLayout.setAnimation(__changeColorAnim);
        colorSelected = ColorUtilities.getColorRightToLeft();
        this._lightColorMainLayout.setBackgroundColor(colorSelected);

        __changeColorAnim = new AlphaAnimation(0.9f, 1.0f);
        __changeColorAnim.setDuration(200);
        this._lightColorMainLayout.setAnimation(__changeColorAnim);
    }

    private OnTouchListener _tipLayout_Tounch = new OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (!isHideTipLayout) {
                isHideTipLayout = true;
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
        if (!_preferenceUtilities.isFirstRunColorLight()) {
            _tipLayout.setVisibility(View.VISIBLE);
            _preferenceUtilities.setFirstRunColorLight(true);
        } else {
            _tipLayout.setVisibility(View.GONE);
        }
    }
}
