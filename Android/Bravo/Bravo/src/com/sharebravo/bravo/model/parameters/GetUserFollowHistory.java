package com.sharebravo.bravo.model.parameters;

import java.util.HashMap;

import org.json.JSONObject;

public class GetUserFollowHistory extends BaseParameter{
    public GetUserFollowHistory(String userID, String accessToken) {
        super(userID, accessToken);
        // TODO Auto-generated constructor stub
    }
    int    start;
    String maxFollowID;
    @Override
    public HashMap<String, String> creatParamHashMap() {
        // TODO Auto-generated method stub
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("Start", String.valueOf(start));
        params.put("Max_Follow_ID", maxFollowID);
        
        parentParams.put("params", new JSONObject(params).toString());

        return parentParams;
    }

   
}
