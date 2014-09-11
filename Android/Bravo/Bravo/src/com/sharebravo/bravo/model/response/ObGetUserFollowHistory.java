package com.sharebravo.bravo.model.response;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;
import com.sharebravo.bravo.model.response.ObBravo.SNS;

public class ObGetUserFollowHistory {
    @SerializedName("data")
    public ArrayList<Follow> data = new ArrayList<Follow>();
    public ObGetUserFollowHistory() {
        // TODO Auto-generated constructor stub
    }
}

class Follow {
    @SerializedName("Follow_ID")
    public String         followID;
    @SerializedName("Follower_ID")
    public String         followerID;
    @SerializedName("Follow_Name")
    public String         followName;
    @SerializedName("Follow_SNS_List")
    public ArrayList<SNS> followSNSList    = new ArrayList<SNS>();
    @SerializedName("Following_ID")
    public String         followingID;
    @SerializedName("Following_SNS_List")
    public ArrayList<SNS> followingSNSList = new ArrayList<SNS>();
    @SerializedName("Action")
    public String         action; //add or remove
    @SerializedName("Date_Created")
    public Date_Created     Date_Created;

    public Follow() {
        // TODO Auto-generated constructor stub
    }
}
