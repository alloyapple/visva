package com.lemon.fromangle.controls;
import java.util.List;
import org.apache.http.NameValuePair;
import android.app.Activity;
import android.provider.Settings;
import com.lemon.fromangle.config.WebServiceConfig;
import com.lemon.fromangle.network.AsyncHttpGet;
import com.lemon.fromangle.network.AsyncHttpResponseProcess;
import com.lemon.fromangle.network.ParameterFactory;

public class CheckPayment {
	private String deviceId;
	private Activity mContext;

	@SuppressWarnings("deprecation")
	public CheckPayment(Activity context) {
		mContext = context;
		deviceId = Settings.System.getString(context.getContentResolver(),
				Settings.System.ANDROID_ID);
	}

	public void checkPayment() {
		List<NameValuePair> params = ParameterFactory
				.createCheckPayment(deviceId);
		AsyncHttpGet get = new AsyncHttpGet(mContext,
				new AsyncHttpResponseProcess(mContext) {
					@Override
					public void processIfResponseSuccess(String response) {

					}

				}, params, true);
		get.execute(WebServiceConfig.URL_CHECK_PAYMENT);
	}
}
