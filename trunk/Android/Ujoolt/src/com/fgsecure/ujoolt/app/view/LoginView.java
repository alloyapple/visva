package com.fgsecure.ujoolt.app.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;

import com.fgsecure.ujoolt.app.widget.DraggableView;

public class LoginView extends DraggableView {
	public boolean isDisapear;
	public boolean isAppear;
	public int hiddenPoint;
	public boolean isLogined, isLoginning, isLogin;

	public LoginView(Context context, AttributeSet attrs) {
		super(context, attrs);
		type = LOGIN;
		orientation = VERTICAL;
	}

	@Override
	public void clampScroll() {
		if (!isDisapear && !isAppear) {
			if (!touching && isStarted && !isQuitRegister && !isStartJolt) {
				if (isMoveUp && !isMoveDown) {
					if (scroll < top_max) {
						scroll += (top_max - scroll) / ACCELERATION;
						if (scroll >= top_max || (top_max - scroll) / ACCELERATION == 0) {
							scroll = top_max;
						}
					} else {
						isMoveUp = false;
						isMoveDown = false;
						isDisapear = false;
						isAppear = false;
					}
				} else if (isMoveDown && !isMoveUp) {
					mainScreenActivity.hideKeyBoardLoginView();
					if (scroll > bottom_max) {
						scroll -= (scroll - bottom_max) / ACCELERATION;
						if (scroll <= bottom_max || (scroll - bottom_max) / ACCELERATION == 0) {
							scroll = bottom_max;
						}
					} else {
						isMoveDown = false;
						isMoveUp = false;
						isDisapear = false;
						isAppear = false;
						// mainScreenActivity.layoutSearch.setVisibility(View.VISIBLE);
						mainScreenActivity.viewSearch.appear(SearchView.REGISTER);
						isLoginning = false;
						mainScreenActivity.isBubbleDetail = false;
						mainScreenActivity.imgToungeLogin.setOnTouchListener(mainScreenActivity);
						this.setOnTouchListener(null);
					}
				}
			}
			if (mainScreenActivity.isQuitRegister) {
				if (scroll < disappearPoint) {
					scroll += (disappearPoint - scroll) / ACCELERATION;
					if (scroll >= disappearPoint || (disappearPoint - scroll) / ACCELERATION == 0) {
						scroll = disappearPoint;
					}
				} else {
					this.setOnTouchListener(null);
					mainScreenActivity.isQuitRegister = false;
					mainScreenActivity.isStartJolt = true;

					isLoginning = false;
					isLogin = false;
					isLogined = true;
					mainScreenActivity.viewJolt.scroll = mainScreenActivity.viewJolt.disappearPoint;
					mainScreenActivity.viewJolt.setVisibility(View.VISIBLE);

					// MainScreenActivity.isRegister = false;
				}
			} else if (mainScreenActivity.isBackRegister) {
				if (scroll > top_max) {
					scroll -= (scroll - top_max) / ACCELERATION;
					if (scroll <= top_max || (scroll - top_max) / ACCELERATION == 0) {
						scroll = top_max;
					}
				} else {
					this.setOnTouchListener(mainScreenActivity);
					mainScreenActivity.isBackRegister = false;
					isLogin = true;
					isLoginning = true;
					isLogined = false;
					this.isStarted = true;
				}
			}
		} else if (isDisapear && !isAppear) {
			if (scroll > hiddenPoint) {
				scroll -= 40;
				if (scroll <= hiddenPoint) {
					scroll = hiddenPoint;
				}
			} else {
				isDisapear = false;
				isAppear = false;
			}
		} else if (isAppear && !isDisapear) {
			if (scroll < bottom_max) {
				scroll += 40;
				if (scroll >= bottom_max) {
					scroll = bottom_max;
				}
			} else {
				isStarted = false;
				isAppear = false;
				isDisapear = false;
			}
		}
	}

	public void move(int move, int xTouch, int yTouch) {
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
			// clampScroll();
			if (scroll > top_max) {
				scroll = top_max;
			}
			if (scroll < bottom_max) {
				scroll = bottom_max;
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
			// int y =
			getChildAt(0).getPaddingTop();
			if (lastDelta >= 0) {
				isMoveUp = true;
				isMoveDown = false;
			} else if (lastDelta < 0) {
				isMoveDown = true;
				isMoveUp = false;
			}

			break;
		}
	}

	public void setMove(int sY, int dY) {
		TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, sY, dY);
		translateAnimation.setDuration(1000);
		translateAnimation.setFillAfter(true);
		getChildAt(0).startAnimation(translateAnimation);
	}

	public int getHiddenPoint() {
		return hiddenPoint;
	}

	public void setHiddenPoint(int hiddenPoint) {
		this.hiddenPoint = hiddenPoint;
	}

	public void disappear() {
		if (!isDisapear && !isAppear) {
			isDisapear = true;
			isAppear = false;
			isStarted = true;
		}
	}

	public void appear() {
		if (!isDisapear && !isAppear) {
			this.setVisibility(View.VISIBLE);
			isAppear = true;
			isDisapear = false;
		}
	}

	public void reset() {
		isAppear = false;
		isDisapear = false;
		isLogined = false;
		isLoginning = false;
	}
}
