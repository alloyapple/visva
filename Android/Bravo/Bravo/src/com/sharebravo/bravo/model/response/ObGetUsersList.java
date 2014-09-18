package com.sharebravo.bravo.model.response;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;
import com.sharebravo.bravo.model.response.ObGetUserBlocking.User;

public class ObGetUsersList {
    @SerializedName("data")
    public ArrayList<User> data = new ArrayList<User>();
    @SerializedName("status")
    public int             status;
    @SerializedName("error")
    public String          error;

    public ObGetUsersList() {
        // TODO Auto-generated constructor stub
    }

}
