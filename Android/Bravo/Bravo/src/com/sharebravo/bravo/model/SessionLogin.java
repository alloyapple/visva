package com.sharebravo.bravo.model;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class SessionLogin {
    public String         userID;
    public String         accessToken;
    public String         aPNSTockens;

    public ArrayList<SNS> mSNSList;

    public SessionLogin() {
    }

    public String getUserId() {
        return userID;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getAPNsToken() {
        return aPNSTockens;
    }

    public ArrayList<SNS> getSNSList() {
        return mSNSList;
    }

    class SNS {
        @SerializedName("Foreign_SNS")
        public String foreignSNS;
        @SerializedName("Foreign_ID")
        public String foreignID;

        public SNS() {
        }
    }

}
