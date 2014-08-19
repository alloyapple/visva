package com.sharebravo.bravo.model.response;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class ObGetUserMylist {
    ArrayList<UserInfo> data = new ArrayList<UserInfo>();

    public ObGetUserMylist() {
        // TODO Auto-generated constructor stub
    }
}

class UserInfo {
    @SerializedName("Date_Created")
    String            bravoID;
    @SerializedName("Date_Created")
    String            userID;
    @SerializedName("Date_Created")
    ArrayList<SNS>    snsList     = new ArrayList<SNS>();
    String            profileImgUrl;
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
    ArrayList<String> bravoPics   = new ArrayList<String>();
    int               totalBravoPics;
    int               totalSavedUser;
    boolean           isTsurete;
    String            effectiveDate;
    DateObject        dateCreated = new DateObject();

    public UserInfo() {
        // TODO Auto-generated constructor stub
    }
}