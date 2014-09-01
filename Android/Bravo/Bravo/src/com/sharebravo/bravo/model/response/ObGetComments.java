package com.sharebravo.bravo.model.response;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class ObGetComments {
    @SerializedName("data")
    public ArrayList<ObGetComment> data = new ArrayList<ObGetComment>();
    @SerializedName("status")
    public int              status;

    public ObGetComments() {
        // TODO Auto-generated constructor stub
    }
}
