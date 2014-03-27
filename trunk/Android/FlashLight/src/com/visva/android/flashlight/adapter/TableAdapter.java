//package com.ninesixapps.flashlightpro.adapter;
//
//import java.util.ArrayList;
//
//import android.content.Context;
//import android.content.res.Resources;
//import android.graphics.BitmapFactory;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TableLayout;
//import android.widget.TableRow;
//
//import com.ninesixapps.flashlightpro.R;
//import com.ninesixapps.flashlightpro.activites.FeatureActivity;
//import com.ninesixapps.flashlightpro.common.ScreenLight;
//
//public class TableAdapter {
//
//	private TableLayout tableLayout;
//	private TableRow row;
//	private LayoutInflater inflater;
//	private boolean isSmallScreen = false;
//	private FeatureActivity activity;
//	private Context context;
//	private Resources resources;
//	private ArrayList<ScreenLight> lstScreenLight = new ArrayList<ScreenLight>();
//	private FeatureViewHelper helper = null;
//	private View convertView = null;
//	private int size;
//	private int sizeRemain;
//	private int numberColumn;
//	private int numberRow;
//
//	public TableAdapter(FeatureActivity activity, boolean isSmallScreen, ArrayList<ScreenLight> lstScreenLight) {
//		this.lstScreenLight.addAll(lstScreenLight);
//		this.size = lstScreenLight.size();
//		this.activity = activity;
//		this.context = activity.getBaseContext();
//		this.isSmallScreen = isSmallScreen;
//		this.resources = activity.getResources();
//		this.inflater = LayoutInflater.from(context);
//	}
//
//	public TableLayout getTableLayout() {
//		return tableLayout;
//	}
//
//	private int getSize() {
//		return lstScreenLight.size();
//	}
//
//	private int getSizeRemain() {
//		return 0;
//	}
//
//	private void initTableLayout() {
//		tableLayout = new TableLayout(context);
//		size = getSize();
//		if (size == 1) {
//			numberColumn = 1;
//			numberRow = 1;
//		} else if (size == 2) {
//			numberColumn = 2;
//			numberRow = 1;
//		} else {
//			numberColumn = 3;
//			if (size % numberColumn == 0) {
//				numberRow = size / numberColumn;
//			} else {
//				numberRow = size / numberColumn + 1;
//			}
//
//		}
//
//		TableRow row = null;
//		for (int i = 0; i < numberRow; i++) {
//			if (sizeRemain < numberColumn) {
//				numberColumn = sizeRemain;
//			}
//			row = initTableRow(numberColumn);
//			tableLayout.addView(row);
//			sizeRemain = size - numberColumn - sizeRemain;
//		}
//	}
//
//	private TableRow initTableRow(int numberItem) {
//		TableRow row = new TableRow(context);
//		LinearLayout linearLayout = new LinearLayout(context);
//		linearLayout.setOrientation(LinearLayout.HORIZONTAL);
//		linearLayout.setGravity(Gravity.CENTER);
//		for (int i = 0; i < numberItem; i++) {
//			linearLayout.addView(iniView(i));
//		}
//		row.addView(linearLayout);
//		return row;
//	}
//
//	public View iniView(int position) {
//		convertView = null;
//		final int pos = position;
//		if (!isSmallScreen) {
//			convertView = inflater.inflate(R.layout.item_feature, null);
//		} else {
//			convertView = inflater.inflate(R.layout.item_feature_small, null);
//		}
//		helper = new FeatureViewHelper();
//		helper.set_iv((ImageView) convertView.findViewById(R.id.ivItemFeature));
//		helper.set_ivBackground((ImageView) convertView.findViewById(R.id.ivItemFeatureBackground));
//		helper.set_btn((Button) convertView.findViewById(R.id.btItemFeature));
//		helper.get_btn().setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				activity.switchToScreen(lstScreenLight.get(pos).getId());
//			}
//		});
//		convertView.setTag(helper);
//		helper.get_iv().setImageBitmap(
//				BitmapFactory.decodeResource(resources, lstScreenLight.get(position).getBitmapId()));
//
//		if (lstScreenLight.get(position).getId() == 11) {
//			helper.get_ivBackground().setVisibility(View.INVISIBLE);
//			helper.get_iv().setVisibility(View.INVISIBLE);
//			helper.get_btn().setVisibility(View.INVISIBLE);
//		}
//		return convertView;
//	}
//
//}
