package com.sharebravo.bravo.model.parameters;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class PaGetBravoSearch extends BasicParameter {
    int     start;
    String  type;
    String  genre;
    String  FID;
    String  source;
    String  name;
    String  address;
    String  location;
    String  minBravoID;
    String  maxBravoID;
    boolean viewDeletedUsers;
    boolean global;

    public PaGetBravoSearch() {
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
        nameValuePairs.add(new BasicNameValuePair("Min_Bravo_ID", minBravoID));
        nameValuePairs.add(new BasicNameValuePair("Max_Bravo_ID", maxBravoID));
        nameValuePairs.add(new BasicNameValuePair("View_Deleted_Users", viewDeletedUsers ? "1" : "0"));
        nameValuePairs.add(new BasicNameValuePair("Global", global ? "TRUE" : "FALSE"));
        return nameValuePairs;
    }
}
