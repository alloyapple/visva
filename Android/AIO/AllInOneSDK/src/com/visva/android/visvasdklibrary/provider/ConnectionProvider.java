package com.visva.android.visvasdklibrary.provider;

import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;

import android.content.Context;
import android.os.StrictMode;

import com.visva.android.visvasdklibrary.constant.AIOConstant;
import com.visva.android.visvasdklibrary.log.AIOLog;
import com.visva.android.visvasdklibrary.network.AsyncHttpDelete;
import com.visva.android.visvasdklibrary.network.AsyncHttpGet;
import com.visva.android.visvasdklibrary.network.AsyncHttpPost;
import com.visva.android.visvasdklibrary.network.AsyncHttpPut;
import com.visva.android.visvasdklibrary.network.AsyncHttpResponseProcess;
import com.visva.android.visvasdklibrary.network.HttpMethod;

/**
 * ConnectionProvider provides methods which uses asyncHttp to request data from a server api.
 * It supports GET, POST, PUT, DELETE methods at {@link HttpMethod}
 * 
 * @author kieu.thang
 * 
 */
public class ConnectionProvider {
    /* singleton class */
    private static final String       TAG = AIOConstant.TAG;
    private static ConnectionProvider mInstance;
    private static Context            mContext;

    /**
     * Connector constructor
     * 
     * @param context
     */
    public ConnectionProvider(Context context) {
        super();
        mContext = context;

    }

    /**
     * get instance of Volley singleton class
     * 
     * @param context
     * @return instance
     */
    public static synchronized ConnectionProvider getInstance(Context context) {
        if (mInstance == null)
            mInstance = new ConnectionProvider(context);
        mContext = context;
        return mInstance;
    }

    // =======================CORE FUNCTIOINS================================
    /**
     * request data from server api by using asyncHttp of connection provider
     * 
     * @param requestType
     *            include get,post,delete, put. It's defined in class {@link HttpMethod}
     * @param api
     * @param iReponseListener
     * @param parameters
     */
    public void requestDataFromServerAPI(int requestType, String api, IReponseListener iReponseListener, List<NameValuePair> parameters) {
        switch (requestType) {
        case HttpMethod.GET:
            requestHttpGetFromServerAPI(api, iReponseListener, parameters);
            break;
        case HttpMethod.DELETE:
            requestHttpDeleteFromServerAPI(api, iReponseListener, parameters);
            break;
        case HttpMethod.POST:
            requestHttpPostFromServerAPI(api, iReponseListener, parameters);
            break;
        case HttpMethod.PUT:
            requestHttpPutFromServerAPI(api, iReponseListener, parameters);
            break;
        default:
            AIOLog.d(TAG, "No request type defined");
            break;
        }
    }

    private void requestHttpGetFromServerAPI(String url, final IReponseListener iReponseListener, List<NameValuePair> parameters) {
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        AsyncHttpGet getRequest = new AsyncHttpGet(mContext, new AsyncHttpResponseProcess(mContext) {
            @Override
            public void processIfResponseSuccess(String response) {
                iReponseListener.onResponse(response);
            }

            @Override
            public void processIfResponseFail(int errorType, HttpResponse errorReponse) {
                AIOLog.d(TAG, "response error");
                if (errorReponse == null)
                    iReponseListener.onErrorResponse(errorType, "");
                else
                    iReponseListener.onErrorResponse(errorType, errorReponse.toString());
            }
        }, parameters);
        getRequest.execute(url);
    }

    private void requestHttpPostFromServerAPI(String url, final IReponseListener iReponseListener, List<NameValuePair> parameters) {
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        AsyncHttpPost getRequest = new AsyncHttpPost(mContext, new AsyncHttpResponseProcess(mContext) {
            @Override
            public void processIfResponseSuccess(String response) {
                iReponseListener.onResponse(response);
            }

            @Override
            public void processIfResponseFail(int errorType, HttpResponse errorReponse) {
                AIOLog.d(TAG, "response error");
                if (errorReponse == null)
                    iReponseListener.onErrorResponse(errorType, "");
                else
                    iReponseListener.onErrorResponse(errorType, errorReponse.toString());
            }
        }, parameters);
        getRequest.execute(url);
    }

    private void requestHttpDeleteFromServerAPI(String url, final IReponseListener iReponseListener, List<NameValuePair> parameters) {
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        AsyncHttpDelete getRequest = new AsyncHttpDelete(mContext, new AsyncHttpResponseProcess(mContext) {
            @Override
            public void processIfResponseSuccess(String response) {
                iReponseListener.onResponse(response);
            }

            @Override
            public void processIfResponseFail(int errorType, HttpResponse errorReponse) {
                AIOLog.d(TAG, "response error");
                if (errorReponse == null)
                    iReponseListener.onErrorResponse(errorType, "");
                else
                    iReponseListener.onErrorResponse(errorType, errorReponse.toString());
            }
        }, parameters);
        getRequest.execute(url);
    }

    private void requestHttpPutFromServerAPI(String url, final IReponseListener iReponseListener, List<NameValuePair> parameters) {
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        AsyncHttpPut getRequest = new AsyncHttpPut(mContext, new AsyncHttpResponseProcess(mContext) {
            @Override
            public void processIfResponseSuccess(String response) {
                iReponseListener.onResponse(response);
            }

            @Override
            public void processIfResponseFail(int errorType, HttpResponse errorReponse) {
                AIOLog.d(TAG, "response error");
                if (errorReponse == null)
                    iReponseListener.onErrorResponse(errorType, "");
                else
                    iReponseListener.onErrorResponse(errorType, errorReponse.toString());
            }
        }, parameters);
        getRequest.execute(url);
    }

}
