package com.samsung.android.alwayssocial.servermanager;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;

import com.samsung.android.alwayssocial.constant.GlobalConstant;
import com.samsung.android.alwayssocial.object.ResponseData;
import com.samsung.android.alwayssocial.object.facebook.FacebookFeeds;
import com.samsung.android.alwayssocial.service.IResponseFromSNSCallback;

import android.util.Log;

public class AlwaysSocialManager {

    public static final String TAG = "AlwaysSocialManager";

    private static AlwaysSocialManager mInstance = null;
    private HashMap<String, AbstractSocial> mSocialMap = new LinkedHashMap<String, AbstractSocial>();

    public static AlwaysSocialManager getInstance()
    {
        if (mInstance == null)
        {
            mInstance = new AlwaysSocialManager();
        }
        return mInstance;
    }

    public void initializeSocialMap() {
        setTempDirectory(GlobalConstant.ALWAYS_FILE_PATH);
        if (!mSocialMap.isEmpty()) {
            Log.d(TAG, "initializeSocialMap map is not empty");
            return;
        }
        // Set the social map FACEBOOK, TWITTER, GOOGLEPLUS, LINKEDIN,
        // INSTAGRAM, FLICKR
        Log.d(TAG, "initializeSocialMap");
        addSocial(new Facebook(mRequestSNS));
        addSocial(new Twitter(mRequestSNS));

    }

    protected void addSocial(AbstractSocial social) {
        if (social == null) {
            Log.e(TAG, "fail to addSocial()");
            return;
        }
        Log.d(TAG, social.getSocialType());
        mSocialMap.put(social.getSocialType(), social);
    }

    public AbstractSocial getSocial(String type) {
        return mSocialMap.get(type);
    }

    public void setTempDirectory(String path) {
        File file = new File(path);
        if (file.exists())
            file.mkdirs();
    }

    // *********************************************************
    // Response from SNS Server
    // *********************************************************
    private IRequestSNS mRequestSNS = new IRequestSNS() {

        @Override
        public void onResponse(String socialType, int requestType, ResponseData responseFacebook, IResponseFromSNSCallback callback) {
            Log.d(TAG, "onResponse");
            callback.onResponse(socialType, requestType, responseFacebook);
        }

        @Override
        public void onErrorResponse(String socialType, int error, IResponseFromSNSCallback callback) {
            callback.onErrorResponse(socialType, error);
        }
    };

    public void requestSNS(String socialType, int requestDataType, HashMap<String, Object> param, IResponseFromSNSCallback callback) {
        Log.d(TAG, "RequestSNS Command - social type = " + socialType + " request type = " + requestDataType);
        mSocialMap.get(socialType).requestSNS(requestDataType, param, callback);
    }

    public void postSNS(String socialType, String id, String data, int postType, IResponseFromSNSCallback callback)
    {
        Log.d(TAG, "PostSNS Command - social type = " + socialType + " request type = " + postType);
        mSocialMap.get(socialType).postSNS(id, data, postType, callback);
    }
    
    /* Test image quality */
    public void getFacebookImagesInformation(int requestDataType, FacebookFeeds feeds, IResponseFromSNSCallback callback)
    {
        mSocialMap.get(GlobalConstant.SOCIAL_TYPE_FACEBOOK).getFacebookImagesInformation(requestDataType, feeds, callback);
    }

}
