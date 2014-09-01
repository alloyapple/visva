package com.sharebravo.bravo.view.fragment;

import java.util.List;

import org.apache.http.NameValuePair;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sharebravo.bravo.R;
import com.sharebravo.bravo.control.activity.HomeActionListener;
import com.sharebravo.bravo.control.activity.HomeActivity;
import com.sharebravo.bravo.model.response.ObGetBravo;
import com.sharebravo.bravo.model.response.ObGetComments;
import com.sharebravo.bravo.sdk.log.AIOLog;
import com.sharebravo.bravo.sdk.util.network.AsyncHttpGet;
import com.sharebravo.bravo.sdk.util.network.AsyncHttpResponseProcess;
import com.sharebravo.bravo.sdk.util.network.ParameterFactory;
import com.sharebravo.bravo.utils.BravoWebServiceConfig;
import com.sharebravo.bravo.view.adapter.AdapterRecentPostDetail;
import com.sharebravo.bravo.view.adapter.DetailPostListener;
import com.sharebravo.bravo.view.lib.imageheader.PullAndLoadListView;

public class FragmentRecentPostDetail extends FragmentBasic implements DetailPostListener {
    private PullAndLoadListView     listviewRecentPostDetail = null;
    private AdapterRecentPostDetail adapterRecentPostDetail  = null;
    private HomeActionListener      mHomeActionListener      = null;
    // private SupportMapFragment mFragementImageMap = null;
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
    ObGetComments                   mObGetComments;

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
        // mFragementImageMap = (SupportMapFragment) getFragmentManager().findFragmentById(R.id.img_map);
        // if (mFragementImageMap == null) {
        // FragmentManager fragmentManager = getFragmentManager();
        // FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        // mFragementImageMap = SupportMapFragment.newInstance();
        // fragmentTransaction.replace(R.id.img_map, mFragementImageMap).commit();
        // }
        // mFragementImageMap =(FragmentMapView) findFragmentById(R.id.img_map);

        return root;
    }

    public void setBravoOb(ObGetBravo obj) {
        this.bravoObj = obj;
        adapterRecentPostDetail.setBravoOb(bravoObj);
        adapterRecentPostDetail.updateCommentList();
    }

    private void requestGetComnents() {
        String userId = "";
        String accessToken = "";
        String bravoID = bravoObj.Bravo_ID;
        String url = BravoWebServiceConfig.URL_GET_COMMENTS.replace("{Bravo_ID}", bravoID);
        List<NameValuePair> params = ParameterFactory.createSubParamsGetComments(userId, accessToken);
        AsyncHttpGet getCommentsRequest = new AsyncHttpGet(getActivity(), new AsyncHttpResponseProcess(getActivity()) {
            @Override
            public void processIfResponseSuccess(String response) {
                AIOLog.d("requestBravoNews:" + response);
                Gson gson = new GsonBuilder().serializeNulls().create();
                mObGetComments = gson.fromJson(response.toString(), ObGetComments.class);
                AIOLog.d("obGetAllBravoRecentPosts:" + mObGetComments);
                if (mObGetComments == null)
                    return;
                else {
                    AIOLog.d("size of recent post list: " + mObGetComments.data.size());
                    adapterRecentPostDetail.updateAllCommentList(mObGetComments);
                    if (listviewRecentPostDetail.getVisibility() == View.GONE)
                        listviewRecentPostDetail.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void processIfResponseFail() {
                AIOLog.d("response error");
            }
        }, params, true);
        getCommentsRequest.execute(url);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        // TODO Auto-generated method stub
        super.onHiddenChanged(hidden);
        if (!hidden) {
            requestGetComnents();
        }
    }

    @Override
    public void goToCallSpot() {
        // TODO Auto-generated method stub
        // Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + bravoObj.Spot_Phone));
        // startActivity(intent);
        showDialogCallSpot();
    }

    @Override
    public void goToShare() {
        // TODO Auto-generated method stub
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "Bravo Share";
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "subject");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    @Override
    public void goToSave() {
        // TODO Auto-generated method stub

    }

    @Override
    public void goToSubmitComment() {
        // TODO Auto-generated method stub

    }

    @Override
    public void goToFragment(int fragmentID) {
        // TODO Auto-generated method stub
        mHomeActionListener.goToFragment(fragmentID);
    }

    public void showDialogCallSpot() {
        Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) getActivity().getLayoutInflater();
        View dialog_view = inflater.inflate(R.layout.dialog_call_spot, null);
        dialog.setContentView(dialog_view);
        dialog.show();
    }

    public void showDialogReport() {
        Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) getActivity().getLayoutInflater();
        View dialog_view = inflater.inflate(R.layout.dialog_report, null);
        dialog.setContentView(dialog_view);
        dialog.show();
    }

    @Override
    public void goToReport() {
        // TODO Auto-generated method stub
        showDialogReport();
    }

}
