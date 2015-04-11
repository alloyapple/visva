package com.visva.voicerecorder.view.favourite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.visva.voicerecorder.R;
import com.visva.voicerecorder.model.FavouriteItem;
import com.visva.voicerecorder.utils.StringUtility;
import com.visva.voicerecorder.utils.Utils;

public class FavouriteAdapter extends BaseAdapter {
    // ======================Constant Define=====================
    // ======================Variable Define=====================
    LayoutInflater                   layoutInflater;
    private Context                  mContext;
    private ArrayList<FavouriteItem> mFavouriteItems = new ArrayList<FavouriteItem>();

    public FavouriteAdapter(Context context, ArrayList<FavouriteItem> favouriteItems) {
        this.mContext = context;
        this.mFavouriteItems = favouriteItems;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Collections.sort(mFavouriteItems, new MyComparator());
    }

    @Override
    public int getCount() {
        return mFavouriteItems.size();
    }

    @Override
    public FavouriteItem getItem(int arg0) {
        return this.mFavouriteItems.get(arg0);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup arg2) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = this.layoutInflater.inflate(R.layout.favourite_item, null);
            holder = new ViewHolder();
            holder.avatar = (ImageView) convertView.findViewById(R.id.phone_avatar);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        FavouriteItem favouriteItem = mFavouriteItems.get(position);
        Uri photoUri = null;
        if (StringUtility.isEmpty(favouriteItem.phoneNo)) {
            holder.avatar.setImageResource(R.drawable.ic_contact_picture_holo_light);
        } else
            photoUri = Utils.getPhotoUriFromPhoneNumber(mContext.getContentResolver(), favouriteItem.phoneNo);
        if (photoUri != null) {
            holder.avatar.setImageURI(photoUri);
        } else {
            holder.avatar.setImageResource(R.drawable.ic_contact_picture_holo_light);
        }
        return convertView;
    }

    static class ViewHolder {
        TextView  textPhoneName;
        ImageView avatar;
    }

    public class MyComparator implements Comparator<FavouriteItem> {
        @Override
        public int compare(FavouriteItem p1, FavouriteItem p2) {
            return p1.phoneName.compareTo(p2.phoneName);
        }
    }

    public void removeFavoriteItem(FavouriteItem favouriteItem) {
        if (mFavouriteItems.size() > 0 && mFavouriteItems.contains(favouriteItem))
            mFavouriteItems.remove(favouriteItem);
        notifyDataSetChanged();
    }

    public void updateFavoriteList(ArrayList<FavouriteItem> favouriteItems) {
        mFavouriteItems.clear();
        mFavouriteItems = favouriteItems;
//        notifyDataSetChanged();
    }
}
