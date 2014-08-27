package com.sharebravo.bravo.view.fragment;

import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.sharebravo.bravo.R;
import com.sharebravo.bravo.control.activity.HomeActionListener;
import com.sharebravo.bravo.sdk.log.AIOLog;
import com.sharebravo.bravo.sdk.util.network.AsyncHttpGet;
import com.sharebravo.bravo.sdk.util.network.AsyncHttpResponseProcess;
import com.sharebravo.bravo.sdk.util.network.ParameterFactory;
import com.sharebravo.bravo.utils.BravoUtils;
import com.sharebravo.bravo.utils.BravoWebServiceConfig;
import com.sharebravo.bravo.view.adapter.AdapterRecentPost;
import com.sharebravo.bravo.view.lib.PullAndLoadListView;

public class FragmentHomeTab extends FragmentBasic {
    private PullAndLoadListView listviewRecentPost      = null;
    private AdapterRecentPost   adapterRecentPost       = null;
    private HomeActionListener  mHomeActionListener     = null;
    private OnItemClickListener recentPostClickListener = new OnItemClickListener() {

                                                            @Override
                                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                                mHomeActionListener.goToRecentPostDetail();
                                                            }
                                                        };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = (ViewGroup) inflater.inflate(R.layout.page_home_tab, null);

        intializeView(root);

        /* request news */
        requestNewsItemsOnBravoServer();
        return root;

    }

    private void requestNewsItemsOnBravoServer() {
        HashMap<String, String> subParams = new HashMap<String, String>();
        subParams.put("Start", String.valueOf(0));
        JSONObject jsonObject = new JSONObject(subParams);
        String userId = BravoUtils.getUserIdFromUserBravoInfo(getActivity());
        String accessToken = BravoUtils.getAccessTokenFromUserBravoInfo(getActivity());
        String url = BravoWebServiceConfig.URL_GET_NEWS_SEARCH;
        List<NameValuePair> params = ParameterFactory.createSubParamsGetNewsBravoItems(userId, accessToken, jsonObject.toString());
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

    private void intializeView(View root) {
        listviewRecentPost = (PullAndLoadListView) root.findViewById(R.id.listview_recent_post);
        adapterRecentPost = new AdapterRecentPost(getActivity());
        listviewRecentPost.setAdapter(adapterRecentPost);
        listviewRecentPost.setOnItemClickListener(recentPostClickListener);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mHomeActionListener = (HomeActionListener) getActivity();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
