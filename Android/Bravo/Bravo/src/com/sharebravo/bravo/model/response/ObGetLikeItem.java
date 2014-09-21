package com.sharebravo.bravo.model.response;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class ObGetLikeItem {
    @SerializedName("data")
    public ArrayList<String> data;
    @SerializedName("status")
    public int               status;
    @SerializedName("valid")
    public int valid;

    public ObGetLikeItem() {
        // TODO Auto-generated constructor stub
    }
}
