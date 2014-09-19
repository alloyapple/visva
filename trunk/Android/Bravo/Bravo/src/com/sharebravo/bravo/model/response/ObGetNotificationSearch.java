package com.sharebravo.bravo.model.response;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;
import com.sharebravo.bravo.model.response.ObGetNotification.Notification;

public class ObGetNotificationSearch {
    @SerializedName("data")
    public ArrayList<Notification> data;
    @SerializedName("status")
    public int             status;
    @SerializedName("error")
    public String             error;
}
