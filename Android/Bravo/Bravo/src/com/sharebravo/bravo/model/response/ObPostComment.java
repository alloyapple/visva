package com.sharebravo.bravo.model.response;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class ObPostComment {
    @SerializedName("data")
    public ArrayList<CommentID> data;
    @SerializedName("status")
    public int               status;
    @SerializedName("error")
    public String            error;

    public ObPostComment() {
        // TODO Auto-generated constructor stub
    }

    public class CommentID {
        @SerializedName("Comment_ID")
        public String commentID;
    }
}
