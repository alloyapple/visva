package com.sharebravo.bravo.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sharebravo.bravo.R;
import com.sharebravo.bravo.control.activity.HomeActionListener;

public class FragmentMapView extends FragmentBasic {

<<<<<<< .mine
    private HomeActionListener mHomeActionListener = null;
=======
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // TODO Auto-generated method stub
            //mHomeActionListener.goToRecentPostDetail();
        }};
>>>>>>> .r1009

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = (ViewGroup) inflater.inflate(R.layout.page_fragment_map_view,
                null);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
