package com.japanappstudio.IDxPassword.activities;

import com.japanappstudio.IDxPassword.activities.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import android.widget.EditText;
import android.widget.TextView;

public class SettingURLActivity extends BaseActivity {
	private WebView webView;
	private EditText editGoogle;
	private EditText editUrl;
	private String URL_SEARCH = "https://www.google.com.vn/#hl=en&output=search&q=$k+e+y+w+o+r+d$";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_url_setup);
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
								editUrl.setText(URL_SEARCH.replace(
										"$k+e+y+w+o+r+d$", keyword));
								editGoogle.setText("");
							}
							return true;
						}
						return false;
					}
				});
		initControl();
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
	public static void startActivity(Activity activity) {
		Intent i = new Intent(activity, SettingURLActivity.class);
		activity.startActivity(i);
	}

	public void onReturn(View v) {
		EditIdPasswordActivity.mUrlItem=webView.getUrl();
		EditIdPasswordActivity.startActivity(this, 2);
		finish();
	}

	public void onReload(View v) {
		webView.loadUrl(editUrl.getText().toString());
	}

	public void onBack(View v) {
		webView.goBack();
	}

	public void onNext(View v) {
		webView.goForward();
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void initControl() {
		// TODO Auto-generated method stub
		editUrl = ((EditText) findViewById(R.id.id_edit_url));
		editUrl.setText("http://www.google.com");
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
