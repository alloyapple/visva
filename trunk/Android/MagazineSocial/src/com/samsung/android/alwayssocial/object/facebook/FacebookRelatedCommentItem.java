package com.samsung.android.alwayssocial.object.facebook;

public class FacebookRelatedCommentItem {
    private String mAuthorOfComment;
    private String mImageUrl;
    private String mPubTime;
    private String mContentOfComment;

    public FacebookRelatedCommentItem() {

    }

    public FacebookRelatedCommentItem(String authorOfComment, String imageURL, String pubTime, String contentComment) {
        // TODO Auto-generated constructor stub
        mAuthorOfComment = authorOfComment;
        mImageUrl = imageURL;
        mPubTime = pubTime;
        mContentOfComment = contentComment;
    }

    public String getContentOfComment()
    {
        return mContentOfComment;
    }

    public String getAuthorOfComment()
    {
        return mAuthorOfComment;
    }

    public String getImageUrl()
    {
        return mImageUrl;
    }

    public String getPubTime()
    {
        return mPubTime;
    }

    public void setContentOfComment(String comment)
    {
        mContentOfComment = comment;
    }

    public void setAuthorOfComment(String authorOfComment)
    {
        mAuthorOfComment = authorOfComment;
    }

    public void setImageURL(String imageURL)
    {
        mImageUrl = imageURL;
    }

    public void setPubTime(String pubTime)
    {
        mPubTime = pubTime;
    }
}
