package com.sharebravo.bravo.view.fragment.home;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sharebravo.bravo.R;
import com.sharebravo.bravo.control.activity.HomeActivity;
import com.sharebravo.bravo.model.SessionLogin;
import com.sharebravo.bravo.model.response.ObBravo;
import com.sharebravo.bravo.model.response.ObGetAllBravoRecentPosts;
import com.sharebravo.bravo.sdk.log.AIOLog;
import com.sharebravo.bravo.sdk.util.network.AsyncHttpGet;
import com.sharebravo.bravo.sdk.util.network.AsyncHttpResponseProcess;
import com.sharebravo.bravo.sdk.util.network.ParameterFactory;
import com.sharebravo.bravo.utils.BravoConstant;
import com.sharebravo.bravo.utils.BravoSharePrefs;
import com.sharebravo.bravo.utils.BravoUtils;
import com.sharebravo.bravo.utils.BravoWebServiceConfig;
import com.sharebravo.bravo.utils.StringUtility;
import com.sharebravo.bravo.view.adapter.AdapterPostList;
import com.sharebravo.bravo.view.adapter.AdapterPostList.IClickUserAvatar;
import com.sharebravo.bravo.view.fragment.FragmentBasic;
import com.sharebravo.bravo.view.lib.pullrefresh_loadmore.XListView;
import com.sharebravo.bravo.view.lib.pullrefresh_loadmore.XListView.IXListViewListener;

public class FragmentHomeTab extends FragmentBasic implements IClickUserAvatar {
    private XListView       mListviewRecentPost       = null;
    private AdapterPostList         mAdapterRecentPost        = null;
    private ObGetAllBravoRecentPosts  mObGetAllBravoRecentPosts = null;

    private Button                    mBtnHomeNotification      = null;
    private IShowPageHomeNotification mListener                 = null;
    private SessionLogin              mSessionLogin             = null;
    private int                       mLoginBravoViaType        = BravoConstant.NO_LOGIN_SNS;
    private boolean                   isNoFirstTime             = false;
    private OnItemClickListener       iRecentPostClickListener  = new OnItemClickListener() {

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
        mHomeActionListener = (HomeActivity) getActivity();
        mLoginBravoViaType = BravoSharePrefs.getInstance(getActivity()).getIntValue(BravoConstant.PREF_KEY_SESSION_LOGIN_BRAVO_VIA_TYPE);
        mSessionLogin = BravoUtils.getSession(getActivity(), mLoginBravoViaType);
        requestNewsItemsOnBravoServer(mSessionLogin);
        isNoFirstTime = BravoSharePrefs.getInstance(getActivity()).getBooleanValue(BravoConstant.PREF_KEY_BRAVO_FISRT_TIME);
        if (!isNoFirstTime) {
            showDialogWelcome();
            BravoSharePrefs.getInstance(getActivity()).putBooleanValue(BravoConstant.PREF_KEY_BRAVO_FISRT_TIME, true);
            
            BravoSharePrefs.getInstance(getActivity()).putBooleanValue(BravoConstant.PREF_KEY_BRAVO_NOTIFICATIONS, true);
            BravoSharePrefs.getInstance(getActivity()).putBooleanValue(BravoConstant.PREF_KEY_COMMENT_NOTIFICATIONS, true);
            BravoSharePrefs.getInstance(getActivity()).putBooleanValue(BravoConstant.PREF_KEY_FOLLOW_NOTIFICATIONS, true);
            BravoSharePrefs.getInstance(getActivity()).putBooleanValue(BravoConstant.PREF_KEY_FAVOURITE_NOTIFICATIONS, true);
            BravoSharePrefs.getInstance(getActivity()).putBooleanValue(BravoConstant.PREF_KEY_TOTAL_BRAVO_NOTIFICATIONS, true);
        }
        return root;

    }

    private void requestNewsItemsOnBravoServer(SessionLogin sessionLogin) {
        String userId = sessionLogin.userID;
        String accessToken = sessionLogin.accessToken;
        AIOLog.d("mUserId:" + sessionLogin.userID + ", mAccessToken:" + sessionLogin.accessToken);
        if (StringUtility.isEmpty(sessionLogin.userID) || StringUtility.isEmpty(sessionLogin.accessToken)) {
            userId = "";
            accessToken = "";
        }
        String url = BravoWebServiceConfig.URL_GET_ALL_BRAVO;
        List<NameValuePair> params = ParameterFactory.createSubParamsGetAllBravoItems(userId, accessToken);
        AsyncHttpGet getLoginRequest = new AsyncHttpGet(getActivity(), new AsyncHttpResponseProcess(getActivity(), this) {
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
                    ArrayList<ObBravo> obBravos = removeIncorrectBravoItems(mObGetAllBravoRecentPosts.data);
                    mObGetAllBravoRecentPosts.data = obBravos;
                    mAdapterRecentPost.updateRecentPostList(obBravos);
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
        mBtnHomeNotification = (Button) root.findViewById(R.id.btn_home_notification);
        mBtnHomeNotification.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // show home notification tab
                mListener.showPageHomeNotification();
            }
        });
        mListviewRecentPost = (XListView) root.findViewById(R.id.listview_recent_post);
        mAdapterRecentPost = new AdapterPostList(getActivity(), mObGetAllBravoRecentPosts);
        mAdapterRecentPost.setListener(this);
        mListviewRecentPost.setAdapter(mAdapterRecentPost);
        mListviewRecentPost.setOnItemClickListener(iRecentPostClickListener);
        mListviewRecentPost.setVisibility(View.GONE);
//        /* load more old items */
//        mListviewRecentPost.setOnLoadMoreListener(new IOnLoadMoreListener() {
//
//            @Override
//            public void onLoadMore() {
//                int size = mObGetAllBravoRecentPosts.data.size();
//                if (size > 0)
//                    onPullDownToRefreshBravoItems(mObGetAllBravoRecentPosts.data.get(size - 1), false);
//                else
//                    mListviewRecentPost.onLoadMoreComplete();
//                AIOLog.d("IOnLoadMoreListener");
//            }
//        });
//
//        /* on refresh new items */
//        /* load more old items */
//        mListviewRecentPost.setOnRefreshListener(new IOnRefreshListener() {
//
//            @Override
//            public void onRefresh() {
//                AIOLog.d("IOnRefreshListener");
//                int size = mObGetAllBravoRecentPosts.data.size();
//                if (size > 0)
//                    onPullDownToRefreshBravoItems(mObGetAllBravoRecentPosts.data.get(0), true);
//                else
//                    mListviewRecentPost.onRefreshComplete();
//            }
//        });
//        
        mListviewRecentPost.setXListViewListener(new IXListViewListener() {
            
            @Override
            public void onRefresh() {
                AIOLog.d("IOnRefreshListener");
                int size = mObGetAllBravoRecentPosts.data.size();
                if (size > 0)
                    onPullDownToRefreshBravoItems(mObGetAllBravoRecentPosts.data.get(0), true);
                else
                    onStopPullAndLoadListView();
            }
            
            @Override
            public void onLoadMore() {
                int size = mObGetAllBravoRecentPosts.data.size();
                if (size > 0)
                    onPullDownToRefreshBravoItems(mObGetAllBravoRecentPosts.data.get(size - 1), false);
                else
                    onStopPullAndLoadListView();
                AIOLog.d("IOnLoadMoreListener");
                
            }
        });
    }

    public void showDialogWelcome() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) getActivity().getLayoutInflater();
        View dialog_view = inflater.inflate(R.layout.dialog_welcome, null);
        Button btnStart = (Button) dialog_view.findViewById(R.id.btn_start);
        btnStart.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();

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

    private void onPullDownToRefreshBravoItems(ObBravo obBravo, final boolean isPulDownToRefresh) {
        AIOLog.d("obBravo.bravoId:" + obBravo.Bravo_ID);
        HashMap<String, String> subParams = new HashMap<String, String>();
        if (isPulDownToRefresh)
            subParams.put("Min_Bravo_ID", obBravo.Bravo_ID);
        else
            subParams.put("Max_Bravo_ID", obBravo.Bravo_ID);
        subParams.put("View_Deleted_Users", "0");
        subParams.put("Global", "TRUE");
        JSONObject subParamsJson = new JSONObject(subParams);
        String subParamsJsonStr = subParamsJson.toString();
        String userId = mSessionLogin.userID;
        String accessToken = mSessionLogin.accessToken;
        AIOLog.d("mUserId:" + mSessionLogin.userID + ", mAccessToken:" + mSessionLogin.accessToken);
        if (StringUtility.isEmpty(mSessionLogin.userID) || StringUtility.isEmpty(mSessionLogin.accessToken)) {
            userId = "";
            accessToken = "";
        }
        String url = BravoWebServiceConfig.URL_GET_BRAVO_SEARCH;
        List<NameValuePair> params = ParameterFactory.createSubParamsGetNewsBravoItems(userId, accessToken, subParamsJsonStr);
        AsyncHttpGet getPullDown_LoadMoreRequest = new AsyncHttpGet(getActivity(), new AsyncHttpResponseProcess(getActivity(), this) {
            @Override
            public void processIfResponseSuccess(String response) {
                AIOLog.d("onLoadMoreBravoItems:" + response);
                onStopPullAndLoadListView();

                Gson gson = new GsonBuilder().serializeNulls().create();
                ObGetAllBravoRecentPosts obGetAllBravoRecentPosts = gson.fromJson(response.toString(), ObGetAllBravoRecentPosts.class);
                AIOLog.d("obGetAllBravoRecentPosts:" + obGetAllBravoRecentPosts);
                if (obGetAllBravoRecentPosts == null)
                    return;
                else {
                    AIOLog.d("size of recent post list: " + obGetAllBravoRecentPosts.data.size());
                    int reponseSize = obGetAllBravoRecentPosts.data.size();
                    if (reponseSize <= 0)
                        return;
                    mAdapterRecentPost.updatePullDownLoadMorePostList(obGetAllBravoRecentPosts.data, isPulDownToRefresh);
                    if (mListviewRecentPost.getVisibility() == View.GONE)
                        mListviewRecentPost.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void processIfResponseFail() {
                AIOLog.e("response error");
               onStopPullAndLoadListView();
            }
        }, params, true);
        AIOLog.e("url: " + url);
        getPullDown_LoadMoreRequest.execute(url);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public interface IShowPageHomeNotification {
        public void showPageHomeNotification();
    }

    public void setListener(IShowPageHomeNotification iShowPageHomeNotification) {
        this.mListener = iShowPageHomeNotification;
    }

    private void updatePullDownLoadMorePostList(ObGetAllBravoRecentPosts obGetAllBravoRecentPosts, boolean isPulDownToRefresh) {
        ArrayList<ObBravo> newObBravos = removeIncorrectBravoItems(obGetAllBravoRecentPosts.data);
        if (mObGetAllBravoRecentPosts == null) {
            mObGetAllBravoRecentPosts = new ObGetAllBravoRecentPosts();
            mObGetAllBravoRecentPosts.data = new ArrayList<ObBravo>();
        }
        if (isPulDownToRefresh)
            mObGetAllBravoRecentPosts.data.addAll(0, newObBravos);
        else
            mObGetAllBravoRecentPosts.data.addAll(newObBravos);
    }

    private ArrayList<ObBravo> removeIncorrectBravoItems(ArrayList<ObBravo> bravoItems) {
        ArrayList<ObBravo> obBravos = new ArrayList<ObBravo>();
        for (ObBravo obBravo : bravoItems) {
            if (StringUtility.isEmpty(obBravo.User_ID) || (StringUtility.isEmpty(obBravo.Full_Name) || "0".equals(obBravo.User_ID))) {
                AIOLog.e("The incorrect bravo items:" + obBravo.User_ID + ", obBravo.Full_Name:" + obBravo.Full_Name);
            } else
                obBravos.add(obBravo);
        }
        return obBravos;
    }

    @Override
    public void onClickUserAvatar(String userId) {
        mHomeActionListener.goToUserData(userId);
    }
    
    private void onStopPullAndLoadListView() {
        mListviewRecentPost.stopRefresh();
        mListviewRecentPost.stopLoadMore();
    }
}
