package com.visva.android.app.funface.view.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.visva.android.app.funface.R;
import com.visva.android.app.funface.view.widget.FaceView;

/** An array adapter that knows how to render views when given CustomData classes */
public class CustomArrayAdapter extends BaseAdapter {
    private LayoutInflater       mInflater;
    private Context              mContext;
    private ArrayList<FaceView> mListItems;

    public CustomArrayAdapter(Context context, ArrayList<FaceView> values) {
        this.mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mListItems = values;
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
        holder.imageItem.setBackgroundResource(getItem(position).getResId());
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
    public FaceView getItem(int arg0) {
        return mListItems.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }
}
