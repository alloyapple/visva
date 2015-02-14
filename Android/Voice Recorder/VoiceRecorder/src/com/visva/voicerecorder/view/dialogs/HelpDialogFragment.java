package com.visva.voicerecorder.view.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class HelpDialogFragment extends DialogFragment {
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	    // Use the Builder class for convenient dialog construction
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    builder.setTitle("Hướng dẫn")
	    	.setMessage("Chương trình tự động ghi âm khi có cuộc g�?i.\nM�?i thắc mắc xin liên hệ contact@joolist.com.")
	        .setNegativeButton("Tắt", new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	            	   HelpDialogFragment.this.getDialog().cancel();
	               }
	           });
	    // Create the AlertDialog object and return it
	    return builder.create();
	}
}
