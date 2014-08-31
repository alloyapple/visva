package com.sharebravo.bravo.model.response;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;
import com.sharebravo.bravo.model.response.ObGetBravo.SNS;

public class ObGetTimeline {
    ArrayList<TimeLine> data = new ArrayList<TimeLine>();

    public ObGetTimeline() {
        // TODO Auto-generated constructor stub
    }

}

class TimeLine {
    @SerializedName("Bravo_ID")
    String            bravoID;
    @SerializedName("User_ID")
    String            userID;
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
    @SerializedName("Last_Pic")
    String            lastPic;
    @SerializedName("Last_Pic_Bravo_ID")
    String            lastPicBravoID;
    @SerializedName("Last_Pic_User_ID")
    String            lastPicUserID;
    @SerializedName("Is_Tsurete")
    boolean           isTsurete;
    @SerializedName("Time_Zone")
    String            timeZone;
    @SerializedName("Date_Created")
    Date_Created        Date_Created = new Date_Created();
}