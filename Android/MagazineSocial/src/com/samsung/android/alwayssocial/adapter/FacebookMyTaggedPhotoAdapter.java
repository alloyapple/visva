package com.samsung.android.alwayssocial.adapter;

import java.util.ArrayList;
import java.util.List;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.facebook.widget.ProfilePictureView;
import com.samsung.android.alwayssocial.R;
import com.samsung.android.alwayssocial.activity.ILikeCommentClick;
import com.samsung.android.alwayssocial.constant.FacebookConstant;
import com.samsung.android.alwayssocial.object.facebook.FacebookFeedWrapper;
import com.samsung.android.alwayssocial.service.StoryItemUnit;
import com.samsung.android.alwayssocial.util.Utils;
import com.samsung.android.alwayssocial.util.VolleySingleton;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class FacebookMyTaggedPhotoAdapter extends ArrayAdapter<StoryItemUnit> {
    ArrayList<StoryItemUnit> mTimelineList;
    LayoutInflater mInflater;
    ImageLoader mImageLoader;
    
    ILikeCommentClick mILikeCommentClick;

    public FacebookMyTaggedPhotoAdapter(Context context, List<StoryItemUnit> objects) {
        super(context, -1, objects);
        mInflater = LayoutInflater.from(context);
        mTimelineList = (ArrayList<StoryItemUnit>) objects;
        //this.mImageLoader = new ImageLoader(context);
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
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.timeline_list_item, null);
            holder.mTextUserNameFrom = (TextView) convertView.findViewById(R.id.username);
            holder.mPostedDate = (TextView) convertView.findViewById(R.id.posted_days);
            holder.mTextContent = (TextView) convertView.findViewById(R.id.text_content);
            holder.mLikeCount = (TextView) convertView.findViewById(R.id.text_like);
            holder.mCommentCount = (TextView) convertView.findViewById(R.id.text_comment);
            holder.mImageContent = (NetworkImageView) convertView.findViewById(R.id.media_summary);
            holder.mPlayButton = convertView.findViewById(R.id.video_play_button);
            holder.mMediaCaption = (TextView) convertView.findViewById(R.id.media_capion);
            holder.mUserAvatar = (ProfilePictureView) convertView.findViewById(R.id.user_avatar);

            // link layout
            holder.mLayoutLink = convertView.findViewById(R.id.layout_link);
            holder.mLinkAvatar = (NetworkImageView) convertView.findViewById(R.id.link_avatar);
            holder.mLinkText = (TextView) convertView.findViewById(R.id.link_text);
            holder.mLinkHeader = (TextView) convertView.findViewById(R.id.link_header);
            
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final StoryItemUnit item = mTimelineList.get(position);

        holder.mLinkHeader.setText("Link");
        holder.mMediaCaption.setVisibility(View.GONE);
        // display content
        FacebookFeedWrapper.TypeTimeline timelineType = null;
        try {
            timelineType = FacebookFeedWrapper.TypeTimeline.valueOf(item.getFeeds_type_detail());
        } catch (Exception e) {

        }
        switch (timelineType) {
        case LINK:
            holder.mLayoutLink.setVisibility(View.VISIBLE);
            holder.mTextContent.setVisibility(View.VISIBLE);
            holder.mImageContent.setVisibility(View.GONE);
            holder.mPlayButton.setVisibility(View.GONE);

            holder.mTextContent.setText(item.getBody());
            holder.mLinkText.setText(item.getBody());
            holder.mImageContent.setVisibility(View.GONE);
            //mImageLoader.displayImage(item.getLink_url(), holder.mLinkAvatar, true);
            holder.mLinkAvatar.setImageUrl(item.getLink_url(), mImageLoader);
            if (item.getMessage() != null && item.getMessage().contains("likes")) {
                holder.mLinkHeader.setText(item.getMessage().subSequence(item.getMessage().indexOf("likes") + 6, item.getMessage().length() - 1));
            }
            break;
        case STATUS:
            holder.mPlayButton.setVisibility(View.GONE);
            holder.mLayoutLink.setVisibility(View.GONE);
            holder.mTextContent.setVisibility(View.VISIBLE);
            holder.mTextContent.setText(item.getBody());
            holder.mImageContent.setVisibility(View.GONE);
            break;
        case PHOTO:
            holder.mLayoutLink.setVisibility(View.GONE);
            holder.mImageContent.setVisibility(View.VISIBLE);
            holder.mPlayButton.setVisibility(View.GONE);
            holder.mTextContent.setText(item.getBody());
            //mImageLoader.displayImage(item.getBody_url(), holder.mImageContent, true);
            holder.mImageContent.setImageUrl(item.getBody_url(), mImageLoader);
            break;
        case VIDEO:
            holder.mPlayButton.setVisibility(View.VISIBLE);
            holder.mLayoutLink.setVisibility(View.GONE);
            holder.mImageContent.setVisibility(View.VISIBLE);
            holder.mTextContent.setText(item.getBody());
            holder.mMediaCaption.setVisibility(View.VISIBLE);
            holder.mMediaCaption.setText(item.getTitle());
            //mImageLoader.displayImage(item.getBody_url(), holder.mImageContent, true);
            holder.mImageContent.setImageUrl(item.getBody_url(), mImageLoader);
            break;

        default:
            break;
        }

        // display avatar
        holder.mUserAvatar.setProfileId(item.getAuthor_image());

        // display user from
        holder.mTextUserNameFrom.setText(item.getAuthor_name());

        // display posted date
        Log.e("FacebookMyTaggedPhotoAdapter", "item.getTime_stamp() "+item.getTime_stamp());
        holder.mPostedDate.setText(Utils.convertToDateTime(item.getTime_stamp()));

        // like count & comment count
//        if (item.getNumber_of_like() == 0) {
//            holder.mLikeCount.setText("");
//        } else {
//            holder.mLikeCount.setText("" + item.getNumber_of_like());
//        }
//        if (item.getNumber_of_comment() == 0) {
//            holder.mCommentCount.setText("");
//        } else {
//            holder.mCommentCount.setText("" + item.getNumber_of_comment());
//        }
        
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

        return convertView;
    }

    @Override
    public int getCount() {
        return mTimelineList.size();
    }

    class ViewHolder {
        View mLayoutLink, mPlayButton;
        TextView mTextUserNameFrom, mPostedDate, mTextContent, mLikeCount, mCommentCount, mLinkText, mLinkHeader, mMediaCaption;
        NetworkImageView mImageContent, mLinkAvatar;
        ProfilePictureView mUserAvatar;

    }
    
    public void setListener(ILikeCommentClick listener) {
        mILikeCommentClick = listener;
    }
    
}
