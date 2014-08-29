package com.sharebravo.bravo.model.response;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class ObGetSpot {
    ArrayList<Spot> data = new ArrayList<Spot>();

    public ObGetSpot() {
        // TODO Auto-generated constructor stub
    }
}

class Spot {
    @SerializedName("Spot_ID")
    String       spotID;
    @SerializedName("Spot_Name")
    String       spotName;
    @SerializedName("Spot_Source")
    String       spotSource;
    @SerializedName("Spot_FID")
    String       spotFID;
    @SerializedName("Spot_Longitude")
    float        spotLongitude;
    @SerializedName("Spot_Latitude")
    float        spotLatitude;
    @SerializedName("Spot_Type")
    String       spotType;
    @SerializedName("Spot_Genre")
    String       spotGenre;
    @SerializedName("Spot_Address")
    String       spotAddress;
    @SerializedName("Spot_Phone")
    String       spotPhone;
    @SerializedName("Spot_Price")
    String       spotPrice;
    @SerializedName("Last_Pic")
    String       lastPic;
    @SerializedName("Last_Pic_Bravo_ID")
    String       lastPicBravoID;
    @SerializedName("Last_Pic_User_ID")
    String       lastPicUserID;
    @SerializedName("Total_Bravo")
    int          totalBravo;
    @SerializedName("Unique_Users")
    int          uniqueUsers;
    @SerializedName("Date_Created")
    Date_Created dateCreated;

    public Spot() {
        // TODO Auto-generated constructor stub
    }
}
