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
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
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

import com.sharebravo.bravo.MyApplication;
import com.sharebravo.bravo.R;
import com.sharebravo.bravo.model.response.ObBravo;
import com.sharebravo.bravo.sdk.log.AIOLog;
import com.sharebravo.bravo.utils.BravoConstant;
import com.sharebravo.bravo.utils.BravoSharePrefs;
import com.sharebravo.bravo.view.fragment.home.FragmentBravoTab;
import com.sharebravo.bravo.view.fragment.home.FragmentCoverImage;
import com.sharebravo.bravo.view.fragment.home.FragmentFollower;
import com.sharebravo.bravo.view.fragment.home.FragmentFollowing;
import com.sharebravo.bravo.view.fragment.home.FragmentHistory;
import com.sharebravo.bravo.view.fragment.home.FragmentHomeNotification;
import com.sharebravo.bravo.view.fragment.home.FragmentHomeNotification.IClosePageHomeNotification;
import com.sharebravo.bravo.view.fragment.home.FragmentHomeTab;
import com.sharebravo.bravo.view.fragment.home.FragmentHomeTab.IShowPageHomeNotification;
import com.sharebravo.bravo.view.fragment.home.FragmentMapView;
import com.sharebravo.bravo.view.fragment.home.FragmentNetworkTab;
import com.sharebravo.bravo.view.fragment.home.FragmentRecentPostDetail;
import com.sharebravo.bravo.view.fragment.home.FragmentSearchTab;
import com.sharebravo.bravo.view.fragment.home.FragmentShare;
import com.sharebravo.bravo.view.fragment.setting.FragmentSetting;
import com.sharebravo.bravo.view.fragment.setting.FragmentSetting.IShowPageTermOfUse;
import com.sharebravo.bravo.view.fragment.setting.FragmentShareWithFriends;
import com.sharebravo.bravo.view.fragment.setting.FragmentTermOfUse;
import com.sharebravo.bravo.view.fragment.setting.FragmentUpdateUserInfo;
import com.sharebravo.bravo.view.fragment.userprofile.FragmentFavourite;
import com.sharebravo.bravo.view.fragment.userprofile.FragmentUserDataTab;
import com.sharebravo.bravo.view.fragment.userprofile.FragmentUserDataTab.IShowPageSettings;

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
    private static final String      FRAGMENT_SHARE                 = "page_share";
    private static final String      FRAGMENT_SHARE_WITH_FRIENDS    = "page_settings_share_with_friends";
    private static final String      FRAGMENT_HISTORY               = "page_history";
    private static final String      FRAGMENT_FOLOWING              = "page_following";
    private static final String      FRAGMENT_FOLLOWER              = "page_follower";
    private static final String      FRAGMENT_FAVOURITE             = "favourite";

    public static final int          FRAGMENT_BASE_ID               = 1000;
    public static final int          FRAGMENT_HOME_TAB_ID           = FRAGMENT_BASE_ID + 1;
    public static final int          FRAGMENT_NETWORK_TAB_ID        = FRAGMENT_BASE_ID + 2;
    public static final int          FRAGMENT_BRAVO_TAB_ID          = FRAGMENT_BASE_ID + 3;
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
    private FragmentShare            mFragmentShare;
    private FragmentShareWithFriends mFragmentShareWithFriends;

    private FragmentHistory          mFragmentHistory;
    private FragmentFollowing        mFragmentFollowing;
    private FragmentFollower         mFragmentFollower;
    private FragmentFavourite        mFragmentFavourite;

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

        /**
         * This if conditions is tested once is
         * redirected from twitter page. Parse the uri to get oAuth
         * Verifier
         * */
        if (!isTwitterLoggedInAlready()) {
            Uri uri = getIntent().getData();
            if (uri != null && uri.toString().startsWith(BravoConstant.TWITTER_CALLBACK_HOME_URL)) {
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
                    // Check for blank text
                    if (mSharedSnsText.trim().length() > 0) {
                        new UpdateTwitterStatus().execute(mSharedSnsText);
                    } else {
                        Toast.makeText(getApplicationContext(), "Please enter status message", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    // Check log for login errors
                    Log.e("Twitter Login Error", "> " + e.getMessage());
                }
            }
        }

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
            // hideTabButton();
            // showFragment(FRAGMENT_BRAVO_TAB_ID);
            // btnBravo.setBackgroundResource(R.drawable.tab_bravo_on);
            Intent bravoIntent = new Intent(HomeActivity.this, ActivityBravoChecking.class);
            startActivity(bravoIntent);
            overridePendingTransition(R.anim.slide_in_up, R.anim.fade_in);
            break;
        case R.id.btn_search:
            hideTabButton();
            showFragment(FRAGMENT_SEARCH_TAB_ID);
            btnSearch.setBackgroundResource(R.drawable.tab_search_on);
            txtSearch.setTextColor(Color.WHITE);
            Toast.makeText(this, "This feature is coming soon", Toast.LENGTH_SHORT).show();
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
        mFragmentShare = (FragmentShare) mFmManager.findFragmentById(R.id.fragment_share);
        mFragmentShareWithFriends = (FragmentShareWithFriends) mFmManager.findFragmentById(R.id.fragment_share_with_friends);

        mFragmentHistory = (FragmentHistory) mFmManager.findFragmentById(R.id.fragment_history);
        mFragmentFollowing = (FragmentFollowing) mFmManager.findFragmentById(R.id.fragment_following);
        mFragmentFollower = (FragmentFollower) mFmManager.findFragmentById(R.id.fragment_follower);
        mFragmentFavourite = (FragmentFavourite) mFmManager.findFragmentById(R.id.fragment_favourite);

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
        mTransaction.hide(mFragmentShare);
        mTransaction.hide(mFragmentShareWithFriends);

        mTransaction.hide(mFragmentHistory);
        mTransaction.hide(mFragmentFollowing);
        mTransaction.hide(mFragmentFollower);
        mTransaction.hide(mFragmentFavourite);

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
        if (currentView.equals(FRAGMENT_RECENT_POST_DETAIL)) {
            mTransaction.show(mFragmentHomeTab);
        } else if (currentView.equals(FRAGMENT_USER_DATA_TAB) && previousView != null && previousView.equals(FRAGMENT_RECENT_POST_DETAIL)) {
            mTransaction.show(mFragmentRecentPostDetail);
        } else if (currentView.equals(FRAGMENT_MAP_VIEW)) {
            if (previousView != null && previousView.equals(FRAGMENT_RECENT_POST_DETAIL))
                mTransaction.show(mFragmentRecentPostDetail);
            else if (previousView != null && previousView.equals(FRAGMENT_USER_DATA_TAB))
                mTransaction.show(mFragmentUserDataTab);
        } else if (currentView.equals(FRAGMENT_COVER_IMAGE) || currentView.equals(FRAGMENT_SHARE)) {
            mTransaction.show(mFragmentRecentPostDetail);
        } else if (currentView.equals(FRAGMENT_SETTINGS) || currentView.equals(FRAGMENT_FAVOURITE)) {
            mTransaction.show(mFragmentUserDataTab);
            mFragmentUserDataTab.getUserInfo("");
        } else if (currentView.equals(FRAGMENT_TERM_OF_USE) || currentView.equals(FRAGMENT_UPDATE_USER_INFO)
                || currentView.equals(FRAGMENT_SHARE_WITH_FRIENDS)) {
            mTransaction.show(mFragmentSetting);
        } else if (currentView.equals(FRAGMENT_HISTORY) || currentView.equals(FRAGMENT_FOLOWING) || currentView.equals(FRAGMENT_FOLLOWER)
                || currentView.equals(FRAGMENT_FAVOURITE)) {
            mTransaction.show(mFragmentUserDataTab);
        } else if (currentView.equals(FRAGMENT_HOME_TAB) || currentView.equals(FRAGMENT_NETWORK_TAB) || currentView.equals(FRAGMENT_BRAVO_TAB)
                || currentView.equals(FRAGMENT_SEARCH_TAB) || currentView.equals(FRAGMENT_USER_DATA_TAB)) {
            super.onBackPressed();
            return;
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

    @Override
    public void goToMapView(String lat, String log, int locationType) {
        if (lat != null && log != null)
            mFragmentMapView.setCordinate(lat, log);
        mFragmentMapView.setTypeMaker(locationType);
        showFragment(FRAGMENT_MAP_VIEW_ID);
    }

    @Override
    public void onClickUserAvatar(String userId) {
        showFragment(FRAGMENT_USER_DATA_TAB_ID);
        mFragmentUserDataTab.getUserInfo(userId);
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
    public void goToUserTimeLine(String foreignID, String foreignName) {
        mFragmentHistory.setForeignID(foreignID);
        mFragmentHistory.setForeignName(foreignName);
        showFragment(FRAGMENT_HISTORY_ID);
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
    public void shareSNSViaTwitter(ObBravo obBravo, String sharedText) {
        // Check if already logged in
        mObBravo = obBravo;
        mSharedSnsText = sharedText;
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
            requestToGetTwitterUserInfo(obBravo, sharedText);
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
}
