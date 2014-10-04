package com.sharebravo.bravo.view.fragment.userprofile;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.sharebravo.bravo.R;
import com.sharebravo.bravo.control.activity.HomeActionListener;
import com.sharebravo.bravo.control.activity.HomeActivity;
import com.sharebravo.bravo.control.request.BravoRequestManager;
import com.sharebravo.bravo.control.request.IRequestListener;
import com.sharebravo.bravo.model.response.ObGetUserInfo;
import com.sharebravo.bravo.sdk.log.AIOLog;
import com.sharebravo.bravo.sdk.util.network.ImageLoader;
import com.sharebravo.bravo.view.adapter.AdapterUserDetail;
import com.sharebravo.bravo.view.fragment.FragmentBasic;
import com.sharebravo.bravo.view.lib.touchview.TouchImageView;

public class FragmentViewImage extends FragmentBasic {
    private TouchImageView     mCoverImage;
    private ObGetUserInfo      mObGetUserInfo;
    private ImageLoader        mImageLoader        = null;
    private Button             mBtnClose           = null;
    private TextView           mBtnDownload        = null;
    private Button             mBtnDelete          = null;
    private HomeActionListener mHomeActionListener = null;
    private String             mImageUrl;
    private int                mUserImageType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mHomeActionListener = (HomeActivity) getActivity();
        View root = (ViewGroup) inflater.inflate(R.layout.page_fragment_view_image, container);
        mCoverImage = (TouchImageView) root.findViewById(R.id.img_cover);
        mImageLoader = new ImageLoader(getActivity());
        mBtnDownload = (TextView) root.findViewById(R.id.btn_download);
        mBtnDownload.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Bitmap bitmap = Bitmap.createBitmap(mCoverImage.getWidth(), mCoverImage.getHeight(), Bitmap.Config.RGB_565);
                Canvas canvas = new Canvas(bitmap);
                mCoverImage.draw(canvas);
                saveImage(bitmap);
            }
        });
        mBtnClose = (Button) root.findViewById(R.id.btn_close_cover);
        mBtnClose.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                mHomeActionListener.goToBack();
            }
        });
        mBtnDelete = (Button) root.findViewById(R.id.btn_delete_image);
        mBtnDelete.setOnClickListener(new OnClickListener() {

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
        if (!hidden) {
            mImageLoader.DisplayImage(mImageUrl, R.drawable.user_picture_default, mCoverImage, false);
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

    public ObGetUserInfo getObUserInfo() {
        return mObGetUserInfo;
    }

    public void setObGetUserInfo(ObGetUserInfo obGetUserInfo, int userImageType) {
        this.mObGetUserInfo = obGetUserInfo;
        if (AdapterUserDetail.USER_AVATAR_ID == userImageType)
            this.mImageUrl = mObGetUserInfo.data.Profile_Img_URL;
        else
            this.mImageUrl = mObGetUserInfo.data.Cover_Img_URL;
        mUserImageType = userImageType;
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
                BravoRequestManager.getInstance(getActivity()).requestToDeleteImageUserProfile(mObGetUserInfo, mUserImageType, new IRequestListener() {
                    
                    @Override
                    public void onResponse(String response) {
                        mHomeActionListener.requestUpdateUserInfo();
                        showDialogRemovePhotoOk();
                    }
                    
                    @Override
                    public void onErrorResponse(String errorMessage) {
                        AIOLog.d("Cannot remove photo");
                    }
                }, FragmentViewImage.this);
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

    private void showDialogSavePhotoOk() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) getActivity().getLayoutInflater();
        View dialog_view = inflater.inflate(R.layout.dialog_save_photo, null);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        // This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);
        Button btnReportClose = (Button) dialog_view.findViewById(R.id.btn_save_photo_ok);
        btnReportClose.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        dialog.setContentView(dialog_view);

        dialog.show();
    }

    private void showDialogRemovePhotoOk() {
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
        String fname = "Bravo-" + mObGetUserInfo.data.User_ID + ".png";
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

        showDialogSavePhotoOk();
    }
}
