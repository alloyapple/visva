package com.visva.voicerecorder.view.favourite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.visva.voicerecorder.R;
import com.visva.voicerecorder.model.FavouriteItem;
import com.visva.voicerecorder.utils.StringUtility;
import com.visva.voicerecorder.utils.Utils;
import com.visva.voicerecorder.view.activity.ActivityHome;
import com.visva.voicerecorder.view.common.IHomeActionListener;
import com.visva.voicerecorder.view.widget.FavouriteCircleImage;

public class FavouriteAdapter extends BaseAdapter {
    // ======================Constant Define=====================
    private static final int         _ID             = 0;
    private static final int         DISPLAY_NAME    = _ID + 1;
    private static final int         NUMBER          = DISPLAY_NAME + 1;
    private static final int         PHOTO_URI       = NUMBER + 1;
    // ======================Variable Define=====================
    LayoutInflater                   layoutInflater;
    private Context                  mContext;
    private IHomeActionListener      iHomeActionListener;
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
            holder.textPhoneName = (TextView) convertView.findViewById(R.id.text_name);
            convertView.setTag(holder);
        }
        final View view = convertView;
        holder = (ViewHolder) convertView.getTag();
        FavouriteItem favouriteItem = mFavouriteItems.get(position);

        Uri photoUri = Utils.getContactUriTypeFromPhoneNumber(mContext.getContentResolver(), favouriteItem.phoneNo, PHOTO_URI);
        if (photoUri != null) {
            holder.avatar.setImageURI(photoUri);
        } else {
            holder.avatar.setImageResource(R.drawable.ic_contact_picture_holo_light);
        }
        holder.textPhoneName.setFocusable(false);
        holder.textPhoneName.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                iHomeActionListener.onLongClickItemListener(view, position, ActivityHome.FRAGMENT_FAVOURITE, 0);
                return false;
            }
        });
        holder.textPhoneName.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                iHomeActionListener.onClickItemListener(view, position, ActivityHome.FRAGMENT_FAVOURITE, 0);
            }
        });

        if (StringUtility.isEmpty(favouriteItem.phoneName))
            holder.textPhoneName.setText(favouriteItem.phoneNo);
        else
            holder.textPhoneName.setText(favouriteItem.phoneName);
        return convertView;
    }

    static class ViewHolder {
        TextView  textPhoneName;
        ImageView avatar;
    }

    public void setListener(IHomeActionListener iHomeActionListener) {
        this.iHomeActionListener = iHomeActionListener;
    }

    public class MyComparator implements Comparator<FavouriteItem> {
        @Override
        public int compare(FavouriteItem p1, FavouriteItem p2) {
            return p1.phoneName.compareTo(p2.phoneName);
        }
    }
}
