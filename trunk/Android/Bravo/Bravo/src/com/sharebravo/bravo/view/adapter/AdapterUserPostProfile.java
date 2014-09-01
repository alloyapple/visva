package com.sharebravo.bravo.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class AdapterUserPostProfile extends BaseAdapter {
    Context                 mContext = null;
    UserPostProfileListener listener = null;

    public AdapterUserPostProfile(Context context) {
        // TODO Auto-generated constructor stub
        mContext = context;
    }

    public void setListener(UserPostProfileListener _listener) {
        listener = _listener;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        return null;
    }

}
