package com.sharebravo.bravo.utils;

import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sharebravo.bravo.model.SessionLogin;
import com.sharebravo.bravo.model.response.ObBravo;
import com.sharebravo.bravo.model.user.ObGetLoginedUser;
import com.sharebravo.bravo.model.user.ObPostUserSuccess;
import com.sharebravo.bravo.sdk.log.AIOLog;

public class BravoUtils {

    public static String getUserIdFromUserBravoInfo(Context context, String shareKeyPre, int loginType) {
        Gson gson = new GsonBuilder().serializeNulls().create();
        String userId = null;
        if (loginType < BravoConstant.LOGIN_BY_FACEBOOK) {
            ObGetLoginedUser obGetLoginedUser = gson.fromJson(shareKeyPre.toString(), ObGetLoginedUser.class);
            if (obGetLoginedUser == null)
                return "";
            userId = obGetLoginedUser.data.get(0).User_ID;
        } else {
            ObPostUserSuccess obPostUserSuccess = gson.fromJson(shareKeyPre.toString(), ObPostUserSuccess.class);
            if (obPostUserSuccess == null)
                return "";
            userId = obPostUserSuccess.data.User_ID;
        }

        return userId;
    }

    public static String getAccessTokenFromUserBravoInfo(Context context, String shareKeyPre, int loginType) {
        Gson gson = new GsonBuilder().serializeNulls().create();
        String accessToken = null;
        if (loginType < BravoConstant.LOGIN_BY_FACEBOOK) {
            ObGetLoginedUser obGetLoginedUser = gson.fromJson(shareKeyPre.toString(), ObGetLoginedUser.class);
            if (obGetLoginedUser == null)
                return "";
            accessToken = obGetLoginedUser.data.get(0).Access_Token;
        } else {
            ObPostUserSuccess obPostUserSuccess = gson.fromJson(shareKeyPre.toString(), ObPostUserSuccess.class);
            if (obPostUserSuccess == null)
                return "";
            accessToken = obPostUserSuccess.data.Access_Token;
        }
        return accessToken;
    }

    /**
     * save data to share preference to check type of login/register to bravo server
     * 
     * @param mRegisterType
     * @param response
     */
    public static void saveResponseToSharePreferences(Context context, int mRegisterType, String response) {
        BravoSharePrefs.getInstance(context).putIntValue(BravoConstant.PREF_KEY_SESSION_LOGIN_BRAVO_VIA_TYPE, mRegisterType);

        switch (mRegisterType) {
        case BravoConstant.REGISTER_BY_FACEBOOK:
            BravoSharePrefs.getInstance(context).putStringValue(BravoConstant.PREF_KEY_SESSION_REGISTER_BY_FACEBOOK, response);
            BravoSharePrefs.getInstance(context).putBooleanValue(BravoConstant.PREF_KEY_POST_ON_FACEBOOK, true);
            break;
        case BravoConstant.REGISTER_BY_TWITTER:
            BravoSharePrefs.getInstance(context).putStringValue(BravoConstant.PREF_KEY_SESSION_REGISTER_BY_TWITTER, response);
            BravoSharePrefs.getInstance(context).putBooleanValue(BravoConstant.PREF_KEY_POST_ON_TWITTER, true);
            break;
        case BravoConstant.REGISTER_BY_4SQUARE:
            BravoSharePrefs.getInstance(context).putStringValue(BravoConstant.PREF_KEY_SESSION_REGISTER_BY_4SQUARE, response);
            BravoSharePrefs.getInstance(context).putBooleanValue(BravoConstant.PREF_KEY_POST_ON_FOURSQUARE, true);
            break;
        case BravoConstant.REGISTER_BY_BRAVO_ACC:
            BravoSharePrefs.getInstance(context).putStringValue(BravoConstant.PREF_KEY_SESSION_REGISTER_BY_BRAVO, response);
            break;
        case BravoConstant.LOGIN_BY_4SQUARE:
            BravoSharePrefs.getInstance(context).putStringValue(BravoConstant.PREF_KEY_SESSION_LOGIN_BY_4SQUARE, response);
            BravoSharePrefs.getInstance(context).putBooleanValue(BravoConstant.PREF_KEY_POST_ON_FOURSQUARE, true);
            break;
        case BravoConstant.LOGIN_BY_BRAVO_ACC:
            BravoSharePrefs.getInstance(context).putStringValue(BravoConstant.PREF_KEY_SESSION_LOGIN_BY_BRAVO, response);
            break;
        case BravoConstant.LOGIN_BY_FACEBOOK:
            BravoSharePrefs.getInstance(context).putStringValue(BravoConstant.PREF_KEY_SESSION_LOGIN_BY_FACEBOOK, response);
            BravoSharePrefs.getInstance(context).putBooleanValue(BravoConstant.PREF_KEY_POST_ON_FACEBOOK, true);
            break;
        case BravoConstant.LOGIN_BY_TWITTER:
            BravoSharePrefs.getInstance(context).putStringValue(BravoConstant.PREF_KEY_SESSION_LOGIN_BY_TWITTER, response);
            BravoSharePrefs.getInstance(context).putBooleanValue(BravoConstant.PREF_KEY_POST_ON_TWITTER, true);
            break;
        default:
            break;
        }
    }

    /**
     * get session after each time login or register
     * 
     * @param context
     * @param mLoginBravoViaType
     */
    public static SessionLogin getSession(Context context, int mLoginBravoViaType) {
        String _userId = null;
        String _accessToken = null;
        String _preKeySessionRegisteredLoginBravo = null;
        switch (mLoginBravoViaType) {
        case BravoConstant.LOGIN_BY_BRAVO_ACC:
            _preKeySessionRegisteredLoginBravo = BravoSharePrefs.getInstance(context).getStringValue(
                    BravoConstant.PREF_KEY_SESSION_LOGIN_BY_BRAVO);
            break;
        case BravoConstant.LOGIN_BY_4SQUARE:
            _preKeySessionRegisteredLoginBravo = BravoSharePrefs.getInstance(context).getStringValue(
                    BravoConstant.PREF_KEY_SESSION_LOGIN_BY_4SQUARE);
            break;
        case BravoConstant.LOGIN_BY_FACEBOOK:
            _preKeySessionRegisteredLoginBravo = BravoSharePrefs.getInstance(context).getStringValue(
                    BravoConstant.PREF_KEY_SESSION_LOGIN_BY_FACEBOOK);
            break;
        case BravoConstant.LOGIN_BY_TWITTER:
            _preKeySessionRegisteredLoginBravo = BravoSharePrefs.getInstance(context).getStringValue(
                    BravoConstant.PREF_KEY_SESSION_LOGIN_BY_TWITTER);
            break;
        case BravoConstant.REGISTER_BY_4SQUARE:
            _preKeySessionRegisteredLoginBravo = BravoSharePrefs.getInstance(context).getStringValue(
                    BravoConstant.PREF_KEY_SESSION_REGISTER_BY_4SQUARE);
            break;
        case BravoConstant.REGISTER_BY_BRAVO_ACC:
            _preKeySessionRegisteredLoginBravo = BravoSharePrefs.getInstance(context).getStringValue(
                    BravoConstant.PREF_KEY_SESSION_REGISTER_BY_BRAVO);
            break;
        case BravoConstant.REGISTER_BY_FACEBOOK:
            _preKeySessionRegisteredLoginBravo = BravoSharePrefs.getInstance(context).getStringValue(
                    BravoConstant.PREF_KEY_SESSION_REGISTER_BY_FACEBOOK);
            break;
        case BravoConstant.REGISTER_BY_TWITTER:
            _preKeySessionRegisteredLoginBravo = BravoSharePrefs.getInstance(context).getStringValue(
                    BravoConstant.PREF_KEY_SESSION_REGISTER_BY_TWITTER);
            break;

        default:
            break;
        }

        if (!StringUtility.isEmpty(_preKeySessionRegisteredLoginBravo)) {
            _userId = getUserIdFromUserBravoInfo(context, _preKeySessionRegisteredLoginBravo, mLoginBravoViaType);
            _accessToken = getAccessTokenFromUserBravoInfo(context, _preKeySessionRegisteredLoginBravo, mLoginBravoViaType);
        }
        AIOLog.d("mUserId:" + _userId + ", mAccessToken:" + _accessToken + ", mLoginBravoViaType:" + mLoginBravoViaType);
        SessionLogin sessionLogin = new SessionLogin();
        sessionLogin.accessToken = _accessToken;
        sessionLogin.aPNSTockens = "";
        sessionLogin.userID = _userId;
        return sessionLogin;
    }

    /**
     * rotate image to have the exact orientation
     * 
     * @param fileUri
     * @return
     */
    public static int checkOrientation(Uri fileUri) {
        int rotate = 0;
        String imagePath = fileUri.getPath();
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(imagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Since API Level 5
        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
        case ExifInterface.ORIENTATION_ROTATE_270:
            rotate = 270;
            break;
        case ExifInterface.ORIENTATION_ROTATE_180:
            rotate = 180;
            break;
        case ExifInterface.ORIENTATION_ROTATE_90:
            rotate = 90;
            break;
        }
        return rotate;
    }

    public static Bitmap decodeSampledBitmapFromFile(String filePath, int reqWidth, int reqHeight, int orientation) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Matrix mtx = new Matrix();
        mtx.postRotate(orientation);
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return decodeBitmap(BitmapFactory.decodeFile(filePath, options), orientation);
    }

    private static Bitmap decodeBitmap(Bitmap bitmap, int orientation) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix mtx = new Matrix();
        mtx.postRotate(orientation);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, mtx, true);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    /**
     * clear session to delete user profile data
     * 
     * @param context
     */
    public static void clearSession(Context context) {
        BravoSharePrefs.getInstance(context).putIntValue(BravoConstant.PREF_KEY_SESSION_LOGIN_BRAVO_VIA_TYPE, BravoConstant.NO_LOGIN_SNS);

        BravoSharePrefs.getInstance(context).putStringValue(BravoConstant.PREF_KEY_SESSION_REGISTER_BY_FACEBOOK, "");
        BravoSharePrefs.getInstance(context).putStringValue(BravoConstant.PREF_KEY_SESSION_REGISTER_BY_TWITTER, "");
        BravoSharePrefs.getInstance(context).putStringValue(BravoConstant.PREF_KEY_SESSION_REGISTER_BY_4SQUARE, "");
        BravoSharePrefs.getInstance(context).putStringValue(BravoConstant.PREF_KEY_SESSION_REGISTER_BY_BRAVO, "");
        BravoSharePrefs.getInstance(context).putStringValue(BravoConstant.PREF_KEY_SESSION_LOGIN_BY_4SQUARE, "");
        BravoSharePrefs.getInstance(context).putStringValue(BravoConstant.PREF_KEY_SESSION_LOGIN_BY_BRAVO, "");
        BravoSharePrefs.getInstance(context).putStringValue(BravoConstant.PREF_KEY_SESSION_LOGIN_BY_FACEBOOK, "");
        BravoSharePrefs.getInstance(context).putStringValue(BravoConstant.PREF_KEY_SESSION_LOGIN_BY_TWITTER, "");

        BravoSharePrefs.getInstance(context).putStringValue(BravoConstant.PREF_KEY_TWITTER_OAUTH_TOKEN, "");
        BravoSharePrefs.getInstance(context).putStringValue(BravoConstant.PREF_KEY_TWITTER_OAUTH_SCRET, "");

        BravoSharePrefs.getInstance(context).putBooleanValue(BravoConstant.PREF_KEY_BRAVO_FISRT_TIME, false);
        BravoSharePrefs.getInstance(context).putBooleanValue(BravoConstant.PREF_KEY_TWITTER_LOGIN, false);
    }

    /**
     * save data to share preference to check type of login/register to bravo server
     * 
     * @param mRegisterType
     * @param response
     */
    public static void saveUserProfileToSharePreferences(Context context, int mRegisterType, String response) {
        switch (mRegisterType) {
        case BravoConstant.REGISTER_BY_FACEBOOK:
            BravoSharePrefs.getInstance(context).putStringValue(BravoConstant.PREF_KEY_SESSION_USER_INFO_VIA_FACEBOOK_REGISTER, response);
            break;
        case BravoConstant.REGISTER_BY_TWITTER:
            BravoSharePrefs.getInstance(context).putStringValue(BravoConstant.PREF_KEY_SESSION_USER_INFO_VIA_TWITTER_REGISTER, response);
            break;
        case BravoConstant.REGISTER_BY_4SQUARE:
            BravoSharePrefs.getInstance(context).putStringValue(BravoConstant.PREF_KEY_SESSION_USER_INFO_VIA_FOURSQUARE_REGISTER, response);
            break;
        case BravoConstant.REGISTER_BY_BRAVO_ACC:
            BravoSharePrefs.getInstance(context).putStringValue(BravoConstant.PREF_KEY_SESSION_USER_INFO_VIA_BRAVO_REGISTER, response);
            break;
        case BravoConstant.LOGIN_BY_4SQUARE:
            BravoSharePrefs.getInstance(context).putStringValue(BravoConstant.PREF_KEY_SESSION_USER_INFO_VIA_FOURSQUARE_LOGIN, response);
            break;
        case BravoConstant.LOGIN_BY_BRAVO_ACC:
            BravoSharePrefs.getInstance(context).putStringValue(BravoConstant.PREF_KEY_SESSION_USER_INFO_VIA_BRAVO_LOGIN, response);
            break;
        case BravoConstant.LOGIN_BY_FACEBOOK:
            BravoSharePrefs.getInstance(context).putStringValue(BravoConstant.PREF_KEY_SESSION_USER_INFO_VIA_FACEBOOK_LOGIN, response);
            break;
        case BravoConstant.LOGIN_BY_TWITTER:
            BravoSharePrefs.getInstance(context).putStringValue(BravoConstant.PREF_KEY_SESSION_USER_INFO_VIA_TWITTER_LOGIN, response);
            break;
        default:
            break;
        }
    }

    /**
     * save data to share preference to check type of login/register to bravo server
     * 
     * @param mRegisterType
     * @param response
     */
    public static String getUserProfileInfo(Context context, int mRegisterType) {
        String userProfileJsonStr;
        switch (mRegisterType) {
        case BravoConstant.REGISTER_BY_FACEBOOK:
            userProfileJsonStr = BravoSharePrefs.getInstance(context).getStringValue(BravoConstant.PREF_KEY_SESSION_USER_INFO_VIA_FACEBOOK_REGISTER);
            break;
        case BravoConstant.REGISTER_BY_TWITTER:
            userProfileJsonStr = BravoSharePrefs.getInstance(context).getStringValue(BravoConstant.PREF_KEY_SESSION_USER_INFO_VIA_TWITTER_REGISTER);
            break;
        case BravoConstant.REGISTER_BY_4SQUARE:
            userProfileJsonStr = BravoSharePrefs.getInstance(context)
                    .getStringValue(BravoConstant.PREF_KEY_SESSION_USER_INFO_VIA_FOURSQUARE_REGISTER);
            break;
        case BravoConstant.REGISTER_BY_BRAVO_ACC:
            userProfileJsonStr = BravoSharePrefs.getInstance(context).getStringValue(BravoConstant.PREF_KEY_SESSION_USER_INFO_VIA_BRAVO_REGISTER);
            break;
        case BravoConstant.LOGIN_BY_4SQUARE:
            userProfileJsonStr = BravoSharePrefs.getInstance(context).getStringValue(BravoConstant.PREF_KEY_SESSION_USER_INFO_VIA_FOURSQUARE_LOGIN);
            break;
        case BravoConstant.LOGIN_BY_BRAVO_ACC:
            userProfileJsonStr = BravoSharePrefs.getInstance(context).getStringValue(BravoConstant.PREF_KEY_SESSION_USER_INFO_VIA_BRAVO_LOGIN);
            break;
        case BravoConstant.LOGIN_BY_FACEBOOK:
            userProfileJsonStr = BravoSharePrefs.getInstance(context).getStringValue(BravoConstant.PREF_KEY_SESSION_USER_INFO_VIA_FACEBOOK_LOGIN);
            break;
        case BravoConstant.LOGIN_BY_TWITTER:
            userProfileJsonStr = BravoSharePrefs.getInstance(context).getStringValue(BravoConstant.PREF_KEY_SESSION_USER_INFO_VIA_TWITTER_LOGIN);
            break;
        default:
            userProfileJsonStr = "";
            break;
        }
        return userProfileJsonStr;
    }

    public static ArrayList<ObBravo> removeIncorrectBravoItems(ArrayList<ObBravo> bravoItems) {
        ArrayList<ObBravo> obBravos = new ArrayList<ObBravo>();
        for (ObBravo obBravo : bravoItems) {
            if (StringUtility.isEmpty(obBravo.User_ID) || (StringUtility.isEmpty(obBravo.Full_Name) || "0".equals(obBravo.User_ID))) {
                AIOLog.e("The incorrect bravo items:" + obBravo.User_ID + ", obBravo.Full_Name:" + obBravo.Full_Name);
            } else
                obBravos.add(obBravo);
        }
        return obBravos;
    }
}
