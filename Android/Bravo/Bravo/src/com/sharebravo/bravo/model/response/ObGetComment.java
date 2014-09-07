package com.sharebravo.bravo.model.response;

import java.util.HashMap;

import com.google.gson.annotations.SerializedName;
import com.sharebravo.bravo.model.response.ObBravo.SNS;

public class ObGetComment {
    @SerializedName("Comment_ID")
    public String           commentID;
    @SerializedName("Bravo_ID")
    public String           bravoID;
    @SerializedName("User_ID")
    public String           userID;
    @SerializedName("Full_Name")
    public String           fullName;
    @SerializedName("Profile_Img_URL")
    public String           profileImgUrl;
    @SerializedName("SNS_List")
    public HashMap<String, SNS> snsList     = new HashMap<String, ObBravo.SNS>();
    @SerializedName("Comment_Text")
    public String           commentText;
    @SerializedName("Date_Created")
    public Date_Created       dateCreated = new Date_Created();

    public ObGetComment() {
        // TODO Auto-generated constructor stub
    }

}
