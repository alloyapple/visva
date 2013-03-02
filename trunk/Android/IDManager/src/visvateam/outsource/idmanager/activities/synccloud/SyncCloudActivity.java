package visvateam.outsource.idmanager.activities.synccloud;

import visvateam.outsource.idmanager.activities.GGDriveSyncActivity;
import visvateam.outsource.idmanager.activities.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class SyncCloudActivity extends Activity {
	private TextView mTextViewCloudType;
	private TextView mTextViewLastTimeSync;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_sync);

		/* init control */
		mTextViewCloudType = (TextView) findViewById(R.id.cloud_type);
		mTextViewLastTimeSync = (TextView) findViewById(R.id.last_time_sync);
	}

	public void onSyncToCloud(View v) {
		Intent intent = new Intent(SyncCloudActivity.this, GGDriveSyncActivity.class);
		startActivity(intent);
	}

	public void OnSyncToDevice(View v) {
		Intent intent = new Intent(SyncCloudActivity.this, DropBoxSyncActivity.class);
		startActivity(intent);
	}

	public void onReturn(View v){
		finish();
	}
}
