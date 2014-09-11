package com.sharebravo.bravo.view.fragment.setting;

import java.io.File;
import java.util.Calendar;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sharebravo.bravo.R;
import com.sharebravo.bravo.control.activity.HomeActivity;
import com.sharebravo.bravo.model.SessionLogin;
import com.sharebravo.bravo.model.response.ObGetUserInfo;
import com.sharebravo.bravo.sdk.log.AIOLog;
import com.sharebravo.bravo.sdk.util.network.ImageLoader;
import com.sharebravo.bravo.utils.BravoConstant;
import com.sharebravo.bravo.utils.BravoSharePrefs;
import com.sharebravo.bravo.utils.BravoUtils;
import com.sharebravo.bravo.utils.StringUtility;
import com.sharebravo.bravo.view.fragment.FragmentBasic;

public class FragmentUpdateUserInfo extends FragmentBasic {
    // =======================Constant Define==============
    private static final int REQUEST_CODE_CAMERA            = 4001;
    private static final int REQUEST_CODE_GALLERY           = 4002;
    private static final int CHANGE_USER_INFO_TYPE_IMAGE    = 0X01;
    private static final int CHANGE_USER_INFO_TYPE_ABOUT_ME = 0X02;
    // =======================Class Define=================
    // =======================Variable Define==============
    private EditText         mEditTextUserName;
    private EditText         mEditTextUserDescription;
    private ImageView        mImgChoosePicture;
    private ImageView        mImgUserPicture;
    private Uri              mCapturedImageURI;
    private Button           mBtnBack;
    private Button           mBtnDone;

    private int              mLoginBravoViaType;
    private SessionLogin     mSessionLogin;
    private ObGetUserInfo    mObGetUserInfo;
    private ImageLoader      mImageLoader;
    private int              mChangeUserInfoType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = (ViewGroup) inflater.inflate(R.layout.page_fragment_update_user_info, container);

        initializeData();
        initializeView(root);
        return root;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            
        }
    }
    private void initializeData() {
        mHomeActionListener = (HomeActivity) getActivity();
        mImageLoader = new ImageLoader(getActivity());
        mLoginBravoViaType = BravoSharePrefs.getInstance(getActivity()).getIntValue(BravoConstant.PREF_KEY_SESSION_LOGIN_BRAVO_VIA_TYPE);
        mSessionLogin = BravoUtils.getSession(getActivity(), mLoginBravoViaType);
        String userInfoSharePreStr = BravoUtils.getUserProfileInfo(getActivity(), mLoginBravoViaType);
        Gson gson = new GsonBuilder().serializeNulls().create();
        mObGetUserInfo = gson.fromJson(userInfoSharePreStr.toString(), ObGetUserInfo.class);
    }

    private void initializeView(View root) {
        mBtnBack = (Button) root.findViewById(R.id.btn_back_update_user_profile);
        mBtnDone = (Button) root.findViewById(R.id.btn_done_update_user_profile);
        mEditTextUserName = (EditText) root.findViewById(R.id.edittext_input_user_name);
        mEditTextUserDescription = (EditText) root.findViewById(R.id.edit_text_update_your_self);
        mImgUserPicture = (ImageView) root.findViewById(R.id.img_user_picture);
        mImgChoosePicture = (ImageView) root.findViewById(R.id.img_picture_choose);
        mImgChoosePicture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showDialogChooseImage();
            }
        });
        mBtnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mHomeActionListener.goToBack();
            }
        });
        mBtnDone.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String userName = mEditTextUserName.getText().toString();
                String userDescription = mEditTextUserDescription.getText().toString();
                if (mObGetUserInfo != null) {
                    if (!mObGetUserInfo.data.Full_Name.equals(userName))
                        mChangeUserInfoType = CHANGE_USER_INFO_TYPE_ABOUT_ME;
                    if (!mObGetUserInfo.data.About_Me.equals(userDescription))
                        mChangeUserInfoType = CHANGE_USER_INFO_TYPE_ABOUT_ME;
                }
                if (CHANGE_USER_INFO_TYPE_ABOUT_ME > 0)
                    onDoneUpdateUserInfo();
                else
                    mHomeActionListener.goToBack();
            }

        });
        if (mObGetUserInfo != null) {
            mEditTextUserName.setText(mObGetUserInfo.data.Full_Name);
            String userAvatarUrl = mObGetUserInfo.data.Profile_Img_URL;
            AIOLog.d("userAvatarUrl:" + userAvatarUrl);
            if (StringUtility.isEmpty(userAvatarUrl)) {
                mImgUserPicture.setImageBitmap(null);
                mImgUserPicture.setBackgroundResource(R.drawable.btn_user_avatar_profile);
            } else {
                mImageLoader.DisplayImage(userAvatarUrl, R.drawable.user_picture_default, mImgUserPicture, true);
            }
        }
    }

    /**
     * on click done update user info
     */
    private void onDoneUpdateUserInfo() {
        showToast("Click done");
        if (mChangeUserInfoType == CHANGE_USER_INFO_TYPE_ABOUT_ME) {
            putUpdateUserProfile();
        } else if (mChangeUserInfoType == CHANGE_USER_INFO_TYPE_IMAGE) {
            postUpdateUserProfile();
        }
    }

    private void postUpdateUserProfile() {

    }

    private void putUpdateUserProfile() {

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
                        mChangeUserInfoType = CHANGE_USER_INFO_TYPE_IMAGE;
                        mImgUserPicture.setImageBitmap(photo);
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
                    mImgUserPicture.setImageBitmap(bmp);
                    mChangeUserInfoType = CHANGE_USER_INFO_TYPE_IMAGE;
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
                    int orientation = BravoUtils.checkOrientation(fileUri);
                    Bitmap bmp;
                    bmp = BravoUtils.decodeSampledBitmapFromFile(imagePath, 100, 100, orientation);
                    mImgUserPicture.setImageBitmap(bmp);
                    mChangeUserInfoType = CHANGE_USER_INFO_TYPE_IMAGE;
                } else {
                    AIOLog.d("file don't exist !");
                }
            }
            break;
        default:
            return;
        }
    }
}
