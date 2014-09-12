package com.sharebravo.bravo.control.activity;

import java.util.ArrayList;

import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.WindowManager;
import android.widget.Toast;

import com.sharebravo.bravo.R;
import com.sharebravo.bravo.view.fragment.bravochecking.FragmentBravoMap;
import com.sharebravo.bravo.view.fragment.bravochecking.FragmentBravoReturnSpot;
import com.sharebravo.bravo.view.fragment.home.FragmentBravoTab;

public class ActivityBravoChecking extends VisvaAbstractFragmentActivity {
    // ======================Constant Define===============
    private static final String     FRAGMENT_BRAVO_MAP             = "bravo_map";
    private static final String     FRAGMENT_BRAVO_RETURN_SPOTS    = "return_spots";
    private static final String     FRAGMENT_BRAVO_TAB             = "bravo_tab";

    public static final int         FRAGMENT_BASE_ID               = 1000;
    public static final int         FRAGMENT_BRAVO_TAB_ID          = FRAGMENT_BASE_ID + 1;
    public static final int         FRAGMENT_BRAVO_MAP_ID          = FRAGMENT_BASE_ID + 2;
    public static final int         FRAGMENT_BRAVO_RETURN_SPOTS_ID = FRAGMENT_BASE_ID + 3;
    // ======================Class Define==================
    private FragmentManager         mFmManager;
    private FragmentTransaction     mTransaction;
    private FragmentBravoMap        mFragmentBravoMap;
    private FragmentBravoReturnSpot mFragmentBravoReturnSpots;
    private FragmentBravoTab        mFragmentBravoTab;

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

    private void initializeFragments() {
        mFmManager = getSupportFragmentManager();
        mFragmentBravoMap = (FragmentBravoMap) mFmManager.findFragmentById(R.id.fragment_bravo_map);
        mFragmentBravoReturnSpots = (FragmentBravoReturnSpot) mFmManager.findFragmentById(R.id.fragment_bravo_return_spot);
        mFragmentBravoTab = (FragmentBravoTab) mFmManager.findFragmentById(R.id.fragment_bravo_tab);

        showFragment(FRAGMENT_BRAVO_TAB_ID);
        Toast.makeText(this, "This feature is coming soon", Toast.LENGTH_SHORT).show();
    }

    private void showFragment(int fragmentID) {
        mTransaction = hideFragment();
        switch (fragmentID) {
        case FRAGMENT_BRAVO_TAB_ID:
            mTransaction.show(mFragmentBravoTab);
            addToSBackStack(FRAGMENT_BRAVO_TAB);
            break;
        case FRAGMENT_BRAVO_MAP_ID:
            mTransaction.show(mFragmentBravoMap);
            addToSBackStack(FRAGMENT_BRAVO_MAP);
            break;
        case FRAGMENT_BRAVO_RETURN_SPOTS_ID:
            mTransaction.show(mFragmentBravoReturnSpots);
            addToSBackStack(FRAGMENT_BRAVO_RETURN_SPOTS);
            break;
        }

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
        mTransaction.hide(mFragmentBravoTab);
        return mTransaction;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.slide_out_up);
    }
}
