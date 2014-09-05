package com.sharebravo.bravo.control.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.sharebravo.bravo.R;
import com.sharebravo.bravo.model.user.BravoUser;
import com.sharebravo.bravo.sdk.log.AIOLog;
import com.sharebravo.bravo.utils.BravoConstant;
import com.sharebravo.bravo.view.fragment.login_register.FragmentBravoLogin;
import com.sharebravo.bravo.view.fragment.login_register.FragmentBravoRegister;
import com.sharebravo.bravo.view.fragment.login_register.FragmentForgotPassword;
import com.sharebravo.bravo.view.fragment.login_register.FragmentLogin;
import com.sharebravo.bravo.view.fragment.login_register.FragmentRegister;
import com.sharebravo.bravo.view.fragment.login_register.FragmentRegisterUserInfo;
import com.sharebravo.bravo.view.fragment.login_register.FragmentBravoLogin.IShowPageForgotPassword;
import com.sharebravo.bravo.view.fragment.login_register.FragmentLogin.IShowPageBravoLogin;
import com.sharebravo.bravo.view.fragment.login_register.FragmentRegister.IShowPageBravoRegister;

public class ActivityLogin_Register extends FragmentActivity implements IShowPageBravoLogin, IShowPageBravoRegister, IShowPageForgotPassword {

    // ======================Constant Define===============
    private static final String      FRAGMENT_BRAVO_REGISTER     = "bravo_register";
    private static final String      FRAGMENT_LOGIN              = "login";
    private static final String      FRAGMENT_REGISTER           = "register";
    private static final String      FRAGMENT_REGISTER_USER_INFO = "register_user_info";
    private static final String      FRAGMENT_BRAVO_LOGIN        = "bravo_login";
    private static final String      FRAGMENT_FORGOT_PASSWORD    = "forgot_password";
    private final String             PENDING_ACTION_BUNDLE_KEY   = "com.sharebravo.bravo:PendingAction";

    // ======================Class Define==================
    private FragmentManager          mFmManager;
    private FragmentTransaction      mTransaction;
    private FragmentBravoRegister    mFragmentBravoRegister;
    private FragmentLogin            mFragmentLogin;
    private FragmentRegister         mFragmentRegister;
    private FragmentRegisterUserInfo mFragmentRegisterUserInfo;
    private FragmentBravoLogin       mFragmentBravoLogin;
    private FragmentForgotPassword   mFragmentForgotPassword;

    // ======================Variable Define===============
    private ArrayList<String>        mBackstack                  = new ArrayList<String>();
    private UiLifecycleHelper        mUiLifecycleHelper;
    private Session.StatusCallback   mFacebookCallback;
    private int                      mAccessType;
    private PendingAction            mPendingAction              = PendingAction.NONE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);
        /* facebook api */
        mFacebookCallback = new Session.StatusCallback() {
            @Override
            public void call(Session session, SessionState state, Exception exception) {
                onSessionStateChange(session, state, exception);
            }
        };
        mUiLifecycleHelper = new UiLifecycleHelper(this, mFacebookCallback);
        mUiLifecycleHelper.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            String name = savedInstanceState.getString(PENDING_ACTION_BUNDLE_KEY);
            mPendingAction = PendingAction.valueOf(name);
        }

        /* initialize fragments */
        initializeFragments(savedInstanceState);

    }

    private void initializeFragments(Bundle savedInstanceState) {
        mFmManager = getSupportFragmentManager();
        mFragmentBravoRegister = (FragmentBravoRegister) mFmManager.findFragmentById(R.id.fragment_bravo_register);
        mFragmentLogin = (FragmentLogin) mFmManager.findFragmentById(R.id.fragment_login);
        mFragmentRegister = (FragmentRegister) mFmManager.findFragmentById(R.id.fragment_register);
        mFragmentRegisterUserInfo = (FragmentRegisterUserInfo) mFmManager.findFragmentById(R.id.fragment_bravo_user_info);
        mFragmentBravoLogin = (FragmentBravoLogin) mFmManager.findFragmentById(R.id.fragment_bravo_login);
        mFragmentForgotPassword = (FragmentForgotPassword) mFmManager.findFragmentById(R.id.fragment_forgot_password);

        /* set listener for all fragments */
        mFragmentLogin.setListener(this);
        mFragmentRegister.setListener(this);
        mFragmentBravoLogin.setListener(this);

        int fragmentAnimationType = 1;
        mTransaction = hideFragment(fragmentAnimationType);
        mAccessType = getIntent().getExtras().getInt(BravoConstant.ACCESS_TYPE);
        if (mAccessType == BravoConstant.FRAGMENT_LOGIN_ID)
            showFragment(BravoConstant.FRAGMENT_LOGIN_ID);
        else
            showFragment(BravoConstant.FRAGMENT_REGISTER_ID);
    }

    private void showFragment(int fragment) {
        if (mTransaction == null || mTransaction.isEmpty())
            return;
        int fragmentAnimationType = 1;
        mTransaction = hideFragment(fragmentAnimationType);
        switch (fragment) {
        case BravoConstant.FRAGMENT_BRAVO_REGISTER_ID:
            mTransaction.show(mFragmentBravoRegister);
            addToSBackStack(FRAGMENT_BRAVO_REGISTER);
            break;

        case BravoConstant.FRAGMENT_LOGIN_ID:
            mTransaction.show(mFragmentLogin);
            addToSBackStack(FRAGMENT_LOGIN);
            break;

        case BravoConstant.FRAGMENT_REGISTER_ID:
            mTransaction.show(mFragmentRegister);
            addToSBackStack(FRAGMENT_REGISTER);
            break;

        case BravoConstant.FRAGMENT_REGISTER_USER_INFO_ID:
            mTransaction.show(mFragmentRegisterUserInfo);
            addToSBackStack(FRAGMENT_REGISTER_USER_INFO);
            break;
        case BravoConstant.FRAGMENT_BRAVO_LOGIN_ID:
            mTransaction.show(mFragmentBravoLogin);
            addToSBackStack(FRAGMENT_BRAVO_LOGIN);
            break;
        case BravoConstant.FRAGMENT_FORGOT_PASSWORD:
            mTransaction.show(mFragmentForgotPassword);
            addToSBackStack(FRAGMENT_FORGOT_PASSWORD);
            break;
        default:
            break;
        }
        mTransaction.commit();
    }

    public FragmentTransaction hideFragment(int fragmentAnimationType) {
        mTransaction = mFmManager.beginTransaction();
        if (fragmentAnimationType == 1)
            mTransaction.setCustomAnimations(R.anim.slide_in_right,R.anim.fade_in);
        else
            mTransaction.setCustomAnimations(R.anim.fade_in, R.anim.slide_in_left);
        mTransaction.hide(mFragmentBravoRegister);
        mTransaction.hide(mFragmentLogin);
        mTransaction.hide(mFragmentRegister);
        mTransaction.hide(mFragmentRegisterUserInfo);
        mTransaction.hide(mFragmentBravoLogin);
        mTransaction.hide(mFragmentForgotPassword);
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
        AIOLog.d("mBackstack size:" + mBackstack.size());
    }

    public void onClickBackPersonal(View v) {
        AIOLog.d("mBackstack=" + mBackstack);
        /*
         * if (backstack.size() == 0) { if(mTopPanel.isOpen()){
         * mTopPanel.setOpen(false, true); return; } super.onBackPressed();
         * return; } if (backstack.size() == 1) { if
         * (!backstack.get(0).equals(VIEW_HOME)) { showToast(backstack.get(0));
         * mTransaction = hideFragment(); mTransaction.show(mFmHome);
         * backstack.clear(); addToSBackStack(VIEW_HOME); mFmHome.refreshUI();
         * mTransaction.commitAllowingStateLoss(); } else {
         * if(mTopPanel.isOpen()){ mTopPanel.setOpen(false, true); return; }
         * super.onBackPressed(); backstack.clear(); } return; }
         */
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
        String currentView = mBackstack.get(mBackstack.size() - 1);
        int fragmentAnimationType = 0;
        mTransaction = hideFragment(fragmentAnimationType);
        if (currentView.equals(FRAGMENT_BRAVO_LOGIN)) {
            mTransaction.show(mFragmentBravoLogin);
        } else if (currentView.equals(FRAGMENT_BRAVO_REGISTER)) {
            mTransaction.show(mFragmentBravoRegister);
        } else if (currentView.equals(FRAGMENT_LOGIN)) {
            mTransaction.show(mFragmentLogin);
        } else if (currentView.equals(FRAGMENT_REGISTER)) {
            mTransaction.show(mFragmentRegister);
        } else if (currentView.equals(FRAGMENT_REGISTER_USER_INFO)) {
            mTransaction.show(mFragmentRegisterUserInfo);
        }
        AIOLog.d("currentView:" + currentView);
        mTransaction.commitAllowingStateLoss();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mUiLifecycleHelper.onSaveInstanceState(outState);

        outState.putString(PENDING_ACTION_BUNDLE_KEY, mPendingAction.name());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mUiLifecycleHelper.onActivityResult(requestCode, resultCode, data, null);
    }

    @Override
    public void onPause() {
        super.onPause();
        mUiLifecycleHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUiLifecycleHelper.onDestroy();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        mUiLifecycleHelper.onResume();

        // Call the 'activateApp' method to log an app event for use in
        // analytics and advertising reporting. Do so in
        // the onResume methods of the primary Activities that an app may be
        // launched into.

        final Session session = Session.getActiveSession();
        if (session == null || session.isClosed() || !session.isOpened()) {
            mUiLifecycleHelper = new UiLifecycleHelper(this, mFacebookCallback);
        } else {
            AIOLog.e("resume: session", "not null");
            Request request = Request.newMeRequest(session, new Request.GraphUserCallback() {
                @Override
                public void onCompleted(GraphUser user, Response response) {
                    if (session == Session.getActiveSession()) {
                        if (user != null) {
                        }
                    }
                }
            });
            request.executeAsync();
        }

    }

    /**
     * interface to show page bravo login
     */
    public void showPageBravoLogin() {
        showFragment(BravoConstant.FRAGMENT_BRAVO_LOGIN_ID);
    }

    @Override
    public void showPageBravoRegister() {
        showFragment(BravoConstant.FRAGMENT_BRAVO_REGISTER_ID);
    }

    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (mPendingAction != PendingAction.NONE
                && (exception instanceof FacebookOperationCanceledException || exception instanceof FacebookAuthorizationException)) {
            mPendingAction = PendingAction.NONE;
        } else if (state == SessionState.OPENED_TOKEN_UPDATED) {
            handlePendingAction();
        }
    }

    private enum PendingAction {
        NONE, POST_PHOTO, POST_STATUS_UPDATE
    }

    @SuppressWarnings("incomplete-switch")
    private void handlePendingAction() {
        PendingAction previouslyPendingAction = mPendingAction;
        mPendingAction = PendingAction.NONE;

        switch (previouslyPendingAction) {
        case POST_PHOTO:
            break;
        case POST_STATUS_UPDATE:
            break;
        }
    }

    @Override
    public void showPageUserInfo(BravoUser bravoUser) {
        showFragment(BravoConstant.FRAGMENT_REGISTER_USER_INFO_ID);
        mFragmentRegisterUserInfo.updateUserInfo(bravoUser);
    }

    @Override
    public void showPageForgotPassword() {
        showFragment(BravoConstant.FRAGMENT_FORGOT_PASSWORD);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        AIOLog.d("onNewIntent");
        super.onNewIntent(intent);
    }

}
