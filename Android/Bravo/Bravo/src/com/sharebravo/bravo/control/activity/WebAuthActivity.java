package com.sharebravo.bravo.control.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.sharebravo.bravo.MyApplication;
import com.sharebravo.bravo.R;
import com.sharebravo.bravo.utils.BravoConstant;

public class WebAuthActivity extends Activity {
    private static String  TAG = "WebAuthActivity";
    private WebView        mWebView;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webauth);
        mWebView = (WebView) findViewById(R.id.webauth_activity);
        mWebView.clearCache(true);
        mWebView.destroyDrawingCache();
        mWebView.setWebViewClient(new MyWebViewClient(this));
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setSupportZoom(true);
        setProgressBarIndeterminateVisibility(true);
        setProgressBarVisibility(true);
        String url = getIntent().getExtras().getString("URL");
        mWebView.loadUrl(url);
        mProgressDialog = ProgressDialog.show(WebAuthActivity.this, "Authorize", "Loading...");
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
            mProgressDialog.show();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
        }

        private boolean IsIgnoreWebsite(String url)
        {
            Log.d(TAG, url);
            if (url.startsWith(BravoConstant.TWITTER_CALLBACK_URL)) {
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
                return true;
            }
            return false;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d(TAG, "shouldOverrideUrlLoading = " + url);
            if (url.startsWith(BravoConstant.TWITTER_CALLBACK_URL))
            {
//                Uri uri = Uri.parse(url);
//                String verifier = uri.getQueryParameter("oauth_verifier");
//                
//                /*save oauth_verifier to share preferences*/
//                MyApplication.getInstance().getBravoSharePrefs().putIntValue(BravoConstant.LOGIN_SNS_TYPE, BravoConstant.LOGIN_BY_TWITTER);
//                MyApplication.getInstance().getBravoSharePrefs().putIntValue(BravoConstant.REGISTER_SNS_TYPE, BravoConstant.REGISTER_BY_TWITTER);
//                MyApplication.getInstance().getBravoSharePrefs().putStringValue(BravoConstant.PREF_KEY_TWITTER_OAUTH_VERIFIER, verifier);
//                Intent intent = new Intent();
//                intent.setData(uri);
//                intent.putExtra(BravoConstant.LOGIN_SNS_TYPE, BravoConstant.LOGIN_BY_TWITTER);
//                setResult(RESULT_OK, intent);
//                if (mProgressDialog.isShowing()) {
//                    mProgressDialog.dismiss();
//                }
//                context.finish();
                return true;
            }
            return true;

        }
    }
}
