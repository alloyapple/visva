package com.visva.android.flashlight.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.visva.android.flashlightmaster.R;

public class AppsActivity extends BaseActivity {

	private Button _btnFeatures;
	private TextView _tvLabel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.apps);

		mapUIElement();

		if (_preferenceUtilities.isShowAppsLabel()) {
			flip(_tvLabel);
			_preferenceUtilities.setShowAppsLabel(false);
		} else {
			_tvLabel.setVisibility(View.GONE);
		}
	}

	private void mapUIElement() {
		_btnFeatures = (Button) findViewById(R.id.btnFeatures);
		_btnFeatures.setOnClickListener(_btnFeature_Click);
		_tvLabel = (TextView) findViewById(R.id.tvLabel);
	}
}
