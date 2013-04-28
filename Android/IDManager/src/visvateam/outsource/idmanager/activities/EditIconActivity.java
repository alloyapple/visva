package visvateam.outsource.idmanager.activities;

import com.google.ads.AdRequest;
import com.google.ads.AdView;

import visvateam.outsource.idmanager.contants.Contants;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PointF;
//import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
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
					EditIdPasswordActivity2
							.updateIcon((Drawable) new BitmapDrawable(
									snapScreen()));
					EditIdPasswordActivity2.startActivity(
							EditIconActivity.this, 2);
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
		if (modeBundle == 1)
			mDrawableIconEdit = EditIdPasswordActivity2.getIcon();
		initAdmod();

	}

	@SuppressWarnings("deprecation")
	@Override
	public void onAttachedToWindow() {
		// TODO Auto-generated method stub
		super.onAttachedToWindow();
		Display d = getWindowManager().getDefaultDisplay();

		getParamBound().width = d.getWidth();
		getParamBound().height = (int) (d.getWidth() * 0.75f);
		imgBound.requestLayout();

		getParam().width = d.getWidth();
		getParam().height = (int) (d.getWidth() * 0.75f);
		getParam().leftMargin = 0;
		imageView.requestLayout();
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
		Bitmap bm = Bitmap.createBitmap(mRelativeLayout.getDrawingCache());
		Bitmap bm2 = Bitmap.createBitmap(bm, getParamBound().leftMargin,
				getParamBound().topMargin, imgBound.getWidth(),
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
		AdView adview = (AdView) findViewById(R.id.main_adView);
		AdRequest re = new AdRequest();
		if (adview != null) {
			adview.loadAd(re);
			adview.setVisibility(View.VISIBLE);
		}
	}

	public void initDataItem() {

	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		imageView.setBackgroundDrawable(mDrawableIconEdit);
		if (fileUri != null)
			imageView.setBackgroundColor(Color.TRANSPARENT);
	}

	public static void startActivity(Activity activity,int value) {
		Intent i = new Intent(activity, EditIconActivity.class);
		i.putExtra("modeBundleEditIcon", value);
		activity.startActivity(i);
	}

	public void onReturn(View v) {
		ListIconActivity.startActivity(this);
		finish();

	}

	public void onLibrary(View v) {
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

	@SuppressLint("NewApi")
	@Override
	protected void onActivityResult(final int requestCode,
			final int resultCode, final Intent data) {
		switch (requestCode) {

		case Contants.SELECT_PHOTO:
			if (resultCode == RESULT_OK) {
				Log.e("data", "dataat " + data);
				fileUri = data.getData();
				imageView.requestLayout();
				imageView.setImageBitmap(null);
				imageView.setImageURI(fileUri);
			}
			break;
		default:
			return;
		}
	}

}
