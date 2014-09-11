package com.fgsecure.ujoolt.app.utillity;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.fgsecure.ujoolt.app.R;
import com.fgsecure.ujoolt.app.json.Jolt;
import com.fgsecure.ujoolt.app.screen.MainScreenActivity;

public class LoadOnlyTextTask extends AsyncTask<String, String, String> {
	MainScreenActivity mainScreenActivity;
	Jolt jolt;
	ProgressBar progressBar;
	public int myProgress;

	public LoadOnlyTextTask(MainScreenActivity mainScreenActivity, Jolt jolt) {
		this.mainScreenActivity = mainScreenActivity;
		progressBar = mainScreenActivity.progressBar;
		this.jolt = jolt;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

		progressBar.setVisibility(View.VISIBLE);
	}

	@Override
	protected String doInBackground(String... params) {
		jolt.setPhotoBitmapFromURL(null);
		return null;
	}

	@Override
	protected void onPostExecute(String result) {
		if (mainScreenActivity.checkCurrentJolt(jolt)) {
			progressBar.setVisibility(View.INVISIBLE);
			
			Log.e("cane:photo bitmap after post", ""+jolt.getPhotoBitmap());
				mainScreenActivity.imgAvatar
						.setImageResource(R.drawable.default_avatar);
		}
	}
}
