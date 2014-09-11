package com.fgsecure.ujoolt.app.view;

import java.util.ArrayList;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.TextView;
import android.widget.Toast;

import com.fgsecure.ujoolt.app.define.Step;
import com.fgsecure.ujoolt.app.json.Jolt;
import com.fgsecure.ujoolt.app.screen.ItemizedOverlays;
import com.fgsecure.ujoolt.app.utillity.CountDownControl;
import com.fgsecure.ujoolt.app.utillity.Language;
import com.fgsecure.ujoolt.app.widget.DraggableView;

public class InformationView extends DraggableView {
	public static int QUIT = 0;
	public static int JOLT = 1;
	public static int REGISTER = 2;
	public boolean isDown, isMove, isUp, isDrag;
	public boolean isRejolt;
	public boolean isDisappear, isAppear;
	public boolean isEnableDrag;
	public boolean isEnableDelte;
	public ArrayList<Jolt> arrJolt = new ArrayList<Jolt>();
	public Jolt curJolt;
	// private int curId;
	// display Time
	private CountDownTimer myTimer;
	// private long curTime;
	// private long startTime;
	// private long timeToLive;
	// private long timeLeft;
	public boolean isPressRejoltButton;
	public boolean isPressGoDown, isPressGoUp;
	public boolean isStartCountDown;
	public CountDownControl countDownControl;

	// private static final int MY_TIME = 14400;

	public InformationView(Context context, AttributeSet attrs) {
		super(context, attrs);
		orientation = VERTICAL;
		// curJolt = arrJolt.get(curId);
	}

	public void clampScroll() {
		if (!isDisappear && !isAppear && !isPressRejoltButton) {
			if (!touching && scroll < top_max) {
				scroll += (top_max - scroll) / ACCELERATION;
				if (scroll >= top_max || (top_max - scroll) / ACCELERATION == 0) {
					scroll = top_max;
					if (mainScreenActivity.isRejolt && isRejolt) {
						RotateAnimation rotateAnimation = new RotateAnimation(180f, 0f,
								Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
						rotateAnimation.setFillAfter(true);
						rotateAnimation.setDuration(500);
						mainScreenActivity.imgArrowLeft.startAnimation(rotateAnimation);
						mainScreenActivity.imgArrowRight.startAnimation(rotateAnimation);
						mainScreenActivity.lblDragAndReleaseToRejolt.setText(Language.pullToRejolt);
						isRejolt = false;
						// mainScreenActivity.layoutInfomationFrame
						// .setOnTouchListener(mainScreenActivity);
						this.setOnTouchListener(null);

						rejolt();
					}
					// if(mainScreenActivity.view_register.isRegister){
					// mainScreenActivity.view_register.appear();
					// } else {
					// mainScreenActivity.view_jolt.appear();
					// }

					// mainScreenActivity.layoutInfomationFrame.setOnTouchListener(mainScreenActivity);
					mainScreenActivity.isRejolt = false;

					this.setOnTouchListener(null);
				}
			}
		} else if (isPressRejoltButton) {
			if (isPressGoDown && !isPressGoUp) {
				if (scroll > bottom_max) {
					scroll -= (scroll - bottom_max) / 3;
					if (scroll <= bottom_max || (scroll - bottom_max) / ACCELERATION == 0) {
						scroll = bottom_max;
					}
				} else {
					isPressGoDown = false;
					isPressGoUp = true;
				}
			} else if (isPressGoUp && !isPressGoDown) {
				if (scroll < top_max) {
					scroll += (top_max - scroll) / ACCELERATION;
					if (scroll >= top_max || (top_max - scroll) / ACCELERATION == 0) {
						scroll = top_max;
					}
				} else {

					isPressGoDown = false;
					isPressGoUp = false;
					isPressRejoltButton = false;
					// mainScreenActivity.button_rejolt
					// .setVisibility(View.INVISIBLE);
					rejolt();

				}
			}
		} else if (isDisappear && !isAppear) {
			if (scroll < hiddenPoint) {
				scroll += (hiddenPoint - scroll) / ACCELERATION;
				if (scroll >= hiddenPoint || (hiddenPoint - scroll) / ACCELERATION == 0) {
					scroll = hiddenPoint;
				}
			} else {
				isStarted = false;
				isDisappear = false;
				isAppear = false;
				mainScreenActivity.isBubbleDetail = false;
				mainScreenActivity.isRejolt = false;
				if (typeAction == QUIT) {
					mainScreenActivity.viewSearch.appear(SearchView.INFO);
					// mainScreenActivity.view_jolt.appear();
				}

			}
		} else if (isAppear && !isDisappear) {
			if (scroll > top_max) {
				scroll -= (scroll - top_max) / ACCELERATION;
				if (scroll <= top_max || (scroll - top_max) / ACCELERATION == 0) {
					// scroll = top_max;
					scroll = top_max;
				}
			} else {
				isDisappear = false;
				isAppear = false;
			}
		}
	}

	public void move(int move, int xTouch, int yTouch) {
		if (isEnableDrag) {
			switch (move & MotionEvent.ACTION_MASK) {

			case MotionEvent.ACTION_DOWN:

				enabled = true;
				lastX = xTouch;
				lastY = yTouch;
				touching = true;
				break;

			case MotionEvent.ACTION_MOVE:

				isMove = true;
				if (type == INFORMATION) {
					if (!isStarted) {
						isStarted = true;
					}
				}
				int delta = lastY - yTouch;
				scroll += delta;
				if (scroll <= -(top_max - 100)) {
					if (mainScreenActivity.viewLogin.isLogin) {
						mainScreenActivity.viewLogin.disappear();
					} else {
						mainScreenActivity.viewJolt.disappear();
					}
				}
				// clampScroll();
				if (scroll > top_max) {
					scroll = top_max;
				}
				if (scroll < bottom_max) {
					scroll = bottom_max;
					if (!isRejolt) {
						RotateAnimation rotateAnimation = new RotateAnimation(0f, 180f,
								Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
						rotateAnimation.setFillAfter(true);
						rotateAnimation.setDuration(500);
						mainScreenActivity.imgArrowLeft.startAnimation(rotateAnimation);
						mainScreenActivity.imgArrowRight.startAnimation(rotateAnimation);
						mainScreenActivity.lblDragAndReleaseToRejolt.setText(Language.releaseToRejolt);
						isRejolt = true;
					}
				}
				if (Math.abs(delta) > 2)
					enabled = false;
				onLayout(true, getLeft(), getTop(), getRight(), getBottom());
				lastX = xTouch;
				lastY = yTouch;
				lastDelta = delta;

				break;

			case MotionEvent.ACTION_UP:
				touching = false;
				isMove = false;
				if (lastDelta >= 0) {
					isUp = true;
					isDown = false;
				} else if (lastDelta < 0) {
					isDown = true;
					isUp = false;
				}

				// if(isRejolt){
				// // ItemizedOverlays itemizedOverlays = (ItemizedOverlays)
				// mainScreenActivity.mapOverlays.get(mainScreenActivity.curTapItem);
				// // Jolt jolt = itemizedOverlays.jolt;
				// // mainScreenActivity.jolter = jolt;
				// // mainScreenActivity.joltHolder.postRejolt(jolt);
				// //
				// mainScreenActivity.joltHolder.postRejolt(jolt.getLatitude(),
				// jolt.getLongitude(), jolt.getDevice_id(), jolt.getNick(),
				// jolt.getId());
				// }
				break;
			}
		}
	}

	public void disappear(int type) {
		setTypeAction(type);
		isDisappear = true;
		isAppear = false;

		mainScreenActivity.hideListRejolt();
		mainScreenActivity.linearLayoutJoltDetail.setVisibility(View.VISIBLE);
		mainScreenActivity.isShowingdetail = false;

		ItemizedOverlays.handlerCountLifejolt.removeCallbacksAndMessages(null);
	}

	public void appear() {
		this.setVisibility(View.VISIBLE);
		isAppear = true;
		isDisappear = false;
		isStarted = true;

		mainScreenActivity.layoutViewListRejolt.removeAllViewsInLayout();
		mainScreenActivity.hideAllFilter();
		mainScreenActivity.inactivePropogation();
		mainScreenActivity.isShowJoltSource = false;
		mainScreenActivity.mapView.getOverlays().remove(mainScreenActivity.circleOverlay);
		mainScreenActivity.isShowingdetail = true;
		if (mainScreenActivity.isTutorialMode && mainScreenActivity.step == Step.FIVE_HALF) {
			mainScreenActivity.showStep(Step.SIX);
		}
	}

	public void reset() {
		isAppear = false;
		isDisappear = false;
		isStarted = false;
		isRejolt = false;
		isDrag = false;
		isEnableDrag = false;
	}

	public void setCountDownTimer(TextView textView, long lifeTime, long startTime, long curTime,
			boolean isRun) {
		if (countDownControl != null) {
			countDownControl.stop();
		}

		if (isRun == true) {
			countDownControl = new CountDownControl(textView, lifeTime, curTime, startTime);
			countDownControl.start();
		}
	}

	public CountDownTimer getMyTimer() {
		return myTimer;
	}

	public void setMyTimer(CountDownTimer myTimer) {
		this.myTimer = myTimer;
	}

	public void rejolt() {

		if (!mainScreenActivity.viewLogin.isLogin) {
			ItemizedOverlays itemizedOverlays = mainScreenActivity.curItemizedOverlays;
			Jolt jolt = itemizedOverlays.getCurrentJolt();
			mainScreenActivity.joltHolder.postRejolt(jolt);

		} else {
			Toast.makeText(mainScreenActivity, Language.notifyLogin, Toast.LENGTH_LONG).show();
		}

		// Log.e("dddd", "RRR " + jolt.isRejolted);
		// Log.e("dddd", "RRR " + jolt.isRejolted);
		// Log.e("dddd", "RRR " + jolt.isRejolted);
		// Log.e("dddd", "RRR " + jolt.isRejolted);
		// Log.e("dddd", "RRR " + jolt.isRejolted);

		// if (!jolt.isRejolted) {

		// if (jolt.isFacebook()) {
		//
		// for (int i = 0; i <
		// mainScreenActivity.joltHolder.arrAvailableJoltFacebook.size();
		// i++) {
		//
		// if (jolt.getFacebookId().equals(
		// mainScreenActivity.joltHolder.arrAvailableJoltFacebook.get(i)
		// .getFacebookId())) {
		// mainScreenActivity.joltHolder
		// .postRejolt(mainScreenActivity.joltHolder.arrAvailableJoltFacebook
		// .get(i));
		//
		// mainScreenActivity.button_rejolt.setBackgroundResource(R.drawable.delete);
		// mainScreenActivity.button_register.setVisibility(View.VISIBLE);
		// break;
		// }
		// }
		//
		// } else if (jolt.isInstagram()) {
		//
		// mainScreenActivity.joltHolder.postRejolt(jolt);

		// for (int i = 0; i <
		// mainScreenActivity.joltHolder.arrAvailableJoltInstagram.size();
		// i++) {
		// if (jolt.getInstagramId().equals(
		// mainScreenActivity.joltHolder.arrAvailableJoltInstagram.get(i)
		// .getInstagramId())) {
		// mainScreenActivity.joltHolder
		// .postRejolt(mainScreenActivity.joltHolder.arrAvailableJoltInstagram
		// .get(i));
		//
		// mainScreenActivity.button_rejolt.setBackgroundResource(R.drawable.delete);
		// mainScreenActivity.button_register.setVisibility(View.VISIBLE);
		// break;
		// }
		// }

		// } else {
		// jolt.setNumberRejolt(jolt.getNumberRejolt() + 1);
		// jolt.setRadius(jolt.getRadius() + 50);
		// if (jolt.getLifeTime() <= 14.0)
		// jolt.setLifeTime(jolt.getLifeTime() + 1);
		// q
		// jolt.isRejolted = true;
		// itemizedOverlays.balloonView.setIcon();
		// itemizedOverlays.setInformation();
		// mainScreenActivity.groupArrayJolt(JoltHolder.GET_DEFAULT);
		// mainScreenActivity.textView_rejolt
		// .setText(R.string.text_view_drag_to_rejolt);
		// mainScreenActivity.joltHolder.postRejolt(jolt);

		// mainScreenActivity.button_rejolt
		// .setVisibility(View.INVISIBLE);
		// }

		// } else {
		// mainScreenActivity
		// .showDialog("This Jolt has already rejolted !");
		// Toast.makeText(getContext(), "This Jolt has already rejolted !",
		// Toast.LENGTH_SHORT)
		// .show();
		// }
		// } else {
		// mainScreenActivity.showDialog("You have not login yet");
		// }

	}
}
