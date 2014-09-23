package com.sharebravo.bravo.view.fragment.setting;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;
import br.com.condesales.EasyFoursquareAsync;
import br.com.condesales.listeners.AccessTokenRequestListener;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.ToggleButtonLogin;
import com.facebook.widget.ToggleButtonLogin.UserInfoChangedCallback;
import com.sharebravo.bravo.MyApplication;
import com.sharebravo.bravo.R;
import com.sharebravo.bravo.control.activity.ActivitySplash;
import com.sharebravo.bravo.control.activity.HomeActivity;
import com.sharebravo.bravo.model.SessionLogin;
import com.sharebravo.bravo.sdk.log.AIOLog;
import com.sharebravo.bravo.sdk.util.network.AsyncHttpDelete;
import com.sharebravo.bravo.sdk.util.network.AsyncHttpResponseProcess;
import com.sharebravo.bravo.utils.BravoConstant;
import com.sharebravo.bravo.utils.BravoSharePrefs;
import com.sharebravo.bravo.utils.BravoUtils;
import com.sharebravo.bravo.utils.BravoWebServiceConfig;
import com.sharebravo.bravo.view.fragment.FragmentBasic;

public class FragmentSetting extends FragmentBasic implements AccessTokenRequestListener {
    // =======================Constant Define==============
    // =======================Class Define=================
    private IShowPageTermOfUse     iShowPageTermOfUse;
    private EasyFoursquareAsync    mEasyFoursquareAsync;
    // =======================Variable Define==============
    private TextView               mTextTermOfUse;
    private TextView               mTextShareWithFriends;
    private TextView               mTextUpdateUserInfo;
    private TextView               mTextDeleteMyAccount;
    private ToggleButtonLogin      mToggleBtnPostOnFacebook;
    private ToggleButton           mToggleBtnPostOnTwitter;
    private ToggleButton           mToggleBtnPostOnFourSquare;
    private ToggleButton           mToggleBtnCommentNotifications;
    private ToggleButton           mToggleBtnFollowNotifications;
    private ToggleButton           mToggleBtnFavouriteNotifications;
    private ToggleButton           mToggleBtnTotalBravoNotifications;
    private ToggleButton           mToggleBtnBravoNotifications;
    private Button                 mBtnBack;

    private UiLifecycleHelper      mUiLifecycleHelper;
    private Session.StatusCallback mFacebookCallback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = (ViewGroup) inflater.inflate(R.layout.page_settings, container);
        mHomeActionListener = (HomeActivity) getActivity();

        initializeView(root);
        initializeData();
        handlerToggleBtnEvents();
        return root;
    }

    private void handlerToggleBtnEvents() {
        mToggleBtnPostOnFacebook.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    onCheckedToggleBtnFacebook(isChecked);
                else
                    BravoSharePrefs.getInstance(getActivity()).putBooleanValue(BravoConstant.PREF_KEY_POST_ON_FACEBOOK, false);
            }
        });
        mToggleBtnPostOnFacebook.setUserInfoChangedCallback(new UserInfoChangedCallback() {

            @Override
            public void onUserInfoFetched(GraphUser user) {
                if (user == null) {
                    BravoSharePrefs.getInstance(getActivity()).putBooleanValue(BravoConstant.PREF_KEY_POST_ON_FACEBOOK, false);
                    mToggleBtnPostOnFacebook.setChecked(false);
                } else {
                    BravoSharePrefs.getInstance(getActivity()).putBooleanValue(BravoConstant.PREF_KEY_POST_ON_FACEBOOK, false);
                    mToggleBtnPostOnFacebook.setChecked(true);
                }
            }
        });
        mToggleBtnPostOnTwitter.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mToggleBtnPostOnTwitter.setChecked(false);
                if (isChecked){
                    mHomeActionListener.requestToLoginSNS(BravoConstant.TWITTER);
                }
                else
                    BravoSharePrefs.getInstance(getActivity()).putBooleanValue(BravoConstant.PREF_KEY_POST_ON_TWITTER, false);
            }
        });
        mToggleBtnPostOnFourSquare.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mToggleBtnPostOnFourSquare.setChecked(false);
                if (isChecked)
                    onCheckedToggleBtnFourSquare(isChecked);
                else
                    BravoSharePrefs.getInstance(getActivity()).putBooleanValue(BravoConstant.PREF_KEY_POST_ON_FOURSQUARE, false);
            }
        });

        /* Notifications */
        mToggleBtnBravoNotifications.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                BravoSharePrefs.getInstance(getActivity()).putBooleanValue(BravoConstant.PREF_KEY_BRAVO_NOTIFICATIONS, isChecked);
            }
        });

        mToggleBtnCommentNotifications.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                BravoSharePrefs.getInstance(getActivity()).putBooleanValue(BravoConstant.PREF_KEY_COMMENT_NOTIFICATIONS, isChecked);
            }
        });
        mToggleBtnFavouriteNotifications.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                BravoSharePrefs.getInstance(getActivity()).putBooleanValue(BravoConstant.PREF_KEY_FAVOURITE_NOTIFICATIONS, isChecked);
            }
        });
        mToggleBtnFollowNotifications.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                BravoSharePrefs.getInstance(getActivity()).putBooleanValue(BravoConstant.PREF_KEY_FOLLOW_NOTIFICATIONS, isChecked);
            }
        });
        mToggleBtnTotalBravoNotifications.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                BravoSharePrefs.getInstance(getActivity()).putBooleanValue(BravoConstant.PREF_KEY_TOTAL_BRAVO_NOTIFICATIONS, isChecked);
            }
        });
    }

    private void onCheckedToggleBtnFacebook(boolean isChecked) {
        Session session = Session.getActiveSession();
        AIOLog.d("session=>" + session);
        if (session == null || session.isClosed() || session.getState() == null || !session.getState().isOpened()) {
            mToggleBtnPostOnFacebook.onClickLoginFb();
        } else {
            BravoSharePrefs.getInstance(getActivity()).putBooleanValue(BravoConstant.PREF_KEY_POST_ON_FACEBOOK, true);
        }
    }

    private void onCheckedToggleBtnFourSquare(boolean isChecked) {
        mEasyFoursquareAsync = new EasyFoursquareAsync(getActivity());
        mEasyFoursquareAsync.requestAccess(this);
    }

    private void initializeData() {

        boolean isPostOnFacebook = BravoSharePrefs.getInstance(getActivity()).getBooleanValue(BravoConstant.PREF_KEY_POST_ON_FACEBOOK);
        if (isPostOnFacebook) {
            mToggleBtnPostOnFacebook.setChecked(true);
        } else {
            mToggleBtnPostOnFacebook.setChecked(false);
        }
        boolean isPostOnTwitter = BravoSharePrefs.getInstance(getActivity()).getBooleanValue(BravoConstant.PREF_KEY_POST_ON_TWITTER);
        if (isPostOnTwitter) {
            mToggleBtnPostOnTwitter.setChecked(true);
        } else {
            mToggleBtnPostOnTwitter.setChecked(false);
        }
        boolean isPostOnFourSquare = BravoSharePrefs.getInstance(getActivity()).getBooleanValue(BravoConstant.PREF_KEY_POST_ON_FOURSQUARE);
        if (isPostOnFourSquare) {
            mToggleBtnPostOnFourSquare.setChecked(true);
        } else {
            mToggleBtnPostOnFourSquare.setChecked(false);
        }

        boolean isCommentNotifications = BravoSharePrefs.getInstance(getActivity()).getBooleanValue(BravoConstant.PREF_KEY_COMMENT_NOTIFICATIONS);
        boolean isFollowNotifications = BravoSharePrefs.getInstance(getActivity()).getBooleanValue(BravoConstant.PREF_KEY_FOLLOW_NOTIFICATIONS);
        boolean isFavouriteNotifications = BravoSharePrefs.getInstance(getActivity()).getBooleanValue(BravoConstant.PREF_KEY_FAVOURITE_NOTIFICATIONS);
        boolean isTotalBravoNotifications = BravoSharePrefs.getInstance(getActivity()).getBooleanValue(
                BravoConstant.PREF_KEY_TOTAL_BRAVO_NOTIFICATIONS);
        boolean isBravoNotifications = BravoSharePrefs.getInstance(getActivity()).getBooleanValue(BravoConstant.PREF_KEY_BRAVO_NOTIFICATIONS);
        mToggleBtnBravoNotifications.setChecked(isBravoNotifications);
        mToggleBtnCommentNotifications.setChecked(isCommentNotifications);
        mToggleBtnFavouriteNotifications.setChecked(isFavouriteNotifications);
        mToggleBtnFollowNotifications.setChecked(isFollowNotifications);
        mToggleBtnTotalBravoNotifications.setChecked(isTotalBravoNotifications);
    }

    private void initializeView(View root) {
        mTextTermOfUse = (TextView) root.findViewById(R.id.text_term_of_use);
        mTextUpdateUserInfo = (TextView) root.findViewById(R.id.text_edit_profile);
        mTextShareWithFriends = (TextView) root.findViewById(R.id.text_share_friends);
        mTextDeleteMyAccount = (TextView) root.findViewById(R.id.text_delete_account);
        mToggleBtnBravoNotifications = (ToggleButton) root.findViewById(R.id.toggle_btn_bravo_notifications);
        mToggleBtnCommentNotifications = (ToggleButton) root.findViewById(R.id.toggle_btn_comment_notifications);
        mToggleBtnFavouriteNotifications = (ToggleButton) root.findViewById(R.id.toggle_btn_favourite_notifications);
        mToggleBtnFollowNotifications = (ToggleButton) root.findViewById(R.id.toggle_btn_follow_notifications);
        mToggleBtnPostOnFacebook = (ToggleButtonLogin) root.findViewById(R.id.toggle_btn_post_on_facebook);
        mToggleBtnPostOnFourSquare = (ToggleButton) root.findViewById(R.id.toggle_btn_post_on_4square);
        mToggleBtnPostOnTwitter = (ToggleButton) root.findViewById(R.id.toggle_btn_post_on_twitter);
        mToggleBtnTotalBravoNotifications = (ToggleButton) root.findViewById(R.id.toggle_btn_total_bravo_notifications);

        mTextDeleteMyAccount.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showDialogToDeleteMyAccount();
            }
        });

        mTextUpdateUserInfo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mHomeActionListener.goToFragment(HomeActivity.FRAGMENT_UPDATE_USER_INFO_ID);
            }
        });
        mTextTermOfUse.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                iShowPageTermOfUse.showPageTermOfUse();
            }
        });
        mBtnBack = (Button) root.findViewById(R.id.btn_back);
        mBtnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mHomeActionListener.goToBack();
            }
        });
        mTextShareWithFriends.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mHomeActionListener.goToFragment(HomeActivity.FRAGMENT_SHARE_WITH_FRIENDS_ID);
            }
        });
    }

    public interface IShowPageTermOfUse {
        public void showPageTermOfUse();
    }

    public void setListener(IShowPageTermOfUse iShowPageTermOfUse) {
        this.iShowPageTermOfUse = iShowPageTermOfUse;
    }

    private void requestToDeleteMyAccount() {
        int loginBravoViaType = BravoSharePrefs.getInstance(getActivity()).getIntValue(BravoConstant.PREF_KEY_SESSION_LOGIN_BRAVO_VIA_TYPE);
        SessionLogin sessionLogin = BravoUtils.getSession(getActivity(), loginBravoViaType);
        String userId = sessionLogin.userID;
        String accessToken = sessionLogin.accessToken;
        String url = BravoWebServiceConfig.URL_DELETE_USER.replace("{User_ID}", userId).replace("{Access_Token}", accessToken);

        AsyncHttpDelete deleteAccount = new AsyncHttpDelete(getActivity(), new AsyncHttpResponseProcess(getActivity(), this) {
            @Override
            public void processIfResponseSuccess(String response) {
                AIOLog.d("response putFollow :===>" + response);
                BravoUtils.clearSession(MyApplication.getInstance().getApplicationContext());
                Intent splashIntent = new Intent(getActivity(), ActivitySplash.class);
                getActivity().startActivity(splashIntent);
                getActivity().finish();
            }

            @Override
            public void processIfResponseFail() {
                AIOLog.d("response error");
            }
        }, null, true);
        deleteAccount.execute(url);
    }

    private void showDialogToDeleteMyAccount() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) getActivity().getLayoutInflater();
        View dialog_view = inflater.inflate(R.layout.dialog_delete_my_account, null);
        Button btnConfirmNo = (Button) dialog_view.findViewById(R.id.btn_delete_confirm_no);
        btnConfirmNo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                return;
            }
        });
        Button btnYes = (Button) dialog_view.findViewById(R.id.btn_delete_confirm_yes);
        btnYes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                requestToDeleteMyAccount();
                return;
            }
        });

        dialog.setContentView(dialog_view);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        // This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);

        dialog.show();
    }

    @Override
    public void onError(String errorMsg) {
        AIOLog.d("errorMsg: " + errorMsg);
        BravoSharePrefs.getInstance(getActivity()).putBooleanValue(BravoConstant.PREF_KEY_POST_ON_FOURSQUARE, false);
        mToggleBtnPostOnFourSquare.setChecked(false);
    }

    @Override
    public void onAccessGrant(String accessToken) {
        AIOLog.d("accessToken: " + accessToken);
        BravoSharePrefs.getInstance(getActivity()).putBooleanValue(BravoConstant.PREF_KEY_POST_ON_FOURSQUARE, true);
        mToggleBtnPostOnFourSquare.setChecked(true);
    }

    @Override
    public void onResume() {
        super.onResume();

        AIOLog.d("isTwitterLogined");
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

    public void setLoginedTwitter(boolean isLoginedTwitter) { 
        BravoSharePrefs.getInstance(getActivity()).putBooleanValue(BravoConstant.PREF_KEY_POST_ON_TWITTER, isLoginedTwitter);
        mToggleBtnPostOnTwitter.setChecked(isLoginedTwitter);
    }
}
