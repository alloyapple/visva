package com.sharebravo.bravo.sdk.util.network;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public final class ParameterFactory {

    public static List<NameValuePair> createSubParams(String subParams) {
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(new BasicNameValuePair("params", subParams));
        return parameters;
    }

    public static List<NameValuePair> createSubParamsAllowBravoDataOnly() {
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(new BasicNameValuePair("Allow_Bravo_Only", "1".toString()));
        return parameters;
    }

    public static List<NameValuePair> createSubParamsLoginBravoAccount(String email, String passWord) {
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(new BasicNameValuePair("Email", email));
        parameters.add(new BasicNameValuePair("Password", passWord));
        return parameters;
    }

    public static List<NameValuePair> createSubParamsGetNewsBravoItems(String userID, String accessToken, String startPoint) {
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(new BasicNameValuePair("User_ID", userID));
        parameters.add(new BasicNameValuePair("Access_Token", String.valueOf(accessToken)));
        parameters.add(new BasicNameValuePair("params", startPoint));
        return parameters;
    }

    public static List<NameValuePair> createSubParamsGetNotificationSearch(String userID, String accessToken, String param) {
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(new BasicNameValuePair("User_ID", userID));
        parameters.add(new BasicNameValuePair("Access_Token", String.valueOf(accessToken)));
        parameters.add(new BasicNameValuePair("params", param));
        return parameters;
    }

    /**
     * get recent post
     * 
     * @param userID
     * @param accessToken
     * @param startPoint
     * @return
     */
    public static List<NameValuePair> createSubParamsGetAllBravoItems(String userID, String accessToken) {
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(new BasicNameValuePair("User_ID", userID));
        parameters.add(new BasicNameValuePair("Access_Token", String.valueOf(accessToken)));
        return parameters;
    }

    public static List<NameValuePair> createSubParamsLoginBySNS(String userID, String accessToken) {
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(new BasicNameValuePair("User_ID", userID));
        parameters.add(new BasicNameValuePair("Access_Token", accessToken));
        return parameters;
    }

    public static List<NameValuePair> createSubParamsGetComments(String userID, String accessToken) {
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(new BasicNameValuePair("User_ID", userID));
        parameters.add(new BasicNameValuePair("Access_Token", String.valueOf(accessToken)));
        return parameters;
    }

    public static List<NameValuePair> createSubParamsGetBravo(String userID, String accessToken) {
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(new BasicNameValuePair("User_ID", userID));
        parameters.add(new BasicNameValuePair("Access_Token", String.valueOf(accessToken)));
        return parameters;
    }

    public static List<NameValuePair> createSubParamsPutFollow(String userIDFollow) {
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(new BasicNameValuePair("params", userIDFollow));
        return parameters;
    }

    public static List<NameValuePair> createSubParamsPutMyListItem(String bravoId) {
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(new BasicNameValuePair("params", bravoId));
        return parameters;
    }

    public static List<NameValuePair> createSubParamsGetTimeLine(String userID, String accessToken, String subParams) {
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(new BasicNameValuePair("User_ID", userID));
        parameters.add(new BasicNameValuePair("Access_Token", String.valueOf(accessToken)));
        parameters.add(new BasicNameValuePair("params", subParams));

        return parameters;
    }

    public static List<NameValuePair> createSubParamsGetFavorites(String userID, String accessToken, String subParams) {
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(new BasicNameValuePair("User_ID", userID));
        parameters.add(new BasicNameValuePair("Access_Token", String.valueOf(accessToken)));
        parameters.add(new BasicNameValuePair("params", subParams));

        return parameters;
    }

    public static List<NameValuePair> createSubParamsUserSearch(String userID, String accessToken, String subParams) {
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(new BasicNameValuePair("User_ID", userID));
        parameters.add(new BasicNameValuePair("Access_Token", String.valueOf(accessToken)));
        parameters.add(new BasicNameValuePair("params", subParams));
        return parameters;
    }
    public static List<NameValuePair> createSubParamsRequest(String userID, String accessToken, String subParams) {
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(new BasicNameValuePair("User_ID", userID));
        parameters.add(new BasicNameValuePair("Access_Token", String.valueOf(accessToken)));
        parameters.add(new BasicNameValuePair("params", subParams));
        return parameters;
    }
}
