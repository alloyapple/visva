package com.visva.voicerecorder.receiver.notification;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.visva.voicerecorder.R;
import com.visva.voicerecorder.utils.ProgramHelper;
import com.visva.voicerecorder.utils.StringUtility;
import com.visva.voicerecorder.utils.Utils;

public class NotificationActivity extends Activity {
    private LinearLayout   mBottomLayout;
    private RelativeLayout mNextBtnLayout;
    private Animation      mContentUpAnime;
    private Animation      mFadeInAnime;
    private Animation      mFadeOutAnime;
    private boolean        isAccept = false;
    private String         phoneNo;

    public NotificationActivity() {
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_notification);

        mBottomLayout = (LinearLayout) findViewById(R.id.welcome_intro_bottom_layout);
        mBottomLayout.setVisibility(View.GONE);
        mNextBtnLayout = (RelativeLayout) findViewById(R.id.intro_next_layout);
        mContentUpAnime = AnimationUtils.loadAnimation(this, R.anim.layout_content_down);
        mFadeInAnime = AnimationUtils.loadAnimation(this, R.anim.welcome_image_fade_in);
        if (mFadeInAnime != null) {
            mFadeInAnime.setAnimationListener(fadeInAniListener);
        }

        phoneNo = getIntent().getExtras().getString("phone_number");
        mBottomLayout.setVisibility(View.VISIBLE);
        mBottomLayout.setBackgroundColor(getResources().getColor(R.color.material_design_color_orange_1));
        mBottomLayout.startAnimation(mContentUpAnime);
    }

    private AnimationListener fadeInAniListener  = new AnimationListener() {

                                                     @Override
                                                     public void onAnimationStart(Animation animation) {
                                                     }

                                                     @Override
                                                     public void onAnimationRepeat(Animation animation) {
                                                     }

                                                     @Override
                                                     public void onAnimationEnd(Animation animation) {
                                                         mNextBtnLayout.requestFocus();
                                                     }
                                                 };

    private AnimationListener fadeOutAniListener = new AnimationListener() {

                                                     @Override
                                                     public void onAnimationStart(Animation animation) {
                                                     }

                                                     @Override
                                                     public void onAnimationRepeat(Animation animation) {
                                                     }

                                                     @Override
                                                     public void onAnimationEnd(Animation animation) {
                                                         Handler handler = new Handler();
                                                         handler.postDelayed(run, 1000);
                                                         Log.d("KieuThang", "mFadeOutAnime");
                                                         mBottomLayout.setVisibility(View.GONE);
                                                         if (!isAccept) {
                                                             ProgramHelper helper = new ProgramHelper();
                                                             helper.removeNewestSession();

                                                         } else {
                                                             Uri phoneUri = Utils.getContactUriTypeFromPhoneNumber(getContentResolver(), phoneNo, 1);
                                                             String phoneName = "";
                                                             if (phoneUri == null || StringUtility.isEmpty(phoneUri.toString()))
                                                                 phoneName = phoneNo;
                                                             else
                                                                 phoneName = phoneUri.toString();
                                                             Utils.showNotificationAfterCalling(NotificationActivity.this, phoneName, phoneNo);
                                                         }
                                                         finish();
                                                     }
                                                 };

    Runnable                  run                = new Runnable() {
                                                     @Override
                                                     public void run() {
                                                         isAccept = false;
                                                         mNextBtnLayout.setSoundEffectsEnabled(true);
                                                     }
                                                 };

    public void onClickButtonAccept(View v) {
        isAccept = true;

        mNextBtnLayout.setSoundEffectsEnabled(false);
        mFadeOutAnime = AnimationUtils.loadAnimation(this, R.anim.welcome_intro_fade_out);
        if (mFadeOutAnime != null) {
            mBottomLayout.startAnimation(mFadeOutAnime);
        }
        mBottomLayout.setBackgroundDrawable(null);
        if (mFadeOutAnime != null) {
            mFadeOutAnime.setAnimationListener(fadeOutAniListener);
        }
    }

    public void onClickButtonCancel(View v) {
        isAccept = false;

        mNextBtnLayout.setSoundEffectsEnabled(false);
        mFadeOutAnime = AnimationUtils.loadAnimation(this, R.anim.welcome_intro_fade_out);
        if (mFadeOutAnime != null) {
            mBottomLayout.startAnimation(mFadeOutAnime);
        }
        mBottomLayout.setBackgroundDrawable(null);
        if (mFadeOutAnime != null) {
            mFadeOutAnime.setAnimationListener(fadeOutAniListener);
        }
    }
}