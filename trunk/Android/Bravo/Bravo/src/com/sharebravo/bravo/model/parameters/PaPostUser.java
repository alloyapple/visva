package com.sharebravo.bravo.model.parameters;

import java.util.HashMap;

import org.json.JSONObject;

public class PaPostUser extends BaseParameter {
    public PaPostUser(String userID, String accessToken) {
        super(userID, accessToken);
        // TODO Auto-generated constructor stub
    }

    String authMethod;
    String fullName;
    String email;
    String foreignID;
    String password;
    String timeZone;
    String locale;
    String apnsToken;

    @Override
    public HashMap<String, String> creatParamHashMap() {
        // TODO Auto-generated method stub
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("Auth_Method", authMethod);
        params.put("Full_Name", fullName);
        params.put("Email", email);
        params.put("Foreign_ID", foreignID);
        params.put("Password", password);
        params.put("Time_Zone", timeZone);
        params.put("Locale", locale);
        params.put("APNS_Token", apnsToken);
        parentParams.put("params", new JSONObject(params).toString());

        return parentParams;
    }

}
