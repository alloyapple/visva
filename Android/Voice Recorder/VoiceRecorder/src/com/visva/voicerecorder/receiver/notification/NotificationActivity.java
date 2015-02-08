package com.visva.voicerecorder.receiver.notification;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Window;

import com.visva.voicerecorder.ProgramHelper;

public class NotificationActivity extends Activity {
    public NotificationActivity() {
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Log.d("GHIAM","before onCreate");
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setTitle("Thông báo");
        dialog.setMessage("Bạn có muốn lưu cuộc gọi vừa rồi lại không ?");
        dialog.setCancelable(true);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Có", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int buttonId) {
                // remove the current newest call in the list
                ProgramHelper helper = new ProgramHelper();
                try {
                    helper.stopMainActivity();
                } catch (Exception e) {
                }
                dialog.cancel();
                finish();
            }
        });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Không", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int buttonId) {
                ProgramHelper helper = new ProgramHelper();
                helper.removeNewestSession();
                dialog.cancel();
                finish();
            }
        });
        dialog.show();
    }
}