package com.visva.android.app.funface.utils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

import com.visva.android.app.funface.R;
import com.visva.android.app.funface.constant.FunFaceConstant;
import com.visva.android.app.funface.log.AIOLog;
import com.visva.android.app.funface.photointent.AlbumStorageDirFactory;
import com.visva.android.app.funface.photointent.BaseAlbumDirFactory;
import com.visva.android.app.funface.photointent.FroyoAlbumDirFactory;

public class FileUtils {
    private static AlbumStorageDirFactory mAlbumStorageDirFactory;

    public static File setUpPhotoFile(Context context, String filePath) throws IOException {
        if (mAlbumStorageDirFactory == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
                mAlbumStorageDirFactory = new FroyoAlbumDirFactory();
            } else {
                mAlbumStorageDirFactory = new BaseAlbumDirFactory();
            }
        }
        File f = createImageFile(context);
        filePath = f.getAbsolutePath();
        return f;
    }

    private static File createImageFile(Context context) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
        String imageFileName = FunFaceConstant.JPEG_FILE_PREFIX + timeStamp + "_";
        File albumF = getAlbumDir(context);
        File imageF = File.createTempFile(imageFileName, FunFaceConstant.JPEG_FILE_SUFFIX, albumF);
        return imageF;
    }

    private static File getAlbumDir(Context context) {
        File storageDir = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            storageDir = mAlbumStorageDirFactory.getAlbumStorageDir(getAlbumName(context));
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

    private static String getAlbumName(Context context) {
        return context.getString(R.string.album_name);
    }

    public static void galleryAddPic(Context context, String imagePath) {
        Intent mediaScanIntent = new Intent(FunFaceConstant.ACTION_SCAN_FILE);
        File f = new File(imagePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }
}
