package com.sharebravo.bravo.model.response;

import java.util.ArrayList;

public class ObGetBravoSearch {
    ArrayList<SearchItem> data = new ArrayList<SearchItem>();

    public ObGetBravoSearch() {
        // TODO Auto-generated constructor stub
    }
}

class SearchItem {
    String     spotId;
    String     spotName;
    String     spotFID;
    String     spotSource;
    float      spotLongitude;
    float      spotLatitude;
    String     spotType;
    String     spotGenre;
    String     spotAddress;
    String     spotPhone;
    String     spotPrice;
    int        totalBravo;
    String     lastPic;
    String     lastPicBravoID;
    String     lastPicUserID;
    int        uniqueUsers;
    int        totalSavedUsers;
    DateObject dateCreated;

    public SearchItem() {
        // TODO Auto-generated constructor stub
    };
}