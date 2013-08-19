package com.visva.android.bookreader.pdfreader;

import com.samsung.svmc.pdfreader.R;
import com.visva.android.bookreader.feature.CameraPreView;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.TextView;

public class FaceDetectionDemo extends Activity {

	private TextView txtNumberOfFacesDetected;
	private FrameLayout mFrameLayoutPreView;

	private Camera mCamera;
	private CameraPreView mCameraPreView;
	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.face_detection);
		getActionBar().hide();

		mContext = this;

		txtNumberOfFacesDetected = (TextView) findViewById(R.id.txtFaceDetectionDemo);
		mFrameLayoutPreView = (FrameLayout) findViewById(R.id.faceDetectionDemoFrameLayout);

	}
	
	public void setNumberOfFaceDetectedForTextView(int numberOfFaceDetected) {
		if (numberOfFaceDetected <= 1) {
			txtNumberOfFacesDetected.setText("Face Detection Demo: " + numberOfFaceDetected + " face detected");
		} else {
			txtNumberOfFacesDetected.setText("Face Detection Demo: " + numberOfFaceDetected + " faces detected");
		}
	}

	// check and get Front Camera Id
	private int getFrontCameraId() {
		CameraInfo mCameraInfo = new CameraInfo();
		for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
			Camera.getCameraInfo(i, mCameraInfo);
			if (mCameraInfo.facing == CameraInfo.CAMERA_FACING_FRONT)
				return i;
		}
		return -1; // No front-facing camera found
	}
	
	// release camera
	public void releaseCamera() {
		if (mCamera != null) {
			mCamera.release();
			mCamera = null;
		}
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		int index = getFrontCameraId();
		mCamera = Camera.open(index);
		mCameraPreView = new CameraPreView(mContext, mCamera, mFrameLayoutPreView, this);
		mCameraPreView.setSurfaceTextureListener(mCameraPreView);
		
		mFrameLayoutPreView.addView(mCameraPreView);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		releaseCamera();
		mFrameLayoutPreView.removeAllViews();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		releaseCamera();
		mFrameLayoutPreView.removeAllViews();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return false;
	}
}
