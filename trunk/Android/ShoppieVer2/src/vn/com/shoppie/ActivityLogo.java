package vn.com.shoppie;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import vn.com.shoppie.activity.ActivityWelcome;
import vn.com.shoppie.activity.HomeActivity;
import vn.com.shoppie.activity.LoginActivity;
import vn.com.shoppie.activity.SettingPreference;
import vn.com.shoppie.constant.GlobalValue;
import vn.com.shoppie.util.SceneAnimation;
import vn.com.shoppie.util.SceneAnimation.OnSceneAnimationListener;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class ActivityLogo extends Activity {
	// ImageView mBtnBubble;
	TextView tvLaunch;
	Animation animLogo;
	Animation animGrowCenter;
	AnimationDrawable frameAnimation;

	private ImageView mTapScreenTextAnimImgView;
	private final int[] mTapScreenTextAnimRes = { R.drawable.pie_01, R.drawable.pie_02, R.drawable.pie_03, R.drawable.pie_04, R.drawable.pie_05, R.drawable.pie_06, R.drawable.pie_07, R.drawable.pie_08, R.drawable.pie_09, R.drawable.pie_10, R.drawable.pie_11, R.drawable.pie_12, R.drawable.pie_13,
			R.drawable.pie_14, R.drawable.pie_15, R.drawable.pie_16, R.drawable.pie_17, R.drawable.pie_18, R.drawable.pie_19, R.drawable.pie_20, R.drawable.pie_21, R.drawable.pie_22, R.drawable.pie_23 };
	private final int mTapScreenTextAnimDuration = 10;
	private final int mTapScreenTextAnimBreak = -1;

	SceneAnimation animShoppie;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_logo);
		SettingPreference.setMem(this, 2);
		
		if (!SettingPreference.getFirstUse(this) && SettingPreference.getUserID(this) > 0) {
			startActivity(new Intent(ActivityLogo.this, HomeActivity.class));
			ActivityLogo.this.finish();
		} else {
			//startActivity(new Intent(ActivityLogo.this, ActivityWelcome.class));
			ActivityLogo.this.finish();
		}
		// testNotification(GlobalValue.EXTRA_TYPE_CHECKIN);
		// testNotification(GlobalValue.EXTRA_TYPE_PURCHASE);
		// showAnim();
		
	}
	
	

	public void testNotification(String type) {
		int notiId = 0;
		if (type.equals(GlobalValue.EXTRA_TYPE_CHECKIN))
			notiId = GlobalValue.NOTIFICATION_ID_CHECKIN;
		if (type.equals(GlobalValue.EXTRA_TYPE_REGISTER))
			notiId = GlobalValue.NOTIFICATION_ID_REGISTER;
		if (type.equals(GlobalValue.EXTRA_TYPE_PURCHASE))
			notiId = GlobalValue.NOTIFICATION_ID_PURCHARSE;
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
		@SuppressWarnings("unused")
		Notification noti = mBuilder.build();
		mBuilder.setSmallIcon(R.drawable.icon_launcher);
		mBuilder.setContentTitle("Shoppie");
		mBuilder.setContentText("Detail message");

		int numberMessage = 1;
		mBuilder.setNumber(++numberMessage);
		// noti.sound=Uri.parse("android.resource://" + getPackageName() + "/" +
		// R.raw.ting_ting );
		Intent intent = new Intent(this, HomeActivity.class);
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		stackBuilder.addParentStack(ActivityLogo.class);
		stackBuilder.addNextIntent(intent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		mBuilder.setAutoCancel(true);

		mNotificationManager.notify(notiId, mBuilder.build());
	}

	public void showAnim() {
		// mBtnBubble = (ImageView) findViewById(R.id.activity_logo_iv_bubble);
		tvLaunch = (TextView) findViewById(R.id.logo_tv_launch);

		// frameAnimation = (AnimationDrawable) mBtnBubble.getBackground();

		mTapScreenTextAnimImgView = (ImageView) findViewById(R.id.activity_logo_iv_bubble);
		animShoppie = new SceneAnimation(mTapScreenTextAnimImgView, false, mTapScreenTextAnimRes, mTapScreenTextAnimDuration, mTapScreenTextAnimBreak);
		animShoppie.setOnAnimationListener(new OnSceneAnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				mTapScreenTextAnimImgView.setVisibility(View.GONE);
			}
		});
		// mBtnBubble.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// anim.start();
		// }
		// });

		animLogo = AnimationUtils.loadAnimation(this, R.anim.anim_logo);
		animGrowCenter = AnimationUtils.loadAnimation(this, R.anim.anim_grow_from_center);
		animGrowCenter.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				Log.e("anim end", "start scene");
				animShoppie.playConstant(1);
			}
		});
		animLogo.setAnimationListener(new AnimationListener() {

			public void onAnimationStart(Animation animation) {

			}

			public void onAnimationRepeat(Animation animation) {

			}

			public void onAnimationEnd(Animation animation) {
				if (SettingPreference.getFirstUse(ActivityLogo.this)) {
					startActivity(new Intent(ActivityLogo.this, LoginActivity.class));
					// ActivityLogo.this.finish();
				} else {
					startActivity(new Intent(ActivityLogo.this, HomeActivity.class));
					// ActivityLogo.this.finish();
				}

			}
		});
		tvLaunch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mTapScreenTextAnimImgView.setVisibility(View.VISIBLE);

				// MediaPlayer mPlayer = MediaPlayer.create(ActivityLogo.this,
				// R.raw.ting_ting);
				// mPlayer.start();
				// mPlayer.setOnCompletionListener(new OnCompletionListener() {
				//
				// @Override
				// public void onCompletion(MediaPlayer mp) {
				// mp.stop();
				// mp.release();
				// }
				// });

				animGrowCenter.startNow();
				animShoppie.playConstant(1);
			}
		});
		tvLaunch.post(new Runnable() {

			@Override
			public void run() {
				// startActivity(new Intent(ActivityLogo.this,
				// ActivityWelcome.class));
				// tvLaunch.startAnimation(animLogo);
				Log.e("user id", SettingPreference.getUserID(ActivityLogo.this) + "");
				if (SettingPreference.getFirstUse(ActivityLogo.this)) {
					startActivity(new Intent(ActivityLogo.this, ActivityWelcome.class));
					// ActivityLogo.this.finish();
				} else {
					startActivity(new Intent(ActivityLogo.this, HomeActivity.class));
					// ActivityLogo.this.finish();
				}

			}
		});

		// check network: SUtil.java$isNetworkConnected()

		// check gcm: SUtilGCM.java

		// register account
		// DialogRegisterAccount dialog=new DialogRegisterAccount(this,
		// "Register", "Please fill some info");
		// dialog.show();
		// dialog.setCancelable(true);
	}

	// Thread anim = new Thread() {
	// public void run() {
	// mBtnBubble.post(new Runnable() {
	//
	// @Override
	// public void run() {
	// frameAnimation.start();
	// }
	// });
	//
	// long totalDuration = 0;
	// for (int i = 0; i < frameAnimation.getNumberOfFrames(); i++) {
	// totalDuration += frameAnimation.getDuration(i);
	// }
	// Timer timer = new Timer();
	//
	// TimerTask timerTask = new TimerTask() {
	// @Override
	// public void run() {
	// frameAnimation.stop();
	// mBtnBubble.post(new Runnable() {
	//
	// @Override
	// public void run() {
	// mBtnBubble.setBackgroundColor(ActivityLogo.this.getResources().getColor(android.R.color.transparent));
	// mBtnBubble.invalidate();
	// mBtnBubble.setVisibility(View.GONE);
	// }
	// });
	//
	// }
	// };
	// timer.schedule(timerTask, totalDuration);
	// };
	// };

	@Override
	protected void onResume() {
		super.onResume();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_logo, menu);
		return true;
	}

}
