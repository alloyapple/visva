package com.visva.voicerecorder.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
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

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.visva.MyCallRecorderApplication;
import com.visva.voicerecorder.record.RecordingSession;

/**
 * Users can use this application up to 10 times before doing activate it
 * How to check that? Store the number of time opening this application in a private file
 */
public class ProgramHelper {
    public final static int NUM_OF_USE = 20;

    public ProgramHelper() {
    }

    public int getNumOfUsed(Context context) {
        String FILENAME = "privateInfo";
        FileInputStream fis;
        int numOfUse = 0;
        try {
            fis = context.openFileInput(FILENAME);
            numOfUse = fis.read();
            fis.close();
            Log.d("GHIAM", "doc file " + numOfUse);
        } catch (Exception e) {
            Log.d("GHIAM", "khong doc duoc file " + numOfUse + ", file chua ton tai");
            e.printStackTrace();
        }
        return numOfUse;
    }

    public int useThisApp(Context context) {
        String FILENAME = "privateInfo";
        FileOutputStream fos;
        int numOfUse = getNumOfUsed(context);
        numOfUse++;
        Log.d("GHIAM", "so lan su dung: " + numOfUse);
        try {
            fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            fos.write(numOfUse);
            fos.close();
            Log.d("GHIAM", "ghi ra file: " + numOfUse);
        } catch (FileNotFoundException e) {
            Log.d("GHIAM", "useThisApp - FileNotFoundException");
            e.printStackTrace();
        } catch (IOException e) {
            Log.d("GHIAM", "useThisApp - IOException");
            e.printStackTrace();
        }
        return numOfUse;
    }

    public String getFileNameAndWriteToList(String phoneNo, int callState) throws Exception {
        String fileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MyCallRecorder/sessions/";
        String currentDateTimeString = new SimpleDateFormat("d-M-yyyy-HH-mm-ss").format(new Date());
        String currentDateString = new SimpleDateFormat("d-M-yyyy-HH:mm:ss").format(new Date());
        fileName += currentDateTimeString + ".wav";
        Log.d("GHIAM", fileName);
        Log.d("GHIAM", "phoneNo: " + phoneNo + " <in Helper/getFi...>");
        this.writeToList(fileName, phoneNo, currentDateString, callState);
        return fileName;
    }

    /**
     * A method to append to data file the meta data about a recording session
     * Text file to write to has already been created at first, the path is:
     * "/sdcard/MyCallRecorder/data" {most case}
     * Format for meta data:
     * filename;phoneNo;callState;date 
     * @param fileName
     * @param phoneNo
     * @param date
     * @param callState
     * @return true/false
     */
    public boolean writeToList(String fileName, String phoneNo,
            String date, int callState) throws Exception {
        String dataFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MyCallRecorder/data";
        File dataFile = new File(dataFilePath);
        Log.d("GHIAM", "phoneNo: " + phoneNo + " <in Helper/writeTo...>");
        String metaData = fileName + ";" + phoneNo + ";" + callState + ";" + date + "\n";
        try {
            FileWriter fileWriter = new FileWriter(dataFile, true);
            BufferedWriter out = new BufferedWriter(fileWriter);
            out.append(metaData);
            out.close();
        } catch (IOException e) {
            throw new Exception("Ghi được file, kiểm tra thẻ nhớ !");
        }
        return true;
    }

    private String sha1Hash(String toHash) {
        String hash = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(toHash.getBytes(), 0, toHash.length());
            hash = new BigInteger(1, digest.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
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
    public ArrayList<RecordingSession> getRecordingSessionsFromFile(Context context) {
        ArrayList<RecordingSession> result = new ArrayList<RecordingSession>();
        ArrayList<RecordingSession> finalResult = new ArrayList<RecordingSession>();
        String fileDataPath = Environment.getExternalStorageDirectory() + "/MyCallRecorder/data";
        File fileData = new File(fileDataPath);
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileData));
            String line;
            while ((line = br.readLine()) != null) {
                String fileName = line.split(";")[0];
                String phoneNo = line.split(";")[1];
                int callState = Integer.parseInt(line.split(";")[2]);
                String dateCreated = line.split(";")[3];
                Uri phoneName = Utils.getContactUriTypeFromPhoneNumber(context.getContentResolver(), phoneNo, 1);
                String phoneNameStr = phoneName != null ? phoneName.toString() : "";
                RecordingSession s = new RecordingSession(fileName, phoneNo, dateCreated, callState, phoneNameStr);
                result.add(s);
            }
            for (int i = result.size() - 1; i >= 0; i--) {
                finalResult.add(result.get(i));
            }
            br.close();
        } catch (IOException e) {
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
    public int isRecordingIncomingCall() {
        int result = 1;
        String settingFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/MyCallRecorder/setting";
        File settingFile = new File(settingFilePath);
        if (settingFile.exists()) {
            // read
            try {
                FileReader fileReader = new FileReader(settingFile);
                BufferedReader bf = new BufferedReader(fileReader);
                String str = bf.readLine();
                result = Integer.parseInt(str.split(";")[0]);
                bf.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            writeSettingValue(1, 1);
        }
        return result;
    }

    public int isRecordingOutgoingCall() {
        int result = 1;
        String settingFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/MyCallRecorder/setting";
        File settingFile = new File(settingFilePath);
        if (settingFile.exists()) {
            // read
            try {
                FileReader fileReader = new FileReader(settingFile);
                BufferedReader bf = new BufferedReader(fileReader);
                String str = bf.readLine();
                result = Integer.parseInt(str.split(";")[1]);
                bf.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            writeSettingValue(1, 1);
        }
        return result;
    }

    public void writeSettingValue(int incoming, int outgoing) {
        String settingFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/MyCallRecorder/setting";
        File settingFile = new File(settingFilePath);
        FileReader fileReader;
        FileWriter fileWriter;
        int oldIncoming = -1, oldOutgoing = -1;
        String str = "";
        if (settingFile.exists()) {
            try {
                fileReader = new FileReader(settingFile);
                BufferedReader bf = new BufferedReader(fileReader);
                str = bf.readLine();
                oldIncoming = Integer.parseInt(str.split(";")[0]);
                oldOutgoing = Integer.parseInt(str.split(";")[1]);
                if (incoming != -1)
                    oldIncoming = incoming;
                if (outgoing != -1)
                    oldOutgoing = outgoing;
                str = oldIncoming + ";" + oldOutgoing;
                bf.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
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
     * 	+ /sdcard/MyCallRecorder/ {folder (most system)}
     *  + /sdcard/MyCallRecorder/data {file to contains all the recording list}
     *  + /sdcard/MyCallRecorder/sessions/ {folder to contain all the recording}
     * - checking activation to show activation dialog
     */
    public void prepare() {
        String recorderRootFolderPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MyCallRecorder/";
        File recorderRootFolder = new File(recorderRootFolderPath);
        if (!recorderRootFolder.exists()) {
            recorderRootFolder.mkdirs();
        }
        String sessionRecordingFolderPath = recorderRootFolderPath + "sessions/";
        File sessionRecordingFolder = new File(sessionRecordingFolderPath);
        if (!sessionRecordingFolder.exists()) {
            sessionRecordingFolder.mkdirs();
        }
        String sessionDataFilePath = recorderRootFolderPath + "data";
        File sessionDataFile = new File(sessionDataFilePath);
        if (!sessionDataFile.exists()) {
            try {
                sessionDataFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void removeNewestSession() {
        String dataFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MyCallRecorder/data";
        File dataFile = new File(dataFilePath);
        String lastFileName = "";
        String line = null;
        ArrayList<String> lines = new ArrayList<String>();
        String finalStringToWriteOut = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(dataFile));
            while ((line = br.readLine()) != null) {
                lastFileName = line.split(";")[0];
                lines.add(line);
            }
            br.close();
            for (int i = 0; i < lines.size() - 1; i++) {
                finalStringToWriteOut += lines.get(i) + "\n";
            }
            FileWriter fileWriter = new FileWriter(dataFile, false);
            BufferedWriter out = new BufferedWriter(fileWriter);
            out.append(finalStringToWriteOut);
            out.close();
            File mediaFile = new File(lastFileName);
            mediaFile.delete();
            MyCallRecorderApplication.getInstance().stopActivity();
        } catch (IOException e) {

        }
    }
}
