package com.sharebravo.bravo.model.parameters;

import java.util.HashMap;

public class PaPutBlocking extends BaseParameter{
    public PaPutBlocking(String userID, String accessToken) {
        super(userID, accessToken);
        // TODO Auto-generated constructor stub
    }

    @Override
    public HashMap<String, String> creatParamHashMap() {
        // TODO Auto-generated method stub
        return parentParams;
    }

   
}
