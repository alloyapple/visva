package com.sharebravo.bravo.model.response;

import java.util.ArrayList;

public class ObGetUserFollowHistory {
    ArrayList<Follow> data = new ArrayList<Follow>();
    public ObGetUserFollowHistory() {
        // TODO Auto-generated constructor stub
    }
}

class Follow {
    String         followID;
    String         followerID;
    String         followName;
    ArrayList<SNS> followSNSList    = new ArrayList<SNS>();
    String         followingID;
    ArrayList<SNS> followingSNSList = new ArrayList<SNS>();
    String         action; //add or remove
    DateObject     dateCreated;

    public Follow() {
        // TODO Auto-generated constructor stub
    }
}
