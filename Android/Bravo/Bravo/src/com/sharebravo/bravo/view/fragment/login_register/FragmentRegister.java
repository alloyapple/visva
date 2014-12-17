package com.sharebravo.bravo.view.fragment.login_register;

import java.util.Locale;
import java.util.TimeZone;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
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

import com.facebook.Session;
import com.sharebravo.bravo.R;
import com.sharebravo.bravo.control.activity.ActivityLogin_Register;
import com.sharebravo.bravo.model.user.BravoUser;
import com.sharebravo.bravo.sdk.log.AIOLog;
import com.sharebravo.bravo.utils.BravoConstant;
import com.sharebravo.bravo.view.fragment.FragmentBasic;
import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.entities.Profile;
import com.sromku.simple.fb.listeners.OnLoginListener;
import com.sromku.simple.fb.listeners.OnProfileListener;

public class FragmentRegister extends FragmentBasic implements AccessTokenRequestListener, ImageRequestListener {
    // ====================Constant Define=================
    public static final int        TW_LOGIN_ERROR   = -1;
    public static final int        TW_LOGIN_SUCCESS = 0;
    // ====================Class Define====================
    // ====================Variable Define=================
    private Button                 mBtnBravoRegister;
    private IShowPageBravoRegister mListener;
    private TextView               mTextViewFacebookRegister;
    private TextView               mTextViewTwitterRegister;
    private TextView               mTextViewFoursquareRegister;
    private EasyFoursquareAsync    mEasyFoursquareAsync;
    private static int             mResgisterType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = (ViewGroup) inflater.inflate(R.layout.page_fragment_register, container);
        mResgisterType = BravoConstant.NO_REGISTER_SNS;

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
        mTextViewFacebookRegister = (TextView) root.findViewById(R.id.text_facebook_register);
        mTextViewFacebookRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mResgisterType = BravoConstant.REGISTER_BY_FACEBOOK;
                onClickTextViewFacebookLogin();
            }
        });

        /* twitter */
        mTextViewTwitterRegister = (TextView) root.findViewById(R.id.text_twitter_register);
        mTextViewTwitterRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mResgisterType = BravoConstant.REGISTER_BY_TWITTER;
                mListener.clickTwitterRegister(ActivityLogin_Register.TWITTER_TYPE_REGISTER);
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
    }

    public interface IShowPageBravoRegister {
        public void showPageBravoRegister();

        public void showPageUserInfo(BravoUser user);

        public void clickTwitterRegister(int twitterType);
    }

    public void setListener(IShowPageBravoRegister iShowPageBravoRegister) {
        this.mListener = iShowPageBravoRegister;
    }

    @Override
    public void onResume() {
        super.onResume();
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

    private void onFacebookUserConnected(Profile user) {
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
        Log.d("KieuThang", "mSimpleFacebook:" + mSimpleFacebook);
        if (mSimpleFacebook == null)
            return;
        mSimpleFacebook.login(onLoginListener);
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
