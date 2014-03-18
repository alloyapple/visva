package com.visva.android.socialstackwidget.object.facebookobject;

import java.util.ArrayList;

import com.visva.android.socialstackwidget.request.SocialResponseBase;

public class FacebookPhotosWrapperObject extends SocialResponseBase{

    private ArrayList<FacebookPhotoObject> data = new ArrayList<FacebookPhotoObject>();
    private FacebookPagingObject paging;
    
    public ArrayList<FacebookPhotoObject> getData() {
        return data;
    }

    public void setData(ArrayList<FacebookPhotoObject> data) {
        this.data = data;
    }

    public FacebookPagingObject getPaging() {
        return paging;
    }

    public void setPaging(FacebookPagingObject paging) {
        this.paging = paging;
    }
          
}
