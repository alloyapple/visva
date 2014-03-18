package com.visva.android.socialstackwidget.object.facebookobject;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;
import com.visva.android.socialstackwidget.request.SocialResponseBase;

public class FacebookFeedsObject extends SocialResponseBase {
    @SerializedName("data")
    private ArrayList<FacebookFeedWrapperObject> data;

    @SerializedName("paging")
    private FacebookPagingObject paging;

    public ArrayList<FacebookFeedWrapperObject> getData() {
        return data;
    }

    public void setData(ArrayList<FacebookFeedWrapperObject> data) {
        this.data = data;
    }

    public FacebookPagingObject getPaging() {
        return paging;
    }

    public void setPaging(FacebookPagingObject paging) {
        this.paging = paging;
    }

    public FacebookFeedsObject() {
        data = new ArrayList<FacebookFeedWrapperObject>();
    }
}
