package com.sharebravo.bravo.view.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.sharebravo.bravo.R;
import com.sharebravo.bravo.model.response.ObGetAllBravoRecentPosts;
import com.sharebravo.bravo.model.response.ObGetBravo;
import com.sharebravo.bravo.sdk.log.AIOLog;
import com.sharebravo.bravo.sdk.provider.VolleyProvider;
import com.sharebravo.bravo.utils.BravoUtils;
import com.sharebravo.bravo.utils.StringUtility;
import com.sharebravo.bravo.utils.TimeUtility;

public class AdapterRecentPost extends BaseAdapter {
    private final int             DEFAULT_ITEM_NUMBER       = 10;
    private ArrayList<ObGetBravo> mObGetAllBravoRecentPosts = new ArrayList<ObGetBravo>();
    private ImageLoader           mImageLoader              = null;

    private Context               mContext;
    private LayoutInflater        mLayoutInflater;

    public AdapterRecentPost(Context context, ObGetAllBravoRecentPosts obGetAllBravoRecentPosts) {
        this.mContext = context;

        if (obGetAllBravoRecentPosts != null)
            mObGetAllBravoRecentPosts = obGetAllBravoRecentPosts.data;
        mImageLoader = VolleyProvider.getInstance(mContext).getImageLoader();

    }

    @Override
    public int getCount() {
        return DEFAULT_ITEM_NUMBER;
    }

    @Override
    public Object getItem(int position) {
        return mObGetAllBravoRecentPosts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (mLayoutInflater == null)
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = mLayoutInflater.inflate(R.layout.row_recent_post, null);

        holder = new ViewHolder();
        holder._recentPostImage = (NetworkImageView) convertView.findViewById(R.id.img_post_recent);
        holder._recentPostTime = (TextView) convertView.findViewById(R.id.text_recent_post_time);
        holder._recentPostSpotName = (TextView) convertView.findViewById(R.id.text_recent_post_spot_name);
        holder._userAvatar = (NetworkImageView) convertView.findViewById(R.id.img_recent_post_user_avatar);
        holder._userName = (TextView) convertView.findViewById(R.id.text_recent_post_user_name);
        AIOLog.d("mObGetAllBravoRecentPosts.size():" + mObGetAllBravoRecentPosts.size());
        if (mObGetAllBravoRecentPosts.size() >= 1) {
            ObGetBravo obGetBravo = mObGetAllBravoRecentPosts.get(position);

            if (StringUtility.isEmpty(obGetBravo.Full_Name)) {
                holder._userName.setText("Unknown");
            } else
                holder._userName.setText(obGetBravo.Full_Name);
            holder._recentPostSpotName.setText(obGetBravo.Spot_Name);

            String profile_img_url = obGetBravo.Profile_Img_URL;

            AIOLog.d("obGetBravo.Profile_Img_URL: " + obGetBravo.Profile_Img_URL);
            if (StringUtility.isEmpty(profile_img_url)) {
                holder._userAvatar.setImageResource(R.drawable.user_picture_default);
            } else {
                holder._userAvatar.setImageUrl(profile_img_url, mImageLoader);
            }
            // set observer to view
            holder._userAvatar.setErrorImageResId(R.drawable.user_picture_default);
            holder._userAvatar.setDefaultImageResId(R.drawable.user_picture_default);

            AIOLog.d("obGetBravo.Last_Pic: " + obGetBravo.Last_Pic);
            String imgSpotUrl = obGetBravo.Last_Pic;
            if (StringUtility.isEmpty(imgSpotUrl)) {
                holder._recentPostImage.setVisibility(View.GONE);
            } else {
                holder._recentPostImage.setVisibility(View.VISIBLE);
                holder._recentPostImage.setImageUrl(imgSpotUrl, mImageLoader);
            }
            // holder._recentPostImage.setErrorImageResId(R.drawable.user_picture_default);
            // holder._recentPostImage.setDefaultImageResId(R.drawable.user_picture_default);
            long createdTime = 0;
            if (obGetBravo.Date_Created == null)
                createdTime = 0;
            else
                createdTime = obGetBravo.Date_Created.getSec();
            if (createdTime == 0) {
                holder._recentPostTime.setText("Unknown");
            } else {
                String createdTimeConvertStr = TimeUtility.convertToDateTime(createdTime);
                holder._recentPostTime.setText(createdTimeConvertStr);
                AIOLog.d("obGetBravo.Date_Created.sec: " + obGetBravo.Date_Created.getSec());
                AIOLog.d("obGetBravo.Date_Created.Usec: " + createdTimeConvertStr);
            }
            // AIOLog.d("obGetBravo.Date_Created.sec: " + obGetBravo.getDateCreated().getSec());
            // String recentPostSpotTime= obGetBravo.Date_Created.sec;
        }
        return convertView;
    }

    class ViewHolder {
        NetworkImageView _userAvatar;
        TextView         _userName;
        NetworkImageView _recentPostImage;
        TextView         _recentPostTime;
        TextView         _recentPostSpotName;
    }

    public void updateRecentPostList(ObGetAllBravoRecentPosts obGetAllBravoRecentPosts) {
        AIOLog.d("mObGetAllBravoRecentPosts.size():" + obGetAllBravoRecentPosts.data.size());
        mObGetAllBravoRecentPosts = obGetAllBravoRecentPosts.data;
        notifyDataSetChanged();
    }

}
