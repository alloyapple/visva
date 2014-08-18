package com.sharebravo.bravo.model.parameters;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class PaGetUserList extends BasicParameter{
    int    start;
    String location;

    public PaGetUserList() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public List<NameValuePair> createNameValuePair() {
        // TODO Auto-generated method stub
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("Start", String.valueOf(start)));
        nameValuePairs.add(new BasicNameValuePair("Location", location));
        return nameValuePairs;
    }
}
