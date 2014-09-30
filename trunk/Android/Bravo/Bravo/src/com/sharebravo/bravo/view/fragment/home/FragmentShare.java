package com.sharebravo.bravo.view.fragment.home;

import java.util.Arrays;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Request.Callback;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Session.NewPermissionsRequest;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginTextView;
import com.facebook.widget.LoginTextView.UserInfoChangedCallback;
import com.sharebravo.bravo.R;
import com.sharebravo.bravo.control.activity.HomeActivity;
import com.sharebravo.bravo.model.response.ObBravo;
import com.sharebravo.bravo.model.response.SNS;
import com.sharebravo.bravo.sdk.log.AIOLog;
import com.sharebravo.bravo.utils.BravoConstant;
import com.sharebravo.bravo.utils.FacebookUtil;
import com.sharebravo.bravo.view.fragment.FragmentBasic;

public class FragmentShare extends FragmentBasic {
    public static final int        SHARE_ON_FACEBOOK = 0;
    public static final int        SHARE_ON_TWITTER  = 1;
    public static final int        SHARE_ON_LINE     = 2;
    private int                    mShareType;
    private ObBravo                mBravo;
    private LoginTextView          mTxtShareFacebook;
    private TextView               mTxtShareTwitter;
    private TextView               mTxtShareLine;
    private EditText               mTxtboxShare;
    private Button                 mBtnBack;
    private boolean                isSharedTextEmpty;
    private UiLifecycleHelper      mUiLifecycleHelper;
    private Session.StatusCallback mFacebookCallback;
    private boolean                isClickFacebook;

    // private boolean isClickFacebook;

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
                isClickFacebook = true;
                Session session = Session.getActiveSession();
                AIOLog.d("getActiveSession=>" + session);
                if (session == null || session.isClosed() || session.getState() == null || !session.getState().isOpened()) {
                    mTxtShareFacebook.onClickLoginFb();
                } else {
                    // isClickFacebook = true;
                    final List<String> PERMISSIONS = Arrays.asList("publish_actions");

                    if (Session.getActiveSession() != null) {
                        List<String> sessionPermission = Session.getActiveSession().getPermissions();
                        if (!sessionPermission.containsAll(PERMISSIONS)) {
                            NewPermissionsRequest reauthRequest = new Session.NewPermissionsRequest(getActivity(), PERMISSIONS);
                            Session.getActiveSession().requestNewPublishPermissions(reauthRequest);
                        }
                    }
                    mTxtShareFacebook.setPublishPermissions(PERMISSIONS);
                    requestUserFacebookInfo(session);
                }
            }
        });
        mTxtShareFacebook.setUserInfoChangedCallback(new UserInfoChangedCallback() {

            @Override
            public void onUserInfoFetched(GraphUser user) {
                AIOLog.d("user at share facebook:" + user);
                if (isClickFacebook && user != null)
                    FacebookUtil.getInstance(getActivity()).publishShareInBackground(mBravo.Bravo_ID, mTxtboxShare.getText().toString(), new Callback() {

                        @Override
                        public void onCompleted(Response response) {
                            Toast.makeText(getActivity(), "share facebook successfully", Toast.LENGTH_SHORT).show();
                            isClickFacebook = false;
                            mHomeActionListener.goToBack();
                        }
                    });

            }
        });
        mTxtShareTwitter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String textShare = mTxtboxShare.getText().toString();
                mHomeActionListener.shareViaSNS(BravoConstant.TWITTER, mBravo, textShare);
            }
        });
        mTxtShareLine.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String textShare = mTxtboxShare.getText().toString();
                mHomeActionListener.shareViaSNS(BravoConstant.LINE, mBravo, textShare);
            }
        });
        return root;
    }

    private void requestUserFacebookInfo(final Session activeSession) {
        AIOLog.d("activeSession:" + activeSession);
        Request infoRequest = Request.newMeRequest(activeSession, new com.facebook.Request.GraphUserCallback() {

            @Override
            public void onCompleted(final GraphUser user, Response response) {
                AIOLog.d("requestUserFacebookInfo:" + user);
                if (user != null)
                    FacebookUtil.getInstance(getActivity()).publishShareInBackground(mBravo.Bravo_ID, mTxtboxShare.getText().toString(), new Callback() {

                        @Override
                        public void onCompleted(Response response) {
                            Toast.makeText(getActivity(), "share facebook successfully", Toast.LENGTH_SHORT).show();
                            isClickFacebook = false;
                            SNS sns = new SNS();
                            sns.foreignAccessToken = activeSession.getAccessToken();
                            sns.foreignID = user.getId();
                            sns.foreignSNS = BravoConstant.FACEBOOK;
                            mHomeActionListener.putSNS(sns);
                            mHomeActionListener.goToBack();
                        }
                    });
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
            String share = getActivity().getResources().getString(R.string.share_bravo_on_sns_text, mBravo.Spot_Name);
            mTxtboxShare.setText(share);
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
