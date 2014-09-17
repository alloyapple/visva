package com.sharebravo.bravo.view.fragment.userprofile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
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
import com.sharebravo.bravo.control.activity.HomeActivity;
import com.sharebravo.bravo.model.SessionLogin;
import com.sharebravo.bravo.model.response.ObDeleteFollowing;
import com.sharebravo.bravo.model.response.ObGetBlockingCheck;
import com.sharebravo.bravo.model.response.ObGetFollowingCheck;
import com.sharebravo.bravo.model.response.ObGetUserInfo;
import com.sharebravo.bravo.model.response.ObGetUserTimeline;
import com.sharebravo.bravo.model.response.ObPutBlocking;
import com.sharebravo.bravo.model.response.ObPutFollowing;
import com.sharebravo.bravo.model.response.ObUserImagePost;
import com.sharebravo.bravo.sdk.log.AIOLog;
import com.sharebravo.bravo.sdk.util.network.AsyncHttpDelete;
import com.sharebravo.bravo.sdk.util.network.AsyncHttpGet;
import com.sharebravo.bravo.sdk.util.network.AsyncHttpPostImage;
import com.sharebravo.bravo.sdk.util.network.AsyncHttpPut;
import com.sharebravo.bravo.sdk.util.network.AsyncHttpResponseProcess;
import com.sharebravo.bravo.sdk.util.network.ParameterFactory;
import com.sharebravo.bravo.utils.BravoConstant;
import com.sharebravo.bravo.utils.BravoSharePrefs;
import com.sharebravo.bravo.utils.BravoUtils;
import com.sharebravo.bravo.utils.BravoWebServiceConfig;
import com.sharebravo.bravo.utils.StringUtility;
import com.sharebravo.bravo.view.adapter.AdapterUserDataProfile;
import com.sharebravo.bravo.view.adapter.UserPostProfileListener;
import com.sharebravo.bravo.view.fragment.FragmentBasic;
import com.sharebravo.bravo.view.fragment.home.FragmentMapView;
import com.sharebravo.bravo.view.lib.imageheader.PullAndLoadListView;

public class FragmentUserDataTab extends FragmentBasic implements UserPostProfileListener, LocationListener {
    private static final int       REQUEST_CODE_CAMERA      = 2001;
    private static final int       REQUEST_CODE_GALLERY     = 2002;

    private Uri                    mCapturedImageURI        = null;
    private Button                 mBtnSettings;
    private IShowPageSettings      iShowPageSettings;
    private PullAndLoadListView    mListViewUserPostProfile = null;
    private ObGetUserInfo          mObGetUserInfo;
    private AdapterUserDataProfile mAdapterUserDataProfile  = null;
    private Button                 mBtnBack;
    private boolean                isMyData                 = false;
    private static int             mUserImageType;

    private OnItemClickListener    onItemClick              = new OnItemClickListener() {

                                                                @Override
                                                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                                                }
                                                            };
    private SessionLogin           mSessionLogin            = null;
    private int                    mLoginBravoViaType       = BravoConstant.NO_LOGIN_SNS;
    private String                 foreignID                = "";
    Location                       location                 = null;
    LocationManager                locationManager          = null;
    double                         mLat, mLong;
    String                         provider;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = (ViewGroup) inflater.inflate(R.layout.page_fragment_user_profile, null);
        mLoginBravoViaType = BravoSharePrefs.getInstance(getActivity()).getIntValue(BravoConstant.PREF_KEY_SESSION_LOGIN_BRAVO_VIA_TYPE);
        mSessionLogin = BravoUtils.getSession(getActivity(), mLoginBravoViaType);
        mHomeActionListener = (HomeActivity) getActivity();
        initializeView(root);
        locationManager = (LocationManager) getActivity().getSystemService(Activity.LOCATION_SERVICE);

        // Creating a criteria object to retrieve provider
        Criteria criteria = new Criteria();

        // Getting the name of the best provider
        provider = locationManager.getBestProvider(criteria, true);

        // Getting Current Location
        location = locationManager.getLastKnownLocation(provider);

        if (location != null) {
            onLocationChanged(location);
        }

        locationManager.requestLocationUpdates(provider, 20000, 0, this);
        return root;
    }

    private void initializeView(View root) {
        mBtnSettings = (Button) root.findViewById(R.id.btn_settings);
        mListViewUserPostProfile = (PullAndLoadListView) root.findViewById(R.id.listview_user_post_profile);
        mAdapterUserDataProfile = new AdapterUserDataProfile(getActivity());
        mAdapterUserDataProfile.setListener(this);
        mListViewUserPostProfile.setFooterDividersEnabled(false);
        mListViewUserPostProfile.setAdapter(mAdapterUserDataProfile);
        mListViewUserPostProfile.setOnItemClickListener(onItemClick);
        mListViewUserPostProfile.onRefreshComplete();
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
            location = locationManager.getLastKnownLocation(provider);

            if (location != null) {
                onLocationChanged(location);
            }

            locationManager.requestLocationUpdates(provider, 20000, 0, this);
        }
        mListViewUserPostProfile.setVisibility(View.GONE);
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
        if (StringUtility.isEmpty(foreignUserId)) {
            mBtnBack.setVisibility(View.GONE);
            mBtnSettings.setVisibility(View.VISIBLE);
            isMyData = true;
            checkingUserId = userId;
            foreignID = userId;
        } else {
            isMyData = false;
            mBtnBack.setVisibility(View.VISIBLE);
            mBtnSettings.setVisibility(View.GONE);
            this.foreignID = foreignUserId;
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
                        mListViewUserPostProfile.resetHeader();
                        mListViewUserPostProfile.setSelection(1);
                        requestGetUserTimeLine(foreignID);
                        requestGetBlockingCheck();
                        requestGetFollowingCheck();
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
        // subParams.put("Location", String.valueOf(mLat)+","+String.valueOf(mLong));
        JSONObject subParamsJson = new JSONObject(subParams);
        String subParamsJsonStr = subParamsJson.toString();
        String url = BravoWebServiceConfig.URL_GET_USER_TIMELINE.replace("{User_ID}", checkingUserId);
        List<NameValuePair> params = ParameterFactory.createSubParamsGetTimeLine(userId, accessToken, subParamsJsonStr);
        AsyncHttpGet getTimeline = new AsyncHttpGet(getActivity(), new AsyncHttpResponseProcess(getActivity(), this) {
            @Override
            public void processIfResponseSuccess(String response) {
                AIOLog.d("obGetUserTimeline:" + response);
                mListViewUserPostProfile.setVisibility(View.VISIBLE);
                Gson gson = new GsonBuilder().serializeNulls().create();
                ObGetUserTimeline obGetUserTimeline;
                obGetUserTimeline = gson.fromJson(response.toString(), ObGetUserTimeline.class);
                AIOLog.d("obGetUserTimeline:" + obGetUserTimeline);
                if (obGetUserTimeline == null || obGetUserTimeline.data.size() == 0) {
                    mAdapterUserDataProfile.updateRecentPostList(null);
                    return;
                }
                else {
                    // ArrayList<ObBravo> obBravos = removeIncorrectBravoItems(obGetUserTimeline.data);
                    mAdapterUserDataProfile.updateRecentPostList(obGetUserTimeline.data);
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

    private void requestGetFollowingCheck() {
        String userId = mSessionLogin.userID;
        String accessToken = mSessionLogin.accessToken;
        String url = BravoWebServiceConfig.URL_GET_FOLLOWING_CHECK.replace("{User_ID}", userId).replace("{User_ID_Other}", foreignID);
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
        String userId = mSessionLogin.userID;
        String accessToken = mSessionLogin.accessToken;
        String url = BravoWebServiceConfig.URL_DELETE_FOLLOWING.replace("{User_ID}", userId).replace("{Access_Token}", accessToken)
                .replace("{User_ID_Other}", foreignID);
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
                    mAdapterUserDataProfile.updateFollow(false);
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
        AsyncHttpGet getBkockingCheckRequest = new AsyncHttpGet(getActivity(), new AsyncHttpResponseProcess(getActivity(), this) {
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
        String userId = mSessionLogin.userID;
        String accessToken = mSessionLogin.accessToken;
        String url = BravoWebServiceConfig.URL_DELETE_BLOCKING.replace("{User_ID}", userId).replace("{Access_Token}", accessToken)
                .replace("{User_ID_Other}", foreignID);
        AsyncHttpDelete deleteBlock = new AsyncHttpDelete(getActivity(), new AsyncHttpResponseProcess(getActivity(), this) {
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
                    mAdapterUserDataProfile.updateBlock(false);
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
        deleteBlock.execute(url);
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
        showDialogChooseImage(userImageType);
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
        TextView txtContent = (TextView) dialog_view.findViewById(R.id.txt_following_content);
        txtContent.setText(getActivity().getResources().getString(R.string.content_following).replace("%s%", mObGetUserInfo.data.Full_Name));
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

    private void showDialogChooseImage(int userImageType) {
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
                        postUpdateUserProfile(photo, mUserImageType);
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
                    Bitmap bmp;
                    bmp = BravoUtils.decodeSampledBitmapFromFile(imagePath, 100, 100, orientation);
                    postUpdateUserProfile(bmp, mUserImageType);
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
                    Bitmap bmp;
                    bmp = BravoUtils.decodeSampledBitmapFromFile(imagePath, 100, 100, orientation);
                    postUpdateUserProfile(bmp, mUserImageType);
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
    public void goToFragment(int fragmentID) {
        mHomeActionListener.goToFragment(fragmentID);
    }

    @Override
    public void onClickUserAvatar(String userId) {
    }

    @Override
    public void goToMapView() {
        mHomeActionListener.goToMapView(null, null, FragmentMapView.MAKER_BY_LOCATION_USER);
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
    public void onLocationChanged(Location location) {
        mLat = location.getLatitude();
        // Getting longitude
        mLong = location.getLongitude();
    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

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
        userAvatarBmp.compress(Bitmap.CompressFormat.PNG, 100, baos);

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
        if (AdapterUserDataProfile.USER_AVATAR_ID == userImageType) {
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

}
