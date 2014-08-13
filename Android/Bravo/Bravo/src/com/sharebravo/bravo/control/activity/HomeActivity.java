package com.sharebravo.bravo.control.activity;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.sharebravo.bravo.MyApplication;
import com.sharebravo.bravo.R;
import com.sharebravo.bravo.view.fragment.FragmentBravoTab;
import com.sharebravo.bravo.view.fragment.FragmentHomeTab;
import com.sharebravo.bravo.view.fragment.FragmentMyDataTab;
import com.sharebravo.bravo.view.fragment.FragmentNetworkTab;
import com.sharebravo.bravo.view.fragment.FragmentSearchTab;

public class HomeActivity extends VisvaAbstractFragmentActivity {

    // ======================Constant Define===============
    private static final String FRAGMENT_HOME_TAB       = "home_tab";
    private static final String FRAGMENT_NETWORK_TAB    = "network_tab";
    private static final String FRAGMENT_BRAVO_TAB      = "bravo_tab";
    private static final String FRAGMENT_SEARCH_TAB     = "search_tab";
    private static final String FRAGMENT_MYDATA_TAB     = "mydata_tab";

    private static final int    FRAGMENT_BASE_ID        = 1000;
    private static final int    FRAGMENT_HOME_TAB_ID    = FRAGMENT_BASE_ID + 1;
    private static final int    FRAGMENT_NETWORK_TAB_ID = FRAGMENT_BASE_ID + 2;
    private static final int    FRAGMENT_BRAVO_TAB_ID   = FRAGMENT_BASE_ID + 3;
    private static final int    FRAGMENT_SEARCH_TAB_ID  = FRAGMENT_BASE_ID + 4;
    private static final int    FRAGMENT_MYDATA_TAB_ID  = FRAGMENT_BASE_ID + 5;

    // ======================Class Define==================
    private FragmentManager     mFmManager;
    private FragmentTransaction mTransaction;
    private FragmentHomeTab     mFragmentHomeTab;
    private FragmentNetworkTab  mFragmentNetworkTab;
    private FragmentBravoTab    mFragmentBravoTab;
    private FragmentSearchTab   mFragmentSearchTab;
    private FragmentMyDataTab   mFragmentMyDataTab;

    private Button              btnHome;
    private Button              btnNetwork;
    private Button              btnBravo;
    private Button              btnSearch;
    private Button              btnMyData;

    // ======================Variable Define===============
    private ArrayList<String>   backstack               = new ArrayList<String>();

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
            break;
        case R.id.btn_network:
            hideTabButton();
            showFragment(FRAGMENT_NETWORK_TAB_ID);
            btnNetwork.setBackgroundResource(R.drawable.tab_feed_on);
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
            break;
        case R.id.btn_mydata:
            hideTabButton();
            showFragment(FRAGMENT_MYDATA_TAB_ID);
            btnMyData.setBackgroundResource(R.drawable.tab_mydata_on);
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

    // Analysis
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

        mTransaction = hideFragment();
        showFragment(FRAGMENT_HOME_TAB_ID);
    }

    private void initializeUITab() {
        btnHome = (Button) findViewById(R.id.btn_home);
        btnNetwork = (Button) findViewById(R.id.btn_network);
        btnBravo = (Button) findViewById(R.id.btn_bravo);
        btnSearch = (Button) findViewById(R.id.btn_search);
        btnMyData = (Button) findViewById(R.id.btn_mydata);
    }

    public void hideTabButton() {
        btnHome.setBackgroundResource(R.drawable.tab_home_off);
        btnNetwork.setBackgroundResource(R.drawable.tab_feed_off);
        btnBravo.setBackgroundResource(R.drawable.tab_bravo_off);
        btnSearch.setBackgroundResource(R.drawable.tab_search_off);
        btnMyData.setBackgroundResource(R.drawable.tab_mydata_off);
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
}
