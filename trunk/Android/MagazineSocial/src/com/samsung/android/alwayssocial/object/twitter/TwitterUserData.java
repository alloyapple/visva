package com.samsung.android.alwayssocial.object.twitter;

import twitter4j.User;

import com.samsung.android.alwayssocial.object.ResponseBase;

public class TwitterUserData extends ResponseBase {
    private User mUser;
    public TwitterUserData() {
        
    }
    public TwitterUserData (User user) {
        mUser = user;
    }
    
    public User getUser() {
        return this.mUser;
    }
    
    public void setUser (User user) {
        this.mUser = user;
    }
}
