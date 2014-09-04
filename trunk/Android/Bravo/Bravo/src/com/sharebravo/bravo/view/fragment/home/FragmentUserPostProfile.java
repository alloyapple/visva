package com.sharebravo.bravo.view.fragment.home;

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
import com.sharebravo.bravo.view.adapter.AdapterUserPostProfile;
import com.sharebravo.bravo.view.adapter.UserPostProfileListener;
import com.sharebravo.bravo.view.fragment.FragmentBasic;
import com.sharebravo.bravo.view.lib.imageheader.PullAndLoadListView;

public class FragmentUserPostProfile extends FragmentBasic implements UserPostProfileListener {
    private PullAndLoadListView    mListViewUserPostProfile = null;
    private AdapterUserPostProfile adapterUserPostProfile   = null;
    private HomeActionListener     mHomeActionListener      = null;
    private Button                 btnBack;
    private OnItemClickListener    onItemClick              = new OnItemClickListener() {

                                                                @Override
                                                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                                    // TODO Auto-generated method stub

                                                                }
                                                            };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = (ViewGroup) inflater.inflate(R.layout.page_fragment_user_post_profile,
                null);
        mHomeActionListener = (HomeActivity) getActivity();
        mListViewUserPostProfile = (PullAndLoadListView) root.findViewById(R.id.listview_user_post_profile);
        adapterUserPostProfile = new AdapterUserPostProfile(getActivity());
        adapterUserPostProfile.setListener(this);
        mListViewUserPostProfile.setFooterDividersEnabled(false);
        mListViewUserPostProfile.setAdapter(adapterUserPostProfile);
        mListViewUserPostProfile.setOnItemClickListener(onItemClick);
        btnBack = (Button) root.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mHomeActionListener.goToBack();
            }
        });
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        // TODO Auto-generated method stub
        super.onHiddenChanged(hidden);

    }

}
