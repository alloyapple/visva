package com.visva.android.app.funface.view.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.visva.android.app.funface.R;
import com.visva.android.app.funface.model.EffectItem;
import com.visva.android.app.funface.view.widget.FaceView;

/** An array adapter that knows how to render views when given CustomData classes */
public class EffectAdapter extends BaseAdapter {
    private LayoutInflater        mInflater;
    private Context               mContext;
    private ArrayList<EffectItem> mEffectItems;

    public EffectAdapter(Context context, ArrayList<EffectItem> values) {
        this.mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mEffectItems = values;
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
            holder.imageEffect = (ImageView) convertView.findViewById(R.id.image_effect);
            holder.textEffect = (TextView) convertView.findViewById(R.id.text_effect);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        // Populate the text
        // holder.imageItem.setBackgroundResource(getItem(position).getResId());
        holder.textEffect.setText(getItem(position).textEffect);
        return convertView;
    }

    /** View holder for the views we need access to */
    private static class Holder {
        public ImageView imageEffect;
        public TextView  textEffect;
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
}
