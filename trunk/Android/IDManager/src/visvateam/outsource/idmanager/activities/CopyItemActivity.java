package visvateam.outsource.idmanager.activities;

import visvateam.outsource.idmanager.contants.Contants;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class CopyItemActivity extends Activity {

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

	public void onReturn(View v) {
		finish();
	}
	public void onPasteBrowse(View v) {
		Intent intentBrowser = new Intent(CopyItemActivity.this, BrowserActivity.class);
		intentBrowser.putExtra(Contants.KEY_TO_BROWSER	, Contants.PASTE_TO);
		startActivity(intentBrowser);
	}
}
