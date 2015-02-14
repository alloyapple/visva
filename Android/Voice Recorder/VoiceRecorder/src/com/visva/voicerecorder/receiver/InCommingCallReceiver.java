package com.visva.voicerecorder.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.ringdroid.soundfile.ExtAudioRecorder;
import com.visva.voicerecorder.receiver.notification.NotificationActivity;
import com.visva.voicerecorder.utils.ProgramHelper;
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
				Toast.makeText(context, "Káº¿t thÃºc ghi Ã¢m", Toast.LENGTH_LONG).show(); // show when call ended
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
					helper.useThisApp(context);
					startRecording(InCommingCallReceiver.phoneNo,1);
					Toast.makeText(context, "Báº¯t Ä‘áº§u ghi Ã¢m "+InCommingCallReceiver.phoneNo, Toast.LENGTH_SHORT).show();
				}else{
					//Toast.makeText(context, "ChÆ°a Ä‘Æ°Æ¡c active, khÃ´ng ghi Ã¢m", Toast.LENGTH_LONG).show();
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
			Toast.makeText(this.rcontext, "KhÃ´ng ghi Ã¢m Ä‘Æ°á»£c, do: "+e.getMessage(), Toast.LENGTH_LONG).show();
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
			Toast.makeText(this.rcontext, "KhÃ´ng ghi Ã¢m Ä‘Æ°á»£c", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
		/*
		catch (IOException e) {
			Toast.makeText(this.rcontext, "KhÃ´ng ghi Ã¢m Ä‘Æ°á»£c, lá»—i Ä‘á»�c/ghi file", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
		*/
		 catch (Exception e){
			Toast.makeText(this.rcontext, "KhÃ´ng ghi Ã¢m Ä‘Æ°á»£c", Toast.LENGTH_SHORT).show();
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