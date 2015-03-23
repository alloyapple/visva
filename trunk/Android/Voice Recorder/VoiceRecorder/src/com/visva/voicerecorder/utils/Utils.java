/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.visva.voicerecorder.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.text.TextUtils;
import android.util.TypedValue;

import com.ringdroid.soundfile.CheapSoundFile;
import com.visva.voicerecorder.MyCallRecorderApplication;
import com.visva.voicerecorder.R;
import com.visva.voicerecorder.constant.MyCallRecorderConstant;
import com.visva.voicerecorder.model.FavouriteItem;
import com.visva.voicerecorder.record.RecordingSession;
import com.visva.voicerecorder.view.activity.ActivityHome;

/**
 * This class contains static utility methods.
 */
public class Utils {
    public static String REPLACE_NON_DIGITS = "[^0-9]";

    // Prevents instantiation.
    private Utils() {
    }

    /**
     * Enables strict mode. This should only be called when debugging the application and is useful
     * for finding some potential bugs or best practice violations.
     */
    @TargetApi(11)
    public static void enableStrictMode() {
        // Strict mode is only available on gingerbread or later
        if (Utils.hasGingerbread()) {

            // Enable all thread strict mode policies
            StrictMode.ThreadPolicy.Builder threadPolicyBuilder = new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog();

            // Enable all VM strict mode policies
            StrictMode.VmPolicy.Builder vmPolicyBuilder = new StrictMode.VmPolicy.Builder().detectAll().penaltyLog();

            // Honeycomb introduced some additional strict mode features
            if (Utils.hasHoneycomb()) {
                // Flash screen when thread policy is violated
                threadPolicyBuilder.penaltyFlashScreen();
                // For each activity class, set an instance limit of 1. Any more instances and
                // there could be a memory leak.
                vmPolicyBuilder.setClassInstanceLimit(ActivityHome.class, 1).setClassInstanceLimit(ActivityHome.class, 1);
            }

            // Use builders to enable strict mode policies
            StrictMode.setThreadPolicy(threadPolicyBuilder.build());
            StrictMode.setVmPolicy(vmPolicyBuilder.build());
        }
    }

    /**
     * Uses static final constants to detect if the device's platform version is Gingerbread or
     * later.
     */
    public static boolean hasGingerbread() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }

    /**
     * Uses static final constants to detect if the device's platform version is Honeycomb or
     * later.
     */
    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    /**
     * Uses static final constants to detect if the device's platform version is Honeycomb MR1 or
     * later.
     */
    public static boolean hasHoneycombMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
    }

    /**
     * Uses static final constants to detect if the device's platform version is ICS or
     * later.
     */
    public static boolean hasICS() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    }

    public static int dp2px(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    public static Uri getContactUriTypeFromPhoneNumber(ContentResolver resolver, String phoneNo, int index) {
        Uri result = null;
        if (phoneNo == "" || "null".equals(phoneNo)) {
            phoneNo = "111111111";
        }
        String[] projection = { ContactsContract.Contacts._ID, ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.PhoneLookup.NUMBER,
                ContactsContract.PhoneLookup.PHOTO_URI, ContactsContract.PhoneLookup.LOOKUP_KEY };
        Uri lookupUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNo));
        Cursor cursor = resolver.query(lookupUri, projection, null, null, null);
        if (cursor == null)
            return null;
        if (cursor.moveToFirst()) {
            if (cursor.getString(index) != null)
                result = Uri.parse(cursor.getString(index));
        }
        if (cursor != null) {
            cursor.close();
            cursor = null;
        }
        return result;
    }

    public static ArrayList<String> getContactUriTypeFromContactId(ContentResolver resolver, String contactId) {
        ArrayList<String> phones = new ArrayList<String>();

        Cursor cursor = resolver.query(
                CommonDataKinds.Phone.CONTENT_URI,
                null,
                CommonDataKinds.Phone.CONTACT_ID + " = ?",
                new String[] { contactId }, null);

        while (cursor.moveToNext())
        {
            phones.add(cursor.getString(cursor.getColumnIndex(CommonDataKinds.Phone.NUMBER)));
        }

        cursor.close();
        return (phones);
    }

    public static boolean isShowTextDate(int position, ArrayList<RecordingSession> recordingSessions) {
        if (recordingSessions == null || recordingSessions.size() == 0)
            return false;
        if (position == 0 || recordingSessions.size() == 1)
            return true;
        int beforeDate, beforeYear, beforeMonth, currentDate, currentYear, currentMonth;
        RecordingSession before = recordingSessions.get(position - 1);
        RecordingSession current = recordingSessions.get(position);
        Calendar calendar = Calendar.getInstance();
        long beforeTime = Long.valueOf(before.dateCreated);
        calendar.setTimeInMillis(beforeTime);
        beforeDate = calendar.get(Calendar.DAY_OF_MONTH);
        beforeMonth = calendar.get(Calendar.MONTH);
        beforeYear = calendar.get(Calendar.YEAR);

        long afterTime = Long.valueOf(current.dateCreated);
        calendar.setTimeInMillis(afterTime);
        currentDate = calendar.get(Calendar.DAY_OF_MONTH);
        currentMonth = calendar.get(Calendar.MONTH);
        currentYear = calendar.get(Calendar.YEAR);

        if (currentYear == beforeYear && currentMonth == beforeMonth && currentDate == beforeDate)
            return false;
        else
            return true;
    }

    public static String getTextDate(Context context, long createdDate) {
        String textDate = "";
        Calendar calendar = Calendar.getInstance();
        int currentDate, currentYear, currentMonth;
        currentDate = calendar.get(Calendar.DAY_OF_MONTH);
        currentYear = calendar.get(Calendar.YEAR);
        currentMonth = calendar.get(Calendar.MONTH);

        calendar.setTimeInMillis(createdDate);
        int date = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        if (currentYear == year && (currentMonth == month) && currentDate == date)
            textDate = context.getString(R.string.today);
        else if (currentYear == year && (currentMonth == month) && currentDate == (date - 1))
            textDate = context.getString(R.string.yesterday);
        else {
            textDate = currentDate + "/" + (currentMonth + 1) + "/" + currentYear;
        }
        return textDate;
    }

    public static boolean deleteContact(Context ctx, Uri contactUri) {
        try {
            ctx.getContentResolver().delete(contactUri, null, null);
            return true;
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
        return false;
    }

    public static boolean isSamePhoneNo(Context context, String number1, String number2) {
        if (TextUtils.isEmpty(number1) || TextUtils.isEmpty(number2)) {
            return false;
        }
        number1 = number1.replaceAll(REPLACE_NON_DIGITS, "");
        number2 = number2.replaceAll(REPLACE_NON_DIGITS, "");

        String longer_no = (number1.length() >= number2.length()) ? number1 : number2;
        String shorter_no = (number1.length() < number2.length()) ? number1 : number2;
        int diff = 1;
        for (diff = 1; diff <= shorter_no.length(); diff++) {
            if (number1.charAt(number1.length() - diff) != number2.charAt(number2.length() - diff)) {
                break;
            }
        }

        // extract zip code
        String zip_code_1 = longer_no.substring(0, longer_no.length() - diff + 1);
        String zip_code_2 = shorter_no.substring(0, shorter_no.length() - diff + 1);
        zip_code_1 = zip_code_1.startsWith("+") ? zip_code_1.substring(1) : zip_code_1;
        zip_code_2 = zip_code_2.startsWith("+") ? zip_code_2.substring(1) : zip_code_2;

        if (TextUtils.isEmpty(zip_code_1) && TextUtils.isEmpty(zip_code_2)) {
            return number1.equalsIgnoreCase(number2);
        }
        else if (TextUtils.isEmpty(zip_code_2) || "0".equalsIgnoreCase(zip_code_2)) {
            return isValidCountryZipCode(context, zip_code_1);
        }
        else {
            return isValidCountryZipCode(context, zip_code_1) && isValidCountryZipCode(context, zip_code_2);
        }
    }

    public static boolean isValidCountryZipCode(Context context, String zipcode) {
        String[] zip_list = context.getResources().getStringArray(R.array.CountryCodes);
        for (String zip : zip_list) {
            if (zip.startsWith(zipcode)) {
                return true;
            }
        }
        return false;
    }

    public static String getTextTime(Context context, long createdTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(createdTime);
        String timeString = calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
        return timeString;
    }

    public static String getDurationTime(Context mContext, RecordingSession recordingSession) {
        MediaPlayer mediaPlayer = new MediaPlayer();
        String durationTime = "";
        long duration = 0L;
        try {
            mediaPlayer.setDataSource(recordingSession.fileName);
            mediaPlayer.prepare();
            duration = mediaPlayer.getDuration();
            durationTime = "" + TimeUtility.milliSecondsToTimer(duration);
            mediaPlayer.reset();
            mediaPlayer.release();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (duration == 0)
            return null;
        return durationTime;
    }

    public static long getDuration(CheapSoundFile cheapSoundFile) {
        if (cheapSoundFile == null)
            return -1;
        int sampleRate = cheapSoundFile.getSampleRate();
        int samplesPerFrame = cheapSoundFile.getSamplesPerFrame();
        int frames = cheapSoundFile.getNumFrames();
        cheapSoundFile = null;
        return 1000 * (frames * samplesPerFrame) / sampleRate;
    }

    public static String getDurationTime(RecordingSession recordingSession) {
        String durationTime = "";
        if (recordingSession.fileName != null && recordingSession.fileName.length() > 0)
            try {
                long totalDuration = getDuration(CheapSoundFile.create(recordingSession.fileName, null));
                durationTime = "" + TimeUtility.milliSecondsToTimer(totalDuration);
            } catch (FileNotFoundException e) {
            } catch (IOException e) {
            }
        return durationTime;
    }

    public static boolean isCheckFavouriteContact(FragmentActivity activity, String contactId) {
        SQLiteHelper sqLiteHelper = MyCallRecorderApplication.getInstance().getSQLiteHelper(activity);
        FavouriteItem favouriteItem = sqLiteHelper.getFavouriteItem(contactId);
        if (favouriteItem == null)
            return false;
        else
            return true;
    }

    // This method is used to checking the valid duration time from the last call
    // The valid duration time is at least equal or more than 2 seconds for the length of a call.
    // After checking, this method also delete the file which is not valid with the duration time.
    public static boolean isCheckValidDurationTime() {
        String dataFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MyCallRecorder/data";
        File dataFile = new File(dataFilePath);
        String lastFileName = "";
        String line = null;
        long durationTime = 0L;
        ArrayList<String> lines = new ArrayList<String>();
        String finalStringToWriteOut = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(dataFile));
            while ((line = br.readLine()) != null) {
                lastFileName = line.split(";")[0];
                lines.add(line);
            }
            br.close();
            for (int i = 0; i < lines.size() - 1; i++) {
                finalStringToWriteOut += lines.get(i) + "\n";
            }
            FileWriter fileWriter = new FileWriter(dataFile, false);
            BufferedWriter out = new BufferedWriter(fileWriter);
            out.append(finalStringToWriteOut);
            out.close();
            try {
                durationTime = getDuration(CheapSoundFile.create(lastFileName, null));
            } catch (FileNotFoundException e) {
            } catch (IOException e) {
            }
        } catch (IOException e) {

        }
        // The valid duration time is at least equal or more than 2 seconds for the length of a call.
        if (durationTime > 2000) {
            return true;
        } else {
            File mediaFile = new File(lastFileName);
            mediaFile.delete();
            MyCallRecorderApplication.getInstance().stopActivity();
            return false;
        }
    }

    public static int isCheckFavouriteContactByPhoneNo(Context context, String phoneNo) {
        SQLiteHelper sqLiteHelper = MyCallRecorderApplication.getInstance().getSQLiteHelper(context);
        FavouriteItem favouriteItem = sqLiteHelper.getFavouriteItemFromPhoneNo(phoneNo);
        if (favouriteItem == null || favouriteItem.isFavourite == 0)
            return 0;
        else
            return favouriteItem.isFavourite;
    }

    public static void showNotificationAfterCalling(Context context, String phoneName, String phoneNo, String createdDate) {
        Resources res = context.getResources();
        String newRecord = res.getString(R.string.you_have_new_record);
        String favorite = res.getString(R.string.favourite);
        String addNote = res.getString(R.string.add_note);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context).setSmallIcon(R.drawable.ic_launcher)
                .setAutoCancel(true).setContentTitle(phoneName).setContentText(newRecord);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(alarmSound);

        int isFavourite = Utils.isCheckFavouriteContactByPhoneNo(context, phoneNo);
        Bundle bundle = new Bundle();
        bundle.putString(MyCallRecorderConstant.EXTRA_PHONE_NAME, phoneName);
        bundle.putString(MyCallRecorderConstant.EXTRA_PHONE_NO, phoneNo);
        bundle.putString(MyCallRecorderConstant.EXTRA_CREATED_DATE, createdDate);
        if (isFavourite == 0) {
            //favorite intent
            Intent favoriteIntent = new Intent();
            favoriteIntent.setAction(MyCallRecorderConstant.FAVORITE_INTENT);
            favoriteIntent.putExtras(bundle);
            PendingIntent pendingFavoriteIntent = PendingIntent.getBroadcast(context, MyCallRecorderConstant.NOTIFICATION_ID, favoriteIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            builder.addAction(R.drawable.ic_favourites, favorite, pendingFavoriteIntent);
        }

        //Make a note intent
        Intent makeNoteIntent = new Intent();
        makeNoteIntent.setAction(MyCallRecorderConstant.MAKE_NOTE_INTENT);
        makeNoteIntent.putExtras(bundle);
        PendingIntent pendingMakeNoteIntent = PendingIntent.getBroadcast(context, MyCallRecorderConstant.NOTIFICATION_ID, makeNoteIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.addAction(R.drawable.ic_customer_create, addNote, pendingMakeNoteIntent);

        Intent resultIntent = new Intent(context, ActivityHome.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(ActivityHome.class);
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(MyCallRecorderConstant.NOTIFICATION_ID, builder.build());
    }
}
