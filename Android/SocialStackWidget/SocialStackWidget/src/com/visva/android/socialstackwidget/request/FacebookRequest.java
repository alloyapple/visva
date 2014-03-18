package com.visva.android.socialstackwidget.request;

import org.json.JSONObject;

import android.os.Bundle;
import android.util.Log;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.visva.android.socialstackwidget.constant.GlobalContstant;
import com.visva.android.socialstackwidget.object.facebookobject.FacebookFeedsObject;

public class FacebookRequest {
    private static final String TAG = GlobalContstant.PRE_TAG + FacebookRequest.class.getName();

    public static void getFeedList(String type, int limitItem,final ISocialResponse iSocialReponse) {
        Log.d(TAG, "getFeedList");
        Session activeSession = Session.getActiveSession();
        String graphPath = GlobalContstant.FACEBOOK_GET_MYNEWSFEED;
        if (activeSession != null && activeSession.getState().isOpened()) {
            Bundle bundle = new Bundle();
            bundle.putInt("limit", limitItem);
            Request feedRequest = Request.newGraphPathRequest(activeSession, graphPath, new Request.Callback() {

                @Override
                public void onCompleted(Response response) {
                    if (response.getError() != null) {
                        iSocialReponse.onErrorResponse(null);
                    } else {
                        Log.d(TAG, "getFeedList()->onCompleted() Successfully");
                        GraphObject graphObject = response.getGraphObject();
                        JSONObject jsonObject = graphObject.getInnerJSONObject();
                        Gson gson = new GsonBuilder().serializeNulls().create();
                        FacebookFeedsObject newsFeed = gson.fromJson(jsonObject.toString(), FacebookFeedsObject.class);
                        iSocialReponse.onResponse(newsFeed);
                    }
                }
            });

            feedRequest.setParameters(bundle);
            feedRequest.executeAsync();
        }
    }
}
