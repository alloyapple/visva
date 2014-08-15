package com.samsung.android.alwayssocial.servermanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import twitter4j.FilterQuery;
import twitter4j.PagableResponseList;
import twitter4j.Paging;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.RateLimitStatus;
import twitter4j.ResponseList;
import twitter4j.SavedSearch;
import twitter4j.StallWarning;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.StatusUpdate;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.User;
import twitter4j.UserList;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;
import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.samsung.android.alwayssocial.app.AlwaysSocialAppImpl;
import com.samsung.android.alwayssocial.constant.GlobalConstant;
import com.samsung.android.alwayssocial.constant.TwitterConstant;
import com.samsung.android.alwayssocial.object.ResponseData;
import com.samsung.android.alwayssocial.object.facebook.FacebookFeeds;
import com.samsung.android.alwayssocial.object.twitter.TwitterFollowersData;
import com.samsung.android.alwayssocial.object.twitter.TwitterListData;
import com.samsung.android.alwayssocial.object.twitter.TwitterListFollowData;
import com.samsung.android.alwayssocial.object.twitter.TwitterSavedSearchesData;
import com.samsung.android.alwayssocial.object.twitter.TwitterStatusData;
import com.samsung.android.alwayssocial.object.twitter.TwitterTimeLineData;
import com.samsung.android.alwayssocial.object.twitter.TwitterUserData;
import com.samsung.android.alwayssocial.service.IResponseFromSNSCallback;

public class Twitter extends AbstractSocial {
    private static final String TAG = "Twitter";
    IResponseFromSNSCallback mCallbackToServie = null;

    public Twitter()
    {
        super(GlobalConstant.SOCIAL_TYPE_TWITTER);
    }

    public Twitter(IRequestSNS listener) {
        super(GlobalConstant.SOCIAL_TYPE_TWITTER, listener);
    }

    @Override
    public void requestSNS(int requestType, HashMap<String, Object> param, IResponseFromSNSCallback callback) {
        mCallbackToServie = callback;
        if (requestType == TwitterConstant.TW_DATA_TYPE_COMMENT) {
            Log.d(TAG, "Twitter request for comments");
            if (param != null) {
                TwitterSearchTask findCommentForTweet = new TwitterSearchTask(TwitterConstant.TW_DATA_TYPE_COMMENT, param);
                findCommentForTweet.execute();
            }
        } else if (requestType == TwitterConstant.TW_DATA_VIEW_SEARCH_ITEM) {
            Log.d(TAG, "Twitter request view search item");
            if (param != null) {
                TwitterSearchTask viewSearchItem = new TwitterSearchTask(TwitterConstant.TW_DATA_VIEW_SEARCH_ITEM, param);
                viewSearchItem.execute();
            }
        }
        else {
            //        Log.d(TAG, "requestSNS requestType = " + requestType + "userName = " + AlwaysSocialAppImpl.getInstance().getLoggedInUser(GlobalConstant.SOCIAL_TYPE_TWITTER).mName);
            TwitterRequest request = new TwitterRequest(requestType, param);
            request.executeStart();
        }

    }

    @Override
    public void postSNS(String id, String data, int postType, IResponseFromSNSCallback callback) {
        Log.d(TAG, "postSNS requestType = " + postType + " id = " + id + "  data = ");
        mCallbackToServie = callback;
        //        TwitterRequest request = new TwitterRequest(postType, Long.parseLong(id));
        TwitterRequest request;
        switch (postType) {
        case TwitterConstant.TW_POST_REPLY:
            request = new TwitterRequest(postType, data, Long.parseLong(id));
            break;
        case TwitterConstant.TW_POST_DESTROY_FRIENDSHIP:
            request = new TwitterRequest(postType, id);
            break;
        default:
            request = new TwitterRequest(postType, Long.parseLong(id));
            break;
        }
        request.executeStart();
    }

    @Override
    public void getFacebookImagesInformation(int requestDataType, FacebookFeeds feeds, IResponseFromSNSCallback callback) {

    }

    @Override
    public void setRequestDataListener(IRequestSNS listener) {
        mResponseToManager = listener;
    }

    @Override
    public void callResponseCallback(int requestType, ResponseData responseFacebook) {
        if (responseFacebook != null)
            mResponseToManager.onResponse(GlobalConstant.SOCIAL_TYPE_TWITTER, requestType, responseFacebook, mCallbackToServie);
    }

    @Override
    public void callErrorResponseCallback(int error) {
        mResponseToManager.onErrorResponse(GlobalConstant.SOCIAL_TYPE_TWITTER, error, mCallbackToServie);
    }

    class TwitterSearchTask extends AsyncTask<Void, Integer, Void> {
        private int mType;
        private List<twitter4j.Status> mListStatus;
        private String mStatusId="";
        private String mUserName="";
        private String mSinceMaxId="";
        private boolean mIsRefresh; // this request is refresh or loadmore

        public TwitterSearchTask() {
            mListStatus = new ArrayList<twitter4j.Status>();
        }

        public TwitterSearchTask(int type, HashMap<String, Object> param) {
            this.mType = type;
            switch (type) {
            case TwitterConstant.TW_DATA_TYPE_COMMENT:
                this.mStatusId = (String) param.get(TwitterConstant.TW_KEY_COMMENT_ID);
                this.mUserName = (String) param.get(TwitterConstant.TW_KEY_STATUS_USER_NAME);
                break;
            case TwitterConstant.TW_DATA_VIEW_SEARCH_ITEM:
                this.mStatusId = (String) param.get(TwitterConstant.TW_KEY_VIEW_SEARCH_ITEM);
                this.mUserName = (String) param.get(TwitterConstant.TW_KEY_STATUS_USER_NAME);
                this.mSinceMaxId = (String) param.get(TwitterConstant.TW_KEY_SINCE_MAX_ID);
                if (param.containsKey(GlobalConstant.TYPE_DATA_UPDATE)){
                    this.mIsRefresh = (Boolean) param.get(GlobalConstant.TYPE_DATA_UPDATE);
                }
            default:
                break;
            }
            this.mListStatus = new ArrayList<twitter4j.Status>();

        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            Log.d(TAG, "onPreExecute searching, id = " + mStatusId + "  userName = " + mUserName);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            ConfigurationBuilder builder = new ConfigurationBuilder();
            builder.setOAuthConsumerKey(TwitterConstant.TWITTER_CONSUMER_KEY);
            builder.setOAuthConsumerSecret(TwitterConstant.TWITTER_CONSUMER_SECRET);
            builder.setUseSSL(true);
            String access_token = AlwaysSocialAppImpl.getInstance().getAlwaysSharePrefs().getStringValue(TwitterConstant.PREF_KEY_TWITTER_OAUTH_TOKEN);
            String access_token_secret = AlwaysSocialAppImpl.getInstance().getAlwaysSharePrefs().getStringValue(TwitterConstant.PREF_KEY_TWITTER_OAUTH_SECRET);

            AccessToken accessToken = new AccessToken(access_token, access_token_secret);

            twitter4j.Twitter twitter = new TwitterFactory(builder.build()).getInstance(accessToken);

            try {
                twitter.verifyCredentials();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Query query;
            QueryResult result;
            switch (mType) {
            case TwitterConstant.TW_DATA_TYPE_COMMENT:
                try {
                    query = new Query("to:" + mUserName);
                    query.setSinceId(Long.parseLong(mStatusId));
                    do {
                        result = twitter.search(query);
                        List<twitter4j.Status> tweets = result.getTweets();
                        for (twitter4j.Status status : tweets) {
                            Log.d(TAG, " search statuses, name = " + status.getUser().getName() + "   text =  " + status.getText() + "   replyInstatusId = " + status.getInReplyToStatusId());
                            if (Long.toString(status.getInReplyToStatusId()).equals(mStatusId)) {
                                mListStatus.add(status);
                            }
                        }
                    } while ((query = result.nextQuery()) != null);
                    Log.d(TAG, "there are " + mListStatus.size() + " comments");
                } catch (TwitterException e) {
                    Log.d(TAG, "exception occurs " + e.toString());
                    e.printStackTrace();
                }
                break;
            case TwitterConstant.TW_DATA_VIEW_SEARCH_ITEM:
                try {
                    query = new Query(mStatusId);
                    if (!mSinceMaxId.equals("")) {
                        if (mIsRefresh) {
                            query.setSinceId(Long.parseLong(mSinceMaxId));
                        } else {
                            query.setMaxId(Long.parseLong(mSinceMaxId));
                        }
                    }
                    result = twitter.search(query);
                    mListStatus = result.getTweets();
                } catch (TwitterException e) {
                    callErrorResponseCallback(mType);
                    e.printStackTrace();
                }
            default:
                break;

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.d(TAG, "searchTask onPostExecute");
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            ResponseData responseTwitterData = new ResponseData(mType);
            if (mType == TwitterConstant.TW_DATA_VIEW_SEARCH_ITEM) {
                responseTwitterData.isMeRequest = false;
            }
            if (mListStatus != null) {
                TwitterTimeLineData twitterData = new TwitterTimeLineData(mListStatus);
                responseTwitterData.responseData = twitterData;
            }
            if (mSinceMaxId.equals("")) {
                responseTwitterData.isFirstTimeUpdate = true;
            } else {
                responseTwitterData.isFirstTimeUpdate = false;
            }
            if (mIsRefresh) {
                responseTwitterData.isRequestUpdateRefresh = true;
            }
            else {
                responseTwitterData.isRequestUpdateRefresh = false;
            }
            callResponseCallback(mType, responseTwitterData);
        }

    }

    /*maybe use later*/
    class TwitterStreamTask extends AsyncTask<Void, Integer, String> {
        private String mStatusId;

        public TwitterStreamTask(String statusId) {
            this.mStatusId = statusId;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub
            ConfigurationBuilder builder = new ConfigurationBuilder();
            builder.setOAuthConsumerKey(TwitterConstant.TWITTER_CONSUMER_KEY);
            builder.setOAuthConsumerSecret(TwitterConstant.TWITTER_CONSUMER_SECRET);

            String access_token = AlwaysSocialAppImpl.getInstance().getAlwaysSharePrefs().getStringValue(TwitterConstant.PREF_KEY_TWITTER_OAUTH_TOKEN);
            String access_token_secret = AlwaysSocialAppImpl.getInstance().getAlwaysSharePrefs().getStringValue(TwitterConstant.PREF_KEY_TWITTER_OAUTH_SECRET);

            AccessToken accessToken = new AccessToken(access_token, access_token_secret);

            StatusListener statusListener = new StatusListener() {

                @Override
                public void onException(Exception arg0) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onTrackLimitationNotice(int arg0) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onStatus(twitter4j.Status status) {
                    // TODO Auto-generated method stub
                    Log.d(TAG, "onStatus, name = " + status.getUser().getScreenName() + "   text = " + status.getText() + " inReplyToStatusId = " + status.getInReplyToStatusId() + "  mStatusId = " + mStatusId);
                    if (Long.toString(status.getInReplyToStatusId()).equals(mStatusId)) {
                        Log.d(TAG, "onStatus, this is the comment");
                    }

                }

                @Override
                public void onStallWarning(StallWarning arg0) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onScrubGeo(long arg0, long arg1) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onDeletionNotice(StatusDeletionNotice arg0) {
                    // TODO Auto-generated method stub

                }
            };

            twitter4j.Twitter twitter = new TwitterFactory(builder.build()).getInstance(accessToken);//AlwaysSocialAppImpl.getTwitterInstance();
            User user = null;
            try {
                user = twitter.verifyCredentials();
            } catch (Exception e) {
                e.printStackTrace();
            }

            ConfigurationBuilder streamBuilder = new ConfigurationBuilder();
            streamBuilder.setOAuthConsumerKey(TwitterConstant.TWITTER_CONSUMER_KEY);
            streamBuilder.setOAuthConsumerSecret(TwitterConstant.TWITTER_CONSUMER_SECRET);
            TwitterStream twitterStream = new TwitterStreamFactory(streamBuilder.build()).getInstance(accessToken);
            twitterStream.addListener(statusListener);
            Log.d(TAG, "Twitter stream is running");
            long[] Ids = { user.getId() };
            twitterStream.filter(new FilterQuery(Ids));
            try {
                Thread.sleep(15000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            //            Log.d(TAG, "Twitter stream is cleaned and stopped");
            //            twitterStream.cleanUp();
            //            twitterStream.shutdown();
            return null;
        }
    }

    class TwitterRequest extends AsyncTask<Void, Void, String> {
        List<twitter4j.Status> mLstStatuses;
        List<SavedSearch> mLstSavedSearches;
        ResponseList<UserList> mlstUserList;
        ResponseList<twitter4j.Status> mListstatuses;
        PagableResponseList<UserList> mlstUserListFollow;
        //        PagableResponseList<UserList> mlstListFollowMe;
        List<User> mFollowers = new ArrayList<User>();
        User mPostUser; //for destroy friendship
        User user = null;
        twitter4j.Status mStatus;
        StatusUpdate mStatusUpdate;
        String mData; // data express reply message/ comment
        String mUserId, mUserName; //params for viewing usertimeline 
        int mRequestType = -1;
        long mId = -1; // get Id at function of Twitter Class such as :postSNS, requestSNS....
        String mTweetId = "";
        boolean mIsRefresh;
        HashMap<String, Object> mParams = new HashMap<String, Object>();

        public TwitterRequest(int requestType, HashMap<String, Object> params) {
            mRequestType = requestType;
            mParams = params;
            if (params != null && params.containsKey(GlobalConstant.TYPE_DATA_UPDATE)) {
                mIsRefresh = (Boolean) params.get(GlobalConstant.TYPE_DATA_UPDATE);
            }
            if (params != null && params.containsKey(TwitterConstant.TW_KEY_SINCE_MAX_ID)) {
                mTweetId = (String) mParams.get(TwitterConstant.TW_KEY_SINCE_MAX_ID);
            } else {
                mTweetId = "";
            }
            if (requestType == TwitterConstant.TW_DATA_VIEW_USER_TIMELINE) {
                mUserId = (String) params.get(TwitterConstant.TW_KEY_STATUS_USER_ID);
                mUserName = (String) params.get(TwitterConstant.TW_KEY_STATUS_USER_NAME);
            } else {
                mUserId = "";
                mUserName = "";
            }

            if (requestType == TwitterConstant.TW_DATA_TYPE_LIST_TIMELINE) {
                String listId;
                listId = (String) mParams.get(TwitterConstant.TW_KEY_LIST_ID);
                if (listId == null)
                    return;
                Log.d(TAG, " TwitterConstant.TW_DATA_TYPE_LIST_TIMELINE 1  of list id = " + listId);
                mId = new Long(listId);
                Log.d(TAG, " TwitterConstant.TW_DATA_TYPE_LIST_TIMELINE 1  of list id = " + mId);
            }
        }

        public TwitterRequest(int requestType, long id) {
            mRequestType = requestType;
            mId = id;
            mTweetId = "";
        }
        
        public TwitterRequest(int requestType, String userName) {
            this.mRequestType = requestType;
            mUserName = userName;
            mTweetId = "";
        }

        // for post comment and reply
        public TwitterRequest(int requestType, String data, long id) {
            this.mData = data;
            this.mRequestType = requestType;
            this.mId = id;
        }

        public AsyncTask<Void, Void, String> executeStart() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                return executeExternStart();
            } else {
                return super.execute();
            }
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        private AsyncTask<Void, Void, String> executeExternStart() {
            return super.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                //                twitter4j.Twitter twitter = AlwaysSocialAppImpl.getTwitterInstance();
                ConfigurationBuilder builder = new ConfigurationBuilder();
                builder.setOAuthConsumerKey(TwitterConstant.TWITTER_CONSUMER_KEY);
                builder.setOAuthConsumerSecret(TwitterConstant.TWITTER_CONSUMER_SECRET);

                String access_token = AlwaysSocialAppImpl.getInstance().getAlwaysSharePrefs().getStringValue(TwitterConstant.PREF_KEY_TWITTER_OAUTH_TOKEN);
                String access_token_secret = AlwaysSocialAppImpl.getInstance().getAlwaysSharePrefs().getStringValue(TwitterConstant.PREF_KEY_TWITTER_OAUTH_SECRET);

                AccessToken accessToken = new AccessToken(access_token, access_token_secret);
                twitter4j.Twitter twitter = new TwitterFactory(builder.build()).getInstance(accessToken);//AlwaysSocialAppImpl.getTwitterInstance();
                User user = null;
                try {
                    user = twitter.verifyCredentials();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                PagableResponseList<User> followers;
                RateLimitStatus rateLimit = twitter.getRateLimitStatus("application").get("/application/rate_limit_status");
                Log.d(TAG, "Application Remaining RateLimit =" + rateLimit.getRemaining());
                int limitRate = rateLimit.getRemaining();
                if (limitRate >= 20) {
                    /**test limit rate*/
                    if (user != null) {
                        switch (mRequestType) {
                        case TwitterConstant.TW_DATA_TYPE_TIMELINE:
                            Log.d(TAG, " getting timeLine of user " + user.getScreenName());
                            if (mTweetId.equals("")) {
                                mLstStatuses = twitter.getHomeTimeline();
                            } else {
                                Paging page;
                                if (!mIsRefresh) { //loadmore
                                    page = new Paging();
                                    page.setMaxId(Long.parseLong(mTweetId));
                                } else { //refresh
                                    page = new Paging();
                                    page.setSinceId(Long.parseLong(mTweetId));
                                }
                                mLstStatuses = twitter.getHomeTimeline(page);
                            }
                            break;
                        case TwitterConstant.TW_DATA_TYPE_TIMELINE_LINKS:
                            Log.d(TAG, "getting timeLine Links of user " + user.getScreenName());
                            if (mTweetId.equals("")) {
                                mLstStatuses = twitter.getHomeTimeline();
                            } else {
                                Paging page;
                                if (!mIsRefresh) { //loadmore
                                    page = new Paging();
                                    page.setMaxId(Long.parseLong(mTweetId));
                                } else { //refresh
                                    page = new Paging();
                                    page.setSinceId(Long.parseLong(mTweetId));
                                }
                                mLstStatuses = twitter.getHomeTimeline(page);
                            }
                            Log.d(TAG, " size before = " + mLstStatuses.size());
                            for (int i = 0; i < mLstStatuses.size(); i++) {
                                if (mLstStatuses.get(i).getURLEntities().length == 0) {
                                    mLstStatuses.remove(i);
                                }
                            }
                            break;
                        case TwitterConstant.TW_DATA_TYPE_TWEETS:
                            Log.d(TAG, "getting myTweets");
                            if (mTweetId.equals("")) {
                                mLstStatuses = twitter.getUserTimeline(user.getName());
                            } else {
                                Paging page;
                                if (!mIsRefresh) { //loadmore
                                    page = new Paging();
                                    page.setMaxId(Long.parseLong(mTweetId));
                                } else { //refresh
                                    page = new Paging();
                                    page.setSinceId(Long.parseLong(mTweetId));

                                }
                                mLstStatuses = twitter.getUserTimeline(user.getName(), page);
                            }
                            //getRetweetedByMe() function is retired from 3.0.x

                            break;
                        case TwitterConstant.TW_DATA_TYPE_FAVORITES:
                            Log.d(TAG, "getting my Favorite Tweets");
                            if (mTweetId.equals("")) {
                                mLstStatuses = twitter.getFavorites();
                            } else {
                                Paging page;
                                if (!mIsRefresh) { //loadmore
                                    page = new Paging();
                                    page.setMaxId(Long.parseLong(mTweetId));
                                } else { //refresh
                                    page = new Paging();
                                    page.setSinceId(Long.parseLong(mTweetId));
                                }
                                mLstStatuses = twitter.getFavorites(page);
                            }
                            break;
                        case TwitterConstant.TW_DATA_TYPE_MENTIONING_TWEETS:
                            Log.d(TAG, "getting tweets that mentioned me");
                            if (mTweetId.equals("")) {
                                mLstStatuses = twitter.getMentionsTimeline();
                            } else {
                                Paging page;
                                if (!mIsRefresh) { //loadmore
                                    page = new Paging();
                                    page.setMaxId(Long.parseLong(mTweetId));
                                } else { //refresh
                                    page = new Paging();
                                    page.setSinceId(Long.parseLong(mTweetId));

                                }
                                mLstStatuses = twitter.getMentionsTimeline(page);
                            }
                            break;
                        case TwitterConstant.TW_DATA_TYPE_SAVED_SEARCHES:
                            Log.d(TAG, "getting savedSearches");
                            mLstSavedSearches = twitter.getSavedSearches();
                            break;
                        case TwitterConstant.TW_DATA_TYPE_LIST:
                            Log.d(TAG, "getting your list");
                            mlstUserList = twitter.getUserLists(user.getId());
                            break;
                        case TwitterConstant.TW_DATA_TYPE_LIST_FOLLOWED:
                            Log.d(TAG, "getting List you followed");
                            mlstUserListFollow = twitter.getUserListSubscriptions(user.getScreenName(), -1);
                            break;
                        case TwitterConstant.TW_DATA_TYPE_LIST_FOLLOWING_ME:
                            Log.d(TAG, "getting List followed me");
                            // temporary fist page
                            mlstUserListFollow = twitter.getUserListMemberships(user.getScreenName(), -1);
                            break;
                        case TwitterConstant.TW_DATA_TYPE_PEOPLE_FOLLOWED:
                            Log.e(TAG, "getting list of followers of user " + user.getScreenName());
                            followers = twitter.getFriendsList(user.getScreenName(), -1);
                            for (User follower : followers) {
                                mFollowers.add(follower);
                            }
                            break;
                        case TwitterConstant.TW_DATA_VIEW_USER_TIMELINE:
                            Log.d(TAG, "getting timeline for user " + mUserId);
                            if (mTweetId.equals("")) {
                                mLstStatuses = twitter.getUserTimeline(Long.parseLong(mUserId));

                            } else {
                                Paging page;
                                if (!mIsRefresh) { //loadmore
                                    page = new Paging();
                                    page.setMaxId(Long.parseLong(mTweetId));
                                } else { //refresh
                                    page = new Paging();
                                    page.setSinceId(Long.parseLong(mTweetId));
                                }
                                mLstStatuses = twitter.getUserTimeline(Long.parseLong(mUserId), page);
                            }
                            Log.d(TAG, "  mLstStatuses.size() " + mLstStatuses.size());
                            break;
                        case TwitterConstant.TW_POST_CREATE_FAVOURITE:
                            Log.d(TAG, "create favourite for id = " + mId);
                            try {
                                mStatus = twitter.createFavorite(mId);
                            } catch (TwitterException te) {
                                te.printStackTrace();
                            }
                            break;
                        case TwitterConstant.TW_POST_DESTROY_FAVOURITE:
                            Log.d(TAG, "destroy favourite id = " + mId);
                            try {
                                mStatus = twitter.destroyFavorite(mId);
                            } catch (TwitterException te) {
                                te.printStackTrace();
                            }
                            break;
                        case TwitterConstant.TW_POST_RETWEET:
                            Log.d(TAG, "retweet id = " + mId);
                            try {
                                mStatus = twitter.retweetStatus(mId);
                            } catch (TwitterException e) {
                                e.printStackTrace();
                            }
                            break;
                        case TwitterConstant.TW_POST_DESTROY_FRIENDSHIP:
                            Log.d(TAG, "destroy friendship, user = " + mUserName);
                            try{
                               mPostUser = twitter.destroyFriendship(mUserName); 
                            } catch (TwitterException e) {
                                e.printStackTrace();
                            }
                            break;
                        case TwitterConstant.TW_DATA_TYPE_PEOPLE_FOLLOWING_ME:
                            Log.d(TAG, "getting list of followers of user " + user.getScreenName());
                            followers = twitter.getFollowersList(user.getScreenName(), -1);
                            for (User follower : followers) {
                                mFollowers.add(follower);
                            }
                            break;
                        // implementing    
                        case TwitterConstant.TW_POST_REPLY:
                            Log.d(TAG, " TwitterConstant.TW_POST_REPLY of tweet id = " + mId);
                            mStatusUpdate = new StatusUpdate(mData);
                            mStatusUpdate.inReplyToStatusId(mId);
                            twitter.updateStatus(mStatusUpdate);
                            break;
                  
                        case TwitterConstant.TW_DATA_TYPE_LIST_TIMELINE:
                            Log.d(TAG, " TwitterConstant.TW_DATA_TYPE_LIST_TIMELINE of list id = " + mId);
                            mListstatuses = twitter.getUserListStatuses((int) mId, new Paging(1));
                            Log.d(TAG, " TwitterConstant.TW_DATA_TYPE_LIST_TIMELINE of mLstStatuses  = " + mListstatuses.size());
                            break;
                        default:
                            break;
                        }
                        return null;
                    } else
                        return GlobalConstant.ERROR_SNS_REPONSE;
                } else
                    return GlobalConstant.ERROR_SNS_REPONSE;
            } catch (TwitterException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.e(TAG, "get timeLine Finish, return the callback here " + mRequestType);

            if (mRequestType == TwitterConstant.TW_POST_CREATE_FAVOURITE || mRequestType == TwitterConstant.TW_POST_DESTROY_FAVOURITE) {
                return;
            }

            if (result != null || GlobalConstant.ERROR_SNS_REPONSE.equals(result)) {
                callErrorResponseCallback(mRequestType);
            }

            ResponseData responseTwitterData = new ResponseData(mRequestType);

            if (mRequestType == TwitterConstant.TW_DATA_TYPE_TIMELINE || mRequestType == TwitterConstant.TW_DATA_TYPE_TIMELINE_LINKS || mRequestType == TwitterConstant.TW_DATA_TYPE_TWEETS || mRequestType == TwitterConstant.TW_DATA_TYPE_FAVORITES
                    || mRequestType == TwitterConstant.TW_DATA_TYPE_MENTIONING_TWEETS || mRequestType == TwitterConstant.TW_DATA_VIEW_USER_TIMELINE) {
                if (mLstStatuses != null && mLstStatuses.size() > 0) {
                    TwitterTimeLineData twitterData = new TwitterTimeLineData(mLstStatuses);
                    responseTwitterData.responseData = twitterData;
                    Log.d(TAG, " TwitterConstant.TW_DATA_VIEW_USER_TIMELINE of responseTwitterData.responseData  = " + mLstStatuses.size());
                }
            } else if (mRequestType == TwitterConstant.TW_DATA_TYPE_SAVED_SEARCHES) {
                TwitterSavedSearchesData twitterDataSaved = new TwitterSavedSearchesData(mLstSavedSearches);
                responseTwitterData.responseData = twitterDataSaved;
            } else if (mRequestType == TwitterConstant.TW_DATA_TYPE_LIST) {
                TwitterListData dataList = new TwitterListData(mlstUserList);
                responseTwitterData.responseData = dataList;
            } else if (mRequestType == TwitterConstant.TW_DATA_TYPE_LIST_FOLLOWED || mRequestType == TwitterConstant.TW_DATA_TYPE_LIST_FOLLOWING_ME) {
                TwitterListFollowData followData = new TwitterListFollowData(mlstUserListFollow);
                responseTwitterData.responseData = followData;
            } else if (mRequestType == TwitterConstant.TW_DATA_TYPE_PEOPLE_FOLLOWED || mRequestType == TwitterConstant.TW_DATA_TYPE_PEOPLE_FOLLOWING_ME) {
                TwitterFollowersData twitterFollowers = new TwitterFollowersData(mFollowers);
                responseTwitterData.responseData = twitterFollowers;
            } else if (mRequestType == TwitterConstant.TW_POST_RETWEET) {
                TwitterStatusData status = new TwitterStatusData(mStatus);
                responseTwitterData.responseData = status;
            } else if (mRequestType == TwitterConstant.TW_POST_DESTROY_FRIENDSHIP) {
                TwitterUserData user = new TwitterUserData(mPostUser);
                responseTwitterData.responseData = user;
            } else if (mRequestType == TwitterConstant.TW_DATA_TYPE_LIST_TIMELINE) {
                if (mListstatuses != null && mListstatuses.size() > 0) {
                    TwitterTimeLineData twitterData = new TwitterTimeLineData(mListstatuses);
                    responseTwitterData.responseData = twitterData;
                    Log.d(TAG, " TwitterConstant.TW_DATA_TYPE_LIST_TIMELINE of responseTwitterData.responseData  = " + mListstatuses.size());
                }
            }

            if (mIsRefresh) {
                responseTwitterData.isRequestUpdateRefresh = true;
            }
            else {
                responseTwitterData.isRequestUpdateRefresh = false;
            }
            if (mParams != null && ((mParams.containsKey(TwitterConstant.TW_KEY_STATUS_USER_ID) && !((String) mParams.get(TwitterConstant.TW_KEY_STATUS_USER_ID)).equals("")) ||
                    (mParams.containsKey(TwitterConstant.TW_KEY_LIST_ID) && !((String) mParams.get(TwitterConstant.TW_KEY_LIST_ID)).equals("")))) {
                responseTwitterData.isMeRequest = false;
            }
            else {
                responseTwitterData.isMeRequest = true;
            }
            Log.d(TAG, "responseTwitterData 1 " + responseTwitterData + " mRequestType " + mRequestType);
            responseTwitterData.isFirstTimeUpdate = mTweetId.equals("") ? true : false;
            callResponseCallback(mRequestType, responseTwitterData);
            return;
        }

    }
}
