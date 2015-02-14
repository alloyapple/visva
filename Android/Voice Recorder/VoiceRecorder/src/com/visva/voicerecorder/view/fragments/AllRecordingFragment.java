package com.visva.voicerecorder.view.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.visva.voicerecorder.MainActivity;
import com.visva.voicerecorder.record.RecordingAdapter;
import com.visva.voicerecorder.record.RecordingListView;

/**
 * A fragment that display all the recording sessions
 */

public class AllRecordingFragment extends Fragment {
    @Override
    public void onStart() {
        super.onStart();
        ViewGroup vg = (ViewGroup) getView();
        vg.removeAllViews();
        vg.addView(this.getListView());
    }

    private View getListView() {
        RecordingListView listView = new RecordingListView(getActivity());
        RecordingAdapter adapter = new RecordingAdapter(getActivity(), android.R.layout.simple_list_item_activated_1,
                MainActivity.recordingManager.showAll());
        listView.setAdapter(adapter);
        return listView;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return this.getListView();
    }
}