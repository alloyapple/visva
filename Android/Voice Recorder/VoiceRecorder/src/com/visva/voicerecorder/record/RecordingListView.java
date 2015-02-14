package com.visva.voicerecorder.record;

import java.io.IOException;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.visva.voicerecorder.MainActivity;
import com.visva.voicerecorder.view.dialogs.VoiceOptionDialogFragment;

public class RecordingListView extends ListView {
	public View lastClickedView;
	public int positionToDelete = -1;
	public MainActivity activity;
	public RecordingListView self;
	
	public RecordingListView(Context context) {
		super(context);
		activity = (MainActivity) context;
		lastClickedView = null;
		self = this;
		this.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {
				RecordingSession s = MainActivity.recordingManager.getSessions().get(position);
				if(lastClickedView == view){
					MainActivity.recordingManager.stopAudio();
					lastClickedView.setBackgroundColor(Color.WHITE);
					lastClickedView = null;
					return;
				}
				if(lastClickedView != null){
					lastClickedView.setBackgroundColor( Color.WHITE );
				}
				view.setBackgroundColor( Color.parseColor("#F4A148") );
				lastClickedView = view;
				Log.d("GHIAM", "path: "+s.fileName);
				try{
					MainActivity.recordingManager.playAudio(s,self);
				}catch(IOException ioe){
					Toast.makeText(getContext(), "Không mở được file âm thanh !", Toast.LENGTH_LONG).show();
				}
			}
		});
		
		this.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> adapterView, View view,
					int position, long id) {
				if(lastClickedView != null){
					lastClickedView.setBackgroundColor(Color.WHITE);
					lastClickedView = null;
				}
				MainActivity.recordingManager.stopAudio();
				RecordingSession s = MainActivity.recordingManager.getSessions().get(position);
				VoiceOptionDialogFragment voiceDialog = new VoiceOptionDialogFragment(RecordingListView.this,position,s.fileName);
				voiceDialog.show( activity.getSupportFragmentManager(), "VOICE_DIALOG");
				return false;
			}
		});
	}
	
	public void removeItemAt(int position){
		// remove the data in the view
		Log.d("GHIAM","at removeItemAt: start, remove: "+position);
		RecordingAdapter adapter = (RecordingAdapter) this.getAdapter();
		adapter.removeAt(position);
		Log.d("GHIAM","at removeItemAt: finish");
	}
}
