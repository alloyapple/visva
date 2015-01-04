package com.visva.android.hangman.ultis;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;

import com.visva.android.hangman.R;

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
