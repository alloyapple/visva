package com.visva.android.bookreader.pdfreader;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
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
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.SystemClock;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.qoppa.samples.viewer.SamplePDFViewer;
import com.samsung.svmc.pdfreader.R;
import com.visva.android.bookreader.adapter.ItemAdapter;
import com.visva.android.bookreader.epub.EPubActivity;
import com.visva.android.bookreader.feature.BrightnessService;
import com.visva.android.bookreader.feature.CameraPreView;
import com.visva.android.bookreader.feature.FaceDetectionThread;
import com.visva.android.bookreader.io.FileSettings;

public class FileExplorer extends Activity {

	// private ScrollView fileExplorerScrollView;
	private FrameLayout fileExplorerFrameLayout;
	private Camera mCamera;
	private CameraPreView mCameraPreView;
	// private FaceDetectionThread mFaceDetectionThread;

	private ListView listView;
	private List<Item> listItems = new ArrayList<Item>();
	public static ItemAdapter adapter;
	private View header;
	private LinearLayout linear_download;
	public static LinearLayout linear_recent;
	private String parentFolderName = "";
	private File mFile;
	private int value;

	private static final int CODE_DEFAUTL = -1;
	public static final String OPEN_FILE_PATH = "FilePath";

	private Context mContext;
	private Window mWindow;
	private ContentResolver mContentResolver;
	private NotificationManager mNotificationManager;
	// private PendingIntent mPendingIntent;
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
			mBoundService = ((BrightnessService.LocalBinder) service)
					.getService();
			PDFReader.setBrightnessMode(mFileSettings, mBoundService);

			brightnessLevel();
			batteryLevel();

			// unlock rotation
			// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.file_explorer);
		getActionBar().hide();

		/*
		 * Lock rotation when activity onCreate. Rotation will be unlocked when
		 * service connected
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
		// mPendingIntent = PendingIntent.getActivity(mContext, 0, null, 0);

		fileExplorerFrameLayout = (FrameLayout) findViewById(R.id.fileExplorerFrameLayout);
		/*
		 * fileExplorerScrollView = (ScrollView)
		 * findViewById(R.id.fileExplorerScrollView);
		 * fileExplorerScrollView.setOnTouchListener(new OnTouchListener() {
		 * 
		 * @Override public boolean onTouch(View v, MotionEvent event) { // TODO
		 * Auto-generated method stub if (event.getAction() ==
		 * MotionEvent.ACTION_UP) { mFaceDetectionThread.resetStatusThread(); }
		 * return false; } });
		 */

		// initialize face detection thread
		// mFaceDetectionThread = new FaceDetectionThread(mCamera,
		// mCameraPreView, mContext, fileExplorerFrameLayout,
		// mNotificationManager, mPendingIntent);
		// Thread mThread = new Thread(mFaceDetectionThread);
		// mThread.start();

		mFileSettings = new FileSettings(BrightnessSettings.FOLDER_PATH,
				BrightnessSettings.FILE_PATH);

		linear_download = (LinearLayout) findViewById(R.id.linear_download);
		linear_recent = (LinearLayout) findViewById(R.id.linear_recent);

		Intent mIntent = getIntent();
		value = mIntent.getIntExtra(PDFReader.KEY, CODE_DEFAUTL);

		if (value == PDFReader.CODE_RECENT) {
			header = (View) getLayoutInflater().inflate(R.layout.recent_header,
					null);

			listItems = addToListItemsFromRecentFiles();

			if (listItems != null) {
				adapter = new ItemAdapter(this, R.layout.row, listItems);
				linear_recent.setVisibility(View.GONE);
			} else {
				adapter = null;
				listItems = new ArrayList<Item>();
				linear_recent.setVisibility(View.VISIBLE);
			}
		}

		else if (value == PDFReader.CODE_SDCARD) {
			mFile = Environment.getExternalStorageDirectory();
			parentFolderName = getParentPath(mFile.getAbsolutePath());

			header = (View) getLayoutInflater().inflate(R.layout.sdcard_header,
					null);

			listItems = getAllInfo(mFile);

			if (listItems != null) {
				adapter = new ItemAdapter(this, R.layout.row, listItems);
			} else {
				adapter = null;
				listItems = new ArrayList<Item>();
			}
		}

		else if (value == PDFReader.CODE_DOWNLOAD) {
			mFile = new File(Environment.getExternalStorageDirectory()
					+ "/Download");
			parentFolderName = getParentPath(mFile.getAbsolutePath());

			header = (View) getLayoutInflater().inflate(
					R.layout.download_header, null);

			listItems = getAllInfo(mFile);

			if (listItems != null) {
				adapter = new ItemAdapter(this, R.layout.row, listItems);
				linear_download.setVisibility(View.GONE);
			} else {
				adapter = null;
				listItems = new ArrayList<Item>();
				linear_download.setVisibility(View.VISIBLE);
			}
		}

		listView = (ListView) findViewById(R.id.listFolder);

		listView.addHeaderView(header);
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

				if (position == 0)
					return;

				Item mItemChoosed = listItems.get(position - 1);

				File currentFile = mItemChoosed.getFile();

				if (currentFile.isDirectory()) {

					List<Item> tmps = getAllInfo(currentFile);
					if (tmps == null) {
						Toast.makeText(FileExplorer.this, "Folder is empty!",
								Toast.LENGTH_SHORT).show();

						String currentFolderName = currentFile.getParent();
						File parentFolder = new File(currentFolderName);
						parentFolderName = parentFolder.getParent();

					} else {
						parentFolderName = currentFile.getParent();
						resetAdapter(tmps);
					}
				} else {
					if (currentFile.getName().endsWith(".pdf")
							|| currentFile.getName().endsWith(".PDF")) {
						// viewPdf(Uri.fromFile(currentFile));
						viewPdf(currentFile.getAbsolutePath());

						// write to RecentFiles
						if (value != PDFReader.CODE_RECENT) {
							mFileSettings.writeToRecentFiles(currentFile);
						}

					} else if (currentFile.getName().endsWith(".epub")
							|| currentFile.getName().endsWith(".EPUB")) {
						Intent intent = new Intent(FileExplorer.this,EPubActivity.class);
						startActivity(intent);
					} else {
						Toast.makeText(FileExplorer.this,
								"Can not open this file!", Toast.LENGTH_SHORT)
								.show();
					}

					String currentFolderName = currentFile.getParent();
					File parentFolder = new File(currentFolderName);
					parentFolderName = parentFolder.getParent();

				}

			}
		});

		listView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_UP) {
					// reset status thread when user touch on monitor
					// mFaceDetectionThread.resetStatusThread();
				}
				return false;
			}
		});
	}

	// Get all of folder
	@SuppressWarnings("deprecation")
	public List<Item> getAllInfo(File file) {

		List<File> mListFile = listFilesNotHidden(file);

		List<Item> items = new ArrayList<Item>();
		Item itm;

		if (mListFile.size() > 0) {
			for (int i = 0; i < mListFile.size(); i++) {

				if (mListFile.get(i).isDirectory()) {
					List<File> tmp = listFilesNotHidden(mListFile.get(i));

					itm = new Item(R.drawable.folder_2, mListFile.get(i)
							.getName(), new Date(mListFile.get(i)
							.lastModified()).toGMTString(), tmp.size()
							+ " files", mListFile.get(i), R.drawable.img_null);

					items.add(itm);

				} else {
					long size = mListFile.get(i).length();

					itm = new Item(R.drawable.file_2, mListFile.get(i)
							.getName(), new Date(mListFile.get(i)
							.lastModified()).toGMTString(),
							convertCapacity(size) + "", mListFile.get(i),
							R.drawable.img_null);

					items.add(itm);

				}
			}
		} else
			items = null;

		return items;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (value == PDFReader.CODE_RECENT) {
				// End Activity
				finish();
			}

			else if ((value == PDFReader.CODE_SDCARD)
					&& (parentFolderName.equals(getParentPath(mFile
							.getAbsolutePath())))) {
				// End Activity
				finish();
			} else if ((value == PDFReader.CODE_DOWNLOAD)
					&& (parentFolderName.equals(getParentPath(mFile
							.getAbsolutePath())))) {
				// End Activity
				finish();
			}

			else {

				File parentFolder = new File(parentFolderName);
				List<Item> tmps = getAllInfo(parentFolder);
				adapter.clear();
				adapter.addAll(tmps);

				parentFolderName = getParentPath(parentFolderName);
				return true;
			}
		}

		return false;
	}

	public String getParentPath(String path) {
		int j = 0;
		String parentPath = "";

		for (int i = path.length() - 1; i >= 0; i--) {
			if (path.charAt(i) == '/') {
				j = i;
				break;
			}
		}

		parentPath = path.substring(0, j);

		return parentPath;
	}

	public String convertCapacity(long size) {
		float sizeConverted;

		if (size <= 1024) {
			return size + " bytes";
		} else if ((size > 1024) && (size <= 1024 * 1024)) {
			sizeConverted = size / 1024f;

			// lay 2 chu so sau dau phay
			int tmp = (int) (sizeConverted * Math.pow(10, 2));

			sizeConverted = (float) (tmp / Math.pow(10, 2));

			return sizeConverted + " KB";
		} else {
			sizeConverted = size / 1024 / 1024f;

			// lay 2 chu so sau dau phay
			int tmp = (int) (sizeConverted * Math.pow(10, 2));

			sizeConverted = (float) (tmp / Math.pow(10, 2));

			return sizeConverted + " MB";
		}

	}

	/*
	 * Not use now - Old version
	 */
	private void viewPdf(Uri file) {
		Intent intent = new Intent(Intent.ACTION_VIEW);

		intent.setDataAndType(file, "application/pdf");

		try {

			startActivity(intent);

		} catch (ActivityNotFoundException e) {
			// No application to view, asd to download one
			AlertDialog.Builder mBuilder = new AlertDialog.Builder(
					FileExplorer.this);

			mBuilder.setTitle("No application to read file");
			mBuilder.setMessage("Download one from Android Market?");

			mBuilder.setPositiveButton("Yes, Please",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							Intent marketIntent = new Intent(Intent.ACTION_VIEW);

							marketIntent.setData(Uri
									.parse("market://details?id=com.adobe.reader"));

							startActivity(marketIntent);
						}
					});

			mBuilder.setNegativeButton("No, Thanks", null);

			mBuilder.create().show();
		}

	}

	private void viewPdf(String aFilePath) {
		Intent intent = new Intent(FileExplorer.this, SamplePDFViewer.class);
		intent.putExtra(OPEN_FILE_PATH, aFilePath);

		startActivity(intent);
	}

	// Return the list of files is not hidden
	public List<File> listFilesNotHidden(File file) {
		List<File> tmp = new ArrayList<File>();
		File[] listFile = file.listFiles();

		for (int i = 0; i < listFile.length; i++) {
			if (!listFile[i].isHidden())
				tmp.add(listFile[i]);
		}

		return tmp;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.options_menu, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == R.id.settings) {
			Intent mIntentSettings = new Intent(FileExplorer.this,
					BrightnessSettings.class);
			startActivity(mIntentSettings);

			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.d("Brightness", "File Explorer onResume");
		bindService(new Intent(FileExplorer.this, BrightnessService.class),
				mConnection, Context.BIND_AUTO_CREATE);

		// reset last brightness level value
		BrightnessService.lastBrightnessLevelValue = -1;

		if (!mFileSettings.getBrightnessMode().equals("AUTO")) {
			WindowManager.LayoutParams mWindowLayoutParam = mWindow
					.getAttributes();
			mWindowLayoutParam.screenBrightness = PDFReader.currentBacklight / 255F;
			mWindow.setAttributes(mWindowLayoutParam);
		} else {
			WindowManager.LayoutParams mWindowLayoutParam = mWindow
					.getAttributes();
			mWindowLayoutParam.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
			mWindow.setAttributes(mWindowLayoutParam);
		}

		// re-initialize variables to check detect face
		PDFReader.lastTimeOnTouched = SystemClock.uptimeMillis();
		// mFaceDetectionThread.resumeThread();

		// reset timeout
		Settings.System.putInt(mContentResolver,
				Settings.System.SCREEN_OFF_TIMEOUT, PDFReader.defaultTimeout);
		PDFReader.mWakeLock.acquire();
		PDFReader.mWakeLock.release();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.d("Brightness", "File Explorer onPause");
		// unBind Service
		if (mConnection != null) {
			if (mBoundService != null)
				mBoundService.stop();
			this.unbindService(mConnection);
		}

		// unRegister receiver
		if (brightnessLevelReceiver != null)
			this.unregisterReceiver(brightnessLevelReceiver);

		if (batteryLevelReceiver != null) {
			this.unregisterReceiver(batteryLevelReceiver);
		}

		// pause FaceDetectionThread and release camera
		// mFaceDetectionThread.pauseThread();
		// mFaceDetectionThread.releaseCamera();
	}

	// Method register receiver to receive brightness level value from broadcast
	private void brightnessLevel() {
		brightnessLevelReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				int brightnessLevelValue = intent.getIntExtra(
						BrightnessService.BROADCAST_BRIGHTNESS_KEY,
						BrightnessService.BROADCAST_BRIGHTNESS_DEFAULT_VALUE);
				mBoundService.setBacklight(brightnessLevelValue, mWindow,
						mContentResolver);
				Log.d("Brightness", context.getClass().toString()
						+ " Receive: " + brightnessLevelValue);
			}

		};

		IntentFilter brightnessLevelFilter = new IntentFilter(
				BrightnessService.BROADCAST_NAME);
		registerReceiver(brightnessLevelReceiver, brightnessLevelFilter);
	}

	/*
	 * Computes battery level by registering a receiver to the intent triggered
	 * by a battery status/level change. If battery level remaining < 30%, show
	 * warring and set brightness level to 30%
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
						final AlertDialog.Builder alert = new AlertDialog.Builder(
								FileExplorer.this);

						alert.setTitle("Warrning");
						alert.setIcon(R.drawable.alert);
						alert.setMessage("Battery remaining is lower 30%. If current brightness display is larger 30%, "
								+ "you should set brightness display to 30% to saving energy!");

						alert.setPositiveButton("Ok",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										mBoundService.setAutoBrightness(false);
										mBoundService.setBacklight(76, mWindow,
												mContentResolver);
										PDFReader.currentBacklight = 76;
										PDFReader.currentPercentBrightness = 30;
										mFileSettings.reWriteFileSetting();
									}
								});

						alert.setNegativeButton("Cancel",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
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

		IntentFilter batteryLevelFilter = new IntentFilter(
				Intent.ACTION_BATTERY_CHANGED);
		registerReceiver(batteryLevelReceiver, batteryLevelFilter);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if (event.getAction() == MotionEvent.ACTION_UP) {
			// mFaceDetectionThread.resetStatusThread();
		}
		return false;
	}

	public List<Item> addToListItemsFromRecentFiles() {
		List<Item> mListItems = new ArrayList<Item>();
		FileSettings aFileSettings = new FileSettings();
		List<File> listRecentFiles = aFileSettings.readRecentFiles();

		if ((listRecentFiles != null) && (listRecentFiles.size() > 0)) {
			long size;
			for (int i = 0; i < listRecentFiles.size(); i++) {
				size = listRecentFiles.get(i).length();

				Item mItem = new Item(R.drawable.file_2, listRecentFiles.get(i)
						.getName(), listRecentFiles.get(i).getParent(),
						convertCapacity(size), listRecentFiles.get(i),
						R.drawable.delete);
				mListItems.add(mItem);
			}
			return mListItems;
		}

		return null;
	}

	public void resetAdapter(List<Item> listItems) {
		adapter.clear();
		if (listItems == null) {
			listItems = new ArrayList<Item>();
			linear_recent.setVisibility(View.VISIBLE);
		} else {
			linear_recent.setVisibility(View.GONE);
		}

		adapter.addAll(listItems);
	}

}
