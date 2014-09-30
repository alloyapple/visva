package com.sharebravo.bravo.model.response;

import com.google.gson.annotations.SerializedName;

public class SNS {
    @SerializedName("Foreign_SNS")
    public String foreignSNS;
    @SerializedName("Foreign_ID")
    public String foreignID;
    @SerializedName("Foreign_Access_Token")
    public String foreignAccessToken;
    @SerializedName("key")
    public String key;

    public SNS() {
    }
}
