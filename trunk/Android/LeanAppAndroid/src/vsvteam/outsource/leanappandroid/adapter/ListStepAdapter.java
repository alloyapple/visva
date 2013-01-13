package vsvteam.outsource.leanappandroid.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import vsvteam.outsource.leanappandroid.define.Constant;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import vsvteam.outsource.leanappandroid.R;

/**
 * 
 * @author vsv team
 */
public class ListStepAdapter extends BaseAdapter {
	public ArrayList<HashMap<String, String>> list;
	Activity activity;

	public ListStepAdapter(Activity activity, ArrayList<HashMap<String, String>> list) {
		super();
		this.activity = activity;
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	private class ViewHolder {
		TextView txtFirst;
		TextView txtSecond;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		ViewHolder holder;
		LayoutInflater inflater = activity.getLayoutInflater();

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.liststep_row, null);
			holder = new ViewHolder();
			holder.txtFirst = (TextView) convertView.findViewById(R.id.first_step_text);
			holder.txtSecond = (TextView) convertView.findViewById(R.id.second_step_text);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		HashMap<String, String> map = list.get(position);
		holder.txtFirst.setText(map.get(Constant.FIRST_COLUMN));
		holder.txtSecond.setText(map.get(Constant.SECOND_COLUMN));

		return convertView;
	}

}
