package com.sharebravo.bravo.model.response;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class ObGetTimeline {
    @SerializedName("data")
    public ArrayList<ObBravo> data;
    @SerializedName("status")
    public int                status;
    @SerializedName("error")
    public String error;

    public ObGetTimeline() {
        // TODO Auto-generated constructor stub
    }

}