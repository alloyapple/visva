package com.visva.voicerecorder.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.ringdroid.soundfile.ExtAudioRecorder;
import com.visva.voicerecorder.MyCallRecorderApplication;
import com.visva.voicerecorder.R;
import com.visva.voicerecorder.constant.MyCallRecorderConstant;
import com.visva.voicerecorder.log.AIOLog;
import com.visva.voicerecorder.receiver.notification.NotificationActivity;
import com.visva.voicerecorder.utils.MyCallRecorderSharePrefs;
import com.visva.voicerecorder.utils.ProgramHelper;
import com.visva.voicerecorder.utils.StringUtility;
import com.visva.voicerecorder.utils.Utils;

public class OutGoingCallReceiver extends BroadcastReceiver {
    public static ExtAudioRecorder recorder     = null;
    public Context                 rcontext;
    TelephonyManager               tm;
    public static ProgramHelper    helper       = new ProgramHelper();
    public static String           phoneNo      = null;
    public static String           mCreatedDate = null;
    public static String           mFileName    = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        rcontext = context;
        Resources res = context.getResources();
        String startRecording = res.getString(R.string.start_record);
        String endRecording = res.getString(R.string.end_record);
        String _tmp = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
        AIOLog.d(MyCallRecorderConstant.TAG, "OutGoingCallReceiver: " + _tmp);
        if (_tmp != null) {
            OutGoingCallReceiver.phoneNo = _tmp;
            long currentTime = System.currentTimeMillis();
            OutGoingCallReceiver.mCreatedDate = String.valueOf(currentTime);
        }
        tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        AIOLog.d(MyCallRecorderConstant.TAG, "TelephonyManager.getCallState: " + tm.getCallState());
        if (tm.getCallState() == TelephonyManager.CALL_STATE_IDLE) {
            AIOLog.d(MyCallRecorderConstant.TAG, "recorder:" + recorder);
            if (recorder != null) {
                Toast.makeText(context, endRecording, Toast.LENGTH_LONG).show(); // show when call ended
                stopRecording();
                MyCallRecorderSharePrefs myCallRecorderSharePrefs = MyCallRecorderApplication.getInstance().getMyCallRecorderSharePref(context);
                boolean isAutoSavedRecordCall = myCallRecorderSharePrefs.getBooleanValue(MyCallRecorderConstant.KEY_AUTO_SAVED);
                boolean isAutoSavedOutGoingCall = myCallRecorderSharePrefs.getBooleanValue(MyCallRecorderConstant.KEY_SAVED_OUTGOING_CALL);
                boolean isValidDurationTime = Utils.isCheckValidDurationTime(mFileName);
                boolean isShowNotication = myCallRecorderSharePrefs.getBooleanValue(MyCallRecorderConstant.KEY_SHOW_NOTIFICATION);
                AIOLog.d(MyCallRecorderConstant.TAG, "isAutoSavedRecordCall:" + isAutoSavedRecordCall + ",isAutoSavedOutGoingCall:"
                        + isAutoSavedOutGoingCall + ",isValidDurationTime:" + isValidDurationTime);
                if (isAutoSavedOutGoingCall && !isAutoSavedRecordCall && isValidDurationTime) {
                    Intent i = new Intent(context, NotificationActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.putExtra(MyCallRecorderConstant.EXTRA_PHONE_NO, phoneNo);
                    i.putExtra(MyCallRecorderConstant.EXTRA_CREATED_DATE, mCreatedDate);
                    i.putExtra(MyCallRecorderConstant.EXTRA_FILE_NAME, mFileName);
                    context.startActivity(i);
                } else {
                    Uri phoneUri = Utils.getContactUriTypeFromPhoneNumber(context.getContentResolver(), phoneNo, 1);
                    String phoneName = "";
                    if (phoneUri == null || StringUtility.isEmpty(phoneUri.toString()))
                        phoneName = phoneNo;
                    else
                        phoneName = phoneUri.toString();
                    if (isShowNotication)
                        Utils.showNotificationAfterCalling(context, phoneName, phoneNo, mCreatedDate);
                }
                OutGoingCallReceiver.phoneNo = null;
                OutGoingCallReceiver.mCreatedDate = null;
                OutGoingCallReceiver.mFileName = null;
            } else {

            }
        }
        if (tm.getCallState() == TelephonyManager.CALL_STATE_OFFHOOK) {
            AIOLog.d(MyCallRecorderConstant.TAG, "CALL_STATE_OFFHOOK" + phoneNo);
            if (recorder != null) {
                stopRecording();
            }
            try {
                AIOLog.d(MyCallRecorderConstant.TAG, "CALL_STATE_OFFHOOK" + phoneNo);
                if (OutGoingCallReceiver.phoneNo != null) {
                    AIOLog.d(MyCallRecorderConstant.TAG, "outgoing offhook useThisApp");
                    startRecording(OutGoingCallReceiver.phoneNo, TelephonyManager.CALL_STATE_OFFHOOK);
                    Toast.makeText(context, startRecording + OutGoingCallReceiver.phoneNo, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void startRecording(String phoneNo, int callState) {
        Resources res = rcontext.getResources();
        String cannotRecording = res.getString(R.string.cannot_record);
        recorder = ExtAudioRecorder.getInstanse(false);
        try {
            AIOLog.d(MyCallRecorderConstant.TAG, "in start recording - write to file, phoneNo: " + phoneNo);
            mFileName = helper.getFileNameAndWriteToList(rcontext, phoneNo, callState, mCreatedDate);
            recorder.setOutputFile(mFileName);
        } catch (Exception e) {
            Toast.makeText(this.rcontext, cannotRecording + ": " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        try {
            recorder.prepare();
            recorder.start();
        } catch (IllegalStateException e) {
            Toast.makeText(this.rcontext, cannotRecording, Toast.LENGTH_SHORT).show();
            AIOLog.e(MyCallRecorderConstant.TAG, "unable to record " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            AIOLog.e(MyCallRecorderConstant.TAG, "unable to record " + e.getMessage());
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
