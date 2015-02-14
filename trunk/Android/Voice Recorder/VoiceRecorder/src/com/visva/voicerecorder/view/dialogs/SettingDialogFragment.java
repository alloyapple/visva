package com.visva.voicerecorder.view.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;

import com.visva.voicerecorder.R;
import com.visva.voicerecorder.utils.ProgramHelper;

public class SettingDialogFragment extends DialogFragment {
    ProgramHelper helper;
    View dialogView;
    CheckBox chkbIncoming;
    CheckBox chkbOutgoing;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        helper = new ProgramHelper();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        dialogView = inflater.inflate(R.layout.setting_dialog, null);
        builder.setTitle("Cấu hình")
                .setView(dialogView);
        chkbIncoming = (CheckBox) dialogView.findViewById(R.id.chkb_setting_incoming);
        chkbOutgoing = (CheckBox) dialogView.findViewById(R.id.chkb_setting_outgoing);
        if (helper.isRecordingIncomingCall() == 1) {
            chkbIncoming.setChecked(true);
        }
        Log.d("GHIAM", "check isRecordingIncomingCall done");
        if (helper.isRecordingOutgoingCall() == 1) {
            chkbOutgoing.setChecked(true);
        }
        Log.d("GHIAM", "check isRecordingOutgoingCall done");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                int incoming = 0, outgoing = 0;
                if (chkbIncoming.isChecked() == true) {
                    incoming = 1;
                }
                if (chkbOutgoing.isChecked() == true) {
                    outgoing = 1;
                }
                helper.writeSettingValue(incoming, outgoing);
                SettingDialogFragment.this.getDialog().cancel();
            }
        });
        builder.setNegativeButton("B�? qua", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                SettingDialogFragment.this.getDialog().cancel();
            }
        });
        return builder.create();
    }
}
