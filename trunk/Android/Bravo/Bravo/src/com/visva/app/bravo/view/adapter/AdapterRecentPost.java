package com.visva.app.bravo.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.visva.app.bravo.R;

public class AdapterRecentPost extends BaseAdapter {
    private final int DEFAULT_ITEM_NUMBER = 10;
    private Context   mContext;

    public AdapterRecentPost(Context context) {
        // TODO Auto-generated constructor stub
        this.mContext = context;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return DEFAULT_ITEM_NUMBER;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_recent_post, null, false);

            holder = new ViewHolder();
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        return convertView;

    }

    class ViewHolder {
    }

}
