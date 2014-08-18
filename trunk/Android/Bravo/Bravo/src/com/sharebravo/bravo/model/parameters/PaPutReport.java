package com.sharebravo.bravo.model.parameters;

import java.util.List;

import org.apache.http.NameValuePair;

public class PaPutReport extends BasicParameter{
    String foreignID;
    String reportType;
    String userID;
    String details;

    public PaPutReport() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public List<NameValuePair> createNameValuePair() {
        // TODO Auto-generated method stub
        return null;
    }
}
