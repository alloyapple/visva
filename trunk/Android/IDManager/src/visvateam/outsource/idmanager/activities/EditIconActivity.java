package visvateam.outsource.idmanager.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class EditIconActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_edit_icon);
	}

	public static void startActivity(Activity activity) {
		Intent i = new Intent(activity, EditIconActivity.class);
		activity.startActivity(i);
	}

	public void onReturn(View v) {
		finish();
	}

	public void onLibrary(View v) {
	}

	public void onInternet(View v) {
		GetInternetImageActivity.startActivity(this);
	}

}
