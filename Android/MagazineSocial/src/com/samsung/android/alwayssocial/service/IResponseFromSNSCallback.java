package com.samsung.android.alwayssocial.service;

import java.util.ArrayList;
import java.util.HashMap;

import com.samsung.android.alwayssocial.object.ResponseData;
import com.samsung.android.alwayssocial.object.facebook.FacebookFeedWrapper;
import com.samsung.android.alwayssocial.object.facebook.FacebookPhoto;

public interface IResponseFromSNSCallback {
    public void onResponse(String socialType, int requestType, ResponseData responseFacebook);
    public void onErrorResponse(String socialType, int error);
    
    /* For testing communication with MH -- Request Images quality callback */
    public void onImagesQualityResponse(String socialType, int requestType, HashMap<String, FacebookPhoto> photos, ArrayList<FacebookFeedWrapper> feeds);
}
