package com.samsung.android.alwayssocial.story;

import java.util.HashMap;
import sstream_new.lib.constants.StreamIds;
import sstream_new.lib.objs.Author;
import sstream_new.lib.objs.Image;
import sstream_new.lib.objs.StoryItem;
import twitter4j.MediaEntity;
import twitter4j.SavedSearch;
import twitter4j.Status;
import twitter4j.UserList;
import twitter4j.UserMentionEntity;
import android.util.Log;

import com.samsung.android.alwayssocial.app.AlwaysSocialAppImpl;
import com.samsung.android.alwayssocial.constant.GlobalConstant;
import com.samsung.android.alwayssocial.constant.TwitterConstant;
import com.samsung.android.alwayssocial.object.facebook.FacebookFeedWrapper;
import com.samsung.android.alwayssocial.object.facebook.FacebookFeedWrapper.TypeTimeline;
import com.samsung.android.alwayssocial.object.facebook.FacebookFeedWrapper.User;
import com.samsung.android.alwayssocial.object.facebook.FacebookPage;
import com.samsung.android.alwayssocial.object.facebook.FacebookPhoto;
import com.samsung.android.alwayssocial.object.facebook.FacebookUser;
import com.samsung.android.alwayssocial.service.StoryItemUnit;

public class StoryConverter {

    /*convert storyItem Unit from database to feed*/
    public static FacebookFeedWrapper convertDBStoryItemUnitToFeed(StoryItemUnit storyItemUnit) {

        FacebookFeedWrapper feed = new FacebookFeedWrapper();
        feed.setId(storyItemUnit.getSocialFeedId());
        feed.setCreated_time(storyItemUnit.getTime_stamp());
        String body[] = storyItemUnit.getBody().split(GlobalConstant.SUB_STRING_CODE);
        if (body.length > 2)
            storyItemUnit.setBody(body[2]);
        if ("".equals(storyItemUnit.getBody()) && body.length > 1)
            storyItemUnit.setBody(body[1]);
        if ("".equals(storyItemUnit.getBody()) && body.length > 0)
            storyItemUnit.setBody(body[0]);
        //set User
        User user = new User();
        String name[] = storyItemUnit.getAuthor_name().split(GlobalConstant.SUB_STRING_CODE);
        if (name.length > 0)
            storyItemUnit.setAuthor_name(name[0]);
        if ("".equals(storyItemUnit.getAuthor_name()) && name.length > 1)
            storyItemUnit.setAuthor_name(name[1]);
        if ("".equals(storyItemUnit.getAuthor_name()) && name.length > 2)
            storyItemUnit.setAuthor_name(name[2]);

        String message[] = storyItemUnit.getMessage().split(GlobalConstant.SUB_STRING_CODE);
        if (message.length > 2)
            storyItemUnit.setMessage(message[2]);
        if ("".equals(storyItemUnit.getMessage()) && name.length > 1)
            storyItemUnit.setMessage(message[1]);
        if ("".equals(storyItemUnit.getMessage()) && name.length > 0)
            storyItemUnit.setMessage(message[0]);

        user.setId("");
        user.setCategory("");
        feed.setFrom(user);
        Log.e("StoryConverter", "StoryConvertername " + storyItemUnit.getAuthor_name() + " body " + storyItemUnit.getBody() + " message " + storyItemUnit.getMessage());
        feed.setIcon(storyItemUnit.getAuthor_image());
        feed.setDescription(storyItemUnit.getBody());
        feed.setLink(storyItemUnit.getBody_url());
        feed.setMessage(storyItemUnit.getMessage());
        feed.setName(storyItemUnit.getAuthor_name());
        feed.setPicture(storyItemUnit.getBody_url());
        feed.setSource(storyItemUnit.getSource());
        feed.setStatus_type(storyItemUnit.getFeeds_type_detail());
        feed.setType(storyItemUnit.getFeeds_type_detail());
        feed.setUpdated_time(storyItemUnit.getUpdate_time());

        feed.setObject_id(storyItemUnit.getObjectId());

        return feed;
    }

    /*convert feed to story item unit to save to database*/
    public static StoryItemUnit convertFBFeedToDBStoryItemUnit(int requestType, FacebookFeedWrapper feed, HashMap<String, FacebookPhoto> photos, String nextPage) {

        int imageHeight = 200;
        int imageWidth = 200;
        String linkPicture = feed.getPicture();
        if (linkPicture == null || linkPicture == "") {
            linkPicture = feed.getIcon();
        }

        if (photos != null && photos.containsKey(feed.getId())) {
            if (photos.get(feed.getId()) != null) {
                linkPicture = photos.get(feed.getId()).getSource();
                imageHeight = Integer.parseInt(photos.get(feed.getId()).getHeight());
                imageWidth = Integer.parseInt(photos.get(feed.getId()).getWidth());
            }
        }

        FacebookFeedWrapper.TypeTimeline typeTimeline = feed.getType();
        String objectId = "";
        if (typeTimeline == TypeTimeline.PHOTO) {
            objectId = feed.getObject_id();
        }

        StoryItemUnit storyItemUnit = new StoryItemUnit(feed.getId(), GlobalConstant.SOCIAL_TYPE_FACEBOOK, feed.getName() + GlobalConstant.SUB_STRING_CODE + feed.getCaption(), feed.getDescription() + GlobalConstant.SUB_STRING_CODE + feed.getCaption() + GlobalConstant.SUB_STRING_CODE
                + feed.getMessage(), linkPicture, imageHeight, imageWidth, feed.getFrom().getName(),
                feed.getFrom().getId(), 50, 50, feed.getCreated_time(), 0, "", "", "" + requestType, feed.getType().toString(), feed.mLikeCount, feed.mCommentCount, feed.getUpdated_time(), feed.getMessage(), feed.getLink(), 0, nextPage, objectId);
        return storyItemUnit;
    }

    /*convert facebook likes page,friend albumn,groups to story item unit to save to database*/
    public static StoryItemUnit convertFBPages_Groups_FriendGroups(int requestType, FacebookPage pages, String nextPage) {

        String message = String.valueOf(pages.getLikes());
        if ("".equals(message) || Integer.valueOf(message) == 0)
            message = pages.getDescription();
        if ("".equals(message))
            message = pages.getList_type();
        StoryItemUnit storyItemUnit = new StoryItemUnit(pages.getId(), GlobalConstant.SOCIAL_TYPE_FACEBOOK, pages.getName(), pages.getDescription(), "", 0, 0, pages.getName(),
                pages.getId(), 50, 50, pages.getCreated_time(), 0, "", "", "" + requestType, "", pages.getLikes(), 0, "", message, "", 0, nextPage, "");
        return storyItemUnit;

    }

    /*convert facebook friend to story item unit to save to database*/
    public static StoryItemUnit convertFBFriend(int requestType, FacebookUser user) {
        StoryItemUnit storyItemUnit = new StoryItemUnit(user.id, GlobalConstant.SOCIAL_TYPE_FACEBOOK, "", "", "", 0, 0, user.name, user.picture.data.url, 50, 50, "", 0, "", "", "" + requestType, "", 0, 0, "", "", "", 0, "", "");
        return storyItemUnit;
    }

    public static StoryItemUnit convertFBMember(int requestType, FacebookUser user) {
        StoryItemUnit storyItemUnit = new StoryItemUnit(user.id, GlobalConstant.SOCIAL_TYPE_FACEBOOK, "", "", "", 0, 0, user.name, user.id, 50, 50, "", 0, "", "", "" + requestType, "", 0, 0, "", "", "", 0, "", "");
        return storyItemUnit;
    }

    /*convert facebook tagged me, photo list to story item unit to save to database*/
    public static StoryItemUnit convertFBTaggedMe(int requestType, FacebookPhoto photo_taggedMe, String nextPage) {
        StoryItemUnit storyItemUnit = new StoryItemUnit(photo_taggedMe.getId(), GlobalConstant.SOCIAL_TYPE_FACEBOOK, photo_taggedMe.getCaption() + photo_taggedMe.getName(), photo_taggedMe.getDescription() + photo_taggedMe.getCaption(), photo_taggedMe.getPicture()
                , 200, 200, photo_taggedMe.getFrom().getName(), photo_taggedMe.getFrom().getId(), 50, 50, photo_taggedMe.getCreated_time(), 0, "", "", "" + requestType, photo_taggedMe.getType().toString(), photo_taggedMe.mLikeCount, photo_taggedMe.mCommentCount,
                photo_taggedMe.getUpdated_time(), photo_taggedMe.getMessage(), photo_taggedMe.getLink(), 0, nextPage, "");
        return storyItemUnit;
    }

    /*convert story item unit from database to send to magazine home*/
    public static StoryItem convertDBStoryItemUnitToMHStoryItem(StoryItemUnit storyItemUnit) {
        String body[] = storyItemUnit.getBody().split(GlobalConstant.SUB_STRING_CODE);
        if (body.length > 2)
            storyItemUnit.setBody(body[2]);
        if ("".equals(storyItemUnit.getBody()) && body.length > 1)
            storyItemUnit.setBody(body[1]);
        if ("".equals(storyItemUnit.getBody()) && body.length > 0)
            storyItemUnit.setBody(body[0]);
        Log.e("convertDBStoryItemUnitToMHStoryItem", "convertDBStoryItemUnitToMHStoryItem "+storyItemUnit.getTitle()+" name "+storyItemUnit.getAuthor_name());
//        //set User
//        User user = new User();
//        String name[] = storyItemUnit.getAuthor_name().split(GlobalConstant.SUB_STRING_CODE);
//        if (name.length > 0)
//            storyItemUnit.setAuthor_name(name[0]);
//        if ("".equals(storyItemUnit.getAuthor_name()) && name.length > 1)
//            storyItemUnit.setAuthor_name(name[1]);
//        if ("".equals(storyItemUnit.getAuthor_name()) && name.length > 2)
//            storyItemUnit.setAuthor_name(name[2]);
//
//        String message[] = storyItemUnit.getMessage().split(GlobalConstant.SUB_STRING_CODE);
//        if (message.length > 2)
//            storyItemUnit.setMessage(message[2]);
//        if ("".equals(storyItemUnit.getMessage()) && name.length > 1)
//            storyItemUnit.setMessage(message[1]);
//        if ("".equals(storyItemUnit.getMessage()) && name.length > 0)
//            storyItemUnit.setMessage(message[0]);

        String title[] = storyItemUnit.getTitle().split(GlobalConstant.SUB_STRING_CODE);
        if (title.length > 0)
            storyItemUnit.setTitle(title[0]);
        if ("".equals(storyItemUnit.getTitle()) && title.length > 1)
            storyItemUnit.setTitle(title[1]);
        if ("".equals(storyItemUnit.getTitle()) && title.length > 2)
            storyItemUnit.setTitle(title[2]);
         
        StoryItem storyItemMH = new StoryItem("facebook-" + storyItemUnit.getSocialFeedId(), StreamIds.SAMSUNG_SOCIAL, GlobalConstant.SOCIAL_TYPE_FACEBOOK,
                // String appPackage,         String title,       String body,                       StoryType type,               Author author,  
                getPackageName(), storyItemUnit.getTitle(), storyItemUnit.getBody(), StoryItem.StoryType.ARTICLE, new Author(storyItemUnit.getAuthor_name(), new Image(storyItemUnit.getAuthor_image(),
                        //                                   Image image,                        long timeStamp,                int more, String source , String target)
                        storyItemUnit.getAuthor_image_width(), storyItemUnit.getAuthor_image_height(), "")), new Image(storyItemUnit.getBody_url(), storyItemUnit.getImage_width(), storyItemUnit.getImage_height(), ""), 0,
                0, null, null);
        return storyItemMH;
    }

    public static String getPackageName(){
        return AlwaysSocialAppImpl.getInstance().getPackageName();
    }

    public static StoryItemUnit convertTWTimeLineToDBStoryItemUnit(int feedType, Status status, String nextPage) {
        // TODO Auto-generated method stub
        Log.d(getPackageName(), "convertTWTimeLineToDBStoryItemUnit " + feedType);
        int imageWidth = 200;
        int imageHeght = 200;
        if (status.getMediaEntities().length > 0) {
            imageWidth = status.getMediaEntities()[0].getSizes().get(MediaEntity.Size.LARGE).getWidth();
            imageHeght = status.getMediaEntities()[0].getSizes().get(MediaEntity.Size.LARGE).getHeight();
        }
        int feed_type_detail;
        String title = "";
        if (status.getHashtagEntities().length > 0){
            title = status.getHashtagEntities()[0].getText();
        }
        String image_url;
        if (status.getMediaEntities().length == 0) {
            feed_type_detail = TwitterConstant.TW_DISPLAY_TYPE_NO_MEDIA_ENTITIES;
            image_url = "";
        } else {
            feed_type_detail = TwitterConstant.TW_DISPLAY_TYPE_MEDIA_ENTITIES;
            image_url = status.getMediaEntities()[0].getMediaURL();
        }
        String link_url = "";
        if (status.getURLEntities().length > 0) {
            link_url = status.getURLEntities()[0].getExpandedURL();
        } else {
            //            link_url = status.getUser().getURL();
        }
        int isLiked = 0;
        if (status.isFavorited()){
            isLiked = 1;
        }
        /*[Twitter] we dont want to create more field in database, so in order to save list of mentioned users (getUserMentionEntities), we save it in an array and separate each item with a code
         * => save in source value in StoryItemUnit*/
        /*string Target will save the value of getInReplyScreenName*/

        UserMentionEntity[] mentionEntity = status.getUserMentionEntities();
        String userMentionArray = "";
        if (mentionEntity.length > 0) {
            for (UserMentionEntity user : mentionEntity) {
                userMentionArray += user.getScreenName() + GlobalConstant.SUB_STRING_CODE;
            }
        }

        String inReplyName = status.getInReplyToScreenName();
        StoryItemUnit storyItemUnit = new StoryItemUnit(Long.toString(status.getId()), GlobalConstant.SOCIAL_TYPE_TWITTER, title, status.getText(), image_url, imageHeght, imageWidth, status.getUser().getScreenName(), status.getUser().getProfileImageURL(), 50, 50, status
                .getCreatedAt().toString(), 0, userMentionArray, inReplyName, "" + feedType, "" + feed_type_detail, status.getFavoriteCount(), status.getRetweetCount(), "", status.getUser().getName(), link_url, isLiked, nextPage, "");
        return storyItemUnit;
    }

    public static StoryItemUnit convertTWSaveSearchToDBStoryItemUnit(int feedType, SavedSearch savedSearch, String nextPage) {
        // TODO Auto-generated method stub
        StoryItemUnit storyItemUnit = new StoryItemUnit(Long.toString(savedSearch.getId()), GlobalConstant.SOCIAL_TYPE_TWITTER, savedSearch.getName(), "", "", 0, 0, "", "", 50, 50, "", 0, "", "", "" + feedType, "", 0, 0, "", "", "", 0, nextPage, "");
        return storyItemUnit;
    }

    public static StoryItemUnit convertTWTypeListToDBStoryItemUnit(int feedType, UserList userList, String nextPage) {
        // TODO Auto-generated method stub
        String body;
        if (userList.getSubscriberCount() == 0) {
            body = userList.getDescription();
        } else {
            body = userList.getSubscriberCount() + " subscribers";
        }

        StoryItemUnit storyItemUnit = new StoryItemUnit(Long.toString(userList.getId()), GlobalConstant.SOCIAL_TYPE_TWITTER, userList.getName() + " by " + userList.getUser().getScreenName(), body, "", 0, 0, "", "", 50, 50, "", 0, "", "", "" + feedType, "", 0, 0, "", "", "", 0, nextPage, "");
        return storyItemUnit;
    }

    public static StoryItemUnit convertTWPeopleFollowListToDBStoryItemUnit(int feedType, twitter4j.User follower) {
        // TODO Auto-generated method stub
        String body;
        if (follower.getStatusesCount() == 0) {
            body = follower.getDescription();
        } else {
            body = follower.getStatusesCount() + " subscribers";
        }
        StoryItemUnit storyItemUnit = new StoryItemUnit(Long.toString(follower.getId()), GlobalConstant.SOCIAL_TYPE_TWITTER, follower.getScreenName(), body, "", 0, 0, "", "", 50, 50, "", 0, "", "", "" + feedType, "", follower.getFollowersCount(), 0, "", "", "", 0, "", "");
        return storyItemUnit;
    }
}
