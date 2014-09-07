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
    String Spot_ID;
    @SerializedName("Spot_Name")
    String Spot_Name;
    @SerializedName("Total")
    int    Total;

    public RankSpot() {
        // TODO Auto-generated constructor stub
    }
}