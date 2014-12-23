package com.sharebravo.bravo.view.fragment.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sharebravo.bravo.R;
import com.sharebravo.bravo.control.activity.HomeActivity;
import com.sharebravo.bravo.control.request.IRequestListener;
import com.sharebravo.bravo.model.response.ObBravo;
import com.sharebravo.bravo.model.response.SNS;
import com.sharebravo.bravo.sdk.log.AIOLog;
import com.sharebravo.bravo.utils.BravoConstant;
import com.sharebravo.bravo.utils.FacebookUtil;
import com.sharebravo.bravo.view.fragment.FragmentBasic;
import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.entities.Profile;
import com.sromku.simple.fb.listeners.OnLoginListener;
import com.sromku.simple.fb.listeners.OnProfileListener;

public class FragmentShare extends FragmentBasic {

    public static final int SHARE_ON_FACEBOOK = 0;
    public static final int SHARE_ON_TWITTER  = 1;
    public static final int SHARE_ON_LINE     = 2;
    private int             mShareType;
    private ObBravo         mBravo;
    private TextView        mTxtShareFacebook;
    private TextView        mTxtShareTwitter;
    private TextView        mTxtShareLine;
    private EditText        mTxtboxShare;
    private Button          mBtnBack;
    private boolean         isSharedTextEmpty;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = (ViewGroup) inflater.inflate(R.layout.page_share, container);
        mHomeActionListener = (HomeActivity) getActivity();
        mTxtShareFacebook = (TextView) root.findViewById(R.id.btn_share_facebook);
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
                Log.d("KieuThang", "mSimpleFacebook:" + mSimpleFacebook);
                if (mSimpleFacebook == null) {
                    mSimpleFacebook.login(onLoginListener);
                    return;
                } else {
                    requestUserFacebookInfo();
                }
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

    private void onFacebookUserConnected(Profile profile) {
        SNS sns = new SNS();
        sns.foreignAccessToken = "";
        sns.foreignID = profile.getId();
        sns.foreignSNS = BravoConstant.FACEBOOK;
        mHomeActionListener.putSNS(sns);

        FacebookUtil.getInstance(getActivity()).publishShareInBackground(mBravo.Bravo_ID, mTxtboxShare.getText().toString(),
                new IRequestListener() {

                    @Override
                    public void onResponse(String response) {
                        String shareDone = getActivity().getString(R.string.share_complete_facebook);
                        Toast.makeText(getActivity(), shareDone, Toast.LENGTH_SHORT).show();
                        mHomeActionListener.goToBack();
                    }

                    @Override
                    public void onErrorResponse(String errorMessage) {

                    }
                });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
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
}
