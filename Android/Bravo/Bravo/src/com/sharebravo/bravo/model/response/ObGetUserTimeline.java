package com.sharebravo.bravo.model.response;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;
import com.sharebravo.bravo.model.response.ObBravo.SNS;

public class ObGetUserTimeline {
    @SerializedName("data")
    public ArrayList<ObBravo> data;
    @SerializedName("status")
    public int                status;
    @SerializedName("error")
    public String error;

    public ObGetUserTimeline() {
        // TODO Auto-generated constructor stub
    }
}

/*class UserTimeLine {
    @SerializedName("Brovo_ID")
    String            bravoID;
    @SerializedName("User_ID")
    String            userID;
    @SerializedName("Full_Name")
    String            fullName;
    @SerializedName("Profile_Img_Url")
    String            profileImgUrl;
    @SerializedName("SNS_List")
    ArrayList<SNS>    snsList     = new ArrayList<SNS>();
    @SerializedName("Bravo_Pics")
    ArrayList<String> bravoPics   = new ArrayList<String>();
    @SerializedName("Total_Bravo_Pics")
    int               totalBravoPics;
    @SerializedName("Total_Bravo_Comments")
    int               totalBravoComments;
    @SerializedName("Spot_ID")
    String            spotID;
    @SerializedName("Spot_Name")
    String            spotName;
    @SerializedName("Spot_Source")
    String            spotSource;
    @SerializedName("Spot_FID")
    String            spotFID;
    @SerializedName("Spot_Longitude")
    float             spotLongitude;
    @SerializedName("Spot_Latitude")
    float             spotLatitude;
    @SerializedName("Spot_Type")
    String            spotType;
    @SerializedName("Spot_Genre")
    String            spotGenre;
    @SerializedName("Spot_Address")
    String            spotAddress;
    @SerializedName("Spot_Phone")
    String            spotPhone;
    @SerializedName("Spot_Price")
    String            spotPrice;
    @SerializedName("Total_Saved_Users")
    int               totalSavedUsers;
    @SerializedName("Date_Created")
    Date_Created        Date_Created = new Date_Created();

    public UserTimeLine() {
        // TODO Auto-generated constructor stub
    }
}*/