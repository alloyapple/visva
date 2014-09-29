package com.sharebravo.bravo.model.response;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.annotations.SerializedName;

public class ObGetSpotHistory {
    @SerializedName("data")
    public ArrayList<SpotHistory> data = new ArrayList<SpotHistory>();
    @SerializedName("status")
    public int                    status;
    @SerializedName("error")
    public String                 error;

    public ObGetSpotHistory() {
        // TODO Auto-generated constructor stub
    }
    public class SpotHistory {
        @SerializedName("User_ID")
        public String               userID;
        @SerializedName("SNS_List")
        public HashMap<String, SNS> snsList   = new HashMap<String, SNS>();
        @SerializedName("Full_Name")
        public String               fullName;
        @SerializedName("Profile_Img_URL")
        public String               profileImgUrl;
        @SerializedName("Bravo_ID")
        public String               bravoID;
        @SerializedName("Bravo_Pics")
        public ArrayList<String>    bravoPics = new ArrayList<String>();
        @SerializedName("Total_Bravo_Pics")
        public int                  totalBravoPics;
        @SerializedName("Total_Comments")
        public int                  totalComments;
        @SerializedName("Is_Tsurete")
        public int              isTsurete;
        @SerializedName("Date_Created")
        public Date_Created         Date_Created;

        public SpotHistory() {
            // TODO Auto-generated constructor stub
        }
    }
}

