package com.visva.android.flashlight.utilities;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import com.visva.android.flashlightmaster.R;

public class SoundUtilities {

	private SoundPool _soundPool;
	private boolean _isPlaySound = false;
	private AudioManager _audioManager;
	private int _switch_in_sound;
	private int _switch_out_sound;
	private Context context;
	private PreferenceUtilities _preferenceUtilities;

	public SoundUtilities(Activity activity, PreferenceUtilities preferenceUtilities) {
		this.context = activity.getBaseContext();
		this._preferenceUtilities = preferenceUtilities;
		_audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		_soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
		loadSound();
		_isPlaySound = _preferenceUtilities.isPlaySound();
	}

	private void loadSound() {
		_switch_in_sound = _soundPool.load(context, R.raw.switch_in, 1);
		_switch_out_sound = _soundPool.load(context, R.raw.switch_out, 1);
	}

	public void muteVolume() {
		if (_isPlaySound) {
			_audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
			_isPlaySound = false;
		} else {
			_audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
			_isPlaySound = true;
		}
		_preferenceUtilities.setPlaySound(_isPlaySound);
	}

	public boolean _isPlaySound() {
		return _preferenceUtilities.isPlaySound();
	}

	public void playSwitchOutSound() {
		if (_isPlaySound) {
			_soundPool.play(_switch_out_sound, getVolume(), getVolume(), 1, 0, 1);
		}
	}

	public void playSwitchInSound() {
		if (_isPlaySound) {
			_soundPool.play(_switch_in_sound, getVolume(), getVolume(), 1, 0, 1);
		}
	}

	public float getVolume() {
		float maxVolume = (float) _audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		return maxVolume;
	}
}
