package com.sharebravo.bravo.model.response;

import java.util.HashMap;

import com.google.gson.annotations.SerializedName;
import com.sharebravo.bravo.model.response.ObBravo.SNS;

public class UserSearch {
    @SerializedName("User_ID")
    public String               userID;
    @SerializedName("SNS_List")
    public HashMap<String, SNS> snsList = new HashMap<String, SNS>();
    @SerializedName("Full_Name")
    public String               fullName;
    @SerializedName("Profile_Img_URL")
    public String               profileImgUrl;
    @SerializedName("Is_Blocked")
    public int              isBlocked;
    @SerializedName("Is_Following")
    public int              isFollowing;
    @SerializedName("Is_Followed")
    public int              isFollowed;

    public UserSearch() {
        // TODO Auto-generated constructor stub
    }
}