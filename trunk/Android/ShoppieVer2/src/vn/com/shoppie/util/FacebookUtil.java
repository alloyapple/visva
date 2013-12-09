package vn.com.shoppie.util;

import java.util.Arrays;
import java.util.List;

import vn.com.shoppie.R;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Session;
import com.facebook.Session.NewPermissionsRequest;

import android.app.Activity;
import android.os.Bundle;

public class FacebookUtil {

	private static Activity mContext;
	private static FacebookUtil mInstance;

	public static FacebookUtil getInstance(Activity context)
    {
        if (mInstance == null){
            mInstance = new FacebookUtil();
            mContext = context;
        }
        return mInstance;
    }

	public void publishLoginSuccessInBackground(String userName) {
		String message = mContext.getString(R.string.fb_login_sucess_content,userName);
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
			NewPermissionsRequest reauthRequest = new Session.NewPermissionsRequest(
					mContext, PERMISSIONS);
			Session.getActiveSession().requestNewPublishPermissions(
					reauthRequest);
		}

		Request request = new Request(Session.getActiveSession(), "feed",
				_postParameter, HttpMethod.POST);

		RequestAsyncTask task = new RequestAsyncTask(request);
		task.execute();
	}

	public void publishCheckInSuccessInBackground(String userName) {
		String message = mContext.getString(R.string.fb_checkin_success,userName);
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
			NewPermissionsRequest reauthRequest = new Session.NewPermissionsRequest(
					mContext, PERMISSIONS);
			Session.getActiveSession().requestNewPublishPermissions(
					reauthRequest);
		}

		Request request = new Request(Session.getActiveSession(), "feed",
				_postParameter, HttpMethod.POST);

		RequestAsyncTask task = new RequestAsyncTask(request);
		task.execute();
	}
	
	public void publishLuckyPieInBackground(String userName) {
		String message = mContext.getString(R.string.fb_lucky_pie_content,userName);
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
			NewPermissionsRequest reauthRequest = new Session.NewPermissionsRequest(
					mContext, PERMISSIONS);
			Session.getActiveSession().requestNewPublishPermissions(
					reauthRequest);
		}

		Request request = new Request(Session.getActiveSession(), "feed",
				_postParameter, HttpMethod.POST);

		RequestAsyncTask task = new RequestAsyncTask(request);
		task.execute();
	}
	
	
}
