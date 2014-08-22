package com.sharebravo.bravo.model.parameters;

import java.util.HashMap;

import org.json.JSONObject;

public class PaPutSpot extends BaseParameter {
    public PaPutSpot(String userID, String accessToken) {
        super(userID, accessToken);
        // TODO Auto-generated constructor stub
    }

    String spotName;
    String spotFID;
    String spotSource;
    float  spotLongitude;
    float  spotLatitude;
    String spotType;
    String spotGenre;
    String spotAddress;
    String spotPhone;
    String spotPrice;

    @Override
    public HashMap<String, String> creatParamHashMap() {
        // TODO Auto-generated method stub
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("Spot_Name", spotName);
        params.put("Spot_FID", spotFID);
        params.put("Spot_Source", spotSource);
        params.put("Spot_Longitude", String.valueOf(spotLongitude));
        params.put("Spot_Latitude", String.valueOf(spotLatitude));
        params.put("Spot_Type", spotType);
        params.put("Spot_Genre", spotGenre);
        params.put("Spot_Address", spotAddress);
        params.put("Spot_Phone", spotPhone);
        params.put("Spot_Price", spotPrice);
        parentParams.put("params", new JSONObject(params).toString());

        return parentParams;
    }

}
