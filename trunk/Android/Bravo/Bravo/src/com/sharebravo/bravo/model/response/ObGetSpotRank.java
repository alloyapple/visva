package com.sharebravo.bravo.model.response;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class ObGetSpotRank {
    ArrayList<SpotRank> data;

    public ObGetSpotRank() {
        // TODO Auto-generated constructor stub
    }
}

class SpotRank {
    @SerializedName("User_ID")
    String         userID;
    @SerializedName("SNS_List")
    ArrayList<SNS> snsList = new ArrayList<SNS>();
    @SerializedName("Full_Name")
    String         fullName;
    @SerializedName("Profile_Img_URL")
    String         profileImgUrl;
    @SerializedName("Total")
    int            total;
}
