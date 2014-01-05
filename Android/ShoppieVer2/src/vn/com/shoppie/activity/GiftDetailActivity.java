package vn.com.shoppie.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import vn.com.shoppie.R;
import vn.com.shoppie.constant.GlobalValue;
import vn.com.shoppie.constant.ShoppieSharePref;
import vn.com.shoppie.database.sobject.GiftItem;
import vn.com.shoppie.database.sobject.GiftRedeemItem;
import vn.com.shoppie.database.sobject.GiftRedeemList;
import vn.com.shoppie.database.sobject.MerchantStoreItem;
import vn.com.shoppie.database.sobject.MerchantStoreList;
import vn.com.shoppie.network.AsyncHttpPost;
import vn.com.shoppie.network.AsyncHttpResponseProcess;
import vn.com.shoppie.network.ParameterFactory;
import vn.com.shoppie.util.DialogUtility;
import vn.com.shoppie.util.ImageLoader;
import vn.com.shoppie.util.log;
import vn.com.shoppie.webconfig.WebServiceConfig;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.facebook.Session;
import com.facebook.widget.GiftTransactionButton;
import com.google.gson.Gson;

public class GiftDetailActivity extends Activity {
	private GiftItem item;
	private ShoppieSharePref mSharePref;
	private int currId;
	private int currStoreId;
	String price[];
	int pie[];
	private GiftTransactionButton mBtnGiftTransaction;
	private Vector<MerchantStoreItem> listStore = new Vector<MerchantStoreItem>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gift_detail_activity);

		init();
	}

	private void init() {
		mSharePref = new ShoppieSharePref(this);
		item = ActivityGiftTransaction.currItem;

		((TextView) findViewById(R.id.title)).setText(item.getGiftName());
		((TextView) findViewById(R.id.desc)).setText(item.getDescription());
		ImageLoader.getInstance(this)
				.DisplayImage(
						WebServiceConfig.HEAD_IMAGE + item.getGiftImage(),
						findViewById(R.id.image), true, true, false, false,
						true, false);
		mBtnGiftTransaction = (GiftTransactionButton) findViewById(R.id.btn_gift_transaction);
		mBtnGiftTransaction.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Spinner spinnerStore = (Spinner) findViewById(R.id.spinner_store);
				SpinnerAdapter adapter = spinnerStore.getAdapter();

				int storeId = 0;
				if (adapter.getCount() > 0) {
					storeId = listStore.get(currStoreId).getStoreId();
				}
				
//				log.d("Id", "MerchId" + ActivityGiftTransaction.currItem.getMerchId() + " storeId " + String.valueOf(storeId)
//						+ " CustId " + mSharePref.getCustId() + " GiftId " + ActivityGiftTransaction.currItem.getGiftId()
//						+ " RedeemQty " + ActivityGiftTransaction.currItem.getRedeemQty() + " pie " + pie[currId]
//								+ " price " + price[currId]);
				
				if (mSharePref.getCurrentBal() >= item.getPiesNotArr()[currId]) {
					updateGiftListAvailable(
							ActivityGiftTransaction.currItem.getMerchId(),
							String.valueOf(storeId), mSharePref.getCustId()
									+ "",
							ActivityGiftTransaction.currItem.getGiftId(),
							ActivityGiftTransaction.currItem.getRedeemQty(),
							pie[currId] + "", price[currId]);
				} else
					showReject();

			}
		});
		price = item.getPricesNotArr();
		pie = item.getPiesNotArr();
		String text[] = new String[price.length];

		for (int i = 0; i < pie.length; i++) {
			text[i] = item.getGiftName() + " " + price[i] + "VNĐ = " + pie[i]
					+ " Pie";
		}

		// ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
		// android.R.layout.simple_spinner_item, text);
		((Spinner) findViewById(R.id.spinner)).setAdapter(new MySpinnerAdapter(
				text));
		((Spinner) findViewById(R.id.spinner))
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int position, long arg3) {
						currId = position;
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {

					}
				});

		requestGetMerchantStores(String.valueOf(mSharePref.getCustId()));
	}

	public void onClickBack(View v) {
		finish();
	}

	private void updateGiftListAvailable(String merchId, String storeId,
			String custId, String giftId, String redeemQty, String pieQty,
			String giftPrice) {
		List<NameValuePair> nameValuePairs = ParameterFactory
				.getGiftListAvailable(merchId, storeId, custId, giftId,
						redeemQty, pieQty, giftPrice);
		AsyncHttpPost postGetGiftList = new AsyncHttpPost(
				GiftDetailActivity.this, new AsyncHttpResponseProcess(
						GiftDetailActivity.this) {
					@Override
					public void processIfResponseSuccess(String response) {
						try {
							log.m(response);
							JSONObject jsonObject = new JSONObject(response);
							Gson gson = new Gson();
							GiftRedeemList redeem = gson.fromJson(
									jsonObject.toString(), GiftRedeemList.class);
							showHelp(item.getGiftCatId() , String.valueOf(redeem.getResult().getTxnId()));
						} catch (JSONException e) {
							DialogUtility.alert(GiftDetailActivity.this, "Không thành công!");
							e.printStackTrace();
						}
					}

					@Override
					public void processIfResponseFail() {
						log.e("failed ", "failed");
						finish();
					}
				}, nameValuePairs, true);
		postGetGiftList
				.execute(WebServiceConfig.URL_GET_GIFT_TRANSACTION_AVAILABLE);
	}

	public String getStringResource(int id) {
		return getResources().getString(id);
	}

	private void showReject() {
		DialogUtility.showYesNoDialog(this, R.string.gift_reject,
				R.string.btn_ok, R.string.btn_cancel, new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						Session session = Session.getActiveSession();
						if (session != null && session.isOpened()) {
							Intent intent = new Intent(GiftDetailActivity.this,
									PersonalInfoActivity.class);
							intent.putExtra(GlobalValue.IS_SHOW_FAVOURITE, 2);
							startActivity(intent);
						} else
							mBtnGiftTransaction.onClickLoginFb();
					}
				});
	}

	private void showHelp(int type , String txn) {
		String header = "";
		String body = "";
		String body1 = "";
		String footer = "";
		if (type == 1)
			footer = getStringResource(R.string.gift_type_1_footer);
		else if (type == 2)
			footer = getStringResource(R.string.gift_type_2_footer);
		else if (type == 3)
			footer = getStringResource(R.string.gift_type_3_footer);
		body = getStringResource(R.string.gift_type_1_body);
		body1 = getStringResource(R.string.gift_type_1_body1);
		header = getStringResource(R.string.gift_type_1_header);

		String time = convertTimeMail(Calendar.getInstance().getTimeInMillis());
		String message = header + item.getGiftName() + body + time + body1 + txn + footer;

		DialogUtility.alert(this, message);
	}

	public static String convertTimeMail(long timeStamp) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(timeStamp);
		if (cal.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR)
				&& cal.get(Calendar.MONTH) == Calendar.getInstance().get(
						Calendar.MONTH)
				&& cal.get(Calendar.DAY_OF_MONTH) == Calendar.getInstance()
						.get(Calendar.DAY_OF_MONTH)) {
			return String.format("%02d", cal.get(Calendar.HOUR_OF_DAY)) + ":"
					+ String.format("%02d", cal.get(Calendar.MINUTE));
		} else {
			return String.format("%02d", cal.get(Calendar.DAY_OF_MONTH)) + "/"
					+ String.format("%02d", cal.get(Calendar.MONTH) + 1);
		}
	}

	class MySpinnerAdapter extends BaseAdapter {

		String data[];

		public MySpinnerAdapter(String data[]) {
			this.data = data;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return data.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return data[position];
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.spinner_item, null);
			((TextView) convertView.findViewById(R.id.text))
					.setText(data[position]);
			return convertView;
		}
	}

	private void setStoreAdapter(ArrayList<MerchantStoreItem> items) {
		for (int i = 0; i < items.size(); i++) {
			if (String.valueOf(items.get(i).getMerchId()).equals(
					item.getMerchId())) {
				listStore.add(items.get(i));
			}
		}
		String text[] = new String[listStore.size()];
		for (int i = 0; i < text.length; i++) {
			text[i] = listStore.get(i).getStoreName();
		}

		Spinner spinnerStore = (Spinner) findViewById(R.id.spinner_store);
		spinnerStore.setAdapter(new MySpinnerAdapter(text));
		spinnerStore.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				currStoreId = position;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	private void requestGetMerchantStores(final String custId) {
		List<NameValuePair> nameValuePairs = ParameterFactory
				.getMerchantStores(custId);
		AsyncHttpPost postGetMerchantProducts = new AsyncHttpPost(
				GiftDetailActivity.this, new AsyncHttpResponseProcess(
						GiftDetailActivity.this) {
					@Override
					public void processIfResponseSuccess(String response) {
						try {
							JSONObject jsonObject = new JSONObject(response);
							Gson gson = new Gson();
							MerchantStoreList merchantStoreList = gson
									.fromJson(jsonObject.toString(),
											MerchantStoreList.class);
							setStoreAdapter(merchantStoreList.getResult());

							/** update to database */

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					@Override
					public void processIfResponseFail() {
						log.e("failed ", "failed");
						finish();
					}
				}, nameValuePairs, true);
		postGetMerchantProducts.execute(WebServiceConfig.URL_MERCHANT_STORES);
	}
}
