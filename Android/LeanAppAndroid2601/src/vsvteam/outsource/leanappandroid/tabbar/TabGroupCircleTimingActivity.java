package vsvteam.outsource.leanappandroid.tabbar;

import vsvteam.outsource.leanappandroid.activity.circletiming.CircleTimeActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class TabGroupCircleTimingActivity extends TabGroupActivity {
	private static final int REQUEST_VIDEO_CAPTURED = 1002;
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;

	private Uri fileUri;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		push("CircleTimeActivity", new Intent(this, CircleTimeActivity.class));
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.e("adjfhaskdjfh " + requestCode, "adfjuahsdfkuh " + data.getFlags());
		if (resultCode == RESULT_OK) {
			if (requestCode == REQUEST_VIDEO_CAPTURED) {
				fileUri = data.getData();
				Toast.makeText(TabGroupCircleTimingActivity.this, fileUri.getPath(),
						Toast.LENGTH_LONG).show();
			}
		} else if (resultCode == RESULT_CANCELED) {
			fileUri = null;
			Toast.makeText(TabGroupCircleTimingActivity.this, "Cancelled!", Toast.LENGTH_LONG)
					.show();
		}
	}
}
