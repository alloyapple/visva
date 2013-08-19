package com.qoppa.samples.viewer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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
import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.samsung.svmc.pdfreader.R;
import com.visva.android.bookreader.feature.BrightnessService;
import com.visva.android.bookreader.feature.CameraPreView;
import com.visva.android.bookreader.feature.FaceDetectionThread;
import com.visva.android.bookreader.io.FileSettings;
import com.visva.android.bookreader.pdfreader.BrightnessSettings;
import com.visva.android.bookreader.pdfreader.FileExplorer;
import com.visva.android.bookreader.pdfreader.PDFReader;


public class SamplePDFViewer extends Activity implements SensorEventListener
{
	private PDFViewer m_PDFViewer;
	
	/*
	 * 1 - Code by HuyDQ
	 */
	private Timer scrollTimer = null;
	private TimerTask scrollerSchedule;
	private int currScrollY = 0;
	private int scrollPos = 0;
	private int verticalScrollMax = 0;
	private int screenHeight;
	private int statusBarHeight;
	
	private SensorManager mSensorManager;
	private long currTimeSensorChanged = 0;
	private long preTimeSensorChanged = 0;
	private float currValueGetFromAccelerometerSensorX = 0.0f;
	private float preValueGetFromAccelerometerSensorX = 0.0f;
	private float currValueGetFromAccelerometerSensorY = 0.0f;
	private float preValueGetFromAccelerometerSensorY = 0.0f;
	private boolean isPreValueGetted = false;
	
	private long lastAutoScroll = 0;
	private long currAutoScroll = 0;
	public static boolean isAutoScrollingChecked = false;
	
	
	private QScrollView pdfViewerScrollView;
	private FrameLayout pdfViewerFrameLayout;
	
	
	private Camera mCamera;
	private CameraPreView mCameraPreView;
	//private FaceDetectionThread mFaceDetectionThread;
	
	public static Activity mActivity;
	public static Context mContext;
	private Window mWindow;
	private ContentResolver mContentResolver;
	private NotificationManager mNotificationManager;
	//private PendingIntent mPendingIntent;
	private BroadcastReceiver brightnessLevelReceiver;
	private BroadcastReceiver batteryLevelReceiver;
	
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
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
		}
	};
	
	/*
	 * End 1
	 */
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_viewer);
		getActionBar().hide();
		
		/*
		 * 2 - Code by HuyDQ
		 * Lock rotation when activity onCreate.
		 * Rotation will be unlocked when service connected
		 */
		if (getResources().getConfiguration().orientation == 1) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		} else {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
		
		mActivity = this;
		mContext = this;
		mWindow = getWindow();
		mContentResolver = getContentResolver();
		mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		//mPendingIntent = PendingIntent.getActivity(mContext, 0, null, 0);
		
		// get Height Dimension of Screen
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		screenHeight = size.y;
		
		// get status bar height
		statusBarHeight = getStatusBarHeight();
		Log.e("MyLog", "Status bar height: " + statusBarHeight);
		
		pdfViewerFrameLayout = (FrameLayout) findViewById(R.id.pdfViewerFrameLayout);
		pdfViewerScrollView = (QScrollView) findViewById(R.id.scrollview);
		pdfViewerScrollView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_UP) {
					//mFaceDetectionThread.resetStatusThread();
					stopAutoScrolling();
					Log.d("MyLog", "Pressed");
				}
				return false;
			}
		});
		
		
		// initialize face detection thread
		//mFaceDetectionThread = new FaceDetectionThread(mCamera, mCameraPreView, mContext, pdfViewerFrameLayout, mNotificationManager, mPendingIntent);
		//Thread mThread = new Thread(mFaceDetectionThread);
		//mThread.start();
		
		mFileSettings = new FileSettings(BrightnessSettings.FOLDER_PATH, BrightnessSettings.FILE_PATH);
		
		/*
		 * End 2
		 */
		
		m_PDFViewer = new PDFViewer(this);
		
		Intent intent = getIntent();
		if(intent.getAction() != null && intent.getAction().equals("android.intent.action.VIEW"))
		{
			//This activity was launched with an Intent to view a specific file
			loadFromUri(intent.getData());
		}
		else
		{
			String filePath = intent.getStringExtra(FileExplorer.OPEN_FILE_PATH);
			
			m_PDFViewer.loadDocument(filePath);
		}
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
		//this occurs when device is rotated or keyboard is exposed/hidden
		super.onConfigurationChanged(newConfig);
		
		m_PDFViewer.onConfigurationChanged(newConfig);
	}
	
	/*
	 * 3 - Modify by HuyDQ
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		//this occurs the first time the "menu" button is pressed
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.options_menu, menu);
	    
	    menu.findItem(R.id.menuItemAutoScroll).setVisible(true);
	    
	    if (isAutoScrollingChecked)
	    	menu.findItem(R.id.menuItemAutoScroll).setChecked(true);
	    
	    return true;
	}
	
	
	@Override 
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		//mFaceDetectionThread.resetStatusThread();
		stopAutoScrolling();
		Log.d("MyLog", "Pressed");
		
		
		if (item.getItemId() == R.id.settings) {
			Intent mIntentSettings = new Intent(SamplePDFViewer.this, BrightnessSettings.class);
			startActivity(mIntentSettings);
			
			return true;
		} else if (item.getItemId() == R.id.menuItemAutoScroll) {
			if (item.isChecked()) {
				stopRegisterAccelerometerSensorEvent();
				item.setChecked(false);
				isAutoScrollingChecked = false;
				Log.e("MyLog", "UnChecked");
			} else {
				registerAccelerometerSensorEvent();
				item.setChecked(true);
				isAutoScrollingChecked = true;
				Log.e("MyLog", "Checked");
			}
		}
		
		return super.onOptionsItemSelected(item);
	}
	/*
	 * End 3
	 */
	
	private void loadFromUri(Uri uri)
	{
		File file = new File(uri.getPath());	
		if(file.exists())
		{
			//intent refers to a local file
			m_PDFViewer.loadDocument(uri.getPath());
		}
		else
		{
			//intent contains an inputstream
			try
			{
				InputStream stream = getContentResolver().openInputStream(uri);
				m_PDFViewer.loadDocument(stream);
			}
			catch (FileNotFoundException e)
			{
				PDFViewer.handleException(this, e);
			}
		}
	}
	
	
	/*
	 * Not use
	 */
	private void showPathEntryDialog()
	{
		final AlertDialog.Builder alert = new AlertDialog.Builder(this);
		final EditText input = new EditText(this);
		input.setText(Environment.getExternalStorageDirectory().getPath()+"/");
		alert.setView(input);
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int whichButton) 
			{
				m_PDFViewer.loadDocument(input.getText().toString());
			}
		});

		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int whichButton) 
			{
				dialog.cancel();
			}
		});
		alert.show();
	}
	
	/*
	 * 4 - Code by HuyDQ
	 */
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return true;
		}
		
		return false;
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.d("Brightness", "PDFViewer onResume");
		bindService(new Intent(SamplePDFViewer.this, BrightnessService.class), mConnection, Context.BIND_AUTO_CREATE);
		
		if (isAutoScrollingChecked) {
			// register Accelerometer Sensor
			registerAccelerometerSensorEvent();
		}
		
		// reset last brightness level value
		BrightnessService.lastBrightnessLevelValue = -1;
		
		if (!mFileSettings.getBrightnessMode().equals("AUTO")) {
			WindowManager.LayoutParams mWindowLayoutParam = mWindow.getAttributes();
			mWindowLayoutParam.screenBrightness = PDFReader.currentBacklight / 255F;
			mWindow.setAttributes(mWindowLayoutParam);
		} else {
			WindowManager.LayoutParams mWindowLayoutParam = mWindow.getAttributes();
			mWindowLayoutParam.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
			mWindow.setAttributes(mWindowLayoutParam);
		}

		// re-initialize variables to check detect face
		PDFReader.lastTimeOnTouched = SystemClock.uptimeMillis();
		//mFaceDetectionThread.resumeThread();
		/*try {
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
		
		// reset time sensor changed
		currTimeSensorChanged = 0;
		preTimeSensorChanged = 0;
		lastAutoScroll = 0;
		currAutoScroll = 0;
		
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.d("Brightness", "PDFViewer onPause");
		
		// unBind service
		if (mConnection != null) {
			mBoundService.stop();
			this.unbindService(mConnection);
		}
		
		// unregister Accelerometer Sensor Event
		stopRegisterAccelerometerSensorEvent();
		
		// unRegister Receiver
		if (brightnessLevelReceiver != null)
			this.unregisterReceiver(brightnessLevelReceiver);
		
		if (batteryLevelReceiver != null) {
			this.unregisterReceiver(batteryLevelReceiver);
		}
		
		// pause FaceDetectionThread and release camera
		//mFaceDetectionThread.pauseThread();
		//mFaceDetectionThread.releaseCamera();
	}
	
	
	// Method register receiver to receive brightness level value from broadcast
	private void brightnessLevel() {
		brightnessLevelReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				int brightnessLevelValue = intent.getIntExtra(BrightnessService.BROADCAST_BRIGHTNESS_KEY, BrightnessService.BROADCAST_BRIGHTNESS_DEFAULT_VALUE);
				mBoundService.setBacklight(brightnessLevelValue, mWindow, mContentResolver);
				Log.d("Brightness", context.getClass().toString() + " Receive: " + brightnessLevelValue);
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
						final AlertDialog.Builder alert = new AlertDialog.Builder(SamplePDFViewer.this);
						
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
	
	private void startAutoScrolling(boolean fromTopToDown){
		currAutoScroll = System.currentTimeMillis();
		
		if ((scrollTimer == null) && ((currAutoScroll - lastAutoScroll) > 1500)) {
			final boolean isFromTopToDown = fromTopToDown;
			
			verticalScrollMax = pdfViewerScrollView.getChildAt(0).getHeight();
			Log.d("MyLog", "" + verticalScrollMax + ", " + screenHeight);
			currScrollY = pdfViewerScrollView.getScrollY();
			
			scrollTimer	= new Timer();
			final Runnable Timer_Tick = new Runnable() {
			    public void run() {
			    	moveScrollView(isFromTopToDown);
			    }
			};
			
			if(scrollerSchedule != null){
				scrollerSchedule.cancel();
				scrollerSchedule = null;
			}
			scrollerSchedule = new TimerTask(){
				@Override
				public void run(){
					runOnUiThread(Timer_Tick);
				}
			};
			
			scrollTimer.schedule(scrollerSchedule, 20, 20);
			lastAutoScroll = currAutoScroll;
		}
	}
    
    private void moveScrollView(boolean fromTopToDown){
    	if (fromTopToDown) {
	    	scrollPos = (int) (pdfViewerScrollView.getScrollY() + 20.0);
			if(scrollPos >= verticalScrollMax) {
				scrollPos =	0;
			}
			pdfViewerScrollView.scrollTo(0,scrollPos);
			
			if ((pdfViewerScrollView.getScrollY() > (currScrollY + screenHeight - 200)) || (pdfViewerScrollView.getScrollY() >= (verticalScrollMax - (screenHeight - statusBarHeight)))) {
				stopAutoScrolling();
				Log.e("MyLog", "Stop scroll");
			} 
    	} else {
    		scrollPos = (int) (pdfViewerScrollView.getScrollY() - 20.0);
			if(scrollPos <= 0) {
				scrollPos =	0;
			}
			pdfViewerScrollView.scrollTo(0,scrollPos);
			
			if ((pdfViewerScrollView.getScrollY() < (currScrollY - screenHeight + 200)) || (pdfViewerScrollView.getScrollY() == 0)) {
				stopAutoScrolling();
			} 
    	}
	}
    
    private void stopAutoScrolling(){
		if (scrollTimer != null) {
			scrollTimer.cancel();
			scrollTimer	=	null;
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		if (isPreValueGetted == false) {
			preValueGetFromAccelerometerSensorX = event.values[0];
			preValueGetFromAccelerometerSensorY = event.values[1];
			isPreValueGetted = true;
		}
		
		if (preTimeSensorChanged == 0)
			preTimeSensorChanged = System.currentTimeMillis();
		
		currTimeSensorChanged = System.currentTimeMillis();
		
		if ((currTimeSensorChanged - preTimeSensorChanged) > 500) {
			currValueGetFromAccelerometerSensorX = event.values[0];
			currValueGetFromAccelerometerSensorY = event.values[1];
			
			if (getResources().getConfiguration().orientation == 1) {
				float deltaValueX = currValueGetFromAccelerometerSensorX - preValueGetFromAccelerometerSensorX;
				
				if (deltaValueX > 1.5f) {
					startAutoScrolling(false);
				} else if (deltaValueX < -1.5f) {
					startAutoScrolling(true);
				}
				
				Log.e("MyLog", "DeltaX = " + deltaValueX);
				
			} else {
				float deltaValueY = currValueGetFromAccelerometerSensorY - preValueGetFromAccelerometerSensorY;
				
				if (deltaValueY > 1.5f) {
					startAutoScrolling(true);
				} else if (deltaValueY < -1.5f) {
					startAutoScrolling(false);
				}
				
				Log.e("MyLog", "DeltaY = " + deltaValueY);
			}
			preTimeSensorChanged = currTimeSensorChanged;
			isPreValueGetted = false;
		}
		
		
	}
	
	private void registerAccelerometerSensorEvent() {
		mSensorManager = (SensorManager) getApplicationContext().getSystemService(Activity.SENSOR_SERVICE);
		List<Sensor> mListSensor = mSensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
		
		if (mListSensor != null && mListSensor.size() > 0) {
			boolean sensorActive = mSensorManager.registerListener(this, mListSensor.get(0), SensorManager.SENSOR_DELAY_NORMAL);
			
			if (!sensorActive) {
				stopRegisterAccelerometerSensorEvent();
				
			} else
				Log.d("Brightness", "Sensor Registed"); 
			
		} else {
			Toast.makeText(this, "Device have not Accelerometer Sensor. Can not use Auto Scroll Feature!", Toast.LENGTH_LONG).show();
		}
	}
	
	private void stopRegisterAccelerometerSensorEvent() {
		if (mSensorManager != null) {
			mSensorManager.unregisterListener(this);
			mSensorManager = null;
		}
	}
	
	// get status bar height
	public int getStatusBarHeight() {
		int result = 0;
		int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
	    if (resourceId > 0) {
	    	result = getResources().getDimensionPixelSize(resourceId);
	    }
	    return result;
	}
	
	/*
	 * End 4
	 */
}
