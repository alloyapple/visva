package com.visva.voicerecorder.view.activity;

import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.views.LayoutRipple;
import com.gc.materialdesign.widgets.Dialog;
import com.visva.voicerecorder.MyCallRecorderApplication;
import com.visva.voicerecorder.R;
import com.visva.voicerecorder.constant.MyCallRecorderConstant;
import com.visva.voicerecorder.log.AIOLog;
import com.visva.voicerecorder.note.ActivityNoteEditor;
import com.visva.voicerecorder.note.NoteItem;
import com.visva.voicerecorder.record.RecordingSession;
import com.visva.voicerecorder.utils.StringUtility;
import com.visva.voicerecorder.utils.TimeUtility;
import com.visva.voicerecorder.utils.Utils;
import com.visva.voicerecorder.view.VisvaAbstractFragmentActivity;
import com.visva.voicerecorder.view.widget.CircleImageView;

public class ActivityPlayRecording extends VisvaAbstractFragmentActivity implements OnCompletionListener, SeekBar.OnSeekBarChangeListener {
    // ======================Constant Define=====================
    private static final int REQUEST_CODE = 1002;
    private static final int _ID          = 0;
    private static final int DISPLAY_NAME = _ID + 1;
    private static final int NUMBER       = DISPLAY_NAME + 1;
    private static final int PHOTO_URI    = NUMBER + 1;

    private SeekBar          songProgressBar;
    private TextView         songCurrentDurationLabel;
    private TextView         songTotalDurationLabel;
    private CircleImageView  mAvatar;
    private TextView         mTextPhoneName;
    private TextView         mTextPhoneNumber;
    private ImageButton      mBtnPlay;
    private RelativeLayout   mLayoutPlayRecord;
    private TextView         mTextClose;
    private LayoutRipple     mLayoutDelete;
    private LayoutRipple     mLayoutCall;
    private LayoutRipple     mLayoutNote;
    private LayoutRipple     mLayoutShare;
    // Media Player
    private MediaPlayer      mMediaPlayer;
    // Handler to update UI timer, progress bar etc,.
    private Handler          mHandler     = new Handler();
    private RecordingSession mRecordingSession;
    private int              mThemeColor;
    private int              mPressedThemeColor;

    private void initLayout() {
        /*theme*/
        mLayoutPlayRecord = (RelativeLayout) findViewById(R.id.layout_play_record);
        mTextClose = (TextView) findViewById(R.id.text_close);

        mLayoutCall = (LayoutRipple) findViewById(R.id.btnCall);
        mLayoutCall.setBackground(new ColorDrawable(mThemeColor));
        mLayoutCall.setRippleColor(mPressedThemeColor);
        mLayoutDelete = (LayoutRipple) findViewById(R.id.btn_delete);
        mLayoutDelete.setBackground(new ColorDrawable(mThemeColor));
        mLayoutDelete.setRippleColor(mPressedThemeColor);
        mLayoutNote = (LayoutRipple) findViewById(R.id.btn_note);
        mLayoutNote.setBackground(new ColorDrawable(mThemeColor));
        mLayoutNote.setRippleColor(mPressedThemeColor);
        mLayoutShare = (LayoutRipple) findViewById(R.id.btn_share);
        mLayoutShare.setBackground(new ColorDrawable(mThemeColor));
        mLayoutShare.setRippleColor(mPressedThemeColor);

        mLayoutPlayRecord.setBackgroundColor(mThemeColor);
        mTextClose.setTextColor(mThemeColor);

        songProgressBar = (SeekBar) findViewById(R.id.songProgressBar);
        songCurrentDurationLabel = (TextView) findViewById(R.id.songCurrentDurationLabel);
        songTotalDurationLabel = (TextView) findViewById(R.id.songTotalDurationLabel);
        mAvatar = (CircleImageView) findViewById(R.id.imageProfile);
        mTextPhoneName = (TextView) findViewById(R.id.txt_phone_name);
        mTextPhoneNumber = (TextView) findViewById(R.id.txt_phone_number);
        mBtnPlay = (ImageButton) findViewById(R.id.btnPlay);
        Uri photoUri = Utils.getContactUriTypeFromPhoneNumber(getContentResolver(), mRecordingSession.phoneNo, PHOTO_URI);
        if (photoUri != null) {
            mAvatar.setImageURI(photoUri);
        } else {
            mAvatar.setImageResource(R.drawable.ic_contact_picture_holo_light);
        }

        Uri contactName = Utils.getContactUriTypeFromPhoneNumber(getContentResolver(), mRecordingSession.phoneNo, DISPLAY_NAME);
        if (contactName == null || com.visva.voicerecorder.utils.StringUtility.isEmpty(contactName.toString())) {
            mTextPhoneName.setVisibility(View.GONE);
        } else {
            mTextPhoneName.setVisibility(View.VISIBLE);
            mTextPhoneName.setText(contactName.toString());
        }
        mTextPhoneNumber.setText(mRecordingSession.phoneNo + "");
        mAvatar.assignContactFromPhone(mRecordingSession.phoneNo, true);
        mBtnPlay.setImageResource(R.drawable.ic_pause);
        /**
         * Play button click event
         * plays a song and changes button to pause image
         * pauses a song and changes button to play image
         * */
        mBtnPlay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // check for already playing
                if (mMediaPlayer.isPlaying()) {
                    if (mMediaPlayer != null) {
                        mMediaPlayer.pause();
                        // Changing button image to play button
                        mBtnPlay.setImageResource(R.drawable.ic_play);
                    }
                } else {
                    // Resume song
                    if (mMediaPlayer != null) {
                        mMediaPlayer.start();
                        // Changing button image to pause button
                        mBtnPlay.setImageResource(R.drawable.ic_pause);
                    }
                }

            }
        });
    }

    /**
     * Receiving song index from playlist view
     * and play the song
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            if (MyCallRecorderApplication.getInstance().getActivity() != null) {
                Utils.requestToRefreshView(MyCallRecorderApplication.getInstance().getActivity(), ActivityHome.FRAGMENT_ALL_RECORDING);
                Utils.requestToRefreshView(MyCallRecorderApplication.getInstance().getActivity(), ActivityHome.FRAGMENT_FAVOURITE);
            }
        }
    }

    /**
     * Function to play a song
     * @param songIndex - index of song
     * */
    public void playRecoder() {

        AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audio.setStreamVolume(AudioManager.STREAM_MUSIC, audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC), AudioManager.FLAG_SHOW_UI);
        // Play song
        try {
            mMediaPlayer.reset();
            File modifiedFile = new File(mRecordingSession.fileName + "-modified.wav");
            if (modifiedFile.exists()) {
                AIOLog.e(MyCallRecorderConstant.TAG, "modifiedFile is exist");
                this.mMediaPlayer.setDataSource(mRecordingSession.fileName + "-modified.wav");
            } else {
                AIOLog.e(MyCallRecorderConstant.TAG, "modifiedFile is not exist!!!!!!!!");
                this.mMediaPlayer.setDataSource(mRecordingSession.fileName);
            }
            mMediaPlayer.prepare();
            mMediaPlayer.start();

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
                                                 long totalDuration = mMediaPlayer.getDuration();
                                                 long currentDuration = mMediaPlayer.getCurrentPosition();

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
        int totalDuration = mMediaPlayer.getDuration();
        int currentPosition = TimeUtility.progressToTimer(seekBar.getProgress(), totalDuration);

        // forward or backward to certain seconds
        mMediaPlayer.seekTo(currentPosition);

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
        mMediaPlayer.release();
    }

    public void onClickCloseButton(View v) {
        finish();
    }

    @Override
    public int contentView() {
        return R.layout.activity_play_recording;
    }

    @Override
    public void onCreate() {
        mRecordingSession = (RecordingSession) getIntent().getExtras().get("recording_session");
        if (mRecordingSession == null) {
            AIOLog.e(MyCallRecorderConstant.TAG, "recordingSession is null");
            finish();
        }
        mThemeColor = MyCallRecorderApplication.getInstance().getApplicationTheme();
        mPressedThemeColor = MyCallRecorderApplication.getInstance().getApplicationPressedTheme();
        initLayout();

        // Mediaplayer
        mMediaPlayer = new MediaPlayer();

        // Listeners
        songProgressBar.setOnSeekBarChangeListener(this); // Important
        mMediaPlayer.setOnCompletionListener(this); // Important

        // By default play first song
        playRecoder();
    }

    public void onCLickDeleteBtn(View v) {
        if (mMediaPlayer.isPlaying()) {
            if (mMediaPlayer != null) {
                mMediaPlayer.pause();
                // Changing button image to play button
                mBtnPlay.setImageResource(R.drawable.ic_play);
            }
        }
        Log.d("KieuThang", "onCLickDeleteBtn");
        if (mRecordingSession == null)
            return;
        String title = mRecordingSession.phoneName;
        if (StringUtility.isEmpty(title)) {
            title = mRecordingSession.phoneNo;
        }
        String contentMsg = getResources().getString(R.string.are_you_sure_to_delete_record);
        String cancel = getResources().getString(R.string.cancel);
        final Dialog dialog = new Dialog(this, title, contentMsg);
        dialog.addCancelButton(cancel, new OnClickListener() {

            @Override
            public void onClick(View v) {
            }
        });
        dialog.setOnAcceptButtonClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(ActivityPlayRecording.this, "Deleted", Toast.LENGTH_SHORT).show();
                Utils.deleteRecordingSesstionAction(ActivityPlayRecording.this, mRecordingSession);

                finish();

                if (MyCallRecorderApplication.getInstance().getActivity() != null) {
                    Utils.requestRefreshViewToRemoveNewRecord(MyCallRecorderApplication.getInstance().getActivity(),
                            ActivityHome.FRAGMENT_ALL_RECORDING, mRecordingSession);
                    Utils.requestToRefreshView(MyCallRecorderApplication.getInstance().getActivity(), ActivityHome.FRAGMENT_FAVOURITE);
                }
            }
        });
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                try {
                    dialog.show();
                } catch (Exception e) {

                }
            }
        });
    }

    public void onClickCallBtn(View v) {
        if (mMediaPlayer.isPlaying()) {
            if (mMediaPlayer != null) {
                mMediaPlayer.pause();
                // Changing button image to play button
                mBtnPlay.setImageResource(R.drawable.ic_play);
            }
        }
        if (mRecordingSession == null)
            return;
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + mRecordingSession.phoneNo));
        startActivity(intent);
    }

    public void onClickNoteBtn(View v) {
        if (mMediaPlayer.isPlaying()) {
            if (mMediaPlayer != null) {
                mMediaPlayer.pause();
                // Changing button image to play button
                mBtnPlay.setImageResource(R.drawable.ic_play);
            }
        }
        if (mRecordingSession == null)
            return;
        int state = MyCallRecorderConstant.STATE_INSERT;
        NoteItem noteItem = Utils.getNoteItemFromRecordSession(this, mRecordingSession.dateCreated);
        if (noteItem == null || (StringUtility.isEmpty(noteItem.note) && StringUtility.isEmpty(noteItem.title))) {
            state = MyCallRecorderConstant.STATE_INSERT;
        } else {
            state = MyCallRecorderConstant.STATE_EDIT;
        }
        Bundle bundle = new Bundle();
        bundle.putInt(MyCallRecorderConstant.EXTRA_STATE, state);
        bundle.putString(MyCallRecorderConstant.EXTRA_CREATED_DATE, mRecordingSession.dateCreated);
        bundle.putString(MyCallRecorderConstant.EXTRA_PHONE_NAME, mRecordingSession.phoneName);
        bundle.putString(MyCallRecorderConstant.EXTRA_PHONE_NO, mRecordingSession.phoneNo);

        Intent updateNoteIntent = new Intent(this, ActivityNoteEditor.class);
        updateNoteIntent.setAction(MyCallRecorderConstant.MAKE_NOTE_INTENT);
        updateNoteIntent.putExtras(bundle);
        updateNoteIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivityForResult(updateNoteIntent, REQUEST_CODE);
    }

    public void onClickShareBtn(View v) {
        if (mMediaPlayer.isPlaying()) {
            if (mMediaPlayer != null) {
                mMediaPlayer.pause();
                // Changing button image to play button
                mBtnPlay.setImageResource(R.drawable.ic_play);
            }
        }
        if (mRecordingSession == null)
            return;
        Utils.shareAllRecordingSessionInfoAction(this, mRecordingSession);
    }

}