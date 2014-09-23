package com.sharebravo.bravo.foursquare.models;

import com.google.gson.annotations.SerializedName;

public class OFGetVenue {
    @SerializedName("meta")
    public Meta     meta;
    @SerializedName("response")
    public Response response;

    public class Meta {
        @SerializedName("code")
        public int code;
    }

    public class Response {
        @SerializedName("venue")
        public Venue venue;
    }

    public class Venue {
        @SerializedName("id")
        public String id;
        @SerializedName("canonicalUrl")
        public String canonicalUrl;
    }
}
