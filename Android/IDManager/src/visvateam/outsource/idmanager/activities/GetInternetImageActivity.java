package visvateam.outsource.idmanager.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.FloatMath;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnTouchListener;

import android.view.inputmethod.EditorInfo;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

public class GetInternetImageActivity extends Activity {
	private WebView webView;
	private CheckBox mCheckBox;
	private FrameLayout mFrameWebView;
	private ImageView imgBound;
	private EditText editText;
	private double x_offset;
	private double y_offset;

	private ScrollView mScrollView;
	private RelativeLayout mRelativeLayout;
//	private LinearLayout mLinearLayout;

	private static final String TAG = "Touch";
	@SuppressWarnings("unused")
	private static final float MIN_ZOOM = 1f, MAX_ZOOM = 1f;

	// These matrices will be used to scale points of the image

	// The 3 states (events) which the user is trying to perform
	static final int NONE = 0;
	static final int DRAG = 1;
	static final int ZOOM = 2;
	int mode = NONE;

	// these PointF objects are used to record the point(s) the user is touching
	PointF start = new PointF();
	PointF mid = new PointF();
	float oldDist = 1f;
	int l, t, w, h;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_get_icon_of_internet);
		mCheckBox = (CheckBox) findViewById(R.id.id_checkbox_get_icon_internet);
		mCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					mRelativeLayout.setVisibility(View.VISIBLE);
					mScrollView.setOnTouchListener(new OnTouchListener() {

						@Override
						public boolean onTouch(View v, MotionEvent event) {

							return true;
						}
					});

				} else {
					mRelativeLayout.setVisibility(View.GONE);
					mScrollView.setOnTouchListener(new OnTouchListener() {

						@Override
						public boolean onTouch(View v, MotionEvent event) {

							return false;
						}
					});
				}
			}
		});

		// mFrameWebView = (FrameLayout) findViewById(R.id.id_frame_webview);
		mFrameWebView = (FrameLayout) findViewById(R.id.id_linear_webview);
		mRelativeLayout = (RelativeLayout) findViewById(R.id.id_frame_bound);
		mRelativeLayout.setVisibility(View.GONE);

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
					Log.d(TAG, "mode=DRAG"); // write to LogCat
					mode = DRAG;

					break;
				case MotionEvent.ACTION_POINTER_DOWN:
					oldDist = spacing(event);
					l = getParam().leftMargin;
					t = getParam().topMargin;
					Log.d(TAG, "oldDist=" + oldDist);
					if (oldDist > 5f) {
						midPoint(mid, event);
						mode = ZOOM;
						Log.d(TAG, "mode=ZOOM");
					}
					break;
				case MotionEvent.ACTION_UP:
					break;
				case MotionEvent.ACTION_POINTER_UP:
					mode = NONE;
					Log.d(TAG, "mode=NONE");
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
		mScrollView = (ScrollView) findViewById(R.id.id_scroll);
		imgBound = (ImageView) findViewById(R.id.id_img_bound);
		initControl();
	}
	public void resiseBound(float scale) {
		int left = (getParam().leftMargin + getParam().width / 2) - (int) ((w * scale) / 2);
		int top = (getParam().topMargin + getParam().height / 2) - (int) ((h * scale) / 2);
		if(left<0||left>mFrameWebView.getWidth()-(w * scale)||top<0||top>mFrameWebView.getHeight()-(h * scale)){
			return;
		}
		imgBound.setLayoutParams(new RelativeLayout.LayoutParams(
				(int) (w * scale), (int) (h * scale)));
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
		} else if (getParam().leftMargin + (int) x > mFrameWebView.getWidth()
				- imgBound.getWidth()) {
			l = mFrameWebView.getWidth() - imgBound.getWidth();
		} else {
			l = getParam().leftMargin + (int) x;
		}
		if (getParam().topMargin + (int) y < 0) {
			t = getParam().topMargin;
		} else if (getParam().topMargin + (int) y > mFrameWebView.getHeight()
				- imgBound.getHeight()) {
			t = mFrameWebView.getHeight() - imgBound.getHeight();
		} else {
			t = getParam().topMargin + (int) y;
		}
		getParam().setMargins(l, t, getParam().rightMargin,
				getParam().bottomMargin);
		imgBound.requestLayout();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			return false;

		default:
			return super.onKeyDown(keyCode, event);
		}

	}

	public boolean onKey(View view, int keyCode, KeyEvent event) {

		if (keyCode == EditorInfo.IME_ACTION_SEARCH
				|| keyCode == EditorInfo.IME_ACTION_DONE
				|| event.getAction() == KeyEvent.ACTION_DOWN
				|| event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

			webView.loadUrl(editText.getText().toString());
		}
		return false; // pass on to other listeners.

	}

	public void onReload(View v) {
		webView.loadUrl(editText.getText().toString());
	}

	public Bitmap snapScreen() {
		mFrameWebView.setDrawingCacheEnabled(true);
		mFrameWebView.measure(
				MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
				MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		mFrameWebView.buildDrawingCache(true);
		Bitmap bm = Bitmap.createBitmap(mFrameWebView.getDrawingCache());
		Bitmap bm2 = Bitmap
				.createBitmap(bm, getParam().leftMargin, getParam().topMargin,
						imgBound.getWidth(), imgBound.getHeight());
		mFrameWebView.setDrawingCacheEnabled(false); //
		return bm2;
	}

	public static void startActivity(Activity activity) {
		Intent i = new Intent(activity, GetInternetImageActivity.class);
		activity.startActivity(i);
	}

	public void onReturn(View v) {
		if (mCheckBox.isChecked()) {
			EditIconActivity.mDrawableIconEdit = (Drawable) new BitmapDrawable(
					snapScreen());
			finish();
		} else {
			finish();
		}
	}

	public void onBroutherBack(View v) {
		webView.goBack();
	}

	public void onBroutherNext(View v) {
		webView.goForward();
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void initControl() {
		// TODO Auto-generated method stub
		editText = ((EditText) findViewById(R.id.id_edit_url));
		editText.setText(EditIdPasswordActivity.mUrlItem);

		webView = (WebView) findViewById(R.id.id_webview_get_icon);

		webView.loadUrl(EditIdPasswordActivity.mUrlItem);

		webView.setContentDescription("application/pdf");
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setLoadsImagesAutomatically(true);
		webSettings.setSupportZoom(true);
		webSettings.setBuiltInZoomControls(true);

		webView.invokeZoomPicker();
		this.webView.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

			@Override
			public void onLoadResource(WebView view, String url) {
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				super.onPageFinished(view, url);
				editText.setText(webView.getUrl());
			}

		});

	}

}
