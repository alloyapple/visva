package com.samsung.android.alwayssocial.adapter;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.aphidmobile.utils.UI;
import com.facebook.widget.ProfilePictureView;
import com.samsung.android.alwayssocial.R;
import com.samsung.android.alwayssocial.constant.FacebookConstant;
import com.samsung.android.alwayssocial.constant.GlobalConstant;
import com.samsung.android.alwayssocial.object.facebook.FacebookFeedWrapper;
import com.samsung.android.alwayssocial.object.facebook.FacebookFeedWrapper.TypeTimeline;
import com.samsung.android.alwayssocial.service.StoryItemUnit;
import com.samsung.android.alwayssocial.util.Utils;
import com.samsung.android.alwayssocial.util.VolleySingleton;

public class FacebookFlipFeedAdapter extends ArrayAdapter<StoryItemUnit> {

    ArrayList<StoryItemUnit> mTimelineList;
    LayoutInflater mInflater;
    ImageLoader mImageLoader;

    ILikeCommentClick mILikeCommentClick;
    //private int mScreenWidth;
    //private int mScreenHeight;

    public FacebookFlipFeedAdapter(Context context, List<StoryItemUnit> objects) {
        super(context, -1, objects);
        mInflater = LayoutInflater.from(context);
        mTimelineList = (ArrayList<StoryItemUnit>) objects;

        //WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        //mScreenWidth = windowManager.getDefaultDisplay().getWidth();
        //mScreenHeight = windowManager.getDefaultDisplay().getHeight();

        //this.mImageLoader = new ImageLoader(context, mScreenWidth, mScreenHeight);
        mImageLoader = VolleySingleton.getInstance(context).getImageLoader();
    }

    public void updateFolderList(ArrayList<StoryItemUnit> list) {
        this.mTimelineList = list;
        notifyDataSetChanged();
    }

    @Override
    public StoryItemUnit getItem(int position) {
        return this.mTimelineList.get(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.timeline_flip_item, null);
            holder.mLayoutHeader = UI.<View> findViewById(convertView, R.id.layout_header);
            holder.mLayoutPhoto = UI.<View> findViewById(convertView, R.id.layout_photo);
            holder.mImagePhoto = UI.<NetworkImageView> findViewById(convertView, R.id.image_photo);
            holder.mLayoutMedia = UI.<RelativeLayout> findViewById(convertView, R.id.layout_media);
            holder.mTextUserNameFrom = UI.<TextView> findViewById(convertView, R.id.username);
            holder.mPostedDate = UI.<TextView> findViewById(convertView, R.id.posted_days);
            holder.mTextContent = UI.<TextView> findViewById(convertView, R.id.text_content);
            holder.mLikeCount = UI.<TextView> findViewById(convertView, R.id.text_like);
            holder.mCommentCount = UI.<TextView> findViewById(convertView, R.id.text_comment);
            holder.mImageContent = UI.<NetworkImageView> findViewById(convertView, R.id.media_summary);
            holder.mPlayButton = UI.<View> findViewById(convertView, R.id.video_play_button);
            //            holder.mMediaCaption = UI.<TextView> findViewById(convertView, R.id.media_capion);
            holder.mUserAvatar = UI.<ProfilePictureView> findViewById(convertView, R.id.user_avatar);
            holder.mTextMessage = UI.<TextView> findViewById(convertView, R.id.text_message_content);
            // link layout
            holder.mLayoutLink = UI.<View> findViewById(convertView, R.id.layout_link);
            holder.mLinkAvatar = UI.<NetworkImageView> findViewById(convertView, R.id.link_avatar);
            holder.mLinkText = UI.<TextView> findViewById(convertView, R.id.link_text);
            holder.mLinkHeader = UI.<TextView> findViewById(convertView, R.id.link_header);
            holder.mTextLinkUrl = UI.<TextView> findViewById(convertView, R.id.text_link_url);

            //layout photo
            holder.mTextPhotoContent = UI.<TextView> findViewById(convertView, R.id.text_photo_content);
            holder.mTextPhotoHeader = UI.<TextView> findViewById(convertView, R.id.text_photo_header);

            //layput video
            holder.mTextVideoContent = UI.<TextView> findViewById(convertView, R.id.text_video_content);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final StoryItemUnit item = mTimelineList.get(position);

        holder.mLinkHeader.setText("Link");
        //        holder.mMediaCaption.setVisibility(View.GONE);

        String content[] = item.getBody().split(GlobalConstant.SUB_STRING_CODE);
        if (content.length > 2) {
            holder.mTextMessage.setText(content[2]);
            holder.mLinkText.setText(content[2]);
        } else {
            holder.mLinkText.setText("");
            holder.mTextMessage.setText("");
        }
        if (content.length > 1) {
            holder.mTextVideoContent.setText(content[1]);
        } else {
            holder.mTextVideoContent.setText("");
        }
        if (content.length > 0)
            holder.mTextVideoContent.setText(holder.mTextVideoContent.getText() + "\n" + content[0]);
        holder.mTextPhotoContent.setText("wwww.facebook.com");
        holder.mTextPhotoHeader.setText(item.getAuthor_name());
        holder.mTextContent.setText(item.getMessage());
        // display content
        FacebookFeedWrapper.TypeTimeline timelineType = FacebookFeedWrapper.TypeTimeline.valueOf(item.getFeeds_type_detail());
        switch (timelineType) {
        case LINK:
            holder.mLayoutPhoto.setVisibility(View.GONE);
            holder.mLayoutHeader.setVisibility(View.VISIBLE);
            holder.mLayoutMedia.setVisibility(View.GONE);
            holder.mLayoutLink.setVisibility(View.VISIBLE);

            holder.mTextLinkUrl.setSelected(true);
            holder.mTextLinkUrl.setVisibility(View.VISIBLE);
            holder.mTextContent.setVisibility(View.GONE);
            holder.mImageContent.setVisibility(View.GONE);
            holder.mTextMessage.setVisibility(View.GONE);

            holder.mLinkAvatar.setVisibility(View.VISIBLE);
            holder.mTextLinkUrl.setText(item.getLink_url());
            //mImageLoader.displayImage(item.getBody_url(), holder.mLinkAvatar, true);
            holder.mLinkAvatar.setImageUrl(item.getBody_url(),mImageLoader);
            break;
        case STATUS:
            holder.mLayoutPhoto.setVisibility(View.GONE);
            holder.mLayoutHeader.setVisibility(View.GONE);
            holder.mLayoutMedia.setVisibility(View.GONE);
            holder.mLayoutLink.setVisibility(View.GONE);

            holder.mTextMessage.setVisibility(View.VISIBLE);
            holder.mTextContent.setVisibility(View.GONE);
            break;
        case PHOTO:
            holder.mLayoutPhoto.setVisibility(View.VISIBLE);
            holder.mLayoutHeader.setVisibility(View.GONE);
            holder.mLayoutMedia.setVisibility(View.GONE);
            holder.mLayoutLink.setVisibility(View.GONE);
            holder.mTextMessage.setVisibility(View.GONE);
            holder.mTextContent.setVisibility(View.VISIBLE);

            //mImageLoader.displayImage(item.getBody_url(), holder.mImagePhoto, true);
            holder.mImagePhoto.setImageUrl(item.getBody_url(), mImageLoader);

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(android.widget.RelativeLayout.LayoutParams.MATCH_PARENT, android.widget.RelativeLayout.LayoutParams.MATCH_PARENT);
            holder.mImagePhoto.setLayoutParams(params);
            holder.mImagePhoto.setScaleType(ImageView.ScaleType.FIT_CENTER);
            break;
        case VIDEO:
            holder.mLayoutPhoto.setVisibility(View.GONE);
            holder.mLayoutHeader.setVisibility(View.VISIBLE);
            holder.mLayoutMedia.setVisibility(View.VISIBLE);
            holder.mLayoutLink.setVisibility(View.GONE);
            holder.mTextContent.setVisibility(View.VISIBLE);

            holder.mTextMessage.setVisibility(View.GONE);
            holder.mImageContent.setVisibility(View.VISIBLE);
            //            holder.mTextContent.setText(item.getMessage());
            //            holder.mMediaCaption.setVisibility(View.VISIBLE);
            //            holder.mMediaCaption.setText(item.getTitle());
            //            RelativeLayout.LayoutParams videoParams = new RelativeLayout.LayoutParams(809, 500);
            //            videoParams.addRule(RelativeLayout.BELOW, holder.mMediaCaption.getId());
            //            videoParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
            //            holder.mImageContent.setScaleType(ImageView.ScaleType.FIT_XY);
            //            holder.mImageContent.setLayoutParams(videoParams);
            //            holder.mLinkAvatar.setVisibility(View.GONE);
            //mImageLoader.displayImage(item.getBody_url(), holder.mImageContent, false);
            holder.mImageContent.setImageUrl(item.getBody_url(), mImageLoader);
            break;

        default:
            break;
        }

        /*if (item.getLink_url() == null || item.getLink_url() == "")
            holder.mLinkAvatar.setVisibility(View.GONE);
        else
            mImageLoader.displayImage(item.getLink_url(), holder.mLinkAvatar, true);*/

        // link header
        if (item.getBody() != null && item.getBody().contains("likes")) {
            holder.mLinkHeader.setText(item.getBody().subSequence(item.getBody().indexOf("likes") + 6, item.getBody().length() - 1));
        } else {
            FacebookFeedWrapper.TypeTimeline timeLine = TypeTimeline.valueOf(item.getFeeds_type_detail());
            String title;
            String display = item.getTitle();
            String[] stringArray = display.split(GlobalConstant.SUB_STRING_CODE);

            if (stringArray.length > 1) {
                if ((timeLine == TypeTimeline.LINK || timeLine == TypeTimeline.STATUS || timeLine == TypeTimeline.VIDEO)) {
                    title = stringArray[0];
                } else {
                    title = stringArray[1];
                }
            } else if (stringArray.length > 0) {
                title = stringArray[0];
            } else {
                title = "";
            }

            holder.mLinkHeader.setText(title);
        }
        // display avatar
        holder.mUserAvatar.setProfileId(item.getAuthor_image());

        // display user from
        holder.mTextUserNameFrom.setText(item.getAuthor_name());

        // display posted date
        holder.mPostedDate.setText(Utils.convertToDateTime(item.getTime_stamp()));
        holder.mLikeCount.setText(Integer.toString(item.getNumber_of_like()));
        holder.mCommentCount.setText(Integer.toString(item.getNumber_of_comment()));
        Log.d("mtuan", "mtuan.minh (item.isLikedFeed = " + item.isLikedFeed());
        if (item.isLikedFeed() == 2) {
            holder.mLikeCount.setActivated(true);
            holder.mLikeCount.setEnabled(false);
        } else {
            holder.mLikeCount.setActivated(false);
            holder.mLikeCount.setEnabled(true);
        }
        holder.mLikeCount.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (holder.mLikeCount.isActivated()) {
                    holder.mLikeCount.setActivated(false);
                } else {
                    holder.mLikeCount.setSelected(true);
                    holder.mLikeCount.setActivated(true);
                    holder.mLikeCount.setText(Integer.parseInt(holder.mLikeCount.getText().toString()) + 1 + "");
                    mILikeCommentClick.onLikeCommentClick(FacebookConstant.FB_LIKE_BTN_CLICK, item, true);
                }
            }
        });
        holder.mCommentCount.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                mILikeCommentClick.onLikeCommentClick(FacebookConstant.FB_COMMENT_BTN_CLICK, item, true);
            }
        });
        holder.mLayoutMedia.setFocusable(false);

        // link layout
        //holder.mLayoutLink = convertView.findViewById(R.id.layout_link);
        //holder.mLinkAvatar = (NetworkImageView) convertView.findViewById(R.id.link_avatar);
        //holder.mLinkText = (TextView) convertView.findViewById(R.id.link_text);
        //holder.mLinkHeader = (TextView) convertView.findViewById(R.id.link_header);
        convertView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mILikeCommentClick.onClickItem(FacebookConstant.FB_DATA_TYPE_FEED, position);
            }
        });
        return convertView;
    }

    @Override
    public int getCount() {
        return mTimelineList.size();
    }

    public interface ILikeCommentClick {
        public void onLikeCommentClick(String type, StoryItemUnit storyItem, boolean like);

        public void onClickItem(int typeFeed, int position);
    }

    public void setListener(ILikeCommentClick listener) {
        mILikeCommentClick = listener;
    }

    class ViewHolder {
        View mLayoutLink, mPlayButton, mLayoutHeader, mLayoutPhoto, mLayoutMedia;
        TextView mTextUserNameFrom, mPostedDate, mTextContent, mLikeCount, mCommentCount, mLinkText, mLinkHeader, mTextMessage, mTextLinkUrl, mTextPhotoContent, mTextPhotoHeader, mTextVideoContent;
        NetworkImageView mLinkAvatar, mImagePhoto, mImageContent;
        ProfilePictureView mUserAvatar;
    }

    public void updateList(ArrayList<StoryItemUnit> sortItemList, boolean type_update) {
        // TODO Auto-generated method stub
        if (type_update) {
            mTimelineList.addAll(0, sortItemList);
        }
        else {
            mTimelineList.addAll(sortItemList);
        }
        notifyDataSetChanged();
    }

    public void clearStoryList() {
        this.mTimelineList.clear();
        notifyDataSetChanged();
    }
}