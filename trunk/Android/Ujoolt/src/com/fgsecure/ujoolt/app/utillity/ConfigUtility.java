package com.fgsecure.ujoolt.app.utillity;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.os.Build;
import android.util.DisplayMetrics;

import com.fgsecure.ujoolt.app.define.ScreenSize;

public class ConfigUtility {
	public static int scrWidth, scrHeight;
	public static ScreenSize screenSize;
	public static int R1, R2, R3, R4;

	public static int SDK_VERSION;
	public static String PHONE_MODEL;
	public static String ANDROID_VERSION;

	public static void getConfig(Activity activity) {
		DisplayMetrics metrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

		scrHeight = metrics.heightPixels;
		scrWidth = metrics.widthPixels;

		if (scrWidth <= 240) {
			screenSize = ScreenSize.W240;
			
		} else if (scrWidth <= 320) {
			screenSize = ScreenSize.W320;
			setRadiusOfJolt(65, 75, 95, 120);
			
		} else if (scrWidth <= 480) {
			screenSize = ScreenSize.W480;
			setRadiusOfJolt(150, 165, 185, 210);
			
		} else if (scrWidth <= 540) {
			screenSize = ScreenSize.W540;
			
		} else if (scrWidth <= 600) {
			screenSize = ScreenSize.W600;
			
		} else {
			screenSize = ScreenSize.W720;
			setRadiusOfJolt(200, 220, 250, 290);
		}

		PHONE_MODEL = android.os.Build.MODEL;
		ANDROID_VERSION = android.os.Build.VERSION.RELEASE;
		SDK_VERSION = Build.VERSION.SDK_INT;
	}

	private static void setRadiusOfJolt(int R1, int R2, int R3, int R4) {
		ConfigUtility.R1 = R1;
		ConfigUtility.R2 = R2;
		ConfigUtility.R3 = R3;
		ConfigUtility.R4 = R4;
	}

	public static long getCurTimeStamp() {
		long time = 0;

		Calendar c = Calendar.getInstance();
		Date date = c.getTime();

		Timestamp timestamp = getTimeStamp(date);
		String timeValue = timestamp.toString();

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date date1 = format.parse(timeValue);
			long t = date1.getTime();
			t = t / 1000;
			return t;

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return time;
	}

	public static Timestamp getTimeStamp(Date date) {
		java.sql.Timestamp timeStampDate = new Timestamp(date.getTime());
		return timeStampDate;
	}

}
