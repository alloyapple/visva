package com.sharebravo.bravo.model.response;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class ObGetBravo {
    @SerializedName("Bravo_ID")
    String            bravoID;
    @SerializedName("Bravo_Type")
    String            bravoType;
    @SerializedName("User_ID")
    String            userID;
    @SerializedName("Full_Name")
    String            fullName;
    @SerializedName("Profile_Img_URL")
    String            profileImgUrl;
    @SerializedName("SNS_List")
    ArrayList<SNS>    snsList   = new ArrayList<SNS>();
    @SerializedName("Bravo_Pics")
    ArrayList<String> bravoPics = new ArrayList<String>();
    @SerializedName("Total_Bravo_Pics")
    int               totalBravoPics;
    @SerializedName("Total_Comments")
    int               totalComments;
    @SerializedName("Spot_ID")
    String            spotId;
    @SerializedName("Spot_Name")
    String            spotName;
    @SerializedName("Spot_PID")
    String            spotPID;
    @SerializedName("Spot_Source")
    String            spotSource;
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
    String            totalSavedUsers;
    @SerializedName("Is_Private")
    boolean           isPrivate;
    @SerializedName("Is_Tsurete")
    boolean           isTsurete;
    @SerializedName("Total_Tsurete")
    int               totalTsurete;
    @SerializedName("Time_Zone")
    String            timeZone;
    @SerializedName("Date_Created")
    DateObject        dateCreated;

    public ObGetBravo() {
        // TODO Auto-generated constructor stub
    }

}

class SNS {
    @SerializedName("Foreign_SNS")
    String foreignSNS;
    @SerializedName("Foreign_ID")
    String foreignID;

    public SNS() {
        // TODO Auto-generated constructor stub
    }
}

class DateObject {
    @SerializedName("sec")
    int sec;
    @SerializedName("usec")
    int usec;

    public DateObject() {
        // TODO Auto-generated constructor stub
    }
}