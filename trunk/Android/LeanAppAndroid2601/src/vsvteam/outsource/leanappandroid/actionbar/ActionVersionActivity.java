package vsvteam.outsource.leanappandroid.actionbar;

import java.util.ArrayList;
import java.util.List;

import vsvteam.outsource.leanappandroid.R;
import vsvteam.outsource.leanappandroid.database.LeanAppAndroidSharePreference;
import vsvteam.outsource.leanappandroid.database.TVersionDataBase;
import vsvteam.outsource.leanappandroid.database.TVersionDataBaseHandler;
import vsvteam.outsource.leanappandroid.tabbar.TabGroupValueStreamMapActivity;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class ActionVersionActivity extends Activity implements OnClickListener {
	private static final byte DARK = 1;
	private static final byte BRIGHT = 2;
	private ListView mListView;
	// private ArrayList<String> mListContent = new ArrayList<String>();
	private ImageView btnExport;
	private ImageView btnSetting;
	private ImageView btnVersion;
	private ImageView btnChangeProject;
	private ArrayList<VersionItem> mListVersions = new ArrayList<ActionVersionActivity.VersionItem>();
	private TVersionDataBaseHandler mVersionDataHandler;
	private LeanAppAndroidSharePreference leanAppAndroidSharePreference;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_action_version);
		visibleView((FrameLayout) findViewById(R.id.id_frame_parent_version),
				(LinearLayout) findViewById(R.id.id_linear_version),
				View.VISIBLE);
		// mListContent.add("Version1");
		// mListContent.add("Version2");
		// mListContent.add("Version3");
		leanAppAndroidSharePreference = LeanAppAndroidSharePreference
				.getInstance(this);
		mVersionDataHandler = new TVersionDataBaseHandler(this);

		Log.i("projectId", String.valueOf(leanAppAndroidSharePreference
				.getProjectIdActive()));
		List<TVersionDataBase> listVersionDataBase = mVersionDataHandler
				.getAllVersions(leanAppAndroidSharePreference
						.getProjectIdActive());
		Log.i("number", String.valueOf(listVersionDataBase.size()));
		for (int i = 0; i < listVersionDataBase.size(); i++) {
			VersionItem item = new VersionItem();
			item.mId = listVersionDataBase.get(i).getVersionNo();
			item.mName = listVersionDataBase.get(i).getVersionNote();
			item.mNote = listVersionDataBase.get(i).getVersionNote();
			mListVersions.add(item);
		}
		mListView = (ListView) findViewById(R.id.id_listview_version);
		mListView.setAdapter(new ListVersionAdapter(this, mListVersions));
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
		//

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

		private Activity mContext;
		private List<VersionItem> mListVersion;

		public ListVersionAdapter(Activity pContext,
				List<VersionItem> pListVersion) {
			// TODO Auto-generated constructor stub
			super();
			mContext = pContext;
			mListVersion = pListVersion;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mListVersion.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mListVersion.get(position);
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
			LayoutInflater mInflater = mContext.getLayoutInflater();
			ViewHolder holder = new ViewHolder();
	
				v = mInflater.inflate(R.layout.item_list_version, null);
				holder.txtNo = (TextView) v.findViewById(R.id.id_no_version);
				holder.txtName = (TextView) v
						.findViewById(R.id.id_name_version);
				holder.txtNote = (TextView) v
						.findViewById(R.id.id_note_version);
			
			holder.txtNo.setText(String.valueOf(mListVersion.get(position).mId));
			holder.txtName.setText(mListVersion.get(position).mName);
			holder.txtNote.setText(mListVersion.get(position).mNote);
			return v;
		}

	}

	private class ViewHolder {
		TextView txtNo;
		TextView txtName;
		TextView txtNote;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	class VersionItem {
		public int mId;
		public String mName;
		public String mNote;
	}
}
