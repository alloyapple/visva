package com.sharebravo.bravo.view.fragment;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sharebravo.bravo.R;
import com.sharebravo.bravo.control.activity.HomeActivity;
import com.sharebravo.bravo.model.response.ObPostUserFailed;
import com.sharebravo.bravo.model.user.BravoUser;
import com.sharebravo.bravo.sdk.log.AIOLog;
import com.sharebravo.bravo.sdk.util.network.AsyncHttpPost;
import com.sharebravo.bravo.sdk.util.network.AsyncHttpResponseProcess;
import com.sharebravo.bravo.sdk.util.network.ParameterFactory;
import com.sharebravo.bravo.utils.BravoConstant;
import com.sharebravo.bravo.utils.BravoSharePrefs;
import com.sharebravo.bravo.utils.BravoWebServiceConfig;
import com.sharebravo.bravo.utils.StringUtility;

public class FragmentRegisterUserInfo extends FragmentBasic {
    // ====================Constant Define=================
    private static final int REQUEST_CODE_CAMERA  = 1001;
    private static final int REQUEST_CODE_GALLERY = 1002;
    // ====================Class Define====================
    private BravoUser        mBravoUser;
    // ====================Variable Define=================
    private EditText         mEditTextUserName;
    private Button           mBtnDoneRegister;
    private ImageView        mImgChoosePicture;
    private ImageView        mImgUserPicture;
    private Uri              mCapturedImageURI;

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
                    mEditTextUserName.setError(getActivity().getResources().getString(R.string.username_not_valid));
            }
        });
        mEditTextUserName = (EditText) root.findViewById(R.id.edittext_input_user_name);
        mImgUserPicture = (ImageView) root.findViewById(R.id.img_user_picture);
        mImgChoosePicture = (ImageView) root.findViewById(R.id.img_picture_choose);
        mImgChoosePicture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                pickImage();
            }
        });
        return root;
    }

    private void requestToPostBravoUserbySNS(BravoUser bravoUser) {
        HashMap<String, String> subParams = new HashMap<String, String>();
        subParams.put("Auth_Method", bravoUser.mAuthenMethod);
        subParams.put("Full_Name", bravoUser.mUserName);
        subParams.put("Email", bravoUser.mUserEmail);
        subParams.put("Foreign_ID", bravoUser.mForeign_Id);
        subParams.put("Password", bravoUser.mUserPassWord);
        subParams.put("Time_Zone", bravoUser.mTimeZone);
        subParams.put("Locale", bravoUser.mLocale);
        JSONObject jsonObject = new JSONObject(subParams);
        String subParamsStr = jsonObject.toString();

        List<NameValuePair> params = ParameterFactory.createSubParams(subParamsStr);
        AsyncHttpPost postRegister = new AsyncHttpPost(getActivity(), new AsyncHttpResponseProcess(getActivity()) {
            @Override
            public void processIfResponseSuccess(String response) {
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
                ObPostUserFailed obPostUserFailed;
                if (status == String.valueOf(BravoWebServiceConfig.STATUS_RESPONSE_DATA_SUCCESS)) {
                    if (mBravoUser.mRegisterType == BravoConstant.REGISTER_TYPE_FACEBOOK) {
                        BravoSharePrefs.getInstance(getActivity()).putStringValue(BravoConstant.PREF_KEY_SESSION_REGISTER_BY_FACEBOOK, response);
                        BravoSharePrefs.getInstance(getActivity()).putIntValue(BravoConstant.PREF_KEY_SESSION_REGISTER_TYPE,
                                BravoConstant.REGISTER_TYPE_FACEBOOK);
                    } else if (mBravoUser.mRegisterType == BravoConstant.REGISTER_TYPE_TWITTER) {
                        BravoSharePrefs.getInstance(getActivity()).putIntValue(BravoConstant.PREF_KEY_SESSION_REGISTER_TYPE,
                                BravoConstant.REGISTER_TYPE_TWITTER);
                        BravoSharePrefs.getInstance(getActivity()).putStringValue(BravoConstant.PREF_KEY_SESSION_REGISTER_BY_TWITTER, response);
                    } else if (mBravoUser.mRegisterType == BravoConstant.REGISTER_TYPE_FOURSQUARE) {
                        BravoSharePrefs.getInstance(getActivity()).putIntValue(BravoConstant.PREF_KEY_SESSION_REGISTER_TYPE,
                                BravoConstant.REGISTER_TYPE_FOURSQUARE);
                        BravoSharePrefs.getInstance(getActivity()).putStringValue(BravoConstant.PREF_KEY_SESSION_REGISTER_BY_4SQUARE, response);
                    }
                    Intent homeIntent = new Intent(getActivity(), HomeActivity.class);
                    getActivity().startActivity(homeIntent);
                    getActivity().finish();
                } else {
                    obPostUserFailed = gson.fromJson(response.toString(), ObPostUserFailed.class);
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

    private void pickImage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.pick_image);
        builder.setItems(R.array.image_choose_type_array, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                case 0:
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
                    break;

                case 1:
                    // when user click gallery to get image
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, REQUEST_CODE_GALLERY);
                    break;

                default:
                    break;
                }
            }
        });
        builder.show();
    }

    @SuppressWarnings("static-access")
    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
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
                    mImgUserPicture.setImageBitmap(photo);
                    return;
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
                    int orientation = checkOrientation(fileUri);
                    Bitmap bmp;
                    bmp = decodeSampledBitmapFromFile(imagePath, 100, 100, orientation);
                    mImgUserPicture.setImageBitmap(bmp);
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
                    int orientation = checkOrientation(fileUri);
                    Bitmap bmp;
                    bmp = decodeSampledBitmapFromFile(imagePath, 100, 100, orientation);
                    mImgUserPicture.setImageBitmap(bmp);

                } else {
                    AIOLog.d("file don't exist !");
                }
            }
            break;
        default:
            return;
        }
    }

    /**
     * rotate image to have the exact orientation
     * 
     * @param fileUri
     * @return
     */
    private int checkOrientation(Uri fileUri) {
        int rotate = 0;
        String imagePath = fileUri.getPath();
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(imagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Since API Level 5
        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
        case ExifInterface.ORIENTATION_ROTATE_270:
            rotate = 270;
            break;
        case ExifInterface.ORIENTATION_ROTATE_180:
            rotate = 180;
            break;
        case ExifInterface.ORIENTATION_ROTATE_90:
            rotate = 90;
            break;
        }
        return rotate;
    }

    private Bitmap decodeSampledBitmapFromFile(String filePath, int reqWidth, int reqHeight, int orientation) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Matrix mtx = new Matrix();
        mtx.postRotate(orientation);
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return decodeBitmap(BitmapFactory.decodeFile(filePath, options), orientation);
    }

    private Bitmap decodeBitmap(Bitmap bitmap, int orientation) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix mtx = new Matrix();
        mtx.postRotate(orientation);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, mtx, true);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

}
