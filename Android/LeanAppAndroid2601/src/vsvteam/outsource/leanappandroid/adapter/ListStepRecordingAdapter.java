package vsvteam.outsource.leanappandroid.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class ListStepRecordingAdapter extends BaseAdapter {

	public ArrayList<HashMap<String, String>> list;
	Activity activity;

	public ListStepRecordingAdapter(Activity activity,
			ArrayList<HashMap<String, String>> list) {
		super();
		this.activity = activity;
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		return null;
	}

}
