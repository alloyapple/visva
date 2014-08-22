package com.sharebravo.bravo.model.parameters;

import java.util.HashMap;

import org.json.JSONObject;

public class PaGetSpotSearch extends BaseParameter {
    public PaGetSpotSearch(String userID, String accessToken) {
        super(userID, accessToken);
        // TODO Auto-generated constructor stub
    }

    int    start;
    String type;
    String genre;
    String FID;
    String source;
    String name;
    String address;
    String location;
    int    bravoTotalOnly;

    @Override
    public HashMap<String, String> creatParamHashMap() {
        // TODO Auto-generated method stub
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("Start", String.valueOf(start));
        params.put("Type", type);
        params.put("Genre", genre);
        params.put("FID", FID);
        params.put("Source", source);
        params.put("Name", name);
        params.put("Address", address);
        params.put("Location", location);
        params.put("Bravo_Total_Only", String.valueOf(bravoTotalOnly));
        parentParams.put("params", new JSONObject(params).toString());

        return parentParams;
    }

}
