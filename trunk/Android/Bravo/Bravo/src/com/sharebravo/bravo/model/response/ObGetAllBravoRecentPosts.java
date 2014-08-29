package com.sharebravo.bravo.model.response;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class ObGetAllBravoRecentPosts {
    @SerializedName("data")
    public ArrayList<ObGetBravo> data;
    @SerializedName("status")
    public int                   status;

    public ObGetAllBravoRecentPosts() {

    }
}
