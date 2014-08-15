package com.sharebravo.bravo.model.response;

import java.util.ArrayList;

public class ObGetUserBlocking {
    ArrayList<User> data = new ArrayList<User>();

    public ObGetUserBlocking() {
        // TODO Auto-generated constructor stub
    }

}

class User {
    String         userID;
    ArrayList<SNS> snsList = new ArrayList<SNS>();
    String         fullName;
    String         profileImgUrl;

    public User() {
        // TODO Auto-generated constructor stub
    }
}