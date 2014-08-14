package com.sharebravo.bravo.model.response;

import java.util.ArrayList;

public class ObGetBravo {
    String            bravoID;
    String            bravoType;
    String            userID;
    String            fullName;
    String            profileImgUrl;
    ArrayList<SNSID>  snsList   = new ArrayList<SNSID>();
    ArrayList<String> bravoPics = new ArrayList<String>();
    int               totalBravoPics;
    int               totalBravoComments;
    String            spotId;
    String            spotName;
    String            spotPID;
    String            spotSource;
    float             spotLongitude;
    float             spotLatitude;
    String            spotType;
    String            spotGenre;
    String            spotAddress;
    String            spotPhone;
    String            spotPrice;
    String            totalSavedUsers;
    boolean           isPrivate;
    boolean           isTsurete;
    int               totalTsurete;
    String            timeZone;
    DateObject        dateCreated;

    public ObGetBravo() {
        // TODO Auto-generated constructor stub
    }

}

class SNSID {
    String foreignSNS;
    String foreignID;

    public SNSID() {
        // TODO Auto-generated constructor stub
    }
}

class DateObject {
    int sec;
    int usec;

    public DateObject() {
        // TODO Auto-generated constructor stub
    }
}