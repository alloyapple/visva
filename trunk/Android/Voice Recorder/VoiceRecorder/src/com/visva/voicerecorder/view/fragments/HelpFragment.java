package com.visva.voicerecorder.view.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.visva.voicerecorder.R;
import com.visva.voicerecorder.utils.ProgramHelper;

public class HelpFragment extends Fragment {
    ProgramHelper helper = new ProgramHelper();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return this._createDefaultView();
    }

    private View _createDefaultView() {
        View helpView = this.getActivity().getLayoutInflater().inflate(R.layout.setting_fragment, null);
        return helpView;
    }
}
