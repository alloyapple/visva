package com.samsung.android.alwayssocial.activity;

import java.util.Arrays;
import java.util.List;

import twitter4j.RateLimitStatus;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.AppEventsLogger;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.LoginButton;
import com.samsung.android.alwayssocial.R;
import com.samsung.android.alwayssocial.app.AlwaysSocialAppImpl;
import com.samsung.android.alwayssocial.constant.GlobalConstant;
import com.samsung.android.alwayssocial.constant.TwitterConstant;
import com.samsung.android.alwayssocial.object.SocialUserObject;

public class AlwaysActivity extends Activity {
    public static final String TAG = "AlwaysActivity";
    public static final int TW_LOGIN_ERROR = -1;
    public static final int TW_LOGIN_SUCCESS = 0;
    private final String PENDING_ACTION_BUNDLE_KEY = "com.samsung.android.always.activity.AlwaysActivity:PendingAction";
    private LoginButton mFacebookLoginButton;
    private Button mTwitterLoginButton;
    private PendingAction mFacebookPendingAction = PendingAction.NONE;
    private UiLifecycleHelper mUiHelper;
    private static RequestToken mTwitterRequestToken;
    protected static Twitter mTwitter;

    private int mViewType = GlobalConstant.VIEW_TYPE_LIST;

    private enum PendingAction {
        NONE, POST_PHOTO, POST_STATUS_UPDATE
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        intiFacebook(savedInstanceState);
        initControl();
    }

    private void initControl() {
        mFacebookLoginButton = (LoginButton) findViewById(R.id.login_fb_button);
        /* permission user_likes = liked pages 
         * user_photos, read_mailbox = tagged_me
         * user_group = groups
         * read_friendlists = friend_group
         * friends_status = friend status (like/comment info)
         * read_stream = me/home
         * friends_photos = photo infor
         * */
        List<String> permissions = Arrays.asList(" ", "user_photos", "read_mailbox", "user_groups", "read_friendlists", "friends_status", "read_stream", "friends_photos");
        mFacebookLoginButton.setReadPermissions(permissions);
        mFacebookLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick() - login fb");
                onClickFacebookLoginButton();
            }
        });
        mFacebookLoginButton.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {

            @Override
            public void onUserInfoFetched(GraphUser user) {
                // Save logged user information
                if (user != null) {
                    SocialUserObject loggedInUser = new SocialUserObject(user.getId(), user.getName());
                    AlwaysSocialAppImpl.getInstance().setLoggedInUser(GlobalConstant.SOCIAL_TYPE_FACEBOOK, loggedInUser);
                }
            }
        });

        /*Twitter Login button */
        mTwitterLoginButton = (Button) findViewById(R.id.login_twitter_button);
        mTwitterLoginButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                onClickTwitterLoginButton(v);
            }
        });
    }

    private void intiFacebook(Bundle savedInstanceState) {
        mUiHelper = new UiLifecycleHelper(this, callback);
        mUiHelper.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            String name = savedInstanceState.getString(PENDING_ACTION_BUNDLE_KEY);
            mFacebookPendingAction = PendingAction.valueOf(name);
        }
    }

    private Session.StatusCallback callback = new Session.StatusCallback() {

        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };

    private FacebookDialog.Callback dialogCallback = new FacebookDialog.Callback() {
        @Override
        public void onError(FacebookDialog.PendingCall pendingCall, Exception error, Bundle data) {
            Log.d(TAG, "onError()-" + String.format("Error: %s", error.toString()));
        }

        @Override
        public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data) {
            Log.d(TAG, "onComplete()-Success!");
        }
    };

    private void onClickFacebookLoginButton() {
        //        if (Utils.isNetworkAvailable(AlwaysActivity.this)) {
        Session session = Session.getActiveSession();
        boolean enableButtons = (session != null && session.isOpened());
        if (enableButtons) {
            Intent intent = new Intent(AlwaysActivity.this, FacebookMainActivity.class);
            intent.putExtra(GlobalConstant.VIEW_MODE, mViewType);
            startActivity(intent);
        } else {
            mFacebookLoginButton.onClickLoginFb();
        }
        //        } else {
        //            showToast(getString(R.string.no_networks_found));
        //        }
    }

    public void onClickTwitterLoginButton(View v) {
        //        if (Utils.isNetworkAvailable(AlwaysActivity.this)) {
        // check if logged, change image to logged image
        // if not -> come to log in function
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setOAuthConsumerKey(TwitterConstant.TWITTER_CONSUMER_KEY);
        builder.setOAuthConsumerSecret(TwitterConstant.TWITTER_CONSUMER_SECRET);
        Configuration configuration = builder.build();

        TwitterFactory factory = new TwitterFactory(configuration);
        mTwitter = factory.getInstance();
        if (!isTwitterLoggedInAlready())
        {
            Thread thread = new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        mTwitterRequestToken = mTwitter.getOAuthRequestToken(TwitterConstant.TWITTER_CALLBACK_URL);
                        //Intent intent = new Intent(AlwaysActivity.this, WebAuthActivity.class);
                        //intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        //intent.putExtra("URL", mTwitterRequestToken.getAuthenticationURL());
                        //intent.putExtra("TYPE", GlobalConstant.SOCIAL_TYPE_TWITTER);
                        //AlwaysActivity.this.startActivityForResult(intent, GlobalConstant.REQUEST_CODE_AUTHORIZE_ACTIVITY);
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mTwitterRequestToken.getAuthenticationURL()));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        AlwaysActivity.this.startActivity(intent);
                        finish();
                    } catch (TwitterException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        } else {
            Intent intent = new Intent(AlwaysActivity.this, TwitterMainActivity.class);
            intent.putExtra(GlobalConstant.VIEW_MODE, mViewType);
            startActivity(intent);
        }
        //        } else {
        //            showToast(getString(R.string.no_networks_found));
        //        }
    }

    public void onClickGooglePlusLoginButton(View v) {
        showToast("This feature is comming soon");
    }

    public void onClickInstargramLoginButton(View v) {
        showToast("This feature is comming soon");
    }

    public void onClickLinkedInLoginButton(View v) {
        showToast("This feature is comming soon");
    }

    @SuppressLint("HandlerLeak")
    public final Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            Log.d(TAG, "handleMessage msg.what = " + msg.what);
            switch (msg.what) {
            case TW_LOGIN_ERROR:
                mTwitterLoginButton.setEnabled(true);
                mTwitterLoginButton.setBackgroundResource(R.drawable.twitter_login_button);
                break;
            case TW_LOGIN_SUCCESS:
                mTwitterLoginButton.setEnabled(true);
                mTwitterLoginButton.setBackgroundResource(R.drawable.twitter_login_button_inactive);
                break;
            default:
                break;
            }
            super.handleMessage(msg);
        }

    };

    @Override
    protected void onResume() {
        super.onResume();
        mUiHelper.onResume();

        // Call the 'activateApp' method to log an app event for use in
        // analytics and advertising reporting. Do so in
        // the onResume methods of the primary Activities that an app may be
        // launched into.
        AppEventsLogger.activateApp(this);
        if (isTwitterLoggedInAlready()) {
            mTwitterLoginButton.setBackgroundResource(R.drawable.twitter_login_button_inactive);
        } else {
            mTwitterLoginButton.setBackgroundResource(R.drawable.twitter_login_button);
        }

        // Twitter Login
        // Get call back from Twitter side
        Uri uri = getIntent().getData();
        if (uri != null && uri.toString().startsWith(TwitterConstant.TWITTER_CALLBACK_URL)) {
            // oAuth verifier
            final String verifier = uri.getQueryParameter(TwitterConstant.URL_TWITTER_OAUTH_VERIFIER);
            Log.d(TAG, "verifier = " + verifier);
            Thread thread = new Thread(new Runnable() {

                @Override
                public void run() {
                    int loginResult = TW_LOGIN_ERROR;
                    try {
                        AccessToken accessToken = mTwitter.getOAuthAccessToken(mTwitterRequestToken, verifier);
                        if (accessToken != null)
                        {
                            // Log in successfully
                            loginResult = TW_LOGIN_SUCCESS;
                            AlwaysSocialAppImpl.getInstance().getAlwaysSharePrefs().putBooleanValue(TwitterConstant.PREF_KEY_TWITTER_LOGIN, true);
                            AlwaysSocialAppImpl.getInstance().getAlwaysSharePrefs().putStringValue(TwitterConstant.PREF_KEY_TWITTER_OAUTH_TOKEN, accessToken.getToken());
                            AlwaysSocialAppImpl.getInstance().getAlwaysSharePrefs().putStringValue(TwitterConstant.PREF_KEY_TWITTER_OAUTH_SECRET, accessToken.getTokenSecret());
                            RateLimitStatus rateLimit = mTwitter.getRateLimitStatus("application").get("/application/rate_limit_status");
                            Log.d(TAG, "Application Remaining RateLimit =" + rateLimit.getRemaining());
                            int limitRate = rateLimit.getRemaining();
                            if (limitRate >= 20) {
                                long userID = accessToken.getUserId();
                                Log.d(TAG, "userID = " + userID);
                                twitter4j.User user = mTwitter.showUser(userID);
                                Log.d(TAG, "Twitter OAuth Token = " + accessToken.getToken() + " username = " + user.getName());
                                SocialUserObject loggedInUser = new SocialUserObject(Long.toString(user.getId()), user.getName(), user.getBiggerProfileImageURL());
                                AlwaysSocialAppImpl.getInstance().setLoggedInUser(GlobalConstant.SOCIAL_TYPE_TWITTER, loggedInUser);
                            }
                        }
                    }
                    catch (TwitterException e) {
                        Log.d(TAG, "Error Twitter OAuth Token = " + e.getMessage());
                        loginResult = TW_LOGIN_ERROR;
                    }
                    mHandler.sendEmptyMessage(loginResult);
                }
            });
            thread.start();
            mTwitterLoginButton.setEnabled(false);
            mViewType = AlwaysSocialAppImpl.getInstance().getViewType();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mUiHelper.onSaveInstanceState(outState);

        outState.putString(PENDING_ACTION_BUNDLE_KEY, mFacebookPendingAction.name());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
        mUiHelper.onActivityResult(requestCode, resultCode, data, dialogCallback);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.menu_view_type:
            // Create dialog
            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.menu_view_type_layout);
            dialog.setTitle(R.string.always_view_type);
            ListView list = (ListView) dialog.findViewById(R.id.view_type_list);
            list.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, getResources().getStringArray(R.array.view_type_items)));
            list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            list.setItemChecked(0, true);
            list.setItemsCanFocus(true);

            list.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.d(TAG, "onItemClick view type position = " + position);
                    mViewType = position;
                    AlwaysSocialAppImpl.getInstance().setViewType(position);
                    dialog.dismiss();
                }
            });
            dialog.show();
            break;

        default:
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.d(TAG, "onNewIntent");
        super.onNewIntent(intent);
    }

    @Override
    public void onPause() {
        super.onPause();
        mUiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUiHelper.onDestroy();
    }

    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        Log.d(TAG, "onSessionStateChange");
        if (mFacebookPendingAction != PendingAction.NONE && (exception instanceof FacebookOperationCanceledException || exception instanceof FacebookAuthorizationException)) {
            new AlertDialog.Builder(AlwaysActivity.this).setTitle(R.string.btn_cancel)
                    .setMessage(R.string.permission_not_granted)
                    .setPositiveButton(R.string.btn_ok, null).show();
            mFacebookPendingAction = PendingAction.NONE;
        } else if (state == SessionState.OPENED_TOKEN_UPDATED) {
            Log.e(TAG, "OPEN_TOKEN_UPDATE");
        }
    }

    private void showToast(final String str) {
        Toast.makeText(AlwaysActivity.this, str, Toast.LENGTH_SHORT).show();
    }

    private boolean isTwitterLoggedInAlready() {
        return AlwaysSocialAppImpl.getInstance().getAlwaysSharePrefs().getBooleanValue(TwitterConstant.PREF_KEY_TWITTER_LOGIN);
    }
}
