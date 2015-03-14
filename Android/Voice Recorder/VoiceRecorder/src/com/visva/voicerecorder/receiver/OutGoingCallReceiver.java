package com.visva.voicerecorder.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.ringdroid.soundfile.ExtAudioRecorder;
import com.visva.voicerecorder.MyCallRecorderApplication;
import com.visva.voicerecorder.R;
import com.visva.voicerecorder.constant.MyCallRecorderConstant;
import com.visva.voicerecorder.receiver.notification.NotificationActivity;
import com.visva.voicerecorder.utils.MyCallRecorderSharePrefs;
import com.visva.voicerecorder.utils.ProgramHelper;
import com.visva.voicerecorder.utils.Utils;

public class OutGoingCallReceiver extends BroadcastReceiver {
    public static ExtAudioRecorder recorder = null;
    public Context                 rcontext;
    TelephonyManager               tm;
    public static ProgramHelper    helper   = new ProgramHelper();
    public static String           phoneNo  = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (helper.isRecordingOutgoingCall() == 0) {
            return;
        }

        Bundle bundle = intent.getExtras();
        if (null == bundle) {
            return;
        }
        Resources res = context.getResources();
        String startRecording = res.getString(R.string.start_record);
        String endRecording = res.getString(R.string.end_record);
        String _tmp = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
        if (_tmp != null) {
            OutGoingCallReceiver.phoneNo = _tmp;
        }
        tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        if (tm.getCallState() == TelephonyManager.CALL_STATE_IDLE) {
            if (recorder != null) {
                Toast.makeText(context, endRecording, Toast.LENGTH_LONG).show(); // show when call ended
                stopRecording();
                OutGoingCallReceiver.phoneNo = null;
                MyCallRecorderSharePrefs myCallRecorderSharePrefs = MyCallRecorderApplication.getInstance().getMyCallRecorderSharePref(context);
                boolean isAutoSavedRecordCall = myCallRecorderSharePrefs.getBooleanValue(MyCallRecorderConstant.KEY_AUTO_SAVED);
                boolean isAutoSavedIncomingCall = myCallRecorderSharePrefs.getBooleanValue(MyCallRecorderConstant.KEY_SAVED_INCOMING_CALL);
                boolean isValidDurationTime = Utils.isCheckValidDurationTime();
                if (isAutoSavedIncomingCall && !isAutoSavedRecordCall && isValidDurationTime) {
                    Intent i = new Intent(context, NotificationActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                }
            } else {
            }
        }
        if (tm.getCallState() == TelephonyManager.CALL_STATE_OFFHOOK) {
            if (recorder != null) {
                stopRecording();
            }
            try {
                if (OutGoingCallReceiver.phoneNo != null) {
                    Log.d("GHIAM", "outgoing offhook useThisApp");
                    helper.useThisApp(context);
                    startRecording(OutGoingCallReceiver.phoneNo, 2);
                    Toast.makeText(context, startRecording + OutGoingCallReceiver.phoneNo, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    public void startRecording(String phoneNo, int callState) {
        Resources res = rcontext.getResources();
        String cannotRecording = res.getString(R.string.cannot_record);
        recorder = ExtAudioRecorder.getInstanse(false);
        try {
            Log.d("GHIAM", "in start recording - write to file, phoneNo: " + phoneNo);
            recorder.setOutputFile(helper.getFileNameAndWriteToList(phoneNo, callState));
        } catch (Exception e) {
            Toast.makeText(this.rcontext, cannotRecording + ": " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        try {
            recorder.prepare();
            recorder.start();
        } catch (IllegalStateException e) {
            Toast.makeText(this.rcontext, cannotRecording, Toast.LENGTH_SHORT).show();
            Log.e("GHIAM", "unable to record " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            Toast.makeText(this.rcontext, cannotRecording, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;
    }

}
