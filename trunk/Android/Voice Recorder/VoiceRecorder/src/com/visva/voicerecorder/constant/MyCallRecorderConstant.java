package com.visva.voicerecorder.constant;

public class MyCallRecorderConstant {
    public static final String  TAG                      = "MyCallRecorder";
    public static final boolean DEBUG_MODE               = true;

    public static final String  KEY_AUTO_SAVED           = "key_auto_record_call";
    public static final String  KEY_SAVED_INCOMING_CALL  = "key_saved_incoming_call";
    public static final String  KEY_SAVED_OUTGOING_CALL  = "key_saved_out_going_call";
    public static final String  KEY_THEME                = "key_theme";
    public static final String  KEY_FIRST_TIME_RUNNING   = "key_first_time_running";
    public static final String  KEY_SHOW_NOTIFICATION    = "key_show_notificaiton";

    // notification intent
    public static final String  FAVORITE_INTENT          = "com.visva.android.myrecordcall.favorite_intent";
    public static final String  MAKE_NOTE_INTENT         = "com.visva.android.myrecordcall.make_note_intent";
    public static final int     NOTIFICATION_ID          = 2459;
    public static final int     NOTIFICATION_REMINDER_ID = 51151;

    //extras data
    public static final String  EXTRA_PHONE_NO           = "phone_no";
    public static final String  EXTRA_PHONE_NAME         = "phone_name";
    public static final String  EXTRA_CREATED_DATE       = "created_date";
    public static final String  EXTRA_CUSTOM_ID          = "custom_id";
    public static final String  EXTRA_FILE_NAME          = "file_name";
    public static final String  EXTRA_DURATION           = "duration";
    public static final String  EXTRA_CALL_STATE         = "call_state";

    public static final String  EXTRA_STATE              = "state";
    public static final int     STATE_INSERT             = 1;
    public static final int     STATE_EDIT               = 0;

    //record file type
    public static final String  WAV_RECORD_TYPE          = ".wav";
    public static final String  MP3_RECORD_TYPE          = ".mp3";
    public static final String  AMR_RECORD_TYPE          = ".AMR";
    public static final String  AAC_RECORD_TYPE          = ".acc";

    public static final int     STATE_INCOMING           = 1;
    public static final int     STATE_OUTGOING            = 2;

}
