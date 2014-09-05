package com.sharebravo.bravo.model.response;

import com.google.gson.annotations.SerializedName;

public class ObGetBravo {
    @SerializedName("data")
    public ObBravo data;
    @SerializedName("status")
    public int                   status;

    public ObGetBravo() {

    }
}
