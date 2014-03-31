package com.vvmaster.android.lazybird;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.SoundPool;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by VV-MasteR team. Copyright 2012
 */
public class AudioPlayer implements MediaPlayer.OnPreparedListener {
	private static final String TAG = "*** AudioManager";

	private static final String CURRENT_REC_PREFIX = "current_";
	private static final String SAVED_REC_PREFIX = "user_";

	public static int PAGE_FLIP_SOUND = -1;
	public static int TAP_SOUND = 0;
	public static int PAGE1_TAP_BIRD_WITH_FISH = 1;
	public static int PAGE2_TAP_ANY_BIRD = 2;
	public static int PAGE6_TAP_A_LAUGHING_BIRD = 3;
	public static int PAGE8_TAP_ANY_BIRD = 4;
	public static int PAGE10_TAP_LB = 5;
	public static int PAGE11_TAP_AN_AIRPLANE = 6;
	public static int PAGE16_TAP_LB = 7;
	public static int PAGE19_TAP_LB = 8;
	public static int PAGE22_TAP_LB = 9;
	public static int PAGE26_TAP_BIRD_GLASSES = 10;
	public static int PAGE26_TAP_RIGHT_BIRD_OPEN = 11;

	private static AudioPlayer mInstance;

	private SoundPool mSoundPool;
	private AudioManager mAudioManager;
	private MediaPlayer mMediaPlayer;
	private MediaRecorder mMediaRecorder;

	private boolean mStopRequested;

	private boolean mRecordStarted;
	private ArrayList<Integer> mTimestamps;
	private int mNextTimestampId;
	private OnTimestampListener mListener;

	private MediaRecorder.OnInfoListener mOnMediaRecorderInfoListener;

	private Context mContext;

	private Handler mHandler;
	private Runnable mNotification;

	// callbacks
	public static interface OnTimestampListener {
		void onEvent(int time, int index);
	}

	public void setTimestampListener(ArrayList<Integer> timestamps,
			OnTimestampListener listener) {
		mNextTimestampId = 0;
		mTimestamps = timestamps;
		mListener = listener;
	}

	public static AudioPlayer getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new AudioPlayer(context);
		}
		return mInstance;
	}

	private AudioPlayer(Context context) {
		mNotification = new Runnable() {
			public void run() {
				playProgressUpdater();
			}
		};

		mStopRequested = false;
		mRecordStarted = false;
		mHandler = new Handler();
		mSoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
		mAudioManager = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);

		mOnMediaRecorderInfoListener = new MediaRecorder.OnInfoListener() {
			@Override
			public void onInfo(MediaRecorder mediaRecorder, int i, int i1) {
				if (i == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED) {
					mRecordStarted = false;
				}
			}
		};
		// load flip page sound
		try {
			AssetFileDescriptor afd = context.getResources().getAssets()
					.openFd("audio/flip.ogg");
			PAGE_FLIP_SOUND = mSoundPool.load(afd, 1);
		} catch (IOException e) {
			Log.d(TAG,
					"Error during attempt to load PAGE_FLIP_SOUND: "
							+ e.getLocalizedMessage());
		}
		// load tap sound
		try {
			AssetFileDescriptor assets = context.getResources().getAssets()
					.openFd("audio/tap_sound.mp3");
			TAP_SOUND = mSoundPool.load(assets, 1);
		} catch (IOException e) {
			Log.d(TAG,
					"Error during attempt to load TAP_SOUND: "
							+ e.getLocalizedMessage());
		}
		// load page 1 bird with fish sound
		try {
			AssetFileDescriptor assets = context.getResources().getAssets()
					.openFd("audio/page1_tap_bird_with_fish.mp3");
			PAGE1_TAP_BIRD_WITH_FISH = mSoundPool.load(assets, 1);
		} catch (IOException e) {
			Log.d(TAG,
					"Error during attempt to load PAGE1_TAP_BIRD_WITH_FISH: "
							+ e.getLocalizedMessage());
		}
		// load page 2 tap any bird sound
		try {
			AssetFileDescriptor assets = context.getResources().getAssets()
					.openFd("audio/page2_tap_any_bird.mp3");
			PAGE2_TAP_ANY_BIRD = mSoundPool.load(assets, 1);
		} catch (IOException e) {
			Log.d(TAG,
					"Error during attempt to load PAGE2_TAP_ANY_BIRD: "
							+ e.getLocalizedMessage());
		}
		// load page 6 tap a laughing bird sound
		try {
			AssetFileDescriptor assets = context.getResources().getAssets()
					.openFd("audio/page6_tap_a_laughing_bird.mp3");
			PAGE6_TAP_A_LAUGHING_BIRD = mSoundPool.load(assets, 1);
		} catch (IOException e) {
			Log.d(TAG,
					"Error during attempt to load PAGE6_TAP_A_LAUGHING_BIRD: "
							+ e.getLocalizedMessage());
		}
		// load page 8 tap any bird sound
		try {
			AssetFileDescriptor assets = context.getResources().getAssets()
					.openFd("audio/page8_tap_any_bird.mp3");
			PAGE8_TAP_ANY_BIRD = mSoundPool.load(assets, 1);
		} catch (IOException e) {
			Log.d(TAG,
					"Error during attempt to load PAGE8_TAP_ANY_BIRD: "
							+ e.getLocalizedMessage());
		}
		// load page 10 tap lazy bird sound
		try {
			AssetFileDescriptor assets = context.getResources().getAssets()
					.openFd("audio/page10_tap_lb.mp3");
			PAGE10_TAP_LB = mSoundPool.load(assets, 1);
		} catch (IOException e) {
			Log.d(TAG,
					"Error during attempt to load PAGE10_TAP_LB: "
							+ e.getLocalizedMessage());
		}
		// load page 11 tap an airplane sound
		try {
			AssetFileDescriptor assets = context.getResources().getAssets()
					.openFd("audio/page11_tap_an_airpalne.mp3");
			PAGE11_TAP_AN_AIRPLANE = mSoundPool.load(assets, 1);
		} catch (IOException e) {
			Log.d(TAG, "Error during attempt to load PAGE11_TAP_AN_AIRPLANE: "
					+ e.getLocalizedMessage());
		}
		// load page 16 tap lazy bird sound
		try {
			AssetFileDescriptor assets = context.getResources().getAssets()
					.openFd("audio/page16_tap _lb.mp3");
			PAGE16_TAP_LB = mSoundPool.load(assets, 1);
		} catch (IOException e) {
			Log.d(TAG,
					"Error during attempt to load PAGE16_TAP_LB: "
							+ e.getLocalizedMessage());
		}
		// load page 19 tap lazy bird sound
		try {
			AssetFileDescriptor assets = context.getResources().getAssets()
					.openFd("audio/page19_tap_lb.mp3");
			PAGE19_TAP_LB = mSoundPool.load(assets, 1);
		} catch (IOException e) {
			Log.d(TAG,
					"Error during attempt to load PAGE19_TAP_LB: "
							+ e.getLocalizedMessage());
		}
		// load page 22 tap lazy bird sound
		try {
			AssetFileDescriptor assets = context.getResources().getAssets()
					.openFd("audio/page22_tap_lb.mp3");
			PAGE22_TAP_LB = mSoundPool.load(assets, 1);
		} catch (IOException e) {
			Log.d(TAG,
					"Error during attempt to load PAGE22_TAP_LB: "
							+ e.getLocalizedMessage());
		}
		// load page 26 tap bird glasses bird sound
		try {
			AssetFileDescriptor assets = context.getResources().getAssets()
					.openFd("audio/page26_tap bird_glasses.mp3");
			PAGE26_TAP_BIRD_GLASSES = mSoundPool.load(assets, 1);
		} catch (IOException e) {
			Log.d(TAG, "Error during attempt to load PAGE26_TAP_BIRD_GLASSES: "
					+ e.getLocalizedMessage());
		}
		// load page 26 tap right bird open bird sound
		try {
			AssetFileDescriptor assets = context.getResources().getAssets()
					.openFd("audio/page26_tap right bird_open.mp3");
			PAGE26_TAP_RIGHT_BIRD_OPEN = mSoundPool.load(assets, 1);
		} catch (IOException e) {
			Log.d(TAG,
					"Error during attempt to load PAGE26_TAP_RIGHT_BIRD_OPEN: "
							+ e.getLocalizedMessage());
		}

		mContext = context;
	}

	public void onDestroy() {
		mHandler.removeMessages(0);
		mHandler.removeCallbacks(mNotification);
		mHandler = null;

		mTimestamps = null;
		mListener = null;

		mSoundPool.release();
		mSoundPool = null;
		mAudioManager = null;

		if (mMediaPlayer != null) {
			mMediaPlayer.reset();
			mMediaPlayer.release();
			mMediaPlayer = null;
		}

		mContext = null;

		mOnMediaRecorderInfoListener = null;
		mNotification = null;

		mInstance = null;
	}

	// play page flip sound
	public void playPageFlip() {
		float streamVolume = mAudioManager
				.getStreamVolume(AudioManager.STREAM_MUSIC);
		streamVolume = streamVolume
				/ mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		mSoundPool
				.play(PAGE_FLIP_SOUND, streamVolume, streamVolume, 1, 0, 1.0f);
	}

	// play tap sound
	public void playTapSound() {
		float streamVolume = mAudioManager
				.getStreamVolume(AudioManager.STREAM_MUSIC);
		streamVolume = streamVolume
				/ mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		mSoundPool.play(TAP_SOUND, streamVolume, streamVolume, 1, 0, 1.0f);
	}

	// play page 1 with fish sound
	public void playPage1WithFish() {
		float streamVolume = mAudioManager
				.getStreamVolume(AudioManager.STREAM_MUSIC);
		streamVolume = streamVolume
				/ mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		mSoundPool.play(PAGE1_TAP_BIRD_WITH_FISH, streamVolume, streamVolume,
				1, 0, 1.0f);
	}

	// play page 2 tap any bird sound
	public void playPage2TapAnyBird() {
		float streamVolume = mAudioManager
				.getStreamVolume(AudioManager.STREAM_MUSIC);
		streamVolume = streamVolume
				/ mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		mSoundPool.play(PAGE2_TAP_ANY_BIRD, streamVolume, streamVolume, 1, 0,
				1.0f);
	}

	// play page 6 tap a laughing bird sound
	public void playPage6TapALaughingBird() {
		float streamVolume = mAudioManager
				.getStreamVolume(AudioManager.STREAM_MUSIC);
		streamVolume = streamVolume
				/ mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		mSoundPool.play(PAGE6_TAP_A_LAUGHING_BIRD, streamVolume, streamVolume,
				1, 0, 1.0f);
	}

	// play page 8 tap a laughing bird sound
	public void playPage8TapALaughingBird() {
		float streamVolume = mAudioManager
				.getStreamVolume(AudioManager.STREAM_MUSIC);
		streamVolume = streamVolume
				/ mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		mSoundPool.play(PAGE8_TAP_ANY_BIRD, streamVolume, streamVolume, 1, 0,
				1.0f);
	}

	// play page 10 tap lazy bird sound
	public void playPage10TapLB() {
		float streamVolume = mAudioManager
				.getStreamVolume(AudioManager.STREAM_MUSIC);
		streamVolume = streamVolume
				/ mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		mSoundPool.play(PAGE10_TAP_LB, streamVolume, streamVolume, 1, 0, 1.0f);
	}

	// play page 11 tap an airplane sound
	public void playPage11TapAnAirPlane() {
		float streamVolume = mAudioManager
				.getStreamVolume(AudioManager.STREAM_MUSIC);
		streamVolume = streamVolume
				/ mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		mSoundPool.play(PAGE11_TAP_AN_AIRPLANE, streamVolume, streamVolume, 1,
				0, 1.0f);
	}

	// play page 16 tap lazy bird sound
	public void playPage16TapLB() {
		float streamVolume = mAudioManager
				.getStreamVolume(AudioManager.STREAM_MUSIC);
		streamVolume = streamVolume
				/ mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		mSoundPool.play(PAGE16_TAP_LB, streamVolume, streamVolume, 1, 0, 1.0f);
	}

	// play page 19 tap lazy bird sound
	public void playPage19TapLB() {
		float streamVolume = mAudioManager
				.getStreamVolume(AudioManager.STREAM_MUSIC);
		streamVolume = streamVolume
				/ mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		mSoundPool.play(PAGE19_TAP_LB, streamVolume, streamVolume, 1, 0, 1.0f);
	}

	// play page 22 tap lazy bird sound
	public void playPage22TapLB() {
		float streamVolume = mAudioManager
				.getStreamVolume(AudioManager.STREAM_MUSIC);
		streamVolume = streamVolume
				/ mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		mSoundPool.play(PAGE22_TAP_LB, streamVolume, streamVolume, 1, 0, 1.0f);
	}

	// play page 26 tap bird glasses sound
	public void playPage26TapBirdGlasses() {
		float streamVolume = mAudioManager
				.getStreamVolume(AudioManager.STREAM_MUSIC);
		streamVolume = streamVolume
				/ mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		mSoundPool.play(PAGE26_TAP_BIRD_GLASSES, streamVolume, streamVolume, 1,
				0, 1.0f);
	}

	// play page 26 tap right bird open sound
	public void playPage26TapRightBirdOpen() {
		float streamVolume = mAudioManager
				.getStreamVolume(AudioManager.STREAM_MUSIC);
		streamVolume = streamVolume
				/ mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		mSoundPool.play(PAGE26_TAP_RIGHT_BIRD_OPEN, streamVolume, streamVolume,
				1, 0, 1.0f);
	}

	public void playSoundTap(int idsound) {
		switch (idsound) {
		case PageData.TAP_SOUND:
			playTapSound();
			break;
		case PageData.PAGE1_TAP_BIRD_WITH_FISH:
			playPage1WithFish();
			break;
		case PageData.PAGE2_TAP_ANY_BIRD:
			playPage2TapAnyBird();
			break;
		case PageData.PAGE6_TAP_A_LAUGHING_BIRD:
			playPage6TapALaughingBird();
			break;
		case PageData.PAGE8_TAP_ANY_BIRD:
			playPage8TapALaughingBird();
			break;
		case PageData.PAGE10_TAP_LB:
			playPage10TapLB();
			break;
		case PageData.PAGE11_TAP_AN_AIRPLANE:
			playPage11TapAnAirPlane();
			break;
		case PageData.PAGE16_TAP_LB:
			playPage16TapLB();
			break;
		case PageData.PAGE19_TAP_LB:
			playPage19TapLB();
			break;
		case PageData.PAGE22_TAP_LB:
			playPage22TapLB();
			break;
		case PageData.PAGE26_TAP_BIRD_GLASSES:
			playPage26TapBirdGlasses();
			break;
		case PageData.PAGE26_TAP_RIGHT_BIRD_OPEN:
			playPage26TapRightBirdOpen();
			break;
		}

	}

	public void stop() {
		if (mMediaPlayer.isPlaying())
			mMediaPlayer.stop();
		else
			mStopRequested = true;
	}

	public void pause() {
		if (mMediaPlayer.isPlaying())
			mMediaPlayer.pause();
	}

	public void start() {
		if (!mMediaPlayer.isPlaying())
			mMediaPlayer.start();
	}

	public void playPage(String pageId, boolean own) {
		if (own) {
			File currentFile = new File(getDataStorageDir().getAbsolutePath()
					+ "/" + CURRENT_REC_PREFIX + pageId);
			File savedFile = new File(getDataStorageDir().getAbsolutePath()
					+ "/" + SAVED_REC_PREFIX + pageId);
			preparePlayerWithExternalTrack(currentFile.exists() ? currentFile
					.getAbsolutePath() : savedFile.getAbsolutePath());
		} else {
			preparePlayerWithAsset("audio/" + pageId + ".mp3", false);
		}
	}

	public void playMainMenu() {
		preparePlayerWithAsset("audio/main-menu.mp3", true);
	}

	private void preparePlayerWithAsset(String filename, boolean looping) {
		try {
			AssetFileDescriptor afd = mContext.getResources().getAssets()
					.openFd(filename);
			createMediaPlayerIfNeeded();
			mMediaPlayer.setDataSource(afd.getFileDescriptor(),
					afd.getStartOffset(), afd.getLength());
			mMediaPlayer.setLooping(looping);
			mMediaPlayer.prepareAsync();
		} catch (IOException e) {
			Log.d(TAG, "Cannot open file descriptor for " + filename);
		}
	}

	private void preparePlayerWithExternalTrack(String filename) {
		try {
			createMediaPlayerIfNeeded();
			mMediaPlayer.setDataSource(filename);
			mMediaPlayer.prepareAsync();
		} catch (IOException e) {
			Log.d(TAG, "Cannot open file descriptor for " + filename);
		}
	}

	private void createMediaPlayerIfNeeded() {
		mStopRequested = false;
		if (mMediaPlayer == null) {
			mMediaPlayer = new MediaPlayer();
			mMediaPlayer.setOnPreparedListener(this);
		} else {
			mMediaPlayer.reset();
			mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		}
	}

	private void createMediaRecorderIfNeeded() {
		if (mMediaRecorder == null) {
			mMediaRecorder = new MediaRecorder();
		} else {
			mMediaRecorder.reset();
		}
		mMediaRecorder.setOnInfoListener(mOnMediaRecorderInfoListener);
		mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		mMediaRecorder.setMaxDuration(2 * 60 * 1000);
		mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
	}

	public void onPrepared(MediaPlayer player) {
		mNextTimestampId = 0;
		if (mStopRequested) {
			mStopRequested = false;
			return;
		}

		if (!mMediaPlayer.isPlaying())
			mMediaPlayer.start();
		playProgressUpdater();
	}

	private void playProgressUpdater() {
		if (mMediaPlayer.isPlaying() && mTimestamps != null
				&& mNextTimestampId < mTimestamps.size()) {

			int pos = mMediaPlayer.getCurrentPosition();
			if (pos >= mTimestamps.get(mNextTimestampId) && mListener != null) {
				mListener.onEvent(pos, mNextTimestampId);
				++mNextTimestampId;
			}
			mHandler.removeMessages(0);
			mHandler.removeCallbacks(mNotification);
			mHandler.postDelayed(mNotification, 50);
		}
	}

	private void startFakePlay() {
		mHandler.removeMessages(0);
		mHandler.removeCallbacks(mNotification);
		mNextTimestampId = 0;
		triggerNextTimestamp(true);
	}

	private void stopFakePlay() {
		mHandler.removeMessages(0);
		mHandler.removeCallbacks(mNotification);
	}

	private void triggerNextTimestamp(boolean isFirstTime) {
		if (mTimestamps != null && mNextTimestampId < mTimestamps.size()) {
			if (mListener != null && !isFirstTime) {
				mListener.onEvent(mTimestamps.get(mNextTimestampId),
						mNextTimestampId);
			}
			if (!isFirstTime) {
				++mNextTimestampId;
			}
			if (mNextTimestampId < mTimestamps.size()) {
				Runnable notification = new Runnable() {
					@Override
					public void run() {
						triggerNextTimestamp(false);
					}
				};
				mHandler.postDelayed(
						notification,
						mNextTimestampId == 0 ? mTimestamps
								.get(mNextTimestampId) : mTimestamps
								.get(mNextTimestampId)
								- mTimestamps.get(mNextTimestampId - 1));
			}
		}
	}

	public void startRecording(String pageId) {
		String state = Environment.getExternalStorageState();
		if (!Environment.MEDIA_MOUNTED.equals(state)
				|| Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			Log.d(TAG,
					"Unable to start recording: external storage is not available");
			return;
		}
		mRecordStarted = true;

		File tmpFile = new File(getDataStorageDir(), CURRENT_REC_PREFIX
				+ pageId);

		try {
			createMediaRecorderIfNeeded();
			mMediaRecorder.setOutputFile(tmpFile.getAbsolutePath());
			mMediaRecorder.prepare();
			mMediaRecorder.start();
			startFakePlay();
		} catch (Exception e) {
			Log.d(TAG, "Unable to start recording: " + e.getLocalizedMessage());
		}
	}

	public void stopRecording() {
		if (mMediaRecorder != null && mRecordStarted) {
			stopFakePlay();
			mMediaRecorder.stop();
			mRecordStarted = false;
		}
	}

	public void saveRecording() {
		stopRecording();

		File[] files = getDataStorageDir().listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File file, String s) {
				return s.startsWith(CURRENT_REC_PREFIX);
			}
		});
		for (File f : files) {
			String parent = f.getParent();
			String absoluteName = f.getName();
			absoluteName = absoluteName.replace(CURRENT_REC_PREFIX,
					SAVED_REC_PREFIX);
			f.renameTo(new File(parent, absoluteName));
		}
	}

	public void deleteRecording() {
		stopRecording();

		File[] files = getDataStorageDir().listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File file, String s) {
				return s.startsWith(CURRENT_REC_PREFIX);
			}
		});
		for (File f : files) {
			f.delete();
		}
	}

	public boolean hasOwnRecording(String pageId) {
		File file = new File(getDataStorageDir(), SAVED_REC_PREFIX + pageId);
		return file.exists();
	}

	private File getDataStorageDir() {
		File dataDir = new File(Environment.getExternalStorageDirectory()
				+ "/Android/data/" + mContext.getPackageName() + "/files/");
		dataDir.mkdirs();
		return dataDir;
	}
}
