package com.lemon.fromangle.controls;

import java.util.Date;
import java.util.List;
import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.lemon.fromangle.config.FromAngleSharedPref;
import com.lemon.fromangle.config.GlobalValue;
import com.lemon.fromangle.config.WebServiceConfig;
import com.lemon.fromangle.network.AsyncHttpPost;
import com.lemon.fromangle.network.AsyncHttpResponseProcess;
import com.lemon.fromangle.network.ParameterFactory;
import com.lemon.fromangle.network.ParserUtility;
import com.lemon.fromangle.payment.BillingHelper;
import com.lemon.fromangle.utility.StringUtility;
import com.lemon.fromangle.utility.TimeUtility;

public class PaymentService {
	// private String deviceId;
	private Activity mContext;
	private static final String TAG = "BillingService";
	private static final String ID_SERVICE_MONTHLY_PAYMENT = "0000000000000001";
	public static final int STATUS_NOT_PAID=2;
	public static final int STATUS_EXPIRED=1;
	public static final int STATUS_NOT_EXPIRED=0;
	private FromAngleSharedPref mFromAngleSharedPref;
	public Handler mTransactionHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Log.i(TAG, "Transaction complete");
			Log.i(TAG, "Transaction status: "
					+ BillingHelper.latestPurchase.purchaseState);
			Log.i(TAG, "Item purchased is: "
					+ BillingHelper.latestPurchase.productId);

			if (BillingHelper.latestPurchase.isPurchased()) {
				showToast("Payment success");
				updatePayment(mFromAngleSharedPref.getUserId(), TimeUtility.getCurentDate(),STATUS_NOT_EXPIRED);
			}
		};

	};

	private DialogInterface.OnClickListener listenerOkPayment = new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			BillingHelper.requestPurchase(mContext, ID_SERVICE_MONTHLY_PAYMENT);
		}
	};

	public PaymentService(Activity context) {
		mContext = context;	
		mFromAngleSharedPref = new FromAngleSharedPref(mContext);
	}

	public void checkPayment(String userId) {
		List<NameValuePair> params = ParameterFactory
				.createCheckPayment(userId);
		AsyncHttpPost postCheckPayMent = new AsyncHttpPost(mContext,
				new AsyncHttpResponseProcess(mContext) {
					@Override
					public void processIfResponseSuccess(String response) {
						/* check info response from server */
						checkInfoReponseFromServer(response);
					}

					@Override
					public void processIfResponseFail() {
						// TODO Auto-generated method stub
						Log.e("failed ", "failed");
					}
				}, params, true);
		postCheckPayMent.execute(WebServiceConfig.URL_CHECK_PAYMENT);
	}

	public void updatePayment(String userId, Date date, int statusPayment) {
		List<NameValuePair> params = ParameterFactory.createUpdatePayment(
				userId, date, statusPayment);
		AsyncHttpPost postUpdatePayment = new AsyncHttpPost(mContext,
				new AsyncHttpResponseProcess(mContext) {
					@Override
					public void processIfResponseSuccess(String response) {
						/* check info response from server */
						checkInfoReponseAfterUpdate(response);
					}

					@Override
					public void processIfResponseFail() {
						// TODO Auto-generated method stub
						Log.e("failed ", "failed");
					}
				}, params, true);
		if (userId != null && !StringUtility.isEmpty(userId))
			postUpdatePayment.execute(WebServiceConfig.URL_CHECK_PAYMENT);
	}

	private void checkInfoReponseAfterUpdate(String response) {
		// TODO Auto-generated method stub
		Log.e("reponse", "reponse " + response);
		JSONObject jsonObject = null;
		String errorMsg = null;
		try {
			jsonObject = new JSONObject(response);
			if (jsonObject != null && jsonObject.length() > 0) {
				errorMsg = ParserUtility.getStringValue(jsonObject,
						GlobalValue.PARAM_ERROR);
				int error = Integer.parseInt(errorMsg);
				if (error == GlobalValue.MSG_REPONSE_PAID_NOT_EXPIRED) {
					/* paid not expired */

				} else if (error == GlobalValue.MSG_REPONSE_PAID_EXPIRED) {
					/* paid expired */

				} else if (error == GlobalValue.MSG_REPONSE_NOT_PAID) {
					/* not paid */

				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void checkInfoReponseFromServer(String response) {
		// TODO Auto-generated method stub
		Log.e("reponse", "reponse " + response);
		JSONObject jsonObject = null;
		String errorMsg = null;
		try {
			jsonObject = new JSONObject(response);
			if (jsonObject != null && jsonObject.length() > 0) {
				errorMsg = ParserUtility.getStringValue(jsonObject,
						GlobalValue.PARAM_ERROR);
				int error = Integer.parseInt(errorMsg);
				if (error == GlobalValue.MSG_REPONSE_PAID_NOT_EXPIRED) {
					/* paid not expired */
					checkPaymentPaidNotExpired();
				} else if (error == GlobalValue.MSG_REPONSE_PAID_EXPIRED) {
					/* paid expired */
					checkPaymentPaidExpired();
				} else if (error == GlobalValue.MSG_REPONSE_NOT_PAID) {
					/* not paid */
					checkPaymentNotPaid();
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * check payment user not paid
	 */
	private void checkPaymentNotPaid() {
		// TODO Auto-generated method stub
		showToast("user not paid");
		creatDialog("User not paid. Please payment to use app.", "Payment",
				listenerOkPayment).show();
	}

	/**
	 * check payment user paid expired
	 */
	private void checkPaymentPaidExpired() {
		// TODO Auto-generated method stub
		showToast("paid expired");
		creatDialog("User not paid. Please payment to use app.", "Payment",
				listenerOkPayment).show();
	}

	/**
	 * check payment user paid not expired
	 */
	private void checkPaymentPaidNotExpired() {
		// TODO Auto-generated method stub
		showToast("paid not expired");
		creatDialog("User not paid. Please payment to use app.", "Payment",
				listenerOkPayment).show();
	}

	public void showToast(String message) {
		Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();

	}

	private AlertDialog creatDialog(String message, String title,
			DialogInterface.OnClickListener listener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton("OK", listener);
		builder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

					}
				});
		return builder.create();
	}
}
