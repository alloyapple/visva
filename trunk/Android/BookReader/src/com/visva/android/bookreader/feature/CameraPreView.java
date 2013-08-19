package com.visva.android.bookreader.feature;

import java.io.IOException;

import com.visva.android.bookreader.pdfreader.FaceDetectionDemo;
import com.visva.android.bookreader.pdfreader.PDFReader;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.Face;
import android.hardware.Camera.FaceDetectionListener;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.TextureView;
import android.view.TextureView.SurfaceTextureListener;
import android.widget.FrameLayout;
import android.widget.Toast;

public class CameraPreView extends TextureView implements SurfaceTextureListener {
	
	private Camera mCamera;
	private FrameLayout mFrameLayout;
	private FaceDetectionThread mFaceDetectionThread;
	private boolean isPreviewing = false;
	
	private boolean isPreviewOfFaceDetectionDemo = false;
	private FaceDetectionDemo mFaceDetectionDemo;
	
	public CameraPreView(Context mContext, Camera mCamera, FrameLayout mFrameLayout, FaceDetectionThread mFaceDetectionThread) {
		super(mContext);
		this.mCamera = mCamera;
		this.mCamera.setDisplayOrientation(90);
		this.mCamera.setFaceDetectionListener(faceDetectionListener);
		
		this.mFrameLayout = mFrameLayout;
		this.mFaceDetectionThread = mFaceDetectionThread;
	}
	
	// constructor for FaceDetectionDemo
	public CameraPreView(Context mContext, Camera mCamera, FrameLayout mFrameLayout, FaceDetectionDemo mFaceDetectionDemo) {
		super(mContext);
		
		this.mCamera = mCamera;
		this.mCamera.setDisplayOrientation(90);
		this.mCamera.setFaceDetectionListener(faceDetectionListener);
		
		this.mFaceDetectionDemo = mFaceDetectionDemo;
		this.mFrameLayout = mFrameLayout;
		isPreviewOfFaceDetectionDemo = true;
	}

	@Override
	public void onSurfaceTextureAvailable(SurfaceTexture surface, int width,
			int height) {
		// TODO Auto-generated method stub
		if (mCamera != null) {
			if (!isPreviewOfFaceDetectionDemo) {
				Camera.Size previewSize = mCamera.getParameters().getPreviewSize();
				setLayoutParams(new FrameLayout.LayoutParams(previewSize.width, previewSize.height, Gravity.CENTER));
			}
			
			try {
				mCamera.setPreviewTexture(surface);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			if (isPreviewing) {
				mCamera.stopFaceDetection();
				mCamera.stopPreview();
				isPreviewing = false;
			}
			
			mCamera.startPreview();
			if (!isPreviewOfFaceDetectionDemo) {
				this.setVisibility(GONE);
//				Toast.makeText(getContext(), "Starting detect faces...", Toast.LENGTH_SHORT).show();
				mFaceDetectionThread.notifyStartDetectFace();
			}
			Log.d("FaceDetection", "Start Preview");
			mCamera.startFaceDetection();
			isPreviewing = true;
		}
	}

	@Override
	public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSurfaceTextureUpdated(SurfaceTexture surface) {
		// TODO Auto-generated method stub
		
	}
	
	FaceDetectionListener faceDetectionListener = new FaceDetectionListener() {
		
		@Override
		public void onFaceDetection(Face[] faces, Camera camera) {
			// TODO Auto-generated method stub
			if (!isPreviewOfFaceDetectionDemo) {
				if (faces.length == 0) {
//					Log.d("FaceDetection", "No face detected");
				} else {
					Log.d("FaceDetection", String.valueOf(faces.length) + " Face Detected :) ");
//					Toast.makeText(getContext(), "Face Detected", Toast.LENGTH_SHORT).show();
					
					// set timeout to 15 second and reset wakelock
					PDFReader.currTimeout = 15000;
					Settings.System.putInt(PDFReader.mContentResolver, Settings.System.SCREEN_OFF_TIMEOUT, 15000);
					PDFReader.mWakeLock.acquire();
					PDFReader.mWakeLock.release();
					
					mFaceDetectionThread.resetStatusThread();
				}
			// if previewing in FaceDetectionDemo Activity	
			} else {
				if (faces.length == 0) {
					Log.d("FaceDetection", String.valueOf(faces.length) + " Face Detected :) ");
					mFaceDetectionDemo.setNumberOfFaceDetectedForTextView(0);
				} else {
					Log.d("FaceDetection", String.valueOf(faces.length) + " Face Detected :) ");
					mFaceDetectionDemo.setNumberOfFaceDetectedForTextView(faces.length);
				}
			}
		}
	};

}
