package vsvteam.outsource.leanappandroid.utilities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import vsvteam.outsource.leanappandroid.R;

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
		alertDialog.setPositiveButton(R.string.button_ok,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
					}
				});
		alertDialog.show();
		// }
	}

	public static void alert(Context context, String message,
			DialogInterface.OnClickListener onOkClick) {
		if (context != null) {
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
			alertDialog.setTitle(R.string.app_name);
			alertDialog.setMessage(message);
			alertDialog.setPositiveButton(R.string.button_ok, onOkClick);
			alertDialog.show();
		}
	}

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
		alertDialog.setPositiveButton(R.string.button_ok,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
					}
				});
		alertDialog.show();
		// }
	}

	public static void showYesNoDialog(Context context, int messageId,
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

	public static void showSimpleOptionDialog(Context mContext, int titleId,
			String[] items, int positiveLabelId,
			DialogInterface.OnClickListener itemOnClick,
			DialogInterface.OnClickListener positiveOnClick) {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle(mContext.getString(titleId));
		builder.setItems(items, itemOnClick);
		builder.setPositiveButton(mContext.getString(positiveLabelId),
				positiveOnClick);
		AlertDialog alert = builder.create();
		alert.show();
	}

}
