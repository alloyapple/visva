package com.sharebravo.bravo.view.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sharebravo.bravo.R;
import com.sharebravo.bravo.sdk.util.network.ImageLoader;

public class AdapterSearchSpotResult extends BaseAdapter {
    private FragmentActivity mContext     = null;
    private ImageLoader      mImageLoader = null;

    public AdapterSearchSpotResult(FragmentActivity fragmentActivity) {
        // TODO Auto-generated constructor stub
        mContext = fragmentActivity;
        mImageLoader = new ImageLoader(mContext);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        // TODO Auto-generated method stub
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.row_recent_post, null);
        ViewHolder holder = new ViewHolder();
        holder.spotAvatar = (ImageView) convertView.findViewById(R.id.img_avatar_spot);
        holder.spotName = (TextView) convertView.findViewById(R.id.txt_spot_name);
        holder.numberBravos = (TextView) convertView.findViewById(R.id.text_number_bravo);

        return convertView;
    }

    class ViewHolder {
        ImageView spotAvatar;
        TextView  spotName;
        TextView  numberBravos;
    }
}
