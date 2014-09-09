package com.sharebravo.bravo.view.adapter;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
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
import com.sharebravo.bravo.model.response.ObGetAllBravoRecentPosts;
import com.sharebravo.bravo.model.response.ObGetUserInfo;
import com.sharebravo.bravo.sdk.log.AIOLog;
import com.sharebravo.bravo.sdk.util.network.ImageLoader;
import com.sharebravo.bravo.utils.BravoConstant;
import com.sharebravo.bravo.utils.BravoSharePrefs;
import com.sharebravo.bravo.utils.BravoUtils;
import com.sharebravo.bravo.utils.StringUtility;
import com.sharebravo.bravo.utils.TimeUtility;

public class AdapterUserDataProfile extends BaseAdapter {
    public static final int         USER_AVATAR_ID            = 2003;
    public static final int         USER_COVER_ID             = 2004;
    private FragmentActivity        mContext                  = null;
    private UserPostProfileListener mListener                 = null;
    private boolean                 isMyData                  = false;
    private ObGetUserInfo           mObGetUserInfo            = null;
    private ImageLoader             mImageLoader              = null;
    private ArrayList<ObBravo>      mObGetAllBravoRecentPosts = new ArrayList<ObBravo>();

    public AdapterUserDataProfile(FragmentActivity fragmentActivity) {
        mContext = fragmentActivity;
        mImageLoader = new ImageLoader(mContext);
    }

    public void setListener(UserPostProfileListener _listener) {
        mListener = _listener;
    }

    @Override
    public int getCount() {
        return mObGetAllBravoRecentPosts.size() + 1;
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
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_recent_post, null);
        }

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

    public void updateRecentPostList(ObGetAllBravoRecentPosts obGetAllBravoRecentPosts) {
        AIOLog.d("mObGetAllBravoRecentPosts.size():" + obGetAllBravoRecentPosts.data.size());
        ArrayList<ObBravo> newObBravos = removeIncorrectBravoItems(obGetAllBravoRecentPosts.data);
        mObGetAllBravoRecentPosts = newObBravos;
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
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.layout_user_post_profile_header, null, false);
        }
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
        LinearLayout layoutBlock = (LinearLayout) convertView.findViewById(R.id.layout_block);
        Button btnBlock = (Button) convertView.findViewById(R.id.btn_block);
        if (isMyData) {
            layoutBlock.setVisibility(View.GONE);
        } else {
            layoutBlock.setVisibility(View.VISIBLE);
        }
        btnBlock.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });

    }

    private void loadingUserFollowInfo(View convertView, int position) {

        LinearLayout layoutFollow = (LinearLayout) convertView.findViewById(R.id.layout_follow);
        Button btnFollow = (Button) convertView.findViewById(R.id.btn_following);
        if (isMyData) {
            layoutFollow.setVisibility(View.GONE);
        } else {
            layoutFollow.setVisibility(View.VISIBLE);
        }
        btnFollow.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

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
                totalFavourites.setText(totalMyList);
            }
        } else {
            layoutFavourites.setVisibility(View.GONE);
        }
    }

    private void loadingUserBravoMapInfo(View convertView, int position) {
        Button btnBravoMap = (Button) convertView.findViewById(R.id.btn_bravo_map);
        btnBravoMap.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mListener.goToFragment(HomeActivity.FRAGMENT_MAP_VIEW_ID);
            }
        });
    }

    private void loadingUserBravos_FollowingInfo(View convertView, int position) {

        TextView textTotalBravos = (TextView) convertView.findViewById(R.id.text_total_bravos);
        TextView textTotalFollowing = (TextView) convertView.findViewById(R.id.text_total_following);
        TextView textTotalFans = (TextView) convertView.findViewById(R.id.text_total_fans);

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
                String coverImagePath = BravoSharePrefs.getInstance(mContext).getStringValue(BravoConstant.PREF_KEY_USER_COVER);
                File file = new File(coverImagePath);
                if (!StringUtility.isEmpty(coverImagePath) && file.exists() && isMyData) {
                    Uri fileUri = Uri.fromFile(file);
                    int orientation = BravoUtils.checkOrientation(fileUri);
                    Bitmap coverBitmap = BravoUtils.decodeSampledBitmapFromFile(coverImagePath, 100, 100, orientation);
                    imgUserCover.setImageBitmap(coverBitmap);
                    btnImgCover.setVisibility(View.GONE);
                } else {
                    imgUserCover.setImageBitmap(null);
                    btnImgCover.setVisibility(View.VISIBLE);
                    imgUserCover.setBackgroundResource(R.color.click_color);
                }
            } else {
                mImageLoader.DisplayImage(userCoverImgUrl, R.drawable.user_picture_default, imgUserCover, false);
                btnImgCover.setVisibility(View.GONE);
            }

            String userAvatarUrl = mObGetUserInfo.data.Profile_Img_URL;
            AIOLog.d("userAvatarUrl:" + userAvatarUrl);
            String avatarImgPath = BravoSharePrefs.getInstance(mContext).getStringValue(BravoConstant.PREF_KEY_USER_AVATAR);
            if (StringUtility.isEmpty(userAvatarUrl)) {
                File file = new File(avatarImgPath);
                if (!StringUtility.isEmpty(avatarImgPath) && file.exists() && isMyData) {
                    Uri fileUri = Uri.fromFile(file);
                    int orientation = BravoUtils.checkOrientation(fileUri);
                    Bitmap avatarBitmap = BravoUtils.decodeSampledBitmapFromFile(avatarImgPath, 100, 100, orientation);
                    imgUserAvatar.setImageBitmap(avatarBitmap);
                } else {
                    imgUserAvatar.setImageBitmap(null);
                    imgUserAvatar.setBackgroundResource(R.drawable.btn_user_avatar_profile);
                }
            } else {
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

    public void setUserImage(int userImageType) {
        notifyDataSetChanged();
    }

}
