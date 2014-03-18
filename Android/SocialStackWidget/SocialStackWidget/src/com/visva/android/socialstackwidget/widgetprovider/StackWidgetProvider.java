package com.visva.android.socialstackwidget.widgetprovider;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.facebook.Session;
import com.visva.android.socialstackwidget.R;
import com.visva.android.socialstackwidget.constant.GlobalContstant;
import com.visva.android.socialstackwidget.service.StackWidgetRemoteViewService;
import com.visva.android.socialstackwidget.util.VisvaLog;

public class StackWidgetProvider extends AppWidgetProvider {
    private static final String TAG = GlobalContstant.PRE_TAG + "StackWidgetProvider";
    private AlarmManager mAlarmManager;
    private PendingIntent mPendingIntent;
    private boolean isAutoUpdateEnable = true;

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        if (isAutoUpdateEnable)
            startAutoRefreshAlarm(context);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        VisvaLog.d(TAG, " onReceive=" + intent.getAction());
        String action = intent.getAction();
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        if (intent.getAction().equals(GlobalContstant.ACTION_CLICK_DETAIL_ITEM)) {
            String socialCategory = intent.getStringExtra(GlobalContstant.EXTRA_ITEM_TYPE);
            String socialID = intent.getStringExtra(GlobalContstant.EXTRA_ITEM_ID);
            Toast.makeText(context, "Touched view " + socialCategory + " socialID " + socialID, Toast.LENGTH_SHORT).show();
            if (GlobalContstant.FACEBOOK.equals(socialCategory)) {
                Intent fbIntent = getOpenFacebookIntent(context, socialID);
                fbIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(fbIntent);
            }
        } else if (GlobalContstant.ACTION_UPDATE_DATA.equals(action)) {
            int[] ids = appWidgetManager.getAppWidgetIds(new ComponentName(context, StackWidgetProvider.class));
            appWidgetManager.notifyAppWidgetViewDataChanged(ids, R.layout.widget_layout);
            this.onUpdate(context, appWidgetManager, ids);
            //            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            //            ComponentName component = new ComponentName(context, StackWidgetProvider.class);
            //            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, StackWidgetProvider.class));
            //            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.stack_view);
            //            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);

        } else if (GlobalContstant.ACTION_AUTO_UPDATE.equals(action)) {
            Intent autoUpdateintent = new Intent(GlobalContstant.ACTION_REQUEST);
            autoUpdateintent.putExtra(GlobalContstant.EXTRA_SOCIAL_TYPE_REQUEST, GlobalContstant.FACEBOOK);
            autoUpdateintent.putExtra(GlobalContstant.EXTRA_SOCIAL_DETAIL_REQUEST, "Feed");
            context.sendBroadcast(autoUpdateintent);
        }
        super.onReceive(context, intent);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        VisvaLog.d(TAG, "onUpdate");
        AppWidgetManager widgetMgr = AppWidgetManager.getInstance(context);
        Session session = Session.getActiveSession();
        boolean enableButtons = (session != null && session.isOpened());
        VisvaLog.d(TAG, "enableButtons " + enableButtons + " appWidgetIds " + appWidgetIds.length);
        if (enableButtons) {
            for (int i = 0; i < appWidgetIds.length; ++i) {

                Intent intent = new Intent(context, StackWidgetRemoteViewService.class);
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
                intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
                RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
                remoteViews.setViewVisibility(R.id.btn_refresh, View.VISIBLE);
                remoteViews.setViewVisibility(R.id.progressBar_refresh, View.GONE);
                remoteViews.setRemoteAdapter(appWidgetIds[i], R.id.stack_view, intent);

                Intent toastIntent = new Intent(context, StackWidgetProvider.class);
                toastIntent.setAction(GlobalContstant.ACTION_CLICK_DETAIL_ITEM);
                toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
                intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
                PendingIntent toastPendingIntent = PendingIntent.getBroadcast(context, 0, toastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                remoteViews.setPendingIntentTemplate(R.id.stack_view, toastPendingIntent);
                remoteViews.setPendingIntentTemplate(R.id.btn_refresh, setPendingIntent(context, /*widgetId,*/R.id.btn_refresh, remoteViews));
                remoteViews.setOnClickPendingIntent(R.id.btn_setting, setPendingIntent(context, /*widgetId,*/R.id.btn_setting, remoteViews));
                //                remoteViews.setOnClickPendingIntent(R.id.btn_refresh, setPendingIntent(context, /*widgetId,*/R.id.btn_refresh,remoteViews));
                synchronized (this) {
                    widgetMgr.notifyAppWidgetViewDataChanged(appWidgetIds[i], R.id.stack_view);
                    widgetMgr.updateAppWidget(appWidgetIds[i], remoteViews);
                }
            }
        } else {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout_empty);
            remoteViews.setOnClickPendingIntent(R.id.empty_view, setPendingIntent(context, /*widgetId,*/R.id.empty_view, remoteViews));
            widgetMgr.updateAppWidget(appWidgetIds, remoteViews);
        }

    }

    private PendingIntent setPendingIntent(Context context, int resId, RemoteViews remoteViews) {
        Intent intent = new Intent();
        switch (resId) {
        case R.id.empty_view:
        case R.id.btn_setting:
            intent.setAction(GlobalContstant.ACTION_SETTINGS);
            break;
        case R.id.btn_refresh:
            VisvaLog.d(TAG, "onClickRefreshButton");
            //            remoteViews.setViewVisibility(R.id.btn_refresh, View.GONE);
            //            remoteViews.setViewVisibility(R.id.progressBar_refresh, View.VISIBLE);
            intent.setAction(GlobalContstant.ACTION_REFRESH);
            break;
        default:
            break;
        }
        return PendingIntent.getBroadcast(context, resId, intent, 0);
    }

    private Intent getOpenFacebookIntent(Context context, String postId) {
        VisvaLog.d(TAG, "id " + postId);
        try {
            VisvaLog.d(TAG, "getOpenFacebookIntent " + postId);
            context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
            return new Intent(Intent.ACTION_VIEW, Uri.parse("fb://post/" + postId));
        } catch (Exception e) {
            VisvaLog.d(TAG, "getOpenFacebookIntent Exception" + postId);
            String id[] = postId.split("_");
            String authorid = "";
            String contentId = "";
            if (id.length > 0)
                authorid = id[0];
            if (id.length > 1)
                contentId = id[1];
            return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/" + authorid + "/posts/" + contentId));
        }
    }

    private synchronized void startAutoRefreshAlarm(Context pContext) {
        VisvaLog.d(TAG, "startAutoRefreshAlarm");
        mAlarmManager = (AlarmManager) pContext.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(GlobalContstant.ACTION_AUTO_UPDATE);
        intent.setClass(pContext, getClass());
        mPendingIntent = PendingIntent.getBroadcast(pContext, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        mAlarmManager.setRepeating(AlarmManager.RTC, 60000, 60000, mPendingIntent);
    }

    private void cancelPendingTask() {
        if (mAlarmManager != null && mPendingIntent != null)
            mAlarmManager.cancel(mPendingIntent);
    }
}