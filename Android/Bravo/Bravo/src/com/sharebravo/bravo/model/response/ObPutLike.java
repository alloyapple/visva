package com.sharebravo.bravo.model.response;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class ObPutLike {
    @SerializedName("data")
    public ArrayList<String> data;
    @SerializedName("status")
    public int    status;
    @SerializedName("error")
    public String error;
    public ObPutLike() {
        // TODO Auto-generated constructor stub
    }
}
