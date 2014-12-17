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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import br.com.condesales.EasyFoursquareAsync;
import br.com.condesales.criterias.CheckInCriteria;
import br.com.condesales.listeners.AccessTokenRequestListener;
import br.com.condesales.listeners.CheckInListener;
import br.com.condesales.listeners.UserInfoRequestListener;
import br.com.condesales.models.Checkin;
import br.com.condesales.models.User;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sharebravo.bravo.R;
import com.sharebravo.bravo.control.activity.ActivityBravoChecking;
import com.sharebravo.bravo.model.SessionLogin;
import com.sharebravo.bravo.model.response.ObPostBravo;
import com.sharebravo.bravo.model.response.ObPostSpot;
import com.sharebravo.bravo.model.response.SNS;
import com.sharebravo.bravo.model.response.Spot;
import com.sharebravo.bravo.sdk.log.AIOLog;
import com.sharebravo.bravo.sdk.util.network.AsyncHttpPost;
import com.sharebravo.bravo.sdk.util.network.AsyncHttpPostBravoWithImage;
import com.sharebravo.bravo.sdk.util.network.AsyncHttpResponseProcess;
import com.sharebravo.bravo.sdk.util.network.ParameterFactory;
import com.sharebravo.bravo.utils.BravoConstant;
import com.sharebravo.bravo.utils.BravoSharePrefs;
import com.sharebravo.bravo.utils.BravoUtils;
import com.sharebravo.bravo.utils.BravoWebServiceConfig;
import com.sharebravo.bravo.utils.StringUtility;
import com.sharebravo.bravo.view.fragment.FragmentBasic;
import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.entities.Profile;
import com.sromku.simple.fb.listeners.OnLoginListener;
import com.sromku.simple.fb.listeners.OnProfileListener;

public class FragmentBravoReturnSpot extends FragmentBasic implements AccessTokenRequestListener {
    // ====================Constant Define=================
    private static final int    REQUEST_CODE_CAMERA  = 101;
    private static final int    REQUEST_CODE_GALLERY = 102;
    // ====================Class Define====================
    private static Spot         mSpot;
    private EasyFoursquareAsync mEasyFoursquareAsync;
    // ====================Variable Define=================
    private ImageView           mImageSpot;

    private ImageView           mImageChooseImage;
    private ImageButton         mBtnImageCover;
    private TextView            mTextSpotName;
    private Button              mBtnReturnSpot;
    private Button              mBtnShareFacebook;
    private Button              mBtnShareTwitter;
    private Button              mBtnShareFourSquare;
    private Uri                 mCapturedImageURI;
    private Bitmap              mSpotBitmap;
    private ObPostBravo         mObPostBravo;
    private SessionLogin        mSessionLogin        = null;
    private int                 mLoginBravoViaType   = BravoConstant.NO_LOGIN_SNS;
    private boolean             isPostOnFacebook, isPostOnFourSquare, isPostOnTwitter;
    private String              mSharedText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = (ViewGroup) inflater.inflate(R.layout.page_fragment_bravo_return_spots, container);
        mLoginBravoViaType = BravoSharePrefs.getInstance(getActivity()).getIntValue(BravoConstant.PREF_KEY_SESSION_LOGIN_BRAVO_VIA_TYPE);
        mSessionLogin = BravoUtils.getSession(getActivity(), mLoginBravoViaType);
        mBravoCheckingListener = (ActivityBravoChecking) getActivity();
        initializeView(root);
        return root;
    }

    private void initializeData() {
        isPostOnTwitter = BravoUtils.isTwitterLoggedInAlready(getActivity());
        isPostOnFacebook = BravoUtils.isFacebookLoggedInAlready(getActivity());
        isPostOnFourSquare = BravoUtils.isFourSquareLoggedInAlready(getActivity());
        boolean isAlreadyTwitterCheck = BravoUtils.isTwitterAlreadyChecked(getActivity());
        boolean isAlreadyFourSquareCheck = BravoUtils.isFourSquareAlreadyChecked(getActivity());
        if (isPostOnFacebook) {
            mBtnShareFacebook.setBackgroundResource(R.drawable.facebook_share_on);
        } else {
            mBtnShareFacebook.setBackgroundResource(R.drawable.facebook_share_off);
        }
        if (isPostOnTwitter && isAlreadyTwitterCheck) {
            mBtnShareTwitter.setBackgroundResource(R.drawable.twitter_share_on);
        } else {
            mBtnShareTwitter.setBackgroundResource(R.drawable.twitter_share_off);
        }
        if (isPostOnFourSquare && isAlreadyFourSquareCheck) {
            mBtnShareFourSquare.setBackgroundResource(R.drawable.foursquare_share_on);
        } else {
            mBtnShareFourSquare.setBackgroundResource(R.drawable.foursquare_share_off);
        }
        mBtnShareFacebook.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d("Facebook", "isPostOnFacebook:" + isPostOnFacebook);
                if (!isPostOnFacebook) {
                    isPostOnFacebook = true;
                    if (mSimpleFacebook == null) {
                        mSimpleFacebook.login(onLoginListener);
                        return;
                    } else {
                        requestUserFacebookInfo();
                    }
                } else {
                    isPostOnFacebook = false;
                    BravoSharePrefs.getInstance(getActivity()).putBooleanValue(BravoConstant.PREF_KEY_FACEBOOK_LOGIN, isPostOnFacebook);
                    BravoSharePrefs.getInstance(getActivity()).putBooleanValue(BravoConstant.PREF_KEY_FACEBOOK_CHECKED, isPostOnFacebook);
                    mBtnShareFacebook.setBackgroundResource(R.drawable.facebook_share_off);
                }
            }
        });
        mBtnShareTwitter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isPostOnTwitter) {
                    isPostOnTwitter = false;
                    BravoSharePrefs.getInstance(getActivity()).putBooleanValue(BravoConstant.PREF_KEY_TWITTER_CHECKED, isPostOnTwitter);
                    BravoSharePrefs.getInstance(getActivity()).putBooleanValue(BravoConstant.PREF_KEY_TWITTER_LOGIN, isPostOnTwitter);
                    mBtnShareTwitter.setBackgroundResource(R.drawable.twitter_share_off);
                } else {
                    BravoSharePrefs.getInstance(getActivity()).putBooleanValue(BravoConstant.PREF_KEY_TWITTER_CHECKED, true);
                    mBravoCheckingListener.requestToLoginSNS(BravoConstant.TWITTER);
                    // mBtnShareTwitter.setBackgroundResource(R.drawable.twitter_share_on);
                }
            }
        });
        mBtnShareFourSquare.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isPostOnFourSquare) {
                    isPostOnFourSquare = false;
                    BravoSharePrefs.getInstance(getActivity()).putBooleanValue(BravoConstant.PREF_KEY_FOURSQUARE_CHECKED, isPostOnFourSquare);
                    BravoSharePrefs.getInstance(getActivity()).putBooleanValue(BravoConstant.PREF_KEY_FOURSQUARE_LOGIN, isPostOnFourSquare);
                    mBtnShareFourSquare.setBackgroundResource(R.drawable.foursquare_share_off);
                } else {
                    BravoSharePrefs.getInstance(getActivity()).putBooleanValue(BravoConstant.PREF_KEY_FOURSQUARE_CHECKED, true);
                    onCheckedToggleBtnFourSquare();
                }
            }
        });

        mEasyFoursquareAsync = new EasyFoursquareAsync(getActivity());
    }

    private void onCheckedToggleBtnFourSquare() {
        mEasyFoursquareAsync = new EasyFoursquareAsync(getActivity());
        mEasyFoursquareAsync.requestAccess(this);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            initializeData();
            if (mSpot != null) {
                AIOLog.d("mSpot.Spot_Name:" + mSpot.Spot_Name);
                mTextSpotName.setText(mSpot.Spot_Name);
            }
        }
    }

    private void requestToPostBravoSpot(Spot spot, Bitmap spotImage) {
        int _loginBravoViaType = BravoSharePrefs.getInstance(getActivity()).getIntValue(BravoConstant.PREF_KEY_SESSION_LOGIN_BRAVO_VIA_TYPE);
        SessionLogin _sessionLogin = BravoUtils.getSession(getActivity(), _loginBravoViaType);
        String userId = _sessionLogin.userID;
        String accessToken = _sessionLogin.accessToken;

        HashMap<String, String> subParams = new HashMap<String, String>();
        subParams.put("Bravo_Type", "spot");
        subParams.put("Spot_ID", "" + spot.Spot_ID);
        subParams.put("Time_Zone", TimeZone.getDefault().getID());

        JSONObject jsonObject = new JSONObject(subParams);
        String subParamsStr = jsonObject.toString();

        String putUserUrl = BravoWebServiceConfig.URL_POST_BRAVO.replace("{User_ID}", userId).replace("{Access_Token}", accessToken)
                .replace("/{Bravo_ID}", "");
        AIOLog.d("putUserUrl:" + putUserUrl);
        List<NameValuePair> params = ParameterFactory.createSubParams(subParamsStr);
        AsyncHttpPost postBravo = new AsyncHttpPost(getActivity(), new AsyncHttpResponseProcess(getActivity(), this) {
            @Override
            public void processIfResponseSuccess(String response) {
                AIOLog.d("obPostBravo:" + response);
                Gson gson = new GsonBuilder().serializeNulls().create();
                mObPostBravo = gson.fromJson(response.toString(), ObPostBravo.class);
                AIOLog.d("obPostBravo:" + mObPostBravo);
                if (mObPostBravo == null) {
                    mBravoCheckingListener.finishPostBravo();
                    return;
                }
                /* share sns */
                shareViaSNS(mObPostBravo);

                AIOLog.d("obPostBravo.Bravo_ID:" + mObPostBravo.data.Bravo_ID + ", FS_Checkin_Bravo" + mObPostBravo.data.FS_Checkin_Bravo);
                if (mSpotBitmap != null) {
                    updateBravoWithImage(mObPostBravo, mSpotBitmap);
                }
                else
                    /* go to return to spot detail */
                    mBravoCheckingListener.goToBack();
            }

            @Override
            public void processIfResponseFail() {
                AIOLog.d("response error");
            }
        }, params, true);
        postBravo.execute(putUserUrl);
    }

    private void shareViaSNS(ObPostBravo mObPostBravo) {
        Log.d("KieuThang", "isPostOnFourSquare:" + isPostOnFourSquare + ",isPostOnTwitter:" + isPostOnTwitter);
        mSharedText = getActivity().getString(R.string.share_bravo_on_sns_text, mSpot.Spot_Name);
        if (isPostOnFacebook)
            mBravoCheckingListener.shareViaSNSByRecentPost(BravoConstant.FACEBOOK, mObPostBravo, mSharedText);
        if (isPostOnFourSquare)
            shareViaFourSquare(mObPostBravo, mSharedText);
        if (isPostOnTwitter)
            mBravoCheckingListener.shareViaSNSByRecentPost(BravoConstant.TWITTER, mObPostBravo, mSharedText);
    }

    private void updateBravoWithImage(ObPostBravo obPostBravo, Bitmap spotBitmap) {
        int _loginBravoViaType = BravoSharePrefs.getInstance(getActivity()).getIntValue(BravoConstant.PREF_KEY_SESSION_LOGIN_BRAVO_VIA_TYPE);
        SessionLogin _sessionLogin = BravoUtils.getSession(getActivity(), _loginBravoViaType);
        String userId = _sessionLogin.userID;
        String accessToken = _sessionLogin.accessToken;
        if (spotBitmap == null)
            return;
        String encodedImage = null;
        BitmapFactory.Options options = new BitmapFactory.Options();

        options.inSampleSize = 1;
        options.inPurgeable = true;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        spotBitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        // bitmap object
        byte byteImage_photo[] = baos.toByteArray();
        encodedImage = Base64.encodeToString(byteImage_photo, Base64.DEFAULT);
        try {
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        HashMap<String, String> subParams = new HashMap<String, String>();
        subParams.put("image", encodedImage);
        subParams.put("Is_Private", "TRUE");

        JSONObject jsonObject = new JSONObject(subParams);
        String subParamsStr = jsonObject.toString();
        String putUserUrl = BravoWebServiceConfig.URL_POST_BRAVO.replace("{User_ID}", userId).replace("{Access_Token}", accessToken)
                .replace("{Bravo_ID}", obPostBravo.data.Bravo_ID + "");
        AIOLog.d("putUserUrl:" + putUserUrl);
        List<NameValuePair> params = ParameterFactory.createSubParams(subParamsStr);
        AsyncHttpPostBravoWithImage postBravoImage = new AsyncHttpPostBravoWithImage(getActivity(),
                new AsyncHttpResponseProcess(getActivity(), this) {
                    @Override
                    public void processIfResponseSuccess(String response) {
                        mBravoCheckingListener.finishPostBravo();
                    }

                    @Override
                    public void processIfResponseFail() {
                        AIOLog.d("response error");
                        mBravoCheckingListener.finishPostBravo();
                    }
                }, params, true);
        postBravoImage.execute(putUserUrl);
    }

    private void initializeView(View root) {
        mImageSpot = (ImageView) root.findViewById(R.id.image_post_detail);
        mImageChooseImage = (ImageView) root.findViewById(R.id.img_picture_choose);
        mBtnImageCover = (ImageButton) root.findViewById(R.id.btn_img_cover);
        mTextSpotName = (TextView) root.findViewById(R.id.txtView_spot_name);
        mBtnReturnSpot = (Button) root.findViewById(R.id.btn_return_spot);
        mBtnShareFacebook = (Button) root.findViewById(R.id.btn_return_spot_share_facebook);
        mBtnShareTwitter = (Button) root.findViewById(R.id.btn_return_spot_share_twitter);
        mBtnShareFourSquare = (Button) root.findViewById(R.id.btn_return_spot_share_foursquare);

        mBtnImageCover.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showDialogChooseImage();
            }
        });
        mImageChooseImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showDialogChooseImage();
            }
        });

        mBtnReturnSpot.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onReturnToSpot();
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
        mSpot = _spot;
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
                        mBtnImageCover.setVisibility(View.GONE);
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
                    mSpotBitmap = BravoUtils.decodeBitmapFromFile(capturedImageFilePath, 1000, 1000, orientation);
                    if (mSpotBitmap == null)
                        return;
                    mImageSpot.setImageBitmap(mSpotBitmap);
                    mBtnImageCover.setVisibility(View.GONE);
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
                    mSpotBitmap = BravoUtils.decodeBitmapFromFile(imagePath, 1000, 1000, orientation);
                    if (mSpotBitmap == null)
                        return;
                    mImageSpot.setImageBitmap(mSpotBitmap);
                    mBtnImageCover.setVisibility(View.GONE);
                } else {
                    AIOLog.d("file don't exist !");
                }
            }
            break;
        default:
            return;
        }
    }

    private void requestPostSpot(Spot spot) {
        String userId = mSessionLogin.userID;
        String accessToken = mSessionLogin.accessToken;
        String url = BravoWebServiceConfig.URL_POST_SPOTS.replace("{User_ID}", userId).replace("{Access_Token}", accessToken);

        HashMap<String, Object> subParams = new HashMap<String, Object>();
        subParams.put("Spot_Name", spot.Spot_Name);
        subParams.put("Spot_FID", spot.Spot_FID);
        subParams.put("Spot_Source", spot.Spot_Source);
        subParams.put("Spot_Longitude", (float) spot.Spot_Longitude);
        subParams.put("Spot_Latitude", (float) spot.Spot_Latitude);
        subParams.put("Spot_Type", spot.Spot_Type);
        subParams.put("Spot_Genre", spot.Spot_Genre);
        subParams.put("Spot_Address", spot.Spot_Address);
        subParams.put("Spot_Phone", spot.Spot_Phone);
        // subParams.put("Spot_Price", spot.Spot_Price);
        JSONObject jsonObject = new JSONObject(subParams);
        List<NameValuePair> params = ParameterFactory.createSubParams(jsonObject.toString());
        AsyncHttpPost request = new AsyncHttpPost(getActivity(), new AsyncHttpResponseProcess(getActivity(), this) {
            @Override
            public void processIfResponseSuccess(String response) {
                AIOLog.d("response mObPostSpot:" + response);
                Gson gson = new GsonBuilder().serializeNulls().create();
                ObPostSpot mObPostSpot;
                mObPostSpot = gson.fromJson(response.toString(), ObPostSpot.class);
                AIOLog.d("mObPostSpot:" + mObPostSpot);
                if (mObPostSpot == null)
                    return;
                else {
                    mSpot.Spot_ID = mObPostSpot.data.Spot_ID;
                    requestToPostBravoSpot(mSpot, mSpotBitmap);
                }
            }

            @Override
            public void processIfResponseFail() {
                AIOLog.d("response error");
            }
        }, params, true);
        request.execute(url);
    }

    public void updatePostSNS(SNS sns, boolean b) {
        if (BravoConstant.FACEBOOK.equals(sns.foreignSNS)) {
            isPostOnFacebook = b;
            if (isPostOnFacebook)
                mBtnShareFacebook.setBackgroundResource(R.drawable.facebook_share_on);
            else
                mBtnShareFacebook.setBackgroundResource(R.drawable.facebook_share_off);
        } else if (BravoConstant.FOURSQUARE.equals(sns.foreignSNS)) {
            isPostOnFourSquare = b;
            if (!isPostOnFourSquare)
                mBtnShareFourSquare.setBackgroundResource(R.drawable.foursquare_share_off);
            else
                mBtnShareFourSquare.setBackgroundResource(R.drawable.foursquare_share_on);
        } else if (BravoConstant.TWITTER.equals(sns.foreignSNS)) {
            isPostOnTwitter = b;
            if (!isPostOnTwitter)
                mBtnShareTwitter.setBackgroundResource(R.drawable.twitter_share_off);
            else
                mBtnShareTwitter.setBackgroundResource(R.drawable.twitter_share_on);
        }
    }

    public void onReturnToSpot() {
        if (mSpot == null)
            return;
        if (mSpot.Spot_ID != null && !StringUtility.isEmpty(mSpot.Spot_ID)) {
            requestToPostBravoSpot(mSpot, mSpotBitmap);
        } else {
            requestPostSpot(mSpot);
        }
    }

    @Override
    public void onError(String errorMsg) {
        isPostOnFourSquare = false;
    }

    @Override
    public void onAccessGrant(final String accessToken) {
        AIOLog.d("accessToken: " + accessToken);
        mEasyFoursquareAsync.getUserInfo(new UserInfoRequestListener() {

            @Override
            public void onError(String errorMsg) {
                isPostOnFourSquare = false;
                BravoSharePrefs.getInstance(getActivity()).putBooleanValue(BravoConstant.PREF_KEY_FOURSQUARE_LOGIN, false);
            }

            @Override
            public void onUserInfoFetched(User user) {
                AIOLog.d("user 4square:" + user);
                if (user == null)
                    return;
                AIOLog.d("user: " + user.getFirstName());
                SNS sns = new SNS();
                sns.foreignAccessToken = accessToken;
                sns.foreignID = user.getId() + "";
                sns.foreignSNS = BravoConstant.FOURSQUARE;

                mBravoCheckingListener.putSNS(sns);
                BravoSharePrefs.getInstance(getActivity()).putBooleanValue(BravoConstant.PREF_KEY_FOURSQUARE_LOGIN, true);
                BravoSharePrefs.getInstance(getActivity()).putStringValue(BravoConstant.PREF_KEY_FOURSQUARE_ID_LOGINED, sns.foreignID);
                mBtnShareFourSquare.setBackgroundResource(R.drawable.foursquare_share_on);
                Log.d("KieuThang", "mObPostBravo:" + mObPostBravo + ",isPostOnFourSquare:" + isPostOnFourSquare);
                if (mObPostBravo != null && isPostOnFourSquare)
                    shareViaFourSquare(mObPostBravo, mSharedText);
            }
        });
        isPostOnFourSquare = true;

    }

    private void shareViaFourSquare(ObPostBravo obPostBravo, String sharedText) {
        if (obPostBravo == null || mEasyFoursquareAsync == null) {
            mEasyFoursquareAsync.requestAccess(this);
        } else {
            CheckInCriteria criteria = new CheckInCriteria();
            criteria.setBroadcast(CheckInCriteria.BroadCastType.PUBLIC);
            criteria.setVenueId(mSpot.Spot_FID);
            AIOLog.d("mSpot.Spot_FID: " + mSpot.Spot_FID);
            // criteria.setVenueId("4c7063da9c6d6dcb9798d27a");

            mEasyFoursquareAsync.checkIn(new CheckInListener() {
                @Override
                public void onCheckInDone(Checkin checkin) {
                    AIOLog.d("check in via recet post");
                }

                @Override
                public void onError(String errorMsg) {
                }
            }, criteria);
        }
    }

    /**
     * Login example.
     */
    // Login listener
    final OnLoginListener onLoginListener = new OnLoginListener() {

                                              @Override
                                              public void onFail(String reason) {
                                                  AIOLog.e("Failed to login");
                                              }

                                              @Override
                                              public void onException(Throwable throwable) {
                                                  AIOLog.e("Bad thing happened", throwable);
                                              }

                                              @Override
                                              public void onThinking() {
                                                  // show progress bar or something to the user while login is
                                                  // happening
                                              }

                                              @Override
                                              public void onLogin() {
                                                  // change the state of the button or do whatever you want
                                                  AIOLog.d("onLogin");
                                                  requestUserFacebookInfo();
                                              }

                                              @Override
                                              public void onNotAcceptingPermissions(Permission.Type type) {
                                                  Toast.makeText(getActivity(), String.format("You didn't accept %s permissions", type.name()),
                                                          Toast.LENGTH_SHORT).show();
                                              }
                                          };

    private void requestUserFacebookInfo() {
        if (mSimpleFacebook == null) {
            mSimpleFacebook = SimpleFacebook.getInstance(getActivity());
            return;
        }
        SimpleFacebook.getInstance().getProfile(new OnProfileListener() {

            @Override
            public void onThinking() {
            }

            @Override
            public void onException(Throwable throwable) {
            }

            @Override
            public void onFail(String reason) {
            }

            @Override
            public void onComplete(Profile profile) {
                onFacebookUserConnected(profile);
            }
        });

    }

    private void onFacebookUserConnected(Profile profile) {
        SNS sns = new SNS();
        sns.foreignAccessToken = "";
        sns.foreignID = profile.getId();
        sns.foreignSNS = BravoConstant.FACEBOOK;
        mBravoCheckingListener.putSNS(sns);

        isPostOnFacebook = true;
        BravoSharePrefs.getInstance(getActivity()).putBooleanValue(BravoConstant.PREF_KEY_FACEBOOK_LOGIN, isPostOnFacebook);
        BravoSharePrefs.getInstance(getActivity()).putBooleanValue(BravoConstant.PREF_KEY_FACEBOOK_CHECKED, isPostOnFacebook);
        BravoSharePrefs.getInstance(getActivity()).putStringValue(BravoConstant.PREF_KEY_FACEBOOK_ID_LOGINED, sns.foreignID);
        mBtnShareFacebook.setBackgroundResource(R.drawable.facebook_share_on);
    }
}
