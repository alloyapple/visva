package com.sharebravo.bravo.model.parameters;

import java.util.HashMap;

import org.json.JSONObject;

public class PaGetTimline extends BaseParameter {
    public PaGetTimline(String userID, String accessToken) {
        super(userID, accessToken);
        // TODO Auto-generated constructor stub
    }

    int    start;
    String maxBravoID;

    @Override
    public HashMap<String, String> creatParamHashMap() {
        // TODO Auto-generated method stub
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("Start", String.valueOf(start));
        params.put("Max_Bravo_ID", String.valueOf(maxBravoID));
        parentParams.put("params", new JSONObject(params).toString());

        return parentParams;
    }

}
