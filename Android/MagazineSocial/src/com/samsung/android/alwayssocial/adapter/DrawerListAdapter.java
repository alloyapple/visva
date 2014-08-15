package com.samsung.android.alwayssocial.adapter;

import java.util.ArrayList;

import com.samsung.android.alwayssocial.R;
import com.samsung.android.alwayssocial.object.DrawerSlideMenuItem;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DrawerListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<DrawerSlideMenuItem> navDrawerItems;
     
    public DrawerListAdapter(Context context, ArrayList<DrawerSlideMenuItem> navDrawerItems){
        this.context = context;
        this.navDrawerItems = navDrawerItems;
    }
 
    @Override
    public int getCount() {
        return navDrawerItems.size();
    }
 
    @Override
    public Object getItem(int position) {       
        return navDrawerItems.get(position);
    }
 
    @Override
    public long getItemId(int position) {
        return position;
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.drawer_list_item, null);
            holder = new ViewHolder();
            holder.mImgIcon = (ImageView) convertView.findViewById(R.id.icon);
            holder.mTxtTitle = (TextView) convertView.findViewById(R.id.title);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
          
        holder.mImgIcon.setImageResource(navDrawerItems.get(position).getIcon());        
        holder.mTxtTitle.setText(navDrawerItems.get(position).getTitle());
        
        return convertView;
    }
    
    class ViewHolder {
        ImageView mImgIcon;
        TextView mTxtTitle;
    }
 
}

