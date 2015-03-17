package com.visva.voicerecorder.record;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Environment;

import com.visva.voicerecorder.view.recording.FragmentAllRecord;

/**
 * A class that manage all recording session
 * - determine which sessions to show
 * 	+ ArrayList<RecordingSession> showAll()
 *  + ArrayList<RecordingSession> getByDate(int date,int month,int year)
 *  + ArrayList<RecordingSession> getByPhoneNo(phoneNo)
 * -  manage which session is played, ensure there is only one session is played at a time
 * 	+ void playAudio(RecordingSession s)
 *  + void stopAudio()
 */
public class RecordingManager extends Object implements OnPreparedListener, OnCompletionListener {
    private static RecordingManager     mInstance;

    private ArrayList<RecordingSession> sessions;
    private RecordingSession            currentSession    = null;
    private FragmentAllRecord           fragmentAllRecord = null;

    public static RecordingManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new RecordingManager(context);
        }
        return mInstance;
    }

    public RecordingManager(Context _context) {
    }

    public RecordingManager(Context _context, ArrayList<RecordingSession> sessions) {
        this.sessions = sessions;
    }

    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }

    public void onCompletion(MediaPlayer arg0) {
        if (this.fragmentAllRecord != null && this.fragmentAllRecord.mLastClickedView != null) {
            // how to change color current view to WHITE
            this.fragmentAllRecord.mLastClickedView.setBackgroundColor(Color.WHITE);
            this.fragmentAllRecord.mLastClickedView = null;
        }
    }

    /**
     * write all the content to file (override)
     * Format for meta data:
     * filename;phoneNo;callState;date
     * File location: /sdcard/TerraVoiceRecorder/data
     */
    public void removeFile(String filePath) {
        String dataFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/VisvaVoiceRecorder/data";
        File dataFile = new File(dataFilePath);
        File mediaFile = new File(filePath);
        File modifiedMediaFile = new File(filePath + "-modified.wav");
        if (modifiedMediaFile.exists()) {
            modifiedMediaFile.delete();
        }
        mediaFile.delete();
        try {
            FileWriter fwriter = new FileWriter(dataFile, false);
            String dataContent = "";
            for (int i = this.sessions.size() - 1; i >= 0; i--) {
                dataContent += this.sessions.get(i).fileName + ";" +
                        this.sessions.get(i).phoneNo + ";" +
                        this.sessions.get(i).callState + ";" +
                        this.sessions.get(i).dateCreated + "\n";
            }
            BufferedWriter bw = new BufferedWriter(fwriter);
            bw.write(dataContent);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * set sessions
     */
    public void setSessions(ArrayList<RecordingSession> sessions) {
        this.sessions = sessions;
        this.currentSession = null;
    }

    /**
     * get sessions
     */
    public ArrayList<RecordingSession> getSessions() {
        return this.sessions;
    }

    /**
     * get currentSession
     */
    public RecordingSession getCurrentSession() {
        return this.currentSession;
    }

    /**
     * set currentSession
     */
    public void setCurrentSession(RecordingSession s) {
        this.currentSession = s;
    }

    /**
     * 
     */

    /**
     * get list of all sessions
     * @return ArrayList<RecordingSession>
     */
    public ArrayList<RecordingSession> showAll() {
        return this.getSessions();
    }

    /**
     * get list of sessions by a specific date
     * @return ArrayList<RecordingSession>
     */
    public ArrayList<RecordingSession> getByDate(int date, int month, int year) {
        ArrayList<RecordingSession> result = new ArrayList<RecordingSession>();
        for (int i = 0; i < this.sessions.size(); i++) {
            String dateCreated = this.sessions.get(i).dateCreated;
            int sdate = Integer.parseInt(dateCreated.split("-")[0]);
            int smonth = Integer.parseInt(dateCreated.split("-")[1]);
            int syear = Integer.parseInt(dateCreated.split("-")[2]);
            if (sdate == date && smonth == month && syear == year) {
                result.add(this.sessions.get(i));
            }
        }
        return result;
    }

    /**
     * get list of sessions by phone number
     * @return ArrayList<RecordingSession>
     */
    public ArrayList<RecordingSession> getByPhoneNo(String phoneNo) {
        ArrayList<RecordingSession> result = new ArrayList<RecordingSession>();
        for (int i = 0; i < this.sessions.size(); i++) {
            String sphoneNo = this.sessions.get(i).phoneNo;
            if (sphoneNo.equals(phoneNo)) {
                result.add(this.sessions.get(i));
            }
        }
        return result;
    }

    /**
     * get list of phone numbers
     * @return ArrayList<String>
     */
    public ArrayList<String> getPhoneNumbers() {
        ArrayList<String> result = new ArrayList<String>();
        for (int i = 0; i < this.sessions.size(); i++) {
            if (!result.contains(this.sessions.get(i).phoneNo)) {
                result.add(this.sessions.get(i).phoneNo);
            }
        }
        return result;
    }

    public void stopAudio() {
        this.currentSession = null;
    }
}