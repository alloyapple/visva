package com.visva.android.socialstackwidget.fragment;

import kankan.widget.WheelView;
import kankan.widget.adapters.ArrayWheelAdapter;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.visva.android.socialstackwidget.R;
import com.visva.android.socialstackwidget.constant.GlobalContstant;
import com.visva.android.socialstackwidget.util.SharePrefUtils;

public class RefreshItemsSettingFragment extends Fragment implements OnClickListener {
    private String choice[] = { "1 minute", "2 minute(Default)", "3 minutes", "5 minutes", "10minutes" };
    private WheelView mWheelViewRemoveData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = (ViewGroup) inflater.inflate(R.layout.fragment_refresh_time, null);
        mWheelViewRemoveData = (WheelView) root.findViewById(R.id.id_wheelview_remove_data);
        mWheelViewRemoveData.setVisibility(View.VISIBLE);
        mWheelViewRemoveData.setViewAdapter(new RemoveDataAdapter(getActivity(), choice, SharePrefUtils.getIntValue(getActivity(), GlobalContstant.SHARE_PRE_REFRESH_TIME_KEY)));
        mWheelViewRemoveData.setCurrentItem(SharePrefUtils.getIntValue(getActivity(), GlobalContstant.SHARE_PRE_REFRESH_TIME_KEY));
        return root;
    }

    @Override
    public void onClick(View v) {

    }

    private class RemoveDataAdapter extends ArrayWheelAdapter<String> {
        // Index of current item
        int currentItem;
        // Index of item to be highlighted
        int currentValue;

        /**
         * Constructor
         */
        public RemoveDataAdapter(Context context, String[] items, int current) {
            super(context, items);
            this.currentValue = current;
            setTextSize(24);
        }

        @Override
        protected void configureTextView(TextView view) {
            super.configureTextView(view);
            if (currentItem == currentValue) {
                view.setTextColor(0xFF0000F0);
            }
            view.setTypeface(Typeface.SANS_SERIF);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            currentItem = index;
            return super.getItem(index, cachedView, parent);
        }
    }
    
    @Override
    public void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        SharePrefUtils.putIntValue(getActivity(), GlobalContstant.SHARE_PRE_REFRESH_TIME_KEY, mWheelViewRemoveData.getCurrentItem());
    }
}
