package com.sharebravo.bravo.sdk.util.network;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.widget.ImageView;

import com.google.android.gms.internal.is;
import com.sharebravo.bravo.R;
import com.sharebravo.bravo.sdk.util.imageloader.FileCache;
import com.sharebravo.bravo.sdk.util.imageloader.MemoryCache;
import com.sharebravo.bravo.sdk.util.imageloader.MyJavaxSSLSocketFactory;
import com.sharebravo.bravo.sdk.util.imageloader.Utils;

public class ImageLoader {
    private Context                mContext;
    MemoryCache                    memoryCache = new MemoryCache();
    FileCache                      fileCache;
    private Map<ImageView, String> imageViews  = Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
    ExecutorService                executorService;

    public ImageLoader(Context context) {
        mContext = context;
        fileCache = new FileCache(context);
        executorService = Executors.newFixedThreadPool(5);
    }

    int stub_id = R.drawable.ic_launcher;

    public void DisplayImage(String url, int loader, ImageView imageView, boolean isCorner) {
        stub_id = loader;
        imageViews.put(imageView, url);
        Bitmap bitmap = memoryCache.get(url);
        if (bitmap != null)
            imageView.setImageBitmap(bitmap);
        else
        {
            queuePhoto(url, imageView, isCorner);
            imageView.setImageResource(loader);
        }
    }

    private void queuePhoto(String url, ImageView imageView, boolean isCorner) {
        PhotoToLoad p = new PhotoToLoad(url, imageView);
        executorService.submit(new PhotosLoader(p, isCorner));
    }

    private Bitmap getBitmap(String url, boolean isCorner) {
        File f = fileCache.getFile(url);

        // from SD cache
        Bitmap b = decodeFile(f, isCorner);
        if (b != null)
            return b;

        // from web
        try {
            Bitmap bitmap = null;
            URL imageUrl = new URL(url);
            HttpsURLConnection conn = (HttpsURLConnection) imageUrl.openConnection();
            conn.setHostnameVerifier(new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            final KeyStore keystore = KeyStore.getInstance("BKS");

            InputStream in = mContext.getResources().openRawResource(R.raw.bravoandroid);
            try {
                // Initialize the keystore with the provided trusted certificates.
                // Also provide the password of the keystore
                keystore.load(in, "bravoandroid@#".toCharArray());
            } finally {
                in.close();
            }
            SSLSocketFactory sf = new MyJavaxSSLSocketFactory(keystore);
            trustAllHosts();
            conn.setSSLSocketFactory(sf);
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.setInstanceFollowRedirects(true);
            InputStream is = conn.getInputStream();
            OutputStream os = new FileOutputStream(f);
            Utils.CopyStream(is, os);
            os.close();
            bitmap = decodeFile(f, isCorner);
            return bitmap;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    // decodes image and scales it to reduce memory consumption
    private Bitmap decodeFile(File f, boolean isCorner) {
        try {
            // decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            // Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE = 70;
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            // while (true) {
            // if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
            // break;
            // width_tmp /= 2;
            // height_tmp /= 2;
            // scale *= 2;
            // }

            // decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return isCorner ? getRoundedCornerBitmap(BitmapFactory.decodeStream(new FileInputStream(f), null, o2), 50) : BitmapFactory.decodeStream(
                    new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {
        }
        return null;
    }

    // Task for the queue
    private class PhotoToLoad
    {
        public String    url;
        public ImageView imageView;

        public PhotoToLoad(String u, ImageView i) {
            url = u;
            imageView = i;
        }
    }

    class PhotosLoader implements Runnable {
        PhotoToLoad photoToLoad;
        boolean     isCorner;

        PhotosLoader(PhotoToLoad photoToLoad, boolean isCorner) {
            this.photoToLoad = photoToLoad;
            this.isCorner = isCorner;
        }

        @Override
        public void run() {
            if (imageViewReused(photoToLoad))
                return;
            Bitmap bmp = getBitmap(photoToLoad.url, isCorner);

            memoryCache.put(photoToLoad.url, bmp);
            if (imageViewReused(photoToLoad))
                return;
            BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);
            Activity a = (Activity) photoToLoad.imageView.getContext();
            a.runOnUiThread(bd);
        }
    }

    boolean imageViewReused(PhotoToLoad photoToLoad) {
        String tag = imageViews.get(photoToLoad.imageView);
        if (tag == null || !tag.equals(photoToLoad.url))
            return true;
        return false;
    }

    // Used to display bitmap in the UI thread
    class BitmapDisplayer implements Runnable
    {
        Bitmap      bitmap;
        PhotoToLoad photoToLoad;

        public BitmapDisplayer(Bitmap b, PhotoToLoad p) {
            bitmap = b;
            photoToLoad = p;
        }

        public void run()
        {
            if (imageViewReused(photoToLoad))
                return;
            if (bitmap != null)
                photoToLoad.imageView.setImageBitmap(bitmap);
            else
                photoToLoad.imageView.setImageResource(stub_id);
        }
    }

    public void clearCache() {
        memoryCache.clear();
        fileCache.clear();
    }

    /**
     * Trust every server - dont check for any certificate
     */
    private static void trustAllHosts() {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[] {};
            }

            public void checkClientTrusted(X509Certificate[] chain,
                    String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain,
                    String authType) throws CertificateException {
            }
        } };

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection
                    .setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
}
