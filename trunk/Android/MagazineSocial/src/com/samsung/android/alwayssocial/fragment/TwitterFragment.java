package com.samsung.android.alwayssocial.fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.samsung.android.alwayssocial.activity.IOnChangeFragment;
import com.samsung.android.alwayssocial.activity.TwitterSecondPageActivity;
import com.samsung.android.alwayssocial.adapter.TwitterSavedSearchAdapter;
import com.samsung.android.alwayssocial.adapter.TwitterSimpleAdaper;
import com.samsung.android.alwayssocial.adapter.TwitterTimeLineAdapter;
import com.samsung.android.alwayssocial.adapter.TwitterTimeLineAdapter.IOnClickCallback;
import com.samsung.android.alwayssocial.adapter.TwitterUserListAdapter;
import com.samsung.android.alwayssocial.app.AlwaysSocialAppImpl;
import com.samsung.android.alwayssocial.constant.GlobalConstant;
import com.samsung.android.alwayssocial.constant.TwitterConstant;
import com.samsung.android.alwayssocial.database.DBManager;
import com.samsung.android.alwayssocial.service.CommentParcelableObject;
import com.samsung.android.alwayssocial.service.IAlwaysService;
import com.samsung.android.alwayssocial.service.IUpdateUiCallback;
import com.samsung.android.alwayssocial.service.StoryItemUnit;
import com.samsung.android.alwayssocial.view.PullAndLoadListView.IOnLoadMoreListener;
import com.samsung.android.alwayssocial.view.PullToRefreshListView.IOnRefreshListener;

public class TwitterFragment extends FragmentBase implements IOnClickCallback {
    protected static final String TAG = "TwitterFragment";

    private BaseAdapter mAdapter;

    private ArrayList<StoryItemUnit> mListTWStatuses = new ArrayList<StoryItemUnit>();
    private ArrayList<StoryItemUnit> mListTwSavedItems = new ArrayList<StoryItemUnit>();
    private ArrayList<StoryItemUnit> mListUserLists = new ArrayList<StoryItemUnit>();
    // Get your List
    private List<StoryItemUnit> mFollowers = new ArrayList<StoryItemUnit>();

    IAlwaysService mService = null;

    private String mActionBarTitle = null;

    private String mUserId = "";
    private String mSearchId = "";
    private String mListId = "";
    private String mListName = "";
    private IOnChangeFragment mIOnChangeFragment;

    private boolean mIsReplyClick;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mIsReplyClick = false;
        mListView.setOnRefreshListener(new IOnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.e(TAG, "onRefresh ");
                String sinceId = "";
                if (getFragmentCommonType() == TwitterConstant.TW_DATA_TYPE_TIMELINE) {
                    if (mListTWStatuses.size() > 0) {
                        sinceId = mListTWStatuses.get(0).getSocialFeedId();
                    }
                }
                refreshListView(sinceId);
            }
        });

        mListView.setOnLoadMoreListener(new IOnLoadMoreListener() {

            @Override
            public void onLoadMore() {
                String maxId = "";
                if (getFragmentCommonType() == TwitterConstant.TW_DATA_TYPE_TIMELINE) {
                    if (mListTWStatuses.size() > 0) {
                        maxId = mListTWStatuses.get(mListTWStatuses.size() - 1).getSocialFeedId();
                    }
                }
                loadMoreListView(maxId);
            }
        });
        /* mFragmentType was assgined value in FragmentBasic by showfFragment () at TwitterMainActivity class
         * mFragmentType will be passed in onStart()
         */
        buildAdapter(mFragmentType);
        mService = AlwaysSocialAppImpl.getInstance().getAlwaysBindService();
        try {
            if (mService != null) {
                mService.registerUiListener(GlobalConstant.SOCIAL_TYPE_TWITTER, 1, mServiceListener);
                mService.requestSNS(GlobalConstant.SOCIAL_TYPE_TWITTER, mFragmentType, null);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mIOnChangeFragment = (IOnChangeFragment) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = initializeView(inflater, container, savedInstanceState);
        return root;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        mIsReplyClick = false;
        try {
            if (mService != null) {
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
            Log.d(TAG, "kieu.thang onUpdateUiByStoryItem" + storyItemList.size() + " feedType " + feedType + " isResponseByRefresh " + isResponseByRefresh
                    );
            updateUI(socialType, feedType, storyItemList, isResponseByRefresh);
        }

        @Override
        public void onUpdateErrorUi(String socialType, int errorCode, boolean isResponseByRefresh) throws RemoteException {
            Log.d(TAG, "Error when Update, error code = " + errorCode);
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
            case TwitterConstant.TW_DATA_VIEW_USER_TIMELINE:
            case TwitterConstant.TW_DATA_VIEW_SEARCH_ITEM:
                Toast.makeText(getActivity(), "Error occured! Please go back and try again ", Toast.LENGTH_SHORT).show();
            default:
                break;
            }

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
            if (GlobalConstant.SOCIAL_TYPE_TWITTER.compareTo(socialType) != 0) {
                return;
            }
            if ((feedType == TwitterConstant.TW_DATA_VIEW_SEARCH_ITEM) && (mFragmentType == TwitterConstant.TW_DATA_TYPE_SAVED_SEARCHES || mFragmentType == TwitterConstant.TW_DATA_VIEW_SEARCH_ITEM)) {
                updateNotMeTimeline(feedType, (ArrayList<StoryItemUnit>) items, isResponseByRefresh, isFirstTimeUpdate);
            } else if (feedType == TwitterConstant.TW_DATA_VIEW_USER_TIMELINE && (mFragmentType == TwitterConstant.TW_DATA_TYPE_PEOPLE_FOLLOWED || mFragmentType == TwitterConstant.TW_DATA_TYPE_PEOPLE_FOLLOWING_ME || mFragmentType == TwitterConstant.TW_DATA_VIEW_USER_TIMELINE)) {
                updateNotMeTimeline(feedType, (ArrayList<StoryItemUnit>) items, isResponseByRefresh, isFirstTimeUpdate);
            } else if (feedType == TwitterConstant.TW_DATA_TYPE_LIST_TIMELINE && (mFragmentType == TwitterConstant.TW_DATA_TYPE_LIST || mFragmentType == TwitterConstant.TW_DATA_TYPE_LIST_FOLLOWED
                    || mFragmentType == TwitterConstant.TW_DATA_TYPE_LIST_FOLLOWING_ME)) {
                Log.d(TAG, "responseTwitterData 3  mRequestType " + feedType);
                updateNotMeTimeline(feedType, (ArrayList<StoryItemUnit>) items, isResponseByRefresh, isFirstTimeUpdate);
            }
        }
    };

    private void updateUI(String socialType, int feedType, ArrayList<StoryItemUnit> storyItemList, boolean isResponseByRefresh) {
        Log.d(TAG, "updateUI() SocialType = " + socialType + " feedType = " + feedType + "mFragmentType =" + mFragmentType + " storyItemList" + storyItemList.size());
        if ((GlobalConstant.SOCIAL_TYPE_TWITTER.compareTo(socialType) != 0 || storyItemList == null) && feedType != TwitterConstant.TW_POST_RETWEET) {
            return;
        }
        if (isResponseByRefresh) {
            mListView.onRefreshComplete();
        } else {
            mListView.onLoadMoreComplete();
        }
        if (feedType == TwitterConstant.TW_POST_RETWEET) {
            Toast.makeText(getActivity(), "Retweet successfully!!!", Toast.LENGTH_SHORT).show();
            return;
        }
        int fragmentType = getFragmentCommonType();
        switch (fragmentType) {
        case TwitterConstant.TW_DATA_TYPE_TIMELINE:
            boolean type_update = true;
            if (mFragmentType == TwitterConstant.TW_DATA_VIEW_SEARCH_ITEM) {
                type_update = false;
                ((TwitterTimeLineAdapter) mAdapter).updateTimelineList(storyItemList, type_update, true);
            } else {
                ArrayList<StoryItemUnit> sortItemList = sortStoryItemList(mListTWStatuses, storyItemList);
                if (!checkTypeUpdate(sortItemList, mListTWStatuses)) {
                    type_update = false;
                }
                ((TwitterTimeLineAdapter) mAdapter).updateTimelineList(sortItemList, type_update, false);
            }

            ((TwitterTimeLineAdapter) mAdapter).setListener(this);
            mListView.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intentConentFeed = new Intent(getActivity(), TwitterSecondPageActivity.class);
                    intentConentFeed.putExtra(GlobalConstant.STORY_ITEM_UNIT, (StoryItemUnit) parent.getAdapter().getItem(position));
                    startActivity(intentConentFeed);
                }
            });
            break;
        case TwitterConstant.TW_DATA_TYPE_SAVED_SEARCHES:
            ((TwitterSavedSearchAdapter) mAdapter).updateSavedList(storyItemList);
            mListView.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        if (mService != null) {
                            HashMap<String, Object> viewSearchParams = new HashMap<String, Object>();
                            mSearchId = ((StoryItemUnit) parent.getAdapter().getItem(position)).getTitle();
                            viewSearchParams.put(TwitterConstant.TW_KEY_VIEW_SEARCH_ITEM, mSearchId);
                            //if this is the first time, we will assign the maxId "";
                            String maxId = "";
                            viewSearchParams.put(TwitterConstant.TW_KEY_SINCE_MAX_ID, maxId);
                            mService.requestSNS(GlobalConstant.SOCIAL_TYPE_TWITTER, TwitterConstant.TW_DATA_VIEW_SEARCH_ITEM, viewSearchParams);
                            mActionBarTitle = ((StoryItemUnit) parent.getAdapter().getItem(position)).getTitle();
                            mListView.setVisibility(View.GONE);
                            mLinearProgressBar.setVisibility(View.VISIBLE);
                        }
                    } catch (RemoteException e) {
                        Log.d(TAG, "error when view search item");
                        e.printStackTrace();
                    }
                }
            });
            break;
        case TwitterConstant.TW_DATA_TYPE_LIST:
        case TwitterConstant.TW_DATA_TYPE_LIST_FOLLOWED:
        case TwitterConstant.TW_DATA_TYPE_LIST_FOLLOWING_ME:
            ((TwitterUserListAdapter) mAdapter).updateUserList(storyItemList);
            mListView.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    try {
                        if (mService != null) {
                            HashMap<String, Object> listParams = new HashMap<String, Object>();
                            mListId = ((StoryItemUnit) parent.getAdapter().getItem(position)).getSocialFeedId();
                            mListName = ((StoryItemUnit) parent.getAdapter().getItem(position)).getTitle();
                            Log.d(TAG, "Click Item of listview,  mListId = " + mListId + " " + mListName);
                            listParams.put(TwitterConstant.TW_KEY_LIST_ID, mListId);
                            listParams.put(TwitterConstant.TW_KEY_LIST_NAME, mListName);
                            mService.requestSNS(GlobalConstant.SOCIAL_TYPE_TWITTER, TwitterConstant.TW_DATA_TYPE_LIST_TIMELINE, listParams);
                            mActionBarTitle = ((StoryItemUnit) parent.getAdapter().getItem(position)).getTitle();
                            mListView.setVisibility(View.GONE);
                            mLinearProgressBar.setVisibility(View.VISIBLE);
                        }
                    } catch (RemoteException e) {
                        Log.d(TAG, "error when item of list");
                        e.printStackTrace();
                    }
                }
            });
            break;
        case TwitterConstant.TW_DATA_TYPE_PEOPLE_FOLLOWING_ME:
        case TwitterConstant.TW_DATA_TYPE_PEOPLE_FOLLOWED:
            ((TwitterSimpleAdaper) mAdapter).updateUserList(storyItemList);
            mListView.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        if (mService != null) {
                            HashMap<String, Object> viewUserTimelineParams = new HashMap<String, Object>();
                            mUserId = ((StoryItemUnit) parent.getAdapter().getItem(position)).getSocialFeedId();
                            viewUserTimelineParams.put(TwitterConstant.TW_KEY_STATUS_USER_ID, mUserId);
                            viewUserTimelineParams.put(TwitterConstant.TW_KEY_STATUS_USER_NAME, ((StoryItemUnit) parent.getAdapter().getItem(position)).getTitle());
                            mService.requestSNS(GlobalConstant.SOCIAL_TYPE_TWITTER, TwitterConstant.TW_DATA_VIEW_USER_TIMELINE, viewUserTimelineParams);
                        }
                        mActionBarTitle = ((StoryItemUnit) parent.getAdapter().getItem(position)).getTitle();
                        mListView.setVisibility(View.GONE);
                        mLinearProgressBar.setVisibility(View.VISIBLE);
                    } catch (RemoteException e) {
                        Log.d(TAG, "error when view user timeline, " + e.toString());
                        e.printStackTrace();
                    }
                }

            });
            break;
        default:
            break;
        }

        mListView.setVisibility(View.VISIBLE);
        if (storyItemList == null || storyItemList.size() == 0) {
            mListView.onRefresh();
            mListView.setVisibility(View.GONE);
            return;
        }
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
        case TwitterConstant.TW_DATA_VIEW_SEARCH_ITEM:
        case TwitterConstant.TW_DATA_VIEW_USER_TIMELINE:
        case TwitterConstant.TW_DATA_TYPE_LIST_TIMELINE: // set adapter for request  timeline of list 
            Log.d(TAG, "build adapter TIMELINE");
            mAdapter = new TwitterTimeLineAdapter(getActivity(), mListTWStatuses);
            ((TwitterTimeLineAdapter) mAdapter).setListener(this);
            break;
        case TwitterConstant.TW_DATA_TYPE_SAVED_SEARCHES:
            Log.d(TAG, "build adapter savedSearches");
            mAdapter = new TwitterSavedSearchAdapter(getActivity(), mListTwSavedItems);
            break;
        case TwitterConstant.TW_DATA_TYPE_PEOPLE_FOLLOWED:
            Log.d(TAG, "buildAdapter() TW_DATA_TYPE_PEOPLE_FOLLOWED ");
            mAdapter = new TwitterSimpleAdaper(getActivity(), mFollowers);
            break;
        case TwitterConstant.TW_DATA_TYPE_PEOPLE_FOLLOWING_ME:
            Log.d(TAG, "buildAdapter() TW_DATA_TYPE_PEOPLE_FOLLOWING_ME ");
            mAdapter = new TwitterSimpleAdaper(getActivity(), mFollowers);
            break;
        case TwitterConstant.TW_DATA_TYPE_LIST_FOLLOWING_ME:
            Log.d(TAG, "build adapter TW_DATA_TYPE_LIST_FOLLOWING_ME");
            mAdapter = new TwitterUserListAdapter(getActivity(), mListUserLists);
        case TwitterConstant.TW_DATA_TYPE_LIST:
        case TwitterConstant.TW_DATA_TYPE_LIST_FOLLOWED:
            Log.d(TAG, "build adapter TW_DATA_TYPE_LIST");
            mAdapter = new TwitterUserListAdapter(getActivity(), mListUserLists);
            break;
        default:
            break;
        }

        if (null != mAdapter) {
            mListView.setVisibility(View.GONE);
            mLinearProgressBar.setVisibility(View.VISIBLE);
            mLayoutEmpty.setVisibility(View.GONE);
            mListView.setAdapter(mAdapter);
        }
    }

    private int getFragmentCommonType() {
        switch (mFragmentType) {
        case TwitterConstant.TW_DATA_TYPE_TIMELINE:
        case TwitterConstant.TW_DATA_TYPE_TIMELINE_LINKS:
        case TwitterConstant.TW_DATA_TYPE_TWEETS:
        case TwitterConstant.TW_DATA_TYPE_FAVORITES:
        case TwitterConstant.TW_DATA_TYPE_MENTIONING_TWEETS:
        case TwitterConstant.TW_DATA_VIEW_SEARCH_ITEM:
        case TwitterConstant.TW_DATA_VIEW_USER_TIMELINE:
        case TwitterConstant.TW_DATA_TYPE_LIST_TIMELINE:
            return TwitterConstant.TW_DATA_TYPE_TIMELINE;

        case TwitterConstant.TW_DATA_TYPE_SAVED_SEARCHES:
            return TwitterConstant.TW_DATA_TYPE_SAVED_SEARCHES;

        case TwitterConstant.TW_DATA_TYPE_PEOPLE_FOLLOWED:
        case TwitterConstant.TW_DATA_TYPE_PEOPLE_FOLLOWING_ME:
            return TwitterConstant.TW_DATA_TYPE_PEOPLE_FOLLOWED;

        case TwitterConstant.TW_DATA_TYPE_LIST_FOLLOWING_ME:
            return TwitterConstant.TW_DATA_TYPE_LIST_FOLLOWING_ME;
        case TwitterConstant.TW_DATA_TYPE_LIST:
        case TwitterConstant.TW_DATA_TYPE_LIST_FOLLOWED:
            return TwitterConstant.TW_DATA_TYPE_LIST_FOLLOWED;
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
            if (!mIsReplyClick) {
                mIsReplyClick = true;
                Intent intentConentFeed = new Intent(getActivity(), TwitterSecondPageActivity.class);
                intentConentFeed.putExtra(GlobalConstant.SOCIAL_2ND_VIEW_TYPE, true);
                intentConentFeed.putExtra(GlobalConstant.STORY_ITEM_UNIT, item);
                startActivity(intentConentFeed);
            }
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

    private void refreshListView(String sinceId) {
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put(GlobalConstant.TYPE_DATA_UPDATE, true);
        param.put(TwitterConstant.TW_KEY_VIEW_SEARCH_ITEM, mSearchId);
        if (mFragmentType == TwitterConstant.TW_DATA_VIEW_USER_TIMELINE) {
            param.put(TwitterConstant.TW_KEY_STATUS_USER_ID, mUserId);
        }
        param.put(TwitterConstant.TW_KEY_SINCE_MAX_ID, sinceId);
        try {
            if (mService != null) {
                Log.d(TAG, "Request Twitter " + param);
                mService.requestSNS(GlobalConstant.SOCIAL_TYPE_TWITTER, mFragmentType, param);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void loadMoreListView(String maxId) {
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put(GlobalConstant.TYPE_DATA_UPDATE, false);
        param.put(TwitterConstant.TW_KEY_SINCE_MAX_ID, maxId);
        if (mFragmentType == TwitterConstant.TW_DATA_VIEW_USER_TIMELINE) {
            param.put(TwitterConstant.TW_KEY_STATUS_USER_ID, mUserId);
        } else if (mFragmentType == TwitterConstant.TW_DATA_VIEW_SEARCH_ITEM) {
            param.put(TwitterConstant.TW_KEY_VIEW_SEARCH_ITEM, mSearchId);
        }
        try {
            if (mService != null) {
                Log.d(TAG, "Request Twitter " + param);
                mService.requestSNS(GlobalConstant.SOCIAL_TYPE_TWITTER, mFragmentType, param);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<StoryItemUnit> sortStoryItemList(ArrayList<StoryItemUnit> sourceList, ArrayList<StoryItemUnit> destList) {
        ArrayList<StoryItemUnit> storyItemUnits = new ArrayList<StoryItemUnit>();
        for (StoryItemUnit storyItemUnit : destList) {
            if (!isContainStoryItem(sourceList, storyItemUnit)) {
                storyItemUnits.add(storyItemUnit);
            }
        }
        return storyItemUnits;
    }

    private boolean isContainStoryItem(ArrayList<StoryItemUnit> storyItemList, StoryItemUnit storyItemUnit) {
        for (StoryItemUnit _storyItemUnit : storyItemList) {
            if (_storyItemUnit.getSocialFeedId().equals(storyItemUnit.getSocialFeedId()))
                return true;
        }
        return false;
    }

    private boolean checkTypeUpdate(ArrayList<StoryItemUnit> sortedStoryList, ArrayList<StoryItemUnit> mFeedList2) {
        if (sortedStoryList.size() > 0 && mFeedList2.size() > 0) {
            long sortTime = 0;
            long feedTime = 0;
            String timeOfSortList = sortedStoryList.get(0).getTime_stamp();
            String timeOfFeedList = mFeedList2.get(0).getTime_stamp();
            SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssZ", Locale.US);

            Date sortDate = null;
            Date feedDate = null;
            try {
                sortDate = formatDate.parse(timeOfSortList);
                feedDate = formatDate.parse(timeOfFeedList);
                sortTime = sortDate.getTime();
                feedTime = feedDate.getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (sortTime >= feedTime)
                return true;
            else
                return false;
        }
        return false;
    }

    public void updateNotMeTimeline(int type, ArrayList<StoryItemUnit> storyItemList, boolean isResponseByRefresh, boolean isFirstTimeUpdate) {
        Log.d(TAG, "updateNotMeTimeLine type = " + type + "  isFirstTimeUpdate = " + isFirstTimeUpdate);
        mFragmentType = type;
        if (isFirstTimeUpdate) {
            buildAdapter(mFragmentType);
        }
        if (isResponseByRefresh) {
            mListView.onRefreshComplete();
        } else {
            mListView.onLoadMoreComplete();
        }
        updateUI(GlobalConstant.SOCIAL_TYPE_TWITTER, type, storyItemList, isResponseByRefresh);
        if (mActionBarTitle != null) {
            mIOnChangeFragment.onRequestChangeTitle(mActionBarTitle);
        }

    }
}
