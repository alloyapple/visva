package vsvteam.outsource.leanappandroid.actionbar;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import vsvteam.outsource.leanappandroid.R;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

public class ActionVersionActivity extends Activity implements OnClickListener{
	private static final byte DARK = 1;
	private static final byte BRIGHT = 2;
	private ListView mListView;
	private ArrayList<String> mListContent = new ArrayList<String>();
	private ImageView btnExport;
	private ImageView btnSetting;
	private ImageView btnVersion;
	private ImageView btnChangeProject;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_action_version);
		visibleView((FrameLayout) findViewById(R.id.id_frame_parent_version),
				(LinearLayout) findViewById(R.id.id_linear_version),
				View.VISIBLE);
		mListContent.add("Version1");
		mListContent.add("Version2");
		mListContent.add("Version3");
		mListView = (ListView) findViewById(R.id.id_listview_version);
		mListView.setAdapter(new ListVersionAdapter(this));
		btnExport = (ImageView) findViewById(R.id.img_choice_project_export);
		btnExport.setOnClickListener(this);
		//
		btnSetting = (ImageView) findViewById(R.id.img_choice_project_setting);
		btnSetting.setOnClickListener(this);
		//
		btnVersion = (ImageView) findViewById(R.id.img_choice_project_version);
		btnVersion.setOnClickListener(this);
		//
		btnChangeProject = (ImageView) findViewById(R.id.img_choice_project_change_project);
		btnChangeProject.setOnClickListener(this);
	}

	public void setTheme(View v, byte type) {
		AlphaAnimation alpha;
		if (type == DARK)
			alpha = new AlphaAnimation(0.3F, 0.3F);
		else
			alpha = new AlphaAnimation(1.0F, 1.0F);
		alpha.setDuration(0); // Make animation instant
		alpha.setFillAfter(true); // Tell it to persist after the animation ends
		// And then on your layout
		v.startAnimation(alpha);

	}

	public void visibleView(View parent, View v, int visible) {
		if (visible == View.VISIBLE) {
			setTheme(parent, DARK);
			v.setVisibility(View.VISIBLE);
			ScaleAnimation scale = new ScaleAnimation(1, 1, 0, 1,
					Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0f);
			scale.setDuration(500);
			v.startAnimation(scale);
		} else {
			ScaleAnimation scale = new ScaleAnimation(1, 1, 1, 0,
					Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 1f);
			scale.setDuration(300);
			v.startAnimation(scale);
			v.setVisibility(View.GONE);
			setTheme(parent, BRIGHT);
		}
	}

	class ListVersionAdapter extends BaseAdapter {

		private Context mContext;

		public ListVersionAdapter(Context pContext) {
			// TODO Auto-generated constructor stub
			mContext = pContext;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 4;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub

			View v = convertView;
			LayoutInflater mInflater = LayoutInflater.from(mContext);
			v = mInflater.inflate(R.layout.item_list_version, null);
			return v;
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
}
