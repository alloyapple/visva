package com.sharebravo.bravo.model.response;

import com.google.gson.annotations.SerializedName;

public class ObPostBravo {
    @SerializedName("data")
    public Data data;
    @SerializedName("status")
    public int  status;

    public ObPostBravo() {
    }

    public class Data {
        @SerializedName("FS_Checkin_Bravo")
        public String FS_Checkin_Bravo;
        @SerializedName("Bravo_ID")
        public String Bravo_ID;

        public Data() {

        }
    }
}
