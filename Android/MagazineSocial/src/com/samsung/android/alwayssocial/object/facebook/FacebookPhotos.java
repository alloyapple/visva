package com.samsung.android.alwayssocial.object.facebook;

import java.util.ArrayList;

import com.samsung.android.alwayssocial.object.ResponseBase;

public class FacebookPhotos extends ResponseBase{

    private ArrayList<FacebookPhoto> data = new ArrayList<FacebookPhoto>();
    private FacebookPaging paging;
    
    public ArrayList<FacebookPhoto> getData() {
        return data;
    }

    public void setData(ArrayList<FacebookPhoto> data) {
        this.data = data;
    }

    public FacebookPaging getPaging() {
        return paging;
    }

    public void setPaging(FacebookPaging paging) {
        this.paging = paging;
    }
          
}
