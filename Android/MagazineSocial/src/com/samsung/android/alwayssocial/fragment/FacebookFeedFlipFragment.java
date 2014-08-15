package com.samsung.android.alwayssocial.fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.aphidmobile.flip.FlipViewController;
import com.samsung.android.alwayssocial.R;
import com.samsung.android.alwayssocial.activity.FacebookSecondPageActivity;
import com.samsung.android.alwayssocial.activity.IOnChangeFragment;
import com.samsung.android.alwayssocial.adapter.FacebookFlipFeedAdapter;
import com.samsung.android.alwayssocial.adapter.FacebookFlipFeedAdapter.ILikeCommentClick;
import com.samsung.android.alwayssocial.app.AlwaysSocialAppImpl;
import com.samsung.android.alwayssocial.constant.FacebookConstant;
import com.samsung.android.alwayssocial.constant.GlobalConstant;
import com.samsung.android.alwayssocial.database.DBManager;
import com.samsung.android.alwayssocial.service.CommentParcelableObject;
import com.samsung.android.alwayssocial.service.IAlwaysService;
import com.samsung.android.alwayssocial.service.IUpdateUiCallback;
import com.samsung.android.alwayssocial.service.StoryItemUnit;

/*Flipboard animation for Facebook feed type*/
public class FacebookFeedFlipFragment extends Fragment implements ILikeCommentClick {
    protected static final String TAG = "FacebookFeedFlipFragment";
    private BaseAdapter mAdapter;
    private ArrayList<StoryItemUnit> mFeedList = new ArrayList<StoryItemUnit>();
    private IAlwaysService mService = null;
    private IOnChangeFragment mRequestChangeFragment;
    private String mActionBarTitle = null;
    private int mFragmentType;
    private FlipViewController mFlipView;
    protected RelativeLayout mLinearProgressBar;
    protected View mLayoutEmpty;
    private String mNextPage;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = (ViewGroup) inflater.inflate(R.layout.page_flip_type, null);
        mFlipView = (FlipViewController) root.findViewById(R.id.fragment_flip_controller);
        mLinearProgressBar = (RelativeLayout) root.findViewById(R.id.fragment_flip_progress_layout);
        mLayoutEmpty = root.findViewById(R.id.fragment_flip_empty_data);
        mLinearProgressBar.setVisibility(View.VISIBLE);

        mFlipView.setAnimationBitmapFormat(Bitmap.Config.RGB_565);
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

        mFlipView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // TODO Auto-generated method stub
                Log.d(TAG, "on Feed Item Click1");
            }
        });
        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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

    private void buildAdapter(int type) {
        Log.d(TAG, "build adapter");
        // Reset adapter
        mAdapter = null;
        switch (type) {
        case FacebookConstant.FB_DATA_TYPE_FEED:
        case FacebookConstant.FB_DATA_TYPE_FEED_PHOTOS:
        case FacebookConstant.FB_DATA_TYPE_FEED_LINKS:
        case FacebookConstant.FB_DATA_TYPE_TIMELINE:
        case FacebookConstant.FB_DATA_TYPE_FRIEND_TIMELINE:
        case FacebookConstant.FB_DATA_TYPE_PAGE_TIMELINE:
            Log.d(TAG, "build FB_DATA_TYPE_TIMELINE");
            mAdapter = new FacebookFlipFeedAdapter(getActivity(), mFeedList);
            ((FacebookFlipFeedAdapter) mAdapter).setListener(this);
            break;
        case FacebookConstant.FB_DATA_TYPE_TAGGEDME:
            Log.d(TAG, "build FB_DATA_TYPE_TAGGEDME");
            //mAdapter = new FacebookMyTaggedPhotoAdapter(getActivity(), mPhotoList);
            break;
        default:
            break;
        }
        if (null != mAdapter) {
            mFlipView.setAdapter(mAdapter);
            mFlipView.setVisibility(View.GONE);
            mLinearProgressBar.setVisibility(View.VISIBLE);
            mLayoutEmpty.setVisibility(View.GONE);
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
        default:
            break;
        }
        return mFragmentType;
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume");
        // Set like, comment count
        if (getFragmentCommonType() == FacebookConstant.FB_DATA_TYPE_FEED && mFeedList != null && mFeedList.size() > 0) {
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
            Intent intentConentFeed = new Intent(getActivity(), FacebookSecondPageActivity.class);
            intentConentFeed.putExtra(GlobalConstant.SOCIAL_2ND_VIEW_TYPE, true);
            intentConentFeed.putExtra(GlobalConstant.STORY_ITEM_UNIT, item);
            intentConentFeed.putExtra(GlobalConstant.SOCIAL_TYPE, GlobalConstant.SOCIAL_TYPE_FACEBOOK);
            startActivity(intentConentFeed);
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
            if (mFeedList.size() == 0) {
                mLayoutEmpty.setVisibility(View.VISIBLE);
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
            if (feedType == FacebookConstant.FB_DATA_TYPE_TIMELINE && mFragmentType == FacebookConstant.FB_DATA_TYPE_FRIENDS) {
                // check the case of currently is in Friends and response is in timeline 
                updateNotMeTimeline(FacebookConstant.FB_DATA_TYPE_TIMELINE, (ArrayList<StoryItemUnit>) items, isResponseByRefresh);
            } else if (feedType == FacebookConstant.FB_DATA_TYPE_PAGE_TIMELINE
                    && (mFragmentType == FacebookConstant.FB_DATA_TYPE_PAGES || mFragmentType == FacebookConstant.FB_DATA_TYPE_GROUPS)) {
                updateNotMeTimeline(FacebookConstant.FB_DATA_TYPE_PAGE_TIMELINE, (ArrayList<StoryItemUnit>) items, isResponseByRefresh);
            }
        }
    };

    public void updateNotMeTimeline(int type, ArrayList<StoryItemUnit> storyItemList, boolean isResponseByRefresh) {
        mFragmentType = type;
        buildAdapter(mFragmentType);
        updateFBFeedsFragment(mFragmentType, storyItemList);
        if (mActionBarTitle != null) {
            mRequestChangeFragment.onRequestChangeTitle(mActionBarTitle);
        }

    }

    public void updateUI(String socialType, int feedType, ArrayList<StoryItemUnit> storyItemList, boolean isResponseByRefresh) {
        Log.d(TAG, "updateUI() SocialType = " + socialType + " feedType = " + feedType + "mFragmentType =" + mFragmentType);
        //      mLinearProgressBar.setVisibility(View.GONE);
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
        default:
            Log.d(TAG, "Not handle Fragment Type = " + mFragmentType);
            break;
        }
    }

    private void updateFBFeedsFragment(int feedType, ArrayList<StoryItemUnit> storyItemList) {

        Log.d(TAG, "updateFeedsFragment");
        // feeds know as new_feed, new_feed_link, new_feed_photo, my_timeline
        // using for getpaging update more data
        boolean type_update = true;
        ArrayList<StoryItemUnit> sortItemList = sortStoryItemList(mFeedList, storyItemList);

        if (!checkTypeUpdate(sortItemList, mFeedList)) {
            type_update = false;
        }
        if (getFragmentCommonType() != FacebookConstant.FB_DATA_TYPE_FEED) {
            Log.d(TAG, "Fragment common type = " + getFragmentCommonType());
            return;
        }
        if (sortItemList.size() == 0) {
            Log.d(TAG, "sortItemList = " + sortItemList);
            // If have no data in database -> request Refresh
            onFlipRefresh();
            return;
        }
        setType(feedType);
        
        Log.e(TAG, "story list = " + storyItemList.size() + ", feed list = " + mFeedList.size());
        int count = 0 ;
        ArrayList<StoryItemUnit> _storyList = new ArrayList<StoryItemUnit>();
        if (sortItemList.size() > 20) {
            for (int i = 20; i < sortItemList.size(); i++) {
                count ++;
                _storyList.add(sortItemList.get(i));
            }
        }
        sortItemList.removeAll(_storyList);
        if(mFeedList.containsAll(_storyList))
            mFeedList.removeAll(_storyList);
        Log.e(TAG, "story list = " + sortItemList.size() + ", feed list = " + mFeedList.size() +"  count "+count);
        ((FacebookFlipFeedAdapter) mAdapter).updateList(sortItemList, type_update);
        ((FacebookFlipFeedAdapter) mAdapter).setListener(this);
        mFlipView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intentConentFeed = new Intent(getActivity(), FacebookSecondPageActivity.class);
                intentConentFeed.putExtra(GlobalConstant.STORY_ITEM_UNIT, (StoryItemUnit) parent.getAdapter().getItem(position));
                intentConentFeed.putExtra(GlobalConstant.SOCIAL_TYPE, GlobalConstant.SOCIAL_TYPE_FACEBOOK);
                startActivity(intentConentFeed);
            }
        });
        mLinearProgressBar.setVisibility(View.GONE);
        mFlipView.setVisibility(View.VISIBLE);
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

    public void setType(int type) {
        mFragmentType = type;
    }

    private void onFlipRefresh() {
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

    private void onFlipLoadMore() {
        // load more data
        switch (mFragmentType) {
        case FacebookConstant.FB_DATA_TYPE_FEED:
        case FacebookConstant.FB_DATA_TYPE_FEED_PHOTOS:
        case FacebookConstant.FB_DATA_TYPE_FEED_LINKS:
        case FacebookConstant.FB_DATA_TYPE_TIMELINE:
            mNextPage = mFeedList.get(mFeedList.size() - 1).getNextPage();
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
        }
    }

    @Override
    public void onClickItem(int typeFeed, int position) {
        // TODO Auto-generated method stub
        switch (typeFeed) {
        case FacebookConstant.FB_DATA_TYPE_FEED:
        case FacebookConstant.FB_DATA_TYPE_FEED_PHOTOS:
        case FacebookConstant.FB_DATA_TYPE_FEED_LINKS:
        case FacebookConstant.FB_DATA_TYPE_TIMELINE:
            Log.d(TAG, "on Feed Item Click2");
            Intent intentConentFeed = new Intent(getActivity(), FacebookSecondPageActivity.class);
            intentConentFeed.putExtra(GlobalConstant.STORY_ITEM_UNIT, mFeedList.get(position));
            intentConentFeed.putExtra(GlobalConstant.SOCIAL_TYPE, GlobalConstant.SOCIAL_TYPE_FACEBOOK);
            startActivity(intentConentFeed);
            break;

        default:
            break;
        }
    }
}
