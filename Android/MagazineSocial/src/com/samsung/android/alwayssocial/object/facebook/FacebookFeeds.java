package com.samsung.android.alwayssocial.object.facebook;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;
import com.samsung.android.alwayssocial.object.ResponseBase;

public class FacebookFeeds extends ResponseBase {
    @SerializedName("data")
    private ArrayList<FacebookFeedWrapper> data;

    @SerializedName("paging")
    private FacebookPaging paging;

    public ArrayList<FacebookFeedWrapper> getData() {
        return data;
    }

    public void setData(ArrayList<FacebookFeedWrapper> data) {
        this.data = data;
    }

    public FacebookPaging getPaging() {
        return paging;
    }

    public void setPaging(FacebookPaging paging) {
        this.paging = paging;
    }

    public FacebookFeeds() {
        data = new ArrayList<FacebookFeedWrapper>();
    }
}
