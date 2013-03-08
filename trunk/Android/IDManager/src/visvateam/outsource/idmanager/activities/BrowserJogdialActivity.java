package visvateam.outsource.idmanager.activities;

import java.util.ArrayList;

import net.sqlcipher.database.SQLiteDatabase;
import visvateam.outsource.idmanager.contants.Contants;
import visvateam.outsource.idmanager.database.DataBaseHandler;
import visvateam.outsource.idmanager.database.IDDataBase;
import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class BrowserJogdialActivity extends Activity {
	WebView webView;
	String url;
	FrameLayout mFrameJogdial;
	ArrayList<String> listId = new ArrayList<String>();
	ArrayList<String> listPass = new ArrayList<String>();
	private int currentPasswordId;
	private DataBaseHandler mDataBaseHandler;
	private LinearLayout mLinear;
	public String[] idInputWeb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		currentPasswordId = getIntent().getExtras().getInt(
				Contants.CURRENT_PASSWORD_ID);
		setContentView(R.layout.page_browser_detail);
		mFrameJogdial = (FrameLayout) findViewById(R.id.id_fr_jogdial);
		mLinear = (LinearLayout) findViewById(R.id.id_list_item_copy);
		mFrameJogdial.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				Log.i("<-------------Touch Jogdial-------->", getClass()
						.toString());
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
		Drawable d=getResources().getDrawable(R.drawable.jog_upperswitch);
		for (int i = 0; i < CopyItemActivity.MAX_ID; i++) {
			if (!id.getDataId(i + 1).equals("")) {
				LinearLayout mLinearItem = new LinearLayout(this);
				mLinearItem.setLayoutParams(new LinearLayout.LayoutParams(
						d.getIntrinsicWidth(),
						LinearLayout.LayoutParams.FILL_PARENT));
				((LinearLayout.LayoutParams) mLinearItem.getLayoutParams()).leftMargin = 6;
				mLinearItem.setBackgroundResource(R.drawable.jog_upperswitch);
				mLinearItem.setOrientation(LinearLayout.VERTICAL);
				{
					TextView textName = new TextView(this);
					textName.setLayoutParams(new LinearLayout.LayoutParams(
							LinearLayout.LayoutParams.FILL_PARENT, 0, 0.5f));
					textName.setTextColor(Color.BLACK);
					textName.setGravity(Gravity.CENTER);
					textName.setText(id.getTitleId(i + 1));
					mLinearItem.addView(textName);

					TextView textContent = new TextView(this);
					textContent.setLayoutParams(new LinearLayout.LayoutParams(
							LinearLayout.LayoutParams.FILL_PARENT, 0, 0.5f));
					textContent.setTextColor(Color.BLACK);
					textContent.setGravity(Gravity.CENTER);
					textContent.setText(id.getDataId(i + 1));
					mLinearItem.addView(textContent);
				}
				mLinear.addView(mLinearItem);
			}
		}
	}

	public void onReload(View v) {
		webView.reload();

	}

	public void onNotes(View v) {

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
				webView.addJavascriptInterface(new JavaScriptInterface(
						BrowserJogdialActivity.this, new String[] { "abc",
								"abc" }), "Android");
				webView.loadUrl("javascript:"
						+ "var nodes=document.querySelectorAll(\"input[type=text],input[type=email],input[type=password]\");for (var i = 0; i < nodes.length; i++){nodes[i].value=\'abc\';}");

			}

		});
	}

	public class JavaScriptInterface {
		Context mContext;
		String[] a;

		/** Instantiate the interface and set the context */
		JavaScriptInterface(Context c, String[] p) {
			mContext = c;
			a = p;
		}

		/** Show a toast from the web page */
		@JavascriptInterface
		public void getInputField(String[] result) {
			idInputWeb = result;
		}
	}
}
