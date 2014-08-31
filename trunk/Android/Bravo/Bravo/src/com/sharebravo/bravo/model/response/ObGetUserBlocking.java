package com.sharebravo.bravo.model.response;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;
import com.sharebravo.bravo.model.response.ObGetBravo.SNS;

public class ObGetUserBlocking {
    ArrayList<User> data = new ArrayList<User>();

    public ObGetUserBlocking() {
        // TODO Auto-generated constructor stub
    }

}

class User {
    @SerializedName("USer_ID")
    String         userID;
    @SerializedName("SNS_List")
    ArrayList<SNS> snsList = new ArrayList<SNS>();
    @SerializedName("Full_Name")
    String         fullName;
    @SerializedName("Profile_Img_URL")
    String         profileImgUrl;

    public User() {
        // TODO Auto-generated constructor stub
    }
}