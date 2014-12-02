package com.sharebravo.bravo.control.request;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sharebravo.bravo.model.SessionLogin;
import com.sharebravo.bravo.model.response.ObBravo;
import com.sharebravo.bravo.model.response.ObDeleteFollowing;
import com.sharebravo.bravo.model.response.ObDeleteLike;
import com.sharebravo.bravo.model.response.ObDeleteMylist;
import com.sharebravo.bravo.model.response.ObGetBravo;
import com.sharebravo.bravo.model.response.ObGetFollowingCheck;
import com.sharebravo.bravo.model.response.ObGetLikeItem;
import com.sharebravo.bravo.model.response.ObGetMylistItem;
import com.sharebravo.bravo.model.response.ObGetSpot;
import com.sharebravo.bravo.model.response.ObGetUserInfo;
import com.sharebravo.bravo.model.response.ObPostComment;
import com.sharebravo.bravo.model.response.ObPutFollowing;
import com.sharebravo.bravo.model.response.ObPutLike;
import com.sharebravo.bravo.model.response.ObPutMyList;
import com.sharebravo.bravo.model.response.ObPutReport;
import com.sharebravo.bravo.model.response.SNS;
import com.sharebravo.bravo.sdk.log.AIOLog;
import com.sharebravo.bravo.sdk.util.network.AsyncHttpDelete;
import com.sharebravo.bravo.sdk.util.network.AsyncHttpGet;
import com.sharebravo.bravo.sdk.util.network.AsyncHttpPost;
import com.sharebravo.bravo.sdk.util.network.AsyncHttpPostBravoWithImage;
import com.sharebravo.bravo.sdk.util.network.AsyncHttpPut;
import com.sharebravo.bravo.sdk.util.network.AsyncHttpResponseProcess;
import com.sharebravo.bravo.sdk.util.network.ParameterFactory;
import com.sharebravo.bravo.utils.BravoConstant;
import com.sharebravo.bravo.utils.BravoSharePrefs;
import com.sharebravo.bravo.utils.BravoUtils;
import com.sharebravo.bravo.utils.BravoWebServiceConfig;
import com.sharebravo.bravo.utils.StringUtility;
import com.sharebravo.bravo.view.adapter.AdapterUserDetail;
import com.sharebravo.bravo.view.fragment.FragmentBasic;

public class BravoRequestManager {
    // ================================================================
    private Context                    mContext;

    /* singleton class */
    private static BravoRequestManager mInstance;

    /**
     * RequestWrapper constructor
     * 
     * @param context
     */
    public BravoRequestManager(Context context) {
        super();
        mContext = context;
    }

    /**
     * get instance of RequestWrapper singleton class
     * 
     * @param context
     * @return instance
     */
    public static synchronized BravoRequestManager getInstance(Context context) {
        if (mInstance == null)
            mInstance = new BravoRequestManager(context);
        return mInstance;
    }

    // ======================== CORE FUNCTIONS ========================

    /**
     * control put,delete notifications
     * 
     * @param notificationType
     * @param isChecked
     * @param iRequestListener
     */
    public void requestToChangeNotificationsType(String notificationType, boolean isChecked, IRequestListener iRequestListener) {
        if (isChecked) {
            putNotification(notificationType, iRequestListener);
        } else {
            deleteNotification(notificationType, iRequestListener);
        }
    }

    // ==========================================================
    // Put Request
    // ==========================================================
    /**
     * put a SNS to bravo server
     * @param sns
     */
    public void putSNS(FragmentActivity activity,final SNS sns,final IRequestListener iRequestListener) {
        boolean isCheckExistedSNS = false;
        isCheckExistedSNS = BravoUtils.isSNSAlreadyLoggined(activity,sns);
        if (!isCheckExistedSNS)
            return;
        int loginBravoViaType = BravoSharePrefs.getInstance(mContext).getIntValue(BravoConstant.PREF_KEY_SESSION_LOGIN_BRAVO_VIA_TYPE);
        SessionLogin sessionLogin = BravoUtils.getSession(mContext, loginBravoViaType);
        String userId = sessionLogin.userID;
        String accessToken = sessionLogin.accessToken;
        HashMap<String, String> subParams = new HashMap<String, String>();
        subParams.put("Foreign_SNS", sns.foreignSNS);
        subParams.put("Foreign_ID", sns.foreignID);
        subParams.put("Foreign_Access_Token", sns.foreignAccessToken);
        JSONObject jsonObject = new JSONObject(subParams);
        List<NameValuePair> params = ParameterFactory.createSubParamsPutFollow(jsonObject.toString());
        String url = BravoWebServiceConfig.URL_PUT_SNS.replace("{User_ID}", userId).replace("{Access_Token}", accessToken);
        AsyncHttpPut putReport = new AsyncHttpPut(activity, new AsyncHttpResponseProcess(activity, null) {
            @Override
            public void processIfResponseSuccess(String response) {
                AIOLog.d("response putSNS :===>" + response);
                JSONObject jsonObject = null;

                try {
                    jsonObject = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (jsonObject == null)
                    return;

                String status = null;
                try {
                    status = jsonObject.getString("status");
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                if (status == String.valueOf(BravoWebServiceConfig.STATUS_RESPONSE_DATA_SUCCESS)) {
                    iRequestListener.onResponse(response);
                } else {
                    iRequestListener.onErrorResponse("Cannot put sns");
                }
            }

            @Override
            public void processIfResponseFail() {
                iRequestListener.onErrorResponse("Cannot put sns");
            }
        }, params, true);
        AIOLog.d(url);
        putReport.execute(url);
    }
    /**
     * request to put follow someone
     * 
     * @param bravoUserID
     * @param iRequestListener
     * @param fragmentBasic
     */
    public void requestToPutFollow(String bravoUserID, final IRequestListener iRequestListener, FragmentBasic fragmentBasic) {
        int loginBravoViaType = BravoSharePrefs.getInstance(mContext).getIntValue(BravoConstant.PREF_KEY_SESSION_LOGIN_BRAVO_VIA_TYPE);
        SessionLogin sessionLogin = BravoUtils.getSession(mContext, loginBravoViaType);
        String userId = sessionLogin.userID;
        String accessToken = sessionLogin.accessToken;
        HashMap<String, String> subParams = new HashMap<String, String>();
        subParams.put("User_ID", bravoUserID);
        JSONObject jsonObject = new JSONObject(subParams);
        List<NameValuePair> params = ParameterFactory.createSubParamsPutFollow(jsonObject.toString());
        String url = BravoWebServiceConfig.URL_PUT_FOLLOWING.replace("{User_ID}", userId).replace("{Access_Token}", accessToken);
        AsyncHttpPut putFollow = new AsyncHttpPut(mContext, new AsyncHttpResponseProcess(mContext, fragmentBasic) {
            @Override
            public void processIfResponseSuccess(String response) {
                AIOLog.d("response putFollow :===>" + response);
                JSONObject jsonObject = null;

                try {
                    jsonObject = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (jsonObject == null)
                    return;

                String status = null;
                try {
                    status = jsonObject.getString("status");
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                Gson gson = new GsonBuilder().serializeNulls().create();
                ObPutFollowing obPutFollowing;
                if (status == String.valueOf(BravoWebServiceConfig.STATUS_RESPONSE_DATA_SUCCESS)) {
                    iRequestListener.onResponse(response);
                } else {
                    obPutFollowing = gson.fromJson(response.toString(), ObPutFollowing.class);
                    iRequestListener.onErrorResponse(obPutFollowing.error);
                }
            }

            @Override
            public void processIfResponseFail() {
                AIOLog.d("response error");
            }
        }, params, true);
        AIOLog.d(url);
        putFollow.execute(url);
    }

    /**
     * request to put this item to my list items
     * 
     * @param bravoID
     * @param iRequestListener
     * @param fragmentBasic
     */
    public void requestToPutMyListItem(String bravoID, final IRequestListener iRequestListener, FragmentBasic fragmentBasic) {
        int loginBravoViaType = BravoSharePrefs.getInstance(mContext).getIntValue(BravoConstant.PREF_KEY_SESSION_LOGIN_BRAVO_VIA_TYPE);
        SessionLogin sessionLogin = BravoUtils.getSession(mContext, loginBravoViaType);
        String userId = sessionLogin.userID;
        String accessToken = sessionLogin.accessToken;
        HashMap<String, String> subParams = new HashMap<String, String>();
        subParams.put("Bravo_ID", bravoID);
        JSONObject jsonObject = new JSONObject(subParams);
        List<NameValuePair> params = ParameterFactory.createSubParamsPutFollow(jsonObject.toString());
        String url = BravoWebServiceConfig.URL_PUT_MYLIST.replace("{User_ID}", userId).replace("{Access_Token}", accessToken);
        AsyncHttpPut putMyListItem = new AsyncHttpPut(mContext, new AsyncHttpResponseProcess(mContext, fragmentBasic) {
            @Override
            public void processIfResponseSuccess(String response) {
                AIOLog.d("response putFollow :===>" + response);
                JSONObject jsonObject = null;

                try {
                    jsonObject = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (jsonObject == null)
                    return;

                String status = null;
                try {
                    status = jsonObject.getString("status");
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                Gson gson = new GsonBuilder().serializeNulls().create();
                ObPutMyList obPutMyList;
                if (status == String.valueOf(BravoWebServiceConfig.STATUS_RESPONSE_DATA_SUCCESS)) {
                    iRequestListener.onResponse(response);
                } else {
                    obPutMyList = gson.fromJson(response.toString(), ObPutMyList.class);
                    iRequestListener.onErrorResponse(obPutMyList.error);
                }
            }

            @Override
            public void processIfResponseFail() {
                iRequestListener.onErrorResponse("Cannot save this item");
            }
        }, params, true);
        AIOLog.d(url);
        putMyListItem.execute(url);
    }

    /**
     * request to put like to some one's spot
     * 
     * @param bravoID
     * @param iRequestListener
     * @param fragmentBasic
     */
    public void requestToPutLike(String bravoID, final IRequestListener iRequestListener, FragmentBasic fragmentBasic) {
        int loginBravoViaType = BravoSharePrefs.getInstance(mContext).getIntValue(BravoConstant.PREF_KEY_SESSION_LOGIN_BRAVO_VIA_TYPE);
        SessionLogin sessionLogin = BravoUtils.getSession(mContext, loginBravoViaType);
        String userId = sessionLogin.userID;
        String accessToken = sessionLogin.accessToken;
        HashMap<String, String> subParams = new HashMap<String, String>();
        subParams.put("Bravo_ID", bravoID);
        JSONObject jsonObject = new JSONObject(subParams);
        List<NameValuePair> params = ParameterFactory.createSubParamsPutFollow(jsonObject.toString());
        String url = BravoWebServiceConfig.URL_PUT_LIKE.replace("{User_ID}", userId).replace("{Access_Token}", accessToken);
        AsyncHttpPut putLikeItem = new AsyncHttpPut(mContext, new AsyncHttpResponseProcess(mContext, fragmentBasic) {
            @Override
            public void processIfResponseSuccess(String response) {
                AIOLog.d("response putFollow :===>" + response);
                JSONObject jsonObject = null;

                try {
                    jsonObject = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (jsonObject == null)
                    return;

                String status = null;
                try {
                    status = jsonObject.getString("status");
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                Gson gson = new GsonBuilder().serializeNulls().create();
                ObPutLike mObPutLike;
                if (status == String.valueOf(BravoWebServiceConfig.STATUS_RESPONSE_DATA_SUCCESS)) {
                    iRequestListener.onResponse(response);
                } else {
                    mObPutLike = gson.fromJson(response.toString(), ObPutLike.class);
                    iRequestListener.onErrorResponse(mObPutLike.error);
                }
            }

            @Override
            public void processIfResponseFail() {
                iRequestListener.onErrorResponse("Cannot like this spot");
            }
        }, params, true);
        putLikeItem.execute(url);
    }

    /**
     * request to put report
     * 
     * @param bravoUserId
     * @param iRequestListener
     * @param fragmentBasic
     */
    public void requestToPutReport(String bravoUserId, final IRequestListener iRequestListener, FragmentBasic fragmentBasic) {
        int loginBravoViaType = BravoSharePrefs.getInstance(mContext).getIntValue(BravoConstant.PREF_KEY_SESSION_LOGIN_BRAVO_VIA_TYPE);
        SessionLogin sessionLogin = BravoUtils.getSession(mContext, loginBravoViaType);
        String userId = sessionLogin.userID;
        String accessToken = sessionLogin.accessToken;
        HashMap<String, String> subParams = new HashMap<String, String>();
        subParams.put("Foreign_ID", bravoUserId);
        subParams.put("Report_Type", "bravo");
        subParams.put("User_ID", userId);
        subParams.put("Detail", "");
        JSONObject jsonObject = new JSONObject(subParams);
        List<NameValuePair> params = ParameterFactory.createSubParamsPutFollow(jsonObject.toString());
        String url = BravoWebServiceConfig.URL_PUT_REPORT.replace("{User_ID}", userId).replace("{Access_Token}", accessToken);
        AsyncHttpPut putReport = new AsyncHttpPut(mContext, new AsyncHttpResponseProcess(mContext, fragmentBasic) {
            @Override
            public void processIfResponseSuccess(String response) {
                AIOLog.d("response putReport :===>" + response);
                JSONObject jsonObject = null;

                try {
                    jsonObject = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (jsonObject == null)
                    return;

                String status = null;
                try {
                    status = jsonObject.getString("status");
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                Gson gson = new GsonBuilder().serializeNulls().create();
                ObPutReport obPutReport;
                if (status == String.valueOf(BravoWebServiceConfig.STATUS_RESPONSE_DATA_SUCCESS)) {
                    iRequestListener.onResponse(response);
                } else {
                    obPutReport = gson.fromJson(response.toString(), ObPutReport.class);
                    iRequestListener.onErrorResponse(obPutReport.error);
                }
            }

            @Override
            public void processIfResponseFail() {
                iRequestListener.onErrorResponse("cannot put report");
            }
        }, params, true);
        putReport.execute(url);
    }

    /**
     * put notification
     * 
     * @param notificationType
     * @param iRequestListener
     */
    private void putNotification(final String notificationType, final IRequestListener iRequestListener) {
        int loginBravoViaType = BravoSharePrefs.getInstance(mContext).getIntValue(BravoConstant.PREF_KEY_SESSION_LOGIN_BRAVO_VIA_TYPE);
        SessionLogin sessionLogin = BravoUtils.getSession(mContext, loginBravoViaType);
        String userId = sessionLogin.userID;
        String accessToken = sessionLogin.accessToken;
        HashMap<String, String> subParams = new HashMap<String, String>();
        subParams.put("Type", notificationType);
        JSONObject jsonObject = new JSONObject(subParams);
        List<NameValuePair> params = ParameterFactory.createSubParamsPutFollow(jsonObject.toString());
        String url = BravoWebServiceConfig.URL_PUT_NOTIFICATION.replace("{User_ID}", userId).replace("{Access_Token}", accessToken);
        AsyncHttpPut putReport = new AsyncHttpPut(mContext, new AsyncHttpResponseProcess(mContext, null) {
            @Override
            public void processIfResponseSuccess(String response) {
                AIOLog.d("response putNotification :===>" + response);
                JSONObject jsonObject = null;

                try {
                    jsonObject = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (jsonObject == null)
                    return;

                String status = null;
                try {
                    status = jsonObject.getString("status");
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                if (status == String.valueOf(BravoWebServiceConfig.STATUS_RESPONSE_DATA_SUCCESS)) {
                    iRequestListener.onResponse(response);
                } else {
                    iRequestListener.onErrorResponse("failed to put notification:" + notificationType + " by failed status");
                }
            }

            @Override
            public void processIfResponseFail() {
                iRequestListener.onErrorResponse("failed to put notification:" + notificationType);
            }
        }, params, true);
        AIOLog.d(url);
        putReport.execute(url);
    }

    // ======================================================
    // Delete Request
    // ======================================================
    public void deleteSNS(String foreignID, final IRequestListener iRequestListener) {
        int loginBravoViaType = BravoSharePrefs.getInstance(mContext).getIntValue(BravoConstant.PREF_KEY_SESSION_LOGIN_BRAVO_VIA_TYPE);
        SessionLogin sessionLogin = BravoUtils.getSession(mContext, loginBravoViaType);
        String userId = sessionLogin.userID;
        String accessToken = sessionLogin.accessToken;
        String url = BravoWebServiceConfig.URL_DELETE_SNS.replace("{User_ID}", userId).replace("{Access_Token}", accessToken)
                .replace("{SNS_ID}", foreignID);
        AsyncHttpDelete deleteSNS = new AsyncHttpDelete(mContext, new AsyncHttpResponseProcess(mContext, null) {
            @Override
            public void processIfResponseSuccess(String response) {
                AIOLog.d("response deleteSNS :===>" + response);
                JSONObject jsonObject = null;

                try {
                    jsonObject = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (jsonObject == null)
                    return;

                String status = null;
                try {
                    status = jsonObject.getString("status");
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                if (status == String.valueOf(BravoWebServiceConfig.STATUS_RESPONSE_DATA_SUCCESS)) {
                    iRequestListener.onResponse(response);
                } else {
                    iRequestListener.onErrorResponse("Cannot delete sns");
                }
            }

            @Override
            public void processIfResponseFail() {
                AIOLog.d("response error");
                iRequestListener.onErrorResponse("Cannot delete sns");
            }
        }, null, true);
        AIOLog.d(url);
        deleteSNS.execute(url);
    }

    /**
     * request to delete block user
     * 
     * @param foreignID
     * @param iRequestListener
     * @param fragmentBasic
     */
    public void requestDeleteBlock(String foreignID, final IRequestListener iRequestListener, FragmentBasic fragmentBasic) {
        int loginBravoViaType = BravoSharePrefs.getInstance(mContext).getIntValue(BravoConstant.PREF_KEY_SESSION_LOGIN_BRAVO_VIA_TYPE);
        SessionLogin sessionLogin = BravoUtils.getSession(mContext, loginBravoViaType);
        String userId = sessionLogin.userID;
        String accessToken = sessionLogin.accessToken;
        String url = BravoWebServiceConfig.URL_DELETE_BLOCKING.replace("{User_ID}", userId).replace("{Access_Token}", accessToken)
                .replace("{User_ID_Other}", foreignID);
        AsyncHttpDelete deleteBlock = new AsyncHttpDelete(mContext, new AsyncHttpResponseProcess(mContext, fragmentBasic) {
            @Override
            public void processIfResponseSuccess(String response) {
                AIOLog.d("response putFollow :===>" + response);
                JSONObject jsonObject = null;

                try {
                    jsonObject = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (jsonObject == null)
                    return;

                String status = null;
                try {
                    status = jsonObject.getString("status");
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                if (status == String.valueOf(BravoWebServiceConfig.STATUS_RESPONSE_DATA_SUCCESS)) {
                    iRequestListener.onResponse(response);
                } else {
                    iRequestListener.onErrorResponse("Cannot delete block");
                }
            }

            @Override
            public void processIfResponseFail() {
                AIOLog.d("response error");
                iRequestListener.onErrorResponse("Cannot delete block");
            }
        }, null, true);
        AIOLog.d(url);
        deleteBlock.execute(url);
    }

    /**
     * request delete follow
     * 
     * @param bravoUserID
     * @param iRequestListener
     * @param fragmentBasic
     */
    public void requestDeleteFollow(String bravoUserID, final IRequestListener iRequestListener, FragmentBasic fragmentBasic) {
        int loginBravoViaType = BravoSharePrefs.getInstance(mContext).getIntValue(BravoConstant.PREF_KEY_SESSION_LOGIN_BRAVO_VIA_TYPE);
        SessionLogin sessionLogin = BravoUtils.getSession(mContext, loginBravoViaType);
        String userId = sessionLogin.userID;
        String accessToken = sessionLogin.accessToken;
        String url = BravoWebServiceConfig.URL_DELETE_FOLLOWING.replace("{User_ID}", userId).replace("{Access_Token}", accessToken)
                .replace("{User_ID_Other}", bravoUserID);
        AsyncHttpDelete deleteFollow = new AsyncHttpDelete(mContext, new AsyncHttpResponseProcess(mContext, fragmentBasic) {
            @Override
            public void processIfResponseSuccess(String response) {
                AIOLog.d("response putFollow :===>" + response);
                JSONObject jsonObject = null;

                try {
                    jsonObject = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (jsonObject == null)
                    return;

                String status = null;
                try {
                    status = jsonObject.getString("status");
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                Gson gson = new GsonBuilder().serializeNulls().create();
                ObDeleteFollowing obDeleteFollowing;
                if (status == String.valueOf(BravoWebServiceConfig.STATUS_RESPONSE_DATA_SUCCESS)) {
                    iRequestListener.onResponse(response);
                } else {
                    obDeleteFollowing = gson.fromJson(response.toString(), ObDeleteFollowing.class);
                    iRequestListener.onErrorResponse(obDeleteFollowing.error);
                }
            }

            @Override
            public void processIfResponseFail() {
                iRequestListener.onErrorResponse("Cannot delete this follow");
            }
        }, null, true);
        AIOLog.d(url);
        deleteFollow.execute(url);
    }

    /**
     * request delete follow
     * 
     * @param bravoUserID
     * @param iRequestListener
     * @param fragmentBasic
     */
    public void requestToDeleteComment(String commentID, final IRequestListener iRequestListener) {
        int loginBravoViaType = BravoSharePrefs.getInstance(mContext).getIntValue(BravoConstant.PREF_KEY_SESSION_LOGIN_BRAVO_VIA_TYPE);
        SessionLogin sessionLogin = BravoUtils.getSession(mContext, loginBravoViaType);
        String userId = sessionLogin.userID;
        String accessToken = sessionLogin.accessToken;
        String url = BravoWebServiceConfig.URL_DELETE_COMMENT.replace("{User_ID}", userId).replace("{Access_Token}", accessToken)
                .replace("{Comment_ID}", commentID);
        AsyncHttpDelete deleteFollow = new AsyncHttpDelete(mContext, new AsyncHttpResponseProcess(mContext, null) {
            @Override
            public void processIfResponseSuccess(String response) {
                AIOLog.d("response delete comment :===>" + response);
                JSONObject jsonObject = null;

                try {
                    jsonObject = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (jsonObject == null)
                    return;

                String status = null;
                try {
                    status = jsonObject.getString("status");
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                if (status == String.valueOf(BravoWebServiceConfig.STATUS_RESPONSE_DATA_SUCCESS)) {
                    iRequestListener.onResponse(response);
                } else {
                    iRequestListener.onErrorResponse("Cannot delete this comment");
                }
            }

            @Override
            public void processIfResponseFail() {
                iRequestListener.onErrorResponse("Cannot delete this comment");
            }
        }, null, true);
        AIOLog.d(url);
        deleteFollow.execute(url);
    }

    /**
     * request to delete this item from my list item
     * 
     * @param bravoID
     * @param iRequestListener
     * @param fragmentBasic
     */
    public void requestDeleteMyListItem(String bravoID, final IRequestListener iRequestListener, FragmentBasic fragmentBasic) {
        int loginBravoViaType = BravoSharePrefs.getInstance(mContext).getIntValue(BravoConstant.PREF_KEY_SESSION_LOGIN_BRAVO_VIA_TYPE);
        SessionLogin sessionLogin = BravoUtils.getSession(mContext, loginBravoViaType);
        String userId = sessionLogin.userID;
        String accessToken = sessionLogin.accessToken;
        String url = BravoWebServiceConfig.URL_DELETE_MYLIST.replace("{User_ID}", userId).replace("{Access_Token}", accessToken)
                .replace("{Bravo_ID}", bravoID);
        AsyncHttpDelete deleteMyListItem = new AsyncHttpDelete(mContext, new AsyncHttpResponseProcess(mContext, fragmentBasic) {
            @Override
            public void processIfResponseSuccess(String response) {
                AIOLog.d("response putFollow :===>" + response);
                JSONObject jsonObject = null;

                try {
                    jsonObject = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (jsonObject == null)
                    return;

                String status = null;
                try {
                    status = jsonObject.getString("status");
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                Gson gson = new GsonBuilder().serializeNulls().create();
                ObDeleteMylist obDeleteMylist;
                if (status == String.valueOf(BravoWebServiceConfig.STATUS_RESPONSE_DATA_SUCCESS)) {
                    iRequestListener.onResponse(response);
                } else {
                    obDeleteMylist = gson.fromJson(response.toString(), ObDeleteMylist.class);
                    iRequestListener.onErrorResponse(obDeleteMylist.error);
                }
            }

            @Override
            public void processIfResponseFail() {
                iRequestListener.onErrorResponse("cannot delete this item");
            }
        }, null, true);
        AIOLog.d(url);
        deleteMyListItem.execute(url);
    }

    /**
     * request to delete like
     * 
     * @param bravoID
     * @param iRequestListener
     * @param fragmentBasic
     */
    public void requestDeleteLike(String bravoID, final IRequestListener iRequestListener, FragmentBasic fragmentBasic) {
        int loginBravoViaType = BravoSharePrefs.getInstance(mContext).getIntValue(BravoConstant.PREF_KEY_SESSION_LOGIN_BRAVO_VIA_TYPE);
        SessionLogin sessionLogin = BravoUtils.getSession(mContext, loginBravoViaType);
        String userId = sessionLogin.userID;
        String accessToken = sessionLogin.accessToken;
        String url = BravoWebServiceConfig.URL_DELETE_LIKE.replace("{User_ID}", userId).replace("{Access_Token}", accessToken)
                .replace("{Bravo_ID}", bravoID);
        AsyncHttpDelete deleteLike = new AsyncHttpDelete(mContext, new AsyncHttpResponseProcess(mContext, fragmentBasic) {
            @Override
            public void processIfResponseSuccess(String response) {
                AIOLog.d("response putFollow :===>" + response);
                JSONObject jsonObject = null;

                try {
                    jsonObject = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (jsonObject == null)
                    return;

                String status = null;
                try {
                    status = jsonObject.getString("status");
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                Gson gson = new GsonBuilder().serializeNulls().create();
                ObDeleteLike mObDeleteLike;
                if (status == String.valueOf(BravoWebServiceConfig.STATUS_RESPONSE_DATA_SUCCESS)) {
                    iRequestListener.onResponse(response);
                } else {
                    mObDeleteLike = gson.fromJson(response.toString(), ObDeleteLike.class);
                    iRequestListener.onErrorResponse(mObDeleteLike.error);
                }
            }

            @Override
            public void processIfResponseFail() {
                iRequestListener.onErrorResponse("Cannot delete like");
            }
        }, null, true);
        AIOLog.d(url);
        deleteLike.execute(url);
    }

    /**
     * put notification
     * 
     * @param notificationType
     * @param iRequestListener
     */
    private void deleteNotification(final String notificationType, final IRequestListener iRequestListener) {
        int loginBravoViaType = BravoSharePrefs.getInstance(mContext).getIntValue(BravoConstant.PREF_KEY_SESSION_LOGIN_BRAVO_VIA_TYPE);
        SessionLogin sessionLogin = BravoUtils.getSession(mContext, loginBravoViaType);
        String userId = sessionLogin.userID;
        String accessToken = sessionLogin.accessToken;
        String url = BravoWebServiceConfig.URL_DELETE_NOTIFICATION.replace("{User_ID}", userId).replace("{Access_Token}", accessToken)
                .replace("{Type}", notificationType);
        AsyncHttpDelete deleteSNS = new AsyncHttpDelete(mContext, new AsyncHttpResponseProcess(mContext, null) {
            @Override
            public void processIfResponseSuccess(String response) {
                AIOLog.d("response deleteSNS :===>" + response);
                JSONObject jsonObject = null;

                try {
                    jsonObject = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (jsonObject == null)
                    return;

                String status = null;
                try {
                    status = jsonObject.getString("status");
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                if (status == String.valueOf(BravoWebServiceConfig.STATUS_RESPONSE_DATA_SUCCESS)) {
                    iRequestListener.onResponse(response);
                } else {
                    iRequestListener.onErrorResponse("failed to delete notification:" + notificationType + " by failed status");
                }
            }

            @Override
            public void processIfResponseFail() {
                AIOLog.d("response error");
                iRequestListener.onErrorResponse("failed to delete notification:" + notificationType);
            }
        }, null, true);
        AIOLog.d(url);
        deleteSNS.execute(url);

    }

    /**
     * request to delete my account
     */
    public void requestToDeleteMyAccount(FragmentBasic fragmentBasic, final IRequestListener iRequestListener) {
        int loginBravoViaType = BravoSharePrefs.getInstance(mContext).getIntValue(BravoConstant.PREF_KEY_SESSION_LOGIN_BRAVO_VIA_TYPE);
        SessionLogin sessionLogin = BravoUtils.getSession(mContext, loginBravoViaType);
        String userId = sessionLogin.userID;
        String accessToken = sessionLogin.accessToken;
        String url = BravoWebServiceConfig.URL_DELETE_USER.replace("{User_ID}", userId).replace("{Access_Token}", accessToken);

        AsyncHttpDelete deleteAccount = new AsyncHttpDelete(mContext, new AsyncHttpResponseProcess(mContext, fragmentBasic) {
            @Override
            public void processIfResponseSuccess(String response) {
                AIOLog.d("response putFollow :===>" + response);
                iRequestListener.onResponse(response);
            }

            @Override
            public void processIfResponseFail() {
                AIOLog.d("response error");
                iRequestListener.onErrorResponse("response error");
            }
        }, null, true);
        AIOLog.d("requestToDeleteMyAccount:" + url);
        deleteAccount.execute(url);
    }

    /**
     * request to delete image avatar, cover of user by post user info API
     */
    public void requestToDeleteImageUserProfile(ObGetUserInfo obGetUserInfo, int imageType, final IRequestListener iRequestListener,
            FragmentBasic fragmentBasic) {
        HashMap<String, Boolean> subParams = new HashMap<String, Boolean>();
        if (AdapterUserDetail.USER_COVER_ID == imageType) {
            subParams.put("Cover_Img_Del", true);
        } else {
            subParams.put("Profile_Img_Del", true);
        }
        JSONObject jsonObject = new JSONObject(subParams);
        int _loginBravoViaType = BravoSharePrefs.getInstance(mContext).getIntValue(BravoConstant.PREF_KEY_SESSION_LOGIN_BRAVO_VIA_TYPE);
        SessionLogin _sessionLogin = BravoUtils.getSession(mContext, _loginBravoViaType);
        String userId = _sessionLogin.userID;
        String accessToken = _sessionLogin.accessToken;
        String putUserUrl = BravoWebServiceConfig.URL_PUT_USER.replace("{User_ID}", userId).replace("{Access_Token}", accessToken);
        List<NameValuePair> params = ParameterFactory.createSubParamsRequestToDeleteImage(jsonObject.toString());
        AsyncHttpPut postRegister = new AsyncHttpPut(mContext, new AsyncHttpResponseProcess(mContext, fragmentBasic) {
            @Override
            public void processIfResponseSuccess(String response) {
                AIOLog.d("reponse after delete image:" + response);
                /* go to home screen */
                iRequestListener.onResponse(response);
            }

            @Override
            public void processIfResponseFail() {
                iRequestListener.onErrorResponse("error");
            }
        }, params, true);
        postRegister.execute(putUserUrl);
    }

    /**
     * request to delete bravo picture of my spot
     */
    public void requestToDeleteBravoImage(ObBravo obBravo, final IRequestListener iRequestListener, FragmentBasic fragmentBasic) {
        int _loginBravoViaType = BravoSharePrefs.getInstance(mContext).getIntValue(BravoConstant.PREF_KEY_SESSION_LOGIN_BRAVO_VIA_TYPE);
        SessionLogin _sessionLogin = BravoUtils.getSession(mContext, _loginBravoViaType);
        String userId = _sessionLogin.userID;
        String accessToken = _sessionLogin.accessToken;
        String putUserUrl = BravoWebServiceConfig.URL_DELETE_BRAVO_PIC.replace("{User_ID}", userId).replace("{Access_Token}", accessToken)
                .replace("{Bravo_ID}", obBravo.Bravo_ID).replace("{Pic_Index}", "0");
        AsyncHttpDelete postRegister = new AsyncHttpDelete(mContext, new AsyncHttpResponseProcess(mContext, fragmentBasic) {
            @Override
            public void processIfResponseSuccess(String response) {
                AIOLog.d("reponse after delete bravo image:" + response);
                /* go to home screen */
                iRequestListener.onResponse(response);
            }

            @Override
            public void processIfResponseFail() {
                iRequestListener.onErrorResponse("error");
            }
        }, null, true);
        postRegister.execute(putUserUrl);
    }

    // =====================================================
    // Get Request
    // =====================================================
    /**
     * request all recent post at home tab
     * 
     * @param iRequestListener
     */
    public void requestNewsItemsOnBravoServer(final IRequestListener iRequestListener) {
        int _loginBravoViaType = BravoSharePrefs.getInstance(mContext).getIntValue(BravoConstant.PREF_KEY_SESSION_LOGIN_BRAVO_VIA_TYPE);
        SessionLogin _sessionLogin = BravoUtils.getSession(mContext, _loginBravoViaType);
        String userId = _sessionLogin.userID;
        String accessToken = _sessionLogin.accessToken;
        if (StringUtility.isEmpty(_sessionLogin.userID) || StringUtility.isEmpty(_sessionLogin.accessToken)) {
            userId = "";
            accessToken = "";
        }
        HashMap<String, String> subParams = new HashMap<String, String>();
        subParams.put("Global", "TRUE");
        subParams.put("View_Deleted_Users", "0");
        JSONObject jsonObject = new JSONObject(subParams);
        String subParamsJsonStr = jsonObject.toString();
        String url = BravoWebServiceConfig.URL_GET_ALL_BRAVO;
        List<NameValuePair> params = ParameterFactory.createSubParamsRequest(userId, accessToken, subParamsJsonStr);
        AsyncHttpGet getLoginRequest = new AsyncHttpGet(mContext, new AsyncHttpResponseProcess(mContext, null) {
            @Override
            public void processIfResponseSuccess(String response) {
                iRequestListener.onResponse(response);
            }

            @Override
            public void processIfResponseFail() {
                AIOLog.d("response error");
                iRequestListener.onErrorResponse("response error");
            }
        }, params, true);

        getLoginRequest.execute(url);

    }

    /**
     * request to get my list item
     * 
     * @param bravoID
     * @param iRequestListener
     */
    public void requestGetMyListItem(String bravoID, final IRequestListener iRequestListener) {
        int _loginBravoViaType = BravoSharePrefs.getInstance(mContext).getIntValue(BravoConstant.PREF_KEY_SESSION_LOGIN_BRAVO_VIA_TYPE);
        SessionLogin _sessionLogin = BravoUtils.getSession(mContext, _loginBravoViaType);
        String userId = _sessionLogin.userID;
        String accessToken = _sessionLogin.accessToken;
        String url = BravoWebServiceConfig.URL_GET_MYLIST_ITEM.replace("{User_ID}", userId).replace("{Bravo_ID}", bravoID);
        List<NameValuePair> params = ParameterFactory.createSubParamsGetBravo(userId, accessToken);
        AsyncHttpGet getMyListItemRequest = new AsyncHttpGet(mContext, new AsyncHttpResponseProcess(mContext, null) {
            @Override
            public void processIfResponseSuccess(String response) {
                AIOLog.d("requestFollowingCheck:" + response);
                Gson gson = new GsonBuilder().serializeNulls().create();
                ObGetMylistItem obGetMylistItem;
                obGetMylistItem = gson.fromJson(response.toString(), ObGetMylistItem.class);
                if (obGetMylistItem == null)
                    return;
                else {
                    iRequestListener.onResponse(response);
                }
            }

            @Override
            public void processIfResponseFail() {
                AIOLog.d("response error");
                iRequestListener.onErrorResponse("cannot get my list item");
            }
        }, params, true);
        getMyListItemRequest.execute(url);
    }

    /**
     * request to get like item
     * 
     * @param bravoID
     * @param iRequestListener
     */
    public void requestGetLikeItem(String bravoID, final IRequestListener iRequestListener) {
        int _loginBravoViaType = BravoSharePrefs.getInstance(mContext).getIntValue(BravoConstant.PREF_KEY_SESSION_LOGIN_BRAVO_VIA_TYPE);
        SessionLogin _sessionLogin = BravoUtils.getSession(mContext, _loginBravoViaType);
        String userId = _sessionLogin.userID;
        String accessToken = _sessionLogin.accessToken;
        String url = BravoWebServiceConfig.URL_GET_LIKE_ITEM.replace("{User_ID}", userId).replace("{Bravo_ID}", bravoID);
        List<NameValuePair> params = ParameterFactory.createSubParamsGetBravo(userId, accessToken);
        AsyncHttpGet getLikeItemRequest = new AsyncHttpGet(mContext, new AsyncHttpResponseProcess(mContext, null) {
            @Override
            public void processIfResponseSuccess(String response) {
                AIOLog.d("requestLikingCheck:" + response);
                Gson gson = new GsonBuilder().serializeNulls().create();
                ObGetLikeItem mObGetLikeItem;
                mObGetLikeItem = gson.fromJson(response.toString(), ObGetLikeItem.class);
                if (mObGetLikeItem == null)
                    return;
                else {
                    iRequestListener.onResponse(response);
                }
            }

            @Override
            public void processIfResponseFail() {
                iRequestListener.onErrorResponse("cannot get like item");
            }
        }, params, true);
        getLikeItemRequest.execute(url);
    }

    /**
     * request to get liked
     * 
     * @param spotID
     * @param iRequestListener
     */
    public void requestGetLiked(String spotID, final IRequestListener iRequestListener) {
        int _loginBravoViaType = BravoSharePrefs.getInstance(mContext).getIntValue(BravoConstant.PREF_KEY_SESSION_LOGIN_BRAVO_VIA_TYPE);
        SessionLogin _sessionLogin = BravoUtils.getSession(mContext, _loginBravoViaType);
        String userId = _sessionLogin.userID;
        String accessToken = _sessionLogin.accessToken;
        String url = BravoWebServiceConfig.URL_GET_SPOT.replace("{Spot_ID}", spotID);
        List<NameValuePair> params = ParameterFactory.createSubParamsGetBravo(userId, accessToken);
        AsyncHttpGet getLikedSavedRequest = new AsyncHttpGet(mContext, new AsyncHttpResponseProcess(mContext, null) {
            @Override
            public void processIfResponseSuccess(String response) {
                AIOLog.d("ObGetSpot:" + response);
                Gson gson = new GsonBuilder().serializeNulls().create();
                ObGetSpot mObGetSpot;
                mObGetSpot = gson.fromJson(response.toString(), ObGetSpot.class);
                if (mObGetSpot == null)
                    return;
                else {
                    AIOLog.e("Spot.data" + mObGetSpot.data);
                    iRequestListener.onResponse(response);
                }
            }

            @Override
            public void processIfResponseFail() {
                AIOLog.d("response error");
                iRequestListener.onErrorResponse("Cannot get liked");
            }
        }, params, true);
        getLikedSavedRequest.execute(url);
    }

    /**
     * request to get following check
     * 
     * @param bravoUserID
     * @param iRequestListener
     */
    public void requestGetFollowingCheck(String bravoUserID, final IRequestListener iRequestListener) {
        int _loginBravoViaType = BravoSharePrefs.getInstance(mContext).getIntValue(BravoConstant.PREF_KEY_SESSION_LOGIN_BRAVO_VIA_TYPE);
        SessionLogin _sessionLogin = BravoUtils.getSession(mContext, _loginBravoViaType);
        String userId = _sessionLogin.userID;
        String accessToken = _sessionLogin.accessToken;
        String url = BravoWebServiceConfig.URL_GET_FOLLOWING_CHECK.replace("{User_ID}", userId).replace("{User_ID_Other}", bravoUserID);
        List<NameValuePair> params = ParameterFactory.createSubParamsGetBravo(userId, accessToken);
        AsyncHttpGet getFollowingCheckRequest = new AsyncHttpGet(mContext, new AsyncHttpResponseProcess(mContext, null) {
            @Override
            public void processIfResponseSuccess(String response) {
                AIOLog.d("requestFollowingCheck:" + response);
                Gson gson = new GsonBuilder().serializeNulls().create();
                ObGetFollowingCheck obGetFollowCheck;
                obGetFollowCheck = gson.fromJson(response.toString(), ObGetFollowingCheck.class);
                if (obGetFollowCheck == null)
                    return;
                else {
                    iRequestListener.onResponse(response);
                }
            }

            @Override
            public void processIfResponseFail() {
                AIOLog.d("response error");
            }
        }, params, true);
        getFollowingCheckRequest.execute(url);
    }

    /**
     * get number allow bravo
     */
    /**
     * get user info to show on user data tab
     * 
     * @param foreignUserId
     */
    public void getNumberAllowBravo(Context context, final IRequestListener iRequestListener, FragmentBasic fragmentBasic) {
        int _loginBravoViaType = BravoSharePrefs.getInstance(context).getIntValue(BravoConstant.PREF_KEY_SESSION_LOGIN_BRAVO_VIA_TYPE);
        SessionLogin _sessionLogin = BravoUtils.getSession(context, _loginBravoViaType);
        String userId = _sessionLogin.userID;
        String accessToken = _sessionLogin.accessToken;
        String url = BravoWebServiceConfig.URL_GET_USER_INFO + "/" + userId;
        List<NameValuePair> params = ParameterFactory.createSubParamsGetNumberAllowBravo(userId, accessToken);
        AsyncHttpGet getUserInfoRequest = new AsyncHttpGet(mContext, new AsyncHttpResponseProcess(mContext, fragmentBasic) {

            @Override
            public void processIfResponseSuccess(String response) {
                AIOLog.d("get user info at my data:" + response);
                if (StringUtility.isEmpty(response))
                    return;
                iRequestListener.onResponse(response);
            }

            @Override
            public void processIfResponseFail() {
                AIOLog.d("response error");
                iRequestListener.onErrorResponse("can not load data");
            }
        }, params, true);
        getUserInfoRequest.execute(url);
    }

    /**
     * get comments for spot detail
     * 
     * @param context
     * @param bravoID
     * @param iRequestListener
     */
    public void requestToGetCommentsForSpotDetail(Context context, String bravoID, final IRequestListener iRequestListener) {
        int _loginBravoViaType = BravoSharePrefs.getInstance(context).getIntValue(BravoConstant.PREF_KEY_SESSION_LOGIN_BRAVO_VIA_TYPE);
        SessionLogin _sessionLogin = BravoUtils.getSession(context, _loginBravoViaType);
        String userId = _sessionLogin.userID;
        String accessToken = _sessionLogin.accessToken;
        String url = BravoWebServiceConfig.URL_GET_COMMENTS.replace("{Bravo_ID}", bravoID);
        List<NameValuePair> params = ParameterFactory.createSubParamsGetComments(userId, accessToken);
        AsyncHttpGet getCommentsRequest = new AsyncHttpGet(context, new AsyncHttpResponseProcess(context, null) {
            @Override
            public void processIfResponseSuccess(String response) {
                iRequestListener.onResponse(response);
            }

            @Override
            public void processIfResponseFail() {
                AIOLog.d("response error");
                iRequestListener.onErrorResponse("can not get comment");
            }
        }, params, true);
        getCommentsRequest.execute(url);
    }

    /**
     * request to get bravo object of spot detail
     * 
     * @param context
     * @param bravoID
     * @param iRequestListener
     * @param fragmentBasic
     */
    public void requestToGetBravoForSpotDetail(Context context, String bravoID, final IRequestListener iRequestListener, FragmentBasic fragmentBasic) {
        int _loginBravoViaType = BravoSharePrefs.getInstance(context).getIntValue(BravoConstant.PREF_KEY_SESSION_LOGIN_BRAVO_VIA_TYPE);
        SessionLogin _sessionLogin = BravoUtils.getSession(context, _loginBravoViaType);
        String userId = _sessionLogin.userID;
        String accessToken = _sessionLogin.accessToken;
        String url = BravoWebServiceConfig.URL_GET_BRAVO.replace("{Bravo_ID}", bravoID);
        List<NameValuePair> params = ParameterFactory.createSubParamsGetBravo(userId, accessToken);
        AsyncHttpGet getBravoRequest = new AsyncHttpGet(context, new AsyncHttpResponseProcess(context, fragmentBasic) {
            @Override
            public void processIfResponseSuccess(String response) {
                Gson gson = new GsonBuilder().serializeNulls().create();
                ObGetBravo obGetBravo;
                obGetBravo = gson.fromJson(response.toString(), ObGetBravo.class);
                AIOLog.d("obGetAllBravoRecentPosts:" + obGetBravo);
                if (obGetBravo == null)
                    return;
                else {
                    iRequestListener.onResponse(response);
                }
            }

            @Override
            public void processIfResponseFail() {
                iRequestListener.onErrorResponse("Cannot get the bravo object");
            }
        }, params, true);
        getBravoRequest.execute(url);
    }

    // ===================================================================
    // Post Request
    // ===================================================================
    /**
     * update image of spot
     * 
     * @param spotBitmap
     * @param bravoId
     * @param fragmentBasic
     * @param iRequestListener
     */
    public void requestToUpdateImageForBravo(Bitmap spotBitmap, String bravoId, FragmentBasic fragmentBasic, final IRequestListener iRequestListener) {

        int _loginBravoViaType = BravoSharePrefs.getInstance(mContext).getIntValue(BravoConstant.PREF_KEY_SESSION_LOGIN_BRAVO_VIA_TYPE);
        SessionLogin _sessionLogin = BravoUtils.getSession(mContext, _loginBravoViaType);
        String userId = _sessionLogin.userID;
        String accessToken = _sessionLogin.accessToken;
        if (spotBitmap == null)
            return;
        String encodedImage = null;
        BitmapFactory.Options options = new BitmapFactory.Options();

        options.inSampleSize = 1;
        options.inPurgeable = true;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        spotBitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        // bitmap object
        byte byteImage_photo[] = baos.toByteArray();
        encodedImage = Base64.encodeToString(byteImage_photo, Base64.DEFAULT);
        try {
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        HashMap<String, String> subParams = new HashMap<String, String>();
        subParams.put("image", encodedImage);
        subParams.put("Is_Private", "TRUE");

        JSONObject jsonObject = new JSONObject(subParams);
        String subParamsStr = jsonObject.toString();
        String putUserUrl = BravoWebServiceConfig.URL_POST_BRAVO.replace("{User_ID}", userId).replace("{Access_Token}", accessToken)
                .replace("{Bravo_ID}", bravoId + "");
        AIOLog.d("putUserUrl:" + putUserUrl);
        List<NameValuePair> params = ParameterFactory.createSubParams(subParamsStr);
        AsyncHttpPostBravoWithImage postBravoImage = new AsyncHttpPostBravoWithImage(mContext,
                new AsyncHttpResponseProcess(mContext, fragmentBasic) {
                    @Override
                    public void processIfResponseSuccess(String response) {
                        iRequestListener.onResponse(response);
                    }

                    @Override
                    public void processIfResponseFail() {
                        AIOLog.d("response error");
                        iRequestListener.onErrorResponse("can not upload image");
                    }
                }, params, true);
        postBravoImage.execute(putUserUrl);
    }

    /**
     * request to post comment
     * 
     * @param context
     * @param commentText
     * @param bravoId
     * @param iRequestListener
     * @param fragmentBasic
     */
    public void requestToPostComment(Context context, String commentText, String bravoId, final IRequestListener iRequestListener,
            FragmentBasic fragmentBasic) {
        int _loginBravoViaType = BravoSharePrefs.getInstance(mContext).getIntValue(BravoConstant.PREF_KEY_SESSION_LOGIN_BRAVO_VIA_TYPE);
        SessionLogin _sessionLogin = BravoUtils.getSession(mContext, _loginBravoViaType);
        String userId = _sessionLogin.userID;
        String accessToken = _sessionLogin.accessToken;
        HashMap<String, String> subParams = new HashMap<String, String>();
        subParams.put("User_ID", _sessionLogin.userID);
        subParams.put("Bravo_ID", bravoId);
        subParams.put("Comment_Text", commentText);
        JSONObject jsonObject = new JSONObject(subParams);
        List<NameValuePair> params = ParameterFactory.createSubParamsPutFollow(jsonObject.toString());
        String url = BravoWebServiceConfig.URL_POST_COMMENT.replace("{User_ID}", userId).replace("{Access_Token}", accessToken);
        AsyncHttpPost postComment = new AsyncHttpPost(context, new AsyncHttpResponseProcess(context, fragmentBasic) {
            @Override
            public void processIfResponseSuccess(String response) {
                AIOLog.d("response putFollow :===>" + response);
                JSONObject jsonObject = null;

                try {
                    jsonObject = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (jsonObject == null)
                    return;

                String status = null;
                try {
                    status = jsonObject.getString("status");
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                Gson gson = new GsonBuilder().serializeNulls().create();
                ObPostComment obPostComment;
                if (status == String.valueOf(BravoWebServiceConfig.STATUS_RESPONSE_DATA_SUCCESS)) {
                    iRequestListener.onResponse(response);
                } else {
                    obPostComment = gson.fromJson(response.toString(), ObPostComment.class);
                    if (obPostComment == null)
                        return;
                    iRequestListener.onErrorResponse(obPostComment.error);
                }
            }

            @Override
            public void processIfResponseFail() {
                iRequestListener.onErrorResponse("Cannot post the comment");
            }
        }, params, true);
        AIOLog.d(url);
        postComment.execute(url);
    }
}
