package com.sharebravo.bravo.model.response;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class ObDeleteFollowing {
    @SerializedName("data")
    public ArrayList<String> data;
    @SerializedName("status")
    public int    status;
    @SerializedName("error")
    public String error;
    public ObDeleteFollowing() {
        // TODO Auto-generated constructor stub
    }
}
