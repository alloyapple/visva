package com.visva.android.flashlight.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.visva.android.flashlight.common.Key;
import com.visva.android.flashlightmaster.R;

public class ShareAdapter extends BaseAdapter implements Key {

	private Context _context;
	private LayoutInflater _inflater;

	public ShareAdapter(Context context) {
		this._context = context;
		this._inflater = LayoutInflater.from(_context);
	}

	@Override
	public int getCount() {
		return SHARES.length;
	}

	@Override
	public String getItem(int position) {
		return SHARES[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ShareViewHelper __helper = null;
		if (convertView == null) {
			convertView = _inflater.inflate(R.layout.item_share, null);
			__helper = new ShareViewHelper();
			__helper.set_tvName((TextView) convertView.findViewById(R.id.tvName));
			convertView.setTag(__helper);
		} else {
			__helper = (ShareViewHelper) convertView.getTag();
		}

		__helper.setName(SHARES[position]);
		return convertView;
	}

}
