package com.sharebravo.bravo.model.parameters;

import java.util.HashMap;

import org.json.JSONObject;

public class PaPutBravo extends BaseParameter {
    public PaPutBravo(String userID, String accessToken) {
        super(userID, accessToken);
        // TODO Auto-generated constructor stub
    }

    int[]   image;
    boolean isPrivate;

    @Override
    public HashMap<String, String> creatParamHashMap() {
        // TODO Auto-generated method stub
        HashMap<String, String> params = new HashMap<String, String>();
        // params.put("image", image);
        params.put("Is_Private", isPrivate ? "TRUE" : "FALSE");
        parentParams.put("params", new JSONObject(params).toString());

        return parentParams;
    }

}
