package com.sharebravo.bravo.view.fragment.home;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sharebravo.bravo.R;
import com.sharebravo.bravo.control.activity.HomeActivity;
import com.sharebravo.bravo.model.SessionLogin;
import com.sharebravo.bravo.model.response.ObBravo;
import com.sharebravo.bravo.model.response.ObDeleteFollowing;
import com.sharebravo.bravo.model.response.ObDeleteLike;
import com.sharebravo.bravo.model.response.ObDeleteMylist;
import com.sharebravo.bravo.model.response.ObGetBravo;
import com.sharebravo.bravo.model.response.ObGetComments;
import com.sharebravo.bravo.model.response.ObGetFollowingCheck;
import com.sharebravo.bravo.model.response.ObGetLikeItem;
import com.sharebravo.bravo.model.response.ObGetMylistItem;
import com.sharebravo.bravo.model.response.ObGetSpot;
import com.sharebravo.bravo.model.response.ObPostComment;
import com.sharebravo.bravo.model.response.ObPutFollowing;
import com.sharebravo.bravo.model.response.ObPutLike;
import com.sharebravo.bravo.model.response.ObPutMyList;
import com.sharebravo.bravo.model.response.ObPutReport;
import com.sharebravo.bravo.sdk.log.AIOLog;
import com.sharebravo.bravo.sdk.util.network.AsyncHttpDelete;
import com.sharebravo.bravo.sdk.util.network.AsyncHttpGet;
import com.sharebravo.bravo.sdk.util.network.AsyncHttpPost;
import com.sharebravo.bravo.sdk.util.network.AsyncHttpPut;
import com.sharebravo.bravo.sdk.util.network.AsyncHttpResponseProcess;
import com.sharebravo.bravo.sdk.util.network.ParameterFactory;
import com.sharebravo.bravo.utils.BravoConstant;
import com.sharebravo.bravo.utils.BravoSharePrefs;
import com.sharebravo.bravo.utils.BravoUtils;
import com.sharebravo.bravo.utils.BravoWebServiceConfig;
import com.sharebravo.bravo.view.adapter.AdapterBravoDetail;
import com.sharebravo.bravo.view.adapter.DetailPostListener;
import com.sharebravo.bravo.view.fragment.FragmentBasic;
import com.sharebravo.bravo.view.lib.pullrefresh_loadmore.XListView;
import com.sharebravo.bravo.view.lib.pullrefresh_loadmore.XListView.IXListViewListener;

public class FragmentBravoDetail extends FragmentBasic implements DetailPostListener {
    private static final int    REQUEST_CODE_CAMERA      = 2001;
    private static final int    REQUEST_CODE_GALLERY     = 2002;

    private Uri                 mCapturedImageURI        = null;
    private XListView           listviewRecentPostDetail = null;
    private AdapterBravoDetail  adapterRecentPostDetail  = null;

    private Button              btnBack;
    private OnItemClickListener onItemClick              = new OnItemClickListener() {

                                                             @Override
                                                             public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                                             }
                                                         };

    private ObBravo             mBravoObj;
    private SessionLogin        mSessionLogin            = null;
    private int                 mLoginBravoViaType       = BravoConstant.NO_LOGIN_SNS;

    // FragmentMapViewCover mapFragment = new FragmentMapViewCover();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = (ViewGroup) inflater.inflate(R.layout.page_recent_post_detail_tab, container);
        mHomeActionListener = (HomeActivity) getActivity();
        listviewRecentPostDetail = (XListView) root.findViewById(R.id.listview_recent_post_detail);
        adapterRecentPostDetail = new AdapterBravoDetail(getActivity(), this);
        adapterRecentPostDetail.setListener(this);
        listviewRecentPostDetail.setFooterDividersEnabled(false);
        listviewRecentPostDetail.setAdapter(adapterRecentPostDetail);
        listviewRecentPostDetail.setOnItemClickListener(onItemClick);
        listviewRecentPostDetail.setXListViewListener(new IXListViewListener() {

            @Override
            public void onRefresh() {
                onStopPullAndLoadListView();
            }

            @Override
            public void onLoadMore() {
                onStopPullAndLoadListView();
            }
        });
        listviewRecentPostDetail.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                adapterRecentPostDetail.parallaxImage(adapterRecentPostDetail.getBackGroundParallax());
            }
        });
        btnBack = (Button) root.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mHomeActionListener.goToBack();
            }
        });
        mLoginBravoViaType = BravoSharePrefs.getInstance(getActivity()).getIntValue(BravoConstant.PREF_KEY_SESSION_LOGIN_BRAVO_VIA_TYPE);
        mSessionLogin = BravoUtils.getSession(getActivity(), mLoginBravoViaType);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void setBravoOb(ObBravo obj) {
        this.mBravoObj = obj;
        FragmentMapViewCover.mLat = mBravoObj.Spot_Latitude;
        FragmentMapViewCover.mLong = mBravoObj.Spot_Longitude;
        adapterRecentPostDetail.setBravoOb(mBravoObj);
        adapterRecentPostDetail.notifyDataSetChanged();

    }

    private void onStopPullAndLoadListView() {
        listviewRecentPostDetail.stopRefresh();
        listviewRecentPostDetail.stopLoadMore();
    }

    private void requestGetComments() {
        // mObGetComments = null;
        String userId = mSessionLogin.userID;
        String accessToken = mSessionLogin.accessToken;
        String bravoID = mBravoObj.Bravo_ID;
        String url = BravoWebServiceConfig.URL_GET_COMMENTS.replace("{Bravo_ID}", bravoID);
        List<NameValuePair> params = ParameterFactory.createSubParamsGetComments(userId, accessToken);
        AsyncHttpGet getCommentsRequest = new AsyncHttpGet(getActivity(), new AsyncHttpResponseProcess(getActivity(), this) {
            @Override
            public void processIfResponseSuccess(String response) {
                AIOLog.d("requestGetComments:" + response);
                Gson gson = new GsonBuilder().serializeNulls().create();
                ObGetComments mObGetComments = gson.fromJson(response.toString(), ObGetComments.class);
                AIOLog.d("mObGetComments:" + mObGetComments);
                if (mObGetComments == null)
                    return;
                else {
                    AIOLog.d("size of recent post list: " + mObGetComments.data.size());
                    adapterRecentPostDetail.updateAllCommentList(mObGetComments);
                    if (listviewRecentPostDetail.getVisibility() == View.GONE)
                        listviewRecentPostDetail.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void processIfResponseFail() {
                AIOLog.d("response error");
            }
        }, params, true);
        getCommentsRequest.execute(url);
    }

    private void requestGetBravo() {
        String userId = mSessionLogin.userID;
        String accessToken = mSessionLogin.accessToken;
        String bravoID = mBravoObj.Bravo_ID;
        String url = BravoWebServiceConfig.URL_GET_BRAVO.replace("{Bravo_ID}", bravoID);
        List<NameValuePair> params = ParameterFactory.createSubParamsGetBravo(userId, accessToken);
        AsyncHttpGet getBravoRequest = new AsyncHttpGet(getActivity(), new AsyncHttpResponseProcess(getActivity(), this) {
            @Override
            public void processIfResponseSuccess(String response) {
                // AIOLog.d("requestBravoNews:" + response);
                Gson gson = new GsonBuilder().serializeNulls().create();
                ObGetBravo obGetBravo;
                obGetBravo = gson.fromJson(response.toString(), ObGetBravo.class);
                AIOLog.d("obGetAllBravoRecentPosts:" + obGetBravo); 
                if (obGetBravo == null)
                    return;
                else {
                    String Last_Pic = mBravoObj.Last_Pic;
                    mBravoObj = obGetBravo.data;
                    mBravoObj.Last_Pic = Last_Pic;
                    adapterRecentPostDetail.setBravoOb(mBravoObj);
                    adapterRecentPostDetail.updateMapView();
                    adapterRecentPostDetail.notifyDataSetChanged();
                    requestGetLiked();
                }
            }

            @Override
            public void processIfResponseFail() {
                AIOLog.d("response error");
            }
        }, params, true);
        getBravoRequest.execute(url);
    }

    private void requestGetFollowingCheck() {
        String userId = mSessionLogin.userID;
        String accessToken = mSessionLogin.accessToken;
        String url = BravoWebServiceConfig.URL_GET_FOLLOWING_CHECK.replace("{User_ID}", userId).replace("{User_ID_Other}", mBravoObj.User_ID);
        List<NameValuePair> params = ParameterFactory.createSubParamsGetBravo(userId, accessToken);
        AsyncHttpGet getFollowingCheckRequest = new AsyncHttpGet(getActivity(), new AsyncHttpResponseProcess(getActivity(), this) {
            @Override
            public void processIfResponseSuccess(String response) {
                AIOLog.d("requestFollowingCheck:" + response);
                Gson gson = new GsonBuilder().serializeNulls().create();
                ObGetFollowingCheck obGetFollowCheck;
                obGetFollowCheck = gson.fromJson(response.toString(), ObGetFollowingCheck.class);

                if (obGetFollowCheck == null)
                    return;
                else {
                    adapterRecentPostDetail.updateFollowing(obGetFollowCheck.valid == 1 ? true : false);
                }
            }

            @Override
            public void processIfResponseFail() {
                AIOLog.d("response error");
            }
        }, params, true);
        getFollowingCheckRequest.execute(url);
    }

    private void requestDeleteFollow(ObBravo obBravo) {
        String userId = mSessionLogin.userID;
        String accessToken = mSessionLogin.accessToken;
        String url = BravoWebServiceConfig.URL_DELETE_FOLLOWING.replace("{User_ID}", userId).replace("{Access_Token}", accessToken)
                .replace("{User_ID_Other}", mBravoObj.User_ID);
        AsyncHttpDelete deleteFollow = new AsyncHttpDelete(getActivity(), new AsyncHttpResponseProcess(getActivity(), this) {
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
                    adapterRecentPostDetail.updateFollowing(false);
                } else {
                    obDeleteFollowing = gson.fromJson(response.toString(), ObDeleteFollowing.class);
                    showToast(obDeleteFollowing.error);
                }
            }

            @Override
            public void processIfResponseFail() {
                AIOLog.d("response error");
            }
        }, null, true);
        AIOLog.d(url);
        deleteFollow.execute(url);
    }

    private void requestToPutFollow(ObBravo obBravo) {
        String userId = mSessionLogin.userID;
        String accessToken = mSessionLogin.accessToken;
        HashMap<String, String> subParams = new HashMap<String, String>();
        subParams.put("User_ID", obBravo.User_ID);
        JSONObject jsonObject = new JSONObject(subParams);
        List<NameValuePair> params = ParameterFactory.createSubParamsPutFollow(jsonObject.toString());
        String url = BravoWebServiceConfig.URL_PUT_FOLLOWING.replace("{User_ID}", userId).replace("{Access_Token}", accessToken);
        AsyncHttpPut putFollow = new AsyncHttpPut(getActivity(), new AsyncHttpResponseProcess(getActivity(), this) {
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
                    adapterRecentPostDetail.updateFollowing(true);
                    showDialogFollowingOK();
                } else {
                    obPutFollowing = gson.fromJson(response.toString(), ObPutFollowing.class);
                    showToast(obPutFollowing.error);
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

    private void requestGetMyListItem() {
        String userId = mSessionLogin.userID;
        String accessToken = mSessionLogin.accessToken;
        String url = BravoWebServiceConfig.URL_GET_MYLIST_ITEM.replace("{User_ID}", userId).replace("{Bravo_ID}", mBravoObj.Bravo_ID);
        List<NameValuePair> params = ParameterFactory.createSubParamsGetBravo(userId, accessToken);
        AsyncHttpGet getMyListItemRequest = new AsyncHttpGet(getActivity(), new AsyncHttpResponseProcess(getActivity(), this) {
            @Override
            public void processIfResponseSuccess(String response) {
                AIOLog.d("requestFollowingCheck:" + response);
                Gson gson = new GsonBuilder().serializeNulls().create();
                ObGetMylistItem obGetMylistItem;
                obGetMylistItem = gson.fromJson(response.toString(), ObGetMylistItem.class);
                // AIOLog.d("obGetAllBravoRecentPosts:" + mObGetComments);

                if (obGetMylistItem == null)
                    return;
                else {
                    adapterRecentPostDetail.updateSave(obGetMylistItem.valid == 1 ? true : false);
                }
            }

            @Override
            public void processIfResponseFail() {
                AIOLog.d("response error");
            }
        }, params, true);
        getMyListItemRequest.execute(url);
    }

    private void requestGetLikeItem() {
        String userId = mSessionLogin.userID;
        String accessToken = mSessionLogin.accessToken;
        String url = BravoWebServiceConfig.URL_GET_LIKE_ITEM.replace("{User_ID}", userId).replace("{Bravo_ID}", mBravoObj.Bravo_ID);
        List<NameValuePair> params = ParameterFactory.createSubParamsGetBravo(userId, accessToken);
        AsyncHttpGet getLikeItemRequest = new AsyncHttpGet(getActivity(), new AsyncHttpResponseProcess(getActivity(), this) {
            @Override
            public void processIfResponseSuccess(String response) {
                AIOLog.d("requestLikingCheck:" + response);
                Gson gson = new GsonBuilder().serializeNulls().create();
                ObGetLikeItem mObGetLikeItem;
                mObGetLikeItem = gson.fromJson(response.toString(), ObGetLikeItem.class);
                // AIOLog.d("obGetAllBravoRecentPosts:" + mObGetComments);

                if (mObGetLikeItem == null)
                    return;
                else {
                    adapterRecentPostDetail.updateLike(mObGetLikeItem.valid == 1 ? true : false);
                }
            }

            @Override
            public void processIfResponseFail() {
                AIOLog.d("response error");
            }
        }, params, true);
        getLikeItemRequest.execute(url);
    }

    private void requestGetLiked() {
        String userId = mSessionLogin.userID;
        String accessToken = mSessionLogin.accessToken;
        String url = BravoWebServiceConfig.URL_GET_SPOT.replace("{Spot_ID}", mBravoObj.Spot_ID);
        List<NameValuePair> params = ParameterFactory.createSubParamsGetBravo(userId, accessToken);
        AsyncHttpGet getLikedSavedRequest = new AsyncHttpGet(getActivity(), new AsyncHttpResponseProcess(getActivity(), this) {
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
                    adapterRecentPostDetail.updateLikedandSaved(mObGetSpot.data);

                }
            }

            @Override
            public void processIfResponseFail() {
                AIOLog.d("response error");
            }
        }, params, true);
        getLikedSavedRequest.execute(url);
    }

    private void requestDeleteMyListItem(ObBravo obBravo) {
        String userId = mSessionLogin.userID;
        String accessToken = mSessionLogin.accessToken;
        String url = BravoWebServiceConfig.URL_DELETE_MYLIST.replace("{User_ID}", userId).replace("{Access_Token}", accessToken)
                .replace("{Bravo_ID}", mBravoObj.Bravo_ID);
        AsyncHttpDelete deleteMyListItem = new AsyncHttpDelete(getActivity(), new AsyncHttpResponseProcess(getActivity(), this) {
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
                    adapterRecentPostDetail.updateSave(false);
                } else {
                    obDeleteMylist = gson.fromJson(response.toString(), ObDeleteMylist.class);
                    showToast(obDeleteMylist.error);
                }
            }

            @Override
            public void processIfResponseFail() {
                AIOLog.d("response error");
            }
        }, null, true);
        AIOLog.d(url);
        deleteMyListItem.execute(url);
    }

    private void requestToPutMyListItem(ObBravo obBravo) {
        String userId = mSessionLogin.userID;
        String accessToken = mSessionLogin.accessToken;
        HashMap<String, String> subParams = new HashMap<String, String>();
        subParams.put("Bravo_ID", obBravo.Bravo_ID);
        JSONObject jsonObject = new JSONObject(subParams);
        List<NameValuePair> params = ParameterFactory.createSubParamsPutFollow(jsonObject.toString());
        String url = BravoWebServiceConfig.URL_PUT_MYLIST.replace("{User_ID}", userId).replace("{Access_Token}", accessToken);
        AsyncHttpPut putMyListItem = new AsyncHttpPut(getActivity(), new AsyncHttpResponseProcess(getActivity(), this) {
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
                    adapterRecentPostDetail.updateSave(true);
                } else {
                    obPutMyList = gson.fromJson(response.toString(), ObPutMyList.class);
                    showToast(obPutMyList.error);
                }
            }

            @Override
            public void processIfResponseFail() {
                AIOLog.d("response error");
            }
        }, params, true);
        AIOLog.d(url);
        putMyListItem.execute(url);
    }

    private void requestDeleteLike(ObBravo obBravo) {
        String userId = mSessionLogin.userID;
        String accessToken = mSessionLogin.accessToken;
        String url = BravoWebServiceConfig.URL_DELETE_LIKE.replace("{User_ID}", userId).replace("{Access_Token}", accessToken)
                .replace("{Bravo_ID}", obBravo.Bravo_ID);
        AsyncHttpDelete deleteLike = new AsyncHttpDelete(getActivity(), new AsyncHttpResponseProcess(getActivity(), this) {
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
                    adapterRecentPostDetail.updateLike(false);
                    requestGetLiked();
                } else {
                    mObDeleteLike = gson.fromJson(response.toString(), ObDeleteLike.class);
                    showToast(mObDeleteLike.error);
                }
            }

            @Override
            public void processIfResponseFail() {
                AIOLog.d("response error");
            }
        }, null, true);
        AIOLog.d(url);
        deleteLike.execute(url);
    }

    private void requestToPutLike(ObBravo obBravo) {
        String userId = mSessionLogin.userID;
        String accessToken = mSessionLogin.accessToken;
        HashMap<String, String> subParams = new HashMap<String, String>();
        subParams.put("Bravo_ID", obBravo.Bravo_ID);
        JSONObject jsonObject = new JSONObject(subParams);
        List<NameValuePair> params = ParameterFactory.createSubParamsPutFollow(jsonObject.toString());
        String url = BravoWebServiceConfig.URL_PUT_LIKE.replace("{User_ID}", userId).replace("{Access_Token}", accessToken);
        AsyncHttpPut putLikeItem = new AsyncHttpPut(getActivity(), new AsyncHttpResponseProcess(getActivity(), this) {
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
                    adapterRecentPostDetail.updateLike(true);
                    requestGetLiked();
                } else {
                    mObPutLike = gson.fromJson(response.toString(), ObPutLike.class);
                    showToast(mObPutLike.error);
                }
            }

            @Override
            public void processIfResponseFail() {
                AIOLog.d("response error");
            }
        }, params, true);
        AIOLog.d(url);
        putLikeItem.execute(url);
    }

    private void requestToPutReport(ObBravo obBravo) {
        String userId = mSessionLogin.userID;
        String accessToken = mSessionLogin.accessToken;
        HashMap<String, String> subParams = new HashMap<String, String>();
        subParams.put("Foreign_ID", obBravo.User_ID);
        subParams.put("Report_Type", "bravo");
        subParams.put("User_ID", userId);
        subParams.put("Detail", "");
        JSONObject jsonObject = new JSONObject(subParams);
        List<NameValuePair> params = ParameterFactory.createSubParamsPutFollow(jsonObject.toString());
        String url = BravoWebServiceConfig.URL_PUT_REPORT.replace("{User_ID}", userId).replace("{Access_Token}", accessToken);
        AsyncHttpPut putReport = new AsyncHttpPut(getActivity(), new AsyncHttpResponseProcess(getActivity(), this) {
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
                    showDialogReportOk();
                } else {
                    obPutReport = gson.fromJson(response.toString(), ObPutReport.class);
                    showToast(obPutReport.error);
                }
            }

            @Override
            public void processIfResponseFail() {
                AIOLog.d("response error");
            }
        }, params, true);
        AIOLog.d(url);
        putReport.execute(url);
    }

    private void requestToPostComment(String commentText) {
        String userId = mSessionLogin.userID;
        String accessToken = mSessionLogin.accessToken;
        HashMap<String, String> subParams = new HashMap<String, String>();
        subParams.put("User_ID", mSessionLogin.userID);
        subParams.put("Bravo_ID", mBravoObj.Bravo_ID);
        subParams.put("Comment_Text", commentText);
        JSONObject jsonObject = new JSONObject(subParams);
        List<NameValuePair> params = ParameterFactory.createSubParamsPutFollow(jsonObject.toString());
        String url = BravoWebServiceConfig.URL_POST_COMMENT.replace("{User_ID}", userId).replace("{Access_Token}", accessToken);
        AsyncHttpPost postComment = new AsyncHttpPost(getActivity(), new AsyncHttpResponseProcess(getActivity(), this) {
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
                    requestGetComments();
                } else {
                    obPostComment = gson.fromJson(response.toString(), ObPostComment.class);
                    showToast(obPostComment.error);
                }
            }

            @Override
            public void processIfResponseFail() {
                AIOLog.d("response error");
            }
        }, params, true);
        AIOLog.d(url);
        postComment.execute(url);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && !isBackStatus()) {
            requestGetBravo();
            requestGetFollowingCheck();
            requestGetMyListItem();
            requestGetComments();
            requestGetLikeItem();
        }
    }

    @Override
    public void goToCallSpot() {
        showDialogCallSpot();
    }

    public void onCallSpot() {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mBravoObj.Spot_Phone));
        startActivity(intent);
    }

    @Override
    public void goToShare() {
        showDialogShare();
    }

    @Override
    public void goToFragment(int fragmentID) {
        if (fragmentID == HomeActivity.FRAGMENT_MAP_VIEW_ID) {
            mHomeActionListener.goToMapView(String.valueOf(mBravoObj.Spot_Latitude), String.valueOf(mBravoObj.Spot_Longitude),
                    FragmentMapView.MAKER_BY_LOCATION_SPOT);
            return;
        }
        mHomeActionListener.goToFragment(fragmentID);
    }

    public void showDialogCallSpot() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) getActivity().getLayoutInflater();
        View dialog_view = inflater.inflate(R.layout.dialog_call_spot, null);
        TextView content = (TextView) dialog_view.findViewById(R.id.call_spot_dialog_content);
        content.setText("Call " + mBravoObj.Spot_Name + "?");
        Button btnCancel = (Button) dialog_view.findViewById(R.id.btn_call_spot_no);
        btnCancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        Button btnOK = (Button) dialog_view.findViewById(R.id.btn_call_spot_yes);
        btnOK.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                onCallSpot();
            }
        });
        dialog.setContentView(dialog_view);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        // This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        dialog.show();
    }

    public void showDialogStopFollowing() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) getActivity().getLayoutInflater();
        View dialog_view = inflater.inflate(R.layout.dialog_stop_following, null);
        Button btnCancel = (Button) dialog_view.findViewById(R.id.btn_stop_following_no);
        btnCancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        Button btnOK = (Button) dialog_view.findViewById(R.id.btn_stop_following_yes);
        btnOK.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                requestDeleteFollow(mBravoObj);
            }
        });
        dialog.setContentView(dialog_view);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        // This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        dialog.show();
    }

    public void showDialogShare() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) getActivity().getLayoutInflater();
        View dialog_view = inflater.inflate(R.layout.dialog_goto_share, null);
        Button btnShareFacebook = (Button) dialog_view.findViewById(R.id.btn_share_facebook);
        btnShareFacebook.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                mHomeActionListener.goToShare(mBravoObj, FragmentShare.SHARE_ON_FACEBOOK);
            }
        });
        Button btnShareTwitter = (Button) dialog_view.findViewById(R.id.btn_share_twitter);
        btnShareTwitter.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                mHomeActionListener.goToShare(mBravoObj, FragmentShare.SHARE_ON_TWITTER);
            }
        });
        Button btnShareLine = (Button) dialog_view.findViewById(R.id.btn_share_line);
        btnShareLine.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                mHomeActionListener.goToShare(mBravoObj, FragmentShare.SHARE_ON_LINE);
            }
        });
        Button btnShareCancel = (Button) dialog_view.findViewById(R.id.btn_share_cancel);
        btnShareCancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setContentView(dialog_view);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        // This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);
        dialog.show();
    }

    public void showDialogReportOk() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) getActivity().getLayoutInflater();
        View dialog_view = inflater.inflate(R.layout.dialog_report_ok, null);
        Button btnReportClose = (Button) dialog_view.findViewById(R.id.btn_report_close);
        btnReportClose.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        dialog.setContentView(dialog_view);

        dialog.show();
    }

    public void showDialogFollowingOK() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) getActivity().getLayoutInflater();
        View dialog_view = inflater.inflate(R.layout.dialog_following, null);
        Button btnOK = (Button) dialog_view.findViewById(R.id.btn_ok);
        TextView txtContent = (TextView) dialog_view.findViewById(R.id.txt_following_content);
        txtContent.setText(getActivity().getResources().getString(R.string.content_following).replace("%s", mBravoObj.Full_Name));
        btnOK.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        dialog.setContentView(dialog_view);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        dialog.show();
    }

    private void showDialogChooseImage() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) getActivity().getLayoutInflater();
        View dialog_view = inflater.inflate(R.layout.dialog_choose_picture, null);
        Button btnTakeAPicture = (Button) dialog_view.findViewById(R.id.btn_take_picture);
        btnTakeAPicture.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // when user click camera to get image
                try {
                    String fileName = "cover" + Calendar.getInstance().getTimeInMillis() + ".jpg";
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.TITLE, fileName);
                    mCapturedImageURI = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
                    startActivityForResult(intent, REQUEST_CODE_CAMERA);
                } catch (Exception e) {
                    AIOLog.e("exception:" + e.getMessage());
                }
                dialog.dismiss();

            }
        });
        Button btnChooseFromLibrary = (Button) dialog_view.findViewById(R.id.btn_choose_from_library);
        btnChooseFromLibrary.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // when user click gallery to get image
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, REQUEST_CODE_GALLERY);
                dialog.dismiss();
            }
        });
        Button btnCancel = (Button) dialog_view.findViewById(R.id.btn_choose_picture_cancel);
        btnCancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setContentView(dialog_view);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        // This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);

        dialog.show();
    }

    public void showDialogReport() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) getActivity().getLayoutInflater();
        View dialog_view = inflater.inflate(R.layout.dialog_report, null);
        Button btnOk = (Button) dialog_view.findViewById(R.id.btn_report_yes);
        btnOk.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                requestToPutReport(mBravoObj);
            }
        });
        Button btnCancel = (Button) dialog_view.findViewById(R.id.btn_report_no);
        btnCancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setContentView(dialog_view);
        dialog.show();
    }

    @SuppressWarnings("static-access")
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        AIOLog.d("requestCode:" + requestCode + ", resultCode:" + resultCode + ", data:" + data);
        switch (requestCode) {
        case REQUEST_CODE_CAMERA:
            if (resultCode == getActivity().RESULT_OK) {
                if (mCapturedImageURI == null) {
                    AIOLog.d("mCapturedImageURI is null");
                    if (data == null || data.getExtras() == null)
                        return;
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    if (photo == null)
                        return;
                    else {
                        // postUpdateUserProfile(photo, mUserImageType);
                        return;
                    }
                }

                String[] projection = { MediaStore.Images.Media.DATA };
                Cursor cursor = getActivity().getContentResolver().query(mCapturedImageURI, projection, null, null, null);
                if (cursor == null) {
                    AIOLog.d("cursor is null");
                    return;
                }
                int column_index_data = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();

                // THIS IS WHAT YOU WANT!
                String capturedImageFilePath = cursor.getString(column_index_data);

                File file = new File(capturedImageFilePath);
                String imagePath = capturedImageFilePath;
                if (file.exists()) {
                    Uri fileUri = Uri.fromFile(file);
                    int orientation = BravoUtils.checkOrientation(fileUri);
                    // Bitmap bmp;
                    // bmp = BravoUtils.decodeSampledBitmapFromFile(imagePath, 100, 100, orientation);
                    // postUpdateUserProfile(bmp, mUserImageType);
                }
            }
            break;
        case REQUEST_CODE_GALLERY:
            if (resultCode == getActivity().RESULT_OK) {
                AIOLog.d("data=" + data);
                if (data == null) {
                    AIOLog.d("Opps!Can not get data from gallery.");
                    return;
                }

                Uri uri = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                Cursor cursor = getActivity().getContentResolver().query(uri, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String imagePath = cursor.getString(columnIndex);
                cursor.close();

                File file = new File(imagePath);
                if (file.exists()) {
                    Uri fileUri = Uri.fromFile(file);
                    int orientation = BravoUtils.checkOrientation(fileUri);
                    // Bitmap bmp;
                    // bmp = BravoUtils.decodeSampledBitmapFromFile(imagePath, 100, 100, orientation);
                    // postUpdateUserProfile(bmp, mUserImageType);
                } else {
                    AIOLog.d("file don't exist !");
                }
            }
            break;
        default:
            return;
        }
    }

    @Override
    public void goToReport() {
        showDialogReport();
    }

    @Override
    public void goToFollow(boolean isFollow) {
        if (isFollow)
            requestToPutFollow(mBravoObj);
        else
            showDialogStopFollowing();
    }

    @Override
    public void goToSubmitComment(String commentText) {
        requestToPostComment(commentText);
    }

    @Override
    public void goToSave(boolean isSave) {
        if (isSave)
            requestToPutMyListItem(mBravoObj);
        else
            requestDeleteMyListItem(mBravoObj);
    }

    @Override
    public void goToCoverImage() {
        mHomeActionListener.goToCoverImage(mBravoObj);
    }

    @Override
    public void goToUserDataTab(String useId) {
        mHomeActionListener.goToUserData(useId);
    }

    @Override
    public void choosePicture() {
        showDialogChooseImage();
    }

    @Override
    public void goToLiked() {
        // TODO Auto-generated method stub
        mHomeActionListener.goToLiked(mBravoObj.Spot_ID);
    }

    @Override
    public void goToSaved() {
        // TODO Auto-generated method stub
        mHomeActionListener.goToSaved(mBravoObj.Spot_ID);
    }

    @Override
    public void goToLike(boolean isLike) {
        // TODO Auto-generated method stub
        if (isLike)
            requestToPutLike(mBravoObj);
        else
            requestDeleteLike(mBravoObj);
    }
}
