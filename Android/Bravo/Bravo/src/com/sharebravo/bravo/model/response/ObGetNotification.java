package com.sharebravo.bravo.model.response;

import com.google.gson.annotations.SerializedName;

public class ObGetNotification {
    @SerializedName("Action_ID")
    String actionID;
    @SerializedName("Action_Type")
    String actionType;
    @SerializedName("User_ID")
    String userID;

    public ObGetNotification() {
        // TODO Auto-generated constructor stub
    }
}

class ActionUser {
}
