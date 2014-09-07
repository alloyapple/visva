package com.sharebravo.bravo.model.response;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class ObGetUserBlocking {
    ArrayList<User> data = new ArrayList<User>();

    public ObGetUserBlocking() {
    }

}

class User {
    @SerializedName("User_ID")
    public String         User_ID;
//    @SerializedName("SNS_List")
//    public ArrayList<SNS> SNS_List;
    @SerializedName("Full_Name")
    public String         Full_Name;
    @SerializedName("Profile_Img_URL")
    public String         Profile_Img_URL;

    public User() {
    }
}