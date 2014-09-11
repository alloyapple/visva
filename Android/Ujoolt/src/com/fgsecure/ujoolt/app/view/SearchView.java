package com.fgsecure.ujoolt.app.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.fgsecure.ujoolt.app.widget.DraggableView;

public class SearchView extends DraggableView {
	public static int SETTING = 0;
	public static int REGISTER = 1;
	public static int JOLT = 2;
	public static int INFO = 3;

	public boolean isAvailable;
	public boolean isDisappear, isAppear;

	public SearchView(Context context, AttributeSet attrs) {
		super(context, attrs);
		isAvailable = true;
		orientation = VERTICAL;
	}

	public void clampScroll() {
		if (isDisappear && !isAppear) {
			if (scroll < top_max) {
				scroll += 40;
				if (scroll >= top_max) {
					scroll = top_max;
				}

			} else {
				isDisappear = false;
				isAppear = false;
				isAvailable = false;
				if (typeAction == SETTING) {
					mainScreenActivity.viewSetting.setVisibility(View.VISIBLE);
					mainScreenActivity.viewSetting.isStarted = true;
					mainScreenActivity.viewSetting.isMoveDown = true;
					mainScreenActivity.viewSetting.isMoveUp = false;
					mainScreenActivity.isSetting = true;
					mainScreenActivity.isBubbleDetail = false;
				} else if (typeAction == INFO) {
					// mainScreenActivity.view_information.setVisibility(View.VISIBLE);
					// mainScreenActivity.view_information.appear();
				}
			}
		} else if (isAppear && !isDisappear) {
			if (scroll > bottom_max) {
				scroll -= 40;
				if (scroll <= bottom_max) {
					scroll = bottom_max;
				}

			} else {
				isAvailable = true;
				isDisappear = false;
				isAppear = false;
				isStarted = false;
			}
		}
	}

	public void disappear(int type) {
		setTypeAction(type);
		isStarted = true;
		isDisappear = true;
		isAppear = false;
		mainScreenActivity.hideAllFilter();
		mainScreenActivity.layoutNumberJolt.setVisibility(View.GONE);
	}

	public void appear(int type) {
		// this.setVisibility(View.VISIBLE);
		setTypeAction(type);
		isAppear = true;
		isDisappear = false;
		mainScreenActivity.inactiveFilter();
		mainScreenActivity.viewInformation.setVisibility(View.GONE);
		mainScreenActivity.layoutNumberJolt.setVisibility(View.VISIBLE);
	}

}
