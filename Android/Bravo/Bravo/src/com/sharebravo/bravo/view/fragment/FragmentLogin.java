package com.sharebravo.bravo.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginTextView;
import com.sharebravo.bravo.R;
import com.sharebravo.bravo.control.activity.HomeActivity;
import com.sharebravo.bravo.model.user.BravoUser;
import com.sharebravo.bravo.view.fragment.FragmentRegister.IShowPageBravoRegister;
import com.visva.android.visvasdklibrary.log.AIOLog;

public class FragmentLogin extends FragmentBasic {
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = (ViewGroup) inflater.inflate(R.layout.page_fragment_login, container);
        mLayoutBravoLogin = (RelativeLayout) root.findViewById(R.id.layout_bravo_login);
        mLayoutBravoLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mListener.showPageBravoLogin();
            }
        });

        /* facebook */
        mTextViewFacebookLogin = (LoginTextView) root.findViewById(R.id.text_facebook_login);
        mTextViewFacebookLogin.setUserInfoChangedCallback(new LoginTextView.UserInfoChangedCallback() {
            @Override
            public void onUserInfoFetched(GraphUser user) {
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
            }
        });

        /* twitter */
        mTextViewTwitterLogin = (TextView) root.findViewById(R.id.text_twitter_login);
        mTextViewTwitterLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent homeIntent = new Intent(getActivity(), HomeActivity.class);
                startActivity(homeIntent);
                getActivity().finish();
            }
        });

        /* foursquare */
        mTextViewFourSquareLogin = (TextView) root.findViewById(R.id.text_4square_loginr);
        mTextViewFourSquareLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent homeIntent = new Intent(getActivity(), HomeActivity.class);
                startActivity(homeIntent);
                getActivity().finish();
            }
        });
        return root;
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

    @Override
    public void onResume() {
        super.onResume();
        Session session = Session.getActiveSession();
        boolean isActivedSession = session.isOpened();
        AIOLog.d("isActivedSession=" + isActivedSession);
    }

    public interface IShowPageBravoLogin {
        public void showPageBravoLogin();
    }

    public void setListener(IShowPageBravoLogin iShowPageBravoLogin) {
        this.mListener = iShowPageBravoLogin;
    }
}
