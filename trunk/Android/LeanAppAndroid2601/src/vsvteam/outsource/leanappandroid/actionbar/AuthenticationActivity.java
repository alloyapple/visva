package vsvteam.outsource.leanappandroid.actionbar;

import vsvteam.outsource.leanappandroid.exportcontrol.SendBoxController;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.box.androidlib.activities.BoxAuthentication;

public class AuthenticationActivity extends Activity {
	private static final int AUTH_REQUEST_CODE = 100;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = new Intent(this, BoxAuthentication.class);
		intent.putExtra("API_KEY", SendBoxController.API_KEY); // API_KEY is
																// required
		startActivityForResult(intent, AUTH_REQUEST_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == AUTH_REQUEST_CODE) {
			if (resultCode == BoxAuthentication.AUTH_RESULT_SUCCESS) {
				SharedPreferences prefs = getSharedPreferences(
						SendBoxController.PREFS_FILE_NAME, 0);
				SharedPreferences.Editor editor = prefs.edit();
				editor.putString(SendBoxController.PREFS_KEY_AUTH_TOKEN,
						data.getStringExtra("AUTH_TOKEN"));
				editor.commit();
				finish();
			} else if (resultCode == BoxAuthentication.AUTH_RESULT_FAIL) {
				Toast.makeText(getApplicationContext(),
						"Unable to log into Box", Toast.LENGTH_LONG).show();
				finish();
			} else {
				finish();
			}
		}
	}
}
