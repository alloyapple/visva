package com.samsung.android.alwayssocial.object.twitter;

import twitter4j.Status;

import com.samsung.android.alwayssocial.object.ResponseBase;

public class TwitterStatusData extends ResponseBase {
    private Status mStatus;
    
    public TwitterStatusData(){
    
    }
    
    public TwitterStatusData(Status status) {
        this.mStatus = status;
    }

    public Status getStatus() {
        return mStatus;
    }

    public void setStatus(Status mStatus) {
        this.mStatus = mStatus;
    }
    
}
