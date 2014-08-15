package com.samsung.android.alwayssocial.adapter;

import java.util.ArrayList;
import java.util.List;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.samsung.android.alwayssocial.R;
import com.samsung.android.alwayssocial.object.twitter.TwitterRelatedCommentItem;
import com.samsung.android.alwayssocial.util.Utils;
import com.samsung.android.alwayssocial.util.VolleySingleton;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class TwitterRelatedContentAdapter extends BaseAdapter {
    
    public static final String TAG = "RelatedCOntentsAdapter";

    public Context mContext = null;
    public List<TwitterRelatedCommentItem> mList = null;
    public LayoutInflater mLayoutInflater = null;
    public Intent mBrowserIntent = null;

    public ImageLoader mImageLoader = null;
    
    public TwitterRelatedContentAdapter(Context context, List<TwitterRelatedCommentItem> list) {
        mContext = context;
        mList = list;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //mImageLoader = new ImageLoader(context);
        mImageLoader = VolleySingleton.getInstance(context).getImageLoader();
    }
    
    public void setRelatedItemList(ArrayList<TwitterRelatedCommentItem> list) {
        mList = list;
    }
    
    @Override
    public int getCount() {
        if (mList == null)
            return 0;
        else
            return mList.size();
    }

    @Override
    public Object getItem(int position) {
        if (getCount() > position)
            return mList.get(position);
        else
            return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        final TwitterRelatedCommentItem item = mList.get(position);

        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.tw_related_contents_template, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.mProPictures = (NetworkImageView) convertView.findViewById(R.id.user_avatar);
            viewHolder.mAuthorOfComment = (TextView) convertView.findViewById(R.id.contents_author);
            viewHolder.mTimeOfComment = (TextView) convertView.findViewById(R.id.contents_pubtime);
            viewHolder.mComment = (TextView) convertView.findViewById(R.id.contents_comments);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //String imageUrl = item.getImageUrl();
        String author = item.getAuthorOfComment();
        String comment = item.getContentOfComment();
        String pubDate = item.getPubTime();
        
        if (author != null && !author.equals("")) {// Valid title.
            viewHolder.mAuthorOfComment.setText(author);

        } else {// Invalid title.
            viewHolder.mAuthorOfComment.setText(" ");
        }
        
        if (comment != null && !comment.equals("")) {// Valid title.
            viewHolder.mComment.setText(comment);

        } else {// Invalid title.
            viewHolder.mComment.setText(" ");
        }
        // Handling contents published time.
        if (!pubDate.equals("")) {// Valid time.
            viewHolder.mTimeOfComment.setText(Utils.convertToDateTimeTwitter(pubDate));
        } else {// Invalid time.
            viewHolder.mTimeOfComment.setText(" ");
        }
        //mImageLoader.displayImage(item.getImageUrl(), viewHolder.mProPictures, true);
        viewHolder.mProPictures.setImageUrl(item.getImageUrl(), mImageLoader);
        return convertView;
    }

    /**
     * Class for Related contents slot.
     */
    private static class ViewHolder {
        public NetworkImageView mProPictures;
        public TextView mAuthorOfComment;
        public TextView mComment;
        public TextView mTimeOfComment;

    }

}
