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

public class FragmentRegister extends FragmentBasic implements AccessTokenRequestListener, ImageRequestListener {
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
    private int                    mClickRegisterFB = 0;

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
                mResgisterType = BravoConstant.REGISTER_BRAVO;
                mListener.showPageBravoRegister();
            }
        });

        /* facebook */
        mTextViewFacebookRegister = (LoginTextView) root.findViewById(R.id.text_facebook_register);
        mTextViewFacebookRegister.setUserInfoChangedCallback(new LoginTextView.UserInfoChangedCallback() {
            @Override
            public void onUserInfoFetched(GraphUser user) {
                mResgisterType = BravoConstant.REGISTER_FACEBOOK;
                mClickRegisterFB++;
                if (mClickRegisterFB < 2)
                    return;
                AIOLog.e("user:" + user);
                if (user == null)
                    return;
                BravoUser bravoUser = new BravoUser();
                bravoUser.mUserEmail = "no_account" + System.currentTimeMillis() + "@nomail.com";
                bravoUser.mUserId = user.getId();
                bravoUser.mUserName = user.getName();
                bravoUser.mAuthenMethod = "Facebook";
                bravoUser.mTimeZone = TimeZone.getDefault().getID();
                Locale current = getResources().getConfiguration().locale;
                bravoUser.mLocale = current.toString();
                bravoUser.mForeign_Id = "No";
                bravoUser.mUserPassWord = "No";
                bravoUser.mRegisterType = BravoConstant.REGISTER_TYPE_FACEBOOK;
                mListener.showPageUserInfo(bravoUser);
            }
        });

        /* twitter */
        mTextViewTwitterRegister = (TextView) root.findViewById(R.id.text_twitter_register);
        mTextViewTwitterRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mResgisterType = BravoConstant.REGISTER_TWITTER;
                onClickTwitterLoginButton();
            }
        });

        /* foursquare */
        mTextViewFoursquareRegister = (TextView) root.findViewById(R.id.text_4square_register);
        mTextViewFoursquareRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mResgisterType = BravoConstant.REGISTER_4SQUARE;
                onClickLogin4Square();
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFacebookCallback = new Session.StatusCallback() {
            @Override
            public void call(final Session session, final SessionState state,
                    final Exception exception) {
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
                                _bravoUser.mUserEmail = "no_account" + System.currentTimeMillis() + "@nomail.com";
                                _bravoUser.mUserName = user.getName();
                                _bravoUser.mUserId = user.getId() + "";
                                _bravoUser.mAuthenMethod = "Twitter";
                                _bravoUser.mTimeZone = TimeZone.getDefault().getID();
                                Locale current = getResources().getConfiguration().locale;
                                _bravoUser.mLocale = current.toString();
                                _bravoUser.mForeign_Id = "No";
                                _bravoUser.mUserPassWord = "No";
                                _bravoUser.mRegisterType = BravoConstant.REGISTER_TYPE_TWITTER;
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
            BravoUser _braBravoUser = new BravoUser();
            _braBravoUser.mUserEmail = "kane";
            _braBravoUser.mUserId = "kane";
            _braBravoUser.mUserName = "kane";
            mListener.showPageUserInfo(_braBravoUser);
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
        // TODO Auto-generated method stub

    }

    @Override
    public void onImageFetched(Bitmap bmp) {
        // TODO Auto-generated method stub

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
                AIOLog.d("user 4square:" + user);
                if (user == null)
                    return;
                Toast.makeText(getActivity(), "Hello " + user.getFirstName() + " " + user.getLastName(), Toast.LENGTH_SHORT).show();
                AIOLog.d("user: " + user.getFirstName());
                BravoUser bravoUser = new BravoUser();
                bravoUser.mUserEmail = "no_account" + System.currentTimeMillis() + "@nomail.com";
                bravoUser.mUserId = String.valueOf(user.getId());
                bravoUser.mUserName = user.getFirstName() + " " + user.getLastName();
                bravoUser.mAuthenMethod = "FourSquare";
                bravoUser.mTimeZone = TimeZone.getDefault().getID();
                Locale current = getResources().getConfiguration().locale;
                bravoUser.mLocale = current.toString();
                bravoUser.mForeign_Id = "No";
                bravoUser.mUserPassWord = "No";
                bravoUser.mRegisterType = BravoConstant.REGISTER_TYPE_FOURSQUARE;
                mListener.showPageUserInfo(bravoUser);
            }
        });
    }

    private void onClickLogin4Square() {
        mEasyFoursquareAsync = new EasyFoursquareAsync(getActivity());
        mEasyFoursquareAsync.requestAccess(this);
    }
}
