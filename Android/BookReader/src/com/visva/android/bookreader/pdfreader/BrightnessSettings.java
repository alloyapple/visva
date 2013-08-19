package com.visva.android.bookreader.pdfreader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import com.samsung.svmc.pdfreader.R;
import com.visva.android.bookreader.feature.BrightnessService;
import com.visva.android.bookreader.feature.CameraPreView;
import com.visva.android.bookreader.feature.FaceDetectionThread;
import com.visva.android.bookreader.io.FileSettings;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Camera;
import android.net.MailTo;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.LinearLayout.LayoutParams;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class BrightnessSettings extends Activity {
	
	private ScrollView mScrollView;
	private FrameLayout setting_FrameLayout;
	private Camera mCamera;
	private CameraPreView mCameraPreView;
	private FaceDetectionThread mFaceDetectionThread;
	
	private ExpandAnimation mExpandAnimationForManual;
	private ExpandAnimation mExpandAnimationForDevice;
	private ExpandAnimation mExpandAnimationForAdvance;
	private RadioButton radioButtonDeviceSetting;
	private RadioButton radioButtonAdvanceSetting;
	private RadioButton radioButtonManualSetting;
	
	private View mLinearBoundSeekbar;
	private View mLinearBoundTxt;
	private View mLinearAdvanceSetting;
	
	public static SeekBar lightControlSeekBar;
	private CheckBox manualCheckbox;
	
	public static TextView mTxtCurrLightSensorValue;
	public static TextView mTxtCurrBrightnessLevelValue;
	private TextView mTxtDeviceSetting;
	
	private String originalMode;
	private int originalBacklight;
	
	/*private Button mBtnSave;
	private Button mBtnCancel;
	private Button mBtnDefault;*/
	
	private ImageView mImgSave;
	private ImageView mImgCancel;
	private ImageView mImgDefault;
	
	private ImageView[] imgViews;
	private AnimationDrawable[] mAnimations;
	
	private Context mContext;
	private Window mWindow;
	private ContentResolver mContentResolver;
	private NotificationManager mNotificationManager;
	private PendingIntent mPendingIntent;
	
	private BroadcastReceiver brightnessLevelReceiver;
	private BroadcastReceiver batteryLevelReceiver;
	
	public static final String CONFIGURATION_DIALOG_CODE = "ConfigurationDialogActivity";
	public static final String FOLDER_PATH = Environment.getExternalStorageDirectory() + "/PDFReader";
	public static final String FILE_PATH = Environment.getExternalStorageDirectory() + "/PDFReader/PDFReaderSetting.txt";
	
	private FileSettings mFileSettings;
	
	private BrightnessService mBoundService;
	private ServiceConnection mConnection = new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mBoundService = ((BrightnessService.LocalBinder)service).getService();
			PDFReader.setBrightnessMode(mFileSettings, mBoundService);
			
			brightnessLevel();
			batteryLevel();
			
			// unlock rotation
//			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
		}
	};

	private Message mMessageAnimation;
	private Handler mHandlerAnimation = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			int key = msg.arg1;
			switch (key) {
			case 1:
				mLinearBoundSeekbar.startAnimation(mExpandAnimationForManual);
				break;
				
			case 2:
				mLinearBoundTxt.startAnimation(mExpandAnimationForDevice);
				break;
				
			case 3:
				mLinearAdvanceSetting.startAnimation(mExpandAnimationForAdvance);
				break;

			default:
				break;
			}
		}
		
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		getActionBar().hide();
		
		/*
		 * Lock rotation when activity onCreate.
		 * Rotation will be unlocked when service connected 
		 */
		if (getResources().getConfiguration().orientation == 1) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		} else {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
		
		mContext = this;
		mWindow = getWindow();
		mContentResolver = getContentResolver();
		mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		mPendingIntent = PendingIntent.getActivity(mContext, 0, null, 0);
		
		setting_FrameLayout = (FrameLayout) findViewById(R.id.settingsFrameLayout);
		
		// initialize face detection thread
		mFaceDetectionThread = new FaceDetectionThread(mCamera, mCameraPreView, mContext, setting_FrameLayout, mNotificationManager, mPendingIntent);
		Thread mThread = new Thread(mFaceDetectionThread);
		mThread.start();
		
		mTxtCurrBrightnessLevelValue = (TextView) findViewById(R.id.txtCurrBrightnessLevelValue);
		
		mTxtCurrLightSensorValue = (TextView) findViewById(R.id.txtCurrLightSensorValue);
		mTxtDeviceSetting = (TextView) findViewById(R.id.txtDeviceSetting);
		
		mLinearAdvanceSetting = (LinearLayout) findViewById(R.id.layoutAdvanceSetting);
		mLinearBoundSeekbar = (LinearLayout) findViewById(R.id.linear_bound_seekbar);
		mLinearBoundTxt = (LinearLayout) findViewById(R.id.linear_bound_txt);
		
		manualCheckbox = (CheckBox) findViewById(R.id.manualCheckbox);
		
		lightControlSeekBar = (SeekBar) findViewById(R.id.lightSeekBar);
		lightControlSeekBar.setMax(255 - 20);
		lightControlSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				PDFReader.currentBacklight = progress + 20;
//				mTxtCurrBrightnessLevelValue.setText("" + PDFReader.currentBacklight);
				PDFReader.currentPercentBrightness = Math.round((float) PDFReader.currentBacklight * 100 / 255);
				mTxtCurrBrightnessLevelValue.setText("" + PDFReader.currentPercentBrightness + " %");
				
				if ( (mBoundService != null) && (mFileSettings.getBrightnessMode().equals("MANUAL"))) {
					mBoundService.setBacklight(PDFReader.currentBacklight, mWindow, mContentResolver);
					Log.d("Brightness", "Setting: " + PDFReader.currentBacklight);
					
					mFaceDetectionThread.resetStatusThread();
				}
			}
		});
		
		radioButtonManualSetting = (RadioButton) findViewById(R.id.manualSetting);
		radioButtonManualSetting.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mLinearBoundSeekbar.getVisibility() == View.GONE) {
					// Expand Animation
					// Device Auto Brightness: arg1 = 2
					if (mLinearBoundTxt.getVisibility() == View.VISIBLE) {
						
						mExpandAnimationForDevice = new ExpandAnimation(mLinearBoundTxt, 500, true);
//						mLinearBoundTxt.startAnimation(mExpandAnimationForDevice);
						
						mMessageAnimation = mHandlerAnimation.obtainMessage();
						mMessageAnimation.arg1 = 2;
						mHandlerAnimation.sendMessage(mMessageAnimation);
						
						radioButtonDeviceSetting.setChecked(false);
						
					// Advance Auto Brightness: arg1 = 3
					} else if (mLinearAdvanceSetting.getVisibility() == View.VISIBLE) {
						
						mExpandAnimationForAdvance = new ExpandAnimation(mLinearAdvanceSetting, 500, true);
//						mLinearAdvanceSetting.startAnimation(mExpandAnimationForAdvance);
						
						mMessageAnimation = mHandlerAnimation.obtainMessage();
						mMessageAnimation.arg1 = 3;
						mHandlerAnimation.sendMessage(mMessageAnimation);
						
						radioButtonAdvanceSetting.setChecked(false);
					} 

					// Manual Auto Brightness: arg1 = 1
					mExpandAnimationForManual = new ExpandAnimation(mLinearBoundSeekbar, 500, false);
//					mLinearBoundSeekbar.startAnimation(mExpandAnimationForManual);
					
					mMessageAnimation = mHandlerAnimation.obtainMessage();
					mMessageAnimation.arg1 = 1;
					mHandlerAnimation.sendMessage(mMessageAnimation);
					
					
					mFileSettings.writeNewConfigToFileSettings("MANUAL", 0);

					lightControlSeekBar.setProgress(PDFReader.currentBacklight - 20);

					if (mBoundService != null) {
						mBoundService.setAutoBrightness(false);
						Log.d("Brightness", "Manual setting: mode change");
					}

					mFaceDetectionThread.resetStatusThread();
				}
			}
		});
		
		
		radioButtonDeviceSetting = (RadioButton) findViewById(R.id.deviceSetting);
		radioButtonDeviceSetting.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mLinearBoundTxt.getVisibility() == View.GONE) {
					// Expand Animation
					// Manual Auto Brightness: arg1 = 1
					if (mLinearBoundSeekbar.getVisibility() == View.VISIBLE) {
						
						mExpandAnimationForManual = new ExpandAnimation(mLinearBoundSeekbar, 500, true);
//						mLinearBoundSeekbar.startAnimation(mExpandAnimationForManual);
						
						mMessageAnimation = mHandlerAnimation.obtainMessage();
						mMessageAnimation.arg1 = 1;
						mHandlerAnimation.sendMessage(mMessageAnimation);
						
						radioButtonManualSetting.setChecked(false);
					
					//	Advance Auto Brightness: arg1 = 3
					} else if (mLinearAdvanceSetting.getVisibility() == View.VISIBLE) {
						
						mExpandAnimationForAdvance = new ExpandAnimation(mLinearAdvanceSetting, 500, true);
//						mLinearAdvanceSetting.startAnimation(mExpandAnimationForAdvance);
						
						mMessageAnimation = mHandlerAnimation.obtainMessage();
						mMessageAnimation.arg1 = 3;
						mHandlerAnimation.sendMessage(mMessageAnimation);
						
						radioButtonAdvanceSetting.setChecked(false);
						
					} 
				
					// Device Auto Brightness: arg1 = 2
					mExpandAnimationForDevice = new ExpandAnimation(mLinearBoundTxt, 500, false);
//					mLinearBoundTxt.startAnimation(mExpandAnimationForDevice);
					
					mMessageAnimation = mHandlerAnimation.obtainMessage();
					mMessageAnimation.arg1 = 2;
					mHandlerAnimation.sendMessage(mMessageAnimation);
					
					mFileSettings.writeNewConfigToFileSettings("AUTO", 0);
					
					if (mBoundService != null) {
						mBoundService.setAutoBrightness(true);
						
						// refresh brightness mode 
						WindowManager.LayoutParams param = mWindow.getAttributes();
						param.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
						mWindow.setAttributes(param);
						
						Log.d("Brightness", "Auto setting: mode change");
						
						mFaceDetectionThread.resetStatusThread();
					}
				}
			}
		});
		
		
		radioButtonAdvanceSetting = (RadioButton) findViewById(R.id.advanceSetting);
		radioButtonAdvanceSetting.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mLinearAdvanceSetting.getVisibility() == View.GONE) {
					// Expand Animation
					// Manual Auto Brightness: arg1 = 1
					if (mLinearBoundSeekbar.getVisibility() == View.VISIBLE) {
						
						mExpandAnimationForManual = new ExpandAnimation(mLinearBoundSeekbar, 500, true);
//						mLinearBoundSeekbar.startAnimation(mExpandAnimationForManual);
						
						mMessageAnimation = mHandlerAnimation.obtainMessage();
						mMessageAnimation.arg1 = 1;
						mHandlerAnimation.sendMessage(mMessageAnimation);
						
						radioButtonManualSetting.setChecked(false);
						
					// Device Auto Brightness: arg = 2	
					} else if (mLinearBoundTxt.getVisibility() == View.VISIBLE) {
						
						mExpandAnimationForDevice = new ExpandAnimation(mLinearBoundTxt, 500, true);
//						mLinearBoundTxt.startAnimation(mExpandAnimationForDevice);
						
						mMessageAnimation = mHandlerAnimation.obtainMessage();
						mMessageAnimation.arg1 = 2;
						mHandlerAnimation.sendMessage(mMessageAnimation);
						
						radioButtonDeviceSetting.setChecked(false);
						
					} 
					
					// Advance Auto Brightness: arg1 = 3
					mExpandAnimationForAdvance = new ExpandAnimation(mLinearAdvanceSetting, 500, false);
//					mLinearAdvanceSetting.startAnimation(mExpandAnimationForAdvance);
					mMessageAnimation = mHandlerAnimation.obtainMessage();
					mMessageAnimation.arg1 = 3;
					mHandlerAnimation.sendMessage(mMessageAnimation);
					
					mFileSettings.writeNewConfigToFileSettings("ADVANCE", 0);
					
					// reset last brightness level value
					BrightnessService.lastBrightnessLevelValue = -1;
					
					if (mBoundService != null) {
						mBoundService.setAutoBrightness(false);
						Log.d("Brightness", "Advance setting: mode change");
					}
					
					mFaceDetectionThread.resetStatusThread();
				}
			}
		});
		
		/*radioButtonAdvanceSetting.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					mTxtDeviceSetting.setVisibility(View.GONE);
					layoutAdvanceSetting.setVisibility(View.VISIBLE);
					lightControlSeekBar.setVisibility(View.GONE);
					
					mFileSettings.writeNewConfigToFileSetting("ADVANCE", mEdtLower, mEdtUper, mEdtLevel);
					
					if (mBoundService != null) {
						mBoundService.setAutoBrightness(false);
						Log.d("Brightness", "Advance setting: mode change");
					}
					
				}
			}
		});*/
		
		
		// Fill out to EditText Modes
		mFileSettings = new FileSettings(FOLDER_PATH, FILE_PATH);
		mFileSettings.readFileSetting(radioButtonManualSetting, radioButtonDeviceSetting, radioButtonAdvanceSetting);
		originalMode = mFileSettings.getBrightnessMode();
		
		if (originalMode.equals("MANUAL")) {
			originalBacklight = PDFReader.currentBacklight;
			lightControlSeekBar.setProgress(PDFReader.currentBacklight - 20);
			
//			mTxtCurrBrightnessLevelValue.setText("" + PDFReader.currentBacklight);
			PDFReader.currentPercentBrightness = Math.round((float) PDFReader.currentBacklight * 100 / 255);
			mTxtCurrBrightnessLevelValue.setText("" + PDFReader.currentPercentBrightness + " %");
			
			Log.d("Brightness", "Curr: " + PDFReader.currentBacklight);
			
//			mTxtDeviceSetting.setVisibility(View.GONE);
//			lightControlSeekBar.setVisibility(View.VISIBLE);
			mLinearBoundSeekbar.setVisibility(View.VISIBLE);
			mLinearBoundTxt.setVisibility(View.GONE);
			mLinearAdvanceSetting.setVisibility(View.GONE);
		} else if (originalMode.equals("AUTO")) {
//			mTxtDeviceSetting.setVisibility(View.VISIBLE);
//			lightControlSeekBar.setVisibility(View.GONE);
			mLinearBoundSeekbar.setVisibility(View.GONE);
			mLinearBoundTxt.setVisibility(View.VISIBLE);
			mLinearAdvanceSetting.setVisibility(View.GONE);
		} else {
//			mTxtDeviceSetting.setVisibility(View.GONE);
//			lightControlSeekBar.setVisibility(View.GONE);
			mLinearBoundSeekbar.setVisibility(View.GONE);
			mLinearBoundTxt.setVisibility(View.GONE);
			mLinearAdvanceSetting.setVisibility(View.VISIBLE);
		}
		
		// Declare Array ImageView
		imgViews = new ImageView[9];
		imgViews[0] = (ImageView) findViewById(R.id.img_cloud_night);
		imgViews[1] = (ImageView) findViewById(R.id.img_office1);
		imgViews[2] = (ImageView) findViewById(R.id.img_office2);
		imgViews[3] = (ImageView) findViewById(R.id.img_office3);
		imgViews[4] = (ImageView) findViewById(R.id.img_office4);
		imgViews[5] = (ImageView) findViewById(R.id.img_cloud1);
		imgViews[6] = (ImageView) findViewById(R.id.img_cloud2);
		imgViews[7] = (ImageView) findViewById(R.id.img_cloud_sun);
		imgViews[8] = (ImageView) findViewById(R.id.img_sun);
		
		// Declare Array AnimationDrawable
		mAnimations = new AnimationDrawable[9];
		
		
		mImgSave = (ImageView) findViewById(R.id.imgSave);
		mImgSave.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String currMode = mFileSettings.getBrightnessMode();
				if ("MANUAL".equals(currMode) && manualCheckbox.isChecked()) {
					try {
//						int currLightSensorValue = Integer.parseInt(mTxtCurrLightSensorValue.getText().toString());
						int currLightSensorValue = 300;
						String strCorrespondingToCurrLightSensorValue = mBoundService.getBrigtnessLevelAtAdvanceBrightnessMode((float) currLightSensorValue);
						String[] tmp = strCorrespondingToCurrLightSensorValue.split(":");
						int brightnessLevelCorrespondingToCurrLightSensorValue = Integer.parseInt(tmp[0]);
						
						// ratio between currentBacklight and BrightnessLevelCorrespondingToCurrLightSensorValue
						float scale = 0.0f; 
						
						// flag indicates positive or negative scale
						boolean isPositive = true;
						
						if (PDFReader.currentBacklight > brightnessLevelCorrespondingToCurrLightSensorValue) {
							scale = (float) (PDFReader.currentBacklight - brightnessLevelCorrespondingToCurrLightSensorValue) / 255;
							scale = Math.round(scale * 100) / 100f;
							
						} else if (PDFReader.currentBacklight < brightnessLevelCorrespondingToCurrLightSensorValue) {
							scale = (float) (brightnessLevelCorrespondingToCurrLightSensorValue - PDFReader.currentBacklight) / 255;
							scale = Math.round(scale * 100) / 100f;
							isPositive = false;
						}
						
						/*//Delete exist PDFReaderSetting and create a default File
						File pdfReaderSetting = new File(FILE_PATH);
						pdfReaderSetting.delete();
						
						mFileSettings.createFileSettingIfNotExist();
						mFileSettings.writeNewConfigToFileSettings("MANUAL", 0);*/
						mFileSettings.writeNewConfigToFileSettings(scale, isPositive);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				finish();
			}
		});
		
		mImgCancel = (ImageView) findViewById(R.id.imgCancel);
		mImgCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				mFileSettings.writeNewConfigToFileSetting(originalMode, mEdtLower, mEdtUper, mEdtLevel);
				mFileSettings.writeNewConfigToFileSettings(originalMode, 0);
				lightControlSeekBar.setProgress(originalBacklight - 20);
				finish();
			}
		});
		
		mImgDefault = (ImageView) findViewById(R.id.imgDefault);
		mImgDefault.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				mLinearBoundSeekbar.setVisibility(View.GONE);
//				mLinearBoundTxt.setVisibility(View.GONE);
//				mLinearAdvanceSetting.setVisibility(View.VISIBLE);
//				radioButtonAdvanceSetting.setChecked(true);
				
				// Animate LinearLayoutes
				if (mLinearAdvanceSetting.getVisibility() != View.VISIBLE) {
					if (mLinearBoundSeekbar.getVisibility() == View.VISIBLE) {
						
						mExpandAnimationForManual = new ExpandAnimation(mLinearBoundSeekbar, 500, true);
						
						mMessageAnimation = mHandlerAnimation.obtainMessage();
						mMessageAnimation.arg1 = 1;
						mHandlerAnimation.sendMessage(mMessageAnimation);
						
						radioButtonManualSetting.setChecked(false);
						
					// Device Auto Brightness: arg1 = 2	
					} else if (mLinearBoundTxt.getVisibility() == View.VISIBLE) {
						
						mExpandAnimationForDevice = new ExpandAnimation(mLinearBoundTxt, 500, true);
						
						mMessageAnimation = mHandlerAnimation.obtainMessage();
						mMessageAnimation.arg1 = 2;
						mHandlerAnimation.sendMessage(mMessageAnimation);
						
						radioButtonDeviceSetting.setChecked(false);
						
					} 
					
					// Advance Auto Brightness: arg1 = 3
					mExpandAnimationForAdvance = new ExpandAnimation(mLinearAdvanceSetting, 500, false);
					mMessageAnimation = mHandlerAnimation.obtainMessage();
					mMessageAnimation.arg1 = 3;
					mHandlerAnimation.sendMessage(mMessageAnimation);
				}
				
				//Delete exist PDFReaderSetting and create a default File
				File pdfReaderSetting = new File(FILE_PATH);
				pdfReaderSetting.delete();
				
				mFileSettings.createFileSettingIfNotExist();
				mFileSettings.readFileSetting(radioButtonManualSetting, radioButtonDeviceSetting, radioButtonAdvanceSetting);
				
				mFaceDetectionThread.resetStatusThread();
				
				BrightnessService.lastBrightnessLevelValue = -1;
			}
		});
		
		
		mScrollView = (ScrollView) findViewById(R.id.scrollView);
		mScrollView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_UP) {
					mFaceDetectionThread.resetStatusThread();
				}
				return false;
			}
		});
	}
	
	
	public void imgViewsOnClick(View v) {
		int i = v.getId();
		Intent mDialogIntent = new Intent(BrightnessSettings.this, ConfigurationSettings.class);
		
		switch (i) {
		case R.id.img_cloud_night:
			mDialogIntent.putExtra(CONFIGURATION_DIALOG_CODE, 0);
			startActivity(mDialogIntent);
			break;
			
		case R.id.img_office1:
			mDialogIntent.putExtra(CONFIGURATION_DIALOG_CODE, 1);
			startActivity(mDialogIntent);
			break;
			
		case R.id.img_office2:
			mDialogIntent.putExtra(CONFIGURATION_DIALOG_CODE, 2);
			startActivity(mDialogIntent);
			break;
			
		case R.id.img_office3:
			mDialogIntent.putExtra(CONFIGURATION_DIALOG_CODE, 3);
			startActivity(mDialogIntent);
			break;
			
		case R.id.img_office4:
			mDialogIntent.putExtra(CONFIGURATION_DIALOG_CODE, 4);
			startActivity(mDialogIntent);
			break;
			
		case R.id.img_cloud1:
			mDialogIntent.putExtra(CONFIGURATION_DIALOG_CODE, 5);
			startActivity(mDialogIntent);
			break;
			
		case R.id.img_cloud2:
			mDialogIntent.putExtra(CONFIGURATION_DIALOG_CODE, 6);
			startActivity(mDialogIntent);
			break;
			
		case R.id.img_cloud_sun:
			mDialogIntent.putExtra(CONFIGURATION_DIALOG_CODE, 7);
			startActivity(mDialogIntent);
			break;
			
		case R.id.img_sun:
			mDialogIntent.putExtra(CONFIGURATION_DIALOG_CODE, 8);
			startActivity(mDialogIntent);
			break;
			
		default:
			break;
		}
	}
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.d("Brightness", "Auto Brightness Setting: onResume");
		// Bind service
		bindService(new Intent(BrightnessSettings.this, BrightnessService.class), mConnection, Context.BIND_AUTO_CREATE);
		
		// reset last brightness level value
		BrightnessService.lastBrightnessLevelValue = -1;
		
		// re-initialize variables to check detect face
		PDFReader.lastTimeOnTouched = SystemClock.uptimeMillis();
		mFaceDetectionThread.resumeThread();
	/*	try {
			PDFReader.defaultTimeout = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT);
		} catch (SettingNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
//		PDFReader.currTimeout = PDFReader.defaultTimeout;

		// reset timeout
		Settings.System.putInt(mContentResolver, Settings.System.SCREEN_OFF_TIMEOUT, PDFReader.defaultTimeout);
		PDFReader.mWakeLock.acquire();
		PDFReader.mWakeLock.release();
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.d("Brightness", "Auto Brightness Setting: onPause");
		
		// Unregister Receiver
		if (brightnessLevelReceiver != null)
			this.unregisterReceiver(brightnessLevelReceiver);
		
		if (batteryLevelReceiver != null) {
			this.unregisterReceiver(batteryLevelReceiver);
		}
		
		// Unbind Service
		if (mConnection != null) {
			mBoundService.stop();
			this.unbindService(mConnection);
		}
		
		// pause FaceDetectionThread and release camera
		mFaceDetectionThread.pauseThread();
		mFaceDetectionThread.releaseCamera();
				
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			mFileSettings.writeNewConfigToFileSetting(originalMode, mEdtLower, mEdtUper, mEdtLevel);
			mFileSettings.writeNewConfigToFileSettings(originalMode, 0);
			lightControlSeekBar.setProgress(originalBacklight - 20);
			finish();
			
			return true;
		}
		
		return false;
	}
	
	// Method register receiver to receive brightness level value from broadcast
	private void brightnessLevel() {
		brightnessLevelReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				int brightnessLevelValue = intent.getIntExtra(BrightnessService.BROADCAST_BRIGHTNESS_KEY, BrightnessService.BROADCAST_BRIGHTNESS_DEFAULT_VALUE);
				mBoundService.setBacklight(brightnessLevelValue, mWindow, mContentResolver);
				lightControlSeekBar.setProgress(brightnessLevelValue - 20);
				Log.d("Brightness", context.getClass().toString() + " Receive: " + brightnessLevelValue);
				
				int position = intent.getIntExtra(BrightnessService.BROAD_CAST_LEVEL_MODE, BrightnessService.BROADCAST_BRIGHTNESS_DEFAULT_VALUE);
				Message mMessage = mHandler.obtainMessage();
				mMessage.arg1 = position;
				mHandler.sendMessage(mMessage);
			}
			
		};
		
		IntentFilter brightnessLevelFilter = new IntentFilter(BrightnessService.BROADCAST_NAME);
		registerReceiver(brightnessLevelReceiver, brightnessLevelFilter);
	}
	
	
	/*
	 * Computes battery level  by registering a receiver to the intent triggered by a battery status/level change.
	 * If battery level remaining < 30%, show warring and set brightness level to 30%
	 */
	private void batteryLevel() {
		batteryLevelReceiver = new BroadcastReceiver() {
			
			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				int rawLevel = intent.getIntExtra("level", -1);
				int scale = intent.getIntExtra("scale", -1);
				int level = -1;
				if ((rawLevel >= 0) && (scale > 0)) {
					level = (rawLevel * 100) / scale;
					
					if ((level <= 30) && (PDFReader.isBatteryLow == false)) {
						final AlertDialog.Builder alert = new AlertDialog.Builder(BrightnessSettings.this);
						
						alert.setTitle("Warrning");
						alert.setIcon(R.drawable.alert);
						alert.setMessage("Battery remaining is lower 30%. If current brightness display is larger 30%, " +
								"you should set brightness display to 30% to saving energy!");
						
						alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() 
						{
							public void onClick(DialogInterface dialog, int whichButton) 
							{
								mBoundService.setAutoBrightness(false);
								mBoundService.setBacklight(76, mWindow, mContentResolver);
								PDFReader.currentBacklight = 76;
								PDFReader.currentPercentBrightness = 30;
								mFileSettings.reWriteFileSetting();
							}
						});
						
						alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								dialog.cancel();
							}
						});
						
						alert.show();
						
						PDFReader.isBatteryLow = true;
						Log.d("Brightness", "Battery LOW");
					} else if (level > 30) {
						PDFReader.isBatteryLow = false;
					}
					
				} 
			}
		};
		
		IntentFilter batteryLevelFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		registerReceiver(batteryLevelReceiver, batteryLevelFilter);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if (event.getAction() == MotionEvent.ACTION_UP) {
			mFaceDetectionThread.resetStatusThread();
			return true;
		} else {
			return false;
		}
	}
	
	private void animateContextImage(int level) {
		for (int i=0; i<9; i++) {
			if (level != i) {
				if ((mAnimations[i] != null) && mAnimations[i].isRunning()) {
					mAnimations[i].stop();
//					imgViews[i].setBackgroundResource(0);
				}
			}
		}
		
		imgViews[0].setImageResource(R.drawable.cloud_night);
		imgViews[1].setImageResource(R.drawable.office1);
		imgViews[2].setImageResource(R.drawable.office2);
		imgViews[3].setImageResource(R.drawable.office3);
		imgViews[4].setImageResource(R.drawable.office4);
		imgViews[5].setImageResource(R.drawable.cloud1);
		imgViews[6].setImageResource(R.drawable.cloud2);
		imgViews[7].setImageResource(R.drawable.cloud_sun);
		imgViews[8].setImageResource(R.drawable.sun);
		
 		switch (level) {
		case 0:
			imgViews[0].setImageResource(0);
			imgViews[0].setBackgroundResource(R.drawable.night_animation);
			imgViews[0].setVisibility(View.VISIBLE);
			mAnimations[0] = (AnimationDrawable) imgViews[0].getBackground();
			
			if (mAnimations[0].isRunning()) {
				mAnimations[0].stop();
			} else {
				mAnimations[0].stop();
				mAnimations[0].start();
			}
			break;
			
		case 1:
			imgViews[1].setImageResource(0);
			imgViews[1].setBackgroundResource(R.drawable.office1_animation);
			imgViews[1].setVisibility(View.VISIBLE);
			mAnimations[1] = (AnimationDrawable) imgViews[1].getBackground();
			
			if (mAnimations[1].isRunning()) {
				mAnimations[1].stop();
			} else {
				mAnimations[1].stop();
				mAnimations[1].start();
			}
			break;
			
		case 2:
			imgViews[2].setImageResource(0);
			imgViews[2].setBackgroundResource(R.drawable.office2_animation);
			imgViews[2].setVisibility(View.VISIBLE);
			mAnimations[2] = (AnimationDrawable) imgViews[2].getBackground();
			
			if (mAnimations[2].isRunning()) {
				mAnimations[2].stop();
			} else {
				mAnimations[2].stop();
				mAnimations[2].start();
			}
			break;
			
		case 3:
			imgViews[3].setImageResource(0);
			imgViews[3].setBackgroundResource(R.drawable.office3_animation);
			imgViews[3].setVisibility(View.VISIBLE);
			mAnimations[3] = (AnimationDrawable) imgViews[3].getBackground();
			
			if (mAnimations[3].isRunning()) {
				mAnimations[3].stop();
			} else {
				mAnimations[3].stop();
				mAnimations[3].start();
			}
			break;
			
		case 4:
			imgViews[4].setImageResource(0);
			imgViews[4].setBackgroundResource(R.drawable.office4_animation);
			imgViews[4].setVisibility(View.VISIBLE);
			mAnimations[4] = (AnimationDrawable) imgViews[4].getBackground();
			
			if (mAnimations[4].isRunning()) {
				mAnimations[4].stop();
			} else {
				mAnimations[4].stop();
				mAnimations[4].start();
			}
			break;
			
		case 5:
			imgViews[5].setImageResource(0);
			imgViews[5].setBackgroundResource(R.drawable.cloud1_animation);
			imgViews[5].setVisibility(View.VISIBLE);
			mAnimations[5] = (AnimationDrawable) imgViews[5].getBackground();
			
			if (mAnimations[5].isRunning()) {
				mAnimations[5].stop();
			} else {
				mAnimations[5].stop();
				mAnimations[5].start();
			}
			break;
			
		case 6:
			imgViews[6].setImageResource(0);
			imgViews[6].setBackgroundResource(R.drawable.cloud2_animation);
			imgViews[6].setVisibility(View.VISIBLE);
			mAnimations[6] = (AnimationDrawable) imgViews[6].getBackground();
			
			if (mAnimations[6].isRunning()) {
				mAnimations[6].stop();
			} else {
				mAnimations[6].stop();
				mAnimations[6].start();
			}
			break;
			
		case 7:
			imgViews[7].setImageResource(0);
			imgViews[7].setBackgroundResource(R.drawable.cloud_sun_animation);
			imgViews[7].setVisibility(View.VISIBLE);
			mAnimations[7] = (AnimationDrawable) imgViews[7].getBackground();
			
			if (mAnimations[7].isRunning()) {
				mAnimations[7].stop();
			} else {
				mAnimations[7].stop();
				mAnimations[7].start();
			}
			break;
			
		case 8:
			imgViews[8].setImageResource(0);
			imgViews[8].setBackgroundResource(R.drawable.sun_animation);
			imgViews[8].setVisibility(View.VISIBLE);
			mAnimations[8] = (AnimationDrawable) imgViews[8].getBackground();
			
			if (mAnimations[8].isRunning()) {
				mAnimations[8].stop();
			} else {
				mAnimations[8].stop();
				mAnimations[8].start();
			}
			break;

		default:
			break;
		}
	}
	
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			int position = msg.arg1;
			
			if (imgViews == null)
				return;
			
			animateContextImage(position);
		}
		
	};
	
}
