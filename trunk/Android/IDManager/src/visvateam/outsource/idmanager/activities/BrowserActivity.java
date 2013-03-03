package visvateam.outsource.idmanager.activities;

import visvateam.outsource.idmanager.contants.Contants;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

public class BrowserActivity extends Activity {

	private WebView webView;
	private String url;
	private int mode;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Bundle b = getIntent().getExtras();
		mode = b.getInt(Contants.KEY_TO_BROWSER);
		setContentView(R.layout.page_browser);

		if (mode == Contants.INFO_TO) {
			((Button) findViewById(R.id.id_jogdial)).setVisibility(View.GONE);
			url="http://www.google.com";
		} else {
			((Button) findViewById(R.id.id_jogdial))
					.setVisibility(View.VISIBLE);
			url="http://www.google.com";
		}
		initControl();
	}

	public void onJogdial(View v) {
		Intent i = new Intent(BrowserActivity.this,
				BrowserJogdialActivity.class);
		startActivity(i);
	}

	public void onReturn(View v) {
		finish();
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
		webView = (WebView) findViewById(R.id.web_view);

		webView.loadUrl(url);

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
			}
		});
	}

	public static void startActivity(Activity activity) {
		Intent i = new Intent(activity, BrowserActivity.class);
		activity.startActivity(i);
	}
}
