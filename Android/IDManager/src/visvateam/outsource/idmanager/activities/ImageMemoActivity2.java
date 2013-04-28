package visvateam.outsource.idmanager.activities;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import visvateam.outsource.idmanager.contants.Contants;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

public class ImageMemoActivity2 extends BaseActivity {
	private Uri fileUri;
	private CheckBox mCheckBoxChoiceImgMemo;
	static final int NONE = 0;
	static final int DRAG = 1;
	static final int ZOOM = 2;
	int mode = NONE;
	private int deltaBound = 10;
	// these PointF objects are used to record the point(s) the user is touching
	PointF start = new PointF();
	PointF start2 = new PointF();
	PointF mid = new PointF();
	float oldDist = 1f;
	int leftB, topB;
	static int widthB, heightB;
	private FrameLayout mFrameMemo;
	Bitmap bmp;
	static Bitmap bmpDraw;
	private Rect rectBmp = new Rect();
	private RectF rectDst = new RectF();
	boolean isBound;
	private int modeBundle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		modeBundle = getIntent().getExtras().getInt("modeBundleMemo");
		setContentView(R.layout.page_memo_2);
		mFrameMemo = (FrameLayout) findViewById(R.id.id_memo_frame);
		mFrameMemo.addView(new MySurface(this));
		mCheckBoxChoiceImgMemo = (CheckBox) findViewById(R.id.check_box_choice_img);
		mCheckBoxChoiceImgMemo
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub
						if (fileUri != null
								&& mCheckBoxChoiceImgMemo.isChecked()
								&& bmp != null) {
							EditIdPasswordActivity2
									.updateMemo((Drawable) new BitmapDrawable(
											snapScreen(leftB, topB, widthB,
													heightB)));
							// Intent resultIntent = new Intent();
							// resultIntent.putExtra(
							// Contants.IS_INTENT_CREATE_NEW_ID, 2);
							// setResult(Activity.RESULT_OK, resultIntent);
							EditIdPasswordActivity2.startActivity(
									ImageMemoActivity2.this, 2);
							finish();
						} else {
							mCheckBoxChoiceImgMemo.setChecked(false);
						}
					}
				});
	}

	public static void startActivity(Activity activity, int value) {
		Intent i = new Intent(activity, ImageMemoActivity2.class);
		i.putExtra("modeBundleMemo", value);
		activity.startActivity(i);
	}

	public Bitmap snapScreen(int l, int t, int w, int h) {
		Bitmap bm2 = Bitmap.createBitmap(bmpDraw, l, t, w, h);
		return bm2;
	}

	public void onReturn(View v) {
		finish();
	}

	public void onCamera(View v) {
		if (bmpDraw == null) {
			widthB = mFrameMemo.getWidth();
			heightB = mFrameMemo.getHeight();
		}
		startCameraIntent();
	}

	public void onLibrary(View v) {
		if (bmpDraw == null) {
			widthB = mFrameMemo.getWidth();
			heightB = mFrameMemo.getHeight();
		}
		startGalleryIntent();
	}

	public void onTrash(View v) {
		bmp = null;
		isBound = false;
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
						bmp = decodeSampledBitmapFromFile(imagePath, widthB,
								heightB, orientation);
						bmpDraw = Bitmap.createBitmap(widthB, heightB,
								Bitmap.Config.ARGB_8888);
						leftB = topB = 0;
						rectBmp.set(0, 0, bmp.getWidth(), bmp.getHeight());
						float ratioH = (float) bmp.getHeight()
								/ bmpDraw.getHeight();
						float ratioW = (float) bmp.getWidth()
								/ bmpDraw.getWidth();
						if (ratioH > ratioW) {
							float w = bmp.getWidth() / ratioH;
							rectDst.set((bmpDraw.getWidth() - w) / 2, 0,
									(bmpDraw.getWidth() - w) / 2 + w,
									bmpDraw.getHeight());
						} else {
							float h = bmp.getHeight() / ratioW;
							rectDst.set(0, (bmpDraw.getHeight() - h) / 2,
									bmpDraw.getWidth(),
									(bmpDraw.getHeight() - h) / 2 + h);
						}
						drawOnBitmap();

					} else {
						Log.e("test", "file don't exist !");
					}
					if (fileUri == null) {
						isBound = false;
					} else {
						isBound = true;
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
					bmpDraw = Bitmap.createBitmap(widthB, heightB,
							Bitmap.Config.ARGB_8888);
					bmp = decodeSampledBitmapFromFile(imagePath, widthB,
							heightB, orientation);
					leftB = topB = 0;
					rectBmp.set(0, 0, bmp.getWidth(), bmp.getHeight());
					float ratioH = (float) bmp.getHeight()
							/ bmpDraw.getHeight();
					float ratioW = (float) bmp.getWidth() / bmpDraw.getWidth();
					if (ratioH > ratioW) {
						float w = bmp.getWidth() / ratioH;
						rectDst.set((bmpDraw.getWidth() - w) / 2, 0,
								(bmpDraw.getWidth() - w) / 2 + w,
								bmpDraw.getHeight());
					} else {
						float h = bmp.getHeight() / ratioW;
						rectDst.set(0, (bmpDraw.getHeight() - h) / 2,
								bmpDraw.getWidth(), (bmpDraw.getHeight() - h)
										/ 2 + h);
					}
					drawOnBitmap();
				} else {
					Log.e("test", "file don't exist !");
				}
				if (fileUri == null) {
					isBound = false;
				} else {
					isBound = true;
				}
			}
			break;
		default:
			return;
		}
	}

	public void drawOnBitmap() {
		Canvas cv = new Canvas(bmpDraw);
		Paint g = new Paint();
		cv.drawBitmap(bmp, rectBmp, rectDst, g);
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

	class MySurface extends SurfaceView implements SurfaceHolder.Callback {
		public MyThread thread;
		float xTouch, yTouch;
		final int L = 0, T = 1, R = 2, B = 3;
		int mode = -1;
		float deltaTouch = 25;

		public MySurface(Context context) {
			super(context);
			getHolder().addCallback(this);
			this.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					if (!isBound)
						return true;
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						xTouch = event.getX();
						yTouch = event.getY();
						if (yTouch >= topB - deltaTouch
								&& yTouch <= topB + deltaBound + deltaTouch) {
							mode = T;
						} else if (xTouch >= leftB - deltaTouch
								&& xTouch <= leftB + deltaBound + deltaTouch) {
							mode = L;

						} else if (yTouch <= topB + heightB + deltaTouch
								&& yTouch >= topB + heightB - deltaBound
										- deltaTouch) {
							mode = B;
						} else if (xTouch <= leftB + widthB + deltaTouch
								&& xTouch >= leftB + widthB - deltaBound
										- deltaTouch) {
							mode = R;
						}
						break;
					case MotionEvent.ACTION_MOVE:
						switch (mode) {
						case L:
							if (leftB + (event.getX() - xTouch) > 0
									&& leftB + (event.getX() - xTouch) <= leftB
											+ widthB - deltaBound) {
								leftB += (event.getX() - xTouch);
								widthB -= (event.getX() - xTouch);
								xTouch = event.getX();
							}
							break;
						case T:
							if (topB + (event.getY() - yTouch) > 0
									&& topB + (event.getY() - yTouch) <= topB
											+ heightB - deltaBound) {
								topB += (event.getY() - yTouch);
								heightB -= (event.getY() - yTouch);
								yTouch = event.getY();
							}
							break;
						case R:
							if (leftB + widthB + (event.getX() - xTouch) >= leftB
									+ deltaBound
									&& leftB + widthB + (event.getX() - xTouch) <= mFrameMemo
											.getWidth()) {
								widthB += (event.getX() - xTouch);
								xTouch = event.getX();
							}
							break;
						case B:
							if (topB + heightB + (event.getY() - yTouch) >= topB
									+ deltaBound
									&& topB + heightB + (event.getY() - yTouch) <= mFrameMemo
											.getHeight()) {
								heightB += (event.getY() - yTouch);
								yTouch = event.getY();
							}
							break;
						default:
							break;
						}
						break;
					case MotionEvent.ACTION_UP:
						mode = -1;
						break;
					default:
						break;
					}
					return true;
				}
			});
			// TODO Auto-generated constructor stub
		}

		@Override
		public void draw(Canvas canvas) {
			// TODO Auto-generated method stub
			Paint g = new Paint();
			g.setColor(Color.WHITE);

			if (canvas != null) {
				canvas.drawRect(0, 0, mFrameMemo.getWidth(),
						mFrameMemo.getHeight(), g);
				if (isBound && bmp != null) {
					canvas.drawBitmap(bmpDraw, 0, 0, g);
					g.setColor(0x80FF0000);
					canvas.drawRect(leftB, topB, leftB + widthB,
							topB + heightB, g);
					drawBound(g, canvas);

				}
			}
		}

		public void drawBound(Paint g, Canvas cv) {
			g.setColor(0xAAFF0000);
			cv.drawRect(leftB, topB, leftB + widthB, topB + deltaBound, g);
			cv.drawRect(leftB, topB, leftB + deltaBound, topB + heightB, g);
			cv.drawRect(leftB + widthB - deltaBound, topB, leftB + widthB, topB
					+ heightB, g);
			cv.drawRect(leftB, topB + heightB - deltaBound, leftB + widthB,
					topB + heightB, g);
		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			// TODO Auto-generated method stub

		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			thread = new MyThread(getHolder(), this);
			thread.setRunning(true);
			thread.start();
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			boolean retry = true;
			thread.setRunning(false);
			while (retry) {
				try {
					thread.join();
					retry = false;
				} catch (InterruptedException e) {
				}
			}

		}
	}

	class MyThread extends Thread {
		private SurfaceHolder myThreadSurfaceHolder;
		private MySurface myThreadSurfaceView;
		private boolean myThreadRun = false;

		public MyThread(SurfaceHolder surfaceHolder, MySurface surfaceView) {
			myThreadSurfaceHolder = surfaceHolder;
			myThreadSurfaceView = surfaceView;
		}

		public void setRunning(boolean b) {
			myThreadRun = b;
		}

		@Override
		public void run() {
			while (myThreadRun) {
				// if (!isPause) {
				Canvas c = null;
				try {
					c = myThreadSurfaceHolder.lockCanvas(null);
					synchronized (myThreadSurfaceHolder) {
						myThreadSurfaceView.draw(c);
					}
				} finally {
					if (c != null) {
						myThreadSurfaceHolder.unlockCanvasAndPost(c);
					}
				}
			}
		}
	}

}
