package com.sharebravo.bravo.model.parameters;

import java.util.List;

import org.apache.http.NameValuePair;

public class PaGetSpotSearch extends BasicParameter{
    int     start;
    String  type;
    String  genre;
    String  FID;
    String  source;
    String  name;
    String  address;
    String  location;
    int bravoTotalOnly;

    public PaGetSpotSearch() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public List<NameValuePair> createNameValuePair() {
        // TODO Auto-generated method stub
        return null;
    }
}
