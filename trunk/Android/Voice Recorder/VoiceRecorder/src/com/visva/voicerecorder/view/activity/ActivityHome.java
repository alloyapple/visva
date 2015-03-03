package com.visva.voicerecorder.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.ContactsContract.Contacts;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.visva.MyCallRecorderApplication;
import com.visva.voicerecorder.R;
import com.visva.voicerecorder.VisvaAbstractFragmentActivity;
import com.visva.voicerecorder.constant.MyCallRecorderConstant;
import com.visva.voicerecorder.log.AIOLog;
import com.visva.voicerecorder.view.fragments.ContactsListFragment.OnContactsInteractionListener;
import com.visva.voicerecorder.view.fragments.FragmentAllRecord;
import com.visva.voicerecorder.view.fragments.FragmentContact;
import com.visva.voicerecorder.view.fragments.FragmentFavourite;
import com.visva.voicerecorder.view.fragments.FragmentSetting;
import com.visva.voicerecorder.view.widget.floatingactionbutton.FloatingActionsMenu;

public class ActivityHome extends VisvaAbstractFragmentActivity implements IHomeActionListener, OnContactsInteractionListener {
    // ======================Constant Define=====================
    public static final int       FRAGMENT_BASE_ID        = 1000;
    public static final int       FRAGMENT_ALL_RECORDING  = FRAGMENT_BASE_ID + 1;
    public static final int       FRAGMENT_CONTACT        = FRAGMENT_ALL_RECORDING + 1;
    public static final int       FRAGMENT_FAVOURITE      = FRAGMENT_CONTACT + 1;
    public static final int       FRAGMENT_SETTING        = FRAGMENT_FAVOURITE + 1;
    public static final int       FRAGMENT_ABOUT          = FRAGMENT_SETTING + 1;
    // ======================Control Define =====================
    private FragmentManager       mFmManager;
    private FragmentTransaction   mTransaction;
    private FragmentAllRecord     mFragmentAllRecord;
    private FragmentContact       mFragmentContact;
    private FragmentFavourite     mFragmentFavourite;
    private FragmentSetting       mFragmentSetting;

    private FloatingActionsMenu   mFloatingActionsMenu;
    private RelativeLayout        mLayoutSearch;
    private EditText              mEditTextSearch;
    private ImageButton           mBtnClearSearch;
    // ======================Variable Define=====================
    private boolean               mBackPressedToExitOnce  = false;
    private int                   mFragmentShowType       = FRAGMENT_ALL_RECORDING;

    @Override
    public int contentView() {
        return R.layout.activity_home;
    }

    @Override
    public void onCreate() {
        if (Build.VERSION.SDK_INT >= 11)
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        //Remove title bar
        MyCallRecorderApplication.getInstance().setActivity(this);
        initLayout();
    }

    private void initLayout() {
        mFloatingActionsMenu = (FloatingActionsMenu) findViewById(R.id.multiple_actions);
        mLayoutSearch = (RelativeLayout) findViewById(R.id.layout_search);
        mBtnClearSearch = (ImageButton) findViewById(R.id.btn_clear_search);
        mEditTextSearch = (EditText) findViewById(R.id.edit_text_search);
        mBtnClearSearch.setVisibility(View.GONE);

        mEditTextSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                switch (mFragmentShowType) {
                case FRAGMENT_ALL_RECORDING:
                    mFragmentAllRecord.onTextSearchChanged(s);
                    break;
                case FRAGMENT_CONTACT:
                    mFragmentContact.onQueryTextChange(s);
                    break;
                case FRAGMENT_FAVOURITE:
                    break;
                default:
                    break;
                }

                mBtnClearSearch.setVisibility(View.VISIBLE);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        initializeFragments();
    }

    // True if this activity instance is a search result view (used on pre-HC devices that load
    // search results in a separate instance of the activity rather than loading results in-line
    // as the query is typed.

    public ActivityHome() {
        super();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

    }

    public void updateHomeActivity() {
        this.onRestart();
        Toast.makeText(this, "do not append to the list", Toast.LENGTH_LONG).show();
        Intent refresh = new Intent(this, ActivityHome.class);
        startActivity(refresh);
        this.finish();
    }

    private void initializeFragments() {
        mFmManager = getSupportFragmentManager();
        mFragmentAllRecord = (FragmentAllRecord) mFmManager.findFragmentById(R.id.fragment_all_record);
        mFragmentContact = (FragmentContact) mFmManager.findFragmentById(R.id.fragment_contact);
        mFragmentFavourite = (FragmentFavourite) mFmManager.findFragmentById(R.id.fragment_favourite);
        mFragmentSetting = (FragmentSetting) mFmManager.findFragmentById(R.id.fragment_settings);

        showFragment(FRAGMENT_ALL_RECORDING, false);
        mFragmentShowType = FRAGMENT_ALL_RECORDING;
    }

    private void showFragment(int fragment, boolean isback) {
        mTransaction = hideFragment();
        mFragmentShowType = fragment;
        mLayoutSearch.setVisibility(View.VISIBLE);
        switch (fragment) {
        case FRAGMENT_ABOUT:
            mFloatingActionsMenu.setVisibility(View.GONE);
            break;
        case FRAGMENT_ALL_RECORDING:
            mFragmentAllRecord.setBackStatus(isback);
            mTransaction.show(mFragmentAllRecord);
            mFloatingActionsMenu.setVisibility(View.VISIBLE);
            break;
        case FRAGMENT_CONTACT:
            // mFragmentContact.setBackStatus(isback);
            mTransaction.show(mFragmentContact);
            mFloatingActionsMenu.setVisibility(View.VISIBLE);
            break;
        case FRAGMENT_FAVOURITE:
            mFragmentFavourite.setBackStatus(isback);
            mTransaction.show(mFragmentFavourite);
            mFloatingActionsMenu.setVisibility(View.VISIBLE);
            break;
        case FRAGMENT_SETTING:
            mLayoutSearch.setVisibility(View.GONE);
            mFragmentSetting.setBackStatus(isback);
            mTransaction.show(mFragmentSetting);
            mFloatingActionsMenu.setVisibility(View.GONE);
            break;
        default:
            mFloatingActionsMenu.setVisibility(View.VISIBLE);
            break;
        }
        if (!isback)
            mTransaction.commit();
    }

    public FragmentTransaction hideFragment() {
        mTransaction = mFmManager.beginTransaction();
        mTransaction.hide(mFragmentAllRecord);
        mTransaction.hide(mFragmentContact);
        mTransaction.hide(mFragmentFavourite);
        mTransaction.hide(mFragmentSetting);
        return mTransaction;
    }

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

    }

    @Override
    public void onSelectionCleared() {

    }

    public void onClickSearchButton(View v) {

    }

    public void onClickClearSearchButton(View v) {
        mBtnClearSearch.setVisibility(View.GONE);
        mEditTextSearch.setText("");
    }

    public void onClickAddContactButton(View v) {
        final Intent intent = new Intent(Intent.ACTION_INSERT, Contacts.CONTENT_URI);
        startActivity(intent);
    }
}
