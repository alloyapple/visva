package com.sharebravo.bravo.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.sharebravo.bravo.R;
import com.sharebravo.bravo.control.activity.HomeActivity;

public class FragmentBravoRegister extends FragmentBasic {
    // ====================Constant Define=================
    // ====================Class Define====================
    // ====================Variable Define=================
    private Button mBtnDoneRegisterBravo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = (ViewGroup) inflater.inflate(R.layout.page_fragment_bravo_register, container);
        mBtnDoneRegisterBravo = (Button)root.findViewById(R.id.btn_done_register_bravo);
        mBtnDoneRegisterBravo.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                Intent homeIntent =new Intent(getActivity(), HomeActivity.class);
                startActivity(homeIntent);
                getActivity().finish();
            }
        });
        return root;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
