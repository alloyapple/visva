package com.sharebravo.bravo.model.parameters;

import java.util.HashMap;

import org.json.JSONObject;

public class PaPostComment extends BaseParameter{
    String userID;
    String bravoID;
    String commentText;
    

    public PaPostComment(String userID, String accessToken) {
        super(userID, accessToken);
        // TODO Auto-generated constructor stub
    }

    @Override
    public HashMap<String, String> creatParamHashMap() {
        // TODO Auto-generated method stub
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("User_ID", userID);
        params.put("Bravo_ID", bravoID);
        params.put("Comment_Text", bravoID);
        parentParams.put("params", new JSONObject(params).toString());

        return parentParams;
    }
   
}
