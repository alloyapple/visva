package com.visva.voicerecorder.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
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

/*
 * record in-coming call only
 */
public class InCommingCallReceiver extends BroadcastReceiver {
    public static ExtAudioRecorder recorder     = null;
    public Context                 rcontext;
    private TelephonyManager       tm;
    private static ProgramHelper   helper       = new ProgramHelper();
    private static String          phoneNo      = null;
    private static String          mCreatedDate = null;
    private static String          mFileName    = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.rcontext = context;
        Resources res = rcontext.getResources();
        String startRecording = res.getString(R.string.start_record);
        String endRecording = res.getString(R.string.end_record);
        Bundle bundle = intent.getExtras();
        tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm.getCallState() == TelephonyManager.CALL_STATE_IDLE) {
            if (recorder != null) {
                Toast.makeText(context, endRecording, Toast.LENGTH_LONG).show(); // show when call ended
                stopRecording();
                MyCallRecorderSharePrefs myCallRecorderSharePrefs = MyCallRecorderApplication.getInstance().getMyCallRecorderSharePref(context);
                boolean isAutoSavedRecordCall = myCallRecorderSharePrefs.getBooleanValue(MyCallRecorderConstant.KEY_AUTO_SAVED);
                boolean isAutoSavedIncomingCall = myCallRecorderSharePrefs.getBooleanValue(MyCallRecorderConstant.KEY_SAVED_INCOMING_CALL);
                boolean isValidDurationTime = Utils.isCheckValidDurationTime(mFileName);
                boolean isShowNotication = myCallRecorderSharePrefs.getBooleanValue(MyCallRecorderConstant.KEY_SHOW_NOTIFICATION);
                AIOLog.d(MyCallRecorderConstant.TAG, "isAutoSavedRecordCall:" + isAutoSavedRecordCall + ",isAutoSavedIncomingCall:"
                        + isAutoSavedIncomingCall + ",isValidDurationTime:" + isValidDurationTime);
                if (isAutoSavedIncomingCall && !isAutoSavedRecordCall && isValidDurationTime) {
                    Intent i = new Intent(context, NotificationActivity.class);
                    i.putExtra(MyCallRecorderConstant.EXTRA_PHONE_NO, phoneNo);
                    i.putExtra(MyCallRecorderConstant.EXTRA_CREATED_DATE, mCreatedDate);
                    i.putExtra(MyCallRecorderConstant.EXTRA_FILE_NAME, mFileName);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
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
            } else {
            }
            InCommingCallReceiver.phoneNo = null;
            InCommingCallReceiver.mCreatedDate = null;
            InCommingCallReceiver.mFileName = null;
        }

        if (tm.getCallState() == TelephonyManager.CALL_STATE_RINGING) {
            InCommingCallReceiver.phoneNo = bundle.getString("incoming_number");
            long currentTime = System.currentTimeMillis();
            InCommingCallReceiver.mCreatedDate = String.valueOf(currentTime);
        }
        if (tm.getCallState() == TelephonyManager.CALL_STATE_OFFHOOK) {
            if (recorder != null) {
                AIOLog.d(MyCallRecorderConstant.TAG, "try to stop, line 40");
                stopRecording();
            }
            try {
                AIOLog.d(MyCallRecorderConstant.TAG, "incoming offhook useThisApp");
                if (InCommingCallReceiver.phoneNo != null) {
                    startRecording(InCommingCallReceiver.phoneNo, 1);
                    Toast.makeText(context, startRecording + InCommingCallReceiver.phoneNo, Toast.LENGTH_SHORT).show();
                } else {
                }
            } catch (Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void startRecording(String phoneNo, int callState) {
        AIOLog.d(MyCallRecorderConstant.TAG, "in start recording");
        Resources res = rcontext.getResources();
        String cannotRecord = res.getString(R.string.cannot_record);
        recorder = ExtAudioRecorder.getInstanse(false);
        try {
            AIOLog.d(MyCallRecorderConstant.TAG, "in start recording - write to file, phoneNo: " + phoneNo);
            mFileName = helper.getFileNameAndWriteToList(rcontext, phoneNo, callState, mCreatedDate);
            recorder.setOutputFile(mFileName);
        } catch (Exception e) {
            Toast.makeText(this.rcontext, cannotRecord + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        try {
            recorder.prepare();
            recorder.start();
        } catch (IllegalStateException e) {
            Toast.makeText(this.rcontext, cannotRecord, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (Exception e) {
            Toast.makeText(this.rcontext, cannotRecord, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;
    }
}