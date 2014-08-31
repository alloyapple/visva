package com.sharebravo.bravo.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;

import com.sharebravo.bravo.R;
import com.sharebravo.bravo.control.activity.HomeActionListener;
import com.sharebravo.bravo.control.activity.HomeActivity;
import com.sharebravo.bravo.view.adapter.AdapterRecentPostDetail;
import com.sharebravo.bravo.view.lib.PullAndLoadListView;

public class FragmentRecentPostDetail extends FragmentBasic {
    private PullAndLoadListView     listviewRecentPostDetail = null;
    private AdapterRecentPostDetail adapterRecentPostDetail  = null;
    private HomeActionListener      mHomeActionListener      = null;
    private Button                  btnBack;
    private OnItemClickListener     onItemClick              = new OnItemClickListener() {

                                                                 @Override
                                                                 public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                                     // TODO Auto-generated method stub

                                                                 }
                                                             };
    Button                          btnViewMap               = null;
    Button                          btnCallSpot              = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = (ViewGroup) inflater.inflate(R.layout.page_recent_post_detail_tab,
                null);
        mHomeActionListener = (HomeActivity) getActivity();
        listviewRecentPostDetail = (PullAndLoadListView) root.findViewById(R.id.listview_recent_post_detail);
        adapterRecentPostDetail = new AdapterRecentPostDetail(getActivity());
        listviewRecentPostDetail.setFooterDividersEnabled(false);
        listviewRecentPostDetail.setAdapter(adapterRecentPostDetail);
        listviewRecentPostDetail.setOnItemClickListener(onItemClick);
        btnBack = (Button) root.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mHomeActionListener.goToBack();
            }
        });
        btnCallSpot = (Button) root.findViewById(R.id.btn_call_spot);
        // btnCallSpot.setOnClickListener(new OnClickListener() {
        //
        // @Override
        // public void onClick(View v) {
        // // TODO Auto-generated method stub
        //
        // }
        // });
        btnViewMap = (Button) root.findViewById(R.id.btn_view_map);
        // btnViewMap.setOnClickListener(new OnClickListener() {
        //
        // @Override
        // public void onClick(View v) {
        // // TODO Auto-generated method stub
        // mHomeActionListener.goToFragment(HomeActivity.FRAGMENT_MAP_VIEW_ID);
        // }
        // });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
