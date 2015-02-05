package com.visva.android.visvasdklibrary.provider;

/**
 * IReponseListener, interface to return the result called from providers.
 * It has 2 methods
 * onResponse( String response): data returns successfully.
 * onErrorResponse(int errorType,String errorMessage): data return failed.
 * 
 * @author kieu.thang
 * 
 */
public interface IReponseListener {
    public void onResponse(String response);

    public void onErrorResponse(int errorType, String errorMessage);
}
