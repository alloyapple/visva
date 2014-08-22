package com.sharebravo.bravo.model.parameters;

import java.util.HashMap;

import org.json.JSONObject;

public class PaPostForgot extends BaseParameter {
    public PaPostForgot(String userID, String accessToken) {
        super(userID, accessToken);
        // TODO Auto-generated constructor stub
    }

    String email;

    @Override
    public HashMap<String, String> creatParamHashMap() {
        // TODO Auto-generated method stub
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("Email", email);
        parentParams.put("params", new JSONObject(params).toString());

        return parentParams;
    }

}
