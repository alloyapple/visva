package com.visva.android.socialstackwidget.fragment;

import java.util.Arrays;
import java.util.List;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.facebook.widget.LoginButtonDefault;
import com.facebook.widget.ProfilePictureView;
import com.visva.android.socialstackwidget.R;
import com.visva.android.socialstackwidget.constant.GlobalContstant;
import com.visva.android.socialstackwidget.util.SharePrefUtils;
import com.visva.android.socialstackwidget.util.VisvaLog;

public class AccountSettingFragment extends Fragment implements OnClickListener {
    private static final String TAG = GlobalContstant.PRE_TAG + "AccountSettingFragment";
    private LoginButton mBtnLoginFacebook;
    private LoginButtonDefault mBtnLogoutFacebook;
    private TextView mTxtAddYourAcc;
    private TextView mTxtSocialDetailType;
    private TextView mTxtSocialName;
    private ProfilePictureView mImgFBAvatar;
    private Button mBtnFacebookOptions;
    private ImageView mImgSocialType;
    private boolean isClickLogin = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = (ViewGroup) inflater.inflate(R.layout.fragment_account_setting, null);

        initView(root);
        return root;
    }

    private void initView(View root) {
        if(SharePrefUtils.getIntValue(getActivity(), GlobalContstant.SHARE_PRE_REFRESH_TIME_KEY) == -1){
            SharePrefUtils.putIntValue(getActivity(), GlobalContstant.SHARE_PRE_REFRESH_TIME_KEY, 1);
        }
        mBtnLoginFacebook = (LoginButton) root.findViewById(R.id.login_fb_button);
        List<String> permissions = Arrays.asList(" ", "user_photos", "read_mailbox", "user_groups", "read_friendlists", "friends_status", "read_stream", "friends_photos");
        mBtnLoginFacebook.setReadPermissions(permissions);
        mBtnLogoutFacebook = (LoginButtonDefault) root.findViewById(R.id.logout_fb_button);
        mTxtAddYourAcc = (TextView) root.findViewById(R.id.txt_add_your_acc);
        mTxtSocialName = (TextView) root.findViewById(R.id.txt_social_name);
        mTxtSocialDetailType = (TextView) root.findViewById(R.id.txt_social_type_detail);
        mImgFBAvatar = (ProfilePictureView) root.findViewById(R.id.img_avatar_fb);
        mBtnFacebookOptions = (Button) root.findViewById(R.id.btn_fb_options);
        mBtnFacebookOptions.setOnClickListener(this);

        mBtnLoginFacebook.setOnClickListener(this);
        mBtnLoginFacebook.setOnClickListener(this);
        mTxtSocialName.setVisibility(View.GONE);
        mTxtSocialDetailType.setVisibility(View.GONE);
        mBtnLoginFacebook.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {

            @Override
            public void onUserInfoFetched(GraphUser user) {
                VisvaLog.d(TAG, "onUserInfoFetched " + user);
                if (user != null) {
                    mTxtSocialName.setText(user.getName());
                    mImgFBAvatar.setProfileId(user.getId());
                    mTxtAddYourAcc.setVisibility(View.GONE);
                    mImgFBAvatar.setVisibility(View.VISIBLE);
                    mTxtSocialName.setVisibility(View.VISIBLE);
                    mTxtSocialDetailType.setVisibility(View.VISIBLE);
                    if (SharePrefUtils.getIntValue(getActivity(), GlobalContstant.SHARE_PRE_FACEBOOK_TYPE_KEY) == 0)
                        mTxtSocialDetailType.setText(getActivity().getString(R.string.facebook_feed));
                    else
                        mTxtSocialDetailType.setText(getActivity().getString(R.string.facebook_timeline));

                    //send request to social server
                    if (isClickLogin) {
                        Intent intent = new Intent(GlobalContstant.ACTION_REQUEST);
                        intent.putExtra(GlobalContstant.EXTRA_SOCIAL_TYPE_REQUEST, GlobalContstant.FACEBOOK);
                        intent.putExtra(GlobalContstant.EXTRA_SOCIAL_DETAIL_REQUEST, "Feed");
                        getActivity().sendBroadcast(intent);
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.login_fb_button:
            onClickFacebookLoginButton();
            break;
        case R.id.logout_fb_button:
            onClickFacebookLogout();
            break;
        case R.id.btn_fb_options:
            showFacebookOptionsDialogOptions();
            break;
        default:
            break;
        }
    }

    private void showFacebookOptionsDialogOptions() {

        Builder builder = new Builder(getActivity());
        builder.setTitle(getActivity().getString(R.string.choose_category_type));
        final String[] items = new String[] { getActivity().getString(R.string.facebook_feed), getActivity().getString(R.string.facebook_timeline) };
        int choiceIndex = SharePrefUtils.getIntValue(getActivity(), GlobalContstant.SHARE_PRE_FACEBOOK_TYPE_KEY);
        builder.setSingleChoiceItems(items, choiceIndex, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                mTxtSocialDetailType.setText(items[which]);
                SharePrefUtils.putIntValue(getActivity(), GlobalContstant.SHARE_PRE_FACEBOOK_TYPE_KEY, which);

                try {
                    dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        builder.show();
    }

    private void onClickFacebookLogout() {
        mBtnLogoutFacebook.onClickFacebookLogoutButton();
    }

    private void onClickFacebookLoginButton() {
        //        if (Utils.isNetworkAvailable(AlwaysActivity.this)) {
        Session session = Session.getActiveSession();
        boolean enableButtons = (session != null && session.isOpened());
        if (enableButtons) {
        } else {
            mBtnLoginFacebook.onClickLoginFb();
            isClickLogin = true;
        }
        //        } else {
        //            showToast(getString(R.string.no_networks_found));
        //        }
    }
}
