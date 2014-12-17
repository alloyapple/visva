package com.sharebravo.bravo.view.fragment.login_register;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
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

import com.facebook.Session;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sharebravo.bravo.R;
import com.sharebravo.bravo.control.activity.ActivityLogin_Register;
import com.sharebravo.bravo.control.activity.HomeActivity;
import com.sharebravo.bravo.model.response.ObGetUserInfo;
import com.sharebravo.bravo.model.user.BravoUser;
import com.sharebravo.bravo.model.user.ObGetLoginedUser;
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
import com.sharebravo.bravo.view.fragment.FragmentBasic;
import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.entities.Profile;
import com.sromku.simple.fb.listeners.OnLoginListener;
import com.sromku.simple.fb.listeners.OnProfileListener;

public class FragmentLogin extends FragmentBasic implements AccessTokenRequestListener, ImageRequestListener {
    // ====================Constant Define=================
    // ====================Class Define====================
    private IShowPageBravoLogin mListener;
    // ====================Variable Define=================
    private TextView            mTextViewFacebookLogin;
    private TextView            mTextViewTwitterLogin;
    private TextView            mTextViewFourSquareLogin;
    private RelativeLayout      mLayoutBravoLogin;
    private EasyFoursquareAsync mEasyFoursquareAsync;
    private static int          mLoginType = BravoConstant.NO_LOGIN_SNS;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = (ViewGroup) inflater.inflate(R.layout.page_fragment_login, container);
        mLoginType = BravoConstant.NO_LOGIN_SNS;

        initializeView(root);
        return root;
    }

    private void initializeView(View root) {
        mLayoutBravoLogin = (RelativeLayout) root.findViewById(R.id.layout_bravo_login);
        mLayoutBravoLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mLoginType = BravoConstant.LOGIN_BY_BRAVO_ACC;
                mListener.showPageBravoLogin();
            }
        });

        /* facebook */
        mTextViewFacebookLogin = (TextView) root.findViewById(R.id.text_facebook_login);
        mTextViewFacebookLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mLoginType = BravoConstant.LOGIN_BY_FACEBOOK;
                String _preKeySessionRegisteredByFacebook = BravoSharePrefs.getInstance(getActivity()).getStringValue(
                        BravoConstant.PREF_KEY_SESSION_REGISTER_BY_FACEBOOK);

                AIOLog.d("_preKeySessionRegisteredByFacebook:" + _preKeySessionRegisteredByFacebook);
                if (!StringUtility.isEmpty(_preKeySessionRegisteredByFacebook)) {
                    // login bravo by bravo userID and access token of facebook
                    String userID = BravoUtils.getUserIdFromUserBravoInfo(getActivity(), _preKeySessionRegisteredByFacebook, mLoginType);
                    String accessToken = BravoUtils.getAccessTokenFromUserBravoInfo(getActivity(), _preKeySessionRegisteredByFacebook, mLoginType);
                    AIOLog.d("accessToken:" + accessToken);
                    onLoginBravoBySNS(userID, accessToken, mLoginType);
                } else {
                    // register bravo by access token of facebook
                    Log.d("KieuThang", "mSimpleFacebook:" + mSimpleFacebook);
                    if (mSimpleFacebook == null)
                        return;
                    mSimpleFacebook.login(onLoginListener);
                }
            }
        });

        /* twitter */
        mTextViewTwitterLogin = (TextView) root.findViewById(R.id.text_twitter_login);
        mTextViewTwitterLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mLoginType = BravoConstant.LOGIN_BY_TWITTER;
                String _preKeySessionRegisteredByTwitter = BravoSharePrefs.getInstance(getActivity()).getStringValue(
                        BravoConstant.PREF_KEY_SESSION_REGISTER_BY_TWITTER);
                AIOLog.d("_preKeySessionRegisteredByTwitter:" + _preKeySessionRegisteredByTwitter);
                if (!StringUtility.isEmpty(_preKeySessionRegisteredByTwitter)) {

                    // login bravo by bravo userID and access token of facebook
                    String userID = BravoUtils.getUserIdFromUserBravoInfo(getActivity(), _preKeySessionRegisteredByTwitter, mLoginType);
                    String accessToken = BravoUtils.getAccessTokenFromUserBravoInfo(getActivity(), _preKeySessionRegisteredByTwitter, mLoginType);
                    onLoginBravoBySNS(userID, accessToken, mLoginType);
                } else {
                    mListener.clickTwitterRegister(ActivityLogin_Register.TWITTER_TYPE_LOGIN);
                }
            }
        });

        /* foursquare */
        mTextViewFourSquareLogin = (TextView) root.findViewById(R.id.text_4square_login);
        mTextViewFourSquareLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mLoginType = BravoConstant.LOGIN_BY_4SQUARE;
                String _preKeySessionRegisteredBy4Square = BravoSharePrefs.getInstance(getActivity()).getStringValue(
                        BravoConstant.PREF_KEY_SESSION_REGISTER_BY_4SQUARE);
                AIOLog.d("_preKeySessionRegisteredByTwitter:" + _preKeySessionRegisteredBy4Square);
                if (!StringUtility.isEmpty(_preKeySessionRegisteredBy4Square)) {

                    // login bravo by bravo userID and access token of facebook
                    String userID = BravoUtils.getUserIdFromUserBravoInfo(getActivity(), _preKeySessionRegisteredBy4Square, mLoginType);
                    String accessToken = BravoUtils.getAccessTokenFromUserBravoInfo(getActivity(), _preKeySessionRegisteredBy4Square, mLoginType);
                    onLoginBravoBySNS(userID, accessToken, mLoginType);
                } else {
                    onClickLogin4Square();
                }
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public interface IShowPageBravoLogin {
        public void showPageBravoLogin();

        public void clickTwitterRegister(int twitterType);
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
    private void onLoginBravoBySNS(String userID, String accessToken, final int loginType) {
        AIOLog.d("userID:" + userID + ", accessToken:" + accessToken);
        String url = BravoWebServiceConfig.URL_GET_USER_INFO_WITH_BRAVO_ACCOUNT + "/" + userID;
        List<NameValuePair> params = ParameterFactory.createSubParamsLoginBySNS(userID, accessToken);
        AsyncHttpGet getLoginRequest = new AsyncHttpGet(getActivity(), new AsyncHttpResponseProcess(getActivity(), this) {
            @Override
            public void processIfResponseSuccess(String response) {
                AIOLog.d("requestToLoginByBravoAccount:" + response);
                Gson gson = new GsonBuilder().serializeNulls().create();
                ObGetUserInfo obGetUserInfo = gson.fromJson(response.toString(), ObGetUserInfo.class);
                if (obGetUserInfo == null) {
                    showToast(getActivity().getResources().getString(R.string.username_password_not_valid));
                } else {
                    /* save data */
                    BravoUtils.saveResponseToSharePreferences(getActivity(), loginType, response);

                    /* go to home screen */
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
                bravoUser.mRegisterType = BravoConstant.REGISTER_BY_4SQUARE;
                Toast.makeText(getActivity(), "Hello " + user.getFirstName() + " " + user.getLastName(), Toast.LENGTH_SHORT).show();

                requestToPostBravoUserbySNS(bravoUser);
            }
        });
    }

    /**
     * facebook login
     */
    private void onFacebookUserConnected(Profile profile) {
        AIOLog.d("profile:" + profile);
        if (profile == null)
            return;
        Session session = Session.getActiveSession();
        BravoUser bravoUser = new BravoUser();
        bravoUser.mUserEmail = "no_account" + System.currentTimeMillis() + "@nomail.com";
        bravoUser.mUserId = profile.getId();
        bravoUser.mUserName = profile.getName();
        bravoUser.mAuthenMethod = "Facebook";
        bravoUser.mTimeZone = TimeZone.getDefault().getID();
        Locale current = getResources().getConfiguration().locale;
        bravoUser.mLocale = current.toString();
        bravoUser.mForeign_Id = profile.getId();
        bravoUser.mUserPassWord = session.getAccessToken();
        bravoUser.mRegisterType = BravoConstant.REGISTER_BY_FACEBOOK;

        requestToPostBravoUserbySNS(bravoUser);

    }

    private void requestUserFacebookInfo() {
        if (mSimpleFacebook == null) {
            mSimpleFacebook = SimpleFacebook.getInstance(getActivity());
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
                onFacebookUserConnected(profile);
            }
        });
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
        AsyncHttpPost postRegister = new AsyncHttpPost(getActivity(), new AsyncHttpResponseProcess(getActivity(), this) {
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
                ObGetLoginedUser obPostUserFailed;

                if (status == String.valueOf(BravoWebServiceConfig.STATUS_RESPONSE_DATA_SUCCESS)) {
                    /* save data */
                    BravoUtils.saveResponseToSharePreferences(getActivity(), bravoUser.mRegisterType, response);

                    /* go to home screen */
                    Intent homeIntent = new Intent(getActivity(), HomeActivity.class);
                    homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    getActivity().startActivity(homeIntent);
                    getActivity().finish();
                } else {
                    obPostUserFailed = gson.fromJson(response.toString(), ObGetLoginedUser.class);
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
                                                  Toast.makeText(getActivity(), String.format("You didn't accept %s permissions", type.name()),
                                                          Toast.LENGTH_SHORT).show();
                                              }
                                          };
}
