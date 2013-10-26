package vn.com.shoppie.activity;

import java.util.ArrayList;
import java.util.List;

import vn.com.shoppie.R;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Base64;

public class SettingPreference extends PreferenceActivity implements OnSharedPreferenceChangeListener {
	public static boolean PRINT_STACK_TRACE = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.pref);
		PreferenceManager.setDefaultValues(this, R.xml.pref, false);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences arg0, String arg1) {

	}

	// example
	public static final boolean KEY_SHOW_DESCRIPTION_DEF = true;
	public static final String KEY_SHOW_DESCRIPTION = "show_description";

	public static boolean getShowDescription(Context context) {
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
		return sharedPref.getBoolean(SettingPreference.KEY_SHOW_DESCRIPTION, KEY_SHOW_DESCRIPTION_DEF);
	}

	public static void setShowDescription(Context context, boolean value) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		Editor editor = prefs.edit();
		editor.putBoolean(SettingPreference.KEY_SHOW_DESCRIPTION, value);
		editor.commit();
	}

	// user-id
	public static final int VALUE_USER_ID_DEF = -1;
	public static final String KEY_USER_ID = "user-id";

	public static int getUserID(Context context) {
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
		return sharedPref.getInt(KEY_USER_ID, VALUE_USER_ID_DEF);
	}

	public static void setUserID(Context context, int value) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		Editor editor = prefs.edit();
		editor.putInt(SettingPreference.KEY_USER_ID, value);
		editor.commit();
	}

	// user-name
	public static final String VALUE_USER_NAME_DEF = "";
	public static final String KEY_USER_NAME = "user-name";

	public static String getUserName(Context context) {
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
		String name64 = sharedPref.getString(KEY_USER_NAME, VALUE_USER_NAME_DEF);
		String name = new String(Base64.decode(name64.getBytes(), Base64.DEFAULT));
		return name;

	}

	public static void setUserName(Context context, String name) {
		try {
			// TODO NullpointerException
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
			Editor editor = prefs.edit();
			String name64 = new String(Base64.encode(name.getBytes(), Base64.DEFAULT));
			editor.putString(KEY_USER_NAME, name64);
			editor.commit();
		} catch (NullPointerException e) {
		}
	}

	// user-picture
	public static final String VALUE_USER_PICTURE_DEF = "";
	public static final String KEY_USER_PICTURE = "user-picture";

	public static String getUserPicture(Context context) {
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
		String pic64 = sharedPref.getString(VALUE_USER_PICTURE_DEF, KEY_USER_PICTURE);
		String pic = new String(Base64.decode(pic64.getBytes(), Base64.DEFAULT));
		return pic;
	}

	public static void setUserPicture(Context context, String url) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		Editor editor = prefs.edit();
		String pic64 = new String(Base64.encode(url.getBytes(), Base64.DEFAULT));
		editor.putString(KEY_USER_PICTURE, pic64);
		editor.commit();
	}

	// user-id-facebook
	public static final String VALUE_USER_ID_FACEBOOK_DEF = "";
	public static final String KEY_USER_ID_FACEBOOK = "user-id-facebook";

	public static String getUserIdFaceBook(Context context) {
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
		String name64 = sharedPref.getString(KEY_USER_ID_FACEBOOK, VALUE_USER_ID_FACEBOOK_DEF);
		String name = new String(Base64.decode(name64.getBytes(), Base64.DEFAULT));
		return name;

	}

	public static void setUserIdFaceBook(Context context, String name) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		Editor editor = prefs.edit();
		String name64 = new String(Base64.encode(name.getBytes(), Base64.DEFAULT));
		editor.putString(KEY_USER_ID_FACEBOOK, name64);
		editor.commit();
	}

	// user-email
	public static final String VALUE_USER_EMAIL_DEF = "";
	public static final String KEY_USER_EMAIL = "user-email";

	public static String getUserEmail(Context context) {
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
		return sharedPref.getString(KEY_USER_EMAIL, VALUE_USER_EMAIL_DEF);
	}

	public static void setUserEmail(Context context, String email) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		Editor editor = prefs.edit();
		editor.putString(KEY_USER_EMAIL, email);
		editor.commit();
	}

	// user-gender
	public static final String VALUE_USER_GENDER_DEF = "";
	public static final String KEY_USER_GENDER = "user-gender";

	public static String getUserGender(Context context) {
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
		return sharedPref.getString(KEY_USER_GENDER, VALUE_USER_GENDER_DEF);
	}

	public static void setUserGender(Context context, String gender) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		Editor editor = prefs.edit();
		editor.putString(KEY_USER_GENDER, gender);
		editor.commit();
	}

	// user-phone
	public static final String VALUE_USER_PHONE_DEF = "";
	public static final String KEY_USER_PHONE = "user-phone";

	public static String getUserPhone(Context context) {
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
		return sharedPref.getString(KEY_USER_PHONE, VALUE_USER_PHONE_DEF);
	}

	public static void setUserPhone(Context context, String phone) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		Editor editor = prefs.edit();
		editor.putString(KEY_USER_PHONE, phone);
		editor.commit();
	}

	// user-address
	public static final String VALUE_USER_ADDRESS_DEF = "";
	public static final String KEY_USER_ADDRESS = "user-address";

	public static String getUserAddress(Context context) {
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
		return sharedPref.getString(KEY_USER_ADDRESS, VALUE_USER_ADDRESS_DEF);
	}

	public static void setUserAddress(Context context, String address) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		Editor editor = prefs.edit();
		editor.putString(KEY_USER_ADDRESS, address);
		editor.commit();
	}
	
	// user-birthday
		public static final String VALUE_USER_BIRTHDAY_DEF = "";
		public static final String KEY_USER_BIRTHDAY = "user-birthday";

		public static String getUserBirthday(Context context) {
			SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
			return sharedPref.getString(KEY_USER_BIRTHDAY, VALUE_USER_BIRTHDAY_DEF);
		}

		public static void setUserBirthday(Context context, String birthday) {
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
			Editor editor = prefs.edit();
			editor.putString(KEY_USER_BIRTHDAY, birthday);
			editor.commit();
		}

	// user-deviceToken
	public static final String VALUE_USER_DEVICE_TOKEN_DEF = "";
	public static final String KEY_USER_DEVICE_TOKEN_ID = "user-device-token";

	public static String getUserDeviceToken(Context context) {
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
		return sharedPref.getString(KEY_USER_DEVICE_TOKEN_ID, VALUE_USER_DEVICE_TOKEN_DEF);
	}

	public static void setUserDeviceToken(Context context, String value) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		Editor editor = prefs.edit();
		editor.putString(SettingPreference.KEY_USER_DEVICE_TOKEN_ID, value);
		editor.commit();
	}

	// user-custom code
	public static final String VALUE_USER_CODE_DEF = "";
	public static final String KEY_USER_CODE_ID = "user-code";

	public static String getUserCode(Context context) {
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
		return sharedPref.getString(KEY_USER_CODE_ID, VALUE_USER_CODE_DEF);
	}

	public static void setUserCode(Context context, String value) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		Editor editor = prefs.edit();
		editor.putString(SettingPreference.KEY_USER_CODE_ID, value);
		editor.commit();
	}

	// first-use
	public static final boolean VALUE_FIRST_USE_DEF = true;
	public static final String KEY_FIST_USE = "first-use";

	public static boolean getFirstUse(Context context) {
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
		return sharedPref.getBoolean(KEY_FIST_USE, VALUE_FIRST_USE_DEF);
	}

	public static void setFirstUse(Context context, boolean value) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		Editor editor = prefs.edit();
		editor.putBoolean(SettingPreference.KEY_FIST_USE, value);
		editor.commit();
	}
	
	// first-use
		public static final long VALUE_LAST_DEL_DEF = 0L;
		public static final String KEY_LAST_DEL = "last-delete";

		public static long getTimeLastDel(Context context) {
			SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
			return sharedPref.getLong(KEY_LAST_DEL, VALUE_LAST_DEL_DEF);
		}

		public static void setTimeLastDel(Context context, long value) {
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
			Editor editor = prefs.edit();
			editor.putLong(SettingPreference.KEY_LAST_DEL, value);
			editor.commit();
		}

	// memory: 1-default/ 2: high / 0: low memory
	public static final int VALUE_MEM_DEF = 2;
	public static final String KEY_MEM = "high-memory";

	public static int getMem(Context context) {
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
		return sharedPref.getInt(KEY_MEM, VALUE_MEM_DEF);
	}
	
	public static void setMem(Context context, int value) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		Editor editor = prefs.edit();
		editor.putInt(SettingPreference.KEY_MEM, value);
		editor.commit();
	}

	// first-use
	public static final String KEY_BACK_STACK = "backstack";

	public static void writeList(Context context, List<String> list, String prefix) {
		SharedPreferences prefs = context.getSharedPreferences(KEY_BACK_STACK, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();

		int size = prefs.getInt(prefix + "_size", 0);

		// clear the previous data if exists
		for (int i = 0; i < size; i++)
			editor.remove(prefix + "_" + i);

		// write the current list
		for (int i = 0; i < list.size(); i++)
			editor.putString(prefix + "_" + i, list.get(i));

		editor.putInt(prefix + "_size", list.size());
		editor.commit();
	}

	public static List<String> readList(Context context, String prefix) {
		SharedPreferences prefs = context.getSharedPreferences(KEY_BACK_STACK, Context.MODE_PRIVATE);

		int size = prefs.getInt(prefix + "_size", 0);

		List<String> data = new ArrayList<String>(size);
		for (int i = 0; i < size; i++)
			data.add(prefs.getString(prefix + "_" + i, null));

		return data;
	}
}
