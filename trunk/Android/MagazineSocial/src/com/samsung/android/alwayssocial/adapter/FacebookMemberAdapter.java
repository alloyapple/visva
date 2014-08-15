package com.samsung.android.alwayssocial.adapter;

import java.util.ArrayList;

import com.facebook.widget.ProfilePictureView;
import com.samsung.android.alwayssocial.R;
import com.samsung.android.alwayssocial.object.facebook.FacebookUser;
import com.samsung.android.alwayssocial.service.StoryItemUnit;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FacebookMemberAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<StoryItemUnit> mListFriend;

    public FacebookMemberAdapter(Context context, ArrayList<StoryItemUnit> mListFriend) {
        this.mContext = context;
        this.mListFriend = mListFriend;
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
        // TODO Auto-generated method stub

        if (convertView == null) {
            convertView = (RelativeLayout) RelativeLayout.inflate(mContext, R.layout.member_item, null);
        }
        final StoryItemUnit item = mListFriend.get(position);
        ProfilePictureView imgProfile = (ProfilePictureView) convertView.findViewById(R.id.fb_user_avatar);
        TextView txtName = (TextView) convertView.findViewById(R.id.fb_user_name);

        txtName.setSelected(true);
        txtName.setText(mListFriend.get(position).getAuthor_name());

        imgProfile.setFocusable(false);

        imgProfile.setProfileId(item.getAuthor_image());
        return convertView;
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

    public interface InviteFriendJoinSPInterface {
        public void inviteFriendJoinSp(FacebookUser friend);
    }

}
