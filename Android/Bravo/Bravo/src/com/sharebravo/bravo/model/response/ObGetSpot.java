package com.sharebravo.bravo.model.response;

import com.google.gson.annotations.SerializedName;

public class ObGetSpot {
    @SerializedName("data")
    public Spot   data;
    @SerializedName("status")
    public int    status;
    @SerializedName("error")
    public String error;

    public ObGetSpot() {
        // TODO Auto-generated constructor stub
    }

    public class Spot {
        @SerializedName("Spot_ID")
        public String       Spot_ID;
        @SerializedName("Spot_Name")
        public String       Spot_Name;
        @SerializedName("Spot_Source")
        public String       Spot_Source;
        @SerializedName("Spot_FID")
        public String       Spot_FID;
        @SerializedName("Spot_Longitude")
        public double       Spot_Longitude;
        @SerializedName("Spot_Latitude")
        public double       Spot_Latitude;
        @SerializedName("Spot_Type")
        public String       Spot_Type;
        @SerializedName("Spot_Genre")
        public String       Spot_Genre;
        @SerializedName("Spot_Address")
        public String       Spot_Address;
        @SerializedName("Spot_Phone")
        public String       Spot_Phone;
        @SerializedName("Spot_Price")
        public String       Spot_Price;
        @SerializedName("Last_Pic")
        public String       Last_Pic;
        @SerializedName("Last_Pic_Bravo_ID")
        public String       Last_Pic_Bravo_ID;
        @SerializedName("Last_Pic_User_ID")
        public String       Last_Pic_User_ID;
        @SerializedName("Total_Bravos")
        public int          Total_Bravos;
        @SerializedName("Unique_Users")
        public int          Unique_Users;
        @SerializedName("Total_Liked_Users")
        public int          Total_Liked_Users;
        @SerializedName("Total_Saved_Users")
        public int          Total_Saved_Users;
        @SerializedName("Date_Created")
        public Date_Created Date_Created = new Date_Created();
        public String       Spot_Icon;

        public Spot() {
            // TODO Auto-generated constructor stub
        }
    }
}
