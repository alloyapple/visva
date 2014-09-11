package com.sharebravo.bravo.model.response;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.annotations.SerializedName;
import com.sharebravo.bravo.model.response.ObBravo.SNS;

public class ObGetUserInfo {

    @SerializedName("status")
    public int  status;
    @SerializedName("data")
    public Data data;

    public class Data {
        @SerializedName("User_ID")
        public String               User_ID;
        @SerializedName("Full_Name")
        public String               Full_Name;
        @SerializedName("SNS_List")
        public HashMap<String, SNS> SNS_List;
        @SerializedName("About_Me")
        public String               About_Me;
        @SerializedName("Profile_Img_URL")
        public String               Profile_Img_URL;
        @SerializedName("Cover_Img_URL")
        public String               Cover_Img_URL;
        @SerializedName("Allow_Bravo")
        public int                  Allow_Bravo;
        @SerializedName("Install_Age")
        public int                  Install_Age;
        @SerializedName("Total_Bravos")
        public int                  Total_Bravos;
        @SerializedName("Received_Bravo")
        public int                  Received_Bravo;
        @SerializedName("Badge_Num")
        public int                  Badge_Num;
        @SerializedName("Bravo_Rank")
        public ArrayList<RankSpot>  Bravo_Rank;
        @SerializedName("Total_Followers")
        public int                  Total_Followers;
        @SerializedName("Followers")
        public ArrayList<Follower>  Followers;
        @SerializedName("Total_Following")
        public int                  Total_Following;
        @SerializedName("Following")
        ArrayList<Follow>           Following = new ArrayList<Follow>();
        @SerializedName("is_My_List_Privte")
        public boolean              is_My_List_Privte;
        @SerializedName("Total_My_List")
        public int                  Total_My_List;
    }

    public class Follower {

        @SerializedName("User_ID")
        public String               User_ID;
        @SerializedName("SNS_List")
        public HashMap<String, SNS> SNS_List;
        @SerializedName("Full_Name")
        public String               Full_Name;
        @SerializedName("Profile_Img_URL")
        public String               Profile_Img_URL;

        public Follower() {
        }

    }
}
