package com.visva.android.socialstackwidget.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.visva.android.socialstackwidget.activity.SocialSettingActivity;
import com.visva.android.socialstackwidget.constant.GlobalContstant;
import com.visva.android.socialstackwidget.service.SocialStackService;

public class SocialStackReceiver extends BroadcastReceiver {
    private static final String TAG = GlobalContstant.PRE_TAG + "SocialStackReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive intent= " + intent.getAction());
        String action = intent.getAction();
        if (GlobalContstant.ACTION_SETTINGS.equals(action)) {
            Intent loginIntent = new Intent(context, SocialSettingActivity.class);
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(loginIntent);
        } else if (GlobalContstant.ACTION_REQUEST.equals(action) || GlobalContstant.ACTION_REFRESH.equals(action)) {
            Intent requestIntent = new Intent(context, SocialStackService.class);
            requestIntent.putExtra(GlobalContstant.EXTRA_SOCIAL_TYPE_REQUEST, GlobalContstant.FACEBOOK);
            requestIntent.putExtra(GlobalContstant.EXTRA_SOCIAL_DETAIL_REQUEST, "Feed");
            requestIntent.putExtra(GlobalContstant.EXTRA_SOCIAL_LIMIT_ITEM, 25);
            context.startService(requestIntent);
        }
    }
}
