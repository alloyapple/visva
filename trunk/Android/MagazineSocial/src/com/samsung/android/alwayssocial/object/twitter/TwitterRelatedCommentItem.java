package com.samsung.android.alwayssocial.object.twitter;

public class TwitterRelatedCommentItem {

    private String authorOfComment;
    private String imageUrl;
    private String pubTime;
    private String contentOfComment;

    public TwitterRelatedCommentItem(String authorOfComment, String imageUrl, String pubTime, String contentOfComment) {
        super();
        this.authorOfComment = authorOfComment;
        this.imageUrl = imageUrl;
        this.pubTime = pubTime;
        this.contentOfComment = contentOfComment;
    }

    public String getAuthorOfComment() {
        return authorOfComment;
    }

    public void setAuthorOfComment(String authorOfComment) {
        this.authorOfComment = authorOfComment;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPubTime() {
        return pubTime;
    }

    public void setPubTime(String pubTime) {
        this.pubTime = pubTime;
    }

    public String getContentOfComment() {
        return contentOfComment;
    }

    public void setContentOfComment(String contentOfComment) {
        this.contentOfComment = contentOfComment;
    }

}
