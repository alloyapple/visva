package com.visva.android.flashlight.adapter;

import android.widget.TextView;

public class ShareViewHelper {

	private TextView _tvName;

	public TextView get_tvName() {
		return _tvName;
	}

	public void set_tvName(TextView _tvName) {
		this._tvName = _tvName;
	}

	public ShareViewHelper(TextView _tvName) {
		super();
		this._tvName = _tvName;
	}

	public void setName(String name) {
		_tvName.setText(name);
	}

	public ShareViewHelper() {
		super();
	}

}
