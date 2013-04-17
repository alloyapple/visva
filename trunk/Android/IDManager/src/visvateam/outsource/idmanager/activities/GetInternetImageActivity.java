package visvateam.outsource.idmanager.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.FloatMath;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.ads.AdRequest;
import com.google.ads.AdView;

public class GetInternetImageActivity extends BaseActivity {
	private String URL_SEARCH = "https://www.google.com.vn/#hl=en&output=search&q=$k+e+y+w+o+r+d$";
	private WebView webView;
	private CheckBox mCheckBox;
	private FrameLayout mFrameWebView;
	private ImageView imgBound;
	private EditText editText;
	private EditText editGoogle;
	@SuppressWarnings("unused")
	private static final float MIN_ZOOM = 1f, MAX_ZOOM = 1f;
	static final int NONE = 0;
	static final int DRAG = 1;
	static final int ZOOM = 2;
	int mode = NONE;
	PointF start = new PointF();
	PointF mid = new PointF();
	float oldDist = 1f;
	int l, t, w, h;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_get_icon_of_internet);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		mCheckBox = (CheckBox) findViewById(R.id.id_checkbox_get_icon_internet);
		mCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {

					EditIconActivity.mDrawableIconEdit = (Drawable) new BitmapDrawable(
							snapScreen());
					finish();

				} else {
				}
			}
		});
		editGoogle = ((EditText) findViewById(R.id.id_edit_google_search));
		editGoogle
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event) {
						if (actionId == EditorInfo.IME_ACTION_SEARCH) {
							InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
							mgr.hideSoftInputFromWindow(
									editGoogle.getWindowToken(), 0);
							String keyword = editGoogle.getText().toString();
							if (keyword != null && keyword.length() > 0) {
								webView.loadUrl(URL_SEARCH.replace(
										"$k+e+y+w+o+r+d$", keyword));
								editText.setText(URL_SEARCH.replace(
										"$k+e+y+w+o+r+d$", keyword));
								editGoogle.setText("");
							}
							return true;
						}
						return false;
					}
				});
		mFrameWebView = (FrameLayout) findViewById(R.id.id_linear_webview);
		imgBound = (ImageView) findViewById(R.id.id_img_bound);
		initControl();
		initAdmod();
	}

	public void onGoogleSearch(View v) {
		String keyword = ((EditText) findViewById(R.id.id_edit_google_search))
				.getText().toString();
		if (keyword != null && keyword.length() > 0) {
			webView.loadUrl(URL_SEARCH.replace("$k+e+y+w+o+r+d$", keyword));
			editText.setText(URL_SEARCH.replace("$k+e+y+w+o+r+d$", keyword));
			((EditText) findViewById(R.id.id_edit_google_search)).setText("");
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onAttachedToWindow() {
		// TODO Auto-generated method stub
		super.onAttachedToWindow();
		Display d = getWindowManager().getDefaultDisplay();
		getParam().width = d.getWidth() - 20;
		getParam().height = (int) ((d.getWidth() - 20) * 0.75f);
		getParam().leftMargin = 10;
		getParam().topMargin = 60;
		imgBound.requestLayout();
	}

	public void initAdmod() {
		AdView adview = (AdView) findViewById(R.id.main_adView);
		AdRequest re = new AdRequest();
		if (adview != null) {
			adview.loadAd(re);
			adview.setVisibility(View.VISIBLE);
		}
	}

	public void resiseBound(float scale) {
		int left = (getParam().leftMargin + getParam().width / 2)
				- (int) ((w * scale) / 2);
		int top = (getParam().topMargin + getParam().height / 2)
				- (int) ((h * scale) / 2);
		if (left < 0 || left > mFrameWebView.getWidth() - (w * scale)
				|| top < 0 || top > mFrameWebView.getHeight() - (h * scale)) {
			return;
		}
		imgBound.setLayoutParams(new RelativeLayout.LayoutParams(
				(int) (w * scale), (int) (h * scale)));
		getParam().leftMargin = left;
		getParam().topMargin = top;
		imgBound.requestLayout();
	}

	@SuppressWarnings("unused")
	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return FloatMath.sqrt(x * x + y * y);
	}

	@SuppressWarnings("unused")
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

		finish();

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
