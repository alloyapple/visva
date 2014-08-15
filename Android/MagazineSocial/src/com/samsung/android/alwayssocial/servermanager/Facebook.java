package com.samsung.android.alwayssocial.servermanager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Request.GraphUserListCallback;
import com.facebook.RequestBatch;
import com.facebook.RequestBatch.Callback;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphUser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.samsung.android.alwayssocial.constant.FacebookConstant;
import com.samsung.android.alwayssocial.constant.GlobalConstant;
import com.samsung.android.alwayssocial.object.ResponseData;
import com.samsung.android.alwayssocial.object.facebook.FacebookCommentList;
import com.samsung.android.alwayssocial.object.facebook.FacebookFeedWrapper;
import com.samsung.android.alwayssocial.object.facebook.FacebookFeedWrapper.TypeTimeline;
import com.samsung.android.alwayssocial.object.facebook.FacebookFeeds;
import com.samsung.android.alwayssocial.object.facebook.FacebookFriends;
import com.samsung.android.alwayssocial.object.facebook.FacebookPages;
import com.samsung.android.alwayssocial.object.facebook.FacebookPaging;
import com.samsung.android.alwayssocial.object.facebook.FacebookPhoto;
import com.samsung.android.alwayssocial.object.facebook.FacebookPhotos;
import com.samsung.android.alwayssocial.object.facebook.FacebookSummary;
import com.samsung.android.alwayssocial.object.facebook.FacebookUser;
import com.samsung.android.alwayssocial.service.IResponseFromSNSCallback;

public class Facebook extends AbstractSocial {

    public static final String TAG = "Facebook";
    public static final int LOAD_MORE_COMMAND = 0;
    private boolean mIsStateOpen = false;
    private Gson mGson;
    IResponseFromSNSCallback mCallbackToServie = null;

    public Facebook() {
        super(GlobalConstant.SOCIAL_TYPE_FACEBOOK);
    }

    public Facebook(IRequestSNS listener) {
        super(GlobalConstant.SOCIAL_TYPE_FACEBOOK, listener);
    }

    public void setRequestDataListener(IRequestSNS listener) {
        mResponseToManager = listener;
    }

    public boolean isSessionOpened() {
        return mIsStateOpen;
    }

    public boolean ensureOpenSession(Activity activity) {
        if (Session.getActiveSession() == null || !Session.getActiveSession().isOpened()) {
            Session.openActiveSession(activity, true, new Session.StatusCallback() {
                @Override
                public void call(Session session, SessionState state, Exception exception) {
                    mIsStateOpen = state.isOpened();
                }
            });
            return false;
        }
        return true;
    }

    @Override
    public void requestSNS(int requestType, HashMap<String, Object> param, IResponseFromSNSCallback callback) {
        mCallbackToServie = callback;
        switch (requestType) {
        case FacebookConstant.FB_DATA_TYPE_FEED:
            getFeedList(param);
            break;
        case FacebookConstant.FB_DATA_TYPE_FEED_PHOTOS:
            getFeedPhotos(param);
            break;
        case FacebookConstant.FB_DATA_TYPE_FEED_LINKS:
            getLinksOnly(param);
            break;
        case FacebookConstant.FB_DATA_TYPE_TIMELINE:
            getTimeline(param);
            break;
        case FacebookConstant.FB_DATA_TYPE_TAGGEDME:
            getTaggedMe(param);
            break;
        case FacebookConstant.FB_DATA_TYPE_GROUPS:
            getGroups(param);
            break;
        case FacebookConstant.FB_DATA_TYPE_PAGES:
            getLikedPages(param);
            break;
        case FacebookConstant.FB_DATA_TYPE_FRIENDS:
            getFriendList();
            break;
        case FacebookConstant.FB_DATA_TYPE_FRIEND_GROUPS:
            getFriendsGroup();
            break;
        case FacebookConstant.FB_DATA_TYPE_LIKEINFO:
            getLikeInfo(param);
            break;
        case FacebookConstant.FB_DATA_TYPE_COMMENTINFO:
            // Comment data
            break;
        case FacebookConstant.FB_DATA_TYPE_USER:
            getUserData();
            break;
        case FacebookConstant.FB_DATA_TYPE_PAGE_TIMELINE:
            getFeedPages(param);
            break;
        case FacebookConstant.FB_DATA_TYPE_FEED_ITEM:
            getSingleFeed(param);
            break;
        case FacebookConstant.FB_DATA_TYPE_PHOTO_ITEM:
            getImageItem(param);
            break;
        case FacebookConstant.FB_DATA_MORE_COMMENT:
            getMoreComment(param);
            break;
        // implement group friends 
        case FacebookConstant.FB_DATA_MEMBERS_OF_GROUP:
            getMembersOfGroup(param);
            break;

        default:
            break;
        }
    }

    @Override
    public void postSNS(String id, String data, int postType, IResponseFromSNSCallback callback) {
        mCallbackToServie = callback;
        switch (postType) {
        case FacebookConstant.FB_POST_TYPE_COMMENT:
            postComment(data, id);
            break;
        case FacebookConstant.FB_POST_TYPE_LIKE:
            postLike(id);
        default:
            break;
        }
    }

    public void getMyFeedList() {
        getFeedList(FacebookConstant.FB_GET_MYNEWSFEED);
    }

    public void getFeedList(String userId) {
        //        getFeedList(null);
    }

    private void getFeedList(final HashMap<String, Object> param) {
        Log.d(TAG, "getFeedList");
        Session activeSession = Session.getActiveSession();
        String graphPath = FacebookConstant.FB_GET_MYNEWSFEED;
        if (activeSession != null && activeSession.getState().isOpened()) {
            Bundle bundle = new Bundle();
            if (param != null) {
                if (param.containsKey("userId")) {
                    graphPath = param.get("userId").toString() + "/home";
                }
                if (param.containsKey("until")) {
                    graphPath = (String) param.get("until");
                    FacebookHttpAsync request = new FacebookHttpAsync(LOAD_MORE_COMMAND, FacebookConstant.FB_DATA_TYPE_FEED, graphPath, param);
                    request.execute();
                    return;
                }
                if (param.containsKey("limit")) {
                    bundle.putInt("limit", (Integer) param.get("limit"));
                }
            }
            // Only request some useful parametters
            if (param != null && !param.containsKey("until")) {
                bundle.putString(FacebookConstant.FB_REQUEST_PARAM_FIELDS_CONSTANT, FacebookConstant.FB_REQUEST_FEED_FIELDS);
            }

            Request feedRequest = Request.newGraphPathRequest(activeSession, graphPath, new Request.Callback() {

                @Override
                public void onCompleted(Response response) {
                    // TODO Auto-generated method stub
                    if (response.getError() != null) {
                        Log.d(TAG, "getFeedList()->onCompleted() errorCode = " + response.getError().getErrorCode());
                        callErrorResponseCallback(response.getError().getErrorCode());
                    } else {
                        Log.d(TAG, "getFeedList()->onCompleted() Successfully");
                        //Utils.LogExt(TAG, "getFeedList()->onCompleted() response = " + response.toString());
                        GraphObject graphObject = response.getGraphObject();
                        JSONObject jsonObject = graphObject.getInnerJSONObject();
                        Gson gson = new GsonBuilder().serializeNulls().create();
                        FacebookFeeds newsFeed = gson.fromJson(jsonObject.toString(), FacebookFeeds.class);

                        ArrayList<FacebookFeedWrapper> tmpList = new ArrayList<FacebookFeedWrapper>();
                        tmpList.addAll(newsFeed.getData());
                        for (FacebookFeedWrapper feedItem : tmpList) {
                            // Analyze timelint data by story
                            String story = feedItem.story;
                            String type = feedItem.type;
                            //Log.d(TAG, "item story = " + story + ", type = " + type + ", message = " + feedItem.getMessage());
                            if (type.compareTo("status") == 0 && story != null) {
                                newsFeed.getData().remove(feedItem);
                                continue;
                            }
                            // Convert image url
                            String picture = feedItem.getPicture();
                            if (picture != null && picture.contains("_s.")) {
                                int indexOfSize = picture.lastIndexOf("s");
                                feedItem.setPicture(picture.substring(0, indexOfSize) + "n" + picture.substring(indexOfSize + 1, picture.length()));
                            }
                        }

                        ResponseData responseFacebook = new ResponseData(FacebookConstant.FB_DATA_TYPE_FEED);
                        responseFacebook.responseData = newsFeed;

                        /*set type of refresh*/
                        if (param != null && param.containsKey(GlobalConstant.TYPE_DATA_UPDATE)) {
                            boolean isRefresh = (Boolean) param.get(GlobalConstant.TYPE_DATA_UPDATE);
                            if (isRefresh)
                                responseFacebook.isRequestUpdateRefresh = true;
                            else
                                responseFacebook.isRequestUpdateRefresh = false;
                        }
                        if (param != null && param.containsKey("userId")) {
                            responseFacebook.isMeRequest = false;
                        }
                        else {
                            responseFacebook.isMeRequest = true;
                        }

                        callResponseCallback(FacebookConstant.FB_DATA_TYPE_FEED, responseFacebook);
                    }
                }
            });

            feedRequest.setParameters(bundle);
            feedRequest.executeAsync();
        }

    }

    /*
     * (non-Javadoc) getFriendList - return list of FacebookUser simple JSon
     * format { "picture": { "data": { "is_silhouette": false
     * "url":"http://...." } } "id":"sadasfsafdsfds" "name": "" }
     */

    private void getFriendList() {
        Log.d(TAG, "getFriendList()");
        Session activeSession = Session.getActiveSession();
        if (activeSession.getState().isOpened()) {
            Request friendRequest = Request.newMyFriendsRequest(activeSession, new GraphUserListCallback() {
                @Override
                public void onCompleted(List<GraphUser> users, Response response) {
                    if (response.getError() != null) {
                        Log.d(TAG, "getFeedList()->onCompleted() errorCode = " + response.getError().getErrorCode());
                        callErrorResponseCallback(response.getError().getErrorCode());
                    }
                    else {
                        Log.d(TAG, "getFriendList()->onCompleted() response = success");
                        ArrayList<FacebookUser> mListFriend = new ArrayList<FacebookUser>();
                        if (users != null) {
                            Gson gson = new Gson();
                            for (int i = 0; i < users.size(); i++) {
                                JSONObject jsonObject = users.get(i).getInnerJSONObject();
                                if (jsonObject != null && jsonObject.length() > 0) {
                                    FacebookUser fbUser = gson.fromJson(jsonObject.toString(), FacebookUser.class);
                                    mListFriend.add(fbUser);
                                }
                            }
                        }
                        ResponseData responseFacebook = new ResponseData(FacebookConstant.FB_DATA_TYPE_FRIENDS);
                        FacebookFriends data = new FacebookFriends(mListFriend);
                        responseFacebook.responseData = data;
                        responseFacebook.isMeRequest = true;
                        callResponseCallback(FacebookConstant.FB_DATA_TYPE_FRIENDS, responseFacebook);
                    }
                }
            });
            Bundle params = new Bundle();
            params.putString(FacebookConstant.FB_REQUEST_PARAM_FIELDS_CONSTANT, FacebookConstant.FB_REQUEST_FRIEND_LIST_FIELDS);
            friendRequest.setParameters(params);
            friendRequest.executeAsync();
        }
    }

    private void getLikedPages(final HashMap<String, Object> param) {
        Log.d(TAG, "getLikedPages()");
        Session activeSession = Session.getActiveSession();
        if (activeSession.getState().isOpened()) {
            Bundle params = new Bundle();

            if (null != param) {
                if (param.containsKey("until")) {
                    params.putString("until", (String) param.get("until"));
                    Log.d("facebook", "until: " + param.get("until").toString());
                }
                if (param.containsKey("limit")) {
                    params.putInt("limit", (Integer) param.get("limit"));
                }
            }
            params.putString(FacebookConstant.FB_REQUEST_PARAM_FIELDS_CONSTANT, FacebookConstant.FB_REQUEST_LIKED_PAGES_FIELDS);
            Request likePagesRequest = new Request(activeSession, FacebookConstant.FB_GET_LIKEDPAGES, params, HttpMethod.GET, new Request.Callback() {

                @Override
                public void onCompleted(Response response) {
                    if (response.getError() != null) {
                        Log.e(TAG, "getLikedPages()->onCompleted() errorCode = " + response.getError());
                        callErrorResponseCallback(response.getError().getErrorCode());
                    } else {
                        Log.d(TAG, "getLikedPages()->onCompleted() response = success");
                        GraphObject graphObject = response.getGraphObject();
                        JSONObject jsonObject = graphObject.getInnerJSONObject();

                        Gson gson = new GsonBuilder().serializeNulls().create();
                        FacebookPages fbPages = gson.fromJson(jsonObject.toString(), FacebookPages.class);

                        ResponseData responseFacebook = new ResponseData(FacebookConstant.FB_DATA_TYPE_PAGES);
                        responseFacebook.responseData = fbPages;

                        /*set type of refresh*/
                        if (param != null && param.containsKey(GlobalConstant.TYPE_DATA_UPDATE)) {
                            boolean isRefresh = (Boolean) param.get(GlobalConstant.TYPE_DATA_UPDATE);
                            if (isRefresh)
                                responseFacebook.isRequestUpdateRefresh = true;
                            else
                                responseFacebook.isRequestUpdateRefresh = false;
                        }
                        if (param != null && param.containsKey("userId")) {
                            responseFacebook.isMeRequest = false;
                        }
                        else {
                            responseFacebook.isMeRequest = true;
                        }
                        callResponseCallback(FacebookConstant.FB_DATA_TYPE_PAGES, responseFacebook);
                    }
                }
            });
            likePagesRequest.executeAsync();
        }
    }

    private void getGroups(final HashMap<String, Object> param) {
        Log.d(TAG, "getGroups()");
        Session activeSession = Session.getActiveSession();
        if (activeSession.getState().isOpened()) {
            Bundle params = new Bundle();
            if (null != param) {
                if (param.containsKey("until")) {
                    params.putString("until", (String) param.get("until"));
                    Log.d("facebook", "until: " + param.get("until").toString());
                }
                if (param.containsKey("limit")) {
                    params.putInt("limit", (Integer) param.get("limit"));
                }
            }
            params.putString("fields", "description, name, id");
            Request likePagesRequest = new Request(activeSession, FacebookConstant.FB_GET_GROUPS, params, HttpMethod.GET, new Request.Callback() {
                @Override
                public void onCompleted(Response response) {
                    if (response.getError() != null) {
                        Log.e(TAG, "getLikedPages()->onCompleted() errorCode = " + response.getError());
                        callErrorResponseCallback(response.getError().getErrorCode());
                    } else {
                        Log.d(TAG, "getGroups()->onCompleted() response = success");
                        GraphObject graphObject = response.getGraphObject();
                        JSONObject jsonObject = graphObject.getInnerJSONObject();
                        Gson gson = new GsonBuilder().serializeNulls().create();
                        FacebookPages fbPages = gson.fromJson(jsonObject.toString(), FacebookPages.class);

                        ResponseData responseFacebook = new ResponseData(FacebookConstant.FB_DATA_TYPE_PAGES);
                        responseFacebook.responseData = fbPages;
                        /*set type of refresh*/
                        if (param != null && param.containsKey(GlobalConstant.TYPE_DATA_UPDATE)) {
                            boolean isRefresh = (Boolean) param.get(GlobalConstant.TYPE_DATA_UPDATE);
                            if (isRefresh)
                                responseFacebook.isRequestUpdateRefresh = true;
                            else
                                responseFacebook.isRequestUpdateRefresh = false;
                        }
                        if (param != null && param.containsKey("userId")) {
                            responseFacebook.isMeRequest = false;
                        }
                        else {
                            responseFacebook.isMeRequest = true;
                        }
                        callResponseCallback(FacebookConstant.FB_DATA_TYPE_GROUPS, responseFacebook);
                    }
                }
            });
            likePagesRequest.executeAsync();
        }
    }

    private void getLinksOnly(final HashMap<String, Object> param) {
        Log.d(TAG, "getLinksOnly()");
        Session activeSession = Session.getActiveSession();
        if (activeSession.getState().isOpened()) {
            String graph;
            if (param != null && param.containsKey(FacebookConstant.FB_KEY_NEXT_PAGE)) {
                graph = (String) param.get(FacebookConstant.FB_KEY_NEXT_PAGE);
                FacebookHttpAsync request = new FacebookHttpAsync(LOAD_MORE_COMMAND, FacebookConstant.FB_DATA_TYPE_FEED_LINKS, graph, param);
                request.execute();
                return;
            }
            Request share = Request.newGraphPathRequest(activeSession, FacebookConstant.FB_GET_MYNEWSFEED, new Request.Callback() {

                @Override
                public void onCompleted(Response response) {
                    if (response.getError() != null) {
                        Log.d(TAG, "get link only error");
                        callErrorResponseCallback(response.getError().getErrorCode());
                    } else {
                        Log.d(TAG, "getLinksOnly()->onCompleted() response = success");
                        GraphObject graphObject = response.getGraphObject();
                        JSONObject json = graphObject.getInnerJSONObject();

                        Gson gson = new GsonBuilder().serializeNulls().create();
                        FacebookFeeds feeds = gson.fromJson(json.toString(), FacebookFeeds.class);

                        FacebookFeeds feedLinks = new FacebookFeeds();
                        for (FacebookFeedWrapper item : feeds.getData()) {
                            Log.d(TAG, "item " + item.getName() + "     feeds.type = " + item.getType());
                            if (item.getType() == TypeTimeline.LINK || item.getType() == TypeTimeline.VIDEO) {
                                feedLinks.getData().add(item);
                            }
                        }
                        if (feeds.getPaging() != null) {
                            feedLinks.setPaging(new FacebookPaging(feeds.getPaging().getNextPage()));
                        }

                        if (feedLinks.getData() == null)
                            Log.d(TAG, "feedLinks is null");

                        for (int i = 0; i < feedLinks.getData().size(); i++) {
                            String picture = feedLinks.getData().get(i).getPicture();
                            if (picture != null && picture.contains("_s.")) {
                                int indexOfSize = picture.lastIndexOf("s");
                                feedLinks.getData().get(i).setPicture(picture.substring(0, indexOfSize) + "n" + picture.substring(indexOfSize + 1, picture.length()));
                            }
                        }

                        ResponseData responseFacebook = new ResponseData(FacebookConstant.FB_DATA_TYPE_FEED_LINKS);
                        responseFacebook.responseData = feedLinks;

                        /*set type of refresh*/
                        if (param != null && param.containsKey(GlobalConstant.TYPE_DATA_UPDATE)) {
                            boolean isRefresh = (Boolean) param.get(GlobalConstant.TYPE_DATA_UPDATE);
                            if (isRefresh)
                                responseFacebook.isRequestUpdateRefresh = true;
                            else
                                responseFacebook.isRequestUpdateRefresh = false;
                        }
                        if (param != null && param.containsKey("userId")) {
                            responseFacebook.isMeRequest = false;
                        }
                        else {
                            responseFacebook.isMeRequest = true;
                        }

                        callResponseCallback(FacebookConstant.FB_DATA_TYPE_FEED_LINKS, responseFacebook);
                    }
                }
            });
            Bundle params = new Bundle();
            params.putInt("limit", 40);
            share.setParameters(params);
            share.executeAsync();
        }
    }

    private void getTaggedMe(final HashMap<String, Object> param) {
        Log.d(TAG, "getMyPhotos()");
        Session activeSession = Session.getActiveSession();
        if (activeSession.getState().isOpened()) {
            Request requestPhotosOnly = Request.newGraphPathRequest(activeSession, FacebookConstant.FB_GET_PHOTOS, new Request.Callback() {

                @Override
                public void onCompleted(Response response) {
                    if (response.getError() != null) {
                        Log.d(TAG, "getMyPhotos()->onCompleted() errorCode = " + response.getError());
                        callErrorResponseCallback(response.getError().getErrorCode());
                    } else {
                        Log.d(TAG, "getMyPhotos()->onCompleted() response = success");
                        GraphObject graphObject = response.getGraphObject();
                        JSONObject json = graphObject.getInnerJSONObject();
                        Gson gson = new GsonBuilder().serializeNulls().create();
                        FacebookPhotos myPhotos = gson.fromJson(json.toString(), FacebookPhotos.class);

                        for (FacebookPhoto item : myPhotos.getData()) {
                            item.setType("photo");
                            String picture = item.getPicture();
                            if (picture != null && picture.contains("_s.")) {
                                int indexOfSize = picture.lastIndexOf("s");
                                item.setPicture(picture.substring(0, indexOfSize) + "n" + picture.substring(indexOfSize + 1, picture.length()));
                            }
                        }
                        ResponseData responseFacebook = new ResponseData(FacebookConstant.FB_DATA_TYPE_TAGGEDME);
                        responseFacebook.responseData = myPhotos;
                        /*set type of refresh*/
                        if (param != null && param.containsKey(GlobalConstant.TYPE_DATA_UPDATE)) {
                            boolean isRefresh = (Boolean) param.get(GlobalConstant.TYPE_DATA_UPDATE);
                            if (isRefresh)
                                responseFacebook.isRequestUpdateRefresh = true;
                            else
                                responseFacebook.isRequestUpdateRefresh = false;
                        }
                        if (param != null && param.containsKey("userId")) {
                            responseFacebook.isMeRequest = false;
                        }
                        else {
                            responseFacebook.isMeRequest = true;
                        }
                        callResponseCallback(FacebookConstant.FB_DATA_TYPE_TAGGEDME, responseFacebook);
                    }
                }
            });
            Bundle params = new Bundle();
            params.putInt("limit", 10);
            params.putString(FacebookConstant.FB_REQUEST_PARAM_FIELDS_CONSTANT, FacebookConstant.FB_REQUEST_TAGGED_PHOTO_FIELDS);
            if (null != param) {
                if (param.containsKey("until")) {
                    params.putString("until", (String) param.get("until"));
                    Log.d("facebook", "until: " + param.get("until").toString());
                }
                if (param.containsKey("limit")) {
                    params.putInt("limit", (Integer) param.get("limit"));
                }
            }
            requestPhotosOnly.setParameters(params);

            requestPhotosOnly.executeAsync();
        }
    }

    public void getUserProfile() {
        getUserData();
    }

    private void getUserData() {
        Log.d(TAG, "getUserData()");
        final Session activieSession = Session.getActiveSession();
        if (activieSession.getState().isOpened()) {
            Request request = Request.newMeRequest(activieSession, new Request.GraphUserCallback() {
                @Override
                public void onCompleted(GraphUser user, Response response) {
                    if (response.getError() != null) {
                        Log.d(TAG, "getMyPhotos()->onCompleted() errorCode = " + response.getError());
                        callErrorResponseCallback(response.getError().getErrorCode());
                        return;
                    }

                    Log.d(TAG, "getUserData()->onCompleted() response = success");
                    if (user != null && activieSession == Session.getActiveSession()) {
                        FacebookUser userData = new FacebookUser();
                        userData.id = user.getId();
                        userData.name = user.getName();
                        ResponseData responseFacebook = new ResponseData(FacebookConstant.FB_DATA_TYPE_USER);
                        responseFacebook.responseData = userData;
                        callResponseCallback(FacebookConstant.FB_DATA_TYPE_USER, responseFacebook);
                    }
                }
            });
            request.executeAsync();
        }
    }

    private void getFriendsGroup() {
        Log.d(TAG, "getFriends Group");
        Session activeSession = Session.getActiveSession();
        if (activeSession.getState().isOpened()) {
            Bundle params = new Bundle();
            params.putString("fields", "id, list_type, name, members");

            Request friendGroupsRequest = new Request(activeSession, FacebookConstant.FB_GET_FRIEND_GROUPS, params, HttpMethod.GET, new Request.Callback() {

                @Override
                public void onCompleted(Response response) {
                    if (response.getError() != null) {
                        Log.d(TAG, "getFriends Group()->onCompleted() errorCode = " + response.getError());
                        callErrorResponseCallback(response.getError().getErrorCode());
                    } else {
                        Log.d(TAG, "getFriends Group() success response = success");
                        GraphObject graphObject = response.getGraphObject();
                        JSONObject jsonObject = graphObject.getInnerJSONObject();
                        Gson gson = new GsonBuilder().serializeNulls().create();
                        FacebookPages fbFriendGroups = gson.fromJson(jsonObject.toString(), FacebookPages.class);
                        ResponseData responseFacebook = new ResponseData(FacebookConstant.FB_DATA_TYPE_FRIEND_GROUPS);
                        responseFacebook.responseData = fbFriendGroups;
                        // Friend Group is just have MeRequest
                        responseFacebook.isMeRequest = true;
                        callResponseCallback(FacebookConstant.FB_DATA_TYPE_FRIEND_GROUPS, responseFacebook);
                    }
                }
            });
            friendGroupsRequest.executeAsync();
        }
    }

    @Override
    public void getFacebookImagesInformation(int requestDataType, FacebookFeeds feedsData, final IResponseFromSNSCallback callback) {
        Log.d(TAG, "getImageQuality");
        FacebookFeeds feeds = new FacebookFeeds();
        // feedList is used to be param in callback
        final ArrayList<FacebookFeedWrapper> feedlist = feedsData.getData();
        feeds.setData(feedlist);
        final ArrayList<String> listIdPhotos = new ArrayList<String>();
        final HashMap<String, FacebookPhoto> mapIdToPhoto = new HashMap<String, FacebookPhoto>();
        final HashMap<String, String> mapIdObjId = new HashMap<String, String>();
        Session activeSession = Session.getActiveSession();

        for (int i = 0; i < feeds.getData().size(); i++) {
            if (feeds.getData().get(i).getType() == TypeTimeline.PHOTO) {
                String id = feeds.getData().get(i).getId();
                String objId = feeds.getData().get(i).getObject_id();
                Log.d(TAG, "choose photos, i = " + i + "   id = " + id + "objId = " + objId);
                mapIdObjId.put(id, objId);
                listIdPhotos.add(id);
            }
        }

        if (activeSession != null && activeSession.isOpened()) {
            RequestBatch imageQualityRequests = new RequestBatch();
            for (int i = 0; i < listIdPhotos.size(); i++) {
                final String id = listIdPhotos.get(i);
                final String graphPath = mapIdObjId.get(id);
                Log.d(TAG, "request graph for i = " + i + "   graphPath =  " + graphPath);

                Request imageRequest = Request.newGraphPathRequest(activeSession, graphPath, new Request.Callback() {

                    @Override
                    public void onCompleted(Response response) {
                        if (response.getError() != null) {
                            Log.d(TAG, "error when request, response = " + response);
                            mapIdToPhoto.put(id, null);
                        } else {
                            Log.d(TAG, "getFacebookImagesInformation->onCompleted response = success");
                            GraphObject graphObject = response.getGraphObject();
                            JSONObject jsonObject = graphObject.getInnerJSONObject();
                            Gson gson = new GsonBuilder().serializeNulls().create();
                            FacebookPhoto photo = gson.fromJson(jsonObject.toString(), FacebookPhoto.class);
                            mapIdToPhoto.put(id, photo);
                        }
                    }
                });

                Bundle params = new Bundle();
                params.putString("fields", "width,height,source");
                imageRequest.setParameters(params);
                imageQualityRequests.add(imageRequest);
            }

            imageQualityRequests.addCallback(new Callback() {

                @Override
                public void onBatchCompleted(RequestBatch imageQualityRequests) {
                    Log.d(TAG, "request batch complete");
                    for (int i = 0; i < mapIdToPhoto.size(); i++)
                    {
                        if (mapIdToPhoto.get(listIdPhotos.get(i)) != null)
                            Log.d(TAG, "i = " + i + "src = " + mapIdToPhoto.get(listIdPhotos.get(i)).getSource());
                        callback.onImagesQualityResponse(GlobalConstant.SOCIAL_TYPE_FACEBOOK, FacebookConstant.FB_DATA_TYPE_SPECIAL_IMAGE_QUALITY, mapIdToPhoto, feedlist);
                    }
                }
            });
            //Settings.addLoggingBehavior(LoggingBehavior.REQUESTS);
            if (!imageQualityRequests.isEmpty()) {
                imageQualityRequests.executeAsync();
            }
        }

    }

    public void getImageItem(final HashMap<String, Object> param)
    {
        Log.d(TAG, "getImageItem");
        String imageId = null;
        if (param != null) {
            if (param.containsKey("object_id")) {
                imageId = (String) param.get("object_id");
            }
        }
        Session activeSession = Session.getActiveSession();
        if (activeSession.getState().isOpened() && imageId != null)
        {
            Log.d(TAG, "getImageItem object_id = " + imageId);
            Request imageRequest = Request.newGraphPathRequest(activeSession, imageId, new Request.Callback() {

                @Override
                public void onCompleted(Response response) {
                    if (response.getError() != null) {
                        Log.d(TAG, "error when request, response = " + response);
                        callErrorResponseCallback(response.getError().getErrorCode());
                    } else {
                        Log.d(TAG, "getImageItem successfully");
                        GraphObject graphObject = response.getGraphObject();
                        JSONObject jsonObject = graphObject.getInnerJSONObject();
                        Gson gson = new GsonBuilder().serializeNulls().create();
                        FacebookPhoto photo = gson.fromJson(jsonObject.toString(), FacebookPhoto.class);
                        ResponseData responseFacebook = new ResponseData(FacebookConstant.FB_DATA_TYPE_PHOTO_ITEM);
                        responseFacebook.responseData = photo;
                        /*set type of refresh*/
                        if (param != null && param.containsKey(GlobalConstant.TYPE_DATA_UPDATE)) {
                            boolean isRefresh = (Boolean) param.get(GlobalConstant.TYPE_DATA_UPDATE);
                            if (isRefresh)
                                responseFacebook.isRequestUpdateRefresh = true;
                            else
                                responseFacebook.isRequestUpdateRefresh = false;
                        }
                        if (param != null && param.containsKey("userId")) {
                            responseFacebook.isMeRequest = false;
                        }
                        else {
                            responseFacebook.isMeRequest = true;
                        }
                        callResponseCallback(FacebookConstant.FB_DATA_TYPE_PHOTO_ITEM, responseFacebook);
                    }
                }
            });
            Bundle params = new Bundle();
            params.putString("fields", "width,height,source,images");
            imageRequest.setParameters(params);
            imageRequest.executeAsync();
        }
    }

    private void getLikeInfo(HashMap<String, Object> param) {
        Log.d(TAG, "getLike&Comemnt");
        String id = "";
        int fragmentType = -1;
        if (param != null) {
            if (param.containsKey("id")) {
                id = (String) param.get("id");
            }
            if (param.containsKey(FacebookConstant.FB_FRAGMENT_TYPE)) {
                fragmentType = (Integer) param.get(FacebookConstant.FB_FRAGMENT_TYPE);
            }
        }

        Log.d(TAG, "getLikeCommentInfo, id " + id);
        Session activeSession = Session.getActiveSession();
        /*please don't delete these code, maybe we will reuse
        String graphLikeRequest = id + "/likes";
        Log.d(TAG, "graphRequest = " + graphLikeRequest);
        Request likeInfoRequest = Request.newGraphPathRequest(activeSession, graphLikeRequest, new Request.Callback() {

            @Override
            public void onCompleted(Response response) {
                if (response.getError() != null) {
                    Log.d(TAG, "error when request, response = " + response.toString());
                    callErrorResponseCallback(response.getError().getErrorCode());
                } else {
                    Log.d(TAG, "mtuan.minh, success response" + response.toString());
                    GraphObject graphObject = response.getGraphObject();
                    JSONObject jsonObject = graphObject.getInnerJSONObject();
                    JSONObject objSummary = jsonObject.optJSONObject("summary");

                    Gson gson = new GsonBuilder().serializeNulls().create();

                    FacebookSummary like_count;
                    if (objSummary == null) {
                        like_count = new FacebookSummary();
                        like_count.setTotal_count(0);
                    }
                    else {
                        like_count = gson.fromJson(objSummary.toString(), FacebookSummary.class);
                    }
                    Log.d(TAG, "mtuan.minh, likeInfo, likecount = " + like_count.getTotal_count());
                    ResponseData dataRespond = new ResponseData(FacebookConstant.FB_DATA_TYPE_LIKEINFO);
                    dataRespond.responseData = like_count;//new FacebookLikeInfoData(like_count);
                    callResponseCallback(FacebookConstant.FB_DATA_TYPE_LIKEINFO, dataRespond);
                }

            }
        });
        Bundle likeparams = new Bundle();
        likeparams.putInt("limit", 1); //we don't need data in this case, likecount only
        likeparams.putBoolean("summary", true);
        likeInfoRequest.setParameters(likeparams);
        */
        String graphCommentRequest = id + "/comments";
        Log.d(TAG, "graphRequest = " + graphCommentRequest);
        Request commentInfoRequest = Request.newGraphPathRequest(activeSession, graphCommentRequest, new Request.Callback() {

            @Override
            public void onCompleted(Response response) {
                if (response.getError() != null) {
                    Log.d(TAG, "error when request, error code = " + response.getError().getErrorCode());
                    callErrorResponseCallback(response.getError().getErrorCode());
                } else {
                    Log.d(TAG, "getLikeInfo - success response");
                    GraphObject graphObject = response.getGraphObject();
                    JSONObject jsonObject = graphObject.getInnerJSONObject();
                    JSONObject objSummary = jsonObject.optJSONObject("summary");

                    Gson gson = new GsonBuilder().serializeNulls().create();
                    FacebookSummary comment_count;
                    if (objSummary == null) {
                        comment_count = new FacebookSummary();
                        comment_count.setTotal_count(0);
                    }
                    else {
                        comment_count = gson.fromJson(objSummary.toString(), FacebookSummary.class);
                    }
                    ResponseData dataRespond = new ResponseData(FacebookConstant.FB_DATA_TYPE_COMMENTINFO);
                    dataRespond.responseData = comment_count;
                    callResponseCallback(FacebookConstant.FB_DATA_TYPE_COMMENTINFO, dataRespond);

                }

            }
        });
        Bundle commentParams = new Bundle();
        commentParams.putInt("limit", 1); //we don't need data in this case, comment count only
        commentParams.putBoolean("summary", true);
        commentInfoRequest.setParameters(commentParams);

        /*check if the user liked this post or not + like count */

        Request checkUserLikeRequest = Request.newGraphPathRequest(activeSession, "fql", new Request.Callback() {
            @Override
            public void onCompleted(Response response) {
                Log.d(TAG, "mtuan.minh, request userLike, reponse = " + response);
                if (response.getError() != null) {
                    Log.d(TAG, "error when request userLike, error code = " + response.getError().getErrorCode());
                } else {
                    Log.d(TAG, "mtuan.minh, getting userlike? success response" + response.toString());
                    GraphObject graphObject = response.getGraphObject();
                    JSONObject jsonObject = graphObject.getInnerJSONObject();
                    org.json.JSONArray jArray = jsonObject.optJSONArray("data");
                    JSONObject obj = jArray.optJSONObject(0);
                    if (null != obj && obj.length() > 0) {
                        JSONObject objLikeInfo = obj.optJSONObject("like_info");
                        if (objLikeInfo != null && objLikeInfo.length() > 0) {
                            FacebookSummary isUserLike = new FacebookSummary();
                            boolean canLike, userLike;
                            int likeCount;
                            try {
                                likeCount = objLikeInfo.getInt("like_count");
                                canLike = objLikeInfo.getBoolean("can_like");
                                userLike = objLikeInfo.getBoolean("user_likes");
                                isUserLike.isUserLiked = userLike ? 2 : 1;
                                isUserLike.canLike = canLike ? 2 : 1;
                                isUserLike.setTotal_count(likeCount);
                                Log.d(TAG, "check user likes ok, userLike = " + userLike);
                                ResponseData dataRespond = new ResponseData(FacebookConstant.FB_DATA_TYPE_LIKEINFO);
                                dataRespond.responseData = isUserLike;
                                callResponseCallback(FacebookConstant.FB_DATA_TYPE_LIKEINFO, dataRespond);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        });
        if (fragmentType == FacebookConstant.FB_DATA_TYPE_TAGGEDME) {
            checkUserLikeRequest.getParameters().putString("q", "SELECT like_info FROM photo WHERE object_id = '" + id + "'");
        } else {
            checkUserLikeRequest.getParameters().putString("q", "SELECT like_info FROM stream WHERE post_id = '" + id + "'");
        }

        Collection<Request> likeCommentRequest = new ArrayList<Request>(2);
        //        likeCommentRequest.add(likeInfoRequest);
        likeCommentRequest.add(commentInfoRequest);
        likeCommentRequest.add(checkUserLikeRequest);
        if (activeSession.getState().isOpened()) {
            RequestBatch likeCommentRequestes = new RequestBatch(likeCommentRequest);
            likeCommentRequestes.executeAsync();
        }
    }

    /*get timeline for others like pages, group 
     * String graphPath = String.format("%s/likes", id);*/
    private void getFeedPages(final HashMap<String, Object> params) {
        String id = "";
        if (params != null) {
            if (params.containsKey("page_id")) {
                id = (String) params.get("page_id");
            }
        }
        String graphPath = String.format("%s/feed", id);
        Log.d(TAG, "getTimeLine for Page id = " + id);
        Session activeSession = Session.getActiveSession();

        if (activeSession.getState().isOpened()) {
            Log.d(TAG, "graphRequest = " + graphPath);
            Request viewPageRequest = Request.newGraphPathRequest(activeSession, graphPath, new Request.Callback() {

                @Override
                public void onCompleted(Response response) {
                    if (response.getError() != null) {
                        Log.d(TAG, "view page error, code = " + response.getError().getErrorCode());
                        callErrorResponseCallback(response.getError().getErrorCode());
                    } else {
                        GraphObject graphObject = response.getGraphObject();
                        JSONObject json = graphObject.getInnerJSONObject();
                        Log.d("facebook", "graph path: " + json.toString());

                        Gson gson = new GsonBuilder().serializeNulls().create();
                        FacebookFeeds list = gson.fromJson(json.toString(), FacebookFeeds.class);
                        for (int i = 0; i < list.getData().size(); i++) {
                            String picture = list.getData().get(i).getPicture();
                            if (picture != null && picture.contains("_s.")) {
                                int indexOfSize = picture.lastIndexOf("s");
                                list.getData().get(i).setPicture(picture.substring(0, indexOfSize) + "n" + picture.substring(indexOfSize + 1, picture.length()));
                            }
                        }
                        ResponseData responseFacebook = new ResponseData(FacebookConstant.FB_DATA_TYPE_PAGE_TIMELINE);
                        responseFacebook.responseData = list;
                        /*set type of refresh*/
                        if (params != null && params.containsKey(GlobalConstant.TYPE_DATA_UPDATE)) {
                            boolean isRefresh = (Boolean) params.get(GlobalConstant.TYPE_DATA_UPDATE);
                            if (isRefresh) {
                                responseFacebook.isRequestUpdateRefresh = true;
                            } else {
                                responseFacebook.isRequestUpdateRefresh = false;
                            }
                        }
                        if (params != null && params.containsKey("page_id")) {
                            responseFacebook.isMeRequest = false;
                        }
                        else {
                            responseFacebook.isMeRequest = true;
                        }
                        callResponseCallback(FacebookConstant.FB_DATA_TYPE_PAGE_TIMELINE, responseFacebook);
                    }

                }
            });
            viewPageRequest.executeAsync();
        }
    }

    /*
     * idGroup/members 
     */
    public void getMembersOfGroup(final HashMap<String, Object> params) {
        String id = "";
        if (params != null) {
            if (params.containsKey("page_id")) {
                id = (String) params.get("page_id");
            }
        }
        String graphPath = String.format("%s/members", id);
        Log.d(TAG, "getTimeLine for Page id = " + id);
        Session activeSession = Session.getActiveSession();

        if (activeSession.getState().isOpened()) {
            Log.d(TAG, "graphRequest = " + graphPath);
            Request viewPageRequest = Request.newGraphPathRequest(activeSession, graphPath, new Request.Callback() {

                @Override
                public void onCompleted(Response response) {
                    if (response.getError() != null) {
                        Log.d(TAG, "view page error, code = " + response.getError().getErrorCode());
                        callErrorResponseCallback(response.getError().getErrorCode());
                    } else {
                        GraphObject graphObject = response.getGraphObject();
                        JSONObject json = graphObject.getInnerJSONObject();
                        Log.d("facebook", "graph path: " + json.toString());

                        Gson gson = new GsonBuilder().serializeNulls().create();
                        FacebookFriends list = gson.fromJson(json.toString(), FacebookFriends.class);
                        Log.d(TAG, "getMembersOfGroup listSize = " + list.mFriendList.size());
                        ResponseData responseFacebook = new ResponseData(FacebookConstant.FB_DATA_MEMBERS_OF_GROUP);
                        responseFacebook.responseData = list;
                        /*set type of refresh*/
                        if (params != null && params.containsKey(GlobalConstant.TYPE_DATA_UPDATE)) {
                            boolean isRefresh = (Boolean) params.get(GlobalConstant.TYPE_DATA_UPDATE);
                            if (isRefresh) {
                                responseFacebook.isRequestUpdateRefresh = true;
                            } else {
                                responseFacebook.isRequestUpdateRefresh = false;
                            }
                        }
                        if (params != null && params.containsKey("page_id")) {
                            responseFacebook.isMeRequest = false;
                        }
                        else {
                            responseFacebook.isMeRequest = true;
                        }
                        callResponseCallback(FacebookConstant.FB_DATA_MEMBERS_OF_GROUP, responseFacebook);
                    }

                }
            });
            viewPageRequest.executeAsync();
        }
    }

    /*me/posts for timeLine?*/
    public void getTimeline(final HashMap<String, Object> params) {
        Session activeSession = Session.getActiveSession();
        String graphPath = FacebookConstant.FB_GET_TIMELINE;
        final Bundle requestPrams = new Bundle();
        Log.d(TAG, "getTimeline()");
        if (null != params) {
            if (params.containsKey(FacebookConstant.FB_KEY_NEXT_PAGE)) {
                //                params.putString(FacebookConstant.FB_KEY_NEXT_PAGE, (String) param.get(FacebookConstant.FB_KEY_NEXT_PAGE));
                //                Log.d("facebook", "until: " + param.get("until").toString());
                String strPath = (String) params.get(FacebookConstant.FB_KEY_NEXT_PAGE);
                FacebookHttpAsync request = new FacebookHttpAsync(LOAD_MORE_COMMAND, FacebookConstant.FB_DATA_TYPE_TIMELINE, strPath, params);
                request.execute();
                return;
            }
            if (params.containsKey("limit")) {
                requestPrams.putInt("limit", (Integer) params.get("limit"));
            }
            if (params.containsKey("userId")) {
                graphPath = params.get("userId").toString() + "/feed";
            }
        }
        if (activeSession.isOpened()) {
            Request request = new Request(activeSession, graphPath, requestPrams, HttpMethod.GET, new Request.Callback() {

                @Override
                public void onCompleted(Response response) {
                    if (response.getError() != null) {
                        callErrorResponseCallback(response.getError().getErrorCode());
                    } else {
                        Log.d(TAG, "getTimeline response = " + response.toString());
                        if (mGson == null) {
                            mGson = new GsonBuilder().serializeNulls().create();
                        }
                        //Utils.LogExt(TAG, response.toString());
                        GraphObject graphObject = response.getGraphObject();
                        JSONObject json = graphObject.getInnerJSONObject();

                        Gson gson = new GsonBuilder().serializeNulls().create();
                        FacebookFeeds list = gson.fromJson(json.toString(), FacebookFeeds.class);
                        ArrayList<FacebookFeedWrapper> tmpList = new ArrayList<FacebookFeedWrapper>();
                        tmpList.addAll(list.getData());
                        for (FacebookFeedWrapper feedItem : tmpList) {
                            // Analyze timelint data by story
                            String story = feedItem.story;
                            String type = feedItem.type;
                            //Log.d(TAG, "item story = " + story + ", type = " + type + ", message = " + feedItem.getMessage());
                            if (type.compareTo("status") == 0 && story != null) {
                                list.getData().remove(feedItem);
                                continue;
                            }
                            // Convert image url
                            String picture = feedItem.getPicture();
                            if (picture != null && picture.contains("_s.")) {
                                int indexOfSize = picture.lastIndexOf("s");
                                feedItem.setPicture(picture.substring(0, indexOfSize) + "n" + picture.substring(indexOfSize + 1, picture.length()));
                            }
                        }
                        ResponseData responseFacebook = new ResponseData(FacebookConstant.FB_DATA_TYPE_FEED);
                        responseFacebook.responseData = list;
                        /*set type of refresh*/
                        if (params != null && params.containsKey(GlobalConstant.TYPE_DATA_UPDATE)) {
                            boolean isRefresh = (Boolean) params.get(GlobalConstant.TYPE_DATA_UPDATE);
                            if (isRefresh) {
                                responseFacebook.isRequestUpdateRefresh = true;
                            } else {
                                responseFacebook.isRequestUpdateRefresh = false;
                            }
                        }
                        if (params != null && params.containsKey("userId")) {
                            responseFacebook.isMeRequest = false;
                        }
                        else {
                            responseFacebook.isMeRequest = true;
                        }
                        callResponseCallback(FacebookConstant.FB_DATA_TYPE_TIMELINE, responseFacebook);
                    }
                }
            });
            request.executeAsync();
        }
    }

    public void getFeedPhotos(final HashMap<String, Object> param) {
        Log.d(TAG, "getFeedPhotos()");
        Session activeSession = Session.getActiveSession();
        String graphPath;
        if (activeSession.getState().isOpened()) {
            if (param != null && param.containsKey(FacebookConstant.FB_KEY_NEXT_PAGE)) {
                graphPath = (String) param.get(FacebookConstant.FB_KEY_NEXT_PAGE);
                FacebookHttpAsync request = new FacebookHttpAsync(LOAD_MORE_COMMAND, FacebookConstant.FB_DATA_TYPE_FEED_PHOTOS, graphPath, param);
                request.execute();
                return;
            }
            Request requestGetFeedPhotos = Request.newGraphPathRequest(activeSession, FacebookConstant.FB_GET_FEED_PHOTOS, new Request.Callback() {

                @Override
                public void onCompleted(Response response) {
                    if (response.getError() != null) {
                        Log.d(TAG, "getFeedPhotos()->onCompleted() errorCode = " + response.getError());
                        callErrorResponseCallback(response.getError().getErrorCode());
                    } else {
                        Log.d(TAG, "getFeedPhotos()->onCompleted() Success");
                        GraphObject graphObject = response.getGraphObject();
                        JSONObject json = graphObject.getInnerJSONObject();
                        Gson gson = new GsonBuilder().serializeNulls().create();
                        FacebookFeeds feedphotos = gson.fromJson(json.toString(), FacebookFeeds.class);

                        for (int i = 0; i < feedphotos.getData().size(); i++) {
                            String picture = feedphotos.getData().get(i).getPicture();
                            if (picture != null && picture.contains("_s.")) {
                                int indexOfSize = picture.lastIndexOf("s");
                                feedphotos.getData().get(i).setPicture(picture.substring(0, indexOfSize) + "n" + picture.substring(indexOfSize + 1, picture.length()));
                            }
                        }

                        ResponseData responseFacebook = new ResponseData(FacebookConstant.FB_DATA_TYPE_FEED_PHOTOS);
                        responseFacebook.responseData = feedphotos;
                        /*set type of refresh*/
                        if (param != null && param.containsKey(GlobalConstant.TYPE_DATA_UPDATE)) {
                            boolean isRefresh = (Boolean) param.get(GlobalConstant.TYPE_DATA_UPDATE);
                            if (isRefresh)
                                responseFacebook.isRequestUpdateRefresh = true;
                            else
                                responseFacebook.isRequestUpdateRefresh = false;
                        }
                        if (param != null && param.containsKey("userId")) {
                            responseFacebook.isMeRequest = false;
                        }
                        else {
                            responseFacebook.isMeRequest = true;
                        }
                        callResponseCallback(FacebookConstant.FB_DATA_TYPE_FEED_PHOTOS, responseFacebook);
                    }
                }
            });
            requestGetFeedPhotos.executeAsync();
        }
    }

    private void getSingleFeed(final HashMap<String, Object> params) {
        String graphPath = "";
        if (params != null) {
            if (params.containsKey("id")) {
                graphPath = (String) params.get("id");
            }
        }
        Log.d(TAG, "getSingleFeed for feed id = " + graphPath);
        Session activeSession = Session.getActiveSession();
        if (activeSession.getState().isOpened()) {
            Log.d(TAG, "graphRequest = " + graphPath);
            Request viewPageRequest = Request.newGraphPathRequest(activeSession, graphPath, new Request.Callback() {

                @Override
                public void onCompleted(Response response) {
                    Log.d(TAG, " getSingleFeed response " + response);
                    if (response.getError() != null) {
                        Log.d(TAG, "getSingleFeed error, code = " + response.getError().getErrorCode());
                        callErrorResponseCallback(response.getError().getErrorCode());
                    } else {
                        GraphObject graphObject = response.getGraphObject();
                        JSONObject json = graphObject.getInnerJSONObject();
                        Log.d(TAG, "getSingleFeed graph path: " + json.toString());

                        Gson gson = new GsonBuilder().serializeNulls().create();
                        FacebookFeedWrapper feed = gson.fromJson(json.toString(), FacebookFeedWrapper.class);

                        ResponseData responseFacebook = new ResponseData(FacebookConstant.FB_DATA_TYPE_FEED_ITEM);
                        responseFacebook.responseData = feed;
                        /*set type of refresh*/
                        if (params != null && params.containsKey(GlobalConstant.TYPE_DATA_UPDATE)) {
                            boolean isRefresh = (Boolean) params.get(GlobalConstant.TYPE_DATA_UPDATE);
                            if (isRefresh)
                                responseFacebook.isRequestUpdateRefresh = true;
                            else
                                responseFacebook.isRequestUpdateRefresh = false;
                        }
                        if (params != null && params.containsKey("userId")) {
                            responseFacebook.isMeRequest = false;
                        }
                        else {
                            responseFacebook.isMeRequest = true;
                        }
                        callResponseCallback(FacebookConstant.FB_DATA_TYPE_FEED_ITEM, responseFacebook);
                    }

                }
            });
            viewPageRequest.executeAsync();
        }
    }

    public void getMoreComment(HashMap<String, Object> param) {
        Log.d(TAG, "getMoreComment");
        Session activeSession = Session.getActiveSession();
        String graphPath = "";
        String header = "https://graph.facebook.com/";
        if (null != param && param.containsKey("feedId")) { //get 1st page comments
            graphPath = (String) param.get("feedId");
            graphPath = graphPath + "/comments";
        } else if (null != param && param.containsKey("util")) { //for load more
            graphPath = (String) param.get("util");
            graphPath = graphPath.substring(header.length());
        }
        Log.d(TAG, "graphPath  =  " + graphPath);
        if (activeSession.getState().isOpened()) {
            Request moreCommentRequest = Request.newGraphPathRequest(activeSession, graphPath, new Request.Callback() {

                @Override
                public void onCompleted(Response response) {
                    if (response.getError() != null) {
                        Log.d(TAG, "error when request, code = " + response.getError().getErrorCode());
                        callErrorResponseCallback(response.getError().getErrorCode());
                    } else {
                        Log.d(TAG, "getMoreComment - successfully request");
                        GraphObject graphObject = response.getGraphObject();
                        JSONObject jsonObject = graphObject.getInnerJSONObject();
                        Gson gson = new GsonBuilder().serializeNulls().create();
                        FacebookCommentList listComments = gson.fromJson(jsonObject.toString(), FacebookCommentList.class);
                        ResponseData responseFacebook = new ResponseData(FacebookConstant.FB_DATA_MORE_COMMENT);
                        responseFacebook.responseData = listComments;
                        callResponseCallback(FacebookConstant.FB_DATA_MORE_COMMENT, responseFacebook);
                    }

                }
            });

            moreCommentRequest.executeAsync();
        }

    }

    public void postLike(String id) {
        Log.d(TAG, "postLike, id " + id);
        Session activieSession = Session.getActiveSession();

        if (activieSession.getState().isOpened()) {
            String graphPath = String.format("%s/likes", id);
            Request likeRequest = new Request(activieSession, graphPath, null, HttpMethod.POST, new Request.Callback() {

                @Override
                public void onCompleted(Response response) {
                    if (response.getError() != null) {
                        Log.d(TAG, "error, error code = " + response.getError().getErrorCode());
                        callErrorResponseCallback(response.getError().getErrorCode());
                    } else {
                        Log.d(TAG, "postlike -> Response " + response.toString());
                        GraphObject graphObject = response.getGraphObject();
                        boolean ret = (Boolean) graphObject.getProperty("FACEBOOK_NON_JSON_RESULT");
                        Log.d(TAG, " ret = " + ret);
                    }
                }
            });
            likeRequest.executeAsync();
        }

    }

    public void postComment(String comment, String id) {
        Log.d(TAG, "postComment " + comment + " for the post id " + id);
        Session activeSession = Session.getActiveSession();
        if (activeSession.getState().isOpened()) {
            String graphPath = String.format("%s/comments", id);
            Bundle bundle = new Bundle();
            bundle.putString("message", comment);
            Request commentRequest = new Request(activeSession, graphPath, bundle, HttpMethod.POST, new Request.Callback() {

                @Override
                public void onCompleted(Response response) {

                    if (response.getError() != null) {
                        Log.d(TAG, "postComment error,  Response " + response.toString());
                        callErrorResponseCallback(response.getError().getErrorCode());
                    } else {
                        Log.d(TAG, "postComment -> Response " + response.toString());
                    }
                }
            });

            commentRequest.executeAsync();

        }
    }

    @Override
    public void callResponseCallback(int requestType, ResponseData responseFacebook) {
        mResponseToManager.onResponse(GlobalConstant.SOCIAL_TYPE_FACEBOOK, requestType, responseFacebook, mCallbackToServie);
    }

    @Override
    public void callErrorResponseCallback(int error) {
        mResponseToManager.onErrorResponse(GlobalConstant.SOCIAL_TYPE_FACEBOOK, error, mCallbackToServie);
    }

    class FacebookHttpAsync extends AsyncTask<Void, Void, Integer> {
        private int mRequestType;
        private String mGraphPath;
        private HashMap<String, Object> mParams;
        private JSONObject mJSon;
        private int mFeedType;

        public FacebookHttpAsync(int requestType, int feedType, String graphPath, HashMap<String, Object> param) {
            this.mRequestType = requestType;
            mFeedType = feedType;
            this.mGraphPath = graphPath;
            this.mParams = param;
        }

        @Override
        protected Integer doInBackground(Void... params) {
            try {
                if (mRequestType == LOAD_MORE_COMMAND) {
                    Log.d(TAG, "LoadMore->grapthPath =  " + mGraphPath);
                    mJSon = requestLoadMore(mGraphPath);
                    Log.d(TAG, "mtuan.minh json = " + mJSon.toString());
                }
                /*best case: successful*/

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            Log.d(TAG, "httpgetFB onPostExecute");
            switch (mFeedType) {
            case FacebookConstant.FB_DATA_TYPE_FEED:
            case FacebookConstant.FB_DATA_TYPE_FEED_PHOTOS:
            case FacebookConstant.FB_DATA_TYPE_FEED_LINKS:
            case FacebookConstant.FB_DATA_TYPE_TIMELINE:
                Gson gson = new GsonBuilder().serializeNulls().create();
                if (mJSon == null) {
                    return;
                }
                FacebookFeeds newsFeed = gson.fromJson(mJSon.toString(), FacebookFeeds.class);

                ArrayList<FacebookFeedWrapper> tmpList = new ArrayList<FacebookFeedWrapper>();
                tmpList.addAll(newsFeed.getData());
                for (FacebookFeedWrapper feedItem : tmpList) {
                    // Analyze timelint data by story
                    String story = feedItem.story;
                    String type = feedItem.type;
                    Log.d(TAG, "nobita->in post execute, item story " + story + ", type = " + type + ", message = " + feedItem.getMessage());
                    if (type.compareTo("status") == 0 && story != null) {
                        newsFeed.getData().remove(feedItem);
                        continue;
                    }
                    String picture = feedItem.getPicture();
                    if (picture != null && picture.contains("_s.")) {
                        int indexOfSize = picture.lastIndexOf("s");
                        feedItem.setPicture(picture.substring(0, indexOfSize) + "n" + picture.substring(indexOfSize + 1, picture.length()));
                    }
                }

                ResponseData responseFacebook = new ResponseData(mFeedType);

                if (mFeedType == FacebookConstant.FB_DATA_TYPE_FEED_LINKS) {
                    FacebookFeeds feedLinks = new FacebookFeeds();
                    for (FacebookFeedWrapper item : newsFeed.getData()) {
                        Log.d(TAG, "nobita->in post execute item " + item.getName() + "     feeds.type = " + item.getType());
                        if (item.getType() == TypeTimeline.LINK || item.getType() == TypeTimeline.VIDEO) {
                            feedLinks.getData().add(item);
                        }
                    }
                    if (newsFeed.getPaging() != null) {
                        feedLinks.setPaging(new FacebookPaging(newsFeed.getPaging().getNextPage()));
                    }
                    responseFacebook.responseData = feedLinks;
                } else {
                    responseFacebook.responseData = newsFeed;
                }

                /*set type of refresh*/
                if (mParams != null && mParams.containsKey(GlobalConstant.TYPE_DATA_UPDATE)) {
                    boolean isRefresh = (Boolean) mParams.get(GlobalConstant.TYPE_DATA_UPDATE);
                    if (isRefresh) {
                        responseFacebook.isRequestUpdateRefresh = true;
                    } else {
                        responseFacebook.isRequestUpdateRefresh = false;
                    }
                }
                if (mParams != null && mParams.containsKey("userId")) {
                    responseFacebook.isMeRequest = false;
                }
                else {
                    responseFacebook.isMeRequest = true;
                }

                callResponseCallback(mFeedType, responseFacebook);

                break;

            default:
                break;
            }

        }
    }

    private JSONObject requestLoadMore(String urlString) throws IOException {
        JSONObject json = null;
        InputStream in = null;

        HttpParams params = new BasicHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, HTTP.DEFAULT_CONTENT_CHARSET);
        HttpProtocolParams.setUseExpectContinue(params, true);

        SchemeRegistry schReg = new SchemeRegistry();
        schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        schReg.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
        ClientConnectionManager conMgr = new ThreadSafeClientConnManager(params, schReg);
        HttpClient httpclient = new DefaultHttpClient(conMgr, params);

        String url = urlString;

        HttpResponse response = null;
        try {
            HttpGet httpget = new HttpGet(url);
            response = httpclient.execute(httpget);
        } catch (Exception ex) {
            Log.e(TAG, "Error getting http responce", ex);
            throw new IOException("Error connecting");
        }

        try {
            // Get hold of the response entity
            HttpEntity entity = null;
            if (response != null) {
                entity = response.getEntity();
            }

            // If the response does not enclose an entity, there is no need
            // to worry about connection release
            if (entity != null) {
                in = entity.getContent();
                String result = convertStreamToString(in);
                Log.d(TAG, "get response + " + result);
                try {
                    result = result.substring(result.indexOf("{"), result.lastIndexOf("}") + 1);
                    json = new JSONObject(result);
                } catch (StringIndexOutOfBoundsException e) {
                    Log.e(TAG, "HTTP GET failed to retrieve valid JSON result: \"" + result + "\"");
                    json = new JSONObject();
                    json.put("stat", "fail");
                    json.put("fail", "Failed to retrieve data");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        //httpclient.getConnectionManager().shutdown();
        return json;
    }

    private String convertStreamToString(InputStream is) {
        /*
         * To convert the InputStream to String we use the BufferedReader.readLine()
         * method. We iterate until the BufferedReader return null which means
         * there's no more data to read. Each line will appended to a StringBuilder
         * and returned as String.
         */
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }

}
