package com.fgsecure.ujoolt.app.twitter;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fgsecure.ujoolt.app.R;
import com.fgsecure.ujoolt.app.screen.MainScreenActivity;
import com.fgsecure.ujoolt.app.twitter.TwitterApp.TwDialogListener;
import com.fgsecure.ujoolt.app.utillity.Language;

/**
 * Modified from FbDialog from Facebook SDK
 * 
 * @author Lorensius W. L. T <lorenz@londatiga.net>
 */
public class TwitterDialog extends Dialog {

	static final float[] DIMENSIONS_LANDSCAPE = { 460, 260 };
	static final float[] DIMENSIONS_PORTRAIT = { 280, 420 };
	static final FrameLayout.LayoutParams FILL = new FrameLayout.LayoutParams(
			ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
	static final int MARGIN = 4;
	static final int PADDING = 2;
	LinearLayout webViewContainer;

	private String mUrl;
	private TwDialogListener mListener;
	private ProgressDialog mSpinner;
	private WebView mWebView;
	private ImageView mCrossImage;
	private FrameLayout mContent;
	// private TextView mTitle;
	public MainScreenActivity mainScreenActivity;

	private final String TAG = getClass().getSimpleName();

	public TwitterDialog(Context context, String url, TwDialogListener listener) {
		super(context, android.R.style.Theme_Translucent_NoTitleBar);
		mUrl = url;
		mListener = listener;
		mainScreenActivity = (MainScreenActivity) context;
		webViewContainer = new LinearLayout(getContext());
		// webViewContainer.setOrientation(LinearLayout.VERTICAL);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mSpinner = new ProgressDialog(getContext());
		mSpinner.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mSpinner.setMessage(Language.loading);

		// mContent = new LinearLayout(getContext());
		//
		// mContent.setOrientation(LinearLayout.VERTICAL);
		mContent = new FrameLayout(getContext());

		// setUpTitle();
		createCrossImage();
		int crossWidth = mCrossImage.getDrawable().getIntrinsicWidth();
		setUpWebView(crossWidth / 2);

		mContent.addView(mCrossImage, new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		addContentView(mContent, new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));

	}

	// private void setUpTitle() {
	// requestWindowFeature(Window.FEATURE_NO_TITLE);
	//
	// mTitle = new TextView(getContext());
	//
	// mTitle.setText("Twitter");
	// mTitle.setTextColor(Color.WHITE);
	// mTitle.setTypeface(Typeface.DEFAULT_BOLD);
	// mTitle.setBackgroundColor(0xFFbbd7e9);
	// mTitle.setPadding(MARGIN + PADDING, MARGIN, MARGIN, MARGIN);
	// mTitle.setCompoundDrawablePadding(MARGIN + PADDING);
	//
	// // mContent.addView(mTitle);
	// webViewContainer.addView(mTitle);
	// mContent.addView(webViewContainer);
	// }

	private void setUpWebView(int margin) {
		mWebView = new WebView(getContext());
		mWebView.clearSslPreferences();
		mWebView.clearCache(true);
		mWebView.setVerticalScrollBarEnabled(false);
		mWebView.setHorizontalScrollBarEnabled(false);
		mWebView.setWebViewClient(new TwitterWebViewClient());
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.loadUrl(mUrl);
		mWebView.setLayoutParams(FILL);
		// mWebView.
		// mContent.setPadding(margin, margin, margin, margin);
		// mContent.addView(mWebView);
		webViewContainer.setPadding(margin, margin, margin, margin);
		webViewContainer.addView(mWebView);
		mContent.addView(webViewContainer);
	}

	private void createCrossImage() {
		mCrossImage = new ImageView(getContext());
		// Dismiss the dialog when user click on the 'x'
		mCrossImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mainScreenActivity.isLoading = false;
				mainScreenActivity.isStartFacebookConnect = false;
				// mListener.onCancel();
				TwitterDialog.this.dismiss();
				// mainScreenActivity.view_login.setVisibility(View.VISIBLE);
			}
		});
		Drawable crossDrawable = getContext().getResources().getDrawable(R.drawable.close);
		mCrossImage.setImageDrawable(crossDrawable);
		/*
		 * 'x' should not be visible while webview is loading make it visible
		 * only after webview has fully loaded
		 */
		mCrossImage.setVisibility(View.VISIBLE);
	}

	private class TwitterWebViewClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			Log.d(TAG, "Redirecting URL " + url);

			if (url.startsWith(TwitterApp.CALLBACK_URL)) {
				mListener.onComplete(url);

				TwitterDialog.this.dismiss();

				return true;
			} else if (url.startsWith("authorize")) {
				// mainScreenActivity.button_share_twitter.setEnabled(false);
				return false;
			}

			return true;
		}

		@Override
		public void onReceivedError(WebView view, int errorCode, String description,
				String failingUrl) {
			Log.d(TAG, "Page error: " + description);

			super.onReceivedError(view, errorCode, description, failingUrl);
			// mainScreenActivity.button_share_twitter.setEnabled(false);
			mListener.onError(description);
			TwitterDialog.this.dismiss();
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			Log.d(TAG, "Loading URL: " + url);
			super.onPageStarted(view, url, favicon);
			mSpinner.show();
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			// String title = mWebView.getTitle();
			// if (title != null && title.length() > 0) {
			// mTitle.setText(title);
			// }
			mSpinner.dismiss();
			mainScreenActivity.isStartTwitterConnect = false;
			mainScreenActivity.isLoading = false;
			mainScreenActivity.setIconSocialNetwork();
			if (!mainScreenActivity.twitterConnector.mTwitterAuthen.hasAccessToken()) {
				mainScreenActivity.toggleButtonShareTwitter.setChecked(false);
			}
			// mainScreenActivity.setToggleShare();
		}

	}
}