package com.sharebravo.bravo.view.fragment.home;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sharebravo.bravo.R;
import com.sharebravo.bravo.control.activity.HomeActionListener;
import com.sharebravo.bravo.control.activity.HomeActivity;
import com.sharebravo.bravo.model.SessionLogin;
import com.sharebravo.bravo.model.response.ObBravo;
import com.sharebravo.bravo.model.response.ObPutReport;
import com.sharebravo.bravo.sdk.log.AIOLog;
import com.sharebravo.bravo.sdk.util.network.AsyncHttpPut;
import com.sharebravo.bravo.sdk.util.network.AsyncHttpResponseProcess;
import com.sharebravo.bravo.sdk.util.network.ImageLoader;
import com.sharebravo.bravo.sdk.util.network.ParameterFactory;
import com.sharebravo.bravo.utils.BravoConstant;
import com.sharebravo.bravo.utils.BravoSharePrefs;
import com.sharebravo.bravo.utils.BravoUtils;
import com.sharebravo.bravo.utils.BravoWebServiceConfig;
import com.sharebravo.bravo.view.fragment.FragmentBasic;
import com.sharebravo.bravo.view.lib.touchview.TouchImageView;

public class FragmentCoverImage extends FragmentBasic {
    TouchImageView             coverImage;
    private ObBravo            mObBravo;
    private ImageLoader        mImageLoader        = null;
    Button                     btnClose            = null;
    TextView                   btnDownload         = null;
    Button                     btnReport           = null;
    private SessionLogin       mSessionLogin       = null;
    private int                mLoginBravoViaType  = BravoConstant.NO_LOGIN_SNS;
    private HomeActionListener mHomeActionListener = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mHomeActionListener = (HomeActivity) getActivity();
        mLoginBravoViaType = BravoSharePrefs.getInstance(getActivity()).getIntValue(BravoConstant.PREF_KEY_SESSION_LOGIN_BRAVO_VIA_TYPE);
        mSessionLogin = BravoUtils.getSession(getActivity(), mLoginBravoViaType);
        View root = (ViewGroup) inflater.inflate(R.layout.page_cover_image, container);
        coverImage = (TouchImageView) root.findViewById(R.id.img_cover);
        mImageLoader = new ImageLoader(getActivity());
        btnDownload = (TextView) root.findViewById(R.id.btn_download);
        btnDownload.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Bitmap bitmap = Bitmap.createBitmap(coverImage.getWidth(), coverImage.getHeight(), Bitmap.Config.RGB_565);
                Canvas canvas = new Canvas(bitmap);
                coverImage.draw(canvas);
                saveImage(bitmap);
            }
        });
        btnClose = (Button) root.findViewById(R.id.btn_close_cover);
        btnClose.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                mHomeActionListener.goToBack();
            }
        });
        btnReport = (Button) root.findViewById(R.id.btn_report);
        btnReport.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                showDialogReport();
            }
        });
        return root;
    }

    private void requestToPutReport(ObBravo obBravo) {
        String userId = mSessionLogin.userID;
        String accessToken = mSessionLogin.accessToken;
        HashMap<String, String> subParams = new HashMap<String, String>();
        subParams.put("Foreign_ID", obBravo.User_ID);
        subParams.put("Report_Type", "bravo");
        subParams.put("User_ID", userId);
        subParams.put("Detail", "");
        JSONObject jsonObject = new JSONObject(subParams);
        List<NameValuePair> params = ParameterFactory.createSubParamsPutFollow(jsonObject.toString());
        String url = BravoWebServiceConfig.URL_PUT_REPORT.replace("{User_ID}", userId).replace("{Access_Token}", accessToken);
        AsyncHttpPut putReport = new AsyncHttpPut(getActivity(), new AsyncHttpResponseProcess(getActivity(), this) {
            @Override
            public void processIfResponseSuccess(String response) {
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
                    showDialogReportOk();
                } else {
                    obPutReport = gson.fromJson(response.toString(), ObPutReport.class);
                    showToast(obPutReport.error);
                }
            }

            @Override
            public void processIfResponseFail() {
                AIOLog.d("response error");
            }
        }, params, true);
        AIOLog.d(url);
        putReport.execute(url);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && !isBackStatus()) {
            mImageLoader.DisplayImage(mObBravo.Last_Pic, R.drawable.user_picture_default, coverImage, false);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public ObBravo getObBravo() {
        return mObBravo;
    }

    public void setObBravo(ObBravo mObBravo) {
        this.mObBravo = mObBravo;
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
                dialog.dismiss();
                requestToPutReport(mObBravo);
            }
        });
        Button btnCancel = (Button) dialog_view.findViewById(R.id.btn_report_no);
        btnCancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setContentView(dialog_view);
        dialog.show();
    }

    public void showDialogReportOk() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) getActivity().getLayoutInflater();
        View dialog_view = inflater.inflate(R.layout.dialog_report_ok, null);
        Button btnReportClose = (Button) dialog_view.findViewById(R.id.btn_report_close);
        btnReportClose.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dialog.dismiss();

            }
        });
        dialog.setContentView(dialog_view);

        dialog.show();
    }

    public void saveImage(final Bitmap finalBitmap) {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/bravo_image");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Bravo-" + mObBravo.Bravo_ID + ".png";
        File file = new File(myDir, fname);
        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        Toast.makeText(getActivity(), "Bravo Saved", Toast.LENGTH_LONG).show();
    }
}
