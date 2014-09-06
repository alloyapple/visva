package com.sharebravo.bravo.view.fragment.home;

import java.io.File;
import java.util.Calendar;
import java.util.List;

import org.apache.http.NameValuePair;

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
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sharebravo.bravo.R;
import com.sharebravo.bravo.control.activity.HomeActionListener;
import com.sharebravo.bravo.control.activity.HomeActivity;
import com.sharebravo.bravo.model.SessionLogin;
import com.sharebravo.bravo.model.response.ObGetUserInfo;
import com.sharebravo.bravo.sdk.log.AIOLog;
import com.sharebravo.bravo.sdk.util.network.AsyncHttpGet;
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
import com.sharebravo.bravo.view.lib.imageheader.PullAndLoadListView;

public class FragmentUserDataTab extends FragmentBasic implements UserPostProfileListener {
    private static final int       REQUEST_CODE_CAMERA      = 2001;
    private static final int       REQUEST_CODE_GALLERY     = 2002;

    private Uri                    mCapturedImageURI        = null;
    private Button                 mBtnSettings;
    private IShowPageSettings      iShowPageSettings;
    private PullAndLoadListView    mListViewUserPostProfile = null;
    private AdapterUserDataProfile mAdapterUserDataProfile  = null;
    private HomeActionListener     mHomeActionListener      = null;
    private Button                 mBtnBack;
    private boolean                isMyData                 = false;
    private static int             mUserImageType;

    private OnItemClickListener    onItemClick              = new OnItemClickListener() {

                                                                @Override
                                                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                                                }
                                                            };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = (ViewGroup) inflater.inflate(R.layout.page_fragment_user_profile, null);

        initializeView(root);
        return root;
    }

    private void initializeView(View root) {
        mBtnSettings = (Button) root.findViewById(R.id.btn_settings);
        mHomeActionListener = (HomeActivity) getActivity();
        mListViewUserPostProfile = (PullAndLoadListView) root.findViewById(R.id.listview_user_post_profile);
        mAdapterUserDataProfile = new AdapterUserDataProfile(getActivity());
        mAdapterUserDataProfile.setListener(this);
        mListViewUserPostProfile.setFooterDividersEnabled(false);
        mListViewUserPostProfile.setAdapter(mAdapterUserDataProfile);
        mListViewUserPostProfile.setOnItemClickListener(onItemClick);
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

    /**
     * get user info to show on user data tab
     * 
     * @param foreignUserId
     */
    public void getUserInfo(String foreignUserId) {
        int _loginBravoViaType = BravoSharePrefs.getInstance(getActivity()).getIntValue(BravoConstant.PREF_KEY_SESSION_LOGIN_BRAVO_VIA_TYPE);
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
        } else {
            isMyData = false;
            mBtnBack.setVisibility(View.VISIBLE);
            mBtnSettings.setVisibility(View.GONE);
        }

        String url = BravoWebServiceConfig.URL_GET_USER_INFO + "/" + checkingUserId;
        List<NameValuePair> params = ParameterFactory.createSubParamsGetAllBravoItems(userId, accessToken);
        AsyncHttpGet getLoginRequest = new AsyncHttpGet(getActivity(), new AsyncHttpResponseProcess(getActivity()) {
            @Override
            public void processIfResponseSuccess(String response) {
                AIOLog.d("get user info at my data:" + response);
                if (StringUtility.isEmpty(response))
                    return;
                Gson gson = new GsonBuilder().serializeNulls().create();
                ObGetUserInfo obGetUserInfo = gson.fromJson(response.toString(), ObGetUserInfo.class);
                if (obGetUserInfo == null) {
                    AIOLog.e("obGetUserInfo is null");
                } else {
                    switch (obGetUserInfo.status) {
                    case BravoConstant.STATUS_FAILED:
                        showToast(getActivity().getResources().getString(R.string.get_user_info_error));
                        break;
                    case BravoConstant.STATUS_SUCCESS:
                        AIOLog.d("BravoConstant.STATUS_SUCCESS");
                        AIOLog.d("BravoConstant.data" + obGetUserInfo.data);
                        mAdapterUserDataProfile.updateUserProfile(obGetUserInfo, isMyData);
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
        getLoginRequest.execute(url);

    }

    public interface IShowPageSettings {
        public void showPageSettings();
    }

    public void setListener(IShowPageSettings ÌShowPageSettings) {
        this.iShowPageSettings = ÌShowPageSettings;
    }

    @Override
    public void requestUserImageType(int userImageType) {
        mUserImageType = userImageType;
        showDialogChooseImage(userImageType);
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
                        mAdapterUserDataProfile.setUserImage(photo, mUserImageType);
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
                    // mImgUserPicture.setImageBitmap(bmp);
                    mAdapterUserDataProfile.setUserImage(bmp, mUserImageType);
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
                    // mImgUserPicture.setImageBitmap(bmp);
                    mAdapterUserDataProfile.setUserImage(bmp, mUserImageType);
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
