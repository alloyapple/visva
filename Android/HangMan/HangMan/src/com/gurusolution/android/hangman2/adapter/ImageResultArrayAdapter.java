package com.gurusolution.android.hangman2.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.gurusolution.android.hangman2.R;
import com.gurusolution.android.hangman2.utils.ImageResult;
import com.loopj.android.image.SmartImageView;

/*
 * When our data changes in our model, change the view
 */
public class ImageResultArrayAdapter extends ArrayAdapter<ImageResult> {

	public ImageResultArrayAdapter(Context context, List<ImageResult> images) {
		super(context, R.layout.item_image_result, images);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageResult imageInfo = this.getItem(position);

		/*
		 * TODO: for tony read up on SmartImageView
		 */
		SmartImageView ivImage;

		// We don't have a view to re-use, inflate it from layout
		if (convertView == null) {
			LayoutInflater inflator = LayoutInflater.from(getContext());
			ivImage = (SmartImageView) inflator.inflate(R.layout.item_image_result, parent, false);
		} else {
			/*
			 * re-use the previous views to save resources, should be much
			 * better for memory
			 */
			ivImage = (SmartImageView) convertView;
			ivImage.setImageResource(android.R.color.transparent);
		}

		ivImage.setImageUrl(imageInfo.getThumbUrl());
		return ivImage;
	}

//	@Override
//	public int getCount() {
//		return 3;
//	}
}
