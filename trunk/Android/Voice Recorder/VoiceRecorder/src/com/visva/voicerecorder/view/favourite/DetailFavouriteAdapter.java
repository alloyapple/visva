package com.visva.voicerecorder.view.favourite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.content.Context;
import android.graphics.Typeface;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.visva.voicerecorder.R;
import com.visva.voicerecorder.constant.MyCallRecorderConstant;
import com.visva.voicerecorder.note.NoteItem;
import com.visva.voicerecorder.record.RecordingSession;
import com.visva.voicerecorder.utils.StringUtility;
import com.visva.voicerecorder.utils.Utils;

public class DetailFavouriteAdapter extends BaseAdapter {
    // ======================Variable Define=====================
    LayoutInflater                      layoutInflater;
    private Context                     mContext;
    private ArrayList<RecordingSession> mRecordingSessions = new ArrayList<RecordingSession>();
    private SparseBooleanArray          mSelectedItemsIds;

    public DetailFavouriteAdapter(Context context, ArrayList<RecordingSession> recordingSessions) {
        this.mContext = context;
        this.mRecordingSessions = recordingSessions;
        mSelectedItemsIds = new SparseBooleanArray();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = this.layoutInflater.inflate(R.layout.layout_detail_favourite, null);
            holder = new ViewHolder();
            holder.textDate = (TextView) convertView.findViewById(R.id.text_date);
            holder.textTime = (TextView) convertView.findViewById(R.id.text_time);
            holder.textDuration = (TextView) convertView.findViewById(R.id.text_duration);
            holder.callIndicator = (ImageView) convertView.findViewById(R.id.callIndicator);
            holder.layoutNote = (RelativeLayout) convertView.findViewById(R.id.layout_note);
            holder.textNote = (TextView) convertView.findViewById(R.id.text_note);
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
        holder.textDuration.setText(Utils.getDurationTextTime(mContext, recordingSession.duration));
        if (this.getItem(position).callState == MyCallRecorderConstant.STATE_INCOMING) {
            holder.callIndicator.setImageResource(R.drawable.ic_incoming);
        } else {
            holder.callIndicator.setImageResource(R.drawable.ic_outgoing);
        }
        // note information
        NoteItem noteItem = Utils.getNoteItemFromRecordSession(mContext, recordingSession.dateCreated);
        if (noteItem == null || (StringUtility.isEmpty(noteItem.note) && StringUtility.isEmpty(noteItem.title))) {
            holder.layoutNote.setVisibility(View.GONE);
        } else {
            holder.layoutNote.setVisibility(View.VISIBLE);
            String note = noteItem.title;
            if (StringUtility.isEmpty(note))
                note = noteItem.note;
            holder.textNote.setText(note);
        }
        return convertView;
    }

    static class ViewHolder {
        TextView       textDate;
        TextView       textTime;
        TextView       textDuration;
        ImageView      callIndicator;
        RelativeLayout layoutNote;
        TextView       textNote;
    }

    public class MyComparator implements Comparator<RecordingSession> {
        @Override
        public int compare(RecordingSession p1, RecordingSession p2) {
            return p1.phoneName.compareTo(p2.phoneName);
        }
    }

    public void updateDetailRecordingSession(ArrayList<RecordingSession> favouriteRecordingSessions) {
        mRecordingSessions.clear();
        mRecordingSessions = favouriteRecordingSessions;
        notifyDataSetChanged();
    }

    public void setLongClickStateView(boolean isLongClick) {
        notifyDataSetChanged();
    }

    public void setSelectedPosition(int position) {
        notifyDataSetChanged();
    }

    public void removeRecord(int position) {
        if (position > mRecordingSessions.size() - 1)
            return;
        mRecordingSessions.remove(position);
        notifyDataSetChanged();
    }

    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }

    private void selectView(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, value);
        else
            mSelectedItemsIds.delete(position);
        notifyDataSetChanged();
    }

    public int getSelectedCount() {
        return mSelectedItemsIds.size();
    }

    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }

    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    public void removeRemovedList(ArrayList<RecordingSession> removedList) {
        mRecordingSessions.removeAll(removedList);
        notifyDataSetChanged();
    }
}
