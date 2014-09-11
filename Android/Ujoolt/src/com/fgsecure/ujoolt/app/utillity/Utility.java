package com.fgsecure.ujoolt.app.utillity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

import android.accounts.AccountManager;
import android.location.Location;
import android.os.Environment;
import android.os.StatFs;
import android.widget.EditText;

import com.google.android.maps.MapView;
import com.google.android.maps.Projection;

public class Utility {
	/*
	 * Modes of app MODE_DEFAUL : the screen default, when user see detail,
	 * setting, login, create jolt... MODE_SEARCH : when user want to search,
	 * after type keyword and press enter or search button the mode of app will
	 * switch to MODE_SEARCH, the user must click on GPS button or Jolt Creation
	 * button to back to MODE_DEFAULT MODE_JOLT_SOURCE : when user want to see
	 * radius of a jolt, he click on header of jolt info detail, the mode of app
	 * will switch to this mode, a header layout appear, to back to DEFAULT,
	 * user click on the layout
	 */
//	public static final byte MODE_DEFAULT = 0;
//	public static final byte MODE_SEARCH = 1;
//	public static final byte MODE_JOLT_SOURCE = 2;
	/*
	 * Types of getting jolts It define the appearing way of a jolt If it's
	 * CREATE, there will be a animation
	 */
	public static final byte CREATE = 0;
	public static final byte GROUP = 1;

	public static AccountManager mAccountManager;

	public static void copyStream(InputStream is, OutputStream os) {
		int buffer_size = 1024;
		try {
			buffer_size = is.available();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			if (buffer_size <= 0) {
				buffer_size = 1024;
			}
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static boolean checkEmptyEditText(EditText editText) {
		String s = editText.getText().toString();
		s = s.trim();
		return s.equalsIgnoreCase("");
	}

	public static long getAvailableStorage() {
		long result = 10000;
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
			result = (long) stat.getBlockSize() * (long) stat.getAvailableBlocks();
		} else {
			StatFs statFs = new StatFs(Environment.getDataDirectory().getAbsolutePath());
			result = (long) statFs.getAvailableBlocks() * (long) statFs.getBlockSize();
		}
		return result;
	}

	public static float getDistance(double slat, double slong, double dlat, double dlong) {
		Location locationA = new Location("point A");
		locationA.setLatitude(slat / 1E6);
		locationA.setLongitude(slong / 1E6);

		Location locationB = new Location("point B");
		locationB.setLatitude(dlat / 1E6);
		locationB.setLongitude(dlong / 1E6);

		float distance = locationA.distanceTo(locationB);
		return distance;
	}

	public static int convertMeterToPixels(MapView mapView, float meter) {
		Projection projection = mapView.getProjection();
		return (int) projection.metersToEquatorPixels(meter);
	}

	public static int getLongSpanFromMeter(double m, double lati) {
		double lKT = 400748d * Math.cos((double) lati / 1E6);
		return (int) ((m * (2 * Math.PI) / lKT) * 1E6);
	}

	public static int getLatSpanFromMeter(double m) {
		return (int) ((((double) ((Math.PI / 2) * m) / (double) 200374) * 1E6));
	}

	private static final Pattern EMAIL_ADDRESS_PATTERN = Pattern
			.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@"
					+ "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\."
					+ "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+");

	public static boolean checkMailValid(String email) {
		return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
	}

	public static String encodeMD5(String s) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException ex) {
		}
		md.update(s.getBytes());
		byte byteData[] = md.digest();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < byteData.length; i++) {
			sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
		}
		return s;
		// return sb.toString();
	}
}
