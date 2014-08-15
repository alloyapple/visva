package com.samsung.android.alwayssocial.story;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import sstream_new.lib.InvalidStoryItemException;
import sstream_new.lib.SStreamContentManager;
import sstream_new.lib.objs.StoryItem;

import android.content.Context;
import android.util.Log;

import com.samsung.android.alwayssocial.constant.FacebookConstant;
import com.samsung.android.alwayssocial.object.facebook.FacebookFeedWrapper;
import com.samsung.android.alwayssocial.object.facebook.FacebookPhoto;

public class StoryHomeConnector {
    private static final String TAG = "StoryHomeConnector";

    //**************************************************************
    // Send broadcast to Magazine Home
    //**************************************************************
    public static void putDataSNSToMagazineHome(Context context, ArrayList<FacebookFeedWrapper> feeds, HashMap<String, FacebookPhoto> photos) {
        // TODO Auto-generated method stub
        Log.d(TAG, "putDataSNSToMagazineHome " + context);
        List<StoryItem> storyItems = Collections.synchronizedList(new ArrayList<StoryItem>());
        for (FacebookFeedWrapper feed : feeds) {

            //            // TODO : Mapping Feed to StoryItem per Feed type.
            //            //        Below StoryItem is just dummy mapping.
            //
            //            //                 public StoryItem(            String id,           String streamId,          String category , 
            //            StoryItem storyItem = new StoryItem("facebook-" + feed.getId(), StreamIds.SAMSUNG_SOCIAL, GlobalConfig.SOCIAL_TYPE_FACEBOOK,
            //                    // String appPackage,         String title,       String body,                       StoryType type,              
            //                    getPackageName().toString(), "title", feed.getMessage() + feed.getLink(), StoryItem.StoryType.ARTICLE,
            //                    // Author author,                                     Image image,                        long timeStamp,                int more, String source , String target)
            //                    new Author(feed.getFrom().getName(), /* LoadImage(feed.getFrom().getId())*/null), new Image(feed.getPicture(), 200, 200, ""), 0, 0, null, null);
            //            storyItems.add(storyItem);
            StoryItem storyItem = StoryConverter.convertDBStoryItemUnitToMHStoryItem(StoryConverter.convertFBFeedToDBStoryItemUnit(FacebookConstant.FB_DATA_TYPE_FEED, feed, photos,""));
            storyItems.add(storyItem);
        }
        try {
            Log.d(TAG, "call sendStoryItems");
            SStreamContentManager.sendStoryItems(context, storyItems);
        } catch (InvalidStoryItemException e) {
            Log.d(TAG, "call Error");
            e.printStackTrace();
        }
    }
}
