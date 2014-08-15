package com.samsung.android.alwayssocial.object.facebook;

import com.google.gson.annotations.SerializedName;

public class FacebookLikeInfo {
    @SerializedName("can_like")
    private boolean mCanLike;
    @SerializedName("like_count")
    private int mLikeCount;
    @SerializedName("user_likes")
    private boolean mUserLike;
    public boolean ismCanLike() {
        return mCanLike;
    }
    public void setmCanLike(boolean mCanLike) {
        this.mCanLike = mCanLike;
    }
    public int getmLikeCount() {
        return mLikeCount;
    }
    public void setmLikeCount(int mLikeCount) {
        this.mLikeCount = mLikeCount;
    }
    public boolean ismUserLike() {
        return mUserLike;
    }
    public void setmUserLike(boolean mUserLike) {
        this.mUserLike = mUserLike;
    }
    
    
}
