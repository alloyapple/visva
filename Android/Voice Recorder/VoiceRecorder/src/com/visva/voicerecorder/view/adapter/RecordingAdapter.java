package com.visva.voicerecorder.view.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.visva.MyCallRecorderApplication;
import com.visva.voicerecorder.MainActivity;
import com.visva.voicerecorder.R;
import com.visva.voicerecorder.record.RecordingManager;
import com.visva.voicerecorder.record.RecordingSession;
import com.visva.voicerecorder.utils.Utils;
import com.visva.voicerecorder.view.widget.CircleImageView;

/**
 * An adapter used for the list view
 */
public class RecordingAdapter extends ArrayAdapter<RecordingSession> {
    // ======================Constant Define=====================
    private static final int            _ID                = 0;
    private static final int            DISPLAY_NAME       = _ID + 1;
    private static final int            NUMBER             = DISPLAY_NAME + 1;
    private static final int            PHOTO_URI          = NUMBER + 1;
    // ======================Control Define =====================

    // =======================Class Define ======================
    // ======================Variable Define=====================
    LayoutInflater                      layoutInflater;
    private RecordingManager            mRecordingManager;
    private ArrayList<RecordingSession> mRecordingSessions = new ArrayList<RecordingSession>();
    private Context                     mContext;

    public RecordingAdapter(Context context, int textViewResourceId, ArrayList<RecordingSession> recordingSessions) {
        super(context, textViewResourceId, recordingSessions);
        mContext = context;
        mRecordingSessions = recordingSessions;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mRecordingManager = MyCallRecorderApplication.getInstance().getRecordManager(context, recordingSessions);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = this.layoutInflater.inflate(R.layout.recorder_item, null);
        }
        final ViewHolder holder = new ViewHolder();
        holder.call = (ImageView) convertView.findViewById(R.id.img_call);
        holder.favourite = (ImageView) convertView.findViewById(R.id.img_favourite);
        holder.callStateImage = (ImageView) convertView.findViewById(R.id.callIndicator);
        holder.avatar = (CircleImageView) convertView.findViewById(R.id.imageProfile);
        holder.textTime = (TextView) convertView.findViewById(R.id.dateTimeTextView);
        holder.textPhoneNo = (TextView) convertView.findViewById(R.id.phoneTextView);
        holder.textPhoneName = (TextView) convertView.findViewById(R.id.txt_phone_name);
        holder.textDate = (TextView) convertView.findViewById(R.id.text_date);
        holder.textPhoneNo.setText(this.getItem(position).phoneNo);
        String timeString = this.getItem(position).dateCreated.split("-")[3];
        String finalTimeString = timeString.split(":")[0] + ":" + timeString.split(":")[1];
        holder.textTime.setText(finalTimeString);

        boolean isShowTextDate = Utils.isShowTextDate(position, mRecordingSessions);
        if (isShowTextDate) {
            holder.textDate.setVisibility(View.VISIBLE);
            String textDate = Utils.getTextDate(mContext,getItem(position));
            holder.textDate.setText(textDate);
        } else {
            holder.textDate.setVisibility(View.GONE);
        }
        Uri photoUri = Utils.getContactUriTypeFromPhoneNumber(getContext().getContentResolver(), this.getItem(position).phoneNo, PHOTO_URI);
        if (photoUri != null) {
            holder.avatar.setImageURI(photoUri);
        } else {
            holder.avatar.setImageResource(R.drawable.ic_contact_picture_holo_light);
        }

        Uri contactName = Utils.getContactUriTypeFromPhoneNumber(getContext().getContentResolver(), this.getItem(position).phoneNo, DISPLAY_NAME);
        if (contactName == null || com.visva.voicerecorder.utils.StringUtility.isEmpty(contactName.toString())) {
            holder.textPhoneName.setText("Unknown");
        } else {
            holder.textPhoneName.setText(contactName.toString());
        }
        if (this.getItem(position).callState == 1) {
            holder.callStateImage.setImageResource(R.drawable.incoming);
        } else {
            holder.callStateImage.setImageResource(R.drawable.outgoing);
        }

        holder.avatar.assignContactFromPhone(this.getItem(position).phoneNo, true);
        holder.call.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

            }
        });
        return convertView;
    }

    public void removeAt(int position) {
        // update the view
        String filePath = this.getItem(position).fileName;
        this.remove(this.getItem(position));
        this.notifyDataSetChanged();
        // update MainActivity.recordingManger
        ArrayList<RecordingSession> _sessions = mRecordingManager.getSessions();
        for (int i = 0; i < _sessions.size(); i++) {
            if (_sessions.get(i).fileName.equals(filePath)) {
                _sessions.remove(i);
                break;
            }
        }
        mRecordingManager.setSessions(_sessions);
        // update the view in AllRecordingFragment
        MainActivity.toDeleteFilePath = filePath;
        // update the physical file
        mRecordingManager.removeFile(filePath);
    }

    private class ViewHolder {
        TextView        textPhoneName;
        TextView        textPhoneNo;
        TextView        textTime;
        TextView        textDate;
        ImageView       favourite;
        ImageView       callStateImage;
        ImageView       call;
        CircleImageView avatar;

    }
}
