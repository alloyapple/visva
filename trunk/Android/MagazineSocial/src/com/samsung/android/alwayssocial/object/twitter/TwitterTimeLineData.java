package com.samsung.android.alwayssocial.object.twitter;

import java.util.List;

import com.samsung.android.alwayssocial.object.ResponseBase;

import twitter4j.Status;

public class TwitterTimeLineData extends ResponseBase {
    private List<Status> mStatuses;

    public TwitterTimeLineData(){}
    
    public TwitterTimeLineData(List<Status> statuses){
        this.mStatuses = statuses;
    }
    
    public List<Status> getStatuses() {
        return mStatuses;
    }

    public void setStatuses(List<Status> mStatuses) {
        this.mStatuses = mStatuses;
    }
    
    
}
