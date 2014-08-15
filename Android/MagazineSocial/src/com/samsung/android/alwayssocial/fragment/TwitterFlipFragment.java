package com.samsung.android.alwayssocial.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.aphidmobile.flip.FlipViewController;
import com.samsung.android.alwayssocial.R;
import com.samsung.android.alwayssocial.activity.TwitterSecondPageActivity;
import com.samsung.android.alwayssocial.adapter.TwitterTimeLineFlipAdapter;
import com.samsung.android.alwayssocial.adapter.TwitterTimeLineFlipAdapter.IOnClickCallback;
import com.samsung.android.alwayssocial.app.AlwaysSocialAppImpl;
import com.samsung.android.alwayssocial.constant.GlobalConstant;
import com.samsung.android.alwayssocial.constant.TwitterConstant;
import com.samsung.android.alwayssocial.database.DBManager;
import com.samsung.android.alwayssocial.service.CommentParcelableObject;
import com.samsung.android.alwayssocial.service.IAlwaysService;
import com.samsung.android.alwayssocial.service.IUpdateUiCallback;
import com.samsung.android.alwayssocial.service.StoryItemUnit;

public class TwitterFlipFragment extends Fragment implements IOnClickCallback {
    protected static final String TAG = "TwitterTimeLineFlipFragment";

    private BaseAdapter mAdapter;
    private FlipViewController mFlipView;
    protected RelativeLayout mLinearProgressBar;
    protected View mLayoutEmpty;
    private int mFragmentType;
    private boolean mIsFirstRequest = true;

    private ArrayList<StoryItemUnit> mListTwStatuses = new ArrayList<StoryItemUnit>();

    IAlwaysService mService = null;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        buildAdapter(mFragmentType);
        mService = AlwaysSocialAppImpl.getInstance().getAlwaysBindService();
        try {
            mService.registerUiListener(GlobalConstant.SOCIAL_TYPE_TWITTER, 1, mServiceListener);
            mService.requestSNS(GlobalConstant.SOCIAL_TYPE_TWITTER, mFragmentType, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = (ViewGroup) inflater.inflate(R.layout.page_flip_type, null);
        mFlipView = (FlipViewController) root.findViewById(R.id.fragment_flip_controller);
        mLinearProgressBar = (RelativeLayout) root.findViewById(R.id.fragment_flip_progress_layout);
        mLayoutEmpty = root.findViewById(R.id.fragment_flip_empty_data);
        mLinearProgressBar.setVisibility(View.VISIBLE);

        mFlipView.setAnimationBitmapFormat(Bitmap.Config.ARGB_4444);
        mFlipView.setOnViewFlipListener(new FlipViewController.ViewFlipListener() {

            @Override
            public void onViewFlipped(View view, int position) {
                if (position == mAdapter.getCount() - 1) {
                    // Load more function
                    onFlipLoadMore();
                }
                if (position == 0) {
                    // Refresh function
                    onFlipRefresh();
                }
            }
        });

        return root;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        try {
            if (mService != null) {
                Log.d(TAG, "Register listener");
                mService.registerUiListener(GlobalConstant.SOCIAL_TYPE_TWITTER, 1, mServiceListener);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private IUpdateUiCallback mServiceListener = new IUpdateUiCallback.Stub() {

        @Override
        public void onUpdateUiByStoryItem(String socialType, int feedType, boolean isResponseByRefresh) throws RemoteException {
            ArrayList<StoryItemUnit> storyItemList = new ArrayList<StoryItemUnit>();
            // Need check if need to sort or not (default set is true)
            storyItemList = DBManager.getInstance(getActivity()).updateDataToUI(socialType, feedType, true);
            updateUI(socialType, feedType, storyItemList, isResponseByRefresh);
        }

        @Override
        public void onUpdateErrorUi(String socialType, int errorCode, boolean isResponseByRefresh) throws RemoteException {
            // TODO Auto-generated method stub
            switch (errorCode) {
            case TwitterConstant.TW_POST_RETWEET:
                Toast.makeText(getActivity(), "Retweet failed!!", Toast.LENGTH_SHORT).show();
                break;
            case TwitterConstant.TW_DATA_TYPE_LIST_FOLLOWED:
            case TwitterConstant.TW_DATA_TYPE_LIST_FOLLOWING_ME:
            case TwitterConstant.TW_DATA_TYPE_MENTIONING_TWEETS:
            case TwitterConstant.TW_DATA_TYPE_TIMELINE:
            case TwitterConstant.TW_DATA_TYPE_TIMELINE_LINKS:
                Toast.makeText(getActivity(), "Cannot request to twitter as twitter's limit rating policy", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
            }

        }

        @Override
        public void onUpdateCommnetInforToUi(String socialType, int requestType, List<CommentParcelableObject> comments, String nextPage) throws RemoteException {
            // TODO Auto-generated method stub

        }

        @Override
        public void onUpdateLikeCommentCount(String socialType, int requestType, int count, int isUserLike) throws RemoteException {
            // TODO Auto-generated method stub

        }

        @Override
        public void receiveStoryItems(String socialType, int feedType, List<StoryItemUnit> items, boolean isResponseByRefresh, boolean isFirstTimeUpdate) throws RemoteException {
            // TODO Auto-generated method stub

        }
    };

    private void updateUI(String socialType, int feedType, ArrayList<StoryItemUnit> storyItemList, boolean isResponseByRefresh) {
        // TODO Auto-generated method stub
        Log.d(TAG, "updateUI() SocialType = " + socialType + " feedType = " + feedType + "mFragmentType =" + mFragmentType + " storyItemList" + storyItemList.size());
        if ((GlobalConstant.SOCIAL_TYPE_TWITTER.compareTo(socialType) != 0 || storyItemList == null) && feedType != TwitterConstant.TW_POST_RETWEET) {
            return;
        }

        if (feedType == TwitterConstant.TW_POST_RETWEET) {
            Toast.makeText(getActivity(), "Retweet successfully!!!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (storyItemList.size() == 0) {
            // if have no data is in database, request
            if (mIsFirstRequest) {
                onFlipRefresh();
            } else {
                mLinearProgressBar.setVisibility(View.GONE);
                mLayoutEmpty.setVisibility(View.VISIBLE);
            }
            return;
        }
        int fragmentType = getFragmentCommonType();
        Log.d(TAG, "onUpdate fragmentType = " + fragmentType);
        switch (fragmentType) {
        case TwitterConstant.TW_DATA_TYPE_TIMELINE:
        case TwitterConstant.TW_DATA_TYPE_TIMELINE_LINKS:
        case TwitterConstant.TW_DATA_TYPE_TWEETS:
        case TwitterConstant.TW_DATA_TYPE_FAVORITES:
        case TwitterConstant.TW_DATA_TYPE_MENTIONING_TWEETS:
            ((TwitterTimeLineFlipAdapter) mAdapter).updateTimelineList(storyItemList);
            break;
        default:
            Log.d(TAG, "Not handle Fragment Type = " + mFragmentType);
            break;
        }

        mLinearProgressBar.setVisibility(View.GONE);
        mFlipView.setVisibility(View.VISIBLE);
    }

    private void buildAdapter(int fragmentType) {
        //we will use 2 kinds of adapter only
        Log.d(TAG, "buildAdapter type = " + fragmentType);
        mAdapter = null;
        switch (fragmentType) {
        case TwitterConstant.TW_DATA_TYPE_TIMELINE:
        case TwitterConstant.TW_DATA_TYPE_TIMELINE_LINKS:
        case TwitterConstant.TW_DATA_TYPE_TWEETS:
        case TwitterConstant.TW_DATA_TYPE_FAVORITES:
        case TwitterConstant.TW_DATA_TYPE_MENTIONING_TWEETS:
            Log.d(TAG, "build adapter TIMELINE");
            mAdapter = new TwitterTimeLineFlipAdapter(getActivity(), mListTwStatuses);
            ((TwitterTimeLineFlipAdapter) mAdapter).setListener(this);
            break;
        default:
            break;
        }

        if (null != mAdapter) {
            mFlipView.setVisibility(View.GONE);
            mLinearProgressBar.setVisibility(View.VISIBLE);
            mLayoutEmpty.setVisibility(View.GONE);
            mFlipView.setAdapter(mAdapter);
        }
    }

    private int getFragmentCommonType() {
        switch (mFragmentType) {
        case TwitterConstant.TW_DATA_TYPE_TIMELINE:
        case TwitterConstant.TW_DATA_TYPE_TIMELINE_LINKS:
        case TwitterConstant.TW_DATA_TYPE_TWEETS:
        case TwitterConstant.TW_DATA_TYPE_FAVORITES:
        case TwitterConstant.TW_DATA_TYPE_MENTIONING_TWEETS:
            return TwitterConstant.TW_DATA_TYPE_TIMELINE;
        default:
            return mFragmentType;
        }
    }

    @Override
    public void onClick(int type, String id, StoryItemUnit item) {
        switch (type) {
        case TwitterConstant.TW_POST_CREATE_FAVOURITE:
            try {
                if (mService != null) {
                    Log.d(TAG, "Create Twitter favourite, id = " + id);
                    mService.postSNS(GlobalConstant.SOCIAL_TYPE_TWITTER, id, null, TwitterConstant.TW_POST_CREATE_FAVOURITE);
                    DBManager.getInstance(getActivity()).updateLikeCommentInfo(item, item.getNumber_of_like() + 1, item.getNumber_of_comment(), 1);
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
        case TwitterConstant.TW_POST_RETWEET:
            try {
                if (mService != null) {
                    Log.d(TAG, "Retweet Twitter, id = " + id);
                    mService.postSNS(GlobalConstant.SOCIAL_TYPE_TWITTER, id, null, TwitterConstant.TW_POST_RETWEET);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            break;
        case TwitterConstant.TW_POST_REPLY:
            Intent intentConentFeed = new Intent(getActivity(), TwitterSecondPageActivity.class);
            intentConentFeed.putExtra(GlobalConstant.SOCIAL_2ND_VIEW_TYPE, true);
            intentConentFeed.putExtra(GlobalConstant.STORY_ITEM_UNIT, item);
            intentConentFeed.putExtra(GlobalConstant.SOCIAL_TYPE, GlobalConstant.SOCIAL_TYPE_TWITTER);
            startActivity(intentConentFeed);
            break;
        default:
            break;
        }
    }

    @Override
    public void onDestroy() {
        try {
            mService.unregisterUiNotify(GlobalConstant.SOCIAL_TYPE_TWITTER, 1);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    public void setType(int type) {
        mFragmentType = type;
    }

    private void onFlipRefresh() {
        mIsFirstRequest = false;
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put(GlobalConstant.TYPE_DATA_UPDATE, true);
        try {
            if (mService != null) {
                Log.d(TAG, "Request Twitter " + param);
                mService.requestSNS(GlobalConstant.SOCIAL_TYPE_TWITTER, mFragmentType, param);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void onFlipLoadMore() {
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put(GlobalConstant.TYPE_DATA_UPDATE, false);
        try {
            if (mService != null) {
                Log.d(TAG, "Request Twitter " + param);
                mService.requestSNS(GlobalConstant.SOCIAL_TYPE_TWITTER, mFragmentType, param);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<StoryItemUnit> getAdapter() {
        // TODO Auto-generated method stub
        return ((TwitterTimeLineFlipAdapter) mAdapter).getAdapter();
    }

    @Override
    public void onItemClickListener(int position) {
        // TODO Auto-generated method stub
        Intent intentConentFeed = new Intent(getActivity(), TwitterSecondPageActivity.class);
        intentConentFeed.putExtra(GlobalConstant.STORY_ITEM_UNIT, (StoryItemUnit) getAdapter().get(position));
        startActivity(intentConentFeed);
    }
}
