package visvateam.outsource.idmanager.activities;

import com.google.ads.AdRequest;
import com.google.ads.AdView;

import net.sqlcipher.database.SQLiteDatabase;
import visvateam.outsource.idmanager.contants.Contants;
import visvateam.outsource.idmanager.idxpwdatabase.ElementID;
import visvateam.outsource.idmanager.idxpwdatabase.IDxPWDataBaseHandler;
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

public class BrowserActivity extends BaseActivity {

	private WebView webView;
	private String url;
	private int mode;
	private int currentElementId;
	private IDxPWDataBaseHandler mDataBaseHandler;
	private EditText editText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Bundle b = getIntent().getExtras();
		mode = b.getInt(Contants.KEY_TO_BROWSER);
		setContentView(R.layout.page_browser);
		currentElementId = getIntent().getExtras().getInt(
				Contants.CURRENT_PASSWORD_ID);

		if (mode == Contants.INFO_TO || mode == Contants.EDIT_TO) {
			url = "http://www.japanappstudio.com/home.html";
		} else {
			initData();
		}
		editText = (EditText) findViewById(R.id.id_edit_browser);
		initControl();
		initAdmod();
	}

	public void initAdmod() {
		AdView adview = (AdView) findViewById(R.id.main_adView);
		AdRequest re = new AdRequest();
		if (adview != null) {
			adview.loadAd(re);
			adview.setVisibility(View.VISIBLE);
		}
	}

	public void initData() {
		SQLiteDatabase.loadLibs(this);
		mDataBaseHandler = new IDxPWDataBaseHandler(this);
		ElementID element = mDataBaseHandler.getElementID(currentElementId);
		url = element.geteUrl();
	}

	public void onJogdial(View v) {
		Intent i = new Intent(BrowserActivity.this,
				BrowserJogdialActivity.class);
		i.putExtra(Contants.CURRENT_PASSWORD_ID, currentElementId);
		startActivity(i);
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
	public void onReturn(View v) {
		if (mode == Contants.EDIT_TO)
			EditIdPasswordActivity2.startActivity(this, 2);
		finish();
	}

	public void onReload(View v) {
		webView.reload();
	}

	public void onBack(View v) {
		webView.goBack();
	}

	@SuppressWarnings("deprecation")
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
