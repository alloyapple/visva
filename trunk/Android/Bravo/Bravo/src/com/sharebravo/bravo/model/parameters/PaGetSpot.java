package com.sharebravo.bravo.model.parameters;

import java.util.HashMap;

public class PaGetSpot extends BaseParameter {

    public PaGetSpot(String userID, String accessToken) {
        super(userID, accessToken);
        // TODO Auto-generated constructor stub
    }

    @Override
    public HashMap<String, String> creatParamHashMap() {
        // TODO Auto-generated method stub
        return parentParams;
    }

}
