package com.sharebravo.bravo.control.activity;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sharebravo.bravo.MyApplication;
import com.sharebravo.bravo.R;
import com.sharebravo.bravo.model.response.ObBravo;
import com.sharebravo.bravo.sdk.log.AIOLog;
import com.sharebravo.bravo.view.fragment.home.FragmentBravoTab;
import com.sharebravo.bravo.view.fragment.home.FragmentHomeNotification;
import com.sharebravo.bravo.view.fragment.home.FragmentHomeNotification.IClosePageHomeNotification;
import com.sharebravo.bravo.view.fragment.home.FragmentHomeTab;
import com.sharebravo.bravo.view.fragment.home.FragmentHomeTab.IShowPageHomeNotification;
import com.sharebravo.bravo.view.fragment.home.FragmentCoverImage;
import com.sharebravo.bravo.view.fragment.home.FragmentMapView;
import com.sharebravo.bravo.view.fragment.home.FragmentNetworkTab;
import com.sharebravo.bravo.view.fragment.home.FragmentRecentPostDetail;
import com.sharebravo.bravo.view.fragment.home.FragmentSearchTab;
import com.sharebravo.bravo.view.fragment.home.FragmentSetting;
import com.sharebravo.bravo.view.fragment.home.FragmentSetting.IShowPageTermOfUse;
import com.sharebravo.bravo.view.fragment.home.FragmentTermOfUse;
import com.sharebravo.bravo.view.fragment.home.FragmentUpdateUserInfo;
import com.sharebravo.bravo.view.fragment.home.FragmentUserDataTab;
import com.sharebravo.bravo.view.fragment.home.FragmentUserDataTab.IShowPageSettings;

public class HomeActivity extends VisvaAbstractFragmentActivity implements HomeActionListener, IShowPageHomeNotification, IClosePageHomeNotification,
        IShowPageSettings, IShowPageTermOfUse {

    // ======================Constant Define===============
    private static final String      FRAGMENT_HOME_TAB              = "home_tab";
    private static final String      FRAGMENT_NETWORK_TAB           = "network_tab";
    private static final String      FRAGMENT_BRAVO_TAB             = "bravo_tab";
    private static final String      FRAGMENT_SEARCH_TAB            = "search_tab";
    private static final String      FRAGMENT_USER_DATA_TAB         = "user_data_tab";

    private static final String      FRAGMENT_RECENT_POST_DETAIL    = "post_detail";
    private static final String      FRAGMENT_MAP_VIEW              = "map_view";

    private static final String      FRAGMENT_HOME_NOTIFICATION     = "notification";
    private static final String      FRAGMENT_SETTINGS              = "settings";
    private static final String      FRAGMENT_UPDATE_USER_INFO      = "update_user_info";
    private static final String      FRAGMENT_TERM_OF_USE           = "term_of_use";
    private static final String      FRAGMENT_COVER_IMAGE           = "cover_image";

    public static final int          FRAGMENT_BASE_ID               = 1000;
    public static final int          FRAGMENT_HOME_TAB_ID           = FRAGMENT_BASE_ID + 1;
    public static final int          FRAGMENT_NETWORK_TAB_ID        = FRAGMENT_BASE_ID + 2;
    public static final int          FRAGMENT_BRAVO_TAB_ID          = FRAGMENT_BASE_ID + 3;
    public static final int          FRAGMENT_SEARCH_TAB_ID         = FRAGMENT_BASE_ID + 4;
    public static final int          FRAGMENT_USER_DATA_TAB_ID      = FRAGMENT_BASE_ID + 5;
    public static final int          FRAGMENT_RECENT_POST_DETAIL_ID = FRAGMENT_BASE_ID + 6;
    public static final int          FRAGMENT_MAP_VIEW_ID           = FRAGMENT_BASE_ID + 7;
    public static final int          FRAGMENT_HOME_NOTIFICATION_ID  = FRAGMENT_MAP_VIEW_ID + 1;
    public static final int          FRAGMENT_SETTINGS_ID           = FRAGMENT_HOME_NOTIFICATION_ID + 1;
    public static final int          FRAGMENT_UPDATE_USER_INFO_ID   = FRAGMENT_SETTINGS_ID + 1;
    public static final int          FRAGMENT_TERM_OF_USE_ID        = FRAGMENT_UPDATE_USER_INFO_ID + 1;
    public static final int          FRAGMENT_COVER_IMAGE_ID        = FRAGMENT_TERM_OF_USE_ID + 1;

    // ======================Class Define==================
    private FragmentManager          mFmManager;
    private FragmentTransaction      mTransaction;
    private FragmentHomeTab          mFragmentHomeTab;
    private FragmentNetworkTab       mFragmentNetworkTab;
    private FragmentBravoTab         mFragmentBravoTab;
    private FragmentSearchTab        mFragmentSearchTab;
    private FragmentUserDataTab      mFragmentUserDataTab;
    private FragmentRecentPostDetail mFragmentRecentPostDetail;
    private FragmentMapView          mFragmentMapView;
    private FragmentHomeNotification mFragmentHomeNotification;
    private FragmentSetting          mFragmentSetting;
    private FragmentUpdateUserInfo   mFragmentUpdateUserInfo;
    private FragmentTermOfUse        mFragmentTermOfUse;
    private FragmentCoverImage       mFragmentCoverImage;

    private Button                   btnHome;
    private Button                   btnNetwork;
    private Button                   btnBravo;
    private Button                   btnSearch;
    private Button                   btnMyData;

    private TextView                 txtHome;
    private TextView                 txtNetwork;
    private TextView                 txtSearch;
    private TextView                 txtMyData;

    // ======================Variable Define===============
    private ArrayList<String>        backstack                      = new ArrayList<String>();

    @Override
    public int contentView() {
        return R.layout.activity_home;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onCreate() {
        MyApplication myApp = (MyApplication) getApplication();
        myApp._homeActivity = this;

        if (Build.VERSION.SDK_INT >= 11)
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        initializeUITab();
        initializeFragments();
    }

    protected void onActivityResult(int req, int res, Intent data) {
        super.onActivityResult(req, res, data);

    }

    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.btn_home:
            hideTabButton();
            showFragment(FRAGMENT_HOME_TAB_ID);
            btnHome.setBackgroundResource(R.drawable.tab_home_on);
            txtHome.setTextColor(Color.WHITE);
            break;
        case R.id.btn_network:
            hideTabButton();
            showFragment(FRAGMENT_NETWORK_TAB_ID);
            btnNetwork.setBackgroundResource(R.drawable.tab_feed_on);
            txtNetwork.setTextColor(Color.WHITE);
            break;
        case R.id.btn_bravo:
            hideTabButton();
            showFragment(FRAGMENT_BRAVO_TAB_ID);
            btnBravo.setBackgroundResource(R.drawable.tab_bravo_on);

            break;
        case R.id.btn_search:
            hideTabButton();
            showFragment(FRAGMENT_SEARCH_TAB_ID);
            btnSearch.setBackgroundResource(R.drawable.tab_search_on);
            txtSearch.setTextColor(Color.WHITE);
            break;
        case R.id.btn_mydata:
            hideTabButton();
            showFragment(FRAGMENT_USER_DATA_TAB_ID);
            mFragmentUserDataTab.getUserInfo("");
            btnMyData.setBackgroundResource(R.drawable.tab_mydata_on);
            txtMyData.setTextColor(Color.WHITE);
            break;
        default:
            break;
        }
    }

    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void initializeFragments() {
        mFmManager = getSupportFragmentManager();
        mFragmentHomeTab = (FragmentHomeTab) mFmManager.findFragmentById(R.id.fragment_home_tab);
        mFragmentNetworkTab = (FragmentNetworkTab) mFmManager.findFragmentById(R.id.fragment_network_tab);
        mFragmentBravoTab = (FragmentBravoTab) mFmManager.findFragmentById(R.id.fragment_bravo_tab);
        mFragmentSearchTab = (FragmentSearchTab) mFmManager.findFragmentById(R.id.fragment_search_tab);
        mFragmentUserDataTab = (FragmentUserDataTab) mFmManager.findFragmentById(R.id.fragment_user_data_tab);
        mFragmentRecentPostDetail = (FragmentRecentPostDetail) mFmManager.findFragmentById(R.id.fragment_recent_post_detail);
        mFragmentMapView = (FragmentMapView) mFmManager.findFragmentById(R.id.fragment_map_view);
        mFragmentHomeNotification = (FragmentHomeNotification) mFmManager.findFragmentById(R.id.fragment_home_notification);
        mFragmentSetting = (FragmentSetting) mFmManager.findFragmentById(R.id.fragment_settings);
        mFragmentUpdateUserInfo = (FragmentUpdateUserInfo) mFmManager.findFragmentById(R.id.fragment_update_user_info);
        mFragmentTermOfUse = (FragmentTermOfUse) mFmManager.findFragmentById(R.id.fragment_term_of_use);
        mFragmentCoverImage = (FragmentCoverImage) mFmManager.findFragmentById(R.id.fragment_cover_image);

        mFragmentHomeTab.setListener(this);
        mFragmentHomeNotification.setListener(this);
        mFragmentUserDataTab.setListener(this);
        mFragmentSetting.setListener(this);

        mTransaction = hideFragment();
        showFragment(FRAGMENT_HOME_TAB_ID);
    }

    private void initializeUITab() {
        btnHome = (Button) findViewById(R.id.btn_home);
        btnNetwork = (Button) findViewById(R.id.btn_network);
        btnBravo = (Button) findViewById(R.id.btn_bravo);
        btnSearch = (Button) findViewById(R.id.btn_search);
        btnMyData = (Button) findViewById(R.id.btn_mydata);

        txtHome = (TextView) findViewById(R.id.txtview_home);
        txtNetwork = (TextView) findViewById(R.id.txtview_network);
        txtSearch = (TextView) findViewById(R.id.txtview_search);
        txtMyData = (TextView) findViewById(R.id.txtview_mydata);
    }

    public void hideTabButton() {
        btnHome.setBackgroundResource(R.drawable.tab_home_off);
        btnNetwork.setBackgroundResource(R.drawable.tab_feed_off);
        btnBravo.setBackgroundResource(R.drawable.tab_bravo_off);
        btnSearch.setBackgroundResource(R.drawable.tab_search_off);
        btnMyData.setBackgroundResource(R.drawable.tab_mydata_off);

        txtHome.setTextColor(getResources().getColor(R.color.click_color));
        txtNetwork.setTextColor(getResources().getColor(R.color.click_color));
        txtSearch.setTextColor(getResources().getColor(R.color.click_color));
        txtMyData.setTextColor(getResources().getColor(R.color.click_color));
    }

    private void showFragment(int fragment) {
        mTransaction = hideFragment();
        switch (fragment) {
        case FRAGMENT_HOME_TAB_ID:
            mTransaction.show(mFragmentHomeTab);
            addToSBackStack(FRAGMENT_HOME_TAB);
            break;

        case FRAGMENT_NETWORK_TAB_ID:
            mTransaction.show(mFragmentNetworkTab);
            addToSBackStack(FRAGMENT_NETWORK_TAB);
            break;
        case FRAGMENT_BRAVO_TAB_ID:
            mTransaction.show(mFragmentBravoTab);
            addToSBackStack(FRAGMENT_BRAVO_TAB);
            break;
        case FRAGMENT_SEARCH_TAB_ID:
            mTransaction.show(mFragmentSearchTab);
            addToSBackStack(FRAGMENT_SEARCH_TAB);
            break;
        case FRAGMENT_USER_DATA_TAB_ID:
            mTransaction.show(mFragmentUserDataTab);
            addToSBackStack(FRAGMENT_USER_DATA_TAB);
            break;
        case FRAGMENT_RECENT_POST_DETAIL_ID:
            mTransaction.show(mFragmentRecentPostDetail);
            addToSBackStack(FRAGMENT_RECENT_POST_DETAIL);
            break;
        case FRAGMENT_MAP_VIEW_ID:
            mTransaction.show(mFragmentMapView);
            addToSBackStack(FRAGMENT_MAP_VIEW);
            break;
        case FRAGMENT_HOME_NOTIFICATION_ID:
            mTransaction.show(mFragmentHomeNotification);
            addToSBackStack(FRAGMENT_HOME_NOTIFICATION);
            break;
        case FRAGMENT_SETTINGS_ID:
            mTransaction.show(mFragmentSetting);
            addToSBackStack(FRAGMENT_SETTINGS);
            break;
        case FRAGMENT_UPDATE_USER_INFO_ID:
            mTransaction.show(mFragmentSetting);
            addToSBackStack(FRAGMENT_UPDATE_USER_INFO);
            break;
        case FRAGMENT_TERM_OF_USE_ID:
            mTransaction.show(mFragmentTermOfUse);
            addToSBackStack(FRAGMENT_TERM_OF_USE);
            break;
        case FRAGMENT_COVER_IMAGE_ID:
            mTransaction.show(mFragmentCoverImage);
            addToSBackStack(FRAGMENT_COVER_IMAGE);
            break;
        default:
            break;
        }
        mTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
        mTransaction.commit();
    }

    public FragmentTransaction hideFragment() {
        mTransaction = mFmManager.beginTransaction();
        mTransaction.hide(mFragmentHomeTab);
        mTransaction.hide(mFragmentNetworkTab);
        mTransaction.hide(mFragmentBravoTab);
        mTransaction.hide(mFragmentSearchTab);
        mTransaction.hide(mFragmentUserDataTab);
        mTransaction.hide(mFragmentRecentPostDetail);
        mTransaction.hide(mFragmentMapView);
        mTransaction.hide(mFragmentHomeNotification);
        mTransaction.hide(mFragmentSetting);
        mTransaction.hide(mFragmentUpdateUserInfo);
        mTransaction.hide(mFragmentTermOfUse);
        mTransaction.hide(mFragmentCoverImage);
        return mTransaction;
    }

    public void addToSBackStack(String tag) {

        int index = backstack.lastIndexOf(tag);
        if (index == -1) {
            backstack.add(tag);
            return;
        }
        try {
            if (!backstack.get(index - 1).equals(
                    backstack.get(backstack.size() - 1))) {
                backstack.add(tag);
                return;
            }
        } catch (IndexOutOfBoundsException e) {

        }
        try {
            ArrayList<String> subStack = new ArrayList<String>(backstack);
            for (int i = 0; i < subStack.size(); i++) {
                if (i > index) {
                    backstack.remove(index);
                }
            }
        } catch (IndexOutOfBoundsException e) {
        }
    }

    @Override
    public void goToRecentPostDetail(ObBravo obGetBravo) {
        AIOLog.d("obGetBravo:" + obGetBravo);
        hideTabButton();
        mFragmentRecentPostDetail.setBravoOb(obGetBravo);
        showFragment(FRAGMENT_RECENT_POST_DETAIL_ID);
        // btnHome.setBackgroundResource(R.drawable.tab_home_on);
    }

    @Override
    public void goToBack() {
        AIOLog.d("mBackstack=" + backstack);

        String currentView = backstack.get(backstack.size() - 1);
        try {
            backstack.remove(backstack.size() - 1);
            if (backstack.size() == 0) {
                super.onBackPressed();
                return;
            }
        } catch (IndexOutOfBoundsException e) {
            super.onBackPressed();
            return;
        }
        mTransaction = hideFragment();

        Toast.makeText(this, currentView, Toast.LENGTH_LONG).show();
        if (currentView.equals(FRAGMENT_RECENT_POST_DETAIL)) {
            mTransaction.show(mFragmentHomeTab);
        } else if (currentView.equals(FRAGMENT_USER_DATA_TAB)) {
            mTransaction.show(mFragmentRecentPostDetail);
        } else if (currentView.equals(FRAGMENT_MAP_VIEW)) {
            mTransaction.show(mFragmentRecentPostDetail);
        } else if (currentView.equals(FRAGMENT_COVER_IMAGE)) {
            mTransaction.show(mFragmentRecentPostDetail);
        }
        mTransaction.commitAllowingStateLoss();

    }

    @Override
    public void goToFragment(int fragmentID) {
        hideTabButton();
        showFragment(fragmentID);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
        case KeyEvent.KEYCODE_BACK:
            goToBack();
            break;

        default:
            break;
        }
        // return super.onKeyDown(keyCode, event);
        return false;

    }

    @Override
    public void showPageHomeNotification() {
        goToFragment(FRAGMENT_HOME_NOTIFICATION_ID);
        mFragmentHomeNotification.onRequestListHomeNotification();
    }

    @Override
    public void closePageHomeNotification() {
        goToFragment(FRAGMENT_HOME_TAB_ID);
    }

    @Override
    public void showPageSettings() {
        goToFragment(FRAGMENT_SETTINGS_ID);
    }

    @Override
    public void showPageTermOfUse() {
        showFragment(FRAGMENT_TERM_OF_USE_ID);
    }

    @Override
    public void goToUserData(String userId) {
        showFragment(FRAGMENT_USER_DATA_TAB_ID);
        mFragmentUserDataTab.getUserInfo(userId);
    }

}
