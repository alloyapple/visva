package com.sharebravo.bravo.model.response;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;
import com.sharebravo.bravo.model.response.ObGetBravo.SNS;

public class ObGetSpotHistory {
    ArrayList<SpotHistory> data = new ArrayList<SpotHistory>();

    public ObGetSpotHistory() {
        // TODO Auto-generated constructor stub
    }
}

class SpotHistory {
    @SerializedName("User_ID")
    String            userID;
    @SerializedName("SNS_List")
    ArrayList<SNS>    snsList   = new ArrayList<SNS>();
    @SerializedName("Full_Name")
    String            fullName;
    @SerializedName("Profile_Img_URL")
    String            profileImgUrl;
    @SerializedName("Bravo_ID")
    String            bravoID;
    @SerializedName("Bravo_Pics")
    ArrayList<String> bravoPics = new ArrayList<String>();
    @SerializedName("Total_Bravo_Pics")
    int               totalBravoPics;
    @SerializedName("Total_Comments")
    int               totalComments;
    @SerializedName("Is_Tsurete")
    boolean           isTsurete;
    @SerializedName("Date_Created")
    Date_Created      Date_Created;

    public SpotHistory() {
        // TODO Auto-generated constructor stub
    }
}