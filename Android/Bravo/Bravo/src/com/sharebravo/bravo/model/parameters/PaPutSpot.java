package com.sharebravo.bravo.model.parameters;

import java.util.List;

import org.apache.http.NameValuePair;

public class PaPutSpot extends BasicParameter{
    String spotName;
    String spotFID;
    String spotSource;
    float  spotLongitude;
    float  spotLatitude;
    String spotType;
    String spotGenre;
    String spotAddress;
    String spotPhone;
    String spotPrice;

    public PaPutSpot() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public List<NameValuePair> createNameValuePair() {
        // TODO Auto-generated method stub
        return null;
    }
}
