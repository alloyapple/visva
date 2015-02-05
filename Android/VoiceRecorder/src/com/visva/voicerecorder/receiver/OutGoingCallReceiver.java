package com.visva.voicerecorder.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.ringdroid.soundfile.ExtAudioRecorder;
import com.visva.voicerecorder.ProgramHelper;
import com.visva.voicerecorder.receiver.notification.NotificationActivity;

public class OutGoingCallReceiver extends BroadcastReceiver {
	//public static MediaRecorder mRecorder = null;
	public static ExtAudioRecorder recorder = null;
	public Context rcontext;
	TelephonyManager tm;
	public static ProgramHelper helper = new ProgramHelper();
	public static String phoneNo = null;
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if(helper.isRecordingOutgoingCall() == 0){
			return;
		}
		Bundle bundle = intent.getExtras();
        if(null == bundle){
        	return;
        }
        String _tmp = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
        if(_tmp != null){
        	OutGoingCallReceiver.phoneNo = _tmp;
        }
        tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        
        if (tm.getCallState() == TelephonyManager.CALL_STATE_IDLE) {
			//if (mRecorder != null){
        	if (recorder != null){
				Toast.makeText(context, "Káº¿t thÃºc ghi Ã¢m", Toast.LENGTH_LONG).show(); // show when call ended
				stopRecording();
				OutGoingCallReceiver.phoneNo = null;
				Intent i = new Intent(context, NotificationActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(i);
			}else{}
		}
		if (tm.getCallState() == TelephonyManager.CALL_STATE_OFFHOOK) {
			//if (mRecorder != null){
			if (recorder != null){
				stopRecording();
			}
			try{
				if( OutGoingCallReceiver.phoneNo != null ){
					Log.d("GHIAM","outgoing offhook useThisApp");
					helper.useThisApp(context);
					startRecording(OutGoingCallReceiver.phoneNo,2);
					Toast.makeText(context, "Báº¯t Ä‘áº§u ghi Ã¢m "+OutGoingCallReceiver.phoneNo, Toast.LENGTH_SHORT).show();
				}	
			}catch(Exception e){
				Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
			}
		}
	}
	
	public void startRecording(String phoneNo,int callState){
		/*
		mRecorder = new MediaRecorder();
		mRecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION);
		mRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
		*/
		recorder = ExtAudioRecorder.getInstanse(false);
		try{
			Log.d("GHIAM", "in start recording - write to file, phoneNo: "+phoneNo);
			//mRecorder.setOutputFile(helper.getFileNameAndWriteToList(phoneNo,callState));
			recorder.setOutputFile(helper.getFileNameAndWriteToList(phoneNo,callState));
		}catch(Exception e){
			Toast.makeText(this.rcontext, "KhÃ´ng ghi Ã¢m Ä‘Æ°á»£c, do: "+e.getMessage(), Toast.LENGTH_LONG).show();
		}
		//mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_WB);
		
		try {
			/*
			mRecorder.prepare();
			mRecorder.start();
			*/
			recorder.prepare();
			recorder.start();
		} catch (IllegalStateException e) {
			Toast.makeText(this.rcontext, "KhÃ´ng ghi Ã¢m Ä‘Æ°á»£c", Toast.LENGTH_SHORT).show();
			Log.e("GHIAM", "unable to record "+e.getMessage());
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
	}
	
	public void stopRecording(){
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
