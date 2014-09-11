package com.fgsecure.ujoolt.app.camera.other;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.CamcorderProfile;
import android.media.ExifInterface;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.fgsecure.ujoolt.app.R;
import com.fgsecure.ujoolt.app.camera.util.NoCamera;
import com.fgsecure.ujoolt.app.camera.util.NoSdCard;
import com.fgsecure.ujoolt.app.utillity.ConfigUtility;

public class VideoRecodingActivity extends Activity implements OnClickListener,
		SensorEventListener, SurfaceHolder.Callback {
	// =========================Camera define =====================
	private Camera mCamera;
	// private int mCameraType;
	// ==========================Sensor define =====================
	private SensorManager sensorManager = null;
	private int orientation;
	// private ExifInterface exif;
	// =========================Control define =====================

	private Button btnCameraMode;
	private Button btnCameraType;
	private Button btnCameraRecord;
	private Button btnFlashMode;
	private Button btnGallery;
	private TextView txtTime;

	private RelativeLayout flBtnContainer;
	private SurfaceHolder holder;
	private MediaRecorder recorder;
	private VideoView videoView;
	private SurfaceView cameraView;
	// =========================Variable ===========================
	private boolean recording = false;
	private boolean isCheckRecoding = false;
	private boolean isFlashOn = false;
	private boolean isCameraFront = false;
	private boolean isCameraBack = true;
	private boolean isEnoughRecordTime;

	private static String fileName;
	private static final String PATH_VIDEO = "/sdcard/UjooltCamera/";
	private String selectedVideoPath;
	private int degrees = -1;
	private int countTime = 1;

	// private int widthDevice, heightDevice;
	// private Display display;
	// ==========================Contants============================
	// private static final int GALLERY = 2;
	// private static final int SELECT_PICTURE = 1;
	// ============================Class Define=====================
	// private UjooltSharedPreferences ujooltSharedPreferences;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		setContentView(R.layout.page_camera_recoding);

		// ujooltSharedPreferences = UjooltSharedPreferences.getInstance(this
		// .getBaseContext());
		// Getting the sensor service.
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
//		isCameraBack = getIntent().getExtras().getBoolean("isCameraBack");
		isEnoughRecordTime = true;

		// display = getWindowManager().getDefaultDisplay();
		// widthDevice = display.getWidth();
		// heightDevice = display.getHeight();

		if (!isCameraBack) {
			controlCamera();
		}
		// initialize
		initButton();
		// control video
		startVideoRecording();
		initControl();
		initRecorder();
	}

	private void controlCamera() {
		// TODO Auto-generated method stub
		int numberCamera = Camera.getNumberOfCameras();
		Log.d("number of camera", "" + numberCamera);
		if (numberCamera < 2) {
			Toast.makeText(this, "This device does not support front camera", Toast.LENGTH_SHORT)
					.show();
		}
		// else {
		// mCameraType = 0;
		// }
		// if (mCamera == null)
		// mCamera = getCameraInstance();
	}

	/**
	 * A safe way to get an instance of the Camera object.
	 */
	public static Camera getCameraInstance() {
		Camera c = null;
		try {
			// attempt to get a Camera instance
			c = Camera.open(1);
		} catch (Exception e) {
			// Camera is not available (in use or does not exist)
		}

		// returns null if camera is unavailable
		return c;
	}

	// initialize camera,holder,recorder
	private void initControl() {
		if (videoView == null) {
			videoView = (VideoView) findViewById(R.id.video_view);
		}
		if (cameraView == null) {
			cameraView = (SurfaceView) findViewById(R.id.cameraView);
		}

		holder = cameraView.getHolder();
		holder.addCallback(this);
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	// initilize button
	private void initButton() {
		if (flBtnContainer == null) {
			flBtnContainer = (RelativeLayout) findViewById(R.id.flBtnContainer);
		}
		if (btnCameraMode == null) {
			btnCameraMode = (Button) findViewById(R.id.camera_mode_video);
			btnCameraMode.setOnClickListener(this);
		}
		btnCameraMode.setEnabled(false);
		if (btnCameraType == null) {
			btnCameraType = (Button) findViewById(R.id.camera_type_video);
			btnCameraType.setOnClickListener(this);
		}
		btnCameraType.setEnabled(false);
		if (btnCameraRecord == null) {
			btnCameraRecord = (Button) findViewById(R.id.camera_recoding_video);
			btnCameraRecord.setOnClickListener(this);
		}
		if (btnFlashMode == null) {
			btnFlashMode = (Button) findViewById(R.id.flash_mode_video);
			btnFlashMode.setOnClickListener(this);
		}

		if (btnGallery == null) {
			btnGallery = (Button) findViewById(R.id.gallery_camera_video);
			btnGallery.setOnClickListener(this);
		}
		// if (btnRetake == null) {
		// btnRetake = (Button) findViewById(R.id.ibRetake_video);
		// btnRetake.setOnClickListener(this);
		// }
		// if (btnUse == null) {
		// btnUse = (Button) findViewById(R.id.ibUse_video);
		// btnUse.setOnClickListener(this);
		// }
		if (txtTime == null) {
			txtTime = (TextView) findViewById(R.id.txt_time);
			txtTime.setText("01/15s");
		}
	}

	private void startVideoRecording() {
		// TODO Auto-generated method stub
		new CountDownTimer(500, 500) {

			@Override
			public void onTick(long millisUntilFinished) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onFinish() {
				if (recording) {
					recorder.stop();
					recording = false;
					// Let's initRecorder so we can record again
					initRecorder();
					prepareRecorder();
				} else {
					recording = true;
					recorder.start();
					isEnoughRecordTime = false;
				}
				startCountTimeForVideo();
			}
		}.start();
	}

	// count time max 15s for one video recoding
	private void startCountTimeForVideo() {
		new CountDownTimer(15000, 1000) {

			@Override
			public void onTick(long millisUntilFinished) {
				if (recording) {
					if (isCheckRecoding) {
						btnCameraRecord.setBackgroundResource(R.drawable.ic_shoot_video_1);
						isCheckRecoding = false;
					} else {
						btnCameraRecord.setBackgroundResource(R.drawable.ic_shoot_video_2);
						isCheckRecoding = true;
					}
				} else {

				}
				countTime++;
				if (countTime < 10) {
					if (countTime > 3) {
						isEnoughRecordTime = true;
					}
					txtTime.setText("0" + countTime + "/15s");
				} else {
					txtTime.setText("" + countTime + "/15s");
				}
			}

			@Override
			public void onFinish() {
				if (recording) {
					recorder.stop();
					recording = false;
					Intent i = new Intent(VideoRecodingActivity.this, VideoViewActivity.class);
					String filePath = PATH_VIDEO + fileName;
					i.putExtra("fileName", filePath);
					startActivity(i);
					countTime = 1;// set time to 0
					finish();
				}
			}
		}.start();
	}

	// initilize recorder
	private void initRecorder() {
		recorder = new MediaRecorder();
		recorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
		recorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);
		CamcorderProfile cpHigh = CamcorderProfile.get(CamcorderProfile.QUALITY_LOW);

		cpHigh.videoBitRate = 3000000;
		if (ConfigUtility.scrWidth <= 320) {
			cpHigh.videoFrameHeight = 320;
			cpHigh.videoFrameWidth = 480;
		} else {
			cpHigh.videoFrameHeight = 480;
			cpHigh.videoFrameWidth = 640;
		}

		// cpHigh.videoFrameHeight = ConfigUtility.scrWidth;
		// cpHigh.videoFrameWidth = ConfigUtility.scrHeight;

		recorder.setProfile(cpHigh);

		fileName = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()).toString() + ".mp4";
		// make sure the directory we plan to store the recording in exists
		String pathFile = PATH_VIDEO + fileName;
		recorder.setOutputFile(pathFile);
		// recorder.setMaxDuration(15000); // 15 seconds
		// recorder.setMaxFileSize(10000000); // Approximately 20 megabytes
	}

	private void prepareRecorder() {
		recorder.setPreviewDisplay(holder.getSurface());
		try {
			recorder.prepare();
		} catch (IllegalStateException e) {
			e.printStackTrace();
			finish();
		} catch (IOException e) {
			e.printStackTrace();
			// finish();
		}
	}

	// add controls to a MediaPlayer like play, pause.
	private void controlVideoRecording() {
		MediaController mc = new MediaController(VideoRecodingActivity.this);
		videoView.setMediaController(mc);
		mc.setAnchorView(videoView);
		mc.setMediaPlayer(videoView);
		// Set the path of Video or URI
		videoView.setVideoURI(Uri.parse(PATH_VIDEO + fileName));
		// Set the focus
		videoView.requestFocus();
	}

	// control click events
	public void onClick(View v) {
		if (v == btnCameraMode) {
			if (recording) {
				recorder.stop();
				recording = false;
			}
			btnCameraMode.setBackgroundResource(R.drawable.ic_camera_video_off);
			Intent i = new Intent(VideoRecodingActivity.this, DgCamActivity.class);
			startActivity(i);
			finish();

		} else if (v == btnCameraType) {
			if (!checkSupportFrontCamera()) {
				Toast.makeText(VideoRecodingActivity.this,
						"Your device does not support front camera", Toast.LENGTH_SHORT).show();
			} else {
				if (recording) {
					recorder.stop();
					recording = false;
					initRecorder();
					prepareRecorder();
				}
				if (!isCameraFront) {
					if (mCamera == null)
						mCamera = Camera.open(1);
					isCameraFront = true;
				} else {
					releaseCamera();
					initRecorder();
					prepareRecorder();
					isCameraFront = false;
				}
				recorder.start();
				recording = true;
				startCountTimeForVideo();
			}
		} else if (v == btnCameraRecord) {
			if (isEnoughRecordTime) {
				if (recording) {
					recorder.stop();
					recording = false;
				}
				Intent i = new Intent(VideoRecodingActivity.this, VideoViewActivity.class);
				String filePath = PATH_VIDEO + fileName;
				i.putExtra("fileName", filePath);
				i.putExtra("isPhoto", false);
				i.putExtra("isCameraBack", isCameraBack);
				startActivity(i);
				finish();

				// add controls to a MediaPlayer like play, pause.
				controlVideoRecording();
			}

		} else if (v == btnFlashMode) {
			if (!FlashUtil.isFlashSupport(VideoRecodingActivity.this))
				Toast.makeText(
						VideoRecodingActivity.this,
						" Your devive don't support flash light "
								+ FlashUtil.isFlashSupport(VideoRecodingActivity.this),
						Toast.LENGTH_LONG).show();
			else {

				if (!isFlashOn) {
					isFlashOn = true;
					btnFlashMode.setBackgroundResource(R.drawable.ic_flash_on);
				} else {
					isFlashOn = false;
					btnFlashMode.setBackgroundResource(R.drawable.ic_flash);
				}
				FlashUtil.setFlashlight(mCamera, isFlashOn);
			}

		} else if (v == btnGallery) {
			Toast.makeText(this, "When recording camera,this feature is invisible",
					Toast.LENGTH_SHORT).show();
			// openVideoGallery();
		}
	}

	public void surfaceCreated(SurfaceHolder holder) {
		prepareRecorder();
		initCamera(holder);
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		if (recording) {
			recorder.stop();
			recording = false;
		}
		// recorder.release();
		releaseCamera();
		finish();
	}

	private void initCamera(SurfaceHolder surfaceHolder) {
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.e("selectPath", "" + selectedVideoPath);
		// Test if there is a camera on the device and if the SD card is
		// mounted.
		if (!checkCameraHardware(this)) {
			Intent i = new Intent(this, NoCamera.class);
			startActivity(i);
			finish();
		} else if (!checkSDCard()) {
			Intent i = new Intent(this, NoSdCard.class);
			startActivity(i);
			finish();
		}
		if (selectedVideoPath == null) {
			// Register this class as a listener for the accelerometer sensor
			sensorManager.registerListener(this,
					sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
					SensorManager.SENSOR_DELAY_NORMAL);
		} else {
			if (recording) {
				recorder.stop();
				recording = false;
				confirmVideoFromGallery();
			}
		}
	}

	/**
	 * check device support front camera or not
	 */
	private boolean checkSupportFrontCamera() {
		// TODO Auto-generated method stub
		int numberCamera = 0;
		numberCamera = Camera.getNumberOfCameras();
		if (numberCamera == 1) {
			return false;
		} else
			return true;
	}

	/**
	 * confirm video from gallery
	 */
	private void confirmVideoFromGallery() {
		// TODO Auto-generated method stub
		Intent i = new Intent(VideoRecodingActivity.this, VideoViewActivity.class);
		i.putExtra("isPhoto", false);
		i.putExtra("fileName", selectedVideoPath);
		startActivity(i);
		finish();
	}

	@Override
	protected void onPause() {
		super.onPause();
		// if (selectedVideoPath != null)
		// release the camera immediately on pause event
		releaseCamera();
	}

	// release camera
	private void releaseCamera() {
		if (mCamera != null) {
			mCamera.release(); // release the camera for other applications
			mCamera = null;
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	/**
	 * Putting in place a listener so we can get the sensor data only when
	 * something changes.
	 */
	public void onSensorChanged(SensorEvent event) {
		synchronized (this) {
			if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
				RotateAnimation animation = null;
				if (event.values[0] < 4 && event.values[0] > -4) {
					if (event.values[1] > 0 && orientation != ExifInterface.ORIENTATION_ROTATE_90) {
						// UP
						orientation = ExifInterface.ORIENTATION_ROTATE_90;
						animation = getRotateAnimation(270);
						degrees = 270;
					} else if (event.values[1] < 0
							&& orientation != ExifInterface.ORIENTATION_ROTATE_270) {
						// UP SIDE DOWN
						orientation = ExifInterface.ORIENTATION_ROTATE_270;
						animation = getRotateAnimation(90);
						degrees = 90;
					}
				} else if (event.values[1] < 4 && event.values[1] > -4) {
					if (event.values[0] > 0 && orientation != ExifInterface.ORIENTATION_NORMAL) {
						// LEFT
						orientation = ExifInterface.ORIENTATION_NORMAL;
						animation = getRotateAnimation(0);
						degrees = 0;
					} else if (event.values[0] < 0
							&& orientation != ExifInterface.ORIENTATION_ROTATE_180) {
						// RIGHT
						orientation = ExifInterface.ORIENTATION_ROTATE_180;
						animation = getRotateAnimation(180);
						degrees = 180;
					}
				}
				if (animation != null) {
					btnCameraRecord.startAnimation(animation);
					btnCameraMode.setAnimation(animation);
					btnCameraType.setAnimation(animation);
					btnFlashMode.setAnimation(animation);
					btnGallery.setAnimation(animation);
				}
			}
		}
	}

	/**
	 * Calculating the degrees needed to rotate the image imposed on the button
	 * so it is always facing the user in the right direction
	 * 
	 * @param toDegrees
	 * @return
	 */
	private RotateAnimation getRotateAnimation(float toDegrees) {
		float compensation = 0;

		if (Math.abs(degrees - toDegrees) > 180) {
			compensation = 360;
		}

		// When the device is being held on the left side (default position for
		// a camera) we need to add, not subtract from the toDegrees.
		if (toDegrees == 0) {
			compensation = -compensation;
		}

		// Creating the animation and the RELATIVE_TO_SELF means that he image
		// will rotate on it center instead of a corner.
		RotateAnimation animation = new RotateAnimation(degrees, toDegrees - compensation,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

		// Adding the time needed to rotate the image
		animation.setDuration(250);

		// Set the animation to stop after reaching the desired position. With
		// out this it would return to the original state.
		animation.setFillAfter(true);

		return animation;
	}

	/** Check if this device has a camera */
	private boolean checkCameraHardware(Context context) {
		if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
			// this device has a camera
			return true;
		} else {
			// no camera on this device
			return false;
		}
	}

	private boolean checkSDCard() {
		boolean state = false;
		String sd = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(sd)) {
			state = true;
		}
		return state;
	}

	// private void showDialogCameraError() {
	// AlertDialog.Builder builder = new AlertDialog.Builder(this);
	// builder.setTitle(R.string.caution);
	// Log.i("Language", "" + ujooltSharedPreferences.getLanguage());
	// if (ujooltSharedPreferences.getLanguage().equals("english"))
	// builder.setMessage(R.string.camera_error_msg_english);
	// else
	// builder.setMessage(R.string.camera_error_msg_france);
	// builder.setCancelable(false);
	// builder.setPositiveButton(R.string.Ok,
	// new DialogInterface.OnClickListener() {
	//
	// @Override
	// public void onClick(DialogInterface dialog, int which) {
	// finish();
	// }
	// });
	// builder.show();
	// }
}
