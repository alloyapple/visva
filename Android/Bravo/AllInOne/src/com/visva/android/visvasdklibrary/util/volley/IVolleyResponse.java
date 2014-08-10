package com.visva.android.visvasdklibrary.util.volley;

/**
 * interface reponse the data from the server to client
 * 
 * @author kieu.thang
 * 
 */
public interface IVolleyResponse {

    /**
     * on Response data success
     * 
     * @param responseObject
     * @return null
     */
    public void onResponse(Object responseObject);

    /**
     * on Response data error
     * 
     * @param errorObject
     * @return null
     */
    public void onErrorResponse(Object errorObject);
}
