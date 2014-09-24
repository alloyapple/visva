package com.sharebravo.bravo.control.activity;

import java.util.ArrayList;

import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.WindowManager;
import android.widget.Toast;

import com.sharebravo.bravo.R;
import com.sharebravo.bravo.model.response.Spot;
import com.sharebravo.bravo.sdk.log.AIOLog;
import com.sharebravo.bravo.view.fragment.bravochecking.FragmentBravoMap;
import com.sharebravo.bravo.view.fragment.bravochecking.FragmentBravoReturnSpot;
import com.sharebravo.bravo.view.fragment.bravochecking.FragmentBravoSearch;

public class ActivityBravoChecking extends VisvaAbstractFragmentActivity implements BravoCheckingListener {
    // ======================Constant Define===============
    private static final String     FRAGMENT_BRAVO_MAP             = "bravo_map";
    private static final String     FRAGMENT_BRAVO_RETURN_SPOTS    = "return_spots";
    private static final String     FRAGMENT_BRAVO_TAB             = "bravo_tab";

    public static final int         FRAGMENT_BASE_ID               = 1000;
    public static final int         FRAGMENT_BRAVO_SEARCH_ID       = FRAGMENT_BASE_ID + 1;
    public static final int         FRAGMENT_BRAVO_MAP_ID          = FRAGMENT_BASE_ID + 2;
    public static final int         FRAGMENT_BRAVO_RETURN_SPOTS_ID = FRAGMENT_BASE_ID + 3;
    // ======================Class Define==================
    private FragmentManager         mFmManager;
    private FragmentTransaction     mTransaction;
    private FragmentBravoMap        mFragmentBravoMap;
    private FragmentBravoReturnSpot mFragmentBravoReturnSpots;
    private FragmentBravoSearch     mFragmentBravoSearch;

    // ======================Variable Define===============
    private ArrayList<String>       mBackstack                     = new ArrayList<String>();

    @Override
    public int contentView() {
        return R.layout.activity_bravo_checking;
    }

    @Override
    public void onCreate() {
        if (Build.VERSION.SDK_INT >= 11)
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        initializeFragments();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initializeFragments() {
        mFmManager = getSupportFragmentManager();
        mFragmentBravoMap = (FragmentBravoMap) mFmManager.findFragmentById(R.id.fragment_bravo_map);
        mFragmentBravoReturnSpots = (FragmentBravoReturnSpot) mFmManager.findFragmentById(R.id.fragment_bravo_return_spot);
        mFragmentBravoSearch = (FragmentBravoSearch) mFmManager.findFragmentById(R.id.fragment_bravo_search);

        showFragment(FRAGMENT_BRAVO_SEARCH_ID);
    }

    private void showFragment(int fragmentID) {
        mTransaction = hideFragment();
        switch (fragmentID) {
        case FRAGMENT_BRAVO_SEARCH_ID:
            mTransaction.show(mFragmentBravoSearch);
            addToSBackStack(FRAGMENT_BRAVO_TAB);
            break;
        case FRAGMENT_BRAVO_MAP_ID:
            mTransaction.show(mFragmentBravoMap);
            addToSBackStack(FRAGMENT_BRAVO_MAP);
            break;
        case FRAGMENT_BRAVO_RETURN_SPOTS_ID:
            mTransaction.show(mFragmentBravoReturnSpots);
            addToSBackStack(FRAGMENT_BRAVO_RETURN_SPOTS);
        }
        mTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
        mTransaction.commit();
    }

    private void addToSBackStack(String tag) {
        int index = mBackstack.lastIndexOf(tag);
        if (index == -1) {
            mBackstack.add(tag);
            return;
        }
        try {
            if (!mBackstack.get(index - 1).equals(
                    mBackstack.get(mBackstack.size() - 1))) {
                mBackstack.add(tag);
                return;
            }
        } catch (IndexOutOfBoundsException e) {

        }
        try {
            ArrayList<String> subStack = new ArrayList<String>(mBackstack);
            for (int i = 0; i < subStack.size(); i++) {
                if (i > index) {
                    mBackstack.remove(index);
                }
            }
        } catch (IndexOutOfBoundsException e) {
        }

    }

    private FragmentTransaction hideFragment() {
        mTransaction = mFmManager.beginTransaction();
        mTransaction.hide(mFragmentBravoMap);
        mTransaction.hide(mFragmentBravoReturnSpots);
        mTransaction.hide(mFragmentBravoSearch);
        return mTransaction;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.slide_out_up);
    }

    @Override
    public void goToFragment(int fragmentID) {
        showFragment(fragmentID);
    }

    @Override
    public void goToMapView(Spot spot, int locationType) {
        AIOLog.d("spot:" + spot + ", locationType" + locationType);
        AIOLog.d("" + spot.Spot_Latitude + ",longt:" + spot.Spot_Longitude);
        mFragmentBravoMap.setBravoSpot(spot);
        goToFragment(FRAGMENT_BRAVO_MAP_ID);
    }

    @Override
    public void goToMapView(String foreignID, int locationType) {

    }

    @Override
    public void goToBack() {
        AIOLog.d("mBackstack=" + mBackstack);
        String currentView = null;
        if (mBackstack.size() - 1 > 0)
            currentView = mBackstack.get(mBackstack.size() - 1);
        try {
            mBackstack.remove(mBackstack.size() - 1);
            if (mBackstack.size() == 0) {
                super.onBackPressed();
                return;
            }
        } catch (IndexOutOfBoundsException e) {
            super.onBackPressed();
            return;
        }
        mTransaction = hideFragment();

        Toast.makeText(this, currentView, Toast.LENGTH_LONG).show();
        if (currentView.equals(FRAGMENT_BRAVO_MAP)) {
            mTransaction.show(mFragmentBravoSearch);
        } else if (currentView.equals(FRAGMENT_BRAVO_RETURN_SPOTS_ID)) {
            mTransaction.show(mFragmentBravoReturnSpots);
        } else if (currentView.equals(FRAGMENT_BRAVO_TAB)) {
            onBackPressed();
            return; 
        }
        mTransaction.commitAllowingStateLoss();
    }

    @Override
    public void goToReturnSpotFragment(Spot _spot) {
        mFragmentBravoReturnSpots.setBravoSpot(_spot);
        goToFragment(FRAGMENT_BRAVO_RETURN_SPOTS_ID);
    }
}
