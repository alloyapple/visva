package com.visva.voicerecorder.record;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * A class represent for a recorded session
 */
public class RecordingSession extends Object implements Parcelable {
    public String phoneNo;
    public String dateCreated;
    public int    callState;
    public String fileName;
    public String phoneName;

    public RecordingSession(String fileName, String phoneNo, String dateCreated, int callState,String phoneName) {
        this.phoneNo = phoneNo;
        this.dateCreated = dateCreated;
        this.callState = callState;
        this.fileName = fileName;
        this.phoneName = phoneName;
    }

    public RecordingSession(Parcel in) {
        this.phoneNo = in.readString();
        this.dateCreated = in.readString();
        this.callState = in.readInt();
        this.fileName = in.readString();
        this.phoneName = in.readString();
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
    }

    public static final Parcelable.Creator<RecordingSession> CREATOR = new Parcelable.Creator<RecordingSession>() {
                                                                         public RecordingSession createFromParcel(Parcel in) {
                                                                             return new RecordingSession(in);
                                                                         }

                                                                         public RecordingSession[] newArray(int size) {
                                                                             return new RecordingSession[size];
                                                                         }
                                                                     };
}