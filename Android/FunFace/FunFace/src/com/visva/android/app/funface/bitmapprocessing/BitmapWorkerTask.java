package com.visva.android.app.funface.bitmapprocessing;

import java.io.File;
import java.lang.ref.WeakReference;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.visva.android.app.funface.utils.Utils;

public class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {

    private final WeakReference<ImageView> imageWeakReference;
    public String                          mPhotoPath = null;

    public BitmapWorkerTask(ImageView imageView) {
        //Use a WeakReference to ensure the ImageView can be garbage collected
        imageWeakReference = new WeakReference<ImageView>(imageView);
    }

    //Decode image in background
    @Override
    protected Bitmap doInBackground(String... params) {
        mPhotoPath = params[0];
        return Utils.decodeSampledBitmapFromFile(mPhotoPath, 720, 1280);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (imageWeakReference != null && bitmap != null) {
            final ImageView imageView = imageWeakReference.get();
            if (imageView != null) {
                imageView.setImageBitmap(bitmap);

                Uri uri = Uri.fromFile(new File(mPhotoPath));
                Log.d("KieuThang", "uri:" + uri);
                int orientation = Utils.checkOrientation(uri);

                Matrix matrix = new Matrix();
                matrix.postRotate(orientation);
                imageView.setImageMatrix(matrix);
                imageView.invalidate();
            }
        }
    }
}
