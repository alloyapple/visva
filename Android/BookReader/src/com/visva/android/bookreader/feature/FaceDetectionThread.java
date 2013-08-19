package com.visva.android.bookreader.feature;

import com.samsung.svmc.pdfreader.R;
import com.samsung.svmc.pdfreader.R.drawable;
import com.visva.android.bookreader.pdfreader.PDFReader;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

public class FaceDetectionThread implements Runnable {
	private boolean isThreadRunning = true;
	private boolean isThreadPause = false;
	private Camera mCamera;
	private CameraPreView mCameraPreView;
	private Context mContext;
	private FrameLayout mFrameLayout;
	
	private NotificationManager mNotificationManager;
	private PendingIntent mPendingIntent;
	
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			mFrameLayout.addView(mCameraPreView);
		};
	};

	public FaceDetectionThread(Camera mCamera, CameraPreView mCameraPreView, Context mContext,
			FrameLayout mFrameLayout, NotificationManager mNotificationManager, PendingIntent mPendingIntent) {
		super();
		this.mCamera = mCamera;
		this.mCameraPreView = mCameraPreView;
		this.mContext = mContext;
		this.mFrameLayout = mFrameLayout;
		this.mNotificationManager = mNotificationManager;
		this.mPendingIntent = mPendingIntent;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (isThreadRunning) {
			if (isThreadPause == false) {
				try {
					PDFReader.currTimeMilisecond = SystemClock.uptimeMillis();
					
					if (((PDFReader.currTimeMilisecond - PDFReader.lastTimeOnTouched) > (PDFReader.currTimeout - 10000)) 
									&& (PDFReader.lastTimeOnTouched != 0)) {
						int timeout = Settings.System.getInt(PDFReader.mContentResolver, Settings.System.SCREEN_OFF_TIMEOUT);
						Log.d("Brightness", "CurrTimeOut: " + PDFReader.currTimeout + "; System Timeout: " + timeout) ;
						
						// release camera if opening
						releaseCamera();
						
						Log.d("FaceDetection", "Open Camera");
						int index = getFrontCameraId();
						mCamera = Camera.open(index);
						mCameraPreView = new CameraPreView(mContext, mCamera, mFrameLayout, this);
						mCameraPreView.setSurfaceTextureListener(mCameraPreView);
						
						Message msg = mHandler.obtainMessage();
						mHandler.sendMessage(msg);
						
						isThreadPause = true;
					}
				} catch (Exception e) {
					Log.d("Brightness", "FaceDetectionThread exception");
					resetStatusThread();
					
					// set timeout to 15 second and reset wakelock
					PDFReader.currTimeout = 15000;
					Settings.System.putInt(PDFReader.mContentResolver, Settings.System.SCREEN_OFF_TIMEOUT, 15000);
					PDFReader.mWakeLock.acquire();
					PDFReader.mWakeLock.release();
				}
			} else {
				try {
					Thread.sleep(500);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}
	}
	
	// check and get Front Camera Id
	private int getFrontCameraId() {
	    CameraInfo mCameraInfo = new CameraInfo();
	    for (int i = 0 ; i < Camera.getNumberOfCameras(); i++) {
	        Camera.getCameraInfo(i, mCameraInfo);
	        if (mCameraInfo.facing == CameraInfo.CAMERA_FACING_FRONT) 
	        	return i;
	    }
	    return -1; // No front-facing camera found
	}
	
	public void endThread() {
		isThreadRunning = false;
	}
	
	public void pauseThread() {
		isThreadPause = true;
	}
	
	public void resumeThread() {
		isThreadPause = false;
	}
	
	// release camera
	public void releaseCamera() {
		if (mCamera != null) {
			mCamera.release();
			mCamera = null;
			
			clearNotification();
		}
	}
	
	/*
	 * release camea, resume thread and reset PDFReader.lastTimeOnTouched 
	 * when user touch on screen
	 */
	public void resetStatusThread() {
		PDFReader.lastTimeOnTouched = SystemClock.uptimeMillis();
		releaseCamera();
		resumeThread();
		Log.d("FaceDetection", "" + PDFReader.lastTimeOnTouched);
	}
	
	public void notifyStartDetectFace() {
		Notification note = new Notification(R.drawable.eye_icon, "Starting detect face...!", System.currentTimeMillis());
		note.setLatestEventInfo(mContext, "Faces detecting...", "Keep screen on and reset timeout as soon as face detected", mPendingIntent);
		note.flags |= Notification.FLAG_NO_CLEAR;

		mNotificationManager.notify(PDFReader.NOTIFY_AUTODETECTFACE_ID, note);
	}

	public void clearNotification() {
		if (mNotificationManager != null)
			mNotificationManager.cancel(PDFReader.NOTIFY_AUTODETECTFACE_ID);
	}

}
