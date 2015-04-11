package com.visva.voicerecorder.receiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.Toast;

import com.visva.voicerecorder.MyCallRecorderApplication;
import com.visva.voicerecorder.R;
import com.visva.voicerecorder.constant.MyCallRecorderConstant;
import com.visva.voicerecorder.log.AIOLog;
import com.visva.voicerecorder.model.FavouriteItem;
import com.visva.voicerecorder.note.ActivityNoteEditor;
import com.visva.voicerecorder.utils.SQLiteHelper;

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
        AIOLog.e(MyCallRecorderConstant.TAG, "intent:" + intent.getAction());
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        notificationManager.cancel(MyCallRecorderConstant.NOTIFICATION_ID);

        //hide status bar after clicking notification action
        Intent closeStatusBarIntent = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        context.sendBroadcast(closeStatusBarIntent);

        String action = intent.getAction();
        Bundle bundle = intent.getExtras();
        if (bundle == null) {
            AIOLog.e(MyCallRecorderConstant.TAG, "bundle is null");
            return;
        }
        AIOLog.e(MyCallRecorderConstant.TAG, "bundle:" + bundle);
        if (MyCallRecorderConstant.FAVORITE_INTENT.equals(action)) {
            Resources res = context.getResources();
            mPhoneNo = bundle.getString(MyCallRecorderConstant.EXTRA_PHONE_NO);
            mPhoneName = bundle.getString(MyCallRecorderConstant.EXTRA_PHONE_NAME);
            FavouriteItem favouriteItem = new FavouriteItem(mPhoneNo, mPhoneName, 1, mContactId);
            SQLiteHelper sqLiteHelper = MyCallRecorderApplication.getInstance().getSQLiteHelper(context);
            sqLiteHelper.addNewFavoriteItem(favouriteItem);
            String phoneName = mPhoneName == null ? mPhoneName : mPhoneNo;
            String addFavouriteContact = res.getString(R.string.added_to_favourite, phoneName);
            Toast.makeText(context, addFavouriteContact, Toast.LENGTH_SHORT).show();
        } else if (MyCallRecorderConstant.MAKE_NOTE_INTENT.equals(action)) {
            Intent addNoteIntent = new Intent(context, ActivityNoteEditor.class);
            addNoteIntent.setAction(MyCallRecorderConstant.MAKE_NOTE_INTENT);
            bundle.putInt(MyCallRecorderConstant.EXTRA_STATE, MyCallRecorderConstant.STATE_INSERT);
            addNoteIntent.putExtras(bundle);
            addNoteIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(addNoteIntent);
        }
    }
}
