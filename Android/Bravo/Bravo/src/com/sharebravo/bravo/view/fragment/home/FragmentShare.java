package com.sharebravo.bravo.view.fragment.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sharebravo.bravo.R;
import com.sharebravo.bravo.control.activity.HomeActivity;
import com.sharebravo.bravo.model.response.ObBravo;
import com.sharebravo.bravo.view.fragment.FragmentBasic;

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

        mTxtShareFacebook.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

            }
        });
        mTxtShareTwitter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String textShare = mTxtboxShare.getText().toString();
                mHomeActionListener.shareSNSViaTwitter(mBravo, textShare);
            }
        });
        mTxtShareLine.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

            }
        });
        return root;
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
