package com.sharebravo.bravo.model.parameters;

import java.util.HashMap;

public class PaDeleteSNS extends BaseParameter {

    public PaDeleteSNS(String userID, String accessToken) {
        super(userID, accessToken);
        // TODO Auto-generated constructor stub
    }

    @Override
    public HashMap<String, String> creatParamHashMap() {
        // TODO Auto-generated method stub
        return parentParams;
    }

}
