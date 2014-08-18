package com.sharebravo.bravo.model.parameters;

import java.util.List;

import org.apache.http.NameValuePair;

public class PaPostUser extends BasicParameter{
    String authMethod;
    String fullName;
    String email;
    String foreignID;
    String password;
    String timeZone;
    String locale;
    String apnsToken;

    public PaPostUser() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public List<NameValuePair> createNameValuePair() {
        // TODO Auto-generated method stub
        return null;
    }
}
