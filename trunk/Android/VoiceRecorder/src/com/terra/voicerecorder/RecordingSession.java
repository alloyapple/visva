package com.terra.voicerecorder;

/**
 * A class represent for a recorded session
 */
public class RecordingSession extends Object{
	public String phoneNo;
	public String dateCreated;
	public int callState;
	public String fileName;
	
	public RecordingSession(String fileName,String phoneNo, String dateCreated, int callState){
		this.phoneNo = phoneNo;
		this.dateCreated = dateCreated;
		this.callState = callState;
		this.fileName = fileName;
	}
}