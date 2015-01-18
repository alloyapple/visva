package com.gurusolution.android.hangman2.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.gurusolution.android.hangman2.R;

public class AdapterShareSns extends BaseAdapter {
	private Context mContext;
	private ArrayList<Integer> mSNSList = new ArrayList<Integer>();

	public AdapterShareSns(Context context) {
		this.mContext = context;
		mSNSList.add(R.drawable.btn_facebook_selector);
		mSNSList.add(R.drawable.btn_gg_plus_selector);
	}

	@Override
	public int getCount() {
		return mSNSList.size();
	}

	@Override
	public Object getItem(int position) {
		return mSNSList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return mSNSList.get(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.drawer_list_item, null, false);
			ImageView img = (ImageView) convertView.findViewById(R.id.image_sns_icon);
			img.setImageResource(mSNSList.get(position));
		}
		return convertView;
	}

}
