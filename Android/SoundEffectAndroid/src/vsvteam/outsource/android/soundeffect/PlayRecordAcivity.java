package vsvteam.outsource.android.soundeffect;

import java.io.IOException;

import vsvteam.outsource.android.soundeffect.layout.RecordSystem;
import vsvteam.outsource.android.soundeffect.util.SoundEffectSharePreference;
import vsvteam.outsource.android.soundeffect.util.Utilities;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class PlayRecordAcivity extends Activity implements OnClickListener,
		OnCompletionListener, SeekBar.OnSeekBarChangeListener {
	// ==============================Control Define=================
	private Button btnMyRecord;
	private Button btnPlay_Record;
	private TextView txtSongName;
	private SeekBar songProgressBar;
	private TextView songCurrentDurationLabel;
	private TextView songTotalDurationLabel;
	// Media Player
	private MediaPlayer mp;
	// Handler to update UI timer, progress bar etc,.
	private Handler mHandler = new Handler();;
	// ==============================Class Define===================
	private Utilities utils;
	private RecordSystem recordSystem;
	private SoundEffectSharePreference soundEffectSharePreference;
	// ==============================Variable Define================
	private String filePath;
	private boolean isRecordStarted = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_play_record);
		initialize();
	}

	private void initialize() {
		filePath = getIntent().getExtras().getString("filePath");

		Log.e("filePath", "filePath " + filePath);
		btnPlay_Record = (Button) findViewById(R.id.btn_play_record);
		btnMyRecord = (Button) findViewById(R.id.btn_my_record);
		btnPlay_Record.setOnClickListener(this);
		btnMyRecord.setOnClickListener(this);
		txtSongName = (TextView) findViewById(R.id.song_name);
		txtSongName.setSelected(true);
		txtSongName.setText(filePath);
		songProgressBar = (SeekBar) findViewById(R.id.songProgressBar);
		songCurrentDurationLabel = (TextView) findViewById(R.id.songCurrentDurationLabel);
		songTotalDurationLabel = (TextView) findViewById(R.id.songTotalDurationLabel);

		// init class
		mp = new MediaPlayer();
		utils = new Utilities();
		recordSystem = new RecordSystem(this);
		soundEffectSharePreference = SoundEffectSharePreference.getInstance(this);
		// Listeners
		songProgressBar.setOnSeekBarChangeListener(this); // Important
		mp.setOnCompletionListener(this); // Important
	}

	@Override
	public void onClick(View v) {
		if (v == btnPlay_Record) {
			// check for already playing
			if (mp.isPlaying()) {
				if (mp != null) {
					mp.pause();
					// Changing button image to play button
					Drawable img = this.getResources().getDrawable(
							R.drawable.ic_play);
					img.setBounds(0, 0, 40, 40);
					btnPlay_Record.setCompoundDrawables(img, null, null, null);
					btnPlay_Record.setText("Play & Record");

					startThreadToCloseRecord();
				}
			} else {
				// Resume song
				if (mp != null) {
					if (soundEffectSharePreference.getLiveCorrectionValue()
							&& !soundEffectSharePreference
									.getHeadSetConnectionValue()) {
						finish();
					} else {
						mp.start();
						// Changing button image to pause button
						Drawable img = this.getResources().getDrawable(
								R.drawable.ic_stop);
						img.setBounds(0, 0, 40, 40);
						btnPlay_Record.setCompoundDrawables(img, null, null,
								null);
						btnPlay_Record.setText("Stop Music");
						// start record
						startRecord();
						// play music and record
						playSong(filePath);
					}
				}
			}

		} else if (v == btnMyRecord) {
			if (mp.isPlaying()) {
				if (mp != null)
					mp.pause();
				startThreadToCloseRecord();
				Log.e("adfajsdhgfkjsd", "adfushdd2 ");
			} else {
				Log.e("adfajsdhgfkjsd", "adfushdd ");
				Intent intent = new Intent(PlayRecordAcivity.this,
						MyRecordActivity.class);
				startActivity(intent);
				finish();
			}
		}
	}

	/**
	 * Function to play a song
	 * 
	 * @param songIndex
	 *            - index of song
	 * */
	private void playSong(String filePath) {
		// Play song
		try {
			mp.reset();
			mp.setDataSource(filePath);
			mp.prepare();
			mp.start();

			// set Progress bar values
			songProgressBar.setProgress(0);
			songProgressBar.setMax(100);

			// Updating progress bar
			updateProgressBar();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Update timer on seekbar
	 * */
	public void updateProgressBar() {
		mHandler.postDelayed(mUpdateTimeTask, 100);
	}

	/**
	 * Background Runnable thread
	 * */
	private Runnable mUpdateTimeTask = new Runnable() {
		public void run() {
			long totalDuration = mp.getDuration();
			long currentDuration = mp.getCurrentPosition();

			// Displaying Total Duration time
			songTotalDurationLabel.setText(""
					+ utils.milliSecondsToTimer(totalDuration));
			// Displaying time completed playing
			songCurrentDurationLabel.setText(""
					+ utils.milliSecondsToTimer(currentDuration));

			// Updating progress bar
			int progress = (int) (utils.getProgressPercentage(currentDuration,
					totalDuration));
			// Log.d("Progress", ""+progress);
			songProgressBar.setProgress(progress);

			// Running this thread after 100 milliseconds
			mHandler.postDelayed(this, 100);
		}
	};

	/**
	 * 
	 * */
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromTouch) {

	}

	/**
	 * When user starts moving the progress handler
	 * */
	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// remove message Handler from updating progress bar
		mHandler.removeCallbacks(mUpdateTimeTask);
	}

	/**
	 * When user stops moving the progress hanlder
	 * */
	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		mHandler.removeCallbacks(mUpdateTimeTask);
		int totalDuration = mp.getDuration();
		int currentPosition = utils.progressToTimer(seekBar.getProgress(),
				totalDuration);

		// forward or backward to certain seconds
		mp.seekTo(currentPosition);

		// update timer progress again
		updateProgressBar();
	}

	/**
	 * On Song Playing completed if repeat is ON play same song again if shuffle
	 * is ON play random song
	 * */
	@Override
	public void onCompletion(MediaPlayer arg0) {
		if (mp != null) {
			mp.stop();
			// Changing button image to pause button
			Drawable img = this.getResources().getDrawable(R.drawable.ic_play);
			img.setBounds(0, 0, 40, 40);
			btnPlay_Record.setCompoundDrawables(img, null, null, null);
			btnPlay_Record.setText("Play &amp; Record");
			finish();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mp.stop();
	}

	private void startRecord() {
		// - system block
		isRecordStarted = true;
		Thread thread = new Thread(new Runnable() {

			public void run() {
				recordSystem.startRecord();
			}
		});
		thread.start();
	}

	private void startThreadToCloseRecord() {
		new CountDownTimer(2000, 1000) {

			@Override
			public void onTick(long millisUntilFinished) {

			}

			@Override
			public void onFinish() {
				if (isRecordStarted) {
					recordSystem.stopRecord();
					isRecordStarted = false;
					Intent intent = new Intent(PlayRecordAcivity.this,
							MyRecordActivity.class);
					startActivity(intent);
					finish();
				}
			}
		}.start();
	}
}
