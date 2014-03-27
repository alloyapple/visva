package com.visva.android.flashlight.utilities;

import java.util.Random;

import android.graphics.Color;

public class ColorUtilities {

	public static int randomColor() {
		Random __rdn = new Random();
		int __red = __rdn.nextInt(255);
		int __green = __rdn.nextInt(255);
		int __blue = __rdn.nextInt(255);
		return Color.rgb(__red, __green, __blue);
	}

	public static int[] color = new int[] { Color.parseColor("#31f331"), Color.parseColor("#21f3a5"),
			Color.parseColor("#21efef"), Color.parseColor("#1838de"), Color.parseColor("#a520f7"),
			Color.parseColor("#ef1ce7"), Color.parseColor("#ef1c8c"), Color.parseColor("#ef1c18"),
			Color.parseColor("#efa218"), Color.parseColor("#efef31") };
	public static int positionColor = 0;

	public static int getColorLeftToRight() {
		positionColor++;
		if (positionColor >= 10) {
			positionColor = 0;
		}
		return color[positionColor];
	}

	public static int getColorRightToLeft() {
		positionColor--;
		if (positionColor < 0) {
			positionColor = 9;
		}
		return color[positionColor];
	}
}
