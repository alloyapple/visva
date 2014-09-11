package com.fgsecure.ujoolt.app.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.fgsecure.ujoolt.app.widget.DraggableView;

public class SettingView extends DraggableView {
	public static final byte QUIT = 0;
	public static final byte OPTIONS = 1;
	public static final byte LANGUAGE = 2;

	public int maxDistane;

	public SettingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		orientation = VERTICAL;
	}

	public void clampScroll() {
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
					this.setVisibility(View.INVISIBLE);
					if (typeAction == QUIT) {
						if (mainScreenActivity.isSetting) {
							// mainScreenActivity.layoutSearch
							// .setVisibility(View.VISIBLE);
							mainScreenActivity.viewSearch.appear(SearchView.SETTING);
							if (mainScreenActivity.viewLogin.isLogin) {
								mainScreenActivity.viewLogin.appear();
							} else {
								mainScreenActivity.viewJolt.appear();
							}
							mainScreenActivity.isSetting = false;
							this.setOnTouchListener(null);
						}
					} else if (typeAction == REGISTER) {
						mainScreenActivity.viewLogin.setVisibility(View.VISIBLE);
						mainScreenActivity.isSetting = false;
						// MainScreenActivity.isBackRegister = true;
						mainScreenActivity.viewLogin.isLogined = false;
						mainScreenActivity.viewLogin.isLogin = true;
						mainScreenActivity.viewLogin.isLoginning = true;
						// mainScreenActivity.setToggleShare();
						mainScreenActivity.setIconSocialNetwork();
						mainScreenActivity.isBackRegister = true;
						mainScreenActivity.viewLogin.isStarted = true;
						mainScreenActivity.viewLogin.scroll = mainScreenActivity.viewLogin.disappearPoint;
						mainScreenActivity.isJolting = false;
						mainScreenActivity.viewSetting.setOnTouchListener(null);
						mainScreenActivity.isStartFacebookConnect = false;
						mainScreenActivity.isStartTwitterConnect = false;
						this.setOnTouchListener(null);
						if (mainScreenActivity.isFacebookConnected) {
							mainScreenActivity.isFacebookConnected = false;

							// if(mainScreenActivity.facebookConnector != null){
							// mainScreenActivity.facebookConnector.logout();
							// }
						}
						if (mainScreenActivity.isTwitterConnected) {
							mainScreenActivity.isTwitterConnected = false;
						}
					}
				}
			} else if (isMoveDown && !isMoveUp) {
				if (scroll > bottom_max) {
					scroll -= (scroll - bottom_max) / ACCELERATION;
					if (scroll <= bottom_max || (scroll - bottom_max) / ACCELERATION == 0) {
						scroll = bottom_max;
					}
				} else {

					isMoveDown = false;
					isMoveUp = false;
					this.setOnTouchListener(mainScreenActivity);
				}
			}
		}

	}

	public void disappear(int type) {
		setTypeAction(type);
		isMoveUp = true;
		isMoveDown = false;
	}

	public int getMaxDistane() {
		return maxDistane;
	}

	public void setMaxDistane(int maxDistane) {
		this.maxDistane = maxDistane;
		Log.e("max distance", "" + maxDistane);
	}

}
