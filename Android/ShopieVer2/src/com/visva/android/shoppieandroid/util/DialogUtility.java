package com.visva.android.shoppieandroid.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

import com.visva.android.shoppieandroid.R;


/**
 * DialogUtility
 * 
 * @author Lemon
 */
public final class DialogUtility {
	/**
	 * Show an alert dialog box
	 * 
	 * @param context
	 * @param message
	 */

	public static void alert(Context context, String message) {
		// if (context != null) {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
		alertDialog.setTitle(context.getString(R.string.app_name));
		alertDialog.setMessage(message);
		alertDialog.setPositiveButton(R.string.btn_close,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
					}
				});
		alertDialog.show();
	}

	public static AlertDialog creatDialog(Context mContext, String message,
			String title) {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		if (title != null)
			builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton(
				mContext.getResources().getString(R.string.btn_close),
				new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

					}
				});

		return builder.create();
	}

	// }

	/**
	 * Show an alert dialog box
	 * 
	 * @param context
	 * @param messageId
	 */
	public static void alert(Context context, int messageId) {
		// if (context != null) {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
		alertDialog.setTitle(context.getString(R.string.app_name));
		alertDialog.setMessage(context.getString(messageId));
		alertDialog.setPositiveButton(R.string.btn_close,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
					}
				});
		alertDialog.show();
		// }
	}

	public static void showOkDialog(Context context, int messageId,
			int OkTextId, final DialogInterface.OnClickListener onOKClick) {
		if (context != null) {
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
			alertDialog.setTitle(context.getString(R.string.app_name));
			alertDialog.setMessage(context.getString(messageId));

			alertDialog.setPositiveButton(OkTextId, onOKClick);
			alertDialog.show();
		}
	}

	public static void showYesNoDialog(final Context context, int messageId,
			int OkTextId, int cancelTextId,
			final DialogInterface.OnClickListener onOKClick) {
		if (context != null) {
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
			alertDialog.setTitle(context.getString(R.string.app_name));
			alertDialog.setMessage(context.getString(messageId));
			alertDialog.setNegativeButton(cancelTextId,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();

						}
					});
			alertDialog.setPositiveButton(OkTextId, onOKClick);
			alertDialog.show();
		}
	}

	public static void showYesNoDialog(final Context context, int messageId,
			int OkTextId, int cancelTextId,
			final DialogInterface.OnClickListener onOKClick,
			final DialogInterface.OnClickListener onCancelClick) {
		if (context != null) {
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
			alertDialog.setTitle(context.getString(R.string.app_name));
			alertDialog.setMessage(context.getString(messageId));
			alertDialog.setNegativeButton(cancelTextId, onCancelClick);
			alertDialog.setPositiveButton(OkTextId, onOKClick);
			alertDialog.show();
		}
	}

	public static void showSimpleOptionDialog(Context mContext, int titleId,
			String[] items, String positiverButton, int selectIndex,
			DialogInterface.OnClickListener positiveOnClick) {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle(mContext.getString(titleId));
		builder.setSingleChoiceItems(items, selectIndex, null);
		builder.setPositiveButton(positiverButton, positiveOnClick);
		builder.setNegativeButton(mContext.getText(R.string.btn_cancel),
				new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();

					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}

	public static void alert(Context context, String message,
			DialogInterface.OnClickListener onOkClick) {
		if (context != null) {
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
			alertDialog.setTitle(R.string.app_name);
			alertDialog.setMessage(message);
			alertDialog.setPositiveButton(R.string.btn_close, onOkClick);
			alertDialog.show();
		}
	}

}
