package com.sharebravo.bravo.view.fragment.home;

import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sharebravo.bravo.R;
import com.sharebravo.bravo.control.activity.HomeActivity;
import com.sharebravo.bravo.foursquare.FactoryFoursquareParams;
import com.sharebravo.bravo.foursquare.models.OFGetVenue;
import com.sharebravo.bravo.foursquare.network.FAsyncHttpGet;
import com.sharebravo.bravo.foursquare.network.FAsyncHttpResponseProcess;
import com.sharebravo.bravo.model.SessionLogin;
import com.sharebravo.bravo.model.response.ObGetSpot;
import com.sharebravo.bravo.model.response.ObGetSpotHistory;
import com.sharebravo.bravo.model.response.ObGetSpotRank;
import com.sharebravo.bravo.model.response.ObPutReport;
import com.sharebravo.bravo.model.response.Spot;
import com.sharebravo.bravo.sdk.log.AIOLog;
import com.sharebravo.bravo.sdk.util.network.AsyncHttpGet;
import com.sharebravo.bravo.sdk.util.network.AsyncHttpPut;
import com.sharebravo.bravo.sdk.util.network.AsyncHttpResponseProcess;
import com.sharebravo.bravo.sdk.util.network.ParameterFactory;
import com.sharebravo.bravo.utils.BravoConstant;
import com.sharebravo.bravo.utils.BravoSharePrefs;
import com.sharebravo.bravo.utils.BravoUtils;
import com.sharebravo.bravo.utils.BravoWebServiceConfig;
import com.sharebravo.bravo.view.adapter.AdapterSpotDetail;
import com.sharebravo.bravo.view.adapter.DetailSpotListener;
import com.sharebravo.bravo.view.fragment.FragmentBasic;
import com.sharebravo.bravo.view.fragment.bravochecking.FragmentBravoMap;
import com.sharebravo.bravo.view.lib.pullrefresh_loadmore.XListView;
import com.sharebravo.bravo.view.lib.pullrefresh_loadmore.XListView.IXListViewListener;

public class FragmentSpotDetail extends FragmentBasic implements DetailSpotListener {
    private XListView           listviewContent     = null;
    private AdapterSpotDetail   mAdapter            = null;
    private Spot                mSpot               = null;

    private Button              btnBack;
    private OnItemClickListener onItemClick         = new OnItemClickListener() {

                                                        @Override
                                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                                        }
                                                    };

    private SessionLogin        mSessionLogin       = null;
    private int                 mLoginBravoViaType  = BravoConstant.NO_LOGIN_SNS;
    private String              mFourquareDetailURL = "http:\\";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = (ViewGroup) inflater.inflate(R.layout.page_spot_detail, container);
        mHomeActionListener = (HomeActivity) getActivity();
        listviewContent = (XListView) root.findViewById(R.id.listview_spot_detail);
        mAdapter = new AdapterSpotDetail(getActivity(), this);
        mAdapter.setListener(this);
        listviewContent.setFooterDividersEnabled(false);
        listviewContent.setAdapter(mAdapter);
        listviewContent.setOnItemClickListener(onItemClick);
        listviewContent.setXListViewListener(new IXListViewListener() {

            @Override
            public void onRefresh() {
                onStopPullAndLoadListView();
            }

            @Override
            public void onLoadMore() {
                onStopPullAndLoadListView();
            }
        });
        btnBack = (Button) root.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mHomeActionListener.goToBack();
            }
        });
        mLoginBravoViaType = BravoSharePrefs.getInstance(getActivity()).getIntValue(BravoConstant.PREF_KEY_SESSION_LOGIN_BRAVO_VIA_TYPE);
        mSessionLogin = BravoUtils.getSession(getActivity(), mLoginBravoViaType);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void onStopPullAndLoadListView() {
        listviewContent.stopRefresh();
        listviewContent.stopLoadMore();
    }

    private void requestGetSpotHistory(String spotID) {
        String userId = mSessionLogin.userID;
        String accessToken = mSessionLogin.accessToken;
        HashMap<String, String> subParams = new HashMap<String, String>();
        subParams.put("Start", "0");
        JSONObject subParamsJson = new JSONObject(subParams);
        String subParamsJsonStr = subParamsJson.toString();
        String url = BravoWebServiceConfig.URL_GET_SPOT_HISTORY.replace("{Spot_ID}", spotID);
        List<NameValuePair> params = ParameterFactory.createSubParamsRequest(userId, accessToken, subParamsJsonStr);
        AsyncHttpGet getSpotHistoryRequest = new AsyncHttpGet(getActivity(), new AsyncHttpResponseProcess(getActivity(), this) {
            @Override
            public void processIfResponseSuccess(String response) {
                AIOLog.d("ObGetSpotHistory:" + response);
                Gson gson = new GsonBuilder().serializeNulls().create();
                ObGetSpotHistory mObGetSpotHistory;
                mObGetSpotHistory = gson.fromJson(response.toString(), ObGetSpotHistory.class);
                AIOLog.d("obGetAllBravoRecentPosts:" + mObGetSpotHistory);
                if (mObGetSpotHistory == null)
                    return;
                else {
                    mAdapter.updatSpotHistory(mObGetSpotHistory.data);
                }
            }

            @Override
            public void processIfResponseFail() {
                AIOLog.d("response error");
            }
        }, params, true);
        getSpotHistoryRequest.execute(url);
    }

    private void requestGetSpot(String spotID) {
        String userId = mSessionLogin.userID;
        String accessToken = mSessionLogin.accessToken;
        String url = BravoWebServiceConfig.URL_GET_SPOT.replace("{Spot_ID}", spotID);
        List<NameValuePair> params = ParameterFactory.createSubParamsRequest(userId, accessToken);
        AsyncHttpGet getBravoRequest = new AsyncHttpGet(getActivity(), new AsyncHttpResponseProcess(getActivity(), this) {
            @Override
            public void processIfResponseSuccess(String response) {
                AIOLog.d("mObGetSpot:" + response);
                Gson gson = new GsonBuilder().serializeNulls().create();
                ObGetSpot mObGetSpot;
                mObGetSpot = gson.fromJson(response.toString(), ObGetSpot.class);
                AIOLog.d("mObGetSpotd:" + mObGetSpot);
                if (mObGetSpot == null)
                    return;
                else {
                    mAdapter.updateMapView();
                    mAdapter.updatSpot(mObGetSpot.data);

                }
            }

            @Override
            public void processIfResponseFail() {
                AIOLog.d("response error");
            }
        }, params, true);
        getBravoRequest.execute(url);
    }

    private void requestGetSpotRank(String spotID) {
        String userId = mSessionLogin.userID;
        String accessToken = mSessionLogin.accessToken;
        HashMap<String, String> subParams = new HashMap<String, String>();
        subParams.put("Start", "0");
        JSONObject subParamsJson = new JSONObject(subParams);
        String subParamsJsonStr = subParamsJson.toString();
        String url = BravoWebServiceConfig.URL_GET_SPOT_RANK.replace("{Spot_ID}", spotID);
        List<NameValuePair> params = ParameterFactory.createSubParamsRequest(userId, accessToken, subParamsJsonStr);
        AsyncHttpGet getSpotRankRequest = new AsyncHttpGet(getActivity(), new AsyncHttpResponseProcess(getActivity(), this) {
            @Override
            public void processIfResponseSuccess(String response) {
                // AIOLog.d("requestBravoNews:" + response);
                Gson gson = new GsonBuilder().serializeNulls().create();
                ObGetSpotRank mObGetSpotRank;
                mObGetSpotRank = gson.fromJson(response.toString(), ObGetSpotRank.class);
                AIOLog.d("mObGetSpotRank:" + mObGetSpotRank);
                if (mObGetSpotRank == null)
                    return;
                else {
                    mAdapter.updateSpotRanks(mObGetSpotRank.data);
                }
            }

            @Override
            public void processIfResponseFail() {
                AIOLog.d("response error");
            }
        }, params, true);
        getSpotRankRequest.execute(url);
    }

    private void requestGet4squareVenue(String SpotFID) {

        String url = BravoWebServiceConfig.URL_FOURSQUARE_GET_VENUE.replace("{Spot_FID}", SpotFID);
        List<NameValuePair> params = FactoryFoursquareParams.createSubParamsRequest();
        FAsyncHttpGet getSpotRankRequest = new FAsyncHttpGet(getActivity(), new FAsyncHttpResponseProcess(getActivity()) {
            @Override
            public void processIfResponseSuccess(String response) {
                AIOLog.d("mOFGetVenue:" + response);
                Gson gson = new GsonBuilder().serializeNulls().create();
                OFGetVenue mOFGetVenue;
                mOFGetVenue = gson.fromJson(response.toString(), OFGetVenue.class);
                AIOLog.d("mOFGetVenue:" + mOFGetVenue);
                if (mOFGetVenue == null)
                    return;
                else {
                    mFourquareDetailURL = mOFGetVenue.response.venue.canonicalUrl;

                    // mAdapter.updateSpotRanks(mOFGetVenue.response.venue);
                }
            }

            @Override
            public void processIfResponseFail() {
                AIOLog.d("response error");
            }
        }, params, true);
        getSpotRankRequest.execute(url);
    }

    private void requestToPutReport(Spot mSpot) {
        String userId = mSessionLogin.userID;
        String accessToken = mSessionLogin.accessToken;
        HashMap<String, String> subParams = new HashMap<String, String>();
        subParams.put("Foreign_ID", mSpot.Spot_ID);
        subParams.put("Report_Type", "spot");
        subParams.put("User_ID", userId);
        subParams.put("Detail", "");
        JSONObject jsonObject = new JSONObject(subParams);
        List<NameValuePair> params = ParameterFactory.createSubParamsPutFollow(jsonObject.toString());
        String url = BravoWebServiceConfig.URL_PUT_REPORT.replace("{User_ID}", userId).replace("{Access_Token}", accessToken);
        AsyncHttpPut putReport = new AsyncHttpPut(getActivity(), new AsyncHttpResponseProcess(getActivity(), this) {
            @Override
            public void processIfResponseSuccess(String response) {
                AIOLog.d("response putReport :===>" + response);
                JSONObject jsonObject = null;

                try {
                    jsonObject = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (jsonObject == null)
                    return;

                String status = null;
                try {
                    status = jsonObject.getString("status");
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                Gson gson = new GsonBuilder().serializeNulls().create();
                ObPutReport obPutReport;
                if (status == String.valueOf(BravoWebServiceConfig.STATUS_RESPONSE_DATA_SUCCESS)) {
                    showDialogReportOk();
                } else {
                    obPutReport = gson.fromJson(response.toString(), ObPutReport.class);
                    showToast(obPutReport.error);
                }
            }

            @Override
            public void processIfResponseFail() {
                AIOLog.d("response error");
            }
        }, params, true);
        AIOLog.d(url);
        putReport.execute(url);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (mSpot.Spot_ID != null) {
                if (!isBackStatus()) {
                    requestGetSpot(mSpot.Spot_ID);
                    requestGetSpotHistory(mSpot.Spot_ID);
                    requestGetSpotRank(mSpot.Spot_ID);
                    requestGet4squareVenue(mSpot.Spot_FID);
                }
            }
        }
    }

    @Override
    public void goToCallSpot() {
        showDialogCallSpot();
    }

    public void onCallSpot() {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mSpot.Spot_Phone));
        startActivity(intent);
    }

    @Override
    public void goToFragment(int fragmentID) {
        if (fragmentID == HomeActivity.FRAGMENT_MAP_VIEW_ID) {
            mHomeActionListener.goToMapView(String.valueOf(mSpot.Spot_Latitude), String.valueOf(mSpot.Spot_Longitude),
                    FragmentMapView.MAKER_BY_LOCATION_SPOT);
            return;
        }
        mHomeActionListener.goToFragment(fragmentID);
    }

    public void showDialogCallSpot() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) getActivity().getLayoutInflater();
        View dialog_view = inflater.inflate(R.layout.dialog_call_spot, null);
        TextView content = (TextView) dialog_view.findViewById(R.id.call_spot_dialog_content);
        content.setText("Call " + mSpot.Spot_Name + "?");
        Button btnCancel = (Button) dialog_view.findViewById(R.id.btn_call_spot_no);
        btnCancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        Button btnOK = (Button) dialog_view.findViewById(R.id.btn_call_spot_yes);
        btnOK.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                onCallSpot();
            }
        });
        dialog.setContentView(dialog_view);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        // This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        dialog.show();
    }

    public void showDialogReportOk() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) getActivity().getLayoutInflater();
        View dialog_view = inflater.inflate(R.layout.dialog_report_ok, null);
        Button btnReportClose = (Button) dialog_view.findViewById(R.id.btn_report_close);
        btnReportClose.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        dialog.setContentView(dialog_view);

        dialog.show();
    }

    public void showDialogReport() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) getActivity().getLayoutInflater();
        View dialog_view = inflater.inflate(R.layout.dialog_report, null);
        Button btnOk = (Button) dialog_view.findViewById(R.id.btn_report_yes);
        btnOk.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                requestToPutReport(mSpot);
            }
        });
        Button btnCancel = (Button) dialog_view.findViewById(R.id.btn_report_no);
        btnCancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setContentView(dialog_view);
        dialog.show();
    }

    public Spot getSpot() {
        return mSpot;
    }

    public void setSpot(Spot mSpot) {
        this.mSpot = mSpot;
        FragmentMapCover.mLat = mSpot.Spot_Latitude;
        FragmentMapCover.mLong = mSpot.Spot_Longitude;
        // mAdapter.updateMapView();
        mAdapter.updatSpot(mSpot);
    }

    @Override
    public void tapToBravo() {
        // TODO Auto-generated method stub
        // mHomeActionListener.goToBravoSpot(mSpot.Spot_Latitude, mSpot.Spot_Longitude);
        mHomeActionListener.goToMapView(mSpot, FragmentBravoMap.MAKER_BY_LOCATION_SPOT);
    }

    @Override
    public void goToUserDataTab(String useId) {
        mHomeActionListener.goToUserData(useId);
    }

    @Override
    public void goToMoreDetailOn4square() {
        // TODO Auto-generated method stub
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mFourquareDetailURL));
        startActivity(browserIntent);
    }

    @Override
    public void goToReport() {
        // TODO Auto-generated method stub
        showDialogReport();
    }

}
