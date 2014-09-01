package com.sharebravo.bravo.view.fragment;

import java.util.List;

import org.apache.http.NameValuePair;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sharebravo.bravo.R;
import com.sharebravo.bravo.control.activity.HomeActionListener;
import com.sharebravo.bravo.model.response.ObGetAllBravoRecentPosts;
import com.sharebravo.bravo.sdk.log.AIOLog;
import com.sharebravo.bravo.sdk.util.network.AsyncHttpGet;
import com.sharebravo.bravo.sdk.util.network.AsyncHttpResponseProcess;
import com.sharebravo.bravo.sdk.util.network.ParameterFactory;
import com.sharebravo.bravo.utils.BravoUtils;
import com.sharebravo.bravo.utils.BravoWebServiceConfig;
import com.sharebravo.bravo.view.adapter.AdapterRecentPost;
import com.sharebravo.bravo.view.lib.PullAndLoadListView;

public class FragmentHomeTab extends FragmentBasic {
    private PullAndLoadListView      mListviewRecentPost       = null;
    private AdapterRecentPost        mAdapterRecentPost        = null;
    private HomeActionListener       mHomeActionListener       = null;
    private ObGetAllBravoRecentPosts mObGetAllBravoRecentPosts = null;
    private OnItemClickListener      iRecentPostClickListener  = new OnItemClickListener() {

                                                                   @Override
                                                                   public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                                       AIOLog.d("position:" + position);
                                                                       mHomeActionListener.goToRecentPostDetail(mObGetAllBravoRecentPosts.data
                                                                               .get(position - 1));
                                                                   }
                                                               };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = (ViewGroup) inflater.inflate(R.layout.page_home_tab, container);

        intializeView(root);

        /* request news */
        requestNewsItemsOnBravoServer();
        return root;

    }

    private void requestNewsItemsOnBravoServer() {
        String userId = BravoUtils.getUserIdFromUserBravoInfo(getActivity());
        String accessToken = BravoUtils.getAccessTokenFromUserBravoInfo(getActivity());
        String url = BravoWebServiceConfig.URL_GET_ALL_BRAVO;
        List<NameValuePair> params = ParameterFactory.createSubParamsGetAllBravoItems(userId, accessToken);
        AsyncHttpGet getLoginRequest = new AsyncHttpGet(getActivity(), new AsyncHttpResponseProcess(getActivity()) {
            @Override
            public void processIfResponseSuccess(String response) {
                AIOLog.d("requestBravoNews:" + response);
                Gson gson = new GsonBuilder().serializeNulls().create();
                mObGetAllBravoRecentPosts = gson.fromJson(response.toString(), ObGetAllBravoRecentPosts.class);
                AIOLog.d("obGetAllBravoRecentPosts:" + mObGetAllBravoRecentPosts);
                if (mObGetAllBravoRecentPosts == null)
                    return;
                else {
                    AIOLog.d("size of recent post list: " + mObGetAllBravoRecentPosts.data.size());
                    mAdapterRecentPost.updateRecentPostList(mObGetAllBravoRecentPosts);
                    if (mListviewRecentPost.getVisibility() == View.GONE)
                        mListviewRecentPost.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void processIfResponseFail() {
                AIOLog.d("response error");
            }
        }, params, true);
        getLoginRequest.execute(url);

    }

    private void intializeView(View root) {
        mListviewRecentPost = (PullAndLoadListView) root.findViewById(R.id.listview_recent_post);
        mAdapterRecentPost = new AdapterRecentPost(getActivity(), mObGetAllBravoRecentPosts);
        mListviewRecentPost.setAdapter(mAdapterRecentPost);
        mListviewRecentPost.setOnItemClickListener(iRecentPostClickListener);
        mListviewRecentPost.setVisibility(View.GONE);
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
