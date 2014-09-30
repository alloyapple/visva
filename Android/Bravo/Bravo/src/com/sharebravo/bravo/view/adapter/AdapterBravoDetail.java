package com.sharebravo.bravo.view.adapter;

import android.content.Context;
import android.graphics.Rect;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sharebravo.bravo.R;
import com.sharebravo.bravo.control.activity.HomeActivity;
import com.sharebravo.bravo.model.SessionLogin;
import com.sharebravo.bravo.model.response.ObBravo;
import com.sharebravo.bravo.model.response.ObGetComment;
import com.sharebravo.bravo.model.response.ObGetComments;
import com.sharebravo.bravo.model.response.Spot;
import com.sharebravo.bravo.sdk.log.AIOLog;
import com.sharebravo.bravo.sdk.util.network.ImageLoader;
import com.sharebravo.bravo.utils.BravoConstant;
import com.sharebravo.bravo.utils.BravoSharePrefs;
import com.sharebravo.bravo.utils.BravoUtils;
import com.sharebravo.bravo.utils.StringUtility;
import com.sharebravo.bravo.utils.TimeUtility;
import com.sharebravo.bravo.view.fragment.home.FragmentBravoDetail;
import com.sharebravo.bravo.view.fragment.maps.FragmentMapCover;

public class AdapterBravoDetail extends BaseAdapter {
    private Context            mContext;
    // private ArrayList<String> commentsData = new ArrayList<String>();
    private DetailPostListener listener;
    private ObBravo            bravoObj             = null;
    private ObGetComments      mObGetComments       = null;
    private ImageLoader        mImageLoader         = null;
    private Spot               mSpot                = null;
    FragmentBravoDetail        fragment;
    // FragmentTransaction transaction;
    private SessionLogin       mSessionLogin        = null;
    private int                mLoginBravoViaType   = BravoConstant.NO_LOGIN_SNS;
    private int                lastTopValueAssigned = 0;

    public AdapterBravoDetail(Context context, FragmentBravoDetail fragment) {
        this.mContext = context;
        mImageLoader = new ImageLoader(mContext);
        this.fragment = fragment;
        mLoginBravoViaType = BravoSharePrefs.getInstance(context).getIntValue(BravoConstant.PREF_KEY_SESSION_LOGIN_BRAVO_VIA_TYPE);
        mSessionLogin = BravoUtils.getSession(context, mLoginBravoViaType);
        fragmentTransaction = fragment.getChildFragmentManager().beginTransaction();

    }

    public void setListener(DetailPostListener listener) {
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return ((mObGetComments == null || mObGetComments.data == null || mObGetComments.data.size() == 0) ? 0 : mObGetComments.data.size()) + 2;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setBravoOb(ObBravo obj) {
        this.bravoObj = obj;
    }

    ImageView           imagePost;
    TextView            contentPost;
    ImageView           userAvatar;
    TextView            txtUserName;
    Button              btnCallSpot;
    Button              btnViewMap;
    Button              btnFollow;
    ImageView           followIcon;
    boolean             isFollowing   = false;
    EditText            textboxComment;
    Button              btnSubmitComment;
    TextView            btnLeft;
    TextView            txtLikedNumber;
    TextView            txtCommentNumber;
    TextView            btnRight;
    boolean             isLiked;
    TextView            btnMiddle;
    boolean             isSave;
    TextView            btnReport;
    FragmentMapCover    mapFragment;
    Button              btnLiked;

    ImageView           iconLiked;
    TextView            txtNumberLiked;
    Button              btnSaved;
    ImageView           iconSaved;
    TextView            txtNumberSaved;
    FrameLayout         layoutMapview = null;
    FragmentTransaction fragmentTransaction;
    ImageView           btnChooseImage;
    LinearLayout        layoutLiked;
    LinearLayout        layoutSaved;
    boolean             isShowMap     = false;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
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
                btnLeft = (TextView) convertView.findViewById(R.id.btn_left);
                btnRight = (TextView) convertView.findViewById(R.id.btn_right);
                btnMiddle = (TextView) convertView.findViewById(R.id.btn_middle);
                txtLikedNumber = (TextView) convertView.findViewById(R.id.txtView_like_number);
                txtCommentNumber = (TextView) convertView.findViewById(R.id.txtView_comment_number);
                layoutMapview = (FrameLayout) convertView.findViewById(R.id.layout_map_img);
                btnLiked = (Button) convertView.findViewById(R.id.btn_liked);
                iconLiked = (ImageView) convertView.findViewById(R.id.icon_liked);
                txtNumberLiked = (TextView) convertView.findViewById(R.id.total_liked);
                layoutLiked = (LinearLayout) convertView.findViewById(R.id.layout_liked);

                btnSaved = (Button) convertView.findViewById(R.id.btn_saved);
                iconSaved = (ImageView) convertView.findViewById(R.id.icon_saved);
                txtNumberSaved = (TextView) convertView.findViewById(R.id.total_saved);
                layoutSaved = (LinearLayout) convertView.findViewById(R.id.layout_saved);

                btnChooseImage = (ImageView) convertView.findViewById(R.id.img_picture_choose);

                mapFragment = (FragmentMapCover) fragment.getChildFragmentManager().findFragmentById(R.id.img_map);
                if (mapFragment == null) {
                    mapFragment = new FragmentMapCover();
                    fragmentTransaction.replace(R.id.img_map, mapFragment).commit();
                }

            }

            btnChooseImage.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    listener.choosePicture();
                }
            });
            imagePost.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    listener.goToCoverImage();
                }
            });
            String imgSpotUrl = null;
            if (bravoObj.Bravo_Pics.size() > 0)
                imgSpotUrl = bravoObj.Bravo_Pics.get(0);

            AIOLog.d("bravoObj.Last_Pic: " + bravoObj.Last_Pic);
            if (StringUtility.isEmpty(imgSpotUrl)) {
                layoutMapview.setVisibility(View.VISIBLE);
            } else {
                layoutMapview.setVisibility(View.GONE);
                mImageLoader.DisplayImage(imgSpotUrl, R.drawable.user_picture_default, imagePost, false);
            }
            if (mSpot != null) {
                txtLikedNumber.setText("" + mSpot.Total_Liked_Users);
                txtNumberLiked.setText("" + mSpot.Total_Liked_Users);
                txtNumberSaved.setText("" + mSpot.Total_Saved_Users);
            }
            else {
                txtLikedNumber.setText("0");
                txtNumberLiked.setText("0");
                txtNumberSaved.setText("0");
            }
            txtCommentNumber.setText("" + bravoObj.Total_Comments);
            contentPost.setText(bravoObj.Spot_Name);
            String avatarUrl = bravoObj.Profile_Img_URL;
            AIOLog.d("obGetBravo.Profile_Img_URL: " + bravoObj.Profile_Img_URL);
            if (StringUtility.isEmpty(avatarUrl)) {
                userAvatar.setBackgroundResource(R.drawable.user_picture_default);
            } else {
                mImageLoader.DisplayImage(avatarUrl, R.drawable.user_picture_default, userAvatar, true);
            }
            userAvatar.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    AIOLog.d("bravoObj.User_ID:" + bravoObj.User_ID);
                    listener.goToUserDataTab(bravoObj.User_ID);
                }
            });

            txtUserName.setText(bravoObj.Full_Name);

            btnCallSpot.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    listener.goToCallSpot();
                }
            });

            btnViewMap.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    listener.goToFragment(HomeActivity.FRAGMENT_MAP_VIEW_ID);
                }
            });

            btnFollow.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    listener.goToFollow(!isFollowing);
                }
            });
            followIcon.setImageResource(isFollowing ? R.drawable.following_icon : R.drawable.follow_icon);
            btnFollow.setText(isFollowing ? "Following" : "Follow");
            if (bravoObj.User_ID.equals(mSessionLogin.userID))
            {
                followIcon.setVisibility(View.GONE);
                btnFollow.setVisibility(View.GONE);
                layoutLiked.setVisibility(View.VISIBLE);
                layoutSaved.setVisibility(View.VISIBLE);
                btnChooseImage.setVisibility(View.VISIBLE);
            }
            else {
                followIcon.setVisibility(View.VISIBLE);
                btnFollow.setVisibility(View.VISIBLE);
                layoutLiked.setVisibility(View.GONE);
                layoutSaved.setVisibility(View.GONE);
                btnChooseImage.setVisibility(View.GONE);
            }
            if (bravoObj.User_ID.equals(mSessionLogin.userID)) {
                btnLeft.setBackgroundResource(R.drawable.btn_save);
            } else {
                btnLeft.setBackgroundResource(R.drawable.btn_save2);
            }
            btnLeft.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (bravoObj.User_ID.equals(mSessionLogin.userID)) {
                        listener.goToSave(!isSave);
                    } else {
                        listener.goToLike(!isLiked);
                    }
                }
            });
            if (bravoObj.User_ID.equals(mSessionLogin.userID)) {
                btnLeft.setCompoundDrawablesWithIntrinsicBounds(0, isSave ? R.drawable.save_bravo_on : R.drawable.save_bravo_off, 0, 0);
                btnLeft.setText(isSave ? "Saved" : "Save");
            } else {
                btnLeft.setCompoundDrawablesWithIntrinsicBounds(0, isLiked ? R.drawable.icon_like : R.drawable.icon_like_off, 0, 0);
                btnLeft.setText(isLiked ? "Liked" : "Like");
            }
            btnMiddle.setBackgroundResource(R.drawable.btn_like2);
            if (bravoObj.User_ID.equals(mSessionLogin.userID)) {
                btnMiddle.setVisibility(View.GONE);
            } else {
                btnMiddle.setVisibility(View.VISIBLE);
            }
            btnMiddle.setCompoundDrawablesWithIntrinsicBounds(0, isSave ? R.drawable.save_bravo_on : R.drawable.save_bravo_off, 0, 0);
            btnMiddle.setText(isSave ? "Saved" : "Save");
            btnMiddle.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    listener.goToSave(!isSave);
                }
            });

            if (bravoObj.User_ID.equals(mSessionLogin.userID)) {
                btnRight.setBackgroundResource(R.drawable.btn_share);
            } else {
                btnRight.setBackgroundResource(R.drawable.btn_share2);
            }

            btnRight.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    listener.goToShare();
                }
            });
            btnLiked.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    listener.goToLiked();
                }
            });
            btnSaved.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    listener.goToSaved();
                }
            });

        }
        else if (position == getCount() - 1) // post content
        {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.layout_post_detail_footer, null, false);
            textboxComment = (EditText) convertView.findViewById(R.id.textbox_comment);
            btnSubmitComment = (Button) convertView.findViewById(R.id.btn_submit_comment);
            btnReport = (TextView) convertView.findViewById(R.id.btn_report);
            btnSubmitComment.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    String commentText = textboxComment.getEditableText().toString();
                    if (!commentText.equals(""))
                        listener.goToSubmitComment(commentText);
                }
            });
            btnReport.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
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
            final ObGetComment comment = mObGetComments.data.get(index);

            String profile_img_url = comment.profileImgUrl;
            holderComment.mAvatarComment.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    listener.goToUserDataTab(comment.userID);
                }
            });
            if (StringUtility.isEmpty(profile_img_url)) {
                holderComment.mAvatarComment.setBackgroundResource(R.drawable.user_picture_default);
            } else {
                mImageLoader.DisplayImage(profile_img_url, R.drawable.user_picture_default, holderComment.mAvatarComment, true);
            }
            if (comment.fullName != null)
                holderComment.mUserNameComment.setText(comment.fullName);
            else
                holderComment.mUserNameComment.setText("Unknown");
            holderComment.mCommentContent.setText(comment.commentText);

            long createdTime = 0;
            if (comment.dateCreated == null)
                createdTime = 0;
            else
                createdTime = comment.dateCreated.getSec();
            if (createdTime == 0) {
                holderComment.mCommentDate.setText("Unknown");
            } else {
                String createdTimeConvertStr = TimeUtility.convertToDateTime(createdTime);
                holderComment.mCommentDate.setText(createdTimeConvertStr);
                AIOLog.d("obGetBravo.Date_Created.sec: " + comment.dateCreated.getSec());
                AIOLog.d("obGetBravo.Date_Created.Usec: " + createdTimeConvertStr);
            }

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

    public void updateMapView() {
        if (mapFragment != null)
            mapFragment.changeLocation(FragmentMapCover.mLat, FragmentMapCover.mLong);
    }

    public void updateSave(boolean isSave) {
        this.isSave = isSave;
        notifyDataSetChanged();
    }

    public void updateLike(boolean isLiked) {
        this.isLiked = isLiked;
        notifyDataSetChanged();
    }

    public void updateCommentList() {
        notifyDataSetChanged();
    }

    public Spot getSpot() {
        return mSpot;
    }

    public void updateLikedandSaved(Spot mSpot) {
        this.mSpot = mSpot;
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

    public View getBackGroundParallax() {
        return imagePost;
    }

    public void parallaxImage(View view) {
        Rect rect = new Rect();
        view.getLocalVisibleRect(rect);
        if (lastTopValueAssigned != rect.top) {
            lastTopValueAssigned = rect.top;
            view.setY((float) (rect.top / 2.0));
        }
    }

    public FrameLayout getMapParallax() {
        return layoutMapview;
    }
}
