package com.sharebravo.bravo.model.response;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;
import com.sharebravo.bravo.model.response.ObGetUserBlocking.User;

public class ObGetUserFollowing {
    @SerializedName("data")
    public ArrayList<User> data = new ArrayList<User>();
    @SerializedName("status")
    public int                   status;
    @SerializedName("error")
    public String                error;

    public ObGetUserFollowing() {
        // TODO Auto-generated constructor stub
    }
}
