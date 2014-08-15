package com.samsung.android.alwayssocial.object.twitter;


import twitter4j.ResponseList;
import twitter4j.UserList;

import com.samsung.android.alwayssocial.object.ResponseBase;

public class TwitterListData extends ResponseBase {
    private ResponseList<UserList> mListUserList;
    
    public TwitterListData(){
    }
    
    public TwitterListData(ResponseList<UserList> userList) {
        this.mListUserList = (ResponseList<UserList>) userList;
    }

    public ResponseList<UserList> getListUserList() {
        return mListUserList;
    }

    public void setListUserList(ResponseList<UserList> mListUserList) {
        this.mListUserList = mListUserList;
    }
    
    
}
