package com.android.visva.allinonesdksample.provider;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.visva.allinonesdksample.R;
import com.visva.android.visvasdklibrary.network.HttpMethod;
import com.visva.android.visvasdklibrary.provider.ConnectionProvider;
import com.visva.android.visvasdklibrary.provider.IReponseListener;

public class ConnectionProviderSampleActivity extends Activity {
    private TextView mTxtResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection_provider_sample);
        mTxtResult = (TextView) findViewById(R.id.result);
    }

    public void onClickGetSample(View v) {
        String url = "http://httpbin.org/get";
        ConnectionProvider.getInstance(this).requestDataFromServerAPI(HttpMethod.GET, url, new IReponseListener() {

            @Override
            public void onResponse(String response) {
                // TODO Auto-generated method stub
                mTxtResult.setText(response);
            }

            @Override
            public void onErrorResponse(int errorType, String response) {
                // TODO Auto-generated method stub
                mTxtResult.setText(errorType + response);
            }
        }, null);
    }

    public void onClickPostSample(View v) {
        String url = "http://httpbin.org/post";
        ConnectionProvider.getInstance(this).requestDataFromServerAPI(HttpMethod.POST, url, new IReponseListener() {

            @Override
            public void onResponse(String response) {
                // TODO Auto-generated method stub
                mTxtResult.setText(response);
            }

            @Override
            public void onErrorResponse(int errorType, String response) {
                // TODO Auto-generated method stub
                mTxtResult.setText(errorType + response);
            }
        }, null);
    }

    public void onClickPutSample(View v) {

        String url = "http://httpbin.org/put";
        ConnectionProvider.getInstance(this).requestDataFromServerAPI(HttpMethod.PUT, url, new IReponseListener() {

            @Override
            public void onResponse(String response) {
                // TODO Auto-generated method stub
                mTxtResult.setText(response);
            }

            @Override
            public void onErrorResponse(int errorType, String response) {
                // TODO Auto-generated method stub
                mTxtResult.setText(errorType + response);
            }
        }, null);

    }

    public void onClickDeleteSample(View v) {

        String url = "http://httpbin.org/delete";
        ConnectionProvider.getInstance(this).requestDataFromServerAPI(HttpMethod.DELETE, url, new IReponseListener() {

            @Override
            public void onResponse(String response) {
                // TODO Auto-generated method stub
                mTxtResult.setText(response);
            }

            @Override
            public void onErrorResponse(int errorType, String response) {
                // TODO Auto-generated method stub
                mTxtResult.setText(errorType + response);
            }
        }, null);
    }

    public void onClickGetIPSample(View v) {
        String url = "http://httpbin.org/ip";
        ConnectionProvider.getInstance(this).requestDataFromServerAPI(HttpMethod.GET, url, new IReponseListener() {

            @Override
            public void onResponse(String response) {
                // TODO Auto-generated method stub
                mTxtResult.setText(response);
            }

            @Override
            public void onErrorResponse(int errorType, String response) {
                // TODO Auto-generated method stub
                mTxtResult.setText(errorType + response);
            }
        }, null);
        
    }
}
