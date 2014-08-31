package com.sharebravo.bravo.view.fragment;

import java.util.List;

import org.apache.http.NameValuePair;

import android.R.integer;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;

import com.sharebravo.bravo.R;
import com.sharebravo.bravo.control.activity.HomeActionListener;
import com.sharebravo.bravo.control.activity.HomeActivity;
import com.sharebravo.bravo.model.response.ObGetBravo;
import com.sharebravo.bravo.sdk.log.AIOLog;
import com.sharebravo.bravo.sdk.util.network.AsyncHttpGet;
import com.sharebravo.bravo.sdk.util.network.AsyncHttpResponseProcess;
import com.sharebravo.bravo.sdk.util.network.ParameterFactory;
import com.sharebravo.bravo.utils.BravoUtils;
import com.sharebravo.bravo.utils.BravoWebServiceConfig;
import com.sharebravo.bravo.view.adapter.AdapterRecentPostDetail;
import com.sharebravo.bravo.view.adapter.DetailPostListener;
import com.sharebravo.bravo.view.lib.PullAndLoadListView;

public class FragmentRecentPostDetail extends FragmentBasic implements DetailPostListener {
    private PullAndLoadListView     listviewRecentPostDetail = null;
    private AdapterRecentPostDetail adapterRecentPostDetail  = null;
    private HomeActionListener      mHomeActionListener      = null;
    private Button                  btnBack;
    private OnItemClickListener     onItemClick              = new OnItemClickListener() {

                                                                 @Override
                                                                 public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                                     // TODO Auto-generated method stub

                                                                 }
                                                             };
    Button                          btnViewMap;
    Button                          btnCallSpot;

    ObGetBravo                      bravoObj;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = (ViewGroup) inflater.inflate(R.layout.page_recent_post_detail_tab,
                null);
        mHomeActionListener = (HomeActivity) getActivity();
        listviewRecentPostDetail = (PullAndLoadListView) root.findViewById(R.id.listview_recent_post_detail);
        adapterRecentPostDetail = new AdapterRecentPostDetail(getActivity());
        adapterRecentPostDetail.setListener(this);
        listviewRecentPostDetail.setFooterDividersEnabled(false);
        listviewRecentPostDetail.setAdapter(adapterRecentPostDetail);
        listviewRecentPostDetail.setOnItemClickListener(onItemClick);
        btnBack = (Button) root.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mHomeActionListener.goToBack();
            }
        });
        return root;
    }

    public void setBravoOb(ObGetBravo obj) {
        this.bravoObj = obj;
        adapterRecentPostDetail.setBravoOb(bravoObj);
        adapterRecentPostDetail.updateCommentList();
    }

    private void requestGetComnents() {
        String userId = BravoUtils.getUserIdFromUserBravoInfo(getActivity());
        String accessToken = BravoUtils.getAccessTokenFromUserBravoInfo(getActivity());
        String url = BravoWebServiceConfig.URL_GET_ALL_BRAVO;
        List<NameValuePair> params = ParameterFactory.createSubParamsGetAllBravoItems(userId, accessToken);
        AsyncHttpGet getLoginRequest = new AsyncHttpGet(getActivity(), new AsyncHttpResponseProcess(getActivity()) {
            @Override
            public void processIfResponseSuccess(String response) {
                AIOLog.d("requestBravoNews:" + response);
            }

            @Override
            public void processIfResponseFail() {
                AIOLog.d("response error");
            }
        }, params, true);
        getLoginRequest.execute(url);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void goToCallSpot() {
        // TODO Auto-generated method stub
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "0973398403"));
        startActivity(intent);
    }

    @Override
    public void goToMapView() {
        // TODO Auto-generated method stub
        mHomeActionListener.goToFragment(HomeActivity.FRAGMENT_MAP_VIEW_ID);
    }

    @Override
    public void goToShare() {
        // TODO Auto-generated method stub

    }

    @Override
    public void goToSave() {
        // TODO Auto-generated method stub

    }

    @Override
    public void goToSubmitComment() {
        // TODO Auto-generated method stub

    }

}
