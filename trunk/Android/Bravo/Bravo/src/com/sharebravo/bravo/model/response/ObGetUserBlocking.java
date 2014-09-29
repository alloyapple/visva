package com.sharebravo.bravo.model.response;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.annotations.SerializedName;

public class ObGetUserBlocking {
    ArrayList<User> data = new ArrayList<User>();

    public ObGetUserBlocking() {
    }

    public class User {
        @SerializedName("User_ID")
        public String               User_ID;
        @SerializedName("SNS_List")
        public HashMap<String, SNS> SNS_List;
        @SerializedName("Full_Name")
        public String               Full_Name;
        @SerializedName("Profile_Img_URL")
        public String               Profile_Img_URL;

        public User() {
        }
    }
}
