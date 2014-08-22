package com.sharebravo.bravo.model.parameters;

import java.util.HashMap;

import org.json.JSONObject;

public class PaPostBravoSNS extends BaseParameter {
    public PaPostBravoSNS(String userID, String accessToken) {
        super(userID, accessToken);
        // TODO Auto-generated constructor stub
    }

    String snsPost;
    String postPic;

    @Override
    public HashMap<String, String> creatParamHashMap() {
        // TODO Auto-generated method stub
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("SNS_Post", snsPost);
        params.put("Post_Pic", postPic);

        parentParams.put("params", new JSONObject(params).toString());

        return parentParams;
    }

}
