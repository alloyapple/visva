package com.sharebravo.bravo.model.response;

import com.google.gson.annotations.SerializedName;

public class ObPostUserSuccess {
    @SerializedName("data")
    public Data   data;
    @SerializedName("status")
    public int    status;
    @SerializedName("error")
    public String error;

    public ObPostUserSuccess() {

    }

    class Data {
        @SerializedName("User_ID")
        String User_ID;
        @SerializedName("Access_Token")
        String Access_Token;

        public Data() {

        }
    }
}
