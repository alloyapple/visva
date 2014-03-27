package com.visva.android.flashlight.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.visva.android.flashlight.R;
import com.visva.android.flashlight.activities.FeatureActivity;
import com.visva.android.flashlight.common.ScreenLight;

public class CustomAdapter {

	private TableLayout tableLayout;
	private LayoutInflater inflater;
	private boolean isSmallScreen = false;
	private FeatureActivity activity;
	private Context context;
	private Resources resources;
	private ArrayList<ScreenLight> lstScreenLight = new ArrayList<ScreenLight>();
	private FeatureViewHelper helper = null;
	private View convertView = null;
	private int size;
	private int sizeRemain;
	private int countNumberItem = 0;
	private int numberColumn;
	private int numberRow;
	private int positionItem = 0;

	public CustomAdapter(FeatureActivity activity, boolean isSmallScreen, ArrayList<ScreenLight> lstScreenLight) {
		this.lstScreenLight.addAll(lstScreenLight);
		this.size = lstScreenLight.size();
		this.activity = activity;
		this.context = activity.getBaseContext();
		this.isSmallScreen = isSmallScreen;
		this.resources = activity.getResources();
		this.inflater = LayoutInflater.from(context);
		initTableLayout();
	}

	public TableLayout getTableLayout() {
		return tableLayout;
	}

	private int getSize() {
		return lstScreenLight.size();
	}

	private void initTableLayout() {
		tableLayout = new TableLayout(context);
		tableLayout.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
				TableRow.LayoutParams.WRAP_CONTENT));
		tableLayout.setStretchAllColumns(true);

		size = getSize();
		if (size == 1) { // Only has one item
			numberColumn = 1;
			numberRow = 1;
		} else if (size == 2) {  // two items
			numberColumn = 2;
			numberRow = 1;
		} else { // other case
			numberColumn = 3;
			if (size % numberColumn == 0) {
				numberRow = size / numberColumn;
			} else {
				numberRow = size / numberColumn + 1;
			}
		}

		TableRow row = null;
		for (int i = 0; i < numberRow; i++) {
			if (sizeRemain < numberColumn && sizeRemain != 0) {
				numberColumn = sizeRemain;
			}
			// Add rows to table
			row = initTableRow(numberColumn);
			tableLayout.addView(row);
			countNumberItem += numberColumn;
			sizeRemain = size - countNumberItem;
		}
	}

	// Init a row
	private TableRow initTableRow(int numberColumn) {
		TableRow row = new TableRow(context);
		row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
				TableRow.LayoutParams.WRAP_CONTENT));
		LinearLayout linearLayout = new LinearLayout(context);
		linearLayout.setOrientation(LinearLayout.HORIZONTAL);
		linearLayout.setGravity(Gravity.CENTER);
		linearLayout.setPadding(3, 3, 3, 3);
		for (int i = 0; i < numberColumn; i++) {
			linearLayout.addView(iniView(positionItem));
			positionItem++;
		}
		row.addView(linearLayout);
		return row;
	}

	// Create a view
	public View iniView(final int position) {
		convertView = null;
		if (!isSmallScreen) {
			convertView = inflater.inflate(R.layout.item_custom, null);
		} else {
			convertView = inflater.inflate(R.layout.item_custom_small, null);
		}
		convertView.setPadding(3, 3, 3, 3);
		helper = new FeatureViewHelper();
		helper.set_iv((ImageView) convertView.findViewById(R.id.ivItemCustom));
		helper.set_ivBackground((ImageView) convertView.findViewById(R.id.ivItemCustomBackground));
		helper.set_btn((Button) convertView.findViewById(R.id.btItemCustom));
		helper.get_btn().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				activity.switchToScreen(lstScreenLight.get(position).getId());
			}
		});
		convertView.setTag(helper);
		helper.get_iv().setImageBitmap(
				BitmapFactory.decodeResource(resources, lstScreenLight.get(position).getBitmapId()));

		if (lstScreenLight.get(position).getId() == 11) {
			helper.get_ivBackground().setVisibility(View.INVISIBLE);
			helper.get_iv().setVisibility(View.INVISIBLE);
			helper.get_btn().setVisibility(View.INVISIBLE);
		}
		return convertView;
	}
}
