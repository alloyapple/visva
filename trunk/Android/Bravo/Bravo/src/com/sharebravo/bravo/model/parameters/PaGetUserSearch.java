package com.sharebravo.bravo.model.parameters;

import java.util.HashMap;

import org.json.JSONObject;

public class PaGetUserSearch extends BaseParameter {
    public PaGetUserSearch(String userID, String accessToken) {
        super(userID, accessToken);
        // TODO Auto-generated constructor stub
    }

    int    start;
    String fullName;
    String foreignSNS;
    String foreignID;
    String foreignAccessTool;
    String email;
    String password;
    String authMethod;

    @Override
    public HashMap<String, String> creatParamHashMap() {
        // TODO Auto-generated method stub
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("Start", String.valueOf(start));
        params.put("Full_Name", fullName);
        params.put("Foreign_SNS", foreignSNS);
        params.put("Foreign_ID", foreignID);
        params.put("Foreign_Access_Tool", foreignAccessTool);
        params.put("Email", email);
        params.put("Password", password);
        params.put("Auth_Method", authMethod);
        parentParams.put("params", new JSONObject(params).toString());

        return parentParams;
    }

}
