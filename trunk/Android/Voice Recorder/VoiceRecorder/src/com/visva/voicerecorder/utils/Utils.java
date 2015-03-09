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

import java.util.ArrayList;
import java.util.Calendar;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds;
import android.text.TextUtils;
import android.util.TypedValue;

import com.visva.voicerecorder.R;
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
        RecordingSession before = recordingSessions.get(position - 1);
        RecordingSession current = recordingSessions.get(position);
        int beforeDate, beforeYear, beforeMonth, currentDate, currentYear, currentMonth;
        beforeDate = Integer.parseInt(before.dateCreated.split("-")[0]);
        beforeMonth = Integer.parseInt(before.dateCreated.split("-")[1]);
        beforeYear = Integer.parseInt(before.dateCreated.split("-")[2]);
        currentDate = Integer.parseInt(current.dateCreated.split("-")[0]);
        currentMonth = Integer.parseInt(current.dateCreated.split("-")[1]);
        currentYear = Integer.parseInt(current.dateCreated.split("-")[2]);
        if (currentYear == beforeYear && currentMonth == beforeMonth && currentDate == beforeDate)
            return false;
        else
            return true;
    }

    public static String getTextDate(Context context, RecordingSession item) {
        String textDate = "";
        int currentDate, currentYear, currentMonth;
        currentDate = Integer.parseInt(item.dateCreated.split("-")[0]);
        currentMonth = Integer.parseInt(item.dateCreated.split("-")[1]);
        currentYear = Integer.parseInt(item.dateCreated.split("-")[2]);
        Calendar calendar = Calendar.getInstance();
        int date = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        if (currentYear == year && (currentMonth - 1 == month) && currentDate == date)
            textDate = context.getString(R.string.today);
        else if (currentYear == year && (currentMonth - 1 == month) && currentDate == (date - 1))
            textDate = context.getString(R.string.yesterday);
        else {
            textDate = currentDate + "/" + currentMonth + "/" + currentYear;
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
}
