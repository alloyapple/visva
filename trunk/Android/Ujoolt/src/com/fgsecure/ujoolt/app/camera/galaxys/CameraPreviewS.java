package com.fgsecure.ujoolt.app.camera.galaxys;

import java.io.IOException;

import android.content.Context;
import android.hardware.Camera;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraPreviewS extends SurfaceView implements
		SurfaceHolder.Callback {
	private SurfaceHolder mHolder;
	private Camera mCamera;

	public CameraPreviewS(Context context, Camera camera) {
		super(context);
		mCamera = camera;

		// Install a SurfaceHolder.Callback so we get notified when the
		// underlying surface is created and destroyed.
		mHolder = getHolder();
		mHolder.addCallback(this);
		// deprecated setting, but required on Android versions prior to 3.0
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		
		mHolder.setFixedSize(100,100);
	}

	public void surfaceCreated(SurfaceHolder holder) {
		// The Surface has been created, now tell the camera where to draw the
		// preview.
		try { 
			mCamera.setPreviewDisplay(holder);
//			mCamera.startPreview();
		} catch (IOException e) {
			Log.e("DG_DEBUG", "Error setting camera preview: " + e.getMessage());
		}
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// If your preview can change or rotate, take care of those events here.
		// Make sure to stop the preview before resizing or reformatting it.
		
		if (mHolder.getSurface() == null) {
			// preview surface does not exist
			return;
		}

		// stop preview before making changes
		try {
			mCamera.stopPreview();
		} catch (Exception e) {
			// ignore: tried to stop a non-existent preview
		}
		// start preview with new settings
		try {
			mCamera.setPreviewDisplay(mHolder);
			mCamera.startPreview();
			Log.e("on surface change", "change it");
		} catch (Exception e) {
			handleStartPreview();
			Log.d("DG_DEBUG",
					"Error starting camera preview: " + e.getMessage());
			
		}
	}

	private void handleStartPreview(){
		new CountDownTimer(1000,100) {
			
			@Override
			public void onTick(long millisUntilFinished) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				mCamera.startPreview();
			}
		}.start();
	}
	public void surfaceDestroyed(SurfaceHolder holder) {
		// empty. Take care of releasing the Camera preview in your activity.
	}

	public SurfaceHolder getmHolder() {
		return mHolder;
	}

	public void setmHolder(SurfaceHolder mHolder) {
		this.mHolder = mHolder;
	}
	

}