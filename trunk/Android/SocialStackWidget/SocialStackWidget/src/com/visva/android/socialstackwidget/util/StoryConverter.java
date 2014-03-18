package com.visva.android.socialstackwidget.util;

import com.visva.android.socialstackwidget.constant.GlobalContstant;
import com.visva.android.socialstackwidget.database.SocialWidgetItem;
import com.visva.android.socialstackwidget.object.facebookobject.FacebookFeedWrapperObject;

public class StoryConverter {

    public static SocialWidgetItem convertFBFeedToDBStoryItemUnit(FacebookFeedWrapperObject feed) {

        int imageHeight = 200;
        int imageWidth = 200;
        String linkPicture = feed.getPicture();
        if (linkPicture == null || linkPicture == "") {
            linkPicture = feed.getIcon();
        }
        String linkUrl = "";
        if (null != feed.actions && feed.actions.size() > 0)
            linkUrl = feed.actions.get(0).link;
        String body = feed.getDescription() + GlobalContstant.SUB_STRING_CODE + feed.getCaption() + GlobalContstant.SUB_STRING_CODE + feed.getMessage();
        SocialWidgetItem stackWidgetTableUnit = new SocialWidgetItem(feed.getId(), GlobalContstant.FACEBOOK, linkPicture, imageHeight, imageWidth, feed.getFrom().getName(), feed.getFrom().getId(), 50, 50, feed.getCreated_time(), feed.getType().toString(), feed.getMessage(), feed.getLink(), body,
                linkUrl);
        return stackWidgetTableUnit;
    }
}
