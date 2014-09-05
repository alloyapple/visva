package com.sharebravo.bravo.model.response;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class ObGetFollowingCheck {
    @SerializedName("data")
    public ArrayList<String> data;
    @SerializedName("status")
    public int               status;
    @SerializedName("valid")
    public int valid;

    public ObGetFollowingCheck() {
        // TODO Auto-generated constructor stub
    }
}
