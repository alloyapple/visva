package com.sharebravo.bravo.model.user;

import com.google.gson.annotations.SerializedName;

public class Data {
    @SerializedName("User_ID")
    public String User_ID;
    @SerializedName("Access_Token")
    public String Access_Token;

    public Data() {

    }
}