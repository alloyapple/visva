package com.sharebravo.bravo.model.response;

import com.google.gson.annotations.SerializedName;

public class ObUserImagePost {
    @SerializedName("data")
    public Data data;
    @SerializedName("status")
    public int  status;

    public ObUserImagePost() {

    }

    public class Data {
        @SerializedName("User_ID")
        public String User_ID;
        @SerializedName("Access_Token")
        public String Access_Token;
        @SerializedName("Profile_Img_URL")
        public String Profile_Img_URL;
        @SerializedName("Cover_Img_URL")
        public String Cover_Img_URL;

        public Data() {

        }
    }
}
