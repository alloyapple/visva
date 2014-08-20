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
    @SerializedName("SNS_List")
    ArrayList<SNS> snsList = new ArrayList<SNS>();
    @SerializedName("Full_Name")
    String         fullName;
    @SerializedName("Profile_Img_URL")
    String         profileImgUrl;
    @SerializedName("Is_Blocking")
    boolean        isBlocking;
    @SerializedName("Is_Following")
    boolean        isFollowing;
    @SerializedName("Is_Followed")
    boolean        isFollowed;

    public UserSearch() {
        // TODO Auto-generated constructor stub
    }
}
