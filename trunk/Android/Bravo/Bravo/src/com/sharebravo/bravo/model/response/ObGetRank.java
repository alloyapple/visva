package com.sharebravo.bravo.model.response;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class ObGetRank {
    ArrayList<RankSpot> data= new ArrayList<RankSpot>();
    public ObGetRank() {
        // TODO Auto-generated constructor stub
    }
}

class RankSpot {
    @SerializedName("Spot_ID")
    String spotID;
    @SerializedName("Spot_Name")
    String spotName;
    @SerializedName("Total_Bravo")
    int    totalBravo;

    public RankSpot() {
        // TODO Auto-generated constructor stub
    }
}