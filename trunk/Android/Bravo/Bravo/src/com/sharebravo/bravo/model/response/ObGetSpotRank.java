package com.sharebravo.bravo.model.response;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.annotations.SerializedName;

public class ObGetSpotRank {
    @SerializedName("data")
    public ArrayList<SpotRank> data;
    @SerializedName("status")
    public int                 status;
    @SerializedName("error")
    public String              error;

    public ObGetSpotRank() {
        // TODO Auto-generated constructor stub
    }

    public class SpotRank {
        @SerializedName("User_ID")
        public String               userID;
        @SerializedName("SNS_List")
        public HashMap<String, SNS> snsList;
        @SerializedName("Full_Name")
        public String               fullName;
        @SerializedName("Profile_Img_URL")
        public String               profileImgUrl;
        @SerializedName("Total")
        public int                  total;
    }
}
