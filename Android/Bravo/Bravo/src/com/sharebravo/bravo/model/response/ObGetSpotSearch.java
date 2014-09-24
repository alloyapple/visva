package com.sharebravo.bravo.model.response;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class ObGetSpotSearch {
    @SerializedName("data")
    public ArrayList<Spot>   data;
    @SerializedName("status")
    public int    status;
    @SerializedName("error")
    public String error;

    public ObGetSpotSearch() {
        // TODO Auto-generated constructor stub
    }
}
