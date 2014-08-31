package com.sharebravo.bravo.model.response;

import com.google.gson.annotations.SerializedName;

public class Date_Created {
    @SerializedName("sec")
    public int sec;
    @SerializedName("usec")
    public int usec;

    public Date_Created() {
        // TODO Auto-generated constructor stub
    }

    public int getSec() {
        return sec;
    }

    public int getUsec() {
        return usec;
    }
}
