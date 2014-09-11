package com.fgsecure.ujoolt.app.camera.other;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

public class DialogConfirm extends Activity {
	final CharSequence[] items = { "Image", "Video" };
	private boolean isPhoto;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		isPhoto = getIntent().getExtras().getBoolean("isPhoto");
		openDialogChoseFile();
	}

	/**
	 * chose image and video from gallery
	 */
	private void openDialogChoseFile() {
		AlertDialog.Builder builder1 = new AlertDialog.Builder(
				DialogConfirm.this);
		builder1.setTitle("Chose type of file:");
		builder1.setOnCancelListener(new DialogInterface.OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				Intent i = new Intent(DialogConfirm.this, DgCamActivity.class);
				i.putExtra("isPhoto", isPhoto);
				startActivity(i);
				finish();
			}
		});
		builder1.setItems(items, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int item) {
				Toast.makeText(getApplicationContext(), items[item],
						Toast.LENGTH_SHORT).show();
				if ("Image".equals(items[item])) {
					// openImageGallery();// chose image from gallery
					Intent intent = new Intent(DialogConfirm.this,ImageViewActivity.class);					
					isPhoto = true;
					intent.putExtra("isPhoto", isPhoto);
					startActivity(intent);
					finish();
				} else if ("Video".equals(items[item])) {
					// openVideoGallery();// chose video from gallery
					Intent intent = new Intent(DialogConfirm.this,
							MediaViewGallery.class);
					isPhoto = false;
					intent.putExtra("isPhoto", isPhoto);
					startActivity(intent);
					finish();
				}
				Log.e("item", "" + items[item]);
			}
		});
		builder1.show();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			Intent i = new Intent(DialogConfirm.this, DgCamActivity.class);
			i.putExtra("isPhoto", isPhoto);
			startActivity(i);
			finish();
		default:
			return super.onKeyDown(keyCode, event);
		}
	}
}
