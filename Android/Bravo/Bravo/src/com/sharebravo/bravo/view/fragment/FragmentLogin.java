package com.sharebravo.bravo.view.fragment;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import br.com.condesales.EasyFoursquareAsync;
import br.com.condesales.listeners.AccessTokenRequestListener;
import br.com.condesales.listeners.ImageRequestListener;
import br.com.condesales.listeners.UserInfoRequestListener;
import br.com.condesales.models.User;

import com.facebook.AppEventsLogger;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginTextView;
import com.sharebravo.bravo.R;
import com.sharebravo.bravo.control.activity.HomeActivity;
import com.sharebravo.bravo.model.user.BravoUser;
import com.sharebravo.bravo.sdk.log.AIOLog;
import com.sharebravo.bravo.utils.BravoConstant;

public class FragmentLogin extends FragmentBasic implements AccessTokenRequestListener, ImageRequestListener {
    // ====================Constant Define=================
    public static final int        TW_LOGIN_ERROR   = -1;
    public static final int        TW_LOGIN_SUCCESS = 0;
    // ====================Class Define====================
    // ====================Variable Define=================
    private LoginTextView          mTextViewFacebookLogin;
    private TextView               mTextViewTwitterLogin;
    private TextView               mTextViewFourSquareLogin;
    private UiLifecycleHelper      mUiLifecycleHelper;
    private Session.StatusCallback mFacebookCallback;
    private RelativeLayout         mLayoutBravoLogin;
    private IShowPageBravoLogin    mListener;
    private static RequestToken    mTwitterRequestToken;
    protected static Twitter       mTwitter;
    private EasyFoursquareAsync    mEasyFoursquareAsync;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = (ViewGroup) inflater.inflate(R.layout.page_fragment_login, container);
        mLayoutBravoLogin = (RelativeLayout) root.findViewById(R.id.layout_bravo_login);
        mLayoutBravoLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mListener.showPageBravoLogin();
            }
        });

        /* facebook */
        mTextViewFacebookLogin = (LoginTextView) root.findViewById(R.id.text_facebook_login);
        mTextViewFacebookLogin.setUserInfoChangedCallback(new LoginTextView.UserInfoChangedCallback() {
            @Override
            public void onUserInfoFetched(GraphUser user) {
                AIOLog.e("user:" + user);
                if (user == null)
                    return;
                BravoUser bravoUser = new BravoUser();
                bravoUser.mUserEmail = user.getLink();
                bravoUser.mUserId = user.getId();
                bravoUser.mUserName = user.getName();

                Intent homeIntent = new Intent(getActivity(), HomeActivity.class);
                startActivity(homeIntent);
                getActivity().finish();
            }
        });

        /* twitter */
        mTextViewTwitterLogin = (TextView) root.findViewById(R.id.text_twitter_login);
        mTextViewTwitterLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onClickTwitterLoginButton();
            }
        });

        /* foursquare */
        mTextViewFourSquareLogin = (TextView) root.findViewById(R.id.text_4square_login);
        mTextViewFourSquareLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Intent homeIntent = new Intent(getActivity(), HomeActivity.class);
                // startActivity(homeIntent);
                // getActivity().finish();
                onClickLogin4Square();
            }
        });

        return root;
    }

    private void onClickLogin4Square() {
        // TODO Auto-generated method stub
        /* 4square api */
        // ask for access
        mEasyFoursquareAsync = new EasyFoursquareAsync(getActivity());
        mEasyFoursquareAsync.requestAccess(this);
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* facebook api */
        mFacebookCallback = new Session.StatusCallback() {
            @Override
            public void call(final Session session, final SessionState state, final Exception exception) {
            }
        };
        mUiLifecycleHelper = new UiLifecycleHelper(getActivity(), mFacebookCallback);
        mUiLifecycleHelper.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        Session session = Session.getActiveSession();
        boolean isActivedSession = session.isOpened();
        AIOLog.d("isActivedSession=" + isActivedSession);
        // Call the 'activateApp' method to log an app event for use in
        // analytics and advertising reporting. Do so in
        // the onResume methods of the primary Activities that an app may be
        // launched into.
        AppEventsLogger.activateApp(getActivity());
        // if (isTwitterLoggedInAlready()) {
        // mTwitterLoginButton.setBackgroundResource(R.drawable.twitter_login_button_inactive);
        // } else {
        // mTwitterLoginButton.setBackgroundResource(R.drawable.twitter_login_button);
        // }

        // Twitter Login
        // Get call back from Twitter side
        Uri uri = getActivity().getIntent().getData();
        if (uri != null && uri.toString().startsWith(BravoConstant.TWITTER_CALLBACK_URL)) {
            // oAuth verifier
            final String verifier = uri.getQueryParameter(BravoConstant.URL_TWITTER_OAUTH_VERIFIER);
            AIOLog.d("verifier = " + verifier);
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
                            // AlwaysSocialAppImpl.getInstance().getAlwaysSharePrefs().putBooleanValue(TwitterConstant.PREF_KEY_TWITTER_LOGIN, true);
                            // AlwaysSocialAppImpl.getInstance().getAlwaysSharePrefs().putStringValue(TwitterConstant.PREF_KEY_TWITTER_OAUTH_TOKEN, accessToken.getToken());
                            // AlwaysSocialAppImpl.getInstance().getAlwaysSharePrefs().putStringValue(TwitterConstant.PREF_KEY_TWITTER_OAUTH_SECRET, accessToken.getTokenSecret());
                            // RateLimitStatus rateLimit = mTwitter.getRateLimitStatus("application").get("/application/rate_limit_status");
                            // Log.d(TAG, "Application Remaining RateLimit =" + rateLimit.getRemaining());
                            // int limitRate = rateLimit.getRemaining();
                            // if (limitRate >= 20) {
                            // long userID = accessToken.getUserId();
                            // Log.d(TAG, "userID = " + userID);
                            // twitter4j.User user = mTwitter.showUser(userID);
                            // Log.d(TAG, "Twitter OAuth Token = " + accessToken.getToken() + " username = " + user.getName());
                            // SocialUserObject loggedInUser = new SocialUserObject(Long.toString(user.getId()), user.getName(), user.getBiggerProfileImageURL());
                            // AlwaysSocialAppImpl.getInstance().setLoggedInUser(GlobalConstant.SOCIAL_TYPE_TWITTER, loggedInUser);
                            // }
                        }
                    }
                    catch (TwitterException e) {
                        AIOLog.d("Error Twitter OAuth Token = " + e.getMessage());
                        loginResult = TW_LOGIN_ERROR;
                    }
                    mHandler.sendEmptyMessage(loginResult);
                }
            });

            thread.start();
            // mTwitterLoginButton.setEnabled(false);
            // mViewType = AlwaysSocialAppImpl.getInstance().getViewType();
        }
    }

    public interface IShowPageBravoLogin {
        public void showPageBravoLogin();
    }

    public void setListener(IShowPageBravoLogin iShowPageBravoLogin) {
        this.mListener = iShowPageBravoLogin;
    }

    public void onClickTwitterLoginButton() {
        // if not -> come to log in function
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setOAuthConsumerKey(BravoConstant.TWITTER_CONSUMER_KEY);
        builder.setOAuthConsumerSecret(BravoConstant.TWITTER_CONSUMER_SECRET);
        Configuration configuration = builder.build();

        TwitterFactory factory = new TwitterFactory(configuration);
        mTwitter = factory.getInstance();
        if (!isTwitterLoggedInAlready())
        {
            Thread thread = new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        mTwitterRequestToken = mTwitter.getOAuthRequestToken(BravoConstant.TWITTER_CALLBACK_URL);
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mTwitterRequestToken.getAuthenticationURL()));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        getActivity().startActivity(intent);
                        getActivity().finish();
                    } catch (TwitterException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        } else {
            Intent intent = new Intent(getActivity(), HomeActivity.class);
            startActivity(intent);
        }
    }

    private boolean isTwitterLoggedInAlready() {
        return false;
    }

    @SuppressLint("HandlerLeak")
    public final Handler mHandler = new Handler() {

                                      @Override
                                      public void handleMessage(Message msg) {
                                          AIOLog.d("handleMessage msg.what = " + msg.what);
                                          switch (msg.what) {
                                          case TW_LOGIN_ERROR:
                                              // mTwitterLoginButton.setEnabled(true);
                                              // mTwitterLoginButton.setBackgroundResource(R.drawable.twitter_login_button);
                                              break;
                                          case TW_LOGIN_SUCCESS:
                                              // mTwitterLoginButton.setEnabled(true);
                                              // mTwitterLoginButton.setBackgroundResource(R.drawable.twitter_login_button_inactive);
                                              break;
                                          default:
                                              break;
                                          }
                                          super.handleMessage(msg);
                                      }

                                  };

    @Override
    public void onError(String errorMsg) {
        // Do something with the error message
        Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onImageFetched(Bitmap bmp) {

    }

    @Override
    public void onAccessGrant(String accessToken) {
        AIOLog.d("accessToken: "+accessToken);
        // with the access token you can perform any request to foursquare.
        mEasyFoursquareAsync.getUserInfo(new UserInfoRequestListener() {

            @Override
            public void onError(String errorMsg) {
                // Some error getting user info
                Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onUserInfoFetched(User user) {
                // OWww. did i already got user!?
                // if (user.getBitmapPhoto() == null) {
                // UserImageRequest request = new UserImageRequest(getActivity(), getActivity());
                // request.execute(user.getPhoto());
                // } else {
                // userImage.setImageBitmap(user.getBitmapPhoto());
                // }
                // userName.setText(user.getFirstName() + " " + user.getLastName());
                // viewSwitcher.showNext();
                // Toast.makeText(MainActivity.this, "Got it!", Toast.LENGTH_LONG).show();
                AIOLog.d("user 4square:" + user);
            }
        });
    }
}
