package com.visva.android.flashlight.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.visva.android.flashlight.R;
import com.visva.android.flashlight.utilities.CameraUtilities;
import com.visva.android.flashlight.utilities.LEDUtilities;
import com.visva.android.flashlight.utilities.MorseCodeConverter;

public class MorseCodeActivity extends BaseActivity {

	private Button _btnFeatures;

	private static EditText _txtMorseCode;

	private TextView _tvLabel;

	private SeekBar _sbarSpeed;

	private ToggleButton _toggleRepeat;

	private Button _btnSendCancel;

	private Button _btnLedPower;

	private static long[] _pattern;

	private static FlashHandler _flashHandler;

	private static String _morseText = "";

	private static int _lenMorseText;

	private KeyListener _keyListener;
	
	private AdView layoutAds;

	/**
	 * Flash light
	 * 
	 * 1: On 0: Off
	 */
	private static int _flag;

	private int _index;

	private int _indexMorseText;

	/** Called when the activity is first created. */
	@SuppressWarnings("deprecation")
    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.morsecode);

		this.mapUIElement();

		// if (_flashHandler == null) {
		_flashHandler = new FlashHandler();
		// }

		if (_preferenceUtilities.isShowDefaultLightSource() || _preferenceUtilities.isShowMorseLightLabel()) {
			flip(_tvLabel);
			_preferenceUtilities.setShowMorseLightLabel(false);
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
	public void onResume() {
		super.onResume();
		_txtMorseCode.setText(_preferenceUtilities.getTextMorseCode());
		_sbarSpeed.setProgress(_preferenceUtilities.getTextMorseCodeSpeed());
	}

	private void mapUIElement() {
		this._btnFeatures = (Button) this.findViewById(R.id.btnFeatures);
		this._btnFeatures.setOnClickListener(_btnFeature_Click);
		this._txtMorseCode = (EditText) this.findViewById(R.id.txtMorseCode);
		this._tvLabel = (TextView) this.findViewById(R.id.tvLabel);
		this._sbarSpeed = (SeekBar) this.findViewById(R.id.sbarSpeed);
		this._sbarSpeed.setOnSeekBarChangeListener(_sbarSpeed_Change);
		this._toggleRepeat = (ToggleButton) this.findViewById(R.id.toggleRepeat);
		this._toggleRepeat.setOnClickListener(_toggleRepeat_Click);
		this._btnSendCancel = (Button) this.findViewById(R.id.btnSendCancel);
		this._btnSendCancel.setOnClickListener(_btnSendCancel_Click);
		this._btnLedPower = (Button) this.findViewById(R.id.btnLEDPower);
		this._btnLedPower.setOnTouchListener(_btnLedPower_Touch);
		_keyListener = _txtMorseCode.getKeyListener();

		_txtMorseCode.setFilters(new InputFilter[] { new InputFilter() {

			@Override
			public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
				for (int i = start; i < end; i++) {
					if (!Character.isLetterOrDigit(source.charAt(i))) {
						return "";
					}
				}
				return null;
			}
		} });

		_txtMorseCode.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
				_preferenceUtilities.setTextMorseCode(_txtMorseCode.getText().toString());
			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
		});

		_toggleRepeat.setChecked(_preferenceUtilities.isMorseCodeRepeat());
	}

	private OnClickListener _btnSendCancel_Click = new OnClickListener() {

		@Override
		public void onClick(View v) {
			String __strLabelButton = (String) _btnSendCancel.getText();
			String __strLabelSend = getResources().getString(R.string.label_text_send);
			String __strLabelCancel = getResources().getString(R.string.label_text_cancel);

			if (__strLabelButton.compareToIgnoreCase(__strLabelSend) == 0) {
				if (_txtMorseCode.getText().toString().trim().length() <= 0) {
					return;
				}
				_btnSendCancel.setText(__strLabelCancel);
				sendMorseCode();
			} else {
				_btnSendCancel.setText(__strLabelSend);
				updateUI();
				cancelMorseCode();
			}
		}
	};

	private OnTouchListener _btnLedPower_Touch = new OnTouchListener() {

		@Override
		public boolean onTouch(View view, MotionEvent event) {
			if (view instanceof Button) {
				if (MotionEvent.ACTION_DOWN == event.getAction()) {
					turnOn();
					// CameraUtilities.turnOnFlashLight();
				} else if (MotionEvent.ACTION_UP == event.getAction()) {
					turnOff();
					// CameraUtilities.turnOffFlashLight();
				}
			}
			return false;
		}
	};

	private OnSeekBarChangeListener _sbarSpeed_Change = new OnSeekBarChangeListener() {
		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
		}

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			MorseCodeConverter.setSpeedBase(700 - 4 * progress);
			_preferenceUtilities.setTextMorseCodeSpeed(progress);
		}
	};

	private OnClickListener _toggleRepeat_Click = new OnClickListener() {

		@Override
		public void onClick(View v) {
			_preferenceUtilities.setMorseCodeRepeat(_toggleRepeat.isChecked());
		}
	};

	private synchronized void updateFlash() {

		if (_indexMorseText < _lenMorseText && (_pattern == null || _index >= _pattern.length)) {
			_index = 0;
			_pattern = MorseCodeConverter.pattern(_morseText.charAt(_indexMorseText));
			_indexMorseText++;
		} else if (_indexMorseText >= _lenMorseText && _index >= _pattern.length) {
			if (_toggleRepeat.isChecked()) {
				sendMorseCode();
			} else {
				updateUI();
				cancelMorseCode();
				return;
			}
		}

		if (_index < _pattern.length) {
			_flashHandler.sleep(_pattern[_index]);
			_index++;
		}

		if (_flag == 1) {
			turnOn();
			// CameraUtilities.turnOnFlashLight();
			_txtMorseCode.setSelection(0, _indexMorseText);
			_flag = 0;
		} else {
			turnOff();
			// CameraUtilities.turnOffFlashLight();
			_flag = 1;
			_txtMorseCode.setSelection(0, _indexMorseText - 1);
		}
	}

	class FlashHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			MorseCodeActivity.this.updateFlash();
		}

		public void sleep(long delayMillis) {
			removeMessages(0);
			sendMessageDelayed(obtainMessage(0), delayMillis);
		}
	}

	private void sendMorseCode() {
		_txtMorseCode.setText(_txtMorseCode.getText().toString().trim().toUpperCase());
		_morseText = _txtMorseCode.getText().toString();
		_lenMorseText = _morseText.length();
		if (_lenMorseText <= 0) {
			return;
		}
		_txtMorseCode.setClickable(false);
		_txtMorseCode.setKeyListener(null);

		_btnLedPower.setVisibility(View.INVISIBLE);
		_flag = 0;

		_index = 0;
		_pattern = null;
		_indexMorseText = 0;
		updateFlash();
	}

	private void updateUI() {
		_txtMorseCode.setClickable(true);
		_txtMorseCode.setKeyListener(_keyListener);
		_txtMorseCode.selectAll();
		_btnLedPower.setVisibility(View.VISIBLE);
		_btnSendCancel.setText(getResources().getString(R.string.label_text_send));
	}

	public static void cancelMorseCode() {
		// _txtMorseCode.setClickable(true);
		// _txtMorseCode.setKeyListener(_keyListener);
		// _txtMorseCode.selectAll();
		// _btnLedPower.setVisibility(View.VISIBLE);
		// _btnSendCancel.setText(getResources().getString(R.string.label_text_send));
		turnOff();
		// CameraUtilities.turnOffFlashLight();
		if (_flashHandler != null) {
			_flashHandler.removeMessages(0);
		}
	}

	// @Override
	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	// super.onKeyDown(keyCode, event);
	// if (keyCode == KeyEvent.KEYCODE_BACK) {
	// // cancelMorseCode();
	// }
	// return false;
	// }

	protected OnClickListener _btnFeature_Click = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// cancelMorseCode();
			switchToActivity(FeatureActivity.class);
			finish();
		}
	};

	protected DialogInterface.OnClickListener positiveButton_Click = new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			_preferenceUtilities.setScreenSelected(SCREEN_LIGHT);
			dialog.dismiss();
			cancelMorseCode();
			finish();
		}
	};

	protected DialogInterface.OnClickListener negetiveButton_Click = new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			_dlgQuit.dismiss();
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
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
