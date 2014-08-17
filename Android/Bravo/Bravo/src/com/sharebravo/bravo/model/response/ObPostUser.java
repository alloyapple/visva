package com.sharebravo.bravo.model.response;

import com.google.gson.annotations.SerializedName;

public class ObPostUser {
    @SerializedName("User_ID")
    String userID;
    @SerializedName("Access_Token")
    String accessToken;

    public ObPostUser() {
        // TODO Auto-generated constructor stub
    }
}
