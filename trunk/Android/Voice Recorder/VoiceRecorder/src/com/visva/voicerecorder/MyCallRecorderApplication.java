package com.visva.voicerecorder;

import java.util.ArrayList;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.visva.voicerecorder.model.FavouriteItem;
import com.visva.voicerecorder.record.RecordingManager;
import com.visva.voicerecorder.record.RecordingSession;
import com.visva.voicerecorder.utils.ContactsQuery;
import com.visva.voicerecorder.utils.MyCallRecorderSharePrefs;
import com.visva.voicerecorder.utils.ProgramHelper;
import com.visva.voicerecorder.utils.SQLiteHelper;
import com.visva.voicerecorder.utils.StringUtility;
import com.visva.voicerecorder.utils.Utils;
import com.visva.voicerecorder.view.activity.ActivityHome;

public class MyCallRecorderApplication extends Application {
    private static MyCallRecorderApplication mInstance;
    private static RecordingManager          mRecordingManager;
    private static SQLiteHelper              mSqLiteHelper;
    private static ProgramHelper             helper;
    private static ActivityHome              activity;
    private static MyCallRecorderSharePrefs  mMyCallRecorderSharePrefs;

    public static MyCallRecorderApplication getInstance() {
        if (mInstance == null) {
            mInstance = new MyCallRecorderApplication();
        }
        return mInstance;
    }

    public Context getAndroidContext() {
        return this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        checkFavouriteContactApplication();
    }

    private void checkFavouriteContactApplication() {
        mSqLiteHelper = getSQLiteHelper(getAndroidContext());
        ArrayList<FavouriteItem> favouriteItems = new ArrayList<FavouriteItem>();
        favouriteItems = mSqLiteHelper.getAllFavouriteItemFromContactApp();

        Uri contentUri = ContactsQuery.CONTENT_URI;

        // Returns a new CursorLoader for querying the Contacts table. No arguments are used
        // for the selection clause. The search string is either encoded onto the content URI,
        // or no contacts search string is used. The other search criteria are constants. See
        // the ContactsQuery interface.
        Cursor cursor = null;
        try {
            cursor = getContentResolver().query(contentUri, ContactsQuery.PROJECTION, "starred=?", new String[] { "1" },
                    ContactsQuery.SORT_ORDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (cursor == null)
            return;

        String phoneName, phoneNo = "";
        if (cursor.moveToFirst()) {
            do {
                String contactId = cursor.getString(ContactsQuery.ID);
                if (StringUtility.isEmpty(contactId))
                    continue;

                phoneName = cursor.getString(ContactsQuery.DISPLAY_NAME);

                ArrayList<String> phones = Utils.getContactUriTypeFromContactId(getContentResolver(), contactId);
                if (phones == null || phones.size() == 0) {
                    phoneNo = "0";
                }
                for (String phone : phones) {
                    if (!StringUtility.isEmpty(phone)) {
                        phoneNo = phone;
                        break;
                    }
                }
                // contact's ID to the Contacts table content Uri
                FavouriteItem item = new FavouriteItem();
                item.contactId = contactId;
                boolean isItemContain = false;
                for (FavouriteItem favouriteItem : favouriteItems) {
                    if (favouriteItem.contactId.equals(contactId)) {
                        isItemContain = true;
                        break;
                    }
                }
                if (!isItemContain) {
                    FavouriteItem favouriteItem = new FavouriteItem(phoneNo, phoneName, 2, contactId);
                    mSqLiteHelper.addNewRecordingSession(favouriteItem);
                }

            } while (cursor.moveToNext());
        }
        cursor.close();

        Log.d("KieuThang", "getNumberFavourite:" + mSqLiteHelper.countAllFavouriteItem());

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public ProgramHelper getProgramHelper() {
        if (helper == null) {
            helper = new ProgramHelper();
        }
        helper.prepare();
        return helper;
    }

    public RecordingManager getRecordManager(Context context, ArrayList<RecordingSession> sessions) {
        if (mRecordingManager == null) {
            mRecordingManager = new RecordingManager(context, sessions);
        }
        return mRecordingManager;
    }

    public void stopActivity() {
        if (activity == null)
            return;
        activity.finish();
    }

    public SQLiteHelper getSQLiteHelper(Context context) {
        if (mSqLiteHelper == null) {
            mSqLiteHelper = new SQLiteHelper(context);
        }
        return mSqLiteHelper;
    }

    public void setActivity(ActivityHome activity) {
        MyCallRecorderApplication.activity = activity;
    }

    public MyCallRecorderSharePrefs getMyCallRecorderSharePref() {
        if (mMyCallRecorderSharePrefs == null) {
            mMyCallRecorderSharePrefs = MyCallRecorderSharePrefs.getInstance(getAndroidContext());
        }
        return mMyCallRecorderSharePrefs;
    }
}
