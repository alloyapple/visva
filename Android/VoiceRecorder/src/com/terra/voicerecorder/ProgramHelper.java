package com.terra.voicerecorder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

/**
 * Users can use this application up to 10 times before doing activate it
 * How to check that? Store the number of time opening this application in a private file
 */
public class ProgramHelper {
	private final String secretString = "foo.bar.troll.facepalm.megusta.123654.2162012";
	private final String localKey = "there.never.was.on.earth.a.knight.So.waited.by.ladies.fair";
	private final String identity = "com.terra.app.terrarecorder";
	public static MainActivity activity = null;
	public final static int NUM_OF_USE = 20;
	
	public ProgramHelper(){
	}
	
	public int getNumOfUsed(Context context){
		String FILENAME = "privateInfo";
		FileInputStream fis;
		int numOfUse = 0;
		try{
			fis = context.openFileInput(FILENAME);
			numOfUse = fis.read();
			fis.close();
			Log.d("GHIAM","doc file "+numOfUse);
		}catch(Exception e){
			Log.d("GHIAM","khong doc duoc file "+numOfUse+", file chua ton tai");
			e.printStackTrace();
		}
		return numOfUse;
	}
	
	public int useThisApp(Context context){
		String FILENAME = "privateInfo";
		FileOutputStream fos;
		int numOfUse = getNumOfUsed(context);
		numOfUse ++;
		Log.d("GHIAM","so lan su dung: "+numOfUse);
		try {
			fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
			fos.write(numOfUse);
			fos.close();
			Log.d("GHIAM","ghi ra file: "+numOfUse);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			Log.d("GHIAM", "useThisApp - FileNotFoundException");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.d("GHIAM", "useThisApp - IOException");
			e.printStackTrace();
		}
		return numOfUse;
	}
	
	public void stopMainActivity(){
		if(ProgramHelper.activity != null){
			ProgramHelper.activity.finish();
		}
	}
	
	public boolean checkActivated(String uuid){
		boolean activated = true;
		// read the encryted file
		String localString = this.sha1Hash(uuid+this.localKey);
		String keyfilePath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/TerraVoiceRecorder/key";
		File file = new File(keyfilePath);
		StringBuilder text = new StringBuilder();
		try{
			BufferedReader br = new BufferedReader(new FileReader(file));
			text.append(br.readLine());
			br.close();
		}catch(IOException e){
			activated = false;
		}
		if(localString.equals(text.toString())){
			activated = true;
		}else{
			activated = false;
		}
		return activated;
	}
	
	public boolean processActivation(String uuid, String activationCode, String phoneNo){
		String URL = "http://joolist.com/demo/activation/code/verify?activecode="+activationCode+"&tel="+phoneNo+"&uuid="+uuid;
    	HttpClient httpClient = new DefaultHttpClient();
    	HttpResponse response;
    	boolean res = false;
    	try {
			response = httpClient.execute(new HttpGet(URL));
			StatusLine statusLine = response.getStatusLine();
			if(statusLine.getStatusCode() ==  HttpStatus.SC_OK){
	    		ByteArrayOutputStream out = new ByteArrayOutputStream();
	    		response.getEntity().writeTo(out);
	    		out.close();
	    		String responseString = out.toString();
	    		// more logic
	    		try {
					JSONObject json = new JSONObject(responseString);
					JSONObject rs = (JSONObject) json.get("result");
					boolean status = rs.getBoolean("status");
					if(status){
						// success
						JSONObject data = rs.getJSONObject("data");
						String encrypted = data.getString("encrypted");
						String activationCodeFromServer = data.getString("code");
						JSONArray books = data.getJSONArray("books");
						Log.d("GHIAM", "encrypted: "+encrypted+";code: "+activationCodeFromServer+"; books: "+books.get(0).toString());
						if(this.activate(uuid, activationCodeFromServer, encrypted, books.get(0).toString())){
							res = true;
						}else{
							res = false;
						}
					}else{
						res = false;
					}
				} catch (Exception e) {
					res = false;
				}
	    	}else{
	    		// close the connection
	    		response.getEntity().getContent().close();
	    	}
		} catch (Exception e) {
			res = false;
		}
    	return res;
	}
	
	public boolean activate (String uuid, String activationCode, String encrypted, String programId) throws Exception{
		boolean success = false;
		// check as if the returned string is in corrected format
		String sha1String = this.sha1Hash(activationCode+uuid+this.secretString);
		String localString = this.sha1Hash(uuid+this.localKey);
		// use programId later !
		Log.d("GHIAM", "sh1String: "+sha1String);
		if(sha1String.equals(encrypted) && programId.equals(identity)){
			success = true;
			// create folder
			File voiceRecorderFolder = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/TerraVoiceRecorder");
			voiceRecorderFolder.mkdirs();
			File keyfile = new File(voiceRecorderFolder.toString(),"key");
			// write the result to a file which cannot be faked between devices
			if(voiceRecorderFolder.canWrite()){
				try{
					FileWriter fileWriter = new FileWriter(keyfile);
					BufferedWriter out = new BufferedWriter(fileWriter);
					out.write(localString);
					out.close();
				}catch(IOException e){
					throw new Exception("Không active được thiết bị, kiểm tra thẻ nhớ !");
				}
			}
		}
		Log.d("GHIAM","success: "+success);
		return success;
	}
	
	public String getFileNameAndWriteToList(String phoneNo,int callState) throws Exception{
		String fileName = Environment.getExternalStorageDirectory().getAbsolutePath()+"/TerraVoiceRecorder/sessions/";
		String currentDateTimeString = new SimpleDateFormat("d-M-yyyy-HH-mm-ss").format(new Date());
		String currentDateString = new SimpleDateFormat("d-M-yyyy-HH:mm:ss").format(new Date());
		fileName += currentDateTimeString+".wav";
		Log.d("GHIAM",fileName);
		Log.d("GHIAM","phoneNo: "+phoneNo+" <in Helper/getFi...>");
		this.writeToList(fileName,phoneNo,currentDateString,callState);
		return fileName;
	}
	
	/**
	 * A method to append to data file the meta data about a recording session
	 * Text file to write to has already been created at first, the path is:
	 * "/sdcard/TerraVoiceRecorder/data" {most case}
	 * Format for meta data:
	 * filename;phoneNo;callState;date 
	 * @param fileName
	 * @param phoneNo
	 * @param date
	 * @param callState
	 * @return true/false
	 */
	public boolean writeToList(String fileName, String phoneNo, 
			String date, int callState) throws Exception{
		String dataFilePath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/TerraVoiceRecorder/data";
		File dataFile = new File(dataFilePath);
		Log.d("GHIAM","phoneNo: "+phoneNo+" <in Helper/writeTo...>");
		String metaData = fileName+";"+phoneNo+";"+callState+";"+date+"\n";
		try{
			FileWriter fileWriter = new FileWriter(dataFile,true);
			BufferedWriter out = new BufferedWriter(fileWriter);
			out.append(metaData);
			out.close();
		}catch(IOException e){
			throw new Exception("Ghi được file, kiểm tra thẻ nhớ !");
		}
		return true;
	}
	
	private String sha1Hash(String toHash){
	    String hash = null;
	    try{
	        MessageDigest digest = MessageDigest.getInstance( "SHA-1" );
	        digest.update( toHash.getBytes(), 0, toHash.length() );
	        hash = new BigInteger( 1, digest.digest() ).toString( 16 );
	    }
	    catch( NoSuchAlgorithmException e ){
	        e.printStackTrace();
	    }
	    return hash;
	}
	
	/**
     * A method that load all the necessary data from a text file
     * 
     * Format for meta data:
	 * filename;phoneNo;callState;date
	 * 
     * store the data in an ArrayList<RecordingSession>
     * @return ArrayList<RecordingSession>
     */
    public ArrayList<RecordingSession> getRecordingSessionsFromFile(){
    	ArrayList<RecordingSession> result = new ArrayList<RecordingSession>();
    	ArrayList<RecordingSession> finalResult = new ArrayList<RecordingSession>();
    	String fileDataPath = Environment.getExternalStorageDirectory()+"/TerraVoiceRecorder/data";
    	File fileData = new File(fileDataPath);
    	try{
			BufferedReader br = new BufferedReader(new FileReader(fileData));
			String line;
			while((line = br.readLine()) != null){
				String fileName = line.split(";")[0];
				String phoneNo = line.split(";")[1];
				int callState = Integer.parseInt(line.split(";")[2]);
				String dateCreated = line.split(";")[3];
				RecordingSession s = new RecordingSession(fileName, phoneNo, dateCreated, callState);
				result.add(s);
			}
			for(int i=result.size()-1;i>=0;i--){
				finalResult.add(result.get(i));
			}
			br.close();
		}catch(IOException e){
			e.printStackTrace();
		}
    	return finalResult;
    }
    
    /**
     * Check if setting file is existed, if not, the value is set to default: 1
     * else, read from the file with format:
     * x;x (x=0,1)
     * @return true/false
     */
    public int isRecordingIncomingCall(){
    	int result = 1;
    	String settingFilePath = Environment.getExternalStorageDirectory().getAbsolutePath()+
    			"/TerraVoiceRecorder/setting";
    	File settingFile = new File(settingFilePath);
    	if(settingFile.exists()){
    		// read
    		try {
				FileReader fileReader = new FileReader(settingFile);
				BufferedReader bf = new BufferedReader(fileReader);
				String str = bf.readLine();
				result = Integer.parseInt(str.split(";")[0]);
				bf.close();
    		} catch (FileNotFoundException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    	}else{
    		writeSettingValue(1,1);
    	}
    	return result;
    }
    
    public int isRecordingOutgoingCall(){
    	int result = 1;
    	String settingFilePath = Environment.getExternalStorageDirectory().getAbsolutePath()+
    			"/TerraVoiceRecorder/setting";
    	File settingFile = new File(settingFilePath);
    	if(settingFile.exists()){
    		// read
    		try {
				FileReader fileReader = new FileReader(settingFile);
				BufferedReader bf = new BufferedReader(fileReader);
				String str = bf.readLine();
				result = Integer.parseInt(str.split(";")[1]);
				bf.close();
    		} catch (FileNotFoundException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    	}else{
    		writeSettingValue(1, 1);
    	}
    	return result;
    }
    
    public void writeSettingValue(int incoming, int outgoing){
    	String settingFilePath = Environment.getExternalStorageDirectory().getAbsolutePath()+
    			"/TerraVoiceRecorder/setting";
    	File settingFile = new File(settingFilePath);
    	FileReader fileReader;
    	FileWriter fileWriter;
    	int oldIncoming = -1, oldOutgoing = -1;
    	String str = "";
    	if(settingFile.exists()){
    		try {
    			fileReader = new FileReader(settingFile);
    			BufferedReader bf = new BufferedReader(fileReader);
    			str = bf.readLine();
    			oldIncoming = Integer.parseInt(str.split(";")[0]);
    			oldOutgoing = Integer.parseInt(str.split(";")[1]);
    			if(incoming != -1) oldIncoming = incoming;
    			if(outgoing != -1) oldOutgoing = outgoing;
    			str = oldIncoming+";"+oldOutgoing;
    			bf.close();
    		} catch (FileNotFoundException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    	}else{
    		str = "1;1";
    	}
		try {
			fileWriter = new FileWriter(settingFile);
			fileWriter.write(str);
			fileWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    /**
     * end reading setting file
     */
    
    /**
     * A method to: 
     * - prepare: 
     * 	+ /sdcard/TerraVoiceRecorder/ {folder (most system)}
     *  + /sdcard/TerraVoiceRecorder/data {file to contains all the recording list}
     *  + /sdcard/TerraVoiceRecorder/sessions/ {folder to contain all the recording}
     * - checking activation to show activation dialog
     */
    public void prepare(){
    	String recorderRootFolderPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/TerraVoiceRecorder/";
    	File recorderRootFolder = new File(recorderRootFolderPath);
    	if(!recorderRootFolder.exists()){
    		recorderRootFolder.mkdirs();
    	}
    	String sessionRecordingFolderPath = recorderRootFolderPath+"sessions/";
    	File sessionRecordingFolder = new File(sessionRecordingFolderPath);
    	if(!sessionRecordingFolder.exists()){
    		sessionRecordingFolder.mkdirs();
    	}
    	String sessionDataFilePath = recorderRootFolderPath+"data";
    	File sessionDataFile = new File(sessionDataFilePath);
    	if(!sessionDataFile.exists()){
    		try {
				sessionDataFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    }
    
    public void removeNewestSession(){
    	String dataFilePath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/TerraVoiceRecorder/data";
		File dataFile = new File(dataFilePath);
		String lastFileName = "";
		String line = null;
		ArrayList<String> lines = new ArrayList<String>();
		String finalStringToWriteOut = "";
		try{
			BufferedReader br = new BufferedReader(new FileReader(dataFile));
			while((line = br.readLine())!=null){
				lastFileName = line.split(";")[0];
				lines.add(line);
			}
			br.close();
			for(int i=0;i<lines.size()-1;i++){
				finalStringToWriteOut += lines.get(i)+"\n";
			}
			FileWriter fileWriter = new FileWriter(dataFile,false);
			BufferedWriter out = new BufferedWriter(fileWriter);
			out.append(finalStringToWriteOut);
			out.close();
			File mediaFile = new File(lastFileName);
			mediaFile.delete();
			this.stopMainActivity();
		}catch(IOException e){
			
		}
    }
}
