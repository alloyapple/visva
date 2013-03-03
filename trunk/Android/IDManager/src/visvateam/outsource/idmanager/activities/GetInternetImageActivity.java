package visvateam.outsource.idmanager.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnTouchListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class GetInternetImageActivity extends Activity {
	private WebView webView;
	private CheckBox mCheckBox;
	private FrameLayout mFrameWebView;
	private ImageView imgBound;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_get_icon_of_internet);
		mCheckBox = (CheckBox) findViewById(R.id.id_checkbox_get_icon_internet);
		// mFrameWebView=(FrameLayout) findViewById(R.id.id_);
		mFrameWebView = (FrameLayout) findViewById(R.id.id_frame_webview);
		mFrameWebView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return false;
			}
		});
		imgBound = (ImageView) findViewById(R.id.id_img_bound);
		initControl();
	}

	public void onReload(View v) {
		webView.reload();
	}

	public Bitmap snapScreen() {
		mFrameWebView.setDrawingCacheEnabled(true);
		// this is the important code :)
		// Without it the view will have a
		// dimension of 0,0 and the bitmap will
		// be null
		mFrameWebView.measure(
				MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
				MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		// v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
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
		});
	}

}
