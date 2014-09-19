package com.sharebravo.bravo.model.response;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.annotations.SerializedName;
import com.sharebravo.bravo.model.response.ObBravo.SNS;

public class ObGetNotification {
    @SerializedName("data")
    public Notification data;
    @SerializedName("status")
    public int          status;

    public class Notification {
        @SerializedName("Notification_ID")
        public String                        Notification_ID;
        @SerializedName("Notification_Type")
        public String                        Notification_Type;
        @SerializedName("User_ID")
        public String                        User_ID;
        @SerializedName("Bravo_ID")
        public String                        Bravo_ID;
        @SerializedName("Spot_ID")
        public String                        Spot_ID;
        @SerializedName("Spot_Name")
        public String                        Spot_Name;
        @SerializedName("Date_Created")
        public Date_Created                  Date_Created;
        @SerializedName("Notification_Users")
        public ArrayList<Notification_Users> Notification_Users;
        @SerializedName("Date_Updated")
        public Date_Updated                  Date_Updated;
        @SerializedName("Last_Commenter_ID")
        public String                        Last_Commenter_ID;
        @SerializedName("Last_Commenter_Name")
        public String                        Last_Commenter_Name;
        @SerializedName("Last_Commenter_Pic")
        public String                        Last_Commenter_Pic;
        @SerializedName("Total_Commenters")
        public int                           Total_Commenters;
    }

    public class Notification_Users {
        @SerializedName("User_ID")
        public String               User_ID;
        @SerializedName("Full_Name")
        public String               Full_Name;
        @SerializedName("Profile_Img_URL")
        public String               Profile_Img_URL;
        @SerializedName("SNS_List")
        public HashMap<String, SNS> SNS_List;
        @SerializedName("Date_Added")
        public Date_Added Date_Added;
    }

    public class Date_Added {
        @SerializedName("sec")
        public long sec;
        @SerializedName("usec")
        public long usec;
    }

    public class Date_Updated {
        @SerializedName("sec")
        public long sec;
        @SerializedName("usec")
        public long usec;
    }

}
