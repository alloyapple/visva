package vsvteam.outsource.leanappandroid.exportcontrol;

import java.io.IOException;

import vsvteam.outsource.leanappandroid.actionbar.AuthenticationActivity;

import com.box.androidlib.Box;
import com.box.androidlib.DAO.User;
import com.box.androidlib.ResponseListeners.GetAccountInfoListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;

public class SendBoxController {
	public static final String PREFS_FILE_NAME = "LeanApp";
	public static final String PREFS_KEY_AUTH_TOKEN = "AUTH_TOKEN";
	public static final String API_KEY = "0mjv7mlwtfkk4z2d0jfmkkhdkzdfaqj0";
	private Context mContext;

	public SendBoxController(Context pContext) {
		mContext = pContext;
	}

	public void upload(String mFilePath) {
		SharedPreferences mPreference = mContext.getSharedPreferences(
				PREFS_FILE_NAME, 0);
		String mToken = mPreference.getString(PREFS_KEY_AUTH_TOKEN, null);
		if (mToken != null) {
			Box box = Box.getInstance(API_KEY);
			box.getAccountInfo(mToken, new GetAccountInfoListener() {
				@Override
				public void onComplete(final User boxUser, final String status) {
					// see
					// http://developers.box.net/w/page/12923928/ApiFunction_get_account_info
					// for possible status codes
					if (status
							.equals(GetAccountInfoListener.STATUS_GET_ACCOUNT_INFO_OK)
							&& boxUser != null) {
						new AlertDialog.Builder(mContext)
								.setMessage(
										"You logged in as "
												+ boxUser.getEmail())
								.setNegativeButton("Other",
										new OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												// TODO Auto-generated method
												// stub
												Intent intent = new Intent(
														((Activity) mContext),
														AuthenticationActivity.class);
												((Activity) mContext)
														.startActivity(intent);
											}
										})
								.setPositiveButton("Continue",
										new OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												// TODO Auto-generated method
												// stub

											}
										}).show();
					} else {
						Intent intent = new Intent(((Activity) mContext),
								AuthenticationActivity.class);
						((Activity) mContext).startActivity(intent);
					}
				}

				@Override
				public void onIOException(IOException e) {
					// No network connection?
					e.printStackTrace();
				}
			});
		} else {
			Intent intent = new Intent(((Activity) mContext),
					AuthenticationActivity.class);
			((Activity) mContext).startActivity(intent);
		}

	}
}
