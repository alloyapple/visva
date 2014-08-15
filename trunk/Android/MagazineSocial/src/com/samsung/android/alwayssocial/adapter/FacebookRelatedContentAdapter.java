package com.samsung.android.alwayssocial.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.widget.ProfilePictureView;
import com.samsung.android.alwayssocial.R;
import com.samsung.android.alwayssocial.constant.GlobalConstant;
import com.samsung.android.alwayssocial.object.facebook.FacebookRelatedCommentItem;
import com.samsung.android.alwayssocial.util.Utils;

public class FacebookRelatedContentAdapter extends BaseAdapter {
	public static final String TAG = "RelatedCOntentsAdapter";

	public Context mContext = null;
	public ArrayList<FacebookRelatedCommentItem> mList = null;
	public LayoutInflater mLayoutInflater = null;
	public Intent mBrowserIntent = null;


	public FacebookRelatedContentAdapter(Context context, ArrayList<FacebookRelatedCommentItem> list) {
		mContext = context;
		mList = list;
		mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void setRelatedItemList(ArrayList<FacebookRelatedCommentItem> list) {
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
		final FacebookRelatedCommentItem item = mList.get(position);

		if (convertView == null) {
			convertView = mLayoutInflater.inflate(R.layout.related_contents_template, parent, false);

			viewHolder = new ViewHolder();
			viewHolder.mProPictures = (ProfilePictureView) convertView.findViewById(R.id.user_avatar);
			viewHolder.mAuthorOfComment = (TextView) convertView.findViewById(R.id.contents_author);
			viewHolder.mTimeOfComment = (TextView) convertView.findViewById(R.id.contents_pubtime);
			viewHolder.mComment = (TextView) convertView.findViewById(R.id.contents_comments);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		String imageUrl = item.getImageUrl();
		String author = item.getAuthorOfComment();
		String comment = item.getContentOfComment();
		String pubDate;
		if(Utils.convertToDateTime(item.getPubTime()).equals(GlobalConstant.SUB_STRING_CODE)) {
			pubDate = item.getPubTime();
		}
		else {
			pubDate = Utils.convertToDateTime(item.getPubTime());
		}
		viewHolder.mProPictures.setProfileId(imageUrl);
		// Handling contents title.
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
			viewHolder.mTimeOfComment.setText(pubDate);
		} else {// Invalid time.
			viewHolder.mTimeOfComment.setText(" ");
		}

		return convertView;
	}

	/**
	 * Class for Related contents slot.
	 */
	private static class ViewHolder {
		public ProfilePictureView mProPictures;
		public TextView mAuthorOfComment;
		public TextView mComment;
		public TextView mTimeOfComment;

	}
}