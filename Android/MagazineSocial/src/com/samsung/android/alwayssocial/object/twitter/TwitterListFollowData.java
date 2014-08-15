package com.samsung.android.alwayssocial.object.twitter;

import twitter4j.PagableResponseList;
import twitter4j.UserList;

import com.samsung.android.alwayssocial.object.ResponseBase;

public class TwitterListFollowData extends ResponseBase{
    PagableResponseList<UserList> mlstUserListFollow;

    public TwitterListFollowData(){
        
    }
    
    public TwitterListFollowData (PagableResponseList<UserList> lstUserList) {
        this.mlstUserListFollow = lstUserList;
    }
    
    public PagableResponseList<UserList> getlstUserListFollow() {
        return mlstUserListFollow;
    }

    public void setlstUserListFollow(PagableResponseList<UserList> mlstUserListFollow) {
        this.mlstUserListFollow = mlstUserListFollow;
    }
    
    
}
