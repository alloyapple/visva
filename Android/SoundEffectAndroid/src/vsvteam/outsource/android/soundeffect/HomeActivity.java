package vsvteam.outsource.android.soundeffect;

import vsvteam.outsource.android.soundeffect.layout.RecordSystem;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.ads.AdRequest;
import com.google.ads.AdView;

public class HomeActivity extends Activity implements OnClickListener {
	private ImageButton btnRecordEffect;
	private ImageButton btnRecordSpeaker;
	private Button btnViewMyRecord;
	int music_column_index;
	int count;
	// start record
	private boolean isRecordStarted = false;
	private static int[] arrBitmapEffectRes;
	private int countEffect = 0;
	private boolean isClickBtnViewMyRecord = false;
	private RecordSystem recordSystem;
	private AdView layoutAds;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_record);
		setUpView();
		Toast.makeText(this, "Tap to the speaker to start recording",
				Toast.LENGTH_LONG).show();
	}

	@SuppressWarnings("deprecation")
	private void setUpView() {
		recordSystem = new RecordSystem(this);
		btnRecordEffect = (ImageButton) findViewById(R.id.img_record_effect);
		btnRecordSpeaker = (ImageButton) findViewById(R.id.img_record_speaker);
		btnRecordSpeaker.setOnClickListener(this);
		btnViewMyRecord = (Button) findViewById(R.id.btn_myrecord_list);
		btnViewMyRecord.setOnClickListener(this);

		// read data animation resource ID
		arrBitmapEffectRes = new int[5];
		for (int i = 0; i < arrBitmapEffectRes.length; i++) {
			arrBitmapEffectRes[i] = getResources().getIdentifier(
					"drawable/anm_sound_" + (i + 1), null, getPackageName());
		}

		layoutAds = (AdView) this.findViewById(R.id.main_adView);
		AdRequest adRequest = new AdRequest();
		adRequest.setTesting(true);
		layoutAds.loadAd(adRequest);
		layoutAds.bringToFront();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		if (v == btnRecordSpeaker) {
			Log.e("adfasdfsd", "asgasdgs " + isRecordStarted);
			if (!isRecordStarted) {
				btnRecordSpeaker
						.setBackgroundResource(R.drawable.songid_mic_active);
				startRecord();
				startEffectForRecord();
				Toast.makeText(this, "Start Record", Toast.LENGTH_SHORT).show();
				isRecordStarted = true;
			} else
				startThreadToCloseRecord();
		} else if (v == btnViewMyRecord) {
			startThreadToCloseRecord();
			isClickBtnViewMyRecord = true;
		}
	}

	private void startEffectForRecord() {
		new CountDownTimer(2000, 400) {

			@Override
			public void onTick(long millisUntilFinished) {
				if (isRecordStarted) {
					btnRecordEffect
							.setBackgroundResource(arrBitmapEffectRes[countEffect]);
					countEffect = ++countEffect % 5;
				}
			}

			@Override
			public void onFinish() {
				if (isRecordStarted)
					startEffectForRecord();
				else
					countEffect = 0;
			}
		}.start();
	}

	private void startRecord() {
		Thread thread = new Thread(new Runnable() {
			public void run() {
				recordSystem.startRecord();
			}
		});
		thread.start();
	}

	private void startThreadToCloseRecord() {
		new CountDownTimer(1000, 1000) {

			@Override
			public void onTick(long millisUntilFinished) {

			}

			@Override
			public void onFinish() {
				if (isRecordStarted) {
					recordSystem.stopRecord();
					isRecordStarted = false;
					btnRecordSpeaker
							.setBackgroundResource(R.drawable.songid_mic_results);
					Toast.makeText(HomeActivity.this, "Close Record",
							Toast.LENGTH_SHORT).show();
					btnRecordEffect
							.setBackgroundResource(R.drawable.anm_sound_1);
				}
				if (isClickBtnViewMyRecord) {
					Intent intent = new Intent(HomeActivity.this,
							MyRecordActivity.class);
					startActivity(intent);
					isClickBtnViewMyRecord = false;
				}
			}
		}.start();
	}
}
