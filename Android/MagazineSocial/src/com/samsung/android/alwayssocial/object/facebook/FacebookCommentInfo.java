package com.samsung.android.alwayssocial.object.facebook;

import com.google.gson.annotations.SerializedName;

public class FacebookCommentInfo {
    @SerializedName("can_comment")
    private boolean mcanComment;
    @SerializedName("comment_count")
    private int mCommentCount;
    @SerializedName("comment_order")
    private String mCommentOrder;

    public boolean isMcanComment() {
        return mcanComment;
    }

    public void setMcanComment(boolean mcanComment) {
        this.mcanComment = mcanComment;
    }

    public int getmCommentCount() {
        return mCommentCount;
    }

    public void setmCommentCount(int mCommentCount) {
        this.mCommentCount = mCommentCount;
    }

    public String getmCommentOrder() {
        return mCommentOrder;
    }

    public void setmCommentOrder(String mCommentOrder) {
        this.mCommentOrder = mCommentOrder;
    }

}
