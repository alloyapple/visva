package com.sharebravo.bravo.foursquare.models;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class OFGetVenueSearch {
    @SerializedName("meta")
    public Meta     meta;
    @SerializedName("response")
    public Response response;

    public class Meta {
        @SerializedName("code")
        public int code;
    }

    public class Response {
        @SerializedName("venues")
        public ArrayList<Venue> venues;
    }

    public class Venue {
        @SerializedName("id")
        public String     id;
        @SerializedName("categories")
        public ArrayList<Categories> categories;
    }

    public class Categories {
        @SerializedName("id")
        public String id;
        @SerializedName("icon")
        public Icon   icon;
        @SerializedName("name")
        public String name;
    }

    public class Icon {
        @SerializedName("prefix")
        public String prefix;
        @SerializedName("suffix")
        public String suffix;
    }
}
