package com.sharebravo.bravo.model.parameters;

import java.util.HashMap;

import org.json.JSONObject;

public class PaPutReport extends BaseParameter {
    public PaPutReport(String userID, String accessToken) {
        super(userID, accessToken);
        // TODO Auto-generated constructor stub
    }

    String foreignID;
    String reportType;
    String userID;
    String details;

    @Override
    public HashMap<String, String> creatParamHashMap() {
        // TODO Auto-generated method stub
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("Foreign_ID", foreignID);
        params.put("Report_Type", reportType);
        params.put("User_ID", userID);
        params.put("Details", details);

        parentParams.put("params", new JSONObject(params).toString());

        return parentParams;
    }

}
