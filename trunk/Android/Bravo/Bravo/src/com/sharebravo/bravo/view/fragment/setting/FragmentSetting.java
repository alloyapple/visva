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
import android.widget.TextView;
import android.widget.ToggleButton;

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
import com.sharebravo.bravo.utils.StringUtility;
import com.sharebravo.bravo.view.fragment.FragmentBasic;

public class FragmentSetting extends FragmentBasic {
    // =======================Constant Define==============
    // =======================Class Define=================
    private IShowPageTermOfUse iShowPageTermOfUse;
    // =======================Variable Define==============
    private TextView           mTextTermOfUse;
    private TextView           mTextShareWithFriends;
    private TextView           mTextUpdateUserInfo;
    private TextView           mTextDeleteMyAccount;
    private ToggleButton       mToggleBtnPostOnFacebook;
    private ToggleButton       mToggleBtnPostOnTwitter;
    private ToggleButton       mToggleBtnPostOnFourSquare;
    private ToggleButton       mToggleBtnCommentNotifications;
    private ToggleButton       mToggleBtnFollowNotifications;
    private ToggleButton       mToggleBtnFavouriteNotifications;
    private ToggleButton       mToggleBtnTotalBravoNotifications;
    private ToggleButton       mToggleBtnBravoNotifications;
    private Button             mBtnBack;
    private SessionLogin       mSessionLogin      = null;
    private int                mLoginBravoViaType = BravoConstant.NO_LOGIN_SNS;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = (ViewGroup) inflater.inflate(R.layout.page_settings, container);
        mHomeActionListener = (HomeActivity) getActivity();
        
        initializeView(root);
        initializeData();
        return root;
    }

    private void initializeData() {
        mLoginBravoViaType = BravoSharePrefs.getInstance(getActivity()).getIntValue(BravoConstant.PREF_KEY_SESSION_LOGIN_BRAVO_VIA_TYPE);
        mSessionLogin = BravoUtils.getSession(getActivity(), mLoginBravoViaType);
        
        String sessionFacebookLoginStr = BravoSharePrefs.getInstance(getActivity()).getStringValue(BravoConstant.PREF_KEY_SESSION_LOGIN_BY_FACEBOOK);
        String sessionFacebookRegisterStr = BravoSharePrefs.getInstance(getActivity()).getStringValue(BravoConstant.PREF_KEY_SESSION_REGISTER_BY_FACEBOOK);
        if(!StringUtility.isEmpty(sessionFacebookRegisterStr) || !StringUtility.isEmpty(sessionFacebookLoginStr)){
            mToggleBtnPostOnFacebook.setChecked(true);
        }else{
            mToggleBtnPostOnFacebook.setChecked(false);
        }
        
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
        mToggleBtnPostOnFacebook = (ToggleButton) root.findViewById(R.id.toggle_btn_post_on_facebook);
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
}
