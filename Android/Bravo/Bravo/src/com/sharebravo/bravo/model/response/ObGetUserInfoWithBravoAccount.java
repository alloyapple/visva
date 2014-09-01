package com.sharebravo.bravo.model.response;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class ObGetUserInfoWithBravoAccount {
    @SerializedName("data")
    public ArrayList<Data> data;
    @SerializedName("status")
    public int             status;

    public ObGetUserInfoWithBravoAccount() {
    }

    public class Data {
        @SerializedName("User_ID")
        public String User_ID;
        @SerializedName("Access_Token")
        public String Access_Token;
        @SerializedName("Full_Name")
        public String Full_Name;
        @SerializedName("Profile_Img_URL")
        public String Profile_Img_URL;
    }
}
