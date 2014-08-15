package com.samsung.android.alwayssocial.activity;

import java.util.HashMap;
import java.util.List;

import com.samsung.android.alwayssocial.R;
import com.samsung.android.alwayssocial.app.AlwaysSocialAppImpl;
import com.samsung.android.alwayssocial.constant.GlobalConstant;
import com.samsung.android.alwayssocial.constant.TwitterConstant;
import com.samsung.android.alwayssocial.database.DBManager;
import com.samsung.android.alwayssocial.fragment.TwitterViewDetailsFragment;
import com.samsung.android.alwayssocial.fragment.TwitterViewDetailsFragment.IUpdateDataCallback;
import com.samsung.android.alwayssocial.fragment.TwitterViewPhotoNLinkFragment;
import com.samsung.android.alwayssocial.service.CommentParcelableObject;
import com.samsung.android.alwayssocial.service.IAlwaysService;
import com.samsung.android.alwayssocial.service.IUpdateUiCallback;
import com.samsung.android.alwayssocial.service.StoryItemUnit;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

public class TwitterSecondPageActivity extends Activity implements IUpdateDataCallback {
    public static final String TAG = "TwitterSecondPageActivity";
    private StoryItemUnit mStoryItem;
    private TwitterViewPhotoNLinkFragment mViewPhotoNLink;
    private TwitterViewDetailsFragment mViewStatusDetails;
    private FragmentTransaction mTransaction;
    private IAlwaysService mService = null;
    private Boolean mMoveToDetails = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        setContentView(R.layout.twitter_second_page);
        super.onCreate(savedInstanceState);
        AlwaysSocialAppImpl app = (AlwaysSocialAppImpl) getApplication();
        mService = app.getAlwaysBindService();
        try {
            mService.registerUiListener(GlobalConstant.SOCIAL_TYPE_TWITTER, 1, mServiceListener);
            mStoryItem = (StoryItemUnit) getIntent().getExtras().getParcelable("StoryItemUnit");
            Log.e(TAG, "Intent extral = " + getIntent().getExtras() + " , Feed data =  " + mStoryItem);
            if (getIntent().getData() != null) {
                Uri uri = getIntent().getData();
                if (uri.getScheme() != null && uri.getScheme().equals("magazinesocial") && uri.getHost() != null && uri.getHost().equals("twitter")) {
                    Log.d(TAG, "host:" + uri.getHost() + ", LastPath:" + uri.getLastPathSegment());
                    String feedId = uri.getLastPathSegment().replace("twitter-", "");
                    Log.d(TAG, "Tweet id :" + feedId);
                    HashMap<String, Object> getFeedItem = new HashMap<String, Object>();
                    getFeedItem.put("id", feedId);
                    if (mService != null) {
                        Log.d(TAG, "Request Twitter TweetItem");
                        mService.requestSNS(GlobalConstant.SOCIAL_TYPE_TWITTER, TwitterConstant.TW_DATA_TYPE_TIMELINE, getFeedItem);
                    } else {
                        Log.d(TAG, "Service is null");
                    }
                }

            } else {
                mMoveToDetails = getIntent().getExtras().getBoolean(GlobalConstant.SOCIAL_2ND_VIEW_TYPE, false);
                if (mMoveToDetails == true) {
                    showFragment(TwitterConstant.TW_2ND_DISPLAY_TYPE_STATUS);
                } else {
                    int type = get2ndPageDisplayType(mStoryItem);
                    Log.d(TAG, "type = " + type);
                    showFragment(type);
                }

            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    public void showFragment(int type) {
        Log.d(TAG, "showFragment, type = " + type);
        mTransaction = getFragmentManager().beginTransaction();
        switch (type) {
        case TwitterConstant.TW_2ND_DISPLAY_TYPE_STATUS:
            mViewStatusDetails = new TwitterViewDetailsFragment();
            mViewStatusDetails.setStoryItem(mStoryItem);
            mViewStatusDetails.setListener(this);
            mTransaction.replace(R.id.tw_second_page_fragment, mViewStatusDetails);
            break;
        case TwitterConstant.TW_2ND_DISPLAY_TYPE_LINK:
        case TwitterConstant.TW_2ND_DISPLAY_TYPE_PICTURE:
            mViewPhotoNLink = new TwitterViewPhotoNLinkFragment();
            mViewPhotoNLink.setStoryItem(mStoryItem);
            mTransaction.replace(R.id.tw_second_page_fragment, mViewPhotoNLink);
            break;
        }
        mTransaction.disallowAddToBackStack();
        mTransaction.commit();
    }

    public int get2ndPageDisplayType(StoryItemUnit item) {
        Log.d(TAG, "get2ndPageDisplayType, link_url = " + item.getLink_url().toString() + " bodyurl = " + item.getBody_url());
        if (!item.getLink_url().equals("")) {
            return TwitterConstant.TW_2ND_DISPLAY_TYPE_LINK;
        } else if (!item.getBody_url().equals("")) {
            return TwitterConstant.TW_2ND_DISPLAY_TYPE_PICTURE;
        } else {
            return TwitterConstant.TW_2ND_DISPLAY_TYPE_STATUS;
        }
    }

    public void onCommentButtonClick() {
        showFragment(TwitterConstant.TW_2ND_DISPLAY_TYPE_STATUS);
    }

    public void onFavouriteButtonClick(int type, String id, StoryItemUnit item) {
        switch (type) {
        case TwitterConstant.TW_POST_CREATE_FAVOURITE:
            try {
                if (mService != null) {
                    Log.d(TAG, "Create Twitter favourite, id = " + id);
                    mService.postSNS(GlobalConstant.SOCIAL_TYPE_TWITTER, id, null, TwitterConstant.TW_POST_CREATE_FAVOURITE);
                    DBManager.getInstance(TwitterSecondPageActivity.this).updateLikeCommentInfo(item, item.getNumber_of_like() + 1, item.getNumber_of_comment(), 1);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            break;
        case TwitterConstant.TW_POST_DESTROY_FAVOURITE:
            try {
                if (mService != null) {
                    Log.d(TAG, "Destroy Twitter favourite");
                    mService.postSNS(GlobalConstant.SOCIAL_TYPE_TWITTER, id, null, TwitterConstant.TW_POST_DESTROY_FAVOURITE);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            break;
        default:
            break;
        }

    }


    public void onRetweetButtonClick(String id) {
        try {
            if (mService != null) {
                Log.d(TAG, "Retweet Twitter, id = " + id);
                mService.postSNS(GlobalConstant.SOCIAL_TYPE_TWITTER, id, null, TwitterConstant.TW_POST_RETWEET);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    IUpdateUiCallback mServiceListener = new IUpdateUiCallback.Stub() {

        @Override
        public void onUpdateUiByStoryItem(String socialType, int feedType, boolean isResponseByRefresh) throws RemoteException {
            Log.d(TAG, "onUpdateUiByStoryItem feedType = " + feedType);
            if (feedType == TwitterConstant.TW_POST_RETWEET) {
                showToast("Retweet successfully!!!");
                return;
            }
        }

        @Override
        public void onUpdateLikeCommentCount(String socialType, int requestType, int count, int isUserLike) throws RemoteException {

        }

        @Override
        public void onUpdateErrorUi(String socialType, int errorCode, boolean isResponseByRefresh) throws RemoteException {
            Log.d(TAG, "onUpdateErrorUi errorCode = " + errorCode);
            if (errorCode == TwitterConstant.TW_POST_RETWEET) {
                showToast("Retweet failed!!!");
                return;
            }
        }

        @Override
        public void onUpdateCommnetInforToUi(String socialType, int requestType, List<CommentParcelableObject> comments, String nextPage) throws RemoteException {

        }

        @Override
        public void receiveStoryItems(String socialType, int feedType, List<StoryItemUnit> items, boolean isResponseByRefresh, boolean isFirstTimeUpdate) throws RemoteException {

        }
    };

    @Override
    public void postReplyMessage(int type, String data, String id, StoryItemUnit item) {
        try {
            if (mService != null) {
                Log.d(TAG, "updateData Twitter favourite, id = " + id + " data " + data);
                mService.postSNS(GlobalConstant.SOCIAL_TYPE_TWITTER, id, data, TwitterConstant.TW_POST_REPLY);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void showToast(String infor) {
        Toast.makeText(this, infor, Toast.LENGTH_SHORT).show();
    }
}
