package com.sharebravo.bravo.model.response;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class ObGetUserSearch {
    ArrayList<UserSearch> data = new ArrayList<UserSearch>();

    public ObGetUserSearch() {
        // TODO Auto-generated constructor stub
    }
}

class UserSearch {
    @SerializedName("User_ID")
    String         userID;
    @SerializedName("Bravo_ID")
    ArrayList<SNS> snsList = new ArrayList<SNS>();
    @SerializedName("Bravo_ID")
    String         fullName;
    @SerializedName("Bravo_ID")
    String         profileImgUrl;
    @SerializedName("Bravo_ID")
    boolean        isBlocking;
    @SerializedName("Bravo_ID")
    boolean        isFollowing;
    @SerializedName("Bravo_ID")
    boolean        isFollowed;

    public UserSearch() {
        // TODO Auto-generated constructor stub
    }
}
