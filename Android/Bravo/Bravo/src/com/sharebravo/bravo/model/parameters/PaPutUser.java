package com.sharebravo.bravo.model.parameters;

import java.util.HashMap;

import org.json.JSONObject;

public class PaPutUser extends BaseParameter {
    public PaPutUser(String userID, String accessToken) {
        super(userID, accessToken);
        // TODO Auto-generated constructor stub
    }

    String  fullName;
    String  timeZone;
    String  aboutMe;
    String  locale;
    String  password;
    int     badgeNum;
    boolean isMyListPrivate;
    String  profileImg;
    String  coverImg;
    String  apnsToken;
    int     profileImgDel;
    int     coverImgDel;

    @Override
    public HashMap<String, String> creatParamHashMap() {
        // TODO Auto-generated method stub
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("Full_Name", fullName);
        params.put("Time_Zone", timeZone);
        params.put("About_Zone", aboutMe);
        params.put("Locale", locale);
        params.put("Password", password);
        params.put("Badge_Num", String.valueOf(badgeNum));
        params.put("Is_My_List_Private", isMyListPrivate?"TRUE":"FALSE");
        params.put("Profile_Img", profileImg);
        params.put("Cover_Img", coverImg);
        params.put("APNS_Token", apnsToken);
        params.put("Profile_Img_Del", String.valueOf(profileImgDel));
        params.put("Cover_Img_Del", String.valueOf(coverImgDel));
        parentParams.put("params", new JSONObject(params).toString());

        return parentParams;
    }

}
