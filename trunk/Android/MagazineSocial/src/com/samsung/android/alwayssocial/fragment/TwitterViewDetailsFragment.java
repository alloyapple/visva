package com.samsung.android.alwayssocial.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog.Builder;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.samsung.android.alwayssocial.R;
import com.samsung.android.alwayssocial.adapter.TwitterRelatedContentAdapter;
import com.samsung.android.alwayssocial.app.AlwaysSocialAppImpl;
import com.samsung.android.alwayssocial.constant.GlobalConstant;
import com.samsung.android.alwayssocial.constant.TwitterConstant;
import com.samsung.android.alwayssocial.object.SocialUserObject;
import com.samsung.android.alwayssocial.object.twitter.TwitterRelatedCommentItem;
import com.samsung.android.alwayssocial.service.CommentParcelableObject;
import com.samsung.android.alwayssocial.service.IAlwaysService;
import com.samsung.android.alwayssocial.service.IUpdateUiCallback;
import com.samsung.android.alwayssocial.service.StoryItemUnit;
import com.samsung.android.alwayssocial.util.Utils;
import com.samsung.android.alwayssocial.util.VolleySingleton;
import com.samsung.android.alwayssocial.view.PullAndLoadListView;

public class TwitterViewDetailsFragment extends Fragment {
    protected static final String TAG = "TwitterViewDetailsFragment";
    private StoryItemUnit mTweet;
    private ImageLoader mImageLoader;
    private String mPostUserName; //user Id of the person who tweet this.
    //    IAlwaysService mService = null;

    /*status*/
    //private LinearLayout mContentBody;
    private NetworkImageView mProfilePicture;
    private TextView mProfileName;
    private TextView mTimePostAgo;
    private TextView mTxtContentBody;

    /*favourite, retweet*/
    private TextView mFavouriteCount;
    private TextView mRetweetCount;
    private ImageButton mBtnHuman;

    /*Mention & In Response*/
    private LinearLayout mLayoutMentions;
    private TextView mMentionUsers;
    //    private LinearLayout mLayoutInResponse;
    //    private ImageView mImgResponseUserProfile;
    //    private TextView mResponseUsers;

    private PullAndLoadListView mListComment;
    private TwitterRelatedContentAdapter mCommentListAdapter;
    private List<TwitterRelatedCommentItem> mArrayCommentItem = new ArrayList<TwitterRelatedCommentItem>();
    private EditText mEditComment;
    private Button mSend;
    private NetworkImageView mMeProfilePicture;

    private ProgressBar mCommentLoading;

    IAlwaysService mService = null;
    //need to compare
    //    private LinearLayout mLikeButtonGroup; 
    //    private ImageView mMiniLikeIcon;
    private ImageButton mBackBtn;

    /*
     * Reply Message
     */
    IUpdateDataCallback mIUpdateDataCallback;

    public TwitterViewDetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = (ViewGroup) inflater.inflate(R.layout.tw_content_tweet_comment, null);

        //mContentBody = (LinearLayout) root.findViewById(R.id.content_body);
        mProfilePicture = (NetworkImageView) root.findViewById(R.id.img_profile_picture);
        mProfileName = (TextView) root.findViewById(R.id.txt_profile_name);
        mTimePostAgo = (TextView) root.findViewById(R.id.txt_time_post_ago);
        mTxtContentBody = (TextView) root.findViewById(R.id.txt_content_body);

        mFavouriteCount = (TextView) root.findViewById(R.id.txt_favourite);
        mRetweetCount = (TextView) root.findViewById(R.id.txt_retweet_count);
        mBtnHuman = (ImageButton) root.findViewById(R.id.img_human);

        mLayoutMentions = (LinearLayout) root.findViewById(R.id.layout_Mentions);
        mMentionUsers = (TextView) root.findViewById(R.id.txt_mention_users);
        //        mLayoutInResponse = (LinearLayout) root.findViewById(R.id.layout_In_Response);
        //        mImgResponseUserProfile = (ImageView) root.findViewById(R.id.img_response_to_profile_picture);
        //        mResponseUsers = (TextView) root.findViewById(R.id.txt_user_response_to);

        //=================================
        //        mMiniLikeIcon = (ImageView) root.findViewById(R.id.like_button_image);
        mListComment = (PullAndLoadListView) root.findViewById(R.id.list_comment);
        mEditComment = (EditText) root.findViewById(R.id.edit_comment);
        mSend = (Button) root.findViewById(R.id.btn_send_comment);
        mBackBtn = (ImageButton) root.findViewById(R.id.more_list_back_icon);
        mMeProfilePicture = (NetworkImageView) root.findViewById(R.id.profile_picture_bottom_comment);
        //=================================
        mCommentLoading = (ProgressBar) root.findViewById(R.id.progress_comment_loading);
        /*
         * Reply Message
         */
        mEditComment.setText("@" + mTweet.getAuthor_name());
        mSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Log.d(TAG, "click send");
                String replyMessage = mEditComment.getText().toString();
                if (replyMessage.length() > TwitterConstant.TW_MAX_LENGTH_OF_TWEET) {
                    Toast.makeText(getActivity(), TwitterConstant.TW_NOTIFCATION + replyMessage.length(), Toast.LENGTH_LONG).show();
                    return;
                } else {
                    mIUpdateDataCallback.postReplyMessage(TwitterConstant.TW_POST_REPLY, replyMessage, mTweet.getSocialFeedId(), mTweet);
                    mEditComment.setText("");
                }

            }
        });

        mService = AlwaysSocialAppImpl.getInstance().getAlwaysBindService();
        HashMap<String, Object> commentParams = new HashMap<String, Object>();
        commentParams.put(TwitterConstant.TW_KEY_COMMENT_ID, mTweet.getSocialFeedId());
        commentParams.put(TwitterConstant.TW_KEY_STATUS_USER_NAME, mTweet.getAuthor_name());
        try {
            if (mService != null) {
                mService.registerUiListener(GlobalConstant.SOCIAL_TYPE_TWITTER, 1, mServiceListener);
                mService.requestSNS(GlobalConstant.SOCIAL_TYPE_TWITTER, TwitterConstant.TW_DATA_TYPE_COMMENT, commentParams);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        //mImageLoader = new ImageLoader(getActivity());
        mImageLoader = VolleySingleton.getInstance(getActivity()).getImageLoader();
        //mImageLoader.displayImage(mTweet.getAuthor_image(), mProfilePicture, true);
        mProfilePicture.setImageUrl(mTweet.getAuthor_image(), mImageLoader);
        mProfileName.setText(mTweet.getAuthor_name());
        mProfileName.setTextColor(getResources().getColor(R.color.blue_twiter));
        mTimePostAgo.setText(Utils.convertToDateTimeTwitter(mTweet.getTime_stamp()));
        mTxtContentBody.setText(mTweet.getBody());
        mPostUserName = mTweet.getAuthor_name();

        mFavouriteCount.setText(mTweet.getNumber_of_like() + "");
        if (mTweet.isLikedFeed() == 1) {
            mFavouriteCount.setActivated(true);
        } else {
            mFavouriteCount.setActivated(false);
        }
        mRetweetCount.setText(mTweet.getNumber_of_comment() + "");

        String source = mTweet.getSource();
        if (source.equals("")) {
            mLayoutMentions.setVisibility(View.GONE);
        } else {
            mLayoutMentions.setVisibility(View.VISIBLE);
            String[] mentionArrary = source.split(GlobalConstant.SUB_STRING_CODE);
            int length = mentionArrary.length;
            if (length == 1) {
                mMentionUsers.setText(mentionArrary[0]);
                mMentionUsers.setTextColor(getResources().getColor(R.color.blue_twiter));
            } else if (length == 2) {
                final SpannableStringBuilder sb = new SpannableStringBuilder(mentionArrary[0] + " and " + mentionArrary[1]);
                final ForegroundColorSpan fcs = new ForegroundColorSpan(getResources().getColor(R.color.blue_twiter));
                final ForegroundColorSpan fcSpan = new ForegroundColorSpan(getResources().getColor(R.color.blue_twiter));
                sb.setSpan(fcs, 0, mentionArrary[0].length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                sb.setSpan(fcSpan, mentionArrary[0].length() + 5, sb.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                mMentionUsers.setText(sb);

            } else {
                final SpannableStringBuilder sb = new SpannableStringBuilder(mentionArrary[0] + ", " + mentionArrary[1] + " and " + (length - 2) + " others");
                final ForegroundColorSpan fcs = new ForegroundColorSpan(getResources().getColor(R.color.blue_twiter));
                final ForegroundColorSpan fcSpan = new ForegroundColorSpan(getResources().getColor(R.color.blue_twiter));
                sb.setSpan(fcs, 0, mentionArrary[0].length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                sb.setSpan(fcSpan, mentionArrary[0].length() + 2, mentionArrary[0].length() + 2 + mentionArrary[1].length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                mMentionUsers.setText(sb);
            }
        }
        mBackBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                getActivity().finish();
            }
        });
        //later, need to request to get full profile of response user
        //        if(mTweet.getTarget() == null){
        //            mLayoutInResponse.setVisibility(View.GONE);
        //        } else {
        //            mLayoutInResponse.setVisibility(View.VISIBLE);
        //            mResponseUsers.setText(mTweet.getTarget());
        //        }

        mBtnHuman.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d(TAG, "onFollow Click");
                if (!Utils.isNetworkAvailable(getActivity())) {
                    Toast.makeText(getActivity(), R.string.no_networks_found, Toast.LENGTH_SHORT).show();
                } else {
                    Builder dialogBuilder = new Builder(getActivity());
                    String[] functions = { "Unfollow " + mPostUserName, "Cancel" };
                    dialogBuilder.setItems(functions, new OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                            case 0:
                                try {
                                    if (mService != null) {
                                        Log.d(TAG, "Destroy friendship for user " + mPostUserName);
                                        mService.postSNS(GlobalConstant.SOCIAL_TYPE_TWITTER, mPostUserName, null, TwitterConstant.TW_POST_DESTROY_FRIENDSHIP);
                                    }
                                } catch (RemoteException e) {
                                    Log.d(TAG, "error occured");
                                    e.printStackTrace();
                                }
                                break;
                            case 1:
                                break;
                            default:
                                break;
                            }
                        }
                    });
                    dialogBuilder.create();
                    dialogBuilder.show();
                }
            }
        });

        //user profile
        SocialUserObject logInUser = AlwaysSocialAppImpl.getInstance().getLoggedInUser(GlobalConstant.SOCIAL_TYPE_TWITTER);
        //mImageLoader.displayImage(logInUser.mImageUrl, mMeProfilePicture, true);
        mMeProfilePicture.setImageUrl(logInUser.mImageUrl, mImageLoader);

        mCommentListAdapter = new TwitterRelatedContentAdapter(getActivity(), mArrayCommentItem);
        mListComment.setAdapter(mCommentListAdapter);
        mListComment.setDivider(null);
        mListComment.setDividerHeight(0);
        mListComment.removeHeaderView();
        mCommentLoading.setVisibility(View.VISIBLE);
    }

    public void setStoryItem(StoryItemUnit item) {
        this.mTweet = item;
    }

    /*
     * Interface for replying message of tweet
     */
    public interface IUpdateDataCallback {

        public void postReplyMessage(int type, String data, String id, StoryItemUnit item);
    }

    public void setListener(IUpdateDataCallback callback) {
        this.mIUpdateDataCallback = callback;
    }

    private IUpdateUiCallback mServiceListener = new IUpdateUiCallback.Stub() {

        @Override
        public void receiveStoryItems(String socialType, int feedType, List<StoryItemUnit> Items, boolean isResponseByRefresh, boolean isFirstTimeUpdate) throws RemoteException {
            // TODO Auto-generated method stub

        }

        @Override
        public void onUpdateUiByStoryItem(String socialType, int feedType, boolean isResponseByRefresh) throws RemoteException {
            Log.d(TAG, "onUpdateUiByStoryItem in ViewDetailsFragment");
            if (GlobalConstant.SOCIAL_TYPE_TWITTER.compareTo(socialType) == 0 && feedType == TwitterConstant.TW_POST_DESTROY_FRIENDSHIP) {
                Toast.makeText(getActivity(), "Unfollow " + mPostUserName, Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        public void onUpdateLikeCommentCount(String socialType, int requestType, int count, int isUserLike) throws RemoteException {
            // TODO Auto-generated method stub
        }

        @Override
        public void onUpdateErrorUi(String socialType, int errorCode, boolean isResponseByRefresh) throws RemoteException {
            if (GlobalConstant.SOCIAL_TYPE_TWITTER.compareTo(socialType) == 0 && errorCode == TwitterConstant.TW_POST_DESTROY_FRIENDSHIP) {
                Toast.makeText(getActivity(), "Error occured, can't unfollow " + mPostUserName + ", please try again later!", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onUpdateCommnetInforToUi(String socialType, int requestType, List<CommentParcelableObject> comments, String nextPage) throws RemoteException {
            // TODO Auto-generated method stub
            if (GlobalConstant.SOCIAL_TYPE_TWITTER.compareTo(socialType) == 0 && requestType == TwitterConstant.TW_DATA_TYPE_COMMENT) {
                for (int i = 0; i < comments.size(); i++) {
                    String author = comments.get(i).fromUserName;
                    String authorImgUrl = comments.get(i).fromUserImageUrl;
                    String message = comments.get(i).message;
                    String createTime = comments.get(i).created_time;
                    TwitterRelatedCommentItem item = new TwitterRelatedCommentItem(author, authorImgUrl, createTime, message);
                    mArrayCommentItem.add(item);
                }
                mCommentLoading.setVisibility(View.GONE);
                mListComment.setAdapter(mCommentListAdapter);
                mCommentListAdapter.notifyDataSetChanged();
            }
        }
    };

}
