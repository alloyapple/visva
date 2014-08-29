package com.sharebravo.bravo.view.fragment;

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
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginTextView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sharebravo.bravo.MyApplication;
import com.sharebravo.bravo.R;
import com.sharebravo.bravo.control.activity.HomeActivity;
import com.sharebravo.bravo.control.activity.WebAuthActivity;
import com.sharebravo.bravo.model.response.ObGetUserInfo;
import com.sharebravo.bravo.model.response.ObPostUserFailed;
import com.sharebravo.bravo.model.user.BravoUser;
import com.sharebravo.bravo.sdk.log.AIOLog;
import com.sharebravo.bravo.sdk.util.network.AsyncHttpGet;
import com.sharebravo.bravo.sdk.util.network.AsyncHttpPost;
import com.sharebravo.bravo.sdk.util.network.AsyncHttpResponseProcess;
import com.sharebravo.bravo.sdk.util.network.ParameterFactory;
import com.sharebravo.bravo.utils.BravoConstant;
import com.sharebravo.bravo.utils.BravoSharePrefs;
import com.sharebravo.bravo.utils.BravoUtils;
import com.sharebravo.bravo.utils.BravoWebServiceConfig;
import com.sharebravo.bravo.utils.StringUtility;

public class FragmentLogin extends FragmentBasic implements AccessTokenRequestListener, ImageRequestListener, LoginTextView.UserInfoChangedCallback {
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
        mTextViewFacebookLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mLoginType = BravoConstant.LOGIN_FACEBOOK;
                String _preKeySessionRegisteredByFacebook = BravoSharePrefs.getInstance(getActivity()).getStringValue(
                        BravoConstant.PREF_KEY_SESSION_REGISTER_BY_FACEBOOK);
                AIOLog.d("_preKeySessionRegisteredByFacebook:" + _preKeySessionRegisteredByFacebook);
                if (!StringUtility.isEmpty(_preKeySessionRegisteredByFacebook)) {

                    // login bravo by bravo userID and access token of facebook
                    String userID = BravoUtils.getUserIdFromUserBravoInfo(getActivity(), _preKeySessionRegisteredByFacebook);
                    String accessToken = BravoUtils.getAccessTokenFromUserBravoInfo(getActivity(), _preKeySessionRegisteredByFacebook);
                    onLoginBravoBySNS(userID, accessToken);
                } else {

                    // register bravo by access token of facebook
                    onClickTextViewFacebookLogin();
                }
            }
        });
        mTextViewFacebookLogin.setUserInfoChangedCallback(this);

        /* twitter */
        mTextViewTwitterLogin = (TextView) root.findViewById(R.id.text_twitter_login);
        mTextViewTwitterLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mLoginType = BravoConstant.LOGIN_TWITTER;
                String _preKeySessionRegisteredByTwitter = BravoSharePrefs.getInstance(getActivity()).getStringValue(
                        BravoConstant.PREF_KEY_SESSION_REGISTER_BY_TWITTER);
                AIOLog.d("_preKeySessionRegisteredByTwitter:" + _preKeySessionRegisteredByTwitter);
                if (!StringUtility.isEmpty(_preKeySessionRegisteredByTwitter)) {

                    // login bravo by bravo userID and access token of facebook
                    String userID = BravoUtils.getUserIdFromUserBravoInfo(getActivity(), _preKeySessionRegisteredByTwitter);
                    String accessToken = BravoUtils.getAccessTokenFromUserBravoInfo(getActivity(), _preKeySessionRegisteredByTwitter);
                    onLoginBravoBySNS(userID, accessToken);
                } else {
                    onClickTwitterLoginButton();
                }
            }
        });

        /* foursquare */
        mTextViewFourSquareLogin = (TextView) root.findViewById(R.id.text_4square_login);
        mTextViewFourSquareLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mLoginType = BravoConstant.LOGIN_4SQUARE;
                String _preKeySessionRegisteredBy4Square = BravoSharePrefs.getInstance(getActivity()).getStringValue(
                        BravoConstant.PREF_KEY_SESSION_REGISTER_BY_4SQUARE);
                AIOLog.d("_preKeySessionRegisteredByTwitter:" + _preKeySessionRegisteredBy4Square);
                if (!StringUtility.isEmpty(_preKeySessionRegisteredBy4Square)) {

                    // login bravo by bravo userID and access token of facebook
                    String userID = BravoUtils.getUserIdFromUserBravoInfo(getActivity(), _preKeySessionRegisteredBy4Square);
                    String accessToken = BravoUtils.getAccessTokenFromUserBravoInfo(getActivity(), _preKeySessionRegisteredBy4Square);
                    onLoginBravoBySNS(userID, accessToken);
                } else {
                    onClickLogin4Square();
                }
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* facebook api */
        mFacebookCallback = new Session.StatusCallback() {
            @Override
            public void call(final Session session, final SessionState state, final Exception exception) {
                AIOLog.d("session callback login:" + session + "state: " + state);
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
                            BravoUser _bravoUser = new BravoUser();
                            _bravoUser.mUserEmail = "no_tw_account" + System.currentTimeMillis() + "@nomail.com";
                            _bravoUser.mUserName = user.getName();
                            _bravoUser.mUserId = user.getId() + "";
                            _bravoUser.mAuthenMethod = "Twitter";
                            _bravoUser.mTimeZone = TimeZone.getDefault().getID();
                            Locale current = getResources().getConfiguration().locale;
                            _bravoUser.mLocale = current.toString();
                            _bravoUser.mForeign_Id = String.valueOf(user.getId());
                            _bravoUser.mUserPassWord = accessToken.getToken();
                            _bravoUser.mRegisterType = BravoConstant.REGISTER_TYPE_TWITTER;


                            requestToPostBravoUserbySNS(_bravoUser);

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
     * login bravo by sns after authen
     * 
     * @param userID
     * @param accessToken
     */
    private void onLoginBravoBySNS(String userID, String accessToken) {
        String url = BravoWebServiceConfig.URL_GET_USER_INFO_WITH_BRAVO_ACCOUNT + "/" + userID;
        List<NameValuePair> params = ParameterFactory.createSubParamsLoginBySNS(userID, accessToken);
        AsyncHttpGet getLoginRequest = new AsyncHttpGet(getActivity(), new AsyncHttpResponseProcess(getActivity()) {
            @Override
            public void processIfResponseSuccess(String response) {
                AIOLog.d("requestToLoginByBravoAccount:" + response);
                Gson gson = new GsonBuilder().serializeNulls().create();
                ObGetUserInfo obGetUserInfo = gson.fromJson(response.toString(), ObGetUserInfo.class);
                if (obGetUserInfo == null) {
                    showToast(getActivity().getResources().getString(R.string.username_password_not_valid));
                } else if (StringUtility.isEmpty(obGetUserInfo.data.New_Access_Token)) {
                    showToast(getActivity().getResources().getString(R.string.username_password_not_valid));
                } else {
                    Intent homeIntent = new Intent(getActivity(), HomeActivity.class);
                    startActivity(homeIntent);
                    getActivity().finish();
                }
            }

            @Override
            public void processIfResponseFail() {
                AIOLog.d("response error");
            }
        }, params, true);
        getLoginRequest.execute(url);
    }

    /**
     * twitter login and authen
     */
    private void onClickTwitterLoginButton() {
        AIOLog.d("isTwitterLoggedInAlready:" + isTwitterLoggedInAlready());
        if (mTwitter == null) {
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
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
    private void onClickLogin4Square() {
        /* 4square api */
        // ask for access
        mEasyFoursquareAsync = new EasyFoursquareAsync(getActivity());
        mEasyFoursquareAsync.requestAccess(this);
    }

    @Override
    public void onError(String errorMsg) {
        // Do something with the error message
        Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_LONG).show();
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
                // }
                AIOLog.d("user 4square:" + user);
                if (user == null)
                    return;
                BravoUser bravoUser = new BravoUser();
                bravoUser.mUserEmail = "no_4q_account" + System.currentTimeMillis() + "@nomail.com";
                bravoUser.mUserId = String.valueOf(user.getId());
                bravoUser.mUserName = user.getFirstName() + " " + user.getLastName();
                bravoUser.mAuthenMethod = "Foursquare";
                bravoUser.mTimeZone = TimeZone.getDefault().getID();
                Locale current = getResources().getConfiguration().locale;
                bravoUser.mLocale = current.toString();
                bravoUser.mForeign_Id = String.valueOf(user.getId());
                bravoUser.mUserPassWord = accessToken;
                bravoUser.mRegisterType = BravoConstant.REGISTER_TYPE_FOURSQUARE;
                Toast.makeText(getActivity(), "Hello " + user.getFirstName() + " " + user.getLastName(), Toast.LENGTH_SHORT).show();

                requestToPostBravoUserbySNS(bravoUser);

            }
        });
    }

    /**
     * facebook login
     */

    @Override
    public void onUserInfoFetched(GraphUser user) {
        AIOLog.d("user at login facebook:" + user);
        if (mLoginType == BravoConstant.LOGIN_FACEBOOK)
            onFacebookUserConnected(user);
    }

    private void onFacebookUserConnected(GraphUser user) {
        AIOLog.d("user:" + user);
        if (user == null)
            return;
        Session session = Session.getActiveSession();
        BravoUser bravoUser = new BravoUser();
        bravoUser.mUserEmail = "no_account" + System.currentTimeMillis() + "@nomail.com";
        bravoUser.mUserId = user.getId();
        bravoUser.mUserName = user.getName();
        bravoUser.mAuthenMethod = "Facebook";
        bravoUser.mTimeZone = TimeZone.getDefault().getID();
        Locale current = getResources().getConfiguration().locale;
        bravoUser.mLocale = current.toString();
        bravoUser.mForeign_Id = user.getId();
        bravoUser.mUserPassWord = session.getAccessToken();
        bravoUser.mRegisterType = BravoConstant.REGISTER_TYPE_FACEBOOK;

        requestToPostBravoUserbySNS(bravoUser);

    }

    public void onClickTextViewFacebookLogin() {
        Session session = Session.getActiveSession();
        if (session == null || session.isClosed() || session.getState() == null || !session.getState().isOpened()) {
            mTextViewFacebookLogin.onClickLoginFb();
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
        AsyncHttpPost postRegister = new AsyncHttpPost(getActivity(), new AsyncHttpResponseProcess(getActivity()) {
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
                ObPostUserFailed obPostUserFailed;
                if (status == String.valueOf(BravoWebServiceConfig.STATUS_RESPONSE_DATA_SUCCESS)) {
                    if (bravoUser.mRegisterType == BravoConstant.REGISTER_TYPE_FACEBOOK) {
                        BravoSharePrefs.getInstance(getActivity()).putStringValue(BravoConstant.PREF_KEY_SESSION_REGISTER_BY_FACEBOOK, response);
                        BravoSharePrefs.getInstance(getActivity()).putIntValue(BravoConstant.PREF_KEY_SESSION_REGISTER_TYPE,
                                BravoConstant.REGISTER_TYPE_FACEBOOK);
                    } else if (bravoUser.mRegisterType == BravoConstant.REGISTER_TYPE_TWITTER) {
                        BravoSharePrefs.getInstance(getActivity()).putIntValue(BravoConstant.PREF_KEY_SESSION_REGISTER_TYPE,
                                BravoConstant.REGISTER_TYPE_TWITTER);
                        BravoSharePrefs.getInstance(getActivity()).putStringValue(BravoConstant.PREF_KEY_SESSION_REGISTER_BY_TWITTER, response);
                    } else if (bravoUser.mRegisterType == BravoConstant.REGISTER_TYPE_FOURSQUARE) {
                        BravoSharePrefs.getInstance(getActivity()).putIntValue(BravoConstant.PREF_KEY_SESSION_REGISTER_TYPE,
                                BravoConstant.REGISTER_TYPE_FOURSQUARE);
                        BravoSharePrefs.getInstance(getActivity()).putStringValue(BravoConstant.PREF_KEY_SESSION_REGISTER_BY_4SQUARE, response);
                    }
                    Intent homeIntent = new Intent(getActivity(), HomeActivity.class);
                    homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    getActivity().startActivity(homeIntent);
                    getActivity().finish();
                } else {
                    obPostUserFailed = gson.fromJson(response.toString(), ObPostUserFailed.class);
                    showToast(obPostUserFailed.error);
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
