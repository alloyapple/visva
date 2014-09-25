package com.sharebravo.bravo.view.fragment.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sharebravo.bravo.R;
import com.sharebravo.bravo.control.activity.HomeActivity;
import com.sharebravo.bravo.model.SessionLogin;
import com.sharebravo.bravo.utils.BravoConstant;
import com.sharebravo.bravo.utils.BravoSharePrefs;
import com.sharebravo.bravo.utils.BravoUtils;
import com.sharebravo.bravo.view.fragment.FragmentBasic;

public class FragmentInputMySpot extends FragmentBasic {

    private Button       btnBack;

    private SessionLogin mSessionLogin      = null;
    private int          mLoginBravoViaType = BravoConstant.NO_LOGIN_SNS;
    private FrameLayout  layoutMap;
    private TextView     btnLocateSpot;
    private Button       btnAdd             = null;

    // FragmentMapViewCover mapFragment = new FragmentMapViewCover();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = (ViewGroup) inflater.inflate(R.layout.page_input_myspot, container);
        mHomeActionListener = (HomeActivity) getActivity();

        btnBack = (Button) root.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mHomeActionListener.goToBack();
            }
        });
        layoutMap = (FrameLayout) root.findViewById(R.id.layout_map_img);
        layoutMap.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(getActivity(), "location your spot", Toast.LENGTH_LONG).show();
            }
        });
        btnLocateSpot = (TextView) root.findViewById(R.id.txt_locate_your_spot);
        btnLocateSpot.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mHomeActionListener.goToLocateMySpot();
            }
        });
        btnAdd = (Button) root.findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                onAddMySpot();
            }
        });
        mLoginBravoViaType = BravoSharePrefs.getInstance(getActivity()).getIntValue(BravoConstant.PREF_KEY_SESSION_LOGIN_BRAVO_VIA_TYPE);
        mSessionLogin = BravoUtils.getSession(getActivity(), mLoginBravoViaType);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void onAddMySpot() {
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && !isBackStatus()) {
            // requestGetBravo();
        }
    }

}
