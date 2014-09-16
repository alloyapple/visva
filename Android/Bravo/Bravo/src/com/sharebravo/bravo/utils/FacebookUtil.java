package com.sharebravo.bravo.utils;

import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Session;
import com.facebook.Session.NewPermissionsRequest;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;
import com.sharebravo.bravo.R;
import com.sharebravo.bravo.sdk.log.AIOLog;

public class FacebookUtil {

    private static Activity     mContext;
    private static FacebookUtil mInstance;

    public static FacebookUtil getInstance(Activity context) {
        if (mInstance == null) {
            mInstance = new FacebookUtil();
            mContext = context;
        }
        return mInstance;
    }

    public void publishShareInBackground(String sharedText) {
        AIOLog.d("sharedText" + sharedText);
        String message = sharedText;
        String name = mContext.getString(R.string.app_name);
        String link = BravoWebServiceConfig.URL_BRAVO_HOME_PAGE;
        String pic = mContext.getString(R.string.picture_share_fb);
        final Bundle _postParameter = new Bundle();
        _postParameter.putString("name", name);
        _postParameter.putString("link", link);
        _postParameter.putString("picture", pic);
        _postParameter.putString("description", message);

        final List<String> PERMISSIONS = Arrays.asList("publish_stream");

        if (Session.getActiveSession() != null) {
            List<String> sessionPermission = Session.getActiveSession() .getPermissions();
            if (!sessionPermission.containsAll(PERMISSIONS)) {
                NewPermissionsRequest reauthRequest = new Session.NewPermissionsRequest(mContext, PERMISSIONS);
                Session.getActiveSession().requestNewPublishPermissions(reauthRequest);
            }
        }

        Request request = new Request(Session.getActiveSession(), "feed", _postParameter, HttpMethod.POST);
        RequestAsyncTask task = new RequestAsyncTask(request);
        task.execute();
    }

    public void publishLikeProductInBackground(String userName) {
        // String message = mContext.getString(R.string.fb_like_product, userName, merchProductItem.getProductName());
        // String name = mContext.getString(R.string.fb_share_name);
        // String link = mContext.getString(R.string.fb_link);
        // String pic = mContext.getString(R.string.fb_picture);
        final Bundle _postParameter = new Bundle();
        _postParameter.putString("name", "");
        _postParameter.putString("link", "");
        _postParameter.putString("picture", "");
        _postParameter.putString("description", "");

        final List<String> PERMISSIONS = Arrays.asList("publish_stream");

        if (Session.getActiveSession() != null) {
            List<String> sessionPermission = Session.getActiveSession()
                    .getPermissions();
            if (!sessionPermission.containsAll(PERMISSIONS)) {
                NewPermissionsRequest reauthRequest = new Session.NewPermissionsRequest(
                        mContext, PERMISSIONS);

                Session.getActiveSession().requestNewPublishPermissions(
                        reauthRequest);
            }
        }
        Request request = new Request(Session.getActiveSession(), "feed",
                _postParameter, HttpMethod.POST);

        RequestAsyncTask task = new RequestAsyncTask(request);
        task.execute();
    }

    public void publishShareDialog() {
        Session session = Session.getActiveSession();
        if (session != null && session.isOpened()) {
            Bundle params = new Bundle();
            params.putString("name", "Shoppie");
            params.putString("caption", "");
            params.putString("description", "text here");
            params.putString("link", "");
            params.putString("picture", "");
            // http://farm6.staticflickr.com/5480/10948560363_bf15322277_m.jpg
            WebDialog feedDialog = (new WebDialog.FeedDialogBuilder(mContext,
                    Session.getActiveSession(), params)).setOnCompleteListener(
                    new OnCompleteListener() {

                        @Override
                        public void onComplete(Bundle values, FacebookException error) {
                            if (error == null) {
                                Toast.makeText(mContext, "Share successfully ", Toast.LENGTH_SHORT).show();
                            } else if (error instanceof FacebookOperationCanceledException) {
                                Toast.makeText(mContext, "Publish cancelled", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(mContext, "Error posting story", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).build();
            feedDialog.show();
        }
    }

    public void publishFeedDialog(String custId) {
        Bundle params = new Bundle();
        params.putString("name", "");
        params.putString("caption", "");
        params.putString("description", "");
        params.putString("link", "http://www.shoppie.com.vn/");
        params.putString("picture", "");
        params.putString("to", "");
        WebDialog feedDialog = (new WebDialog.FeedDialogBuilder(mContext,
                Session.getActiveSession(), params)).setOnCompleteListener(
                new OnCompleteListener() {

                    @Override
                    public void onComplete(Bundle values,
                            FacebookException error) {
                        if (error == null) {
                            // When the story is posted, echo the success
                            // and the post Id.
                            final String name = "";
                            Toast.makeText(mContext, "Invited " + name,
                                    Toast.LENGTH_SHORT).show();
                        } else if (error instanceof FacebookOperationCanceledException) {
                            // User clicked the "x" button
                            Toast.makeText(mContext, "Publish cancelled",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // Generic, ex: network error
                            Toast.makeText(mContext, "Error posting story",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }).build();
        feedDialog.show();

    }
}
