package com.sharebravo.bravo.control.request;

public interface IRequestListener {
    public void onResponse(String response);

    public void onErrorResponse(String errorMessage);
}
