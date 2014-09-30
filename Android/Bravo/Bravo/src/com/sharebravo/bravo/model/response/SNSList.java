package com.sharebravo.bravo.model.response;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class SNSList {
    @SerializedName("SNSArrList")
    public ArrayList<SNS> snsArrList;

    public SNSList() {

    }

}
