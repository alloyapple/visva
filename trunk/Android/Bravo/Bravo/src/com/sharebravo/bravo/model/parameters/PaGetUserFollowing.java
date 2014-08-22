package com.sharebravo.bravo.model.parameters;

import java.util.HashMap;

import org.json.JSONObject;

public class PaGetUserFollowing extends BaseParameter {
    int start;

    public PaGetUserFollowing(String userID, String accessToken) {
        super(userID, accessToken);
        // TODO Auto-generated constructor stub
    }

    @Override
    public HashMap<String, String> creatParamHashMap() {
        // TODO Auto-generated method stub
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("Start", String.valueOf(start));
        parentParams.put("params", new JSONObject(params).toString());

        return parentParams;
    }

}
