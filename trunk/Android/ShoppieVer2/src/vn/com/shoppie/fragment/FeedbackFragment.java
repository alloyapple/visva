package vn.com.shoppie.fragment;

import java.util.List;

import org.apache.http.NameValuePair;

import vn.com.shoppie.R;
import vn.com.shoppie.activity.PersonalInfoActivity;
import vn.com.shoppie.network.AsyncHttpPost;
import vn.com.shoppie.network.AsyncHttpResponseProcess;
import vn.com.shoppie.network.ParameterFactory;
import vn.com.shoppie.webconfig.WebServiceConfig;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FeedbackFragment extends FragmentBasic {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = (ViewGroup) inflater.inflate(R.layout.feedback_fragment,
				null);
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
	
	private void uploadFeedback(String custId, String message) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		List<NameValuePair> nameValuePairs = ParameterFactory.sendFeedback(
				custId, message);
		AsyncHttpPost postFeedback = new AsyncHttpPost(
				getActivity(), new AsyncHttpResponseProcess(
						getActivity()) {
					@Override
					public void processIfResponseSuccess(String response) {
						Log.e("post success ", "post success");
					}

					@Override
					public void processIfResponseFail() {
						// finish();
					}
				}, nameValuePairs, true);
		postFeedback.execute(WebServiceConfig.URL_FEEDBACK);

	}
}
