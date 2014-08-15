package com.sharebravo.bravo.model.response;

import java.util.ArrayList;

public class ObGetSpot {
    ArrayList<Spot> data = new ArrayList<Spot>();
    public ObGetSpot() {
        // TODO Auto-generated constructor stub
    }
}

class Spot {
    String     spotID;
    String     spotName;
    String     spotSource;
    String     spotFID;
    float      spotLongitude;
    float      spotLatitude;
    String     spotType;
    String     spotGenre;
    String     spotAddress;
    String     spotPhone;
    String     spotPrice;
    String     lastPic;
    String     lastPicBravoID;
    String     lastPicUserID;
    int        totalBravo;
    int        uniqueUsers;
    DateObject dateCreated = new DateObject();

    public Spot() {
        // TODO Auto-generated constructor stub
    }
}
