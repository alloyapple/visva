package com.samsung.android.alwayssocial.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.facebook.widget.ProfilePictureView;
import com.samsung.android.alwayssocial.R;
import com.samsung.android.alwayssocial.activity.FacebookSecondPageActivity;
import com.samsung.android.alwayssocial.adapter.FacebookRelatedContentAdapter;
import com.samsung.android.alwayssocial.app.AlwaysSocialAppImpl;
import com.samsung.android.alwayssocial.constant.FacebookConstant;
import com.samsung.android.alwayssocial.constant.GlobalConstant;
import com.samsung.android.alwayssocial.object.SocialUserObject;
import com.samsung.android.alwayssocial.object.facebook.FacebookFeedWrapper;
import com.samsung.android.alwayssocial.object.facebook.FacebookRelatedCommentItem;
import com.samsung.android.alwayssocial.object.facebook.FacebookFeedWrapper.TypeTimeline;
import com.samsung.android.alwayssocial.service.CommentParcelableObject;
import com.samsung.android.alwayssocial.service.IAlwaysService;
import com.samsung.android.alwayssocial.service.IUpdateUiCallback;
import com.samsung.android.alwayssocial.service.StoryItemUnit;
import com.samsung.android.alwayssocial.util.Utils;
import com.samsung.android.alwayssocial.util.VolleySingleton;
import com.samsung.android.alwayssocial.view.DynamicImageView;
import com.samsung.android.alwayssocial.view.ExpandableTextView;
import com.samsung.android.alwayssocial.view.PullAndLoadListView;
import com.samsung.android.alwayssocial.view.PullAndLoadListView.IOnLoadMoreListener;

/*
 * This fragment only displays data of FacebookFeedWrapper
 */
public class FacebookViewDetailsFragment extends Fragment {
    protected static final String TAG = "FacebookViewDetailsFragment";
    private StoryItemUnit mStoryItem;
    private String mStoryItemId;
    private SocialUserObject mFacebookLoggedInUser;

    private PullAndLoadListView mListComment;
    private FacebookRelatedContentAdapter mCommentListAdapter;
    private ArrayList<FacebookRelatedCommentItem> mArrayCommentItem = new ArrayList<FacebookRelatedCommentItem>();
    private EditText mEditComment;
    private Button mSend;
    private LinearLayout mLikeButtonGroup;
//    private ImageView mMiniLikeIcon;
    private TextView mLikeCountText;
    private TextView mCommentCountText;
    private ImageButton mBackBtn;

    private ProfilePictureView mProfilePicture;
    private TextView mProfileName;
    private TextView mTimePostAgo;
    private ExpandableTextView mTxtContentBody;
    private ProfilePictureView mMeProfilePicture;
    IAlwaysService mService = null;

    //progress bar
    private ProgressBar mLikeCountLoading;
    private ProgressBar mCommentLoading;

    //like, comment count
    private int mNumOfLikes;
    private int mNumOfComments;
    private int mIsLiked;
    private String mNextPage;
    //item avatar, pics
    private DynamicImageView mFeedPhoto;
    private ImageLoader mImageLoader;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = (ViewGroup) inflater.inflate(R.layout.facebook_content_feed_text_only, null);
        //=================================
//        mMiniLikeIcon = (ImageView) root.findViewById(R.id.like_button_image);
        mLikeCountText = (TextView) root.findViewById(R.id.like_count);
        mCommentCountText = (TextView) root.findViewById(R.id.txt_comments_label);
        mListComment = (PullAndLoadListView) root.findViewById(R.id.list_comment);
        mEditComment = (EditText) root.findViewById(R.id.edit_comment);
        mSend = (Button) root.findViewById(R.id.btn_send_comment);
        mBackBtn = (ImageButton) root.findViewById(R.id.more_list_back_icon);

        //=================================

        mMeProfilePicture = (ProfilePictureView) root.findViewById(R.id.profile_picture_bottom_comment);
        mProfilePicture = (ProfilePictureView) root.findViewById(R.id.img_profile_picture);
        mProfileName = (TextView) root.findViewById(R.id.txt_profile_name);
        mTimePostAgo = (TextView) root.findViewById(R.id.txt_time_post_ago);
        mTxtContentBody = (ExpandableTextView) root.findViewById(R.id.txt_content_body);
        mLikeButtonGroup = (LinearLayout) root.findViewById(R.id.like_group_view_button);

        mLikeCountLoading = (ProgressBar) root.findViewById(R.id.like_loading_progressBar);
        mCommentLoading = (ProgressBar) root.findViewById(R.id.comment_loading_progress);

        //===============================
        mFeedPhoto = (DynamicImageView) root.findViewById(R.id.feed_photo);

        mService = AlwaysSocialAppImpl.getInstance().getAlwaysBindService();
        HashMap<String, Object> getLikeCountParams = new HashMap<String, Object>();
        getLikeCountParams.put("id", mStoryItem.getSocialFeedId());
        int firstPageFragmentType = ((FacebookSecondPageActivity) getActivity()).getFirstPageFragmentType();
        getLikeCountParams.put(FacebookConstant.FB_FRAGMENT_TYPE, firstPageFragmentType);
        try {
            if (mService != null) {
                mService.registerUiListener(GlobalConstant.SOCIAL_TYPE_FACEBOOK, 1, mServiceListener);
                mService.requestSNS(GlobalConstant.SOCIAL_TYPE_FACEBOOK, FacebookConstant.FB_DATA_TYPE_LIKEINFO, getLikeCountParams);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        ((FacebookSecondPageActivity) getActivity()).setNeedToUpdate(false);
        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mBackBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        // Get Logged in user
        mFacebookLoggedInUser = AlwaysSocialAppImpl.getInstance().getLoggedInUser(GlobalConstant.SOCIAL_TYPE_FACEBOOK);
        mMeProfilePicture.setProfileId(mFacebookLoggedInUser.mId);
        mProfilePicture.setProfileId(mStoryItem.getAuthor_image());
        mTimePostAgo.setText(Utils.convertToDateTime(mStoryItem.getTime_stamp()));
        mProfileName.setText(mStoryItem.getAuthor_name());
        String textContent = mStoryItem.getBody();
        FacebookFeedWrapper.TypeTimeline timelineType = TypeTimeline.valueOf(mStoryItem.getFeeds_type_detail());
        if (timelineType == TypeTimeline.LINK && mStoryItem.getMessage() != null) {
            textContent += "\n(" + mStoryItem.getMessage() + ")";
        }
        String[] itemBody = textContent.split(GlobalConstant.SUB_STRING_CODE);

        if (itemBody.length >= 3 && itemBody[2] != null && !itemBody[2].equals("")) {
            mTxtContentBody.setText(itemBody[2]);
        }
        /*mTxtContentBody.setText(textContent);*/
        //mTxtContentBody.setMovementMethod(new ScrollingMovementMethod());
        mCommentListAdapter = new FacebookRelatedContentAdapter(getActivity(), mArrayCommentItem);
        mListComment.setAdapter(mCommentListAdapter);
        mListComment.setDivider(null);
        mListComment.setDividerHeight(0);
        mListComment.removeHeaderView();
        //        mListComment.getChildCount();

        mStoryItemId = mStoryItem.getSocialFeedId();
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("feedId", mStoryItemId);
        Log.d(TAG, "StoryItem ID = " + mStoryItemId);
        try {
            if (mService != null)
                mService.requestSNS(GlobalConstant.SOCIAL_TYPE_FACEBOOK, FacebookConstant.FB_DATA_MORE_COMMENT, param);
        } catch (RemoteException e1) {
            e1.printStackTrace();
        }

        /* load more from database */
        mListComment.setOnLoadMoreListener(new IOnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                Log.d(TAG, "onLoadMore");
                if (mNextPage != null && mNextPage != "") {
                    Log.d("facebook", "onLoadMore, next Page = " + mNextPage);
                    HashMap<String, Object> param = new HashMap<String, Object>();
                    param.put("util", mNextPage);
                    try {
                        if (mService != null)
                            mService.requestSNS(GlobalConstant.SOCIAL_TYPE_FACEBOOK, FacebookConstant.FB_DATA_MORE_COMMENT, param);
                    } catch (RemoteException e1) {
                        e1.printStackTrace();
                    }
                } else {
                    mListComment.onLoadMoreComplete();
                }
            }
        });
        //========================================

        mSend.setOnClickListener(sendCommentClick);
        mEditComment.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                if (mEditComment.getText().toString().equals(""))
                    mSend.setEnabled(false);
                else
                    mSend.setEnabled(true);
            }
        });

//        mLikeButtonGroup.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                v.setSelected(true);
//                mLikeCountText.setTextColor(Color.WHITE);
//                mMiniLikeIcon.setSelected(true);
//            }
//        });

        mLikeCountLoading.setVisibility(View.VISIBLE);
        mCommentLoading.setVisibility(View.VISIBLE);

        //==========================================
        // for feed photo
        mImageLoader = VolleySingleton.getInstance(getActivity()).getImageLoader();
        if (mStoryItem.getBody_url() != null && !mStoryItem.getBody_url().equals("")) {
            mFeedPhoto.setVisibility(View.GONE);
            mFeedPhoto.setImageUrl(mStoryItem.getBody_url(), mImageLoader);
        }
        else {
            mFeedPhoto.setVisibility(View.GONE);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        try {
            if (mService != null) {
                mService.registerUiListener(GlobalConstant.SOCIAL_TYPE_FACEBOOK, 1, mServiceListener);
            }
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        super.onResume();
    }

    OnClickListener sendCommentClick = new OnClickListener() {

        @Override
        public void onClick(View v) {
            Log.d(TAG, "onCommentClick");
            try {
                if (mService != null)
                {
                    Log.d(TAG, "request comment infor");
                    mService.postSNS(GlobalConstant.SOCIAL_TYPE_FACEBOOK, mStoryItem.getSocialFeedId(), mEditComment.getText().toString(), FacebookConstant.FB_POST_TYPE_COMMENT);
                }
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            String authorOfComment = mFacebookLoggedInUser.mName;
            String imageURL = mFacebookLoggedInUser.mId;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssZ", Locale.US);
            String pubTime = dateFormat.format(new Date(System.currentTimeMillis()));
            String contentComment = mEditComment.getText().toString();

            FacebookRelatedCommentItem commentItem = new FacebookRelatedCommentItem(authorOfComment, imageURL, pubTime, contentComment);
            mArrayCommentItem.add(commentItem);
            mCommentListAdapter.notifyDataSetChanged();

            mEditComment.setText("");
            mSend.setEnabled(false);
            //update number of comment
            mCommentCountText.setText(getResources().getString(R.string.comment_label) + "( " + (mNumOfComments + 1) + " )");
        }
    };

    public void setFeed(StoryItemUnit feed) {
        mStoryItem = feed;
    }

    private IUpdateUiCallback mServiceListener = new IUpdateUiCallback.Stub() {

        @Override
        public void onUpdateUiByStoryItem(String socialType, int feedType, boolean isResponseByRefresh) throws RemoteException {
            // TODO
        }

        @Override
        public void onUpdateErrorUi(String socialType, int errorCode, boolean isResponseByRefresh) throws RemoteException {
            if (GlobalConstant.SOCIAL_TYPE_FACEBOOK.compareTo(socialType) == 0)
            {
                Toast.makeText(getActivity(), "Error in connection or Dont have permission? Error code = " + errorCode, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onUpdateCommnetInforToUi(String socialType, int requestType, List<CommentParcelableObject> comments, String nextPage) throws RemoteException {
            if (FacebookConstant.FB_DATA_MORE_COMMENT == requestType) {
                Log.d(TAG, "load more comments");
                for (int i = 0; i < comments.size(); i++) {
                    String authorOfComment = comments.get(i).fromUserName;
                    String imageURL = comments.get(i).fromUserImageUrl;
                    String pubTime = Utils.convertToDateTime(comments.get(i).created_time);
                    String contentComment = comments.get(i).message;
                    FacebookRelatedCommentItem commentItem = new FacebookRelatedCommentItem(authorOfComment, imageURL, pubTime, contentComment);
                    mArrayCommentItem.add(commentItem);
                }
                mNextPage = nextPage;
                mListComment.setAdapter(mCommentListAdapter);
                mCommentListAdapter.notifyDataSetChanged();

            }
        }

        @Override
        public void onUpdateLikeCommentCount(String socialType, int requestType, int count, int isUserLike) throws RemoteException {
            Log.d(TAG, "onUpdateLikeCommentCount request type = " + requestType + " count = " + count + " isUserLike = " + isUserLike);
            ((FacebookSecondPageActivity) getActivity()).setNeedToUpdate(true);
            if (GlobalConstant.SOCIAL_TYPE_FACEBOOK.compareTo(socialType) == 0) {
                if (requestType == FacebookConstant.FB_DATA_TYPE_LIKEINFO) {
                    mLikeCountLoading.setVisibility(View.GONE);
                    mNumOfLikes = count;
                    mIsLiked = isUserLike;
                    mLikeCountText.setText(Integer.toString(mNumOfLikes));
                    if (isUserLike != 0) {
                        mLikeButtonGroup.setSelected(isUserLike == 1 ? false : true);
                        mLikeButtonGroup.setClickable(false);
                    }
                } else if (requestType == FacebookConstant.FB_DATA_TYPE_COMMENTINFO) {
                    mCommentLoading.setVisibility(View.GONE);
                    mNumOfComments = count;
                    mCommentCountText.setText(getResources().getString(R.string.comment_label) + "( " + count + " )");
                }
            }
        }

        @Override
        public void receiveStoryItems(String socialType, int feedType, List<StoryItemUnit> items, boolean isResponseByRefresh, boolean isFirstTimeUpdate) throws RemoteException {
            // TODO Auto-generated method stub

        }
    };

    public int getLikesCount() {
        return mNumOfLikes;
    }

    public int getCommentsCount() {
        return mNumOfComments;
    }

    public int isUserLiked() {
        if (mIsLiked > 0)
            return mIsLiked;
        return 0;
    }

    @Override
    public void onDestroy() {
        try {
            mService.unregisterUiNotify(GlobalConstant.SOCIAL_TYPE_FACEBOOK, 1);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    public void onBackPressed() {
        Log.d(TAG, "onBackPress");

    }

}
