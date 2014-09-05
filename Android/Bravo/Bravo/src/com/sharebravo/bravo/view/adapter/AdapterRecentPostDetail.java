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

import com.sharebravo.bravo.R;
import com.sharebravo.bravo.control.activity.HomeActivity;
import com.sharebravo.bravo.model.SessionLogin;
import com.sharebravo.bravo.model.response.ObBravo;
import com.sharebravo.bravo.model.response.ObGetComment;
import com.sharebravo.bravo.model.response.ObGetComments;
import com.sharebravo.bravo.sdk.util.network.ImageLoader;
import com.sharebravo.bravo.utils.BravoConstant;
import com.sharebravo.bravo.utils.BravoSharePrefs;
import com.sharebravo.bravo.utils.BravoUtils;
import com.sharebravo.bravo.utils.StringUtility;

public class AdapterRecentPostDetail extends BaseAdapter {
    private Context            mContext;
    // private ArrayList<String> commentsData = new ArrayList<String>();
    private DetailPostListener listener;
    private ObBravo         bravoObj           = null;
    private ObGetComments      mObGetComments     = null;
    private ImageLoader        mImageLoader       = null;
    Fragment                   fragment;
    FragmentTransaction        transaction;
    private SessionLogin       mSessionLogin      = null;
    private int                mLoginBravoViaType = BravoConstant.NO_LOGIN_SNS;

    public AdapterRecentPostDetail(Context context, Fragment fragment) {
        // TODO Auto-generated constructor stub
        this.mContext = context;
        mImageLoader = new ImageLoader(mContext);
        this.fragment = fragment;
        mLoginBravoViaType = BravoSharePrefs.getInstance(context).getIntValue(BravoConstant.PREF_KEY_SESSION_LOGIN_BRAVO_VIA_TYPE);
        mSessionLogin = BravoUtils.getSession(context, mLoginBravoViaType);
    }

    public void setListener(DetailPostListener listener) {
        this.listener = listener;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return ((mObGetComments == null || mObGetComments.data == null || mObGetComments.data.size() == 0) ? 0 : mObGetComments.data.size()) + 2;
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

    public void setBravoOb(ObBravo obj) {
        this.bravoObj = obj;
    }

    ImageView   imagePost;
    TextView    contentPost;
    ImageView   userAvatar;
    TextView    txtUserName;
    Button      btnCallSpot;
    Button      btnViewMap;
    Button      btnFollow;
    ImageView   followIcon;
    boolean     isFollowing   = false;
    EditText    textboxComment;
    Button      btnSubmitComment;
    TextView    btnSave;
    TextView    btnShare;
    boolean     isSave;
    TextView    btnReport;
    Fragment    mapFragment   = new Fragment();
    FrameLayout layoutMapview = null;

    // ViewHolderComment holderComment = new ViewHolderComment();

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
                layoutMapview = (FrameLayout) convertView.findViewById(R.id.layout_map_img);

            }
            String imgSpotUrl = bravoObj.Last_Pic;
            if (StringUtility.isEmpty(imgSpotUrl)) {
                layoutMapview.setVisibility(View.VISIBLE);
            } else {

                layoutMapview.setVisibility(View.GONE);
                mImageLoader.DisplayImage(imgSpotUrl, R.drawable.user_picture_default, imagePost);
            }
            contentPost.setText(bravoObj.Spot_Name);
            String avatarUrl = bravoObj.Profile_Img_URL;
            if (StringUtility.isEmpty(imgSpotUrl)) {
                userAvatar.setBackgroundResource(R.drawable.user_picture_default);
            } else {
                mImageLoader.DisplayImage(avatarUrl, R.drawable.user_picture_default, userAvatar);
            }
            userAvatar.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    listener.goToFragment(HomeActivity.FRAGMENT_USER_DATA_TAB_ID);
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
                    listener.goToFollow(!isFollowing);
                }
            });
            followIcon.setImageResource(isFollowing ? R.drawable.following_icon : R.drawable.follow_icon);
            btnFollow.setText(isFollowing ? "Following" : "Follow");
            if (bravoObj.User_ID.equals(mSessionLogin.userID))
            {
                followIcon.setVisibility(View.GONE);
                btnFollow.setVisibility(View.GONE);
            }
            else {
                followIcon.setVisibility(View.VISIBLE);
                btnFollow.setVisibility(View.VISIBLE);
            }
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
            // if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.layout_post_detail_footer, null, false);
            textboxComment = (EditText) convertView.findViewById(R.id.textbox_comment);
            btnSubmitComment = (Button) convertView.findViewById(R.id.btn_submit_comment);
            btnReport = (TextView) convertView.findViewById(R.id.btn_report);
            // }
            btnSubmitComment.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    String commentText = textboxComment.getEditableText().toString();
                    if (!commentText.equals(""))
                        listener.goToSubmitComment(commentText);
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
            // if (convertView == null) {
            ViewHolderComment holderComment = new ViewHolderComment();

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_comment_content, null, false);
            holderComment.mAvatarComment = (ImageView) convertView.findViewById(R.id.img_avatar_comment);
            holderComment.mUserNameComment = (TextView) convertView.findViewById(R.id.txtview_user_name_comment);
            holderComment.mCommentContent = (TextView) convertView.findViewById(R.id.txtview_comment_content);
            holderComment.mCommentDate = (TextView) convertView.findViewById(R.id.comment_txtview_date);
            // }
            /*
             * else
             * holderComment = (ViewHolderComment) convertView.getTag();
             */
            ObGetComment comment = mObGetComments.data.get(index);

            String profile_img_url = comment.profileImgUrl;

            if (StringUtility.isEmpty(profile_img_url)) {
                holderComment.mAvatarComment.setBackgroundResource(R.drawable.home_default_avatar);
            } else {
                mImageLoader.DisplayImage(profile_img_url, R.drawable.home_default_avatar, holderComment.mAvatarComment);
            }
            if (comment.fullName != null)
                holderComment.mUserNameComment.setText(comment.fullName);
            else
                holderComment.mUserNameComment.setText("Unknown");
            holderComment.mCommentContent.setText(comment.commentText);

        }

        return convertView;
    }

    public void updateAllCommentList(ObGetComments objGetComments) {
        mObGetComments = objGetComments;
        notifyDataSetChanged();
    }

    public void updateFollowing(boolean isFollow) {
        this.isFollowing = isFollow;
        notifyDataSetChanged();
    }

    public void updateCommentList() {
        notifyDataSetChanged();
    }

    class ViewHolderComment {
        ImageView mAvatarComment;
        TextView  mUserNameComment;
        TextView  mCommentContent;
        TextView  mCommentDate;
    }

    class ViewHolderHeader {
    }

}
