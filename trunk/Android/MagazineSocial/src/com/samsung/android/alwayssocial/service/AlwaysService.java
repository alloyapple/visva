package com.samsung.android.alwayssocial.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import twitter4j.PagableResponseList;
import twitter4j.ResponseList;
import twitter4j.SavedSearch;
import twitter4j.Status;
import twitter4j.User;
import twitter4j.UserList;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.ContactsContract.Contacts.Photo;
import android.util.Log;

import com.samsung.android.alwayssocial.app.AlwaysSocialAppImpl;
import com.samsung.android.alwayssocial.constant.FacebookConstant;
import com.samsung.android.alwayssocial.constant.GlobalConstant;
import com.samsung.android.alwayssocial.constant.TwitterConstant;
import com.samsung.android.alwayssocial.database.AlwaysDatabaseProvider;
import com.samsung.android.alwayssocial.object.ResponseData;
import com.samsung.android.alwayssocial.object.SocialUserObject;
import com.samsung.android.alwayssocial.object.facebook.FacebookCommentList;
import com.samsung.android.alwayssocial.object.facebook.FacebookFeedWrapper;
import com.samsung.android.alwayssocial.object.facebook.FacebookFeeds;
import com.samsung.android.alwayssocial.object.facebook.FacebookFriends;
import com.samsung.android.alwayssocial.object.facebook.FacebookPage;
import com.samsung.android.alwayssocial.object.facebook.FacebookPages;
import com.samsung.android.alwayssocial.object.facebook.FacebookPhoto;
import com.samsung.android.alwayssocial.object.facebook.FacebookPhotos;
import com.samsung.android.alwayssocial.object.facebook.FacebookSummary;
import com.samsung.android.alwayssocial.object.facebook.FacebookUser;
import com.samsung.android.alwayssocial.object.twitter.TwitterFollowersData;
import com.samsung.android.alwayssocial.object.twitter.TwitterListData;
import com.samsung.android.alwayssocial.object.twitter.TwitterListFollowData;
import com.samsung.android.alwayssocial.object.twitter.TwitterSavedSearchesData;
import com.samsung.android.alwayssocial.object.twitter.TwitterStatusData;
import com.samsung.android.alwayssocial.object.twitter.TwitterTimeLineData;
import com.samsung.android.alwayssocial.object.twitter.TwitterUserData;
import com.samsung.android.alwayssocial.servermanager.AlwaysSocialManager;
import com.samsung.android.alwayssocial.story.StoryConverter;
import com.samsung.android.alwayssocial.story.StoryHomeConnector;

public class AlwaysService extends Service implements IResponseFromSNSCallback {

    /*TODO HashMap for Listener with owner_id
     * Background thread for service
     */
    private static final String TAG = "AlwaysService";
    private AlwaysDatabaseProvider mAlwaysDBProvider;
    private IUpdateUiCallback mISendDataToUI;
    private ArrayList<StoryItemUnit> mStoryItemList = new ArrayList<StoryItemUnit>();
    boolean mIsRequestedForMagazineHome = false;

    @Override
    public void onResponse(String socialType, int feedType, ResponseData responseData) {
        Log.d(TAG, "Received from SNS - normal case, feedtype = " + feedType + " MeRquest = " + responseData.isMeRequest + " isRequestUpdateRefresh =" + responseData.isRequestUpdateRefresh);
        if (GlobalConstant.SOCIAL_TYPE_FACEBOOK.compareTo(socialType) == 0) {
            handleResponseFromFacebook(feedType, responseData);
        } else if (GlobalConstant.SOCIAL_TYPE_TWITTER.compareTo(socialType) == 0) {
            handleResponseFromTwitter(feedType, responseData);
        }
    }

    @Override
    public void onErrorResponse(String socialType, int error) {
        Log.d(TAG, "Received from SNS - error case");
        if (GlobalConstant.SOCIAL_TYPE_FACEBOOK.compareTo(socialType) == 0) {
            try {
                if (mISendDataToUI != null) {
                    mISendDataToUI.onUpdateErrorUi(socialType, error, false);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onImagesQualityResponse(String socialType, int requestType, HashMap<String, FacebookPhoto> photos, ArrayList<FacebookFeedWrapper> feeds) {
        /**update to database*/
        if (requestType == FacebookConstant.FB_DATA_TYPE_SPECIAL_IMAGE_QUALITY) {
            updateDataFacebookFeedToDataBase(requestType, feeds, photos, "");
        }
        // Get image with height, width, image_url information
        // Send story Item
        mIsRequestedForMagazineHome = false;
        StoryHomeConnector.putDataSNSToMagazineHome(getApplicationContext(), feeds, photos);
    }

    public void handleResponseFromFacebook(int feedType, ResponseData responseData) {
        String socialType = GlobalConstant.SOCIAL_TYPE_FACEBOOK;
        // For second page, no need to use database
        try {
            if (feedType == FacebookConstant.FB_DATA_TYPE_FEED) {
                if (mIsRequestedForMagazineHome) {
                    // Request quality of image in case of MagazineHome requesting
                    FacebookFeeds feedsData = (FacebookFeeds) responseData.responseData;
                    AlwaysSocialManager.getInstance().getFacebookImagesInformation(FacebookConstant.FB_DATA_TYPE_SPECIAL_IMAGE_QUALITY, feedsData, AlwaysService.this);
                } else {
                    // Response to UI in case of UI requesting
                    if (responseData.isMeRequest) {
                        // Just save to database if it is my request
                        updateDataSNSToDataBase(socialType, feedType, responseData, null);
                        reponseDataFromDataBase(socialType, feedType, responseData.isRequestUpdateRefresh);
                    }
                }
            } else if (feedType == FacebookConstant.FB_DATA_MORE_COMMENT) {
                // Facebook comments data
                FacebookCommentList commentData = (FacebookCommentList) responseData.responseData;
                List<FacebookFeedWrapper.Comment> comments = commentData.getData();
                List<CommentParcelableObject> commnetParcelableObjects = new ArrayList<CommentParcelableObject>();
                for (int i = 0; i < comments.size(); i++) {
                    commnetParcelableObjects.add(new CommentParcelableObject(comments.get(i).getId(),
                            comments.get(i).getFrom().getName(),
                            comments.get(i).getFrom().getId(),
                            comments.get(i).getMessage(),
                            comments.get(i).getCreated_time()));
                }
                if (mISendDataToUI != null) {
                    String nextPage = commentData.getPaging() == null ? "" : commentData.getPaging().next;
                    mISendDataToUI.onUpdateCommnetInforToUi(socialType, feedType, commnetParcelableObjects, nextPage);
                }
            } else if (feedType == FacebookConstant.FB_DATA_TYPE_LIKEINFO || feedType == FacebookConstant.FB_DATA_TYPE_COMMENTINFO) {
                // Comment and like count
                FacebookSummary likeInfo = (FacebookSummary) responseData.responseData;
                if (mISendDataToUI != null) {
                    mISendDataToUI.onUpdateLikeCommentCount(socialType, feedType, likeInfo.getTotal_count(), likeInfo.isUserLiked);
                }
            } else if (feedType == FacebookConstant.FB_DATA_TYPE_USER) {
                // Save User information of facebook user to share prefs
                FacebookUser user = (FacebookUser) responseData.responseData;
                if (user != null) {
                    SocialUserObject loggedInUser = new SocialUserObject(user.id, user.name);
                    AlwaysSocialAppImpl.getInstance().setLoggedInUser(GlobalConstant.SOCIAL_TYPE_FACEBOOK, loggedInUser);
                }
                // implement groups
            } else if (feedType == FacebookConstant.FB_DATA_MEMBERS_OF_GROUP) {
                FacebookFriends membersOfGroup = (FacebookFriends) responseData.responseData;
                ArrayList<FacebookUser> members = membersOfGroup.mFriendList;
                ArrayList<StoryItemUnit> storyItemUnitList = new ArrayList<StoryItemUnit>();
                for (FacebookUser mem : members) {
                    StoryItemUnit storyItemUnit = StoryConverter.convertFBMember(feedType, mem);
                    storyItemUnitList.add(storyItemUnit);
                }
                if (mISendDataToUI != null) {
                    mISendDataToUI.receiveStoryItems(socialType, feedType, storyItemUnitList, responseData.isRequestUpdateRefresh, responseData.isFirstTimeUpdate);
                }
            } else {
                // Feed
                if (responseData.isMeRequest) {
                    // Just save to database if it is my request
                    updateDataSNSToDataBase(socialType, feedType, responseData, null);
                    reponseDataFromDataBase(socialType, feedType, responseData.isRequestUpdateRefresh);
                } else {

                    Log.d(TAG, "Response : Not me request");
                    FacebookFeeds feedsData = (FacebookFeeds) responseData.responseData;
                    ArrayList<FacebookFeedWrapper> feeds = feedsData.getData();
                    ArrayList<StoryItemUnit> storyItemUnitList = new ArrayList<StoryItemUnit>();
                    String nextPage = feedsData.getPaging() == null ? "" : feedsData.getPaging().getNextPage();
                    for (FacebookFeedWrapper feed : feeds) {
                        StoryItemUnit storyItemUnit = StoryConverter.convertFBFeedToDBStoryItemUnit(feedType, feed, null, nextPage);
                        storyItemUnitList.add(storyItemUnit);
                    }
                    if (mISendDataToUI != null) {
                        mISendDataToUI.receiveStoryItems(socialType, feedType, storyItemUnitList, responseData.isRequestUpdateRefresh, responseData.isFirstTimeUpdate);
                    }
                }
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void handleResponseFromTwitter(int feedType, ResponseData responseData) {
        String socialType = GlobalConstant.SOCIAL_TYPE_TWITTER;
        try {
            if (feedType == TwitterConstant.TW_POST_RETWEET) {
                TwitterStatusData data = (TwitterStatusData) responseData.responseData;
                if (mISendDataToUI != null)
                {
                    if (data.getStatus() != null) {
                        mISendDataToUI.onUpdateUiByStoryItem(socialType, feedType, responseData.isRequestUpdateRefresh);
                    } else {
                        mISendDataToUI.onUpdateErrorUi(socialType, feedType, responseData.isRequestUpdateRefresh);
                    }
                }
            } else if (feedType == TwitterConstant.TW_DATA_TYPE_COMMENT) {
                TwitterTimeLineData commentStatusData = (TwitterTimeLineData) responseData.responseData;
                List<Status> listStatuses = commentStatusData.getStatuses();
                List<CommentParcelableObject> listCommentParcelable = new ArrayList<CommentParcelableObject>();
                for (int i = 0; i < listStatuses.size(); i++) {
                    listCommentParcelable.add(new CommentParcelableObject(Long.toString(listStatuses.get(i).getId()), listStatuses.get(i).getUser().getScreenName(), listStatuses.get(i).getUser().getProfileImageURL(), listStatuses.get(i).getText(), listStatuses.get(i).getCreatedAt().toString()));
                }
                if (mISendDataToUI != null) {
                    mISendDataToUI.onUpdateCommnetInforToUi(socialType, TwitterConstant.TW_DATA_TYPE_COMMENT, listCommentParcelable, "");
                }
            } else if (feedType == TwitterConstant.TW_POST_DESTROY_FRIENDSHIP) {
                TwitterUserData user = (TwitterUserData) responseData.responseData;
                if(mISendDataToUI != null) {
                    if(user != null) {
                        Log.d(TAG, "destroy friendship successfully ");
                        mISendDataToUI.onUpdateUiByStoryItem(socialType, feedType, responseData.isRequestUpdateRefresh);
                    } else {
                        Log.d(TAG, "destroy friendship failed");
                        mISendDataToUI.onUpdateErrorUi(socialType, feedType, responseData.isRequestUpdateRefresh);
                    }
                }
            } else {
                if (responseData.isMeRequest) {
                    // Just save to database if it is my request
                    updateDataSNSToDataBase(socialType, feedType, responseData, null);
                    reponseDataFromDataBase(socialType, feedType, responseData.isRequestUpdateRefresh);
                } else {
                    Log.d(TAG, "not me Request");
                    TwitterTimeLineData tweetsData = (TwitterTimeLineData) responseData.responseData;
                    ArrayList<twitter4j.Status> listStatuses = (ArrayList<Status>) tweetsData.getStatuses();
                    ArrayList<StoryItemUnit> storyItemUnitList = new ArrayList<StoryItemUnit>();
                    try {
                        for (Status status : listStatuses) {
                            StoryItemUnit item = StoryConverter.convertTWTimeLineToDBStoryItemUnit(feedType, status, "");
                            storyItemUnitList.add(item);
                        }
                    } catch (NullPointerException e) {
                        Log.d(TAG, "null pointer exception occur ~.~");
                        if (mISendDataToUI != null) {
                            mISendDataToUI.onUpdateErrorUi(socialType, feedType, responseData.isRequestUpdateRefresh);
                        }
                        e.printStackTrace();
                    }
                    if (mISendDataToUI != null) {
                        mISendDataToUI.receiveStoryItems(socialType, feedType, storyItemUnitList, responseData.isRequestUpdateRefresh, responseData.isFirstTimeUpdate);
                    }
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void updateDataSNSToDataBase(String socialType, int feedType, ResponseData responseData, HashMap<String, FacebookPhoto> photos) {
        if (responseData == null) {
            return;
        }
        mStoryItemList = mAlwaysDBProvider.getAllStoryItem(socialType, feedType);
        if (GlobalConstant.SOCIAL_TYPE_FACEBOOK.compareTo(socialType) == 0) {
            updateDataFacebookToDatabase(feedType, responseData, null);
        } else if (GlobalConstant.SOCIAL_TYPE_TWITTER.compareTo(socialType) == 0) {
            updateDataTwitterToDatabase(feedType, responseData, null);
        }
    }

    /* Remote method definition */
    private final IBinder mBinder = new IAlwaysService.Stub() {

        @SuppressWarnings({ "rawtypes", "unchecked" })
        @Override
        public void requestSNS(String socialType, int requestDataType, Map param) throws RemoteException {
            // GET method
            if (param != null) {
                HashMap<String, Object> params = (param instanceof HashMap) ? (HashMap) param : new HashMap<String, Object>(param);
                AlwaysService.this.requestSNS(socialType, requestDataType, params);
            }
            else {
                AlwaysService.this.requestSNS(socialType, requestDataType, null);
            }
        }

        @Override
        public void postSNS(String socialType, String id, String data, int postType) throws RemoteException {
            // POST method
            AlwaysService.this.postSNS(socialType, id, data, postType);
        }

        @Override
        public void registerUiListener(String socialType, int ownerId, IUpdateUiCallback listener) throws RemoteException {
            // Register Callback of UI
            AlwaysService.this.registerUiListener(listener);
        }

        @Override
        public void unregisterUiNotify(String socialType, int ownerId) throws RemoteException {
            // unregister callback of UI
            AlwaysService.this.unregisterUiNotify();
        }

        @Override
        public void logoutSocial() throws RemoteException {
            AlwaysService.this.resetDataWhenUserLogOut();
        }

    };

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind()");
        return mBinder;
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate()");
        super.onCreate();
        intializeDB();

        /**update only feed facebook and timeline twitter*/
        //        timer.start();
    }

    private void intializeDB() {
        mAlwaysDBProvider = new AlwaysDatabaseProvider(this);
        mStoryItemList = mAlwaysDBProvider.getAllStoryItem();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy()");
        super.onDestroy();
    }

    @Override
    public void onRebind(Intent intent) {
        Log.d(TAG, "onRebind()");
        super.onRebind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand() - intent:" + intent + ", flags:" + flags + ", startId:" + startId);
        // Service connected
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null && bundle.getString("action").equals("update")) {
                // Request for Magazine Home
                mIsRequestedForMagazineHome = true;
                requestSNS(GlobalConstant.SOCIAL_TYPE_FACEBOOK, FacebookConstant.FB_DATA_TYPE_FEED, null);
            }
        }
        return super.onStartCommand(intent, START_NOT_STICKY, startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind()");
        return super.onUnbind(intent);
    }

    // command request to SNS
    private void requestSNS(String socialType, int requestDataType, HashMap<String, Object> param){
        if (param != null || requestDataType == FacebookConstant.FB_DATA_TYPE_USER) {
            Log.d(TAG, "Request SNS");
            AlwaysSocialManager.getInstance().requestSNS(socialType, requestDataType, param, this);
        } else if (requestDataType != FacebookConstant.FB_DATA_MORE_COMMENT) {
            // 1st page will be responsed immeadiately
            Log.d(TAG, "Reponse from saved data");
            reponseDataFromDataBase(socialType, requestDataType, true);
            if(mIsRequestedForMagazineHome){
                ArrayList<StoryItemUnit> storyItemUnits = new ArrayList<StoryItemUnit>();
                storyItemUnits = mAlwaysDBProvider.getAllStoryItem(GlobalConstant.SOCIAL_TYPE_FACEBOOK, FacebookConstant.FB_DATA_TYPE_FEED);
                ArrayList<FacebookFeedWrapper> data = new ArrayList<FacebookFeedWrapper>();
                for(int i = 0 ;i < storyItemUnits.size();i++){
                    FacebookFeedWrapper facebookFeedWrapper = StoryConverter.convertDBStoryItemUnitToFeed(storyItemUnits.get(i));
                    data.add(facebookFeedWrapper);
                }
                FacebookFeeds feedsData = new FacebookFeeds();
                feedsData.setData(data);
                AlwaysSocialManager.getInstance().getFacebookImagesInformation(FacebookConstant.FB_DATA_TYPE_SPECIAL_IMAGE_QUALITY, feedsData, AlwaysService.this);
        }
        }
    }

    public void postSNS(String socialType, String id, String data, int postType){
        AlwaysSocialManager.getInstance().postSNS(socialType, id, data, postType, this);
    }

    public void registerUiListener(IUpdateUiCallback listener) {
        mISendDataToUI = listener;
    }

    public void unregisterUiNotify() {
        mISendDataToUI = null;
    }

    //**********************************************************
    // database handler
    //**********************************************************
    private void updateDataTwitterToDatabase(int feedType, ResponseData responseData, Object object) {
        Log.d(TAG, "kieu.thag updateDataTwitterToDatabase " + feedType);
        switch (feedType) {
        case TwitterConstant.TW_DATA_TYPE_TIMELINE:
        case TwitterConstant.TW_DATA_TYPE_TIMELINE_LINKS:
        case TwitterConstant.TW_DATA_TYPE_TWEETS:
        case TwitterConstant.TW_DATA_TYPE_FAVORITES:
        case TwitterConstant.TW_DATA_TYPE_MENTIONING_TWEETS:
            TwitterTimeLineData twTimeLineData = (TwitterTimeLineData) responseData.responseData;
            List<twitter4j.Status> mLstStatuses = twTimeLineData.getStatuses();
            //            if(responseData)
            updateDataTwitterTimeLineToDatabase(feedType, mLstStatuses, "");
            break;
        case TwitterConstant.TW_DATA_TYPE_SAVED_SEARCHES:
            TwitterSavedSearchesData twSaveSearchData = (TwitterSavedSearchesData) responseData.responseData;
            List<SavedSearch> mLstSavedSearches = twSaveSearchData.getListSavedSearches();
            updateDataTwitterSavedSearchToDatabase(feedType, mLstSavedSearches, "");
            break;
        case TwitterConstant.TW_DATA_TYPE_LIST:
            TwitterListData twListData = (TwitterListData) responseData.responseData;
            ResponseList<UserList> mlstUserList = twListData.getListUserList();
            updateDataTwitterUserListToDatabase(feedType, mlstUserList, "");
            break;
        case TwitterConstant.TW_DATA_TYPE_LIST_FOLLOWED:
        case TwitterConstant.TW_DATA_TYPE_LIST_FOLLOWING_ME:
            TwitterListFollowData twListFollowedData = (TwitterListFollowData) responseData.responseData;
            PagableResponseList<UserList> mlstListFollowMe = twListFollowedData.getlstUserListFollow();
            updateDataTwitterListFollowingMeToDatabase(feedType, mlstListFollowMe, "");
            break;
        case TwitterConstant.TW_DATA_TYPE_PEOPLE_FOLLOWED:
        case TwitterConstant.TW_DATA_TYPE_PEOPLE_FOLLOWING_ME:
            TwitterFollowersData twListPeopleFollowedData = (TwitterFollowersData) responseData.responseData;
            List<User> mlstListPeopleFollow = twListPeopleFollowedData.getFollowers();
            updateDataTwitterPeopleFollowToDatabase(feedType, mlstListPeopleFollow, "");
            break;
        default:
            break;
        }
    }

    private void updateDataTwitterPeopleFollowToDatabase(int feedType, List<User> mlstListPeopleFollow, String nextPage) {
        ArrayList<StoryItemUnit> storyItemUnitList = new ArrayList<StoryItemUnit>();
        for (User user : mlstListPeopleFollow) {
            StoryItemUnit storyItemUnit = StoryConverter.convertTWPeopleFollowListToDBStoryItemUnit(feedType, user);
            if (!isContainTWStoryItem(mStoryItemList, storyItemUnit)) {
                mAlwaysDBProvider.addNewStoryItem(storyItemUnit);
                mStoryItemList.add(storyItemUnit);
                storyItemUnitList.add(storyItemUnit);
            }
        }
    }

    private void updateDataTwitterListFollowingMeToDatabase(int feedType, PagableResponseList<UserList> mlstListFollowMe, String nextPage) {
        ArrayList<StoryItemUnit> storyItemUnitList = new ArrayList<StoryItemUnit>();
        for (UserList userList : mlstListFollowMe) {
            StoryItemUnit storyItemUnit = StoryConverter.convertTWTypeListToDBStoryItemUnit(feedType, userList, nextPage);
            if (!isContainTWStoryItem(mStoryItemList, storyItemUnit)) {
                mAlwaysDBProvider.addNewStoryItem(storyItemUnit);
                mStoryItemList.add(storyItemUnit);
                storyItemUnitList.add(storyItemUnit);
            }
        }
    }

    //    private void udpateDataTwitterFollowerToDatabase(int feedType, List<User> mFollowers) {
    //        ArrayList<StoryItemUnit> storyItemUnitList = new ArrayList<StoryItemUnit>();
    //        for (User user : mFollowers) {
    //            StoryItemUnit storyItemUnit = StoryConverter.convertTWPeopleFollowListToDBStoryItemUnit(feedType, user);
    //            if (!isContainStoryItem(mStoryItemList, storyItemUnit)) {
    //                mAlwaysDBProvider.addNewStoryItem(storyItemUnit);
    //                mStoryItemList.add(storyItemUnit);
    //                storyItemUnitList.add(storyItemUnit);
    //            }
    //        }
    //    }

    private void updateDataTwitterUserListToDatabase(int feedType, ResponseList<UserList> mlstUserList, String nextPage) {
        ArrayList<StoryItemUnit> storyItemUnitList = new ArrayList<StoryItemUnit>();
        for (UserList userList : mlstUserList) {
            StoryItemUnit storyItemUnit = StoryConverter.convertTWTypeListToDBStoryItemUnit(feedType, userList, nextPage);
            if (!isContainTWStoryItem(mStoryItemList, storyItemUnit)) {
                mAlwaysDBProvider.addNewStoryItem(storyItemUnit);
                mStoryItemList.add(storyItemUnit);
                storyItemUnitList.add(storyItemUnit);
            }
        }
    }

    private void updateDataTwitterSavedSearchToDatabase(int feedType, List<SavedSearch> mLstSavedSearches, String nextPage) {
        ArrayList<StoryItemUnit> storyItemUnitList = new ArrayList<StoryItemUnit>();
        for (SavedSearch savedSearch : mLstSavedSearches) {
            StoryItemUnit storyItemUnit = StoryConverter.convertTWSaveSearchToDBStoryItemUnit(feedType, savedSearch, nextPage);
            if (!isContainTWStoryItem(mStoryItemList, storyItemUnit)) {
                mAlwaysDBProvider.addNewStoryItem(storyItemUnit);
                mStoryItemList.add(storyItemUnit);
                storyItemUnitList.add(storyItemUnit);
            }
        }
    }

    private void updateDataTwitterTimeLineToDatabase(int feedType, List<Status> mLstStatuses, String nextPage) {
        ArrayList<StoryItemUnit> storyItemUnitList = new ArrayList<StoryItemUnit>();
        if (mLstStatuses != null)
            for (Status status : mLstStatuses) {
                StoryItemUnit storyItemUnit = StoryConverter.convertTWTimeLineToDBStoryItemUnit(feedType, status, nextPage);
                Log.d(TAG, "kieu.thang isContainStoryItem " + isContainStoryItem(mStoryItemList, storyItemUnit) + " feedType " + feedType);
                if (!isContainTWStoryItem(mStoryItemList, storyItemUnit)) {
                    mAlwaysDBProvider.addNewStoryItem(storyItemUnit);
                    mStoryItemList.add(storyItemUnit);
                    storyItemUnitList.add(storyItemUnit);
                }
            }
    }

    private void updateDataFacebookToDatabase(int feedType, ResponseData responseData, HashMap<String, FacebookPhoto> object) {
        String nextPage;
        switch (feedType) {
        case FacebookConstant.FB_DATA_TYPE_FEED:
        case FacebookConstant.FB_DATA_TYPE_FEED_LINKS:
        case FacebookConstant.FB_DATA_TYPE_FEED_PHOTOS:
        case FacebookConstant.FB_DATA_TYPE_TIMELINE:
            FacebookFeeds feedsData = (FacebookFeeds) responseData.responseData;
            ArrayList<FacebookFeedWrapper> feeds = feedsData.getData();
            nextPage = feedsData.getPaging() == null ? "" : feedsData.getPaging().getNextPage();
            updateDataFacebookFeedToDataBase(feedType, feeds, null, nextPage);
            break;
        case FacebookConstant.FB_DATA_TYPE_GROUPS:
        case FacebookConstant.FB_DATA_TYPE_PAGES:
        case FacebookConstant.FB_DATA_TYPE_FRIEND_GROUPS:
            FacebookPages likeData = (FacebookPages) responseData.responseData;
            ArrayList<FacebookPage> feedLikesPage = likeData.getData();
            nextPage = likeData.getPaging() == null ? "" : likeData.getPaging().getNextPage();
            updateDataFacebookPageGroupFriendAlbumnToDatabase(feedType, feedLikesPage, null, nextPage);
            break;
        case FacebookConstant.FB_DATA_TYPE_FRIENDS:
            FacebookFriends friendData = (FacebookFriends) responseData.responseData;
            ArrayList<FacebookUser> mListFriend = friendData.mFriendList;
            updateDataFacebookFriendsToDatabase(feedType, mListFriend, null);
            break;
        case FacebookConstant.FB_DATA_TYPE_TAGGEDME:
            FacebookPhotos taggedMeData = (FacebookPhotos) responseData.responseData;
            ArrayList<FacebookPhoto> mListTaggedme = taggedMeData.getData();
            nextPage = taggedMeData.getPaging() == null ? "" : taggedMeData.getPaging().getNextPage();
            updateDataFacebookTaggedMeToDatabase(feedType, mListTaggedme, nextPage);
            break;
        default:
            break;
        }
    }

    private void reponseDataFromDataBase(String socialType, int feedType, boolean isResponseByRefresh) {
        // Send Data to UI
        Log.d(TAG, "reponseDataFromDataBase feedType = " + feedType);
        try {
            if (mISendDataToUI != null) {
                mISendDataToUI.onUpdateUiByStoryItem(socialType, feedType, isResponseByRefresh);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void updateDataFacebookFeedToDataBase(int requestType, ArrayList<FacebookFeedWrapper> feeds, HashMap<String, FacebookPhoto> photos, String nextPage) {
        ArrayList<StoryItemUnit> storyItemUnitList = new ArrayList<StoryItemUnit>();
        for (FacebookFeedWrapper feed : feeds) {
            StoryItemUnit storyItemUnit = StoryConverter.convertFBFeedToDBStoryItemUnit(requestType, feed, photos, nextPage);
            if (!isContainStoryItem(mStoryItemList, storyItemUnit)) {
                mAlwaysDBProvider.addNewStoryItem(storyItemUnit);
                mStoryItemList.add(storyItemUnit);
                storyItemUnitList.add(storyItemUnit);
            }
        }
    }

    private void updateDataFacebookPageGroupFriendAlbumnToDatabase(int feedType, ArrayList<FacebookPage> feedLikesPage, HashMap<String, Photo> photos, String nextPage) {
        ArrayList<StoryItemUnit> storyItemUnitList = new ArrayList<StoryItemUnit>();
        for (FacebookPage pages : feedLikesPage) {
            StoryItemUnit storyItemUnit = StoryConverter.convertFBPages_Groups_FriendGroups(feedType, pages, nextPage);
            if (!isContainStoryItem(mStoryItemList, storyItemUnit)) {
                mAlwaysDBProvider.addNewStoryItem(storyItemUnit);
                mStoryItemList.add(storyItemUnit);
                storyItemUnitList.add(storyItemUnit);
            }
        }
    }

    private void updateDataFacebookFriendsToDatabase(int feedType, ArrayList<FacebookUser> mListFriend, Object object) {
        ArrayList<StoryItemUnit> storyItemUnitList = new ArrayList<StoryItemUnit>();
        for (FacebookUser friend : mListFriend) {
            StoryItemUnit storyItemUnit = StoryConverter.convertFBFriend(feedType, friend);
            if (!isContainStoryItem(mStoryItemList, storyItemUnit)) {
                mAlwaysDBProvider.addNewStoryItem(storyItemUnit);
                mStoryItemList.add(storyItemUnit);
                storyItemUnitList.add(storyItemUnit);
            }
        }
    }

    private void updateDataFacebookTaggedMeToDatabase(int feedType, ArrayList<FacebookPhoto> mListTaggedme, String nextPage) {
        ArrayList<StoryItemUnit> storyItemUnitList = new ArrayList<StoryItemUnit>();
        for (FacebookPhoto photo : mListTaggedme) {
            StoryItemUnit storyItemUnit = StoryConverter.convertFBTaggedMe(feedType, photo, nextPage);
            if (!isContainStoryItem(mStoryItemList, storyItemUnit)) {
                mAlwaysDBProvider.addNewStoryItem(storyItemUnit);
                mStoryItemList.add(storyItemUnit);
                storyItemUnitList.add(storyItemUnit);
            }
        }
    }

    private boolean isContainStoryItem(ArrayList<StoryItemUnit> storyItemList, StoryItemUnit storyItemUnit) {
        for (StoryItemUnit _storyItemUnit : storyItemList) {
            if (_storyItemUnit.getSocialFeedId().equals(storyItemUnit.getSocialFeedId()) && _storyItemUnit.getFeeds_type().equals(storyItemUnit.getFeeds_type()))
                return true;
        }
        return false;
    }

    private boolean isContainTWStoryItem(ArrayList<StoryItemUnit> storyItemList, StoryItemUnit storyItemUnit) {
        for (StoryItemUnit _storyItemUnit : storyItemList) {
            if (_storyItemUnit.getSocialFeedId().equals(storyItemUnit.getSocialFeedId()))
                return true;
        }
        return false;
    }

    private void resetDataWhenUserLogOut() {
        this.mStoryItemList.clear();
    }

    //    private CountDownTimer timer = new CountDownTimer(30000,30000) {
    //        
    //        @Override
    //        public void onTick(long millisUntilFinished) {
    //            Log.e(TAG, "service auto update");
    //            AlwaysSocialManager.getInstance().requestSNS(GlobalConstant.SOCIAL_TYPE_FACEBOOK, FacebookConstant.FB_DATA_TYPE_FEED,null, AlwaysService.this);
    //            AlwaysSocialManager.getInstance().requestSNS(GlobalConstant.SOCIAL_TYPE_TWITTER, TwitterConstant.TW_DATA_TYPE_TIMELINE,null, AlwaysService.this);
    //        }
    //        
    //        @Override
    //        public void onFinish() {
    //            timer.start();
    //        }
    //    };
}
