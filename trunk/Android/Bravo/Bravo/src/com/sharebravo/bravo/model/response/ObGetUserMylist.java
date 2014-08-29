package com.sharebravo.bravo.model.response;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class ObGetUserMylist {
    ArrayList<UserInfo> data = new ArrayList<UserInfo>();

    public ObGetUserMylist() {
        // TODO Auto-generated constructor stub
    }
}

class UserInfo {
    @SerializedName("Bravo_ID")
    String            bravoID;
    @SerializedName("User_ID")
    String            userID;
    @SerializedName("SNS_List")
    ArrayList<SNS>    snsList     = new ArrayList<SNS>();
    @SerializedName("Profile_Img_URL")
    String            profileImgUrl;
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
    @SerializedName("Bravo_Pics")
    ArrayList<String> bravoPics   = new ArrayList<String>();
    @SerializedName("Total_Bravo_Pics")
    int               totalBravoPics;
    @SerializedName("Total_Saved_User")
    int               totalSavedUser;
    @SerializedName("Is_Tsurete")
    boolean           isTsurete;
    @SerializedName("Effective_Date")
    String            effectiveDate;
    @SerializedName("Date_Created")
    Date_Created        Date_Created;

    public UserInfo() {
        // TODO Auto-generated constructor stub
    }
}