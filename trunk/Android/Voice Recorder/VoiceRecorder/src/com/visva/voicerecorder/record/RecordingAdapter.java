package com.visva.voicerecorder.record;

import java.util.ArrayList;
import java.util.List;

import com.visva.voicerecorder.MainActivity;
import com.visva.voicerecorder.R;
import com.visva.voicerecorder.R.drawable;
import com.visva.voicerecorder.R.id;
import com.visva.voicerecorder.R.layout;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * An adapter used for the list view
 */
public class RecordingAdapter extends ArrayAdapter<RecordingSession>{
	LayoutInflater layoutInflater;
	
	public RecordingAdapter(Context context, int textViewResourceId, 
			List<RecordingSession> objects) {
		super(context, textViewResourceId, objects);
		layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public Uri getContactPhotoFromNumber(ContentResolver resolver,String phoneNo){
		Uri result = null;
		if(phoneNo == "" || phoneNo == "null"){
			phoneNo = "111111111";
		}
    	String[] projection = {ContactsContract.Contacts._ID, 
        		ContactsContract.PhoneLookup.DISPLAY_NAME, 
        		ContactsContract.PhoneLookup.NUMBER, 
        		ContactsContract.PhoneLookup.PHOTO_URI};
    	Uri lookupUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNo));
    	Cursor cursor = resolver.query(lookupUri, projection, null, null, null);
    	if(cursor.moveToFirst()){
    		if(cursor.getString(3) != null)
    			result = Uri.parse(cursor.getString(3));
    	}
    	cursor.close();
    	return result;
    }
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){	
		if(convertView == null){
			convertView = this.layoutInflater.inflate(R.layout.recorder_item, null);
		}
		TextView phoneTextView = (TextView) convertView.findViewById(R.id.phoneTextView);
		TextView dateTextView = (TextView) convertView.findViewById(R.id.dateTimeTextView);
		ImageView profileImage = (ImageView) convertView.findViewById(R.id.imageProfile);
		ImageView callStateImage = (ImageView) convertView.findViewById(R.id.callIndicator);
		phoneTextView.setText(this.getItem(position).phoneNo);
		String dateString = this.getItem(position).dateCreated.split("-")[0]+
				"/"+this.getItem(position).dateCreated.split("-")[1]+
				"/"+this.getItem(position).dateCreated.split("-")[2];
		String timeString = this.getItem(position).dateCreated.split("-")[3];
		String finalTimeString = timeString.split(":")[0]+":"+timeString.split(":")[1];
		dateTextView.setText(finalTimeString+" "+dateString);
		Uri photoUri = this.getContactPhotoFromNumber(getContext().getContentResolver(), 
				this.getItem(position).phoneNo);
		if(photoUri != null){
			profileImage.setImageURI(photoUri);
			profileImage.getLayoutParams().width = 68;
			profileImage.getLayoutParams().height = 68;
		}else{
			profileImage.setImageResource(R.drawable.default_contact_icon);
		}
		if(this.getItem(position).callState == 1){
			callStateImage.setImageResource(R.drawable.incoming);
		}else{
			callStateImage.setImageResource(R.drawable.outgoing);
		}
		return convertView;
	}
	
	public void removeAt(int position){
		// update the view
		String filePath = this.getItem(position).fileName;
		this.remove(this.getItem(position));
		this.notifyDataSetChanged();
		// update MainActivity.recordingManger
		ArrayList<RecordingSession> _sessions = MainActivity.recordingManager.getSessions();
		for(int i=0;i<_sessions.size();i++){
			if(_sessions.get(i).fileName.equals(filePath)){
				_sessions.remove(i);
				break;
			}
		}
		MainActivity.recordingManager.setSessions(_sessions);
		// update the view in AllRecordingFragment
		MainActivity.toDeleteFilePath = filePath;
		// update the physical file
		MainActivity.recordingManager.removeFile(filePath);
	}
}
