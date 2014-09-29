package com.sharebravo.bravo.model.response;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;
import com.sharebravo.bravo.model.response.ObBravo.SNS;

public class SNSList {
    @SerializedName("key")
    public String         key;
    @SerializedName("SNSArrList")
    public ArrayList<SNS> snsArrList;

    public SNSList() {

    }

}
