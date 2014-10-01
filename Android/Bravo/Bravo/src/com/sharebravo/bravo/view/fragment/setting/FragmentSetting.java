package com.sharebravo.bravo.view.fragment.setting;

import java.util.ArrayList;

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
import br.com.condesales.listeners.UserInfoRequestListener;
import br.com.condesales.models.User;

import com.facebook.Request;
import com.facebook.Request.GraphUserCallback;
import com.facebook.Response;
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
import com.sharebravo.bravo.model.response.SNS;
import com.sharebravo.bravo.model.response.SNSList;
import com.sharebravo.bravo.sdk.log.AIOLog;
import com.sharebravo.bravo.sdk.request.BravoRequestManager;
import com.sharebravo.bravo.sdk.request.IRequestListener;
import com.sharebravo.bravo.utils.BravoConstant;
import com.sharebravo.bravo.utils.BravoSharePrefs;
import com.sharebravo.bravo.utils.BravoUtils;
import com.sharebravo.bravo.view.fragment.FragmentBasic;

public class FragmentSetting extends FragmentBasic implements AccessTokenRequestListener {
    // =======================Constant Define==============
    // =======================Class Define=================
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
    private SNSList                mSNSList;
    private ArrayList<SNS>         mArrSNSList;
    private boolean                isPostOnFacebook, isPostOnTwitter, isPostOnFourSquare;
    private boolean                isClickFacebookToggle;

    private UiLifecycleHelper      mUiLifecycleHelper;
    private Session.StatusCallback mFacebookCallback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = (ViewGroup) inflater.inflate(R.layout.page_settings, container);
        mHomeActionListener = (HomeActivity) getActivity();

        initializeView(root);
        return root;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        AIOLog.d("hidden:" + hidden);
        if (!hidden) {
            initializeData();
        }
    }

    private void handlerToggleBtnEvents() {
        mToggleBtnPostOnFacebook.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    onCheckedToggleBtnFacebook(isChecked);
                else {
                    isPostOnFacebook = false;
                    for (int i = 0; i < mArrSNSList.size(); i++) {
                        if (BravoConstant.FACEBOOK.equals(mArrSNSList.get(i).foreignSNS)) {
                            mHomeActionListener.deleteSNS(mArrSNSList.get(i));
                        }
                    }
                }
                isClickFacebookToggle = true;
            }
        });
        mToggleBtnPostOnFacebook.setUserInfoChangedCallback(new UserInfoChangedCallback() {

            @Override
            public void onUserInfoFetched(GraphUser user) {
                AIOLog.d("facebook: user=>" + user + ",isClickFacebookToggle:" + isClickFacebookToggle);
                if (isClickFacebookToggle) {
                    if (user == null) {
                        mToggleBtnPostOnFacebook.setChecked(false);
                        isPostOnFacebook = false;
                    } else {
                        mToggleBtnPostOnFacebook.setChecked(true);
                        isPostOnFacebook = true;
                        Session session = Session.getActiveSession();
                        if (session == null || session.isClosed() || session.getState() == null || !session.getState().isOpened()) {
                            AIOLog.d("session is null");
                            isPostOnFacebook = false;
                            return;
                        }
                        SNS sns = new SNS();
                        if (session != null || session.isOpened()) {
                            sns.foreignAccessToken = session.getAccessToken();
                            sns.foreignID = user.getId();
                            sns.foreignSNS = BravoConstant.FACEBOOK;
                            mHomeActionListener.putSNS(sns);
                        }
                    }
                    isClickFacebookToggle = false;
                }
            }
        });
        mToggleBtnPostOnTwitter.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mToggleBtnPostOnTwitter.setChecked(false);
                if (isChecked) {
                    mHomeActionListener.requestToLoginSNS(BravoConstant.TWITTER);
                } else {
                    isPostOnTwitter = false;
                    for (int i = 0; i < mArrSNSList.size(); i++) {
                        if (BravoConstant.TWITTER.equals(mArrSNSList.get(i).foreignSNS)) {
                            mHomeActionListener.deleteSNS(mArrSNSList.get(i));
                        }
                    }
                }
            }
        });
        mToggleBtnPostOnFourSquare.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mToggleBtnPostOnFourSquare.setChecked(false);
                if (isChecked)
                    onCheckedToggleBtnFourSquare(isChecked);
                else {
                    isPostOnFourSquare = false;
                    for (int i = 0; i < mArrSNSList.size(); i++) {
                        if (BravoConstant.FOURSQUARE.equals(mArrSNSList.get(i).foreignSNS)) {
                            mHomeActionListener.deleteSNS(mArrSNSList.get(i));
                        }
                    }
                }
            }
        });

        /* Notifications */
        mToggleBtnBravoNotifications.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                requestToChangeNotificationsType(BravoConstant.PREF_KEY_BRAVO_NOTIFICATIONS, isChecked);
            }
        });

        mToggleBtnCommentNotifications.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                requestToChangeNotificationsType(BravoConstant.PREF_KEY_COMMENT_NOTIFICATIONS, isChecked);
            }
        });
        mToggleBtnFavouriteNotifications.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                requestToChangeNotificationsType(BravoConstant.PREF_KEY_FAVOURITE_NOTIFICATIONS, isChecked);
            }
        });
        mToggleBtnFollowNotifications.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                requestToChangeNotificationsType(BravoConstant.PREF_KEY_FOLLOW_NOTIFICATIONS, isChecked);
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
        final Session session = Session.getActiveSession();
        AIOLog.d("session=>" + session);
        if (session == null || session.isClosed() || session.getState() == null || !session.getState().isOpened()) {
            mToggleBtnPostOnFacebook.onClickLoginFb();
        } else {
            Request infoRequest = Request.newMeRequest(session, new GraphUserCallback() {

                @Override
                public void onCompleted(GraphUser user, Response response) {
                    SNS sns = new SNS();
                    sns.foreignAccessToken = session.getAccessToken();
                    sns.foreignID = user.getId();
                    sns.foreignSNS = BravoConstant.FACEBOOK;
                    mHomeActionListener.putSNS(sns);
                }
            });
            Bundle params = new Bundle();
            params.putString("fields", "id, name, picture");
            infoRequest.setParameters(params);
            infoRequest.executeAsync();
        }
    }

    private void onCheckedToggleBtnFourSquare(boolean isChecked) {
        mEasyFoursquareAsync = new EasyFoursquareAsync(getActivity());
        mEasyFoursquareAsync.requestAccess(this);
    }

    private void initializeData() {
        mSNSList = BravoUtils.getSNSList(getActivity());
        if (mSNSList == null)
            mArrSNSList = new ArrayList<SNS>();
        else
            mArrSNSList = mSNSList.snsArrList;
        if (mArrSNSList == null || mArrSNSList.size() == 0) {
            isPostOnFacebook = false;
            isPostOnFourSquare = false;
            isPostOnTwitter = false;
        } else
            for (int i = 0; i < mArrSNSList.size(); i++) {
                if (BravoConstant.FACEBOOK.equals(mArrSNSList.get(i).foreignSNS))
                    isPostOnFacebook = true;
                if (BravoConstant.FOURSQUARE.equals(mArrSNSList.get(i).foreignSNS))
                    isPostOnFourSquare = true;
                if (BravoConstant.TWITTER.equals(mArrSNSList.get(i).foreignSNS))
                    isPostOnTwitter = true;
            }
        if (isPostOnFacebook) {
            mToggleBtnPostOnFacebook.setChecked(true);
        } else {
            mToggleBtnPostOnFacebook.setChecked(false);
        }
        if (isPostOnTwitter) {
            mToggleBtnPostOnTwitter.setChecked(true);
        } else {
            mToggleBtnPostOnTwitter.setChecked(false);
        }
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

        handlerToggleBtnEvents();
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
                mHomeActionListener.showPageTermOfUse();
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

    private void requestToDeleteMyAccount() {
        BravoRequestManager.getInstance(getActivity()).requestToDeleteMyAccount(this, new IRequestListener() {
            @Override
            public void onResponse(String response) {
                AIOLog.d("response putFollow :===>" + response);
                BravoUtils.clearSession(MyApplication.getInstance().getApplicationContext());
                Intent splashIntent = new Intent(getActivity(), ActivitySplash.class);
                getActivity().startActivity(splashIntent);
                getActivity().finish();
            }
            @Override
            public void onErrorResponse(String errorMessage) {
                
            }
        });
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
        mToggleBtnPostOnFourSquare.setChecked(false);
        isPostOnFourSquare = false;
    }

    @Override
    public void onAccessGrant(final String accessToken) {
        AIOLog.d("accessToken: " + accessToken);
        mEasyFoursquareAsync.getUserInfo(new UserInfoRequestListener() {

            @Override
            public void onError(String errorMsg) {
                mToggleBtnPostOnFourSquare.setChecked(false);
                isPostOnFourSquare = false;
            }

            @Override
            public void onUserInfoFetched(User user) {
                AIOLog.d("user 4square:" + user);
                if (user == null)
                    return;
                AIOLog.d("user: " + user.getFirstName());
                SNS sns = new SNS();
                sns.foreignAccessToken = accessToken;
                sns.foreignID = user.getId() + "";
                sns.foreignSNS = BravoConstant.FOURSQUARE;
                mHomeActionListener.putSNS(sns);
            }
        });
        mToggleBtnPostOnFourSquare.setChecked(true);
        isPostOnFourSquare = true;
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

    public void setLoginedTwitter(boolean isLoginedTwitter, SNS sns) {
        mToggleBtnPostOnTwitter.setChecked(isLoginedTwitter);
        isPostOnTwitter = isLoginedTwitter;
        mHomeActionListener.putSNS(sns);
    }

    public void updatePostSNS(SNS sns, boolean isPostOnSNS) {
        if (BravoConstant.FACEBOOK.equals(sns.foreignSNS)) {
            isPostOnFacebook = isPostOnSNS;
            mToggleBtnPostOnFacebook.setChecked(isPostOnSNS);
        } else if (BravoConstant.TWITTER.equals(sns.foreignSNS)) {
            isPostOnTwitter = isPostOnSNS;
            mToggleBtnPostOnTwitter.setChecked(isPostOnSNS);
        } else if (BravoConstant.FOURSQUARE.equals(sns.foreignSNS)) {
            isPostOnFourSquare = isPostOnSNS;
            mToggleBtnPostOnFourSquare.setChecked(isPostOnSNS);
        }
    }

    private void requestToChangeNotificationsType(final String bravoNotificationsType, final boolean isChecked) {
        BravoRequestManager.getInstance(getActivity()).requestToChangeNotificationsType(bravoNotificationsType, isChecked,
                new IRequestListener() {

                    @Override
                    public void onResponse(String response) {
                        AIOLog.d("requestToChangeNotificationsType: " + response + " ,bravoNotificationsType:" + bravoNotificationsType);
                        BravoSharePrefs.getInstance(getActivity()).putBooleanValue(bravoNotificationsType, isChecked);
                    }

                    @Override
                    public void onErrorResponse(String errorMessage) {
                        AIOLog.d("errorMessage: " +errorMessage);
                    }
                });
    }
}
