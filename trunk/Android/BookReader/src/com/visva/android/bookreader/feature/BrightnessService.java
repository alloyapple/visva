package com.visva.android.bookreader.feature;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import com.visva.android.bookreader.io.FileSettings;
import com.visva.android.bookreader.pdfreader.BrightnessSettings;
import com.visva.android.bookreader.pdfreader.PDFReader;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Toast;

public class BrightnessService extends Service implements SensorEventListener {
	
	private float currLightSensorValue;
	private int currBrightnessLevelValue;
	public static int lastBrightnessLevelValue = -1;
	private long lastUpdate = -1;
	
	// light value of current display
	private int currBacklight = -1;
	private int averageValue;
	
	private FileSettings mFileSettings;
	private static final String CMD_GRAND = "cat /sys/devices/platform/panel/backlight/panel/actual_brightness";
	private static final String CMD_NOTE2 = "cat /sys/devices/platform/s5p-dsim.0/ea8061/backlight/panel/actual_brightness";
	
	private BroadcastReceiver batteryLevelReceiver;
	
	public static final String BROADCAST_NAME = "com.samsung.svmc.pdfreader.brightness";
	
	public static final String BROADCAST_BRIGHTNESS_KEY = "Brightness ";
	public static final int BROADCAST_BRIGHTNESS_DEFAULT_VALUE = -1;
	
	public static final String BROADCAST_BATTERY_KEY = "Battery";
	public static final String BROADCAST_BATTERY_LOWER = "Battery low";
	
	public static final String BROAD_CAST_LEVEL_MODE = "broad cast level mode";
	private Intent mBroadcastIntent;
	
	private final IBinder mBinder = new LocalBinder();
	private SensorManager mSensorManager;
	
	public class LocalBinder extends Binder{
		public BrightnessService getService(){
			return BrightnessService.this;
		}
	}
	

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		Log.d("Brightness", "Service Started");
		
		// register receiver battery level
//		batteryLevel();
		
		mBroadcastIntent = new Intent(BROADCAST_NAME);
		
		mFileSettings = new FileSettings(BrightnessSettings.FOLDER_PATH, BrightnessSettings.FILE_PATH);
//		mAutoBrightnessSettings = new AutoBrightnessSettings();
		
		mSensorManager = (SensorManager) getApplicationContext().getSystemService(Activity.SENSOR_SERVICE);
		List<Sensor> mListSensor = mSensorManager.getSensorList(Sensor.TYPE_LIGHT);
		
		if (mListSensor != null && mListSensor.size() > 0) {
			boolean sensorActive = mSensorManager.registerListener(BrightnessService.this, mListSensor.get(0), SensorManager.SENSOR_DELAY_UI);
			
			if (!sensorActive) {
				stop();
				
			} else
				Log.d("Brightness", "Sensor Registed"); 
			
		} else {
			/*final AlertDialog.Builder alert = new AlertDialog.Builder(PDFReader.mContext);
			
			alert.setTitle("Warrning");
			alert.setIcon(R.drawable.alert);
			alert.setMessage("Can not access the Light Sensor. The reason may be this device have not Light Sensor " +
					"or you have not permission to access. Please using Device auto brightness mode");
			alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int whichButton) 
				{
					dialog.cancel();
				}
			});
			alert.show();*/
			
			/*Toast.makeText(getApplicationContext(), "Can not access the Light Sensor. " +
					"The reason may be this device have not Light Sensor " +
					"or you have not permission to access. Please using Device auto brightness mode", 
					Toast.LENGTH_LONG).show();*/
		}
		
		return mBinder;
	}

	/*
	 * Turn On Auto Brightness Mode On Device if check "Device Auto Brightness"
	 * Else Turn Off Auto Brightness Mode 
	 */
	public void setAutoBrightness(boolean isAutoBrightness) {
		if (isAutoBrightness) {
			Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, 
					Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
			
//			Toast.makeText(getApplicationContext(), "Device Auto Brightness Mode: On", Toast.LENGTH_SHORT).show();
		} else {
			Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, 
					Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
			
//			Toast.makeText(getApplicationContext(), "Device Auto Brightness Mode: Off", Toast.LENGTH_SHORT).show();
		}
		
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		long currTime = SystemClock.uptimeMillis();
		currLightSensorValue = event.values[0];
//		Log.e("MyLog", "" + currLightSensorValue);
		
		/*
		 * Ignore events that have occurred too early to get better change
		 */
		if (currTime - lastUpdate > 100) {
			lastUpdate = currTime;
			
			/*
			 * Store 50 lastest value (in 5 second) from Light Sensor 
			 */
			if (PDFReader.listOfSensorValue.size() == 50) {
				PDFReader.listOfSensorValue.remove(0);
			}
			PDFReader.listOfSensorValue.add((int) currLightSensorValue);

			if (BrightnessSettings.mTxtCurrLightSensorValue != null) {
				BrightnessSettings.mTxtCurrLightSensorValue.setText("" + currLightSensorValue);
//				Log.e("MyLog", "Ok");
			}

			/*
			 * if using Manual Setting Mode: do nothing
			 */
			if (mFileSettings.getBrightnessMode().equals("MANUAL")) {
			}

			/*
			 * if using Device Auto Brightness Mode: get current brightness
			 * level from file system on device
			 */
			else if (mFileSettings.getBrightnessMode().equals("AUTO")) {
				// Device: NOTE2
				if (android.os.Build.MODEL.equals("GT-N7105")) {
					currBacklight = getCurrentBacklight(CMD_NOTE2);
				} else if (android.os.Build.MODEL.equals("GT-I9082")) {
					currBacklight = getCurrentBacklight(CMD_GRAND);
				}
				
				if (currBacklight != -1) {
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							PDFReader.currentBacklight = currBacklight;
							PDFReader.currentPercentBrightness = Math.round((float) PDFReader.currentBacklight * 100 / 255);
						}
					}).start();
					
				}

				if (BrightnessSettings.mTxtCurrBrightnessLevelValue != null) {
//					AutoBrightnessSettings.mTxtCurrBrightnessLevelValue.setText("" + PDFReader.currentBacklight);
					BrightnessSettings.mTxtCurrBrightnessLevelValue.setText("" + PDFReader.currentPercentBrightness + " %");
				}
				
			}

			/*
			 * if using Advance Auto Brightness Mode: get current brightness
			 * level from API to set Text for TextView CurrBrightnessLevelValue
			 */
			else if (mFileSettings.getBrightnessMode().equals("ADVANCE")) {
				if (PDFReader.listOfSensorValue.size() == 50) {
					Log.d("MyLog", "Size of ListOfSensorValue: 50");

					try {
						currBacklight = Settings.System.getInt(PDFReader.mContentResolver,Settings.System.SCREEN_BRIGHTNESS);
						PDFReader.currentBacklight = currBacklight;
						PDFReader.currentPercentBrightness = Math.round((float) PDFReader.currentBacklight * 100 / 255);
	
						if (BrightnessSettings.mTxtCurrBrightnessLevelValue != null) {
	//						AutoBrightnessSettings.mTxtCurrBrightnessLevelValue.setText("" + currBacklight);
							BrightnessSettings.mTxtCurrBrightnessLevelValue.setText("" + PDFReader.currentPercentBrightness + " %");
						}
	
					} catch (SettingNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
	
					/*
					 * Handle advance auto brightness mode
					 */
					int sum = 0;
					for (int i=0; i<PDFReader.listOfSensorValue.size(); i++) {
						sum += PDFReader.listOfSensorValue.get(i);
					}
					averageValue = sum / PDFReader.listOfSensorValue.size();
					
					String strCurrBrightnessLevelValueAndMode = getBrigtnessLevelAtAdvanceBrightnessMode(averageValue);
					String[] tmp = strCurrBrightnessLevelValueAndMode.split(":");
					currBrightnessLevelValue = Integer.parseInt(tmp[0]);
					int position = Integer.parseInt(tmp[1]);
					
	//				Log.d("Brightness", "Average value: " + averageValue + ", CurrBrightness: " + currBrightnessLevelValue 
	//						+ ", LastBrightness: " + lastBrightnessLevelValue);
	
					if (currBrightnessLevelValue != lastBrightnessLevelValue) {
						mBroadcastIntent.putExtra(BROADCAST_BRIGHTNESS_KEY, currBrightnessLevelValue);
						mBroadcastIntent.putExtra(BROAD_CAST_LEVEL_MODE, position);
						sendBroadcast(mBroadcastIntent);
					}
	
					lastBrightnessLevelValue = currBrightnessLevelValue;
	
				}
			}
		}
		
	}
	
	// Method set backlight for monitor
	public void setBacklight(int value, Window mWindow, ContentResolver mContentResolver) {
		WindowManager.LayoutParams mWindowLayoutParam = mWindow.getAttributes();
		mWindowLayoutParam.screenBrightness = value / 255F;
		mWindow.setAttributes(mWindowLayoutParam);
		
		Settings.System.putInt(mContentResolver, Settings.System.SCREEN_BRIGHTNESS, value);
	}
	
	public String getBrigtnessLevelAtAdvanceBrightnessMode(float currLightSensorValue) {
		int position = -1;
		String aLine;
		String[] tmpValue;
		
		try {
			FileReader fileReader = new FileReader(BrightnessSettings.FILE_PATH);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			
			//ignore the first line which contains auto brightness mode
			bufferedReader.readLine();
			
			while ((aLine = bufferedReader.readLine()) != null) {
				position ++;
				tmpValue = aLine.split(" ");
				
				if ((currLightSensorValue >= Integer.parseInt(tmpValue[0])) && 
						(currLightSensorValue <= Integer.parseInt(tmpValue[1]))) {
					return (Integer.parseInt(tmpValue[2]) + ":" + position);
				}
			}
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
		
		return (255 + ":" + position);
	}
	
	
	public int getCurrentBacklight(String cmd) {
		try {
			java.lang.Process p = Runtime.getRuntime().exec(cmd);
			
			BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
			
			int value;
			value = Integer.parseInt(in.readLine());
			return value;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		} catch (NumberFormatException nfe) {
			// TODO Auto-generated catch block
			nfe.printStackTrace();
			return -1;
		}
		
	}
	
	
	public void stop() {
		if (mSensorManager != null) {
			mSensorManager.unregisterListener(this);
			mSensorManager = null;
			Log.d("Brightness", "stop service");
		}
	}
	
	
	/*
	 * Computes battery level  by registering a receiver to the intent triggered by a battery status/level change.
	 * If battery level remaining < 30%, show warring and set brightness level to 30%
	 */
	/*private void batteryLevel() {
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
					
					if ((level <= 100) && (PDFReader.isBatteryLow == false)) {
						mBroadcastIntent.putExtra(BROADCAST_BATTERY_KEY, BROADCAST_BATTERY_LOWER);
						sendBroadcast(mBroadcastIntent);
						PDFReader.isBatteryLow = true;
						Log.d("Brightness", "Battery LOW");
					} else if (level > 100) {
						PDFReader.isBatteryLow = false;
					}
					
				} 
			}
		};
		
		IntentFilter batteryLevelFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		registerReceiver(batteryLevelReceiver, batteryLevelFilter);
	}
	*/
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.d("Brightness", "Service onDetroy");
		if (batteryLevelReceiver != null)
			unregisterReceiver(batteryLevelReceiver);
	}
	
	
}

