package com.sharebravo.bravo.model.parameters;

import java.util.HashMap;

import org.json.JSONObject;

public class PaGetBravoSearch extends BaseParameter {
    int     start;
    String  type;
    String  genre;
    String  FID;
    String  source;
    String  name;
    String  address;
    String  location;
    String  minBravoID;
    String  maxBravoID;
    boolean viewDeletedUsers;
    boolean global;

    public PaGetBravoSearch(String userID, String accessToken) {
        super(userID, accessToken);
        // TODO Auto-generated constructor stub
    }

    @Override
    public HashMap<String, String> creatParamHashMap() {
        // TODO Auto-generated method stub
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("Start", String.valueOf(start));
        params.put("Type", type);
        params.put("Genre", genre);
        params.put("FID", FID);
        params.put("Source", source);
        params.put("Name", name);
        params.put("Address", address);
        params.put("Location", location);
        params.put("Min_Bravo_ID", minBravoID);
        params.put("Max_Bravo_ID", maxBravoID);
        params.put("View_Deleted_Users", viewDeletedUsers ? "1" : "0");
        params.put("Global", global ? "TRUE" : "FALSE");
        parentParams.put("params", new JSONObject(params).toString());

        return parentParams;
    }

}
