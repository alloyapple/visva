package com.samsung.android.alwayssocial.service;

import android.os.Parcel;
import android.os.Parcelable;

public class CommentParcelableObject implements Parcelable{
    
    public String id;
    public String fromUserName;
    public String fromUserImageUrl;
    public String message;
    public String created_time;
    
    // Constructor
    public CommentParcelableObject(String _id, String _fromUserName, String _fromUserImageUrl, String _message, String _created_time){
        id = _id;
        fromUserName = _fromUserName;
        fromUserImageUrl = _fromUserImageUrl;
        message = _message;
        created_time = _created_time;
    }
    
    // Parcelling 
    public CommentParcelableObject(Parcel in){
        String[] data = new String[5];
        in.readStringArray(data);
        this.id = data[0];
        this.fromUserName = data[1];
        this.fromUserImageUrl = data[2];
        this.message = data[3];
        this.created_time = data[4];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {this.id, this.fromUserName, this.fromUserImageUrl, this.message, this.created_time});
    }
    
    public static final Parcelable.Creator<CommentParcelableObject> CREATOR = new Parcelable.Creator<CommentParcelableObject>() {
        public CommentParcelableObject createFromParcel(Parcel in) {
            return new CommentParcelableObject(in); 
        }

        public CommentParcelableObject[] newArray(int size) {
            return new CommentParcelableObject[size];
        }
    };

}
