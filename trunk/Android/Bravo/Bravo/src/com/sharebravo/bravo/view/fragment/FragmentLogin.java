package com.sharebravo.bravo.view.fragment;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
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
import com.sharebravo.bravo.MyApplication;
import com.sharebravo.bravo.R;
import com.sharebravo.bravo.control.activity.HomeActivity;
import com.sharebravo.bravo.control.activity.WebAuthActivity;
import com.sharebravo.bravo.model.user.BravoUser;
import com.sharebravo.bravo.sdk.log.AIOLog;
import com.sharebravo.bravo.utils.BravoConstant;

public class FragmentLogin extends FragmentBasic implements AccessTokenRequestListener, ImageRequestListener {
    // ====================Constant Define=================
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

    private static int             mLoginType = BravoConstant.NO_LOGIN_SNS;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = (ViewGroup) inflater.inflate(R.layout.page_fragment_login, container);
        mLoginType = BravoConstant.NO_LOGIN_SNS;

        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setOAuthConsumerKey(BravoConstant.TWITTER_CONSUMER_KEY);
        builder.setOAuthConsumerSecret(BravoConstant.TWITTER_CONSUMER_SECRET);
        Configuration configuration = builder.build();

        TwitterFactory factory = new TwitterFactory(configuration);
        mTwitter = factory.getInstance();

        initializeView(root);
        return root;
    }

    private void initializeView(View root) {
        mLayoutBravoLogin = (RelativeLayout) root.findViewById(R.id.layout_bravo_login);
        mLayoutBravoLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mLoginType = BravoConstant.LOGIN_BRAVO;
                mListener.showPageBravoLogin();
            }
        });

        /* facebook */
        mTextViewFacebookLogin = (LoginTextView) root.findViewById(R.id.text_facebook_login);
        mTextViewFacebookLogin.setUserInfoChangedCallback(new LoginTextView.UserInfoChangedCallback() {
            @Override
            public void onUserInfoFetched(GraphUser user) {
                mLoginType = BravoConstant.LOGIN_FACEBOOK;
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
                mLoginType = BravoConstant.NO_LOGIN_SNS;
            }
        });

        /* twitter */
        mTextViewTwitterLogin = (TextView) root.findViewById(R.id.text_twitter_login);
        mTextViewTwitterLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mLoginType = BravoConstant.LOGIN_TWITTER;
                onClickTwitterLoginButton();
            }
        });

        /* foursquare */
        mTextViewFourSquareLogin = (TextView) root.findViewById(R.id.text_4square_login);
        mTextViewFourSquareLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mLoginType = BravoConstant.LOGIN_4SQUARE;
                onClickLogin4Square();
            }
        });
    }

    private void onClickLogin4Square() {
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
        // facebook
        Session session = Session.getActiveSession();
        boolean isActivedSession = session.isOpened();
        AIOLog.d("isActivedSession=" + isActivedSession);
        AppEventsLogger.activateApp(getActivity());

        AIOLog.d("mLoginType:" + mLoginType);
        if (isTwitterLoggedInAlready()) {
            AIOLog.d("isTwitterLogined");
            final String verifier = MyApplication.getInstance().getBravoSharePrefs().getStringValue(BravoConstant.PREF_KEY_TWITTER_OAUTH_VERIFIER);
            // if()
            if (mTwitter == null) {
                AIOLog.d("mTwitter is null");
            }
            Thread thread = new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        if (mTwitter == null) {
                            AIOLog.d("mTwitter is null");
                            return;
                        }
                        AccessToken accessToken = mTwitter.getOAuthAccessToken(mTwitterRequestToken, verifier);
                        if (accessToken != null) {

                            long userID = accessToken.getUserId();
                            AIOLog.d("userID = " + userID);
                            twitter4j.User user = mTwitter.showUser(userID);

                            AIOLog.d("Twitter OAuth Token = " + accessToken.getToken() + " username = " + user.getName());

                            if (user != null) {
                                Intent homeIntent = new Intent(getActivity(), HomeActivity.class);
                                startActivity(homeIntent);
                                getActivity().finish();
                            }
                        }
                    }
                    catch (TwitterException e) {
                        AIOLog.d("Error Twitter OAuth Token = " + e.getMessage());
                    }
                }
            });
            thread.start();
        }

    }

    public interface IShowPageBravoLogin {
        public void showPageBravoLogin();
    }

    public void setListener(IShowPageBravoLogin iShowPageBravoLogin) {
        this.mListener = iShowPageBravoLogin;
    }

    /**
     * twitter login and authen
     */
    private void onClickTwitterLoginButton() {
        AIOLog.d("isTwitterLoggedInAlready:" + isTwitterLoggedInAlready());
        if (mTwitter == null){
            AIOLog.e("mTwitter is null");
            return;
        }
        if (!isTwitterLoggedInAlready()) {

            Thread thread = new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        mTwitterRequestToken = mTwitter.getOAuthRequestToken(BravoConstant.TWITTER_CALLBACK_URL);
                        if (getActivity() == null)
                            return;
                        Intent intent = new Intent(getActivity(), WebAuthActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        intent.putExtra("URL", mTwitterRequestToken.getAuthenticationURL());
                        getActivity().startActivityForResult(intent, 0);
                    } catch (TwitterException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        } else {
            createThreadGetTWUserTOLoginBravo();
        }
    }

    private void createThreadGetTWUserTOLoginBravo() {
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    if (mTwitter == null) {
                        AIOLog.e("mTwitter is null");
                        return;
                    }
                    AccessToken accessToken = mTwitter.getOAuthAccessToken();
                    AIOLog.d("accessToken:" + accessToken);
                    if (accessToken != null) {
                        long userID = accessToken.getUserId();
                        AIOLog.d("userID = " + userID);
                        twitter4j.User user = mTwitter.showUser(userID);
                        AIOLog.d("Twitter OAuth Token = " + accessToken.getToken() + " username = " + user.getName());
                        if (user != null) {
                            BravoUser _bravoUser = new BravoUser();
                            _bravoUser.mUserEmail = user.getName();
                            _bravoUser.mUserName = user.getName();
                            _bravoUser.mUserId = user.getId() + "";
                            Intent intent = new Intent(getActivity(), HomeActivity.class);
                            startActivity(intent);
                        }
                    }
                }
                catch (TwitterException e) {
                    AIOLog.d("Error Twitter OAuth Token = " + e.getMessage());
                }
            }
        });
        thread.start();

    }

    private boolean isTwitterLoggedInAlready() {
        int _loginType = MyApplication.getInstance().getBravoSharePrefs().getIntValue(BravoConstant.LOGIN_SNS_TYPE);
        if (_loginType == mLoginType)
            return true;
        return false;
    }

    /**
     * foursquare login
     */
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
        AIOLog.d("accessToken: " + accessToken);
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
                AIOLog.d("user 4square:" + user);
                if (user == null)
                    return;
                Toast.makeText(getActivity(), "Hello " + user.getFirstName() + " " + user.getLastName(), Toast.LENGTH_SHORT).show();
                AIOLog.d("user: " + user.getFirstName());
                Intent homeIntent = new Intent(getActivity(), HomeActivity.class);
                startActivity(homeIntent);
            }
        });
    }
}
