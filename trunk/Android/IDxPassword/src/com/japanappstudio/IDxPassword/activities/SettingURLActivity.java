package com.japanappstudio.IDxPassword.activities;

import com.japanappstudio.IDxPassword.activities.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class SettingURLActivity extends BaseActivity {
	private WebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.e("adfdshf", "adfjhf ");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_url_setup);
		initControl();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			Log.e("url	", "web url "+webView.getUrl());
			EditIdPasswordActivity.mUrlItem = webView.getUrl();
			EditIdPasswordActivity.startActivity(this, 2);
			finish();
			return false;

		default:
			return super.onKeyDown(keyCode, event);
		}

	}

	public static void startActivity(Activity activity) {
		Intent i = new Intent(activity, SettingURLActivity.class);
		activity.startActivity(i);
	}

	public void onReturn(View v) {
		EditIdPasswordActivity.mUrlItem = webView.getUrl();
		EditIdPasswordActivity.startActivity(this, 2);
		finish();
	}

	public void onRefresh(View v) {
		webView.reload();
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
		webView = (WebView) findViewById(R.id.id_webview_url_setup);
		webView.loadUrl("http://www.google.com");

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
