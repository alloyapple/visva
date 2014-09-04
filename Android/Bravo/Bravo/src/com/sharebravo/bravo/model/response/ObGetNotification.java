package com.sharebravo.bravo.model.response;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class ObGetNotification {
    @SerializedName("Action_ID")
    public ArrayList<Data> data;
    @SerializedName("status")
    public int             status;

    public ObGetNotification() {
    }
}

class Data {
    @SerializedName("Action_ID")
    public String                  Action_ID;
    @SerializedName("Action_Type")
    public String                  Action_Type;
    @SerializedName("User_ID")
    public String                  Bravo_ID;
    @SerializedName("Bravo_ID")
    public String                  Spot_ID;
    @SerializedName("Spot_ID")
    public String                  Spot_Name;
    @SerializedName("Date_Created")
    public Date_Created            Date_Created;
    @SerializedName("Action_Users")
    public ArrayList<Action_Users> Action_Users;
    @SerializedName("Date_Updated")
    public Date_Updated            Date_Updated;
}

class Action_Users {
    @SerializedName("User_ID")
    public String              User_ID;
    @SerializedName("Full_Name")
    public String              Full_Name;
    @SerializedName("Profile_Img_URL")
    public String              Profile_Img_URL;
    @SerializedName("SNS_List")
    public ArrayList<SNS_List> SNS_List;
    @SerializedName("Date_Added")
    public ArrayList<SNS_List> Date_Added;
}

class SNS_List {
    @SerializedName("Foreign_SNS")
    public String Foreign_SNS;
    @SerializedName("Foreign_ID")
    public String Foreign_ID;
}

class Date_Added {
    @SerializedName("sec")
    public long sec;
    @SerializedName("usec")
    public long usec;
}

class Date_Updated {
    @SerializedName("sec")
    public long sec;
    @SerializedName("usec")
    public long usec;
}
