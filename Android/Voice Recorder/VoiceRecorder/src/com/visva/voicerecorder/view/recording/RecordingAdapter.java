package com.visva.voicerecorder.view.recording;

import java.util.ArrayList;
import java.util.Locale;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filter.FilterListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.visva.voicerecorder.MyCallRecorderApplication;
import com.visva.voicerecorder.R;
import com.visva.voicerecorder.note.NoteItem;
import com.visva.voicerecorder.record.RecordingSession;
import com.visva.voicerecorder.utils.StringUtility;
import com.visva.voicerecorder.utils.Utils;
import com.visva.voicerecorder.view.widget.CircleImageView;

/**
 * An adapter used for the list view
 */
public class RecordingAdapter extends ArrayAdapter<RecordingSession> {
    // ======================Constant Define=====================
    // ======================Control Define =====================

    // =======================Class Define ======================
    private SearchFilter                filter;
    // ======================Variable Define=====================
    private LayoutInflater              layoutInflater;
    private ArrayList<RecordingSession> mRecordingSessions      = new ArrayList<RecordingSession>();
    private ArrayList<RecordingSession> filteredModelItemsArray = new ArrayList<RecordingSession>();
    private Context                     mContext;
    private SparseBooleanArray          mSelectedItemsIds;

    public RecordingAdapter(Context context, int textViewResourceId, ArrayList<RecordingSession> recordingSessions) {
        super(context, textViewResourceId, recordingSessions);
        mContext = context;
        mRecordingSessions = recordingSessions;
        mSelectedItemsIds = new SparseBooleanArray();
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = this.layoutInflater.inflate(R.layout.recorder_item, null);
            holder.callStateImage = (ImageView) convertView.findViewById(R.id.callIndicator);
            holder.avatar = (CircleImageView) convertView.findViewById(R.id.imageProfile);
            holder.textTime = (TextView) convertView.findViewById(R.id.dateTimeTextView);
            holder.textDuration = (TextView) convertView.findViewById(R.id.phoneTextView);
            holder.textPhoneName = (TextView) convertView.findViewById(R.id.txt_phone_name);
            holder.favorite = (ImageView) convertView.findViewById(R.id.favorite);
            holder.layoutNote = (RelativeLayout) convertView.findViewById(R.id.layout_note);
            holder.textNote = (TextView) convertView.findViewById(R.id.text_note);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        RecordingSession recordingSession = getItem(position);
        if (recordingSession == null) {
            return null;
        }
        String durationTime = Utils.getDurationTextTime(mContext, recordingSession.duration);
        holder.textDuration.setText(durationTime);
        String textDate = Utils.getTextDate(mContext, Long.valueOf(recordingSession.dateCreated));
        holder.textTime.setText(Utils.getTextTime(mContext, Long.valueOf(recordingSession.dateCreated)) + " " + textDate);

        Uri photoUri = Utils.getPhotoUriFromPhoneNumber(getContext().getContentResolver(), recordingSession.phoneNo);
        if (photoUri != null) {
            holder.avatar.setImageURI(photoUri);
        } else {
            holder.avatar.setImageResource(R.drawable.ic_contact_picture_holo_light);
        }

        int isFavorite = Utils.isCheckFavouriteContactByPhoneNo(mContext, recordingSession.phoneNo);
        if (isFavorite > 0) {
            holder.favorite.setVisibility(View.VISIBLE);
        } else {
            holder.favorite.setVisibility(View.GONE);
        }
        String contactName = getItem(position).phoneName;
        if (contactName == null || com.visva.voicerecorder.utils.StringUtility.isEmpty(contactName.toString())) {
            holder.textPhoneName.setText(recordingSession.phoneNo);
        } else {
            holder.textPhoneName.setText(contactName.toString());
        }
        if (this.getItem(position).callState == 1) {
            holder.callStateImage.setImageResource(R.drawable.ic_incoming);
        } else {
            holder.callStateImage.setImageResource(R.drawable.ic_outgoing);
        }

        holder.avatar.assignContactFromPhone(recordingSession.phoneNo, true);

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

    private class ViewHolder {
        TextView        textPhoneName;
        TextView        textDuration;
        TextView        textTime;
        ImageView       callStateImage;
        CircleImageView avatar;
        ImageView       favorite;
        RelativeLayout  layoutNote;
        TextView        textNote;
    }

    public void onTextSearchChanged(CharSequence s) {
        getFilter().filter(s, new FilterListener() {

            @Override
            public void onFilterComplete(int count) {

            }
        });
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new SearchFilter();
        }
        return filter;
    }

    private class SearchFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            constraint = constraint.toString().toLowerCase(Locale.ENGLISH);
            FilterResults result = new FilterResults();
            Log.d("KieuThang", "constraint:" + (constraint != null && constraint.toString().length() > 0));
            if (constraint != null && constraint.toString().length() > 0) {
                ArrayList<RecordingSession> filteredItems = new ArrayList<RecordingSession>();

                for (int i = 0, l = mRecordingSessions.size(); i < l; i++) {
                    RecordingSession recordingSession = mRecordingSessions.get(i);
                    if (recordingSession.phoneNo.toLowerCase(Locale.ENGLISH).contains(constraint)
                            || recordingSession.phoneName.toLowerCase(Locale.ENGLISH).contains(constraint))
                        filteredItems.add(recordingSession);
                }
                result.count = filteredItems.size();
                result.values = filteredItems;
            } else {
                Log.d("KieuThang", "mRecordingSessions:" + mRecordingSessions.size());
                mRecordingSessions = MyCallRecorderApplication.getInstance().getProgramHelper().getRecordingSessionsFromFile(mContext);
                synchronized (this)
                {
                    result.values = mRecordingSessions;
                    result.count = mRecordingSessions.size();
                }
            }
            return result;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredModelItemsArray = (ArrayList<RecordingSession>) results.values;
            notifyDataSetChanged();
            clear();
            for (int i = 0, l = filteredModelItemsArray.size(); i < l; i++)
                add(filteredModelItemsArray.get(i));
            notifyDataSetInvalidated();
        }
    }

    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }

    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    public void selectView(int position, boolean value) {
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

    public void updateRecordingSession(ArrayList<RecordingSession> sessions) {
        mRecordingSessions.clear();
        mRecordingSessions = sessions;
        notifyDataSetChanged();
    }

    public void removeRecord(int position) {
        if (position > mRecordingSessions.size() - 1)
            return;
        mRecordingSessions.remove(position);
        notifyDataSetChanged();
    }

    public void addNewRecord(RecordingSession recordingSession) {
        mRecordingSessions.add(0, recordingSession);
        notifyDataSetChanged();
    }
}
