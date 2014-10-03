package com.sharebravo.bravo.view.adapter;

import com.sharebravo.bravo.R;
import com.sharebravo.bravo.view.fragment.home.FragmentDuplicateSpot;
import com.sharebravo.bravo.view.fragment.home.FragmentInputMySpot;
import com.sharebravo.bravo.view.fragment.maps.FragmentMapCover;

import android.content.Context;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class AdapterMapDuplicateSpot extends BaseAdapter {
    private Context       mContext;
    FragmentTransaction   fragmentTransaction;
    FragmentMapCover      mapFragment;
    FragmentDuplicateSpot fragment;

    public AdapterMapDuplicateSpot(Context context, FragmentDuplicateSpot fragment) {
        // TODO Auto-generated constructor stub
        mContext = context;
        this.fragment = fragment;
        fragmentTransaction = fragment.getChildFragmentManager().beginTransaction();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return 1;
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public View getView(int arg0, View convertView, ViewGroup arg2) {
        // TODO Auto-generated method stub
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.row_map_cover2, null, false);
        mapFragment = (FragmentMapCover) fragment.getChildFragmentManager().findFragmentById(R.id.spot_map_add_2);
        if (mapFragment == null) {
            mapFragment = new FragmentMapCover();
            fragmentTransaction.replace(R.id.spot_map_add_2, mapFragment).commit();
        }
        return convertView;
    }

    public void updateMapView() {
        if (mapFragment != null)
            mapFragment.changeLocation(FragmentMapCover.mLat, FragmentMapCover.mLong);
    }

}
