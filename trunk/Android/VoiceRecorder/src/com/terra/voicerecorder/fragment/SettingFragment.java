package com.terra.voicerecorder.fragment;

import com.terra.voicerecorder.ProgramHelper;
import com.terra.voicerecorder.R;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;

public class SettingFragment extends Fragment {
	ProgramHelper helper = new ProgramHelper();
	CheckBox chkbIncoming;
	CheckBox chkbOutgoing;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		return this._createDefaultView();
    }
	
	private View _createDefaultView(){
		View settingView = this.getActivity().getLayoutInflater().inflate(R.layout.setting_fragment, null);
		chkbIncoming = (CheckBox) settingView.findViewById(R.id.chkb_setting_incoming);
		chkbOutgoing = (CheckBox) settingView.findViewById(R.id.chkb_setting_outgoing);
		Button acceptBtn = (Button) settingView.findViewById(R.id.btn_OK);
		Button cancelBtn = (Button) settingView.findViewById(R.id.btn_Cancel);
	    if(helper.isRecordingIncomingCall() == 1){
	    	chkbIncoming.setChecked(true);
	    }
	    Log.d("GHIAM","check isRecordingIncomingCall done");
	    if(helper.isRecordingOutgoingCall() == 1){
	    	chkbOutgoing.setChecked(true);
	    }
	    Log.d("GHIAM","check isRecordingOutgoingCall done");
	    acceptBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int incoming = 0, outgoing = 0;
				if(chkbIncoming.isChecked() == true){
			    	incoming = 1;
			    }
			    if(chkbOutgoing.isChecked() == true){
			    	outgoing = 1;
			    }
				helper.writeSettingValue(incoming, outgoing);
				// show an alert
				AlertDialog.Builder alertBuilder = new Builder(getActivity());
				alertBuilder.setCancelable(true);
				alertBuilder.setTitle("Thông báo")
					.setMessage("Thông tin đã được lưu lại !")
					.show();
			}
		});
	    cancelBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// reset checkbox to its original value
				if(helper.isRecordingIncomingCall() == 1){
			    	chkbIncoming.setChecked(true);
			    }else{
			    	chkbIncoming.setChecked(false);
			    }
			    if(helper.isRecordingOutgoingCall() == 1){
			    	chkbOutgoing.setChecked(true);
			    }else{
			    	chkbOutgoing.setChecked(false);
			    }
			}
		});
	    
		return settingView;
	}
}
