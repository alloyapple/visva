package com.sharebravo.bravo.model.parameters;

import java.util.List;

import org.apache.http.NameValuePair;

public class PaGetUserSearch extends BasicParameter{
    int    start;
    String fullName;
    String foreignSNS;
    String foreignID;
    String foreignAccessTool;
    String email;
    String password;
    String authMethod;

    public PaGetUserSearch() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public List<NameValuePair> createNameValuePair() {
        // TODO Auto-generated method stub
        return null;
    }
}
