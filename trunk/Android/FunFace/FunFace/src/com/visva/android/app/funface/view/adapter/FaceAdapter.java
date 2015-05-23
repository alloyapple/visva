package com.visva.android.app.funface.view.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
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
public class FaceAdapter extends BaseAdapter {
    private LayoutInflater       mInflater;
    private Context              mContext;
    private ArrayList<EffectItem>  mListItems;
    private DisplayImageOptions  options;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    public FaceAdapter(Context context, ArrayList<EffectItem> values) {
        this.mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mListItems = values;
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_stub)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_close)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .displayer(new RoundedBitmapDisplayer(20)).build();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;

        if (convertView == null) {
            // Inflate the view since it does not exist
            convertView = mInflater.inflate(R.layout.options_item_view, parent, false);

            // Create and save off the holder in the tag so we get quick access to inner fields
            // This must be done for performance reasons
            holder = new Holder();
            holder.imageItem = (ImageView) convertView.findViewById(R.id.image_item);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        // Populate the text
        String uri = Utils.convertResourceToImageLoaderUri(mContext, getItem(position).effectId);
        ImageLoader.getInstance().displayImage(uri, holder.imageItem, options, animateFirstListener);
        return convertView;
    }

    /** View holder for the views we need access to */
    private static class Holder {
        public ImageView imageItem;
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
}
