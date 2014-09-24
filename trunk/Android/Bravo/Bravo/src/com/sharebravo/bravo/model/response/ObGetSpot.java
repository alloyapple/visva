package com.sharebravo.bravo.model.response;

import com.google.gson.annotations.SerializedName;

public class ObGetSpot {
    @SerializedName("data")
    public Spot   data;
    @SerializedName("status")
    public int    status;
    @SerializedName("error")
    public String error;

    public ObGetSpot() {
        // TODO Auto-generated constructor stub
    }

}
