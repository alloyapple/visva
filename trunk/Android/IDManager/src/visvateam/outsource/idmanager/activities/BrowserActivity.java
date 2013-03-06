package visvateam.outsource.idmanager.activities;

import net.sqlcipher.database.SQLiteDatabase;
import visvateam.outsource.idmanager.contants.Contants;
import visvateam.outsource.idmanager.database.DataBaseHandler;
import visvateam.outsource.idmanager.database.IDDataBase;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

public class BrowserActivity extends Activity {

	private WebView webView;
	private String url;
	private int mode;
	private int currentPasswordId;
	private DataBaseHandler mDataBaseHandler;
	private EditText editText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Bundle b = getIntent().getExtras();
		mode = b.getInt(Contants.KEY_TO_BROWSER);
		setContentView(R.layout.page_browser);
		currentPasswordId = getIntent().getExtras().getInt(
				Contants.CURRENT_PASSWORD_ID);

		if (mode == Contants.INFO_TO) {
			((Button) findViewById(R.id.id_jogdial)).setVisibility(View.GONE);
			url = "http://www.google.com";
		} else {
			((Button) findViewById(R.id.id_jogdial))
					.setVisibility(View.VISIBLE);
			initData();
		}
		editText = (EditText) findViewById(R.id.id_edit_browser);
		initControl();
	}

	public void initData() {
		SQLiteDatabase.loadLibs(this);
		mDataBaseHandler = new DataBaseHandler(this);
		IDDataBase id = mDataBaseHandler.getId(currentPasswordId);
		url = id.getUrl();
	}

	public void onJogdial(View v) {
		Intent i = new Intent(BrowserActivity.this,
				BrowserJogdialActivity.class);
		i.putExtra(Contants.CURRENT_PASSWORD_ID, currentPasswordId);
		startActivity(i);
	}

	public void onReturn(View v) {
		finish();
	}

	public void onReload(View v) {
		webView.reload();
	}

	public void onBack(View v) {
		webView.goBack();
	}

	public void onNext(View v) {
		try {
			KeyEvent shiftPressEvent = new KeyEvent(0, 0, KeyEvent.ACTION_DOWN,
					KeyEvent.KEYCODE_SHIFT_RIGHT, 0, 0);
			shiftPressEvent.dispatch(webView);
		} catch (Exception e) {
			throw new AssertionError(e);
		}
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
				editText.setText(webView.getUrl());
				super.onPageFinished(view, url);
			}
		});

	}

	public static void startActivity(Activity activity) {
		Intent i = new Intent(activity, BrowserActivity.class);
		activity.startActivity(i);
	}
}
