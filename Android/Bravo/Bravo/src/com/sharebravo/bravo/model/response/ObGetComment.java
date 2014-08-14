package com.sharebravo.bravo.model.response;

import java.util.ArrayList;

public class ObGetComment {
    String           commentID;
    String           bravoID;
    String           userID;
    String           fullName;
    String           profileImgUrl;
    ArrayList<SNSID> snsList     = new ArrayList<SNSID>();
    String           commentText;
    DateObject       dateCreated = new DateObject();

    public ObGetComment() {
        // TODO Auto-generated constructor stub
    }

}
