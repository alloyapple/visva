package com.visva.android.socialstackwidget.widgetprovider;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.facebook.LoggingBehavior;
import com.facebook.internal.ImageDownloader;
import com.facebook.internal.ImageRequest;
import com.facebook.internal.ImageResponse;
import com.facebook.internal.Logger;
import com.facebook.internal.MyImageDownloader;
import com.facebook.internal.MyImageRequest;
import com.facebook.internal.MyImageResponse;
import com.visva.android.socialstackwidget.R;
import com.visva.android.socialstackwidget.constant.GlobalContstant;
import com.visva.android.socialstackwidget.database.SocialWidgetItem;
import com.visva.android.socialstackwidget.object.facebookobject.FacebookFeedWrapperObject;
import com.visva.android.socialstackwidget.util.Utils;
import com.visva.android.socialstackwidget.util.VisvaLog;

public class SocialWidgetMgr {
    private static final String TAG = GlobalContstant.PRE_TAG + "SocialWidgetMgr";

    public static Bitmap getImageBitmap(String imageUrl) {
        if (imageUrl == null)
            return null;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Bitmap imgBitmap = null;
        HttpURLConnection conn = null;
        BufferedInputStream bis = null;
        try {
            URL url = new URL(imageUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.connect();
            int nSize = conn.getContentLength();
            bis = new BufferedInputStream(conn.getInputStream(), nSize);
            imgBitmap = BitmapFactory.decodeStream(bis);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null)
                conn.disconnect();
        }
        return imgBitmap;
    }

    public synchronized static void getMainImageRequest(Context context, String imageUrl, final SocialWidgetItem socialWidgetItem) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        ImageLoader imageLoader = new ImageLoader(context, socialWidgetItem);
        imageLoader.execute(socialWidgetItem.getImage_Url());
        //        mImageLoader = VolleySingleton.getInstance(context).getImageLoader();
        //        mImageLoader.get(imageUrl, new ImageListener() {
        //            @Override
        //            public void onErrorResponse(VolleyError volleyError) {
        //                VisvaLog.e(TAG, "onErrorResponse" + volleyError.getMessage());
        //            }
        //
        //            @Override
        //            public void onResponse(ImageContainer response, boolean isImmediate) {
        //                if (response.getBitmap() != null) {
        //                    socialWidgetItem.mMainBitmap = response.getBitmap();
        //                }
        //            }
        //        });
    }

    public static void getAuthorImageRequest(Context context, String profileId, boolean allowCachedResponse, final SocialWidgetItem socialWidgetItem) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            ImageRequest.Builder requestBuilder = new ImageRequest.Builder(context, ImageRequest.getProfilePictureUrl(profileId, 50, 50));

            final ImageRequest request = requestBuilder.setAllowCachedRedirects(allowCachedResponse)
                    .setCallerTag(context).setCallback(new ImageRequest.Callback() {
                        @Override
                        public void onCompleted(ImageResponse response) {
                            socialWidgetItem.mAuthorBitmap = response.getBitmap();
                        }
                    }).build();
            ImageDownloader.downloadAsync(request);
        } catch (URISyntaxException e) {
            Logger.log(LoggingBehavior.REQUESTS, Log.ERROR, TAG, e.toString());
        }
    }

    public static String setMainImageRequest(Context context, String imageUrl, boolean allowCachedResponse, final SocialWidgetItem socialWidgetItem) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            MyImageRequest.Builder requestBuilder = new MyImageRequest.Builder(context, MyImageRequest.getProfilePictureUrl(imageUrl, 50, 50));

            final MyImageRequest request = requestBuilder.setAllowCachedRedirects(allowCachedResponse)
                    .setCallerTag(context).setCallback(new MyImageRequest.Callback() {
                        @Override
                        public void onCompleted(MyImageResponse response) {
                            VisvaLog.d(TAG, "sendImageRequest " + response.getBitmap());
                            socialWidgetItem.mMainBitmap = response.getBitmap();
                        }
                    }).build();
            MyImageDownloader.downloadAsync(request);
        } catch (URISyntaxException e) {
            Logger.log(LoggingBehavior.REQUESTS, Log.ERROR, TAG, e.toString());
        }
        return imageUrl;
    }

    public static RemoteViews getRemoteView(Context context, SocialWidgetItem socialWidgetItem) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        FacebookFeedWrapperObject.TypeTimeline timelineType = FacebookFeedWrapperObject.TypeTimeline.valueOf(socialWidgetItem.getFeeds_type());
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_item);
        rv.setTextViewText(R.id.txt_author_name, socialWidgetItem.getAuthor_name());
        rv.setImageViewBitmap(R.id.img_author_avatar, socialWidgetItem.mAuthorBitmap);
        rv.setTextViewText(R.id.text_created_time, Utils.convertToDateTime(socialWidgetItem.getTime_stamp()));
        rv.setTextViewText(R.id.text_item_content, socialWidgetItem.getMessage());
        switch (timelineType) {
        case LINK:
            rv.setViewVisibility(R.id.layout_link, View.VISIBLE);
            rv.setViewVisibility(R.id.layout_photo, View.GONE);
            rv.setViewVisibility(R.id.main_image, View.GONE);
            rv.setViewVisibility(R.id.layout_text_only, View.GONE);
            rv.setViewVisibility(R.id.text_item_content, View.VISIBLE);
            rv.setTextViewText(R.id.text_link_url, socialWidgetItem.getLink_url());
            rv.setTextViewText(R.id.text_link_body, setTextLinkBody(rv, socialWidgetItem));
            rv.setImageViewBitmap(R.id.image_link, socialWidgetItem.mMainBitmap);
            break;
        case STATUS:
            rv.setViewVisibility(R.id.layout_text_only, View.VISIBLE);
            rv.setViewVisibility(R.id.layout_link, View.GONE);
            rv.setViewVisibility(R.id.layout_photo, View.VISIBLE);
            rv.setViewVisibility(R.id.main_image, View.GONE);
            rv.setViewVisibility(R.id.text_item_content, View.GONE);
            rv.setTextViewText(R.id.text_body_only, socialWidgetItem.getMessage());
            break;
        case PHOTO:
            rv.setViewVisibility(R.id.ic_video_play, View.GONE);
            rv.setViewVisibility(R.id.text_item_content, View.VISIBLE);
            rv.setViewVisibility(R.id.layout_link, View.GONE);
            rv.setViewVisibility(R.id.layout_text_only, View.GONE);
            rv.setViewVisibility(R.id.layout_photo, View.VISIBLE);
            rv.setViewVisibility(R.id.main_image, View.VISIBLE);
            rv.setImageViewBitmap(R.id.main_image, socialWidgetItem.mMainBitmap);
            break;
        case VIDEO:
            rv.setViewVisibility(R.id.ic_video_play, View.VISIBLE);
            rv.setViewVisibility(R.id.text_item_content, View.VISIBLE);
            rv.setViewVisibility(R.id.layout_link, View.GONE);
            rv.setViewVisibility(R.id.layout_text_only, View.GONE);
            rv.setViewVisibility(R.id.layout_photo, View.VISIBLE);
            rv.setViewVisibility(R.id.main_image, View.VISIBLE);
            rv.setImageViewBitmap(R.id.main_image, socialWidgetItem.mMainBitmap);
            break;
        default:
            rv.setViewVisibility(R.id.layout_text_only, View.VISIBLE);
            rv.setViewVisibility(R.id.layout_link, View.GONE);
            rv.setViewVisibility(R.id.layout_photo, View.VISIBLE);
            rv.setViewVisibility(R.id.main_image, View.GONE);
            rv.setViewVisibility(R.id.text_item_content, View.GONE);
            rv.setTextViewText(R.id.text_body_only, socialWidgetItem.getMessage());
            break;
        }
        return rv;
    }

    private static CharSequence setTextLinkBody(RemoteViews rv, SocialWidgetItem item) {
        String storyItemBody = item.body;
        String[] itemBody = storyItemBody.split(GlobalContstant.SUB_STRING_CODE);
        if (itemBody.length >= 1) {
            if (itemBody[0] != null && !itemBody[0].equals("")) {
                return itemBody[0];
            } else if (itemBody[1] != null && !itemBody[1].equals("")) {
                return itemBody[1];
            } else if (itemBody[2] != null && !itemBody[2].equals("")) {
                return itemBody[2];
            }
            else {
                return "";
            }
        }
        else {
            return "";
        }

    }
}

class ImageLoader extends AsyncTask<String, Void, Bitmap> {
    private SocialWidgetItem mSocialWidgetItem;
    private static final String TAG = GlobalContstant.PRE_TAG + "ImageLoader";

    public ImageLoader(Context context, SocialWidgetItem socialWidgetItem) {
        mSocialWidgetItem = socialWidgetItem;
    }

    @Override
    protected Bitmap doInBackground(java.lang.String... url) {
        return getImageBitmapInAsyncTask(url[0]);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        Log.d(TAG, "bitmap Reuslt " + bitmap);
        if (null != bitmap)
            mSocialWidgetItem.mMainBitmap = bitmap;
    }

    public Bitmap getImageBitmapInAsyncTask(String imageUrl) {
        if (imageUrl == null)
            return null;
        Log.d("fsdfdfj", "dfkdjd "+imageUrl );
        //                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        //                StrictMode.setThreadPolicy(policy);

        Bitmap imgBitmap = null;
        HttpURLConnection conn = null;
        BufferedInputStream bis = null;

        try {
            URL url = new URL(imageUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.connect();
            int nSize = conn.getContentLength();
            bis = new BufferedInputStream(conn.getInputStream(), nSize);
            imgBitmap = BitmapFactory.decodeStream(bis);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (conn != null)
                conn.disconnect();
        }
        return imgBitmap;
    }
}
