package com.sharebravo.bravo.sdk.util.network;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import android.content.Context;
import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.sharebravo.bravo.sdk.log.AIOLog;
import com.sharebravo.bravo.utils.BravoWebServiceConfig;

/**
 * AsyncHttpGet makes http post request based on AsyncTask
 * 
 * @author Visva
 */
public class AsyncHttpPostBravoWithImage extends AsyncHttpBase {
    /**
     * Constructor
     * 
     * @param context
     * @param listener
     * @param parameters
     */
    public AsyncHttpPostBravoWithImage(Context context, AsyncHttpResponseListener listener,
            List<NameValuePair> parameters, boolean isShowDilog) {
        super(context, listener, parameters, isShowDilog);
    }

    /**
     * Constructor
     * 
     * @param context
     * @param process
     * @param parameters
     */
    public AsyncHttpPostBravoWithImage(Context context, AsyncHttpResponseProcess process,
            List<NameValuePair> parameters, boolean isShowDilag) {
        super(context, process, parameters, isShowDilag);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.fgsecure.meyclub.app.network.AsyncHttpBase#request(java.lang.String)
     */
    @Override
    protected String request(String url) {
        try {
            HttpParams params = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(params, BravoWebServiceConfig.NETWORK_TIME_OUT);
            HttpConnectionParams.setSoTimeout(params, BravoWebServiceConfig.NETWORK_TIME_OUT);
            HttpClient httpclient = createHttpClient(url, params);
            HttpPost httppost = new HttpPost(url);

            String paramsName = parameters.get(0).getName();
            String paramsValue = parameters.get(0).getValue();
            AIOLog.d("paramsName:" + paramsName);
            AIOLog.d("parameValue:" + paramsValue);
            JSONObject jsonObject = new JSONObject(paramsValue);
            Gson gson = new GsonBuilder().serializeNulls().create();
            ModelRequestImage modelRequestImage = gson.fromJson(jsonObject.toString(), ModelRequestImage.class);
            if (modelRequestImage == null) {
                AIOLog.e("ModelRequestImage is null!!!!");
                return null;
            }
            byte[] imageData;
            ByteArrayBody imageBab;

            imageData = Base64.decode(modelRequestImage.image, Base64.DEFAULT);

            imageBab = new ByteArrayBody(imageData, "image/png", md5(System.currentTimeMillis() + "") + ".jpg");

            MultipartEntity reqEntity = new MultipartEntity(
                    HttpMultipartMode.BROWSER_COMPATIBLE, null, Charset.forName("UTF-8"));
            reqEntity.addPart("image", imageBab);
            reqEntity.addPart("Is_Private", new StringBody("" + modelRequestImage.Is_Private, Charset.forName("UTF-8")));

            httppost.setEntity(reqEntity);

            response = httpclient.execute(httppost);
            statusCode = NETWORK_STATUS_OK;

            imageBab = null;
            imageData = null;
        } catch (Exception e) {
            statusCode = NETWORK_STATUS_ERROR;
            e.printStackTrace();
        }
        return null;
    }

    public String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    class ModelRequestImage {
        @SerializedName("image")
        public String image;
        @SerializedName("Is_Private")
        public String Is_Private;
    }
}
