package com.samsung.android.alwayssocial.activity;

import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import com.samsung.android.alwayssocial.R;
import com.samsung.android.alwayssocial.app.AlwaysSocialAppImpl;
import com.samsung.android.alwayssocial.constant.FacebookConstant;
import com.samsung.android.alwayssocial.constant.GlobalConstant;
import com.samsung.android.alwayssocial.database.DBManager;
import com.samsung.android.alwayssocial.fragment.FacebookViewDetailsFragment;
import com.samsung.android.alwayssocial.fragment.FacebookViewPhotoNLinkFragment;
import com.samsung.android.alwayssocial.object.facebook.FacebookFeedWrapper;
import com.samsung.android.alwayssocial.object.facebook.FacebookFeedWrapper.TypeTimeline;
import com.samsung.android.alwayssocial.service.CommentParcelableObject;
import com.samsung.android.alwayssocial.service.IAlwaysService;
import com.samsung.android.alwayssocial.service.IUpdateUiCallback;
import com.samsung.android.alwayssocial.service.StoryItemUnit;

public class FacebookSecondPageActivity extends Activity {
    public static final String TAG = "FacebookSecondPageActivity";
    private StoryItemUnit mStoryItem;
    private FacebookViewDetailsFragment mViewDetails;
    private FacebookViewPhotoNLinkFragment mViewPhotoNLink;
    private FragmentTransaction mTransaction;
    private IAlwaysService mService = null;
    private Boolean mMoveToDetails = false;
    private boolean mIsNeedToUpdate; // do we need to request like + update comment?
    private int mFragmentType;
    private String mFeedId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        setContentView(R.layout.facebook_second_page);
        super.onCreate(savedInstanceState);
        AlwaysSocialAppImpl app = (AlwaysSocialAppImpl) getApplication();
        mService = app.getAlwaysBindService();
        try {
            mService.registerUiListener(GlobalConstant.SOCIAL_TYPE_FACEBOOK, 1, mServiceListener);
            Log.e(TAG, "Intent extral = " + getIntent().getData() + " , Feed data =  " + mStoryItem);
            if (getIntent().getData() != null) {
                Uri uri = getIntent().getData();
                Log.e(TAG, "getSheme " + (uri.getScheme() != null && uri.getScheme().equals("magazinesocial") && uri.getHost() != null && uri.getHost().equals("facebook")));
                if (uri.getScheme() != null && uri.getScheme().equals("magazinesocial") && uri.getHost() != null && uri.getHost().equals("facebook")) {
                    Log.d(TAG, "host:" + uri.getHost() + ", LastPath:" + uri.getLastPathSegment());
                    mFeedId = uri.getLastPathSegment().replace("facebook-", "");
                    mStoryItem = DBManager.getInstance(this).getStoryItemByFeedId(mFeedId);
                    Log.e("adfdjkfh", "fsdfdsfjdhf " + mStoryItem.getLink_url());
                    if (mStoryItem != null) {
                        FacebookFeedWrapper.TypeTimeline timelineType = TypeTimeline.valueOf(mStoryItem.getFeeds_type_detail());
                        showFragment(timelineType);
                    }else{
                        HashMap<String, Object> getFeedItem = new HashMap<String, Object>();
                        getFeedItem.put("id", mFeedId);
                        if (mService != null) {
                            Log.d(TAG, "Request Facebook FeedItem");
                            mService.requestSNS(GlobalConstant.SOCIAL_TYPE_FACEBOOK, FacebookConstant.FB_DATA_TYPE_FEED_ITEM, getFeedItem);
                        } else {
                            Log.d(TAG, "Service is null");
                        }
                    }
                }
            } else {
                mStoryItem = (StoryItemUnit) getIntent().getExtras().getParcelable("StoryItemUnit");
                mFragmentType = (int) getIntent().getExtras().getInt(FacebookConstant.FB_FRAGMENT_TYPE);
                mMoveToDetails = getIntent().getExtras().getBoolean(GlobalConstant.SOCIAL_2ND_VIEW_TYPE, false);
                if (mMoveToDetails == true) {
                    showFragment(TypeTimeline.STATUS);
                } else {
                    FacebookFeedWrapper.TypeTimeline timelineType = TypeTimeline.valueOf(mStoryItem.getFeeds_type_detail());
                    showFragment(timelineType);
                }

            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void showFragment(TypeTimeline type/*,  boolean isNeedToUpdate*/) {
        mTransaction = getFragmentManager().beginTransaction();
        if (type == TypeTimeline.STATUS) {
            mViewDetails = new FacebookViewDetailsFragment();
            mViewDetails.setFeed(mStoryItem);
            mTransaction.replace(R.id.fb_second_page_fragment, mViewDetails);
        } else if (type == TypeTimeline.PHOTO || type == TypeTimeline.LINK) {
            mViewPhotoNLink = new FacebookViewPhotoNLinkFragment();
            mViewPhotoNLink.setFeed(mStoryItem);
            mTransaction.replace(R.id.fb_second_page_fragment, mViewPhotoNLink);
        } else {
            //for TypeTimeline.VIDEO
            Intent viewIntent = new Intent("android.intent.action.VIEW", Uri.parse(mStoryItem.getLink_url()));
            startActivity(viewIntent);
        }
        mTransaction.disallowAddToBackStack();
        mTransaction.commit();
    }

    public void onCommentButtonClick() {
        showFragment(TypeTimeline.STATUS);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d(TAG, "onBackPressed");
        if (isNeedToUpdate()) {
            if (mViewDetails != null) {
                int likeCount = mViewDetails.getLikesCount();
                int commentCount = mViewDetails.getCommentsCount();
                int isLiked = mViewDetails.isUserLiked();
                AlwaysSocialAppImpl.getInstance().getAlwaysSharePrefs().putStringValue(FacebookConstant.FB_PREF_KEY_LIKECOMMEND + "_id", mStoryItem.getSocialFeedId());
                AlwaysSocialAppImpl.getInstance().getAlwaysSharePrefs().putIntValue(FacebookConstant.FB_PREF_KEY_LIKECOMMEND + "_comments", commentCount);
                AlwaysSocialAppImpl.getInstance().getAlwaysSharePrefs().putIntValue(FacebookConstant.FB_PREF_KEY_LIKECOMMEND + "_likes", likeCount);
                AlwaysSocialAppImpl.getInstance().getAlwaysSharePrefs().putIntValue(FacebookConstant.FB_PREF_KEY_IS_LIKED, isLiked);
                mViewDetails.onBackPressed();
            }

            if (mViewPhotoNLink != null) {
                int likeCount = mViewPhotoNLink.getLikesCount();
                int commentCount = mViewPhotoNLink.getCommentsCount();
                int isLiked = mViewPhotoNLink.isUserLiked();
                AlwaysSocialAppImpl.getInstance().getAlwaysSharePrefs().putStringValue(FacebookConstant.FB_PREF_KEY_LIKECOMMEND + "_id", mStoryItem.getSocialFeedId());
                AlwaysSocialAppImpl.getInstance().getAlwaysSharePrefs().putIntValue(FacebookConstant.FB_PREF_KEY_LIKECOMMEND + "_comments", commentCount);
                AlwaysSocialAppImpl.getInstance().getAlwaysSharePrefs().putIntValue(FacebookConstant.FB_PREF_KEY_LIKECOMMEND + "_likes", likeCount);
                AlwaysSocialAppImpl.getInstance().getAlwaysSharePrefs().putIntValue(FacebookConstant.FB_PREF_KEY_IS_LIKED, isLiked);
                mViewPhotoNLink.onBackPressed();
            }
        }
    }

    IUpdateUiCallback mServiceListener = new IUpdateUiCallback.Stub() {

        @Override
        public void onUpdateUiByStoryItem(String socialType, int feedType, boolean isResponseByRefresh) throws RemoteException {
            Log.d(TAG, "onUpdateUI Social type = " + socialType + " ,fead type = " + feedType);
            mStoryItem = DBManager.getInstance(FacebookSecondPageActivity.this).getStoryItemByFeedId(mFeedId);
            Log.e("adfdjkfh", "fsdfdsfjdhf " + mStoryItem.getLink_url());
            if (mStoryItem != null) {
                FacebookFeedWrapper.TypeTimeline timelineType = TypeTimeline.valueOf(mStoryItem.getFeeds_type_detail());
                showFragment(timelineType);
            }else{
                HashMap<String, Object> getFeedItem = new HashMap<String, Object>();
                getFeedItem.put("id", mFeedId);
                if (mService != null) {
                    Log.d(TAG, "Request Facebook FeedItem");
                    mService.requestSNS(GlobalConstant.SOCIAL_TYPE_FACEBOOK, FacebookConstant.FB_DATA_TYPE_FEED_ITEM, getFeedItem);
                } else {
                    Log.d(TAG, "Service is null");
                }
            }
        }

        @Override
        public void onUpdateErrorUi(String socialType, int errorCode, boolean isReponseByRefresh) throws RemoteException {
            // Do nothing
        }

        @Override
        public void onUpdateCommnetInforToUi(String socialType, int requestType, List<CommentParcelableObject> comments, String nextPage) throws RemoteException {
            // Do nothing
        }

        @Override
        public void onUpdateLikeCommentCount(String socialType, int requestType, int count, int isUserLike) throws RemoteException {
            // Do nothing
        }

        @Override
        public void receiveStoryItems(String socialType, int feedType, List<StoryItemUnit> items, boolean isResponseByRefresh, boolean isFirstTimeUpdate) throws RemoteException {
            // TODO Auto-generated method stub

        }
    };

    public boolean isNeedToUpdate() {
        return mIsNeedToUpdate;
    }

    public void setNeedToUpdate(boolean update) {
        mIsNeedToUpdate = update;
    }

    public int getFirstPageFragmentType() {
        return mFragmentType;
    }
}
