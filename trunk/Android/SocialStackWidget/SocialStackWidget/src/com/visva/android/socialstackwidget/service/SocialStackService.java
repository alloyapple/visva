package com.visva.android.socialstackwidget.service;

import java.util.ArrayList;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import com.visva.android.socialstackwidget.constant.GlobalContstant;
import com.visva.android.socialstackwidget.database.SocialStacksDBProvider;
import com.visva.android.socialstackwidget.database.SocialWidgetItem;
import com.visva.android.socialstackwidget.object.facebookobject.FacebookFeedWrapperObject;
import com.visva.android.socialstackwidget.object.facebookobject.FacebookFeedsObject;
import com.visva.android.socialstackwidget.request.FacebookRequest;
import com.visva.android.socialstackwidget.request.ISocialResponse;
import com.visva.android.socialstackwidget.request.SocialErrorResponseBase;
import com.visva.android.socialstackwidget.request.SocialResponseBase;
import com.visva.android.socialstackwidget.util.StoryConverter;
import com.visva.android.socialstackwidget.util.VisvaLog;
import com.visva.android.socialstackwidget.widgetprovider.StackWidgetProvider;

public class SocialStackService extends Service {
    private static final String TAG = GlobalContstant.PRE_TAG + "SocialStackService";
    private SocialStacksDBProvider mSocialStacksDBProvider;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        VisvaLog.d(TAG, "SocialStackService onStartCommand");
        mSocialStacksDBProvider = new SocialStacksDBProvider(this);
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (GlobalContstant.FACEBOOK.equals(bundle.getString(GlobalContstant.EXTRA_SOCIAL_TYPE_REQUEST))) {
                String type = bundle.getString(GlobalContstant.EXTRA_SOCIAL_DETAIL_REQUEST);
                int limitItem = bundle.getInt(GlobalContstant.EXTRA_SOCIAL_LIMIT_ITEM);
                requestFacebookAPI(type, limitItem);
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void requestFacebookAPI(String type, int limitItem) {
        FacebookRequest.getFeedList(type, limitItem, new ISocialResponse() {

            @Override
            public void onResponse(SocialResponseBase data) {
                if (data instanceof FacebookFeedsObject) {
                    FacebookFeedsObject newsFeed = (FacebookFeedsObject) data;
                    ArrayList<FacebookFeedWrapperObject> tmpList = new ArrayList<FacebookFeedWrapperObject>();
                    tmpList.addAll(newsFeed.getData());
                    if(tmpList.size() > 0){
                        mSocialStacksDBProvider.deleteAllStoryItem();
                    }
                    VisvaLog.d(TAG, " onResponse Data size " + tmpList.size());
                    for (FacebookFeedWrapperObject feedItem : tmpList) {
                        String story = feedItem.story;
                        String type = feedItem.type;
                        if (type.compareTo("status") == 0 && story != null) {
                            newsFeed.getData().remove(feedItem); 
                            continue;
                        }
                        String picture = feedItem.getPicture();
                        if (picture != null && picture.contains("_s.")) {
                            int indexOfSize = picture.lastIndexOf("s");
                            feedItem.setPicture(picture.substring(0, indexOfSize) + "n" + picture.substring(indexOfSize + 1, picture.length()));
                        }
                        SocialWidgetItem stackWidgetItem = StoryConverter.convertFBFeedToDBStoryItemUnit(feedItem);
                        mSocialStacksDBProvider.addNewStoryItem(stackWidgetItem);
                    }
                    Intent intent = new Intent(SocialStackService.this,StackWidgetProvider.class);
                    intent.setAction(GlobalContstant.ACTION_UPDATE_DATA);
                    sendBroadcast(intent);
                }
            }

            @Override
            public void onErrorResponse(SocialErrorResponseBase error) {
                VisvaLog.d(TAG, "onErrorResponse");
            }
        });
    }
}
