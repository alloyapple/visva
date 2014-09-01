package com.sharebravo.bravo.view.fragment;

import java.util.Locale;
import java.util.TimeZone;

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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import br.com.condesales.EasyFoursquareAsync;
import br.com.condesales.listeners.AccessTokenRequestListener;
import br.com.condesales.listeners.ImageRequestListener;
import br.com.condesales.listeners.UserInfoRequestListener;
import br.com.condesales.models.User;

import com.facebook.AppEventsLogger;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginTextView;
import com.sharebravo.bravo.MyApplication;
import com.sharebravo.bravo.R;
import com.sharebravo.bravo.control.activity.WebAuthActivity;
import com.sharebravo.bravo.model.user.BravoUser;
import com.sharebravo.bravo.sdk.log.AIOLog;
import com.sharebravo.bravo.utils.BravoConstant;

public class FragmentRegister extends FragmentBasic implements AccessTokenRequestListener, ImageRequestListener,
        LoginTextView.UserInfoChangedCallback {
    // ====================Constant Define=================
    public static final int        TW_LOGIN_ERROR   = -1;
    public static final int        TW_LOGIN_SUCCESS = 0;
    // ====================Class Define====================
    // ====================Variable Define=================
    private Button                 mBtnBravoRegister;
    private IShowPageBravoRegister mListener;
    private LoginTextView          mTextViewFacebookRegister;
    private TextView               mTextViewTwitterRegister;
    private TextView               mTextViewFoursquareRegister;
    private UiLifecycleHelper      mUiLifecycleHelper;
    private Session.StatusCallback mFacebookCallback;
    private static RequestToken    mTwitterRequestToken;
    protected static Twitter       mTwitter;
    private EasyFoursquareAsync    mEasyFoursquareAsync;
    private static int             mResgisterType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = (ViewGroup) inflater.inflate(R.layout.page_fragment_register, container);
        mResgisterType = BravoConstant.NO_REGISTER_SNS;

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
        mBtnBravoRegister = (Button) root.findViewById(R.id.btn_sign_up_bravo);
        mBtnBravoRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mResgisterType = BravoConstant.REGISTER_BY_BRAVO_ACC;
                mListener.showPageBravoRegister();
            }
        });

        /* facebook */
        mTextViewFacebookRegister = (LoginTextView) root.findViewById(R.id.text_facebook_register);
        mTextViewFacebookRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mResgisterType = BravoConstant.REGISTER_BY_FACEBOOK;
                onClickTextViewFacebookLogin();
            }
        });
        mTextViewFacebookRegister.setUserInfoChangedCallback(this);

        /* twitter */
        mTextViewTwitterRegister = (TextView) root.findViewById(R.id.text_twitter_register);
        mTextViewTwitterRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mResgisterType = BravoConstant.REGISTER_BY_TWITTER;
                onClickTwitterLoginButton();
            }
        });

        /* foursquare */
        mTextViewFoursquareRegister = (TextView) root.findViewById(R.id.text_4square_register);
        mTextViewFoursquareRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mResgisterType = BravoConstant.REGISTER_BY_4SQUARE;
                onClickLogin4Square();
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFacebookCallback = new Session.StatusCallback() {
            @Override
            public void call(final Session session, final SessionState state, final Exception exception) {
                AIOLog.d("session callback register:" + session + "state: " + state);
            }
        };
        mUiLifecycleHelper = new UiLifecycleHelper(getActivity(), mFacebookCallback);
        mUiLifecycleHelper.onCreate(savedInstanceState);
    }

    public interface IShowPageBravoRegister {
        public void showPageBravoRegister();

        public void showPageUserInfo(BravoUser user);
    }

    public void setListener(IShowPageBravoRegister iShowPageBravoRegister) {
        this.mListener = iShowPageBravoRegister;
    }

    @Override
    public void onResume() {
        super.onResume();
        Session session = Session.getActiveSession();
        boolean isActivedSession = session.isOpened();
        AIOLog.d("isActivedSession=" + isActivedSession);
        AppEventsLogger.activateApp(getActivity());

        AIOLog.d("isTwitterLogined:" + mResgisterType);
        if (isTwitterLoggedInAlready()) {
            AIOLog.d("isTwitterLogined");
            final String verifier = MyApplication.getInstance().getBravoSharePrefs().getStringValue(BravoConstant.PREF_KEY_TWITTER_OAUTH_VERIFIER);
            // if()

            Thread thread = new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        if (mTwitter == null || mTwitterRequestToken == null) {
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
                                mListener.showPageUserInfo(_bravoUser);
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

    /**
     * twitter login and authen
     */
    private void onClickTwitterLoginButton() {
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
            requestToGetTwitterUserInfo();
        }
    }

    private void requestToGetTwitterUserInfo() {
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setOAuthConsumerKey(BravoConstant.TWITTER_CONSUMER_KEY);
        builder.setOAuthConsumerSecret(BravoConstant.TWITTER_CONSUMER_SECRET);
        Configuration configuration = builder.build();

        TwitterFactory factory = new TwitterFactory(configuration);
        mTwitter = factory.getInstance();

        if (isTwitterLoggedInAlready()) {
            AIOLog.d("isTwitterLogined");
            final String verifier = MyApplication.getInstance().getBravoSharePrefs().getStringValue(BravoConstant.PREF_KEY_TWITTER_OAUTH_VERIFIER);
            // if()

            Thread thread = new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        if (mTwitter == null) {
                            AIOLog.d("mTwitter is null");
                            return;
                        }
                        AccessToken accessToken = mTwitter.getOAuthAccessToken();
                        if (accessToken != null) {
                            long userID = accessToken.getUserId();
                            AIOLog.d("userID = " + userID);
                            twitter4j.User user = mTwitter.showUser(userID);
                            AIOLog.d("Twitter OAuth Token = " + accessToken.getToken() + " username = " + user.getName());
                            if (user != null) {
                                BravoUser _bravoUser = new BravoUser();
                                _bravoUser.mUserEmail = "no_tw_account" + System.currentTimeMillis() + "@nomail.com";
                                _bravoUser.mUserName = user.getName();
                                _bravoUser.mUserId = user.getId() + "";
                                _bravoUser.mAuthenMethod = BravoConstant.TWITTER;
                                _bravoUser.mTimeZone = TimeZone.getDefault().getID();
                                Locale current = getResources().getConfiguration().locale;
                                _bravoUser.mLocale = current.toString();
                                _bravoUser.mForeign_Id = String.valueOf(user.getId());
                                _bravoUser.mUserPassWord = accessToken.getToken();
                                _bravoUser.mRegisterType = BravoConstant.REGISTER_BY_TWITTER;
                                mListener.showPageUserInfo(_bravoUser);
                            }
                        }
                    } catch (TwitterException e) {
                        AIOLog.d("Error Twitter OAuth Token = " + e.getMessage());
                    }
                }
            });
            thread.start();
        }
    }

    private boolean isTwitterLoggedInAlready() {
        int _loginType = MyApplication.getInstance().getBravoSharePrefs().getIntValue(BravoConstant.REGISTER_SNS_TYPE);
        if (_loginType == mResgisterType)
            return true;
        return false;
    }

    /**
     * foursquare api
     */
    @Override
    public void onError(String errorMsg) {

    }

    @Override
    public void onImageFetched(Bitmap bmp) {

    }

    @Override
    public void onAccessGrant(final String accessToken) {
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
                AIOLog.d("user 4square:" + user);
                if (user == null)
                    return;
                AIOLog.d("user: " + user.getFirstName());
                BravoUser bravoUser = new BravoUser();
                bravoUser.mUserEmail = "no_4q_account" + System.currentTimeMillis() + "@nomail.com";
                bravoUser.mUserId = String.valueOf(user.getId());
                bravoUser.mUserName = user.getFirstName() + " " + user.getLastName();
                bravoUser.mAuthenMethod = BravoConstant.FOURSQUARE;
                bravoUser.mTimeZone = TimeZone.getDefault().getID();
                Locale current = getResources().getConfiguration().locale;
                bravoUser.mLocale = current.toString();
                bravoUser.mForeign_Id = String.valueOf(user.getId());
                bravoUser.mUserPassWord = accessToken;
                bravoUser.mRegisterType = BravoConstant.REGISTER_BY_4SQUARE;
                mListener.showPageUserInfo(bravoUser);
            }
        });
    }

    private void onClickLogin4Square() {
        mEasyFoursquareAsync = new EasyFoursquareAsync(getActivity());
        mEasyFoursquareAsync.requestAccess(this);
    }

    /**
     * facebook login
     */

    @Override
    public void onUserInfoFetched(GraphUser user) {
        AIOLog.d("user at login facebook:" + user + " mResgisterType:" + mResgisterType);
        if (mResgisterType == BravoConstant.REGISTER_BY_FACEBOOK)
            onFacebookUserConnected(user);
    }

    private void onFacebookUserConnected(GraphUser user) {
        if (user == null)
            return;
        Session session = Session.getActiveSession();
        BravoUser bravoUser = new BravoUser();
        bravoUser.mUserEmail = "no_account" + System.currentTimeMillis() + "@nomail.com";
        bravoUser.mUserId = user.getId();
        bravoUser.mUserName = user.getName();
        bravoUser.mAuthenMethod = BravoConstant.FACEBOOK;
        bravoUser.mTimeZone = TimeZone.getDefault().getID();
        Locale current = getResources().getConfiguration().locale;
        bravoUser.mLocale = current.toString();
        bravoUser.mForeign_Id = user.getId();
        bravoUser.mUserPassWord = session.getAccessToken();
        bravoUser.mRegisterType = BravoConstant.REGISTER_BY_FACEBOOK;
        mListener.showPageUserInfo(bravoUser);
    }

    public void onClickTextViewFacebookLogin() {
        Session session = Session.getActiveSession();
        if (session == null || session.isClosed() || session.getState() == null || !session.getState().isOpened()) {
            mTextViewFacebookRegister.onClickLoginFb();
        } else {
            AIOLog.d("request user info:" + session);
            requestUserFacebookInfo(session);
        }
    }

    private void requestUserFacebookInfo(Session activeSession) {
        Request infoRequest = Request.newMeRequest(activeSession, new com.facebook.Request.GraphUserCallback() {

            @Override
            public void onCompleted(GraphUser user, Response response) {
                AIOLog.d("requestUserFacebookInfo:" + user);
                onFacebookUserConnected(user);
            }

        });
        Bundle params = new Bundle();
        params.putString("fields", "id, name, picture");
        infoRequest.setParameters(params);
        infoRequest.executeAsync();
    }
}
