package com.sharebravo.bravo.model.response;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class ObGetBlockingCheck {
    @SerializedName("data")
    public ArrayList<String> data;
    @SerializedName("status")
    public int               status;
    @SerializedName("valid")
    public int               valid;

    public ObGetBlockingCheck() {
        // TODO Auto-generated constructor stub
    }
}
