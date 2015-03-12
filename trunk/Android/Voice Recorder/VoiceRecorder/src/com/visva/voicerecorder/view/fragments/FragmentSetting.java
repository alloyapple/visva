package com.visva.voicerecorder.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gc.materialdesign.views.LayoutRipple;
import com.gc.materialdesign.views.Switch;
import com.gc.materialdesign.views.Switch.OnCheckListener;
import com.visva.voicerecorder.MyCallRecorderApplication;
import com.visva.voicerecorder.R;
import com.visva.voicerecorder.utils.MyCallRecorderSharePrefs;
import com.visva.voicerecorder.view.common.FragmentBasic;

public class FragmentSetting extends FragmentBasic {
    //========================Constant Define=============
    //========================Control Define==============
    private Switch       mSwitchAutoSavedRecordCall;
    private Switch       mSwitchIncomingCalls;
    private Switch       mSwitchOutGoingCalls;
    private LayoutRipple mLayoutTheme;

    //========================Variable Define=============
    private MyCallRecorderSharePrefs mMyRecorderCallSharePrefs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = (ViewGroup) inflater.inflate(R.layout.page_fragment_setting, container);
        
        initData();
        initLayout(root);
        return root;
    }

    private void initData() {
        mMyRecorderCallSharePrefs = MyCallRecorderApplication.getInstance().getMyCallRecorderSharePref();
    }

    private void initLayout(View root) {
        mSwitchAutoSavedRecordCall = (Switch) root.findViewById(R.id.switch_auto_saved_record_call);
        mSwitchIncomingCalls = (Switch)root.findViewById(R.id.switch_incoming_calls);
        mSwitchOutGoingCalls=(Switch)root.findViewById(R.id.switch_ongoing_calls);
        mLayoutTheme = (LayoutRipple)root.findViewById(R.id.layout_themes);
        mSwitchAutoSavedRecordCall.setOncheckListener(new OnCheckListener() {
            
            @Override
            public void onCheck(boolean check) {
                // TODO Auto-generated method stub
            }
        });
        mSwitchIncomingCalls.setOncheckListener(new OnCheckListener() {
            
            @Override
            public void onCheck(boolean check) {
                // TODO Auto-generated method stub
                
            }
        });
        mSwitchOutGoingCalls.setOncheckListener(new OnCheckListener() {
            
            @Override
            public void onCheck(boolean check) {
                // TODO Auto-generated method stub
                
            }
        });
    }
}
