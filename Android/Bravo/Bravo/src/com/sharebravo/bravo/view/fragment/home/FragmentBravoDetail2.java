package com.sharebravo.bravo.view.fragment.home;

import java.io.File;
import java.util.Calendar;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sharebravo.bravo.R;
import com.sharebravo.bravo.control.activity.HomeActivity;
import com.sharebravo.bravo.control.request.BravoRequestManager;
import com.sharebravo.bravo.control.request.IRequestListener;
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
import com.sharebravo.bravo.model.response.Spot;
import com.sharebravo.bravo.sdk.log.AIOLog;
import com.sharebravo.bravo.sdk.util.network.ImageLoader;
import com.sharebravo.bravo.sdk.util.network.NetworkUtility;
import com.sharebravo.bravo.utils.BravoConstant;
import com.sharebravo.bravo.utils.BravoSharePrefs;
import com.sharebravo.bravo.utils.BravoUtils;
import com.sharebravo.bravo.utils.BravoWebServiceConfig;
import com.sharebravo.bravo.utils.DialogUtility;
import com.sharebravo.bravo.utils.IDialogListener;
import com.sharebravo.bravo.utils.StringUtility;
import com.sharebravo.bravo.view.fragment.FragmentBasic;
import com.sharebravo.bravo.view.fragment.maps.FragmentMapCover;
import com.sharebravo.bravo.view.fragment.maps.FragmentMapView;

public class FragmentBravoDetail2 extends FragmentBasic {
    // =================================Constant Define================
    private static final int    REQUEST_CODE_CAMERA  = 8001;
    private static final int    REQUEST_CODE_GALLERY = 8002;
    private static final int    CROP_FROM_CAMERA     = 8003;
    // =================================Class Define===================
    // =================================Variable Define================
    private Uri                 mCapturedImageURI    = null;
    // ================================Control Define==================
    private Button              btnBack;
    private ObBravo             mBravoObj;
    private SessionLogin        mSessionLogin        = null;
    private int                 mLoginBravoViaType   = BravoConstant.NO_LOGIN_SNS;
    private Spot                mSpot;
    private LinearLayout        mLayoutPoorConnection;
    private ImageView           imagePost;
    private TextView            spotName;
    private ImageView           userAvatar;
    private TextView            txtUserName;
    private Button              btnCallSpot;
    private Button              btnViewMap;
    private Button              btnFollow;
    private ImageView           followIcon;
    private boolean             isFollowing          = false;
    private EditText            textboxComment;
    private Button              btnSubmitComment;
    private TextView            btnLeft;
    private TextView            txtLikedNumber;
    private TextView            txtCommentNumber;
    private TextView            btnRight;
    private boolean             isLiked;
    private TextView            btnMiddle;
    private boolean             isSave;
    private TextView            btnReport;
    private FragmentMapCover    mapFragment;
    private Button              btnLiked;

    private ImageView           iconLiked;
    private TextView            txtNumberLiked;
    private Button              btnSaved;
    private ImageView           iconSaved;
    private TextView            txtNumberSaved;
    private FrameLayout         layoutMapview        = null;
    private FragmentTransaction fragmentTransaction;
    private ImageView           btnChooseImage;
    private LinearLayout        layoutLiked;
    private LinearLayout        layoutSaved;
    private LinearLayout        layoutReport;

    private ObGetComments       mObGetComments       = null;
    private ImageLoader         mImageLoader         = null;
    private int                 lastTopValueAssigned = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = (ViewGroup) inflater.inflate(R.layout.page_recent_post_detail_tab_2, container);
        mImageLoader = new ImageLoader(getActivity());
        mHomeActionListener = (HomeActivity) getActivity();
        mLoginBravoViaType = BravoSharePrefs.getInstance(getActivity()).getIntValue(BravoConstant.PREF_KEY_SESSION_LOGIN_BRAVO_VIA_TYPE);
        mSessionLogin = BravoUtils.getSession(getActivity(), mLoginBravoViaType);

        initializeView(root);
        return root;
    }

    private void initializeView(View root) {
        mLayoutPoorConnection = (LinearLayout) root.findViewById(R.id.layout_poor_connection);
        if (NetworkUtility.getInstance(getActivity()).isNetworkAvailable()) {
            mLayoutPoorConnection.setVisibility(View.GONE);
        } else {
            mLayoutPoorConnection.setVisibility(View.VISIBLE);
        }
        btnBack = (Button) root.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mHomeActionListener.goToBack();
            }
        });

        initializeViewForDetail(root);
        initializeViewForComment(root);
    }

    private void initializeViewForComment(View root) {

    }

    private void initializeViewForDetail(View root) {
        initializeViewForImage_Map(root);
        initializeViewForCallSpot_ViewMap(root);
        initializeViewForUserInfo(root);
        initializeViewForFollowInfo(root);
        initializeViewForSave_Like(root);
        initializeViewForLikedInfo(root);
        initializeViewForSavedInfo(root);
        initializeViewForDetailFooter(root);
    }

    private void initializeViewForDetailFooter(View root) {
        LinearLayout layoutPostDetailFooter = (LinearLayout) root.findViewById(R.id.layout_post_spot_detail_footer);
        textboxComment = (EditText) layoutPostDetailFooter.findViewById(R.id.textbox_comment);
        btnSubmitComment = (Button) layoutPostDetailFooter.findViewById(R.id.btn_submit_comment);
        btnReport = (TextView) layoutPostDetailFooter.findViewById(R.id.btn_report);
        layoutReport = (LinearLayout) layoutPostDetailFooter.findViewById(R.id.layout_btn_report);
        btnSubmitComment.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String commentText = textboxComment.getEditableText().toString();
                if (!commentText.equals(""))
                    goToSubmitComment(commentText);
            }
        });
        btnReport.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                goToReport();
            }
        });
    }

    private void initializeViewForSavedInfo(View root) {
        LinearLayout layoutSaved_LikedInfo = (LinearLayout) root.findViewById(R.id.layout_saved);
        btnSaved = (Button) layoutSaved_LikedInfo.findViewById(R.id.btn_saved);
        iconSaved = (ImageView) layoutSaved_LikedInfo.findViewById(R.id.icon_saved);
        txtNumberSaved = (TextView) layoutSaved_LikedInfo.findViewById(R.id.total_saved);
        layoutSaved = (LinearLayout) layoutSaved_LikedInfo.findViewById(R.id.layout_saved);

        btnSaved.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                goToSaved();
            }
        });
    }

    private void initializeViewForLikedInfo(View root) {
        LinearLayout layoutLikedInfo = (LinearLayout) root.findViewById(R.id.layout_liked);
        btnLiked = (Button) layoutLikedInfo.findViewById(R.id.btn_liked);
        iconLiked = (ImageView) layoutLikedInfo.findViewById(R.id.icon_liked);
        txtNumberLiked = (TextView) layoutLikedInfo.findViewById(R.id.total_liked);
        layoutLiked = (LinearLayout) layoutLikedInfo.findViewById(R.id.layout_liked);

        btnLiked.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                goToLiked();
            }
        });
    }

    private void initializeViewForFollowInfo(View root) {
        LinearLayout layoutFollowInfo = (LinearLayout) root.findViewById(R.id.layout_follow_info);
        followIcon = (ImageView) layoutFollowInfo.findViewById(R.id.icon_follow);
        btnFollow = (Button) layoutFollowInfo.findViewById(R.id.btn_follow);

        btnFollow.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                goToFollow(!isFollowing);
            }
        });
    }

    private void initializeViewForCallSpot_ViewMap(View root) {
        LinearLayout layoutUserInfo = (LinearLayout) root.findViewById(R.id.layout_callspot_viewmap);
        btnCallSpot = (Button) layoutUserInfo.findViewById(R.id.btn_call_spot);
        btnViewMap = (Button) layoutUserInfo.findViewById(R.id.btn_view_map);

        btnCallSpot.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                DialogUtility.showDialogCallSpot(getActivity(), mBravoObj);
            }
        });

        btnViewMap.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                goToFragment(HomeActivity.FRAGMENT_MAP_VIEW_ID);
            }
        });
    }

    private void initializeViewForUserInfo(View root) {
        LinearLayout layoutUserInfo = (LinearLayout) root.findViewById(R.id.layout_user_info);
        userAvatar = (ImageView) layoutUserInfo.findViewById(R.id.img_avatar);
        txtUserName = (TextView) layoutUserInfo.findViewById(R.id.txt_user_name);

        userAvatar.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mBravoObj == null)
                    return;
                AIOLog.d("mBravoObj.User_ID:" + mBravoObj.User_ID);
                goToUserDataTab(mBravoObj.User_ID);
            }
        });
    }

    private void initializeViewForSave_Like(View root) {
        LinearLayout layoutSaveLike = (LinearLayout) root.findViewById(R.id.layout_save_like);
        btnLeft = (TextView) layoutSaveLike.findViewById(R.id.btn_left);
        btnRight = (TextView) layoutSaveLike.findViewById(R.id.btn_right);
        btnMiddle = (TextView) layoutSaveLike.findViewById(R.id.btn_middle);
        btnLeft.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mBravoObj.User_ID.equals(mSessionLogin.userID)) {
                    goToSave(!isSave);
                } else {
                    goToLike(!isLiked);
                }
            }
        });
        btnRight.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                DialogUtility.showDialogShare(getActivity(), mHomeActionListener, mBravoObj);
            }
        });

        btnMiddle.setBackgroundResource(R.drawable.btn_like2);
        btnMiddle.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                goToSave(!isSave);
            }
        });
    }

    private void initializeViewForImage_Map(View root) {
        FrameLayout frameLayoutMap_View = (FrameLayout) root.findViewById(R.id.layout_map_image);

        imagePost = (ImageView) frameLayoutMap_View.findViewById(R.id.image_post_detail);

        spotName = (TextView) frameLayoutMap_View.findViewById(R.id.content_post_detail);

        txtLikedNumber = (TextView) frameLayoutMap_View.findViewById(R.id.txtView_like_number);

        txtCommentNumber = (TextView) frameLayoutMap_View.findViewById(R.id.txtView_comment_number);

        layoutMapview = (FrameLayout) frameLayoutMap_View.findViewById(R.id.layout_map_img);

        btnChooseImage = (ImageView) frameLayoutMap_View.findViewById(R.id.img_picture_choose);

        fragmentTransaction = getChildFragmentManager().beginTransaction();

        mapFragment = (FragmentMapCover) getChildFragmentManager().findFragmentById(R.id.img_map);

        if (mapFragment == null) {

            mapFragment = new FragmentMapCover();

            fragmentTransaction.replace(R.id.img_map, mapFragment).commit();

        }

        spotName.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                goToSpotDetail();
            }
        });
        btnChooseImage.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                choosePicture();
            }
        });
        imagePost.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                goToCoverImage();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void setBravoOb(ObBravo obj) {
        this.mBravoObj = obj;
        mSpot = new Spot();
        mSpot.Spot_ID = mBravoObj.Spot_ID;
        mSpot.Spot_FID = mBravoObj.Spot_FID;
        mSpot.Spot_Name = mBravoObj.Spot_Name;
        mSpot.Spot_Source = mBravoObj.Spot_Source;
        mSpot.Spot_Latitude = mBravoObj.Spot_Latitude;
        mSpot.Spot_Longitude = mBravoObj.Spot_Longitude;
        mSpot.Spot_Genre = mBravoObj.Spot_Genre;
        mSpot.Spot_Phone = mBravoObj.Spot_Phone;
        mSpot.Spot_Price = mBravoObj.Spot_Price;
        mSpot.Spot_Type = mBravoObj.Spot_Type;
        FragmentMapCover.mLat = mBravoObj.Spot_Latitude;
        FragmentMapCover.mLong = mBravoObj.Spot_Longitude;
    }

    private void requestGetComments() {
        BravoRequestManager.getInstance(getActivity()).requestToGetCommentsForSpotDetail(getActivity(), mBravoObj.Bravo_ID, new IRequestListener() {

            @Override
            public void onResponse(String response) {
                AIOLog.d("requestGetComments:" + response);
                Gson gson = new GsonBuilder().serializeNulls().create();
                ObGetComments mObGetComments = gson.fromJson(response.toString(), ObGetComments.class);
                AIOLog.d("mObGetComments:" + mObGetComments);
                if (mObGetComments == null)
                    return;
                else {
                    AIOLog.d("size of recent post list: " + mObGetComments.data.size());
                    // adapterRecentPostDetail.updateAllCommentList(mObGetComments);
                }
            }

            @Override
            public void onErrorResponse(String errorMessage) {

            }
        });
    }

    private void requestGetBravo() {
        if (mBravoObj == null)
            return;
        BravoRequestManager.getInstance(getActivity()).requestToGetBravoForSpotDetail(getActivity(), mBravoObj.Bravo_ID, new IRequestListener() {

            @Override
            public void onResponse(String response) {
                Gson gson = new GsonBuilder().serializeNulls().create();
                ObGetBravo obGetBravo = gson.fromJson(response.toString(), ObGetBravo.class);
                AIOLog.d("obGetAllBravoRecentPosts:" + obGetBravo);
                if (obGetBravo == null)
                    return;
                else {
                    String Last_Pic = mBravoObj.Last_Pic;
                    mBravoObj = obGetBravo.data;
                    mBravoObj.Last_Pic = Last_Pic;
                    setBravoOb(mBravoObj);
                    requestGetLiked();
                }
            }

            @Override
            public void onErrorResponse(String errorMessage) {
                AIOLog.d("response error");
            }
        }, FragmentBravoDetail2.this);
    }

    private void requestGetFollowingCheck() {
        BravoRequestManager.getInstance(getActivity()).requestGetFollowingCheck(mBravoObj.User_ID, new IRequestListener() {

            @Override
            public void onResponse(String response) {
                AIOLog.d("requestFollowingCheck:" + response);
                Gson gson = new GsonBuilder().serializeNulls().create();
                ObGetFollowingCheck obGetFollowCheck;
                obGetFollowCheck = gson.fromJson(response.toString(), ObGetFollowingCheck.class);
                if (obGetFollowCheck == null)
                    return;
                else {
                    isFollowing = obGetFollowCheck.valid == 1 ? true : false;
                    followIcon.setImageResource(isFollowing ? R.drawable.following_icon : R.drawable.follow_icon);
                    btnFollow.setText(isFollowing ? getString(R.string.following) : getString(R.string.follow));
                }
            }

            @Override
            public void onErrorResponse(String errorMessage) {
                AIOLog.d("response error");
            }
        });
    }

    private void requestDeleteFollow(ObBravo obBravo) {
        if (mBravoObj == null)
            return;
        BravoRequestManager.getInstance(getActivity()).requestDeleteFollow(mBravoObj.User_ID, new IRequestListener() {

            @Override
            public void onResponse(String response) {

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
                    isFollowing = false;
                    followIcon.setImageResource(isFollowing ? R.drawable.following_icon : R.drawable.follow_icon);
                    btnFollow.setText(isFollowing ? getString(R.string.following) : getString(R.string.follow));
                } else {
                    obDeleteFollowing = gson.fromJson(response.toString(), ObDeleteFollowing.class);
                    showToast(obDeleteFollowing.error);
                }
            }

            @Override
            public void onErrorResponse(String errorMessage) {
                AIOLog.d("response error");
            }
        }, FragmentBravoDetail2.this);
    }

    private void requestToPutFollow(ObBravo obBravo) {
        BravoRequestManager.getInstance(getActivity()).requestToPutFollow(obBravo.User_ID, new IRequestListener() {

            @Override
            public void onResponse(String response) {

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
                    updateFollowing(true);
                    DialogUtility.showDialogFollowingOK(getActivity(), mBravoObj.Full_Name);
                } else {
                    obPutFollowing = gson.fromJson(response.toString(), ObPutFollowing.class);
                    showToast(obPutFollowing.error);
                }
            }

            @Override
            public void onErrorResponse(String errorMessage) {
                AIOLog.e("errorMessage");
            }
        }, FragmentBravoDetail2.this);
    }
    private void updateFollowing(boolean b) {
        // TODO Auto-generated method stub
        
    }
    private void requestGetMyListItem() {
        BravoRequestManager.getInstance(getActivity()).requestGetMyListItem(mBravoObj.Bravo_ID, new IRequestListener() {

            @Override
            public void onResponse(String response) {
                AIOLog.d("requestFollowingCheck:" + response);
                Gson gson = new GsonBuilder().serializeNulls().create();
                ObGetMylistItem obGetMylistItem;
                obGetMylistItem = gson.fromJson(response.toString(), ObGetMylistItem.class);
                // AIOLog.d("obGetAllBravoRecentPosts:" + mObGetComments);
                if (obGetMylistItem == null)
                    return;
                else {
                    // adapterRecentPostDetail.updateSave(obGetMylistItem.valid == 1 ? true : false);
                }
            }

            @Override
            public void onErrorResponse(String errorMessage) {
                AIOLog.d("response error");
            }
        });
    }

    private void requestGetLikeItem() {
        BravoRequestManager.getInstance(getActivity()).requestGetLikeItem(mBravoObj.Bravo_ID, new IRequestListener() {

            @Override
            public void onResponse(String response) {
                AIOLog.d("requestLikingCheck:" + response);
                Gson gson = new GsonBuilder().serializeNulls().create();
                ObGetLikeItem mObGetLikeItem;
                mObGetLikeItem = gson.fromJson(response.toString(), ObGetLikeItem.class);
                if (mObGetLikeItem == null)
                    return;
                else {
                    // adapterRecentPostDetail.updateLike(mObGetLikeItem.valid == 1 ? true : false);
                }
            }

            @Override
            public void onErrorResponse(String errorMessage) {
                AIOLog.d("response error");
            }
        });
    }

    private void requestGetLiked() {
        BravoRequestManager.getInstance(getActivity()).requestGetLiked(mBravoObj.Spot_ID, new IRequestListener() {

            @Override
            public void onResponse(String response) {
                AIOLog.d("ObGetSpot:" + response);
                Gson gson = new GsonBuilder().serializeNulls().create();
                ObGetSpot mObGetSpot;
                mObGetSpot = gson.fromJson(response.toString(), ObGetSpot.class);
                if (mObGetSpot == null)
                    return;
                else {
                    AIOLog.e("Spot.data" + mObGetSpot.data);
                    // adapterRecentPostDetail.updateLikedandSaved(mObGetSpot.data);
                }
            }

            @Override
            public void onErrorResponse(String errorMessage) {
                AIOLog.d("response error");
            }
        });
    }

    private void requestDeleteMyListItem(ObBravo obBravo) {
        BravoRequestManager.getInstance(getActivity()).requestDeleteMyListItem(mBravoObj.Bravo_ID, new IRequestListener() {

            @Override
            public void onResponse(String response) {
                AIOLog.d("response requestDeleteMyListItem :===>" + response);
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
                    // adapterRecentPostDetail.updateSave(false);
                } else {
                    obDeleteMylist = gson.fromJson(response.toString(), ObDeleteMylist.class);
                    showToast(obDeleteMylist.error);
                }
            }

            @Override
            public void onErrorResponse(String errorMessage) {
                AIOLog.d("response error");
            }
        }, FragmentBravoDetail2.this);
    }

    private void requestToPutMyListItem(ObBravo obBravo) {
        BravoRequestManager.getInstance(getActivity()).requestToPutMyListItem(obBravo.Bravo_ID, new IRequestListener() {

            @Override
            public void onResponse(String response) {

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
                    // adapterRecentPostDetail.updateSave(true);
                } else {
                    obPutMyList = gson.fromJson(response.toString(), ObPutMyList.class);
                    showToast(obPutMyList.error);
                }
            }

            @Override
            public void onErrorResponse(String errorMessage) {
                AIOLog.d("response error");
            }
        }, FragmentBravoDetail2.this);
    }

    private void requestDeleteLike(ObBravo obBravo) {
        BravoRequestManager.getInstance(getActivity()).requestDeleteLike(obBravo.Bravo_ID, new IRequestListener() {

            @Override
            public void onResponse(String response) {
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
                    // adapterRecentPostDetail.updateLike(false);
                    requestGetLiked();
                } else {
                    mObDeleteLike = gson.fromJson(response.toString(), ObDeleteLike.class);
                    showToast(mObDeleteLike.error);
                }
            }

            @Override
            public void onErrorResponse(String errorMessage) {
                AIOLog.d("response error");
            }
        }, FragmentBravoDetail2.this);
    }

    private void requestToPutLike(ObBravo obBravo) {
        BravoRequestManager.getInstance(getActivity()).requestToPutLike(obBravo.Bravo_ID, new IRequestListener() {

            @Override
            public void onResponse(String response) {
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
                    // adapterRecentPostDetail.updateLike(true);
                    requestGetLiked();
                } else {
                    mObPutLike = gson.fromJson(response.toString(), ObPutLike.class);
                    showToast(mObPutLike.error);
                }
            }

            @Override
            public void onErrorResponse(String errorMessage) {
                AIOLog.d("response error");
            }
        }, FragmentBravoDetail2.this);
    }

    private void requestToPutReport(ObBravo obBravo) {
        BravoRequestManager.getInstance(getActivity()).requestToPutReport(obBravo.User_ID, new IRequestListener() {

            @Override
            public void onResponse(String response) {

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
                    DialogUtility.showDialogReportOk(getActivity());
                } else {
                    obPutReport = gson.fromJson(response.toString(), ObPutReport.class);
                    showToast(obPutReport.error);
                }
            }

            @Override
            public void onErrorResponse(String errorMessage) {
                AIOLog.d("response error");
            }
        }, FragmentBravoDetail2.this);
    }

    private void requestToPostComment(String commentText) {
        if (mBravoObj == null)
            return;
        BravoRequestManager.getInstance(getActivity()).requestToPostComment(getActivity(), commentText, mBravoObj.Bravo_ID, new IRequestListener() {
            @Override
            public void onResponse(String response) {
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
                    if (obPostComment == null)
                        return;
                    showToast(obPostComment.error);
                }
            }

            @Override
            public void onErrorResponse(String errorMessage) {
                AIOLog.d("response error");
            }
        }, FragmentBravoDetail2.this);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && !isBackStatus() && NetworkUtility.getInstance(getActivity()).isNetworkAvailable()) {
            updateMapView();
            initializeData();
            requestGetBravo();
            requestGetFollowingCheck();
            requestGetMyListItem();
            requestGetComments();
            requestGetLikeItem();
        }
    }

    private void updateMapView() {
        if (mapFragment != null)
            mapFragment.changeLocation(FragmentMapCover.mLat, FragmentMapCover.mLong);
    }

    private void initializeData() {
        if (mBravoObj == null)
            return;
        String imgSpotUrl = null;
        if (mBravoObj.Bravo_Pics.size() > 0)
            imgSpotUrl = mBravoObj.Bravo_Pics.get(0);

        AIOLog.d("mBravoObj.Last_Pic: " + mBravoObj.Last_Pic);
        if (StringUtility.isEmpty(imgSpotUrl)) {
            layoutMapview.setVisibility(View.VISIBLE);
        } else {
            layoutMapview.setVisibility(View.GONE);
            btnChooseImage.setVisibility(View.GONE);
            mImageLoader.DisplayImage(imgSpotUrl, R.drawable.user_picture_default, imagePost, false);
        }
        if (mSpot != null) {
            txtLikedNumber.setText("" + mSpot.Total_Liked_Users);
            txtNumberLiked.setText("" + mSpot.Total_Liked_Users);
            txtNumberSaved.setText("" + mSpot.Total_Saved_Users);
        }
        else {
            txtLikedNumber.setText("0");
            txtNumberLiked.setText("0");
            txtNumberSaved.setText("0");
        }
        txtCommentNumber.setText("" + mBravoObj.Total_Comments);
        spotName.setText(mBravoObj.Spot_Name);
        String avatarUrl = mBravoObj.Profile_Img_URL;
        AIOLog.d("obGetBravo.Profile_Img_URL: " + mBravoObj.Profile_Img_URL);
        if (StringUtility.isEmpty(avatarUrl)) {
            userAvatar.setBackgroundResource(R.drawable.user_picture_default);
        } else {
            mImageLoader.DisplayImage(avatarUrl, R.drawable.user_picture_default, userAvatar, true);
        }
        txtUserName.setText(mBravoObj.Full_Name);
        followIcon.setImageResource(isFollowing ? R.drawable.following_icon : R.drawable.follow_icon);
        btnFollow.setText(isFollowing ? getString(R.string.following) : getString(R.string.follow));
        btnMiddle.setBackgroundResource(R.drawable.btn_like2);
        if (mBravoObj.User_ID.equals(mSessionLogin.userID))
        {
            followIcon.setVisibility(View.GONE);
            btnFollow.setVisibility(View.GONE);
            layoutLiked.setVisibility(View.VISIBLE);
            layoutSaved.setVisibility(View.VISIBLE);
            if (StringUtility.isEmpty(imgSpotUrl))
                btnChooseImage.setVisibility(View.VISIBLE);
            btnLeft.setBackgroundResource(R.drawable.btn_save);
            btnLeft.setCompoundDrawablesWithIntrinsicBounds(0, isSave ? R.drawable.save_bravo_on : R.drawable.save_bravo_off, 0, 0);
            btnLeft.setText(isSave ? getString(R.string.bravo_info_saved) : getString(R.string.bravo_info_save));
            btnMiddle.setVisibility(View.GONE);
            btnRight.setBackgroundResource(R.drawable.btn_share);
            layoutReport.setVisibility(View.GONE);
        } else {
            followIcon.setVisibility(View.VISIBLE);
            btnFollow.setVisibility(View.VISIBLE);
            layoutLiked.setVisibility(View.GONE);
            layoutSaved.setVisibility(View.GONE);
            btnChooseImage.setVisibility(View.GONE);
            btnLeft.setBackgroundResource(R.drawable.btn_save2);
            btnLeft.setCompoundDrawablesWithIntrinsicBounds(0, isLiked ? R.drawable.icon_like : R.drawable.icon_like_off, 0, 0);
            btnLeft.setText(isLiked ? getString(R.string.bravo_info_liked) : getString(R.string.bravo_info_like));
            btnMiddle.setVisibility(View.VISIBLE);
            btnRight.setBackgroundResource(R.drawable.btn_share2);
            layoutReport.setVisibility(View.VISIBLE);
        }
        btnMiddle.setCompoundDrawablesWithIntrinsicBounds(0, isSave ? R.drawable.save_bravo_on : R.drawable.save_bravo_off, 0, 0);
        btnMiddle.setText(isSave ? getString(R.string.bravo_info_saved) : getString(R.string.bravo_info_save));
    }

    public void goToFragment(int fragmentID) {
        if (fragmentID == HomeActivity.FRAGMENT_MAP_VIEW_ID) {
            mHomeActionListener.goToMapView(mBravoObj.Spot_Latitude, mBravoObj.Spot_Longitude,
                    FragmentMapView.MAKER_BY_LOCATION_SPOT);
            return;
        }
        mHomeActionListener.goToFragment(fragmentID);
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
                        cropImageFromUri(data.getData());
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
                if (file.exists()) {
                    Uri fileUri = Uri.fromFile(file);
                    cropImageFromUri(fileUri);
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
                if (uri == null)
                    return;
                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                if (getActivity().getContentResolver() == null)
                    return;
                Cursor cursor = getActivity().getContentResolver().query(uri, filePathColumn, null, null, null);
                if (cursor == null)
                    return;
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String imagePath = cursor.getString(columnIndex);
                cursor.close();

                File file = new File(imagePath);
                if (file.exists()) {
                    Uri fileUri = Uri.fromFile(file);
                    cropImageFromUri(fileUri);
                } else {

                    AIOLog.d("file don't exist !");
                }
            }
            break;
        case CROP_FROM_CAMERA:
            if (resultCode == getActivity().RESULT_OK) {
                Bundle extras = data.getExtras();
                if (extras != null) {
                    Bitmap photo = extras.getParcelable("data");
                    updateBravoImage(photo);
                }
            }
            break;
        default:
            return;
        }
    }

    private void updateBravoImage(Bitmap photo) {
        BravoRequestManager.getInstance(getActivity()).requestToUpdateImageForBravo(photo, mBravoObj.Bravo_ID, FragmentBravoDetail2.this,
                new IRequestListener() {

                    @Override
                    public void onResponse(String response) {
                        requestGetBravo();
                    }

                    @Override
                    public void onErrorResponse(String errorMessage) {
                        requestGetBravo();
                    }
                });
    }

    private void cropImageFromUri(Uri uri) {
        AIOLog.d("uri:" + uri);
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");
        List<ResolveInfo> list = getActivity().getPackageManager().queryIntentActivities(intent, 0);

        int size = list.size();
        AIOLog.d("size:" + size);
        if (size == 0) {
            showToast("Can not crop image");
            return;
        } else {
            intent.setData(uri);
            intent.putExtra("outputX", 1500);
            intent.putExtra("outputY", 1500);
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("scale", true);
            intent.putExtra("return-data", true);
            if (size >= 1) {
                Intent i = new Intent(intent);
                ResolveInfo res = list.get(0);
                i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
                startActivityForResult(i, CROP_FROM_CAMERA);
            }
        }
    }

    public void goToReport() {
        DialogUtility.showDialogReport(getActivity(), new IDialogListener() {

            @Override
            public void onClickPositve() {
                requestToPutReport(mBravoObj);
            }

            @Override
            public void onClickNegative() {

            }

            @Override
            public void onClickCancel() {

            }
        });
    }

    public void goToFollow(boolean isFollow) {
        if (isFollow)
            requestToPutFollow(mBravoObj);
        else
            DialogUtility.showDialogStopFollowing(getActivity(), new IDialogListener() {

                @Override
                public void onClickPositve() {
                    requestDeleteFollow(mBravoObj);
                }

                @Override
                public void onClickNegative() {

                }

                @Override
                public void onClickCancel() {

                }
            });
    }

    public void goToSubmitComment(String commentText) {
        requestToPostComment(commentText);
    }

    public void goToSave(boolean isSave) {
        if (isSave)
            requestToPutMyListItem(mBravoObj);
        else
            requestDeleteMyListItem(mBravoObj);
    }

    public void goToCoverImage() {
        mHomeActionListener.goToCoverImage(mBravoObj);
    }

    public void goToUserDataTab(String useId) {
        mHomeActionListener.goToUserData(useId);
    }

    private void choosePicture() {
        DialogUtility.showDialogChooseImage(getActivity(), new IDialogListener() {

            @Override
            public void onClickPositve() {
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
            }

            @Override
            public void onClickNegative() {
                // when user click gallery to get image
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, REQUEST_CODE_GALLERY);
            }

            @Override
            public void onClickCancel() {

            }
        });
    }

    public void goToLiked() {
        mHomeActionListener.goToLiked(mBravoObj.Spot_ID);
    }

    public void goToSaved() {
        mHomeActionListener.goToSaved(mBravoObj.Spot_ID);
    }

    public void goToLike(boolean isLike) {
        if (isLike)
            requestToPutLike(mBravoObj);
        else
            requestDeleteLike(mBravoObj);
    }

    public void goToSpotDetail() {
        mHomeActionListener.goToSpotDetail(mSpot);
    }

    public void updateInfo() {
        if (NetworkUtility.getInstance(getActivity()).isNetworkAvailable()) {
            // updateMapView();
            requestGetBravo();
            requestGetFollowingCheck();
            requestGetMyListItem();
            requestGetComments();
            requestGetLikeItem();
        }
    }
}
