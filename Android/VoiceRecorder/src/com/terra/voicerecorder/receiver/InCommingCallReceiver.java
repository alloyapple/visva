package com.terra.voicerecorder.receiver;

import java.io.IOException;

import com.ringdroid.soundfile.ExtAudioRecorder;
import com.terra.voicerecorder.ProgramHelper;
import com.terra.voicerecorder.receiver.notification.NotificationActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;
/*
 * record in-coming call only
 */
public class InCommingCallReceiver extends BroadcastReceiver {
	//public static MediaRecorder mRecorder = null;
	public static ExtAudioRecorder recorder = null;
	public Context rcontext;
	TelephonyManager tm;
	public static ProgramHelper helper = new ProgramHelper();
	public static String phoneNo = null;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		if(helper.isRecordingIncomingCall() == 0){
			return;
		}
		this.rcontext = context;
		Bundle bundle = intent.getExtras();
		tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		if (tm.getCallState() == TelephonyManager.CALL_STATE_IDLE) {
			//if (mRecorder != null){
			if (recorder != null){
				Toast.makeText(context, "Kết thúc ghi âm", Toast.LENGTH_LONG).show(); // show when call ended
				stopRecording();
				InCommingCallReceiver.phoneNo = null;
				Intent i = new Intent(context, NotificationActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(i);
			}else{}
		}
		
		if (tm.getCallState() == TelephonyManager.CALL_STATE_RINGING) {
			InCommingCallReceiver.phoneNo = bundle.getString("incoming_number");
		}
		if (tm.getCallState() == TelephonyManager.CALL_STATE_OFFHOOK) {
			//if (mRecorder != null){
			if (recorder != null){
				Log.d("GHIAM","try to stop, line 40");
				stopRecording();
			}
			try{
				Log.d("GHIAM","incoming offhook useThisApp");
				if( InCommingCallReceiver.phoneNo != null){
					if(!( helper.checkActivated(tm.getDeviceId()) || (helper.getNumOfUsed(context) < ProgramHelper.NUM_OF_USE)  )){
						Toast.makeText(context, "Xin vui lòng kích hoạt phần mềm 'Thu âm cuộc gọi' để thu âm !", Toast.LENGTH_LONG).show();
						return;
					}
					helper.useThisApp(context);
					startRecording(InCommingCallReceiver.phoneNo,1);
					Toast.makeText(context, "Bắt đầu ghi âm "+InCommingCallReceiver.phoneNo, Toast.LENGTH_SHORT).show();
				}else{
					//Toast.makeText(context, "Chưa đươc active, không ghi âm", Toast.LENGTH_LONG).show();
					//Log.d("GHIAM", "Activated fail, no record conversation with "+phoneNo);
				}	
			}catch(Exception e){
				Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
			}
		}
	}

	private void startRecording(String phoneNo,int callState) {
		Log.d("GHIAM", "in start recording");
		recorder = ExtAudioRecorder.getInstanse(false);
		/*
		mRecorder = new MediaRecorder();
		mRecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION);
		mRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
		*/
		try{
			Log.d("GHIAM", "in start recording - write to file, phoneNo: "+phoneNo);
			recorder.setOutputFile(helper.getFileNameAndWriteToList(phoneNo,callState));
			//mRecorder.setOutputFile(helper.getFileNameAndWriteToList(phoneNo,callState));
		}catch(Exception e){
			Toast.makeText(this.rcontext, "Không ghi âm được, do: "+e.getMessage(), Toast.LENGTH_LONG).show();
		}
		//mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_WB);
		
		try {
			recorder.prepare();
			recorder.start();
			/*
			mRecorder.prepare();
			mRecorder.start();
			*/
		} catch (IllegalStateException e) {
			Toast.makeText(this.rcontext, "Không ghi âm được", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
		/*
		catch (IOException e) {
			Toast.makeText(this.rcontext, "Không ghi âm được, lỗi đọc/ghi file", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
		*/
		 catch (Exception e){
			Toast.makeText(this.rcontext, "Không ghi âm được", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
		Log.d("GHIAM"," Bat dau ghi");
	}

	private void stopRecording() {
		/*
		mRecorder.stop();
		mRecorder.release();
		mRecorder = null;
		*/
		recorder.stop();
		recorder.release();
		recorder = null;
	}
}