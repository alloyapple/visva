package com.sharebravo.bravo.model.response;

import java.util.ArrayList;

public class ObGetUserSearch {
    ArrayList<UserSearch> data = new ArrayList<UserSearch>();
    public ObGetUserSearch() {
        // TODO Auto-generated constructor stub
    }
}

class UserSearch {
    String         userID;
    ArrayList<SNS> snsList = new ArrayList<SNS>();
    String         fullName;
    String         profileImgUrl;
    boolean        isBlocking;
    boolean        isFollowing;
    boolean        isFollowed;

    public UserSearch() {
        // TODO Auto-generated constructor stub
    }
}
