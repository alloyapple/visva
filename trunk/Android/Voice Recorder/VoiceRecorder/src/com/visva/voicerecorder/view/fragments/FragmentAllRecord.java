package com.visva.voicerecorder.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.visva.voicerecorder.R;

public class FragmentAllRecord extends FragmentBasic {
 // ======================Constant Define=====================
    
    // ======================Control Define =====================
    private SwipeMenuListView mLvRecords;
    // =======================Class Define ======================

    // ======================Variable Define=====================
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = (ViewGroup) inflater.inflate(R.layout.page_fragment_all_record, container);

        initLayout(root);
        return root;
    }

    private void initLayout(View root) {

    }
}
