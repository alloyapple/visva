package com.samsung.android.alwayssocial.adapter;

import java.util.ArrayList;

import com.samsung.android.alwayssocial.R;
import com.samsung.android.alwayssocial.service.StoryItemUnit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TwitterUserListAdapter extends BaseAdapter {

    private ArrayList<StoryItemUnit> mListUsrList;
    private LayoutInflater mInflater;

    public TwitterUserListAdapter(Context context, ArrayList<StoryItemUnit> userList) {
        mInflater = LayoutInflater.from(context);
        this.mListUsrList = userList;
    }

    public void updateUserList(ArrayList<StoryItemUnit> userList) {
        this.mListUsrList = userList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mListUsrList.size();
    }

    @Override
    public Object getItem(int position) {
        return mListUsrList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
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
        StoryItemUnit storyItem = mListUsrList.get(position);
        holder.mTxtName.setText(storyItem.getTitle() + storyItem.getAuthor_name());
        holder.mTxtDescription.setText(storyItem.getBody());
        holder.mTxtDescription.setSingleLine(true);
        return convertView;
    }

    class ViewHolder {
        TextView mTxtName, mTxtDescription;
    }
}
