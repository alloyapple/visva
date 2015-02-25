package com.visva.voicerecorder.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.visva.MyCallRecorderApplication;
import com.visva.voicerecorder.R;
import com.visva.voicerecorder.VisvaAbstractFragmentActivity;
import com.visva.voicerecorder.constant.MyCallRecorderConstant;
import com.visva.voicerecorder.log.AIOLog;
import com.visva.voicerecorder.view.fragments.ContactsListFragment.OnContactsInteractionListener;
import com.visva.voicerecorder.view.fragments.FragmentAbout;
import com.visva.voicerecorder.view.fragments.FragmentAllRecord;
import com.visva.voicerecorder.view.fragments.FragmentContact;
import com.visva.voicerecorder.view.fragments.FragmentFavourite;
import com.visva.voicerecorder.view.fragments.FragmentSetting;
import com.visva.voicerecorder.view.widget.floatingactionbutton.FloatingActionsMenu;

public class HomeActivity extends VisvaAbstractFragmentActivity implements IHomeActionListener,OnContactsInteractionListener {
    // ======================Constant Define=====================
    public static final int     FRAGMENT_BASE_ID        = 1000;
    public static final int     FRAGMENT_ALL_RECORDING  = FRAGMENT_BASE_ID + 1;
    public static final int     FRAGMENT_CONTACT        = FRAGMENT_ALL_RECORDING + 1;
    public static final int     FRAGMENT_FAVOURITE      = FRAGMENT_CONTACT + 1;
    public static final int     FRAGMENT_SETTING        = FRAGMENT_FAVOURITE + 1;
    public static final int     FRAGMENT_ABOUT          = FRAGMENT_SETTING + 1;
    // ======================Control Define =====================
    private FragmentManager     mFmManager;
    private FragmentTransaction mTransaction;
    private FragmentAllRecord   mFragmentAllRecord;
    private FragmentContact     mFragmentContact;
    private FragmentFavourite   mFragmentFavourite;
    private FragmentSetting     mFragmentSetting;
    private FragmentAbout       mFragmentAbout;

    private FloatingActionsMenu mFloatingActionsMenu;
    // ======================Variable Define=====================
    //private ArrayList<Integer>  backstackID             = new ArrayList<Integer>();
    private boolean             mBackPressedToExitOnce  = false;

    @Override
    public int contentView() {
        return R.layout.activity_home;
    }

    @Override
    public void onCreate() {
        if (Build.VERSION.SDK_INT >= 11)
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);

        MyCallRecorderApplication.getInstance().setActivity(this);
        initLayout();
    }

    private void initLayout() {
        mFloatingActionsMenu = (FloatingActionsMenu) findViewById(R.id.multiple_actions);
        initializeFragments();
    }

    // True if this activity instance is a search result view (used on pre-HC devices that load
    // search results in a separate instance of the activity rather than loading results in-line
    // as the query is typed.

    public HomeActivity() {
        super();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

    }

    public void updateHomeActivity() {
        this.onRestart();
        Toast.makeText(this, "do not append to the list", Toast.LENGTH_LONG).show();
        Intent refresh = new Intent(this, HomeActivity.class);
        startActivity(refresh);
        this.finish();
    }

    private void initializeFragments() {
        mFmManager = getSupportFragmentManager();
        mFragmentAllRecord = (FragmentAllRecord) mFmManager.findFragmentById(R.id.fragment_all_record);
        mFragmentContact = (FragmentContact) mFmManager.findFragmentById(R.id.fragment_contact);
        mFragmentFavourite = (FragmentFavourite) mFmManager.findFragmentById(R.id.fragment_favourite);
        mFragmentSetting = (FragmentSetting) mFmManager.findFragmentById(R.id.fragment_settings);
        mFragmentAbout = (FragmentAbout) mFmManager.findFragmentById(R.id.fragment_about);

        showFragment(FRAGMENT_ALL_RECORDING, false);
    }

    private void showFragment(int fragment, boolean isback) {
        mTransaction = hideFragment();
        switch (fragment) {
        case FRAGMENT_ABOUT:
            mFragmentAbout.setBackStatus(isback);
            mTransaction.show(mFragmentAbout);
            break;
        case FRAGMENT_ALL_RECORDING:
            mFragmentAllRecord.setBackStatus(isback);
            mTransaction.show(mFragmentAllRecord);
            break;
        case FRAGMENT_CONTACT:
           // mFragmentContact.setBackStatus(isback);
            mTransaction.show(mFragmentContact);
            break;
        case FRAGMENT_FAVOURITE:
            mFragmentFavourite.setBackStatus(isback);
            mTransaction.show(mFragmentFavourite);
            break;
        case FRAGMENT_SETTING:
            mFragmentSetting.setBackStatus(isback);
            mTransaction.show(mFragmentSetting);
            break;
        default:
            break;
        }
        if (!isback)
           // addToSBackStack(fragment);
        // mTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
        mTransaction.commit();
    }

    public FragmentTransaction hideFragment() {
        mTransaction = mFmManager.beginTransaction();
        //        if (fragmentAnimationType == 1)
        //            mTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right);
        //        else if (fragmentAnimationType == 2)
        //            mTransaction.setCustomAnimations(R.anim.slide_out_left, R.anim.slide_in_left);
        mTransaction.hide(mFragmentAbout);
        mTransaction.hide(mFragmentAllRecord);
        mTransaction.hide(mFragmentContact);
        mTransaction.hide(mFragmentFavourite);
        mTransaction.hide(mFragmentSetting);
        return mTransaction;
    }

//    public void addToSBackStack(int ID) {
//        backstackID.add(ID);
//    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
        case KeyEvent.KEYCODE_BACK:
            onBackPressedToExit();
            break;

        default:
            break;
        }
        return false;
    }

    private void onBackPressedToExit() {
        AIOLog.d(MyCallRecorderConstant.TAG, "onBackPressed:" + mBackPressedToExitOnce);
        if (mBackPressedToExitOnce) {
            super.onBackPressed();
        } else {
            this.mBackPressedToExitOnce = true;
            showToast("Press again to exit");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mBackPressedToExitOnce = false;
                }
            }, 2000);
        }
    }

    private void showToast(String string) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
    }

    public void onClickFloatingActionButton(View v) {
        switch (v.getId()) {
        case R.id.action_about:
            showFragment(FRAGMENT_ABOUT, false);
            break;
        case R.id.action_all_record:
            showFragment(FRAGMENT_ALL_RECORDING, false);
            break;
        case R.id.action_contact:
            showFragment(FRAGMENT_CONTACT, false);
            break;
        case R.id.action_favourite:
            showFragment(FRAGMENT_FAVOURITE, false);
            break;
        case R.id.action_setting:
            showFragment(FRAGMENT_SETTING, false);
            break;
        default:
            break;
        }
        if (mFloatingActionsMenu == null)
            return;
        if (mFloatingActionsMenu.isExpanded())
            mFloatingActionsMenu.collapse();
    }

    @Override
    public void onContactSelected(Uri contactUri) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onSelectionCleared() {
        // TODO Auto-generated method stub
        
    }
}
