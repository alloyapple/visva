package com.sharebravo.bravo.view.fragment.userprofile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sharebravo.bravo.R;
import com.sharebravo.bravo.control.activity.HomeActivity;
import com.sharebravo.bravo.control.request.BravoRequestManager;
import com.sharebravo.bravo.control.request.IRequestListener;
import com.sharebravo.bravo.model.SessionLogin;
import com.sharebravo.bravo.model.response.ObBravo;
import com.sharebravo.bravo.model.response.ObGetBlockingCheck;
import com.sharebravo.bravo.model.response.ObGetFollowingCheck;
import com.sharebravo.bravo.model.response.ObGetUserInfo;
import com.sharebravo.bravo.model.response.ObGetUserTimeline;
import com.sharebravo.bravo.model.response.ObPutBlocking;
import com.sharebravo.bravo.model.response.ObPutFollowing;
import com.sharebravo.bravo.model.response.ObUserImagePost;
import com.sharebravo.bravo.sdk.log.AIOLog;
import com.sharebravo.bravo.sdk.util.network.AsyncHttpGet;
import com.sharebravo.bravo.sdk.util.network.AsyncHttpPostImage;
import com.sharebravo.bravo.sdk.util.network.AsyncHttpPut;
import com.sharebravo.bravo.sdk.util.network.AsyncHttpResponseProcess;
import com.sharebravo.bravo.sdk.util.network.NetworkUtility;
import com.sharebravo.bravo.sdk.util.network.ParameterFactory;
import com.sharebravo.bravo.utils.BravoConstant;
import com.sharebravo.bravo.utils.BravoSharePrefs;
import com.sharebravo.bravo.utils.BravoUtils;
import com.sharebravo.bravo.utils.BravoWebServiceConfig;
import com.sharebravo.bravo.utils.StringUtility;
import com.sharebravo.bravo.view.adapter.AdapterUserDetail;
import com.sharebravo.bravo.view.adapter.CropOption;
import com.sharebravo.bravo.view.adapter.CropOptionAdapter;
import com.sharebravo.bravo.view.adapter.UserPostProfileListener;
import com.sharebravo.bravo.view.fragment.FragmentBasic;
import com.sharebravo.bravo.view.fragment.maps.FragmentMapView;
import com.sharebravo.bravo.view.lib.pullrefresh_loadmore.XListView;
import com.sharebravo.bravo.view.lib.pullrefresh_loadmore.XListView.IXListViewListener;

public class FragmentUserDataTab extends FragmentBasic implements UserPostProfileListener {
    private static final int    REQUEST_CODE_CAMERA      = 2001;
    private static final int    REQUEST_CODE_GALLERY     = 2002;
    private static final int    CROP_FROM_CAMERA         = 2003;

    private Uri                 mCapturedImageURI        = null;
    private Button              mBtnSettings;
    private IShowPageSettings   iShowPageSettings;
    private XListView           mListViewUserPostProfile = null;
    private ObGetUserInfo       mObGetUserInfo;
    private AdapterUserDetail   mAdapterUserDataProfile  = null;
    private Button              mBtnBack;
    private boolean             isMyData                 = false;
    private static int          mUserImageType;

    private OnItemClickListener onItemClick              = new OnItemClickListener() {

                                                             @Override
                                                             public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                                 AIOLog.d("mObGetUserTimeline.data.size:" + mObGetUserTimeline.data.size()
                                                                         + ",position" + position);
                                                                 if (position >= 2) {
                                                                     mHomeActionListener.goToRecentPostDetail(mObGetUserTimeline.data
                                                                             .get(position - 2));
                                                                 }
                                                             }
                                                         };
    private SessionLogin        mSessionLogin            = null;
    private int                 mLoginBravoViaType       = BravoConstant.NO_LOGIN_SNS;
    private String              foreignID                = "";
    private ObGetUserTimeline   mObGetUserTimeline;
    private boolean             isOutOfDataLoadMore;
    private LinearLayout        mLayoutPoorConnection;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = (ViewGroup) inflater.inflate(R.layout.page_fragment_user_profile, null);
        mLoginBravoViaType = BravoSharePrefs.getInstance(getActivity()).getIntValue(BravoConstant.PREF_KEY_SESSION_LOGIN_BRAVO_VIA_TYPE);
        mSessionLogin = BravoUtils.getSession(getActivity(), mLoginBravoViaType);
        mHomeActionListener = (HomeActivity) getActivity();
        initializeView(root);
        return root;
    }

    private void onStopPullAndLoadListView() {
        mListViewUserPostProfile.stopRefresh();
        mListViewUserPostProfile.stopLoadMore();
    }

    private void initializeView(View root) {
        mLayoutPoorConnection = (LinearLayout) root.findViewById(R.id.layout_poor_connection);
        if (NetworkUtility.getInstance(getActivity()).isNetworkAvailable()) {
            mLayoutPoorConnection.setVisibility(View.GONE);
        } else {
            mLayoutPoorConnection.setVisibility(View.VISIBLE);
        }
        mBtnSettings = (Button) root.findViewById(R.id.btn_settings);
        mListViewUserPostProfile = (XListView) root.findViewById(R.id.listview_user_post_profile);
        mAdapterUserDataProfile = new AdapterUserDetail(getActivity());
        mAdapterUserDataProfile.setListener(this);
        mListViewUserPostProfile.setFooterDividersEnabled(false);
        mListViewUserPostProfile.setAdapter(mAdapterUserDataProfile);
        mListViewUserPostProfile.setOnItemClickListener(onItemClick);
        onStopPullAndLoadListView();
        mListViewUserPostProfile.setXListViewListener(new IXListViewListener() {

            @Override
            public void onRefresh() {
                onStopPullAndLoadListView();
            }

            @Override
            public void onLoadMore() {
                if (mObGetUserTimeline == null) {
                    onStopPullAndLoadListView();
                    return;
                }
                AIOLog.d("IOnRefreshListener:" + mObGetUserTimeline.data.size());
                int size = mObGetUserTimeline.data.size();
                if (size > 0 && !isOutOfDataLoadMore && NetworkUtility.getInstance(getActivity()).isNetworkAvailable())
                    onPullDownToRefreshBravoItems(false, size);
                else
                    onStopPullAndLoadListView();
            }
        });
        mListViewUserPostProfile.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (mAdapterUserDataProfile != null)
                    mAdapterUserDataProfile.parallaxImage(mAdapterUserDataProfile.getBackGroundParallax());
            }
        });
        mBtnBack = (Button) root.findViewById(R.id.btn_back);
        mBtnBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mHomeActionListener.goToBack();
            }
        });
        mBtnSettings.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                iShowPageSettings.showPageSettings();
            }
        });
    }

    private void onPullDownToRefreshBravoItems(final boolean isPulDownToRefresh, int position) {
        String userId = mSessionLogin.userID;
        String accessToken = mSessionLogin.accessToken;
        if (StringUtility.isEmpty(mSessionLogin.userID) || StringUtility.isEmpty(mSessionLogin.accessToken)) {
            userId = "";
            accessToken = "";
        }
        HashMap<String, String> subParams = new HashMap<String, String>();
        subParams.put("Start", position + "");
        JSONObject subParamsJson = new JSONObject(subParams);
        String subParamsJsonStr = subParamsJson.toString();
        String url = BravoWebServiceConfig.URL_GET_USER_TIMELINE.replace("{User_ID}", foreignID);
        List<NameValuePair> params = ParameterFactory.createSubParamsGetTimeLine(userId, accessToken, subParamsJsonStr);
        AsyncHttpGet getTimeline = new AsyncHttpGet(getActivity(), new AsyncHttpResponseProcess(getActivity(), null) {
            @Override
            public void processIfResponseSuccess(String response) {
                Gson gson = new GsonBuilder().serializeNulls().create();
                ObGetUserTimeline obGetUserTimeline = gson.fromJson(response.toString(), ObGetUserTimeline.class);
                AIOLog.d("obGetUserTimeline:" + obGetUserTimeline);
                if (obGetUserTimeline == null || obGetUserTimeline.data.size() == 0) {
                    if (!isPulDownToRefresh)
                        isOutOfDataLoadMore = true;
                } else {
                    ArrayList<ObBravo> obBravos = modifyIncorrectBravoItems(obGetUserTimeline.data);
                    if (!isPulDownToRefresh)
                        mObGetUserTimeline.data.addAll(obBravos);
                    else
                        mObGetUserTimeline.data.addAll(0, obBravos);
                    mAdapterUserDataProfile.updatePullDownLoadMorePostList(obBravos, isPulDownToRefresh);
                }
                onStopPullAndLoadListView();
                return;
            }

            @Override
            public void processIfResponseFail() {
                AIOLog.d("response error");
                onStopPullAndLoadListView();
            }
        }, params, true);
        getTimeline.execute(url);

    }

    private ArrayList<ObBravo> modifyIncorrectBravoItems(ArrayList<ObBravo> bravoItems) {
        ArrayList<ObBravo> obBravos = new ArrayList<ObBravo>();
        for (ObBravo obBravo : bravoItems) {
            if (StringUtility.isEmpty(obBravo.User_ID) || (StringUtility.isEmpty(obBravo.Full_Name) || "0".equals(obBravo.User_ID))) {
                AIOLog.e("The incorrect bravo items:" + obBravo.User_ID + ", obBravo.Full_Name:" + obBravo.Full_Name);
                obBravo.User_ID = mObGetUserInfo.data.User_ID;
                obBravo.Full_Name = mObGetUserInfo.data.Full_Name;
                obBravo.Profile_Img_URL = mObGetUserInfo.data.Profile_Img_URL;
            }
            obBravos.add(obBravo);
        }
        return obBravos;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            // if (!isBackStatus()) {
            if (NetworkUtility.getInstance(getActivity()).isNetworkAvailable()) {
                getUserInfo(foreignID);
                mAdapterUserDataProfile.clearTimeLine();
                mListViewUserPostProfile.setSelection(0);
                requestGetBlockingCheck();
                requestGetFollowingCheck();
                requestGetUserTimeLine(foreignID);
            } else {
                mObGetUserInfo = BravoUtils.getUserInfoFromDb(getActivity());
                if (mObGetUserInfo == null) {
                    AIOLog.e("obGetUserInfo is null");
                } else {
                    mAdapterUserDataProfile.updateUserProfile(mObGetUserInfo, isMyData);
                }
            }
            // }
            checkUserDataType(foreignID);
        } else {
            isOutOfDataLoadMore = false;
        }
    }

    private void checkUserDataType(String foreignID) {
        final int _loginBravoViaType = BravoSharePrefs.getInstance(getActivity()).getIntValue(BravoConstant.PREF_KEY_SESSION_LOGIN_BRAVO_VIA_TYPE);
        SessionLogin _sessionLogin = BravoUtils.getSession(getActivity(), _loginBravoViaType);
        String userId = _sessionLogin.userID;
        if (foreignID.equals(userId)) {
            isMyData = true;
        } else {
            isMyData = false;
        }
        mAdapterUserDataProfile.updateUserProfileType(isMyData);
    }

    /**
     * get user info to show on user data tab
     * 
     * @param foreignUserId
     */
    public void getUserInfo(final String foreignUserId) {

        final int _loginBravoViaType = BravoSharePrefs.getInstance(getActivity()).getIntValue(BravoConstant.PREF_KEY_SESSION_LOGIN_BRAVO_VIA_TYPE);
        SessionLogin _sessionLogin = BravoUtils.getSession(getActivity(), _loginBravoViaType);
        String userId = _sessionLogin.userID;
        String accessToken = _sessionLogin.accessToken;
        AIOLog.d("mUserId:" + _sessionLogin.userID + ", mAccessToken:" + _sessionLogin.accessToken);
        if (StringUtility.isEmpty(_sessionLogin.userID) || StringUtility.isEmpty(_sessionLogin.accessToken)) {
            userId = "";
            accessToken = "";
        }

        String checkingUserId = foreignUserId;
        if (foreignUserId.equals(userId)) {
            mBtnBack.setVisibility(View.GONE);
            mBtnSettings.setVisibility(View.VISIBLE);
            isMyData = true;
        } else {
            isMyData = false;
            mBtnBack.setVisibility(View.VISIBLE);
            mBtnSettings.setVisibility(View.GONE);
        }

        String url = BravoWebServiceConfig.URL_GET_USER_INFO + "/" + checkingUserId;
        List<NameValuePair> params = ParameterFactory.createSubParamsGetAllBravoItems(userId, accessToken);
        AsyncHttpGet getUserInfoRequest = new AsyncHttpGet(getActivity(), new AsyncHttpResponseProcess(getActivity(), this) {

            @Override
            public void processIfResponseSuccess(String response) {
                AIOLog.d("get user info at my data:" + response);
                if (StringUtility.isEmpty(response))
                    return;
                Gson gson = new GsonBuilder().serializeNulls().create();
                mObGetUserInfo = gson.fromJson(response.toString(), ObGetUserInfo.class);
                if (mObGetUserInfo == null) {
                    AIOLog.e("obGetUserInfo is null");
                } else {
                    switch (mObGetUserInfo.status) {
                    case BravoConstant.STATUS_FAILED:
                        showToast(getActivity().getResources().getString(R.string.get_user_info_error));
                        break;
                    case BravoConstant.STATUS_SUCCESS:
                        AIOLog.d("BravoConstant.STATUS_SUCCESS");
                        AIOLog.d("BravoConstant.data" + mObGetUserInfo.data);
                        mAdapterUserDataProfile.updateUserProfile(mObGetUserInfo, isMyData);
                        onStopPullAndLoadListView();
                        if (isMyData) {
                            BravoUtils.saveUserDataBeforeExit(getActivity(), mObGetUserInfo);
                        }
                        break;
                    default:
                        break;
                    }
                }
            }

            @Override
            public void processIfResponseFail() {
                AIOLog.d("response error");
            }
        }, params, true);
        getUserInfoRequest.execute(url);
    }

    private void requestGetUserTimeLine(String checkingUserId) {
        String userId = mSessionLogin.userID;
        String accessToken = mSessionLogin.accessToken;
        if (StringUtility.isEmpty(mSessionLogin.userID) || StringUtility.isEmpty(mSessionLogin.accessToken)) {
            userId = "";
            accessToken = "";
        }
        HashMap<String, String> subParams = new HashMap<String, String>();
        subParams.put("Start", "0");
        JSONObject subParamsJson = new JSONObject(subParams);
        String subParamsJsonStr = subParamsJson.toString();
        String url = BravoWebServiceConfig.URL_GET_USER_TIMELINE.replace("{User_ID}", checkingUserId);
        List<NameValuePair> params = ParameterFactory.createSubParamsGetTimeLine(userId, accessToken, subParamsJsonStr);
        AsyncHttpGet getTimeline = new AsyncHttpGet(getActivity(), new AsyncHttpResponseProcess(getActivity(), null) {
            @Override
            public void processIfResponseSuccess(String response) {
                AIOLog.d("obGetUserTimeline:" + response);
                mListViewUserPostProfile.setVisibility(View.VISIBLE);
                Gson gson = new GsonBuilder().serializeNulls().create();
                mObGetUserTimeline = gson.fromJson(response.toString(), ObGetUserTimeline.class);
                AIOLog.d("obGetUserTimeline:" + mObGetUserTimeline);
                if (mObGetUserTimeline == null || mObGetUserTimeline.data.size() == 0) {
                    mAdapterUserDataProfile.updateRecentPostList(null);
                    return;
                }
                else {
                    ArrayList<ObBravo> obBravos = modifyIncorrectBravoItems(mObGetUserTimeline.data);
                    mAdapterUserDataProfile.updateRecentPostList(obBravos);
                }
                mListViewUserPostProfile.setVisibility(View.VISIBLE);
            }

            @Override
            public void processIfResponseFail() {
                AIOLog.d("response error");
            }
        }, params, true);
        getTimeline.execute(url);

    }

    public void addUserBravoLastPic(ArrayList<ObBravo> bravoItems) {
        if (bravoItems == null)
            return;
        for (int i = 0; i < bravoItems.size(); i++) {
            bravoItems.get(i).Last_Pic = bravoItems.get(i).Bravo_Pics.size() > 0 ? bravoItems.get(i).Bravo_Pics.get(0) : "";
            bravoItems.get(i).User_ID = foreignID;
        }
    }

    private void requestGetFollowingCheck() {
        String userId = mSessionLogin.userID;
        String accessToken = mSessionLogin.accessToken;
        String url = BravoWebServiceConfig.URL_GET_FOLLOWING_CHECK.replace("{User_ID}", userId).replace("{User_ID_Other}", foreignID);
        List<NameValuePair> params = ParameterFactory.createSubParamsGetBravo(userId, accessToken);
        AsyncHttpGet getFollowingCheckRequest = new AsyncHttpGet(getActivity(), new AsyncHttpResponseProcess(getActivity(), null) {
            @Override
            public void processIfResponseSuccess(String response) {
                AIOLog.d("requestFollowingCheck:" + response);
                Gson gson = new GsonBuilder().serializeNulls().create();
                ObGetFollowingCheck obGetFollowCheck;
                obGetFollowCheck = gson.fromJson(response.toString(), ObGetFollowingCheck.class);

                if (obGetFollowCheck == null)
                    return;
                else {
                    mAdapterUserDataProfile.updateFollow(obGetFollowCheck.valid == 1 ? true : false);
                }
            }

            @Override
            public void processIfResponseFail() {
                AIOLog.d("response error");
            }
        }, params, true);
        getFollowingCheckRequest.execute(url);
    }

    private void requestDeleteFollow() {
        BravoRequestManager.getInstance(getActivity()).requestDeleteFollow(foreignID, new IRequestListener() {

            @Override
            public void onResponse(String response) {
                mAdapterUserDataProfile.updateFollow(false);
            }

            @Override
            public void onErrorResponse(String errorMessage) {
                AIOLog.d("errorMessage:" + errorMessage);
            }
        }, FragmentUserDataTab.this);
    }

    private void requestToPutFollow() {
        String userId = mSessionLogin.userID;
        String accessToken = mSessionLogin.accessToken;
        HashMap<String, String> subParams = new HashMap<String, String>();
        subParams.put("User_ID", foreignID);
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
                    mAdapterUserDataProfile.updateFollow(true);
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

    private void requestGetBlockingCheck() {
        String userId = mSessionLogin.userID;
        String accessToken = mSessionLogin.accessToken;
        String url = BravoWebServiceConfig.URL_GET_BLOCKING_CHECK.replace("{User_ID}", userId).replace("{User_ID_Other}", foreignID);
        List<NameValuePair> params = ParameterFactory.createSubParamsGetBravo(userId, accessToken);
        AsyncHttpGet getBkockingCheckRequest = new AsyncHttpGet(getActivity(), new AsyncHttpResponseProcess(getActivity(), null) {
            @Override
            public void processIfResponseSuccess(String response) {
                AIOLog.d("requestFollowingCheck:" + response);
                Gson gson = new GsonBuilder().serializeNulls().create();
                ObGetBlockingCheck obGetBlockingCheck;
                obGetBlockingCheck = gson.fromJson(response.toString(), ObGetBlockingCheck.class);

                if (obGetBlockingCheck == null)
                    return;
                else {
                    mAdapterUserDataProfile.updateBlock(obGetBlockingCheck.valid == 1 ? true : false);
                }
            }

            @Override
            public void processIfResponseFail() {
                AIOLog.d("response error");
            }
        }, params, true);
        getBkockingCheckRequest.execute(url);
    }

    private void requestDeleteBlock() {
        BravoRequestManager.getInstance(getActivity()).requestDeleteBlock(foreignID, new IRequestListener() {

            @Override
            public void onResponse(String response) {
                AIOLog.d("response putFollow :===>" + response);
                mAdapterUserDataProfile.updateBlock(false);
            }

            @Override
            public void onErrorResponse(String errorMessage) {
                AIOLog.d("response error");
            }
        }, FragmentUserDataTab.this);
    }

    private void requestToPutBlock() {
        String userId = mSessionLogin.userID;
        String accessToken = mSessionLogin.accessToken;
        HashMap<String, String> subParams = new HashMap<String, String>();
        subParams.put("User_ID", foreignID);
        JSONObject jsonObject = new JSONObject(subParams);
        List<NameValuePair> params = ParameterFactory.createSubParamsPutFollow(jsonObject.toString());
        String url = BravoWebServiceConfig.URL_PUT_BLOCKING.replace("{User_ID}", userId).replace("{Access_Token}", accessToken);
        AsyncHttpPut putBlock = new AsyncHttpPut(getActivity(), new AsyncHttpResponseProcess(getActivity(), this) {
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
                ObPutBlocking obPutBlocking;
                if (status == String.valueOf(BravoWebServiceConfig.STATUS_RESPONSE_DATA_SUCCESS)) {
                    mAdapterUserDataProfile.updateBlock(true);
                } else {
                    obPutBlocking = gson.fromJson(response.toString(), ObPutBlocking.class);
                    showToast(obPutBlocking.error);
                }
            }

            @Override
            public void processIfResponseFail() {
                AIOLog.d("response error");
            }
        }, params, true);
        AIOLog.d(url);
        putBlock.execute(url);
    }

    public interface IShowPageSettings {
        public void showPageSettings();

        public void onClickUserAvatar(String userId);
    }

    public void setListener(IShowPageSettings ÌShowPageSettings) {
        this.iShowPageSettings = ÌShowPageSettings;
    }

    @Override
    public void requestUserImageType(int userImageType) {
        mUserImageType = userImageType;
        showDialogChooseImage(userImageType, mObGetUserInfo);
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
                requestDeleteFollow();
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

    public void getUserFollowHistory() {
    }

    public void getUserUserFollowing() {
    }

    public void getUserFollower() {
    }

    public void showDialogFollowingOK() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) getActivity().getLayoutInflater();
        View dialog_view = inflater.inflate(R.layout.dialog_following, null);
        Button btnOK = (Button) dialog_view.findViewById(R.id.btn_ok);
        FrameLayout frameLoop = (FrameLayout) dialog_view.findViewById(R.id.flower_loop);
        View view = new GIFView(getActivity());
        view.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.FILL_PARENT, FrameLayout.LayoutParams.FILL_PARENT));
//        mInputStream = getActivity().getResources().openRawResource(R.drawable.flower_anim);
//        mMovie = Movie.decodeStream(mInputStream);
        frameLoop.addView(view);
        TextView txtContent = (TextView) dialog_view.findViewById(R.id.txt_following_content);
        txtContent.setText(getActivity().getResources().getString(R.string.profile_follow_alert).replace("%s", mObGetUserInfo.data.Full_Name));
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
        // This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        dialog.show();
    }
    private class GIFView extends View{

        public GIFView(Context context) {
            super(context);
            mInputStream = context.getResources().openRawResource(R.drawable.flower_anim);
            mMovie = Movie.decodeStream(mInputStream);
        }

        private Movie       mMovie;
        private InputStream mInputStream = null;
        private long              mMovieStart;

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            long now = android.os.SystemClock.uptimeMillis();
            if (mMovieStart == 0) {
                mMovieStart = now;
            }
            int relTime = (int) ((now - mMovieStart) % mMovie.duration());
            mMovie.setTime(relTime);
            double scalex = (double) this.getWidth() / (double) mMovie.width();
             double scaley = (double) this.getHeight() / (double) mMovie.height();
            canvas.scale((float) scalex, (float) scaley);
            mMovie.draw(canvas, 0, 0, paint);
            this.invalidate();
        }
    }

    private void showDialogChooseImage(final int userImageType, ObGetUserInfo obGetUserInfo) {
        String imageProfileUrl = obGetUserInfo.data.Profile_Img_URL;
        String imageCoverUrl = obGetUserInfo.data.Cover_Img_URL;

        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) getActivity().getLayoutInflater();
        View dialog_view = inflater.inflate(R.layout.dialog_choose_picture, null);
        Button btnZoomAPicture = (Button) dialog_view.findViewById(R.id.btn_zoom_a_picture);

        if ((AdapterUserDetail.USER_AVATAR_ID == userImageType && StringUtility.isEmpty(imageProfileUrl))
                || (AdapterUserDetail.USER_COVER_ID == userImageType && StringUtility.isEmpty(imageCoverUrl))) {
            btnZoomAPicture.setVisibility(View.GONE);
        }
        btnZoomAPicture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                mHomeActionListener.goToViewImage(mObGetUserInfo, userImageType);
            }
        });

        Button btnTakeAPicture = (Button) dialog_view.findViewById(R.id.btn_take_picture);
        btnTakeAPicture.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // when user click camera to get image
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                mCapturedImageURI = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
                        "tmp_avatar_" + String.valueOf(System.currentTimeMillis()) + ".jpg"));

                intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mCapturedImageURI);

                try {
                    intent.putExtra("return-data", true);

                    startActivityForResult(intent, REQUEST_CODE_CAMERA);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }
        });
        Button btnChooseFromLibrary = (Button) dialog_view.findViewById(R.id.btn_choose_from_library);
        btnChooseFromLibrary.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // when user click gallery to get image
                Intent intent = new Intent();

                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent, "Complete action using"), REQUEST_CODE_GALLERY);
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

    @SuppressWarnings("static-access")
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        AIOLog.d("requestCode:" + requestCode + ", resultCode:" + resultCode + ", data:" + data);
        switch (requestCode) {
        case REQUEST_CODE_CAMERA:
            if (resultCode == getActivity().RESULT_OK) {
                cropImageAfterPicking();
            }
            break;
        case REQUEST_CODE_GALLERY:
            if (resultCode == getActivity().RESULT_OK) {
                mCapturedImageURI = data.getData();

                cropImageAfterPicking();
            }
            break;
        case CROP_FROM_CAMERA:
            if (resultCode == getActivity().RESULT_OK) {
                Bundle extras = data.getExtras();
                if (extras != null) {
                    Bitmap photo = extras.getParcelable("data");
                    postUpdateUserProfile(photo, mUserImageType);
                }
                File f = new File(mCapturedImageURI.getPath());

                if (f.exists())
                    f.delete();
            }
            break;
        default:
            return;
        }
    }

    @Override
    public void goToFragment(int fragmentID) {
        mHomeActionListener.goToFragment(fragmentID);
    }

    @Override
    public void onClickUserAvatar(String userId) {
    }

    @Override
    public void goToMapView() {
        if (mObGetUserInfo != null)
            mHomeActionListener.goToMapView(mObGetUserInfo.data.User_ID, FragmentMapView.MAKER_BY_LOCATION_USER, mObGetUserInfo.data.Full_Name);
        else
            mHomeActionListener.goToMapView(null, FragmentMapView.MAKER_BY_LOCATION_USER, null);
    }

    @Override
    public void goToFollow(boolean isFollow) {
        if (isFollow)
            requestToPutFollow();
        else
            showDialogStopFollowing();
    }

    @Override
    public void goToBlock(boolean isBlock) {
        if (isBlock)
            requestToPutBlock();
        else
            requestDeleteBlock();
    }

    @Override
    public void goToUserTimeline() {
        mHomeActionListener.goToUserTimeLine(mObGetUserInfo);
    }

    @Override
    public void goToUserFollowing() {
        mHomeActionListener.goToUsergFollowing(foreignID);
    }

    @Override
    public void goToUserFollower() {
        mHomeActionListener.goToUsergFollower(foreignID);
    }

    @Override
    public void goToFravouriteView(int fragmentId) {
        mHomeActionListener.goToFragment(fragmentId);
    }

    /**
     * on click done update user info
     */
    private void postUpdateUserProfile(Bitmap userAvatarBmp, int userImageType) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;
        options.inPurgeable = true;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        userAvatarBmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        byte byteImage_photo[] = baos.toByteArray();
        String encodedImage = Base64.encodeToString(byteImage_photo, Base64.DEFAULT);
        try {
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int _loginBravoViaType = BravoSharePrefs.getInstance(getActivity()).getIntValue(BravoConstant.PREF_KEY_SESSION_LOGIN_BRAVO_VIA_TYPE);
        SessionLogin _sessionLogin = BravoUtils.getSession(getActivity(), _loginBravoViaType);
        String userId = _sessionLogin.userID;
        String accessToken = _sessionLogin.accessToken;

        HashMap<String, String> subParams = new HashMap<String, String>();
        if (AdapterUserDetail.USER_AVATAR_ID == userImageType) {
            subParams.put("Profile_Img", encodedImage);
            subParams.put("Cover_Img", "");
        } else {
            subParams.put("Profile_Img", "");
            subParams.put("Cover_Img", encodedImage);
        }
        subParams.put("Profile_Img_Del", "1");
        subParams.put("Cover_Img_Del", "0");
        subParams.put("About_Me", "");
        subParams.put("UserId", userId);

        JSONObject jsonObject = new JSONObject(subParams);
        String subParamsStr = jsonObject.toString();

        AIOLog.d("encodedImage:" + encodedImage);
        String putUserUrl = BravoWebServiceConfig.URL_PUT_USER.replace("{User_ID}", userId).replace("{Access_Token}", accessToken);
        AIOLog.d("putUserUrl:" + putUserUrl);
        List<NameValuePair> params = ParameterFactory.createSubParams(subParamsStr);
        AsyncHttpPostImage postRegister = new AsyncHttpPostImage(getActivity(), new AsyncHttpResponseProcess(getActivity(), this) {
            @Override
            public void processIfResponseSuccess(String response) {
                AIOLog.d("reponse after uploading image:" + response);
                Gson gson = new GsonBuilder().serializeNulls().create();
                ObUserImagePost obUserImagePost = gson.fromJson(response.toString(), ObUserImagePost.class);
                if (obUserImagePost == null)
                    return;
                if (BravoConstant.STATUS_SUCCESS == obUserImagePost.status) {
                    mObGetUserInfo.data.Profile_Img_URL = obUserImagePost.data.Profile_Img_URL;
                    mObGetUserInfo.data.Cover_Img_URL = obUserImagePost.data.Cover_Img_URL;
                    mAdapterUserDataProfile.updateUserProfile(mObGetUserInfo, isMyData);
                }
            }

            @Override
            public void processIfResponseFail() {
                AIOLog.d("response error");
            }
        }, params, true);
        postRegister.execute(putUserUrl);

    }

    private void cropImageAfterPicking() {
        final ArrayList<CropOption> cropOptions = new ArrayList<CropOption>();

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");

        List<ResolveInfo> list = getActivity().getPackageManager().queryIntentActivities(intent, 0);

        int size = list.size();

        if (size == 0) {
            Toast.makeText(getActivity(), "Can not find image crop app", Toast.LENGTH_SHORT).show();

            return;
        } else {
            intent.setData(mCapturedImageURI);

            if (AdapterUserDetail.USER_AVATAR_ID == mUserImageType) {
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("outputX", 200);
                intent.putExtra("outputY", 200);
            } else {
                intent.putExtra("aspectX", 3);
                intent.putExtra("aspectY", 4);
                intent.putExtra("outputX", 200);
                intent.putExtra("outputY", 267);
            }
            intent.putExtra("scale", true);
            intent.putExtra("return-data", true);

            if (size == 1) {
                Intent i = new Intent(intent);
                ResolveInfo res = list.get(0);

                i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));

                startActivityForResult(i, CROP_FROM_CAMERA);
            } else {
                for (ResolveInfo res : list) {
                    final CropOption co = new CropOption();

                    co.title = getActivity().getPackageManager().getApplicationLabel(res.activityInfo.applicationInfo);
                    co.icon = getActivity().getPackageManager().getApplicationIcon(res.activityInfo.applicationInfo);
                    co.appIntent = new Intent(intent);

                    co.appIntent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));

                    cropOptions.add(co);
                }

                CropOptionAdapter adapter = new CropOptionAdapter(getActivity().getApplicationContext(), cropOptions);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Choose Crop App");
                builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        startActivityForResult(cropOptions.get(item).appIntent, CROP_FROM_CAMERA);
                    }
                });

                builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {

                        if (mCapturedImageURI != null) {
                            getActivity().getContentResolver().delete(mCapturedImageURI, null, null);
                            mCapturedImageURI = null;
                        }
                    }
                });

                AlertDialog alert = builder.create();

                alert.show();
            }
        }
    }

    public void setForeignID(String foreignID) {
        this.foreignID = foreignID;
    }

    public void updateAllInformation() {
        if (NetworkUtility.getInstance(getActivity()).isNetworkAvailable()) {
            getUserInfo(foreignID);
            requestGetBlockingCheck();
            requestGetFollowingCheck();
            requestGetUserTimeLine(foreignID);
        }
    }

    public ObGetUserInfo getUserInfo() {
        return mObGetUserInfo;
    }
}
