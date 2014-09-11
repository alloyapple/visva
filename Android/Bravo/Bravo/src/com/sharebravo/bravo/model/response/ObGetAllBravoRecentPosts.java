package com.sharebravo.bravo.model.response;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class ObGetAllBravoRecentPosts {
    @SerializedName("data")
    public ArrayList<ObBravo> data;
    @SerializedName("status")
    public int                status;
    @SerializedName("error")
    public String             error;

    public ObGetAllBravoRecentPosts() {

    }
}
