package com.sharebravo.bravo.model.parameters;

import java.util.HashMap;

public abstract class BaseParameter {

    HashMap<String, String> parentParams = new HashMap<String, String>();

    public BaseParameter(String userID, String accessToken) {
        // TODO Auto-generated constructor stub
        parentParams.put("User_ID", userID);
        parentParams.put("Access_Token", accessToken);
    }

    public abstract HashMap<String, String> creatParamHashMap();
}
