package com.sharebravo.bravo.model.parameters;

import java.util.HashMap;
import java.util.Iterator;

public abstract class BaseParameter {

    HashMap<String, String> parentParams = new HashMap<String, String>();

    public BaseParameter(String userID, String accessToken) {
        // TODO Auto-generated constructor stub
        parentParams.put("User_ID", userID);
        parentParams.put("Access_Token", accessToken);
    }

    public abstract HashMap<String, String> creatParamHashMap();

    public String creatStringParams() {
        String strParams = "";
        Iterator<String> iterator = parentParams.keySet().iterator();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            String value = (String) parentParams.get(key);
            strParams = strParams + key + "=" + value;
        }
        return strParams.length() > 0 ? strParams.substring(0, strParams.length() - 1) : strParams;
    }
}
