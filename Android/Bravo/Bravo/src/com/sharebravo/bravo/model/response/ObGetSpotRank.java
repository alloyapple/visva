package com.sharebravo.bravo.model.response;

import java.util.ArrayList;

public class ObGetSpotRank {
    ArrayList<SpotRank> data;

    public ObGetSpotRank() {
        // TODO Auto-generated constructor stub
    }
}

class SpotRank {
    String         userID;
    ArrayList<SNS> snsList = new ArrayList<SNS>();
    String         fullName;
    String         profileImgUrl;
    int            total;
}
