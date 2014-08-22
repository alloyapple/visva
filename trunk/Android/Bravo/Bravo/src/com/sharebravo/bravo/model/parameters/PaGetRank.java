package com.sharebravo.bravo.model.parameters;

import java.util.HashMap;

import org.json.JSONObject;

public class PaGetRank extends BaseParameter {
    int    start;
    String type;

    public PaGetRank(String userID, String accessToken) {
        super(userID, accessToken);
        // TODO Auto-generated constructor stub
    }

    @Override
    public HashMap<String, String> creatParamHashMap() {
        // TODO Auto-generated method stub
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("Start", String.valueOf(start));
        params.put("Type", type);
        parentParams.put("params", new JSONObject(params).toString());

        return parentParams;
    }

}
