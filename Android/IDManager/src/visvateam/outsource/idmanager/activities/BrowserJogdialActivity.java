package visvateam.outsource.idmanager.activities;

import java.util.ArrayList;

import net.sqlcipher.database.SQLiteDatabase;
import visvateam.outsource.idmanager.contants.Contants;
import visvateam.outsource.idmanager.database.DataBaseHandler;
import visvateam.outsource.idmanager.database.IDDataBase;
import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.FrameLayout;

public class BrowserJogdialActivity extends Activity {
	WebView webView;
	String url;
	FrameLayout mFrameJogdial;
	ArrayList<String> listId = new ArrayList<String>();
	ArrayList<String> listPass = new ArrayList<String>();
	private int currentPasswordId;
	private DataBaseHandler mDataBaseHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		currentPasswordId = getIntent().getExtras().getInt(
				Contants.CURRENT_PASSWORD_ID);
		setContentView(R.layout.page_browser_detail);
		mFrameJogdial=(FrameLayout) findViewById(R.id.id_fr_jogdial);
		mFrameJogdial.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				Log.i("<-------------Touch Jogdial-------->", getClass().toString());
				return false;
			}
		});
		initData();
		initControl();
	}

	public void onReturn(View v) {
		finish();
	}

	public void initData() {
		SQLiteDatabase.loadLibs(this);
		mDataBaseHandler = new DataBaseHandler(this);
		IDDataBase id = mDataBaseHandler.getId(currentPasswordId);
		url = id.getUrl();
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
				((EditText) findViewById(R.id.id_editText_jogdial))
						.setText(webView.getUrl());
			}

		});
	}
	public class WebAppInterface {
	    Context mContext;

	    /** Instantiate the interface and set the context */
	    WebAppInterface(Context c) {
	        mContext = c;
	    }

	    /** Show a toast from the web page */
	    @JavascriptInterface
	    public void showToast(String toast) {
//	        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
	    }
	}
}
