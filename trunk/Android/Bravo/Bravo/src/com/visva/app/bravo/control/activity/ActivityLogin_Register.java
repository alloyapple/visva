package com.visva.app.bravo.control.activity;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.visva.app.bravo.R;
import com.visva.app.bravo.view.fragment.FragmentBravoRegister;
import com.visva.app.bravo.view.fragment.FragmentLogin;
import com.visva.app.bravo.view.fragment.FragmentRegister;
import com.visva.app.bravo.view.fragment.FragmentRegisterUserInfo;

public class ActivityLogin_Register extends FragmentActivity {

    // ======================Constant Define===============
    private static final String      FRAGMENT_BRAVO_REGISTER        = "bravo_register";
    private static final String      FRAGMENT_LOGIN                 = "login";
    private static final String      FRAGMENT_REGISTER              = "register";
    private static final String      FRIEND_REGISTER_USER_INFO      = "register_user_info";

    private static final int         FRAGMENT_BRAVO_REGISTER_ID     = 1001;
    private static final int         FRAGMENT_LOGIN_ID              = 1002;
    private static final int         FRAGMENT_REGISTER_ID           = 1003;
    private static final int         FRAGMENT_REGISTER_USER_INFO_ID = 1004;

    // ======================Class Define==================
    private FragmentManager          mFmManager;
    private FragmentTransaction      mTransaction;
    private FragmentBravoRegister    mFragmentBravoRegister;
    private FragmentLogin            mFragmentLogin;
    private FragmentRegister         mFragmentRegister;
    private FragmentRegisterUserInfo mFragmentRegisterUserInfo;

    // ======================Variable Define===============
    private ArrayList<String>        mBackstack                      = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);

        /* intialize fragments */
         initializeFragments();

    }

    private void initializeFragments() {
        mFmManager = getSupportFragmentManager();
        mFragmentBravoRegister = (FragmentBravoRegister) mFmManager.findFragmentById(R.id.fragment_bravo_register);
        mFragmentLogin = (FragmentLogin) mFmManager.findFragmentById(R.id.fragment_login);
        mFragmentRegister = (FragmentRegister) mFmManager.findFragmentById(R.id.fragment_register);
        mFragmentRegisterUserInfo = (FragmentRegisterUserInfo) mFmManager.findFragmentById(R.id.fragment_bravo_user_info);

        mTransaction = hideFragment();
        showFragment(FRAGMENT_LOGIN_ID);
    }

    private void showFragment(int fragment) {
        switch (fragment) {

        case FRAGMENT_BRAVO_REGISTER_ID:
            mTransaction = hideFragment();
            mTransaction.show(mFragmentBravoRegister);
            addToSBackStack(FRAGMENT_BRAVO_REGISTER);
            mTransaction.commit();
            break;

        case FRAGMENT_LOGIN_ID:
            mTransaction = hideFragment();
            mTransaction.show(mFragmentLogin);
            addToSBackStack(FRAGMENT_LOGIN);
            mTransaction.commit();
            break;

        case FRAGMENT_REGISTER_ID:
            mTransaction = hideFragment();
            mTransaction.show(mFragmentRegister);
            addToSBackStack(FRAGMENT_REGISTER);
            mTransaction.commit();
            break;

        case FRAGMENT_REGISTER_USER_INFO_ID:
            mTransaction = hideFragment();
            mTransaction.show(mFragmentRegisterUserInfo);
            addToSBackStack(FRIEND_REGISTER_USER_INFO);
            mTransaction.commit();
            break;
        default:
            break;
        }
    }

    public FragmentTransaction hideFragment() {
        mTransaction = mFmManager.beginTransaction();
        mTransaction.hide(mFragmentBravoRegister);
        mTransaction.hide(mFragmentLogin);
        mTransaction.hide(mFragmentRegister);
        mTransaction.hide(mFragmentRegisterUserInfo);
        return mTransaction;
    }

    public void addToSBackStack(String tag) {
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
}
