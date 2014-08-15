package com.sharebravo.bravo.model.response;

import java.util.ArrayList;

public class ObGetSpotHistory {
    ArrayList<SpotHistory> data = new ArrayList<SpotHistory>();

    public ObGetSpotHistory() {
        // TODO Auto-generated constructor stub
    }
}

class SpotHistory {
    String            userID;
    ArrayList<SNS>    snsList   = new ArrayList<SNS>();
    String            fullName;
    String            profileImgUrl;
    String            bravoID;
    ArrayList<String> bravoPics = new ArrayList<String>();
    int               totalBravoPics;
    int               totalComments;
    boolean           isTsurete;
    DateObject        dateCreate;

    public SpotHistory() {
        // TODO Auto-generated constructor stub
    }
}