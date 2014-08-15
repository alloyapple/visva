package com.samsung.android.alwayssocial.adapter;

import java.util.List;

import com.samsung.android.alwayssocial.R;
import com.samsung.android.alwayssocial.service.StoryItemUnit;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/*
 * for display list of followers and list you're following
 */
public class TwitterSimpleAdaper extends BaseAdapter {

    private Context mContext;
    private List<StoryItemUnit> mStoryItemList;
    private LayoutInflater mInflater;

    public TwitterSimpleAdaper(Context context, List<StoryItemUnit> userList) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mStoryItemList = userList;
    }

    public void updateUserList(List<StoryItemUnit> userList) {
        mStoryItemList = userList;
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
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.twitter_simple_adapter_item, null);
            holder.txtName = (TextView) convertView.findViewById(R.id.txt_name);
            holder.txtDes = (TextView) convertView.findViewById(R.id.txt_description);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Log.e("asdfkjdsfh "+mStoryItemList.get(position).getAuthor_name(), "asdfdkfh "+mStoryItemList.get(position).getTitle());
        holder.txtName.setText(mStoryItemList.get(position).getTitle()); 
        holder.txtDes.setText(mStoryItemList.get(position).getNumber_of_like() + mStoryItemList.get(position).getNumber_of_like() < 1 ? " follower" : " followers");
        return convertView;
    }

    class ViewHolder {
        TextView txtName;
        TextView txtDes;
    }

}
