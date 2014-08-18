package com.sharebravo.bravo.model.parameters;

import java.util.List;

import org.apache.http.NameValuePair;

public class PaPutUser extends BasicParameter{
    String  fullName;
    String  timeZone;
    String  aboutMe;
    String  locale;
    String  password;
    int     badgeNum;
    boolean isMyListPrivate;
    String  profileImg;
    String  coverImg;
    String  apnsToken;
    int     profileImgDel;
    int     coverImgDel;

    public PaPutUser() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public List<NameValuePair> createNameValuePair() {
        // TODO Auto-generated method stub
        return null;
    }
}
