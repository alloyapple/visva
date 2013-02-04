package vsvteam.outsource.leanappandroid.activity.circletiming;

import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import vsvteam.outsource.leanappandroid.R;
import vsvteam.outsource.leanappandroid.activity.home.VSVTeamBaseActivity;
import vsvteam.outsource.leanappandroid.adapter.ListStepRecordingAdapter;
import vsvteam.outsource.leanappandroid.database.LeanAppAndroidSharePreference;
import vsvteam.outsource.leanappandroid.database.TCycleTimeDataBaseHandler;
import vsvteam.outsource.leanappandroid.database.TStepsDataBaseHandler;

public class CircleTimeDetailActivity extends VSVTeamBaseActivity implements OnClickListener,
		OnItemClickListener, OnPreparedListener {
	// =============================Control Define ==========================
	private Button btnCompare;
	private Button btnPlay;
	private WheelView wheelSpotter1;
	private WheelView wheelSpotter2;
	private WheelView wheelSpotter3;
	private ListView listViewRecording;
//	private VideoView videoView;
	// =============================Class Define =============================
	private LeanAppAndroidSharePreference leanAppAndroidSharePreference;
	private TCycleTimeDataBaseHandler tCycleTimeDataBaseHandler;
	private TStepsDataBaseHandler tStepsDataBaseHandler;
	// =============================Variable Define ==========================
	private String[] record_detail = { "Cycle 1 Open Box", "Cycle 1 Extract Goods",
			"Cycle 1 Parts On Table" };
	private String[] record_detail2 = { "Cycle 1 Operator Frank", "Cycle 2 Operator John",
			"Cycle 3 Operator Kane" };
	private int _currentProcessIdActive;
	private String _currentProcessNameActive;
	private int _currentProjectIdActive;
	private String _currentProjectNameActive;
	// video info
	private long timeOfVideo;
	private String fileName;

	@Override
	public void onClick(View v) {
		if (v == btnCompare) {
			Log.e("orientation", "orientaion " + _getScreenOrientation());
			if (_getScreenOrientation() == Configuration.ORIENTATION_LANDSCAPE) {
				Intent intentSpotter = new Intent(CircleTimeDetailActivity.this,
						VariationSpotterActivity.class);
				startActivity(intentSpotter);
			}
		} else if (v == btnPlay) {
			// gotoActivityInGroup(CircleTimeDetailActivity.this,
			// CircleTimeVideoViewActivity.class);

//			if (!videoView.isPlaying()) {
//				videoView.setVisibility(View.VISIBLE);
//				videoView.start();
//			} else
//				Toast.makeText(CircleTimeDetailActivity.this, "Video is playing", Toast.LENGTH_LONG)
//						.show();
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_recording_detail);
		// initDataBase
		initDatabase();
		// initControl
		initControl();
	}

	/**
	 * intialize control
	 */
	private void initControl() {
		// wheel
		if (_getScreenOrientation() == Configuration.ORIENTATION_PORTRAIT) {
			wheelSpotter1 = (WheelView) findViewById(R.id.wheel_recording_cycle_1);
			wheelSpotter2 = (WheelView) findViewById(R.id.wheel_recording_cycle_2);
			wheelSpotter3 = (WheelView) findViewById(R.id.wheel_recording_cycle_3);
			//
			wheelSpotter1.setVisibleItems(5);
			wheelSpotter1.setCurrentItem(0);
			wheelSpotter1.setViewAdapter(new RecoredDetailArrayAdapter(this, record_detail, 0));

			wheelSpotter2.setVisibleItems(5);
			wheelSpotter2.setCurrentItem(0);
			wheelSpotter2.setViewAdapter(new RecoredDetailArrayAdapter(this, record_detail, 0));

			wheelSpotter3.setVisibleItems(5);
			wheelSpotter3.setCurrentItem(0);
			wheelSpotter3.setViewAdapter(new RecoredDetailArrayAdapter(this, record_detail2, 0));
		}
		// button
		btnCompare = (Button) findViewById(R.id.btn_recording_detail_compare);
		btnPlay = (Button) findViewById(R.id.btn_recording_detail_play);
		btnCompare.setOnClickListener(this);
		btnPlay.setOnClickListener(this);
		// listview
		listViewRecording = (ListView) findViewById(R.id.listview_recording_detail);
		// set header for list view
		// LayoutInflater inflater = this.getLayoutInflater();
		// View header = inflater.inflate(R.layout.list_recording_header, null);
		// listViewRecording.addHeaderView(header);
		// calculate for list process
		populateListData();
		ListStepRecordingAdapter listProcessAdapter = new ListStepRecordingAdapter(this, null);
		listViewRecording.setAdapter(listProcessAdapter);
		listProcessAdapter.notifyDataSetChanged();
		listViewRecording.setOnItemClickListener(this);
		listViewRecording.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		// video view
//		videoView = (VideoView) findViewById(R.id.videoView_recording_detail);
//		videoView.setVisibility(View.GONE);
//		fileName = leanAppAndroidSharePreference.getFileName();
//		Log.e("file name", "file name " + fileName);
//		if ("".equals(fileName)) {
//			Toast.makeText(CircleTimeDetailActivity.this, "Error in loading video file",
//					Toast.LENGTH_LONG).show();
//		} else
//			controlVideoRecording();
	}

//	// add controls to a MediaPlayer like play, pause.
//	private void controlVideoRecording() {
//		MediaController mc = new MediaController(CircleTimeDetailActivity.this);
//		videoView.setMediaController(mc);
//		mc.setAnchorView(videoView);
//		mc.setMediaPlayer(videoView);
//		// Set the path of Video or URI
//		videoView.setVideoURI(Uri.parse(fileName));
//		// String myPath = getRealPathFromURI(photoUri);
//		videoView.setPressed(true);
//		// Set the focus
//		videoView.requestFocus();
//
//		// MediaPlayer mediaPlayer = MediaPlayer.create(this,
//		// Uri.parse(fileName));
//		// try {
//		// mediaPlayer.prepare();
//		// } catch (IllegalStateException e) {
//		// // TODO Auto-generated catch block
//		// e.printStackTrace();
//		// } catch (IOException e) {  
//		// // TODO Auto-generated catch block
//		// e.printStackTrace();
//		// }
//		//
//		videoView.setOnPreparedListener(this);
//		// onPrepared(mediaPlayer);
//	}

	/**
	 * calculate data for listview to display
	 */
	private void populateListData() {

	}

	/**
	 * initialize database
	 */
	private void initDatabase() {
		// share preference
		leanAppAndroidSharePreference = LeanAppAndroidSharePreference.getInstance(this);
		_currentProcessIdActive = leanAppAndroidSharePreference.getProcessIdActive();
		_currentProcessNameActive = leanAppAndroidSharePreference.getProcessNameActive();
		_currentProjectIdActive = leanAppAndroidSharePreference.getProjectIdActive();
		_currentProjectNameActive = leanAppAndroidSharePreference.getProjectNameActive();
		// intDatabase
		tStepsDataBaseHandler = new TStepsDataBaseHandler(this);
		tCycleTimeDataBaseHandler = new TCycleTimeDataBaseHandler(this);
	}

	private int _getScreenOrientation() {
		return getResources().getConfiguration().orientation;
	}

	/**
	 * Adapter for string based wheel. Highlights the current value.
	 */
	private class RecoredDetailArrayAdapter extends ArrayWheelAdapter<String> {
		// Index of current item
		int currentItem;
		// Index of item to be highlighted
		int currentValue;

		/**
		 * Constructor
		 */
		public RecoredDetailArrayAdapter(Context context, String[] items, int current) {
			super(context, items);
			this.currentValue = current;
			setTextSize(24);
		}

		@Override
		protected void configureTextView(TextView view) {
			super.configureTextView(view);
			if (currentItem == currentValue) {
				view.setTextColor(0xFF0000F0);
			}
			view.setTypeface(Typeface.SANS_SERIF);
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			currentItem = index;
			return super.getItem(index, cachedView, parent);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		// TODO Auto-generated method stub
		// long mediatime = videoView.getDuration() / 1000;// this returned
		// // the
		// // duration instead of//
		// Log.i(": mediatime", mediatime + "");
		// int hour = (int) mediatime / 3600;
		// int minute = (int) (mediatime - hour * 3600) / 60;
		// int second = (int) (mediatime - hour * 3600 - minute * 60);
		// timeOfVideo = second + 60 * minute + hour * 3600;
		// Toast.makeText(getBaseContext(), hour + ": " + minute + ": " +
		// second,
		// Toast.LENGTH_SHORT).show();
		// if (mediatime > 15) {
		// Toast.makeText(getBaseContext(), "out of time2 ",
		// Toast.LENGTH_SHORT).show();
		// }

	}
}
