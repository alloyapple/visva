package com.sharebravo.bravo.control.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sharebravo.bravo.R;
import com.sharebravo.bravo.model.user.BravoUser;
import com.sharebravo.bravo.model.user.ObGetLoginedUser;
import com.sharebravo.bravo.sdk.log.AIOLog;
import com.sharebravo.bravo.sdk.util.network.AsyncHttpPost;
import com.sharebravo.bravo.sdk.util.network.AsyncHttpResponseProcess;
import com.sharebravo.bravo.sdk.util.network.ParameterFactory;
import com.sharebravo.bravo.utils.BravoConstant;
import com.sharebravo.bravo.utils.BravoSharePrefs;
import com.sharebravo.bravo.utils.BravoUtils;
import com.sharebravo.bravo.utils.BravoWebServiceConfig;
import com.sharebravo.bravo.view.fragment.login_register.FragmentBravoLogin;
import com.sharebravo.bravo.view.fragment.login_register.FragmentBravoLogin.IShowPageForgotPassword;
import com.sharebravo.bravo.view.fragment.login_register.FragmentBravoRegister;
import com.sharebravo.bravo.view.fragment.login_register.FragmentForgotPassword;
import com.sharebravo.bravo.view.fragment.login_register.FragmentLogin;
import com.sharebravo.bravo.view.fragment.login_register.FragmentLogin.IShowPageBravoLogin;
import com.sharebravo.bravo.view.fragment.login_register.FragmentRegister;
import com.sharebravo.bravo.view.fragment.login_register.FragmentRegister.IShowPageBravoRegister;
import com.sharebravo.bravo.view.fragment.login_register.FragmentRegisterUserInfo;

public class ActivityLogin_Register extends FragmentActivity implements IShowPageBravoLogin, IShowPageBravoRegister, IShowPageForgotPassword {

    // ======================Constant Define===============
    public static final int          TWITTER_TYPE_LOGIN          = 1;
    public static final int          TWITTER_TYPE_REGISTER       = 2;
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
    private static RequestToken      mTwitterRequestToken;
    protected static Twitter         mTwitter;
    private static int               mTwitterType;

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

        /**
         * This if conditions is tested once is
         * redirected from twitter page. Parse the uri to get oAuth
         * Verifier
         * */
        if (!isTwitterLoggedInAlready()) {
            Uri uri = getIntent().getData();
            if (uri != null && uri.toString().startsWith(BravoConstant.TWITTER_CALLBACK_URL)) {
                // oAuth verifier
                String verifier = uri.getQueryParameter(BravoConstant.URL_TWITTER_OAUTH_VERIFIER);

                try {
                    // Get the access token
                    AccessToken accessToken = mTwitter.getOAuthAccessToken(mTwitterRequestToken, verifier);

                    BravoSharePrefs.getInstance(this).putStringValue(BravoConstant.PREF_KEY_TWITTER_OAUTH_TOKEN, accessToken.getToken());
                    BravoSharePrefs.getInstance(this).putStringValue(BravoConstant.PREF_KEY_TWITTER_OAUTH_SCRET,
                            accessToken.getTokenSecret());
                    BravoSharePrefs.getInstance(this).putBooleanValue(BravoConstant.PREF_KEY_TWITTER_LOGIN, true);
                    Log.e("Twitter OAuth Token", "> " + accessToken.getToken());

                    // Getting user details from twitter
                    long userID = accessToken.getUserId();
                    twitter4j.User user = mTwitter.showUser(userID);

                    BravoUser _bravoUser = new BravoUser();
                    _bravoUser.mUserEmail = "no_tw_account" + System.currentTimeMillis() + "@nomail.com";
                    _bravoUser.mUserName = user.getName();
                    _bravoUser.mUserId = user.getId() + "";
                    _bravoUser.mAuthenMethod = BravoConstant.TWITTER;
                    _bravoUser.mTimeZone = TimeZone.getDefault().getID();
                    Locale current = getResources().getConfiguration().locale;
                    _bravoUser.mLocale = current.toString();
                    _bravoUser.mForeign_Id = String.valueOf(user.getId());
                    _bravoUser.mUserPassWord = accessToken.getToken() + "," + accessToken.getTokenSecret();
                    _bravoUser.mRegisterType = BravoConstant.REGISTER_BY_TWITTER;

                    AIOLog.d("accessToken.getTokenSecret():" + accessToken.getTokenSecret() + ",accessToken.getToken():" + accessToken.getToken());
                    if (mTwitterType == TWITTER_TYPE_REGISTER) {
                        showFragment(BravoConstant.FRAGMENT_REGISTER_USER_INFO_ID);
                        mFragmentRegisterUserInfo.updateUserInfo(_bravoUser);
                    } else if (mTwitterType == TWITTER_TYPE_LOGIN) {
                        requestToPostBravoUserbySNS(_bravoUser);
                    }
                } catch (Exception e) {
                    // Check log for login errors
                    Log.e("Twitter Login Error", "> " + e.getMessage());
                }
            }
        }

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
            mTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.fade_in);
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

    /**
     * Check user already logged in your application using twitter Login flag is
     * fetched from Shared Preferences
     * */
    private boolean isTwitterLoggedInAlready() {
        // return twitter login status from Shared Preferences
        return BravoSharePrefs.getInstance(this).getBooleanValue(BravoConstant.PREF_KEY_TWITTER_LOGIN);
    }

    /**
     * twitter login and authen
     */
    private void requestToGetTwitterUserInfo() {
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setOAuthConsumerKey(BravoConstant.TWITTER_CONSUMER_KEY);
        builder.setOAuthConsumerSecret(BravoConstant.TWITTER_CONSUMER_SECRET);
        builder.setUseSSL(true);

        String access_token = BravoSharePrefs.getInstance(this).getStringValue(BravoConstant.PREF_KEY_TWITTER_OAUTH_TOKEN);
        String access_token_secret = BravoSharePrefs.getInstance(this).getStringValue(BravoConstant.PREF_KEY_TWITTER_OAUTH_SCRET);
        AccessToken accessToken = new AccessToken(access_token, access_token_secret);

        mTwitter = new TwitterFactory(builder.build()).getInstance(accessToken);

        try {
            mTwitter.verifyCredentials();
        } catch (Exception e) {
            e.printStackTrace();
        }
        long userID = accessToken.getUserId();
        twitter4j.User user = null;
        try {
            user = mTwitter.showUser(userID);
        } catch (TwitterException e) {
            e.printStackTrace();
        }
        if (user == null){
            AIOLog.e("user twitter is null");
            return;
        }
        BravoUser _bravoUser = new BravoUser();
        _bravoUser.mUserEmail = "no_tw_account" + System.currentTimeMillis() + "@nomail.com";
        _bravoUser.mUserName = user.getName();
        _bravoUser.mUserId = user.getId() + "";
        _bravoUser.mAuthenMethod = BravoConstant.TWITTER;
        _bravoUser.mTimeZone = TimeZone.getDefault().getID();
        Locale current = getResources().getConfiguration().locale;
        _bravoUser.mLocale = current.toString();
        _bravoUser.mForeign_Id = String.valueOf(user.getId());
        _bravoUser.mUserPassWord = accessToken.getToken() + "," + accessToken.getTokenSecret();
        _bravoUser.mRegisterType = BravoConstant.REGISTER_BY_TWITTER;

        AIOLog.d("accessToken.getTokenSecret():" + accessToken.getTokenSecret() + ",accessToken.getToken():" + accessToken.getToken());
        if (mTwitterType == TWITTER_TYPE_REGISTER) {
            showFragment(BravoConstant.FRAGMENT_REGISTER_USER_INFO_ID);
            mFragmentRegisterUserInfo.updateUserInfo(_bravoUser);
        } else if (mTwitterType == TWITTER_TYPE_LOGIN) {
            requestToPostBravoUserbySNS(_bravoUser);
        }
    }

    @Override
    public void clickTwitterRegister(int twitterType) {
        mTwitterType = twitterType;
        // Check if already logged in
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        if (!isTwitterLoggedInAlready()) {
            ConfigurationBuilder builder = new ConfigurationBuilder();
            builder.setOAuthConsumerKey(BravoConstant.TWITTER_CONSUMER_KEY);
            builder.setOAuthConsumerSecret(BravoConstant.TWITTER_CONSUMER_SECRET);
            Configuration configuration = builder.build();

            TwitterFactory factory = new TwitterFactory(configuration);
            mTwitter = factory.getInstance();

            try {
                mTwitterRequestToken = mTwitter.getOAuthRequestToken(BravoConstant.TWITTER_CALLBACK_URL);
                this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mTwitterRequestToken.getAuthenticationURL())));
                finish();
            } catch (TwitterException e) {
                e.printStackTrace();
            }
        } else {
            // user already logged into twitter
            requestToGetTwitterUserInfo();
            Toast.makeText(this, "Already Logged into twitter", Toast.LENGTH_LONG).show();
        }
    }

    private void requestToPostBravoUserbySNS(final BravoUser bravoUser) {
        AIOLog.d("bravoUser:" + bravoUser);
        if (bravoUser == null)
            return;
        AIOLog.d("==================================");
        AIOLog.d("bravoUser.mAuthenMethod=>" + bravoUser.mAuthenMethod);
        AIOLog.d("bravoUser.mUserName=>" + bravoUser.mUserName);
        AIOLog.d("bravoUser.mUserEmail=>" + bravoUser.mUserEmail);
        AIOLog.d("bravoUser.mUserPassWord=>" + bravoUser.mUserPassWord);
        AIOLog.d("bravoUser.mTimeZone=>" + bravoUser.mTimeZone);
        AIOLog.d("bravoUser.mLocale=>" + bravoUser.mLocale);
        AIOLog.d("bravoUser.mForeign_Id=>" + bravoUser.mForeign_Id);
        AIOLog.d("==================================");
        AIOLog.d("==================================");
        HashMap<String, String> subParams = new HashMap<String, String>();
        subParams.put("Auth_Method", bravoUser.mAuthenMethod);
        subParams.put("Full_Name", bravoUser.mUserName);
        subParams.put("Email", bravoUser.mUserEmail);
        subParams.put("Password", bravoUser.mUserPassWord);
        subParams.put("Time_Zone", bravoUser.mTimeZone);
        subParams.put("Locale", bravoUser.mLocale);
        subParams.put("Foreign_ID", bravoUser.mForeign_Id);
        subParams.put("APNS_Token", "abcdef12345");
        JSONObject jsonObject = new JSONObject(subParams);
        String subParamsStr = jsonObject.toString();

        List<NameValuePair> params = ParameterFactory.createSubParams(subParamsStr);
        AsyncHttpPost postRegister = new AsyncHttpPost(ActivityLogin_Register.this, new AsyncHttpResponseProcess(ActivityLogin_Register.this,
                mFragmentBravoLogin) {
            @Override
            public void processIfResponseSuccess(String response) {
                AIOLog.d("reponse:" + response);
                JSONObject jsonObject = null;

                try {
                    jsonObject = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (jsonObject == null)
                    return;

                String status = null;
                try {
                    status = jsonObject.getString("status");
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                Gson gson = new GsonBuilder().serializeNulls().create();
                ObGetLoginedUser obPostUserFailed;

                if (status == String.valueOf(BravoWebServiceConfig.STATUS_RESPONSE_DATA_SUCCESS)) {
                    /* save data */
                    BravoUtils.saveResponseToSharePreferences(ActivityLogin_Register.this, bravoUser.mRegisterType, response);

                    /* go to home screen */
                    Intent homeIntent = new Intent(ActivityLogin_Register.this, HomeActivity.class);
                    homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(homeIntent);
                    finish();
                } else {
                    obPostUserFailed = gson.fromJson(response.toString(), ObGetLoginedUser.class);
                    AIOLog.e("obPostUserFailed.error:" + obPostUserFailed.error);
                }
            }

            @Override
            public void processIfResponseFail() {
                AIOLog.d("response error");
            }
        }, params, true);
        postRegister.execute(BravoWebServiceConfig.URL_POST_USER);
    }
}
