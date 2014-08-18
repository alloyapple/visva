package com.sharebravo.bravo.model.parameters;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

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
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("Start", String.valueOf(start)));
        nameValuePairs.add(new BasicNameValuePair("Full_Name", fullName));
        nameValuePairs.add(new BasicNameValuePair("Foreign_SNS", foreignSNS));
        nameValuePairs.add(new BasicNameValuePair("Foreign_ID", foreignID));
        nameValuePairs.add(new BasicNameValuePair("Foreign_Access_Tool", foreignAccessTool));
        nameValuePairs.add(new BasicNameValuePair("Email", email));
        nameValuePairs.add(new BasicNameValuePair("Password", password));
        nameValuePairs.add(new BasicNameValuePair("Auth_Method", authMethod));
        return nameValuePairs;
    }
}
