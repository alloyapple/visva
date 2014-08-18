package com.sharebravo.bravo.model.parameters;

import java.util.List;

import org.apache.http.NameValuePair;

public class PaPostBravoSNS extends BasicParameter{
    String userID;
    String bravoID;
    String commentText;

    public PaPostBravoSNS() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public List<NameValuePair> createNameValuePair() {
        // TODO Auto-generated method stub
        return null;
    }
}
