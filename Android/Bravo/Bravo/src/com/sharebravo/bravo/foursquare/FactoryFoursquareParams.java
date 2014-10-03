package com.sharebravo.bravo.foursquare;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class FactoryFoursquareParams {
    // public static final String client_id = "1PE10F3WC5LEFMNQ113TAKWJRR11AOPH33QZUE0VWDP3XDXD";
    // public static final String client_secret = "JM1AYBSTBDZX00H3Z5J2IS2QJQFFORUIWOMIL42NUYM0OXGR";
    // public static final String authen_token = "RR31UYN1IQJUDU5B3MC3QSJ10PCPJW2BGXFZIBQ3MVMVE3L5";
    public static final String client_id     = "WM4ZYMAHYHOFMYDJLP11A2ILREDZSJTF2NBW1QHZHZKKAVCW";
    public static final String client_secret = "HID5OW04WBSRECQQO4UCFJRKYCZ35MGUP5HNMSW53K5TWMQJ";
    public static final String authen_token  = "RR31UYN1IQJUDU5B3MC3QSJ10PCPJW2BGXFZIBQ3MVMVE3L5";
    public static final int    v             = 20131121;

    public static List<NameValuePair> createSubParamsRequest() {
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(new BasicNameValuePair("client_id", client_id));
        parameters.add(new BasicNameValuePair("client_secret", client_secret));
        parameters.add(new BasicNameValuePair("v", v + ""));
        return parameters;
    }

    public static List<NameValuePair> createSubParamsRequestSearchVenue(double _lat, double _long, String _query) {
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(new BasicNameValuePair("client_id", client_id));
        parameters.add(new BasicNameValuePair("client_secret", client_secret));
        parameters.add(new BasicNameValuePair("v", v + ""));
        parameters.add(new BasicNameValuePair("ll", _lat + "," + _long));
        parameters.add(new BasicNameValuePair("query", _query));
        parameters.add(new BasicNameValuePair("intent", "global"));
        parameters.add(new BasicNameValuePair("limit", "20"));
        return parameters;
    }

    public static List<NameValuePair> createSubParamsRequestSearchArroundMe(double _lat, double _long, String categoryIds) {
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(new BasicNameValuePair("client_id", client_id));
        parameters.add(new BasicNameValuePair("client_secret", client_secret));
        parameters.add(new BasicNameValuePair("v", v + ""));
        parameters.add(new BasicNameValuePair("intent", "browse"));
        parameters.add(new BasicNameValuePair("ll", _lat + "," + _long));
        parameters.add(new BasicNameValuePair("radius", "3000"));
        parameters.add(new BasicNameValuePair("categoryId", categoryIds));
        parameters.add(new BasicNameValuePair("limit", "20"));
        return parameters;
    }

    public static List<NameValuePair> createSubParamsRequestSearchArroundMeQuery(double _lat, double _long, String _query) {
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(new BasicNameValuePair("client_id", client_id));
        parameters.add(new BasicNameValuePair("client_secret", client_secret));
        parameters.add(new BasicNameValuePair("v", v + ""));
        parameters.add(new BasicNameValuePair("query", _query));
        parameters.add(new BasicNameValuePair("intent", "global"));
        parameters.add(new BasicNameValuePair("ll", _lat + "," + _long));
        parameters.add(new BasicNameValuePair("limit", "20"));
        return parameters;
    }

    public static List<NameValuePair> createSubParamsRequestAddVenue(double lat, double lng, String name, String address) {
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(new BasicNameValuePair("ll", lat + "," + lng));
        parameters.add(new BasicNameValuePair("address", address));
        parameters.add(new BasicNameValuePair("name", name));
        return parameters;
    }

    public static List<NameValuePair> createSubParamsRequestAddVenueDuplicate(double lat, double lng, String name, String address, String keyDuplicate) {
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(new BasicNameValuePair("ll", lat + "," + lng));
        parameters.add(new BasicNameValuePair("address", address));
        parameters.add(new BasicNameValuePair("name", name));
        parameters.add(new BasicNameValuePair("ignoreDuplicates", true + ""));
        parameters.add(new BasicNameValuePair("ignoreDuplicatesKey", keyDuplicate));
        return parameters;
    }
}
