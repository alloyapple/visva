package com.terra.voicerecorder.fragment;

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

import com.terra.voicerecorder.ProgramHelper;
import com.terra.voicerecorder.R;

public class HelpFragment extends Fragment {
	ProgramHelper helper = new ProgramHelper();
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		return this._createDefaultView();
    }
	
	private View _createDefaultView(){
		View helpView = this.getActivity().getLayoutInflater().inflate(R.layout.setting_fragment, null);
		
	    
		return helpView;
	}
}
