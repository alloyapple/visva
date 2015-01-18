package com.gurusolution.android.hangman2.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;

import com.gurusolution.android.hangman2.R;

public class ProgressDialog extends Dialog {

	public ProgressDialog(Context context) {
		super(context);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setBackgroundDrawableResource(R.drawable.bg_black_transparent);
		setContentView(R.layout.layout_progress_dialog);
	}
}
