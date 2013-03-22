package visvateam.outsource.idmanager.activities;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import visvateam.outsource.idmanager.contants.Contants;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.FloatMath;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnTouchListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class ImageMemoActivity extends BaseActivity {

	private ImageView imageView;
	private Uri fileUri;
	private CheckBox mCheckBoxChoiceImgMemo;
	static final int NONE = 0;
	static final int DRAG = 1;
	static final int ZOOM = 2;
	int mode = NONE;

	// these PointF objects are used to record the point(s) the user is touching
	PointF start = new PointF();
	PointF start2 = new PointF();
	PointF mid = new PointF();
	float oldDist = 1f;
	int l, t, w, h;
	private double x_offset;
	private double y_offset;
	private ImageView imgBound;
	private RelativeLayout mRelativeLayout;
	private int width, height;
	private FrameLayout mFrameMemo;
	private double angle1, angle2;
	private int countClockwise1, countUnclockwise1;
	private int countClockwise2, countUnclockwise2;
	private boolean isClockwise1, isClockwise2;
	Bitmap bmp;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_memo);
		imageView = (ImageView) findViewById(R.id.img_memo);
		mRelativeLayout = (RelativeLayout) findViewById(R.id.id_relative_bound);
		mFrameMemo = (FrameLayout) findViewById(R.id.id_memo_frame);
		mCheckBoxChoiceImgMemo = (CheckBox) findViewById(R.id.check_box_choice_img);
		mCheckBoxChoiceImgMemo
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub
						if (fileUri != null
								&& mCheckBoxChoiceImgMemo.isChecked()) {
							// String imagePth = fileUri.getPath();
							// // int orientation = checkOrientation(fileUri);
							// EditIdPasswordActivity.updateMemo((Drawable) new
							// BitmapDrawable(
							// decodeSampledBitmapFromFile(imagePth, 200, 100,
							// orientation)));
							EditIdPasswordActivity
									.updateMemo((Drawable) new BitmapDrawable(
											snapScreen()));
							Intent resultIntent = new Intent();
							// resultIntent.putExtra(Contants.FIlE_PATH_IMG_MEMO,
							// fileUri.toString());
							setResult(Activity.RESULT_OK, resultIntent);
							finish();
						} else {
							mCheckBoxChoiceImgMemo.setChecked(false);
						}
					}
				});
		imgBound = (ImageView) findViewById(R.id.img_bound_memo);
		imgBound.setVisibility(View.GONE);
		mRelativeLayout.setOnTouchListener(new OnTouchListener() {
			float scale;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (imgBound.getVisibility() != View.VISIBLE)
					return true;
				switch (event.getAction() & MotionEvent.ACTION_MASK) {
				case MotionEvent.ACTION_DOWN:
					countClockwise1 = countClockwise2 = countUnclockwise1 = countUnclockwise2 = 0;
					isClockwise1 = isClockwise2 = false;
					w = getParam().width;
					h = getParam().height;
					start.set(event.getX(), event.getY());
					mode = DRAG;
					angle1 = getAngle(event.getX(), event.getY());
					break;
				case MotionEvent.ACTION_POINTER_DOWN:
					start2.set(event.getX(), event.getY());
					oldDist = spacing(event);
					l = getParam().leftMargin;
					t = getParam().topMargin;

					if (oldDist > 5f) {
						midPoint(mid, event);
						mode = ZOOM;

					}
					angle2 = getAngle(event.getX(), event.getY());
					break;
				case MotionEvent.ACTION_UP:
					break;
				case MotionEvent.ACTION_POINTER_UP:
					mode = NONE;
					if (countClockwise1 > countUnclockwise1) {
						isClockwise1 = true;
					} else if (countClockwise1 < countUnclockwise1) {
						isClockwise1 = false;
					}
					if (countClockwise2 > countUnclockwise2) {
						isClockwise2 = true;
					} else if (countClockwise2 < countUnclockwise2) {
						isClockwise2 = false;
					}
					if (isClockwise1 && isClockwise2) {
						rote(90);
					}
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
						// float scaleX = 0, scaleY = 0;
						// scaleX = Math.abs(event.getX(0) - event.getX(1))
						// / oldDist;
						// scaleY = Math.abs(event.getY(0) - event.getY(1))
						// / oldDist;
						resiseBound(scale, scale);
					}
					if (mode == ZOOM) {
						double currentAngle1 = getAngle(event.getX(0),
								event.getY(0));
						if (((float) (angle1 - currentAngle1)) >= 0) {
							countClockwise1++;
						} else {
							countUnclockwise1++;
						}
						angle1 = currentAngle1;
						double currentAngle2 = getAngle(event.getX(1),
								event.getY(1));
						if (((float) (angle2 - currentAngle2)) >= 0) {
							countClockwise2++;
						} else {
							countUnclockwise2++;
						}
						angle2 = currentAngle2;
					}
					break;
				default:
					break;
				}
				return true;
			}
		});
		Display d = getWindowManager().getDefaultDisplay();
		width = d.getWidth();
		getParam().width = width / 2;
		getParam().height = width / 2;
		getParam().topMargin = getParam().leftMargin = width / 2;
	}

	public void rote(int angle) {

		imageView.setImageBitmap(rotate(bmp, 90));
		// imageView.setScaleType(ScaleType.FIT_CENTER);

	}

	public Bitmap rotate(Bitmap src, float degree) {
		// create new matrix
		Matrix matrix = new Matrix();
		// setup rotation degree
		matrix.postRotate(degree);

		// return new bitmap rotated using matrix
		bmp = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(),
				matrix, true);
		return bmp;
	}

	int widthT, heightT;

	private double getAngle(double xTouch, double yTouch) {
		widthT = mFrameMemo.getWidth();
		heightT = mFrameMemo.getHeight();
		double x = xTouch - (widthT / 2d);
		double y = heightT - yTouch - (heightT / 2d);
		switch (getQuadrant(x, y)) {
		case 1:
			return Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI;
		case 2:
			return 180 - Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI;
		case 3:
			return 180 + (-1 * Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI);
		case 4:
			return 360 + Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI;
		default:
			return 0;
		}
	}

	private static int getQuadrant(double x, double y) {
		if (x >= 0) {
			return y >= 0 ? 1 : 4;
		} else {
			return y >= 0 ? 2 : 3;
		}
	}

	public void resiseBound(float scaleX, float scaleY) {
		int left = (getParam().leftMargin + getParam().width / 2)
				- (int) ((w * scaleX) / 2);
		int top = (getParam().topMargin + getParam().height / 2)
				- (int) ((h * scaleY) / 2);
		if (left < 0 || left > mRelativeLayout.getWidth() - (w * scaleX)
				|| top < 0 || top > mRelativeLayout.getHeight() - (h * scaleY)) {
			return;
		}
		imgBound.setLayoutParams(new RelativeLayout.LayoutParams(
				(int) (w * scaleX), (int) (h * scaleY)));
		getParam().leftMargin = left;
		getParam().topMargin = top;
		imgBound.requestLayout();
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
		return (RelativeLayout.LayoutParams) imgBound.getLayoutParams();
	}

	public void translateBound(double x, double y) {
		int l, t;
		if (getParam().leftMargin + (int) x < 0) {
			l = getParam().leftMargin;
		} else if (getParam().leftMargin + (int) x > mRelativeLayout.getWidth()
				- imgBound.getWidth()) {
			l = mRelativeLayout.getWidth() - imgBound.getWidth();
		} else {
			l = getParam().leftMargin + (int) x;
		}
		if (getParam().topMargin + (int) y < 0) {
			t = getParam().topMargin;
		} else if (getParam().topMargin + (int) y > mRelativeLayout.getHeight()
				- imgBound.getHeight()) {
			t = mRelativeLayout.getHeight() - imgBound.getHeight();
		} else {
			t = getParam().topMargin + (int) y;
		}
		getParam().setMargins(l, t, getParam().rightMargin,
				getParam().bottomMargin);
		imgBound.requestLayout();
	}

	public static void startActivity(Activity activity) {
		Intent i = new Intent(activity, ImageMemoActivity.class);
		activity.startActivity(i);
	}

	public Bitmap snapScreen() {
		mFrameMemo.setDrawingCacheEnabled(true);
		mFrameMemo.measure(
				MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
				MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		mFrameMemo.buildDrawingCache(true);
		Bitmap bm = Bitmap.createBitmap(mFrameMemo.getDrawingCache());
		Bitmap bm2 = Bitmap
				.createBitmap(bm, getParam().leftMargin, getParam().topMargin,
						imgBound.getWidth(), imgBound.getHeight());
		mFrameMemo.setDrawingCacheEnabled(false); //
		return bm2;
	}

	public void onReturn(View v) {
		finish();
	}

	public void onCamera(View v) {
		startCameraIntent();
	}

	public void onLibrary(View v) {
		startGalleryIntent();
	}

	public void onTrash(View v) {
		imageView.setImageBitmap(null);
		imgBound.setVisibility(View.GONE);
		fileUri = null;
	}

	private void startCameraIntent() {
		String mediaStorageDir = Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_PICTURES).getPath();
		@SuppressWarnings("unused")
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US)
				.format(new Date());
		fileUri = Uri.fromFile(new java.io.File(mediaStorageDir
				+ java.io.File.separator + "IMG_" + "test" + ".jpg"));

		Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
		startActivityForResult(cameraIntent, Contants.CAPTURE_IMAGE);
	}

	private void startGalleryIntent() {
		Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
		photoPickerIntent.setType("image/*");
		startActivityForResult(photoPickerIntent, Contants.SELECT_PHOTO);
	}

	@Override
	protected void onActivityResult(final int requestCode,
			final int resultCode, final Intent data) {
		switch (requestCode) {
		case Contants.CAPTURE_IMAGE:
			if (resultCode == Activity.RESULT_OK) {
				Log.e("file Uri	", "file uri " + fileUri);
				if (fileUri != null) {

					String imagePath = fileUri.getPath();
					File file = new File(imagePath);
					if (file.exists()) {
						fileUri = Uri.fromFile(file);
						int orientation = checkOrientation(fileUri);
						bmp = decodeSampledBitmapFromFile(imagePath, 400, 300,
								orientation);
						imageView.setImageBitmap(bmp);
						imgBound.setVisibility(View.VISIBLE);
					} else {
						Log.e("test", "file don't exist !");
					}
				}
			}
			break;
		case Contants.SELECT_PHOTO:
			if (resultCode == RESULT_OK) {
				Log.e("data", "dataat " + data);
				Uri uri = data.getData();
				String[] filePathColumn = { MediaStore.Images.Media.DATA };
				Cursor cursor = getContentResolver().query(uri, filePathColumn,
						null, null, null);
				cursor.moveToFirst();
				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				String imagePath = cursor.getString(columnIndex);
				cursor.close();
				Log.e("test", "imagePath: " + imagePath);

				File file = new File(imagePath);
				if (file.exists()) {
					fileUri = Uri.fromFile(file);
					int orientation = checkOrientation(fileUri);
					bmp = decodeSampledBitmapFromFile(imagePath, 400, 300,
							orientation);
					imageView.setImageBitmap(bmp);
				} else {
					Log.e("test", "file don't exist !");
				}
				if (fileUri == null) {
					imgBound.setVisibility(View.GONE);
				} else {
					imgBound.setVisibility(View.VISIBLE);
				}
				// imageView.setImageBitmap(null);
				// imageView.setImageURI(fileUri);
			}
			break;
		default:
			return;
		}
	}

	public void showToast(final String toast) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(getApplicationContext(), toast,
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	public static Bitmap decodeSampledBitmapFromFile(String filePath,
			int reqWidth, int reqHeight, int orientation) {
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

		return BitmapFactory.decodeFile(filePath, options);

	}

	private void decodeBitmap(Bitmap bitmap, int orientation) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		Matrix mtx = new Matrix();

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
