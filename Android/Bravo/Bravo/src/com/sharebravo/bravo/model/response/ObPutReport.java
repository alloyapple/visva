package com.sharebravo.bravo.model.response;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class ObPutReport {
    @SerializedName("data")
    public ArrayList<String> data;
    @SerializedName("status")
    public int    status;
    @SerializedName("error")
    public String error;
    public ObPutReport() {
        // TODO Auto-generated constructor stub
    }
}
