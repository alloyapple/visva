package com.sharebravo.bravo.model.response;

import com.google.gson.annotations.SerializedName;

public class ObGetUserInfo {

    @SerializedName("status")
    public int  status;
    @SerializedName("data")
    public Data data;

    public class Data {
        @SerializedName("User_ID")
        public String User_ID;
        @SerializedName("New_Access_Token")
        public String New_Access_Token;
        // @SerializedName("SNS_List")
        // ArrayList<SNS> SNS_List = new ArrayList<SNS>();
        @SerializedName("Full_Name")
        public String Full_Name;
        // @SerializedName("About_Me")
        // public String About_Me;
        // @SerializedName("Profile_Img_URL")
        // public String Profile_Img_URL;
        // @SerializedName("Cover_Img_URL")
        // public String Cover_Img_URL;
        // @SerializedName("Allow_Bravo")
        // public boolean Allow_Bravo;
        // @SerializedName("Install_Age")
        // public int Install_Age;
        // @SerializedName("Total_Bravo")
        // public int Total_Bravo;
        // @SerializedName("Received_Bravo")
        // public int Received_Bravo;
        // @SerializedName("Badge_Num")
        // public int Badge_Num;
        // @SerializedName("Bravo_Rank")
        // ArrayList<RankSpot> Bravo_Rank = new ArrayList<RankSpot>();
        // @SerializedName("Total_Follower")
        // public int Total_Follower;
        // @SerializedName("Followers")
        // ArrayList<User> Followers = new ArrayList<User>();
        // @SerializedName("Total_Following")
        // public int Total_Following;
        // @SerializedName("Followings")
        // ArrayList<Follow> Followings = new ArrayList<Follow>();
        // @SerializedName("is_My_List_Privte")
        // public boolean is_My_List_Privte;
        // @SerializedName("Total_My_List")
        // public int Total_My_List;
    }
}
