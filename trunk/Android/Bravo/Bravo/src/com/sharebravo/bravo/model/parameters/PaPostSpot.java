package com.sharebravo.bravo.model.parameters;

import java.util.HashMap;

import org.json.JSONObject;

public class PaPostSpot extends BaseParameter {
    public PaPostSpot(String userID, String accessToken) {
        super(userID, accessToken);
        // TODO Auto-generated constructor stub
    }

    String spotName;
    String spotFID;
    String spotSource;
    String  spotLongitude;
    String  spotLatitude;
    String spotType;
    String spotGenre;
    String spotAddress;
    String spotPhone;
    String spotPrice;
    String lag;

    @Override
    public HashMap<String, String> creatParamHashMap() {
        // TODO Auto-generated method stub
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("Spot_Name", spotName);
        params.put("Spot_FID", spotFID);
        params.put("Spot_Source", spotSource);
        params.put("Spot_Longitude", spotLongitude);
        params.put("Spot_Latitude", spotLatitude);
        params.put("Spot_Type", spotType);
        params.put("Spot_Genre", spotGenre);
        params.put("Spot_Address", spotAddress);
        params.put("Spot_Phone", spotPhone);
        params.put("Spot_Price", spotPrice);
        params.put("Lag", lag);
        parentParams.put("params", new JSONObject(params).toString());

        return parentParams;
    }

}
