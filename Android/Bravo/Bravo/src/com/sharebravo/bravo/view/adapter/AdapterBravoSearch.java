package com.sharebravo.bravo.view.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sharebravo.bravo.R;
import com.sharebravo.bravo.model.response.Spot;

@SuppressLint("InflateParams")
public class AdapterBravoSearch extends BaseAdapter {
    private FragmentActivity mContext     = null;
//    private ImageLoader      mImageLoader = null;
    private ArrayList<Spot>  mSpots       = new ArrayList<Spot>();
    private LayoutInflater   mLayoutInflater;

    public AdapterBravoSearch(FragmentActivity fragmentActivity) {
        mContext = fragmentActivity;
//        mImageLoader = new ImageLoader(mContext);
    }

    @Override
    public int getCount() {
        return mSpots.size();
    }

    @Override
    public Object getItem(int arg0) {
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parentView) {
        if (mLayoutInflater == null)
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = mLayoutInflater.inflate(R.layout.row_search_spot_result, null);
        ViewHolder holder = new ViewHolder();
        holder.spotAvatar = (ImageView) convertView.findViewById(R.id.img_avatar_spot);
        holder.spotName = (TextView) convertView.findViewById(R.id.txt_spot_name);
        holder.numberBravos = (TextView) convertView.findViewById(R.id.text_number_bravo);
        if (mSpots.size() > 0 && position < mSpots.size()) {
            holder.spotName.setText(mSpots.get(position).Spot_Name);
            holder.numberBravos.setText(mSpots.get(position).Total_Bravos + " Bravos");
        }
        return convertView;
    }

    public void updateData(ArrayList<Spot> mSpots) {
        this.mSpots = mSpots;
        notifyDataSetChanged();
    }

    class ViewHolder {
        ImageView spotAvatar;
        TextView  spotName;
        TextView  numberBravos;
    }
}
