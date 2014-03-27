package com.visva.android.flashlight.adapter;

import android.widget.CheckBox;
import android.widget.TextView;

public class VisibleLightSourcesViewHelper {

	private TextView tvName;
	private CheckBox cbName;
	private boolean isCheck;

	public TextView getTvName() {
		return tvName;
	}

	public void setTvName(TextView tvName) {
		this.tvName = tvName;
	}

	public CheckBox getCbName() {
		return cbName;
	}

	public void setCbName(CheckBox cbName) {
		this.cbName = cbName;
	}

	public boolean isCheck() {
		return isCheck;
	}

	public void setCheck(boolean isCheck) {
		this.isCheck = isCheck;
	}

}
