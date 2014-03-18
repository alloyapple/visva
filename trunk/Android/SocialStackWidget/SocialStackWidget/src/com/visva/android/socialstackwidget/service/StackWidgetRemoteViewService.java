package com.visva.android.socialstackwidget.service;

import java.util.ArrayList;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.visva.android.socialstackwidget.R;
import com.visva.android.socialstackwidget.constant.GlobalContstant;
import com.visva.android.socialstackwidget.database.SocialStacksDBProvider;
import com.visva.android.socialstackwidget.database.SocialWidgetItem;
import com.visva.android.socialstackwidget.util.VisvaLog;
import com.visva.android.socialstackwidget.widgetprovider.SocialWidgetMgr;

public class StackWidgetRemoteViewService extends RemoteViewsService {
    private static final String TAG = GlobalContstant.PRE_TAG + "StackWidgetRemoteViewService";
    private SocialStacksDBProvider mSocialStacksDBProvider;
    private ArrayList<SocialWidgetItem> mSocialWidgetItems = new ArrayList<SocialWidgetItem>();

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        VisvaLog.d(TAG, "onGetViewFactory");
        return new StackRemoteViewsFactory(this.getApplicationContext(), intent);
    }

    @Override
    public void onCreate() {
        VisvaLog.d("StackWidgetService", "onCreate");
        mSocialStacksDBProvider = new SocialStacksDBProvider(this);
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        VisvaLog.d(TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
        private static final String TAG = GlobalContstant.PRE_TAG + "StackRemoteViewsFactory";
        private Context mContext;
        private int mAppWidgetId;

        public StackRemoteViewsFactory(Context context, Intent intent) {
            mContext = context;
            mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        @Override
        public void onCreate() {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onDestroy() {
            mSocialWidgetItems.clear();
        }

        @Override
        public int getCount() {
            return mSocialWidgetItems.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            Log.d("WP", "GetView at " + mSocialWidgetItems.get(position).getAuthor_name());
            RemoteViews rv = SocialWidgetMgr.getRemoteView(mContext, mSocialWidgetItems.get(position));
            Bundle extras = new Bundle();
            extras.putString(GlobalContstant.EXTRA_ITEM_TYPE, mSocialWidgetItems.get(position).getSocial_category());
            extras.putString(GlobalContstant.EXTRA_ITEM_ID, mSocialWidgetItems.get(position).getSocialFeedId());
            Intent fillInIntent = new Intent();
            fillInIntent.putExtras(extras);
            rv.setOnClickFillInIntent(R.id.layout_widget_item, fillInIntent);

            try {
                System.out.println("Loading view " + position);
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return rv;
        }

        @Override
        public RemoteViews getLoadingView() {
            RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_loading);
            return rv;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public boolean hasStableIds() {
            return true;
        }

        @Override
        public void onDataSetChanged() {
            VisvaLog.d(TAG, "onDataSetChanged");
            mSocialWidgetItems = mSocialStacksDBProvider.getAllStoryItem(GlobalContstant.FACEBOOK);
            for (int i = 0; i < mSocialWidgetItems.size(); i++) {
                String authorImageUrl = mSocialWidgetItems.get(i).getAuthor_image();
                String mainImageUrl = getMainImage(mSocialWidgetItems.get(i).getImage_Url());
                SocialWidgetMgr.getAuthorImageRequest(mContext, authorImageUrl, false, mSocialWidgetItems.get(i));
                if (null != mainImageUrl && !"".equals(mainImageUrl))
                    SocialWidgetMgr.setMainImageRequest(mContext, mainImageUrl,false, mSocialWidgetItems.get(i));
                getViewAt(i);
            }
        }

        private String getMainImage(String image_Url) {
            image_Url = image_Url.replaceAll("%3A", "s");
            image_Url = image_Url.replaceAll("%2F", "/");
            image_Url = image_Url.replaceAll("%3F", "?");
            image_Url = image_Url.replaceAll("%25", "%");
            image_Url = image_Url.replaceAll("%20", " ");
            image_Url = image_Url.replaceAll("%26", "&");
            image_Url = image_Url.replaceAll("%3D", "=");
            return image_Url;
        }
    }
}