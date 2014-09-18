package com.sharebravo.bravo.model.response;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class ObDeleteLike {
    @SerializedName("data")
    public ArrayList<String> data;
    @SerializedName("status")
    public int               status;
    @SerializedName("error")
    public String            error;

    public ObDeleteLike() {
        // TODO Auto-generated constructor stub
    }
}
