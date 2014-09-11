package com.fgsecure.ujoolt.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.fgsecure.ujoolt.app.screen.MainScreenActivity;

public class MessageBox extends Activity {

	public static boolean whenReceivedNotification = false;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.popup);
		String message = getIntent().getExtras().getString("message");
		TextView textView = (TextView) findViewById(R.id.textNotification);
		Log.d("Language", "msg " + message);
		textView.setText(message);

		// AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// builder.setMessage("Are you sure you want to exit?")
		// .setCancelable(false)
		// .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		// public void onClick(DialogInterface dialog, int id) {
		// whenReceivedNotification = true;
		//
		// Intent intent = new Intent(getBaseContext(),
		// MainScreenActivity.class);
		//
		// intent.setFlags(0x00020000);
		// intent.putExtra("PrevActivity", "NOTIFY");
		// startActivity(intent);
		// finish();
		// }
		// })
		// .setNegativeButton("No", new DialogInterface.OnClickListener() {
		// public void onClick(DialogInterface dialog, int id) {
		// finish();
		// }
		// });
		// AlertDialog alert = builder.create();
		// alert.show();

		Button buttonOpen = (Button) findViewById(R.id.button_open);
		buttonOpen.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				whenReceivedNotification = true;

				Intent intent = new Intent(getBaseContext(), MainScreenActivity.class);

				intent.setFlags(0x00020000);
				startActivity(intent);
				finish();
			}
		});

		Button btnCancel = (Button) findViewById(R.id.button_cancel_popup);
		btnCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}
