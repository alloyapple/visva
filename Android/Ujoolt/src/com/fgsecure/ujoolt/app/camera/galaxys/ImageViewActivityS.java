package com.fgsecure.ujoolt.app.camera.galaxys;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.fgsecure.ujoolt.app.R;
import com.fgsecure.ujoolt.app.screen.MainScreenActivity;
import com.fgsecure.ujoolt.app.utillity.JoltHolder;

public class ImageViewActivityS extends Activity implements OnClickListener {
	// =======================Control init ==================
	private FrameLayout frameLayout;
	private Button btnUse;
	private Button btnRetake;
	private ImageView imgView;
	// ========================Variable init ================
	private static boolean isGetImg = false;
	private String selectedImagePath = null;
	private static final int SELECT_PICTURE = 1;
	private Bitmap avatar = null;
	private Uri photoUri;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_image_view_gallery_s);
		// init
		isGetImg = false;
		if (frameLayout == null)
			frameLayout = (FrameLayout) findViewById(R.id.camera_preview);
		if (btnRetake == null)
			btnRetake = (Button) findViewById(R.id.btn_retake_img_gallery);
		btnRetake.setOnClickListener(this);
		if (btnUse == null)
			btnUse = (Button) findViewById(R.id.btn_use_img_gallery);
		btnUse.setOnClickListener(this);
		if (imgView == null)
			imgView = (ImageView) findViewById(R.id.img_view_1);
		if (!isGetImg)
			openImgFromGallery();
	}

	private void openImgFromGallery() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_PICK);
		startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == SELECT_PICTURE) {
				isGetImg = true;

				photoUri = data.getData();

				selectedImagePath = getPath(photoUri);
				Log.e("file path", "Image Path : " + selectedImagePath);
				MainScreenActivity.myPath = selectedImagePath;
				Log.e("Lemon-ImageViewActivityS", "Path in main screen : "
						+ MainScreenActivity.myPath);
				if (selectedImagePath != null) {
					imgView.setImageBitmap(resizeImage(selectedImagePath, null, 400, 600));
				} else {
					Intent i = new Intent(ImageViewActivityS.this, DgCamActivityS.class);
					i.putExtra("isPhoto", true);
					startActivity(i);
					finish();
				}
			}
		} else {
			Intent i = new Intent(ImageViewActivityS.this, DgCamActivityS.class);
			i.putExtra("isPhoto", true);
			startActivity(i);
			finish();
		}
	}

	public String getPath(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	@Override
	public void onClick(View v) {

		// if (v == btnClose) {
		// if (imgView != null) {
		// imgView = null;
		// }
		// Intent i = new Intent(ImageViewActivityS.this, DgCamActivityS.class);
		// startActivity(i);
		// finish();
		// }
		if (v == btnRetake) {
			if (imgView != null) {
				imgView = null;
			}
			Intent i = new Intent(ImageViewActivityS.this, DgCamActivityS.class);
			startActivity(i);
			finish();
			selectedImagePath = null;
		}
		if (v == btnUse) {
			Log.e("Cane:use this pic", "ok");
			BitmapDrawable drawable = (BitmapDrawable) imgView.getDrawable();
			if (avatar == null)
				avatar = drawable.getBitmap();
			MainScreenActivity.bitmapJoltAvatar = avatar;
			MainScreenActivity.btnUploadPhoto.setBackgroundDrawable(new BitmapDrawable(avatar));
			MainScreenActivity.photoUri = photoUri;
			MainScreenActivity.myPath = selectedImagePath;
			JoltHolder.isPhoto = true;
			Log.e("Lemon-ImageViewActivityS", "Path in main screen after Use click : "
					+ MainScreenActivity.myPath);
			if (imgView != null) {
				imgView = null;
			}
			selectedImagePath = null;
			finish();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			Log.d("finish", "ok");
			finish();
		default:
			return super.onKeyDown(keyCode, event);
		}
	}

	private Bitmap resizeImage(String inFile, String outFile, int destHeight, int destWidth) {
		Bitmap resizedBitmap = null;
		try {

			int inWidth = 0;
			int inHeight = 0;

			InputStream in = new FileInputStream(inFile);

			// decode image size (decode metadata only, not the whole image)
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(in, null, options);
			in.close();
			in = null;

			// save width and height
			inWidth = options.outWidth;
			inHeight = options.outHeight;
			// byte per pixel = 4
			int sizeOfFile = inWidth * inHeight * 4;
			// 50M = 52428800
			if (sizeOfFile < 0) { // size of file is exceed 50M
				if (getAvailableMemory() - 20480 < 52428800) {
					Log.e("Ujoolt", "Out of memory");
					return null;
				}
			} else if (sizeOfFile > getAvailableMemory() - 20480) {
				Log.e("Ujoolt", "Out of memory");
				return null;
			}

			// decode full image pre-resizeda
			in = new FileInputStream(inFile);
			options = new BitmapFactory.Options();
			// calc rought re-size (this is no exact resize)
			options.inSampleSize = Math.max(inWidth / destWidth, inHeight / destHeight);
			options.inPurgeable = true;
			options.inPreferredConfig = Bitmap.Config.RGB_565;
			try {
				// decode full image
				Bitmap roughBitmap = BitmapFactory.decodeStream(in, null, options);

				// calc exact destination size
				Matrix m = new Matrix();
				RectF inRect = new RectF(0, 0, roughBitmap.getWidth(), roughBitmap.getHeight());
				RectF outRect = new RectF(0, 0, destWidth, destHeight);
				m.setRectToRect(inRect, outRect, Matrix.ScaleToFit.CENTER);
				float[] values = new float[9];
				m.getValues(values);

				// resize bitmap
				resizedBitmap = Bitmap.createScaledBitmap(roughBitmap,
						(int) (roughBitmap.getWidth() * values[0]),
						(int) (roughBitmap.getHeight() * values[4]), true);

				in.close();
			} catch (Exception e) {
				Log.e("Image", e.getMessage(), e);
			} catch (OutOfMemoryError e) {
				Log.e("Image", e.getMessage(), e);
			}
		} catch (IOException e) {
			Log.e("Image", e.getMessage(), e);
		} catch (OutOfMemoryError e) {
			Log.e("Image", e.getMessage(), e);
		}
		return resizedBitmap;
	}

	private long getAvailableMemory() {
		MemoryInfo mi = new MemoryInfo();

		ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

		activityManager.getMemoryInfo(mi);

		return mi.availMem;
	}

	protected void onResume() {
		super.onResume();
		Log.d("selectedImagePath", "Image path resume : " + selectedImagePath);
		Log.d("isgetImag", "" + isGetImg);
		if (selectedImagePath == null && isGetImg) {
			Intent i = new Intent(ImageViewActivityS.this, DgCamActivityS.class);
			i.putExtra("isPhoto", true);
			startActivity(i);
			finish();
		}
	}
}
