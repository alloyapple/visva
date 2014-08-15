package com.samsung.android.alwayssocial.activity;

import com.samsung.android.alwayssocial.R;
import com.samsung.android.alwayssocial.constant.TwitterConstant;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebAuthActivity extends Activity {
    private static String TAG = "WebAuthActivity";
    WebView mWebView;
    int miServiceType;
    private ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webauth_activity);
        //final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        mWebView = (WebView) findViewById(R.id.webauth_activity);
        mWebView.clearCache(true);
        mWebView.destroyDrawingCache();
        //mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new MyWebViewClient(this));
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setSupportZoom(true);
        setProgressBarIndeterminateVisibility(true);
        setProgressBarVisibility(true);
        String url = getIntent().getExtras().getString("URL");
        mWebView.loadUrl(url);
        progressBar = ProgressDialog.show(WebAuthActivity.this, "Authorize", "Loading...");
    }

    private class MyWebViewClient extends WebViewClient {
        Activity context;

        MyWebViewClient(Activity context) {
            this.context = context;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            if (IsIgnoreWebsite(url)) {
                view.stopLoading();
                context.finish();
                return;
            }
            progressBar.show();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (progressBar.isShowing()) {
                progressBar.dismiss();
            }
        }

        private boolean IsIgnoreWebsite(String url)
        {
            Log.d(TAG, url);
            if (url.startsWith(TwitterConstant.TWITTER_CALLBACK_URL))
            {
                //Uri uri = Uri.parse(url);
                if (progressBar.isShowing()) {
                    progressBar.dismiss();
                }

                return true;
            }
            return false;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d(TAG, "shouldOverrideUrlLoading = " + url);
            if (url.startsWith(TwitterConstant.TWITTER_CALLBACK_URL))
            {
                //Uri uri = Uri.parse(url);
                //String verifier = uri.getQueryParameter("oauth_verifier");
                //Log.d(TAG)
                //                Intent intent = new Intent();
                //                intent.setData(uri);
                //                setResult(RESULT_OK, intent);
                //                if (progressBar.isShowing()) {
                //                    progressBar.dismiss();
                //                }
                //context.finish();
                return true;
            }
            return true;

        }
    }
}
