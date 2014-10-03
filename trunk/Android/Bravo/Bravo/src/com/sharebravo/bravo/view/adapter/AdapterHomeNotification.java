package com.sharebravo.bravo.view.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sharebravo.bravo.R;
import com.sharebravo.bravo.model.response.ObGetNotification.Notification;
import com.sharebravo.bravo.sdk.util.network.ImageLoader;
import com.sharebravo.bravo.utils.StringUtility;
import com.sharebravo.bravo.utils.TimeUtility;
import com.sharebravo.bravo.view.adapter.AdapterPostList.IClickUserAvatar;

public class AdapterHomeNotification extends BaseAdapter {
    private ArrayList<Notification> mNotificationList;
    private ImageLoader             mImageLoader = null;

    private Context                 mContext;
    private LayoutInflater          mLayoutInflater;

    private IClickUserAvatar        iClickUserAvatar;

    public AdapterHomeNotification(Context context) {
        // TODO Auto-generated constructor stub
        this.mContext = context;
        mImageLoader = new ImageLoader(mContext);
        mNotificationList = new ArrayList<Notification>();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mNotificationList.size();
        // return 0;
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder;
        if (mLayoutInflater == null)
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = mLayoutInflater.inflate(R.layout.row_home_notification, null);

        holder = new ViewHolder();
        holder._userAvatar = (ImageView) convertView.findViewById(R.id.img_avatar_notify);
        holder._userName = (TextView) convertView.findViewById(R.id.user_name_notify);
        holder._notification = (TextView) convertView.findViewById(R.id.text_notification_description);
        holder._dateTime = (TextView) convertView.findViewById(R.id.text_notification_time);
        final Notification mNo = mNotificationList.get(position);
        String urlAvatar = "";
        String userName = "Unknow";
        String userID = "";
        if (mNo.Notification_Type.equals("comment")) {
            userName = mNo.Last_Commenter_Name;
            urlAvatar = mNo.Last_Commenter_Pic;
            userID = mNo.Last_Commenter_ID;
        } else if (mNo.Notification_Type.equals("bravo")) {
            userName = mContext.getResources().getString(R.string.app_name);
        } else {
            userName = mNo.Notification_Users.get(0).Full_Name;
            urlAvatar = mNo.Notification_Users.get(0).Profile_Img_URL;
            userID = mNo.Notification_Users.get(0).User_ID;
        }
        final String userIDClick = userID;
        holder._userName.setText(userName);

        if (!mNo.Notification_Type.equals("bravo")) {
            if (StringUtility.isEmpty(urlAvatar)) {
                holder._userAvatar.setImageResource(R.drawable.user_picture_default);
            } else {
                mImageLoader.DisplayImage(urlAvatar, R.drawable.user_picture_default, holder._userAvatar, true);
            }
        } else
            holder._userAvatar.setImageResource(R.drawable.ic_launcher);

        holder._userAvatar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!mNo.Notification_Type.equals("bravo"))
                    iClickUserAvatar.onClickUserAvatar(userIDClick);
            }
        });
        String notificationContent = "";
        if (mNo.Notification_Type.equals("bravo")) {
        } else if (mNo.Notification_Type.equals("follow")) {
            notificationContent = mContext.getResources().getString(R.string.notification_follow).replace("%s", userName);
        }
        else if (mNo.Notification_Type.equals("comment")) {
            notificationContent = mContext.getResources().getString(R.string.notification_post_comment)
                    .replace("%s", mNo.Spot_Name);
        }
        else if (mNo.Notification_Type.equals("mylist")) {
            notificationContent = mContext.getResources().getString(R.string.notification_save_bravo)
                    .replace("%s", mNo.Spot_Name).replace("%n", userName);
        }
        else if (mNo.Notification_Type.equals("bravo")) {
            notificationContent = mContext.getResources().getString(R.string.notification_have_bravo)
                    .replace("%1$s", mNo.Spot_Name).replace("%2$s", userName);
        } else {
            notificationContent = mContext.getResources().getString(R.string.notification_news_bravos_yesterday)
                    .replace("%d", "");
        }
        holder._notification.setText(notificationContent);
        long createdTime = 0;
        if (mNo.Date_Created == null)
            createdTime = 0;
        else
            createdTime = mNo.Date_Created.getSec();
        if (createdTime == 0) {
            holder._dateTime.setText("Unknown");
        } else {
            String createdTimeConvertStr = TimeUtility.convertToDateTime(createdTime);
            holder._dateTime.setText(createdTimeConvertStr);
        }
        return convertView;
    }

    public void setListener(IClickUserAvatar iClickUserAvatar) {
        this.iClickUserAvatar = iClickUserAvatar;
    }

    public ArrayList<Notification> getNotificationList() {
        return mNotificationList;
    }

    public void updateNotificationList(ArrayList<Notification> mNotificationList) {
        this.mNotificationList = mNotificationList;
        notifyDataSetChanged();
    }

    class ViewHolder {
        ImageView _userAvatar;
        TextView  _userName;
        TextView  _notification;
        TextView  _dateTime;
    }
}
