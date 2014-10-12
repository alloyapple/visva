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
        public String                id;
        @SerializedName("name")
        public String                name;
        @SerializedName("location")
        public Location              location;
        @SerializedName("contact")
        public Contact               contact;
        @SerializedName("categories")
        public ArrayList<Categories> categories;
    }

    public class Location {
        @SerializedName("address")
        public String            address;
        @SerializedName("lat")
        public double            lat;
        @SerializedName("lng")
        public double            lon;
        @SerializedName("city")
        public String            city;
        @SerializedName("state")
        public String            state;
        @SerializedName("country")
        public String            country;
        @SerializedName("formattedAddress")
        public ArrayList<String> formattedAddress;
    }

    public class Contact {
        @SerializedName("phone")
        public String phone;

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
