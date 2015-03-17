package com.visva.voicerecorder.receiver;

import com.visva.voicerecorder.MyCallRecorderApplication;
import com.visva.voicerecorder.R;
import com.visva.voicerecorder.constant.MyCallRecorderConstant;
import com.visva.voicerecorder.log.AIOLog;
import com.visva.voicerecorder.model.FavouriteItem;
import com.visva.voicerecorder.utils.SQLiteHelper;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.Toast;

public class MyRecordCallReceiver extends BroadcastReceiver {
    private String mPhoneNo;
    private String mPhoneName;
    private String mContactId;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null || intent.getAction() == null) {
            AIOLog.e(MyCallRecorderConstant.TAG, "intent or action is null");
            return;
        }
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        notificationManager.cancel(MyCallRecorderConstant.NOTIFICATION_ID);
        String action = intent.getAction();
        Bundle bundle = intent.getExtras();
        if (bundle == null) {
            AIOLog.e(MyCallRecorderConstant.TAG, "bundle is null");
            return;
        }
        Resources res = context.getResources();
        if (MyCallRecorderConstant.FAVORITE_INTENT.equals(action)) {
            mPhoneNo = bundle.getString("phone_no");
            mPhoneName = bundle.getString("phone_name");
            FavouriteItem favouriteItem = new FavouriteItem(mPhoneNo, mPhoneName, 1, mContactId);
            SQLiteHelper sqLiteHelper = MyCallRecorderApplication.getInstance().getSQLiteHelper(context);
            sqLiteHelper.addNewFavoriteItem(favouriteItem);
            String phoneName = mPhoneName == null ? mPhoneName : mPhoneNo;
            String addFavouriteContact = res.getString(R.string.added_to_favourite, phoneName);
            Toast.makeText(context, addFavouriteContact, Toast.LENGTH_SHORT).show();
        } else if (MyCallRecorderConstant.MAKE_NOTE_INTENT.equals(action)) {

        }
    }

}
