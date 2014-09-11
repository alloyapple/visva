package com.fgsecure.ujoolt.app.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.ScaleAnimation;

import com.fgsecure.ujoolt.app.widget.DraggableView;

public class SearchItemView extends DraggableView {
	public SearchItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		orientation = HORIZONAL;
		type = SEARCH_ITEM;
	}

	public void clampScroll() {
		if (!touching && isStarted && !isQuitRegister && !isStartJolt) {
			if (!isMoveLeft && isMoveRight) {
				if (scroll < right_max) {
					scroll += (right_max - scroll) / ACCELERATION;
					if (scroll >= right_max || (right_max - scroll) / ACCELERATION == 0) {
						scroll = right_max;
					}
					if ((right_max - scroll) / ACCELERATION <= 1) {
						if (mainScreenActivity.isSearch) {
							mainScreenActivity.isSearch = false;
							ScaleAnimation scale = new ScaleAnimation(1.4f, 1.0f, 1.0f, 1.0f,
									Animation.RELATIVE_TO_SELF, (float) 0.5,
									Animation.RELATIVE_TO_SELF, (float) 0.5);
							scale.setFillBefore(true);
							scale.setFillAfter(true);
							scale.setDuration(300);
							scale.setAnimationListener(new AnimationListener() {

								@Override
								public void onAnimationStart(Animation animation) {

								}

								@Override
								public void onAnimationRepeat(Animation animation) {

								}

								@Override
								public void onAnimationEnd(Animation animation) {
									mainScreenActivity.btnGps.setVisibility(View.VISIBLE);
									mainScreenActivity.btnSetting.setVisibility(View.VISIBLE);
								}
							});
							mainScreenActivity.imgBackGroundSearchBar.startAnimation(scale);

						}
					}
				} else {
					isMoveLeft = false;
					isMoveRight = false;
					isStarted = false;

				}
			} else if (isMoveLeft && !isMoveRight) {
				if (scroll > left_max) {
					scroll -= (scroll - left_max) / ACCELERATION;
					if (scroll <= left_max || (scroll - left_max) / ACCELERATION == 0) {
						scroll = left_max;
					}
				} else {
					isMoveRight = false;
					isMoveLeft = false;
					mainScreenActivity.showKeyBoard(mainScreenActivity.txtSearchBar);
				}
			}
		}

	}

	public void moveLeft() {
		isStarted = true;
		isMoveLeft = true;
		isMoveRight = false;
	}

	public void moveRight() {
		isMoveRight = true;
		isMoveLeft = false;

	}

}
