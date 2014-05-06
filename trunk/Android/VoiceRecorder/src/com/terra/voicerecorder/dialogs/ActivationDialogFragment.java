package com.terra.voicerecorder.dialogs;

import com.terra.voicerecorder.ProgramHelper;
import com.terra.voicerecorder.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ActivationDialogFragment extends DialogFragment {
	ProgramHelper helper;
	ActivationDialogListener listener;
	View dialogView;
	public interface ActivationDialogListener {
        public void onActivationSuccess(DialogFragment dialog);
    }
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (ActivationDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement ActivationDialogListener");
        }
    }
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    // Get the layout inflater
	    LayoutInflater inflater = getActivity().getLayoutInflater();
	    helper = new ProgramHelper();
	    // Inflate and set the layout for the dialog
	    // Pass null as the parent view because its going in the dialog layout
	    dialogView = inflater.inflate(R.layout.activation_dialog, null); 
	    builder.setTitle("Đã sử dụng "+helper.getNumOfUsed(getActivity())+"/"+ProgramHelper.NUM_OF_USE)
	    	.setView(dialogView)
	    // Add action buttons
	           .setPositiveButton("Kích hoạt", new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	            	   EditText editTextPart1 = (EditText) dialogView.findViewById(R.id.editTextActivationCodePart1);
	            	   EditText editTextPart2 = (EditText) dialogView.findViewById(R.id.editTextActivationCodePart2);
	            	   EditText editTextPart3 = (EditText) dialogView.findViewById(R.id.editTextActivationCodePart3);
	            	   EditText editTextPart4 = (EditText) dialogView.findViewById(R.id.editTextActivationCodePart4);
	            	   
	            	   EditText phoneText = (EditText) dialogView.findViewById(R.id.editTextPhone);
					   String activationCode = editTextPart1.getText().toString()+"-"
							   +editTextPart2.getText().toString()+"-"
							   +editTextPart3.getText().toString()+"-"
							   +editTextPart4.getText().toString();
					   String phoneNo = phoneText.getText().toString();
					   TelephonyManager tm = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
					   String uuid = tm.getDeviceId(); 
					   if(helper.processActivation(uuid, activationCode, phoneNo)){
							Toast.makeText(getActivity(), "Kích hoạt thành công!", Toast.LENGTH_LONG).show();
							listener.onActivationSuccess(ActivationDialogFragment.this);
						}else{
							Toast.makeText(getActivity(), "Kích hoạt không thành công!", Toast.LENGTH_LONG).show();
						}
	            	   ActivationDialogFragment.this.getDialog().cancel();
	               }
	           })
	           .setNegativeButton("Bỏ qua", new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	            	   ActivationDialogFragment.this.getDialog().cancel();
	               }
	           });      
	    return builder.create();
	}
}
