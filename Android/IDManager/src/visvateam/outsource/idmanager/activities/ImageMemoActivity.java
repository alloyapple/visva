package visvateam.outsource.idmanager.activities;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import visvateam.outsource.idmanager.contants.Contants;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_memo);
		imageView = (ImageView) findViewById(R.id.img_memo);
		mRelativeLayout = (RelativeLayout) findViewById(R.id.id_relative_bound);
		mFrameMemo= (FrameLayout) findViewById(R.id.id_memo_frame);
		mCheckBoxChoiceImgMemo = (CheckBox) findViewById(R.id.check_box_choice_img);
		mCheckBoxChoiceImgMemo
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub
						if (fileUri != null
								&& mCheckBoxChoiceImgMemo.isChecked()) {
							EditIdPasswordActivity.updateMemo((Drawable) new BitmapDrawable(
									snapScreen()));
							Intent resultIntent = new Intent();
//							resultIntent.putExtra(Contants.FIlE_PATH_IMG_MEMO,
//									fileUri.toString());
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
					w = getParam().width;
					h = getParam().height;
					start.set(event.getX(), event.getY());
					mode = DRAG;

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
						// float scaleX = 0, scaleY = 0;
						// scaleX = Math.abs(event.getX(0) - event.getX(1))
						// / oldDist;
						// scaleY = Math.abs(event.getY(0) - event.getY(1))
						// / oldDist;
						resiseBound(scale, scale);
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
				if (fileUri == null) {
					imgBound.setVisibility(View.GONE);
				} else {
					imgBound.setVisibility(View.VISIBLE);
				}
				imageView.setImageURI(fileUri);
			}
			break;
		case Contants.SELECT_PHOTO:
			if (resultCode == RESULT_OK) {
				Log.e("data", "dataat " + data);
				fileUri = data.getData();
				if (fileUri == null) {
					imgBound.setVisibility(View.GONE);
				} else {
					imgBound.setVisibility(View.VISIBLE);
				}
				imageView.setImageBitmap(null);
				imageView.setImageURI(fileUri);
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

}
