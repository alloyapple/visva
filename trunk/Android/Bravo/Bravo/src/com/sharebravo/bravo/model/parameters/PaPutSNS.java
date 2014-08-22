package com.sharebravo.bravo.model.parameters;

import java.util.HashMap;

import org.json.JSONObject;

public class PaPutSNS extends BaseParameter {
    public PaPutSNS(String userID, String accessToken) {
        super(userID, accessToken);
        // TODO Auto-generated constructor stub
    }

    String foreignID;
    String foreignSNS;
    String foreignAccessToken;

    @Override
    public HashMap<String, String> creatParamHashMap() {
        // TODO Auto-generated method stub
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("Foreign_ID", foreignID);
        params.put("Foreign_SNS", foreignSNS);
        params.put("Foreign_Access_Token", foreignAccessToken);
        parentParams.put("params", new JSONObject(params).toString());

        return parentParams;
    }

}
