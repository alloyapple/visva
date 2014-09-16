package com.sharebravo.bravo.view.fragment.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginTextView;
import com.facebook.widget.LoginTextView.UserInfoChangedCallback;
import com.sharebravo.bravo.R;
import com.sharebravo.bravo.control.activity.HomeActivity;
import com.sharebravo.bravo.model.response.ObBravo;
import com.sharebravo.bravo.sdk.log.AIOLog;
import com.sharebravo.bravo.utils.FacebookUtil;
import com.sharebravo.bravo.utils.StringUtility;
import com.sharebravo.bravo.view.fragment.FragmentBasic;

public class FragmentShare extends FragmentBasic {
    public static final int SHARE_ON_FACEBOOK = 0;
    public static final int SHARE_ON_TWITTER  = 1;
    public static final int SHARE_ON_LINE     = 2;
    private int             mShareType;
    private ObBravo         mBravo;
    private LoginTextView   mTxtShareFacebook;
    private TextView        mTxtShareTwitter;
    private TextView        mTxtShareLine;
    private EditText        mTxtboxShare;
    private Button          mBtnBack;
    private boolean         isSharedTextEmpty;
    private boolean         isClickFacebook;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = (ViewGroup) inflater.inflate(R.layout.page_share, container);
        mHomeActionListener = (HomeActivity) getActivity();
        mTxtShareFacebook = (LoginTextView) root.findViewById(R.id.btn_share_facebook);
        mTxtShareTwitter = (TextView) root.findViewById(R.id.btn_share_twitter);
        mTxtShareLine = (TextView) root.findViewById(R.id.btn_share_line);

        mTxtboxShare = (EditText) root.findViewById(R.id.txtbox_share);
        mBtnBack = (Button) root.findViewById(R.id.btn_back);
        mBtnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mHomeActionListener.goToBack();
            }
        });

        mTxtboxShare.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isSharedTextEmpty) {
                    isSharedTextEmpty = false;
                    mTxtboxShare.setText("");
                    mTxtboxShare.setHint("");
                    mTxtboxShare.setHintTextColor(getActivity().getResources().getColor(R.color.black));
                }
            }
        });
        mTxtShareFacebook.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Session session = Session.getActiveSession();
                if (session == null || session.isClosed() || session.getState() == null || !session.getState().isOpened()) {
                    mTxtShareFacebook.onClickLoginFb();
                } else {
                    AIOLog.d("request user info:" + session);
                    if (StringUtility.isEmpty(mTxtboxShare)) {
                        isSharedTextEmpty = true;
                        mTxtboxShare.setText("");
                        mTxtboxShare.setHint(getString(R.string.sharedtext_is_empty));
                        mTxtboxShare.setHintTextColor(getActivity().getResources().getColor(R.color.red));
                    } else {
                        isClickFacebook = true;
                        requestUserFacebookInfo(session);
                    }
                }
            }
        });
        mTxtShareFacebook.setUserInfoChangedCallback(new UserInfoChangedCallback() {

            @Override
            public void onUserInfoFetched(GraphUser user) {
                if (isClickFacebook)
                    FacebookUtil.getInstance(getActivity()).publishShareInBackground(mTxtboxShare.getText().toString());
            }
        });
        mTxtShareTwitter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String textShare = mTxtboxShare.getText().toString();
                if (StringUtility.isEmpty(textShare)) {
                    isSharedTextEmpty = true;
                    mTxtboxShare.setText("");
                    mTxtboxShare.setHint(getString(R.string.sharedtext_is_empty));
                    mTxtboxShare.setHintTextColor(getActivity().getResources().getColor(R.color.red));
                } else {
                    mHomeActionListener.shareSNSViaTwitter(mBravo, textShare);
                }
            }
        });
        mTxtShareLine.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showToast("This feature is comming soon!");
            }
        });
        return root;
    }

    private void requestUserFacebookInfo(Session activeSession) {
        Request infoRequest = Request.newMeRequest(activeSession, new com.facebook.Request.GraphUserCallback() {

            @Override
            public void onCompleted(GraphUser user, Response response) {
                AIOLog.d("requestUserFacebookInfo:" + user);
                // onFacebookUserConnected(user);
                FacebookUtil.getInstance(getActivity()).publishShareInBackground(mTxtboxShare.getText().toString());
            }

        });
        Bundle params = new Bundle();
        params.putString("fields", "id, name, picture");
        infoRequest.setParameters(params);
        infoRequest.executeAsync();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            switch (mShareType) {
            case SHARE_ON_FACEBOOK:
                mTxtShareFacebook.setVisibility(View.VISIBLE);
                mTxtShareTwitter.setVisibility(View.GONE);
                mTxtShareLine.setVisibility(View.GONE);
                break;
            case SHARE_ON_TWITTER:
                mTxtShareFacebook.setVisibility(View.GONE);
                mTxtShareTwitter.setVisibility(View.VISIBLE);
                mTxtShareLine.setVisibility(View.GONE);
                break;
            case SHARE_ON_LINE:
                mTxtShareFacebook.setVisibility(View.GONE);
                mTxtShareTwitter.setVisibility(View.GONE);
                mTxtShareLine.setVisibility(View.VISIBLE);
                break;

            default:
                break;
            }
            mTxtboxShare.setText("Share " + mBravo.Spot_Name + " Bravos");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void setData(ObBravo bravo, int shareType) {
        this.mShareType = shareType;
        mBravo = bravo;
    }
}
