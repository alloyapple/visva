package com.sharebravo.bravo.model.parameters;

import java.util.HashMap;

import org.json.JSONObject;

public class PaPostBravo extends BaseParameter {
    public PaPostBravo(String userID, String accessToken) {
        super(userID, accessToken);
        // TODO Auto-generated constructor stub
    }
    String  bravoType;
    String  spotID;
    String  timeZone;
    boolean isPrivate;
    String  snsPost;
    String  fsUserID;
    String  fsAccessToken;
    int[]   image;
    @Override
    public HashMap<String, String> creatParamHashMap() {
        // TODO Auto-generated method stub
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("Bravo_type", bravoType);
        params.put("Spot_ID", spotID);
        params.put("Time_Zone", timeZone);
        params.put("Is_Private", isPrivate ? "TRUE" : "FALSE");
        params.put("SNS_Post", snsPost);
        params.put("FS_User_IS", fsUserID);
        params.put("FS_Access_Token", fsAccessToken);
        params.put("images", spotID);
        parentParams.put("params", new JSONObject(params).toString());

        return parentParams;
    }

}
