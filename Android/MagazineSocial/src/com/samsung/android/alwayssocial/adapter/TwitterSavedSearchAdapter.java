package com.samsung.android.alwayssocial.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.samsung.android.alwayssocial.R;
import com.samsung.android.alwayssocial.service.StoryItemUnit;

public class TwitterSavedSearchAdapter extends BaseAdapter {

    private ArrayList<StoryItemUnit> mStoryItemList;
    private LayoutInflater mInflater;

    public TwitterSavedSearchAdapter(Context context, List<StoryItemUnit> listSavedItems) {
        mStoryItemList = (ArrayList<StoryItemUnit>) listSavedItems;
        mInflater = LayoutInflater.from(context);
    }

    public void updateSavedList(ArrayList<StoryItemUnit> listSavedItem) {
        mStoryItemList = listSavedItem;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mStoryItemList.size();
    }

    @Override
    public StoryItemUnit getItem(int position) {
        return mStoryItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewParent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.twitter_simple_adapter_item, null);
            holder.mTxtName = (TextView) convertView.findViewById(R.id.txt_name);
            holder.mTxtDescription = (TextView) convertView.findViewById(R.id.txt_description);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        StoryItemUnit storyItemUnit = mStoryItemList.get(position);
        holder.mTxtName.setText(storyItemUnit.getTitle());
        holder.mTxtDescription.setVisibility(View.VISIBLE);
        return convertView;
    }

    class ViewHolder {
        TextView mTxtName, mTxtDescription;
    }
}
