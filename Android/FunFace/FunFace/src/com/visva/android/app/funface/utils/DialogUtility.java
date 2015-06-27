package com.visva.android.app.funface.utils;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.visva.android.app.funface.R;
import com.visva.android.app.funface.view.activity.ActivityFaceLoader;

public class DialogUtility {

    public static void showDialogAddText(ActivityFaceLoader activity, final IDialogListener iDialogListener) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        alertDialog.setTitle("PASSWORD");
        alertDialog.setMessage("Enter Password");

        final EditText input = new EditText(activity);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);
        String ok = activity.getString(R.string.ok);
        String cancel = activity.getString(R.string.cancel);
        alertDialog.setPositiveButton(ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                iDialogListener.onClickPositve(input.getText().toString());
            }
        });

        alertDialog.setNegativeButton(cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                iDialogListener.onClickCancel();
            }
        });

        alertDialog.show();
    }

}
