package vn.com.shoppie.network;

import org.apache.http.HttpResponse;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

import vn.com.shoppie.R;
import vn.com.shoppie.util.DialogUtility;
import vn.com.shoppie.util.VisvaDialog;
import android.app.Activity;
import android.util.Log;

/**
 * AsyncHttpResponseProcess: process server response
 * 
 * @author Visva
 */
public class AsyncHttpResponseProcess implements AsyncHttpResponseListener {
	private static final String TAG = "AsyncHttpResponseProcess";

	private Activity context;
	private VisvaDialog progressDialog;

	public AsyncHttpResponseProcess(Activity context) {
		this.context = context;
	}

	@Override
	public void before() {
		// Show waiting dialog during connection
		progressDialog = new VisvaDialog(context);
		progressDialog.show();
		progressDialog.setCancelable(false);

	}

	@Override
	public void after(int statusCode, HttpResponse response) {
		// Process server response
		if (progressDialog != null) {
			try {
				progressDialog.dismiss();
			} catch (Exception e) {
			}
			progressDialog = null;
		}

		switch (statusCode) {
		case AsyncHttpBase.NETWORK_STATUS_OFF:
			try {
				DialogUtility.alert(context,
						context.getString(R.string.network_unvailable));
			} catch (Exception e) {
				DialogUtility.alert(context.getParent(),
						context.getString(R.string.network_unvailable));
				e.printStackTrace();
			}
			break;
		case AsyncHttpBase.NETWORK_STATUS_OK:
			processHttpResponse(response);
			break;
		default:
			DialogUtility.alert(context,
					context.getString(R.string.failed_to_conect_server));
			break;
		}
	}

	/**
	 * Process HttpResponse
	 * 
	 * @param response
	 */
	public void processHttpResponse(HttpResponse response) {
		String json = "";
		try {
			// Get json response
			long current = System.currentTimeMillis();
			json = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);

			if (json == null) {
				DialogUtility.alert(context, "Can't extract server data");
				return;
			}

			// Parser information
			Log.i(TAG, "Webservice response : " + json);
			// CommonInfo commonInfo = ParserUtility.parserCommonResponse(json);
			// if (commonInfo.isSuccess()) {
			processIfResponseSuccess(json);
			// } else {
			// processIfResponseFail();
			// context.checkInvalidToken(commonInfo.getMessage());
			// }
		} catch (Exception e) {
			e.printStackTrace();
			// try {
			// DialogUtility.alert(context, "Server error");
			// } catch (Exception e1) {
			// // TODO Auto-generated catch block
			// DialogUtility.alert(context.getParent(), "Server error");
			// e1.printStackTrace();
			// }

		}
	}

	/**
	 * Interface function
	 * 
	 * @throws JSONException
	 */
	public void processIfResponseSuccess(String response) {
		Log.i(TAG, "Process if response is success ===================");
	}

	/**
	 * Interface function
	 */
	public void processIfResponseFail() {
		// SmartLog.log(TAG, "Process if response is fail ===================");
	}
}
