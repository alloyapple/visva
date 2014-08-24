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
}
