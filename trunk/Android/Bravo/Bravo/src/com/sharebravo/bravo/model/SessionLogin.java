package com.sharebravo.bravo.model;

public class SessionLogin {
    public String userID;
    public String accessToken;
    public String aPNSTockens;

    public SessionLogin() {
    }

    public String getUserId() {
        return userID;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getAPNsToken() {
        return aPNSTockens;
    }

}
