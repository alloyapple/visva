package com.sharebravo.bravo.model.response;

import java.util.ArrayList;

public class ObGetTimeline {
    ArrayList<TimeLine> data = new ArrayList<TimeLine>();
    public ObGetTimeline() {
        // TODO Auto-generated constructor stub
    }

}

class TimeLine {
    String            bravoID;
    String            userID;
    ArrayList<SNS>    snsList     = new ArrayList<SNS>();
    ArrayList<String> bravoPics   = new ArrayList<String>();
    int               totalBravoPics;
    int               totalBravoComments;
    String            spotID;
    String            spotName;
    String            spotSource;
    String            spotFID;
    float             spotLongitude;
    float             spotLatitude;
    String            spotType;
    String            spotGenre;
    String            spotAddress;
    String            spotPhone;
    String            spotPrice;
    String            lastPic;
    String            lastPicBravoID;
    String            lastPicUserID;
    boolean           isTsurete;
    String            timeZone;
    DateObject        dateCreated = new DateObject();
}