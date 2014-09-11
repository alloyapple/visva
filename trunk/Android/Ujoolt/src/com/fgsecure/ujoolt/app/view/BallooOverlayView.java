package com.fgsecure.ujoolt.app.view;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fgsecure.ujoolt.app.R;
import com.fgsecure.ujoolt.app.define.NumberFacebookFriend;
import com.fgsecure.ujoolt.app.define.Orange;
import com.fgsecure.ujoolt.app.define.State;
import com.fgsecure.ujoolt.app.define.Type;
import com.fgsecure.ujoolt.app.json.Jolt;
import com.fgsecure.ujoolt.app.screen.ItemizedOverlays;
import com.fgsecure.ujoolt.app.screen.MainScreenActivity;
import com.fgsecure.ujoolt.app.utillity.ConfigUtility;
import com.fgsecure.ujoolt.app.utillity.Utility;
import com.google.android.maps.OverlayItem;

public class BallooOverlayView<Item extends OverlayItem> extends FrameLayout implements
		View.OnClickListener {

	private LinearLayout layout;
	public TextView lblNumberRejolt;
	private RelativeLayout relaytivelayout;

	// public ImageView lblNumberRejolt;
	// public ImageView imv_icon;

	private TextView lblNumberJoltInGroup;
	private String TAG = getClass().getSimpleName();
	private ItemizedOverlays itemizedOverlays;
	private MainScreenActivity mainScreenActivity;

	public int type;
	private double radius;

	public Runnable runnableCountLifejolt;
	public Thread myThread = null;

	public static final byte NORMAL = 0;
	public static final byte CIRCLE = 1;

	/**
	 * Create a new BalloonOverlayView
	 * 
	 * @param context
	 *            - The activity context.
	 * @param balloonBottomOffset
	 *            - The bottom padding (in pixels) to be applied when rendering
	 *            this view.
	 */
	public BallooOverlayView(ItemizedOverlays itemizedOverlays, int balloonBottomOffset, int type,
			double radius, byte t) {

		super(itemizedOverlays.mainScreenActivity);
		this.type = type;
		mainScreenActivity = itemizedOverlays.mainScreenActivity;
		setPadding(0, 0, 0, 0);
		this.itemizedOverlays = itemizedOverlays;

		layout = new LinearLayout(mainScreenActivity);
		layout.setVisibility(VISIBLE);

		LayoutInflater inflater = (LayoutInflater) mainScreenActivity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.balloon_overlay, layout);

		// if (relaytivelayout == null) {

		// relaytivelayout.setBackgroundResource(R.drawable.normal_jolt_orange_5_fb_0);
		// }

		// if (lblNumberRejolt == null) {
		relaytivelayout = (RelativeLayout) v.findViewById(R.id.ballon_overlay);
		lblNumberRejolt = (TextView) v.findViewById(R.id.textView_balloon);

		lblNumberRejolt.setOnClickListener(this);
		relaytivelayout.setOnClickListener(this);
		// }

		if (type == 0) {
			lblNumberJoltInGroup = (TextView) v.findViewById(R.id.text_view_number_jolt_in_group);
			setIcon();
			if (t == Utility.CREATE) {
				setAnimationWhenDisplay();
			}
		} else {
			this.radius = radius;
			layout = new LinearLayout(mainScreenActivity);
			layout.setVisibility(VISIBLE);
		}

		// FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
		// 400, 300);
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.CENTER;

		// MapView.LayoutParams params = new
		// MapView.LayoutParams(MapView.LayoutParams.WRAP_CONTENT,
		// MapView.LayoutParams.WRAP_CONTENT, null,
		// MapView.LayoutParams.BOTTOM_CENTER);

		addView(layout, params);
	}

	public void gotoActivityInGroup(Context context, Class<?> cla, String key, CharSequence value) {
		Intent previewMessage = new Intent(context, cla);
		previewMessage.putExtra(key, value);
	}

	public void setIcon() {

		Jolt jolt = itemizedOverlays.getCurrentJolt();
		if (jolt != null) {

			int size = itemizedOverlays.arrayJolt.size();
			if (size <= 1) {
				lblNumberJoltInGroup.setVisibility(View.GONE);
			} else {
				lblNumberJoltInGroup.setText("" + size);
				if (size < 10) {
					lblNumberJoltInGroup.setBackgroundResource(R.drawable.bg_number_group_jolt_1);
				} else if (size < 100) {
					lblNumberJoltInGroup.setBackgroundResource(R.drawable.bg_number_group_jolt_2);
				} else {
					lblNumberJoltInGroup.setBackgroundResource(R.drawable.bg_number_group_jolt_3);
				}
				lblNumberJoltInGroup.setVisibility(View.VISIBLE);
			}

			/*
			 * Set Icon for jolts
			 */
			setNormalIcon(jolt);
		}
	}

	public void setNormalIcon(Jolt jolt) {
		jolt.setNumberFacebookRejolt(mainScreenActivity.arrFriendsFacebook);

		long timeAlive = ConfigUtility.getCurTimeStamp() - jolt.getDate();

		if (jolt.isInstagram()) {
			setResourceForIconInstagramJolt(jolt, timeAlive);

		} else if (jolt.isFacebook()) {
			setResourceForIconFacebookJolt(jolt, timeAlive);

		} else if (jolt.isMyJolt()) {
			setResourceForIconMyJolt(jolt);

		} else {
			setResourceForIconNormalJolt(jolt, timeAlive);
		}

		if (jolt.getNumberRejolt() > 0) {
			lblNumberRejolt.setText("" + jolt.getNumberRejolt());
		} else {
			lblNumberRejolt.setText("");
		}
	}

	public void setSizeForIcon(Jolt jolt) {
		int numberRejolt = jolt.getNumberRejolt();
		int newSize;

		// if (ConfigUtility.scrWidth <= 320) {
		if (numberRejolt == 0) {
			newSize = ConfigUtility.R1;
		} else if (numberRejolt >= 1 && numberRejolt < 10) {
			newSize = ConfigUtility.R2;
		} else if (numberRejolt >= 10 && numberRejolt <= 20) {
			newSize = ConfigUtility.R3;
		} else {
			newSize = ConfigUtility.R4;
		}
		LinearLayout.LayoutParams a = new LinearLayout.LayoutParams(newSize, newSize);
		relaytivelayout.setLayoutParams(a);

		// } else if (ConfigUtility.scrWidth <= 480) {
		// if (numberRejolt == 0) {
		// newSize = 150;
		// } else if (numberRejolt >= 1 && numberRejolt < 10) {
		// newSize = 165;
		// } else if (numberRejolt >= 10 && numberRejolt <= 20) {
		// newSize = 185;
		// } else {
		// newSize = 210;
		// }
		// LinearLayout.LayoutParams a = new LinearLayout.LayoutParams(newSize,
		// newSize);
		// relaytivelayout.setLayoutParams(a);
		//
		// } else {
		// if (numberRejolt == 0) {
		// newSize = 200;
		// } else if (numberRejolt >= 1 && numberRejolt < 10) {
		// newSize = 220;
		// } else if (numberRejolt >= 10 && numberRejolt <= 20) {
		// newSize = 250;
		// } else {
		// newSize = 290;
		// }
		// LinearLayout.LayoutParams a = new LinearLayout.LayoutParams(newSize,
		// newSize);
		// relaytivelayout.setLayoutParams(a);
		// }
	}

	public State getState(Jolt jolt) {
		boolean isTop = jolt.isTop();
		boolean isLike = jolt.isLike();
		if (isTop) {
			return State.TOP;
		} else if (isLike) {
			return State.FAVOURITE;
		} else {
			return State.NONE;
		}
	}

	public NumberFacebookFriend getNumberFacebookFriend(Jolt jolt) {
		int numberFacebookRejolt = jolt.getNumberFacebookRejolt();

		if (numberFacebookRejolt == 0) {
			return NumberFacebookFriend.ZERO;
		} else if (numberFacebookRejolt == 1 || numberFacebookRejolt == 2) {
			return NumberFacebookFriend.ONE;
		} else if (numberFacebookRejolt >= 3 && numberFacebookRejolt <= 5) {
			return NumberFacebookFriend.TWO;
		} else if (numberFacebookRejolt >= 6 && numberFacebookRejolt <= 9) {
			return NumberFacebookFriend.THREE;
		} else if (numberFacebookRejolt >= 10 && numberFacebookRejolt <= 14) {
			return NumberFacebookFriend.FOUR;
		} else {
			return NumberFacebookFriend.FIVE;
		}
	}

	public Orange getOrange(long timeAlive) {
		if (timeAlive <= 900) {
			return Orange.ONE;
		} else if (timeAlive <= 4500) {
			return Orange.TWO;
		} else if (timeAlive <= 8100) {
			return Orange.THREE;
		} else if (timeAlive <= 11700) {
			return Orange.FOUR;
		} else {
			return Orange.FIVE;
		}
	}

	public void setResourceForIconInstagramJolt(Jolt jolt, long timeAlive) {

		State state = getState(jolt);
		NumberFacebookFriend numberFacebookFriend = getNumberFacebookFriend(jolt);
		Orange orange = getOrange(timeAlive);

		int resID = IconView.getResouceIcon(Type.INSTAGRAM, state, orange, numberFacebookFriend);
		if (resID == 0) {
			Toast.makeText(mainScreenActivity, "get wrong resId instagram", Toast.LENGTH_LONG)
					.show();
		}

		// relaytivelayout.setBackgroundResource(resID);
		lblNumberRejolt.setBackgroundResource(resID);

		setSizeForIcon(jolt);
	}

	public void setResourceForIconFacebookJolt(Jolt jolt, long timeAlive) {

		State state = getState(jolt);
		NumberFacebookFriend numberFacebookFriend = getNumberFacebookFriend(jolt);
		Orange orange = getOrange(timeAlive);

		int resID = IconView.getResouceIcon(Type.FACEBOOK, state, orange, numberFacebookFriend);
		if (resID == 0) {
			Toast.makeText(mainScreenActivity, "get wrong resId facebook", Toast.LENGTH_LONG)
					.show();
		}
		// relaytivelayout.setBackgroundResource(resID);
		lblNumberRejolt.setBackgroundResource(resID);

		setSizeForIcon(jolt);
	}

	public void setResourceForIconMyJolt(Jolt jolt) {

		State state = getState(jolt);
		NumberFacebookFriend numberFacebookFriend = getNumberFacebookFriend(jolt);

		int resID = IconView.getResouceIconMyJolt(state, numberFacebookFriend);
		if (resID == 0) {
			Toast.makeText(mainScreenActivity, "get wrong resId my jolt", Toast.LENGTH_LONG).show();
		}

		// relaytivelayout.setBackgroundResource(resID);
		lblNumberRejolt.setBackgroundResource(resID);

		setSizeForIcon(jolt);
	}

	public void setResourceForIconNormalJolt(Jolt jolt, long timeAlive) {

		State state = getState(jolt);
		NumberFacebookFriend numberFacebookFriend = getNumberFacebookFriend(jolt);
		Orange orange = getOrange(timeAlive);

		int resID = IconView.getResouceIcon(Type.NORMAL, state, orange, numberFacebookFriend);
		if (resID == 0) {
			Toast.makeText(mainScreenActivity, "get wrong resId normal", Toast.LENGTH_LONG).show();
		}

		// relaytivelayout.setBackgroundResource(resID);
		lblNumberRejolt.setBackgroundResource(resID);
		setSizeForIcon(jolt);
	}

	public void setCircleIcon() {
		int w = Utility.convertMeterToPixels(mainScreenActivity.mapView, (float) radius);
		Log.e(TAG, "ra " + w);
		if (w * 2 < ConfigUtility.scrWidth) {
			lblNumberRejolt.setVisibility(View.VISIBLE);
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(w * 2, w * 2);
			lblNumberRejolt.setLayoutParams(layoutParams);

		} else {
			lblNumberRejolt.setVisibility(View.INVISIBLE);
		}
	}

	public void zoomIcon() {
		AnimationSet animSet = new AnimationSet(true);
		ScaleAnimation scale = new ScaleAnimation(1f, 1.2f, 1f, 1.2f, Animation.RELATIVE_TO_SELF,
				(float) 0.5, Animation.RELATIVE_TO_SELF, (float) 0.5);
		scale.setDuration(150);

		animSet.addAnimation(scale);
		animSet.setFillEnabled(true);
		animSet.setFillAfter(true);

		// lblNumberRejolt.clearAnimation();
		// lblNumberRejolt.setVisibility(View.VISIBLE);
		// lblNumberRejolt.startAnimation(animSet);

		// relaytivelayout.setVisibility(View.VISIBLE);

		relaytivelayout.clearAnimation();
		relaytivelayout.startAnimation(animSet);
	}

	public void resetIcon() {
		AnimationSet animSet = new AnimationSet(true);
		ScaleAnimation scale = new ScaleAnimation(1.2f, 1f, 1.2f, 1f, Animation.RELATIVE_TO_SELF,
				(float) 0.5, Animation.RELATIVE_TO_SELF, (float) 0.5);
		scale.setDuration(150);

		animSet.addAnimation(scale);
		animSet.setFillEnabled(true);
		animSet.setFillAfter(true);
		// lblNumberRejolt.clearAnimation();
		// lblNumberRejolt.startAnimation(animSet);

		relaytivelayout.clearAnimation();
		relaytivelayout.startAnimation(animSet);
	}

	@Override
	public void onClick(View v) {
		// if (v == lblNumberRejolt) {
		// Log.d("Onclick jolt", "ok");
		mainScreenActivity.hideListRejolt();
		viewCurrentJoltAndShowDetail();
		// }
		//
		// if (v == relaytivelayout) {
		// viewCurrentJoltAndShowDetail();
		// }
	}

	public void viewCurrentJoltAndShowDetail() {

		mainScreenActivity.viewInformation.setVisibility(View.VISIBLE);

		if (!mainScreenActivity.viewInformation.isStarted
				|| (mainScreenActivity.viewInformation.isStarted && mainScreenActivity.viewInformation.scroll == mainScreenActivity.viewInformation.top_max)) {
			if (mainScreenActivity.isSearch) {
				mainScreenActivity.endSearch();
			}

			ItemizedOverlays item = mainScreenActivity.getCurItem();
			if (item != null) {
				item.balloonView.resetIcon();
			}
			Log.e(TAG, "Click jolt");

			mainScreenActivity.isTapOnItem = true;
			mainScreenActivity.curTapItem = itemizedOverlays.positionOfItemizedOverlay;

			itemizedOverlays.centerMap();

			mainScreenActivity.curItemizedOverlays = itemizedOverlays;

			mainScreenActivity.isBubbleDetail = true;
			// if (main.isShowDetail)

			itemizedOverlays.index = 1;

			itemizedOverlays.setInformation();

			if (runnableCountLifejolt != null)
				runnableCountLifejolt = null;

			myThread = new Thread(runnableCountLifejolt);
			myThread.start();

			// if (main.view_search.isAvailable) {
			// main.view_search.disappear(SearchView.INFO);
			// }

			mainScreenActivity.setShowSearchLayout(View.GONE);
			mainScreenActivity.viewSearch.disappear(SearchView.JOLT);
			mainScreenActivity.viewInformation.setVisibility(View.VISIBLE);
			mainScreenActivity.isBubbleDetail = true;
			mainScreenActivity.viewInformation.appear();

			if (mainScreenActivity.isUpdate) {
				mainScreenActivity.groupArrayJoltFilter();
				mainScreenActivity.isUpdate = false;
			}

			zoomIcon();
		}
	}

	public void setAnimationWhenDisplay() {
		Animation hyperspaceJump = AnimationUtils.loadAnimation(mainScreenActivity,
				R.anim.scale_out);
		// lblNumberRejolt.startAnimation(hyperspaceJump);
		relaytivelayout.clearAnimation();
		relaytivelayout.startAnimation(hyperspaceJump);
	}

	public void disappear() {
		lblNumberJoltInGroup.setVisibility(View.GONE);

		AnimationSet animSet = new AnimationSet(true);
		AlphaAnimation alpha = new AlphaAnimation(1f, 0f);
		alpha.setDuration(500);
		animSet.addAnimation(alpha);
		animSet.setFillEnabled(true);
		animSet.setFillAfter(true);
		alpha.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				itemizedOverlays.balloonView.setVisibility(View.GONE);
			}
		});
		// lblNumberRejolt.clearAnimation();
		// lblNumberRejolt.startAnimation(alpha);

		// relaytivelayout.clearAnimation();
		// relaytivelayout.startAnimation(alpha);
	}

	// private void setAnimationZoom() {
	// Animation hyperspaceJump = AnimationUtils.loadAnimation(main,
	// R.anim.scale_zoom);
	// // lblNumberRejolt.startAnimation(hyperspaceJump);
	// relaytivelayout.startAnimation(hyperspaceJump);
	// }
	//
	// private void zoomInIcon(View view) {
	// Animation hyperspaceJump = AnimationUtils.loadAnimation(main,
	// R.anim.scale_zoom);
	// view.startAnimation(hyperspaceJump);
	// }
}
