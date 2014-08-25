package com.sharebravo.bravo.model.response;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class ObPostUserFailed {
    @SerializedName("data")
    public ArrayList<Data> data;
    @SerializedName("status")
    public int             status;
    @SerializedName("error")
    public String          error;

    public ObPostUserFailed() {

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
