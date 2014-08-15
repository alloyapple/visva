package com.samsung.android.alwayssocial.object.facebook;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;
import com.samsung.android.alwayssocial.object.ResponseBase;

public class FacebookFriends extends ResponseBase {
    @SerializedName("data")
    public ArrayList<FacebookUser> mFriendList;

    public FacebookFriends()
    {
        mFriendList = new ArrayList<FacebookUser>();
    }

    public FacebookFriends(ArrayList<FacebookUser> friends) {
        // TODO Auto-generated constructor stub
        mFriendList = friends;
    }
}
