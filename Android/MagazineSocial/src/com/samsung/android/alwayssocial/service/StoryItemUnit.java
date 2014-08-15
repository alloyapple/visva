package com.samsung.android.alwayssocial.service;

import android.os.Parcel;
import android.os.Parcelable;

public class StoryItemUnit implements Parcelable {
    /***
     * story item base for always database send to Magazine Home
     * @param id
     * @param streamId
     * @param category
     * @param appPackage
     * @param title
     * @param body
     * @param type
     * @param author
     * @param image
     * @param timeStamp
     * @param more
     * @param source
     * @param target
     */
    private int storyItemId;
    private String socialFeedId;
    // StreamIds.SAMSUNG_SOCIAL
    /** private String stream_id;*/
    // getPackageName()
    /** private String application_name;*/
    //social type(facebook,twitter,instargram...)
    private String social_category;
    //StoryItem.StoryType.ARTICLE
    /**private String type;*/
    private String title;
    private String body;
    private String image_url;
    private int image_height;
    private int image_width;
    private String author_name;
    private String author_image;
    private int author_image_width;
    private int author_image_height;
    private String time_stamp;
    private int more;
    private String source;
    private String target;
    private String link_url;
    private int isLiked;

    /**
     * always social data
     * 
     * 
     */
    private String feeds_type;
    private String feeds_type_detail;
    private int number_of_like = 0;
    private int number_of_comment = 0;
    private String update_time;
    private String message;
    private String next_page;
    private String objectId;

    /* contructor */
    public StoryItemUnit() {

    }

    public StoryItemUnit(String socialFeedId, String social_category, String title, String body, String body_url, int image_height, int image_width, String author_name, String author_image, int author_image_width, int author_image_height, String time_stamp, int more, String source, String target,
            String feeds_type, String feeds_type_detail, int number_of_like, int number_of_comment, String update_time, String message, String link_url, int isLiked, String next_page, String objectId) {
        super();
        this.socialFeedId = socialFeedId;
        this.social_category = social_category;
        this.title = title;
        this.body = body;
        this.image_url = body_url;
        this.image_height = image_height;
        this.image_width = image_width;
        this.author_name = author_name;
        this.author_image = author_image;
        this.author_image_width = author_image_width;
        this.author_image_height = author_image_height;
        this.time_stamp = time_stamp;
        this.more = more;
        this.source = source;
        this.target = target;
        this.feeds_type = feeds_type;
        this.feeds_type_detail = feeds_type_detail;
        this.number_of_like = number_of_like;
        this.number_of_comment = number_of_comment;
        this.update_time = update_time;
        this.message = message;
        this.link_url = link_url;
        this.isLiked = isLiked;
        this.next_page = next_page;
        this.objectId = objectId;
    }

    public StoryItemUnit(int storyItemId, String socialFeedId, String social_category, String title, String body, String body_url, int image_height, int image_width, String author_name, String author_image, int author_image_width, int author_image_height, String time_stamp, int more, String source,
            String target, String feeds_type, String feeds_type_detail, int number_of_like, int number_of_comment, String update_time, String message, String link_url, int isLiked, String next_page, String objectId) {
        super();
        this.storyItemId = storyItemId;
        this.socialFeedId = socialFeedId;
        this.social_category = social_category;
        this.title = title;
        this.body = body;
        this.image_url = body_url;
        this.image_height = image_height;
        this.image_width = image_width;
        this.author_name = author_name;
        this.author_image = author_image;
        this.author_image_width = author_image_width;
        this.author_image_height = author_image_height;
        this.time_stamp = time_stamp;
        this.more = more;
        this.source = source;
        this.target = target;
        this.feeds_type = feeds_type;
        this.feeds_type_detail = feeds_type_detail;
        this.number_of_like = number_of_like;
        this.number_of_comment = number_of_comment;
        this.update_time = update_time;
        this.message = message;
        this.link_url = link_url;
        this.isLiked = isLiked;
        this.next_page = next_page;
        this.objectId = objectId;
    }

    public StoryItemUnit(Parcel in) {
        // TODO Auto-generated constructor stub
        this.socialFeedId = in.readString();
        this.social_category = in.readString();
        this.title = in.readString();
        this.body = in.readString();
        this.image_url = in.readString();
        this.image_height = in.readInt();
        this.image_width = in.readInt();
        this.author_name = in.readString();
        this.author_image = in.readString();
        this.time_stamp = in.readString();
        this.link_url = in.readString();
        this.feeds_type = in.readString();
        this.feeds_type_detail = in.readString();
        this.isLiked = in.readInt();
        this.source = in.readString();
        this.target = in.readString();
        this.next_page = in.readString();
        this.number_of_like = in.readInt();
        this.number_of_comment = in.readInt();
        this.objectId = in.readString();
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        // TODO Auto-generated method stub
        out.writeString(socialFeedId);
        out.writeString(social_category);
        out.writeString(title);
        out.writeString(body);
        out.writeString(image_url);
        out.writeInt(image_height);
        out.writeInt(image_width);
        out.writeString(author_name);
        out.writeString(author_image);
        out.writeString(time_stamp);
        out.writeString(link_url);
        out.writeString(feeds_type);
        out.writeString(feeds_type_detail);
        out.writeInt(isLiked);
        out.writeString(source);
        out.writeString(target);
        out.writeString(next_page);
        out.writeInt(number_of_like);
        out.writeInt(number_of_comment);
        out.writeString(objectId);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<StoryItemUnit> CREATOR = new Parcelable.Creator<StoryItemUnit>() {
        public StoryItemUnit createFromParcel(Parcel in) {
            return new StoryItemUnit(in);
        }

        public StoryItemUnit[] newArray(int size) {
            return new StoryItemUnit[size];
        }
    };

    public String getLink_url() {
        return link_url;
    }

    public int getStoryItemId() {
        return storyItemId;
    }

    public String getSocialFeedId() {
        return socialFeedId;
    }

    public String getSocial_category() {
        return social_category;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getBody_url() {
        return image_url;
    }

    public int getImage_height() {
        return image_height;
    }

    public int getImage_width() {
        return image_width;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public String getAuthor_image() {
        return author_image;
    }

    public int getAuthor_image_width() {
        return author_image_width;
    }

    public int getAuthor_image_height() {
        return author_image_height;
    }

    public String getTime_stamp() {
        return time_stamp;
    }

    public int getMore() {
        return more;
    }

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public String getFeeds_type_detail() {
        return feeds_type_detail;
    }

    public int getNumber_of_like() {
        if (number_of_like < 0)
            return 0;
        return number_of_like;
    }

    public int getNumber_of_comment() {
        if (number_of_comment < 0)
            return 0;
        return number_of_comment;
    }

    public String getFeeds_type() {
        return feeds_type;
    }

    public String getMessage() {
        return message;
    }

    public void setNumber_of_like(int number_of_like) {
        this.number_of_like = number_of_like;
    }

    public void setNumber_of_comment(int number_of_comment) {
        this.number_of_comment = number_of_comment;
    }

    public int isLikedFeed() {
        return isLiked;
    }

    public void setIsLiked(int isLiked) {
        this.isLiked = isLiked;
    }

    public void setNextPage(String next_page) {
        this.next_page = next_page;
    }

    public String getNextPage() {
        return next_page;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public void setAuthor_name(String authorName) {
        this.author_name = authorName;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
