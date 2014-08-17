package com.sharebravo.bravo.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sharebravo.bravo.R;

public class FragmentHomeTab extends FragmentBasic {
//    private ListView          listviewRecentPost = null;
//    private AdapterRecentPost adapterRecentPost  = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = (ViewGroup) inflater.inflate(R.layout.page_home_tab,
                null);
//        listviewRecentPost = (ListView) root.findViewById(R.id.listview_recent_post);
//        adapterRecentPost = new AdapterRecentPost(getActivity());
//        listviewRecentPost.setAdapter(adapterRecentPost);
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
