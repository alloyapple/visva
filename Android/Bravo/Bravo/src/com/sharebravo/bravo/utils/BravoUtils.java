package com.sharebravo.bravo.utils;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sharebravo.bravo.model.response.ObPostUserSuccess;

public class BravoUtils {

    public static String getUserIdFromUserBravoInfo(Context context,String shareKeyPre) {
        Gson gson = new GsonBuilder().serializeNulls().create();
        ObPostUserSuccess obPostUserSuccess = gson.fromJson(shareKeyPre.toString(), ObPostUserSuccess.class);
        if (obPostUserSuccess == null)
            return "";
        return obPostUserSuccess.data.User_ID;
    }
    
    public static String getAccessTokenFromUserBravoInfo(Context context,String shareKeyPre) {
        String userBravoInfo = BravoSharePrefs.getInstance(context).getStringValue(shareKeyPre);
        Gson gson = new GsonBuilder().serializeNulls().create();
        ObPostUserSuccess obPostUserSuccess = gson.fromJson(userBravoInfo.toString(), ObPostUserSuccess.class);
        if (obPostUserSuccess == null)
            return "";
        return obPostUserSuccess.data.Access_Token;
    }
}
