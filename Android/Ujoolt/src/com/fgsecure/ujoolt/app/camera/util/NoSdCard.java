package com.fgsecure.ujoolt.app.camera.util;

import android.app.Activity;
import android.os.Bundle;
import com.fgsecure.ujoolt.app.R;

public class NoSdCard extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.no_sdcard);
	}
}