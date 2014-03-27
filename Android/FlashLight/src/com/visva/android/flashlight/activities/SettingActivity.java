/**
 * 
 */
package com.visva.android.flashlight.activities;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.visva.android.flashlight.R;
import com.visva.android.flashlight.adapter.VisibleLightSourcesAdapter;
import com.visva.android.flashlight.adapter.VisibleLightSourcesViewHelper;
import com.visva.android.flashlight.common.Key;
import com.visva.android.flashlight.common.OnShakeListener;
import com.visva.android.flashlight.common.SaveVisibleLightSource;
import com.visva.android.flashlight.common.ScreenLight;
import com.visva.android.flashlight.common.ScreenLightKey;
import com.visva.android.flashlight.common.ShakedListener;
import com.visva.android.flashlight.utilities.CameraUtilities;
import com.visva.android.flashlight.utilities.HardwareUtilities;
import com.visva.android.flashlight.utilities.LEDUtilities;

/**
 * @author KieuThang
 * 
 */
public class SettingActivity extends BaseActivity implements Key {
    private Button _btnFeatures;
    private Button _btnShare;
    private ToggleButton _btnPlaySounds;
    private Spinner _spDefaultLightSource;
    private Spinner _spShakeLightTimeOut;
    private Button _btnVisibleLightSource;
    private ToggleButton _btnAskOnQuit;
    private ToggleButton _btnDefaultStateLed;
    private ToggleButton _btnSharkOnLockScreen;
    private Button _btnShakeSensitivity;
    private ToggleButton _btnShowNotification;
    private ToggleButton _btnLedVibration;
    private ToggleButton _btnScreenLightDefaultState;
    private ToggleButton _btnScreenLightVibration;
    private TextView _tvLabel;

    private AlertDialog _dlgVisibleLightSources;
    private AlertDialog _dlgShakeSensitivity;
    private ListView _lvVisibleLightSources;
    private SeekBar _sbShakeSensitivity;
    private TextView _tvShakeSensitivity;

    private VisibleLightSourcesAdapter _visibleLightSourcesAdapter;
    private boolean _isFlashLight = false;
    private ArrayList<ScreenLight> _lstLightSources = new ArrayList<ScreenLight>();
    private String _visibleLightSource = "";
    private ArrayList<Integer> _lstVisibleLightSourcesId = new ArrayList<Integer>();
    private ShakedListener _shakedListenerSetting;
    private boolean _isTurnOnFlashLight = false;
    private boolean _isShake = false;
    private LayoutInflater _inflater;
    private LinearLayout _layoutLed;
    private AdView layoutAds;

    private OnClickListener onClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
            case R.id.btnShare:
                btnShare_Click();
                break;
            case R.id.btnPlaySounds:
                btnPlaySounds_Click();
                break;
            case R.id.btnVisibleLightSources:
                btnVisibleLightSources_Click();
                break;
            case R.id.btnAskOnQuit:
                btnAskOnQuit_Click();
                break;
            case R.id.btnDefaultState:
                btnDefaultState_Click();
                break;
            case R.id.btnSharkOnLockScreen:
                btnSharkOnLockScreen_Click();
                break;
            case R.id.btnShakeSensitivity:
                btnShakeSensitivity_Click();
                break;
            case R.id.btnShowNotification:
                btnShowNotification_Click();
                break;
            case R.id.btnVibrationInLedLight:
                btnVibrationInLedLight_Click();
                break;
            case R.id.btnDefaultStateScreenLight:
                btnDefaultStateScreenLight_Click();
                break;
            case R.id.btnVibrationInScreenLight:
                btnVibrationInScreenLight_Click();
                break;
            }
        }
    };

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);

        mapUIElement();
        initLstLightSource(_isFlashLight);
        initSpinnerDefaultLightSource();
        initSpinnerLedShakeLightTimeOut();
        initAllDialog(SettingActivity.this);

        if (_preferenceUtilities.isShowSettingLabel()) {
            flip(_tvLabel);
            _preferenceUtilities.setShowSettingLabel(false);
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
    protected void onResume() {
        super.onResume();
        updateAllState();
        _shakedListenerSetting.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        _dlgVisibleLightSources.dismiss();
        _shakedListenerSetting.pause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        SaveVisibleLightSource._lstVisibleLightSourcesId.clear();
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //switchToActivity(MainActivity.class);
            finish();
        } else if (keyCode == KeyEvent.KEYCODE_MENU) {
            return true;
        }
        return false;
    }

    private void mapUIElement() {
        _inflater = LayoutInflater.from(getBaseContext());

        _btnAskOnQuit = (ToggleButton) findViewById(R.id.btnAskOnQuit);
        _btnDefaultStateLed = (ToggleButton) findViewById(R.id.btnDefaultState);
        _btnScreenLightDefaultState = (ToggleButton) findViewById(R.id.btnDefaultStateScreenLight);
        _btnFeatures = (Button) findViewById(R.id.btnFeatures);
        _btnPlaySounds = (ToggleButton) findViewById(R.id.btnPlaySounds);
        _btnShakeSensitivity = (Button) findViewById(R.id.btnShakeSensitivity);
        _btnShare = (Button) findViewById(R.id.btnShare);
        _btnShowNotification = (ToggleButton) findViewById(R.id.btnShowNotification);
        _btnLedVibration = (ToggleButton) findViewById(R.id.btnVibrationInLedLight);
        _btnScreenLightVibration = (ToggleButton) findViewById(R.id.btnVibrationInScreenLight);
        _btnVisibleLightSource = (Button) findViewById(R.id.btnVisibleLightSources);
        _btnSharkOnLockScreen = (ToggleButton) findViewById(R.id.btnSharkOnLockScreen);

        _btnAskOnQuit.setOnClickListener(onClickListener);
        _btnDefaultStateLed.setOnClickListener(onClickListener);
        _btnScreenLightDefaultState.setOnClickListener(onClickListener);
        _btnFeatures.setOnClickListener(_btnFeature_Click);
        _btnPlaySounds.setOnClickListener(onClickListener);
        _btnShakeSensitivity.setOnClickListener(onClickListener);
        _btnShare.setOnClickListener(onClickListener);
        _btnShowNotification.setOnClickListener(onClickListener);
        _btnLedVibration.setOnClickListener(onClickListener);
        _btnScreenLightVibration.setOnClickListener(onClickListener);
        _btnVisibleLightSource.setOnClickListener(onClickListener);
        _btnSharkOnLockScreen.setOnClickListener(onClickListener);

        _layoutLed = (LinearLayout) findViewById(R.id.ledLightLayout);

        _tvLabel = (TextView) findViewById(R.id.tvLabel);

        _spDefaultLightSource = (Spinner) findViewById(R.id.spDefaultLightSource);
        _spDefaultLightSource.setOnItemSelectedListener(_itemDefaultLightSource_Selected);

        _spShakeLightTimeOut = (Spinner) findViewById(R.id.spShakeLightTimeOut);

        _isFlashLight = CameraUtilities.isFlashLightAvailable(SettingActivity.this);

        if (_isFlashLight || LEDUtilities.isSupported()) {
            _layoutLed.setVisibility(View.VISIBLE);
        } else {
            _layoutLed.setVisibility(View.GONE);
        }

        _shakedListenerSetting = new ShakedListener(this);
        _shakedListenerSetting.setPreferenceUtilities(_preferenceUtilities);
        _shakedListenerSetting.setOnShakeListener(_onShakeListenerSetting);

    }

    private void initLstLightSource(boolean isFlashLight) {
        for (int i = 0; i < ScreenLightKey.lstScreenLight.size() - 1; i++) {
            if (!isFlashLight && !LEDUtilities.isSupported()) {
                if (ScreenLightKey.lstScreenLight.get(i).getId() != LED_LIGHT
                        && ScreenLightKey.lstScreenLight.get(i).getId() != MORSE_CODE
                        && ScreenLightKey.lstScreenLight.get(i).getId() != STROBE_LIGHT) {
                    _lstLightSources.add(ScreenLightKey.lstScreenLight.get(i));
                }
            } else {
                _lstLightSources.add(ScreenLightKey.lstScreenLight.get(i));
            }
        }
    }

    private void updateAllState() {
        _btnAskOnQuit.setChecked(_preferenceUtilities.isAskonQuit());
        _btnDefaultStateLed.setChecked(_preferenceUtilities.isLedDefaultState());
        _btnScreenLightDefaultState.setChecked(_preferenceUtilities.isScreenLightDefaultState());
        _btnPlaySounds.setChecked(_preferenceUtilities.isPlaySound());
        _btnSharkOnLockScreen.setChecked(_preferenceUtilities.isLedSharkOnLockScreen());
        _btnShowNotification.setChecked(_preferenceUtilities.isLedShowNotification());
        _btnLedVibration.setChecked(_preferenceUtilities.isLedVibration());
        _btnScreenLightVibration.setChecked(_preferenceUtilities.isScreenLightVibration());
        _btnShakeSensitivity.setEnabled(_preferenceUtilities.isLedSharkOnLockScreen());

        _spDefaultLightSource.setSelection(
                getPositionInSpDefaultLightSource(_preferenceUtilities.getDefaultLightSource()), true);

        _spShakeLightTimeOut.setEnabled(_preferenceUtilities.isLedSharkOnLockScreen());
        _spShakeLightTimeOut.setSelection(_preferenceUtilities.getLedShakeLightTimeOut());

        _visibleLightSource = _preferenceUtilities.getVisibleLightSources();
        _lstVisibleLightSourcesId.addAll(SaveVisibleLightSource.getListVisibleSources(_visibleLightSource));
        SaveVisibleLightSource.setLstVisibleLightSourcesId(_lstVisibleLightSourcesId);
        for (int i = 0; i < _lstLightSources.size(); i++) {
            for (int j = 0; j < _lstVisibleLightSourcesId.size(); j++) {
                if (_lstLightSources.get(i).getId() == _lstVisibleLightSourcesId.get(j)) {
                    _visibleLightSourcesAdapter.getItem(i).setCheck(true);
                    _lstLightSources.get(i).setCheck(true);
                }
            }
        }
    }

    private int getPositionInSpDefaultLightSource(int defaultLightSource) {
        int position = 0;
        for (int i = 0; i < _lstLightSources.size(); i++) {
            if (_lstLightSources.get(i).getId() == defaultLightSource) {
                position = i;
                break;
            }
        }
        return position;
    }

    private void createLightSourceDialog(Context context) {
        View __view = _inflater.inflate(R.layout.list_view_light_source, null);
        _lvVisibleLightSources = (ListView) __view.findViewById(R.id.lvLightSource);
        _visibleLightSourcesAdapter = new VisibleLightSourcesAdapter(context, _lstLightSources);
        _lvVisibleLightSources.setAdapter(_visibleLightSourcesAdapter);
        _lvVisibleLightSources.setOnItemClickListener(_itemVisibleLightSources_Click);
        _dlgVisibleLightSources = new AlertDialog.Builder(SettingActivity.this).setTitle("Visible Light Sources")
                .setView(__view).create();
        _dlgVisibleLightSources.setOnDismissListener(_dlgVisibleLightSource_Dismiss);
    }

    private void createShakeSensitivityDialog(Context context) {
        _sbShakeSensitivity = new SeekBar(context);
        _sbShakeSensitivity.setMax(10);
        _sbShakeSensitivity.setProgress(_preferenceUtilities.getLedShakeSensitivity());
        _sbShakeSensitivity.setOnSeekBarChangeListener(_sbShakeSensitivity_Change);
        _tvShakeSensitivity = new TextView(context);
        _tvShakeSensitivity.setGravity(Gravity.CENTER);
        _tvShakeSensitivity.setText("Shake the device to test the sensitivity");
        LinearLayout __layout = new LinearLayout(context);
        __layout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        __layout.setOrientation(LinearLayout.VERTICAL);
        __layout.setGravity(Gravity.CENTER);
        __layout.setPadding(5, 5, 5, 5);
        __layout.addView(_sbShakeSensitivity);
        __layout.addView(_tvShakeSensitivity);
        LinearLayout __layout1 = new LinearLayout(context);
        __layout1.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        __layout1.setOrientation(LinearLayout.HORIZONTAL);
        __layout1.setGravity(Gravity.CENTER);
        Button __btOk = new Button(context);
        __btOk.setText("OK");
        __btOk.setOnClickListener(_btOk_Click);
        Button __btSimulate = new Button(context);
        __btSimulate.setText("Simulate vibration");
        __btSimulate.setOnClickListener(_btSimulator_Click);
        __layout1.addView(__btOk);
        __layout1.addView(__btSimulate);
        __layout.addView(__layout1);
        _dlgShakeSensitivity = new AlertDialog.Builder(context).setTitle("Shake: Sensitivity").setView(__layout)
                .create();

        _dlgShakeSensitivity.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {
                _isShake = false;
            }
        });
    }

    private void initAllDialog(Context context) {
        createLightSourceDialog(context);
        createShakeSensitivityDialog(context);
    }

    private void initSpinnerDefaultLightSource() {
        ArrayList<String> __lstLightSourceName = new ArrayList<String>();
        for (int i = 0; i < _lstLightSources.size(); i++) {
            __lstLightSourceName.add(_lstLightSources.get(i).getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(SettingActivity.this,
                android.R.layout.simple_spinner_item, __lstLightSourceName);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _spDefaultLightSource.setAdapter(adapter);

    }

    private void initSpinnerLedShakeLightTimeOut() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(SettingActivity.this,
                android.R.layout.simple_spinner_item, SHAKE_LIGHT_TIMEOUT);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _spShakeLightTimeOut.setAdapter(adapter);
        _spShakeLightTimeOut.setOnItemSelectedListener(_itemLedShakeLightTimeout_Selected);
    }

    private OnDismissListener _dlgVisibleLightSource_Dismiss = new OnDismissListener() {

        @Override
        public void onDismiss(DialogInterface dialog) {
            new Save().execute();
        }
    };

    private OnItemClickListener _itemVisibleLightSources_Click = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
            VisibleLightSourcesViewHelper helper = (VisibleLightSourcesViewHelper) view.getTag();
            ScreenLight screenLight = _lstLightSources.get(position);
            screenLight.toggedCheck();
            helper.getCbName().setChecked(screenLight.isCheck());
            SaveVisibleLightSource.updateVisibleLightSource(screenLight.isCheck(), _lstLightSources.get(position)
                    .getId());
        }
    };

    private OnItemSelectedListener _itemDefaultLightSource_Selected = new OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
            _preferenceUtilities.setDefaultLightSource(_lstLightSources.get(position).getId());
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {

        }
    };

    private OnItemSelectedListener _itemLedShakeLightTimeout_Selected = new OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
            _preferenceUtilities.setLedShakeLightTimeOut(position);
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {

        }
    };

    private void btnAskOnQuit_Click() {
        _preferenceUtilities.setAskOnQuit(_btnAskOnQuit.isChecked());
    }

    private void btnDefaultState_Click() {
        _preferenceUtilities.setLedDefaultState(_btnDefaultStateLed.isChecked());
    }

    private void btnDefaultStateScreenLight_Click() {
        _preferenceUtilities.setScreenLightDefaultState(_btnScreenLightDefaultState.isChecked());
    }

    private void btnPlaySounds_Click() {
        _preferenceUtilities.setPlaySound(_btnPlaySounds.isChecked());
    }

    private void btnShakeSensitivity_Click() {
        _dlgShakeSensitivity.show();
        _isShake = true;
    }

    private void btnShare_Click() {
        _dlgShare.show();
    }

    private void btnShowNotification_Click() {
        _preferenceUtilities.setLedShowNotification(_btnShowNotification.isChecked());
    }

    private void btnVibrationInLedLight_Click() {
        _preferenceUtilities.setLedVibration(_btnLedVibration.isChecked());
    }

    private void btnVibrationInScreenLight_Click() {
        _preferenceUtilities.setScreenLightVibration(_btnScreenLightVibration.isChecked());
    }

    private void btnSharkOnLockScreen_Click() {
        _preferenceUtilities.setLedSharkOnLockScreen(_btnSharkOnLockScreen.isChecked());
        _btnShakeSensitivity.setEnabled(_preferenceUtilities.isLedSharkOnLockScreen());
        _spShakeLightTimeOut.setEnabled(_preferenceUtilities.isLedSharkOnLockScreen());
        if (_btnSharkOnLockScreen.isChecked()) {
            _dlgShakeSensitivity.show();
        }
    }

    private void btnVisibleLightSources_Click() {
        _dlgVisibleLightSources.show();
    }

    private OnClickListener _btOk_Click = new OnClickListener() {

        @Override
        public void onClick(View v) {
            _dlgShakeSensitivity.dismiss();
            if (_isTurnOnFlashLight) {
                CameraUtilities.turnOffFlashLight();
                _isTurnOnFlashLight = false;
            }
        }
    };

    private OnClickListener _btSimulator_Click = new OnClickListener() {

        @Override
        public void onClick(View v) {
            HardwareUtilities.vibrate(SettingActivity.this, 1000);
        }
    };

    private OnSeekBarChangeListener _sbShakeSensitivity_Change = new OnSeekBarChangeListener() {

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            _preferenceUtilities.setLedShakeSensitivity(seekBar.getProgress());
            _shakedListenerSetting.setCheckShakeCount(Math.abs(_preferenceUtilities.getLedShakeSensitivity() - 8));
            _shakedListener.setCheckShakeCount(Math.abs(_preferenceUtilities.getLedShakeSensitivity() - 8));
            if (seekBar.getProgress() >= 0 && seekBar.getProgress() <= 10) {
                _isShake = true;
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            _isShake = false;
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        }
    };

    private OnShakeListener _onShakeListenerSetting = new OnShakeListener() {

        @Override
        public void onShake() {
            if (_preferenceUtilities.isLedSharkOnLockScreen()) {
                if (_isShake) {
                    if (!_isTurnOnFlashLight) {
                        // CameraUtilities.turnOnFlashLight();
                        turnOn();
                        _isTurnOnFlashLight = true;
                    } else {
                        // CameraUtilities.turnOffFlashLight();
                        turnOff();
                        _isTurnOnFlashLight = false;
                    }
                    HardwareUtilities.vibrate(SettingActivity.this, 300);
                }
            }
        }
    };

    private static void turnOn() {
        if (LEDUtilities.isSupported()) {
            LEDUtilities.turnOn();
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

    class Save extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            _preferenceUtilities.setVisibleLightSources(SaveVisibleLightSource.getStringVisibleLightSource());
            return null;
        }
    }
}
