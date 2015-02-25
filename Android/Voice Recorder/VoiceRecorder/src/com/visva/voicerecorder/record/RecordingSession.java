package com.visva.voicerecorder.record;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * A class represent for a recorded session
 */
public class RecordingSession extends Object implements Parcelable {
    public String            phoneNo;
    public String            dateCreated;
    public int               callState;
    public String            fileName;

    private RecordingSession session;

    public RecordingSession(String fileName, String phoneNo, String dateCreated, int callState) {
        this.phoneNo = phoneNo;
        this.dateCreated = dateCreated;
        this.callState = callState;
        this.fileName = fileName;
    }

    public RecordingSession(Parcel in) {
    }

    private void readFromParcel(Parcel in) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO Auto-generated method stub

    }

    /**
    *
    * This field is needed for Android to be able to
    * create new objects, individually or as arrays.
    *
    * This also means that you can use use the default
    * constructor to create the object and use another
    * method to hyrdate it as necessary.
    *
    * I just find it easier to use the constructor.
    * It makes sense for the way my brain thinks ;-)
    *
    */
    public static final Parcelable.Creator CREATOR =
                                                           new Parcelable.Creator() {
                                                               public RecordingSession createFromParcel(Parcel in) {
                                                                   return new RecordingSession(in);
                                                               }

                                                               public RecordingSession[] newArray(int size) {
                                                                   return new RecordingSession[size];
                                                               }
                                                           };
}