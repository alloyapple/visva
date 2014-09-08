package com.sharebravo.bravo.view.fragment.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sharebravo.bravo.R;
import com.sharebravo.bravo.control.activity.HomeActionListener;
import com.sharebravo.bravo.control.activity.HomeActivity;
import com.sharebravo.bravo.model.response.ObBravo;
import com.sharebravo.bravo.view.fragment.FragmentBasic;

public class FragmentShare extends FragmentBasic {
    public static final int SHARE_ON_FACEBOOK = 0;
    public static final int SHARE_ON_TWITTER  = 1;
    public static final int SHARE_ON_LINE     = 2;
    private int             shareType;
    ObBravo                 mBravo;
    TextView                txtShare;
    EditText                txtboxShare;
    Button                  btnBack;
    private HomeActionListener mHomeActionListener = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mHomeActionListener = (HomeActivity) getActivity();
        View root = (ViewGroup) inflater.inflate(R.layout.page_share,
                null);
        txtShare = (TextView) root.findViewById(R.id.btn_share);
        txtboxShare = (EditText) root.findViewById(R.id.txtbox_share);
        btnBack = (Button) root.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mHomeActionListener.goToBack();
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
        // TODO Auto-generated method stub
        super.onHiddenChanged(hidden);
        if (!hidden) {
            switch (shareType) {
            case SHARE_ON_FACEBOOK:
                txtShare.setText(getResources().getString(R.string.share_on_facebook));
                break;
            case SHARE_ON_TWITTER:
                txtShare.setText(getResources().getString(R.string.share_on_twitter));
                break;
            case SHARE_ON_LINE:
                txtShare.setText(getResources().getString(R.string.share_on_line));
                break;

            default:
                break;
            }
            txtboxShare.setText("Share " + mBravo.Spot_Name + " Bravos");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void setData(ObBravo bravo, int shareType) {
        this.shareType = shareType;
        mBravo = bravo;
    }
}
