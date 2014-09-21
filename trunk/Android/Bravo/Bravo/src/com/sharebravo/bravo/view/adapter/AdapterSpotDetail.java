package com.sharebravo.bravo.view.adapter;

import java.util.ArrayList;

import com.sharebravo.bravo.R;
import com.sharebravo.bravo.model.response.ObGetSpotHistory;
import com.sharebravo.bravo.model.response.ObGetSpotHistory.SpotHistory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class AdapterSpotDetail extends BaseAdapter {
    private ArrayList<SpotHistory> mSpotHistorys;
    private Context                mContext;

    public AdapterSpotDetail(Context context) {
        // TODO Auto-generated constructor stub
        this.mContext = context;
        mSpotHistorys = new ArrayList<ObGetSpotHistory.SpotHistory>();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mSpotHistorys.size() + 1;
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
    public View getView(int position, View convertView, ViewGroup arg2) {
        // TODO Auto-generated method stub
        if (position == 0)
        {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.layout_spot_detail_header, null, false);
        } else {
        }
        return convertView;
    }

}
