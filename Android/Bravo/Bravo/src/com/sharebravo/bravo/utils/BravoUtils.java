package com.sharebravo.bravo.utils;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sharebravo.bravo.model.SessionLogin;
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
            break;
        case BravoConstant.REGISTER_BY_TWITTER:
            BravoSharePrefs.getInstance(context).putStringValue(BravoConstant.PREF_KEY_SESSION_REGISTER_BY_TWITTER, response);
            break;
        case BravoConstant.REGISTER_BY_4SQUARE:
            BravoSharePrefs.getInstance(context).putStringValue(BravoConstant.PREF_KEY_SESSION_REGISTER_BY_4SQUARE, response);
            break;
        case BravoConstant.REGISTER_BY_BRAVO_ACC:
            BravoSharePrefs.getInstance(context).putStringValue(BravoConstant.PREF_KEY_SESSION_REGISTER_BY_BRAVO, response);
            break;
        case BravoConstant.LOGIN_BY_4SQUARE:
            BravoSharePrefs.getInstance(context).putStringValue(BravoConstant.PREF_KEY_SESSION_LOGIN_BY_4SQUARE, response);
            break;
        case BravoConstant.LOGIN_BY_BRAVO_ACC:
            BravoSharePrefs.getInstance(context).putStringValue(BravoConstant.PREF_KEY_SESSION_LOGIN_BY_BRAVO, response);
            break;
        case BravoConstant.LOGIN_BY_FACEBOOK:
            BravoSharePrefs.getInstance(context).putStringValue(BravoConstant.PREF_KEY_SESSION_LOGIN_BY_FACEBOOK, response);
            break;
        case BravoConstant.LOGIN_BY_TWITTER:
            BravoSharePrefs.getInstance(context).putStringValue(BravoConstant.PREF_KEY_SESSION_LOGIN_BY_TWITTER, response);
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
}
