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
import com.samsung.android.alwayssocial.activity.FacebookSecondPageActivity;
import com.samsung.android.alwayssocial.activity.ILikeCommentClick;
import com.samsung.android.alwayssocial.activity.IOnChangeFragment;
import com.samsung.android.alwayssocial.adapter.FacebookMemberAdapter;
import com.samsung.android.alwayssocial.adapter.FacebookMyTaggedPhotoAdapter;
import com.samsung.android.alwayssocial.adapter.FacebookFeedAdapter;
import com.samsung.android.alwayssocial.adapter.FacebookFriendAdapter;
import com.samsung.android.alwayssocial.adapter.FacebookLikedPageAdapter;
import com.samsung.android.alwayssocial.app.AlwaysSocialAppImpl;
import com.samsung.android.alwayssocial.constant.FacebookConstant;
import com.samsung.android.alwayssocial.constant.GlobalConstant;
import com.samsung.android.alwayssocial.database.DBManager;
import com.samsung.android.alwayssocial.service.CommentParcelableObject;
import com.samsung.android.alwayssocial.service.IAlwaysService;
import com.samsung.android.alwayssocial.service.IUpdateUiCallback;
import com.samsung.android.alwayssocial.service.StoryItemUnit;
import com.samsung.android.alwayssocial.view.PullAndLoadListView.IOnLoadMoreListener;
import com.samsung.android.alwayssocial.view.PullToRefreshListView.IOnRefreshListener;

public class FacebookFragment extends FragmentBase implements ILikeCommentClick {

    protected static final String TAG = "FragmentFacebook";
    private BaseAdapter mAdapter;
    private ArrayList<StoryItemUnit> mFriendList = new ArrayList<StoryItemUnit>();
    private ArrayList<StoryItemUnit> mLikedPagesList = new ArrayList<StoryItemUnit>();
    private ArrayList<StoryItemUnit> mPhotoList = new ArrayList<StoryItemUnit>();
    private ArrayList<StoryItemUnit> mFeedList = new ArrayList<StoryItemUnit>();
    // response data from SNS
    private String mNextPage;
    private IAlwaysService mService = null;
    private IOnChangeFragment mRequestChangeFragment;
    private String mActionBarTitle = null;

    private boolean mIsCommentClick; //prevent to start more than 2 2ndpageactivity

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mRequestChangeFragment = (IOnChangeFragment) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = initializeView(inflater, container, savedInstanceState);

        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mIsCommentClick = false;
        mListView.setOnRefreshListener(new IOnRefreshListener() {

            @Override
            public void onRefresh() {
                Log.e(TAG, "onRefresh ");
                refreshListView();
            }

        });

        /* load more from database */
        mListView.setOnLoadMoreListener(new IOnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                Log.d(TAG, "setOnLoadMoreListener");
                loadMoreListView();
            }
        });
        buildAdapter(mFragmentType);
        mService = AlwaysSocialAppImpl.getInstance().getAlwaysBindService();
        try {
            mService.registerUiListener(GlobalConstant.SOCIAL_TYPE_FACEBOOK, 1, mServiceListener);
            mService.requestSNS(GlobalConstant.SOCIAL_TYPE_FACEBOOK, mFragmentType, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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

    private void buildAdapter(int fragmentTYpe) {
        Log.d(TAG, "build adapter");
        // Reset adapter
        mAdapter = null;
        switch (fragmentTYpe) {
        case FacebookConstant.FB_DATA_TYPE_FEED:
        case FacebookConstant.FB_DATA_TYPE_FEED_PHOTOS:
        case FacebookConstant.FB_DATA_TYPE_FEED_LINKS:
        case FacebookConstant.FB_DATA_TYPE_TIMELINE:
        case FacebookConstant.FB_DATA_TYPE_FRIEND_TIMELINE:
        case FacebookConstant.FB_DATA_TYPE_PAGE_TIMELINE:
            Log.d(TAG, "build FB_DATA_TYPE_TIMELINE");
            mAdapter = new FacebookFeedAdapter(getActivity(), mFeedList);
            ((FacebookFeedAdapter) mAdapter).setListener(this);
            break;
        case FacebookConstant.FB_DATA_TYPE_TAGGEDME:
            Log.d(TAG, "build FB_DATA_TYPE_TAGGEDME");
            mAdapter = new FacebookMyTaggedPhotoAdapter(getActivity(), mPhotoList);
            ((FacebookMyTaggedPhotoAdapter) mAdapter).setListener(this);
            break;

        case FacebookConstant.FB_DATA_TYPE_GROUPS:
        case FacebookConstant.FB_DATA_TYPE_PAGES:
        case FacebookConstant.FB_DATA_TYPE_FRIEND_GROUPS:
            Log.d(TAG, "build FB_DATA_TYPE_FRIEND_GROUPS");
            mAdapter = new FacebookLikedPageAdapter(getActivity(), mLikedPagesList);
            break;
        case FacebookConstant.FB_DATA_TYPE_FRIENDS:
            Log.d(TAG, "build FB_DATA_TYPE_FRIENDS");
            mAdapter = new FacebookFriendAdapter(getActivity(), mFriendList);
            break;
        case FacebookConstant.FB_DATA_MEMBERS_OF_GROUP:
            Log.d(TAG, "build FB_DATA_TYPE_FRIENDS");
            mAdapter = new FacebookMemberAdapter(getActivity(), mFriendList);
            break;

        case FacebookConstant.FB_DATA_TYPE_FRIEND_ALBUMS:
            break;
        // Not implement
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

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private int getFragmentCommonType() {
        switch (mFragmentType) {
        case FacebookConstant.FB_DATA_TYPE_FEED:
        case FacebookConstant.FB_DATA_TYPE_FEED_PHOTOS:
        case FacebookConstant.FB_DATA_TYPE_FEED_LINKS:
        case FacebookConstant.FB_DATA_TYPE_TIMELINE:
        case FacebookConstant.FB_DATA_TYPE_FRIEND_TIMELINE:
        case FacebookConstant.FB_DATA_TYPE_PAGE_TIMELINE:
            return FacebookConstant.FB_DATA_TYPE_FEED;
        case FacebookConstant.FB_DATA_TYPE_GROUPS:
        case FacebookConstant.FB_DATA_TYPE_PAGES:
        case FacebookConstant.FB_DATA_TYPE_FRIEND_GROUPS:
            return FacebookConstant.FB_DATA_TYPE_PAGES;
        case FacebookConstant.FB_DATA_TYPE_TAGGEDME:
            return FacebookConstant.FB_DATA_TYPE_TAGGEDME;
        case FacebookConstant.FB_DATA_MEMBERS_OF_GROUP:
            return FacebookConstant.FB_DATA_MEMBERS_OF_GROUP;
        case FacebookConstant.FB_DATA_TYPE_FRIENDS:
            return FacebookConstant.FB_DATA_TYPE_FRIENDS;
        default:
            break;
        }
        return mFragmentType;
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume");
        mIsCommentClick = false;
        // Set like, comment count
        if (getFragmentCommonType() == FacebookConstant.FB_DATA_TYPE_FEED && mFeedList != null && mFeedList.size() > 0)  {
            String ItemId = AlwaysSocialAppImpl.getInstance().getAlwaysSharePrefs().getStringValue(FacebookConstant.FB_PREF_KEY_LIKECOMMEND + "_id");
            Log.d(TAG, "ItemId " + ItemId);
            if (ItemId != "") {
                int likeCount = AlwaysSocialAppImpl.getInstance().getAlwaysSharePrefs().getIntValue(FacebookConstant.FB_PREF_KEY_LIKECOMMEND + "_likes");
                int commentCount = AlwaysSocialAppImpl.getInstance().getAlwaysSharePrefs().getIntValue(FacebookConstant.FB_PREF_KEY_LIKECOMMEND + "_comments");
                int isLiked = AlwaysSocialAppImpl.getInstance().getAlwaysSharePrefs().getIntValue(FacebookConstant.FB_PREF_KEY_IS_LIKED);
                for (StoryItemUnit unit : mFeedList) {
                    if (unit.getSocialFeedId().compareTo(ItemId) == 0) {
                        unit.setNumber_of_like(likeCount);
                        unit.setNumber_of_comment(commentCount);
                        unit.setIsLiked(isLiked);
                        /**update like,comment,is liked to db*/
                        DBManager.getInstance(getActivity()).updateLikeCommentInfo(unit, likeCount, commentCount, isLiked);
                        AlwaysSocialAppImpl.getInstance().getAlwaysSharePrefs().remove(FacebookConstant.FB_PREF_KEY_LIKECOMMEND + "_id");
                        AlwaysSocialAppImpl.getInstance().getAlwaysSharePrefs().remove(FacebookConstant.FB_PREF_KEY_LIKECOMMEND + "_comments");
                        AlwaysSocialAppImpl.getInstance().getAlwaysSharePrefs().remove(FacebookConstant.FB_PREF_KEY_LIKECOMMEND + "_likes");
                        break;
                    }
                }
            }
            mAdapter.notifyDataSetChanged();
        }
        
        if(getFragmentCommonType() == FacebookConstant.FB_DATA_TYPE_TAGGEDME && mPhotoList != null && mPhotoList.size() > 0) {
            String ItemId = AlwaysSocialAppImpl.getInstance().getAlwaysSharePrefs().getStringValue(FacebookConstant.FB_PREF_KEY_LIKECOMMEND + "_id");
            Log.d(TAG, "ItemId " + ItemId);
            if (ItemId != "") {
                int likeCount = AlwaysSocialAppImpl.getInstance().getAlwaysSharePrefs().getIntValue(FacebookConstant.FB_PREF_KEY_LIKECOMMEND + "_likes");
                int commentCount = AlwaysSocialAppImpl.getInstance().getAlwaysSharePrefs().getIntValue(FacebookConstant.FB_PREF_KEY_LIKECOMMEND + "_comments");
                int isLiked = AlwaysSocialAppImpl.getInstance().getAlwaysSharePrefs().getIntValue(FacebookConstant.FB_PREF_KEY_IS_LIKED);
                for (StoryItemUnit unit : mPhotoList) {
                    if (unit.getSocialFeedId().compareTo(ItemId) == 0) {
                        unit.setNumber_of_like(likeCount);
                        unit.setNumber_of_comment(commentCount);
                        unit.setIsLiked(isLiked);
                        /**update like,comment,is liked to db*/
                        DBManager.getInstance(getActivity()).updateLikeCommentInfo(unit, likeCount, commentCount, isLiked);
                        AlwaysSocialAppImpl.getInstance().getAlwaysSharePrefs().remove(FacebookConstant.FB_PREF_KEY_LIKECOMMEND + "_id");
                        AlwaysSocialAppImpl.getInstance().getAlwaysSharePrefs().remove(FacebookConstant.FB_PREF_KEY_LIKECOMMEND + "_comments");
                        AlwaysSocialAppImpl.getInstance().getAlwaysSharePrefs().remove(FacebookConstant.FB_PREF_KEY_LIKECOMMEND + "_likes");
                        break;
                    }
                }
            }
            mAdapter.notifyDataSetChanged();
        }
        
        // Register listener from service
        try {
            mService.registerUiListener(GlobalConstant.SOCIAL_TYPE_FACEBOOK, 1, mServiceListener);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        super.onResume();
    }

    @Override
    public void onLikeCommentClick(String type, StoryItemUnit item, boolean like) {
        if (type == FacebookConstant.FB_LIKE_BTN_CLICK && like == true) {
            try {
                mService.postSNS(GlobalConstant.SOCIAL_TYPE_FACEBOOK, item.getSocialFeedId(), null, FacebookConstant.FB_POST_TYPE_LIKE);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else if (type == FacebookConstant.FB_COMMENT_BTN_CLICK) {
            if (!mIsCommentClick) {
                mIsCommentClick = true;
                Intent intentConentFeed = new Intent(getActivity(), FacebookSecondPageActivity.class);
                intentConentFeed.putExtra(FacebookConstant.FB_FRAGMENT_TYPE, getFragmentCommonType());
                intentConentFeed.putExtra(GlobalConstant.SOCIAL_2ND_VIEW_TYPE, true);
                intentConentFeed.putExtra(GlobalConstant.STORY_ITEM_UNIT, item);
                startActivity(intentConentFeed);
            }
        }
    }

    private IUpdateUiCallback mServiceListener = new IUpdateUiCallback.Stub() {

        @Override
        public void onUpdateUiByStoryItem(String socialType, int feedType, boolean isResponseByRefresh) throws RemoteException {
            ArrayList<StoryItemUnit> storyItemList = new ArrayList<StoryItemUnit>();
            boolean isSort = false;
            isSort = (feedType == FacebookConstant.FB_DATA_TYPE_FEED) || (feedType == FacebookConstant.FB_DATA_TYPE_FEED_PHOTOS)
                    || (feedType == FacebookConstant.FB_DATA_TYPE_FEED_LINKS) || (feedType == FacebookConstant.FB_DATA_TYPE_TIMELINE)
                    || (feedType == FacebookConstant.FB_DATA_TYPE_TAGGEDME);
            storyItemList = DBManager.getInstance(getActivity()).updateDataToUI(socialType, feedType, isSort);
            updateUI(socialType, feedType, storyItemList, isResponseByRefresh);
        }

        @Override
        public void onUpdateErrorUi(String socialType, int errorCode, boolean isResponseByRefresh) throws RemoteException {
            if (GlobalConstant.SOCIAL_TYPE_FACEBOOK.compareTo(socialType) == 0) {
                Toast.makeText(getActivity(), "Dont have permission Or network error? Error code = " + errorCode, Toast.LENGTH_LONG).show();
            }
            mLinearProgressBar.setVisibility(View.GONE);
            if (isResponseByRefresh) {
                mListView.onRefreshComplete();
            } else {
                mListView.onLoadMoreComplete();
            }
            switch (mFragmentType) {
            case FacebookConstant.FB_DATA_TYPE_FEED:
            case FacebookConstant.FB_DATA_TYPE_FEED_PHOTOS:
            case FacebookConstant.FB_DATA_TYPE_FEED_LINKS:
            case FacebookConstant.FB_DATA_TYPE_TIMELINE:
                if (mFeedList.size() == 0) {
                    mLayoutEmpty.setVisibility(View.VISIBLE);
                }
                break;
            case FacebookConstant.FB_DATA_TYPE_GROUPS:
            case FacebookConstant.FB_DATA_TYPE_PAGES:
            case FacebookConstant.FB_DATA_TYPE_FRIEND_GROUPS:
                if (mLikedPagesList.size() == 0) {
                    mLayoutEmpty.setVisibility(View.VISIBLE);
                }
                break;
            case FacebookConstant.FB_DATA_TYPE_TAGGEDME:
                if (mPhotoList.size() == 0) {
                    mLayoutEmpty.setVisibility(View.VISIBLE);
                }
                break;
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
            Log.d(TAG, "receiveStoryItems feed type = " + feedType + ", mFragmentType = " + mFragmentType + " items Size = " + items.size());
            // build adapter - change fragment type
            if (feedType == FacebookConstant.FB_DATA_TYPE_TIMELINE && (mFragmentType == FacebookConstant.FB_DATA_TYPE_FRIENDS || mFragmentType == FacebookConstant.FB_DATA_MEMBERS_OF_GROUP)) {
                // check the case of currently is in Friends and response is in timeline 
                updateNotMeTimeline(FacebookConstant.FB_DATA_TYPE_TIMELINE, (ArrayList<StoryItemUnit>) items, isResponseByRefresh);
            } else if (feedType == FacebookConstant.FB_DATA_TYPE_PAGE_TIMELINE
                    && (mFragmentType == FacebookConstant.FB_DATA_TYPE_PAGES || mFragmentType == FacebookConstant.FB_DATA_TYPE_GROUPS)) {
                updateNotMeTimeline(FacebookConstant.FB_DATA_TYPE_PAGE_TIMELINE, (ArrayList<StoryItemUnit>) items, isResponseByRefresh);
            } else if (feedType == FacebookConstant.FB_DATA_MEMBERS_OF_GROUP && mFragmentType == FacebookConstant.FB_DATA_TYPE_FRIEND_GROUPS) {
                Log.d(TAG, "size " + items.size() + " mFragmentType " + mFragmentType + " feedType " + feedType);
                updateNotMeFriends(FacebookConstant.FB_DATA_MEMBERS_OF_GROUP, (ArrayList<StoryItemUnit>) items, isResponseByRefresh);
            }
        }
    };

    public void updateNotMeFriends(int type, ArrayList<StoryItemUnit> storyItemList, boolean isResponseByRefresh) {
        mFragmentType = type;
        buildAdapter(mFragmentType);
        if (isResponseByRefresh) {
            mListView.onRefreshComplete();
        } else {
            mListView.onLoadMoreComplete();
        }
        updateFriendsFragment(type, storyItemList);
        if (mActionBarTitle != null) {
            mRequestChangeFragment.onRequestChangeTitle(mActionBarTitle);
        }
    }

    public void updateNotMeTimeline(int type, ArrayList<StoryItemUnit> storyItemList, boolean isResponseByRefresh) {
        mFragmentType = type;
        buildAdapter(mFragmentType);
        if (isResponseByRefresh) {
            mListView.onRefreshComplete();
        } else {
            mListView.onLoadMoreComplete();
        }
        updateFBFeedsFragment(mFragmentType, storyItemList);
        if (mActionBarTitle != null) {
            mRequestChangeFragment.onRequestChangeTitle(mActionBarTitle);
        }

    }

    public void updateUI(String socialType, int feedType, ArrayList<StoryItemUnit> storyItemList, boolean isResponseByRefresh) {
        Log.d(TAG, "updateUI() SocialType = " + socialType + " feedType = " + feedType + "mFragmentType =" + mFragmentType);
        //      mLinearProgressBar.setVisibility(View.GONE);
        if (isResponseByRefresh) {
            mListView.onRefreshComplete();
        } else {
            mListView.onLoadMoreComplete();
        }

        if (GlobalConstant.SOCIAL_TYPE_FACEBOOK.compareTo(socialType) != 0 || storyItemList == null) {
            return;
        }
        int fragmentType = getFragmentCommonType();
        switch (fragmentType) {
        case FacebookConstant.FB_DATA_TYPE_FEED:
        case FacebookConstant.FB_DATA_TYPE_FEED_PHOTOS:
        case FacebookConstant.FB_DATA_TYPE_FEED_LINKS:
        case FacebookConstant.FB_DATA_TYPE_TIMELINE:
            /** feed, feed_photo, feed_link, timeline, page_timeline*/
            updateFBFeedsFragment(feedType, storyItemList);
            break;
        case FacebookConstant.FB_DATA_TYPE_GROUPS:
        case FacebookConstant.FB_DATA_TYPE_PAGES:
        case FacebookConstant.FB_DATA_TYPE_FRIEND_GROUPS:
            updatePagesFragment(feedType, storyItemList);
            break;
        case FacebookConstant.FB_DATA_TYPE_TAGGEDME:
            updateTaggedMeFragment(feedType, storyItemList);
            break;
        case FacebookConstant.FB_DATA_TYPE_FRIENDS:
            updateFriendsFragment(feedType, storyItemList);
            break;
        default:
            Log.d(TAG, "Not handle Fragment Type = " + mFragmentType);
            break;
        }
    }

    private void updateTaggedMeFragment(int feedType, ArrayList<StoryItemUnit> storyItemList) {
        // check empty data
        mPhotoList = storyItemList;
        if (mPhotoList == null || mPhotoList.size() == 0) {
            //            mLayoutEmpty.setVisibility(View.VISIBLE);
            mListView.onRefresh();
            return;
        }
        ((FacebookMyTaggedPhotoAdapter) mAdapter).updateFolderList(mPhotoList);
        ((FacebookMyTaggedPhotoAdapter) mAdapter).setListener(this);
        mListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intentConentFeed = new Intent(getActivity(), FacebookSecondPageActivity.class);
                intentConentFeed.putExtra(GlobalConstant.STORY_ITEM_UNIT, (StoryItemUnit) parent.getAdapter().getItem(position));
                intentConentFeed.putExtra(GlobalConstant.SOCIAL_TYPE, GlobalConstant.SOCIAL_TYPE_FACEBOOK);
                startActivity(intentConentFeed);
            }
        });

        mListView.setVisibility(View.VISIBLE);
    }

    private void updatePagesFragment(int feedType, ArrayList<StoryItemUnit> storyItemList) {
        // check empty data
        if (storyItemList == null || storyItemList.size() == 0) {
            //            mLayoutEmpty.setVisibility(View.VISIBLE);
            mListView.onRefresh();
            return;
        }
        // convert to implement in onItemClick
        final int type = feedType;
        ((FacebookLikedPageAdapter) mAdapter).updateList(storyItemList);
        mLikedPagesList = storyItemList;
        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemClick, page, position = " + position);
                try {
                    if (mService != null) {
                        HashMap<String, Object> getTimelineParams = new HashMap<String, Object>();
                        getTimelineParams.put("page_id", ((StoryItemUnit) parent.getAdapter().getItem(position)).getSocialFeedId());
                        Log.d(TAG, "onPageClick = " + ((StoryItemUnit) parent.getAdapter().getItem(position)).getSocialFeedId());
                        mActionBarTitle = ((StoryItemUnit) parent.getAdapter().getItem(position)).getTitle();
                        // implemnt get members of group
                        if (type == FacebookConstant.FB_DATA_TYPE_FRIEND_GROUPS) {
                            mService.requestSNS(GlobalConstant.SOCIAL_TYPE_FACEBOOK, FacebookConstant.FB_DATA_MEMBERS_OF_GROUP, getTimelineParams);
                        } else {
                            mService.requestSNS(GlobalConstant.SOCIAL_TYPE_FACEBOOK, FacebookConstant.FB_DATA_TYPE_PAGE_TIMELINE, getTimelineParams);
                        }
                        mListView.setVisibility(View.GONE);
                        mLinearProgressBar.setVisibility(View.VISIBLE);
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        mListView.setVisibility(View.VISIBLE);
    }

    private void updateFBFeedsFragment(int feedType, ArrayList<StoryItemUnit> storyItemList) {

        Log.d(TAG, "updateFeedsFragment");
        // feeds know as new_feed, new_feed_link, new_feed_photo, my_timeline
        // using for getpaging update more data
        boolean type_update = true;
        ArrayList<StoryItemUnit> sortItemList = sortStoryItemList(mFeedList, storyItemList);
        Log.e(TAG, "story list = " + storyItemList.size() + ", feed list = " + mFeedList.size());
        if (!checkTypeUpdate(sortItemList, mFeedList)) {
            type_update = false;
        }
        if (getFragmentCommonType() != FacebookConstant.FB_DATA_TYPE_FEED) {
            Log.d(TAG, "Fragment common type = " + getFragmentCommonType());
            return;
        }
        setType(feedType);
        ((FacebookFeedAdapter) mAdapter).updateList(sortItemList, type_update);
        ((FacebookFeedAdapter) mAdapter).setListener(this);
        // check empty data
        if (mFeedList == null || mFeedList.size() == 0) {
            // mLayoutEmpty.setVisibility(View.VISIBLE);
            // mLinearProgressBar.setVisibility(View.GONE);
            Log.d(TAG, "mFeedList = " + mFeedList);
            mListView.onRefresh();
            return;
        }
        mListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intentConentFeed = new Intent(getActivity(), FacebookSecondPageActivity.class);
                intentConentFeed.putExtra(GlobalConstant.STORY_ITEM_UNIT, (StoryItemUnit) parent.getAdapter().getItem(position));
                startActivity(intentConentFeed);
            }
        });
        mLinearProgressBar.setVisibility(View.GONE);
        mListView.setVisibility(View.VISIBLE);
    }

    public void updateFriendsFragment(int feedType, ArrayList<StoryItemUnit> storyItemList)
    {
        Log.d(TAG, "updateFriendsFragment");
        mFriendList = storyItemList;
        if (mFriendList == null || mFriendList.size() == 0) {
            mListView.onRefresh();
            return;
        }
        if (feedType == FacebookConstant.FB_DATA_MEMBERS_OF_GROUP) {
            ((FacebookMemberAdapter) mAdapter).updateFolderList(mFriendList);
        } else {
            ((FacebookFriendAdapter) mAdapter).updateFolderList(mFriendList);
        }
        //        ((FacebookFriendAdapter) mAdapter).updateFolderList(mFriendList);
        mListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    if (mService != null) {
                        //mFragmentType = FacebookConstant.FB_DATA_TYPE_FEED;
                        // show timeline for friend
                        HashMap<String, Object> getTimelineParams = new HashMap<String, Object>();
                        getTimelineParams.put("userId", ((StoryItemUnit) parent.getAdapter().getItem(position)).getSocialFeedId());
                        Log.d(TAG, "onFriendClick = " + ((StoryItemUnit) parent.getAdapter().getItem(position)).getSocialFeedId());
                        mActionBarTitle = ((StoryItemUnit) parent.getAdapter().getItem(position)).getAuthor_name();
                        mService.requestSNS(GlobalConstant.SOCIAL_TYPE_FACEBOOK, FacebookConstant.FB_DATA_TYPE_TIMELINE, getTimelineParams);
                        mListView.setVisibility(View.GONE);
                        mLinearProgressBar.setVisibility(View.VISIBLE);
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        mListView.setVisibility(View.VISIBLE);
    }

    private boolean checkTypeUpdate(ArrayList<StoryItemUnit> sortedStoryList, ArrayList<StoryItemUnit> mFeedList2) {
        // TODO Auto-generated method stub
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

    private void refreshListView() {
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put(GlobalConstant.TYPE_DATA_UPDATE, true);
        // Refresh
        try {
            if (mService != null) {
                mService.requestSNS(GlobalConstant.SOCIAL_TYPE_FACEBOOK, mFragmentType, param);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void loadMoreListView() {
        // load more data
        switch (mFragmentType) {
        case FacebookConstant.FB_DATA_TYPE_FEED:
        case FacebookConstant.FB_DATA_TYPE_FEED_PHOTOS:
        case FacebookConstant.FB_DATA_TYPE_FEED_LINKS:
        case FacebookConstant.FB_DATA_TYPE_TIMELINE:
            mNextPage = mFeedList.get(mFeedList.size() - 1).getNextPage();
            break;
        case FacebookConstant.FB_DATA_TYPE_GROUPS:
        case FacebookConstant.FB_DATA_TYPE_PAGES:
        case FacebookConstant.FB_DATA_TYPE_FRIEND_GROUPS:
            mNextPage = mLikedPagesList.get(mLikedPagesList.size() - 1).getNextPage();
            break;
        case FacebookConstant.FB_DATA_TYPE_TAGGEDME:
            mNextPage = mPhotoList.get(mPhotoList.size() - 1).getNextPage();
            break;
        default:
            break;
        }
        Log.d(TAG, "onLoadMore mNextPage = " + mNextPage);
        if (mNextPage != null && !"".equals(mNextPage)) {
            HashMap<String, Object> param = new HashMap<String, Object>();
            param.put(FacebookConstant.FB_KEY_NEXT_PAGE, mNextPage);
            param.put(GlobalConstant.TYPE_DATA_UPDATE, false);
            // Loading more
            try {
                if (mService != null) {
                    mService.requestSNS(GlobalConstant.SOCIAL_TYPE_FACEBOOK, mFragmentType, param);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            mListView.onLoadMoreComplete();
        }
    }
}
