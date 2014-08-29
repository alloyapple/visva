package com.sharebravo.bravo.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.sharebravo.bravo.R;
import com.sharebravo.bravo.control.activity.HomeActionListener;
import com.sharebravo.bravo.view.adapter.AdapterRecentPostDetail;
import com.sharebravo.bravo.view.lib.PullAndLoadListView;


public class FragmentRecentPostDetail extends FragmentBasic {
    private PullAndLoadListView listviewRecentPostDetail  = null;
    private AdapterRecentPostDetail   adapterRecentPostDetail   = null;
    private HomeActionListener  mHomeActionListener = null;
    private OnItemClickListener onItemClick = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            
        }};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = (ViewGroup) inflater.inflate(R.layout.page_recent_post_detail_tab,
                null);

        listviewRecentPostDetail = (PullAndLoadListView) root.findViewById(R.id.listview_recent_post_detail);
        adapterRecentPostDetail = new AdapterRecentPostDetail(getActivity());
        listviewRecentPostDetail.setFooterDividersEnabled(false);
        listviewRecentPostDetail.setAdapter(adapterRecentPostDetail);
        listviewRecentPostDetail.setOnItemClickListener(onItemClick);
        
        return root;
    }
    @Override
    public void onResume() {
        super.onResume();
    }
}
