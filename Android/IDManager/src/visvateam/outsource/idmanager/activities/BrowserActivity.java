package visvateam.outsource.idmanager.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class BrowserActivity extends Activity{

	private WebView webView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_browser);
		
		/*init control*/
		initControl();
	}
	@SuppressLint("SetJavaScriptEnabled")
	private void initControl() {
		// TODO Auto-generated method stub
		webView = (WebView) findViewById(R.id.web_view);
		
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
