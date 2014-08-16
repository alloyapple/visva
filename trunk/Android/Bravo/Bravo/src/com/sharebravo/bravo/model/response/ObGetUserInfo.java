package com.sharebravo.bravo.model.response;

import java.util.ArrayList;

public class ObGetUserInfo {
    String              userID;
    String              accessToken;
    ArrayList<SNS>      snsList    = new ArrayList<SNS>();
    String              fullName;
    String              aboutMe;
    String              profileImgUrl;
    String              coverImgUrl;
    boolean             allowBravo;
    int                 installAge;
    int                 totalBravo;
    int                 receivedBravo;
    int                 badgeNum;
    ArrayList<RankSpot> bravoRank  = new ArrayList<RankSpot>();
    int                 totalFollower;
    ArrayList<User>     followers  = new ArrayList<User>();
    int                 totalFollowing;
    ArrayList<Follow>   followings = new ArrayList<Follow>();
    boolean             isMylistPrivate;
    int                 totalMylist;

}
