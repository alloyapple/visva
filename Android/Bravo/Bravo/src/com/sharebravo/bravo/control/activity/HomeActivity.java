package com.sharebravo.bravo.control.activity;

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
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
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
import com.sharebravo.bravo.MyApplication;
import com.sharebravo.bravo.R;
import com.sharebravo.bravo.model.SessionLogin;
import com.sharebravo.bravo.model.response.ObBravo;
import com.sharebravo.bravo.model.response.ObGetUserInfo;
import com.sharebravo.bravo.model.response.Spot;
import com.sharebravo.bravo.sdk.log.AIOLog;
import com.sharebravo.bravo.utils.BravoConstant;
import com.sharebravo.bravo.utils.BravoSharePrefs;
import com.sharebravo.bravo.utils.BravoUtils;
import com.sharebravo.bravo.utils.StringUtility;
import com.sharebravo.bravo.view.fragment.home.FragmentAddMySpot;
import com.sharebravo.bravo.view.fragment.home.FragmentAfterBravo;
import com.sharebravo.bravo.view.fragment.home.FragmentBravoDetail;
import com.sharebravo.bravo.view.fragment.home.FragmentCoverImage;
import com.sharebravo.bravo.view.fragment.home.FragmentHomeNotification;
import com.sharebravo.bravo.view.fragment.home.FragmentHomeNotification.IClosePageHomeNotification;
import com.sharebravo.bravo.view.fragment.home.FragmentHomeTab;
import com.sharebravo.bravo.view.fragment.home.FragmentHomeTab.IShowPageHomeNotification;
import com.sharebravo.bravo.view.fragment.home.FragmentInputMySpot;
import com.sharebravo.bravo.view.fragment.home.FragmentLiked;
import com.sharebravo.bravo.view.fragment.home.FragmentLocateMySpot;
import com.sharebravo.bravo.view.fragment.home.FragmentMapView;
import com.sharebravo.bravo.view.fragment.home.FragmentNetworkTab;
import com.sharebravo.bravo.view.fragment.home.FragmentSaved;
import com.sharebravo.bravo.view.fragment.home.FragmentSearchTab;
import com.sharebravo.bravo.view.fragment.home.FragmentShare;
import com.sharebravo.bravo.view.fragment.home.FragmentSpotDetail;
import com.sharebravo.bravo.view.fragment.setting.FragmentSetting;
import com.sharebravo.bravo.view.fragment.setting.FragmentSetting.IShowPageTermOfUse;
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

public class HomeActivity extends VisvaAbstractFragmentActivity implements HomeActionListener, IShowPageHomeNotification, IClosePageHomeNotification,
        IShowPageSettings, IShowPageTermOfUse {

    // ======================Constant Define===============
    private static final String      PENDING_ACTION_BUNDLE_KEY      = "com.sharebravo.bravo:PendingAction";
    private static final String      FRAGMENT_HOME_TAB              = "home_tab";
    private static final String      FRAGMENT_NETWORK_TAB           = "network_tab";
    private static final String      FRAGMENT_SEARCH_TAB            = "search_tab";
    private static final String      FRAGMENT_USER_DATA_TAB         = "user_data_tab";

    private static final String      FRAGMENT_RECENT_POST_DETAIL    = "post_detail";
    private static final String      FRAGMENT_MAP_VIEW              = "map_view";

    private static final String      FRAGMENT_HOME_NOTIFICATION     = "notification";
    private static final String      FRAGMENT_SETTINGS              = "settings";
    private static final String      FRAGMENT_UPDATE_USER_INFO      = "update_user_info";
    private static final String      FRAGMENT_TERM_OF_USE           = "term_of_use";
    private static final String      FRAGMENT_COVER_IMAGE           = "cover_image";
    private static final String      FRAGMENT_SHARE                 = "page_share";
    private static final String      FRAGMENT_SHARE_WITH_FRIENDS    = "page_settings_share_with_friends";
    private static final String      FRAGMENT_HISTORY               = "page_history";
    private static final String      FRAGMENT_FOLOWING              = "page_following";
    private static final String      FRAGMENT_FOLLOWER              = "page_follower";
    private static final String      FRAGMENT_FAVOURITE             = "favourite";
    private static final String      FRAGMENT_LIKED                 = "liked";
    private static final String      FRAGMENT_SAVED                 = "saved";
    private static final String      FRAGMENT_VIEW_IMAGE            = "view_image";
    private static final String      FRAGMENT_SPOT_DETAIL           = "spot_detail";
    private static final String      FRAGMENT_ADD_MYSPOT            = "add_my_spot";
    private static final String      FRAGMENT_INPUT_MYSPOT          = "input_my_spot";
    private static final String      FRAGMENT_LOCATE_MYSPOT         = "loate_my_spot";
    private static final String      FRAGMENT_AFTER_BRAVO           = "after_bravo";

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
    public static final int          FRAGMENT_ADD_MYSPOT_ID         = FRAGMENT_BASE_ID + 23;
    public static final int          FRAGMENT_INPUT_MYSPOT_ID       = FRAGMENT_BASE_ID + 24;
    public static final int          FRAGMENT_LOCATE_MYSPOT_ID      = FRAGMENT_BASE_ID + 25;
    public static final int          FRAGMENT_AFTER_BRAVO_ID        = FRAGMENT_BASE_ID + 26;

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
    private FragmentAddMySpot        mFragmentAddMySpot;
    private FragmentInputMySpot      mFragmentInputMySpot;
    private FragmentLocateMySpot     mFragmentLocateMySpot;
    private FragmentAfterBravo       mFragmentAfterBravo;

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
    private static String            mSharedSnsText;
    // ======================Variable Define===============
    private ArrayList<String>        backstack                      = new ArrayList<String>();
    private ArrayList<Integer>       backstackID                    = new ArrayList<Integer>();

    private UiLifecycleHelper        mUiLifecycleHelper;
    private Session.StatusCallback   mFacebookCallback;
    private PendingAction            mPendingAction                 = PendingAction.NONE;

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
        final int _loginBravoViaType = BravoSharePrefs.getInstance(this).getIntValue(BravoConstant.PREF_KEY_SESSION_LOGIN_BRAVO_VIA_TYPE);
        SessionLogin _sessionLogin = BravoUtils.getSession(this, _loginBravoViaType);
        userId = _sessionLogin.userID;
        accessToken = _sessionLogin.accessToken;
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
        if (!isTwitterLoggedInAlready()) {
            Uri uri = getIntent().getData();
            if (uri != null
                    && (uri.toString().startsWith(BravoConstant.TWITTER_CALLBACK_HOME_URL) || uri.toString().startsWith(
                            BravoConstant.TWITTER_CALLBACK_SETTING_URL))) {
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
                    if (user == null) {
                        AIOLog.e("user twitter to share is null");
                        return;
                    }
                    AIOLog.d("uri:" + uri);
                    if (uri.toString().startsWith(BravoConstant.TWITTER_CALLBACK_HOME_URL)) {
                        // Check for blank text
                        if (StringUtility.isEmpty(mObBravo.Last_Pic)) {
                            new UpdateTwitterStatus().execute(mSharedSnsText);
                        } else {
                            new UpdateTwitterStatus().execute(mSharedSnsText + " \n" + mObBravo.Last_Pic);
                            Toast.makeText(getApplicationContext(), "Please enter status message", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        mFragmentSetting.setLoginedTwitter(true);
                        goToFragment(FRAGMENT_SETTINGS_ID);
                    }
                } catch (Exception e) {
                    // Check log for login errors
                    Log.e("Twitter Login Error", "> " + e.getMessage());
                }
            }
        }
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
            Intent bravoIntent = new Intent(HomeActivity.this, ActivityBravoChecking.class);
            startActivity(bravoIntent);
            overridePendingTransition(R.anim.slide_in_up, R.anim.fade_in);
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
            mFragmentUserDataTab.setForeignID(userId);
            btnMyData.setBackgroundResource(R.drawable.tab_mydata_on);
            txtMyData.setTextColor(Color.WHITE);
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
        mFragmentAddMySpot = (FragmentAddMySpot) mFmManager.findFragmentById(R.id.fragment_add_myspot);
        mFragmentInputMySpot = (FragmentInputMySpot) mFmManager.findFragmentById(R.id.fragment_input_myspot);
        mFragmentLocateMySpot = (FragmentLocateMySpot) mFmManager.findFragmentById(R.id.fragment_locate_myspot);
        mFragmentAfterBravo = (FragmentAfterBravo) mFmManager.findFragmentById(R.id.fragment_after_bravo);

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
            hideTabButton();
            btnHome.setBackgroundResource(R.drawable.tab_home_on);
            txtHome.setTextColor(Color.WHITE);
            break;

        case FRAGMENT_NETWORK_TAB_ID:
            mTransaction.show(mFragmentNetworkTab);
            addToSBackStack(FRAGMENT_NETWORK_TAB);
            hideTabButton();
            btnNetwork.setBackgroundResource(R.drawable.tab_home_on);
            txtNetwork.setTextColor(Color.WHITE);
            break;
        case FRAGMENT_SEARCH_TAB_ID:
            mTransaction.show(mFragmentSearchTab);
            addToSBackStack(FRAGMENT_SEARCH_TAB);
            hideTabButton();
            btnSearch.setBackgroundResource(R.drawable.tab_home_on);
            txtSearch.setTextColor(Color.WHITE);
            break;
        case FRAGMENT_USER_DATA_TAB_ID:
            mTransaction.show(mFragmentUserDataTab);
            addToSBackStack(FRAGMENT_USER_DATA_TAB);
            hideTabButton();
            btnMyData.setBackgroundResource(R.drawable.tab_home_on);
            txtMyData.setTextColor(Color.WHITE);
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
            mTransaction.show(mFragmentUpdateUserInfo);
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
        case FRAGMENT_SHARE_ID:
            mTransaction.show(mFragmentShare);
            addToSBackStack(FRAGMENT_SHARE);
            break;
        case FRAGMENT_SHARE_WITH_FRIENDS_ID:
            mTransaction.show(mFragmentShareWithFriends);
            addToSBackStack(FRAGMENT_SHARE_WITH_FRIENDS);
            break;

        case FRAGMENT_HISTORY_ID:
            mTransaction.show(mFragmentHistory);
            addToSBackStack(FRAGMENT_HISTORY);
            break;
        case FRAGMENT_FOLLOWING_ID:
            mTransaction.show(mFragmentFollowing);
            addToSBackStack(FRAGMENT_FOLOWING);
            break;
        case FRAGMENT_FOLLOWER_ID:
            mTransaction.show(mFragmentFollower);
            addToSBackStack(FRAGMENT_FOLLOWER);
            break;

        case FRAGMENT_FAVOURITE_ID:
            mTransaction.show(mFragmentFavourite);
            addToSBackStack(FRAGMENT_FAVOURITE);
            break;
        case FRAGMENT_LIKED_ID:
            mTransaction.show(mFragmentLiked);
            addToSBackStack(FRAGMENT_LIKED);
            break;

        case FRAGMENT_SAVED_ID:
            mTransaction.show(mFragmentSaved);
            addToSBackStack(FRAGMENT_SAVED);
            break;
        case FRAGMENT_VIEW_IMAGE_ID:
            mTransaction.show(mFragmentViewImage);
            addToSBackStack(FRAGMENT_VIEW_IMAGE);
            break;
        case FRAGMENT_SPOT_DETAIL_ID:
            mTransaction.show(mFragmentSpotDetail);
            addToSBackStack(FRAGMENT_SPOT_DETAIL);
            break;
        case FRAGMENT_ADD_MYSPOT_ID:
            mTransaction.show(mFragmentAddMySpot);
            addToSBackStack(FRAGMENT_ADD_MYSPOT);
            break;
        case FRAGMENT_INPUT_MYSPOT_ID:
            mTransaction.show(mFragmentInputMySpot);
            addToSBackStack(FRAGMENT_INPUT_MYSPOT);
            break;
        case FRAGMENT_LOCATE_MYSPOT_ID:
            mTransaction.show(mFragmentLocateMySpot);
            addToSBackStack(FRAGMENT_LOCATE_MYSPOT);
            break;
        case FRAGMENT_AFTER_BRAVO_ID:
            mTransaction.show(mFragmentAfterBravo);
            addToSBackStack(FRAGMENT_AFTER_BRAVO);
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
        mTransaction.hide(mFragmentAddMySpot);
        mTransaction.hide(mFragmentInputMySpot);
        mTransaction.hide(mFragmentLocateMySpot);
        mTransaction.hide(mFragmentAfterBravo);

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

    public void addToSBackStackID(int ID) {

        int index = backstackID.lastIndexOf(ID);
        if (index == -1) {
            backstackID.add(ID);
            return;
        }
        try {
            if (!backstackID.get(index - 1).equals(
                    backstackID.get(backstackID.size() - 1))) {
                backstackID.add(ID);
                return;
            }
        } catch (IndexOutOfBoundsException e) {

        }
        try {
            ArrayList<Integer> subStack = new ArrayList<Integer>(backstackID);
            for (int i = 0; i < subStack.size(); i++) {
                if (i > index) {
                    backstackID.remove(index);
                }
            }
        } catch (IndexOutOfBoundsException e) {
        }
    }

    @Override
    public void goToRecentPostDetail(ObBravo obGetBravo) {
        if (obGetBravo == null)
            return;
        AIOLog.d("obGetBravo:" + obGetBravo);
        mFragmentRecentPostDetail.setBravoOb(obGetBravo);
        showFragment(FRAGMENT_RECENT_POST_DETAIL_ID);
        // btnHome.setBackgroundResource(R.drawable.tab_home_on);
    }

    @Override
    public void goToBack() {
        AIOLog.d("mBackstack=" + backstack);
        String currentView = null;
        if (backstack.size() - 1 > 0)
            currentView = backstack.get(backstack.size() - 1);
        String previousView = null;
        if (backstack.size() - 2 > 0)
            previousView = backstack.get(backstack.size() - 2);
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
        if (currentView.equals(FRAGMENT_RECENT_POST_DETAIL) && previousView != null) {
            if (previousView.equals(FRAGMENT_HOME_TAB))
                mTransaction.show(mFragmentHomeTab);
            else if (previousView.equals(FRAGMENT_USER_DATA_TAB))
                mTransaction.show(mFragmentUserDataTab);
            else if (previousView.equals(FRAGMENT_HISTORY))
                mTransaction.show(mFragmentHistory);
            else if (previousView.equals(FRAGMENT_NETWORK_TAB))
                mTransaction.show(mFragmentNetworkTab);
            else if (previousView.equals(FRAGMENT_SPOT_DETAIL))
                mTransaction.show(mFragmentSpotDetail);
            else if (previousView.equals(FRAGMENT_MAP_VIEW)) {
                mTransaction.show(mFragmentMapView);
            }

        } else if (currentView.equals(FRAGMENT_USER_DATA_TAB) && previousView != null) {
            if (previousView.equals(FRAGMENT_RECENT_POST_DETAIL))
                mTransaction.show(mFragmentRecentPostDetail);
            else if (previousView.equals(FRAGMENT_HOME_TAB)) {
                mTransaction.show(mFragmentHomeTab);
            } else if (previousView.equals(FRAGMENT_NETWORK_TAB)) {
                mTransaction.show(mFragmentNetworkTab);
            } else if (previousView.equals(FRAGMENT_SPOT_DETAIL)) {
                mTransaction.show(mFragmentSpotDetail);
            }else if (previousView.equals(FRAGMENT_FOLOWING)) {
                mTransaction.show(mFragmentFollowing);
            }else if (previousView.equals(FRAGMENT_FOLLOWER)) {
                mTransaction.show(mFragmentFollower);
            }

        } else if (currentView.equals(FRAGMENT_MAP_VIEW)) {
            if (previousView != null && previousView.equals(FRAGMENT_RECENT_POST_DETAIL))
                mTransaction.show(mFragmentRecentPostDetail);
            else if (previousView != null && previousView.equals(FRAGMENT_USER_DATA_TAB))
                mTransaction.show(mFragmentUserDataTab);
            else if (previousView != null && previousView.equals(FRAGMENT_SPOT_DETAIL))
                mTransaction.show(mFragmentSpotDetail);
        } else if (currentView.equals(FRAGMENT_COVER_IMAGE) || currentView.equals(FRAGMENT_SHARE)) {
            mTransaction.show(mFragmentRecentPostDetail);
        } else if (currentView.equals(FRAGMENT_SETTINGS) || currentView.equals(FRAGMENT_FAVOURITE)) {
            mTransaction.show(mFragmentUserDataTab);
            mFragmentUserDataTab.getUserInfo("");
        } else if (currentView.equals(FRAGMENT_TERM_OF_USE) || currentView.equals(FRAGMENT_UPDATE_USER_INFO)
                || currentView.equals(FRAGMENT_SHARE_WITH_FRIENDS)) {
            mTransaction.show(mFragmentSetting);
        } else if (currentView.equals(FRAGMENT_HISTORY) || currentView.equals(FRAGMENT_FOLOWING) || currentView.equals(FRAGMENT_FOLLOWER)
                || currentView.equals(FRAGMENT_VIEW_IMAGE)) {
            mTransaction.show(mFragmentUserDataTab);

        } else if (currentView.equals(FRAGMENT_LIKED) || currentView.equals(FRAGMENT_SAVED)) {
            mTransaction.show(mFragmentRecentPostDetail);
        } else if (currentView.equals(FRAGMENT_SPOT_DETAIL)) {
            mTransaction.show(mFragmentSearchTab);
        } else if (currentView.equals(FRAGMENT_ADD_MYSPOT) || currentView.equals(FRAGMENT_INPUT_MYSPOT)) {
            mTransaction.show(mFragmentSearchTab);
        } else if (currentView.equals(FRAGMENT_LOCATE_MYSPOT)) {
            mTransaction.show(mFragmentInputMySpot);
        } else if (currentView.equals(FRAGMENT_HOME_NOTIFICATION)) {
            mTransaction.show(mFragmentHomeTab);
        } else if (currentView.equals(FRAGMENT_HOME_TAB) || currentView.equals(FRAGMENT_NETWORK_TAB)
                || currentView.equals(FRAGMENT_SEARCH_TAB)) {
            super.onBackPressed();
            return;
        }

        mTransaction.commitAllowingStateLoss();

    }

    @Override
    public void goToFragment(int fragmentID) {
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
        // mFragmentHomeNotification.onRequestListHomeNotification();
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
        mFragmentUserDataTab.setForeignID(userId);
        showFragment(FRAGMENT_USER_DATA_TAB_ID);
    }

    @Override
    public void goToMapView(String lat, String log, int locationType) {
        if (lat != null && log != null)
            mFragmentMapView.setCordinate(lat, log);
        mFragmentMapView.setTypeMaker(locationType);
        showFragment(FRAGMENT_MAP_VIEW_ID);
    }

    @Override
    public void onClickUserAvatar(String userId) {
        mFragmentUserDataTab.setForeignID(userId);
        showFragment(FRAGMENT_USER_DATA_TAB_ID);

    }

    @Override
    public void goToShare(ObBravo bravoObj, int shareType) {
        mFragmentShare.setData(bravoObj, shareType);
        showFragment(FRAGMENT_SHARE_ID);
    }

    @Override
    public void goToCoverImage(ObBravo obGetBravo) {
        mFragmentCoverImage.setObBravo(obGetBravo);
        showFragment(FRAGMENT_COVER_IMAGE_ID);
    }

    @Override
    public void goToUsergFollowing(String foreignID) {
        mFragmentFollowing.setForeignID(foreignID);
        showFragment(FRAGMENT_FOLLOWING_ID);
    }

    @Override
    public void goToUsergFollower(String foreignID) {
        mFragmentFollower.setForeignID(foreignID);
        showFragment(FRAGMENT_FOLLOWER_ID);
    }

    @Override
    public void shareViaSNS(String snsType, ObBravo obBravo, String sharedText) {
        mObBravo = obBravo;
        mSharedSnsText = sharedText;
        // Check if already logged in
        if (BravoConstant.TWITTER.equals(snsType)) {
            shareViaTwitter();
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
            if (StringUtility.isEmpty(mObBravo.Last_Pic)) {
                sharedText = mSharedSnsText;
            } else
                sharedText = mSharedSnsText + ":\n " + mObBravo.Last_Pic;

            PackageInfo info = pm.getPackageInfo("jp.naver.line.android", PackageManager.GET_META_DATA);
            if (info == null) {
            }
            // Check if package exists or not. If not then code
            // in catch block will be called
            waIntent.setPackage("jp.naver.line.android");
            waIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
            waIntent.putExtra(Intent.EXTRA_TEXT, sharedText);
            startActivity(Intent.createChooser(waIntent, getString(R.string.share)));
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

    private void shareViaTwitter() {
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
                mTwitterRequestToken = mTwitter.getOAuthRequestToken(BravoConstant.TWITTER_CALLBACK_HOME_URL);
                this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mTwitterRequestToken.getAuthenticationURL())));
                finish();
            } catch (TwitterException e) {
                e.printStackTrace();
            }
        } else {
            // user already logged into twitter
            requestToGetTwitterUserInfo(mObBravo, mSharedSnsText);
        }
    }

    private void requestToGetTwitterUserInfo(ObBravo obBravo, String sharedText) {

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
        // Call update status function
        // Get the status from EditText

        // Check for blank text
        if (sharedText.trim().length() > 0) {
            // update status
            new UpdateTwitterStatus().execute(sharedText);
        } else {
            // EditText is empty
            Toast.makeText(getApplicationContext(),
                    "Please enter status message", Toast.LENGTH_SHORT)
                    .show();
        }

        AIOLog.d("accessToken.getTokenSecret():" + accessToken.getTokenSecret() + ",accessToken.getToken():" + accessToken.getToken());

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
                    Toast.makeText(getApplicationContext(), "Status tweeted successfully", Toast.LENGTH_SHORT).show();
                    goToBack();
                }
            });
        }
    }

    @Override
    public void goToUserTimeLine(ObGetUserInfo userInfo) {
        mFragmentHistory.setmUserInfo(userInfo);
        showFragment(FRAGMENT_HISTORY_ID);
    }

    private enum PendingAction {
        NONE, POST_PHOTO, POST_STATUS_UPDATE
    }

    @Override
    public void goToMapView(String foreignID, int locationType, String fullName) {
        mFragmentMapView.setForeignID(foreignID);
        mFragmentMapView.setTypeMaker(locationType);
        mFragmentMapView.setFullName(fullName);
        showFragment(FRAGMENT_MAP_VIEW_ID);
    }

    @Override
    public void goToLiked(String mSpotID) {
        mFragmentLiked.setSpotID(mSpotID);
        showFragment(FRAGMENT_LIKED_ID);
    }

    @Override
    public void goToSaved(String mSpotID) {
        mFragmentSaved.setSpotID(mSpotID);
        showFragment(FRAGMENT_SAVED_ID);
    }

    @Override
    public void goToViewImage(ObGetUserInfo obGetUserInfo, int userImageType) {
        mFragmentViewImage.setObGetUserInfo(obGetUserInfo, userImageType);
        showFragment(FRAGMENT_VIEW_IMAGE_ID);
    }

    @Override
    public void goToSpotDetail(Spot mSpot) {
        mFragmentSpotDetail.setSpot(mSpot);
        showFragment(FRAGMENT_SPOT_DETAIL_ID);
    }

    @Override
    public void goToAddSpot() {
        showFragment(FRAGMENT_ADD_MYSPOT_ID);
    }

    @Override
    public void goToInputMySpot() {
        showFragment(FRAGMENT_INPUT_MYSPOT_ID);
    }

    @Override
    public void goToLocateMySpot() {
        showFragment(FRAGMENT_LOCATE_MYSPOT_ID);
    }

    @Override
    public void goToAfterBravo() {
        // TODO Auto-generated method stub
        showFragment(FRAGMENT_AFTER_BRAVO_ID);
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

}
