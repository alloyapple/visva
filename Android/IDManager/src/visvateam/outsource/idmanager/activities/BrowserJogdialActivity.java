package visvateam.outsource.idmanager.activities;

import java.util.ArrayList;

import visvateam.outsource.idmanager.database.IdManagerPreference;

import com.google.ads.AdRequest;
import com.google.ads.AdView;

import net.sqlcipher.database.SQLiteDatabase;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BrowserJogdialActivity extends BaseActivity {
	private static int deltaX = 20;
	private static int deltaY = 20;
	private WebView webView;
	private String url;
	private ArrayList<Item> itemList = new ArrayList<Item>();
	private ArrayList<String> items = new ArrayList<String>();
	private String[] pasteItem;
	private LinearLayout mLinear;
	public String[] idInputWeb;
	private ImageView dialer;
	private int dialerHeight, dialerWidth;
	private MediaPlayer effect_sound;
	private Button mBtnJogdial;
	private LinearLayout mLinearBottom;
	private FrameLayout mFrameJogdial;
	private boolean isJogdial = false;
	private String valuePaste = "khaidt.hut@gmail";
	int count;
	int currentField = 0;
	int currentData = 0;
	String note;
	String valueGet;
	PointF startPoint = new PointF();
	private AdView adview;
	private IdManagerPreference mIdManagerPreference;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		itemList = CopyItemActivity.itemList;
		url = getIntent().getExtras().getString(CopyItemActivity.KEY_URL);
		note = getIntent().getExtras().getString(CopyItemActivity.KEY_NOTE);
		setContentView(R.layout.page_browser_jogdial);
		mLinear = (LinearLayout) findViewById(R.id.id_list_item_copy);
		dialer = (ImageView) findViewById(R.id.id_img_wheel);
		mBtnJogdial = (Button) findViewById(R.id.id_jogdial);
		mFrameJogdial = (FrameLayout) findViewById(R.id.id_frame_jogdial);
		mLinearBottom = (LinearLayout) findViewById(R.id.id_linear_bottom_browser);
		mFrameJogdial.setVisibility(View.GONE);

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
					startPoint.set(event.getX(), event.getY());
					break;
				case MotionEvent.ACTION_UP:
					if (checkPoint(startPoint)
							&& checkPoint(new PointF(event.getX(), event.getY()))) {
						nextInput();
					} else {
						if (countClockwise > countUnclockwise) {
							pasteJogdial(true);
						} else if (countClockwise < countUnclockwise) {
							pasteJogdial(false);
						}
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
		mIdManagerPreference = IdManagerPreference.getInstance(this);
		initData();
		initControl();
		initAdmod();
	}

	public void initAdmod() {
		adview = (AdView) findViewById(R.id.main_adView);
		AdRequest re = new AdRequest();
		if (adview != null) {
			adview.loadAd(re);
			if (!mIdManagerPreference.getIsPaymentNoAd())
				adview.setVisibility(View.VISIBLE);
			else
				adview.setVisibility(View.GONE);
		}
	}

	public boolean checkPoint(PointF p) {
		if (dialer.getWidth() / 2 - deltaX < p.x
				&& dialer.getWidth() / 2 + deltaX > p.x
				&& dialer.getHeight() / 2 - deltaY < p.y
				&& dialer.getHeight() / 2 + deltaY > p.y)
			return true;
		return false;
	}

	public void onJogdial(View v) {
		isJogdial = !isJogdial;
		if (isJogdial) {
			mFrameJogdial.setVisibility(View.VISIBLE);
			mLinearBottom.setVisibility(View.GONE);
			mBtnJogdial.setBackgroundResource(R.drawable.btn_wheel_false);
			InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			mgr.hideSoftInputFromWindow(webView.getWindowToken(), 0);
		} else {
			mFrameJogdial.setVisibility(View.GONE);
			mLinearBottom.setVisibility(View.VISIBLE);
			mBtnJogdial.setBackgroundResource(R.drawable.btn_wheel);
		}
	}

	public void onBack(View v) {
		webView.goBack();
		currentField = 0;
		currentData = 0;
	}

	public void onNext(View v) {
		webView.goForward();
		currentField = 0;
		currentData = 0;
	}

	public void nextInput() {
		if (currentField < count - 1)
			currentField++;
		webView.loadUrl("javascript:"
				+ "var nodes=document.querySelectorAll(\"input[type=\"text\"],input[type=email],input[type=password]\"); var k="
				+ currentField + ";Android.getValueField(nodes[k].value);");
		webView.loadUrl("javascript:"
				+ "var nodes=document.querySelectorAll(\"input[type=text],input[type=email],input[type=password]\"); var k="
				+ currentField + ";nodes[k].focus();nodes[k].scrollIntoView()");

	}

	private void playSoundEffect(int _idSound) {
		if (effect_sound == null)
			effect_sound = MediaPlayer.create(this, _idSound);
		if (effect_sound != null) {
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
		Drawable d = getResources().getDrawable(R.drawable.jog_upperswitch);
		for (int i = 0; i < itemList.size(); i++) {
			if (!itemList.get(i).mContentItem.equals("")) {
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
					textName.setText(itemList.get(i).mNameItem);
					mLinearItem.addView(textName);

					TextView textContent = new TextView(this);
					textContent.setLayoutParams(new LinearLayout.LayoutParams(
							LinearLayout.LayoutParams.FILL_PARENT, 0, 0.5f));
					textContent.setTextColor(Color.BLACK);
					textContent.setGravity(Gravity.CENTER);
					items.add(itemList.get(i).mContentItem);
					textContent.setText(itemList.get(i).mContentItem);
					mLinearItem.addView(textContent);
				}
				mLinear.addView(mLinearItem);
			}
		}
		pasteItem = new String[items.size()];
		for (int i = 0; i < items.size(); i++) {
			pasteItem[i] = items.get(i);
		}
	}

	public void onReload(View v) {
		webView.reload();

	}

	public void onNote(View v) {
		if (note == null || note.equals("")) {
			return;
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getResources().getString(R.string.hint_note));
		builder.setIcon(R.drawable.icon);
		builder.setMessage(note);
		builder.setPositiveButton(
				getResources().getString(R.string.confirm_cancel),
				new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

					}
				});
		builder.create().show();
	}

	public void onKeyBoard(View v) {
		webView.setFocusableInTouchMode(true);
		webView.setFocusable(true);
		webView.requestFocus(View.FOCUS_DOWN);
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				isJogdial = false;
				webView.loadUrl("javascript:");
				InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				mgr.showSoftInput(webView, InputMethodManager.SHOW_IMPLICIT);
				mFrameJogdial.setVisibility(View.GONE);
				mBtnJogdial.setBackgroundResource(R.drawable.btn_wheel);
			}
		});
	}

	public void pasteJogdial(boolean clockwise) {
		playSoundEffect(R.raw.jogwheel);
		if (clockwise) {

			if (currentField >= count || currentField < 0)
				return;
			// valuePaste = pasteItem[currentField];
			if (currentData >= pasteItem.length || currentData < 0)
				return;
			valuePaste = pasteItem[currentData];
			// else {
			// valuePaste = "";
			// return;
			// }
			webView.loadUrl("javascript:"
					+ "var nodes=document.querySelectorAll(\"input[type=\"text\"],input[type=email],input[type=password]\"); var k="
					+ currentField + ";Android.getValueField(nodes[k].value);");

			webView.loadUrl("javascript:"
					+ "var nodes=document.querySelectorAll(\"input[type=text],input[type=email],input[type=password]\"); var k="
					+ currentField + ";nodes[k].value=\"" + valuePaste + "\";");

			if (currentField < count - 1) {
				currentField++;
				webView.loadUrl("javascript:"
						+ "var nodes=document.querySelectorAll(\"input[type=text],input[type=email],input[type=password]\"); var k="
						+ currentField
						+ ";nodes[k].focus();nodes[k].scrollIntoView()");
			}
			if (currentData < pasteItem.length - 1)
				currentData++;

		} else {
			if (currentField >= count || currentField < 0)
				return;
			valuePaste = "";
			webView.loadUrl("javascript:"
					+ "var nodes=document.querySelectorAll(\"input[type=\"text\"],input[type=email],input[type=password]\"); var k="
					+ currentField + ";Android.getValueField(nodes[k].value);");

			webView.loadUrl("javascript:"
					+ "var nodes=document.querySelectorAll(\"input[type=text],input[type=email],input[type=password]\"); var k="
					+ currentField + ";nodes[k].value=\"" + valuePaste + "\";");

			if (currentField > 0) {
				currentField--;
				webView.loadUrl("javascript:"
						+ "var nodes=document.querySelectorAll(\"input[type=text],input[type=email],input[type=password]\"); var k="
						+ currentField
						+ ";nodes[k].focus();nodes[k].scrollIntoView()");
			}
			if (currentData > 0)
				currentData--;

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
				webView.loadUrl("javascript:"
						+ "var nodes=document.querySelectorAll(\"input[type=\"text\"],input[type=email],input[type=password]\");Android.count(nodes.length);");
				webView.loadUrl("javascript:"
						+ "var nodes=document.querySelectorAll(\"input[type=text],input[type=email],input[type=password]\");Android.count(nodes.length);");
				currentField = 0;
				currentData = 0;
			}

		});
	}

	public class JavaScriptInterface {
		Context mContext;

		/** Instantiate the interface and set the context */
		JavaScriptInterface(Context c) {
			mContext = c;
		}

		public void count(int c) {
			count = c;
		}

		/** Show a toast from the web page */
		@JavascriptInterface
		public void getValueField(String v) {
			valueGet = v;
		}
	}

}
