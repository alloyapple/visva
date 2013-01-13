package vsvteam.outsource.leanappandroid.actionbar;

import vsvteam.outsource.leanappandroid.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class VersionActivity extends Activity implements OnClickListener {

	private Button btnDone;

	@Override
	public void onClick(View v) {
		if (v == btnDone) {
			finish();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_version);

		btnDone = (Button) findViewById(R.id.btn_version_done);
		btnDone.setOnClickListener(this);
	}

}
