package com.samsung.android.alwayssocial.object;

public class SocialUserObject {
    public String mImageUrl;
    public String mId;
    public String mName;
    public SocialUserObject(String id, String name, String url){
        mId = id;
        mName = name;
        mImageUrl = url;
    }
    // For facebook no need to have imageUrl
    public SocialUserObject(String id, String name){
        mId = id;
        mName = name;
    }
    public SocialUserObject() {
    }
}
