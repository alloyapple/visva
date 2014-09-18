package com.sharebravo.bravo.model.response;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class ObGetSpotTimeline {
    @SerializedName("data")
    public ArrayList<SpotTimeline> data;
    @SerializedName("status")
    public int                     status;
    @SerializedName("error")
    public String                  error;

    public ObGetSpotTimeline() {
        // TODO Auto-generated constructor stub
    }

    public class SpotTimeline {
        @SerializedName("Spot_Name")
        public String Spot_Name;
        @SerializedName("Spot_Latitude")
        public double Spot_Latitude;
        @SerializedName("Spot_Longitude")
        public double Spot_Longitude;
        @SerializedName("Bravo_ID")
        public String Bravo_ID;
        @SerializedName("Total_Bravo_Pics")
        public String Total_Bravo_Pics;
        @SerializedName("Total_Saved_Users")
        public String Total_Saved_Users;
    }
}
