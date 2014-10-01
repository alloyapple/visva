package com.sharebravo.bravo.sdk.request;

public interface IRequestListener {
    public void onResponse(String response);

    public void onErrorResponse(String errorMessage);
}
