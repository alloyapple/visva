package com.sharebravo.bravo.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginTextView;
import com.sharebravo.bravo.R;
import com.sharebravo.bravo.model.user.BravoUser;
import com.visva.android.visvasdklibrary.log.AIOLog;

public class FragmentRegister extends FragmentBasic {
    // ====================Constant Define=================
    // ====================Class Define====================
    // ====================Variable Define=================
    private Button                 mBtnBravoRegister;
    private IShowPageBravoRegister mListener;
    private LoginTextView          mTextViewFacebookLogin;
    private UiLifecycleHelper      mUiLifecycleHelper;
    private Session.StatusCallback mFacebookCallback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = (ViewGroup) inflater.inflate(R.layout.page_fragment_register, container);
        mBtnBravoRegister = (Button) root.findViewById(R.id.btn_sign_up_bravo);
        mBtnBravoRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mListener.showPageBravoRegister();
            }
        });

        /* facebook */
        mTextViewFacebookLogin = (LoginTextView) root.findViewById(R.id.text_facebook_register);
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
                mListener.showPageUserInfo(bravoUser);
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

    public interface IShowPageBravoRegister {
        public void showPageBravoRegister();

        public void showPageUserInfo(BravoUser user);
    }

    public void setListener(IShowPageBravoRegister iShowPageBravoRegister) {
        this.mListener = iShowPageBravoRegister;
    }

}
