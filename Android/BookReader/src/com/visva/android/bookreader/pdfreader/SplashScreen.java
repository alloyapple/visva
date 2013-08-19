package com.visva.android.bookreader.pdfreader;

import com.samsung.svmc.pdfreader.R;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageView;

public class SplashScreen extends Activity {

	Thread mSplashThread;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.frame_animations_layout);

		// Lock Rotation
		if (getResources().getConfiguration().orientation == 1) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		} else {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
		
		// Splash Animation
		animate();

		mSplashThread = new Thread() {
			public void run() {
				try {
					synchronized (this) {
						wait(2000);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				Intent intent = new Intent(SplashScreen.this, PDFReader.class);
				startActivity(intent);

				finish();
			}
		};
		mSplashThread.start();
	}

	private void animate() {
		ImageView imgView = (ImageView) findViewById(R.id.animationImage);
		imgView.setVisibility(ImageView.VISIBLE);
		imgView.setBackgroundResource(R.drawable.frame_animation);

		AnimationDrawable frameAnimation = (AnimationDrawable) imgView
				.getBackground();

		if (frameAnimation.isRunning()) {
			frameAnimation.stop();
		} else {
			frameAnimation.stop();
			frameAnimation.start();
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			synchronized (mSplashThread) {
				mSplashThread.notifyAll();
			}
		}
		return true;
	}

}
