package com.visva.voicerecorder.view.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.ContactsContract.Contacts;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.gc.materialdesign.views.LayoutRipple;
import com.visva.voicerecorder.MyCallRecorderApplication;
import com.visva.voicerecorder.R;
import com.visva.voicerecorder.constant.MyCallRecorderConstant;
import com.visva.voicerecorder.log.AIOLog;
import com.visva.voicerecorder.record.RecordingSession;
import com.visva.voicerecorder.utils.StringUtility;
import com.visva.voicerecorder.view.VisvaAbstractFragmentActivity;
import com.visva.voicerecorder.view.common.IHomeActionListener;
import com.visva.voicerecorder.view.favourite.FragmentFavourite;
import com.visva.voicerecorder.view.fragments.FragmentContact;
import com.visva.voicerecorder.view.fragments.FragmentSetting;
import com.visva.voicerecorder.view.recording.FragmentAllRecord;

public class ActivityHome extends VisvaAbstractFragmentActivity implements IHomeActionListener {
    // ======================Constant Define=====================
    public static final int     FRAGMENT_BASE_ID       = 1000;
    public static final int     FRAGMENT_ALL_RECORDING = FRAGMENT_BASE_ID + 1;
    public static final int     FRAGMENT_CONTACT       = FRAGMENT_ALL_RECORDING + 1;
    public static final int     FRAGMENT_FAVOURITE     = FRAGMENT_CONTACT + 1;
    public static final int     FRAGMENT_SETTING       = FRAGMENT_FAVOURITE + 1;
    public static final int     FRAGMENT_ABOUT         = FRAGMENT_SETTING + 1;
    // ======================Control Define =====================
    private FragmentManager     mFmManager;
    private FragmentTransaction mTransaction;
    private FragmentAllRecord   mFragmentAllRecord;
    private FragmentContact     mFragmentContact;
    private FragmentFavourite   mFragmentFavourite;
    private FragmentSetting     mFragmentSetting;

    private RelativeLayout      mLayoutSearch;
    private EditText            mEditTextSearch;
    private ImageButton         mBtnClearSearch;
    private LayoutRipple        mLayoutContact;
    private LayoutRipple        mLayoutRecord;
    private LayoutRipple        mLayoutFavourite;
    private LayoutRipple        mLayoutSetting;

    /*theme*/
    private LinearLayout        mLayoutHeader;
    private LayoutRipple        mLayoutBtnContact;
    private LayoutRipple        mLayoutBtnRecord;
    private LayoutRipple        mLayoutBtnFavorite;
    private LayoutRipple        mLayoutBtnSetting;
    private LayoutRipple        mLayoutBtnAddContact;
    // ======================Variable Define=====================
    private boolean             mBackPressedToExitOnce = false;
    private int                 mFragmentShowType      = FRAGMENT_ALL_RECORDING;
    private int                 mThemeColor;
    private int                 mPressedThemeColor;

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

        mThemeColor = MyCallRecorderApplication.getInstance().getApplicationTheme();
        mPressedThemeColor = MyCallRecorderApplication.getInstance().getApplicationPressedTheme();
        updateTheme(mThemeColor, mPressedThemeColor);
    }

    private void initLayout() {
        //theme
        mLayoutHeader = (LinearLayout) findViewById(R.id.layout_header);
        mLayoutBtnContact = (LayoutRipple) findViewById(R.id.btn_contact);
        mLayoutBtnFavorite = (LayoutRipple) findViewById(R.id.btn_favourite);
        mLayoutBtnRecord = (LayoutRipple) findViewById(R.id.btn_all_record);
        mLayoutBtnSetting = (LayoutRipple) findViewById(R.id.btn_settings);

        mLayoutSearch = (RelativeLayout) findViewById(R.id.layout_search);
        mBtnClearSearch = (ImageButton) findViewById(R.id.btn_clear_search);
        mEditTextSearch = (EditText) findViewById(R.id.edit_text_search);
        mLayoutContact = (LayoutRipple) findViewById(R.id.btn_contact);
        mLayoutFavourite = (LayoutRipple) findViewById(R.id.btn_favourite);
        mLayoutRecord = (LayoutRipple) findViewById(R.id.btn_all_record);
        mLayoutSetting = (LayoutRipple) findViewById(R.id.btn_settings);
        mLayoutBtnAddContact = (LayoutRipple) findViewById(R.id.layout_add_contact);
        mBtnClearSearch.setVisibility(View.GONE);

        mEditTextSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
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
                if (s != null && !"".equals(s.toString()))
                    mBtnClearSearch.setVisibility(View.VISIBLE);
                else
                    mBtnClearSearch.setVisibility(View.GONE);
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
        resetSearchLayout();
        mTransaction = hideFragment();
        mFragmentShowType = fragment;
        mLayoutSearch.setVisibility(View.VISIBLE);
        switch (fragment) {
        case FRAGMENT_ABOUT:
            mLayoutSearch.setVisibility(View.VISIBLE);
            break;
        case FRAGMENT_ALL_RECORDING:
            Log.d("KieuThang", "onClickRecordTab");
            mLayoutSetting.setBackgroundColor(mThemeColor);
            mLayoutFavourite.setBackgroundColor(mThemeColor);
            mLayoutRecord.setBackgroundColor(mPressedThemeColor);
            mLayoutContact.setBackgroundColor(mThemeColor);

            mFragmentAllRecord.setBackStatus(isback);
            mTransaction.show(mFragmentAllRecord);
            mLayoutSearch.setVisibility(View.VISIBLE);
            break;
        case FRAGMENT_CONTACT:
            Log.d("KieuThang", "onClickContactTab");
            mLayoutSetting.setBackgroundColor(mThemeColor);
            mLayoutFavourite.setBackgroundColor(mThemeColor);
            mLayoutRecord.setBackgroundColor(mThemeColor);
            mLayoutContact.setBackgroundColor(mPressedThemeColor);

            mTransaction.show(mFragmentContact);
            mLayoutSearch.setVisibility(View.VISIBLE);
            break;
        case FRAGMENT_FAVOURITE:
            Log.d("KieuThang", "onClickFavouriteTab");
            mLayoutSetting.setBackgroundColor(mThemeColor);
            mLayoutFavourite.setBackgroundColor(mPressedThemeColor);
            mLayoutRecord.setBackgroundColor(mThemeColor);
            mLayoutContact.setBackgroundColor(mThemeColor);

            mFragmentFavourite.setBackStatus(isback);
            mTransaction.show(mFragmentFavourite);
            mLayoutSearch.setVisibility(View.GONE);
            break;
        case FRAGMENT_SETTING:
            Log.d("KieuThang", "onClickSettingTab");
            mLayoutSetting.setBackgroundColor(mPressedThemeColor);
            mLayoutFavourite.setBackgroundColor(mThemeColor);
            mLayoutRecord.setBackgroundColor(mThemeColor);
            mLayoutContact.setBackgroundColor(mThemeColor);

            mLayoutSearch.setVisibility(View.GONE);
            mFragmentSetting.setBackStatus(isback);
            mTransaction.show(mFragmentSetting);
            mLayoutSearch.setVisibility(View.GONE);
            break;
        default:
            break;
        }
        if (!isback)
            mTransaction.commit();
    }

    private void resetSearchLayout() {
        if (StringUtility.isEmpty(mEditTextSearch)) {
            return;
        } else {
            mEditTextSearch.setText("");
            mBtnClearSearch.setVisibility(View.GONE);
        }
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

    @Override
    public void onClickItemListener(View view, int position, int fragment, int listViewType) {
        switch (fragment) {
        case FRAGMENT_FAVOURITE:
            mFragmentFavourite.onClickItemListener(view, position, listViewType);
            break;

        default:
            break;
        }
    }

    @Override
    public void onLongClickItemListener(View view, int position, int fragment, int listViewType) {
        switch (fragment) {
        case FRAGMENT_FAVOURITE:
            mFragmentFavourite.onLongClickItemListener(view, position, listViewType);
            break;

        default:
            break;
        }
    }

    // hold onClick tabs action
    public void onClickSettingTab(View v) {
        showFragment(FRAGMENT_SETTING, false);
    }

    public void onClickFavouriteTab(View v) {
        showFragment(FRAGMENT_FAVOURITE, false);
    }

    public void onClickRecordTab(View v) {
        showFragment(FRAGMENT_ALL_RECORDING, false);
    }

    public void onClickContactTab(View v) {
        showFragment(FRAGMENT_CONTACT, false);
    }

    public void requestToRefreshView(int fragmentAllRecording) {
        switch (fragmentAllRecording) {
        case FRAGMENT_ALL_RECORDING:
            if (mFragmentAllRecord == null)
                return;
            mFragmentAllRecord.refreshUI();
            break;
        case FRAGMENT_FAVOURITE:
            if (mFragmentFavourite == null)
                return;
            mFragmentFavourite.refreshUI();
            break;
        case FRAGMENT_CONTACT:
            if (mFragmentContact == null)
                return;
            mFragmentContact.refreshUI();
            break;
        default:
            break;
        }
    }

    public void addNewRecord(RecordingSession recordingSession) {
        mFragmentAllRecord.addNewRecord(recordingSession);
    }

    public void removeARecord(RecordingSession recordingSession) {
        mFragmentAllRecord.removeARecord(recordingSession);
    }

    public void updateRecordList(int fragmentAllRecording) {
        mFragmentAllRecord.updateRecordList();
    }

    public void updateTheme(int themeColor, int pressThemeColor) {
        updateThemeForMainActivity(themeColor, pressThemeColor);

        //update theme for fragments
        mFragmentAllRecord.updateTheme(themeColor, pressThemeColor);
        mFragmentContact.updateTheme(themeColor, pressThemeColor);
        mFragmentFavourite.updateTheme(themeColor, pressThemeColor);
        mFragmentSetting.updateTheme(themeColor, pressThemeColor);
    }

    private void updateThemeForMainActivity(int themeColor, int pressThemeColor) {
        mThemeColor = themeColor;
        mPressedThemeColor = pressThemeColor;
        mLayoutHeader.setBackgroundColor(themeColor);
        mLayoutBtnContact.setBackground(new ColorDrawable(themeColor));
        mLayoutBtnFavorite.setBackground(new ColorDrawable(themeColor));
        mLayoutBtnRecord.setBackground(new ColorDrawable(themeColor));
        mLayoutBtnSetting.setBackground(new ColorDrawable(themeColor));
        mLayoutSearch.setBackground(new ColorDrawable(themeColor));
        mLayoutBtnAddContact.setBackground(new ColorDrawable(themeColor));

        mLayoutBtnContact.setRippleColor(pressThemeColor);
        mLayoutBtnFavorite.setRippleColor(pressThemeColor);
        mLayoutBtnRecord.setRippleColor(pressThemeColor);
        mLayoutBtnSetting.setRippleColor(pressThemeColor);
        mLayoutBtnAddContact.setRippleColor(pressThemeColor);

        switch (mFragmentShowType) {
        case FRAGMENT_ALL_RECORDING:
            mLayoutBtnRecord.setBackground(new ColorDrawable(pressThemeColor));
            break;
        case FRAGMENT_CONTACT:
            mLayoutBtnContact.setBackground(new ColorDrawable(pressThemeColor));
            break;
        case FRAGMENT_FAVOURITE:
            mLayoutBtnFavorite.setBackground(new ColorDrawable(pressThemeColor));
            break;
        case FRAGMENT_SETTING:
            mLayoutBtnSetting.setBackground(new ColorDrawable(pressThemeColor));
            break;
        default:
            break;
        }
    }
}
