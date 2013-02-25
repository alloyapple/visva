package visvateam.outsource.idmanager.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class EditIdPasswordActivity extends Activity{

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

}
