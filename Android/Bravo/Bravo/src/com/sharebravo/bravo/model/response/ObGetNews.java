package com.sharebravo.bravo.model.response;

import com.google.gson.annotations.SerializedName;

public class ObGetNews {
    @SerializedName("News_ID")
    String     newsId;
    @SerializedName("News_Content")
    String     newsContent;
    @SerializedName("News_Date")
    Date_Created newsDate    = new Date_Created();
    @SerializedName("Date_Created")
    Date_Created dateCreated = new Date_Created();

    public ObGetNews() {
        // TODO Auto-generated constructor stub
    }
}
