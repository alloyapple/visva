package com.visva.android.app.funface.view.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.visva.android.app.funface.R;
import com.visva.android.app.funface.model.EffectItem;
import com.visva.android.app.funface.utils.Utils;

/** An array adapter that knows how to render views when given CustomData classes */
public class FrameAdapter extends BaseAdapter {
    private LayoutInflater        mInflater;
    private Context               mContext;
    private ArrayList<EffectItem> mListItems;
    private DisplayImageOptions   options;
    private ImageLoadingListener  animateFirstListener = new AnimateFirstDisplayListener();
    private int                   mCurrentSelectedIndex;

    public FrameAdapter(Context context, ArrayList<EffectItem> values) {
        this.mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mListItems = values;
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.empty_photo)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_close)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .displayer(new RoundedBitmapDisplayer(20)).build();
        mCurrentSelectedIndex = -1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;

        if (convertView == null) {
            // Inflate the view since it does not exist
            convertView = mInflater.inflate(R.layout.frame_item, parent, false);

            // Create and save off the holder in the tag so we get quick access to inner fields
            // This must be done for performance reasons
            holder = new Holder();
            holder.imageItem = (ImageView) convertView.findViewById(R.id.img_item);
            holder.imgSeletedItem = (ImageView) convertView.findViewById(R.id.img_seleted_item);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        if (getItem(position).isSelected)
            holder.imgSeletedItem.setVisibility(View.VISIBLE);
        else
            holder.imgSeletedItem.setVisibility(View.GONE);
        // Populate the text
        if (getItem(position).effectId != 0) {
            String uri = Utils.convertResourceToImageLoaderUri(mContext, getItem(position).effectId);
            ImageLoader.getInstance().displayImage(uri, holder.imageItem, options, animateFirstListener);
        }
        return convertView;
    }

    /** View holder for the views we need access to */
    private static class Holder {
        public ImageView imageItem;
        public ImageView imgSeletedItem;
    }

    @Override
    public int getCount() {
        return mListItems.size();
    }

    @Override
    public EffectItem getItem(int arg0) {
        return mListItems.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }

    public void updateSelectedItem(int position) {
        Log.d("KieuThang", "updateSelectedItem position:" + position + ",mCurrentSelectedIndex: " + mCurrentSelectedIndex);
        if (mCurrentSelectedIndex == position)
            return;
        EffectItem effectItem = mListItems.get(position);
        effectItem.isSelected = true;

        if (mCurrentSelectedIndex != -1) {
            EffectItem preSeletedItem = mListItems.get(mCurrentSelectedIndex);
            preSeletedItem.isSelected = false;
        }
        mCurrentSelectedIndex = position;
        notifyDataSetChanged();
    }
}
