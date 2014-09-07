package com.sharebravo.bravo.view.fragment.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.sharebravo.bravo.R;
import com.sharebravo.bravo.view.fragment.FragmentBasic;

@SuppressLint("SetJavaScriptEnabled")
public class FragmentTermOfUse extends FragmentBasic {
    // =============================Constant Define=====================
    // ============================Control Define =====================
    private WebView mWebView;

    // ============================Class Define =======================

    // ============================Variable Define =====================

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = (ViewGroup) inflater.inflate(R.layout.page_fragment_term_of_use, container);
        mWebView = (WebView) root.findViewById(R.id.web_support);
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.loadUrl("https://www.google.co.uk/"); 
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true); 
        return root; 
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
