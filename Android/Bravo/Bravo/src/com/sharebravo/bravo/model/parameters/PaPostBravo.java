package com.sharebravo.bravo.model.parameters;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class PaPostBravo extends BasicParameter {
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
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("Bravo_type", bravoType));
        nameValuePairs.add(new BasicNameValuePair("Spot_ID", spotID));
        nameValuePairs.add(new BasicNameValuePair("Time_Zone", timeZone));
        nameValuePairs.add(new BasicNameValuePair("Is_Private", isPrivate ? "TRUE" : "FALSE"));
        nameValuePairs.add(new BasicNameValuePair("SNS_Post", snsPost));
        nameValuePairs.add(new BasicNameValuePair("FS_User_IS", fsUserID));
        nameValuePairs.add(new BasicNameValuePair("FS_Access_Token", fsAccessToken));
        nameValuePairs.add(new BasicNameValuePair("images", spotID));
        return nameValuePairs;
    }
}
