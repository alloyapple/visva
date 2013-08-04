package com.japanappstudio.IDxPassword.activities;

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
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnTouchListener;
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
import com.japanappstudio.IDxPassword.activities.homescreen.HomeScreeenActivity;

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
	private double x_offset;
	private double y_offset;
	PointF start = new PointF();
	PointF mid = new PointF();
	float oldDist = 1f;
	int l, t, w, h;
	private boolean checkFirstChangeWindow = true;

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
					EditIconActivity.startActivity(
							GetInternetImageActivity.this, 2);
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
		imgBound.setOnTouchListener(new OnTouchListener() {
			float scale;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch (event.getAction() & MotionEvent.ACTION_MASK) {
				case MotionEvent.ACTION_DOWN:
					w = getParam().width;
					h = getParam().height;
					start.set(event.getRawX(), event.getRawY());

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
						x_offset = event.getRawX() - start.x;
						y_offset = event.getRawY() - start.y;
						translateBound(x_offset, y_offset);
						start.set(event.getRawX(), event.getRawY());
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
		initControl();
		initAdmod();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (checkFirstChangeWindow) {
			Display d = getWindowManager().getDefaultDisplay();
			int heightP = mFrameWebView.getHeight();

			getParam().width = d.getWidth() * 3 / 4;
			getParam().height = (int) (d.getWidth() * 3 / 4 * 0.75f);
			getParam().leftMargin = (int) (d.getWidth() - getParam().width) / 2;
			getParam().topMargin = (int) (heightP - getParam().height) / 2;
			imgBound.requestLayout();
			checkFirstChangeWindow = false;
		}

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

	@Override
	public void onAttachedToWindow() {
		// TODO Auto-generated method stub
		super.onAttachedToWindow();
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
			onReturn(null);
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
		EditIconActivity.startActivity(this, 2);
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
		editText.setText(mPrefApp.isEditIDxPassHome() ? HomeScreeenActivity.mUrlItem
				: EditIdPasswordActivity.mUrlItem);

		webView = (WebView) findViewById(R.id.id_webview_get_icon);
		if (!mPrefApp.isEditIDxPassHome()) {
			if (EditIdPasswordActivity.mUrlItem != null
					&& !EditIdPasswordActivity.mUrlItem.equals(""))
				webView.loadUrl(EditIdPasswordActivity.mUrlItem);
			else
				webView.loadUrl("https://www.google.com");
		}else{
				if (HomeScreeenActivity.mUrlItem != null
						&& !HomeScreeenActivity.mUrlItem.equals(""))
					webView.loadUrl(HomeScreeenActivity.mUrlItem);
				else
					webView.loadUrl("https://www.google.com");
			
		}
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
