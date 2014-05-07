package vsvteam.outsource.android.soundeffect;

import vsvteam.outsource.android.soundeffect.util.SoundEffectSharePreference;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceChangeListener;
import android.util.Log;

public class SoundPreferenceActivity extends PreferenceActivity {
	// ==============================Control Define=================
	// ==============================Class Define===================
	private SoundEffectSharePreference soundEffectSharePreference;
	private Preference preference;
	private Preference bufferSizePreference;
	private Preference sampleRatePreference;

	// ==============================Variable Define================
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preference);

		soundEffectSharePreference = SoundEffectSharePreference.getInstance(this);
		updateLiveCorrectionValue();
		updateBufferSizeValue();
		updateSampleRateValue();
	}

	@SuppressWarnings("deprecation")
	private void updateSampleRateValue() {
		sampleRatePreference = this.findPreference(this.getString(R.string.prefs_sample_rate));
		sampleRatePreference.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				// TODO Auto-generated method stub
				
				if("44100 Hz".equals(newValue.toString())){
					soundEffectSharePreference.putSampleRateValue(44100);
				}else if("22050 Hz".equals(newValue.toString())){
					soundEffectSharePreference.putSampleRateValue(22050);
				}else if("11025 Hz(default)".equals(newValue.toString())){
					soundEffectSharePreference.putSampleRateValue(11025);
				}else if("8000 Hz".equals(newValue.toString())){
					soundEffectSharePreference.putSampleRateValue(8000);
				}
				return true;
			}
		});
	}

	@SuppressWarnings("deprecation")
	private void updateBufferSizeValue() {
		bufferSizePreference = this.findPreference(this.getString(R.string.prefs_buffer_size));
		bufferSizePreference.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				if("1 (default)".equals(newValue.toString())){
					soundEffectSharePreference.putBufferSizeValue(1);
				}else if("2".equals(newValue.toString())){
					soundEffectSharePreference.putBufferSizeValue(2);
				}else if("4".equals(newValue.toString())){
					soundEffectSharePreference.putBufferSizeValue(4);
				}else if("8".equals(newValue.toString())){
					soundEffectSharePreference.putBufferSizeValue(8);
				}else if("16".equals(newValue.toString())){
					soundEffectSharePreference.putBufferSizeValue(16);
				}else if("32".equals(newValue.toString())){
					soundEffectSharePreference.putBufferSizeValue(32);
				}
				return true;
			}
		});

	}

	@SuppressWarnings("deprecation")
	private void updateLiveCorrectionValue() {
		preference = this.findPreference(this.getString(R.string.prefs_live_correction));
		preference.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				if (newValue.equals(true))
					soundEffectSharePreference.putLiveCorrectionValue(true);
				else
					soundEffectSharePreference.putLiveCorrectionValue(false);
				Log.e("adfjahdfs",
						"asdfasjdfhlasd " + soundEffectSharePreference.getLiveCorrectionValue());
				return true;
			}
		});
	}

}
