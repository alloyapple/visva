package visvateam.outsource.idmanager.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SettingURLActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_url_setup);
	}

	public static void startActivity(Activity activity) {
		Intent i = new Intent(activity, SettingURLActivity.class);
		activity.startActivity(i);
	}

	public void onReturn(View v) {
		finish();
	}

	public void onRefresh(View v) {
	}

	public void onBroutherBack(View v) {

	}

	public void onBroutherNext(View v) {

	}

}
