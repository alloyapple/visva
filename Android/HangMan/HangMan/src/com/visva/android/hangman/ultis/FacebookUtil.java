package com.visva.android.hangman.ultis;

import android.app.Activity;

import com.facebook.widget.WebDialog;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.entities.Feed;
import com.sromku.simple.fb.listeners.OnPublishListener;
import com.visva.android.hangman.R;

public class FacebookUtil {

	private static Activity mContext;
	private static FacebookUtil mInstance;
	private static VisvaDialog mProgressDialog;

	public static FacebookUtil getInstance(Activity context) {
		if (mInstance == null) {
			mInstance = new FacebookUtil();
			mContext = context;
			mProgressDialog = new VisvaDialog(mContext, R.style.ProgressHUD);
		}
		return mInstance;
	}

	public void publishShareInBackground(final IRequestListener callback) {
		String message = mContext.getString(R.string.share_sns_introduce_project);
		String pic = "https://c1.staticflickr.com/9/8678/15971615079_f49232103f.jpg";
		String name = mContext.getString(R.string.app_name);
		String bravoUrl = "https://play.google.com/store/apps/details?id=" + mContext.getPackageName();
		final Feed feed = new Feed.Builder().setMessage(message).setName(name).setDescription(message).setPicture(pic).setLink(bravoUrl).build();
		SimpleFacebook.getInstance().publish(feed,true, new OnPublishListener() {

			@Override
			public void onException(Throwable throwable) {
				if (mProgressDialog != null) {
					try {
						mProgressDialog.dismiss();
						mProgressDialog = null;
					} catch (Exception e) {

					}
				}
				callback.onErrorResponse("");
			}

			@Override
			public void onFail(String reason) {
				if (mProgressDialog != null) {
					try {
						mProgressDialog.dismiss();
						mProgressDialog = null;
					} catch (Exception e) {

					}
				}
				callback.onErrorResponse("");
			}

			@Override
			public void onThinking() {
				// Show waiting dialog during connection
				try {
					mProgressDialog.setCancelable(false);
					mProgressDialog.show();
				} catch (Exception e) {
					mProgressDialog = null;
				}
			}

			@Override
			public void onComplete(String response) {
				if (mProgressDialog != null) {
					try {
						mProgressDialog.dismiss();
						mProgressDialog = null;
					} catch (Exception e) {

					}
				}
				callback.onResponse(response);
			}
		});
		
	}
	
//	private void publishFeedDialog() {
//		String message = mContext.getString(R.string.share_sns_introduce_project);
//		String pic = "https://c1.staticflickr.com/9/8678/15971615079_f49232103f.jpg";
//		String name = mContext.getString(R.string.app_name);
//		String bravoUrl = "https://play.google.com/store/apps/details?id=" + mContext.getPackageName();
//		final Feed feed = new Feed.Builder().setMessage(message).setName(name).setCaption(message).setDescription(message).setPicture(pic).setLink(bravoUrl).build();
//
//		SimpleFacebook.getInstance().publish(feed, new OnPublishListener() {
//
//			@Override
//			public void onException(Throwable throwable) {
//				if (mProgressDialog != null) {
//					try {
//						mProgressDialog.dismiss();
//						mProgressDialog = null;
//					} catch (Exception e) {
//
//					}
//				}
//				callback.onErrorResponse("");
//			}
//
//			@Override
//			public void onFail(String reason) {
//				if (mProgressDialog != null) {
//					try {
//						mProgressDialog.dismiss();
//						mProgressDialog = null;
//					} catch (Exception e) {
//
//					}
//				}
//				callback.onErrorResponse("");
//			}
//
//			@Override
//			public void onThinking() {
//				// Show waiting dialog during connection
//				try {
//					mProgressDialog.setCancelable(false);
//					mProgressDialog.show();
//				} catch (Exception e) {
//					mProgressDialog = null;
//				}
//			}
//
//			@Override
//			public void onComplete(String response) {
//				if (mProgressDialog != null) {
//					try {
//						mProgressDialog.dismiss();
//						mProgressDialog = null;
//					} catch (Exception e) {
//
//					}
//				}
//				callback.onResponse(response);
//			}
//		});
//	}
}
