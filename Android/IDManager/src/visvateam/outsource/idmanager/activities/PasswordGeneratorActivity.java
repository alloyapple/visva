package visvateam.outsource.idmanager.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class PasswordGeneratorActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.password_generator);
	}

	public void onReturn(View v) {

	}

	public void onGenerate(View v) {
	}
	public static void startActivity(Activity activity) {
		Intent i = new Intent(activity, PasswordGeneratorActivity.class);
		activity.startActivity(i);
	}
}