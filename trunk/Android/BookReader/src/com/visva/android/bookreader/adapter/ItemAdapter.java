package com.visva.android.bookreader.adapter;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.samsung.svmc.pdfreader.R;
import com.samsung.svmc.pdfreader.R.drawable;
import com.samsung.svmc.pdfreader.R.id;
import com.visva.android.bookreader.io.FileSettings;
import com.visva.android.bookreader.pdfreader.FileExplorer;
import com.visva.android.bookreader.pdfreader.Item;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class ItemAdapter extends ArrayAdapter<Item>{
	
	private Context context; 
	private int layoutResourceId;    
	private List<Item> data = new ArrayList<Item>();
	private FileSettings mFileSettings;
	private FileExplorer mFileExplorer;
	private String aPathRecentFile;

	public ItemAdapter(Context context, int layoutResourceId, List<Item> data) {
		super(context, layoutResourceId, data);
		this.context = context;
		this.layoutResourceId = layoutResourceId;
		this.data = data;
		mFileSettings = new FileSettings();
		mFileExplorer = new FileExplorer();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View row = convertView;
		ItemHoder holder = null;
		
		if (row == null){
			LayoutInflater inflater = ((Activity)context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
			
			holder = new ItemHoder();
			holder.imgIcon = (ImageView) row.findViewById(R.id.imgIcon);
			holder.name = (TextView) row.findViewById(R.id.txtName);
			holder.date = (TextView) row.findViewById(R.id.txtDate);
			holder.info = (TextView) row.findViewById(R.id.txtInfo);
			holder.deleteIcon = (ImageView) row.findViewById(R.id.imgDeleteRecentFile);
			holder.ll = (LinearLayout) row.findViewById(R.id.ll);
			
			row.setTag(holder);
		}
		else{
			holder = (ItemHoder)row.getTag();
		}
		
		Item item = data.get(position);
		holder.imgIcon.setImageResource(item.getViewIcon());
		holder.name.setText(item.getName());
		holder.date.setText(item.getDate());
		holder.info.setText(item.getInfo());
		holder.deleteIcon.setImageResource(item.getDeleteIcon());
		
		if (item.getDeleteIcon() == R.drawable.img_null) {
			holder.ll.setWeightSum(5);
			
			LinearLayout.LayoutParams paramTxtDate = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
			paramTxtDate.weight = 4.0f;
			holder.date.setLayoutParams(paramTxtDate);
			
			LinearLayout.LayoutParams paramTxtInfo = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
			paramTxtInfo.weight = 1.0f;
			holder.info.setLayoutParams(paramTxtInfo);
			
			holder.deleteIcon.setVisibility(View.GONE);
		} else {
			holder.info.setVisibility(View.GONE);
			
			holder.ll.setWeightSum(5);
			
			LinearLayout.LayoutParams paramTxtDate = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
			paramTxtDate.weight = 4.0f;
			holder.date.setLayoutParams(paramTxtDate);
			
			LinearLayout.LayoutParams paramImgDelete = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
			paramImgDelete.weight = 1.0f;
			holder.deleteIcon.setLayoutParams(paramImgDelete);
			
			final File aFile = item.getFile();
			
			holder.deleteIcon.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					aPathRecentFile = aFile.getAbsolutePath();
					mFileSettings.deleteARecentFile(aPathRecentFile);
					
					List<Item> mListItems = mFileExplorer.addToListItemsFromRecentFiles();
					mFileExplorer.resetAdapter(mListItems);
				}
			});
			
		}
		
		return row;
	}



	static class ItemHoder
    {
        ImageView imgIcon;
        TextView name;
        TextView date;
        TextView info;
        ImageView deleteIcon;
        LinearLayout ll;
    }
}
