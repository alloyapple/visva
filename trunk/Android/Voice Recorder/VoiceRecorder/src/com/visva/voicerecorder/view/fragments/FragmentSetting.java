package com.visva.voicerecorder.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.visva.voicerecorder.R;
import com.visva.voicerecorder.view.common.FragmentBasic;

public class FragmentSetting extends FragmentBasic{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = (ViewGroup) inflater.inflate(R.layout.page_fragment_setting, container);
        return root;
    }
}
