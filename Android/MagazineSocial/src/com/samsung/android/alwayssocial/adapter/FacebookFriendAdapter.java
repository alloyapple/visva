package com.samsung.android.alwayssocial.adapter;

import java.util.ArrayList;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.samsung.android.alwayssocial.R;
import com.samsung.android.alwayssocial.service.StoryItemUnit;
import com.samsung.android.alwayssocial.util.VolleySingleton;

public class FacebookFriendAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<StoryItemUnit> mListFriend;
    private ImageLoader mImageLoader;

    public FacebookFriendAdapter(Context context, ArrayList<StoryItemUnit> mListFriend) {
        this.mContext = context;
        this.mListFriend = mListFriend;
        //this.mImageLoader = new ImageLoader(context);
        mImageLoader = VolleySingleton.getInstance(context).getImageLoader();
    }

    @Override
    public int getCount() {
        return mListFriend.size();
    }

    @Override
    public StoryItemUnit getItem(int position) {
        return this.mListFriend.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup arg2) {
        final ViewHolder holder;
        
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = (RelativeLayout) RelativeLayout.inflate(mContext, R.layout.friend_item, null);
            holder.mImagePhoto = (NetworkImageView) convertView.findViewById(R.id.fb_friend_avatar);
            holder.mTxtName = (TextView) convertView.findViewById(R.id.fb_friend_name);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }



        holder.mTxtName.setSelected(true);
        holder.mTxtName.setText(mListFriend.get(position).getAuthor_name());

        holder.mImagePhoto.setFocusable(false);
        holder.mImagePhoto.setImageUrl(mListFriend.get(position).getAuthor_image(), mImageLoader);
        //mImageLoader.DisplayImage(mListFriend.get(position).getAuthor_image(), imgPhoto);
        return convertView;
    }
    
    class ViewHolder {
        NetworkImageView mImagePhoto;
        TextView mTxtName;
    }

    public void removeItem(int position) {
        mListFriend.remove(position);
        notifyDataSetChanged();
    }

    public void updateFolderList(ArrayList<StoryItemUnit> mListFriend) {
        this.mListFriend = mListFriend;
        notifyDataSetChanged();
    }

    public void addNewPhoto(StoryItemUnit friend) {
        mListFriend.add(friend);
        notifyDataSetChanged();
    }
}
