package visvateam.outsource.idmanager.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import android.view.inputmethod.EditorInfo;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class GetInternetImageActivity extends Activity {
	private WebView webView;
	private CheckBox mCheckBox;
	private FrameLayout mFrameWebView;
	private ImageView imgBound;
	private EditText editText;
	private float x, x_offset;
	private float y, y_offset;
	private boolean move;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_get_icon_of_internet);
		mCheckBox = (CheckBox) findViewById(R.id.id_checkbox_get_icon_internet);
		// mFrameWebView=(FrameLayout) findViewById(R.id.id_);
		mFrameWebView = (FrameLayout) findViewById(R.id.id_frame_webview);
		imgBound = (ImageView) findViewById(R.id.id_img_bound);
//		imgBound.setOnTouchListener(new OnTouchListener() {
//
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				// TODO Auto-generated method stub
//				switch (event.getAction()) {
//				case MotionEvent.ACTION_DOWN:
//					x = event.getX();
//					y = event.getY();
//					move = true;
//					// if (((FrameLayout.LayoutParams)
//					// imgBound.getLayoutParams()).leftMargin < x
//					// && x < ((FrameLayout.LayoutParams) imgBound
//					// .getLayoutParams()).leftMargin
//					// + imgBound.getWidth()
//					// && ((FrameLayout.LayoutParams) imgBound
//					// .getLayoutParams()).topMargin < y
//					// && y < ((FrameLayout.LayoutParams) imgBound
//					// .getLayoutParams()).topMargin
//					// + imgBound.getHeight()) {
//					// move = true;
//					// }
//					break;
//				case MotionEvent.ACTION_MOVE:
//					x_offset = event.getX() - x;
//					y_offset = event.getY() - y;
//					if (move)
//						translateBound(x_offset, y_offset);
//					break;
//				default:
//					break;
//				}
//				return false;
//			}
//		});

		initControl();
	}

	@SuppressLint("NewApi")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			x = event.getX();
			y = event.getY();
			move = false;
			if (imgBound.getX() < x
					&& x < imgBound.getX() + imgBound.getWidth()
					&& imgBound.getY() < y
					&& y < imgBound.getY() + imgBound.getHeight()) {
				move = true;
			}
			break;
		case MotionEvent.ACTION_MOVE:
			x_offset = event.getX() - x;
			y_offset = event.getY() - y;
			Log.i("<----------move------------->", "<---------okie------->");
			if (move)
				translateBound(x_offset, y_offset);
			break;
		default:
			break;
		}
		return true;
//		return super.onTouchEvent(event);
	}

	public void translateBound(float x, float y) {
		((FrameLayout.LayoutParams) imgBound.getLayoutParams()).leftMargin += x;
		((FrameLayout.LayoutParams) imgBound.getLayoutParams()).topMargin += y;
		imgBound.requestFocus();
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
		mFrameWebView
				.layout(((FrameLayout.LayoutParams) imgBound.getLayoutParams()).leftMargin,
						((FrameLayout.LayoutParams) imgBound.getLayoutParams()).topMargin,
						imgBound.getWidth(), imgBound.getHeight());
		mFrameWebView.buildDrawingCache(true);
		Bitmap bm = Bitmap.createBitmap(mFrameWebView.getDrawingCache());
		mFrameWebView.setDrawingCacheEnabled(false); //
		return bm;
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
