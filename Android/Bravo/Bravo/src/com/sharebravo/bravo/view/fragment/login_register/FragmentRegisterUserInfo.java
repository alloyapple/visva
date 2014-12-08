package com.sharebravo.bravo.view.fragment.login_register;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sharebravo.bravo.R;
import com.sharebravo.bravo.control.activity.HomeActivity;
import com.sharebravo.bravo.model.SessionLogin;
import com.sharebravo.bravo.model.user.BravoUser;
import com.sharebravo.bravo.model.user.ObGetLoginedUser;
import com.sharebravo.bravo.sdk.log.AIOLog;
import com.sharebravo.bravo.sdk.util.network.AsyncHttpPost;
import com.sharebravo.bravo.sdk.util.network.AsyncHttpPostImage;
import com.sharebravo.bravo.sdk.util.network.AsyncHttpResponseProcess;
import com.sharebravo.bravo.sdk.util.network.ParameterFactory;
import com.sharebravo.bravo.utils.BravoConstant;
import com.sharebravo.bravo.utils.BravoSharePrefs;
import com.sharebravo.bravo.utils.BravoUtils;
import com.sharebravo.bravo.utils.BravoWebServiceConfig;
import com.sharebravo.bravo.utils.StringUtility;
import com.sharebravo.bravo.view.fragment.FragmentBasic;

public class FragmentRegisterUserInfo extends FragmentBasic {
    // ====================Constant Define=================
    private static final int REQUEST_CODE_CAMERA  = 1001;
    private static final int REQUEST_CODE_GALLERY = 1002;
    private static final int CROP_FROM_CAMERA     = 1003;
    // ====================Class Define====================
    private BravoUser        mBravoUser;
    // ====================Variable Define=================
    private EditText         mEditTextUserName;
    private Button           mBtnDoneRegister;
    private ImageView        mImgChoosePicture;
    private ImageView        mImgUserPicture;
    private Uri              mCapturedImageURI;
    private Bitmap           mUserAvatarBitmap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = (ViewGroup) inflater.inflate(R.layout.page_fragment_register_userinfo, container);
        mBtnDoneRegister = (Button) root.findViewById(R.id.btn_bravo_done_register);
        mBtnDoneRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!StringUtility.isEmpty(mEditTextUserName))
                    requestToPostBravoUserbySNS(mBravoUser);
                else
                    mEditTextUserName.setError(getActivity().getResources().getString(R.string.username_is_empty));
            }
        });
        mEditTextUserName = (EditText) root.findViewById(R.id.edittext_input_user_name);
        mImgUserPicture = (ImageView) root.findViewById(R.id.img_user_picture);
        mImgChoosePicture = (ImageView) root.findViewById(R.id.img_picture_choose);
        mImgChoosePicture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showDialogChooseImage();
            }
        });
        return root;
    }

    private void requestToPostBravoUserbySNS(final BravoUser bravoUser) {
        AIOLog.d("==================================");
        AIOLog.d("bravoUser.mAuthenMethod=>" + bravoUser.mAuthenMethod);
        AIOLog.d("bravoUser.mUserName=>" + bravoUser.mUserName);
        AIOLog.d("bravoUser.mUserEmail=>" + bravoUser.mUserEmail);
        AIOLog.d("bravoUser.mUserPassWord=>" + bravoUser.mUserPassWord);
        AIOLog.d("bravoUser.mTimeZone=>" + bravoUser.mTimeZone);
        AIOLog.d("bravoUser.mLocale=>" + bravoUser.mLocale);
        AIOLog.d("bravoUser.mForeign_Id=>" + bravoUser.mForeign_Id);
        AIOLog.d("==================================");
        AIOLog.d("==================================");
        HashMap<String, String> subParams = new HashMap<String, String>();
        subParams.put("Auth_Method", bravoUser.mAuthenMethod);
        subParams.put("Full_Name", bravoUser.mUserName);
        subParams.put("Email", bravoUser.mUserEmail);
        subParams.put("Password", bravoUser.mUserPassWord);
        subParams.put("Time_Zone", bravoUser.mTimeZone);
        subParams.put("Locale", bravoUser.mLocale);
        subParams.put("Foreign_ID", bravoUser.mForeign_Id);
        subParams.put("APNS_Token", "abcdef12345");
        JSONObject jsonObject = new JSONObject(subParams);
        String subParamsStr = jsonObject.toString();

        List<NameValuePair> params = ParameterFactory.createSubParams(subParamsStr);
        AsyncHttpPost postRegister = new AsyncHttpPost(getActivity(), new AsyncHttpResponseProcess(getActivity(), this) {
            @Override
            public void processIfResponseSuccess(String response) {
                AIOLog.d("reponse:" + response);
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
                ObGetLoginedUser obPostUserFailed;
                if (status == String.valueOf(BravoWebServiceConfig.STATUS_RESPONSE_DATA_SUCCESS)) {
                    /* save data to share preferences */
                    BravoUtils.saveResponseToSharePreferences(getActivity(), mBravoUser.mRegisterType, response);

                    if (mUserAvatarBitmap != null) {
                        requestToPostUserWithAvatarImage(bravoUser, mUserAvatarBitmap);
                    } else {
                        addSharePreKey(bravoUser.mAuthenMethod, bravoUser.mForeign_Id);
                        /* go to home screen */
                        Intent homeIntent = new Intent(getActivity(), HomeActivity.class);
                        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homeIntent);
                        getActivity().finish();
                    }
                } else {
                    obPostUserFailed = gson.fromJson(response.toString(), ObGetLoginedUser.class);
                    showToast(obPostUserFailed.error);
                }
            }

            @Override
            public void processIfResponseFail() {
                AIOLog.d("response error");
            }
        }, params, true);
        postRegister.execute(BravoWebServiceConfig.URL_POST_USER);

    }

    public void addSharePreKey(String snsType, String snsID) {
        if (BravoConstant.FACEBOOK.equals(snsType)) {
            BravoSharePrefs.getInstance(getActivity()).putBooleanValue(BravoConstant.PREF_KEY_FACEBOOK_LOGIN, true);
            BravoSharePrefs.getInstance(getActivity()).putStringValue(BravoConstant.PREF_KEY_FACEBOOK_ID_LOGINED, snsID);
        }
        else if (BravoConstant.FOURSQUARE.equals(snsType)) {
            BravoSharePrefs.getInstance(getActivity()).putBooleanValue(BravoConstant.PREF_KEY_FOURSQUARE_LOGIN, true);
            BravoSharePrefs.getInstance(getActivity()).putStringValue(BravoConstant.PREF_KEY_FOURSQUARE_ID_LOGINED, snsID);
        }
        else if (BravoConstant.TWITTER.equals(snsType)) {
            BravoSharePrefs.getInstance(getActivity()).putBooleanValue(BravoConstant.PREF_KEY_TWITTER_LOGIN, true);
            BravoSharePrefs.getInstance(getActivity()).putStringValue(BravoConstant.PREF_KEY_TWITTER_ID_LOGINED, snsID);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void updateUserInfo(BravoUser bravoUser) {
        mBravoUser = bravoUser;
        if (mBravoUser != null)
            mEditTextUserName.setText(mBravoUser.mUserName);
    }

    private void showDialogChooseImage() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) getActivity().getLayoutInflater();
        View dialog_view = inflater.inflate(R.layout.dialog_choose_picture, null);
        Button btnZoomAPicture = (Button) dialog_view.findViewById(R.id.btn_zoom_a_picture);
        btnZoomAPicture.setVisibility(View.GONE);
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
                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                Cursor cursor = getActivity().getContentResolver().query(uri, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String imagePath = cursor.getString(columnIndex);
                cursor.close();

                File file = new File(imagePath);
                Uri fileUri = null;
                if (file.exists()) {
                    fileUri = Uri.fromFile(file);
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
                    mUserAvatarBitmap = photo;
                    mImgUserPicture.setImageBitmap(photo);
                }
            }
            break;
        default:
            return;
        }
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
            intent.putExtra("outputX", 1000);
            intent.putExtra("outputY", 1000);
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

    private void requestToPostUserWithAvatarImage(BravoUser bravoUser, Bitmap userAvatarBmp) {
        if (userAvatarBmp == null) {
            /* go to home screen */
            Intent homeIntent = new Intent(getActivity(), HomeActivity.class);
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
            getActivity().finish();
        }
        BitmapFactory.Options options = new BitmapFactory.Options();

        options.inSampleSize = 1;
        options.inPurgeable = true;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        userAvatarBmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        // bitmap object
        byte byteImage_photo[] = baos.toByteArray();
        String encodedImage = Base64.encodeToString(byteImage_photo, Base64.DEFAULT);
        try {
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte imageByte[] = Base64.decode(encodedImage, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);
        if (bitmap == null)
            AIOLog.d("bitmap:" + bitmap);
        mImgUserPicture.setImageBitmap(bitmap);
        int _loginBravoViaType = BravoSharePrefs.getInstance(getActivity()).getIntValue(BravoConstant.PREF_KEY_SESSION_LOGIN_BRAVO_VIA_TYPE);
        SessionLogin _sessionLogin = BravoUtils.getSession(getActivity(), _loginBravoViaType);
        String userId = _sessionLogin.userID;
        String accessToken = _sessionLogin.accessToken;

        HashMap<String, String> subParams = new HashMap<String, String>();
        subParams.put("Profile_Img", encodedImage);
        subParams.put("Cover_Img", "");
        subParams.put("Profile_Img_Del", "0");
        subParams.put("Cover_Img_Del", "0");
        subParams.put("About_Me", "xxxxx");
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
                /* go to home screen */
                Intent homeIntent = new Intent(getActivity(), HomeActivity.class);
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
                getActivity().finish();
            }

            @Override
            public void processIfResponseFail() {
                AIOLog.d("response error");
            }
        }, params, true);
        postRegister.execute(putUserUrl);

    }
}
