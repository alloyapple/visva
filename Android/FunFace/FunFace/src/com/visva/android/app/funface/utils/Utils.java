package com.visva.android.app.funface.utils;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.view.View;
import android.widget.RelativeLayout.LayoutParams;

public class Utils {
    public static Bitmap decodeBitmapFromCameraIntent(String photoPath) {
        Bitmap bitmap = BitmapFactory.decodeFile(photoPath);
        if (bitmap == null)
            return null;
        /* Get the size of the ImageView */
        int targetW = bitmap.getWidth();
        int targetH = bitmap.getHeight();

        /* Get the size of the image */
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        /* Figure out which way needs to be reduced less */
        int scaleFactor = 1;
        if ((targetW > 0) || (targetH > 0)) {
            scaleFactor = Math.min(photoW / targetW, photoH / targetH);
        }

        /* Set bitmap options to scale the image decode target */
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        /* Decode the JPEG file into a Bitmap */
        Bitmap decodedBitmap = BitmapFactory.decodeFile(photoPath, bmOptions);

        Uri uri = Uri.fromFile(new File(photoPath));
        int orientation = checkOrientation(uri);

        return decodeBitmap(decodedBitmap, orientation);
    }

    private static Bitmap decodeBitmap(Bitmap bitmap, int orientation) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix mtx = new Matrix();
        mtx.postRotate(orientation);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, mtx, true);
    }

    /**
     * rotate image to have the exact orientation
     * 
     * @param fileUri
     * @return
     */
    public static int checkOrientation(Uri fileUri) {
        int rotate = 0;
        String imagePath = fileUri.getPath();
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(imagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Since API Level 5
        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
        case ExifInterface.ORIENTATION_ROTATE_270:
            rotate = 270;
            break;
        case ExifInterface.ORIENTATION_ROTATE_180:
            rotate = 180;
            break;
        case ExifInterface.ORIENTATION_ROTATE_90:
            rotate = 90;
            break;
        }
        return rotate;
    }

    public static float getInitAngle() {
        int orientation = -1;
        Random random = new Random();
        float angle = 0.0F;
        switch (random.nextInt(2)) {
        case 0:
            orientation = -1;
            break;

        default:
            orientation = 1;
            break;
        }
        switch (random.nextInt(3)) {
        case 0:
            angle = 0.0F;
            break;
        case 1:
            angle = 0.1F;
            break;
        case 2:
            angle = 0.2F;
            break;
        case 3:
            angle = 0.3F;
            break;
        default:
            break;
        }
        return orientation * angle;
    }

    public static Bitmap loadBitmapFromView(View v) {
        if (v.getMeasuredHeight() <= 0) {
            v.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            Bitmap b = Bitmap.createBitmap(v.getMeasuredWidth(), v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(b);
            v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
            v.draw(c);
            return b;
        } else {
            Bitmap b = Bitmap.createBitmap(v.getMeasuredWidth(), v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(b);
            v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
            v.draw(c);
            return b;
        }
    }

    public static int getResId(Context context, String resName) {
        int id = context.getResources().getIdentifier(resName, "drawable", context.getPackageName());
        return id;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromFile(String photoPath, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        //        BitmapFactory.decodeResource(res, resId, options);
        BitmapFactory.decodeFile(photoPath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inPreferredConfig = Bitmap.Config.RGB_565;

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(photoPath, options);
    }

    public static Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        if (bm != null)
            bm.recycle();
        return resizedBitmap;
    }

    public static String convertResourceToImageLoaderUri(Context context, int resID) {
        return "drawable://" + resID;
    }

    public static String convertResourceToUri(Context context, int resID) {
        return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                context.getResources().getResourcePackageName(resID) + '/' +
                context.getResources().getResourceTypeName(resID) + '/' +
                context.getResources().getResourceEntryName(resID)).toString();
    }
}
