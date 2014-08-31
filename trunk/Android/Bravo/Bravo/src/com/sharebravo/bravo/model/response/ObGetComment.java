package com.sharebravo.bravo.model.response;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;
import com.sharebravo.bravo.model.response.ObGetBravo.SNS;

public class ObGetComment {
    @SerializedName("Comment_ID")
    String           commentID;
    @SerializedName("Bravo_ID")
    String           bravoID;
    @SerializedName("User_ID")
    String           userID;
    @SerializedName("Full_Name")
    String           fullName;
    @SerializedName("Profile_Img_URL")
    String           profileImgUrl;
    @SerializedName("SNS_List")
    ArrayList<SNS> snsList     = new ArrayList<SNS>();
    @SerializedName("Comment_Text")
    String           commentText;
    @SerializedName("Date_Created")
    Date_Created       dateCreated = new Date_Created();

    public ObGetComment() {
        // TODO Auto-generated constructor stub
    }

}
