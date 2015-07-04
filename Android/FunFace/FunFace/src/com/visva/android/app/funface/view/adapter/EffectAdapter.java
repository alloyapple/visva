package com.visva.android.app.funface.view.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.visva.android.app.funface.R;
import com.visva.android.app.funface.imageprocessing.ImageEffectLoader;
import com.visva.android.app.funface.model.EffectItem;

/** An array adapter that knows how to render views when given CustomData classes */
public class EffectAdapter extends BaseAdapter {
    private LayoutInflater        mInflater;
    private Context               mContext;
    private ArrayList<EffectItem> mEffectItems;
    private Bitmap                mBitmap;
    private int                   mCurrentSelectedIndex;

    public EffectAdapter(Context context, ArrayList<EffectItem> values) {
        this.mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mEffectItems = values;
        mBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_custom_service);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;

        if (convertView == null) {
            // Inflate the view since it does not exist
            convertView = mInflater.inflate(R.layout.effect_view, parent, false);

            // Create and save off the holder in the tag so we get quick access to inner fields
            // This must be done for performance reasons
            holder = new Holder();
            holder.imageEffect = (ImageView) convertView.findViewById(R.id.img_effect);
            holder.textEffect = (TextView) convertView.findViewById(R.id.tv_effect);
            holder.imgSelectedItem = (ImageView) convertView.findViewById(R.id.img_seleted_item);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        // Populate the text
        ImageEffectLoader.getInstance(mContext).displayImage(holder.imageEffect, position, mBitmap, false);
        holder.textEffect.setText(getItem(position).textEffect);
        if (getItem(position).isSelected)
            holder.imgSelectedItem.setVisibility(View.VISIBLE);
        else
            holder.imgSelectedItem.setVisibility(View.GONE);
        return convertView;
    }

    /** View holder for the views we need access to */
    private static class Holder {
        public ImageView imageEffect;
        public TextView  textEffect;
        public ImageView imgSelectedItem;
    }

    @Override
    public int getCount() {
        return mEffectItems.size();
    }

    @Override
    public EffectItem getItem(int arg0) {
        return mEffectItems.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    public void updateSelectedItem(int position) {
        EffectItem effectItem = mEffectItems.get(position);
        effectItem.isSelected = true;

        if (mCurrentSelectedIndex != -1) {
            EffectItem preSeletedItem = mEffectItems.get(mCurrentSelectedIndex);
            preSeletedItem.isSelected = false;
        }
        mCurrentSelectedIndex = position;
        notifyDataSetChanged();
    }
}
