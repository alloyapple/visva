package com.samsung.android.alwayssocial.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.aphidmobile.utils.UI;
import com.samsung.android.alwayssocial.R;
import com.samsung.android.alwayssocial.constant.TwitterConstant;
import com.samsung.android.alwayssocial.service.StoryItemUnit;
import com.samsung.android.alwayssocial.util.Utils;
import com.samsung.android.alwayssocial.util.VolleySingleton;

public class TwitterTimeLineFlipAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<StoryItemUnit> mListStatuses;
    private LayoutInflater mInflater;
    private ImageLoader mImageLoader;

    private boolean[] mIsFavourite;
    private float mScreenWidth;
    private float mScreenHeight;

    private IOnClickCallback mIOnClickCallBack;

    @SuppressWarnings("deprecation")
    public TwitterTimeLineFlipAdapter(Context context, List<StoryItemUnit> listStatuses) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mListStatuses = (ArrayList<StoryItemUnit>) listStatuses;
        //mImageLoader = new ImageLoader(mContext);
        mImageLoader = VolleySingleton.getInstance(context).getImageLoader();
        mIsFavourite = new boolean[25];
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        mScreenWidth = display.getWidth();
        mScreenHeight = display.getHeight();
        Log.e("adfdf", "adf " + mScreenWidth);
    }

    public void updateTimelineList(ArrayList<StoryItemUnit> list) {
        mIsFavourite = new boolean[list.size()];
        this.mListStatuses = list;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isLikedFeed() == 1)
                mIsFavourite[i] = true;
            else
                mIsFavourite[i] = false;
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mListStatuses.size();
    }

    @Override
    public Object getItem(int position) {
        return mListStatuses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        final StoryItemUnit storyItem = mListStatuses.get(position);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.twitter_flip_timeline_item, null);
            holder.mLayoutMedia = UI.<RelativeLayout> findViewById(convertView, R.id.layout_media);
            holder.mImageMedia = UI.<NetworkImageView> findViewById(convertView, R.id.media_summary);
            holder.mPlayButton = UI.<ImageView> findViewById(convertView, R.id.video_play_button);
//            holder.mLayoutTweetInfo = UI.<LinearLayout> findViewById(convertView, R.id.layout_tweet_content);
            holder.mHeader = UI.<TextView> findViewById(convertView, R.id.tweet_header);
            holder.mUrl = UI.<TextView> findViewById(convertView, R.id.link_text);
            holder.mTextSummary = UI.<TextView> findViewById(convertView, R.id.tweet_summary);

            //user & post info
            holder.mLinkAvatar = UI.<NetworkImageView> findViewById(convertView, R.id.user_avatar);
            holder.mUserName = UI.<TextView> findViewById(convertView, R.id.username);
            holder.mCreatedTime = UI.<TextView> findViewById(convertView, R.id.posted_days);
            holder.mRetweetBy = UI.<TextView> findViewById(convertView, R.id.text_content);
            holder.mFavouriteCount = UI.<TextView> findViewById(convertView, R.id.txt_favorite_count);
            holder.mReply = UI.<TextView> findViewById(convertView, R.id.txt_reply);
            holder.mRetweetCount = UI.<TextView> findViewById(convertView, R.id.text_retweet);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //we have two types of layout, one has image, an other doesn't. Check with getMediaEntities. However, in some cases, Flipboard can diplay but we cant, maybe we need to parse 1 more url link
        switch (getDisplayType(storyItem)) {
        case TwitterConstant.TW_DISPLAY_TYPE_NO_MEDIA_ENTITIES:
            Log.d("mtuan.minh", " gone layout media");
            holder.mLayoutMedia.setVisibility(View.GONE);
            holder.mHeader.setTextSize(30);
            holder.mTextSummary.setTextSize(23);
            holder.mHeader.setSingleLine(true);
            break;
        case TwitterConstant.TW_DISPLAY_TYPE_MEDIA_ENTITIES:
            holder.mPlayButton.setVisibility(View.GONE);
            holder.mLayoutMedia.setVisibility(View.VISIBLE);
            holder.mHeader.setTextSize(25);
            holder.mTextSummary.setTextSize(18);
            holder.mHeader.setSingleLine(true);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(android.widget.RelativeLayout.LayoutParams.MATCH_PARENT, android.widget.RelativeLayout.LayoutParams.MATCH_PARENT);
            params.width = storyItem.getImage_width();
            params.height = storyItem.getImage_height();
            holder.mImageMedia.setScaleType(ScaleType.FIT_XY);
            holder.mImageMedia.setLayoutParams(params);
            float deltaX = mScreenWidth / params.width;
            Log.e("Swidth " + mScreenWidth + " SHeight " + mScreenHeight, "params.w " + params.width + " params.h " + params.height);
            params.width = (int) (params.width * deltaX);
            params.height = (int) (params.height * deltaX);
            //            holder.mLayoutMedia.setGravity(Gravity.CENTER);
            //mImageLoader.displayImage(storyItem.getBody_url(), holder.mImageMedia, false);
            holder.mImageMedia.setImageUrl(storyItem.getBody_url(), mImageLoader);
            break;
        default:
            break;
        }
        if (!"".equals(storyItem.getTitle())) {
            holder.mHeader.setText(storyItem.getTitle());
        } else {
            //            holder.mHeader.setText(status.getText());
            holder.mHeader.setVisibility(View.GONE);
        }
        holder.mTextSummary.setText(storyItem.getBody());
        holder.mUrl.setText(storyItem.getLink_url());
        //mImageLoader.displayImage(storyItem.getAuthor_image(), holder.mLinkAvatar, true);
        holder.mLinkAvatar.setImageUrl(storyItem.getAuthor_image(), mImageLoader);
        holder.mUserName.setText(storyItem.getAuthor_name());
        //        holder.mRetweetBy.setText(status.getUser().getName());
        holder.mCreatedTime.setText(Utils.convertToDateTimeTwitter(storyItem.getTime_stamp()));
        final int index = position;
        //        mIsFavourite[index] = status.isFavorited();

        holder.mFavouriteCount.setActivated(mIsFavourite[index]);

        holder.mFavouriteCount.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (!Utils.isNetworkAvailable(mContext)) {
                    Toast.makeText(mContext, R.string.no_networks_found, Toast.LENGTH_SHORT).show();
                } else {
                    int count = Integer.parseInt(holder.mFavouriteCount.getText().toString());
                    if (!mIsFavourite[index]) {
                        holder.mFavouriteCount.setActivated(true);
                        mIsFavourite[index] = true;
                        holder.mFavouriteCount.setText(count + 1 + "");
                        mIOnClickCallBack.onClick(TwitterConstant.TW_POST_CREATE_FAVOURITE, storyItem.getSocialFeedId(), storyItem);
                    } else {
                        holder.mFavouriteCount.setActivated(false);
                        holder.mFavouriteCount.setText(count - 1 + "");
                        mIsFavourite[index] = false;
                        mIOnClickCallBack.onClick(TwitterConstant.TW_POST_DESTROY_FAVOURITE, storyItem.getSocialFeedId(), storyItem);
                    }
                }
            }
        });

        holder.mRetweetCount.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!Utils.isNetworkAvailable(mContext)) {
                    Toast.makeText(mContext, R.string.no_networks_found, Toast.LENGTH_SHORT).show();
                } else {
                    Builder dialogBuilder = new Builder(mContext);
                    String[] functions = { "Retweet"/*, "Retweet With Comment"*/, "Cancel" };
                    dialogBuilder.setItems(functions, new OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            switch (which) {
                            case 0:
                                mIOnClickCallBack.onClick(TwitterConstant.TW_POST_RETWEET, storyItem.getSocialFeedId(), storyItem);
                                break;
                            case 1:
                                break;
                            }

                        }
                    });

                    dialogBuilder.create();
                    dialogBuilder.show();

                }
            }
        });

        holder.mRetweetCount.setText(storyItem.getNumber_of_comment() + "");
        Log.d("mtuan.minh", "getnumber of favourite for message " + storyItem.getBody() + " number of favourite = " + storyItem.getNumber_of_like());
        // holder.mFavouriteCount.setActivated(mIsFavourite[index]);
        if (mIsFavourite[index] && storyItem.isLikedFeed() == 1) {
            holder.mFavouriteCount.setText(storyItem.getNumber_of_like() > 0 ? (storyItem.getNumber_of_like() + "") : "");
        } else if (mIsFavourite[index] && storyItem.isLikedFeed() == 0) {
            holder.mFavouriteCount.setText(storyItem.getNumber_of_like() + 1 + "");
        } else if (!mIsFavourite[index] && storyItem.isLikedFeed() == 1) {
            holder.mFavouriteCount.setText(((storyItem.getNumber_of_like() - 1) > 0) ? ((storyItem.getNumber_of_like() - 1) + "") : "");
        } else if (!mIsFavourite[index] && storyItem.isLikedFeed() == 0) {
            holder.mFavouriteCount.setText(storyItem.getNumber_of_like() > 0 ? (storyItem.getNumber_of_like() + "") : "");
        }

        holder.mReply.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mIOnClickCallBack.onClick(TwitterConstant.TW_POST_REPLY, storyItem.getSocialFeedId(), storyItem);

            }
        });
        return convertView;
    }

    private int getDisplayType(StoryItemUnit storyItem) {
        if ("".equals(storyItem.getBody_url())) {
            return TwitterConstant.TW_DISPLAY_TYPE_NO_MEDIA_ENTITIES;
        } else {
            return TwitterConstant.TW_DISPLAY_TYPE_MEDIA_ENTITIES;
        }
    }

    public interface IOnClickCallback {
        /*callback used when user clicks on favourite, retweet, reply
         * type is favourite,unfavourite retweet
         * */
        public void onClick(int type, String id, StoryItemUnit item);

        public void onItemClickListener(int position);
    }

    public void setListener(IOnClickCallback callback) {
        this.mIOnClickCallBack = callback;
    }

    class ViewHolder {
        View mLayoutLink, mPlayButton;
        NetworkImageView mImageMedia, mLinkAvatar;
        TextView mHeader, mUrl, mTextSummary, mUserName, mCreatedTime, mFavouriteCount, mReply, mRetweetCount, mRetweetBy;
        RelativeLayout mLayoutMedia;
    }

    public ArrayList<StoryItemUnit> getAdapter() {
        return mListStatuses;
    }

}
