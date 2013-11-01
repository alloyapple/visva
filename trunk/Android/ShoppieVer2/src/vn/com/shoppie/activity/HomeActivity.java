package vn.com.shoppie.activity;

import vn.com.shoppie.R;
import vn.com.shoppie.adapter.CatelogyAdapter;
import vn.com.shoppie.view.MPager;
import vn.com.shoppie.view.OnItemClick;
import vn.com.shoppie.view.MPager.OnStartExtend;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.Interpolator;
import android.view.animation.RotateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class HomeActivity extends VisvaAbstractActivity {
	private RelativeLayout actionBar;
	private View checkinCircle;
	private MPager pager;
	private CatelogyAdapter adapter;
	private boolean isChecked = false;

	@Override
	public int contentView() {
		// TODO Auto-generated method stub
		return R.layout.home_act;

	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public void onCreate() {
		if (Build.VERSION.SDK_INT >= 11)
			getWindow().setFlags(
					WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
					WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
		
		//setup actionbar
		actionBar = (RelativeLayout) findViewById(R.id.actionbar);
		TextView tvTitle = new TextView(this);
		tvTitle.setGravity(Gravity.CENTER);
		tvTitle.setText("Tìm nơi tích điểm");
		actionBar.addView(tvTitle, -1, -1);
		
		checkinCircle = findViewById(R.id.checkin_circle);
		pager = (MPager) findViewById(R.id.pager);
		adapter = new CatelogyAdapter(this);
		pager.setAdapter(adapter);
		
		pager.setOnStartExtendListenner(new OnStartExtend() {
			
			@Override
			public void onExtend(View v) {
				isChecked = false;
				setCheckIn(isChecked);
			}
			
			@Override
			public void onCollapse(View v) {
				
			}
		});
		
		adapter.setOnItemClick(new OnItemClick() {
			
			@Override
			public void onClick(int pos) {
				Log.d("OnClick", "Pos " + pos);
				changeToActivity(new Intent(HomeActivity.this, CollectionList.class), false);
			}
		});
	}
	
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_canhan:
			// isChecked = false;
			// setCheckIn(isChecked);
			// changeToActivity(new Intent(this, HomeActivity.class), false);
			// pager.extendView();
			gotoActivity(HomeActivity.this, PersonalInfoActivity.class);
			break;
		case R.id.bt_quatang:
			// goBack();
			pager.collapseView();
			break;
		case R.id.checkin:
			isChecked = !isChecked;
			setCheckIn(isChecked);
			break;

		default:
			break;
		}
	}

	private void setCheckIn(boolean isChecked) {
		if (isChecked) {
			int angle = 36000000;
			final RotateAnimation anim = new RotateAnimation(0, angle,
					checkinCircle.getWidth() / 2, checkinCircle.getHeight() / 2);
			final RotateAnimation anim1 = new RotateAnimation(0, angle,
					checkinCircle.getWidth() / 2, checkinCircle.getHeight() / 2);

			anim.setDuration(angle * 15);
			anim1.setDuration(angle * 15);

			anim.setInterpolator(new Interpolator() {

				@Override
				public float getInterpolation(float input) {
					// TODO Auto-generated method stub
					return input;
				}
			});
			anim1.setInterpolator(anim.getInterpolator());

			anim.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {
				}

				@Override
				public void onAnimationRepeat(Animation animation) {
				}

				@Override
				public void onAnimationEnd(Animation animation) {
					checkinCircle.startAnimation(anim1);
				}
			});

			checkinCircle.startAnimation(anim);
		} else {
			checkinCircle.clearAnimation();
		}
	}
}
