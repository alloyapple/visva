package com.sharebravo.bravo.model.response;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.annotations.SerializedName;

public class ObBravo {
    @SerializedName("Bravo_ID")
    public String               Bravo_ID;
    @SerializedName("User_ID")
    public String               User_ID;
    @SerializedName("Full_Name")
    public String               Full_Name;
    @SerializedName("Profile_Img_URL")
    public String               Profile_Img_URL;
    @SerializedName("SNS_List")
    public HashMap<String, SNS> SNS_List   = new HashMap<String, SNS>();
    @SerializedName("Bravo_Pics")
    public ArrayList<String>    Bravo_Pics = new ArrayList<String>();
    @SerializedName("Total_Bravo_Pics")
    public int                  Total_Bravo_Pics;
    @SerializedName("Total_Comments")
    public int                  Total_Comments;
    @SerializedName("Spot_ID")
    public String               Spot_ID;
    @SerializedName("Spot_Name")
    public String               Spot_Name;
    @SerializedName("Spot_PID")
    public String               Spot_PID;
    @SerializedName("Spot_Source")
    public String               Spot_Source;
    @SerializedName("Spot_Longitude")
    public double               Spot_Longitude;
    @SerializedName("Spot_Latitude")
    public double               Spot_Latitude;
    @SerializedName("Spot_Type")
    public String               Spot_Type;
    @SerializedName("Spot_Genre")
    public String               Spot_Genre;
    @SerializedName("Spot_Address")
    public String               Spot_Address;
    @SerializedName("Spot_Phone")
    public String               Spot_Phone;
    @SerializedName("Spot_Price")
    public String               Spot_Price;
    @SerializedName("Last_Pic")
    public String               Last_Pic;
    @SerializedName("Last_Pic_User_ID")
    public String               Last_Pic_User_ID;
    @SerializedName("Last_Pic_Bravo_ID")
    public String               Last_Pic_Bravo_ID;

    @SerializedName("Total_Saved_Users")
    public int                  Total_Saved_Users;
    @SerializedName("Is_Private")
    public boolean              Is_Private;
    @SerializedName("Is_Tsurete")
    public int                  Is_Tsurete;
    @SerializedName("Total_Tsurete")
    public int                  Total_Tsurete;
    @SerializedName("Time_Zone")
    public String               Time_Zone;
    @SerializedName("Date_Created")
    public Date_Created         Date_Created;

    public ObBravo() {

    }

    public Date_Created getDateCreated() {
        return Date_Created;
    }
}