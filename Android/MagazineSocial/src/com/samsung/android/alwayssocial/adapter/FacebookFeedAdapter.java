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
import com.facebook.widget.ProfilePictureView;
import com.samsung.android.alwayssocial.R;
import com.samsung.android.alwayssocial.activity.ILikeCommentClick;
import com.samsung.android.alwayssocial.constant.FacebookConstant;
import com.samsung.android.alwayssocial.constant.GlobalConstant;
import com.samsung.android.alwayssocial.service.StoryItemUnit;
import com.samsung.android.alwayssocial.object.facebook.FacebookFeedWrapper;
import com.samsung.android.alwayssocial.object.facebook.FacebookFeedWrapper.TypeTimeline;
import com.samsung.android.alwayssocial.util.Utils;
import com.samsung.android.alwayssocial.util.VolleySingleton;
import com.samsung.android.alwayssocial.view.DynamicImageView;
import com.samsung.android.alwayssocial.view.ExpandableTextView;

public class FacebookFeedAdapter extends ArrayAdapter<StoryItemUnit> {

    ArrayList<StoryItemUnit> mTimelineList;
    LayoutInflater mInflater;
    ImageLoader mImageLoader;

    ILikeCommentClick mILikeCommentClick;

    public FacebookFeedAdapter(Context context, List<StoryItemUnit> objects) {
        super(context, -1, objects);
        mInflater = LayoutInflater.from(context);
        mTimelineList = (ArrayList<StoryItemUnit>) objects;
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
            convertView = mInflater.inflate(R.layout.timeline_list_item, null);

            holder.mLayoutMedia = (RelativeLayout) convertView.findViewById(R.id.layout_media);

            holder.mTextUserNameFrom = (TextView) convertView.findViewById(R.id.username);
            holder.mPostedDate = (TextView) convertView.findViewById(R.id.posted_days);
            holder.mTextContent = (ExpandableTextView) convertView.findViewById(R.id.text_content);
            holder.mLikeCount = (TextView) convertView.findViewById(R.id.text_like);
            holder.mCommentCount = (TextView) convertView.findViewById(R.id.text_comment);
            holder.mImageContent = (DynamicImageView) convertView.findViewById(R.id.media_summary);
            holder.mPlayButton = convertView.findViewById(R.id.video_play_button);
            holder.mMediaCaption = (TextView) convertView.findViewById(R.id.media_capion);
            holder.mUserAvatar = (ProfilePictureView) convertView.findViewById(R.id.user_avatar);

            // link layout
            holder.mLayoutLink = convertView.findViewById(R.id.layout_link);
            holder.mLinkAvatar = (DynamicImageView) convertView.findViewById(R.id.link_avatar);
            holder.mLinkText = (ExpandableTextView) convertView.findViewById(R.id.link_text);
            holder.mLinkHeader = (ExpandableTextView) convertView.findViewById(R.id.link_header);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final StoryItemUnit item = mTimelineList.get(position);

        holder.mLinkHeader.setText("Link");
        holder.mMediaCaption.setVisibility(View.GONE);
        // display content
        FacebookFeedWrapper.TypeTimeline timelineType = FacebookFeedWrapper.TypeTimeline.valueOf(item.getFeeds_type_detail());
        switch (timelineType) {
        case LINK:
            holder.mLayoutLink.setVisibility(View.VISIBLE);
            holder.mTextContent.setVisibility(View.GONE);
            holder.mImageContent.setVisibility(View.GONE);
            holder.mPlayButton.setVisibility(View.GONE);

            holder.mTextContent.setText(item.getBody());
            holder.mLinkText.setText(item.getBody());
            holder.mImageContent.setVisibility(View.GONE);
            holder.mLinkAvatar.setVisibility(View.VISIBLE);
            //mImageLoader.displayImage(item.getBody_url(), holder.mLinkAvatar, true);
            holder.mLinkAvatar.setImageUrl(item.getBody_url(),mImageLoader);
            break;
        case STATUS:
            holder.mPlayButton.setVisibility(View.GONE);
            holder.mLayoutLink.setVisibility(View.GONE);
            holder.mTextContent.setVisibility(View.VISIBLE);
            holder.mTextContent.setText(item.getBody());
            holder.mImageContent.setVisibility(View.GONE);
            holder.mLinkAvatar.setVisibility(View.GONE);
            break;
        case PHOTO:
            holder.mLayoutLink.setVisibility(View.GONE);
            holder.mImageContent.setVisibility(View.VISIBLE);
            holder.mPlayButton.setVisibility(View.GONE);
            
            holder.mTextContent.setVisibility(View.VISIBLE);
            holder.mTextContent.setText(item.getBody());
            
            //mImageLoader.displayImage(item.getBody_url(), holder.mImageContent, false);
            holder.mImageContent.setImageUrl(item.getBody_url(), mImageLoader);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(android.widget.RelativeLayout.LayoutParams.MATCH_PARENT, android.widget.RelativeLayout.LayoutParams.MATCH_PARENT);
            holder.mImageContent.setLayoutParams(params);
            holder.mImageContent.setScaleType(ImageView.ScaleType.FIT_CENTER);
            holder.mLinkAvatar.setVisibility(View.GONE);
            //            holder.mImageContent.setBackground(getContext().getResources().getDrawable(R.drawable.ui_flyout_body_bg));
            break;
        case VIDEO:
            holder.mPlayButton.setVisibility(View.VISIBLE);
            holder.mLayoutLink.setVisibility(View.GONE);
            holder.mImageContent.setVisibility(View.VISIBLE);
            holder.mTextContent.setText(item.getMessage());
            holder.mTextContent.setText(item.getMessage());
            holder.mTextContent.setText(item.getBody());
            holder.mMediaCaption.setVisibility(View.VISIBLE);
            holder.mMediaCaption.setText(item.getTitle());
            RelativeLayout.LayoutParams videoParams = new RelativeLayout.LayoutParams(809, 500);
            videoParams.addRule(RelativeLayout.BELOW, holder.mMediaCaption.getId());
            videoParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
            holder.mImageContent.setScaleType(ImageView.ScaleType.FIT_XY);
            holder.mImageContent.setLayoutParams(videoParams);
            holder.mLinkAvatar.setVisibility(View.GONE);
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
        String storyItemBody = item.getBody();
        String[] itemBody = storyItemBody.split(GlobalConstant.SUB_STRING_CODE);
        if(itemBody.length >= 3) {
        	if(itemBody[2] != null && !itemBody[2].equals("")) {
        		holder.mLinkHeader.setText(itemBody[2]);
        		holder.mTextContent.setText(itemBody[2]);
        		holder.mMediaCaption.setText(itemBody[2]);
        	} else {
        		holder.mLinkHeader.setText("");
        		holder.mTextContent.setText("");
        		holder.mMediaCaption.setText("");
        	}
        	if(itemBody[0] != null && !itemBody[0].equals("")) {
        		holder.mLinkText.setText(itemBody[0]);
        	} else {
        		holder.mLinkText.setText("");
        	}
        }
        else {
        	holder.mLinkHeader.setText("");
        	holder.mTextContent.setText("");
        	holder.mLinkText.setText("");
        	holder.mMediaCaption.setText("");
        }
        
        /*String storyItemTitle = item.getTitle();
        String[] itemTitle = storyItemTitle.split(GlobalConstant.SUB_STRING_CODE);
        if(itemTitle.length >= 2) {
        	if(itemTitle[1] != null && !itemTitle[1].equals("")) {
        		holder.mMediaCaption.setText(itemTitle[1]);
        	}
        	else {
        		holder.mMediaCaption.setText("");
        	}
        }
        else {
        	holder.mMediaCaption.setText("");
        }*/
        
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
                    //holder.mLikeCount.setActivated(false);
                } else {
                    holder.mLikeCount.setSelected(true);
                    holder.mLikeCount.setActivated(true);
                    holder.mLikeCount.setText(Integer.parseInt(holder.mLikeCount.getText().toString()) + 1 + "");
                    mILikeCommentClick.onLikeCommentClick(FacebookConstant.FB_LIKE_BTN_CLICK, item, true);
                    //fake item's like status
                    item.setIsLiked(2);
                    item.setNumber_of_like(1);
                }
            }
        });
        holder.mCommentCount.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                mILikeCommentClick.onLikeCommentClick(FacebookConstant.FB_COMMENT_BTN_CLICK, item, true);
            }
        });

        return convertView;
    }

    @Override
    public int getCount() {
        return mTimelineList.size();
    }

    class ViewHolder {
        RelativeLayout mLayoutMedia;
        View mLayoutLink, mPlayButton;
        ExpandableTextView mTextContent, mLinkText, mLinkHeader;
        TextView mTextUserNameFrom, mPostedDate, mLikeCount, mCommentCount, mMediaCaption;
        DynamicImageView mImageContent, mLinkAvatar;
        ProfilePictureView mUserAvatar;
    }

    public void updateList(ArrayList<StoryItemUnit> sortItemList, boolean type_update) {
        // TODO Auto-generated method stub
        if (type_update)
            mTimelineList.addAll(0, sortItemList);
        else
            mTimelineList.addAll(sortItemList);
        notifyDataSetChanged();
    }
    
    public void setListener(ILikeCommentClick listener) {
        mILikeCommentClick = listener;
    }

}
