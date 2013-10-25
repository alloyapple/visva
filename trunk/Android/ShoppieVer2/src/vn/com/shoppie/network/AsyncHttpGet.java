package vn.com.shoppie.network;

import java.net.URLEncoder;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import vn.com.shoppie.webconfig.WebServiceConfig;
import android.content.Context;
import android.util.Log;


/**
 * AsyncHttpGet makes http get request based on AsyncTask
 * 
 * @author Visva
 */
public class AsyncHttpGet extends AsyncHttpBase {
	private static final String TAG = "AsyncHttpGet";

	/**
	 * Constructor
	 * 
	 * @param context
	 * @param listener
	 * @param parameters
	 */
	public AsyncHttpGet(Context context, AsyncHttpResponseListener listener,
			List<NameValuePair> parameters, boolean isShowWaitingDialog) {
		super(context, listener, parameters, isShowWaitingDialog);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.fgsecure.meyclub.app.network.AsyncHttpBase#request(java.lang.String)
	 */
	@Override
	protected String request(String url) {
		try {
			HttpParams params = new BasicHttpParams();
			// Lemon commented 19/04/2012
			// if (parameters != null) {
			// Iterator<NameValuePair> it = parameters.iterator();
			// while (it.hasNext()) {
			// NameValuePair nv = it.next();
			// params.setParameter(nv.getName(), nv.getValue());
			// }
			// }

			// Bind param direct to URL

			String combinedParams = "";
			if (!parameters.isEmpty()) {
				combinedParams += "?";
				for (NameValuePair p : parameters) {
					String paramString = p.getName() + "="
							+ URLEncoder.encode(p.getValue(), "UTF-8");
					if (combinedParams.length() > 1) {
						combinedParams += "&" + paramString;
					} else {
						combinedParams += paramString;
					}
				}
				Log.i(TAG, "CombineParams : " + combinedParams);
			}
			HttpConnectionParams.setConnectionTimeout(params, WebServiceConfig.NETWORK_TIME_OUT);
			HttpConnectionParams.setSoTimeout(params, WebServiceConfig.NETWORK_TIME_OUT);
			// Lemon commented 19/04/2012
			 HttpClient httpclient = createHttpClient(url, params);
//			DefaultHttpClient httpclient = new DefaultHttpClient();
			HttpGet httpget = new HttpGet(url + combinedParams);
			HttpGet httpget1 = new HttpGet(url + combinedParams);
			response = httpclient.execute(httpget1);
			// Lemon added
			// httpclient.getConnectionManager().shutdown();
			statusCode = NETWORK_STATUS_OK;
		} catch (Exception e) {
			statusCode = NETWORK_STATUS_ERROR;
			e.printStackTrace();
		}
		return null;
	}
}
