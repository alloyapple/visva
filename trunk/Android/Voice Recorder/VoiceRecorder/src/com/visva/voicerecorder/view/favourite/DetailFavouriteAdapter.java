package com.visva.voicerecorder.view.favourite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gc.materialdesign.views.CheckBox;
import com.visva.voicerecorder.R;
import com.visva.voicerecorder.record.RecordingSession;
import com.visva.voicerecorder.utils.Utils;

public class DetailFavouriteAdapter extends BaseAdapter {
    // ======================Variable Define=====================
    LayoutInflater                      layoutInflater;
    private Context                     mContext;
    private boolean                     mIsLongClick       = false;
    private ArrayList<Boolean>          mSeletedList       = new ArrayList<Boolean>();
    private ArrayList<RecordingSession> mRecordingSessions = new ArrayList<RecordingSession>();

    public DetailFavouriteAdapter(Context context, ArrayList<RecordingSession> recordingSessions) {
        this.mContext = context;
        this.mRecordingSessions = recordingSessions;
        for (int i = 0; i < mRecordingSessions.size(); i++) {
            mSeletedList.add(false);
        }
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Collections.sort(mRecordingSessions, new MyComparator());
    }

    @Override
    public int getCount() {
        return mRecordingSessions.size();
    }

    @Override
    public RecordingSession getItem(int arg0) {
        return this.mRecordingSessions.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup arg2) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = this.layoutInflater.inflate(R.layout.layout_detail_favourite, null);
            holder = new ViewHolder();
            holder.textDate = (TextView) convertView.findViewById(R.id.text_date);
            holder.textTime = (TextView) convertView.findViewById(R.id.text_time);
            holder.textDuration = (TextView) convertView.findViewById(R.id.text_duration);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox_select_favourite_detail_item);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) (convertView).getTag();
        RecordingSession recordingSession = mRecordingSessions.get(position);

        boolean isShowTextDate = Utils.isShowTextDate(position, mRecordingSessions);
        if (isShowTextDate) {
            holder.textDate.setTypeface(null, Typeface.BOLD);
        } else
            holder.textDate.setTypeface(null, Typeface.NORMAL);
        holder.textDate.setText(Utils.getTextDate(mContext, Long.valueOf(recordingSession.dateCreated)));
        holder.textTime.setText(Utils.getTextTime(mContext, Long.valueOf(recordingSession.dateCreated)));
        holder.textDuration.setText(Utils.getDurationTime(mContext, recordingSession));
        if (mIsLongClick) {
            holder.checkBox.setVisibility(View.VISIBLE);
        } else
            holder.checkBox.setVisibility(View.GONE);

        return convertView;
    }

    static class ViewHolder {
        TextView textDate;
        TextView textTime;
        TextView textDuration;
        CheckBox checkBox;
    }

    public class MyComparator implements Comparator<RecordingSession> {
        @Override
        public int compare(RecordingSession p1, RecordingSession p2) {
            return p1.phoneName.compareTo(p2.phoneName);
        }
    }

    public void updateDetailRecordingSession(ArrayList<RecordingSession> favouriteRecordingSessions) {
        mRecordingSessions.clear();
        mSeletedList.clear();
        mRecordingSessions = favouriteRecordingSessions;
        for (int i = 0; i < mRecordingSessions.size(); i++) {
            mSeletedList.add(false);
        }
        notifyDataSetChanged();
    }

    public void setLongClickStateView(boolean isLongClick) {
        mIsLongClick = isLongClick;
        notifyDataSetChanged();
    }

    public void setSelectedPosition(int position) {
        mSeletedList.set(position, true);
        notifyDataSetChanged();
    }
}
