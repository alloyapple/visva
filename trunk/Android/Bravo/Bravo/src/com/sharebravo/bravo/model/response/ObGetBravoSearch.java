package com.sharebravo.bravo.model.response;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class ObGetBravoSearch {
    ArrayList<SearchItem> data = new ArrayList<SearchItem>();

    public ObGetBravoSearch() {
        // TODO Auto-generated constructor stub
    }
}

class SearchItem {
    @SerializedName("Spot_ID")
    String     spotId;
    @SerializedName("Spot_Name")
    String     spotName;
    @SerializedName("Spot_FID")
    String     spotFID;
    @SerializedName("Spot_Source")
    String     spotSource;
    @SerializedName("Spot_Logitude")
    float      spotLongitude;
    @SerializedName("Spot_Latitude")
    float      spotLatitude;
    @SerializedName("Spot_Type")
    String     spotType;
    @SerializedName("Spot_Genre")
    String     spotGenre;
    @SerializedName("Spot_Address")
    String     spotAddress;
    @SerializedName("Spot_Phone")
    String     spotPhone;
    @SerializedName("Spot_Price")
    String     spotPrice;
    @SerializedName("Total_Bravo")
    int        totalBravo;
    @SerializedName("Last_Pic")
    String     lastPic;
    @SerializedName("Last_Pic_Bravo_ID")
    String     lastPicBravoID;
    @SerializedName("Last_Pic_User_ID")
    String     lastPicUserID;
    @SerializedName("Unique_Users")
    int        uniqueUsers;
    @SerializedName("Total_Saved_Users")
    int        totalSavedUsers;
    @SerializedName("Date_Created")
    DateObject dateCreated;

    public SearchItem() {
        // TODO Auto-generated constructor stub
    };
}