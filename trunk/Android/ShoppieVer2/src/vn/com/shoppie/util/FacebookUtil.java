package vn.com.shoppie.util;

import java.util.Arrays;
import java.util.List;

import vn.com.shoppie.R;
import vn.com.shoppie.database.sobject.MerchProductItem;
import vn.com.shoppie.database.sobject.MerchantStoreItem;
import vn.com.shoppie.object.FBUser;
import vn.com.shoppie.webconfig.WebServiceConfig;

import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Session;
import com.facebook.Session.NewPermissionsRequest;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class FacebookUtil {

	private static Activity mContext;
	private static FacebookUtil mInstance;

	public static FacebookUtil getInstance(Activity context) {
		if (mInstance == null) {
			mInstance = new FacebookUtil();
			mContext = context;
		}
		return mInstance;
	}

	public void publishLoginSuccessInBackground(String userName) {
		String message = mContext.getString(R.string.fb_login_sucess_content,
				userName);
		String name = mContext.getString(R.string.fb_share_name);
		// String desc = "Ứng dụng kích thích mua sắm.";
		String link = mContext.getString(R.string.fb_link);
		String pic = mContext.getString(R.string.fb_picture);
		final Bundle _postParameter = new Bundle();
		_postParameter.putString("name", name);
		_postParameter.putString("link", link);
		_postParameter.putString("picture", pic);
		// _postParameter.putString("caption", desc);
		_postParameter.putString("description", message);

		final List<String> PERMISSIONS = Arrays.asList("publish_stream");

		if (Session.getActiveSession() != null) {
			List<String> sessionPermission = Session.getActiveSession()
					.getPermissions();
			if (!sessionPermission.containsAll(PERMISSIONS)) {
				NewPermissionsRequest reauthRequest = new Session.NewPermissionsRequest(
						mContext, PERMISSIONS);

				Session.getActiveSession().requestNewPublishPermissions(
						reauthRequest);
			}
		}

		Request request = new Request(Session.getActiveSession(), "feed",
				_postParameter, HttpMethod.POST);

		RequestAsyncTask task = new RequestAsyncTask(request);
		task.execute();
	}

	public void publishCheckInSuccessInBackground(String userName) {
		String message = mContext.getString(R.string.fb_checkin_success,
				userName);
		String name = mContext.getString(R.string.fb_share_name);
		// String desc = "Ứng dụng kích thích mua sắm.";
		String link = mContext.getString(R.string.fb_link);
		String pic = mContext.getString(R.string.fb_picture);
		final Bundle _postParameter = new Bundle();
		_postParameter.putString("name", name);
		_postParameter.putString("link", link);
		_postParameter.putString("picture", pic);
		// _postParameter.putString("caption", desc);
		_postParameter.putString("description", message);

		final List<String> PERMISSIONS = Arrays.asList("publish_stream");

		if (Session.getActiveSession() != null) {
			List<String> sessionPermission = Session.getActiveSession()
					.getPermissions();
			if (!sessionPermission.containsAll(PERMISSIONS)) {
				NewPermissionsRequest reauthRequest = new Session.NewPermissionsRequest(
						mContext, PERMISSIONS);

				Session.getActiveSession().requestNewPublishPermissions(
						reauthRequest);
			}
		}

		Request request = new Request(Session.getActiveSession(), "feed",
				_postParameter, HttpMethod.POST);

		RequestAsyncTask task = new RequestAsyncTask(request);
		task.execute();
	}

	public void publishLikeProductInBackground(String userName,
			MerchProductItem merchProductItem) {
		String message = mContext.getString(R.string.fb_like_product, userName,
				merchProductItem.getProductName());
		String name = mContext.getString(R.string.fb_share_name);
		// String desc = "Ứng dụng kích thích mua sắm.";
		String link = mContext.getString(R.string.fb_link);
		String pic = mContext.getString(R.string.fb_picture);
		final Bundle _postParameter = new Bundle();
		_postParameter.putString("name", name);
		_postParameter.putString("link", link);
		_postParameter.putString("picture", pic);
		// _postParameter.putString("caption", desc);
		_postParameter.putString("description", message);

		final List<String> PERMISSIONS = Arrays.asList("publish_stream");

		if (Session.getActiveSession() != null) {
			List<String> sessionPermission = Session.getActiveSession()
					.getPermissions();
			if (!sessionPermission.containsAll(PERMISSIONS)) {
				NewPermissionsRequest reauthRequest = new Session.NewPermissionsRequest(
						mContext, PERMISSIONS);

				Session.getActiveSession().requestNewPublishPermissions(
						reauthRequest);
			}
		}
		Request request = new Request(Session.getActiveSession(), "feed",
				_postParameter, HttpMethod.POST);

		RequestAsyncTask task = new RequestAsyncTask(request);
		task.execute();
	}

	public void publishLikeBrandInBackground(String userName,
			MerchantStoreItem merchantStoreItem) {
		String message = mContext.getString(R.string.fb_like_product, userName,
				merchantStoreItem.getStoreName());
		String name = mContext.getString(R.string.fb_share_name);
		// String desc = "Ứng dụng kích thích mua sắm.";
		String link = mContext.getString(R.string.fb_link);
		String pic = mContext.getString(R.string.fb_picture);
		final Bundle _postParameter = new Bundle();
		_postParameter.putString("name", name);
		_postParameter.putString("link", link);
		_postParameter.putString("picture", pic);
		// _postParameter.putString("caption", desc);
		_postParameter.putString("description", message);

		final List<String> PERMISSIONS = Arrays.asList("publish_stream");

		if (Session.getActiveSession() != null) {
			List<String> sessionPermission = Session.getActiveSession()
					.getPermissions();
			if (!sessionPermission.containsAll(PERMISSIONS)) {
				NewPermissionsRequest reauthRequest = new Session.NewPermissionsRequest(
						mContext, PERMISSIONS);

				Session.getActiveSession().requestNewPublishPermissions(
						reauthRequest);
			}
		}

		Request request = new Request(Session.getActiveSession(), "feed",
				_postParameter, HttpMethod.POST);

		RequestAsyncTask task = new RequestAsyncTask(request);
		task.execute();
	}

	public void publishLuckyPieInBackground(String userName) {
		String message = mContext.getString(R.string.fb_lucky_pie_content,
				userName);
		String name = mContext.getString(R.string.fb_share_name);
		// String desc = "Ứng dụng kích thích mua sắm.";
		String link = mContext.getString(R.string.fb_link);
		String pic = mContext.getString(R.string.fb_picture);
		final Bundle _postParameter = new Bundle();
		_postParameter.putString("name", name);
		_postParameter.putString("link", link);
		_postParameter.putString("picture", pic);
		// _postParameter.putString("caption", desc);
		_postParameter.putString("description", message);

		final List<String> PERMISSIONS = Arrays.asList("publish_stream");

		if (Session.getActiveSession() != null) {
			List<String> sessionPermission = Session.getActiveSession()
					.getPermissions();
			if (!sessionPermission.containsAll(PERMISSIONS)) {
				NewPermissionsRequest reauthRequest = new Session.NewPermissionsRequest(
						mContext, PERMISSIONS);

				Session.getActiveSession().requestNewPublishPermissions(
						reauthRequest);
			}
		}

		Request request = new Request(Session.getActiveSession(), "feed",
				_postParameter, HttpMethod.POST);

		RequestAsyncTask task = new RequestAsyncTask(request);
		task.execute();
	}

	public void publishShareDialog(MerchProductItem item) {
		Session session = Session.getActiveSession();
		if (session != null && session.isOpened()) {
			Bundle params = new Bundle();
			params.putString("name", "Shoppie");
			params.putString("caption", "");
			params.putString(
					"description",
					mContext.getString(R.string.introduction_invitation,
							item.getProductName(), item.getShortDesc()));
			params.putString("link",
					"" + WebServiceConfig.HEAD_IMAGE + item.getThumbNail());
			params.putString("picture",
					"" + WebServiceConfig.HEAD_IMAGE + item.getThumbNail());
			// http://farm6.staticflickr.com/5480/10948560363_bf15322277_m.jpg
			WebDialog feedDialog = (new WebDialog.FeedDialogBuilder(mContext,
					Session.getActiveSession(), params)).setOnCompleteListener(
					new OnCompleteListener() {

						@Override
						public void onComplete(Bundle values,
								FacebookException error) {
							if (error == null) {
								// When the story is posted, echo the success
								// and the post Id.
								Toast.makeText(mContext, "Share successfully ",
										Toast.LENGTH_SHORT).show();
							} else if (error instanceof FacebookOperationCanceledException) {
								// User clicked the "x" button
								Toast.makeText(mContext, "Publish cancelled",
										Toast.LENGTH_SHORT).show();
							} else {
								// Generic, ex: network error
								Toast.makeText(mContext, "Error posting story",
										Toast.LENGTH_SHORT).show();
							}
						}
					}).build();
			feedDialog.show();
		}
	}

	public void publishFeedDialog(String custId, final FBUser friend) {
		Bundle params = new Bundle();
		params.putString("name", "Shoppie");
		params.putString("caption", "");
		params.putString("description",
				mContext.getString(R.string.invitation_content, custId));
		params.putString("link", "http://www.shoppie.com.vn/");
		params.putString("picture",
				"http://farm3.staticflickr.com/2827/11212635324_f135544731_o.png");
		params.putString("to", "" + friend.getUserId());
		WebDialog feedDialog = (new WebDialog.FeedDialogBuilder(mContext,
				Session.getActiveSession(), params)).setOnCompleteListener(
				new OnCompleteListener() {

					@Override
					public void onComplete(Bundle values,
							FacebookException error) {
						if (error == null) {
							// When the story is posted, echo the success
							// and the post Id.
							final String name = friend.getUserName();
							if (name != null) {
								Toast.makeText(mContext, "Invited " + name,
										Toast.LENGTH_SHORT).show();
							} else {
								// User clicked the Cancel button
								Toast.makeText(mContext, "Publish cancelled",
										Toast.LENGTH_SHORT).show();
							}
						} else if (error instanceof FacebookOperationCanceledException) {
							// User clicked the "x" button
							Toast.makeText(mContext, "Publish cancelled",
									Toast.LENGTH_SHORT).show();
						} else {
							// Generic, ex: network error
							Toast.makeText(mContext, "Error posting story",
									Toast.LENGTH_SHORT).show();
						}
					}
				}).build();
		feedDialog.show();

	}
}
