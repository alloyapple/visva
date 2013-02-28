package visvateam.outsource.idmanager.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ListIconActivity extends Activity {
	public static final int DIALOG_DELETE = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_list_icon);
	}

	public static void startActivity(Activity activity) {
		Intent i = new Intent(activity, ListIconActivity.class);
		activity.startActivity(i);
	}

	public void onReturn(View v) {

	}

	public void onEditImage(View v) {
		EditIconActivity.startActivity(this);
	}

	public void onDeleteImage(View v) {
		showDialog(DIALOG_DELETE);
	}

	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		switch (id) {
		case DIALOG_DELETE:
			builder.setTitle("Delete").setMessage("Are you sure?")
					.setPositiveButton("Agree", new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub

						}
					}).setNegativeButton("Cancel", new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub

						}
					});
			return builder.create();
		default:
			break;
		}
		return null;
	}

}
