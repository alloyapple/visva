package com.fgsecure.ujoolt.app.camera.galaxys;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.CamcorderProfile;
import android.media.ExifInterface;
import android.media.MediaRecorder;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fgsecure.ujoolt.app.R;
import com.fgsecure.ujoolt.app.camera.util.AlbumStorageDirFactory;
import com.fgsecure.ujoolt.app.camera.util.BaseAlbumDirFactory;
import com.fgsecure.ujoolt.app.camera.util.FroyoAlbumDirFactory;
import com.fgsecure.ujoolt.app.camera.util.NoCamera;
import com.fgsecure.ujoolt.app.camera.util.NoSdCard;
import com.fgsecure.ujoolt.app.screen.MainScreenActivity;
import com.fgsecure.ujoolt.app.utillity.DeviceConfig;
import com.fgsecure.ujoolt.app.utillity.JoltHolder;

public class DgCamActivityS extends Activity implements SensorEventListener,
		SurfaceHolder.Callback {

	// ==========================Camera define =====================
	private Camera mCamera;
	private CameraPreviewS mPreview;
	// ==========================Sensor define =====================
	private SensorManager sensorManager = null;
	private int orientation;
	private ExifInterface exif;
	// =========================Control define =====================
	private Button btnClose;
	private Button ibRetake;
	private Button ibUse;

	private Button btnCameraMode;
	private Button btnCameraType;
	private Button btnCameraCapture;
	private Button btnFlashMode;
	private Button btnGallery;

	private TextView textView_countTime;

	private RelativeLayout flBtnContainer;

	// =========================Variable ===========================
	private CountDownTimer myTimer;
	private File sdRoot;
	private String dir;
	private String fileNamePhoto;
	private String selectedImagePath = null;
	private String fileName;
	private int degrees = -1;
	private boolean isFlashOn = false;
	private boolean isCameraBack = true;

	private Bitmap avatar;

	private MediaRecorder mrec;

	private boolean isPhoto = true;
	private boolean isRecoding = false;
	private boolean recording = false;
	private boolean isKeyBack = false;
	private boolean isCheckRecoding = false;
	private int deviceHeight;
	private int countTime = 0;
	private Uri uriImage;
	private String orientationPicture;
	final CharSequence[] items = { "Image", "Video" };
	private AlbumStorageDirFactory mAlbumStorageDirFactory = null;

	// =========================Constants =========================
	private static final String JPEG_FILE_SUFFIX = ".jpg";
	// RequestCode when startActivityForResult
	public static final byte CAPTURE = 1;
	// file path video
	private static final String VIDEO_PATH = "/sdcard/UjooltCamera/";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().clearFlags(
				WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		setContentView(R.layout.camera_screen_s);

		// Setting all the path for the image
		sdRoot = Environment.getExternalStorageDirectory();
		dir = "/sdcard/DCIM/Camera/UjooltCamera/";

		// Getting all the needed elements from the layout
		initButton();

		// Getting the sensor service.
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		// Register this class as a listener for the accelerometer sensor
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_NORMAL);
		// Selecting the resolution of the Android device so we can create a
		// proportional preview
		Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE))
				.getDefaultDisplay();
		deviceHeight = display.getHeight();

		// Add a listener to the Capture button
		btnCameraCapture.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// set event for click capture
				Log.e("isRecoding", "" + isRecoding);
				if (!isRecoding) {
					textView_countTime.setVisibility(View.GONE);
					FlashUtilS.setFlashlight(mCamera, isFlashOn);
					mCamera.takePicture(null, null, mPicture);
				} else {
					if (recording) {
						stopCountDownTimerCaptureVideo();
					} else {
						try {
							startRecording();
							startCountDownTimer();
							textView_countTime.setVisibility(View.VISIBLE);
						} catch (Exception e) {
							String message = e.getMessage();
							Log.i(null, "Problem Start" + message);
							mrec.release();
						}
					}
				}
			}
		});

		// Add a listener to the Retake button
		ibRetake.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Deleting the image from the SD card/
				File discardedPhoto = new File(sdRoot, dir + fileNamePhoto);
				discardedPhoto.delete();

				// Restart the camera preview.
				mCamera.startPreview();

				// Reorganize the buttons on the screen
				flBtnContainer.setVisibility(LinearLayout.VISIBLE);
				ibRetake.setVisibility(LinearLayout.GONE);
				ibUse.setVisibility(LinearLayout.GONE);
			}
		});

		// Add a listener to the Use button
		ibUse.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// scanSdCardPhoto();
				Log.e("file nam", "" + fileNamePhoto);
				Log.e("check file exist",
						"" + new File(dir + fileNamePhoto).exists());
				Bitmap uploadBitmap = null;
				if (DeviceConfig.device_name
						.equalsIgnoreCase(DeviceConfig.SAMSUNG)) {
					Log.d("device ss", "ok");
					uploadBitmap = getBitmapInfoSamsung(avatar);
					MainScreenActivity.bitmapJoltAvatar = uploadBitmap;
					MainScreenActivity.btnUploadPhoto
							.setBackgroundDrawable(new BitmapDrawable(
									uploadBitmap));
				} else {
					MainScreenActivity.bitmapJoltAvatar = avatar;
					MainScreenActivity.btnUploadPhoto
							.setBackgroundDrawable(new BitmapDrawable(avatar));
				}
				if (uriImage != null) {
					String filePath = uriImage.getPath();
					// postToFaceBook(filePath);
					Log.e("filePath", "" + filePath);
					Log.e("check file exist he he ha ha", ""
							+ new File(filePath).exists());
					MainScreenActivity.photoUri = uriImage;
					MainScreenActivity.myPath = filePath;
					JoltHolder.isPhoto = true;

				}
				uploadBitmap = null;
				System.gc();
				finish();
			}
		});

		// add a listenr to the close button
		btnClose.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		// add a listener to the gallery button
		btnGallery.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// show dialog to chose file from gallery
				// openDialogChoseFile();
				Intent i = null;
				if (recording) {
					Toast.makeText(DgCamActivityS.this,
							"Can not use when recording!", Toast.LENGTH_LONG)
							.show();
				} else {
					i = new Intent(DgCamActivityS.this, DialogConfirmS.class);
					i.putExtra("isPhoto", isPhoto);
					startActivity(i);
					finish();
				}
			}
		});

		// add a listener to the flash button
		btnFlashMode.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (recording) {
					Toast.makeText(DgCamActivityS.this,
							"Can not use when recording!", Toast.LENGTH_LONG)
							.show();
				} else {
					if (!FlashUtilS.isFlashSupport(DgCamActivityS.this))
						Toast.makeText(
								DgCamActivityS.this,
								" Your devive don't support flash light "
										+ FlashUtilS
												.isFlashSupport(DgCamActivityS.this),
								Toast.LENGTH_LONG).show();
					else {

						if (!isFlashOn) {
							isFlashOn = true;
							btnFlashMode
									.setBackgroundResource(R.drawable.ic_flash_on);
						} else {
							isFlashOn = false;
							btnFlashMode
									.setBackgroundResource(R.drawable.ic_flash);
						}
					}
				}
			}
		});
		// add a listener to the camera type
		btnCameraType.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (recording) {
					Toast.makeText(DgCamActivityS.this,
							"Can not use when recording!", Toast.LENGTH_LONG)
							.show();
				} else {
					if (!checkSupportFrontCamera()) {
						Toast.makeText(DgCamActivityS.this,
								"Your device does not support front camera",
								Toast.LENGTH_SHORT).show();
					} else {
						/* release camera */
						releaseCamera();
						if (isCameraBack) {
							openFrontFacingCameraGingerbread();
							isCameraBack = false;
						} else {
							createCameraBack();
							isCameraBack = true;
						}
						mCamera.startPreview();
					}
				}
			}
		});

		// add a listener to the camera mode button
		btnCameraMode.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.e("cameraMode", "ok");
				if (recording) {
					Toast.makeText(DgCamActivityS.this,
							"Can not use when recording!", Toast.LENGTH_LONG)
							.show();
				} else {
					if (isRecoding) {
						isRecoding = false;
						btnCameraCapture
								.setBackgroundResource(R.drawable.ic_shoot_photo);
						btnCameraMode
								.setBackgroundResource(R.drawable.ic_camera_video_off);
					} else {
						isRecoding = true;
						btnCameraCapture
								.setBackgroundResource(R.drawable.ic_shoot_video);
						btnCameraMode
								.setBackgroundResource(R.drawable.ic_camera_photo_off);
					}
				}
			}
		});
		// create albumn
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
			mAlbumStorageDirFactory = new FroyoAlbumDirFactory();
		} else {
			mAlbumStorageDirFactory = new BaseAlbumDirFactory();
		}
	}

	// scan source media and image in sdcard
	private void scanSdCardPhoto(String pathFile) {
		MyMediaConnectorClientS client = new MyMediaConnectorClientS(pathFile);
		MediaScannerConnection scanner = new MediaScannerConnection(
				DgCamActivityS.this, client);
		client.setScanner(scanner);
		scanner.connect();
	}

	protected void startRecording() throws IOException {
		mrec = new MediaRecorder(); // Works well
		mCamera.unlock();

		mrec.setCamera(mCamera);

		mrec.setPreviewDisplay(mPreview.getHolder().getSurface());
		mrec.setVideoSource(MediaRecorder.VideoSource.CAMERA);
		mrec.setAudioSource(MediaRecorder.AudioSource.MIC);

		CamcorderProfile cpHigh = CamcorderProfile
				.get(CamcorderProfile.QUALITY_HIGH);
		CamcorderProfile cpLow = CamcorderProfile
				.get(CamcorderProfile.QUALITY_LOW);
		if (isCameraBack) {
			cpHigh.videoBitRate = 2400000;
			cpHigh.videoFrameHeight = 480;
			cpHigh.videoFrameWidth = 640;
			mrec.setProfile(cpHigh);
		} else {
			mrec.setProfile(cpLow);
		}

		mrec.setPreviewDisplay(mPreview.getHolder().getSurface());
		fileName = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date())
				.toString() + ".mp4";
		mrec.setOutputFile(VIDEO_PATH + fileName);

		mrec.prepare();
		mrec.start();
	}

	/**
	 * get device of samsung
	 * 
	 * @return
	 */
	//
	private Bitmap getBitmapInfoSamsung(Bitmap roughBitmap) {
		// calc exact destination size

		int destWidth = 400, destHeight = 600;
		Matrix m = new Matrix();
		RectF inRect = new RectF(0, 0, roughBitmap.getWidth(),
				roughBitmap.getHeight());
		RectF outRect = new RectF(0, 0, destWidth, destHeight);
		m.setRectToRect(inRect, outRect, Matrix.ScaleToFit.CENTER);
		float[] values = new float[9];
		m.getValues(values);

		// resize bitmap
		Bitmap resizedBitmap = Bitmap.createScaledBitmap(roughBitmap,
				(int) (roughBitmap.getWidth() * values[0]),
				(int) (roughBitmap.getHeight() * values[4]), true);

		if ((resizedBitmap.getWidth() > resizedBitmap.getHeight())
				&& "6".endsWith(orientationPicture)) {
			Matrix matrix = new Matrix();
			matrix.postRotate(90);
			resizedBitmap = Bitmap.createBitmap(resizedBitmap, 0, 0,
					resizedBitmap.getWidth(), resizedBitmap.getHeight(),
					matrix, true);
		}
		return resizedBitmap;
	}

	// count time max 15s for one video recoding
	private CountDownTimer startCountTimeForVideo() {
		return new CountDownTimer(15000, 1000) {

			@Override
			public void onTick(long millisUntilFinished) {
				recording = true;
				if (isRecoding) {
					if (isCheckRecoding) {
						btnCameraCapture
								.setBackgroundResource(R.drawable.ic_shoot_video_1);
						isCheckRecoding = false;
					} else {
						btnCameraCapture
								.setBackgroundResource(R.drawable.ic_shoot_video_2);
						isCheckRecoding = true;
					}
				} else {

				}
				countTime++;
				if (countTime < 10)
					textView_countTime.setText("0" + countTime + "/15s");
				else
					textView_countTime.setText("" + countTime + "/15s");
			}

			@Override
			public void onFinish() {
				stopCountDownTimerCaptureVideo();
			}
		};
	}

	/**
	 * stop count down timer capture video
	 */
	private void stopCountDownTimerCaptureVideo() {
		if (isRecoding) {
			mrec.stop();
			mrec.release();
			mrec = null;
			isRecoding = false;
			if (!fileName.equals("")) {
				Log.e("path file", "path " + (VIDEO_PATH + fileName));
				scanSdCardPhoto(VIDEO_PATH + fileName);
			}
			Intent i = null;
			countTime = 0;// set time to 0
			recording = false;
			textView_countTime.setVisibility(View.GONE);
			if (!isKeyBack) {
				i = new Intent(DgCamActivityS.this, VideoViewActivityS.class);
				String filePath = VIDEO_PATH + fileName;
				i.putExtra("fileName", filePath);
				startActivity(i);
				finish();
			}
		}
	}

	// initilize button
	private void initButton() {
		if (btnCameraMode == null) {
			btnCameraMode = (Button) findViewById(R.id.camera_mode);
		}
		if (btnCameraType == null) {
			btnCameraType = (Button) findViewById(R.id.camera_type);
		}
		if (btnCameraCapture == null) {
			btnCameraCapture = (Button) findViewById(R.id.photo_capture);
		}
		if (btnFlashMode == null) {
			btnFlashMode = (Button) findViewById(R.id.flash_mode);
		}
		if (btnGallery == null) {
			btnGallery = (Button) findViewById(R.id.gallery_camera);
		}
		if (ibRetake == null) {
			ibRetake = (Button) findViewById(R.id.ibRetake);
		}
		if (ibUse == null) {
			ibUse = (Button) findViewById(R.id.ibUse);
		}
		if (btnClose == null) {
			btnClose = (Button) findViewById(R.id.close_camera);
		}
		if (flBtnContainer == null) {
			flBtnContainer = (RelativeLayout) findViewById(R.id.flBtnContainer);
		}
		if (textView_countTime == null) {
			textView_countTime = (TextView) findViewById(R.id.textView_count_time);
		}
	}

	// open front face camera
	private void openFrontFacingCameraGingerbread() {
		int cameraCount = 0;
		mCamera = null;
		Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
		cameraCount = Camera.getNumberOfCameras();
		for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
			Camera.getCameraInfo(camIdx, cameraInfo);
			if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
				try {
					mCamera = Camera.open(camIdx);
				} catch (RuntimeException e) {
					Log.e("DgCam",
							"Camera failed to open: " + e.getLocalizedMessage());
				}
			}
		}
		// setCameraView
		setCameraView();
	}

	/* Photo album for this application */
	private String getAlbumName() {
		return getString(R.string.album_name);
	}

	// get the direct link to save image
	private File getAlbumDir() {
		File storageDir = null;
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {

			storageDir = mAlbumStorageDirFactory
					.getAlbumStorageDir(getAlbumName());

			if (storageDir != null) {
				if (!storageDir.mkdirs()) {
					if (!storageDir.exists()) {
						Log.d("CameraSample", "failed to create directory");
						return null;
					}
				}
			}

		} else {
			Log.v(getString(R.string.app_name),
					"External storage is not mounted READ/WRITE.");
		}
		return storageDir;
	}

	// create info of the image taken
	private File createImageFile() throws IOException {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		String imageFileName = timeStamp + "_";
		File albumF = getAlbumDir();
		Log.e("dfghjklkjhgfdsas", "" + imageFileName);
		File imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX,
				albumF);
		return imageF;
	}

	// set up file of image taken
	private File setUpPhotoFile() throws IOException {
		File f = createImageFile();
		// fileNamePhoto = f.getAbsolutePath();
		// Log.d("fileNamePhotosdfsdfsfdfdf", ""+fileNamePhoto);
		return f;
	}

	private void createCameraBack() {
		// Create an instance of Camera
		if (mCamera == null)
			mCamera = getCameraInstance();
		Log.d("Camera", "ok");
		// Setting the right parameters in the camera
		Camera.Parameters params = mCamera.getParameters();
		params.setPictureSize(1600, 1200);
		params.setPictureFormat(PixelFormat.JPEG);
		params.setJpegQuality(90);
		mCamera.setParameters(params);
		// set camera view
		setCameraView();
	}

	private void setCameraView() {
		// Create our Preview view and set it as the content of our activity.
		mPreview = new CameraPreviewS(this, mCamera);
		RelativeLayout surfaceView = null;
		surfaceView = (RelativeLayout) findViewById(R.id.camera_preview);
		// Calculating the width of the preview so it is proportional.

		int width = 0;
		if (deviceHeight > 320 && deviceHeight < 600)
			width = 700;
		else if (deviceHeight <= 320)
			width = 416;
		// Resizing the LinearLayout so we can make a proportional preview. This
		// approach is not 100% perfect because on devices with a really small
		// screen the the image will still be distorted - there is place for
		// improvment.
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				width, deviceHeight);
		surfaceView.setLayoutParams(layoutParams);

		// Adding the camera preview after the FrameLayout and before the button
		// as a separated element.
		surfaceView.addView(mPreview, 0);
	}

	@Override
	protected void onResume() {
		super.onResume();
		// Test if there is a camera on the device and if the SD card is
		// mounted.
		Log.e("cane:dgCamera", "onResume");
		if (getIntent().getExtras() != null)
			if (getIntent().getExtras().getBoolean("isCameraBack")) {
				Log.d("Quay phim sau", "ok");
			} else {
				Log.d("Quay phim trk ", "ok");
			}
		if (!checkCameraHardware(this)) {
			Intent i = new Intent(this, NoCamera.class);
			startActivity(i);
			finish();
		} else if (!checkSDCard()) {
			Intent i = new Intent(this, NoSdCard.class);
			startActivity(i);
			finish();
		}
		if (selectedImagePath == null) {
			// Creating the camera
			createCameraBack();
			if (mCamera != null)
				try {
					mCamera.reconnect();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			if (getIntent().getExtras() != null)
				if (getIntent().getExtras().getBoolean("isPhoto")) {
					isRecoding = false;
					btnCameraCapture
							.setBackgroundResource(R.drawable.ic_shoot_photo);
					btnCameraMode
							.setBackgroundResource(R.drawable.ic_camera_video_off);
				} else {
					isRecoding = true;
					btnCameraCapture
							.setBackgroundResource(R.drawable.ic_shoot_video);
					btnCameraMode
							.setBackgroundResource(R.drawable.ic_camera_photo_off);
				}
		} else {
			confirmImageFromGallery();
		}
		// reset bitmap
		if (avatar != null) {
			avatar.recycle();
			avatar = null;
		}
	}

	/**
	 * check device support front camera or not
	 */
	private boolean checkSupportFrontCamera() {
		int numberCamera = 0;
		numberCamera = Camera.getNumberOfCameras();
		if (numberCamera == 1) {
			return false;
		} else
			return true;
	}

	/**
	 * Confirm image from gallery
	 */
	private void confirmImageFromGallery() {
		Intent i = new Intent(DgCamActivityS.this, VideoViewActivityS.class);
		i.putExtra("isPhoto", isPhoto);
		i.putExtra("fileName", selectedImagePath);
		startActivity(i);
		finish();
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.e("cane:dgCamera", "onPause");
		// release the camera immediately on pause event
		if (mCamera != null)
			mCamera.stopPreview();
		if (mPreview != null)
			mPreview = null;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.e("Cane:DGCameraActivity", "onDestroyed");

		if (mCamera != null) {
			releaseCamera();
		}
		// if (mPreview != null)
		// mPreview = null;
		// removing the inserted view - so when we come back to the app we
		// won't have the views on top of each other.
		RelativeLayout preview = (RelativeLayout) findViewById(R.id.camera_preview);
		preview.removeViewAt(0);
		preview.removeAllViews();
		if (mPicture != null) {
			mPicture = null;
		}
	}

	private void releaseCamera() {
		if (mCamera != null) {
			mCamera.stopPreview();
			mCamera.release(); // release the camera for other applications
			mCamera = null;
		}
	}

	/** Check if this device has a camera */
	private boolean checkCameraHardware(Context context) {
		if (context.getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_CAMERA)) {
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

	/**
	 * A safe way to get an instance of the Camera object.
	 */
	public static Camera getCameraInstance() {
		Camera c = null;
		try {
			// attempt to get a Camera instance
			c = Camera.open();
		} catch (Exception e) {
			// Camera is not available (in use or does not exist)
		}

		// returns null if camera is unavailable
		return c;
	}

	private PictureCallback mPicture = new PictureCallback() {

		public void onPictureTaken(byte[] data, Camera camera) {

			// Replacing the button after a photho was taken.
			flBtnContainer.setVisibility(View.GONE);
			ibRetake.setVisibility(View.VISIBLE);
			ibUse.setVisibility(View.VISIBLE);
			if (avatar == null)
				avatar = resizeImage(data, 400, 600);
			Log.e("avatar ", "" + avatar);
			// File name of the image that we just took.
			fileNamePhoto = new SimpleDateFormat("yyyyMMdd_HHmmss").format(
					new Date()).toString()
					+ ".jpg";
			try {
				setUpPhotoFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			// Creating the directory where to save the image. Sadly in older
			// version of Android we can not get the Media catalog name
			File mkDir = new File(sdRoot, dir);
			mkDir.mkdirs();

			// Main file where to save the data that we recive from the camera
			File pictureFile = new File(sdRoot, dir + fileNamePhoto);
			Log.e("file name photto", "" + fileNamePhoto);
			// save image
			saveImageFromCapture(data);
			try {
				FileOutputStream purge = new FileOutputStream(pictureFile);
				purge.write(data);
				purge.close();
			} catch (IOException e) {
				Log.e("DG_DEBUG", "Error accessing file: " + e.getMessage());
			}

			// Adding Exif data for the orientation. For some strange reason the
			// ExifInterface class takes a string instead of a file.
			try {
				exif = new ExifInterface(dir + fileNamePhoto);
				exif.setAttribute(ExifInterface.TAG_ORIENTATION, ""
						+ orientation);
				orientationPicture = exif
						.getAttribute(ExifInterface.TAG_ORIENTATION);
				Log.d("exifOrientation", "" + orientationPicture);
				exif.saveAttributes();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// scanSdCardPhoto();
			Log.d("check file exit 1 ",
					"" + new File(dir + fileNamePhoto).exists());
			// reset data
			data = null;
			if (isFlashOn) {
				FlashUtilS.setFlashlight(mCamera, false);
			}
		}
	};

	/**
	 * save image from camera capture
	 */
	private void saveImageFromCapture(byte[] data) {
		int imageNum = 0;
		String filePath = "";
		Intent imageIntent = new Intent(
				android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		File imagesFolder = new File(Environment.getExternalStorageDirectory(),
				"UjooltCamera");
		imagesFolder.mkdirs();
		String fileName = "image_" + String.valueOf(imageNum) + ".jpg";
		File output = new File(imagesFolder, fileName);
		while (output.exists()) {
			imageNum++;
			fileName = "image_" + String.valueOf(imageNum) + ".jpg";
			filePath = "/sdcard/UjooltCamera/" + fileName;
			output = new File(imagesFolder, fileName);
		}
		uriImage = Uri.fromFile(output);
		imageIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriImage);

		OutputStream imageFileOS;
		try {
			imageFileOS = getContentResolver().openOutputStream(uriImage);
			imageFileOS.write(data);
			imageFileOS.flush();
			imageFileOS.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		scanSdCardPhoto(filePath);
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
					if (event.values[1] > 0
							&& orientation != ExifInterface.ORIENTATION_ROTATE_90) {
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
					if (event.values[0] > 0
							&& orientation != ExifInterface.ORIENTATION_NORMAL) {
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
					btnCameraCapture.startAnimation(animation);
					btnCameraMode.setAnimation(animation);
					btnCameraType.setAnimation(animation);
					// btnClose.setAnimation(animation);
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
		RotateAnimation animation = new RotateAnimation(degrees, toDegrees
				- compensation, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);

		// Adding the time needed to rotate the image
		animation.setDuration(250);

		// Set the animation to stop after reaching the desired position. With
		// out this it would return to the original state.
		animation.setFillAfter(true);

		return animation;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
	}

	/**
	 * STUFF THAT WE DON'T NEED BUT MUST BE HEAR FOR THE COMPILER TO BE HAPPY.
	 */
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	private Bitmap resizeImage(byte[] data, int destHeight, int destWidth) {
		Bitmap resizedBitmap = null;
		try {

			int inWidth = 0;
			int inHeight = 0;

			// decode image size (decode metadata only, not the whole image)
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;

			// save width and height
			inWidth = options.outWidth;
			inHeight = options.outHeight;
			// byte per pixel = 4
			int sizeOfFile = inWidth * inHeight * 4;
			// 50M = 52428800
			if (sizeOfFile < 0) { // size of file is exceed 50M
				if (getAvailableMemory() - 20480 < 15428800) {
					Log.e("Word Search", "Out of memory");
					return null;
				}
			} else if (sizeOfFile > getAvailableMemory() - 20480) {
				Log.e("Word Search", "Out of memory");
				return null;
			}
			options = new BitmapFactory.Options();
			// calc rought re-size (this is no exact resize)
			options.inSampleSize = Math.max(inWidth / destWidth, inHeight
					/ destHeight);
			options.inPurgeable = true;
			options.inPreferredConfig = Bitmap.Config.RGB_565;
			try {
				Bitmap roughBitmap = null;

				options.inSampleSize = 2;

				roughBitmap = BitmapFactory.decodeByteArray(data, 0,
						data.length, options);

				// calc exact destination size
				Matrix m = new Matrix();
				RectF inRect = new RectF(0, 0, roughBitmap.getWidth(),
						roughBitmap.getHeight());
				RectF outRect = new RectF(0, 0, destWidth, destHeight);
				m.setRectToRect(inRect, outRect, Matrix.ScaleToFit.CENTER);
				float[] values = new float[9];
				m.getValues(values);

				// resize bitmap
				resizedBitmap = Bitmap.createScaledBitmap(roughBitmap,
						(int) (roughBitmap.getWidth() * values[0]),
						(int) (roughBitmap.getHeight() * values[4]), true);
			} catch (Exception e) {
				Log.e("Image", e.getMessage(), e);
			} catch (OutOfMemoryError e) {
				Log.e("Image", e.getMessage(), e);
			}
		} catch (OutOfMemoryError e) {
			Log.e("Image", e.getMessage(), e);
		}
		return resizedBitmap;
	}

	private long getAvailableMemory() {
		MemoryInfo mi = new MemoryInfo();

		ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

		activityManager.getMemoryInfo(mi);

		return mi.availMem;
	}

	/**
	 * set count down timer for set the animation
	 */
	private void startCountDownTimer() {
		if (myTimer == null) {
			myTimer = startCountTimeForVideo();
		}
		myTimer.start();
	}

	/**
	 * stop my count down timer
	 */
	private void finishCountDownTimer() {
		myTimer.onFinish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			isKeyBack = true;
			if (recording)
				finishCountDownTimer();
			finish();
			return true;

		}
		if (keyCode == KeyEvent.KEYCODE_CALL) {
			this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}