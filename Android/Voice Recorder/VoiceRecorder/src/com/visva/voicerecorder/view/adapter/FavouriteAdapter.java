package com.visva.voicerecorder.view.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.content.Context;
import android.net.Uri;
import android.provider.ContactsContract.Profile;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.visva.voicerecorder.R;
import com.visva.voicerecorder.model.FavouriteItem;
import com.visva.voicerecorder.utils.StringUtility;
import com.visva.voicerecorder.utils.Utils;
import com.visva.voicerecorder.view.widget.CircleImageView;

public class FavouriteAdapter extends BaseAdapter {
    // ======================Constant Define=====================
    private static final int         _ID             = 0;
    private static final int         DISPLAY_NAME    = _ID + 1;
    private static final int         NUMBER          = DISPLAY_NAME + 1;
    private static final int         PHOTO_URI       = NUMBER + 1;
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
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup arg2) {
        if (convertView == null) {
            convertView = this.layoutInflater.inflate(R.layout.favourite_item, null);
        }
        ViewHolder holder = new ViewHolder();
        FavouriteItem favouriteItem = mFavouriteItems.get(position);
        holder.avatar = (CircleImageView) convertView.findViewById(R.id.phone_avatar);
        Uri photoUri = Utils.getContactUriTypeFromPhoneNumber(mContext.getContentResolver(), favouriteItem.phoneNo, PHOTO_URI);
        if (photoUri != null) {
            holder.avatar.setImageURI(photoUri);
        } else {
            holder.avatar.setImageResource(R.drawable.ic_contact_picture_holo_light);
        }
        holder.avatar.setFocusable(false);
        holder.avatar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d("KieuThang", "onClick:" + position);
            }
        });
        holder.textPhoneName = (TextView) convertView.findViewById(R.id.text_name);
        if (StringUtility.isEmpty(favouriteItem.phoneName))
            holder.textPhoneName.setText(favouriteItem.phoneNo);
        else
            holder.textPhoneName.setText(favouriteItem.phoneName);
        return convertView;
    }

    private class ViewHolder {
        TextView        textPhoneName;
        CircleImageView avatar;
    }

    public class MyComparator implements Comparator<FavouriteItem> {
        @Override
        public int compare(FavouriteItem p1, FavouriteItem p2) {
            return p1.phoneName.compareTo(p2.phoneName);
        }
    }
}
