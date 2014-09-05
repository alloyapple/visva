package com.sharebravo.bravo.view.fragment.home;

import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sharebravo.bravo.R;
import com.sharebravo.bravo.control.activity.HomeActionListener;
import com.sharebravo.bravo.control.activity.HomeActivity;
import com.sharebravo.bravo.model.SessionLogin;
import com.sharebravo.bravo.model.response.ObDeleteFollowing;
import com.sharebravo.bravo.model.response.ObBravo;
import com.sharebravo.bravo.model.response.ObGetBravo;
import com.sharebravo.bravo.model.response.ObGetComments;
import com.sharebravo.bravo.model.response.ObGetFollowingCheck;
import com.sharebravo.bravo.model.response.ObPostComment;
import com.sharebravo.bravo.model.response.ObPutFollowing;
import com.sharebravo.bravo.model.user.BravoUser;
import com.sharebravo.bravo.model.user.ObGetLoginedUser;
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
import com.sharebravo.bravo.view.adapter.AdapterRecentPostDetail;
import com.sharebravo.bravo.view.adapter.DetailPostListener;
import com.sharebravo.bravo.view.fragment.FragmentBasic;
import com.sharebravo.bravo.view.lib.imageheader.PullAndLoadListView;

public class FragmentRecentPostDetail extends FragmentBasic implements DetailPostListener {
    private PullAndLoadListView     listviewRecentPostDetail = null;
    private AdapterRecentPostDetail adapterRecentPostDetail  = null;
    private HomeActionListener      mHomeActionListener      = null;

    // private SupportMapFragment mFragementImageMap = null;
    private Button                  btnBack;
    private OnItemClickListener     onItemClick              = new OnItemClickListener() {

                                                                 @Override
                                                                 public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                                     // TODO Auto-generated method stub

                                                                 }
                                                             };
    Button                          btnViewMap;
    Button                          btnCallSpot;

    ObBravo                         bravoObj;
//    ObGetComments                   mObGetComments;
    private SessionLogin            mSessionLogin            = null;
    private int                     mLoginBravoViaType       = BravoConstant.NO_LOGIN_SNS;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = (ViewGroup) inflater.inflate(R.layout.page_recent_post_detail_tab,
                null);
        mHomeActionListener = (HomeActivity) getActivity();
        listviewRecentPostDetail = (PullAndLoadListView) root.findViewById(R.id.listview_recent_post_detail);
        adapterRecentPostDetail = new AdapterRecentPostDetail(getActivity(), this);
        adapterRecentPostDetail.setListener(this);
        listviewRecentPostDetail.setFooterDividersEnabled(false);
        listviewRecentPostDetail.setAdapter(adapterRecentPostDetail);
        listviewRecentPostDetail.setOnItemClickListener(onItemClick);
        btnBack = (Button) root.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mHomeActionListener.goToBack();
            }
        });
        mLoginBravoViaType = BravoSharePrefs.getInstance(getActivity()).getIntValue(BravoConstant.PREF_KEY_SESSION_LOGIN_BRAVO_VIA_TYPE);
        mSessionLogin = BravoUtils.getSession(getActivity(), mLoginBravoViaType);
        // mFragementImageMap = (SupportMapFragment) getFragmentManager().findFragmentById(R.id.img_map);
        // if (mFragementImageMap == null) {
        // FragmentManager fragmentManager = getFragmentManager();
        // FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        // mFragementImageMap = SupportMapFragment.newInstance();
        // fragmentTransaction.replace(R.id.img_map, mFragementImageMap).commit();
        // }
        // mFragementImageMap =(FragmentMapView) findFragmentById(R.id.img_map);

        return root;
    }

    public void setBravoOb(ObBravo obj) {
        this.bravoObj = obj;
        adapterRecentPostDetail.setBravoOb(bravoObj);
        adapterRecentPostDetail.updateCommentList();
    }

    private void requestGetComments() {
//        mObGetComments = null;
        String userId = mSessionLogin.userID;
        String accessToken = mSessionLogin.accessToken;
        String bravoID = bravoObj.Bravo_ID;
        String url = BravoWebServiceConfig.URL_GET_COMMENTS.replace("{Bravo_ID}", bravoID);
        List<NameValuePair> params = ParameterFactory.createSubParamsGetComments(userId, accessToken);
        AsyncHttpGet getCommentsRequest = new AsyncHttpGet(getActivity(), new AsyncHttpResponseProcess(getActivity()) {
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
        String bravoID = bravoObj.Bravo_ID;
        String url = BravoWebServiceConfig.URL_GET_BRAVO.replace("{Bravo_ID}", bravoID);
        List<NameValuePair> params = ParameterFactory.createSubParamsGetBravo(userId, accessToken);
        AsyncHttpGet getBravoRequest = new AsyncHttpGet(getActivity(), new AsyncHttpResponseProcess(getActivity()) {
            @Override
            public void processIfResponseSuccess(String response) {
                // AIOLog.d("requestBravoNews:" + response);
                Gson gson = new GsonBuilder().serializeNulls().create();
                ObGetBravo obGetBravo;
                obGetBravo = gson.fromJson(response.toString(), ObGetBravo.class);
                // AIOLog.d("obGetAllBravoRecentPosts:" + mObGetComments);
                if (obGetBravo == null)
                    return;
                else {
                    // AIOLog.d("size of recent post list: " + mObGetComments.data.size());
                    bravoObj = obGetBravo.data;
                    adapterRecentPostDetail.setBravoOb(bravoObj);
                    // requestGetComments();
                    requestGetFollowingCheck();
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
        String url = BravoWebServiceConfig.URL_GET_FOLLOWING_CHECK.replace("{User_ID}", userId).replace("{User_ID_Other}", bravoObj.User_ID);
        List<NameValuePair> params = ParameterFactory.createSubParamsGetBravo(userId, accessToken);
        AsyncHttpGet getFollowingCheckRequest = new AsyncHttpGet(getActivity(), new AsyncHttpResponseProcess(getActivity()) {
            @Override
            public void processIfResponseSuccess(String response) {
                AIOLog.d("requestFollowingCheck:" + response);
                Gson gson = new GsonBuilder().serializeNulls().create();
                ObGetFollowingCheck obGetFollowCheck;
                obGetFollowCheck = gson.fromJson(response.toString(), ObGetFollowingCheck.class);
                // AIOLog.d("obGetAllBravoRecentPosts:" + mObGetComments);

                if (obGetFollowCheck == null)
                    return;
                else {
                    // AIOLog.d("size of recent post list: " + mObGetComments.data.size());

                    adapterRecentPostDetail.updateFollowing(obGetFollowCheck.valid == 0 ? true : false);
                    requestGetComments();
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
                .replace("{User_ID_Other}", bravoObj.User_ID);
        AsyncHttpDelete deleteFollow = new AsyncHttpDelete(getActivity(), new AsyncHttpResponseProcess(getActivity()) {
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
        AsyncHttpPut putFollow = new AsyncHttpPut(getActivity(), new AsyncHttpResponseProcess(getActivity()) {
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

    private void requestToPostComment(String commentText) {
        String userId = mSessionLogin.userID;
        String accessToken = mSessionLogin.accessToken;
        HashMap<String, String> subParams = new HashMap<String, String>();
        subParams.put("User_ID", mSessionLogin.userID);
        subParams.put("Bravo_ID", bravoObj.Bravo_ID);
        subParams.put("Comment_Text", commentText);
        JSONObject jsonObject = new JSONObject(subParams);
        List<NameValuePair> params = ParameterFactory.createSubParamsPutFollow(jsonObject.toString());
        String url = BravoWebServiceConfig.URL_POST_COMMENT.replace("{User_ID}", userId).replace("{Access_Token}", accessToken);
        AsyncHttpPost postComment = new AsyncHttpPost(getActivity(), new AsyncHttpResponseProcess(getActivity()) {
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
        // TODO Auto-generated method stub
        super.onHiddenChanged(hidden);
        if (!hidden) {
            requestGetBravo();
        }
    }

    @Override
    public void goToCallSpot() {
        // TODO Auto-generated method stub
        // Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + bravoObj.Spot_Phone));
        // startActivity(intent);
        showDialogCallSpot();
    }

    public void onCallSpot() {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + bravoObj.Spot_Phone));
        startActivity(intent);
    }

    @Override
    public void goToShare() {
        // TODO Auto-generated method stub
        showDialogShare();
    }

    @Override
    public void goToSave() {
        // TODO Auto-generated method stub

    }

    @Override
    public void goToFragment(int fragmentID) {
        // TODO Auto-generated method stub
        mHomeActionListener.goToFragment(fragmentID);
    }

    public void showDialogCallSpot() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) getActivity().getLayoutInflater();
        View dialog_view = inflater.inflate(R.layout.dialog_call_spot, null);
        TextView content = (TextView) dialog_view.findViewById(R.id.call_spot_dialog_content);
        content.setText("Call " + bravoObj.Spot_Name + "?");
        Button btnCancel = (Button) dialog_view.findViewById(R.id.btn_call_spot_no);
        btnCancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dialog.dismiss();

            }
        });
        Button btnOK = (Button) dialog_view.findViewById(R.id.btn_call_spot_yes);
        btnOK.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
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
                // TODO Auto-generated method stub
                dialog.dismiss();

            }
        });
        Button btnShareTwitter = (Button) dialog_view.findViewById(R.id.btn_share_twitter);
        btnShareTwitter.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }
        });
        Button btnShareLine = (Button) dialog_view.findViewById(R.id.btn_share_line);
        btnShareLine.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }
        });
        Button btnShareCancel = (Button) dialog_view.findViewById(R.id.btn_share_cancel);
        btnShareCancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
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
                // TODO Auto-generated method stub
                dialog.dismiss();
            }
        });
        Button btnCancel = (Button) dialog_view.findViewById(R.id.btn_report_no);
        btnCancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }
        });
        dialog.setContentView(dialog_view);
        dialog.show();
    }

    @Override
    public void goToReport() {
        // TODO Auto-generated method stub
        showDialogReport();
    }

    @Override
    public void goToFollow(boolean isFollow) {
        // TODO Auto-generated method stub
        if (isFollow)
            requestToPutFollow(bravoObj);
        else
            requestDeleteFollow(bravoObj);
    }

    @Override
    public void goToSubmitComment(String commentText) {
        // TODO Auto-generated method stub
        requestToPostComment(commentText);
    }
}
