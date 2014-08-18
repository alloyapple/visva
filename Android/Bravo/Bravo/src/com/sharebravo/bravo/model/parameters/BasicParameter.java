package com.sharebravo.bravo.model.parameters;

import java.util.List;

import org.apache.http.NameValuePair;

public abstract class BasicParameter {
    public abstract List<NameValuePair> createNameValuePair();
}
