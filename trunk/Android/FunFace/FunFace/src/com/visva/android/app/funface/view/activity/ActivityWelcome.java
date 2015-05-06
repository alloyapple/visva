package com.visva.android.app.funface.view.activity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.visva.android.app.funface.R;
import com.visva.android.app.funface.constant.FunFaceConstant;
import com.visva.android.app.funface.log.AIOLog;
import com.visva.android.app.funface.photointent.AlbumStorageDirFactory;
import com.visva.android.app.funface.photointent.BaseAlbumDirFactory;
import com.visva.android.app.funface.photointent.FroyoAlbumDirFactory;

public class ActivityWelcome extends Activity {
    private static final int       REQUEST_CODE_CAMERA     = 100;
    private static final int       REQUEST_CODE_GALLERY    = 101;
    private String                 mCurrentPhotoPath;
    private AlbumStorageDirFactory mAlbumStorageDirFactory = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((Button) findViewById(R.id.take_picture)).setOnClickListener(btnClick);
        ((Button) findViewById(R.id.take_picture_from_gallery)).setOnClickListener(btnClick);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            mAlbumStorageDirFactory = new FroyoAlbumDirFactory();
        } else {
            mAlbumStorageDirFactory = new BaseAlbumDirFactory();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("KieuThang", "onActivityResult:" + requestCode + ",data:" + data);
        if (REQUEST_CODE_CAMERA == requestCode) {
            galleryAddPic();
            sendIntentToActivityFaceLoader(mCurrentPhotoPath, REQUEST_CODE_CAMERA);
        } else if (REQUEST_CODE_GALLERY == requestCode) {
            if (data == null || resultCode == RESULT_CANCELED)
                return;
            Uri uri = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String imagePath = cursor.getString(columnIndex);
            cursor.close();

            File file = new File(imagePath);
            if (file.exists()) {
                sendIntentToActivityFaceLoader(imagePath, REQUEST_CODE_GALLERY);
            }
        }
    }

    private void sendIntentToActivityFaceLoader(String imagePath, int requestCode) {
        Intent intent = new Intent(this, ActivityFaceLoader.class);
        Log.d("KieuThang", "requestCode:" + requestCode);
        Log.d("KieuThang", "mCurrentPhotoPath:" + imagePath);
        intent.putExtra(FunFaceConstant.EXTRA_IMAGE_PATH, imagePath);
        intent.putExtra(FunFaceConstant.EXTRA_IMAGE_PATH, imagePath);
        startActivity(intent);
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        File f = null;

        try {
            f = setUpPhotoFile();
            mCurrentPhotoPath = f.getAbsolutePath();
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        } catch (IOException e) {
            e.printStackTrace();
            f = null;
            mCurrentPhotoPath = null;
        }
        startActivityForResult(takePictureIntent, REQUEST_CODE_CAMERA);
    }

    private File setUpPhotoFile() throws IOException {
        File f = createImageFile();
        mCurrentPhotoPath = f.getAbsolutePath();
        return f;
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
        String imageFileName = FunFaceConstant.JPEG_FILE_PREFIX + timeStamp + "_";
        File albumF = getAlbumDir();
        File imageF = File.createTempFile(imageFileName, FunFaceConstant.JPEG_FILE_SUFFIX, albumF);
        return imageF;
    }

    private File getAlbumDir() {
        File storageDir = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            storageDir = mAlbumStorageDirFactory.getAlbumStorageDir(getAlbumName());
            if (storageDir != null) {
                if (!storageDir.mkdirs()) {
                    if (!storageDir.exists()) {
                        AIOLog.d(FunFaceConstant.TAG, "failed to create directory");
                        return null;
                    }
                }
            }
        } else {
            AIOLog.d(FunFaceConstant.TAG, "External storage is not mounted READ/WRITE.");
        }
        return storageDir;
    }

    /* Photo album for this application */
    private String getAlbumName() {
        return getString(R.string.album_name);
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(FunFaceConstant.ACTION_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private View.OnClickListener btnClick = new View.OnClickListener() {
                                              @Override
                                              public void onClick(View v) {
                                                  switch (v.getId()) {
                                                  case R.id.take_picture:
                                                      openCamera();
                                                      break;
                                                  case R.id.take_picture_from_gallery:
                                                      takePictureFromGallery();
                                                      break;
                                                  }
                                              }
                                          };

    private void takePictureFromGallery() {
        // when user click gallery to get image
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, REQUEST_CODE_GALLERY);
    }
}
