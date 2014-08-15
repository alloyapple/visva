package com.samsung.android.alwayssocial.object.facebook;

import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.samsung.android.alwayssocial.object.ResponseBase;
import com.samsung.android.alwayssocial.object.facebook.FacebookFeedWrapper.Comment;
import com.samsung.android.alwayssocial.object.facebook.FacebookFeedWrapper.LikeCommentPaging;

public class FacebookCommentList extends ResponseBase{
    @SerializedName("data")
    List<Comment> data;

    public List<Comment> getData() {
        return data;
    }
    
    
    private LikeCommentPaging paging;

    public LikeCommentPaging getPaging() {
        return paging;
    }

    public void setPaging(LikeCommentPaging paging) {
        this.paging = paging;
    }

    public void setData(List<Comment> data) {
        this.data = data;
    }

    public int getTotalCount() {
        return null != data ? data.size() : 0;
    }
}
