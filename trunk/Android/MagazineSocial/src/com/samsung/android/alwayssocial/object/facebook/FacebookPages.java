package com.samsung.android.alwayssocial.object.facebook;

import java.util.ArrayList;

import com.samsung.android.alwayssocial.object.ResponseBase;


public class FacebookPages extends ResponseBase{

    private ArrayList<FacebookPage> data = new ArrayList<FacebookPage>();
    
    private FacebookPaging paging;

    public ArrayList<FacebookPage> getData() {
        return data;
    }

    public void setData(ArrayList<FacebookPage> data) {
        this.data = data;
    }

    public FacebookPaging getPaging() {
        return paging;
    }

    public void setPaging(FacebookPaging paging) {
        this.paging = paging;
    }

}
