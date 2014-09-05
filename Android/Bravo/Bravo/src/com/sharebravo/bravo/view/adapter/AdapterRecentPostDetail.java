package com.sharebravo.bravo.view.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.sharebravo.bravo.R;
import com.sharebravo.bravo.control.activity.HomeActivity;
import com.sharebravo.bravo.model.response.ObGetBravo;
import com.sharebravo.bravo.model.response.ObGetComment;
import com.sharebravo.bravo.model.response.ObGetComments;
import com.sharebravo.bravo.sdk.util.network.ImageLoader;
import com.sharebravo.bravo.utils.StringUtility;
import com.sharebravo.bravo.view.fragment.home.FragmentRecentPostDetail;

public class AdapterRecentPostDetail extends BaseAdapter {
    private Context            mContext;
    // private ArrayList<String> commentsData = new ArrayList<String>();
    private DetailPostListener listener;
    private ObGetBravo         bravoObj       = null;
    private ObGetComments      mObGetComments = null;
    private ImageLoader        mImageLoader   = null;
    Fragment                   fragment;
    FragmentTransaction        transaction;

    public AdapterRecentPostDetail(Context context, Fragment fragment) {
        // TODO Auto-generated constructor stub
        this.mContext = context;
        mImageLoader = new ImageLoader(mContext);
        this.fragment = fragment;
        transaction = fragment.getChildFragmentManager().beginTransaction();
        
    }

    public void setListener(DetailPostListener listener) {
        this.listener = listener;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return (mObGetComments == null) ? 0 : mObGetComments.data.size() + 2;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public void setBravoOb(ObGetBravo obj) {
        this.bravoObj = obj;
    }

    ImageView imagePost;
    TextView  contentPost;
    ImageView userAvatar;
    TextView  txtUserName;
    Button    btnCallSpot;
    Button    btnViewMap;
    Button    btnFollow;
    ImageView followIcon;
    boolean   isFollowing;
    EditText  textboxComment;
    Button    btnSubmitComment;
    TextView  btnSave;
    TextView  btnShare;
    boolean   isSave;
    TextView  btnReport;
    Fragment  mapFragment = new Fragment();
    FrameLayout layoutMapview = null;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        if (position == 0) // post content
        {
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.layout_post_detail_header, null, false);
                imagePost = (ImageView) convertView.findViewById(R.id.image_post_detail);
                contentPost = (TextView) convertView.findViewById(R.id.content_post_detail);
                userAvatar = (ImageView) convertView.findViewById(R.id.img_avatar);
                txtUserName = (TextView) convertView.findViewById(R.id.txt_user_name);
                btnCallSpot = (Button) convertView.findViewById(R.id.btn_call_spot);
                btnViewMap = (Button) convertView.findViewById(R.id.btn_view_map);
                followIcon = (ImageView) convertView.findViewById(R.id.icon_follow);
                btnFollow = (Button) convertView.findViewById(R.id.btn_follow);
                btnSave = (TextView) convertView.findViewById(R.id.btn_save);
                btnShare = (TextView) convertView.findViewById(R.id.btn_share);
                layoutMapview =(FrameLayout) convertView.findViewById(R.id.layout_map_img);
//                transaction.add(R.id.img_map, mapFragment);
//                transaction.hide(mapFragment);
//                transaction.commit();
            }
            String imgSpotUrl = bravoObj.Last_Pic;
            if (StringUtility.isEmpty(imgSpotUrl)) {
                imagePost.setImageResource(R.drawable.user_picture_default);
                //transaction.commit();
                layoutMapview.setVisibility(View.VISIBLE);
            } else {
                
                layoutMapview.setVisibility(View.GONE);
                mImageLoader.DisplayImage(imgSpotUrl, R.drawable.user_picture_default, imagePost);
            }
            contentPost.setText(bravoObj.Spot_Name);
            // userAvatar.setDefaultImageResId(R.drawable.user_picture_default);
            userAvatar.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    listener.goToFragment(HomeActivity.FRAGMENT_USER_POST_PROFILE_ID);
                }
            });

            txtUserName.setText(bravoObj.Full_Name);

            btnCallSpot.setOnClickListener(new OnClickListener() {

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

            btnFollow.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    isFollowing = !isFollowing;
                    followIcon.setImageResource(isFollowing ? R.drawable.following_icon : R.drawable.follow_icon);
                    btnFollow.setText(isFollowing ? "Following" : "Follow");
                }
            });

            btnSave.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    isSave = !isSave;
                    btnSave.setCompoundDrawablesWithIntrinsicBounds(isSave ? R.drawable.save_bravo_on : R.drawable.save_bravo_off, 0, 0, 0);
                    btnSave.setText(isSave ? "Saved" : "Save");
                }
            });

            btnShare.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    listener.goToShare();
                }
            });

        }
        else if (position == getCount() - 1) // post content
        {
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.layout_post_detail_footer, null, false);
                textboxComment = (EditText) convertView.findViewById(R.id.textbox_comment);
                btnSubmitComment = (Button) convertView.findViewById(R.id.btn_submit_comment);
                btnReport = (TextView) convertView.findViewById(R.id.btn_report);
            }
            btnSubmitComment.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    listener.goToSubmitComment();
                }
            });
            btnReport.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    listener.goToReport();
                }
            });

        } else {
            int index = position - 1;
            ViewHolderComment holderComment = new ViewHolderComment();
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.row_comment_content, null, false);
                holderComment.mAvatarComment = (NetworkImageView) convertView.findViewById(R.id.img_avatar_comment);
                holderComment.mUserNameComment = (TextView) convertView.findViewById(R.id.txtview_user_name_comment);
                holderComment.mCommentContent = (TextView) convertView.findViewById(R.id.txtview_comment_content);
                holderComment.mCommentDate = (TextView) convertView.findViewById(R.id.comment_txtview_date);

                convertView.setTag(holderComment);
            } else
                holderComment = (ViewHolderComment) convertView.getTag();
            ObGetComment comment = mObGetComments.data.get(index);

            String profile_img_url = comment.profileImgUrl;

            if (StringUtility.isEmpty(profile_img_url)) {
                holderComment.mAvatarComment.setImageResource(R.drawable.home_default_avatar);
            } else {
                mImageLoader.DisplayImage(profile_img_url, R.drawable.home_default_avatar, holderComment.mAvatarComment);
            }
            holderComment.mUserNameComment.setText(comment.fullName);
            holderComment.mCommentContent.setText(comment.commentText);

        }

        return convertView;
    }

    public void updateAllCommentList(ObGetComments objGetComments) {
        mObGetComments = objGetComments;
        notifyDataSetChanged();
    }

    public void updateCommentList() {
        notifyDataSetChanged();
    }

    class ViewHolderComment {
        NetworkImageView mAvatarComment;
        TextView         mUserNameComment;
        TextView         mCommentContent;
        TextView         mCommentDate;
    }

    class ViewHolderHeader {
    }

}
