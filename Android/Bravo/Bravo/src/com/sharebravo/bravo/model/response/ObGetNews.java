package com.sharebravo.bravo.model.response;

import com.google.gson.annotations.SerializedName;

public class ObGetNews {
    @SerializedName("News_ID")
    String     newsId;
    @SerializedName("News_Content")
    String     newsContent;
    @SerializedName("News_Date")
    DateObject newsDate    = new DateObject();
    @SerializedName("Date_Created")
    DateObject dateCreated = new DateObject();

    public ObGetNews() {
        // TODO Auto-generated constructor stub
    }
}
