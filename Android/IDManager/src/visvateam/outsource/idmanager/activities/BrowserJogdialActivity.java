package visvateam.outsource.idmanager.activities;

import java.util.ArrayList;

import net.sqlcipher.database.SQLiteDatabase;
import visvateam.outsource.idmanager.contants.Contants;
import visvateam.outsource.idmanager.database.DataBaseHandler;
import visvateam.outsource.idmanager.database.IDDataBase;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BrowserJogdialActivity extends Activity {
	WebView webView;
	String url;
	FrameLayout mFrameJogdial;
	ArrayList<String> listId = new ArrayList<String>();
	ArrayList<String> listPass = new ArrayList<String>();
	ArrayList<String> items = new ArrayList<String>();
	private int currentPasswordId;
	private DataBaseHandler mDataBaseHandler;
	private LinearLayout mLinear;
	public String[] idInputWeb;
	private ImageView dialer;
	private int dialerHeight, dialerWidth;
	private MediaPlayer effect_sound;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		currentPasswordId = getIntent().getExtras().getInt(
				Contants.CURRENT_PASSWORD_ID);
		setContentView(R.layout.page_browser_detail);
		mFrameJogdial = (FrameLayout) findViewById(R.id.id_fr_jogdial);
		mLinear = (LinearLayout) findViewById(R.id.id_list_item_copy);
		dialer = (ImageView) findViewById(R.id.id_img_wheel);
		dialer.setOnTouchListener(new OnTouchListener() {
			private double startAngle;
			private int countClockwise = 0;
			private int countUnclockwise = 0;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch (event.getAction() & MotionEvent.ACTION_MASK) {
				case MotionEvent.ACTION_DOWN:
					startAngle = getAngle(event.getX(), event.getY());
					countClockwise = 0;
					countUnclockwise = 0;
					break;
				case MotionEvent.ACTION_UP:
					if (countClockwise > countUnclockwise) {
						pasteJogdial(true);
					} else if (countClockwise < countUnclockwise) {
						pasteJogdial(false);
					}
					break;
				case MotionEvent.ACTION_MOVE:
					double currentAngle = getAngle(event.getX(), event.getY());
					if (((float) (startAngle - currentAngle)) >= 0) {
						countClockwise++;
					} else {
						countUnclockwise++;
					}
					startAngle = currentAngle;
					break;
				}
				return true;
			}
		});
		initData();
		initControl();
	}
	private void playSoundEffect(int _idSound) {
		if (effect_sound == null)
			effect_sound = MediaPlayer.create(this, _idSound);
		if (effect_sound != null ) {
			effect_sound.seekTo(0);
			effect_sound.start();
		}

	}
	public void onReturn(View v) {
		finish();
	}

	private double getAngle(double xTouch, double yTouch) {
		dialerWidth = dialer.getWidth();
		dialerHeight = dialer.getHeight();
		double x = xTouch - (dialerWidth / 2d);
		double y = dialerHeight - yTouch - (dialerHeight / 2d);
		switch (getQuadrant(x, y)) {
		case 1:
			return Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI;
		case 2:
			return 180 - Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI;
		case 3:
			return 180 + (-1 * Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI);
		case 4:
			return 360 + Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI;
		default:
			return 0;
		}
	}

	/**
	 * @return The selected quadrant.
	 */
	private static int getQuadrant(double x, double y) {
		if (x >= 0) {
			return y >= 0 ? 1 : 4;
		} else {
			return y >= 0 ? 2 : 3;
		}
	}

	@SuppressWarnings("deprecation")
	public void initData() {
		SQLiteDatabase.loadLibs(this);
		mDataBaseHandler = new DataBaseHandler(this);
		IDDataBase id = mDataBaseHandler.getId(currentPasswordId);
		url = id.getUrl();
		Drawable d = getResources().getDrawable(R.drawable.jog_upperswitch);
		for (int i = 0; i < CopyItemActivity.MAX_ID; i++) {
			if (!id.getDataId(i + 1).equals("")) {
				LinearLayout mLinearItem = new LinearLayout(this);
				mLinearItem.setLayoutParams(new LinearLayout.LayoutParams(d
						.getIntrinsicWidth(),
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
					items.add(id.getDataId(i + 1));
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

	public void onNote(View v) {

	}

	public void onKeyBoard(View v) {
	}

	public void pasteJogdial(boolean clockwise) {
		playSoundEffect(R.raw.jogwheel);
		if (clockwise) {
			webView.loadUrl("javascript:"
					+ "var nodes=document.querySelectorAll(\"input[type=text],input[type=email],input[type=password]\");var paste="+items+";for (var i = 0; i < nodes.length; i++){nodes[i].value=paste[i];}");
		} else {

		}
	}

	@SuppressLint("SetJavaScriptEnabled")
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
						BrowserJogdialActivity.this), "Android");

			}

		});
	}

	public class JavaScriptInterface {
		Context mContext;

		/** Instantiate the interface and set the context */
		JavaScriptInterface(Context c) {
			mContext = c;
		}

		/** Show a toast from the web page */
		@JavascriptInterface
		public void getInputField(String[] result) {
			idInputWeb = result;
		}
	}
}
