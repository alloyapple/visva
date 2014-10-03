package com.sharebravo.bravo.view.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sharebravo.bravo.R;
import com.sharebravo.bravo.foursquare.imgloader.ImageLoader;
import com.sharebravo.bravo.model.response.Spot;
import com.sharebravo.bravo.utils.StringUtility;

public class AdapterDuplicateSpots extends BaseAdapter {
    public static int             SEARCH_FOR_SPOT             = 0;
    public static int             SEARCH_LOCAL_BRAVO          = 1;
    public static int             SEARCH_LOCAL_BRAVO_KEY      = 2;
    public static int             SEARCH_ARROUND_ME           = 3;
    public static int             SEARCH_ARROUND_KEY          = 4;
    public static int             SEARCH_PEOPLE_FOLLOWING     = 5;
    public static int             SEARCH_PEOPLE_FOLLOWING_KEY = 6;
    private FragmentActivity      mContext                    = null;
    private ImageLoader           mImageLoader                = null;
    private ArrayList<Spot>       mSpots                      = new ArrayList<Spot>();
    private SpotDuplicateListener listener;

    public AdapterDuplicateSpots(FragmentActivity fragmentActivity) {
        mContext = fragmentActivity;
        mImageLoader = new ImageLoader(fragmentActivity);
    }

    @Override
    public int getCount() {
        return mSpots.size();
    }

    @Override
    public Object getItem(int arg0) {
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    TextView     btnAddSpot;
    LinearLayout layoutBtnAddSpot;

    @Override
    public View getView(int position, View convertView, ViewGroup parentView) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.row_search_spot_result, null);
        ViewHolder holder = new ViewHolder();
        holder.spotAvatar = (ImageView) convertView.findViewById(R.id.img_avatar_spot);
        holder.spotName = (TextView) convertView.findViewById(R.id.txt_spot_name);
        holder.numberBravos = (TextView) convertView.findViewById(R.id.text_number_bravo);
        holder.spotName.setText(mSpots.get(position).Spot_Name);
        holder.numberBravos.setText(mSpots.get(position).Total_Bravos + " Bravos");
        if (StringUtility.isEmpty(mSpots.get(position).Spot_Icon)) {
            holder.spotAvatar.setImageResource(R.drawable.place_icon);
        } else {
            mImageLoader.DisplayImage(mSpots.get(position).Spot_Icon, R.drawable.place_icon, holder.spotAvatar);
        }
        holder.spotAvatar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });

        return convertView;
    }

    public SpotDuplicateListener getListener() {
        return listener;
    }

    public void updateData(ArrayList<Spot> mSpots) {
        this.mSpots = mSpots;
        notifyDataSetChanged();
    }

    public void setListener(SpotDuplicateListener listener) {
        this.listener = listener;
    }

    class ViewHolder {
        ImageView spotAvatar;
        TextView  spotName;
        TextView  numberBravos;
    }
}
