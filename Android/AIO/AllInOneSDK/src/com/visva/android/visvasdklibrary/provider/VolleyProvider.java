package com.visva.android.visvasdklibrary.provider;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Cache.Entry;
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
import com.visva.android.visvasdklibrary.constant.AIOConstant;
import com.visva.android.visvasdklibrary.log.AIOLog;
import com.visva.android.visvasdklibrary.util.StringUtils;
import com.visva.android.visvasdklibrary.volley.LruBitmapCache;

/**
 * Volley provider supports all methods of volley library includes:GET,POST,PUT,DELETE.
 * It can handler cache and image loader also.
 * 
 * @author kieu.thang
 * 
 */
public class VolleyProvider {
    /* TAG attributes for add to queue to request string by volley */
    private static final String   TAG_STRING_REQ      = "req_string";

    /* TAG attributes for add to queue to request json object by volley */
    private static final String   TAG_JSON_OBJECT_REQ = "req_json_object";

    /* TAG attributes for add to queue to request json array by volley */
    private static final String   TAG_JSON_ARRAY_REQ  = "req_json_array";

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
        if (mInstance == null){
            AIOLog.e(AIOConstant.TAG, "opps! mInstance is null.");
            mInstance = new VolleyProvider(context);
        }
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
        req.setTag(TextUtils.isEmpty(tag) ? AIOConstant.TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(AIOConstant.TAG);
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
    public void requestStringFromURL(String url, final IReponseListener iReponseListener) {
        StringRequest strReq = new StringRequest(Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                iReponseListener.onResponse(response);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
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
    public void requestJsonObjectFromURL(String url, final IReponseListener iReponseListener) {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                if (response == null || StringUtils.isEmpty(response.toString())) {
                    iReponseListener.onErrorResponse(AIOConstant.NETWORK_PARSE_JSON_ERROR, "");
                    return;
                }
                iReponseListener.onResponse(response.toString());
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        // {
        //
        // /**
        // * Passing some request headers
        // *
        // */
        // @Override
        // public Map<String, String> getHeaders() throws AuthFailureError {
        // HashMap<String, String> headers = new HashMap<String, String>();
        // headers.put("Content-Type", "application/json");
        // return headers;
        // }
        //
        // @Override
        // protected Map<String, String> getParams() {
        // Map<String, String> params = new HashMap<String, String>();
        // params.put("name", "Androidhive");
        // params.put("email", "abc@androidhive.info");
        // params.put("pass", "password123");
        //
        // return params;
        // }
        //
        // };

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
    public void requestJsonArrayFromURL(String url, final IReponseListener iReponseListener) {
        JsonArrayRequest req = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response == null || StringUtils.isEmpty(response.toString())) {
                    iReponseListener.onErrorResponse(AIOConstant.NETWORK_PARSE_JSON_ERROR, "");
                    return;
                }

                iReponseListener.onResponse(response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error == null) {
                    iReponseListener.onErrorResponse(AIOConstant.NETWORK_PARSE_JSON_ERROR, "");
                    return;
                }
            }
        });

        // Adding request to request queue
        VolleyProvider.getInstance(mContext).addToRequestQueue(req, TAG_JSON_ARRAY_REQ);
    }

    public void requestDataFromServerAPI(int requestType, String url, final IReponseListener iReponseListener, final Map<String, String> params) {
        // We first check for cached request
        Cache cache = getRequestQueue().getCache();
        Entry entry = cache.get(url + requestType);
        if (entry != null) {
            // fetch the data from cache
            try {
                String data = new String(entry.data, "UTF-8");
                iReponseListener.onResponse(data);
                return;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        // making fresh volley request and getting json
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(requestType, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                if (response == null || StringUtils.isEmpty(response.toString())) {
                    iReponseListener.onErrorResponse(AIOConstant.NETWORK_PARSE_JSON_ERROR, "");
                    return;
                }
                iReponseListener.onResponse(response.toString());
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
            }
        })
        {
            /**
             * Passing some request headers
             * 
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                if (params == null)
                    return null;
                return params;
            }

        };

        // Adding request to request queue
        VolleyProvider.getInstance(mContext).addToRequestQueue(jsonObjReq, url + requestType);
    }
}
