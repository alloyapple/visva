package visvateam.outsource.idmanager.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;

public class BrowserJogdialActivity extends Activity {
	WebView webView;
	String url;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_browser_detail);
		initControl();
	}

	public void onReturn(View v) {
		finish();
	}

	public void onReload(View v) {
		webView.reload();
	}

	public void onNote(View v) {
	}

	public void onKeyBoard(View v) {
	}

	private void initControl() {
		// TODO Auto-generated method stub
		webView = (WebView) findViewById(R.id.webview_jogdial);

		webView.loadUrl("url");

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
				((EditText) findViewById(R.id.id_editText_jogdial)).setText(webView.getUrl());
			}
			
		});
	}

}
