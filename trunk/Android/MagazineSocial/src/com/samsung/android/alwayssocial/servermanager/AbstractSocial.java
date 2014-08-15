package com.samsung.android.alwayssocial.servermanager;

import java.util.HashMap;

import com.samsung.android.alwayssocial.object.ResponseData;
import com.samsung.android.alwayssocial.object.facebook.FacebookFeeds;
import com.samsung.android.alwayssocial.service.IResponseFromSNSCallback;

public abstract class AbstractSocial {
    private String mSocialType;
    protected IRequestSNS mResponseToManager;
    
    /*
	public abstract void login();
	public abstract void logout();
	public abstract void getFriendList();
	public abstract void getUserProfile();
	public abstract void getUserData();
	public abstract void getGroups(HashMap<String, Object> param);
	public abstract void getLikedPages(HashMap<String, Object> param);
	public abstract void getMyFeedList();
    public abstract void getFeedList(HashMap<String, Object> param);
    public abstract void getFeedList(String userId);
    public abstract void getTaggedMe(HashMap<String, Object> param); 
    public abstract void getFeedPhotos(HashMap<String, Object> param);
    public abstract void getLinksOnly(HashMap<String, Object> param);
    public abstract void getTimeline(HashMap<String, Object> param);
    public abstract void getLikeInfo(HashMap<String, Object> param);
    public abstract void getFriendsGroup();
    public abstract void postMessage(String id, String data, int postType);
    */
    
    public abstract void requestSNS(int requestType,  HashMap<String, Object> param, IResponseFromSNSCallback callback);
    public abstract void postSNS(String id, String data, int postType, IResponseFromSNSCallback callback);
    public abstract void getFacebookImagesInformation(int requestDataType, FacebookFeeds feeds, IResponseFromSNSCallback callback);
    
    public abstract void setRequestDataListener(IRequestSNS listener);
    public abstract void callResponseCallback(int requestType, ResponseData responseFacebook);
    public abstract void callErrorResponseCallback(int error);
  	
    public AbstractSocial(String type) {
        mSocialType = type;
    }
    
    public AbstractSocial(String type, IRequestSNS listener)
    {
        mSocialType = type;
        mResponseToManager = listener;
    }
    
    public String getSocialType()
    {
        return mSocialType;
    }
}
