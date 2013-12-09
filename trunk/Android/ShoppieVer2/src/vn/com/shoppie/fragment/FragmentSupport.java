package vn.com.shoppie.fragment;

import com.google.analytics.tracking.android.Log;

import vn.com.shoppie.R;
import vn.com.shoppie.constant.ShoppieSharePref;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

@SuppressLint("SetJavaScriptEnabled")
public class FragmentSupport extends FragmentBasic{
	// =============================Constant Define=====================
	// ============================Control Define =====================
	private WebView mWebView;
	// ============================Class Define =======================
	private ShoppieSharePref mShopieSharePref;

	// ============================Variable Define =====================

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = (ViewGroup) inflater.inflate(
				R.layout.page_support, null);
		mWebView = (WebView)root.findViewById(R.id.web_support);
		mWebView.loadUrl(mShopieSharePref.getParamMobileLink());
		WebSettings webSettings = mWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		return root;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mShopieSharePref = new ShoppieSharePref(getActivity());
	}
	
	public class MyWebViewClient extends WebViewClient {
	    @Override
	    public boolean shouldOverrideUrlLoading(WebView view, String url) {
	        view.loadUrl("https://www.google.co.uk/");
	        return true;
	    }
	}
	@Override
	public void onResume() {
		super.onResume();
	}
}
