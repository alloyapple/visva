package com.sharebravo.bravo.model.response;

import java.util.ArrayList;

public class ObGetRank {
    ArrayList<RankSpot> data= new ArrayList<RankSpot>();
    public ObGetRank() {
        // TODO Auto-generated constructor stub
    }
}

class RankSpot {
    String spotID;
    String spotName;
    int    totalBravo;

    public RankSpot() {
        // TODO Auto-generated constructor stub
    }
}