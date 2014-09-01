package com.sharebravo.bravo.control.activity;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.sharebravo.bravo.MyApplication;
import com.sharebravo.bravo.R;
import com.sharebravo.bravo.model.response.ObGetBravo;
import com.sharebravo.bravo.sdk.log.AIOLog;
import com.sharebravo.bravo.view.fragment.FragmentBravoTab;
import com.sharebravo.bravo.view.fragment.FragmentHomeTab;
import com.sharebravo.bravo.view.fragment.FragmentMapView;
import com.sharebravo.bravo.view.fragment.FragmentMyDataTab;
import com.sharebravo.bravo.view.fragment.FragmentNetworkTab;
import com.sharebravo.bravo.view.fragment.FragmentRecentPostDetail;
import com.sharebravo.bravo.view.fragment.FragmentSearchTab;

public class HomeActivity extends VisvaAbstractFragmentActivity implements HomeActionListener {

    // ======================Constant Define===============
    private static final String      FRAGMENT_HOME_TAB              = "home_tab";
    private static final String      FRAGMENT_NETWORK_TAB           = "network_tab";
    private static final String      FRAGMENT_BRAVO_TAB             = "bravo_tab";
    private static final String      FRAGMENT_SEARCH_TAB            = "search_tab";
    private static final String      FRAGMENT_MYDATA_TAB            = "mydata_tab";

    private static final String      FRAGMENT_RECENT_POST_DETAIL    = "post_detail";
    private static final String      FRAGMENT_MAP_VIEW              = "map_view";

    public static final int          FRAGMENT_BASE_ID               = 1000;
    public static final int          FRAGMENT_HOME_TAB_ID           = FRAGMENT_BASE_ID + 1;
    public static final int          FRAGMENT_NETWORK_TAB_ID        = FRAGMENT_BASE_ID + 2;
    public static final int          FRAGMENT_BRAVO_TAB_ID          = FRAGMENT_BASE_ID + 3;
    public static final int          FRAGMENT_SEARCH_TAB_ID         = FRAGMENT_BASE_ID + 4;
    public static final int          FRAGMENT_MYDATA_TAB_ID         = FRAGMENT_BASE_ID + 5;
    public static final int          FRAGMENT_RECENT_POST_DETAIL_ID = FRAGMENT_BASE_ID + 6;
    public static final int          FRAGMENT_MAP_VIEW_ID           = FRAGMENT_BASE_ID + 7;

    // ======================Class Define==================
    private FragmentManager          mFmManager;
    private FragmentTransaction      mTransaction;
    private FragmentHomeTab          mFragmentHomeTab;
    private FragmentNetworkTab       mFragmentNetworkTab;
    private FragmentBravoTab         mFragmentBravoTab;
    private FragmentSearchTab        mFragmentSearchTab;
    private FragmentMyDataTab        mFragmentMyDataTab;
    private FragmentRecentPostDetail mFragmentRecentPostDetail;
    private FragmentMapView          mFragmentMapView;

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
            showFragment(FRAGMENT_MYDATA_TAB_ID);
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
        mFragmentMyDataTab = (FragmentMyDataTab) mFmManager.findFragmentById(R.id.fragment_mydata_tab);
        mFragmentRecentPostDetail = (FragmentRecentPostDetail) mFmManager.findFragmentById(R.id.fragment_recent_post_detail);
        mFragmentMapView = (FragmentMapView) mFmManager.findFragmentById(R.id.fragment_map_view);

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
        switch (fragment) {

        case FRAGMENT_HOME_TAB_ID:
            mTransaction = hideFragment();
            mTransaction.show(mFragmentHomeTab);
            addToSBackStack(FRAGMENT_HOME_TAB);
            mTransaction.commit();
            break;

        case FRAGMENT_NETWORK_TAB_ID:
            mTransaction = hideFragment();
            mTransaction.show(mFragmentNetworkTab);
            addToSBackStack(FRAGMENT_NETWORK_TAB);
            mTransaction.commit();
            break;
        case FRAGMENT_BRAVO_TAB_ID:
            mTransaction = hideFragment();
            mTransaction.show(mFragmentBravoTab);
            addToSBackStack(FRAGMENT_BRAVO_TAB);
            mTransaction.commit();
            break;
        case FRAGMENT_SEARCH_TAB_ID:
            mTransaction = hideFragment();
            mTransaction.show(mFragmentSearchTab);
            addToSBackStack(FRAGMENT_SEARCH_TAB);
            mTransaction.commit();
            break;
        case FRAGMENT_MYDATA_TAB_ID:
            mTransaction = hideFragment();
            mTransaction.show(mFragmentMyDataTab);
            addToSBackStack(FRAGMENT_MYDATA_TAB);
            mTransaction.commit();
            break;
        case FRAGMENT_RECENT_POST_DETAIL_ID:
            mTransaction = hideFragment();
            mTransaction.show(mFragmentRecentPostDetail);
            addToSBackStack(FRAGMENT_RECENT_POST_DETAIL);
            mTransaction.commit();
            break;
        case FRAGMENT_MAP_VIEW_ID:
            mTransaction = hideFragment();
            mTransaction.show(mFragmentMapView);
            addToSBackStack(FRAGMENT_MAP_VIEW);
            mTransaction.commit();
            break;
        default:
            break;
        }
    }

    public FragmentTransaction hideFragment() {
        mTransaction = mFmManager.beginTransaction();
        mTransaction.hide(mFragmentHomeTab);
        mTransaction.hide(mFragmentNetworkTab);
        mTransaction.hide(mFragmentBravoTab);
        mTransaction.hide(mFragmentSearchTab);
        mTransaction.hide(mFragmentMyDataTab);
        mTransaction.hide(mFragmentRecentPostDetail);
        mTransaction.hide(mFragmentMapView);
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
    public void goToRecentPostDetail(ObGetBravo obGetBravo) {
        AIOLog.d("obGetBravo:" + obGetBravo);
        hideTabButton();
        mFragmentRecentPostDetail.setBravoOb(obGetBravo);
        showFragment(FRAGMENT_RECENT_POST_DETAIL_ID);
        // btnHome.setBackgroundResource(R.drawable.tab_home_on);
    }

    @Override
    public void goToBack() {
        // TODO Auto-generated method stub
        AIOLog.d("mBackstack=" + backstack);
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
        String currentView = backstack.get(backstack.size() - 1);
        mTransaction = hideFragment();
        // if (currentView.equals(FRAGMENT_RECENT_POST_DETAIL)) {
        mTransaction.show(mFragmentHomeTab); 
        // }
        mTransaction.commitAllowingStateLoss();

    }

    @Override
    public void goToFragment(int fragmentID) {
        // TODO Auto-generated method stub
        showFragment(fragmentID);
    }
}
