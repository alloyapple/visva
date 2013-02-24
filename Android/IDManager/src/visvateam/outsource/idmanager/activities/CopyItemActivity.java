package visvateam.outsource.idmanager.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class CopyItemActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_choice_id_pass_item);
	}
	public static void startActivity(Activity activity) {
		Intent i = new Intent(activity, CopyItemActivity.class);
		activity.startActivity(i);
	}
}
