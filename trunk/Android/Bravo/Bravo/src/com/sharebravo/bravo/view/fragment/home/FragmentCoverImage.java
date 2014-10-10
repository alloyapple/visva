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
import android.view.WindowManager;
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
import com.sharebravo.bravo.control.request.BravoRequestManager;
import com.sharebravo.bravo.control.request.IRequestListener;
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
import com.sharebravo.bravo.view.fragment.userprofile.FragmentViewImage;
import com.sharebravo.bravo.view.lib.touchview.TouchImageView;

public class FragmentCoverImage extends FragmentBasic {
    TouchImageView             coverImage;
    private ObBravo            mObBravo;
    private ImageLoader        mImageLoader        = null;
    Button                     btnClose            = null;
    TextView                   btnDownload         = null;
    Button                     btnDeleteImage      = null;
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
        btnDeleteImage = (Button) root.findViewById(R.id.btn_delete_image);
        btnDeleteImage.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                showDialogDelete();
            }
        });
        return root;
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

    public void showDialogDelete() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) getActivity().getLayoutInflater();
        View dialog_view = inflater.inflate(R.layout.dialog_delete_image, null);
        Button btnOk = (Button) dialog_view.findViewById(R.id.btn_delete_yes);
        btnOk.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                BravoRequestManager.getInstance(getActivity()).requestToDeleteBravoImage(mObBravo, new IRequestListener() {
                    
                    @Override
                    public void onResponse(String response) {
                        mHomeActionListener.requestBravoDetailInfo();
                        showDialogRemovePhotoOk();
                    }
                    
                    @Override
                    public void onErrorResponse(String errorMessage) {
                        AIOLog.d("Cannot remove photo");
                    }
                }, FragmentCoverImage.this);
            }
        });
        Button btnCancel = (Button) dialog_view.findViewById(R.id.btn_delete_no);
        btnCancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setContentView(dialog_view);
        dialog.show();
    }

    public void showDialogRemovePhotoOk() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) getActivity().getLayoutInflater();
        View dialog_view = inflater.inflate(R.layout.dialog_removed_photo, null);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        // This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);
        Button btnReportClose = (Button) dialog_view.findViewById(R.id.btn_remove_photo_ok);
        btnReportClose.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
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
