package vsvteam.outsource.android.soundeffect.adapter;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import vsvteam.outsource.android.soundeffect.R;

public class MyRecordListAdapter extends BaseAdapter{
	private ArrayList<String> titleSongArray;

	private Context context;

	public MyRecordListAdapter(Context _context, ArrayList<String> arr) {
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
		View view = (View) inflate.inflate(R.layout.list_record_playlist_row, null);
		String label = titleSongArray.get(position);
		TextView songName = (TextView) view.findViewById(R.id.textViewRecordName);
		songName.setText(label);
		songName.setSelected(true);
		return view;
	}
}
