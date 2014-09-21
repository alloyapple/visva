package com.sharebravo.bravo.view.fragment.home;

import java.util.List;

import org.apache.http.NameValuePair;

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
import com.sharebravo.bravo.model.SessionLogin;
import com.sharebravo.bravo.model.response.ObBravo;
import com.sharebravo.bravo.model.response.ObGetBravo;
import com.sharebravo.bravo.model.response.ObGetComments;
import com.sharebravo.bravo.model.response.ObGetSpot.Spot;
import com.sharebravo.bravo.sdk.log.AIOLog;
import com.sharebravo.bravo.sdk.util.network.AsyncHttpGet;
import com.sharebravo.bravo.sdk.util.network.AsyncHttpResponseProcess;
import com.sharebravo.bravo.sdk.util.network.ParameterFactory;
import com.sharebravo.bravo.utils.BravoConstant;
import com.sharebravo.bravo.utils.BravoSharePrefs;
import com.sharebravo.bravo.utils.BravoUtils;
import com.sharebravo.bravo.utils.BravoWebServiceConfig;
import com.sharebravo.bravo.view.adapter.AdapterSpotDetail;
import com.sharebravo.bravo.view.adapter.DetailSpotListener;
import com.sharebravo.bravo.view.fragment.FragmentBasic;
import com.sharebravo.bravo.view.lib.pullrefresh_loadmore.XListView;
import com.sharebravo.bravo.view.lib.pullrefresh_loadmore.XListView.IXListViewListener;

public class FragmentSpotDetail extends FragmentBasic implements DetailSpotListener {
    private XListView           listviewContent    = null;
    private AdapterSpotDetail   mAdapter           = null;
    private Spot                mSpot              = null;

    private Button              btnBack;
    private OnItemClickListener onItemClick        = new OnItemClickListener() {

                                                       @Override
                                                       public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                                       }
                                                   };

    private ObBravo             mBravoObj;
    private SessionLogin        mSessionLogin      = null;
    private int                 mLoginBravoViaType = BravoConstant.NO_LOGIN_SNS;

    // FragmentMapViewCover mapFragment = new FragmentMapViewCover();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = (ViewGroup) inflater.inflate(R.layout.page_recent_post_detail_tab, container);
        mHomeActionListener = (HomeActivity) getActivity();
        listviewContent = (XListView) root.findViewById(R.id.listview_recent_post_detail);
        mAdapter = new AdapterSpotDetail(getActivity());
        // mAdapter.setListener(this);
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

    public void setBravoOb(ObBravo obj) {
        this.mBravoObj = obj;
        FragmentMapViewCover.mLat = mBravoObj.Spot_Latitude;
        FragmentMapViewCover.mLong = mBravoObj.Spot_Longitude;
        mAdapter.notifyDataSetChanged();

    }

    private void onStopPullAndLoadListView() {
        listviewContent.stopRefresh();
        listviewContent.stopLoadMore();
    }


    private void requestGetBravo() {
        String userId = mSessionLogin.userID;
        String accessToken = mSessionLogin.accessToken;
        String bravoID = mBravoObj.Bravo_ID;
        String url = BravoWebServiceConfig.URL_GET_BRAVO.replace("{Bravo_ID}", bravoID);
        List<NameValuePair> params = ParameterFactory.createSubParamsGetBravo(userId, accessToken);
        AsyncHttpGet getBravoRequest = new AsyncHttpGet(getActivity(), new AsyncHttpResponseProcess(getActivity(), this) {
            @Override
            public void processIfResponseSuccess(String response) {
                // AIOLog.d("requestBravoNews:" + response);
                Gson gson = new GsonBuilder().serializeNulls().create();
                ObGetBravo obGetBravo;
                obGetBravo = gson.fromJson(response.toString(), ObGetBravo.class);
                AIOLog.d("obGetAllBravoRecentPosts:" + obGetBravo);
                if (obGetBravo == null)
                    return;
                else {
                    String Last_Pic = mBravoObj.Last_Pic;
                    mBravoObj = obGetBravo.data;
                    mBravoObj.Last_Pic = Last_Pic;
                    // mAdapter.updateMapView();
                    mAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void processIfResponseFail() {
                AIOLog.d("response error");
            }
        }, params, true);
        getBravoRequest.execute(url);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
           // requestGetBravo();
        }
    }

    @Override
    public void goToCallSpot() {
        showDialogCallSpot();
    }

    public void onCallSpot() {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mBravoObj.Spot_Phone));
        startActivity(intent);
    }

    @Override
    public void goToFragment(int fragmentID) {
        if (fragmentID == HomeActivity.FRAGMENT_MAP_VIEW_ID) {
            mHomeActionListener.goToMapView(String.valueOf(mBravoObj.Spot_Latitude), String.valueOf(mBravoObj.Spot_Longitude),
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
        content.setText("Call " + mBravoObj.Spot_Name + "?");
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

    @Override
    public void goToCoverImage() {
        mHomeActionListener.goToCoverImage(mBravoObj);
    }

    @Override
    public void goToUserDataTab(String useId) {
        mHomeActionListener.goToUserData(useId);
    }

    public Spot getSpot() {
        return mSpot;
    }

    public void setSpot(Spot mSpot) {
        this.mSpot = mSpot;
    }

}
