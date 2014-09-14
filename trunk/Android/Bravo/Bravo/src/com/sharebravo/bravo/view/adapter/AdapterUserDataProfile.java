package com.sharebravo.bravo.view.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sharebravo.bravo.R;
import com.sharebravo.bravo.control.activity.HomeActivity;
import com.sharebravo.bravo.model.response.ObBravo;
import com.sharebravo.bravo.model.response.ObGetUserInfo;
import com.sharebravo.bravo.sdk.log.AIOLog;
import com.sharebravo.bravo.sdk.util.network.ImageLoader;
import com.sharebravo.bravo.utils.StringUtility;
import com.sharebravo.bravo.utils.TimeUtility;

public class AdapterUserDataProfile extends BaseAdapter {
    public static final int         USER_AVATAR_ID = 2003;
    public static final int         USER_COVER_ID  = 2004;
    private FragmentActivity        mContext       = null;
    private UserPostProfileListener mListener      = null;
    private boolean                 isMyData       = false;
    private ObGetUserInfo           mObGetUserInfo = null;
    private ImageLoader             mImageLoader   = null;
    private ArrayList<ObBravo>      mObGetTimeLine = new ArrayList<ObBravo>();
    private boolean                 isFollowing    = false;
    private boolean                 isBlocked      = true;

    public AdapterUserDataProfile(FragmentActivity fragmentActivity) {
        mContext = fragmentActivity;
        mImageLoader = new ImageLoader(mContext);
    }

    public void setListener(UserPostProfileListener _listener) {
        mListener = _listener;
    }

    @Override
    public int getCount() {
        return mObGetTimeLine.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (position == 0) {
            convertView = makeLayoutForUserBasicInfo(convertView, position);
        } else {
            convertView = makeLayoutForUserHistoryTimeLine(position, convertView, parent);
        }
        return convertView;
    }

    private View makeLayoutForUserHistoryTimeLine(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        // if (convertView == null) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.row_recent_post, null);
        // }

        holder = new ViewHolder();
        holder._recentPostImage = (ImageView) convertView.findViewById(R.id.img_post_recent);
        holder._recentPostTime = (TextView) convertView.findViewById(R.id.text_recent_post_time);
        holder._recentPostSpotName = (TextView) convertView.findViewById(R.id.text_recent_post_spot_name);
        holder._userAvatar = (ImageView) convertView.findViewById(R.id.img_recent_post_user_avatar);
        holder._userName = (TextView) convertView.findViewById(R.id.text_recent_post_user_name);
        holder._totalComment = (TextView) convertView.findViewById(R.id.text_total_spot_comment);
        AIOLog.d("mObGetTimeLine.size():" + mObGetTimeLine.size() + ", position:" + position);
        if (mObGetTimeLine.size() > 0 && position < mObGetTimeLine.size() + 1) {
            final ObBravo obGetBravo = mObGetTimeLine.get(position - 1);

            if (mObGetUserInfo.data.Full_Name == null || StringUtility.isEmpty(mObGetUserInfo.data.Full_Name)) {
                holder._userName.setText("Unknown");
            } else
                holder._userName.setText(mObGetUserInfo == null ? "" : mObGetUserInfo.data.Full_Name);
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
                    mListener.onClickUserAvatar(obGetBravo.User_ID);
                }
            });

            // set observer to view
            AIOLog.d("obGetBravo.Last_Pic: " + obGetBravo.Last_Pic);
            String imgSpotUrl = obGetBravo.Last_Pic;
            if (StringUtility.isEmpty(imgSpotUrl)) {
                holder._recentPostImage.setVisibility(View.GONE);
                holder._recentPostSpotName.setBackgroundResource(R.drawable.recent_post_none_img);
            } else {
                holder._recentPostImage.setVisibility(View.VISIBLE);
                holder._recentPostSpotName.setBackgroundResource(R.drawable.bg_home_cover);
                mImageLoader.DisplayImage(imgSpotUrl, R.drawable.user_picture_default, holder._recentPostImage, false);
            }

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
            AIOLog.d("obGetBravo.Total_Comments: " + obGetBravo.Total_Comments + "  holder._totalComment : " + holder._totalComment);
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

    public void updateRecentPostList(ArrayList<ObBravo> bravoItems) {

        if (bravoItems == null)
            mObGetTimeLine.clear();
        else {
            AIOLog.d("bravoItems.size():" + bravoItems.size());
            // ArrayList<ObBravo> newObBravos = removeIncorrectBravoItems(bravoItems);
            mObGetTimeLine = bravoItems;
        }
        notifyDataSetChanged();
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

    private View makeLayoutForUserBasicInfo(View convertView, int position) {
        // if (convertView == null) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.layout_user_post_profile_header, null, false);
        // }
        if (mObGetUserInfo != null) {
            loadingUserImageInfo(convertView, position);
            loadingUserBravos_FollowingInfo(convertView, position);
            loadingUserBravoMapInfo(convertView, position);
            loadingUserFavouriteInfo(convertView, position);
            loadingUserFollowInfo(convertView, position);
            loadingUserBlockInfo(convertView, position);
        }
        return convertView;
    }

    private void loadingUserBlockInfo(View convertView, int position) {
        ImageView blockIcon = (ImageView) convertView.findViewById(R.id.icon_block);
        LinearLayout layoutBlock = (LinearLayout) convertView.findViewById(R.id.layout_block);
        Button btnBlock = (Button) convertView.findViewById(R.id.btn_block);
        blockIcon.setImageResource(isBlocked ? R.drawable.block_icon : R.drawable.block_icon);
        btnBlock.setText(isBlocked ? "Unblock" : "Block");
        if (isMyData) {
            layoutBlock.setVisibility(View.GONE);
        } else {
            layoutBlock.setVisibility(View.VISIBLE);
        }
        btnBlock.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mListener.goToBlock(!isBlocked);
            }
        });

    }

    private void loadingUserFollowInfo(View convertView, int position) {
        ImageView followIcon = (ImageView) convertView.findViewById(R.id.icon_follow);
        LinearLayout layoutFollow = (LinearLayout) convertView.findViewById(R.id.layout_follow);
        Button btnFollow = (Button) convertView.findViewById(R.id.btn_following);
        followIcon.setImageResource(isFollowing ? R.drawable.following_icon : R.drawable.follow_icon);
        btnFollow.setText(isFollowing ? "Following" : "Follow");
        if (isMyData || isBlocked) {
            layoutFollow.setVisibility(View.GONE);
        } else {
            layoutFollow.setVisibility(View.VISIBLE);
        }
        btnFollow.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mListener.goToFollow(!isFollowing);
            }
        });
    }

    private void loadingUserFavouriteInfo(View convertView, int position) {
        LinearLayout layoutFavourites = (LinearLayout) convertView.findViewById(R.id.layout_favourites);
        Button btnFavourites = (Button) convertView.findViewById(R.id.btn_favorites);
        TextView totalFavourites = (TextView) convertView.findViewById(R.id.total_favorite);
        if (isMyData) {
            layoutFavourites.setVisibility(View.VISIBLE);
            int totalMyList = mObGetUserInfo.data.Total_My_List;
            if (totalMyList <= 0) {
                totalFavourites.setText(0 + "");
            } else {
                totalFavourites.setText("" + totalMyList);
            }
        } else {
            layoutFavourites.setVisibility(View.GONE);
        }
        btnFavourites.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mObGetUserInfo == null)
                    return;
                if (mObGetUserInfo.data.Total_My_List <= 0)
                    return;
                mListener.goToFravouriteView(HomeActivity.FRAGMENT_FAVOURITE_ID);
            }
        });
    }

    private void loadingUserBravoMapInfo(View convertView, int position) {
        Button btnBravoMap = (Button) convertView.findViewById(R.id.btn_bravo_map);
        btnBravoMap.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mListener.goToMapView();
            }
        });
    }

    private void loadingUserBravos_FollowingInfo(View convertView, int position) {

        TextView textTotalBravos = (TextView) convertView.findViewById(R.id.text_total_bravos);
        TextView textTotalFollowing = (TextView) convertView.findViewById(R.id.text_total_following);
        TextView textTotalFans = (TextView) convertView.findViewById(R.id.text_total_fans);
        textTotalBravos.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mListener.goToUserTimeline();
            }
        });
        textTotalFollowing.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mListener.goToUserFollowing();
            }
        });
        textTotalFans.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mListener.goToUserFollower();
            }
        });
        int totalBravos = mObGetUserInfo.data.Total_Bravos;
        AIOLog.d("totalBravos:" + totalBravos);
        if (totalBravos <= 0)
            textTotalBravos.setText(0 + "");
        else
            textTotalBravos.setText(totalBravos + "");

        int totalFollowing = mObGetUserInfo.data.Total_Following;
        AIOLog.d("totalFollowing:" + totalFollowing);
        if (totalFollowing <= 0)
            textTotalFollowing.setText(0 + "");
        else
            textTotalFollowing.setText(totalFollowing + "");

        int totalFans = mObGetUserInfo.data.Total_Followers;
        AIOLog.d("totalFans:" + totalFans);
        if (totalFans <= 0)
            textTotalFans.setText(0 + "");
        else
            textTotalFans.setText(totalBravos + "");
    }

    private void loadingUserImageInfo(View convertView, int position) {

        ImageView imgUserCover = (ImageView) convertView.findViewById(R.id.img_user_cover);
        ImageView imgUserAvatar = (ImageView) convertView.findViewById(R.id.img_user_avatar);
        ImageView btnImgCover = (ImageView) convertView.findViewById(R.id.btn_img_cover);
        TextView textUserName = (TextView) convertView.findViewById(R.id.txt_user_name);

        AIOLog.d("mObGetUserInfo.data:" + mObGetUserInfo);
        if (mObGetUserInfo != null) {
            String userCoverImgUrl = mObGetUserInfo.data.Cover_Img_URL;
            AIOLog.d("userCoverImgUrl:" + userCoverImgUrl);
            if (StringUtility.isEmpty(userCoverImgUrl)) {
                imgUserCover.setImageBitmap(null);
                btnImgCover.setVisibility(View.VISIBLE);
                imgUserCover.setBackgroundResource(R.color.click_color);
            } else {
                mImageLoader.DisplayImage(userCoverImgUrl, R.drawable.user_picture_default, imgUserCover, false);
                btnImgCover.setVisibility(View.GONE);
            }

            String userAvatarUrl = mObGetUserInfo.data.Profile_Img_URL;
            AIOLog.d("userAvatarUrl:" + userAvatarUrl);
            if (StringUtility.isEmpty(userAvatarUrl)) {
                imgUserAvatar.setImageBitmap(null);
                imgUserAvatar.setBackgroundResource(R.drawable.btn_user_avatar_profile);
            } else {
                imgUserAvatar.setBackgroundDrawable(null);
                mImageLoader.DisplayImage(userAvatarUrl, R.drawable.user_picture_default, imgUserAvatar, true);
            }

            imgUserCover.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (isMyData) {
                        mListener.requestUserImageType(USER_COVER_ID);
                    }
                }
            });

            imgUserAvatar.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (isMyData) {
                        mListener.requestUserImageType(USER_AVATAR_ID);
                    }
                }
            });
            String userName = mObGetUserInfo.data.Full_Name;
            AIOLog.d("userName:" + userName);
            if (StringUtility.isEmpty(userName)) {
                textUserName.setText(mContext.getResources().getString(R.string.unknown));
            } else {
                textUserName.setText(userName);
            }
        }
    }

    public void updateUserProfile(ObGetUserInfo obGetUserInfo, boolean isMyData) {
        if (obGetUserInfo.data == null)
            return;
        this.isMyData = isMyData;
        this.mObGetUserInfo = obGetUserInfo;
        notifyDataSetChanged();
    }

    public void updateFollow(boolean isFollowing) {
        this.isFollowing = isFollowing;
        notifyDataSetChanged();
    }

    public void updateBlock(boolean isBlocked) {
        this.isBlocked = isBlocked;
        notifyDataSetChanged();
    }

    public void setUserImage(int userImageType) {
        notifyDataSetChanged();
    }

}
