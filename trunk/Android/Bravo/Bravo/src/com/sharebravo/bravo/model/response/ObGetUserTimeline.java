package com.sharebravo.bravo.model.response;

import java.util.ArrayList;

public class ObGetUserTimeline {
    ArrayList<UserTimeLine> data = new ArrayList<UserTimeLine>();

    public ObGetUserTimeline() {
        // TODO Auto-generated constructor stub
    }
}

class UserTimeLine {
    String            bravoID;
    String            userID;
    String            fullName;
    String            profileImgUrl;
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
    int               totalSavedUsers;
    DateObject        dateCreated = new DateObject();

    public UserTimeLine() {
        // TODO Auto-generated constructor stub
    }
}