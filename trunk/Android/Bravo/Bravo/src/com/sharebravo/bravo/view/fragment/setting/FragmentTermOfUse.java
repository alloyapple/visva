package com.sharebravo.bravo.view.fragment.setting;

import android.annotation.SuppressLint;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;

import com.sharebravo.bravo.R;
import com.sharebravo.bravo.control.activity.HomeActionListener;
import com.sharebravo.bravo.control.activity.HomeActivity;
import com.sharebravo.bravo.sdk.util.network.NetworkUtility;
import com.sharebravo.bravo.utils.BravoWebServiceConfig;
import com.sharebravo.bravo.view.fragment.FragmentBasic;

@SuppressLint("SetJavaScriptEnabled")
public class FragmentTermOfUse extends FragmentBasic {

    private WebView            mWebView;
    private Button             btnBack;
    private HomeActionListener mHomeActionListener;
    private LinearLayout       mLayoutPoorConnection;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = (ViewGroup) inflater.inflate(R.layout.page_fragment_term_of_use, container);
        mLayoutPoorConnection = (LinearLayout) root.findViewById(R.id.layout_poor_connection);
        if (NetworkUtility.getInstance(getActivity()).isNetworkAvailable()) {
            mLayoutPoorConnection.setVisibility(View.GONE);
        } else {
            mLayoutPoorConnection.setVisibility(View.VISIBLE);
        }
        mWebView = (WebView) root.findViewById(R.id.web_support);
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.loadUrl(BravoWebServiceConfig.URL_BRAVO_RULE);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        btnBack = (Button) root.findViewById(R.id.btn_back);
        mHomeActionListener = (HomeActivity) getActivity();
        btnBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mHomeActionListener.goToBack();
            }
        });
        return root;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return true;
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed(); // Ignore SSL certificate errors
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
