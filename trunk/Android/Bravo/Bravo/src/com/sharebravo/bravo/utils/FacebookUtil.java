package com.sharebravo.bravo.utils;

import android.app.Activity;

import com.sharebravo.bravo.R;
import com.sharebravo.bravo.control.request.IRequestListener;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.entities.Feed;
import com.sromku.simple.fb.listeners.OnPublishListener;

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
               callback.onErrorResponse("");
            }

            @Override
            public void onFail(String reason) {
                callback.onErrorResponse("");
            }

            @Override
            public void onThinking() {
                callback.onErrorResponse("");
            }

            @Override
            public void onComplete(String response) {
                callback.onResponse(response);
            }
        });
        
//        AIOLog.d("sharedText" + sharedText);
//        String message = sharedText;
//        String name = mContext.getString(R.string.app_name);
//        String bravoUrl = BravoWebServiceConfig.URL_BRAVO_ID_DETAIL.replace("{Bravo_ID}", bravoId);
//        String pic = mContext.getString(R.string.picture_share_fb);
//        final Bundle _postParameter = new Bundle();
//        _postParameter.putString("name", name);
//        _postParameter.putString("link", bravoUrl);
//        _postParameter.putString("picture", pic);
//        _postParameter.putString("description", message);
//
//        final List<String> PERMISSIONS = Arrays.asList("publish_stream");
//
//        if (Session.getActiveSession() != null) {
//            List<String> sessionPermission = Session.getActiveSession().getPermissions();
//            if (!sessionPermission.containsAll(PERMISSIONS)) {
//                NewPermissionsRequest reauthRequest = new Session.NewPermissionsRequest(mContext, PERMISSIONS);
//                Session.getActiveSession().requestNewPublishPermissions(reauthRequest);
//            }
//        }
//
//        Request request = new Request(Session.getActiveSession(), "feed", _postParameter, HttpMethod.POST);
//        request.setCallback(new Callback() {
//
//            @Override
//            public void onCompleted(Response response) {
//                callback.onCompleted(response);
//                AIOLog.d("response:" + response);
//            }
//        });
//        RequestAsyncTask task = new RequestAsyncTask(request);
//        task.execute();
    }
}
