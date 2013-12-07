package vn.com.shoppie.fragment;

import java.util.List;

import org.apache.http.NameValuePair;

import vn.com.shoppie.R;
import vn.com.shoppie.activity.PersonalInfoActivity;
import vn.com.shoppie.constant.ShoppieSharePref;
import vn.com.shoppie.network.AsyncHttpPost;
import vn.com.shoppie.network.AsyncHttpResponseProcess;
import vn.com.shoppie.network.ParameterFactory;
import vn.com.shoppie.webconfig.WebServiceConfig;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class FeedbackFragment extends FragmentBasic {
	private ShoppieSharePref mSharePref;
	private View root;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mSharePref = new ShoppieSharePref(getActivity());
		View root = (ViewGroup) inflater.inflate(R.layout.feedback_act,
				null);

		RelativeLayout containerLayout = (RelativeLayout) root.findViewById(R.id.container);
		View content = (View) root.findViewById(R.id.content);

		View cover = new View(getActivity());
		cover.setBackgroundResource(R.drawable.bg_center);
		LayoutParams params = new LayoutParams((int)(getDimention(R.dimen.collectiondetail_item_width) * 1.06f), 
				(int)((getDimention(R.dimen.collectiondetail_item_height) * 1.05f)));
		params.addRule(RelativeLayout.CENTER_IN_PARENT);
		containerLayout.addView(cover, 0 , params);

		root.findViewById(R.id.btn_accept).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				EditText edt = (EditText) FeedbackFragment.this.root.findViewById(R.id.feed_content);
				String content = edt.getText().toString().trim();
				if(content.equals(""))
					return;
				uploadFeedback("" + mSharePref.getCustId(), content);
			}
		});

		root.findViewById(R.id.btn_cancel).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				((PersonalInfoActivity)getActivity()).onClickBackPersonal(null);
			}
		});
		this.root = root;
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
		List<NameValuePair> nameValuePairs = ParameterFactory.sendFeedback(
				custId, message);
		AsyncHttpPost postFeedback = new AsyncHttpPost(
				getActivity(), new AsyncHttpResponseProcess(
						getActivity()) {
					@Override
					public void processIfResponseSuccess(String response) {
						Log.e("post success ", "post success");
						//((PersonalInfoActivity)getActivity()).onClickBackPersonal(null);
					}

					@Override
					public void processIfResponseFail() {
						// finish();
					}
				}, nameValuePairs, true);
		postFeedback.execute(WebServiceConfig.URL_FEEDBACK);

	}

	private int getDimention(int id) {
		return (int) getResources().getDimension(id);
	}
}
