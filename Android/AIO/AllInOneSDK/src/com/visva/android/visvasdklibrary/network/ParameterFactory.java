package com.visva.android.visvasdklibrary.network;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public final class ParameterFactory {

    public static List<NameValuePair> createUpdatePayment(String userId) {
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(new BasicNameValuePair("params", userId));
        return parameters;

    }

}
