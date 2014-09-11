package com.fgsecure.ujoolt.app.utillity;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import com.fgsecure.ujoolt.app.json.Jolt;
import com.fgsecure.ujoolt.app.screen.MainScreenActivity;

public class LoadImageTask extends AsyncTask<String, String, String> {
	MainScreenActivity mainScreenActivity;
	Jolt jolt;
	ProgressBar progressBar;
	public int myProgress;

	public LoadImageTask(MainScreenActivity mainScreenActivity, Jolt jolt) {
		this.mainScreenActivity = mainScreenActivity;
		progressBar = mainScreenActivity.progressBar;
		this.jolt = jolt;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		mainScreenActivity.imgAvatar.setImageBitmap(MainScreenActivity.bitmapNoAvatar);
		progressBar.setVisibility(View.VISIBLE);
	}

	@Override
	protected String doInBackground(String... params) {
		jolt.setPhotoBitmapFromURL(jolt.getPhotoURL());
		return null;
	}

	@Override
	protected void onPostExecute(String result) {

		// if (mainScreenActivity.checkCurrentJolt(jolt)) {
		progressBar.setVisibility(View.INVISIBLE);
		if (jolt.getPhotoBitmap() != null) {
			mainScreenActivity.imgAvatar.setImageBitmap(jolt.getPhotoBitmap());
		}
		progressBar.setVisibility(View.INVISIBLE);

		if (mainScreenActivity.getBitmapOfJolt() != null) {
			MainScreenActivity.bitmapJoltAvatar = null;
		}

		// view information to show detail jolt
		mainScreenActivity.setShowSearchLayout(View.GONE);
		mainScreenActivity.viewInformation.setVisibility(View.VISIBLE);
		mainScreenActivity.isBubbleDetail = true;
		// }
	}
}
