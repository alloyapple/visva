package com.vvmaster.android.lazybird;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
//import android.util.Log;
//import android.util.Log;

import java.util.ArrayList;

/**
 * Created by VV-MasteR team. Copyright 2012
 */
public class PageData {
	public String mPageId;
	public String[] mStrings;
	public int[] mStringXOffsets;
	public int[] mStringYOffsets;
	public ArrayList<Integer> mTimeStamps;
	public int mBitmapResId;
	public float mFontSize;
	public Typeface mTypeface;
	public int mFontColor;
	public int mFrameXOffset;
	public int mFrameYOffset;
	public int mBackToMenu_marginLeft;
	public int mBackToMenu_marginBottom;

	// data array with touch and text appear
	public ArrayList<Rect[]> mRectTouch;
	public Rect[] mRectText;
	public Rect[] mRectBoxText;
	public String[] mStringText;
	public int[] mBitmapAnimationId;
	public Rect[] mRectAnimation;
	public ArrayList<Integer> mOrderAnimation;
	public ArrayList<Integer> mTimeAnimation;
	public Typeface mTypefaceTextTouch;
	public int idSound[];
	public boolean mIsZoom = true;
	public int mPivotZoomX = 0;
	public int mPivotZoomY = 0;

	public static final int TAP_SOUND = 0;
	public static final int PAGE1_TAP_BIRD_WITH_FISH = 1;
	public static final int PAGE2_TAP_ANY_BIRD = 2;
	public static final int PAGE6_TAP_A_LAUGHING_BIRD = 3;
	public static final int PAGE8_TAP_ANY_BIRD = 4;
	public static final int PAGE10_TAP_LB = 5;
	public static final int PAGE11_TAP_AN_AIRPLANE = 6;
	public static final int PAGE16_TAP_LB = 7;
	public static final int PAGE19_TAP_LB = 8;
	public static final int PAGE22_TAP_LB = 9;
	public static final int PAGE26_TAP_BIRD_GLASSES = 10;
	public static final int PAGE26_TAP_RIGHT_BIRD_OPEN = 11;

	public PageData(Context context, int id) {
		mPageId = getPageIdForIndex(id);

		Resources r = context.getResources();
		final String packageName = context.getPackageName();

		mBitmapResId = r.getIdentifier("drawable/bg_" + mPageId, null,
				packageName);

		int strResId = r.getIdentifier("str_" + mPageId, "array", packageName);
		mStrings = r.getStringArray(strResId);

		strResId = r.getIdentifier("timestamps_" + mPageId, "string",
				packageName);
		String timestamps = r.getString(strResId);
		String[] values = timestamps.split(" ");
		mTimeStamps = new ArrayList<Integer>(values.length);
		for (int i = 0; i < values.length; ++i) {
			mTimeStamps.add((int) (Double.valueOf(values[i]) * 1000.f));
		}

		int xOffsetId = r.getIdentifier("x_" + mPageId, "array", packageName);
		int yOffsetId = r.getIdentifier("y_" + mPageId, "array", packageName);

		mStringXOffsets = r.getIntArray(xOffsetId);
		mStringYOffsets = r.getIntArray(yOffsetId);

		assert mStringXOffsets.length == mStringYOffsets.length;
		assert mStringXOffsets.length == mStrings.length;

		int fontId = r.getIdentifier("fontsize_" + mPageId, "dimen",
				packageName);
		mFontSize = r.getDimension(fontId);

		xOffsetId = r.getIdentifier("frame_x_" + mPageId, "integer",
				packageName);
		yOffsetId = r.getIdentifier("frame_y_" + mPageId, "integer",
				packageName);
		mFrameXOffset = r.getInteger(xOffsetId);
		// hack there -
		if (r.getDisplayMetrics().densityDpi != DisplayMetrics.DENSITY_HIGH)
			mFrameXOffset -= 27;
		mFrameYOffset = r.getInteger(yOffsetId);

		int colorId = r.getIdentifier("text_color_" + mPageId, "color",
				packageName);
		mFontColor = r.getColor(colorId);

		fontId = r.getIdentifier("font_name_" + mPageId, "string", packageName);
		String fontName;
		if (fontId > 0) {
			fontName = r.getString(fontId);
		} else {
			fontName = r.getString(R.string.font_name_default);
		}

		mTypeface = StoryActionActivity.getTypeface(context, fontName);

		int fontIdTextTouch = r.getIdentifier("font_name_texttouch_" + mPageId,
				"string", packageName);
		String fontNameTextTouch;
		if (fontIdTextTouch > 0) {
			fontNameTextTouch = r.getString(fontIdTextTouch);
		} else {
			fontNameTextTouch = r.getString(R.string.font_name_times);
		}

		mTypefaceTextTouch = StoryActionActivity.getTypeface(context,
				fontNameTextTouch);

		int idBackLeft = r.getIdentifier("backToMenu_marginLeft_" + mPageId,
				"dimen", packageName);
		if (idBackLeft == 0)
			idBackLeft = R.dimen.backToMenu_marginLeft_default;
		int idBackBottom = r.getIdentifier(
				"backToMenu_marginBottom_" + mPageId, "dimen", packageName);
		if (idBackBottom == 0)
			idBackBottom = R.dimen.backToMenu_marginBottom_default;

		mBackToMenu_marginLeft = r.getDimensionPixelSize(idBackLeft);
		mBackToMenu_marginBottom = r.getDimensionPixelSize(idBackBottom);

		// read data appear text content
		int strTextResId = r.getIdentifier("str_touch_" + mPageId, "array",
				packageName);
		mStringText = r.getStringArray(strTextResId);
		// read data touch
		int strTouchResId = r.getIdentifier("touch_area_" + mPageId, "array",
				packageName);
		String[] arrayTouchArea = r.getStringArray(strTouchResId);
		assert mStringText.length == arrayTouchArea.length;
		mRectTouch = new ArrayList<Rect[]>(mStringText.length);
		for (int i = 0; i < arrayTouchArea.length; i++) {
//			Log.i("PAGE.........", arrayTouchArea[i]);
			String[] tmpSplit1 = arrayTouchArea[i].split(";");
//			Log.i("aaaaaa...", "number of array "+tmpSplit1.length);
			Rect[] tmpRectTouch = new Rect[tmpSplit1.length];
			for (int j = 0; j < tmpSplit1.length; j++) {
				String[] tmpSplit2 = tmpSplit1[j].split(" ");
//				Log.i("bbbbb", "number of string "+tmpSplit2.length);
				if (r.getDisplayMetrics().densityDpi != DisplayMetrics.DENSITY_HIGH)
					tmpRectTouch[j] = new Rect(
							Integer.valueOf(tmpSplit2[0]) - 27,
							Integer.valueOf(tmpSplit2[1]),
							Integer.valueOf(tmpSplit2[2]) - 27,
							Integer.valueOf(tmpSplit2[3]));
				else
					tmpRectTouch[j] = new Rect(Integer.valueOf(tmpSplit2[0]),
							Integer.valueOf(tmpSplit2[1]),
							Integer.valueOf(tmpSplit2[2]),
							Integer.valueOf(tmpSplit2[3]));
			}
			mRectTouch.add(i, tmpRectTouch);
		}
		// read data -appear text box rectangle
		int strTextBoxResId = r.getIdentifier("text_" + mPageId, "array",
				packageName);
		String[] strTmpRectBox = r.getStringArray(strTextBoxResId);
		assert mStringText.length == strTmpRectBox.length;
		mRectBoxText = new Rect[strTmpRectBox.length];
		for (int i = 0; i < strTmpRectBox.length; i++) {
			String[] tmpSplit = strTmpRectBox[i].split(" ");
			if (r.getDisplayMetrics().densityDpi != DisplayMetrics.DENSITY_HIGH)
				mRectBoxText[i] = new Rect(Integer.valueOf(tmpSplit[0]) - 27,
						Integer.valueOf(tmpSplit[1]),
						Integer.valueOf(tmpSplit[0])
								+ Integer.valueOf(tmpSplit[2]) - 27,
						Integer.valueOf(tmpSplit[1])
								+ Integer.valueOf(tmpSplit[3]));
			else
				mRectBoxText[i] = new Rect(Integer.valueOf(tmpSplit[0]),
						Integer.valueOf(tmpSplit[1]),
						Integer.valueOf(tmpSplit[0])
								+ Integer.valueOf(tmpSplit[2]),
						Integer.valueOf(tmpSplit[1])
								+ Integer.valueOf(tmpSplit[3]));
		}
		// read data animation rectangle
		int arrayRectAniamtionId = r.getIdentifier("anim_" + mPageId, "array",
				packageName);
		String[] strRectAnimation = r.getStringArray(arrayRectAniamtionId);
		mRectAnimation = new Rect[strRectAnimation.length];
		for (int i = 0; i < strRectAnimation.length; i++) {
			String[] tmpSplit = strRectAnimation[i].split(" ");
			mRectAnimation[i] = new Rect(Integer.valueOf(tmpSplit[0]),
					Integer.valueOf(tmpSplit[1]), Integer.valueOf(tmpSplit[0])
							+ Integer.valueOf(tmpSplit[2]),
					Integer.valueOf(tmpSplit[1]) + Integer.valueOf(tmpSplit[3]));
		}
		// read data animation resource ID
		mBitmapAnimationId = new int[mRectAnimation.length];
		for (int i = 0; i < mRectAnimation.length; i++) {
			mBitmapAnimationId[i] = r.getIdentifier("drawable/anim" + (i + 1)
					+ "_" + mPageId, null, packageName);
		}
		// read data order animation follow time.
		int strOrderId = r.getIdentifier("order_" + mPageId, "string",
				packageName);
		String orderString = r.getString(strOrderId);
		String[] orderValue = orderString.split(" ");
		mOrderAnimation = new ArrayList<Integer>(orderValue.length);
		for (int i = 0; i < orderValue.length; i++) {
			mOrderAnimation.add(i, Integer.valueOf(orderValue[i]));
		}
		// read data animation - time run animation
		int strAnimationTimeId = r.getIdentifier("time_animation_" + mPageId,
				"string", packageName);
		String timeAnimation = r.getString(strAnimationTimeId);
		String[] valueTimeAnimation = timeAnimation.split(" ");
		mTimeAnimation = new ArrayList<Integer>(valueTimeAnimation.length);
		for (int i = 0; i < valueTimeAnimation.length; i++) {
			mTimeAnimation.add(i,
					(int) (Double.valueOf(valueTimeAnimation[i]) * 1000));
		}
		// read data sound tab
		int soundIdTab = r.getIdentifier("sound_id_" + mPageId, "array",
				packageName);
		idSound = r.getIntArray(soundIdTab);
		// read data animation zoom
		// data zoom animation contain:
		// isZoom: 1 or 0
		//pivot zoom
		int isZoomResId = r.getIdentifier("is_zoom_" + mPageId, "integer",
				packageName);
		int tmpIsZoom = r.getInteger(isZoomResId);
		if (tmpIsZoom == 0) {
			mIsZoom = false;
		} else {
			mIsZoom = true;
		}
		if (mIsZoom) {
			int codinatePivotResId = r.getIdentifier("pivot_zoom_" + mPageId,
					"string", packageName);
			String codinateString = r.getString(codinatePivotResId);
			String[] splitCodinate = codinateString.split(" ");
			mPivotZoomX = Integer.parseInt(splitCodinate[0]);
			mPivotZoomY = Integer.parseInt(splitCodinate[1]);
		}
	}

	public static String getPageIdForIndex(int id) {
		return "page" + String.valueOf(id);
	}
}
