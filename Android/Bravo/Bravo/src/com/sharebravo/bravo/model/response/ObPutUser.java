package com.sharebravo.bravo.model.response;

import com.google.gson.annotations.SerializedName;

public class ObPutUser {
    @SerializedName("User_ID")
    String userID;
    @SerializedName("Access_Token")
    String accessToken;
    @SerializedName("Profile_Img_URL")
    String profileImgUrl;
    @SerializedName("Cover_Img_URL")
    String coverImgUrl;

    public ObPutUser() {
        // TODO Auto-generated constructor stub
    }
}
