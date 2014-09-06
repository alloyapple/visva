package com.sharebravo.bravo.view.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sharebravo.bravo.R;
import com.sharebravo.bravo.model.response.ObGetUserInfo;
import com.sharebravo.bravo.sdk.log.AIOLog;
import com.sharebravo.bravo.sdk.util.network.ImageLoader;
import com.sharebravo.bravo.utils.StringUtility;

public class AdapterUserDataProfile extends BaseAdapter {
    private static final int        USER_AVATAR_ID    = 2003;
    private static final int        USER_COVER_ID     = 2004;
    private FragmentActivity        mContext          = null;
    private UserPostProfileListener mListener         = null;
    private boolean                 isMyData          = false;
    private ObGetUserInfo           mObGetUserInfo    = null;
    private ImageLoader             mImageLoader      = null;
    private Bitmap                  mUserAvatarBitmap = null;
    private Bitmap                  mUserCoverBitmap  = null;

    public AdapterUserDataProfile(FragmentActivity fragmentActivity) {
        mContext = fragmentActivity;
    }

    public void setListener(UserPostProfileListener _listener) {
        mListener = _listener;
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (position == 0) {
            convertView = makeLayoutForUserBasicInfo(convertView, position);
        }
        return convertView;
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
        // TODO Auto-generated method stub

    }

    private void loadingUserFollowInfo(View convertView, int position) {
        // TODO Auto-generated method stub

    }

    private void loadingUserFavouriteInfo(View convertView, int position) {
        // TODO Auto-generated method stub

    }

    private void loadingUserBravoMapInfo(View convertView, int position) {

    }

    private void loadingUserBravos_FollowingInfo(View convertView, int position) {

        TextView textTotalBravos = (TextView) convertView.findViewById(R.id.text_total_bravos);
        TextView textTotalFollowing = (TextView) convertView.findViewById(R.id.text_total_following);
        TextView textTotalFans = (TextView) convertView.findViewById(R.id.text_total_fans);

        int totalBravos = mObGetUserInfo.data.Total_Bravo;
        if (totalBravos <= 0)
            textTotalBravos.setText(0 + "");
        else
            textTotalBravos.setText(totalBravos + "");

        int totalFollowing = mObGetUserInfo.data.Total_Following;
        if (totalFollowing <= 0)
            textTotalFollowing.setText(0 + "");
        else
            textTotalFollowing.setText(totalFollowing + "");

        int totalFans = mObGetUserInfo.data.Total_Follower;
        if (totalFans <= 0)
            textTotalFans.setText(0 + "");
        else
            textTotalFans.setText(totalBravos + "");

        // return convertView;
    }

    private void loadingUserImageInfo(View convertView, int position) {

        ImageView imgUserCover = (ImageView) convertView.findViewById(R.id.img_user_cover);
        ImageView imgUserAvatar = (ImageView) convertView.findViewById(R.id.img_user_avatar);
        ImageView btnImgCover = (ImageView) convertView.findViewById(R.id.btn_img_cover);
        TextView textUserName = (TextView) convertView.findViewById(R.id.txt_user_name);

        AIOLog.d("mObGetUserInfo.data:" + mObGetUserInfo);
        if (mObGetUserInfo != null) {
            String userCoverImgUrl = mObGetUserInfo.data.Cover_Img_URL;
            if (StringUtility.isEmpty(userCoverImgUrl)) {
                if (mUserCoverBitmap != null) {
                    imgUserCover.setImageBitmap(mUserCoverBitmap);
                    btnImgCover.setVisibility(View.GONE);
                } else {
                    imgUserCover.setBackgroundResource(R.color.click_color);
                    btnImgCover.setVisibility(View.VISIBLE);
                }
            } else {
                mImageLoader.DisplayImage(userCoverImgUrl, R.drawable.user_picture_default, imgUserCover);
                btnImgCover.setVisibility(View.GONE);
            }

            String userAvatarUrl = mObGetUserInfo.data.Profile_Img_URL;
            if (StringUtility.isEmpty(userAvatarUrl)) {
                if (mUserAvatarBitmap != null)
                    imgUserAvatar.setImageBitmap(mUserAvatarBitmap);
                else
                    imgUserAvatar.setBackgroundResource(R.drawable.btn_user_avatar_profile);
            } else {
                mImageLoader.DisplayImage(userAvatarUrl, R.drawable.user_picture_default, imgUserAvatar);
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

    public void setUserImage(Bitmap photo, int userImageType) {
        AIOLog.d("userImageType: " + userImageType + ", photo:" + photo);
        if (USER_AVATAR_ID == userImageType)
            mUserAvatarBitmap = photo;
        else
            mUserCoverBitmap = photo;
        notifyDataSetChanged();
    }
}
