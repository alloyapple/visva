package com.samsung.android.alwayssocial.object.facebook;

import com.google.gson.annotations.SerializedName;
import com.samsung.android.alwayssocial.object.ResponseBase;

public class FacebookSummary extends ResponseBase{
    /*
     * canLike and isUserLiked detect
     * 0: default value, means error occurs when getting infor from sns
     * 1: false
     * 2: true
     */
    public int canLike;
    public int isUserLiked;
    
    public FacebookSummary(){
        canLike = 0;
        isUserLiked = 0;
        total_count = 0;
    }
    @SerializedName("total_count")
    private int total_count;

    public int getTotal_count() {
        return total_count;
    }

    public void setTotal_count(int total_count) {
        this.total_count = total_count;
    }
    
}
