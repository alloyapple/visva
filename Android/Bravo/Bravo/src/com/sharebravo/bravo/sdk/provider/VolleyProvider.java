package com.sharebravo.bravo.sdk.provider;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sharebravo.bravo.R;
import com.sharebravo.bravo.sdk.log.AIOLog;
import com.sharebravo.bravo.sdk.util.AIOConstants;
import com.sharebravo.bravo.sdk.util.volley.ExtHttpClientStack;
import com.sharebravo.bravo.sdk.util.volley.IVolleyResponse;
import com.sharebravo.bravo.sdk.util.volley.LruBitmapCache;
import com.sharebravo.bravo.sdk.util.volley.SslHttpClient;
import com.sharebravo.bravo.utils.BravoConstant;
import com.sharebravo.bravo.utils.StringUtility;

/**
 * volley provider supports all methods of volley library includes:
 * 
 * - get Json from url:
 * -JsonObject, JsonArray
 * - get image from url
 * 
 * @author kieu.thang
 * 
 */
public class VolleyProvider {
    /* TAG attributes for add to queue to request string by volley */
    private static final String   TAG_STRING_REQ               = "req_string";

    /* TAG attributes for add to queue to request json object by volley */
    private static final String   TAG_JSON_OBJECT_REQ          = "req_json_object";

    /* TAG attributes for add to queue to request json object by volley with ssl */
    private static final String   TAG_JSON_OBJECT_REQ_WITH_SSL = "req_json_object_with_ssl";

    /* TAG attributes for add to queue to request json array by volley */
    private static final String   TAG_JSON_ARRAY_REQ           = "req_json_array";

    /* volley class */
    private RequestQueue          mRequestQueue;
    private ImageLoader           mImageLoader;

    /* singleton class */
    private static VolleyProvider mInstance;
    private Context               mContext;

    /**
     * Volley constructor
     * 
     * @param context
     */
    public VolleyProvider(Context context) {
        super();
        mContext = context;

    }

    /**
     * get instance of Volley singleton class
     * 
     * @param context
     * @return instance
     */
    public static synchronized VolleyProvider getInstance(Context context) {
        if (mInstance == null)
            mInstance = new VolleyProvider(context);
        return mInstance;
    }

    /**
     * volley methods
     * 
     * @return
     */
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext);
        }

        return mRequestQueue;
    }

    /**
     * imageloader is the loader for controlling request and save image
     * 
     * @return ImageLoader
     */
    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue, new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? BravoConstant.TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(BravoConstant.TAG);
        getRequestQueue().add(req);
    }

    /**
     * cancel pending request
     * 
     * @param tag
     * 
     * @return null
     */
    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    /**
     * using Volley to request string from url
     * 
     * @param url
     *            the link url
     * @param volleyResponse
     *            the reponse from url
     * 
     * @return null
     */
    public void requestStringFromURL(String url, final IVolleyResponse volleyResponse) {
        StringRequest strReq = new StringRequest(Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                AIOLog.d("requestStringFromURL onResponse=" + response);
                volleyResponse.onResponse(response);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                AIOLog.d("requestStringFromURL onErrorResponse=" + error.getMessage());
            }
        });

        // Adding request to request queue
        VolleyProvider.getInstance(mContext).addToRequestQueue(strReq, TAG_STRING_REQ);
    }

    /**
     * request Jsonobject from URL
     * 
     * @param url
     * @param volleyResponse
     * 
     * @return null
     */
    public void requestJsonObjectFromURL(String url, final IVolleyResponse volleyResponse) {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                AIOLog.d("requestJsonObjectFromURL onResponse=" + response.toString());
                volleyResponse.onResponse(response);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                AIOLog.d("requestJsonObjectFromURL onErrorResponse=" + error.getMessage());
            }
        });

        // Adding request to request queue
        VolleyProvider.getInstance(mContext).addToRequestQueue(jsonObjReq, TAG_JSON_OBJECT_REQ);
    }

    /**
     * request JsonArray from URL
     * 
     * @param url
     * @param volleyResponse
     * @return null
     */
    public void requestJsonArrayFromURL(String url, final IVolleyResponse volleyResponse) {
        JsonArrayRequest req = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response == null)
                    volleyResponse.onErrorResponse(mContext.getString(R.string.result_not_found));

                AIOLog.d("requestJsonArrayFromURL onResponse=" + response.toString());
                volleyResponse.onResponse(response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error == null)
                    volleyResponse.onErrorResponse(mContext.getString(R.string.result_not_found));
                AIOLog.d("requestJsonArrayFromURL onErrorResponse: " + error.getMessage());
            }
        });

        // Adding request to request queue
        VolleyProvider.getInstance(mContext).addToRequestQueue(req, TAG_JSON_ARRAY_REQ);
    }

    /**
     * 
     * @param url
     *            the link url request to post server with SSL authentication
     * @param params
     *            parameters add to url
     * @param volleyResponse
     *            interface to callback to response from server side
     * @return null
     */
    public void requestToPostDataToServerWithSSL(String url, HashMap<String, String> params, final IVolleyResponse volleyResponse) {
        InputStream keyStore = mContext.getResources().openRawResource(R.raw.bravoandroid);
        RequestQueue queue = Volley.newRequestQueue(mContext, new ExtHttpClientStack(new SslHttpClient(keyStore, AIOConstants.BRAVO_CRT_PASS, /* 44401 */
        AIOConstants.HTTPS_PORT)));
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.POST, url, new JSONObject(params), new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                AIOLog.d("requestJsonObjectFromURL onResponse=" + response.toString());
                volleyResponse.onResponse(response);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                AIOLog.d("requestJsonObjectFromURL onErrorResponse=" + error.getMessage());
            }
        });

        queue.add(jsonObjReq);
        VolleyProvider.getInstance(mContext).addToRequestQueue(jsonObjReq);
    }

    /**
     * 
     * @param url
     *            the link url request get data from server with SSL authentication
     * @param params
     *            parameters add to url
     * @param volleyResponse
     *            interface to callback to response from server side
     * @return null
     */
    public void requestToGetDataFromServerWithSSL(String url, HashMap<String, String> params, final IVolleyResponse volleyResponse) {
        AIOLog.d("requestToGetDataFromServerWithSSL " + url);
        String _url = makeLinkFromUrlAndParam(url, params);
        InputStream keyStore = mContext.getResources().openRawResource(R.raw.bravoandroid);
        RequestQueue queue = Volley.newRequestQueue(mContext, new ExtHttpClientStack(new SslHttpClient(keyStore, AIOConstants.BRAVO_CRT_PASS, /* 44401 */
        AIOConstants.HTTPS_PORT)));
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.GET, _url, new JSONObject(params), new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                AIOLog.d("requestJsonObjectFromURL =" + response.toString());
                volleyResponse.onResponse(response);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                AIOLog.d("error =" + error.getMessage());
            }
        });
        AIOLog.d("jsonObjReq url:" + jsonObjReq.getUrl());
        queue.add(jsonObjReq);
        VolleyProvider.getInstance(mContext).addToRequestQueue(jsonObjReq, TAG_JSON_OBJECT_REQ_WITH_SSL);
    }

    private String makeLinkFromUrlAndParam(String url, HashMap<String, String> params) {
        String _url = url + "?";
        Iterator<Map.Entry<String, String>> entries = params.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, String> entry = entries.next();
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
            String paramStr = null;
            paramStr = entry.getKey() + "=" + entry.getValue() + "&";
            _url += paramStr;
        }
        if (params.size() > 0)
            _url = _url.subSequence(0, _url.length() - 3).toString();
        if (StringUtility.isEmpty(_url))
            return null;
        else
            return _url;
    }
}
