package com.japanappstudio.IDxPassword.activities;

import java.io.File;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.FloatMath;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnTouchListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.japanappstudio.IDxPassword.activities.homescreen.HomeScreeenActivity;
import com.japanappstudio.IDxPassword.contants.Contants;

public class EditIconActivity extends BaseActivity {
	ImageView imageView;
	private Uri fileUri = null;
	public static Drawable mDrawableIconEdit;
	private CheckBox mCheckBox;
	// These matrices will be used to scale points of the image

	// The 3 states (events) which the user is trying to perform
	static final int NONE = 0;
	static final int DRAG = 1;
	static final int ZOOM = 2;
	int mode = NONE;
	private double x_offset;
	private double y_offset;
	// these PointF objects are used to record the point(s) the user is touching
	PointF start = new PointF();
	PointF mid = new PointF();
	float oldDist = 1f;
	int l, t, w, h;
	private ImageView imgBound;
	private RelativeLayout mRelativeLayout;
	@SuppressWarnings("unused")
	private int width, height;
	private int modeBundle;
	private boolean checkFirstChangeWindow = true;
	private AdView adview;
	private Thread mThreadAd;
	private Bitmap bmp;
	static int widthB, heightB;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		modeBundle = getIntent().getExtras().getInt("modeBundleEditIcon");
		Display d = getWindowManager().getDefaultDisplay();
		width = d.getWidth();
		height = d.getHeight();
		setContentView(R.layout.page_edit_icon);
		imageView = (ImageView) findViewById(R.id.id_img_need_edit);
		imgBound = (ImageView) findViewById(R.id.id_bound_edit_icon);
		mCheckBox = (CheckBox) findViewById(R.id.id_checkbox_edit_icon);
		mCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					if (mDrawableIconEdit == null) {
						mCheckBox.setChecked(false);
						return;
					}
					if (!mPrefApp.isEditIDxPassHome()) {
						EditIdPasswordActivity
								.updateIcon((Drawable) new BitmapDrawable(
										snapScreen()));
						EditIdPasswordActivity.startActivity(
								EditIconActivity.this, 2);
					} else {
						HomeScreeenActivity
								.updateIcon((Drawable) new BitmapDrawable(
										snapScreen()));
					}
					finish();
				}
			}
		});
		mRelativeLayout = (RelativeLayout) findViewById(R.id.id_relative_background);
		mRelativeLayout.setVisibility(View.VISIBLE);
		mRelativeLayout.setOnTouchListener(new OnTouchListener() {
			float scale;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch (event.getAction() & MotionEvent.ACTION_MASK) {
				case MotionEvent.ACTION_DOWN:
					w = getParam().width;
					h = getParam().height;
					start.set(event.getX(), event.getY());
					mode = DRAG;
					break;
				case MotionEvent.ACTION_POINTER_DOWN:
					oldDist = spacing(event);
					l = getParam().leftMargin;
					t = getParam().topMargin;

					if (oldDist > 5f) {
						midPoint(mid, event);
						mode = ZOOM;

					}
					break;
				case MotionEvent.ACTION_UP:
					break;
				case MotionEvent.ACTION_POINTER_UP:
					mode = NONE;

					break;

				case MotionEvent.ACTION_MOVE:
					if (mode == DRAG) {
						x_offset = event.getX() - start.x;
						y_offset = event.getY() - start.y;
						translateBound(x_offset, y_offset);
						start.set(event.getX(), event.getY());
					} else if (mode == ZOOM) {
						float newDist = spacing(event);
						scale = newDist / oldDist;
						resiseBound(scale);
					}
					break;
				default:
					break;
				}
				return true;
			}
		});
		if (modeBundle == 1) {
			if (!mPrefApp.isEditIDxPassHome())
				mDrawableIconEdit = EditIdPasswordActivity.getIcon();
			else
				mDrawableIconEdit = HomeScreeenActivity.getIcon();
		}
		initAdmod();

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			onReturn(null);
			break;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onAttachedToWindow() {
		// TODO Auto-generated method stub
		super.onAttachedToWindow();

	}

	@SuppressWarnings("deprecation")
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (checkFirstChangeWindow) {
			Display d = getWindowManager().getDefaultDisplay();
			int heightP = mRelativeLayout.getHeight();
			getParamBound().width = d.getWidth() * 3 / 4;
			getParamBound().height = (int) (d.getWidth() * 3 / 4 * 0.75f);
			imgBound.requestLayout();

			getParam().width = d.getWidth() * 3 / 4;
			getParam().height = (int) (d.getWidth() * 3 / 4 * 0.75f);
			getParam().leftMargin = (int) (d.getWidth() - getParamBound().width) / 2;
			getParam().topMargin = (int) (heightP - getParamBound().height) / 2;
			imageView.requestLayout();
			checkFirstChangeWindow = false;
		}

	}

	public void resiseBound(float scale) {
		int left = (getParam().leftMargin + getParam().width / 2)
				- (int) ((w * scale) / 2);
		int top = (getParam().topMargin + getParam().height / 2)
				- (int) ((h * scale) / 2);
		int right;
		if (left > width - (int) (w * scale)) {
			right = width - left - (int) (w * scale);
		} else {
			right = getParam().rightMargin;
		}
		int bottom;
		if (top > mRelativeLayout.getHeight() - (int) (h * scale)) {
			bottom = mRelativeLayout.getHeight() - top - (int) (h * scale);
		} else {
			bottom = getParam().bottomMargin;
		}
		if (left < -(w * scale) || left > mRelativeLayout.getWidth()
				|| top < -(h * scale) || top > mRelativeLayout.getHeight()) {
			return;
		}
		imageView.setLayoutParams(new RelativeLayout.LayoutParams(
				(int) (w * scale), (int) (h * scale)));
		getParam().leftMargin = left;
		getParam().topMargin = top;
		getParam().rightMargin = right;
		getParam().bottomMargin = bottom;
		imageView.requestLayout();
	}

	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return FloatMath.sqrt(x * x + y * y);
	}

	private void midPoint(PointF point, MotionEvent event) {
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		point.set(x / 2, y / 2);
	}

	public RelativeLayout.LayoutParams getParam() {
		return (RelativeLayout.LayoutParams) imageView.getLayoutParams();
	}

	public FrameLayout.LayoutParams getParamBound() {
		return (FrameLayout.LayoutParams) imgBound.getLayoutParams();
	}

	public Bitmap snapScreen() {
		mRelativeLayout.setDrawingCacheEnabled(true);
		mRelativeLayout.measure(
				MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
				MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		mRelativeLayout.buildDrawingCache(true);
		int left = (int) (mRelativeLayout.getWidth() - getParamBound().width) / 2;
		int top = (int) (mRelativeLayout.getHeight() - getParamBound().height) / 2;
		Bitmap bm = Bitmap.createBitmap(mRelativeLayout.getDrawingCache());
		Bitmap bm2 = Bitmap.createBitmap(bm, left, top, imgBound.getWidth(),
				imgBound.getHeight());
		mRelativeLayout.setDrawingCacheEnabled(false); //
		return bm2;
	}

	public void translateBound(double x, double y) {
		int l, t, r, b;
		if (getParam().leftMargin + (int) x < -imageView.getWidth()) {
			l = -imageView.getWidth();

		} else if (getParam().leftMargin + (int) x > width) {
			l = width;

		} else {
			l = getParam().leftMargin + (int) x;
		}
		if (getParam().leftMargin + (int) x > width - imageView.getWidth()) {
			r = width - l - imageView.getWidth();
		} else {
			r = getParam().rightMargin;
		}
		if (getParam().topMargin + (int) y < -imageView.getHeight()) {
			t = -imageView.getHeight();
		} else if (getParam().topMargin + (int) y > mRelativeLayout.getHeight()) {
			t = mRelativeLayout.getHeight();
		} else {
			t = getParam().topMargin + (int) y;
		}
		if (getParam().topMargin + (int) y > mRelativeLayout.getHeight()
				- imageView.getHeight()) {
			b = mRelativeLayout.getHeight() - t - imageView.getHeight();
		} else {
			b = getParam().bottomMargin;
		}
		getParam().setMargins(l, t, r, b);
		imageView.requestLayout();
	}

	public void initAdmod() {
		adview = (AdView) findViewById(R.id.main_adView);
		AdRequest re = new AdRequest();
		if (adview != null) {
			adview.loadAd(re);
			adview.setVisibility(View.VISIBLE);
			mThreadAd = new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					while (true) {

						if (adview.getHeight() > 0) {
							runOnUiThread(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									getParam().topMargin -= adview.getHeight() / 2;
									imageView.requestLayout();
								}
							});
							break;
						}
						try {
							Thread.sleep(50);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			});
			mThreadAd.start();
		}

	}

	public void initDataItem() {

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		imageView.setBackgroundDrawable(mDrawableIconEdit);
		// if (mDrawableIconEdit == null)
		// imgBound.setVisibility(View.GONE);
		if (fileUri != null)
			imageView.setBackgroundColor(Color.TRANSPARENT);
	}

	public static void startActivity(Activity activity, int value) {
		Intent i = new Intent(activity, EditIconActivity.class);
		i.putExtra("modeBundleEditIcon", value);
		activity.startActivity(i);
	}

	public void onReturn(View v) {
		ListIconActivity.startActivity(this);
		finish();

	}

	public void onLibrary(View v) {
		widthB = imgBound.getWidth();
		heightB = imgBound.getHeight();
		startGalleryIntent();
	}

	public void onInternet(View v) {
		GetInternetImageActivity.startActivity(this);
		finish();
	}

	private void startGalleryIntent() {
		fileUri = null;
		Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
		photoPickerIntent.setType("image/*");
		startActivityForResult(photoPickerIntent, Contants.SELECT_PHOTO);
	}

	@Override
	protected void onActivityResult(final int requestCode,
			final int resultCode, final Intent data) {
		switch (requestCode) {

		case Contants.SELECT_PHOTO:
			if (resultCode == RESULT_OK) {
				// fileUri = data.getData();
				// imageView.requestLayout();
				// imageView.setImageBitmap(null);
				// imageView.setImageURI(fileUri);
				Uri uri = data.getData();
				String[] filePathColumn = { MediaStore.Images.Media.DATA };
				Cursor cursor = getContentResolver().query(uri, filePathColumn,
						null, null, null);
				cursor.moveToFirst();
				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				String imagePath = cursor.getString(columnIndex);
				cursor.close();

				File file = new File(imagePath);
				if (file.exists()) {
					fileUri = Uri.fromFile(file);
					int orientation = checkOrientation(fileUri);
					bmp = decodeSampledBitmapFromFile(imagePath, widthB,
							heightB, orientation);
					float ratioH = (float) bmp.getHeight() / heightB;
					float ratioW = (float) bmp.getWidth() / widthB;
					float w, h;
					if (ratioH > ratioW) {
						w = bmp.getWidth() / ratioH;
						h = imageView.getHeight();

					} else {
						w = imageView.getWidth();
						h = bmp.getHeight() / ratioW;
					}
					Display d = getWindowManager().getDefaultDisplay();
					int heightP = mRelativeLayout.getHeight();
					getParam().width = (int) w;
					getParam().height = (int) h;
					getParam().leftMargin = (int) (d.getWidth() - getParam().width) / 2;
					getParam().topMargin = (int) (heightP - getParam().height) / 2;
					mDrawableIconEdit = new BitmapDrawable(bmp);
					imageView.setImageDrawable(mDrawableIconEdit);
					imageView.requestLayout();

				} else {
					Log.e("test", "file don't exist !");
				}

			}
			break;
		default:
			return;
		}
	}

	private Bitmap decodeSampledBitmapFromFile(String filePath, int reqWidth,
			int reqHeight, int orientation) {
		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		Matrix mtx = new Matrix();
		mtx.postRotate(orientation);
		// BitmapFactory.decodeResource(res, resId, options);
		BitmapFactory.decodeFile(filePath, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);
		int width = options.outWidth;
		int height = options.outHeight;
		Log.e("width " + height, "width " + width);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		Log.e("orientation ", "orientation " + orientation);
		// return Bitmap.createBitmap(BitmapFactory.decodeFile(filePath,
		// options), 0, 0, reqHeight,
		// reqWidth, mtx, true);

		return decodeBitmap(BitmapFactory.decodeFile(filePath, options),
				orientation);

	}

	private Bitmap decodeBitmap(Bitmap bitmap, int orientation) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		Matrix mtx = new Matrix();
		mtx.postRotate(orientation);
		return Bitmap.createBitmap(bitmap, 0, 0, width, height, mtx, true);
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}

	private int checkOrientation(Uri fileUri) {
		int rotate = 0;
		String imagePath = fileUri.getPath();
		ExifInterface exif = null;
		try {
			exif = new ExifInterface(imagePath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Since API Level 5
		int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
				ExifInterface.ORIENTATION_NORMAL);
		switch (orientation) {
		case ExifInterface.ORIENTATION_ROTATE_270:
			rotate = 270;
			break;
		case ExifInterface.ORIENTATION_ROTATE_180:
			rotate = 180;
			break;
		case ExifInterface.ORIENTATION_ROTATE_90:
			rotate = 90;
			break;
		}
		return rotate;
	}

}
