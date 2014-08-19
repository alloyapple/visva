package com.sharebravo.bravo.model.response;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class ObGetUserInfo {
    @SerializedName("USer_ID")
    String              userID;
    @SerializedName("Access_Token")
    String              accessToken;
    @SerializedName("SNS_List")
    ArrayList<SNS>      snsList    = new ArrayList<SNS>();
    @SerializedName("Full_Name")
    String              fullName;
    @SerializedName("About_Me")
    String              aboutMe;
    @SerializedName("Profile_Img_URL")
    String              profileImgUrl;
    @SerializedName("Cover_Img_URL")
    String              coverImgUrl;
    @SerializedName("Allow_Bravo")
    boolean             allowBravo;
    @SerializedName("Install_Age")
    int                 installAge;
    @SerializedName("Total_Bravo")
    int                 totalBravo;
    @SerializedName("Received_Bravo")
    int                 receivedBravo;
    @SerializedName("Badge_Num")
    int                 badgeNum;
    @SerializedName("Bravo_Rank")
    ArrayList<RankSpot> bravoRank  = new ArrayList<RankSpot>();
    @SerializedName("Total_Follower")
    int                 totalFollower;
    @SerializedName("Followers")
    ArrayList<User>     followers  = new ArrayList<User>();
    @SerializedName("Total_Following")
    int                 totalFollowing;
    @SerializedName("Followings")
    ArrayList<Follow>   followings = new ArrayList<Follow>();
    @SerializedName("is_My_List_Privte")
    boolean             isMylistPrivate;
    @SerializedName("Total_My_List")
    int                 totalMylist;

}
