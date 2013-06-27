package com.lemon.fromangle.controls;

import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.lemon.fromangle.R;
import com.lemon.fromangle.config.GlobalValue;
import com.lemon.fromangle.config.WebServiceConfig;
import com.lemon.fromangle.network.AsyncHttpPost;
import com.lemon.fromangle.network.AsyncHttpResponseProcess;
import com.lemon.fromangle.network.ParameterFactory;
import com.lemon.fromangle.network.ParserUtility;
import com.lemon.fromangle.utility.StringUtility;
import com.lemon.fromangle.utility.TimeUtility;
import com.payment.BillingHelper;

public class PaymentService {
	// private String deviceId;
	private PaymentActivity mContext;
	private static final String ID_SERVICE_MONTHLY_PAYMENT = "0000000000000002";

	public PaymentService(PaymentActivity context) {
		mContext = context;
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

	public void checkPaymentTopScreeen(String userId) {
		List<NameValuePair> params = ParameterFactory
				.createCheckPayment(userId);
		AsyncHttpPost postCheckPayMent = new AsyncHttpPost(mContext,
				new AsyncHttpResponseProcess(mContext) {
					@Override
					public void processIfResponseSuccess(String response) {
						/* check info response from server */
						checkInfoReponseFromServerTopScreen(response);
					}

					@Override
					public void processIfResponseFail() {
						// TODO Auto-generated method stub
						Log.e("failed ", "failed");
					}
				}, params, true);
		postCheckPayMent.execute(WebServiceConfig.URL_CHECK_PAYMENT);
	}

	public void updatePayment(String userId, String paymentDay, String expiry) {
		List<NameValuePair> params = ParameterFactory.createUpdatePayment(
				userId, paymentDay, expiry);
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

	public void updatePayment(String userId) {
		List<NameValuePair> params = ParameterFactory
				.createUpdatePayment(userId);
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
			postUpdatePayment.execute(WebServiceConfig.URL_UPDATE_PAYMENT);
	}

	public void updatePaymentTopScreen(String userId) {
		List<NameValuePair> params = ParameterFactory
				.createUpdatePayment(userId);
		AsyncHttpPost postUpdatePayment = new AsyncHttpPost(mContext,
				new AsyncHttpResponseProcess(mContext) {
					@Override
					public void processIfResponseSuccess(String response) {
						/* check info response from server */
						checkInfoReponseAfterUpdateTopScreen(response);
					}

					@Override
					public void processIfResponseFail() {
						// TODO Auto-generated method stub
						Log.e("failed ", "failed");
					}
				}, params, true);
		if (userId != null && !StringUtility.isEmpty(userId))
			postUpdatePayment.execute(WebServiceConfig.URL_UPDATE_PAYMENT);
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

				} else if (error == GlobalValue.MSG_REPONSE_PAID_SUCCESS) {
					mContext.onStartSuccess();
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void checkInfoReponseAfterUpdateTopScreen(String response) {
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

				} else if (error == GlobalValue.MSG_REPONSE_PAID_SUCCESS) {
					// mContext.onStartSuccess();
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void checkInfoReponseFromServerTopScreen(String response) {
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
					checkPaymentPaidExpired();
				} else if (error == GlobalValue.MSG_REPONSE_NOT_PAID) {
					/* not paid */
//					checkPaymentNotPaid();
					mContext.onTrialCase();
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
					mContext.onTrialCase();
					// checkPaymentNotPaid();
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

		AlertDialog dialog = creatDialog(
				mContext.getResources().getString(
						R.string.message_paid_not_paid), null,
				R.layout.dialog_not_paid);
		dialog.show();
		dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
	}

	/**
	 * check payment user paid expired
	 */
	private void checkPaymentPaidExpired() {
		// TODO Auto-generated method stub
//		creatDialog(
//				null,
//				mContext.getResources()
//						.getString(R.string.message_finish_trial),
//				new DialogInterface.OnClickListener() {
//
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						// TODO Auto-generated method stub
//						mContext.onPaymentSuccess();
//					}
//				}).show();
		AlertDialog dialog = creatDialog(
				mContext.getResources().getString(
						R.string.message_finish_trial), null,
				R.layout.dialog_not_paid);
		dialog.show();
		dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
	}

	/**
	 * check payment user paid not expired
	 */
	private void checkPaymentPaidNotExpired() {
		// TODO Auto-generated method stub
		mContext.onStartSuccess();
	}

	public void showToast(String message) {
		Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();

	}

	private AlertDialog creatDialog(String message, String title,
			DialogInterface.OnClickListener listener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		if (title != null)
			builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton(
				mContext.getResources().getString(R.string.btn_ok), listener);
		builder.setNegativeButton(
				mContext.getResources().getString(R.string.btn_cancel),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

					}
				});
		return builder.create();
	}

	private AlertDialog creatDialog(String message, String title, int layout) {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		if (title != null)
			builder.setTitle(title);
		LayoutInflater inflater2 = (LayoutInflater) mContext
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		View layoutAnyNumber = inflater2.inflate(layout,
				(ViewGroup) mContext.findViewById(R.id.id_layout_not_paid));
		TextView textView = (TextView) layoutAnyNumber
				.findViewById(R.id.link_web);
		final CheckBox checkbox = (CheckBox) layoutAnyNumber
				.findViewById(R.id.id_checkbox_agreed);

		textView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse("http://fromangel.net/main/?page_id=254"));
				mContext.startActivity(intent);
			}
		});
		builder.setPositiveButton(
				mContext.getResources().getString(R.string.btn_agreed),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Log.i("payment request", "<-------true-------->");
						if (checkbox.isChecked()) {
							// if (BillingHelper.isBillingSupported()) {
							// BillingHelper.requestPurchase(mContext,
							// ID_SERVICE_MONTHLY_PAYMENT);
							// } else {
							// Log.i("Billing",
							// "Can't purchase on this device");
							//
							// }
							mContext.onPaymentSuccess();
						}

					}
				});
		builder.setNegativeButton(
				mContext.getResources().getString(R.string.btn_cancel),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						mContext.onDeniedPayment();
					}
				});
		builder.setView(layoutAnyNumber);
		final AlertDialog dialog = builder.create();
		checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(
							true);
				} else {
					dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(
							false);
				}
			}
		});
		return dialog;
	}
}
