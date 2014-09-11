package com.fgsecure.ujoolt.app.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.fgsecure.ujoolt.app.widget.DraggableView;

public class JoltView extends DraggableView {
	public boolean isDisapear;
	public boolean isAppear;
	public int hiddenPoint;

	public JoltView(Context context, AttributeSet attrs) {
		super(context, attrs);
		orientation = VERTICAL;
	}

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
						if (isMoveUp) {
							isMoveUp = false;
							isMoveDown = false;
							mainScreenActivity.showKeyBoard(mainScreenActivity.txtText);
							mainScreenActivity.clearUjooltFile();
						}
					}
				} else if (isMoveDown && !isMoveUp) {
					mainScreenActivity.hideKeyBoard(mainScreenActivity.txtText);
					if (scroll > bottom_max) {
						scroll -= (scroll - bottom_max) / ACCELERATION;
						if (scroll <= bottom_max || (scroll - bottom_max) / ACCELERATION == 0) {
							scroll = bottom_max;
						}
					} else {

						isMoveDown = false;
						isMoveUp = false;
						mainScreenActivity.viewSearch.appear(SearchView.JOLT);
						mainScreenActivity.isJolting = false;
						mainScreenActivity.isBubbleDetail = false;
						mainScreenActivity.imgToungeJolt.setOnTouchListener(mainScreenActivity);
						this.setOnTouchListener(null);
					}
				}
			}
			if (mainScreenActivity.isStartJolt) {
				if (scroll > top_max) {
					scroll -= (scroll - top_max) / ACCELERATION;
					if (scroll <= top_max || (scroll - top_max) / ACCELERATION == 0) {
						scroll = top_max;
					}
				} else {

					mainScreenActivity.showKeyBoard(mainScreenActivity.txtText);
					mainScreenActivity.isStartJolt = false;
					mainScreenActivity.isLoading = false;
					mainScreenActivity.isJolting = true;
					this.isStarted = true;
					this.setOnTouchListener(mainScreenActivity);

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
		if (!isAppear && !isDisapear) {
			this.setVisibility(View.VISIBLE);
			isAppear = true;
			isDisapear = false;
		}

	}

	public void reset() {
		isAppear = false;
		isDisapear = false;
		isStarted = false;
		isStartJolt = false;
		touching = false;
	}

}
