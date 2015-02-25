package com.visva.voicerecorder.view.activity;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;

import com.visva.voicerecorder.R;
import com.visva.voicerecorder.utils.TimeUtility;

public class ActivityPlayRecording extends Activity implements OnCompletionListener, SeekBar.OnSeekBarChangeListener {

    private SeekBar     songProgressBar;
    private TextView    songCurrentDurationLabel;
    private TextView    songTotalDurationLabel;
    // Media Player
    private MediaPlayer mp;
    // Handler to update UI timer, progress bar etc,.
    private Handler     mHandler = new Handler(); ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_recording);

        // All player buttons
        songProgressBar = (SeekBar) findViewById(R.id.songProgressBar);
        songCurrentDurationLabel = (TextView) findViewById(R.id.songCurrentDurationLabel);
        songTotalDurationLabel = (TextView) findViewById(R.id.songTotalDurationLabel);
        // Mediaplayer
        mp = new MediaPlayer();

        // Listeners
        songProgressBar.setOnSeekBarChangeListener(this); // Important
        mp.setOnCompletionListener(this); // Important

        // By default play first song
        playRecoder();

    }

    /**
     * Receiving song index from playlist view
     * and play the song
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    /**
     * Function to play a song
     * @param songIndex - index of song
     * */
    public void playRecoder() {
        // Play song
        try {
            mp.reset();
            String source = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Ringtones/hangouts_incoming_call.ogg";
            Log.d("KieuThang", "source:" + source);
            mp.setDataSource(source);
            mp.prepare();
            mp.start();

            // set Progress bar values
            songProgressBar.setProgress(0);
            songProgressBar.setMax(100);

            // Updating progress bar
            updateProgressBar();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Update timer on seekbar
     * */
    public void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }

    /**
     * Background Runnable thread
     * */
    private Runnable mUpdateTimeTask = new Runnable() {
                                         public void run() {
                                             try {
                                                 long totalDuration = mp.getDuration();
                                                 long currentDuration = mp.getCurrentPosition();

                                                 // Displaying Total Duration time
                                                 songTotalDurationLabel.setText("" + TimeUtility.milliSecondsToTimer(totalDuration));
                                                 // Displaying time completed playing
                                                 songCurrentDurationLabel.setText("" + TimeUtility.milliSecondsToTimer(currentDuration));

                                                 // Updating progress bar
                                                 int progress = (int) (TimeUtility.getProgressPercentage(currentDuration, totalDuration));
                                                 //Log.d("Progress", ""+progress);
                                                 songProgressBar.setProgress(progress);

                                                 // Running this thread after 100 milliseconds
                                                 mHandler.postDelayed(this, 100);
                                             } catch (IllegalStateException e) {
                                             }

                                         }
                                     };

    /**
     * 
     * */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {

    }

    /**
     * When user starts moving the progress handler
     * */
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // remove message Handler from updating progress bar
        mHandler.removeCallbacks(mUpdateTimeTask);
    }

    /**
     * When user stops moving the progress hanlder
     * */
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mHandler.removeCallbacks(mUpdateTimeTask);
        int totalDuration = mp.getDuration();
        int currentPosition = TimeUtility.progressToTimer(seekBar.getProgress(), totalDuration);

        // forward or backward to certain seconds
        mp.seekTo(currentPosition);

        // update timer progress again
        updateProgressBar();
    }

    /**
     * On Song Playing completed
     * if repeat is ON play same song again
     * if shuffle is ON play random song
     * */
    @Override
    public void onCompletion(MediaPlayer arg0) {

        // check for repeat is ON or OFF
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mp.release();
    }

}