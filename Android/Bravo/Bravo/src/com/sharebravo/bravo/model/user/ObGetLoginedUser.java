package com.sharebravo.bravo.model.user;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class ObGetLoginedUser {
    @SerializedName("data")
    public ArrayList<Data> data;
    @SerializedName("status")
    public int             status;
    @SerializedName("error")
    public String          error;

    public ObGetLoginedUser() {

    }
}
