package com.visva.android.socialstackwidget.database;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class SocialWidgetItem implements Parcelable {
    private int _id;
    private String socialFeedId;
    private String social_category;
    private String image_url;
    private int image_height;
    private int image_width;
    private String author_name;
    private String author_image;
    private int author_image_width;
    private int author_image_height;
    private String time_stamp;
    private String link_url;

    private String feeds_type;
    private String message;
    public Bitmap mAuthorBitmap;
    public Bitmap mMainBitmap;

    public String body;
    public String webLink;

    /* contructor */
    public SocialWidgetItem() {

    }

    public SocialWidgetItem(String socialFeedId, String social_category, String image_url, int image_height, int image_width, String author_name, String author_image, int author_image_width, int author_image_height, String time_stamp, String feeds_type,
            String message, String link_url, String body, String webLink) {
        super();
        this.socialFeedId = socialFeedId;
        this.social_category = social_category;
        this.image_url = image_url;
        this.image_height = image_height;
        this.image_width = image_width;
        this.author_name = author_name;
        this.author_image = author_image;
        this.author_image_width = author_image_width;
        this.author_image_height = author_image_height;
        this.time_stamp = time_stamp;
        this.feeds_type = feeds_type;
        this.message = message;
        this.link_url = link_url;
        this.body = body;
        this.webLink = webLink;
    }

    public SocialWidgetItem(int id, String socialFeedId, String social_category, String image_url, int image_height, int image_width, String author_name, String author_image, int author_image_width, int author_image_height, String time_stamp, String feeds_type,
            String message, String link_url, String body, String webLink) {
        super();
        this._id = id;
        this.socialFeedId = socialFeedId;
        this.social_category = social_category;
        this.image_url = image_url;
        this.image_height = image_height;
        this.image_width = image_width;
        this.author_name = author_name;
        this.author_image = author_image;
        this.author_image_width = author_image_width;
        this.author_image_height = author_image_height;
        this.time_stamp = time_stamp;
        this.feeds_type = feeds_type;
        this.message = message;
        this.link_url = link_url;
        this.body = body;
        this.webLink = webLink;
    }

    public SocialWidgetItem(Parcel in) {
        // TODO Auto-generated constructor stub
        this.socialFeedId = in.readString();
        this.social_category = in.readString();
        this.image_url = in.readString();
        this.image_height = in.readInt();
        this.image_width = in.readInt();
        this.author_name = in.readString();
        this.author_image = in.readString();
        this.time_stamp = in.readString();
        this.link_url = in.readString();
        this.feeds_type = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(socialFeedId);
        out.writeString(social_category);
        out.writeString(image_url);
        out.writeInt(image_height);
        out.writeInt(image_width);
        out.writeString(author_name);
        out.writeString(author_image);
        out.writeString(time_stamp);
        out.writeString(link_url);
        out.writeString(feeds_type);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<SocialWidgetItem> CREATOR = new Parcelable.Creator<SocialWidgetItem>() {
        public SocialWidgetItem createFromParcel(Parcel in) {
            return new SocialWidgetItem(in);
        }

        public SocialWidgetItem[] newArray(int size) {
            return new SocialWidgetItem[size];
        }
    };

    public String getLink_url() {
        return link_url;
    }

    public String getSocialFeedId() {
        return socialFeedId;
    }

    public String getSocial_category() {
        return social_category;
    }

    public String getImage_Url() {
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

    public String getFeeds_type() {
        return feeds_type;
    }

    public String getMessage() {
        return message;
    }

    public void setAuthor_name(String authorName) {
        this.author_name = authorName;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getId() {
        return _id;
    }

    public void setId(int _id) {
        this._id = _id;
    }

    public String getWebLink() {
        return webLink;
    }
}
