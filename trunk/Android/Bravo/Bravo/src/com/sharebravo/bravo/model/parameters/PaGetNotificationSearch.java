package com.sharebravo.bravo.model.parameters;

import java.util.HashMap;

import org.json.JSONObject;

public class PaGetNotificationSearch extends BaseParameter {
    int start;
    int postPic;

    public PaGetNotificationSearch(String userID, String accessToken) {
        super(userID, accessToken);
        // TODO Auto-generated constructor stub
    }

    @Override
    public HashMap<String, String> creatParamHashMap() {
        // TODO Auto-generated method stub
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("Start", String.valueOf(start));
        params.put("Post_pic", String.valueOf(postPic));
        parentParams.put("params", new JSONObject(params).toString());

        return parentParams;
    }
}
