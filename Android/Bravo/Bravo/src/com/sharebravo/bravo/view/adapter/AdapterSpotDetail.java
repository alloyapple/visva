package com.sharebravo.bravo.view.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sharebravo.bravo.R;
import com.sharebravo.bravo.control.activity.HomeActivity;
import com.sharebravo.bravo.model.response.ObGetSpotHistory;
import com.sharebravo.bravo.model.response.ObGetSpotHistory.SpotHistory;
import com.sharebravo.bravo.model.response.ObGetSpotRank;
import com.sharebravo.bravo.model.response.ObGetSpotRank.SpotRank;
import com.sharebravo.bravo.model.response.Spot;
import com.sharebravo.bravo.sdk.log.AIOLog;
import com.sharebravo.bravo.sdk.util.network.ImageLoader;
import com.sharebravo.bravo.utils.StringUtility;
import com.sharebravo.bravo.utils.TimeUtility;
import com.sharebravo.bravo.view.fragment.home.FragmentSpotDetail;
import com.sharebravo.bravo.view.fragment.maps.FragmentMapCover;

public class AdapterSpotDetail extends BaseAdapter {
    private ArrayList<SpotHistory> mSpotHistorys;
    private Context                mContext;
    private Spot                   mSpot;
    FragmentTransaction            fragmentTransaction;
    FragmentSpotDetail             fragment;
    FragmentMapCover               mapFragment;
    private DetailSpotListener     listener;
    private ArrayList<SpotRank>    mSpotRanks   = new ArrayList<ObGetSpotRank.SpotRank>();
    private ImageLoader            mImageLoader = null;

    public AdapterSpotDetail(Context context, FragmentSpotDetail fragment) {
        this.mContext = context;
        mSpotHistorys = new ArrayList<ObGetSpotHistory.SpotHistory>();
        this.fragment = fragment;
        mImageLoader = new ImageLoader(mContext);
        fragmentTransaction = fragment.getChildFragmentManager().beginTransaction();
    }

    @Override
    public int getCount() {
        return mSpotHistorys.size() + 2;
    }

    @Override
    public Object getItem(int arg0) {
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    Button         btnCall;
    Button         btnViewMap;
    TextView       txtSpotName;
    TextView       txtBravoNumber;
    Button         btnTapBravo;
    LayoutInflater mLayoutInflater;
    ImageView      imgCoverSpot;
    FrameLayout    layoutMapview = null;
    LinearLayout   layoutListTopFans;
    LinearLayout   layoutTopFans;
    TextView       btnMoreDetail;
    TextView       btnReport;
    LinearLayout   layoutBtnReport;

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        if (position == 0)
        {
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.layout_spot_detail_header, null, false);
                mapFragment = (FragmentMapCover) fragment.getChildFragmentManager().findFragmentById(R.id.myspot_map);
                if (mapFragment == null) {
                    mapFragment = new FragmentMapCover();
                    fragmentTransaction.replace(R.id.myspot_map, mapFragment).commit();
                }
                btnCall = (Button) convertView.findViewById(R.id.btn_call_spot);
                btnViewMap = (Button) convertView.findViewById(R.id.btn_view_map);
                txtSpotName = (TextView) convertView.findViewById(R.id.txt_spot_name);
                txtBravoNumber = (TextView) convertView.findViewById(R.id.txtView_bravo_number);
                btnTapBravo = (Button) convertView.findViewById(R.id.btn_tap_to_bravo);
                imgCoverSpot = (ImageView) convertView.findViewById(R.id.image_cover_spot);
                layoutMapview = (FrameLayout) convertView.findViewById(R.id.layout_map_cover_spot);
                layoutTopFans = (LinearLayout) convertView.findViewById(R.id.layout_top_fans);
                layoutListTopFans = (LinearLayout) convertView.findViewById(R.id.list_top_fans);
            }
            layoutListTopFans.removeAllViews();
            for (int i = 0; i < mSpotRanks.size(); i++) {
                ImageView fanAvatar = new ImageView(mContext);
                fanAvatar.setScaleType(ScaleType.FIT_XY);
                fanAvatar.setLayoutParams(new LinearLayout.LayoutParams((int) mContext.getResources().getDimension(R.dimen.size_avatar_recent_post),
                        (int) mContext.getResources().getDimension(R.dimen.size_avatar_recent_post)));
                String profile_img_url = mSpotRanks.get(i).profileImgUrl;
                ((LinearLayout.LayoutParams) fanAvatar.getLayoutParams()).leftMargin = 10;

                if (StringUtility.isEmpty(profile_img_url)) {
                    fanAvatar.setImageResource(R.drawable.user_picture_default);
                } else {
                    mImageLoader.DisplayImage(profile_img_url, R.drawable.user_picture_default, fanAvatar, true);
                }
                layoutListTopFans.addView(fanAvatar);
            }
            if (mSpotRanks.size() > 0)
                layoutTopFans.setVisibility(View.VISIBLE);
            else
                layoutTopFans.setVisibility(View.GONE);
            String imgSpotCoverUrl = mSpot.Last_Pic;

            if (StringUtility.isEmpty(imgSpotCoverUrl)) {
                layoutMapview.setVisibility(View.VISIBLE);
            } else {
                layoutMapview.setVisibility(View.INVISIBLE);
                mImageLoader.DisplayImage(imgSpotCoverUrl, R.drawable.user_picture_default, imgCoverSpot, false);
            }
            if (mSpot != null) {
                txtSpotName.setText(mSpot.Spot_Name);
                if (mSpot.Total_Bravos > 0) {
                    txtBravoNumber.setVisibility(View.VISIBLE);
                    txtBravoNumber.setText("" + mSpot.Total_Bravos);
                } else
                    txtBravoNumber.setVisibility(View.GONE);
            }
            btnCall.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    listener.goToCallSpot();
                }
            });
            btnViewMap.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    listener.goToFragment(HomeActivity.FRAGMENT_MAP_VIEW_ID);
                }
            });
            btnTapBravo.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    listener.tapToBravo();
                }
            });
        } else if (position == getCount() - 1)
        {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.layout_spot_detail_footer, null, false);
            btnMoreDetail = (TextView) convertView.findViewById(R.id.btn_detail_foursquare);
            btnMoreDetail.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    listener.goToMoreDetailOn4square();
                }
            });
            layoutBtnReport = (LinearLayout) convertView.findViewById(R.id.layout_btn_report);
            btnReport = (TextView) convertView.findViewById(R.id.btn_report);
            btnReport.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    listener.goToReport();
                }
            });
            if (mSpot.Total_Bravos <= 0)
                layoutBtnReport.setVisibility(View.GONE);
            else
                layoutBtnReport.setVisibility(View.VISIBLE);

        } else {
            if (mLayoutInflater == null)
                mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = mLayoutInflater.inflate(R.layout.row_recent_post, null);

            ViewHolder holder = new ViewHolder();
            holder._recentPostImage = (ImageView) convertView.findViewById(R.id.img_post_recent);
            holder._recentPostTime = (TextView) convertView.findViewById(R.id.text_recent_post_time);
            holder._recentPostSpotName = (TextView) convertView.findViewById(R.id.text_recent_post_spot_name);
            holder._userAvatar = (ImageView) convertView.findViewById(R.id.img_recent_post_user_avatar);
            holder._userName = (TextView) convertView.findViewById(R.id.text_recent_post_user_name);
            holder._totalComment = (TextView) convertView.findViewById(R.id.text_total_spot_comment);
            final SpotHistory mBravo = mSpotHistorys.get(position - 1);
            if (StringUtility.isEmpty(mBravo.fullName)) {
                holder._userName.setText("Unknown");
            } else
                holder._userName.setText(mBravo.fullName);
            holder._recentPostSpotName.setText(mSpot.Spot_Name);

            String profile_img_url = mBravo.profileImgUrl;

            AIOLog.d("mBravo.Profile_Img_URL: " + mBravo.profileImgUrl);
            if (StringUtility.isEmpty(profile_img_url)) {
                holder._userAvatar.setImageResource(R.drawable.user_picture_default);
            } else {
                mImageLoader.DisplayImage(profile_img_url, R.drawable.user_picture_default, holder._userAvatar, true);
            }
            holder._userAvatar.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    listener.goToUserDataTab(mBravo.userID);
                }
            });

            // set observer to view

            String imgSpotUrl = (mBravo.bravoPics != null && mBravo.bravoPics.size() > 0) ? mBravo.bravoPics.get(0) : null;
            // String imgSpotUrl = null;
            if (StringUtility.isEmpty(imgSpotUrl)) {
                holder._recentPostImage.setVisibility(View.GONE);
                holder._recentPostSpotName.setBackgroundResource(R.drawable.recent_post_none_img);
            } else {
                holder._recentPostImage.setVisibility(View.VISIBLE);
                holder._recentPostSpotName.setBackgroundResource(R.drawable.bg_home_cover);
                mImageLoader.DisplayImage(imgSpotUrl, R.drawable.user_picture_default, holder._recentPostImage, false);
            }

            long createdTime = 0;
            if (mBravo.Date_Created == null)
                createdTime = 0;
            else
                createdTime = mBravo.Date_Created.getSec();
            if (createdTime == 0) {
                holder._recentPostTime.setText("Unknown");
            } else {
                String createdTimeConvertStr = TimeUtility.convertToDateTime(mContext, createdTime);
                holder._recentPostTime.setText(createdTimeConvertStr);
                AIOLog.d("mBravo.Date_Created.sec: " + mBravo.Date_Created.getSec());
                AIOLog.d("mBravo.Date_Created.Usec: " + createdTimeConvertStr);
            }
            AIOLog.d("mBravo.Total_Comments: " + mBravo.totalComments + "  holder._totalComment : " + holder._totalComment);
            if (mBravo.totalComments <= 0) {
                holder._totalComment.setVisibility(View.GONE);
            } else {
                holder._totalComment.setVisibility(View.VISIBLE);
                holder._totalComment.setText(mBravo.totalComments + "");
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

    public void updateMapView() {
        if (mapFragment != null)
            mapFragment.changeLocation(FragmentMapCover.mLat, FragmentMapCover.mLong);
    }

    public DetailSpotListener getListener() {
        return listener;
    }

    public void setListener(DetailSpotListener listener) {
        this.listener = listener;
    }

    public Spot getmSpot() {
        return mSpot;
    }

    public void updatSpot(Spot mSpot) {
        this.mSpot = mSpot;
        notifyDataSetChanged();
    }

    public void updatSpotHistory(ArrayList<SpotHistory> mSpotHistorys) {
        this.mSpotHistorys = mSpotHistorys;
        notifyDataSetChanged();
    }

    public ArrayList<SpotRank> getSpotRanks() {
        return mSpotRanks;
    }

    public void updateSpotRanks(ArrayList<SpotRank> mSpotRanks) {
        this.mSpotRanks = mSpotRanks;
        notifyDataSetChanged();
    }

}
