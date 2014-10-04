package com.sharebravo.bravo.view.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sharebravo.bravo.R;
import com.sharebravo.bravo.model.response.ObBravo;
import com.sharebravo.bravo.model.response.ObGetAllBravoRecentPosts;
import com.sharebravo.bravo.sdk.log.AIOLog;
import com.sharebravo.bravo.sdk.util.network.ImageLoader;
import com.sharebravo.bravo.utils.BravoUtils;
import com.sharebravo.bravo.utils.StringUtility;
import com.sharebravo.bravo.utils.TimeUtility;

public class AdapterFavourite extends BaseAdapter {
    private boolean            isSortByDate              = true;
    private ArrayList<ObBravo> mObGetAllBravoRecentPosts = new ArrayList<ObBravo>();
    private ImageLoader        mImageLoader              = null;
    private double             mLat, mLong;
    private Context            mContext;
    private LayoutInflater     mLayoutInflater;

    private IClickUserAvatar   iClickUserAvatar;

    public AdapterFavourite(Context context, ObGetAllBravoRecentPosts obGetAllBravoRecentPosts) {
        this.mContext = context;

        if (obGetAllBravoRecentPosts != null)
            mObGetAllBravoRecentPosts = obGetAllBravoRecentPosts.data;
        mImageLoader = new ImageLoader(mContext);

    }

    @Override
    public int getCount() {
        return mObGetAllBravoRecentPosts.size();
    }

    @Override
    public Object getItem(int position) {
        return mObGetAllBravoRecentPosts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (mLayoutInflater == null)
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = mLayoutInflater.inflate(R.layout.row_recent_post, null);

        holder = new ViewHolder();
        holder._recentPostImage = (ImageView) convertView.findViewById(R.id.img_post_recent);
        holder._recentPostTime = (TextView) convertView.findViewById(R.id.text_recent_post_time);
        holder._recentPostSpotName = (TextView) convertView.findViewById(R.id.text_recent_post_spot_name);
        holder._userAvatar = (ImageView) convertView.findViewById(R.id.img_recent_post_user_avatar);
        holder._userName = (TextView) convertView.findViewById(R.id.text_recent_post_user_name);
        holder._totalComment = (TextView) convertView.findViewById(R.id.text_total_spot_comment);
        AIOLog.d("mObGetAllBravoRecentPosts.size():" + mObGetAllBravoRecentPosts.size() + ", position:" + position);
        if (mObGetAllBravoRecentPosts.size() > 0 && position < mObGetAllBravoRecentPosts.size()) {
            final ObBravo obGetBravo = mObGetAllBravoRecentPosts.get(position);

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
                mImageLoader.DisplayImage(profile_img_url, R.drawable.user_picture_default, holder._userAvatar, true);
            }
            holder._userAvatar.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    iClickUserAvatar.onClickUserAvatar(obGetBravo.User_ID);
                }
            });

            // set observer to view
            AIOLog.d("obGetBravo.Last_Pic: " + obGetBravo.Last_Pic);
            String imgSpotUrl = null;
            if (obGetBravo.Bravo_Pics.size() > 0)
                imgSpotUrl = obGetBravo.Bravo_Pics.get(0);
            if (StringUtility.isEmpty(imgSpotUrl)) {
                holder._recentPostImage.setVisibility(View.GONE);
                holder._recentPostSpotName.setBackgroundResource(R.drawable.recent_post_none_img);
            } else {
                holder._recentPostImage.setVisibility(View.VISIBLE);
                holder._recentPostSpotName.setBackgroundResource(R.drawable.bg_home_cover);
                mImageLoader.DisplayImage(imgSpotUrl, R.drawable.user_picture_default, holder._recentPostImage, false);
            }
            if (isSortByDate) {
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
                }
            } else {
                Double distance = BravoUtils.gps2m((float) mLat, (float) mLong, (float) obGetBravo.Spot_Latitude, (float) obGetBravo.Spot_Longitude);
                String result = String.format("%.1f", distance);
                holder._recentPostTime.setText(result + "km");
            }
            if (obGetBravo.Total_Comments <= 0) {
                holder._totalComment.setVisibility(View.GONE);
            } else {
                holder._totalComment.setVisibility(View.VISIBLE);
                holder._totalComment.setText(obGetBravo.Total_Comments + "");
            }
        }
        return convertView;
    }

    class ViewHolder {
        ImageView _userAvatar;
        TextView  _userName;
        ImageView _recentPostImage;
        TextView  _recentPostTime;
        TextView  _recentPostSpotName;
        TextView  _totalComment;
    }

    public void updateRecentPostList(ObGetAllBravoRecentPosts obGetAllBravoRecentPosts, boolean isSortByDate, Double _lat, Double _long) {
        this.isSortByDate = isSortByDate;
        if (!isSortByDate) {
            mLat = _lat;
            mLong = _long;
        }
        AIOLog.d("mObGetAllBravoRecentPosts.size():" + obGetAllBravoRecentPosts.data.size());
        ArrayList<ObBravo> newObBravos = removeIncorrectBravoItems(obGetAllBravoRecentPosts.data);
        mObGetAllBravoRecentPosts = newObBravos;
        notifyDataSetChanged();
    }

    public void updatePullDownLoadMorePostList(ObGetAllBravoRecentPosts obGetAllBravoRecentPosts, boolean isPulDownToRefresh) {
        ArrayList<ObBravo> newObBravos = removeIncorrectBravoItems(obGetAllBravoRecentPosts.data);
        if (isPulDownToRefresh)
            mObGetAllBravoRecentPosts.addAll(0, newObBravos);
        else
            mObGetAllBravoRecentPosts.addAll(newObBravos);
    }

    public ArrayList<ObBravo> removeIncorrectBravoItems(ArrayList<ObBravo> bravoItems) {
        ArrayList<ObBravo> obBravos = new ArrayList<ObBravo>();
        for (ObBravo obBravo : bravoItems) {
            if (StringUtility.isEmpty(obBravo.User_ID) || (StringUtility.isEmpty(obBravo.Full_Name) || "0".equals(obBravo.User_ID))) {
                AIOLog.e("The incorrect bravo items:" + obBravo.User_ID + ", obBravo.Full_Name:" + obBravo.Full_Name);
            } else
                obBravos.add(obBravo);
        }
        return obBravos;
    }

    public interface IClickUserAvatar {
        public void onClickUserAvatar(String userId);
    }

    public void setListener(IClickUserAvatar iClickUserAvatar) {
        this.iClickUserAvatar = iClickUserAvatar;
    }

    public void remove(int position) {
        if (position < mObGetAllBravoRecentPosts.size())
            mObGetAllBravoRecentPosts.remove(position);
        notifyDataSetChanged();
    }
}
