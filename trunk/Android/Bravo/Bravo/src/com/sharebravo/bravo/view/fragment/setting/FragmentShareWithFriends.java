package com.sharebravo.bravo.view.fragment.setting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sharebravo.bravo.R;
import com.sharebravo.bravo.view.fragment.FragmentBasic;

public class FragmentShareWithFriends extends FragmentBasic {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = (ViewGroup) inflater.inflate(R.layout.page_fragment_share_with_friends, container);
        return root;
    }
}
