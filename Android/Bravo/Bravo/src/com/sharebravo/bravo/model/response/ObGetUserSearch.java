package com.sharebravo.bravo.model.response;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class ObGetUserSearch {
    @SerializedName("data")
    public ArrayList<UserSearch> data = new ArrayList<UserSearch>();
    @SerializedName("status")
    public int                   status;
    @SerializedName("error")
    public String                error;

    public ObGetUserSearch() {
        // TODO Auto-generated constructor stub
    }
}
