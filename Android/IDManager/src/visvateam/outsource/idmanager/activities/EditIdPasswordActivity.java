package visvateam.outsource.idmanager.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class EditIdPasswordActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_id_pass);
	}

	public static void startActivity(Activity activity) {
		Intent i = new Intent(activity, EditIdPasswordActivity.class);
		activity.startActivity(i);
	}

	public void onToGeneratorPass(View v) {
		PasswordGeneratorActivity.startActivity(this);

	}

	public void onToGeneratorID(View v) {
		PasswordGeneratorActivity.startActivity(this);
	}

	public void onReturn(View v) {
		finish();
	}

	public void onInfo(View v) {

	}

	public void onGoogleHome(View v) {

	}

	public void onLike(View v) {

	}

}
