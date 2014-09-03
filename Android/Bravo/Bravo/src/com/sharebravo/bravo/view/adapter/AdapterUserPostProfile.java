package com.sharebravo.bravo.view.adapter;

import com.android.volley.toolbox.NetworkImageView;
import com.sharebravo.bravo.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
        return 1;
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
        if (position == 0) // post content
        {
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.layout_user_post_profile_header, null, false);
            }
        }
        return convertView;
    }

}
