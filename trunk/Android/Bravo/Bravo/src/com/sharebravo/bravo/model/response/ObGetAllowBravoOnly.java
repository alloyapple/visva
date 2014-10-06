package com.sharebravo.bravo.model.response;

import com.google.gson.annotations.SerializedName;

public class ObGetAllowBravoOnly {

    @SerializedName("status")
    public int  status;
    @SerializedName("data")
    public Data data;

    public class Data {
        @SerializedName("Allow_Bravo")
        public int allow_bravo;
    }

}
