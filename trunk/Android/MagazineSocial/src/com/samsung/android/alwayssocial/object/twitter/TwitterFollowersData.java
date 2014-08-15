package com.samsung.android.alwayssocial.object.twitter;

import java.util.List;

import com.samsung.android.alwayssocial.object.ResponseBase;

import twitter4j.PagableResponseList;
import twitter4j.User;

public class TwitterFollowersData  extends ResponseBase{
    
    private List<User> followers;

    public TwitterFollowersData( List<User> followers) {
        this.followers = followers;
    }

    public TwitterFollowersData() {
    }

    public List<User> getFollowers() {
        return followers;
    }

    public void setFollowers(PagableResponseList<User> followers) {
        this.followers = followers;
    }
    

  

}
