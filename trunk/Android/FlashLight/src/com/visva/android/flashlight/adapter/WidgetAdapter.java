package com.visva.android.flashlight.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.visva.android.flashlight.R;
import com.visva.android.flashlight.common.ScreenLight;

public class WidgetAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private Resources resources;
	private ArrayList<ScreenLight> lstScreenLight = new ArrayList<ScreenLight>();
	private boolean isSmallScreen = false;

	public WidgetAdapter(Activity activity, ArrayList<ScreenLight> lst, boolean isSmallScreen) {
		this.isSmallScreen = isSmallScreen;
		this.lstScreenLight.addAll(lst);
		this.resources = activity.getResources();
		this.inflater = LayoutInflater.from(activity);
	}

	@Override
	public int getCount() {
		return lstScreenLight.size();
	}

	@Override
	public ScreenLight getItem(int position) {
		return lstScreenLight.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		WidgetViewHelper helper = null;
		if (convertView == null) {
			if (!isSmallScreen) {
				convertView = inflater.inflate(R.layout.widget_item, null);
			} else {
				convertView = inflater.inflate(R.layout.widget_item_small, null);
			}
			helper = new WidgetViewHelper();
			helper.set_iv((ImageView) convertView.findViewById(R.id.ivWidgetItem));
			helper.set_ivBackground((ImageView) convertView.findViewById(R.id.ivWidgetItemBackground));
			convertView.setTag(helper);
		} else {
			helper = (WidgetViewHelper) convertView.getTag();
		}

		helper.get_iv().setImageBitmap(BitmapFactory.decodeResource(resources, getItem(position).getBitmapId()));
		helper.get_ivBackground().setVisibility(View.INVISIBLE);
		return convertView;
	}

}
