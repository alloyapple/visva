package com.visva.voicerecorder.record;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * A class represent for a recorded session
 */
public class RecordingSession extends Object implements Parcelable, Comparable<RecordingSession> {
    public String phoneNo;
    public int    callState;
    public String fileName;
    public String phoneName;
    public int    isFavourite;
    public String dateCreated;
    public String duration;

    public RecordingSession(String phoneNo, int callState, String fileName, String phoneName, int isFavourite, String dateCreated, String duration) {
        this.phoneNo = phoneNo;
        this.dateCreated = dateCreated;
        this.callState = callState;
        this.fileName = fileName;
        this.phoneName = phoneName;
        this.isFavourite = isFavourite;
        this.duration = duration;
    }

    public RecordingSession(Parcel in) {
        this.phoneNo = in.readString();
        this.dateCreated = in.readString();
        this.callState = in.readInt();
        this.fileName = in.readString();
        this.phoneName = in.readString();
        this.isFavourite = in.readInt();
        this.duration = in.readString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(phoneNo);
        out.writeString(dateCreated);
        out.writeInt(callState);
        out.writeString(fileName);
        out.writeString(phoneName);
        out.writeInt(isFavourite);
        out.writeString(duration);
    }

    public static final Parcelable.Creator<RecordingSession> CREATOR = new Parcelable.Creator<RecordingSession>() {
                                                                         public RecordingSession createFromParcel(Parcel in) {
                                                                             return new RecordingSession(in);
                                                                         }

                                                                         public RecordingSession[] newArray(int size) {
                                                                             return new RecordingSession[size];
                                                                         }
                                                                     };

    //getter and setter methods same as the above example 
    @Override
    public int compareTo(RecordingSession compareSession) {
        long currentDuration = Long.valueOf(dateCreated);
        long compareDuration = Long.valueOf(compareSession.dateCreated);
        /* For Ascending order*/
        return (int) (compareDuration - currentDuration);
    }

    @Override
    public String toString() {
        return duration;
    }
}