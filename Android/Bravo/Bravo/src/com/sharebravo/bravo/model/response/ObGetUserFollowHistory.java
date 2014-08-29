package com.sharebravo.bravo.model.response;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class ObGetUserFollowHistory {
    ArrayList<Follow> data = new ArrayList<Follow>();

    public ObGetUserFollowHistory() {
        // TODO Auto-generated constructor stub
    }
}

class Follow {
    @SerializedName("Follow_ID")
    String         followID;
    @SerializedName("Follower_ID")
    String         followerID;
    @SerializedName("Follow_Name")
    String         followName;
    @SerializedName("Follow_SNS_List")
    ArrayList<SNS> followSNSList    = new ArrayList<SNS>();
    @SerializedName("Following_ID")
    String         followingID;
    @SerializedName("Following_SNS_List")
    ArrayList<SNS> followingSNSList = new ArrayList<SNS>();
    @SerializedName("Action")
    String         action;                                 // add or remove
    @SerializedName("Date_Created")
    Date_Created   Date_Created;

    public Follow() {
        // TODO Auto-generated constructor stub
    }
}
