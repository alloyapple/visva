package vsvteam.outsource.android.soundeffect;

import vsvteam.outsource.android.soundeffect.layout.PlaySystem;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class PlayChipMunkStyleAcitivity extends Activity implements OnClickListener {

	private Button btnPlayChipMunkStyle;
	private Button btnBack;
	private String filePath;
	private TextView txtSongName;
	private PlaySystem playSystem;
	private boolean isPlay = false;
	private boolean status;
	// private SeekBar seekBar;
	private static int SPEED_PLAY_CHIPMUNK_STYLE = 10000;
	private static int SPEED_PLAY_NORMAL_STYLE = 11025;

	// private SoundEffectSharePreference soundEffectSharePreference;

	@Override
	public void onClick(View v) {
		if (v == btnBack) {
			finish();
		} else if (v == btnPlayChipMunkStyle) {
			// play chipmunk style
			if (!isPlay) {
				btnPlayChipMunkStyle.setEnabled(false);
				isPlay = true;
				thread.start();
			} else {
				isPlay = false;
			}
		}
	}

	// start thread to play
	Thread thread = new Thread(new Runnable() {

		public void run() {
			if (isPlay) {
				if (status)
					playSystem.startPlay(SPEED_PLAY_CHIPMUNK_STYLE);
				else
					playSystem.startPlay(SPEED_PLAY_NORMAL_STYLE);
			} else
				playSystem.stopPlay();
		}
	});

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_play_chipmunk_style);

		filePath = getIntent().getExtras().getString("filePath");
		status = getIntent().getExtras().getBoolean("status");
		btnBack = (Button) findViewById(R.id.btn_back);
		btnBack.setOnClickListener(this);
		btnPlayChipMunkStyle = (Button) findViewById(R.id.btn_play_chipmunk_style);
		btnPlayChipMunkStyle.setOnClickListener(this);
		txtSongName = (TextView) findViewById(R.id.lblSongNamePlay);
		txtSongName.setText(filePath);
		txtSongName.setSelected(true);
		// init play
		if (status) {
			btnPlayChipMunkStyle.setText("Play ChipMunk Style");
		} else
			btnPlayChipMunkStyle.setText("Play Normal Style");
		playSystem = new PlaySystem(filePath);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onStop() {
		super.onStop();
		playSystem.stopPlay();
		Log.e("adsfads", "adsfkjhasd ");
	}

	protected void onPause() {
		super.onPause();
		playSystem.stopPlay();
		Log.e("opnpause", "onpause");

	}
}
