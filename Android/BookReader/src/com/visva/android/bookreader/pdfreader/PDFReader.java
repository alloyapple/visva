package com.visva.android.bookreader.pdfreader;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.samsung.svmc.pdfreader.R;
import com.visva.android.bookreader.adapter.ItemAdapter;
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
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.net.MailTo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.os.SystemClock;
import android.os.PowerManager.WakeLock;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class PDFReader extends Activity {
	
	private ListView listOption;
	private List<Item> items = new ArrayList<Item>();
	private ItemAdapter adapter; 
	
	public static Context mContext;
	private Window mWindow;
	public static ContentResolver mContentResolver;
	private NotificationManager mNotificationManager;
	//private PendingIntent mPendingIntent;
	public static final int NOTIFY_AUTODETECTFACE_ID = 1990;
	
	private Camera mCamera;
	private CameraPreView mCameraPreView;
	
	private FrameLayout pDFReader_FrameLayout;
	private PowerManager mPowerManager;
	public static WakeLock mWakeLock;
	public static int defaultTimeout;
	public static int currTimeout;
	
	public static long lastTimeOnTouched = 0;
	public static long currTimeMilisecond = 0;
	//private FaceDetectionThread mFaceDetectionThread;
	
	private BroadcastReceiver brightnessLevelReceiver;
	private BroadcastReceiver batteryLevelReceiver;
	
	public static boolean isBatteryLow = false;
	public static int currentBacklight;
	public static int currentPercentBrightness;
	
	/*
	 * ArrayList store 15 values got from Light Sensor
	 * Using in Advance Brightness Mode
	 * Calculate average of 15 this values to change brightness of monitor
	 */
	public static ArrayList<Integer> listOfSensorValue = new ArrayList<Integer>();
	
	public static final String KEY = "key";
	
	public static final int CODE_RECENT = 1;
	public static final int CODE_SDCARD = 2;
	public static final int CODE_DOWNLOAD = 3;
	
	private FileSettings mFileSettings;
	public static final String BRIHGTNESS_MODE_KEY = "Brightness mode key";
	
	private BrightnessService mBoundService;
	private ServiceConnection mConnection = new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mBoundService = ((BrightnessService.LocalBinder)service).getService();
			setBrightnessMode(mFileSettings, mBoundService);
			
			// register receiver battery level and brightness level
			batteryLevel();
			brightnessLevel();
			
			// unlock rotation
//			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
		}
	};

	private int tmpPadding = 0;
	private Timer paddingTimer;
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
			
			paddingTimer	= new Timer();
			final Runnable Timer_Tick = new Runnable() {
			    public void run() {
			    	tmpPadding ++;
			    	if (tmpPadding <= getStatusBarHeight()) {
			    		listOption.setPadding(0, tmpPadding, 0, 0);
			    	} else {
			    		if (paddingTimer != null) {
				    		paddingTimer.cancel();
				    		paddingTimer = null;
			    		}
			    	}
//			    	Log.e("MyLog", "Padding: "+ tmpPadding);
			    }
			};
			
			TimerTask paddingSchedule = new TimerTask(){
				@Override
				public void run(){
					runOnUiThread(Timer_Tick);
				}
			};
			
			paddingTimer.schedule(paddingSchedule, 20, 20);
		}
		
	};	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		getActionBar().hide();
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		WindowManager.LayoutParams attributes = getWindow().getAttributes();
		attributes.flags |= WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
		getWindow().setAttributes(attributes);
		
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
		mContentResolver = this.getContentResolver();
		mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		//mPendingIntent = PendingIntent.getActivity(mContext, 0, null, 0);
		
		pDFReader_FrameLayout = (FrameLayout) findViewById(R.id.mainFrameLayout);
		mPowerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
		mWakeLock = mPowerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, "WakeLock");
		
		try {
			defaultTimeout = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT);
			currTimeout = defaultTimeout;
		} catch (SettingNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// initialize face detection thread
		//mFaceDetectionThread = new FaceDetectionThread(mCamera, mCameraPreView, mContext, pDFReader_FrameLayout, mNotificationManager, mPendingIntent);
		//Thread mThread = new Thread(mFaceDetectionThread);
		//mThread.start();
		
		mFileSettings = new FileSettings(BrightnessSettings.FOLDER_PATH, BrightnessSettings.FILE_PATH);
		mFileSettings.createFileSettingIfNotExist();
		
		try {
			currentBacklight = Settings.System.getInt(mContentResolver, Settings.System.SCREEN_BRIGHTNESS);
			currentPercentBrightness = Math.round((float) currentBacklight * 100 / 255);
		} catch (SettingNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Item mRecentFilesItem = new Item(R.drawable.recent_file, "RECENTS FILES", "Các tệp tin được mở gần đây", 
				"", null, R.drawable.img_null);
		items.add(mRecentFilesItem);
		
		Item mSdcardItem = new Item(R.drawable.sdcard_2, "SDCARD", "Các tệp lưu trên thẻ SD", 
				"", null, R.drawable.img_null);
		items.add(mSdcardItem);
		
		Item mDownloadItem = new Item(R.drawable.download, "DOWNLOAD", "Các tệp được tải v�? từ Internet", "", 
				null, R.drawable.img_null);
		items.add(mDownloadItem);
		
		adapter = new ItemAdapter(mContext, R.layout.row, items);
		
		listOption = (ListView)findViewById(R.id.listOption);
		View header = (View)getLayoutInflater().inflate(R.layout.main_header, null);
		
		listOption.addHeaderView(header);
		listOption.setAdapter(adapter);
		
		listOption.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				// if click in main_header
				if (position == 0) {
					//mFaceDetectionThread.resetStatusThread();
					return;
				}
				
				else if (position == 1) {
					Intent i = new Intent(PDFReader.this, FileExplorer.class);
					i.putExtra(KEY, CODE_RECENT);
					startActivity(i);
				}
				
				else if (position == 2) {
					Intent i = new Intent(PDFReader.this, FileExplorer.class);
					i.putExtra(KEY, CODE_SDCARD);
					startActivity(i);
				}
				
				else if (position == 3) {
					Intent i = new Intent(PDFReader.this, FileExplorer.class);
					i.putExtra(KEY, CODE_DOWNLOAD);
					startActivity(i);
				}
			}
			
		});
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					synchronized (this) {
						wait(1000);
					}
				} catch (InterruptedException e) { }
				
				Message mMessage = mHandler.obtainMessage();
				mHandler.sendMessage(mMessage);
			}	
		}).start();
		
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.d("Brightness", "PDFReader onResume");
		
		bindService(new Intent(PDFReader.this, BrightnessService.class), mConnection, Context.BIND_AUTO_CREATE);
		
		// reset last brightness level value
		BrightnessService.lastBrightnessLevelValue = -1;
		
		if (!mFileSettings.getBrightnessMode().equals("AUTO")) {
			WindowManager.LayoutParams mWindowLayoutParam = mWindow.getAttributes();
			mWindowLayoutParam.screenBrightness = currentBacklight / 255F;
			mWindow.setAttributes(mWindowLayoutParam);
		} else {
			WindowManager.LayoutParams mWindowLayoutParam = mWindow.getAttributes();
			mWindowLayoutParam.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
			mWindow.setAttributes(mWindowLayoutParam);
		}
		
		// re-initialize variables to check detect face
		lastTimeOnTouched = SystemClock.uptimeMillis();
		//mFaceDetectionThread.resumeThread();
		/*try {
			defaultTimeout = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT);
		} catch (SettingNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
//		currTimeout = defaultTimeout;
		
		// reset timeout
		Settings.System.putInt(mContentResolver, Settings.System.SCREEN_OFF_TIMEOUT, defaultTimeout);
		mWakeLock.acquire();
		mWakeLock.release();
		
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.d("Brightness", "PDFReader onPause");
		
		// unRegister Receiver brightness level
		if (brightnessLevelReceiver != null) {
			this.unregisterReceiver(brightnessLevelReceiver);
			Log.d("Brightness", "PDFReader: unregister Receiver");
		}
		
		if (batteryLevelReceiver != null) {
			this.unregisterReceiver(batteryLevelReceiver);
		}
		
		// unBind Service
		if (mConnection != null) {
			if (mBoundService != null)
				mBoundService.stop();
			this.unbindService(mConnection);
		}
		
		// pause FaceDetectionThread and release camera
		//mFaceDetectionThread.pauseThread();
		//mFaceDetectionThread.releaseCamera();
		
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.options_menu, menu);
		
		menu.findItem(R.id.menuFaceDetectionDemo).setVisible(true);
		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == R.id.settings) {
			Intent mIntentSettings = new Intent(PDFReader.this, BrightnessSettings.class);
			startActivity(mIntentSettings);
			
			return true;
		} else if (item.getItemId() == R.id.menuFaceDetectionDemo) {
			Intent mIntentFaceDetectionDemo = new Intent(PDFReader.this, FaceDetectionDemo.class);
			startActivity(mIntentFaceDetectionDemo);
			
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
	

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		// stop thread
		//mFaceDetectionThread.endThread();
		
		// release static variables
		releaseStaticVariables();
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			final AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setTitle("Exit");
			alert.setIcon(R.drawable.alert);
			alert.setMessage("Do you want exit Application?");
			alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int whichButton) 
				{
					isBatteryLow = false;
					finish();
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
		
		return false;
	}
	
	
	// Set Brightness Mode: Device auto brightness or Advance auto brightness
	public static void setBrightnessMode(FileSettings mFileSettings, BrightnessService mBrightnessService) {
		String brightnessMode = mFileSettings.getBrightnessMode();
		if (brightnessMode.equals("AUTO")) {
			mBrightnessService.setAutoBrightness(true);
		}
		else {
			mBrightnessService.setAutoBrightness(false);
		}
	}
	
	
	// Method register receiver to receive brightness level value from broadcast
	private void brightnessLevel() {
		brightnessLevelReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				// Receive brightness level from broadcast
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
//				context.unregisterReceiver(this);
				int rawLevel = intent.getIntExtra("level", -1);
				int scale = intent.getIntExtra("scale", -1);
				int level = -1;
				if ((rawLevel >= 0) && (scale > 0)) {
					level = (rawLevel * 100) / scale;
					
					if ((level <= 30) && (isBatteryLow == false)) {
						final AlertDialog.Builder alert = new AlertDialog.Builder(PDFReader.this);
						
						alert.setTitle("Warrning");
						alert.setIcon(R.drawable.alert);
						alert.setMessage("Battery remaining is lower 30%. If current brightness display is larger 30%, " +
								"you should set brightness display to 30% to saving energy!");
						
						alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() 
						{
							public void onClick(DialogInterface dialog, int whichButton) 
							{
								// Set Brightness level to 30%
								mBoundService.setAutoBrightness(false);
								mBoundService.setBacklight(76, mWindow, mContentResolver);
								currentBacklight = 76;
								currentPercentBrightness = 30;
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
						
						isBatteryLow = true;
						Log.d("Brightness", "Battery LOW");
					} else if (level > 30) {
						isBatteryLow = false;
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
			//mFaceDetectionThread.resetStatusThread();
			return true;
		} else
			return false;
	}
	
	private void releaseStaticVariables() {
		mContext = null;
		mContentResolver = null;
		mWakeLock = null;
		lastTimeOnTouched = 0;
		currTimeMilisecond = 0;
		isBatteryLow = false;
		currentBacklight = 0;
		currentPercentBrightness = 0;
		
	}
	
	private int getStatusBarHeight() {
		int result = 0;
		int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}
}
