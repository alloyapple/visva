package vsvteam.outsource.leanappandroid.adapter;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import vsvteam.outsource.leanappandroid.R;

public class SettingListAdapter extends BaseAdapter {
	private ArrayList<String> titleSongArray;

	private Context context;

	public SettingListAdapter(Context _context, ArrayList<String> arr) {
		titleSongArray = arr;
		context = _context;
	}

	public int getCount() {
		return titleSongArray.size();
	}

	public Object getItem(int arg0) {
		return titleSongArray.get(arg0);
	}

	public long getItemId(int arg0) {
		return 0;
	}

	public View getView(int position, View v, ViewGroup parent) {
		LayoutInflater inflate = ((Activity) context).getLayoutInflater();
		View view = (View) inflate.inflate(R.layout.list_setting_item_row, null);
		String label = titleSongArray.get(position);

		CheckBox songName = (CheckBox) view.findViewById(R.id.checkBox1);
		songName.setText(label);
		return view;
	}


	public int getSectionForPosition(int arg0) {
		return 0;
	}

	public Object[] getSections() {
		return null;
	}
}
