package com.sharebravo.bravo.model.parameters;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class PaGetSpotSearch extends BasicParameter {
    int    start;
    String type;
    String genre;
    String FID;
    String source;
    String name;
    String address;
    String location;
    int    bravoTotalOnly;

    public PaGetSpotSearch() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public List<NameValuePair> createNameValuePair() {
        // TODO Auto-generated method stub
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("Start", String.valueOf(start)));
        nameValuePairs.add(new BasicNameValuePair("Type", type));
        nameValuePairs.add(new BasicNameValuePair("Genre", genre));
        nameValuePairs.add(new BasicNameValuePair("FID", FID));
        nameValuePairs.add(new BasicNameValuePair("Source", source));
        nameValuePairs.add(new BasicNameValuePair("Name", name));
        nameValuePairs.add(new BasicNameValuePair("Address", address));
        nameValuePairs.add(new BasicNameValuePair("Location", location));
        nameValuePairs.add(new BasicNameValuePair("Bravo_Total_Only", String.valueOf(bravoTotalOnly)));
        return nameValuePairs;
    }
}
