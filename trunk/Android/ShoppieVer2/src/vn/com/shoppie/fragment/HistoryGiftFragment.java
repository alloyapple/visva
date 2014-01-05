package vn.com.shoppie.fragment;

import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import vn.com.shoppie.R;
import vn.com.shoppie.constant.ShoppieSharePref;
import vn.com.shoppie.database.sobject.GiftHistoryItem;
import vn.com.shoppie.database.sobject.GiftHistoryList;
import vn.com.shoppie.network.AsyncHttpPost;
import vn.com.shoppie.network.AsyncHttpResponseProcess;
import vn.com.shoppie.network.ParameterFactory;
import vn.com.shoppie.util.DialogUtility;
import vn.com.shoppie.util.log;
import vn.com.shoppie.webconfig.WebServiceConfig;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

public class HistoryGiftFragment extends FragmentBasic {
	ShoppieSharePref mSharePref;
	List<GiftHistoryItem> data;
	LinearLayout containerView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mSharePref = new ShoppieSharePref(getActivity());
		View root = (ViewGroup) inflater.inflate(R.layout.gift_history,
				null);
		containerView = (LinearLayout) root.findViewById(R.id.container);
		return root;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onResume() {
		super.onResume();
	}
	
	public void requestCancelGift(final GiftHistoryItem item) {
		List<NameValuePair> nameValuePairs = ParameterFactory.requestCancelGift(item.getTxnId());
		AsyncHttpPost postGetGiftList = new AsyncHttpPost(
				getActivity(), new AsyncHttpResponseProcess(
						getActivity()) {
					@Override
					public void processIfResponseSuccess(String response) {
						try {
							JSONObject jsonObject = new JSONObject(response);
//							log.m(">>>>>>>>>>>>> " + response);
							DialogUtility.alert(getActivity(), "Hủy quà thành công");
							data.remove(item);
							setData(data);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							DialogUtility.alert(getActivity(), "Hủy quà thất bại");
						}
					}

					@Override
					public void processIfResponseFail() {
						log.e("failed ", "failed");
					}
				}, nameValuePairs, true);
		postGetGiftList.execute(WebServiceConfig.URL_REQUEST_CANCEL_GIFT);
	}

	
	public void updateListHistoryGift(String custId) {
		// TODO Auto-generated method stub
		List<NameValuePair> nameValuePairs = ParameterFactory.updateHistoryList(custId);
		AsyncHttpPost postGetGiftList = new AsyncHttpPost(
				getActivity(), new AsyncHttpResponseProcess(
						getActivity()) {
					@Override
					public void processIfResponseSuccess(String response) {
						try {
							JSONObject jsonObject = new JSONObject(response);
							Gson gson = new Gson();
							GiftHistoryList giftList = gson.fromJson(
									jsonObject.toString(), GiftHistoryList.class);
							setData(giftList.getGifts());
							log.m(">>>>>>>>>>>>> " + giftList.getGifts().size());
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					@Override
					public void processIfResponseFail() {
						log.e("failed ", "failed");
					}
				}, nameValuePairs, true);
		postGetGiftList.execute(WebServiceConfig.URL_GET_GIFT_HISTORY_LIST);
	}

	private void setData(List<GiftHistoryItem> data) {
		this.data = data;
		containerView.removeAllViews();
		LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService
			      (Context.LAYOUT_INFLATER_SERVICE);
		for (GiftHistoryItem giftHistoryItem : data) {
			View v = inflater.inflate(R.layout.gift_history_item, null);
			((TextView) v.findViewById(R.id.name)).setText(giftHistoryItem.getGiftName());
			((TextView) v.findViewById(R.id.pie)).setText(giftHistoryItem.getPieQty() + " pie");
			((TextView) v.findViewById(R.id.time)).setText(giftHistoryItem.getTxnDate());
			((TextView) v.findViewById(R.id.status)).setText(giftHistoryItem.getStatus().equals("N") ? 
					"Đang chờ" : "Thành công");
			containerView.addView(v);
			v.setTag(giftHistoryItem);
			
			v.findViewById(R.id.btn_cancel).setTag(giftHistoryItem);
			v.findViewById(R.id.btn_cancel).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(final View v) {
					DialogUtility.showYesNoDialog(getActivity(), R.string.gift_cancel_message, R.string.btn_ok, R.string.btn_cancel, 
							new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									requestCancelGift((GiftHistoryItem) v.getTag());
								}
							});
				}
			});
			
			v.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(v.findViewById(R.id.btn_cancel).getVisibility() == View.GONE) {
						v.findViewById(R.id.btn_cancel).setVisibility(View.VISIBLE);
					}
					else {
						v.findViewById(R.id.btn_cancel).setVisibility(View.GONE);
					}
				}
			});
		}
	}
}
