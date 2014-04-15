package com.visva.android.flashlight.adapter;

import java.util.ArrayList;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;

import com.visva.android.flashlight.activities.FeatureActivity;
import com.visva.android.flashlight.common.Key;
import com.visva.android.flashlight.common.ScreenLight;
import com.visva.android.flashlightmaster.R;

public class FeatureAdapter extends BaseAdapter implements Key {

	private LayoutInflater inflater;
	private Resources resources;
	private ArrayList<ScreenLight> lstScreenLight = new ArrayList<ScreenLight>();
	private boolean isSmallScreen = false;
	private FeatureActivity activity;

	public FeatureAdapter(FeatureActivity activity, ArrayList<ScreenLight> lst, boolean isSmallScreen) {
		this.activity = activity;
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
		FeatureViewHelper helper = null;
		final int pos = position;
		if (convertView == null) {
			if (!isSmallScreen) {
				convertView = inflater.inflate(R.layout.item_feature, null);
			} else {
				convertView = inflater.inflate(R.layout.item_feature_small, null);
			}
			helper = new FeatureViewHelper();
			helper.set_iv((ImageView) convertView.findViewById(R.id.ivItemFeature));
			helper.set_ivBackground((ImageView) convertView.findViewById(R.id.ivItemFeatureBackground));
			helper.set_btn((Button) convertView.findViewById(R.id.btItemFeature));
			helper.get_btn().setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					activity.switchToScreen(getItem(pos).getId());
				}
			});
			convertView.setTag(helper);
		} else {
			helper = (FeatureViewHelper) convertView.getTag();
		}
		helper.get_iv().setImageBitmap(BitmapFactory.decodeResource(resources, getItem(position).getBitmapId()));

		if (getItem(position).getId() == 11) {
			helper.get_ivBackground().setVisibility(View.INVISIBLE);
			helper.get_iv().setVisibility(View.INVISIBLE);
			helper.get_btn().setVisibility(View.INVISIBLE);
		}
		return convertView;
	}

}
