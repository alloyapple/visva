package com.sharebravo.bravo.model.response;

import com.google.gson.annotations.SerializedName;

public class ObPostSpot {
    @SerializedName("data")
    public Data data;
    @SerializedName("status")
    public int  status;

    public class Data {

        @SerializedName("Spot_ID")
        public String Spot_ID;

    }
}
