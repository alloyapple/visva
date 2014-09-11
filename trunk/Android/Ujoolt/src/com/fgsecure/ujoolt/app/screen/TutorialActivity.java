package com.fgsecure.ujoolt.app.screen;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;

import com.fgsecure.ujoolt.app.R;
import com.fgsecure.ujoolt.app.define.ResultCode;
import com.fgsecure.ujoolt.app.utillity.Language;

public class TutorialActivity extends Activity implements OnClickListener {
	private String mUrl;
	private ProgressDialog mSpinner;
	private Button btnCloseTutorial;
	private Button btnShowTutorial;
	private WebView mWebView;
	private LinearLayout layoutWebView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_tutorial);
		setTitle("Ujoolt tutorial");
		initComponent();
	}

	private void initComponent() {
		mUrl = "https://www.google.com.vn/";
		mUrl = "http://mobile.ujoolt.com/comment-ca-marche/";
		btnCloseTutorial = (Button) findViewById(R.id.imgCloseTutorial);
		btnCloseTutorial.setOnClickListener(this);
		btnShowTutorial = (Button) findViewById(R.id.imgShowTutorial);
		btnShowTutorial.setOnClickListener(this);
		layoutWebView = (LinearLayout) findViewById(R.id.layoutWebView);

		mSpinner = new ProgressDialog(this);
		mSpinner.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mSpinner.setMessage(Language.loading);

		setUpWebView();
	}

	private void setUpWebView() {
		mWebView = new WebView(this);
		mWebView.setVerticalScrollBarEnabled(true);
		mWebView.setHorizontalScrollBarEnabled(true);
		mWebView.setWebViewClient(new TutorialWebViewClient());
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.loadUrl(mUrl);
		mWebView.setVisibility(View.INVISIBLE);

		layoutWebView.addView(mWebView);
	}

	private class TutorialWebViewClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
			return true;
		}

		@Override
		public void onReceivedError(WebView view, int errorCode, String description,
				String failingUrl) {
			super.onReceivedError(view, errorCode, description, failingUrl);
			finish();
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			mSpinner.show();
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			mSpinner.dismiss();
			mWebView.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onClick(View v) {
		if (v == btnCloseTutorial) {
			setResult(ResultCode.BACK);
			
		} else if (v == btnShowTutorial) {
			setResult(ResultCode.START_TUTORIAL);
		}
		finish();
	}
}
