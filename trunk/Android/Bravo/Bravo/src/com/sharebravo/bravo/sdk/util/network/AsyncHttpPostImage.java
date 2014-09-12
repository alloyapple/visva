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
public class AsyncHttpPostImage extends AsyncHttpBase {
    /**
     * Constructor
     * 
     * @param context
     * @param listener
     * @param parameters
     */
    public AsyncHttpPostImage(Context context, AsyncHttpResponseListener listener,
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
    public AsyncHttpPostImage(Context context, AsyncHttpResponseProcess process,
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
            byte[] coverdata;
            byte[] profileData;
            ByteArrayBody coverBab;
            ByteArrayBody profileBab;

            coverdata = Base64.decode(modelRequestImage.Cover_Img, Base64.DEFAULT);
            profileData = Base64.decode(modelRequestImage.Profile_Img, Base64.DEFAULT);

            profileBab = new ByteArrayBody(profileData, "image/png", md5(modelRequestImage.UserId) + ".profile.jpg");
            coverBab = new ByteArrayBody(coverdata, "image/png", md5(modelRequestImage.UserId) + ".cover.jpg");

            MultipartEntity reqEntity = new MultipartEntity(
                    HttpMultipartMode.BROWSER_COMPATIBLE, null, Charset.forName("UTF-8"));
            reqEntity.addPart("Profile_Img", profileBab);
            reqEntity.addPart("Cover_Img", coverBab);
            reqEntity.addPart("Profile_Img_Del", new StringBody("" + modelRequestImage.Profile_Img_Del, Charset.forName("UTF-8")));
            reqEntity.addPart("Cover_Img_Del", new StringBody("" + modelRequestImage.Cover_Img_Del, Charset.forName("UTF-8")));
            reqEntity.addPart("About_Me", new StringBody("" + modelRequestImage.About_Me, Charset.forName("UTF-8")));

            httppost.setEntity(reqEntity);

            response = httpclient.execute(httppost);
            statusCode = NETWORK_STATUS_OK;

            coverBab = null;
            coverdata = null;
            profileBab = null;
            profileData = null;
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
        @SerializedName("Profile_Img")
        public String Profile_Img;
        @SerializedName("Cover_Img")
        public String Cover_Img;
        @SerializedName("Profile_Img_Del")
        public int    Profile_Img_Del;
        @SerializedName("Cover_Img_Del")
        public int    Cover_Img_Del;
        @SerializedName("About_Me")
        public String About_Me;
        @SerializedName("UserId")
        public String UserId;
    }
}
