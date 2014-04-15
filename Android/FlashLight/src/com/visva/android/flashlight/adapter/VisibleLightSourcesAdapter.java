package com.visva.android.flashlight.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.visva.android.flashlight.common.Key;
import com.visva.android.flashlight.common.SaveVisibleLightSource;
import com.visva.android.flashlight.common.ScreenLight;
import com.visva.android.flashlightmaster.R;

public class VisibleLightSourcesAdapter extends BaseAdapter implements Key {
	private LayoutInflater _inflater;
	private ArrayList<ScreenLight> _lstScreenLights = new ArrayList<ScreenLight>();

	public VisibleLightSourcesAdapter(Context context, ArrayList<ScreenLight> lst) {
		this._inflater = LayoutInflater.from(context);
		_lstScreenLights.addAll(lst);
	}

	@Override
	public int getCount() {
		return _lstScreenLights.size();
	}

	@Override
	public ScreenLight getItem(int position) {
		return _lstScreenLights.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final int pos = position;
		VisibleLightSourcesViewHelper __helper = null;
		if (convertView == null) {
			convertView = _inflater.inflate(R.layout.item_visible_light_sources, null);
			__helper = new VisibleLightSourcesViewHelper();
			__helper.setCbName((CheckBox) convertView.findViewById(R.id.cbName));
			__helper.setTvName((TextView) convertView.findViewById(R.id.tvName));
			__helper.getCbName().setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					CheckBox cb = (CheckBox) v;
					ScreenLight screenLight = (ScreenLight) cb.getTag();
					screenLight.setCheck(cb.isChecked());
					SaveVisibleLightSource.updateVisibleLightSource(cb.isChecked(), _lstScreenLights.get(pos).getId());
				}
			});
			convertView.setTag(__helper);
		} else {
			__helper = (VisibleLightSourcesViewHelper) convertView.getTag();
		}
		__helper.getCbName().setTag(getItem(position));
		__helper.getTvName().setText(getItem(position).getName());
		__helper.getCbName().setChecked(getItem(position).isCheck());

		return convertView;
	}
}
