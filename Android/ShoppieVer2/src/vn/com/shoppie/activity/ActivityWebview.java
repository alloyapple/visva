package vn.com.shoppie.activity;

import vn.com.shoppie.R;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class ActivityWebview extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_webview);
		
		String url = getIntent().getStringExtra("url");
		((WebView) findViewById(R.id.webview)).loadUrl(url);
	}
}
