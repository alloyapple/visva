package com.visva.voicerecorder.view.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.visva.voicerecorder.R;
import com.visva.voicerecorder.record.RecordingSession;
import com.visva.voicerecorder.utils.StringUtility;
import com.visva.voicerecorder.utils.Utils;
import com.visva.voicerecorder.view.widget.CircleImageView;

public class DetailFavouriteAdapter extends BaseAdapter {
    // ======================Constant Define=====================
    private static final int            _ID                = 0;
    private static final int            DISPLAY_NAME       = _ID + 1;
    private static final int            NUMBER             = DISPLAY_NAME + 1;
    private static final int            PHOTO_URI          = NUMBER + 1;
    // ======================Variable Define=====================
    LayoutInflater                      layoutInflater;
    private Context                     mContext;
    private ArrayList<RecordingSession> mRecordingSessions = new ArrayList<RecordingSession>();

    public DetailFavouriteAdapter(Context context, ArrayList<RecordingSession> recordingSessions) {
        this.mContext = context;
        this.mRecordingSessions = recordingSessions;
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
        if (convertView == null) {
            convertView = this.layoutInflater.inflate(R.layout.layout_detail_favourite, null);
        }
        ViewHolder holder = new ViewHolder();
        RecordingSession recordingSession = mRecordingSessions.get(position);
        holder.textDateTime = (TextView) convertView.findViewById(R.id.text_date_time);
        holder.textPhoneName = (TextView) convertView.findViewById(R.id.text_name);
        holder.textDuration = (TextView) convertView.findViewById(R.id.text_duration);
        if (StringUtility.isEmpty(recordingSession.phoneName))
            holder.textPhoneName.setText(recordingSession.phoneNo);
        else
            holder.textPhoneName.setText(recordingSession.phoneName);
        holder.textDateTime.setText(recordingSession.dateCreated);
        return convertView;
    }

    static class ViewHolder {
        TextView textPhoneName;
        TextView textDateTime;
        TextView textDuration;
    }

    public class MyComparator implements Comparator<RecordingSession> {
        @Override
        public int compare(RecordingSession p1, RecordingSession p2) {
            return p1.phoneName.compareTo(p2.phoneName);
        }
    }
}
