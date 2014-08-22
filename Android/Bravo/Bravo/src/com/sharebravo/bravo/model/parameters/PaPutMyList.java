package com.sharebravo.bravo.model.parameters;

import java.util.HashMap;

import org.json.JSONObject;

public class PaPutMyList extends BaseParameter {
    public PaPutMyList(String userID, String accessToken) {
        super(userID, accessToken);
        // TODO Auto-generated constructor stub
    }

    String bravoID;

    @Override
    public HashMap<String, String> creatParamHashMap() {
        // TODO Auto-generated method stub
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("Bravo_ID", bravoID);
        parentParams.put("params", new JSONObject(params).toString());

        return parentParams;
    }

}
