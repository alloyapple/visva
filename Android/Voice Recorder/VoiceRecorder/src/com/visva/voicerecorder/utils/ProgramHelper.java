package com.visva.voicerecorder.utils;

import java.io.File;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import com.visva.voicerecorder.MyCallRecorderApplication;
import com.visva.voicerecorder.constant.MyCallRecorderConstant;
import com.visva.voicerecorder.log.AIOLog;
import com.visva.voicerecorder.record.RecordingSession;

/**
 * Users can use this application up to 10 times before doing activate it
 * How to check that? Store the number of time opening this application in a private file
 */
public class ProgramHelper {
    public final static int NUM_OF_USE = 20;

    public ProgramHelper() {
    }

    public String getFileNameAndWriteToList(Context context, String phoneNo, int callState, String createdDate) throws Exception {
        String fileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MyCallRecorder/sessions/";
        fileName += phoneNo + ";" + callState + ";" + createdDate + ";" + "1" + MyCallRecorderConstant.AMR_RECORD_TYPE;
        this.writeToList(context, fileName, phoneNo, createdDate, callState);
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
    public boolean writeToList(Context context, String fileName, String phoneNo, String date, int callState) throws Exception {
        String phoneName = "";
        Uri phoneNameUri = Utils.getContactUriTypeFromPhoneNumber(context.getContentResolver(), phoneNo, 1);
        if (phoneNameUri == null || StringUtility.isEmpty(phoneNameUri.toString())) {
            phoneName = "";
        } else
            phoneName = phoneNameUri.toString();
        int isFavorite = Utils.isCheckFavouriteContactByPhoneNo(context, phoneNo);
        RecordingSession session = new RecordingSession(phoneNo, callState, fileName, phoneName, isFavorite, date);
        SQLiteHelper sqLiteHelper = MyCallRecorderApplication.getInstance().getSQLiteHelper(context);
        sqLiteHelper.addNewRecordingSession(session);

        if (MyCallRecorderApplication.getInstance().getActivity() != null) {
            MyCallRecorderApplication.getInstance().getActivity().addNewRecord(session);
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
        SQLiteHelper sqLiteHelper = MyCallRecorderApplication.getInstance().getSQLiteHelper(context);
        result = sqLiteHelper.getAllRecordItem();
        for (int i = result.size() - 1; i >= 0; i--) {
            finalResult.add(result.get(i));
        }
        return finalResult;
    }

    /**
     * end reading setting file
     */

    /**
     * A method to: 
     * - prepare: 
     * 	+ /sdcard/MyCallRecorder/ {folder (most system)}
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
    }

    public void removeNewestSession(String filePath) {
        File dataFile = new File(filePath);
        if (dataFile.exists()) {
            AIOLog.d(MyCallRecorderConstant.TAG, "delete the last record file");
            dataFile.delete();
        }
    }
}
