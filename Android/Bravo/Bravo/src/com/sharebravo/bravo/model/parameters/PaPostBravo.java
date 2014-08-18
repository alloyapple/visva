package com.sharebravo.bravo.model.parameters;

import java.util.List;

import org.apache.http.NameValuePair;

public class PaPostBravo extends BasicParameter{
    String  bravoType;
    String  spotID;
    String  timeZone;
    boolean isPrivate;
    String  snsPost;
    String  fsUserID;
    String  fsAccessToken;
    int[]   image;

    public PaPostBravo() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public List<NameValuePair> createNameValuePair() {
        // TODO Auto-generated method stub
        return null;
    }
}
