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

import com.visva.voicerecorder.MyCallRecorderApplication;
import com.visva.voicerecorder.R;
import com.visva.voicerecorder.constant.MyCallRecorderConstant;
import com.visva.voicerecorder.log.AIOLog;
import com.visva.voicerecorder.record.RecordingSession;
import com.visva.voicerecorder.utils.MyCallRecorderSharePrefs;
import com.visva.voicerecorder.utils.ProgramHelper;
import com.visva.voicerecorder.utils.StringUtility;
import com.visva.voicerecorder.utils.Utils;
import com.visva.voicerecorder.view.activity.ActivityHome;

public class NotificationActivity extends Activity {
    private LinearLayout   mBottomLayout;
    private RelativeLayout mNextBtnLayout;
    private Animation      mContentUpAnime;
    private Animation      mFadeInAnime;
    private Animation      mFadeOutAnime;
    private boolean        isAccept = false;
    private String         phoneNo;
    private String         mCreatedDate;
    private String         mFileName;
    private String         mDurationTime;
    private int            mCallState;
    private int            mThemeColor;

    //    private LinearLayout   mLayoutWelcome;

    public NotificationActivity() {
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_notification);
        
        MyCallRecorderSharePrefs myCallRecorderSharePrefs = MyCallRecorderSharePrefs.getInstance(this);
        int which = myCallRecorderSharePrefs.getIntValue(MyCallRecorderConstant.KEY_THEME);
        mThemeColor = Utils.getThemeColor(this, which);
        mBottomLayout = (LinearLayout) findViewById(R.id.welcome_intro_bottom_layout);
        Log.d("KieuThang", "mThemeColor:" + mThemeColor);
        //        mLayoutWelcome = (LinearLayout) findViewById(R.id.welcome_intro_content);
        // mLayoutWelcome.setBackgroundColor(mThemeColor);
        mBottomLayout.setBackgroundColor(mThemeColor);
        mBottomLayout.setVisibility(View.GONE);
        mNextBtnLayout = (RelativeLayout) findViewById(R.id.intro_next_layout);
        mContentUpAnime = AnimationUtils.loadAnimation(this, R.anim.layout_content_down);
        mFadeInAnime = AnimationUtils.loadAnimation(this, R.anim.welcome_image_fade_in);
        if (mFadeInAnime != null) {
            mFadeInAnime.setAnimationListener(fadeInAniListener);
        }

        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            finish();
            return;
        }
        phoneNo = bundle.getString(MyCallRecorderConstant.EXTRA_PHONE_NO);
        mCreatedDate = bundle.getString(MyCallRecorderConstant.EXTRA_CREATED_DATE);
        mFileName = bundle.getString(MyCallRecorderConstant.EXTRA_FILE_NAME);
        mCallState = bundle.getInt(MyCallRecorderConstant.EXTRA_CALL_STATE);
        mDurationTime = bundle.getString(MyCallRecorderConstant.EXTRA_DURATION);

        if (StringUtility.isEmpty(mCreatedDate)) {
            AIOLog.e(MyCallRecorderConstant.TAG, "created date time is null");
            finish();
        }
        mBottomLayout.setVisibility(View.VISIBLE);
        mBottomLayout.setBackgroundColor(mThemeColor);
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
                                                             helper.removeNewestSession(mFileName);

                                                         } else {
                                                             //save the call to the db
                                                             try {
                                                                 ProgramHelper.writeToList(NotificationActivity.this, mFileName, phoneNo,
                                                                         mCreatedDate, mCallState, mDurationTime);
                                                             } catch (Exception e) {
                                                                 e.printStackTrace();
                                                             }

                                                             Uri phoneUri = Utils.getContactUriTypeFromPhoneNumber(getContentResolver(), phoneNo, 1);
                                                             String phoneName = "";
                                                             if (phoneUri == null || StringUtility.isEmpty(phoneUri.toString()))
                                                                 phoneName = phoneNo;
                                                             else
                                                                 phoneName = phoneUri.toString();
                                                             boolean isShowNotication = MyCallRecorderApplication.getInstance()
                                                                     .getMyCallRecorderSharePref(NotificationActivity.this)
                                                                     .getBooleanValue(MyCallRecorderConstant.KEY_SHOW_NOTIFICATION);
                                                             if (isShowNotication)
                                                                 Utils.showNotificationAfterCalling(NotificationActivity.this, phoneName, phoneNo,
                                                                         mCreatedDate);

                                                             //After call recording, we need to update view if activity is still alive
                                                             requestToRefreshActivityView(mDurationTime);
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

    @SuppressWarnings("deprecation")
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

    @SuppressWarnings("deprecation")
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

    private void requestToRefreshActivityView(String duration) {
        if (MyCallRecorderApplication.getInstance().getActivity() != null) {
            String phoneName = "";
            Uri phoneNameUri = Utils.getContactUriTypeFromPhoneNumber(getContentResolver(), phoneNo, 1);
            if (phoneNameUri == null || StringUtility.isEmpty(phoneNameUri.toString())) {
                phoneName = "";
            } else
                phoneName = phoneNameUri.toString();
            int isFavorite = Utils.isCheckFavouriteContactByPhoneNo(this, phoneNo);
            RecordingSession session = new RecordingSession(phoneNo, MyCallRecorderConstant.STATE_INCOMING, mFileName, phoneName, isFavorite,
                    mCreatedDate, duration);
            Utils.requestRefreshViewToAddNewRecord(MyCallRecorderApplication.getInstance().getActivity(), ActivityHome.FRAGMENT_ALL_RECORDING,
                    session);
        }
    }
}