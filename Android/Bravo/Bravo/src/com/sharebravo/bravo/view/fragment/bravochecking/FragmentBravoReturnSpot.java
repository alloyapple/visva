package com.sharebravo.bravo.view.fragment.bravochecking;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.sharebravo.bravo.R;
import com.sharebravo.bravo.control.activity.ActivityBravoChecking;
import com.sharebravo.bravo.control.activity.BravoCheckingListener;
import com.sharebravo.bravo.model.SessionLogin;
import com.sharebravo.bravo.model.response.ObGetSpot.Spot;
import com.sharebravo.bravo.sdk.log.AIOLog;
import com.sharebravo.bravo.sdk.util.network.AsyncHttpPostImage;
import com.sharebravo.bravo.sdk.util.network.AsyncHttpResponseProcess;
import com.sharebravo.bravo.sdk.util.network.ParameterFactory;
import com.sharebravo.bravo.utils.BravoConstant;
import com.sharebravo.bravo.utils.BravoSharePrefs;
import com.sharebravo.bravo.utils.BravoUtils;
import com.sharebravo.bravo.utils.BravoWebServiceConfig;
import com.sharebravo.bravo.view.fragment.FragmentBasic;

public class FragmentBravoReturnSpot extends FragmentBasic {
    // ====================Constant Define=================
    private static final int      REQUEST_CODE_CAMERA  = 7001;
    private static final int      REQUEST_CODE_GALLERY = 7002;
    // ====================Class Define====================
    private Spot                  mSpot;
    private BravoCheckingListener mBravoCheckingListener;
    // ====================Variable Define=================
    private ImageView             mImageSpot;
    private ImageView             mImageChooseImage;
    private TextView              mTextSpotName;
    private Button                mBtnReturnSpot;
    private Button                mBtnShareFacebook;
    private Button                mBtnShareTwitter;
    private Button                mBtnShareFourSquare;
    private Uri                   mCapturedImageURI;
    private Bitmap                mSpotBitmap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = (ViewGroup) inflater.inflate(R.layout.page_fragment_bravo_return_spots, container);

        mBravoCheckingListener = (ActivityBravoChecking) getActivity();
        initializeView(root);
        return root;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (mSpot != null) {
                AIOLog.d("mSpot.Spot_Name:" + mSpot.Spot_Name);
                mTextSpotName.setText(mSpot.Spot_Name);
            }
        }
    }

    private void requestToPostBravoSpotWithImage(Spot spot, Bitmap spotImage) {
        String encodedImage = null;
        if (mSpotBitmap != null) {
            BitmapFactory.Options options = new BitmapFactory.Options();

            options.inSampleSize = 1;
            options.inPurgeable = true;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            mSpotBitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
            // bitmap object
            byte byteImage_photo[] = baos.toByteArray();
            encodedImage = Base64.encodeToString(byteImage_photo, Base64.DEFAULT);
            try {
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            byte imageByte[] = Base64.decode(encodedImage, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);
            if (bitmap == null)
                AIOLog.d("bitmap:" + bitmap);
        }
        int _loginBravoViaType = BravoSharePrefs.getInstance(getActivity()).getIntValue(BravoConstant.PREF_KEY_SESSION_LOGIN_BRAVO_VIA_TYPE);
        SessionLogin _sessionLogin = BravoUtils.getSession(getActivity(), _loginBravoViaType);
        String userId = _sessionLogin.userID;
        String accessToken = _sessionLogin.accessToken;

        HashMap<String, String> subParams = new HashMap<String, String>();
        subParams.put("Bravo_Type", "spot");
        subParams.put("Spot_ID", "" + spot.Spot_ID);
        subParams.put("Time_Zone", TimeZone.getDefault().getID());
        subParams.put("Is_Private", "TRUE");
        subParams.put("SNS_Post", "");
        subParams.put("FS_User_ID", "");
        subParams.put("FS_Access_Token", "");
        if (mSpotBitmap == null)
            subParams.put("images", "");
        else
            subParams.put("images", encodedImage);

        JSONObject jsonObject = new JSONObject(subParams);
        String subParamsStr = jsonObject.toString();

        AIOLog.d("encodedImage:" + encodedImage);
        String putUserUrl = BravoWebServiceConfig.URL_POST_BRAVO.replace("{User_ID}", userId).replace("{Access_Token}", accessToken)
                .replace("{Bravo_ID}", "");
        AIOLog.d("putUserUrl:" + putUserUrl);
        List<NameValuePair> params = ParameterFactory.createSubParams(subParamsStr);
        AsyncHttpPostImage postBravo = new AsyncHttpPostImage(getActivity(), new AsyncHttpResponseProcess(getActivity(), this) {
            @Override
            public void processIfResponseSuccess(String response) {
                AIOLog.d("reponse after post bravo:" + response);
                /* go to home screen */
                mBravoCheckingListener.goToBack();
            }

            @Override
            public void processIfResponseFail() {
                AIOLog.d("response error");
            }
        }, params, true);
        postBravo.execute(putUserUrl);
    }

    private void initializeView(View root) {
        mImageSpot = (ImageView) root.findViewById(R.id.image_post_detail);
        mImageChooseImage = (ImageView) root.findViewById(R.id.img_picture_choose);
        mTextSpotName = (TextView) root.findViewById(R.id.txtView_spot_name);
        mBtnReturnSpot = (Button) root.findViewById(R.id.btn_return_spot);
        mBtnShareFacebook = (Button) root.findViewById(R.id.btn_return_spot_share_facebook);
        mBtnShareTwitter = (Button) root.findViewById(R.id.btn_return_spot_share_twitter);
        mBtnShareFourSquare = (Button) root.findViewById(R.id.btn_return_spot_share_foursquare);

        mImageChooseImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showDialogChooseImage();
            }
        });

        mBtnReturnSpot.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                requestToPostBravoSpotWithImage(mSpot, mSpotBitmap);
                // mBravoCheckingListener.goToBack();
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

    public void setBravoSpot(Spot _spot) {
        AIOLog.d("spot:=>" + _spot);
        this.mSpot = _spot;
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
                        mSpotBitmap = photo;
                        mImageSpot.setImageBitmap(photo);
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
                    int orientation = BravoUtils.checkOrientation(fileUri);
                    Bitmap bitmap = BravoUtils.decodeBitmapFromFile(capturedImageFilePath, 1000, 1000, orientation);
                    if (bitmap == null)
                        return;
                    mSpotBitmap = bitmap;
                    mImageSpot.setImageBitmap(bitmap);
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
                    Bitmap bitmap = BravoUtils.decodeBitmapFromFile(imagePath, 1000, 1000, orientation);
                    if (bitmap == null)
                        return;
                    mSpotBitmap = bitmap;
                    mImageSpot.setImageBitmap(bitmap);
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
