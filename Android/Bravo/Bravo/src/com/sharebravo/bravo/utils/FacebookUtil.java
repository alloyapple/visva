package com.sharebravo.bravo.utils;

import android.app.Activity;

import com.sharebravo.bravo.R;
import com.sharebravo.bravo.control.request.IRequestListener;
import com.sharebravo.bravo.sdk.util.VisvaDialog;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.entities.Feed;
import com.sromku.simple.fb.listeners.OnPublishListener;

public class FacebookUtil {

    private static Activity     mContext;
    private static FacebookUtil mInstance;
    private static VisvaDialog         mProgressDialog;

    public static FacebookUtil getInstance(Activity context) {
        if (mInstance == null) {
            mInstance = new FacebookUtil();
            mContext = context;
            mProgressDialog = new VisvaDialog(mContext, R.style.ProgressHUD);
        }
        return mInstance;
    }

    public void publishShareInBackground(String bravoId, String sharedText, final IRequestListener callback) {
        String message = sharedText;
        String name = mContext.getString(R.string.app_name);
        String bravoUrl = BravoWebServiceConfig.URL_BRAVO_ID_DETAIL.replace("{Bravo_ID}", bravoId);
        String pic = mContext.getString(R.string.picture_share_fb);
        final Feed feed = new Feed.Builder().setMessage(sharedText).setName(name).setCaption(message)
                .setDescription(message).setPicture(pic).setLink(bravoUrl).build();

        SimpleFacebook.getInstance().publish(feed, new OnPublishListener() {

            @Override
            public void onException(Throwable throwable) {
                if (mProgressDialog != null)
                {
                    try {
                        mProgressDialog.dismiss();
                        mProgressDialog = null;
                    } catch (Exception e) {

                    }
                }
                callback.onErrorResponse("");
            }

            @Override
            public void onFail(String reason) {
                if (mProgressDialog != null)
                {
                    try {
                        mProgressDialog.dismiss();
                        mProgressDialog = null;
                    } catch (Exception e) {

                    }
                }
                callback.onErrorResponse("");
            }

            @Override
            public void onThinking() {
                // Show waiting dialog during connection
               
                try {
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.show();
                } catch (Exception e) {
                    mProgressDialog = null;
                }
                //callback.onErrorResponse("");
            }

            @Override
            public void onComplete(String response) {
                if (mProgressDialog != null)
                {
                    try {
                        mProgressDialog.dismiss();
                        mProgressDialog = null;
                    } catch (Exception e) {

                    }
                }
                callback.onResponse(response);
            }
        });
    }
}
