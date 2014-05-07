package vsvteam.outsource.android.soundeffect.layout;

import vsvteam.outsource.android.soundeffect.MyRecordActivity;
import vsvteam.outsource.android.soundeffect.R;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class LayoutRecord extends Fragment implements OnClickListener {

	/** Called when the activity is first created. */

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
	private static Context context;

	public static Fragment newInstance(Context context) {
		LayoutRecord.context = context;
		LayoutRecord f = new LayoutRecord();
		Resources r = context.getResources();

		// read data animation resource ID
		arrBitmapEffectRes = new int[5];
		for (int i = 0; i < arrBitmapEffectRes.length; i++) {
			arrBitmapEffectRes[i] = r.getIdentifier("drawable/anm_sound_" + (i + 1), null,
					context.getPackageName());
		}
		return f;

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup root = (ViewGroup) inflater.inflate(R.layout.page_record, null);
		initButton(root);
		// init share preference
//		soundEffectSharePreference = SoundEffectSharePreference.getInstance(context);
		Toast.makeText(context, "Tap to the speaker to start recording", Toast.LENGTH_LONG).show();
		return root;
	}

	private void initButton(ViewGroup root) {
		recordSystem = new RecordSystem(getActivity());

		btnRecordEffect = (ImageButton) root.findViewById(R.id.img_record_effect);
		btnRecordSpeaker = (ImageButton) root.findViewById(R.id.img_record_speaker);
		btnRecordSpeaker.setOnClickListener(this);
		btnViewMyRecord = (Button) root.findViewById(R.id.btn_myrecord_list);
		btnViewMyRecord.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == btnRecordSpeaker) {
			Log.e("adfasdfsd", "asgasdgs " + isRecordStarted);
			if (!isRecordStarted) {
//				if (soundEffectSharePreference.getLiveCorrectionValue()
//						&& !soundEffectSharePreference.getHeadSetConnectionValue()) {
//					Toast.makeText(context, "Live correction mode requires headphones",
//							Toast.LENGTH_LONG).show();
//				} else {
					btnRecordSpeaker.setBackgroundResource(R.drawable.songid_mic_active);
					startRecord();
					startEffectForRecord();
					Toast.makeText(getActivity(), "Start Record", Toast.LENGTH_SHORT).show();
					isRecordStarted = true;
//				}
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
					btnRecordEffect.setBackgroundResource(arrBitmapEffectRes[countEffect]);
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
		// - system block
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
					btnRecordSpeaker.setBackgroundResource(R.drawable.songid_mic_results);
					Toast.makeText(getActivity(), "Close Record", Toast.LENGTH_SHORT).show();
					btnRecordEffect.setBackgroundResource(R.drawable.anm_sound_1);
				}
				if (isClickBtnViewMyRecord) {
					Intent intent = new Intent(getActivity(), MyRecordActivity.class);
					startActivity(intent);
					isClickBtnViewMyRecord = false;
				}
			}
		}.start();
	}
}
