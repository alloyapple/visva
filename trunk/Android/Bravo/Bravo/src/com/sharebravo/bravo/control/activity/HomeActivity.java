package com.sharebravo.bravo.control.activity;

import java.io.IOException;
import java.util.ArrayList;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sharebravo.bravo.MyApplication;
import com.sharebravo.bravo.R;
import com.sharebravo.bravo.control.request.BravoRequestManager;
import com.sharebravo.bravo.control.request.IRequestListener;
import com.sharebravo.bravo.foursquare.models.OFPostVenue;
import com.sharebravo.bravo.model.SessionLogin;
import com.sharebravo.bravo.model.response.ObAllowBravo;
import com.sharebravo.bravo.model.response.ObBravo;
import com.sharebravo.bravo.model.response.ObGetAllBravoRecentPosts;
import com.sharebravo.bravo.model.response.ObGetUserInfo;
import com.sharebravo.bravo.model.response.SNS;
import com.sharebravo.bravo.model.response.Spot;
import com.sharebravo.bravo.sdk.log.AIOLog;
import com.sharebravo.bravo.utils.BravoConstant;
import com.sharebravo.bravo.utils.BravoSharePrefs;
import com.sharebravo.bravo.utils.BravoUtils;
import com.sharebravo.bravo.utils.BravoWebServiceConfig;
import com.sharebravo.bravo.utils.DialogUtility;
import com.sharebravo.bravo.utils.WakeLocker;
import com.sharebravo.bravo.view.fragment.home.FragmentBravoDetail;
import com.sharebravo.bravo.view.fragment.home.FragmentCoverImage;
import com.sharebravo.bravo.view.fragment.home.FragmentDuplicateSpot;
import com.sharebravo.bravo.view.fragment.home.FragmentHomeNotification;
import com.sharebravo.bravo.view.fragment.home.FragmentHomeTab;
import com.sharebravo.bravo.view.fragment.home.FragmentInputMySpot;
import com.sharebravo.bravo.view.fragment.home.FragmentLiked;
import com.sharebravo.bravo.view.fragment.home.FragmentNetworkTab;
import com.sharebravo.bravo.view.fragment.home.FragmentSaved;
import com.sharebravo.bravo.view.fragment.home.FragmentSearchTab;
import com.sharebravo.bravo.view.fragment.home.FragmentShare;
import com.sharebravo.bravo.view.fragment.home.FragmentSpotDetail;
import com.sharebravo.bravo.view.fragment.maps.FragmentMapView;
import com.sharebravo.bravo.view.fragment.setting.FragmentSetting;
import com.sharebravo.bravo.view.fragment.setting.FragmentShareWithFriends;
import com.sharebravo.bravo.view.fragment.setting.FragmentTermOfUse;
import com.sharebravo.bravo.view.fragment.setting.FragmentUpdateUserInfo;
import com.sharebravo.bravo.view.fragment.userprofile.FragmentFavourite;
import com.sharebravo.bravo.view.fragment.userprofile.FragmentFollower;
import com.sharebravo.bravo.view.fragment.userprofile.FragmentFollowing;
import com.sharebravo.bravo.view.fragment.userprofile.FragmentHistory;
import com.sharebravo.bravo.view.fragment.userprofile.FragmentUserDataTab;
import com.sharebravo.bravo.view.fragment.userprofile.FragmentUserDataTab.IShowPageSettings;
import com.sharebravo.bravo.view.fragment.userprofile.FragmentViewImage;

public class HomeActivity extends VisvaAbstractFragmentActivity implements HomeActionListener, IShowPageSettings {

    // ======================Constant Define===============
    private static final String      PENDING_ACTION_BUNDLE_KEY      = "com.sharebravo.bravo:PendingAction";
    public static final int          REQUEST_CODE_CHECKING_BRAVO    = 1;
    public static final int          REQUEST_CODE_TAP_HERE_BRAVO    = 2;
    public static final String       EXTRA_MESSAGE                  = "message";
    private static final int         NOT_SHOW_FRAGMENT              = 0;
    private static final int         SHOW_ANIMATION_TO_LEFT         = 1;
    private static final int         SHOW_ANIMATION_TO_RIGHT        = 2;

    // private int mTab;

    public static final int          FRAGMENT_BASE_ID               = 1000;
    public static final int          FRAGMENT_HOME_TAB_ID           = FRAGMENT_BASE_ID + 1;
    public static final int          FRAGMENT_NETWORK_TAB_ID        = FRAGMENT_BASE_ID + 2;
    public static final int          FRAGMENT_SEARCH_TAB_ID         = FRAGMENT_BASE_ID + 4;
    public static final int          FRAGMENT_USER_DATA_TAB_ID      = FRAGMENT_BASE_ID + 5;
    public static final int          FRAGMENT_RECENT_POST_DETAIL_ID = FRAGMENT_BASE_ID + 6;
    public static final int          FRAGMENT_MAP_VIEW_ID           = FRAGMENT_BASE_ID + 7;
    public static final int          FRAGMENT_HOME_NOTIFICATION_ID  = FRAGMENT_BASE_ID + 8;
    public static final int          FRAGMENT_SETTINGS_ID           = FRAGMENT_BASE_ID + 9;
    public static final int          FRAGMENT_UPDATE_USER_INFO_ID   = FRAGMENT_BASE_ID + 10;
    public static final int          FRAGMENT_TERM_OF_USE_ID        = FRAGMENT_BASE_ID + 11;
    public static final int          FRAGMENT_COVER_IMAGE_ID        = FRAGMENT_BASE_ID + 12;
    public static final int          FRAGMENT_SHARE_ID              = FRAGMENT_BASE_ID + 13;
    public static final int          FRAGMENT_SHARE_WITH_FRIENDS_ID = FRAGMENT_BASE_ID + 14;
    public static final int          FRAGMENT_HISTORY_ID            = FRAGMENT_BASE_ID + 15;
    public static final int          FRAGMENT_FOLLOWING_ID          = FRAGMENT_BASE_ID + 16;
    public static final int          FRAGMENT_FOLLOWER_ID           = FRAGMENT_BASE_ID + 17;
    public static final int          FRAGMENT_FAVOURITE_ID          = FRAGMENT_BASE_ID + 18;
    public static final int          FRAGMENT_LIKED_ID              = FRAGMENT_BASE_ID + 19;
    public static final int          FRAGMENT_SAVED_ID              = FRAGMENT_BASE_ID + 20;
    public static final int          FRAGMENT_VIEW_IMAGE_ID         = FRAGMENT_BASE_ID + 21;
    public static final int          FRAGMENT_SPOT_DETAIL_ID        = FRAGMENT_BASE_ID + 22;
    public static final int          FRAGMENT_INPUT_MYSPOT_ID       = FRAGMENT_BASE_ID + 23;
    public static final int          FRAGMENT_AFTER_BRAVO_ID        = FRAGMENT_BASE_ID + 24;
    public static final int          FRAGMENT_SPOT_DUPLICATE_ID     = FRAGMENT_BASE_ID + 25;

    // ======================Class Define==================
    private FragmentManager          mFmManager;
    private FragmentTransaction      mTransaction;
    private FragmentHomeTab          mFragmentHomeTab;
    private FragmentNetworkTab       mFragmentNetworkTab;
    private FragmentSearchTab        mFragmentSearchTab;
    private FragmentUserDataTab      mFragmentUserDataTab;
    private FragmentBravoDetail      mFragmentRecentPostDetail;
    private FragmentMapView          mFragmentMapView;
    private FragmentHomeNotification mFragmentHomeNotification;
    private FragmentSetting          mFragmentSetting;
    private FragmentUpdateUserInfo   mFragmentUpdateUserInfo;
    private FragmentTermOfUse        mFragmentTermOfUse;
    private FragmentCoverImage       mFragmentCoverImage;
    private FragmentShare            mFragmentShare;
    private FragmentShareWithFriends mFragmentShareWithFriends;
    private FragmentViewImage        mFragmentViewImage;

    private FragmentHistory          mFragmentHistory;
    private FragmentFollowing        mFragmentFollowing;
    private FragmentFollower         mFragmentFollower;
    private FragmentFavourite        mFragmentFavourite;
    private FragmentLiked            mFragmentLiked;
    private FragmentSaved            mFragmentSaved;
    private FragmentSpotDetail       mFragmentSpotDetail;
    private FragmentInputMySpot      mFragmentInputMySpot;
    private FragmentDuplicateSpot    mFragmentDuplicateSpot;

    private Button                   btnHome;
    private Button                   btnNetwork;
    private Button                   btnBravo;
    private Button                   btnSearch;
    private Button                   btnMyData;

    private TextView                 txtHome;
    private TextView                 txtNetwork;
    private TextView                 txtSearch;
    private TextView                 txtMyData;
    private static RequestToken      mTwitterRequestToken;
    protected static Twitter         mTwitter;
    protected ProgressDialog         pDialog;
    private static ObBravo           mObBravo;
    private static String            mBravoId;
    private static String            mSharedSnsText;
    // ======================Variable Define===============
    private ArrayList<Integer>       backstackID                    = new ArrayList<Integer>();
    private ArrayList<Integer>       backstackHome                  = new ArrayList<Integer>();
    private ArrayList<Integer>       backstackNetwork               = new ArrayList<Integer>();
    private ArrayList<Integer>       backstackSearch                = new ArrayList<Integer>();
    private ArrayList<Integer>       backstackMyData                = new ArrayList<Integer>();

    private UiLifecycleHelper        mUiLifecycleHelper;
    private Session.StatusCallback   mFacebookCallback;
    private PendingAction            mPendingAction                 = PendingAction.NONE;
    private boolean                  mBackPressedToExitOnce         = false;
    private SessionLogin             mSessionLogin                  = null;
    private int                      mLoginBravoViaType             = BravoConstant.NO_LOGIN_SNS;
    private GoogleCloudMessaging     mGoogleCloudMessaging;
    private String                   mRegisterId;

    @Override
    public int contentView() {
        return R.layout.activity_home;
    }

    String userId;
    String accessToken;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onCreate() {
        MyApplication myApp = (MyApplication) getApplication();
        myApp._homeActivity = this;
        Log.d("Twitter", "onCreated");
        mLoginBravoViaType = BravoSharePrefs.getInstance(this).getIntValue(BravoConstant.PREF_KEY_SESSION_LOGIN_BRAVO_VIA_TYPE);
        mSessionLogin = BravoUtils.getSession(this, mLoginBravoViaType);
        userId = mSessionLogin.userID;
        accessToken = mSessionLogin.accessToken;
        /* facebook api */
        mFacebookCallback = new Session.StatusCallback() {
            @Override
            public void call(final Session session, final SessionState state, final Exception exception) {
                AIOLog.d("session callback login:" + session + "state: " + state);
            }
        };
        Bundle bundle = getIntent().getExtras();
        mUiLifecycleHelper = new UiLifecycleHelper(this, mFacebookCallback);
        mUiLifecycleHelper.onCreate(bundle);

        if (Build.VERSION.SDK_INT >= 11)
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        initializeUITab();
        initializeFragments();

        /**
         * This if conditions is tested once is
         * redirected from twitter page. Parse the uri to get oAuth
         * Verifier
         * */
        Log.d("Twitter", "BravoUtils.isTwitterLoggedInAlready:" + BravoUtils.isTwitterLoggedInAlready(this));
        if (!BravoUtils.isTwitterLoggedInAlready(this)) {
            Uri uri = getIntent().getData();
            
            Log.d("Twitter", "URI:"+uri);
            if (uri != null
                    && (uri.toString().startsWith(BravoConstant.TWITTER_CALLBACK_HOME_URL) || uri.toString().startsWith(
                            BravoConstant.TWITTER_CALLBACK_SETTING_URL))) {
                // oAuth verifier
                String verifier = uri.getQueryParameter(BravoConstant.URL_TWITTER_OAUTH_VERIFIER);
                Log.d("Twitter", "verifier:"+verifier);
                try {
                    // Get the access token
                    AccessToken accessToken = mTwitter.getOAuthAccessToken(mTwitterRequestToken, verifier);

                    BravoSharePrefs.getInstance(this).putStringValue(BravoConstant.PREF_KEY_TWITTER_OAUTH_TOKEN, accessToken.getToken());
                    BravoSharePrefs.getInstance(this).putStringValue(BravoConstant.PREF_KEY_TWITTER_OAUTH_SCRET,
                            accessToken.getTokenSecret());
                    BravoSharePrefs.getInstance(this).putBooleanValue(BravoConstant.PREF_KEY_TWITTER_LOGIN, true);
                    BravoSharePrefs.getInstance(this).putStringValue(BravoConstant.PREF_KEY_TWITTER_ID_LOGINED, userId);
                    Log.e("Twitter", "> " + accessToken.getToken());

                    // Getting user details from twitter
                    long userID = accessToken.getUserId();
                    twitter4j.User user = mTwitter.showUser(userID);
                    if (user == null) {
                        Log.d("Twitter", "user twitter to share is null");
                        return;
                    }
                    Log.d("Twitter", "twitter uri:" + uri);
                    if (uri.toString().startsWith(BravoConstant.TWITTER_CALLBACK_HOME_URL)) {
                        // Check for blank text
                        String bravoUrl = BravoWebServiceConfig.URL_BRAVO_ID_DETAIL.replace("{Bravo_ID}", mBravoId);
                        new UpdateTwitterStatus().execute(mSharedSnsText + " \n" + bravoUrl);
                        goToRecentPostDetail(mObBravo);
                    } else {
                        goToFragment(FRAGMENT_SETTINGS_ID);
                    }
                    SNS sns = new SNS();
                    sns.foreignID = userId;
                    sns.foreignSNS = BravoConstant.TWITTER;
                    sns.foreignAccessToken = accessToken.getToken() + "," + accessToken.getTokenSecret();
                    putSNS(sns);
                } catch (Exception e) {
                    Log.e("Twitter", " Login Error > " + e.getMessage());
                }
            }
        }

        registerReceiver(mHandleMessageReceiver, new IntentFilter(BravoConstant.DISPLAY_MESSAGE_ACTION));

        /**
         * GCM
         */
        if (BravoUtils.checkPlayServices(this)) {
            mGoogleCloudMessaging = GoogleCloudMessaging.getInstance(this);
            mRegisterId = getRegistrationId(this);
            AIOLog.d("regid:" + mRegisterId);
            if (mRegisterId.isEmpty()) {
                registerInBackground();
            }
        } else {
            AIOLog.e("No valid Google Play Services APK found.");
        }
        
    }

    public void resetStack() {

    }

    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.btn_home: {
            backstackID = backstackHome;
            hideTabButton();
            if (backstackID.size() == 0) {
                showFragment(FRAGMENT_HOME_TAB_ID, false, NOT_SHOW_FRAGMENT);
            } else {
                showFragment(getTopBackStack(), true, NOT_SHOW_FRAGMENT);
            }
            btnHome.setBackgroundResource(R.drawable.tab_home_on);
            txtHome.setTextColor(Color.WHITE);
        }
            break;
        case R.id.btn_network: {
            backstackID = backstackNetwork;
            hideTabButton();
            if (backstackID.size() == 0) {
                showFragment(FRAGMENT_NETWORK_TAB_ID, false, NOT_SHOW_FRAGMENT);
            } else {
                showFragment(getTopBackStack(), true, NOT_SHOW_FRAGMENT);
            }
            btnNetwork.setBackgroundResource(R.drawable.tab_feed_on);
            txtNetwork.setTextColor(Color.WHITE);
        }
            break;
        case R.id.btn_bravo:
            hideTabButton();
            Intent bravoIntent = new Intent(HomeActivity.this, ActivityBravoChecking.class);
            bravoIntent.putExtra(BravoConstant.EXTRA_SPOT_JSON, "");
            startActivityForResult(bravoIntent, REQUEST_CODE_CHECKING_BRAVO);
            overridePendingTransition(R.anim.slide_in_up, R.anim.fade_in);
            break;
        case R.id.btn_search: {
            backstackID = backstackSearch;
            hideTabButton();
            if (backstackID.size() == 0) {
                showFragment(FRAGMENT_SEARCH_TAB_ID, false, NOT_SHOW_FRAGMENT);
            } else {
                showFragment(getTopBackStack(), true, NOT_SHOW_FRAGMENT);
            }
            btnSearch.setBackgroundResource(R.drawable.tab_search_on);
            txtSearch.setTextColor(Color.WHITE);
        }
            break;
        case R.id.btn_mydata: {
            backstackID = backstackMyData;
            mFragmentUserDataTab.setForeignID(userId);
            if (backstackID.size() == 0) {
                showFragment(FRAGMENT_USER_DATA_TAB_ID, false, NOT_SHOW_FRAGMENT);
            } else {
                showFragment(getTopBackStack(), true, NOT_SHOW_FRAGMENT);
            }
            hideTabButton();
            btnMyData.setBackgroundResource(R.drawable.tab_mydata_on);
            txtMyData.setTextColor(Color.WHITE);
        }
            break;
        default:
            break;
        }
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
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_CHECKING_BRAVO) {
                goToAddSpot();
            } else if (requestCode == REQUEST_CODE_TAP_HERE_BRAVO) {
            }
        }
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
    protected void onStop() {
        try {
            unregisterReceiver(mHandleMessageReceiver);
        } catch (IllegalArgumentException e) {

        }
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Twitter", "onResume");
        mUiLifecycleHelper.onResume();
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

    private void initializeFragments() {
        mFmManager = getSupportFragmentManager();
        mFragmentHomeTab = (FragmentHomeTab) mFmManager.findFragmentById(R.id.fragment_home_tab);
        mFragmentNetworkTab = (FragmentNetworkTab) mFmManager.findFragmentById(R.id.fragment_network_tab);
        mFragmentSearchTab = (FragmentSearchTab) mFmManager.findFragmentById(R.id.fragment_search_tab);
        mFragmentUserDataTab = (FragmentUserDataTab) mFmManager.findFragmentById(R.id.fragment_user_data_tab);
        mFragmentRecentPostDetail = (FragmentBravoDetail) mFmManager.findFragmentById(R.id.fragment_recent_post_detail);
        mFragmentMapView = (FragmentMapView) mFmManager.findFragmentById(R.id.fragment_map_view);
        mFragmentHomeNotification = (FragmentHomeNotification) mFmManager.findFragmentById(R.id.fragment_home_notification);
        mFragmentSetting = (FragmentSetting) mFmManager.findFragmentById(R.id.fragment_settings);
        mFragmentUpdateUserInfo = (FragmentUpdateUserInfo) mFmManager.findFragmentById(R.id.fragment_update_user_info);
        mFragmentTermOfUse = (FragmentTermOfUse) mFmManager.findFragmentById(R.id.fragment_term_of_use);
        mFragmentCoverImage = (FragmentCoverImage) mFmManager.findFragmentById(R.id.fragment_cover_image);
        mFragmentShare = (FragmentShare) mFmManager.findFragmentById(R.id.fragment_share);
        mFragmentShareWithFriends = (FragmentShareWithFriends) mFmManager.findFragmentById(R.id.fragment_share_with_friends);
        mFragmentViewImage = (FragmentViewImage) mFmManager.findFragmentById(R.id.fragment_view_image);

        mFragmentHistory = (FragmentHistory) mFmManager.findFragmentById(R.id.fragment_history);
        mFragmentFollowing = (FragmentFollowing) mFmManager.findFragmentById(R.id.fragment_following);
        mFragmentFollower = (FragmentFollower) mFmManager.findFragmentById(R.id.fragment_follower);
        mFragmentFavourite = (FragmentFavourite) mFmManager.findFragmentById(R.id.fragment_favourite);
        mFragmentLiked = (FragmentLiked) mFmManager.findFragmentById(R.id.fragment_liked);
        mFragmentSaved = (FragmentSaved) mFmManager.findFragmentById(R.id.fragment_saved);
        mFragmentSpotDetail = (FragmentSpotDetail) mFmManager.findFragmentById(R.id.fragment_spot_detail);
        mFragmentInputMySpot = (FragmentInputMySpot) mFmManager.findFragmentById(R.id.fragment_input_myspot);
        mFragmentDuplicateSpot = (FragmentDuplicateSpot) mFmManager.findFragmentById(R.id.fragment_duplicate_spot);

        mFragmentUserDataTab.setListener(this);
        showFragment(FRAGMENT_HOME_TAB_ID, false, NOT_SHOW_FRAGMENT);
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

    private void showFragment(int fragment, boolean isback, int fragmentAnimationType) {
        mTransaction = hideFragment(fragmentAnimationType);
        switch (fragment) {
        case FRAGMENT_HOME_TAB_ID:
            mFragmentHomeTab.setBackStatus(isback);
            mTransaction.show(mFragmentHomeTab);
            break;
        case FRAGMENT_NETWORK_TAB_ID:
            mFragmentNetworkTab.setBackStatus(isback);
            mTransaction.show(mFragmentNetworkTab);
            break;
        case FRAGMENT_SEARCH_TAB_ID:
            mFragmentSearchTab.setBackStatus(isback);
            mTransaction.show(mFragmentSearchTab);
            break;
        case FRAGMENT_USER_DATA_TAB_ID:
            mFragmentUserDataTab.setBackStatus(isback);
            mTransaction.show(mFragmentUserDataTab);
            break;
        case FRAGMENT_RECENT_POST_DETAIL_ID:
            mFragmentRecentPostDetail.setBackStatus(isback);
            mTransaction.show(mFragmentRecentPostDetail);
            break;
        case FRAGMENT_MAP_VIEW_ID:
            mFragmentMapView.setBackStatus(isback);
            mTransaction.show(mFragmentMapView);
            break;
        case FRAGMENT_HOME_NOTIFICATION_ID:
            mFragmentHomeNotification.setBackStatus(isback);
            mTransaction.show(mFragmentHomeNotification);
            break;
        case FRAGMENT_SETTINGS_ID:
            mFragmentSetting.setBackStatus(isback);
            mTransaction.show(mFragmentSetting);
            break;
        case FRAGMENT_UPDATE_USER_INFO_ID:
            mFragmentUpdateUserInfo.setBackStatus(isback);
            mTransaction.show(mFragmentUpdateUserInfo);
            break;
        case FRAGMENT_TERM_OF_USE_ID:
            mFragmentTermOfUse.setBackStatus(isback);
            mTransaction.show(mFragmentTermOfUse);
            break;
        case FRAGMENT_COVER_IMAGE_ID:
            mFragmentCoverImage.setBackStatus(isback);
            mTransaction.show(mFragmentCoverImage);
            break;
        case FRAGMENT_SHARE_ID:
            mFragmentShare.setBackStatus(isback);
            mTransaction.show(mFragmentShare);
            break;
        case FRAGMENT_SHARE_WITH_FRIENDS_ID:
            mFragmentShareWithFriends.setBackStatus(isback);
            mTransaction.show(mFragmentShareWithFriends);
            break;
        case FRAGMENT_HISTORY_ID:
            mFragmentHistory.setBackStatus(isback);
            mTransaction.show(mFragmentHistory);
            break;
        case FRAGMENT_FOLLOWING_ID:
            mFragmentFollowing.setBackStatus(isback);
            mTransaction.show(mFragmentFollowing);
            break;
        case FRAGMENT_FOLLOWER_ID:
            mFragmentFollower.setBackStatus(isback);
            mTransaction.show(mFragmentFollower);
            break;
        case FRAGMENT_FAVOURITE_ID:
            mFragmentFavourite.setBackStatus(isback);
            mTransaction.show(mFragmentFavourite);
            break;
        case FRAGMENT_LIKED_ID:
            mFragmentLiked.setBackStatus(isback);
            mTransaction.show(mFragmentLiked);
            break;
        case FRAGMENT_SAVED_ID:
            mFragmentSaved.setBackStatus(isback);
            mTransaction.show(mFragmentSaved);
            break;
        case FRAGMENT_VIEW_IMAGE_ID:
            mFragmentViewImage.setBackStatus(isback);
            mTransaction.show(mFragmentViewImage);
            break;
        case FRAGMENT_SPOT_DETAIL_ID:
            mFragmentSpotDetail.setBackStatus(isback);
            mTransaction.show(mFragmentSpotDetail);
            break;
        case FRAGMENT_INPUT_MYSPOT_ID:
            mFragmentInputMySpot.setBackStatus(isback);
            mTransaction.show(mFragmentInputMySpot);
            break;
        case FRAGMENT_SPOT_DUPLICATE_ID:
            mFragmentDuplicateSpot.setBackStatus(isback);
            mTransaction.show(mFragmentDuplicateSpot);
            break;
        default:
            break;
        }
        if (!isback)
            addToSBackStack(fragment);
        mTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
        mTransaction.commit();
    }

    public FragmentTransaction hideFragment(int fragmentAnimationType) {
        mTransaction = mFmManager.beginTransaction();
        if (fragmentAnimationType == 1)
            mTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right);
        else if (fragmentAnimationType == 2)
            mTransaction.setCustomAnimations(R.anim.slide_out_left, R.anim.slide_in_left);
        mTransaction.hide(mFragmentHomeTab);
        mTransaction.hide(mFragmentNetworkTab);
        mTransaction.hide(mFragmentSearchTab);
        mTransaction.hide(mFragmentUserDataTab);
        mTransaction.hide(mFragmentRecentPostDetail);
        mTransaction.hide(mFragmentMapView);
        mTransaction.hide(mFragmentHomeNotification);
        mTransaction.hide(mFragmentSetting);
        mTransaction.hide(mFragmentUpdateUserInfo);
        mTransaction.hide(mFragmentTermOfUse);
        mTransaction.hide(mFragmentCoverImage);
        mTransaction.hide(mFragmentShare);
        mTransaction.hide(mFragmentShareWithFriends);
        mTransaction.hide(mFragmentLiked);
        mTransaction.hide(mFragmentSaved);
        mTransaction.hide(mFragmentViewImage);

        mTransaction.hide(mFragmentHistory);
        mTransaction.hide(mFragmentFollowing);
        mTransaction.hide(mFragmentFollower);
        mTransaction.hide(mFragmentFavourite);
        mTransaction.hide(mFragmentSpotDetail);
        mTransaction.hide(mFragmentDuplicateSpot);
        mTransaction.hide(mFragmentInputMySpot);

        return mTransaction;
    }

    public void addToSBackStack(int ID) {
        backstackID.add(ID);
    }

    @Override
    public void goToRecentPostDetail(ObBravo obGetBravo) {
        mFragmentRecentPostDetail.setBravoOb(obGetBravo);
        showFragment(FRAGMENT_RECENT_POST_DETAIL_ID, false, SHOW_ANIMATION_TO_LEFT);
    }

    public int getTopBackStack() {
        if (backstackID.size() > 0)
            return backstackID.get(backstackID.size() - 1);
        else
            return -1;
    }

    public void goToBack() {
        int currentIndex = backstackID.size() - 1;
        int previousView = -1;
        if (currentIndex > 0)
            previousView = backstackID.get(currentIndex - 1);
        try {
            backstackID.remove(currentIndex);
            if (backstackID.size() == 0) {
                onBackPressedToExit();
            }
        } catch (IndexOutOfBoundsException e) {
            onBackPressedToExit();
            return;
        }
        AIOLog.d("previousView:" + previousView);
        if (previousView > 0) {
            showFragment(previousView, true, SHOW_ANIMATION_TO_RIGHT);
        }
    }

    @Override
    public void goToFragment(int fragmentID) {
        showFragment(fragmentID, false, SHOW_ANIMATION_TO_LEFT);
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
        return false;

    }

    @Override
    public void showPageHomeNotification() {
        showFragment(FRAGMENT_HOME_NOTIFICATION_ID, false, SHOW_ANIMATION_TO_RIGHT);
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
        showFragment(FRAGMENT_TERM_OF_USE_ID, false, SHOW_ANIMATION_TO_LEFT);
    }

    @Override
    public void goToUserData(String userId) {
        mFragmentUserDataTab.setForeignID(userId);
        showFragment(FRAGMENT_USER_DATA_TAB_ID, false, SHOW_ANIMATION_TO_LEFT);
    }

    @Override
    public void goToMapView(double lat, double log, int locationType) {
        mFragmentMapView.setCordinate(lat, log);
        mFragmentMapView.setTypeMaker(locationType);
        showFragment(FRAGMENT_MAP_VIEW_ID, false, SHOW_ANIMATION_TO_LEFT);
    }

    @Override
    public void onClickUserAvatar(String userId) {
        mFragmentUserDataTab.setForeignID(userId);
        showFragment(FRAGMENT_USER_DATA_TAB_ID, false, SHOW_ANIMATION_TO_LEFT);

    }

    @Override
    public void goToShare(ObBravo bravoObj, int shareType) {
        mFragmentShare.setData(bravoObj, shareType);
        showFragment(FRAGMENT_SHARE_ID, false, SHOW_ANIMATION_TO_LEFT);
    }

    @Override
    public void goToCoverImage(ObBravo obGetBravo) {
        mFragmentCoverImage.setObBravo(obGetBravo);
        showFragment(FRAGMENT_COVER_IMAGE_ID, false, SHOW_ANIMATION_TO_LEFT);
    }

    @Override
    public void goToUsergFollowing(String foreignID) {
        mFragmentFollowing.setForeignID(foreignID);
        showFragment(FRAGMENT_FOLLOWING_ID, false, SHOW_ANIMATION_TO_LEFT);
    }

    @Override
    public void goToUsergFollower(String foreignID) {
        mFragmentFollower.setForeignID(foreignID);
        showFragment(FRAGMENT_FOLLOWER_ID, false, SHOW_ANIMATION_TO_LEFT);
    }

    @Override
    public void shareViaSNS(String snsType, ObBravo obBravo, String sharedText) {
        mObBravo = obBravo;
        mBravoId = mObBravo.Bravo_ID;
        mSharedSnsText = sharedText;
        // Check if already logged in
        if (BravoConstant.TWITTER.equals(snsType)) {
            shareViaTwitter(BravoConstant.TWITTER_CALLBACK_HOME_URL);
        } else if (BravoConstant.FACEBOOK.equals(snsType)) {
            shareViaFacebook();
        } else if (BravoConstant.LINE.equals(snsType)) {
            shareViaLINE();
        }
    }

    private void shareViaLINE() {
        PackageManager pm = getPackageManager();
        try {
            Intent waIntent = new Intent(Intent.ACTION_SEND);
            waIntent.setType("text/plain");
            String sharedText;
            String bravoUrl = BravoWebServiceConfig.URL_BRAVO_ID_DETAIL.replace("{Bravo_ID}", mBravoId);
            sharedText = mSharedSnsText + " \n" + bravoUrl;

            PackageInfo info = pm.getPackageInfo("jp.naver.line.android", PackageManager.GET_META_DATA);
            if (info == null) {
            }
            // Check if package exists or not. If not then code
            // in catch block will be called
            waIntent.setPackage("jp.naver.line.android");
            waIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
            waIntent.putExtra(Intent.EXTRA_TEXT, sharedText);
            startActivity(Intent.createChooser(waIntent, getString(R.string.bravo_after_share_sns)));
        } catch (NameNotFoundException e) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://details?id=jp.naver.line.android"));
                startActivity(intent);
            } catch (Exception exception) { // google play app is not installed
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=jp.naver.line.android"));
                startActivity(intent);
            }
        }

        goToBack();
    }

    private void shareViaFacebook() {

    }

    private void shareViaTwitter(String urlCallback) {
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        if (!BravoUtils.isTwitterLoggedInAlready(this)) {
            ConfigurationBuilder builder = new ConfigurationBuilder();
            builder.setOAuthConsumerKey(BravoConstant.TWITTER_CONSUMER_KEY);
            builder.setOAuthConsumerSecret(BravoConstant.TWITTER_CONSUMER_SECRET);
            Configuration configuration = builder.build();

            TwitterFactory factory = new TwitterFactory(configuration);
            mTwitter = factory.getInstance();

            try {
                mTwitterRequestToken = mTwitter.getOAuthRequestToken(urlCallback);
                this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mTwitterRequestToken.getAuthenticationURL())));
                finish();
            } catch (TwitterException e) {
                e.printStackTrace();
            }
        } else {
            // user already logged into twitter
            if (BravoConstant.TWITTER_CALLBACK_HOME_URL.equals(urlCallback))
                requestToGetTwitterUserInfo(mBravoId, mSharedSnsText);
        }
    }

    private void requestToGetTwitterUserInfo(String bravoId, String sharedText) {

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
        if (user == null) {
            AIOLog.e("user twitter is null");
            return;
        }
        String bravoUrl = BravoWebServiceConfig.URL_BRAVO_ID_DETAIL.replace("{Bravo_ID}", mBravoId);
        // update twitter status
        new UpdateTwitterStatus().execute(sharedText + " \n" + bravoUrl);
    }

    /**
     * Function to update status
     * */
    class UpdateTwitterStatus extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(HomeActivity.this);
            pDialog.setMessage("Updating to twitter...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting Places JSON
         * */
        protected String doInBackground(String... args) {
            Log.d("Tweet Text", "> " + args[0]);
            String status = args[0];
            try {
                ConfigurationBuilder builder = new ConfigurationBuilder();
                builder.setOAuthConsumerKey(BravoConstant.TWITTER_CONSUMER_KEY);
                builder.setOAuthConsumerSecret(BravoConstant.TWITTER_CONSUMER_SECRET);

                // Access Token
                String access_token = BravoSharePrefs.getInstance(HomeActivity.this).getStringValue(BravoConstant.PREF_KEY_TWITTER_OAUTH_TOKEN);
                String access_token_secret = BravoSharePrefs.getInstance(HomeActivity.this)
                        .getStringValue(BravoConstant.PREF_KEY_TWITTER_OAUTH_SCRET);

                AccessToken accessToken = new AccessToken(access_token, access_token_secret);
                Twitter twitter = new TwitterFactory(builder.build()).getInstance(accessToken);

                // Update status
                twitter4j.Status response = twitter.updateStatus(status);

                Log.d("Status", "> " + response.getText());
            } catch (TwitterException e) {
                // Error in updating status
                Log.d("Twitter Update Error", e.getMessage());
            }
            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog and show
         * the data in UI Always use runOnUiThread(new Runnable()) to update UI
         * from background thread, otherwise you will get error
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String shareTwDone = getString(R.string.share_complete_twitter);
                    Toast.makeText(getApplicationContext(), shareTwDone, Toast.LENGTH_SHORT).show();
                    goToBack();
                }
            });
        }
    }

    @Override
    public void goToUserTimeLine(ObGetUserInfo userInfo) {
        mFragmentHistory.setmUserInfo(userInfo);
        showFragment(FRAGMENT_HISTORY_ID, false, SHOW_ANIMATION_TO_LEFT);
    }

    private enum PendingAction {
        NONE, POST_PHOTO, POST_STATUS_UPDATE
    }

    @Override
    public void goToMapView(String foreignID, int locationType, String fullName) {
        // mFragmentMapView = new FragmentMapView();
        mFragmentMapView.setForeignID(foreignID);
        mFragmentMapView.setTypeMaker(locationType);
        mFragmentMapView.setFullName(fullName);
        showFragment(FRAGMENT_MAP_VIEW_ID, false, SHOW_ANIMATION_TO_LEFT);
    }

    @Override
    public void goToLiked(String mSpotID) {
        mFragmentLiked.setSpotID(mSpotID);
        showFragment(FRAGMENT_LIKED_ID, false, SHOW_ANIMATION_TO_LEFT);
    }

    @Override
    public void goToSaved(String mSpotID) {
        mFragmentSaved.setSpotID(mSpotID);
        showFragment(FRAGMENT_SAVED_ID, false, SHOW_ANIMATION_TO_LEFT);
    }

    @Override
    public void goToViewImage(ObGetUserInfo obGetUserInfo, int userImageType) {
        mFragmentViewImage.setObGetUserInfo(obGetUserInfo, userImageType);
        showFragment(FRAGMENT_VIEW_IMAGE_ID, false, SHOW_ANIMATION_TO_LEFT);
    }

    @Override
    public void goToSpotDetail(Spot mSpot) {
        mFragmentSpotDetail.setSpot(mSpot);
        showFragment(FRAGMENT_SPOT_DETAIL_ID, false, SHOW_ANIMATION_TO_LEFT);
    }

    @Override
    public void goToAddSpot() {
        showFragment(FRAGMENT_INPUT_MYSPOT_ID, false, SHOW_ANIMATION_TO_LEFT);
    }

    @Override
    public void goToInputMySpot() {
        showFragment(FRAGMENT_INPUT_MYSPOT_ID, false, SHOW_ANIMATION_TO_LEFT);
    }

    @Override
    public void goToLocateMySpot(double lat, double log, int locationType) {
        mFragmentMapView.setCordinate(lat, log);
        mFragmentMapView.setTypeMaker(locationType);
        showFragment(FRAGMENT_MAP_VIEW_ID, false, SHOW_ANIMATION_TO_LEFT);
    }

    @Override
    public void requestToLoginSNS(String snsType) {
        if (BravoConstant.TWITTER.equals(snsType)) {
            ConfigurationBuilder builder = new ConfigurationBuilder();
            builder.setOAuthConsumerKey(BravoConstant.TWITTER_CONSUMER_KEY);
            builder.setOAuthConsumerSecret(BravoConstant.TWITTER_CONSUMER_SECRET);
            Configuration configuration = builder.build();

            TwitterFactory factory = new TwitterFactory(configuration);
            mTwitter = factory.getInstance();

            try {
                mTwitterRequestToken = mTwitter.getOAuthRequestToken(BravoConstant.TWITTER_CALLBACK_SETTING_URL);
                this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mTwitterRequestToken.getAuthenticationURL())));
                finish();
            } catch (TwitterException e) {
                e.printStackTrace();
            }
        }
    }

    private void onBackPressedToExit() {
        AIOLog.d("onBackPressed:" + mBackPressedToExitOnce);
        if (mBackPressedToExitOnce) {
            ObGetAllBravoRecentPosts obGetAllBravoRecentPosts = mFragmentHomeTab.getRecentPostData();
            BravoUtils.saveDataBeforeExit(this, obGetAllBravoRecentPosts);
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

    @Override
    public void putSNS(final SNS sns) {
        BravoRequestManager.getInstance(this).putSNS(this, sns, new IRequestListener() {
            
            @Override
            public void onResponse(String response) {
                AIOLog.d("putSNS onResponse:"+response);
            }
            
            @Override
            public void onErrorResponse(String errorMessage) {
                AIOLog.d("putSNS onErrorResponse:"+errorMessage);
            }
        });
    }

    @Override
    public void deleteSNS(String foreignID) {
        BravoRequestManager.getInstance(this).deleteSNS(foreignID, new IRequestListener() {

            @Override
            public void onResponse(String response) {
                AIOLog.d("deleteSNS response:"+response);
            }

            @Override
            public void onErrorResponse(String errorMessage) {
                AIOLog.d("response error");
            }
        });
    }

    @Override
    public void goToMapView(final Spot mSpot, int makerByLocationSpot) {
        BravoRequestManager.getInstance(this).getNumberAllowBravo(this, new IRequestListener() {

            @Override
            public void onResponse(String response) {
                Gson gson = new GsonBuilder().serializeNulls().create();
                ObAllowBravo obAllowBravo = gson.fromJson(response.toString(), ObAllowBravo.class);
                if (obAllowBravo == null)
                    return;
                AIOLog.d("obAllowBravo.data.Allow_Bravo:" + obAllowBravo.data.Allow_Bravo);
                int numberAllowBravo = obAllowBravo.data.Allow_Bravo;
                if (numberAllowBravo > 0) {
                    String snsListJSON = gson.toJson(mSpot);
                    Intent bravoIntent = new Intent(HomeActivity.this, ActivityBravoChecking.class);
                    bravoIntent.putExtra(BravoConstant.EXTRA_SPOT_JSON, snsListJSON);
                    startActivityForResult(bravoIntent, REQUEST_CODE_TAP_HERE_BRAVO);
                    overridePendingTransition(R.anim.slide_in_up, R.anim.fade_in);
                } else {
                    DialogUtility.showDialogSpentBravoADay(HomeActivity.this);
                }
            }

            @Override
            public void onErrorResponse(String errorMessage) {

            }
        }, mFragmentHomeTab);
    }

    /***
     * GCM only
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void registerInBackground() {
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object... params) {
                String msg = "";
                try {
                    if (mGoogleCloudMessaging == null) {
                        mGoogleCloudMessaging = GoogleCloudMessaging.getInstance(HomeActivity.this);
                    }
                    mRegisterId = mGoogleCloudMessaging.register(BravoConstant.GCM_SENDER_ID);
                    msg = "Device registered, registration ID=" + mRegisterId;
                    AIOLog.d("registerInBackground regid:" + mRegisterId);

                    storeRegistrationId(HomeActivity.this, mRegisterId);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                }
                return msg;
            }
        }.execute(null, null, null);
    }

    /**
     * Stores the registration ID and app versionCode in the application's {@code SharedPreferences}.
     * 
     * @param context
     *            application's context.
     * @param regId
     *            registration ID
     */
    private void storeRegistrationId(Context context, String regId) {
        int appVersion = getAppVersion(context);
        BravoSharePrefs.getInstance(context).putStringValue(BravoConstant.PROPERTY_REG_ID, regId);
        BravoSharePrefs.getInstance(context).putIntValue(BravoConstant.PROPERTY_APP_VERSION, appVersion);
    }

    /**
     * Gets the current registration ID for application on GCM service.
     * <p>
     * If result is empty, the app needs to register.
     * 
     * @return registration ID, or empty string if there is no existing
     *         registration ID.
     */
    private String getRegistrationId(Context context) {
        String registrationId = BravoSharePrefs.getInstance(context).getStringValue(BravoConstant.PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            return "";
        }
        int registeredVersion = BravoSharePrefs.getInstance(context).getIntValue(BravoConstant.PROPERTY_APP_VERSION);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            return "";
        }
        return registrationId;
    }

    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    /**
     * Receiving push messages
     * */
    private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
                                                               @Override
                                                               public void onReceive(Context context, Intent intent) {
                                                                   String alert = intent.getExtras().getString("alert");
                                                                   String badge = intent.getExtras().getString("badge");
                                                                   String source = intent.getExtras().getString("source");
                                                                   // Waking up mobile if it is sleeping
                                                                   WakeLocker.acquire(getApplicationContext());

                                                                   /**
                                                                    * Take appropriate action on this message depending upon your app
                                                                    * requirement For now i am just displaying it on the screen
                                                                    * */

                                                                   // Showing received message
                                                                   mFragmentHomeTab.updateNotification(badge, alert, source);
                                                                   // Releasing wake lock
                                                                   WakeLocker.release();
                                                               }
                                                           };

    @Override
    public void goToDuplicateSpot(Spot mSpot, OFPostVenue mOPostVenue) {
        mFragmentDuplicateSpot.setSpot(mSpot);
        mFragmentDuplicateSpot.setOFPostVenue(mOPostVenue);
        showFragment(FRAGMENT_SPOT_DUPLICATE_ID, false, SHOW_ANIMATION_TO_LEFT);
    }

    @Override
    public void requestUpdateUserInfo() {
        goToBack();
        mFragmentUserDataTab.updateAllInformation();
    }

    @Override
    public void requestBravoDetailInfo() {
        goToBack();
        mFragmentRecentPostDetail.updateInfo();
    }
}
