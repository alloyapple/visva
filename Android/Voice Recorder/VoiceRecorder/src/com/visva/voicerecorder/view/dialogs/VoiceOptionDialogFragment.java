package com.visva.voicerecorder.view.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.visva.soundanalyzer.SoundAnalyzer;
import com.visva.voicerecorder.R;
import com.visva.voicerecorder.record.RecordingListView;
import com.visva.voicerecorder.utils.ProgramHelper;

public class VoiceOptionDialogFragment extends DialogFragment {
	View dialogView;
	ProgramHelper helper;
	RecordingListView rlv;
	int position;
	String fullPath;
	double voiceLvl;
	public VoiceOptionDialogFragment(RecordingListView rlv, int position, String fullpath){
		super();
		this.rlv = rlv;
		this.position = position;
		this.fullPath = fullpath;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	    // Use the Builder class for convenient dialog construction
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	 // Get the layout inflater
	    LayoutInflater inflater = getActivity().getLayoutInflater();
	    helper = new ProgramHelper();
	    // Inflate and set the layout for the dialog
	    // Pass null as the parent view because its going in the dialog layout
	    dialogView = inflater.inflate(R.layout.voiceoption_dialog, null); 
	    builder.setTitle("Lựa ch�?n")
	    	.setView(dialogView);
	    Button btn = (Button) dialogView.findViewById(R.id.btn_voiceoption_del);
	    EditText editVoiceLevel = (EditText) dialogView.findViewById(R.id.editVoiceLevel_voiceoption);
	    editVoiceLevel.setText("2");
	    Button confirmBtn = (Button) dialogView.findViewById(R.id.btn_OK_voiceoption);
	    // set editVoiceLevel to the current value
	    try{
	    	this.voiceLvl = Double.parseDouble(editVoiceLevel.getText().toString());
	    }catch(Exception e){
	    	this.voiceLvl = 2;
	    }
	    confirmBtn.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// process wav file
				SoundAnalyzer sa = new SoundAnalyzer(fullPath,voiceLvl);
				// write checked value to file
				sa.optimize();
				VoiceOptionDialogFragment.this.getDialog().cancel();
			}
		});
	    
	    btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// delete this item
				AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
				dialog.setTitle("Thông báo");
				dialog.setMessage("Bạn có muốn xóa bản ghi âm này?");
				dialog.setCancelable(true);
				rlv.positionToDelete = position;
				dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Có", new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialog, int buttonId) {
				    	if(rlv != null && rlv.lastClickedView != null){
				    		rlv.lastClickedView.setBackgroundColor(Color.WHITE);
				    		rlv.lastClickedView = null;
				    	}
				    	rlv.removeItemAt(rlv.positionToDelete);
				    }
				});
				dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Không", new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialog, int buttonId) {
				    	if(rlv != null && rlv.lastClickedView != null){
				    		rlv.lastClickedView.setBackgroundColor(Color.WHITE);
				    		rlv.lastClickedView = null;
				    	}
				        dialog.cancel();
				    }
				});
				dialog.show();
				VoiceOptionDialogFragment.this.getDialog().cancel();
			}
		});
	    return builder.create();
	}
}