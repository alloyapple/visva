package com.sharebravo.bravo.control.activity;

import java.util.ArrayList;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;
import br.com.condesales.EasyFoursquareAsync;
import br.com.condesales.criterias.CheckInCriteria;
import br.com.condesales.listeners.AccessTokenRequestListener;
import br.com.condesales.listeners.CheckInListener;
import br.com.condesales.listeners.UserInfoRequestListener;
import br.com.condesales.models.Checkin;
import br.com.condesales.models.User;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sharebravo.bravo.R;
import com.sharebravo.bravo.control.request.BravoRequestManager;
import com.sharebravo.bravo.control.request.IRequestListener;
import com.sharebravo.bravo.model.response.ObPostBravo;
import com.sharebravo.bravo.model.response.SNS;
import com.sharebravo.bravo.model.response.Spot;
import com.sharebravo.bravo.sdk.log.AIOLog;
import com.sharebravo.bravo.utils.BravoConstant;
import com.sharebravo.bravo.utils.BravoSharePrefs;
import com.sharebravo.bravo.utils.BravoUtils;
import com.sharebravo.bravo.utils.BravoWebServiceConfig;
import com.sharebravo.bravo.utils.FacebookUtil;
import com.sharebravo.bravo.utils.StringUtility;
import com.sharebravo.bravo.view.fragment.bravochecking.FragmentBravoMap;
import com.sharebravo.bravo.view.fragment.bravochecking.FragmentBravoReturnSpot;
import com.sharebravo.bravo.view.fragment.bravochecking.FragmentBravoSearch;
import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.entities.Profile;
import com.sromku.simple.fb.listeners.OnLoginListener;
import com.sromku.simple.fb.listeners.OnProfileListener;

public class ActivityBravoChecking extends VisvaAbstractFragmentActivity implements BravoCheckingListener, AccessTokenRequestListener {
    // ======================Constant Define===============
    private static final String     FRAGMENT_BRAVO_MAP             = "bravo_map";
    // private static final String FRAGMENT_BRAVO_RETURN_SPOTS = "return_spots";
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
    private FragmentBravoSearch     mFragmentBravoSearch;
    private Spot                    mSpot;
    // ======================Variable Define===============
    private ArrayList<String>       mBackstack                     = new ArrayList<String>();
    private static RequestToken     mTwitterRequestToken;
    protected static Twitter        mTwitter;
    private EasyFoursquareAsync     mEasyFoursquareAsync;
    private SimpleFacebook          mSimpleFacebook;
    private ObPostBravo             mObPostBravo;
    private String                  mSharedText;

    @Override
    public int contentView() {
        return R.layout.activity_bravo_checking;
    }

    @Override
    public void onCreate() {
        mSimpleFacebook = SimpleFacebook.getInstance(this);
        if (Build.VERSION.SDK_INT >= 11)
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        initializeFragments();
        configTwitterLogin();
    }

    private void configTwitterLogin() {
        /**
         * This if conditions is tested once is
         * redirected from twitter page. Parse the uri to get oAuth
         * Verifier
         * */
        if (!BravoUtils.isTwitterLoggedInAlready(this)) {
            Uri uri = getIntent().getData();
            if (uri != null
                    && (uri.toString().startsWith(BravoConstant.TWITTER_CALLBACK_RECENT_POST_URL))) {
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
                    if (uri.toString().startsWith(BravoConstant.TWITTER_CALLBACK_RECENT_POST_URL)) {
                        SNS sns = new SNS();
                        sns.foreignID = userID + "";
                        sns.foreignSNS = BravoConstant.TWITTER;
                        sns.foreignAccessToken = accessToken.getToken() + "," + accessToken.getTokenSecret();
                        putSNS(sns);
                        mFragmentBravoReturnSpots.updatePostSNS(sns, true);
                        showFragment(FRAGMENT_BRAVO_RETURN_SPOTS_ID);
                    }
                } catch (Exception e) {
                    Log.e("Twitter Login Error", "> " + e.getMessage());
                }
            }
        }
    }

    private void initializeFragments() {
        mFmManager = getSupportFragmentManager();
        mFragmentBravoMap = (FragmentBravoMap) mFmManager.findFragmentById(R.id.fragment_bravo_map);
        mFragmentBravoReturnSpots = (FragmentBravoReturnSpot) mFmManager.findFragmentById(R.id.fragment_bravo_return_spot);
        mFragmentBravoSearch = (FragmentBravoSearch) mFmManager.findFragmentById(R.id.fragment_bravo_search);

        String spotJSon = getIntent().getExtras().getString(BravoConstant.EXTRA_SPOT_JSON);
        AIOLog.d("spotJSon:" + spotJSon);
        if (StringUtility.isEmpty(spotJSon))
            showFragment(FRAGMENT_BRAVO_TAB_ID);
        else {
            Gson gson = new GsonBuilder().serializeNulls().create();
            mSpot = gson.fromJson(spotJSon.toString(), Spot.class);
            if (mSpot == null)
                showFragment(FRAGMENT_BRAVO_TAB_ID);
            else {
                mFragmentBravoMap.setBravoSpot(mSpot);
                showFragment(FRAGMENT_BRAVO_MAP_ID);
            }
        }
    }

    private void showFragment(int fragmentID) {
        mTransaction = hideFragment();
        switch (fragmentID) {
        case FRAGMENT_BRAVO_TAB_ID:
            mTransaction.show(mFragmentBravoSearch);
            addToSBackStack(FRAGMENT_BRAVO_TAB);
            break;
        case FRAGMENT_BRAVO_MAP_ID:
            mTransaction.show(mFragmentBravoMap);
            break;
        case FRAGMENT_BRAVO_RETURN_SPOTS_ID:
            mTransaction.show(mFragmentBravoReturnSpots);
            break;
        }
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
    }

    @Override
    public void goToFragment(int fragmentID) {

    }

    @Override
    public void goToMapView(Spot spot, int locationType) {
        AIOLog.d("spot:" + spot + ", locationType" + locationType);
        mFragmentBravoMap.setBravoSpot(spot);
        showFragment(FRAGMENT_BRAVO_MAP_ID);
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
        } else if (currentView.equals(FRAGMENT_BRAVO_TAB)) {
            super.onBackPressed();
            return;
        }
        mTransaction.commitAllowingStateLoss();
    }

    @Override
    public void goToReturnSpotFragment(Spot mSpot) {
        mFragmentBravoReturnSpots.setBravoSpot(mSpot);
        showFragment(FRAGMENT_BRAVO_RETURN_SPOTS_ID);
    }

    @Override
    public void goToAddSpot() {
        setResult(ActivityBravoChecking.RESULT_CANCELED);
        finish();
    }

    @Override
    public void finishPostBravo() {
        Toast.makeText(this, getString(R.string.post_bravo_message_special), Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.slide_out_up);
    }

    @Override
    public void shareViaSNSByRecentPost(String snsType, ObPostBravo obPostBravo, String sharedText) {
        // Check if already logged in
        if (BravoConstant.TWITTER.equals(snsType)) {
            shareViaTwitter(BravoConstant.TWITTER_CALLBACK_RECENT_POST_URL, obPostBravo, sharedText);
        } else if (BravoConstant.FOURSQUARE.equals(snsType)) {
            shareViaFourSquare(obPostBravo, sharedText);
        } else if (BravoConstant.FACEBOOK.equals(snsType)) {
            shareViaFacebook(obPostBravo, sharedText);
        }
    }

    private void shareViaFacebook(ObPostBravo obPostBravo, String sharedText) {
        mObPostBravo = obPostBravo;
        mSharedText = sharedText;
        if (mSimpleFacebook == null) {
            mSimpleFacebook.login(onLoginListener);
            return;
        } else {
            requestUserFacebookInfo();
        }
    }

    private void shareViaFourSquare(ObPostBravo obPostBravo, String sharedText) {
        if (obPostBravo == null || mEasyFoursquareAsync == null) {
            mEasyFoursquareAsync = new EasyFoursquareAsync(this);
            mEasyFoursquareAsync.requestAccess(this);
        } else {
            CheckInCriteria criteria = new CheckInCriteria();
            criteria.setBroadcast(CheckInCriteria.BroadCastType.PUBLIC);
            criteria.setVenueId("4c7063da9c6d6dcb9798d27a");

            mEasyFoursquareAsync.checkIn(new CheckInListener() {
                @Override
                public void onCheckInDone(Checkin checkin) {
                    AIOLog.d("check in via recet post");
                }

                @Override
                public void onError(String errorMsg) {
                }
            }, criteria);
        }
    }

    private void shareViaTwitter(String urlCallback, ObPostBravo obPostBravo, String sharedText) {
        if (obPostBravo == null)
            return;
        Log.d("Twitter", "shareViaTwitter :" + obPostBravo.data.Bravo_ID + ",mTwitter: " + mTwitter);
        if (mTwitter == null) {
            requestToLoginSNS(BravoConstant.TWITTER);
        } else
            requestToGetTwitterUserInfo(obPostBravo.data.Bravo_ID, sharedText);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mSimpleFacebook.onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSimpleFacebook = SimpleFacebook.getInstance(this);
    }

    @Override
    public void putSNS(final SNS sns) {
        BravoRequestManager.getInstance(this).putSNS(this, sns, new IRequestListener() {

            @Override
            public void onResponse(String response) {
                AIOLog.d("putSNS to bravo onResponse:" + response);
            }

            @Override
            public void onErrorResponse(String errorMessage) {
                AIOLog.d("putSNS to bravo onErrorResponse:" + errorMessage);
            }
        });
    }

    @Override
    public void deleteSNS(final SNS sns) {
        BravoRequestManager.getInstance(this).deleteSNS(sns.foreignID, new IRequestListener() {

            @Override
            public void onResponse(String response) {
                mFragmentBravoReturnSpots.updatePostSNS(sns, false);
            }

            @Override
            public void onErrorResponse(String errorMessage) {
                AIOLog.d("response error");
                mFragmentBravoReturnSpots.updatePostSNS(sns, true);
            }
        });
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
        String bravoUrl = BravoWebServiceConfig.URL_BRAVO_ID_DETAIL.replace("{Bravo_ID}", bravoId);
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
        }

        /**
         * getting Places JSON
         * */
        protected String doInBackground(String... args) {
            Log.d("Twitter", "> " + args[0]);
            String status = args[0];
            try {
                ConfigurationBuilder builder = new ConfigurationBuilder();
                builder.setOAuthConsumerKey(BravoConstant.TWITTER_CONSUMER_KEY);
                builder.setOAuthConsumerSecret(BravoConstant.TWITTER_CONSUMER_SECRET);

                // Access Token
                String access_token = BravoSharePrefs.getInstance(ActivityBravoChecking.this).getStringValue(
                        BravoConstant.PREF_KEY_TWITTER_OAUTH_TOKEN);
                String access_token_secret = BravoSharePrefs.getInstance(ActivityBravoChecking.this)
                        .getStringValue(BravoConstant.PREF_KEY_TWITTER_OAUTH_SCRET);

                AccessToken accessToken = new AccessToken(access_token, access_token_secret);
                Twitter twitter = new TwitterFactory(builder.build()).getInstance(accessToken);

                // Update status
                twitter4j.Status response = twitter.updateStatus(status);

                Log.d("Twitter", "> " + response.getText());
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
            AIOLog.d("share twitter success by recent post");
        }
    }

    @Override
    public void onError(String errorMsg) {
        SNS sns = new SNS();
        sns.foreignSNS = BravoConstant.FOURSQUARE;
        mFragmentBravoReturnSpots.updatePostSNS(sns, false);
    }

    @Override
    public void onAccessGrant(final String accessToken) {
        AIOLog.d("accessToken: " + accessToken);
        SNS sns = new SNS();
        sns.foreignSNS = BravoConstant.FOURSQUARE;
        mFragmentBravoReturnSpots.updatePostSNS(sns, true);
        mEasyFoursquareAsync.getUserInfo(new UserInfoRequestListener() {

            @Override
            public void onError(String errorMsg) {
                SNS sns = new SNS();
                sns.foreignSNS = BravoConstant.FOURSQUARE;
                mFragmentBravoReturnSpots.updatePostSNS(sns, false);
                BravoSharePrefs.getInstance(ActivityBravoChecking.this).putBooleanValue(BravoConstant.PREF_KEY_FOURSQUARE_LOGIN, false);
            }

            @Override
            public void onUserInfoFetched(User user) {
                AIOLog.d("user 4square:" + user);
                if (user == null)
                    return;
                AIOLog.d("user: " + user.getFirstName());
                SNS sns = new SNS();
                sns.foreignID = user.getId() + "";
                sns.foreignSNS = BravoConstant.FOURSQUARE;
                sns.foreignAccessToken = accessToken;
                putSNS(sns);
                mFragmentBravoReturnSpots.updatePostSNS(sns, true);
                BravoSharePrefs.getInstance(ActivityBravoChecking.this).putBooleanValue(BravoConstant.PREF_KEY_FOURSQUARE_LOGIN, true);
            }
        });
    }

    @Override
    public void requestToLoginSNS(String snsType) {
        if (BravoConstant.TWITTER.equals(snsType)) {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            Log.d("Twitter", "BravoUtils.isTwitterLoggedInAlready:" + BravoUtils.isTwitterLoggedInAlready(this));
            if (!BravoUtils.isTwitterLoggedInAlready(this)) {
                ConfigurationBuilder builder = new ConfigurationBuilder();
                builder.setOAuthConsumerKey(BravoConstant.TWITTER_CONSUMER_KEY);
                builder.setOAuthConsumerSecret(BravoConstant.TWITTER_CONSUMER_SECRET);
                Configuration configuration = builder.build();

                TwitterFactory factory = new TwitterFactory(configuration);
                mTwitter = factory.getInstance();

                try {
                    mTwitterRequestToken = mTwitter.getOAuthRequestToken(BravoConstant.TWITTER_CALLBACK_RECENT_POST_URL);
                    this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mTwitterRequestToken.getAuthenticationURL())));
                    finish();
                } catch (TwitterException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Login example.
     */
    // Login listener
    final OnLoginListener onLoginListener = new OnLoginListener() {

                                              @Override
                                              public void onFail(String reason) {
                                                  AIOLog.e("Failed to login");
                                              }

                                              @Override
                                              public void onException(Throwable throwable) {
                                                  AIOLog.e("Bad thing happened", throwable);
                                              }

                                              @Override
                                              public void onThinking() {
                                                  // show progress bar or something to the user while login is
                                                  // happening
                                              }

                                              @Override
                                              public void onLogin() {
                                                  // change the state of the button or do whatever you want
                                                  AIOLog.d("onLogin");
                                                  requestUserFacebookInfo();
                                              }

                                              @Override
                                              public void onNotAcceptingPermissions(Permission.Type type) {
                                                  Toast.makeText(ActivityBravoChecking.this,
                                                          String.format("You didn't accept %s permissions", type.name()),
                                                          Toast.LENGTH_SHORT).show();
                                              }
                                          };

    private void requestUserFacebookInfo() {
        if (mSimpleFacebook == null) {
            mSimpleFacebook = SimpleFacebook.getInstance(this);
            return;
        }
        SimpleFacebook.getInstance().getProfile(new OnProfileListener() {

            @Override
            public void onThinking() {
            }

            @Override
            public void onException(Throwable throwable) {
            }

            @Override
            public void onFail(String reason) {
            }

            @Override
            public void onComplete(Profile profile) {
                onFacebookUserConnected();
            }
        });
    }

    private void onFacebookUserConnected() {
        if(mObPostBravo == null)
            return;
        FacebookUtil.getInstance(ActivityBravoChecking.this).publishShareInBackground(mObPostBravo.data.Bravo_ID, mSharedText,
                new IRequestListener() {

                    @Override
                    public void onResponse(String response) {
                        AIOLog.d("share facebook success via recent post");
                    }

                    @Override
                    public void onErrorResponse(String errorMessage) {

                    }
                });
    }
}
