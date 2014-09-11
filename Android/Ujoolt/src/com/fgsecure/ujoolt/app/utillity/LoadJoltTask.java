package com.fgsecure.ujoolt.app.utillity;

import android.util.Log;

import com.fgsecure.ujoolt.app.screen.MainScreenActivity;
import com.google.android.maps.GeoPoint;

public class LoadJoltTask implements Runnable {
	MainScreenActivity mainScreenActivity;

	public LoadJoltTask(MainScreenActivity mainScreenActivity) {
		this.mainScreenActivity = mainScreenActivity;
	}

	@Override
	public void run() {
		Log.e("vap", "ok");
		mainScreenActivity.curLati = 48895000;
		mainScreenActivity.curLongi = 2282500;
		mainScreenActivity.mapController.animateTo(new GeoPoint(mainScreenActivity.curLati,
				mainScreenActivity.curLongi));

		// mc.animateTo(new GeoPoint(48895000, 2282500));
		mainScreenActivity.mapController.setZoom(17);
		mainScreenActivity.mapView.invalidate();
		// mainScreenActivity.joltHolder.setCoordinates(mainScreenActivity.curLati,
		// mainScreenActivity.curLongi);
		// mainScreenActivity.joltHolder.getAllJoltsFromLocation(
		// mainScreenActivity.lati_jolt, mainScreenActivity.longi_jolt,
		// ConfigUtility.getCurTimeStamp());
		// mainScreenActivity.joltHolder.autoGenarateJoltBlues(
		// mainScreenActivity.lati_jolt, mainScreenActivity.longi_jolt);
		//
		// mainScreenActivity.getArrayJoltAvailable();
		// mainScreenActivity.mapView.regroupJolts();
		// mainScreenActivity.displayJolts();
	}

}
