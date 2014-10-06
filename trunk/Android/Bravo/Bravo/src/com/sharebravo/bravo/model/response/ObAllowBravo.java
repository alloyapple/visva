package com.sharebravo.bravo.model.response;

import com.google.gson.annotations.SerializedName;

public class ObAllowBravo {
    @SerializedName("data")
    public Data data;

    public ObAllowBravo() {

    }

    public class Data {
        @SerializedName("Allow_Bravo")
        public int Allow_Bravo;
        @SerializedName("status")
        public int status;
    }
}
